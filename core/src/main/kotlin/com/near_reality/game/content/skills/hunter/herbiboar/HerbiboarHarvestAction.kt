package com.near_reality.game.content.skills.hunter.herbiboar

import com.near_reality.game.content.skills.hunter.herbiboar.Herbiboar.unharvestedHerbiboar
import com.zenyte.game.content.drops.table.DropTable
import com.zenyte.game.content.follower.impl.MiscPet
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.plugins.item.HerbSack
import org.apache.commons.lang3.ArrayUtils

/**
 * @author Andys1814
 * @since 1/26/2025
 */
class HerbiboarHarvestAction : NPCPlugin() {

    companion object {
        const val HERBI_RARITY = 3_000
    }

    override fun handle() {
        bind("Harvest") { player: Player, npc: NPC ->
            harvestHerbiboar(player, npc)
        }
    }

    private fun Player.hasMagicSecateurs(): Boolean {
        return equipment.getId(EquipmentSlot.WEAPON) == 7409 || inventory.containsItem(7409)
    }

    private fun Player.getHerbiboarHarvestTable(): DropTable {
        val table = DropTable()

        table.append(ItemId.GRIMY_GUAM_LEAF, 1, 1)
        if (skills.getLevel(Skills.HERBLORE) <= 80) {
            table.append(ItemId.GRIMY_MARRENTILL, 10, 1)
        }
        if (skills.getLevel(Skills.HERBLORE) <= 78) {
            table.append(ItemId.GRIMY_TARROMIN, 6, 1)
        }
        if (skills.getLevel(Skills.HERBLORE) <= 76) {
            table.append(ItemId.GRIMY_HARRALANDER, 8, 1)
        }
        if (skills.getLevel(Skills.HERBLORE) >= 41) {
            table.append(ItemId.GRIMY_TARROMIN, 7, 1)
        }
        if (skills.getLevel(Skills.HERBLORE) >= 62) {
            table.append(ItemId.GRIMY_DWARF_WEED, 6, 1)
        }
        if (skills.getLevel(Skills.HERBLORE) >= 74) {
            table.append(ItemId.GRIMY_SNAPDRAGON, 5, 1)
        }
        if (skills.getLevel(Skills.HERBLORE) >= 77) {
            table.append(ItemId.GRIMY_TORSTOL, 5, 1)
        }

        table.append(ItemId.GRIMY_GUAM_LEAF, 25, 1)
        table.append(ItemId.GRIMY_IRIT_LEAF, 12, 1)
        table.append(ItemId.GRIMY_AVANTOE, 10, 1)
        table.append(ItemId.GRIMY_CADANTINE, 9, 1)
        table.append(ItemId.GRIMY_KWUARM, 9, 1)

        return table
    }

    private fun harvestHerbiboar(player: Player, npc: NPC) {
        if (player.unharvestedHerbiboar == null || player.unharvestedHerbiboar != npc.index) {
            player.sendMessage("This is not your herbiboar to harvest.")
            return
        }

        if (player.inventory.freeSlots == 0) {
            player.sendMessage("You have no space to harvest from the herbiboar.")
            return
        }

        schedule(object : TickTask() {
            override fun run() {
                if (ticks == 0) {
                    player.lock(3)
                    player.animation = Animation(2277)
                } else if (ticks == 1) {
                    player.sendSound(SoundEffect(1106))
                    npc.animation = Animation(7689)
                    player.animation = Animation.STOP

                    var numHerbs = Utils.random(1, 3)
                    if (player.hasMagicSecateurs()) {
                        numHerbs *= 2
                    }

                    val table = player.getHerbiboarHarvestTable()
                    val numRolls = player.inventory.freeSlots.coerceAtMost(numHerbs)
                    val results = table.roll(numRolls)
                    results.forEach {
                        val item = Item(it.id, it.quantity)
                        val herbSack = player.herbSack
                        if (herbSack != null &&
                            player.inventory.containsItem(HerbSack.HERB_SACK_OPEN)
                            && herbSack.isOpen && !herbSack.isFull(item) &&
                            ArrayUtils.contains(
                                com.zenyte.game.model.item.containers.HerbSack.HERBS,
                                item.id
                            )
                        ) {
                            herbSack.container.add(item)
                            herbSack.container.refresh(player)
                        } else {
                            player.inventory.addItem(item)
                        }
                    }

                    MiscPet.HERBI.roll(player, HERBI_RARITY)

                    player.notificationSettings.increaseKill("Herbiboar")
                    val harvested = player.notificationSettings.getKillcount("Herbiboar")
                    player.sendMessage("Your herbiboar harvest count is: " + Colour.RED.wrap(harvested.toString() + "") + ".")

                    player.skills.addXp(Skills.HERBLORE, 25.0)

                    player.sendMessage("You harvest herbs from the herbiboar, whereupon it escapes.")
                    npc.setTransformation(7786)
                } else if (ticks == 2) {
                    npc.animation = Animation(7690)
                    npc.graphics = Graphics(310)
                } else if (ticks == 3) {
                    npc.remove()
                    player.unharvestedHerbiboar = null
                }
                ticks++
            }
        }, 0, 1)
    }

    override fun getNPCs() = intArrayOf(NpcId.HERBIBOAR)

}