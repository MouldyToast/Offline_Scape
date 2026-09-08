package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.game.world.region.area.wilderness.WildernessArea
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class RevenantWeaponsItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(
            CRAWS_BOW,
            THAMMARONS_SCEPTRE,
            VIGGORAS_CHAINMACE,
            27655, // Webweaver bow
            27665, // Accursed sceptre
            27660 // Ursine chainmace
        )

        death {
            if (pvp || WildernessArea.isWithinWilderness(player.x, player.y)) {
                lost {
                    val transformation = when (item.id) {
                        CRAWS_BOW -> CRAWS_BOW_U
                        THAMMARONS_SCEPTRE -> THAMMARONS_SCEPTRE_U
                        VIGGORAS_CHAINMACE -> VIGGORAS_CHAINMACE_U
                        27655 -> 27652
                        27665 -> 27662
                        27660 -> 27657
                        else -> throw IllegalArgumentException("Unable to find death transformation for revenant item: ${item.id}")
                    }
                    yield(Item(transformation))
                    if (item.charges > 0) {
                        yield(Item(REVENANT_ETHER, item.charges))
                    }
                }
                status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
