package org.jesse.game.model.item

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import kotlinx.datetime.Clock
import mgi.types.config.items.ItemDefinitions
import org.jesse.game.GameConstants
import org.jesse.game.GameInterface
import org.jesse.game.content.boss.corporealbeast.CorporealBeastDynamicArea
import org.jesse.game.content.consumables.Consumable
import org.jesse.game.content.follower.Follower
import org.jesse.game.content.follower.PetWrapper
import org.jesse.game.item.Item
import org.jesse.game.model.item.degradableitems.DegradableItem
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingVariables
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingsInterface
import org.jesse.game.util.Colour
import org.jesse.game.util.ItemUtil
import org.jesse.game.world.World
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.player.FakePlayer
import org.jesse.game.world.entity.player.LogLevel
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.game.world.region.area.plugins.DropPlugin
import org.jesse.game.world.region.area.plugins.IDropPlugin
import org.jesse.game.world.region.area.wilderness.WildernessArea
import org.jesse.logger.NearRealityLogger
import org.jesse.plugins.dialogue.DestroyItemDialogue
import org.jesse.plugins.dialogue.followers.PetFishDropD
import org.jesse.tools.logging.GameLogMessage
import org.jesse.tools.logging.GameLogger.log
import org.jesse.utils.StaticInitializer
import org.slf4j.Logger
import org.slf4j.event.Level

@StaticInitializer
object ItemActionHandler {
    private val log: Logger = NearRealityLogger.getLogger(ItemActionHandler::class.java)
        @JvmField
        val defaultAction: ItemPlugin

        init {
            defaultAction = object : ItemPlugin() {
                override fun handle() {
                }

                override fun getItems(): IntArray? {
                    return IntArray(0)
                }
            }
            defaultAction.setDefaultHandlers()
        }

        @JvmStatic fun handle(player: Player, itemId: Int, slotId: Int, option: Int) {
            val isDebugEnabled: Boolean = log.isDebugEnabled()
            if (option == 10) {
                if (isDebugEnabled) log.debug(
                    "[" + itemId + "] Item examine: " + ItemDefinitions.getOrThrow(itemId).getName() + "."
                )
                val item = player.getInventory().getItem(slotId)
                if (item == null || item.getId() != itemId) {
                    return
                }
                ItemUtil.sendItemExamine(player, item)
                return
            }
            if (player !is FakePlayer) {
                if (player.isLocked() || player.isFullMovementLocked() || !player.getInterfaceHandler()
                        .isPresent(GameInterface.INVENTORY_TAB)
                ) {
                    return
                }
            }
            player.stopAll(false, true, true)
            val item = player.getInventory().getItem(slotId)
            if (item == null || item.getId() != itemId) {
                return
            }
            val itemDef = item.getDefinitions()
            if (itemDef == null) {
                player.sendMessage("Nothing interesting happens.")
                log.debug("Item (id: {}, slot: {}) did not have a definition! (option: {})", itemId, slotId, option)
                return
            }
            val optionName = itemDef.getOption(option - 1)
            if (optionName == null) {
                log.debug("Item (id: {}, slot: {}) option not found for option: {}", itemId, slotId, option)
                return
            }
            val action = ItemPlugin.getPlugin(itemId)
            val handler = action.getHandler(optionName)
            if (handler != null) {
                val pluginClassName = action.javaClass.getSimpleName()
                if (isDebugEnabled) log.debug(
                    ("[" + (if (pluginClassName.isEmpty()) "Absent" else pluginClassName) + "] " + item.getName() + ": "
                            + item.getId() + " x " + item.getAmount() + ", Slot: " + slotId + ", Option: " + (optionName + " [" + option + "]"))
                )
                handler.handle(player, item, player.getInventory().getContainer(), slotId)
                return
            }
            player.sendMessage("Nothing interesting happens.")
            if (isDebugEnabled) log.debug(
                "Item option: " + item.getId() + " x " + item.getAmount() + ", " + item.getName() + ", " + slotId +
                        ", " + option
            )
        }

        @JvmField
        val intActions: Int2ObjectMap<ItemPlugin> = Int2ObjectOpenHashMap<ItemPlugin>()

        @JvmField
        val deathIntActions: Int2ObjectMap<ItemPlugin?> = Int2ObjectOpenHashMap<ItemPlugin?>()

