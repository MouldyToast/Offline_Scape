package com.near_reality.game.content.bountyhunter

import com.near_reality.game.world.entity.player.bountyHunterPoints
import com.near_reality.game.world.entity.player.bountyTargetLevelRange
import com.zenyte.game.content.universalshop.UniversalShopInterface.Companion.openInterfaceToTab
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.EMBLEM_TRADER_12113
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-07
 */
class EmblemTrader: ItemOnNPCAction, NPCPlugin() {
    override fun handle() {
        bind("Talk-to") { player: Player, _: NPC -> sendTalkTo(player) }
        bind("Rewards") { player: Player, _: NPC -> openInterfaceToTab(player, 9) }
        bind("Skull") { player: Player, _: NPC -> sendRequestForSkull(player) }
    }

    companion object {
        fun hasEsotericEmblem(player: Player): Boolean =
            player.inventory.containsAnyOf(
                ESOTERIC_EMBLEM_TIER_1,
                ESOTERIC_EMBLEM_TIER_2,
                ESOTERIC_EMBLEM_TIER_3,
                ESOTERIC_EMBLEM_TIER_4,
                ESOTERIC_EMBLEM_TIER_5,
                ESOTERIC_EMBLEM_TIER_6,
                ESOTERIC_EMBLEM_TIER_7,
                ESOTERIC_EMBLEM_TIER_8,
                ESOTERIC_EMBLEM_TIER_9,
                ESOTERIC_EMBLEM_TIER_10
            )

        fun hasMysteriousEmblem(player: Player): Boolean =
            player.inventory.containsAnyOf(
                MYSTERIOUS_EMBLEM,
                MYSTERIOUS_EMBLEM_TIER_2,
                MYSTERIOUS_EMBLEM_TIER_3,
                MYSTERIOUS_EMBLEM_TIER_4,
                MYSTERIOUS_EMBLEM_TIER_5,
                MYSTERIOUS_EMBLEM_TIER_6,
                MYSTERIOUS_EMBLEM_TIER_7,
                MYSTERIOUS_EMBLEM_TIER_8,
                MYSTERIOUS_EMBLEM_TIER_9,
                MYSTERIOUS_EMBLEM_TIER_10
            )

        val emblemTiers = mapOf(
            ESOTERIC_EMBLEM_TIER_1 to 1,
            ESOTERIC_EMBLEM_TIER_2 to 2,
            ESOTERIC_EMBLEM_TIER_3 to 3,
            ESOTERIC_EMBLEM_TIER_4 to 4,
            ESOTERIC_EMBLEM_TIER_5 to 5,
            ESOTERIC_EMBLEM_TIER_6 to 6,
            ESOTERIC_EMBLEM_TIER_7 to 7,
            ESOTERIC_EMBLEM_TIER_8 to 8,
            ESOTERIC_EMBLEM_TIER_9 to 9,
            ESOTERIC_EMBLEM_TIER_10 to 10
        )

        val emblemTierPoints = mapOf(
            1 to 2,
            2 to 4,
            3 to 7,
            4 to 11,
            5 to 16,
            6 to 22,
            7 to 29,
            8 to 37,
            9 to 46,
            10 to 56
        )

        val mapOfMysteriousEmblemValues = mapOf(
            MYSTERIOUS_EMBLEM to 15,
            MYSTERIOUS_EMBLEM_TIER_2 to 20,
            MYSTERIOUS_EMBLEM_TIER_3 to 30,
            MYSTERIOUS_EMBLEM_TIER_4 to 45,
            MYSTERIOUS_EMBLEM_TIER_5 to 60,
            MYSTERIOUS_EMBLEM_TIER_6 to 100,
            MYSTERIOUS_EMBLEM_TIER_7 to 130,
            MYSTERIOUS_EMBLEM_TIER_8 to 180,
            MYSTERIOUS_EMBLEM_TIER_9 to 250,
            MYSTERIOUS_EMBLEM_TIER_10 to 350
        )

        fun handleEsotericEmblem(player: Player, item: Item, includeDialogue: Boolean = true) {
            val tierOfEmblem = emblemTiers[item.id] ?: return
            val pointForEmblem = emblemTierPoints[tierOfEmblem] ?: return
            if (player.inventory.deleteItem(item).result == RequestResult.SUCCESS) {
                player.bountyHunterPoints += pointForEmblem
                if(includeDialogue) {
                    player.dialogue(EMBLEM_TRADER_12113) {
                        item(item.id, "You have received $pointForEmblem points for your emblem.")
                    }
                }
            }
        }

        fun handleMysteriousEmblem(player: Player, item: Item, includeDialogue: Boolean = true) {
            val valueForEmblem = mapOfMysteriousEmblemValues[item.id] ?: return
            if (player.inventory.deleteItem(item).result == RequestResult.SUCCESS) {
                player.inventory.addOrDrop(BLOOD_MONEY, valueForEmblem)
                if(includeDialogue) {
                    player.dialogue(EMBLEM_TRADER_12113) {
                        item(item.id, "You have received $valueForEmblem Blood Money for your emblem.")
                    }
                }
            }
        }
    }





    private fun sendTalkTo(player: Player) {
        if (hasEsotericEmblem(player) || hasMysteriousEmblem(player) ) {
            player.dialogue(EMBLEM_TRADER_12113) {
                npc("Don't suppose you've come across any strange emblems or artefacts along your journey? Ancient artefacts?")
                player("Well I have found this...")
                options {
                    "Give ALL my emblems to the trader." {
                        player.exchangeEmblems()
                    }
                    "Actually it's kind of cool, I want to keep it." {}
                }
            }
        }
        else sendNormalGreeting(player)
    }

