package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.game.world.region.area.wilderness.WildernessArea
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class RevenantMiscItemaction : ItemActionScript() {

    init {
        items(
            ANCIENT_EMBLEM, ANCIENT_TOTEM, ANCIENT_STATUETTE,
            ANCIENT_MEDALLION, ANCIENT_EFFIGY, ANCIENT_RELIC
        )

        death {
            setAlwaysLostOnDeath()
            if (pvp || WildernessArea.isWithinWilderness(player.x, player.y))
                lost { yield(item) }
            else
                kept { yield(item) }
            status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
        }
    }
}
