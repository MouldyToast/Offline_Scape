package com.near_reality.plugins.item.customs

import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*
import com.zenyte.game.world.entity.player.SkillConstants.*
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.*
import com.zenyte.game.world.entity.player.Bonuses.Bonus.*

class HandcannonItems : ItemDefinitionsScript() {

    init {
        HAND_CANNON {
            slot = EquipmentSlot.WEAPON.slot
            equipment {
                weapon {
                    isTwoHanded = true
                    attackSpeed = 4
                    longAttackDistance = 10
                    normalAttackDistance = 9
                    interfaceVarbit = 3
                    walkAnimation = 25053
                    runAnimation = 25053
                    standAnimation = 25054
                    blockAnimation = 25055
                    accurateAnimation = 25052
                    aggressiveAnimation = 25052
                    defensiveAnimation = 25052
                    controlledAnimation = 25052
                }
                bonuses {
                    Bonuses.Bonus.ATT_RANGED(90)
                }
                requirements {
                    RANGED(61)
                }
            }
        }

        HAND_CANNON_SHOT {
            slot = EquipmentSlot.AMMUNITION.slot
            equipment {
                bonuses {
                    Bonuses.Bonus.RANGE_STRENGTH(170)
                }
                requirements {
                    RANGED(61)
                }
            }
        }
    }
}