        fun add(c: Class<*>) {
            try {
                val base = c.getDeclaredConstructor().newInstance() as ItemPlugin
                if (base.getItemDeathHandler() !== ItemPlugin.DEFAULT_ITEM_DEATH_HANDLER) {
                    for (item in base.getItems()) {
                        val old: ItemPlugin? = deathIntActions.put(item, base)
                        check(old == null) { "Overriding item plugin: " + item + ", " + old!!.javaClass.getSimpleName() + " with " + c.getSimpleName() + "!" }
                    }
                }
                base.handle()
                if (!base.getDelegatedInventoryHandlers().isEmpty()) {
                    for (item in base.getItems()) {
                        val old: ItemPlugin? = intActions.put(item, base)
                        check(old == null) { "Overriding item plugin: " + item + ", " + old!!.javaClass.getSimpleName() + " with " + c.getSimpleName() + "!" }
                    }
                }
            } catch (e: Exception) {
                log.error("", e)
            }
        }

        fun setDefaults() {
            for (plugin in intActions.values) {
                plugin.setDefaultHandlers()
            }
        }

        private val OTHER_SOUND = SoundEffect(2739)

        @JvmStatic fun dropItem(
            player: Player, option: String, slotId: Int, invisibleDelay: Int,
            visibleDelay: Int
        ) {
            val item = player.getInventory().getItem(slotId)
            if (item == null) {
                return
            }
            if (player.getBankPin().requiresVerification(
                    player,
                    Runnable { dropItem(player, option, slotId, invisibleDelay, visibleDelay) })
            ) return

            val area = player.getArea()
            if ((area is IDropPlugin && !(area as IDropPlugin).drop(player, item)) || !player.getControllerManager()
                    .canDropItem(item)
            ) {
                player.log(LogLevel.INFO, "Area-Dropping item '" + item + "' at " + player.location + ".")
                logValuableItemDrop(player, item)
                return
            }
            if (option == "Destroy") {
                player.getDialogueManager().start(DestroyItemDialogue(player, item, slotId))
                return
            }
            if (PetWrapper.getByItem(item.getId()) != null) {
                if (player.getFollower() != null) {
                    player.sendMessage("You already have a follower!")
                    return
                }
                if (player.inArea("Corporeal Beast cavern") || player.getArea() is CorporealBeastDynamicArea) {
                    player.sendMessage("Your follower hides in fear and won't come out.")
                    return
                }
                val pet = PetWrapper.getByItem(item.getId())
                if (pet.petId() == -1) {
                    player.getDialogueManager().start(PetFishDropD(player, item, slotId))
                    return
                }
                player.getInventory().deleteItem(slotId, item)
                player.setFollower(Follower(pet.petId(), player))
                player.setAnimation(PetWrapper.DROP_ANIMATION)
                return
            }

            val degradableItem = DegradableItem.ITEMS.get(item.getId())
            if (degradableItem != null) {
                if (player.getAttributes().containsKey("Ignore charged item drop message")) {
                    degrade(player, item, slotId, false, invisibleDelay, visibleDelay)
                    return
                }
                player.getDialogueManager().start(object : Dialogue(player) {
                    override fun buildDialogue() {
                        item(item, "Dropping this item will completely degrade it. Are you sure you wish to do so?")
                        options(
                            "Drop the item?", DialogueOption("Yes.", Runnable {
                                degrade(
                                    player, item, slotId, false,
                                    invisibleDelay, visibleDelay
                                )
                            }), DialogueOption(
                                "Yes, don't ask this for any " +
                                        "charged " +
                                        "item again.", Runnable {
                                    degrade(
                                        player, item, slotId, true, invisibleDelay,
                                        visibleDelay
                                    )
                                }),
                            DialogueOption("No.")
                        )
                    }
                })
                return
            }
            if (player.getTemporaryAttributes().remove("threshold warning bypass") == null) {
                val threshold =
                    player.getVarManager().getBitValue(SettingsInterface.MINIMUM_DROP_ITEM_VALUE_VARBIT_ID)
                if (player.getVarManager()
                        .getBitValue(SettingVariables.DROP_ITEM_WARNING_VARBIT_ID) == 1 && threshold > 0 && item.getSellPrice() >= threshold
                ) {
                    player.getDialogueManager().start(object : Dialogue(player) {
                        override fun buildDialogue() {
                            item(
                                item,
                                "This item you are trying to drop is considered " + Colour.RS_RED.wrap("valuable") +
                                        ". Are you absolutely sure you want to drop it?"
                            )
                            options(item.getName() + ": Really drop it?", DialogueOption("Drop it.", Runnable {
                                player.getTemporaryAttributes().put("threshold warning bypass", true)
                                dropItem(player, option, slotId, invisibleDelay, visibleDelay)
                            }), DialogueOption("No, don't drop it."))
                        }
                    })
                    return
                }
            }
            player.getInventory().deleteItem(slotId, item)
            player.getInterfaceHandler().closeInterfaces()
            player.getPacketDispatcher().sendSoundEffect(OTHER_SOUND)
            if (area is DropPlugin) {
                if (!(area as DropPlugin).dropOnGround(player, item)) {
                    return
                }
            }
            if (GameConstants.WORLD_PROFILE.isPublic()) {
                if (player.getPrivilege().`is`(PlayerPrivilege.FORUM_MODERATOR)) {
                    player.sendMessage("Your item turns into dust as it hits the ground.")
                    return
                }
            }

            player.log(LogLevel.INFO, "Dropping item '" + item + "' at " + player.location + ".")
            logValuableItemDrop(player, item)
            if (WildernessArea.isWithinWilderness(player)) {
                val consumable =
                    item.getDefinitions().containsOption("Eat") || item.getDefinitions()
                        .containsOption("Drink") || Consumable.consumables.containsKey(item.getId())
                World.spawnFloorItem(
                    item, player, if (!consumable && item.isTradable()) -1 else invisibleDelay,
                    if (item.isTradable()) visibleDelay else -1
                )
            } else {
                World.spawnFloorItem(item, player, invisibleDelay, if (item.isTradable()) visibleDelay else -1)
            }
        }

