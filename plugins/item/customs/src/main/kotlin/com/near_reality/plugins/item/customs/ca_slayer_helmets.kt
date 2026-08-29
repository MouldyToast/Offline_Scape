package com.near_reality.plugins.item.customs

import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import mgi.types.config.items.JSONItemDefinitions
import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*
import com.zenyte.game.world.entity.player.SkillConstants.*
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.*
import com.zenyte.game.world.entity.player.Bonuses.Bonus.*

class CaSlayerHelmetsItems : ItemDefinitionsScript() {

    fun Int.slayerHelmet(build: JSONItemDefinitions.() -> Unit = {}) = invoke {
        this.weight = 2.2F
        this.slot = EquipmentSlot.HELMET.slot
        this.equipmentType = FULL_MASK
        equipment {
            slot = EquipmentSlot.HELMET.slot
            bonuses {
                ATT_MAGIC(-6)
                ATT_RANGED(-2)

                DEF_STAB(30)
                DEF_SLASH(32)
                DEF_CRUSH(27)
                DEF_MAGIC(-1)
                DEF_RANGE(30)
            }
        }
        build()
    }

    fun Int.slayerHelmetImbued(build: JSONItemDefinitions.() -> Unit = {}) = invoke {
        this.weight = 2.2F
        this.slot = EquipmentSlot.HELMET.slot
        this.equipmentType = FULL_MASK
        equipment {
            slot = EquipmentSlot.HELMET.slot
            bonuses {
                ATT_MAGIC(3)
                ATT_RANGED(3)

                DEF_STAB(30)
                DEF_SLASH(32)
                DEF_CRUSH(27)
                DEF_MAGIC(10)
                DEF_RANGE(30)
            }
        }
        build()
    }

    init {
        TZTOK_SLAYER_HELMET.slayerHelmet()
        VAMPYRIC_SLAYER_HELMET.slayerHelmet()
        TZKAL_SLAYER_HELMET.slayerHelmet()

        TZTOK_SLAYER_HELMET_I.slayerHelmetImbued()
        TZTOK_SLAYER_HELMET_I_25902.slayerHelmetImbued()
        VAMPYRIC_SLAYER_HELMET_I.slayerHelmetImbued()
        VAMPYRIC_SLAYER_HELMET_I_25908.slayerHelmetImbued()
        TZKAL_SLAYER_HELMET_I.slayerHelmetImbued()
        TZKAL_SLAYER_HELMET_I_25914.slayerHelmetImbued()
    }
}
