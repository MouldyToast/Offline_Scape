package org.jesse.plugins.item.actions.death_items

import org.jesse.game.content.consumables.Consumable
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingStructs
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingVariables
import org.jesse.game.model.ui.testinterfaces.advancedsettings.Settings
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class ConsumablesItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 13/06/2022
         */
        items(Consumable.consumables.keys)

        death {
            lost { yield(item) }
            if (pvp) {
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else if (SettingVariables.getVariableValue(Settings.findSettingByStructId(SettingStructs.FOOD_AND_POTIONS_CAN_FORM_SUPPLY_PILES_ON_DEATH_STRUCT_ID), player) == 0) {
                status { ItemDeathStatus.GO_TO_GRAVESTONE_OR_DROP_ON_GROUND }
            } else {
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