        private fun logValuableItemDrop(player: Player, item: Item) {
            val value = item.getAmount().toLong() * item.getSellPrice()
            if (value > 150000) {
                if (GameConstants.WORLD_PROFILE.isLogsDatabaseEnabled()) log<GameLogMessage.GroundItem.Drop>(Level.INFO) {
                    GameLogMessage.GroundItem.Drop(
                        Clock.System.now(),
                        player.getDbUsername(),
                        item,
                        player.location
                    )
                }
            }
        }

        private fun degrade(
            player: Player, item: Item, slotId: Int,
            ignoreMessages: Boolean, invisibleDelay: Int, visibleDelay: Int
        ) {
            if (player.getInventory().getItem(slotId) !== item) {
                return
            }
            if (ignoreMessages) {
                player.getAttributes().put("Ignore charged item drop message", true)
            }
            val area = player.getArea()
            if (area is IDropPlugin) {
                if (!(area as IDropPlugin).dropOnGround(player, item)) {
                    return
                }
            }
            val degraded = DegradableItem.getCompletelyDegradedId(item.getId())
            player.getInventory().deleteItem(slotId, item)
            player.getInterfaceHandler().closeInterfaces()
            player.getPacketDispatcher().sendSoundEffect(OTHER_SOUND)
            if (degraded != -1) {
                if (WildernessArea.isWithinWilderness(player)) {
                    val definitions = ItemDefinitions.getOrThrow(degraded)
                    val consumable =
                        definitions.containsOption("Eat") || definitions.containsOption("Drink") || Consumable.consumables.containsKey(
                            degraded
                        )
                    if (consumable) {
                        World.spawnFloorItem(
                            Item(degraded, item.getAmount()), player, invisibleDelay + visibleDelay,
                            -1
                        )
                    } else {
                        World.spawnFloorItem(
                            Item(degraded, item.getAmount()),
                            player,
                            if (item.isTradable()) -1 else invisibleDelay,
                            if (item.isTradable()) visibleDelay else -1
                        )
                    }
                } else {
                    World.spawnFloorItem(
                        Item(degraded, item.getAmount()), player, invisibleDelay, if (item.isTradable())
                            visibleDelay
                        else
                            -1
                    )
                }
            }
        }
}
