package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.CRYSTAL_ARMOUR_SEED
import com.zenyte.game.item.ItemId.CRYSTAL_BODY
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_27697
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_27709
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_27721
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_27733
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_27745
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_27757
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_27769
import com.zenyte.game.item.ItemId.CRYSTAL_HELM
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_27705
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_27717
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_27729
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_27741
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_27753
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_27765
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_27777
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_27701
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_27713
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_27725
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_27737
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_27749
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_27761
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_27773

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