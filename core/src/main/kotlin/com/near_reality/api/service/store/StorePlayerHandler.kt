package com.near_reality.api.service.store

import com.near_reality.api.APIClient
import com.near_reality.api.GameDatabase
import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.model.CreditStoreCartItem
import com.near_reality.api.responses.StoreCheckoutResponse
import com.near_reality.api.service.user.UserAPIService
import com.near_reality.api.service.user.UserPlayerHandler
import com.near_reality.api.service.user.storeCredits
import com.near_reality.game.content.commands.DeveloperCommands
import com.near_reality.game.coroutine.stop
import com.near_reality.game.model.ui.credit_store.CreditPackage
import com.near_reality.game.model.ui.credit_store.coinbaseEnabled
import com.near_reality.game.world.entity.player.totalDonatedAfterLaunch
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.cores.CoresManager
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.logger.NearRealityLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import mgi.types.config.items.ItemDefinitions
import kotlin.time.ExperimentalTime

/**
 * Handles player related store actions.
 *
 * @author Stan van der Bend
 */
object StorePlayerHandler {

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val logger = NearRealityLogger.getLogger(StorePlayerHandler::class.java)

    lateinit var creditPurchasesSinceRelaunch: Map<Long, Int>

    internal fun tryClaim(order: CreditPackageOrder, onSuccess: () -> Unit = {}) {
        fun Player.tryClaim() {
            WorldTasksManager.schedule {
                if (storeClaimedOrders.add(order.id)) {
                    sendDeveloperMessage("Claimed order: ${order.id}")
                    this.totalDonatedAfterLaunch += Math.round(order.creditPackage.price)
                    user = order.user
                    onSuccess()
                } else
                    sendDeveloperMessage("Failed to claim order: ${order.id}")
            }
        }

        val user = order.user
        val onlinePlayer = World.getPlayer(user.name)
        if (onlinePlayer.isPresent) {
            onlinePlayer.get().tryClaim()
        } else {
            CoresManager.getLoginManager().load(true, order.user.name) { offlinePlayer ->
                if (offlinePlayer.isPresent)
                    offlinePlayer.get().tryClaim()
                else
                    logger.error("Failed to claim order: ${order.id} for user: ${order.user.name}, user not found.")
            }
        }
    }

    fun populateOrderData() {
        ioScope.launch {
            creditPurchasesSinceRelaunch = GameDatabase.getCreditPurchaseSinceRelaunch()
        }
    }

    @JvmStatic
    fun startNewOrder(
        player: Player,
        creditPackage: CreditPackage,
        paymentMethod: CreditPackageOrder.PaymentMethod,
    ): Channel<StoreOrderUpdate>? {
        when {
            !APIClient.enabled -> player.notify("You cannot purchase items on this world.", schedule = false)
            !StoreAPIService.enabled -> player.notify("The store is currently unavailable.", schedule = false)
            !player.inventory.hasFreeSlots() -> player.notify(
                "You cannot purchase a DPin without any free inventory slots.",
                schedule = false
            )

            World.isUpdating() -> player.notify(
                "You cannot purchase items while the world is updating.",
                schedule = false
            )

            else -> {
                if (paymentMethod == CreditPackageOrder.PaymentMethod.COINBASE && !coinbaseEnabled) {
                    player.notify("Coinbase payments are currently disabled.", schedule = false)
                    return null
                }

                player.lock()

                val user = player.user
                if (user != null) {
                    val updateChannel = StoreAPIService.submitOrder(user, creditPackage, paymentMethod)

                    ioScope.launch {
                        for (update in updateChannel) {
                            player.sendDeveloperMessage("Received update: $update")
                            WorldTasksManager.schedule {
                                player.lock(1)

                                when (update) {
                                    is StoreOrderUpdate.AwaitingPayment -> {
                                        player.packetDispatcher.sendURL(update.url)
                                        player.dialogue {
                                            if (paymentMethod == CreditPackageOrder.PaymentMethod.COINBASE) {
                                                plain(
                                                    "A checkout page should have opened where you can pay with Coinbase." +
                                                            "<br>Coinbase payments take a while to be verified on the blockchain." +
                                                            "<br>Verification can take up to 30 minutes depending on the network." +
                                                            "<br>You may close this dialogue while waiting."
                                                )
                                            } else
                                                loading("Awaiting payment completion...")
                                        }
                                    }

                                    StoreOrderUpdate.Canceled ->
                                        player.dialogue { plain("Order canceled.") }

                                    is StoreOrderUpdate.Claimed -> {
                                        player.tryAddInventoryThenBank(update.order.products.first)

                                        UserPlayerHandler.subtractCredits(player, -update.order.products.second) {
                                            player.dialogue {
                                                plain(Colour.RS_GREEN.wrap("Payment Received!") + "<br>You have received your bond and ${update.order.products.second} bonus credits.").executeAction {
                                                    DeveloperCommands.openStore(player)
                                                }
                                            }
                                        }
                                    }

                                    is StoreOrderUpdate.Failed ->
                                        player.dialogue { plain("Order failed: ${update.reason}") }

                                    StoreOrderUpdate.Submitted ->
                                        player.dialogue { plain("Order submitted.") }
                                }
                            }
                        }
                    }
                } else
                    player.notify("You must be logged in to create a store order.", schedule = false)
            }
        }
        return null
    }

