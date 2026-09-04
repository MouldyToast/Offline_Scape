package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class CrystalArmourItemaction : ItemActionScript() {

    init {
        items(CRYSTAL_HELM, CRYSTAL_BODY, CRYSTAL_LEGS, CRYSTAL_HELM_27777, CRYSTAL_LEGS_27773, CRYSTAL_BODY_27769,
            CRYSTAL_HELM_27753, CRYSTAL_BODY_27757, CRYSTAL_LEGS_27749, CRYSTAL_HELM_27765, CRYSTAL_BODY_27709,
            CRYSTAL_LEGS_27761, CRYSTAL_HELM_27705, CRYSTAL_BODY_27697, CRYSTAL_LEGS_27701, CRYSTAL_HELM_27729,
            CRYSTAL_BODY_27733, CRYSTAL_LEGS_27725, CRYSTAL_HELM_27717, CRYSTAL_BODY_27721, CRYSTAL_LEGS_27713,
            CRYSTAL_HELM_27741, CRYSTAL_BODY_27745, CRYSTAL_LEGS_27737)

        death {
            if (pvp) {
                lost {
                    yield(Item(CRYSTAL_ARMOUR_SEED))
                }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                lost { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}