    private fun sendNormalGreeting(player: Player) {
        player.dialogue(EMBLEM_TRADER_12113) {
            npc("Hello, wanderer. How may I help you?")
            options {
                "What is this place?" { sendWhatIsThisPlace(player) }
                "What level Targets might I be assigned?" { sendTargetInformation(player) }
                if (!player.variables.isSkulled)
                    "Can I have a PK skull, please?" { sendRequestForSkull(player) }
                else
                    "Can you make my PK skull last longer?" { sendRequestToExtendSkull(player) }
                "What do the different colour skulls mean?" { sendColoredSkullInfo(player) }
            }
        }
    }

    private fun sendWhatIsThisPlace(player: Player) {
        player.dialogue(EMBLEM_TRADER_12113) {
            player("What is this place?")
            npc("This is the Wilderness. Once, it was home to a group of demons who set up a fortress inside this dormant volcano to mine rare minerals. However, their activity reactivated the volcano, resulting in their demise.")
            npc("Years later, humans settled in the ruins. Convinced that they were the descendants of the demons, they began to hunt each other for fun.")
            npc("Today, the wilderness is home to Bounty Hunter, a magnificent battle arena where you can engage in combat and test your mettle against skilled opponents.")
        }
    }

    private fun sendColoredSkullInfo(player: Player) {
        player.dialogue(EMBLEM_TRADER_12113) {
            player("What do the different colour skulls mean?")
            npc("The different colours represent risk values. Bronze is up to 200K. Iron is up to 800K. Green is up to 2M. Blue is up to 8M. Red is any amount greater than 8M.")
        }
    }

    private fun sendRequestForSkull(player: Player) {
        player.dialogue(EMBLEM_TRADER_12113) {
            player("Can I have a PK skull, please?")
            plain("A PK skull means you drop ALL your items on death.")
            options {
                "Give me a PK skull." {
                    player.variables.setSkull(true)
                    player.dialogue(EMBLEM_TRADER_12113) {
                        item(SKULL, "You are now skulled.")
                    }
                }
                "Cancel." {}
            }
        }
    }

    private fun sendRequestToExtendSkull(player: Player) {
        player.dialogue(EMBLEM_TRADER_12113) {
            plain("Extend your PK skull duration?")
            options {
                "Yes" {
                    player.variables.setSkull(true)
                    player.dialogue(EMBLEM_TRADER_12113) {
                        item(SKULL, "Your PK skull will last for another 20 minutes.")
                    }
                }
                "No" {}
            }
        }
    }

    private fun sendTargetInformation(player: Player) {
        player.dialogue(EMBLEM_TRADER_12113) {
            player("What level Targets might I be assigned?")
            npc("You may be assigned Targets up to ${player.bountyTargetLevelRange} levels stronger than you.")
            plain("Current maximum: ${player.bountyTargetLevelRange} levels higher")
            options {
                "Set maximum to 5 levels higher" {
                    player.bountyTargetLevelRange = 5
                    player.dialogue(EMBLEM_TRADER_12113) {
                        npc("Very well, you may be assigned Targets up to 5 levels stronger than you. This range only applies to Targets.")
                    }
                }
                "Set maximum to 10 levels higher" {
                    player.bountyTargetLevelRange = 10
                    player.dialogue(EMBLEM_TRADER_12113) {
                        npc("Very well, you may be assigned Targets up to 10 levels stronger than you. This range only applies to Targets.")
                    }
                }
                "Set maximum to 15 levels higher" {
                    player.bountyTargetLevelRange = 15
                    player.dialogue(EMBLEM_TRADER_12113) {
                        npc("Very well, you may be assigned Targets up to 15 levels stronger than you. This range only applies to Targets.")
                    }
                }
            }
        }
    }

    override fun getNPCs(): IntArray = intArrayOf(EMBLEM_TRADER_12113)

    override fun handleItemOnNPCAction(player: Player?, item: Item?, slot: Int, npc: NPC?) {
        player ?: return; item ?:return; npc ?: return

        if (item.name.contains("esoteric", true))
            handleEsotericEmblem(player, item)
        if (item.name.contains("archaic", true))
            handleMysteriousEmblem(player, item)
    }



    override fun getItems(): Array<Any> = arrayOf(
        ESOTERIC_EMBLEM_TIER_1, MYSTERIOUS_EMBLEM,
        ESOTERIC_EMBLEM_TIER_2, MYSTERIOUS_EMBLEM_TIER_2,
        ESOTERIC_EMBLEM_TIER_3, MYSTERIOUS_EMBLEM_TIER_3,
        ESOTERIC_EMBLEM_TIER_4, MYSTERIOUS_EMBLEM_TIER_4,
        ESOTERIC_EMBLEM_TIER_5, MYSTERIOUS_EMBLEM_TIER_5,
        ESOTERIC_EMBLEM_TIER_6, MYSTERIOUS_EMBLEM_TIER_6,
        ESOTERIC_EMBLEM_TIER_7, MYSTERIOUS_EMBLEM_TIER_7,
        ESOTERIC_EMBLEM_TIER_8, MYSTERIOUS_EMBLEM_TIER_8,
        ESOTERIC_EMBLEM_TIER_9, MYSTERIOUS_EMBLEM_TIER_9,
        ESOTERIC_EMBLEM_TIER_10, MYSTERIOUS_EMBLEM_TIER_10
    )

    override fun getObjects(): Array<Any> = arrayOf(EMBLEM_TRADER_12113)
}

fun Player.exchangeEmblems() {
    for(item in inventory.container.items.values) {
        if (item.name.contains("esoteric", true))
            EmblemTrader.handleEsotericEmblem(this, item, false)
        if (item.name.contains("archaic", true))
            EmblemTrader.handleMysteriousEmblem(this, item, false)
    }
}
