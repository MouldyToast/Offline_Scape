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

class LordMarshallItems : ItemDefinitionsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.14.2025
         */

        LORD_MARSHALL_CAP {
            slot = EquipmentSlot.HELMET.slot
            equipment {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(0)
                    Bonuses.Bonus.ATT_STAB(0)
                    Bonuses.Bonus.ATT_CRUSH(0)
                    Bonuses.Bonus.ATT_MAGIC(16)
                    Bonuses.Bonus.ATT_RANGED(5.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(25)
                    Bonuses.Bonus.DEF_SLASH(20)
                    Bonuses.Bonus.DEF_CRUSH(28)
                    Bonuses.Bonus.DEF_MAGIC(16)
                    Bonuses.Bonus.DEF_RANGE(0)

                    Bonuses.Bonus.MAGIC_DAMAGE(1)
                }
                requirements {
                    MAGIC(70)
                    DEFENCE(70)
                }
            }
        }

        LORD_MARSHALL_TOP {
            slot = EquipmentSlot.PLATE.slot
            equipment(FULL_BODY) {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(0)
                    Bonuses.Bonus.ATT_STAB(0)
                    Bonuses.Bonus.ATT_CRUSH(0)
                    Bonuses.Bonus.ATT_MAGIC(33)
                    Bonuses.Bonus.ATT_RANGED(9.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(50)
                    Bonuses.Bonus.DEF_SLASH(40)
                    Bonuses.Bonus.DEF_CRUSH(59)
                    Bonuses.Bonus.DEF_MAGIC(31)
                    Bonuses.Bonus.DEF_RANGE(0)

                    Bonuses.Bonus.MAGIC_DAMAGE(1)
                }
                requirements {
                    MAGIC(70)
                    DEFENCE(70)
                }
            }
        }

        LORD_MARSHALL_TROUSERS {
            slot = EquipmentSlot.LEGS.slot
            equipment(FULL_LEGS) {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(0)
                    Bonuses.Bonus.ATT_STAB(0)
                    Bonuses.Bonus.ATT_CRUSH(0)
                    Bonuses.Bonus.ATT_MAGIC(16)
                    Bonuses.Bonus.ATT_RANGED(5.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(25)
                    Bonuses.Bonus.DEF_SLASH(20)
                    Bonuses.Bonus.DEF_CRUSH(28)
                    Bonuses.Bonus.DEF_MAGIC(16)
                    Bonuses.Bonus.DEF_RANGE(0)

                    Bonuses.Bonus.MAGIC_DAMAGE(1)
                }
                requirements {
                    MAGIC(70)
                    DEFENCE(70)
                }
            }
        }

        LORD_MARSHALL_BOOTS {
            slot = EquipmentSlot.BOOTS.slot
            equipment {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(0)
                    Bonuses.Bonus.ATT_STAB(0)
                    Bonuses.Bonus.ATT_CRUSH(0)
                    Bonuses.Bonus.ATT_MAGIC(8)
                    Bonuses.Bonus.ATT_RANGED(3.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(8)
                    Bonuses.Bonus.DEF_SLASH(8)
                    Bonuses.Bonus.DEF_CRUSH(8)
                    Bonuses.Bonus.DEF_MAGIC(8)
                    Bonuses.Bonus.DEF_RANGE(0)

                    Bonuses.Bonus.MAGIC_DAMAGE(1)
                }
                requirements {
                    MAGIC(70)
                    DEFENCE(70)
                }
            }
        }

        LORD_MARSHALL_GLOVES {
            slot = EquipmentSlot.HANDS.slot
            equipment {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(0)
                    Bonuses.Bonus.ATT_STAB(0)
                    Bonuses.Bonus.ATT_CRUSH(0)
                    Bonuses.Bonus.ATT_MAGIC(8)
                    Bonuses.Bonus.ATT_RANGED(3.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(8)
                    Bonuses.Bonus.DEF_SLASH(8)
                    Bonuses.Bonus.DEF_CRUSH(8)
                    Bonuses.Bonus.DEF_MAGIC(8)
                    Bonuses.Bonus.DEF_RANGE(0)

                    Bonuses.Bonus.MAGIC_DAMAGE(1)
                }
                requirements {
                    MAGIC(70)
                    DEFENCE(70)
                }
            }
        }
    }
}
