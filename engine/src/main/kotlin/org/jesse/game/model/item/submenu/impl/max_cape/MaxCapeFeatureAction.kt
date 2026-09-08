package org.jesse.game.model.item.submenu.impl.max_cape

import org.jesse.game.model.item.submenu.ISubMenuAction
import org.jesse.game.content.RespawnPoint
import org.jesse.game.item.Item
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue.TITLE
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.entity.player.variables.TickVariable
import org.jesse.logger.NearRealityLogger
import org.jesse.plugins.item.capes.NewMaxCapes
import org.jesse.utils.TimeUnit
import org.jesse.game.item.ids.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-10
 */
class MaxCapeFeatureAction(
    private val searchIndex: Int = 0,
    private val ringOfLifeIndex: Int = 1,
    private val consumeIndex: Int = 2,
    private val staminaBoostIndex: Int = 3
): ISubMenuAction {

    private val logger = NearRealityLogger.getLogger(MaxCapeFeatureAction::class.java)

    override fun onAction(player: Player, selectedItemIndex: Int) {
        when(selectedItemIndex) {
            searchIndex -> player.maxCapeSearch()
            ringOfLifeIndex -> player.ringOfLifeEffect()
            consumeIndex -> player.consumeEffect()
            staminaBoostIndex -> player.staminaBoostEffect()
            else -> logger.warn("Unused action index in MaxCapeFeatureAction: $selectedItemIndex")
        }
    }

    private fun Player.staminaBoostEffect() {
        dialogue {
            val lastConsumptionDate = getNumericAttribute("Stamina Boost Use")
            val milliseconds = lastConsumptionDate.toLong()
            if (TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - milliseconds) < 24) {
                sendMessage("You have used your stamina boost for today. Try again tomorrow.")
                return@dialogue
            }
            addAttribute("Stamina Boost Use", System.currentTimeMillis())
            sendMessage("You feel reinvigorated.")
            variables.runEnergy = 100.0
            varManager.sendBit(25, 1)
            variables.schedule(100, TickVariable.STAMINA_ENHANCEMENT)
        }
    }

    private fun Player.consumeEffect() {
        dialogue {
            val isGatheringJunk = player.attributes.containsKey("avasDeviceRetrieve")
            if (isGatheringJunk) {
                plain("The undead chicken can protect some of your ammunition while you're ranging, and will also gather random metal items for you.")
                options("Ask it to stop gathering junk?") {
                    "Yes" {
                        player.attributes.remove("avasDeviceRetrieve")
                        plain("You somehow communicate your message to the undead chicken. Henceforth it will no longer gather up random metal items while you've got it equipped.")
                    }
                    "No" {}
                }
            }
            else {
                plain("The undead chicken understands that you currently don't want it to accumulate random metal items while you've got it equipped.")
                options("Ask it to start gathering junk?") {
                    "Yes" {
                        player.attributes["avasDeviceRetrieve"] = true
                        plain("You somehow communicate your message to the undead chicken. Henceforth it will gather up random metal items while you've got it equipped.")
                    }
                    "No" {}
                }
            }
        }
    }

    private fun Player.ringOfLifeEffect() {
        dialogue {
            options(TITLE) {
                "Ring of life effect" {
                    val enabled = player.getBooleanAttribute("Skillcape ring of life teleport")
                    plain("Your cape will ${if (enabled) "" else " not"} currently teleport you to safety should your health reach dangerous levels.")

                    options("Would you like to ${if (enabled) "disable" else "enable"} this feature?") {
                        "Yes" {
                            player.toggleBooleanAttribute("Skillcape ring of life teleport")
                            plain("Your cape will follow your instructions to${if (enabled) " not " else " "}save you when your health is low.")
                        }
                        "No" {}
                    }
                }
                "Ring of life spawn" {
                    options("Choose a respawn destination.") {
                        "Ardougne" {
                            player.respawnPoint = RespawnPoint.ARDOUGNE
                            player.sendMessage(Colour.RED.wrap("Your respawn location has now been changed to Ardougne."))
                        }
                        "Lumbridge - ${Colour.RS_RED.wrap("Default")}" {
                            player.respawnPoint = RespawnPoint.LUMBRIDGE
                            player.sendMessage(Colour.RED.wrap("Your respawn location has now been changed to the default."))
                        }
                    }
                }
            }
        }
    }

    private fun Player.maxCapeSearch() {
        dialogue {
            options("What would you like to search for?") {
                "Pestle and Mortar" {
                    if (inventory.containsItem(PESTLE_AND_MORTAR, 1))
                        sendMessage("You already have a pestle and mortar with you.")
                    else {
                        player.inventory.addOrDrop(Item(PESTLE_AND_MORTAR))
                        player.sendMessage("You search the cape and find a ${Colour.RS_RED.wrap("Pestle and mortar.")}")
                        player.animation = Animation(1376)
                    }
                }
                "Mithril Grapple & Crossbow" {
                    val searches = variables.grappleAndCrossbowSearches
                    if (searches >= NewMaxCapes.MAX_GRAPPLE_SEARCHES)
                        sendMessage("You may only receive a grapple and crossbow three times per day. Try again tomorrow.")
                    else if (inventory.containsItem(BRONZE_CROSSBOW, 1)
                        && inventory.containsItem(MITH_GRAPPLE_9419, 1))
                        sendMessage("You already have a crossbow and a Mithril Grapple with you.")
                    else {
                        variables.grappleAndCrossbowSearches = searches + 1
                        inventory.addOrDrop(Item(BRONZE_CROSSBOW))
                        inventory.addOrDrop(Item(MITH_GRAPPLE_9419))
                        sendMessage("You search the cape and find a ${
                            Colour.RS_RED.wrap("Bronze crossbow")
                        } and ${
                            Colour.RS_RED.wrap("Mithril grapple.")
                        }")
                        val remaining = NewMaxCapes.MAX_GRAPPLE_SEARCHES - (searches + 1)
                        if (remaining >= 1) {
                            val remainingString = if (remaining == 2) "twice more today" else "one more time today"
                            sendMessage("You may search the cape $remainingString")
                        }
                        else
                            sendMessage("You search the cape for the final time today.")

                        animation = Animation(1376)
                    }

                }
            }
        }
    }
}