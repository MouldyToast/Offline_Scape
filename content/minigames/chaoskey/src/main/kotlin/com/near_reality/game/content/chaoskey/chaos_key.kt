package com.near_reality.game.content.chaoskey

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.world.World
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.CHAOS_KEY_ACTIVE

class ChaosKeyItemaction : ItemActionScript() {

    init {
        /**
         * @author Alycia <https://github.com/alycii>
         */

        items(CHAOS_KEY_ACTIVE)

        "Drop" {
            player.inventory.deleteItem(CHAOS_KEY_ACTIVE, 1)
            World.spawnFloorItem(Item(CHAOS_KEY_ACTIVE), player.location, null, 0, 300)
            World.getPlayers().forEach { p ->
                p.sendMessage("<img=54> Event: " + player.username + " dropped the <col=800002>Chaos Key</col>.")
                p.sendMessage(
                    "<img=54> Event: It has been dropped in the wild at <col=800002>Lvl " + WildernessArea.getWildernessLevel(
                        player.location
                    ).orElse(0) + " </col>."
                )
            }
        }

        death {
            setAlwaysLostOnDeath()
            status { ItemDeathStatus.DELETE }
            lost {
                World.spawnFloorItem(Item(CHAOS_KEY_ACTIVE), player.location, null, 0, 300)
                World.getPlayers().forEach { p ->
                    p.sendMessage("<img=54> Event: " + player.username + " was killed and dropped the <col=800002>Chaos Key</col>.")
                    p.sendMessage(
                        "<img=54> Event: It has been dropped in the wild at <col=800002>Lvl " + WildernessArea.getWildernessLevel(
                            player.location
                        ).orElse(0) + " </col>."
                    )
                }
            }
        }
    }
}
