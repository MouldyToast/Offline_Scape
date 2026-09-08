package org.jesse.game.model.item.leagues.raging_echo

import org.jesse.game.world.entity.player.echoAxeBanking
import org.jesse.game.world.entity.player.echoAxeBurningLogs
import org.jesse.game.world.entity.player.echoPickaxeBanking
import org.jesse.game.world.entity.player.echoPickaxeSmelting
import org.jesse.game.content.skills.crafting.CraftingDefinitions
import org.jesse.game.content.skills.firemaking.Firemaking
import org.jesse.game.content.skills.fletching.FletchingDefinitions
import org.jesse.game.content.skills.smithing.SmeltableBar
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-21
 */
class EchoPickaxe: ItemPlugin() {

    companion object {

        fun Player.processEchoPickaxe(productId: Int) {
            var product = Item(productId, 1)
            val smeltingOre = echoPickaxeSmelting
            if (smeltingOre) {
                val data = SmeltableBar.getData(productId)
                if (data == null)
                    sendFilteredMessage("This item cannot be smelted.")
                else {
                    getSkills().addXp(SkillConstants.SMITHING, data.xp)
                    sendFilteredMessage("Your pickaxe smelted the ${product.name} for ${data.xp} smithing experience.")
                    product = product.oreToBar()
                }
            }
            else {
                val gemData = CraftingDefinitions.GemCuttingData.getDataByMaterial(product, Item(CHISEL))
                gemData ?: return
                product = gemData.products[0]
                getSkills().addXp(SkillConstants.CRAFTING, gemData.xp)
                sendFilteredMessage("Your pickaxe cut the ${product.name} for ${gemData.xp} crafting experience.")
            }
            if (echoPickaxeBanking)
                bank.add(product)
            else
                inventory.addItem(product)
        }

        private fun Item.oreToBar(): Item {
            return when (id) {
                BLURITE_ORE -> Item(BLURITE_BAR)
                SILVER_ORE -> Item(SILVER_BAR)
                IRON_ORE -> Item(STEEL_BAR)
                GOLD_ORE -> Item(GOLD_BAR)
                MITHRIL_ORE -> Item(MITHRIL_ORE)
                ADAMANTITE_ORE -> Item(ADAMANTITE_BAR)
                RUNITE_ORE -> Item(RUNITE_BAR)
                else -> this
            }
        }
    }


    private fun Player.sendToggleMessage()  =
        sendMessage("Your echo pickaxe will now ${
            if(echoPickaxeSmelting) "smelt your ores" else "cut your gems"
        } for you.")

    private fun Player.sendToggleBankingMessage()  =
        sendMessage("Your echo pickaxe will ${
            if(echoAxeBanking) "now automatically bank" else "no longer bank"
        } logs for you.")

    override fun handle() {
        bind("Toggle") { player, _, _ ->
            player.dialogue {
                options {
                    "Toggle Banking" {
                        val current: Boolean = player.echoPickaxeBanking
                        player.echoPickaxeBanking = !current
                        player.sendToggleBankingMessage()

                    }
                    "Toggle Passive" {
                        val current: Boolean = player.echoPickaxeSmelting
                        player.echoPickaxeSmelting = !current
                        player.sendToggleMessage()
                    }
                }
            }
        }
    }

    override fun getItems(): IntArray = intArrayOf(ECHO_PICKAXE)

}