    @OptIn(ExperimentalTime::class)
    fun checkout(player: Player, cart: List<CreditStoreCartItem>, onSuccess: () -> Unit) {
        val totalCost = cart.sumOf { it.cost }
        if (player.storeCredits < totalCost) {
            player.sendMessage("You do not have enough credits to purchase these items.")
            return
        }
        if (totalCost < 0) {
            player.dialogue { plain("An error occurred while checking out your items.") }
            return
        }
        if (player.gameMode == GameMode.ULTIMATE_IRON_MAN) {
            val items = cart.map { it.toGameItem() }.toTypedArray()
            if (!player.inventory.hasSpaceFor(*items)) {
                player.dialogue { plain("You do not have enough inventory space to purchase these items.") }
                return
            }
        }
        player.dialogue { loading("Checking out items in cart...") }
        val user = player.user
        if (user == null) {
            player.notify("You must be logged in to checkout a store cart.", schedule = false)
            return
        }
        StoreAPIService.checkoutCart(user, cart) { response ->
            when (response) {
                null -> player.notify("Failed to checkout store cart.", schedule = true)
                else -> {
                    WorldTasksManager.schedule {
                        player.sendDeveloperMessage("Received checkout response: ${response::class.simpleName}")
                        when (response) {
                            is StoreCheckoutResponse.AccountNotFound ->
                                player.notify("Checkout failed: Account not found.", schedule = false)

                            is StoreCheckoutResponse.InsufficientFunds ->
                                player.notify(
                                    "Checkout failed: Insufficient funds.<br> Missing: ${response.difference} credits.",
                                    schedule = false
                                )

                            is StoreCheckoutResponse.Success -> {
                                player.user = response.user
                                val purchasedItems = response.purchased.map { it.toGameItem() }
                                if (purchasedItems.isNotEmpty()) {
                                    val all =
                                        response.purchased.size == response.outOfStock.size + response.missing.size + response.insufficientFunds.size
                                    val checkoutMessage =
                                        "You have successfully checked out ${if (all) "all" else "some"} of your items."
                                    if (player.gameMode == GameMode.ULTIMATE_IRON_MAN) {
                                        purchasedItems.forEach { player.inventory.addOrDrop(it) }
                                        player.sendMessage("The items have been added to your inventory")
                                        player.dialogue { plain("$checkoutMessage<br>They have been added to your inventory!") }
                                    } else {
                                        purchasedItems.forEach { player.bank.add(it) }
                                        player.sendMessage("The items have been added to your bank!")
                                        player.dialogue { plain("$checkoutMessage<br>They have been added to your bank!") }
                                    }
                                    GameLogger.log {
                                        GameLogMessage.CreditStoreCheckout(
                                            username = player.dbUsername,
                                            cart = response.purchased
                                        )
                                    }
                                } else {
                                    player.dialogue { plain("You have no items to checkout.<br>They may have become unavailable during checkout.") }
                                }
                                logger.info("Checkout for $player -> ${response.purchased.size} items purchased, ${response.outOfStock.size} out of stock, ${response.missing.size} missing, ${response.insufficientFunds.size} insufficient funds.")
                                onSuccess()
                            }

                            is StoreCheckoutResponse.Failed ->
                                player.notify("Checkout failed: ${response.reason}", schedule = false)
                        }
                    }
                }
            }
        }
    }

    private fun CreditStoreCartItem.toGameItem() = Item(product.itemId, product.itemAmount * amount)
    private val CreditStoreCartItem.nameWithAmount
        get() = "${product.itemAmount * amount}x ${
            ItemDefinitions.nameOfLowercase(
                product.itemId
            )
        }"
}
