package org.jesse.game.content.chaoskey

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.game.world.World
import org.jesse.game.world.region.area.wilderness.WildernessArea
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
