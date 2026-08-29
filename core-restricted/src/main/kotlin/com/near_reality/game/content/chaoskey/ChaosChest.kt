package com.near_reality.game.content.chaoskey

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.content.drops.table.DropTable
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Action
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject
import mgi.types.config.items.ItemDefinitions
import mgi.utilities.StringFormatUtil
import java.util.function.Consumer

/**
 * @author Alycia <https:></https:>//github.com/alycii>
 */
class ChaosChest : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        player.actionManager.setAction(object : Action() {
            var cycle: Int = 0
            override fun start(): Boolean {
                if (!player.inventory.containsItem(ItemId.CHAOS_KEY)) {
                    player.sendMessage("You need a Chaos Key to open the Chaos Chest.")
                    return false
                }
                player.animation = Animation(4411)
                return true
            }

            override fun process(): Boolean {
                return cycle <= 3
            }

            override fun processWithDelay(): Int {
                if (cycle == 3) {
                    if (player.inventory.deleteItem(ItemId.CHAOS_KEY, 1).result == RequestResult.SUCCESS) {
                        val reward = ChaosChestTable.roll()
                        player.inventory.addItem(995, Utils.random(150_000, 250_000))
                        World.getPlayers().forEach(Consumer { p: Player ->
                            p.sendMessage(
                                " <img=53>  " + StringFormatUtil.formatString(player.username) + " looted the Chaos Chest for gold and <col=800002>" + reward.amount + "</col> x <col=800002>" + ItemDefinitions.getOrThrow(
                                    reward.id
                                ).name + "</col>."
                            )
                        })
                        player.variables.setSkull(true)
                        if (!player.variables.isTeleBlocked && !player.variables.hasTeleblockImmunity()) {
                            player.variables.schedule(250, TickVariable.TELEBLOCK)
                            player.variables.schedule(350, TickVariable.TELEBLOCK_IMMUNITY)
                            player.sendMessage("<col=4f006f>You have been tele-blocked for looting the Chaos Chest. It will expire in 2 minutes, 30 seconds</col>.")
                        }
                        player.inventory.addOrDrop(reward)
                    }
                }
                cycle++
                return 1
            }
        })
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(
            55668
        )
    }
}


internal object ChaosChestTable {
    private val table = DropTable()

    init {
        table
            .append(ItemId.DRAGON_SCIMITAR + 1, 10, 3)
            .append(ItemId.DRAGON_DAGGER + 1, 10, 3)
            .append(ItemId.DRAGON_BONES_PACK, 10, 1)
            .append(ItemId.DRAGON_ARROW, 10, 100)
            .append(ItemId.LARRANS_KEY, 10, 1)
            .append(ItemId.CRYSTAL_KEY, 10, 3)
            .append(ItemId.YEW_LOGS + 1, 10, 150)
            .append(ItemId.MAGIC_LOGS + 1, 10, 90)
            .append(ItemId.RAW_MANTA_RAY + 1, 10, 150)
            .append(ItemId.REMNANT_POINT_VOUCHER_1, 5, 1000)
            .append(CustomItemId.OSNR_MYSTERY_BOX, 2, 1)
            .append(CustomItemId.DEATH_CAPE, 2, 1)
            .append(ItemId.PRIMAL_WARHAMMER, 1, 1)
    }

    fun roll(): Item {
        return table.rollItem()
    }
}