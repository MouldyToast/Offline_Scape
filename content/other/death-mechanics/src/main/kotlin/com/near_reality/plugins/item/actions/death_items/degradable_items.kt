package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.degradableitems.DegradableItem
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.*

class DegradableItemsItemaction : ItemActionScript() {

    private val degradableItems = DegradableItem.ITEMS.keys
        .filterNot {
            it == BONECRUSHER
                    || it == CRAWS_BOW
                    || it == WEBWEAVER_BOW_27655
                    || it == THAMMARONS_SCEPTRE
                    || it == ACCURSED_SCEPTRE_27665
                    || it == VIGGORAS_CHAINMACE
                    || it == URSINE_CHAINMACE_27660
                    || it == RING_OF_SUFFERING_RI
                    || it == RING_OF_SUFFERING_I
                    || it == BRACELET_OF_ETHEREUM
                    || it == AMULET_OF_BLOOD_FURY
                    || it == BOW_OF_FAERDHINEN
                    || it == BOW_OF_FAERDHINEN_C
                    || it == DIZANAS_QUIVER
                    || it == DIZANAS_QUIVER_L
                    || it == CRYSTAL_HELM
                    || it == CRYSTAL_BODY
                    || it == CRYSTAL_LEGS
        }

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(degradableItems)

        death {
            status { if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE }
            lost {
                if (!pvp) {
                    return@lost yield(item)
                }
                val degradable = DegradableItem.ITEMS[item.id]
                requireNotNull(degradable)
                yield(Item(DegradableItem.getCompletelyDegradedId(item.id, true)))
                val function = degradable.function
                if (function != null) {
                    val result = function.apply(item)
                    for (other in result) {
                        if (other.amount <= 0) continue
                        yield(other)
                    }
                }
            }
        }
    }
}
