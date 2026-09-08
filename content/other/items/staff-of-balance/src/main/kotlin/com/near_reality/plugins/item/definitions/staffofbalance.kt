package com.near_reality.plugins.item.definitions

import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.*
import com.zenyte.game.world.entity.player.SkillConstants.*
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.*
import com.zenyte.game.world.entity.player.Bonuses.Bonus.*

class StaffofbalanceItems : ItemDefinitionsScript() {

    init {
        STAFF_OF_BALANCE {
            weight = 1.5F
            equipment {
                slot = EquipmentSlot.WEAPON.slot
                weapon {
                    blockAnimation = 420
                    standAnimation = 813
                    walkAnimation = 1205
                    runAnimation = 1210
                    standTurnAnimation = 1209
                    rotate90Animation = 1207
                    rotate180Animation = 1206
                    rotate270Animation = 1208
                    accurateAnimation = 440
                    aggressiveAnimation = 440
                    controlledAnimation = 440
                    defensiveAnimation = 440
                    attackSpeed = 4
                    interfaceVarbit = 18
                    normalAttackDistance = 0
                    longAttackDistance = 0
                }
                bonuses {
                    ATT_STAB(55)
                    ATT_SLASH(70)
                    ATT_MAGIC(17)

                    DEF_SLASH(3)
                    DEF_CRUSH(3)
                    DEF_MAGIC(17)

                    Bonuses.Bonus.STRENGTH(72)
                    MAGIC_DAMAGE(15)
                }
                requirements {
                    ATTACK(75)
                    MAGIC(75)
                }
            }
        }
    }
}
