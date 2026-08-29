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

class PrimalsItems : ItemDefinitionsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.14.2025
         */

        PRIMAL_FULL_HELM {
            slot = EquipmentSlot.HELMET.slot
            equipment(FULL_MASK) {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(3.unaryMinus())
                    Bonuses.Bonus.ATT_STAB(3.unaryMinus())
                    Bonuses.Bonus.ATT_CRUSH(3.unaryMinus())
                    Bonuses.Bonus.ATT_MAGIC(15.unaryMinus())
                    Bonuses.Bonus.ATT_RANGED(15.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(67)
                    Bonuses.Bonus.DEF_SLASH(72)
                    Bonuses.Bonus.DEF_CRUSH(46)
                    Bonuses.Bonus.DEF_MAGIC(27.unaryMinus())
                    Bonuses.Bonus.DEF_RANGE(67)

                    Bonuses.Bonus.PRAYER(1)
                    Bonuses.Bonus.STRENGTH(10)
                }
                requirements {
                    DEFENCE(99)
                }
            }
        }

        PRIMAL_PLATEBODY {
            slot = EquipmentSlot.PLATE.slot
            equipment(FULL_BODY) {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(10.unaryMinus())
                    Bonuses.Bonus.ATT_STAB(10.unaryMinus())
                    Bonuses.Bonus.ATT_CRUSH(10.unaryMinus())
                    Bonuses.Bonus.ATT_MAGIC(20.unaryMinus())
                    Bonuses.Bonus.ATT_RANGED(20.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(127)
                    Bonuses.Bonus.DEF_SLASH(130)
                    Bonuses.Bonus.DEF_CRUSH(93)
                    Bonuses.Bonus.DEF_MAGIC(30.unaryMinus())
                    Bonuses.Bonus.DEF_RANGE(147)

                    Bonuses.Bonus.PRAYER(1)
                    Bonuses.Bonus.STRENGTH(8)
                }
                requirements {
                    DEFENCE(99)
                }
            }
        }

        PRIMAL_CHAINBODY {
            slot = EquipmentSlot.PLATE.slot
            equipment(FULL_BODY) {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(7.unaryMinus())
                    Bonuses.Bonus.ATT_STAB(7.unaryMinus())
                    Bonuses.Bonus.ATT_CRUSH(7.unaryMinus())
                    Bonuses.Bonus.ATT_MAGIC(20.unaryMinus())
                    Bonuses.Bonus.ATT_RANGED(20.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(113)
                    Bonuses.Bonus.DEF_SLASH(120)
                    Bonuses.Bonus.DEF_CRUSH(83)
                    Bonuses.Bonus.DEF_MAGIC(20.unaryMinus())
                    Bonuses.Bonus.DEF_RANGE(127)

                    Bonuses.Bonus.PRAYER(1)
                    Bonuses.Bonus.STRENGTH(4)
                }
                requirements {
                    DEFENCE(99)
                }
            }
        }


        PRIMAL_PLATELEGS {
            slot = EquipmentSlot.LEGS.slot
            equipment(FULL_LEGS) {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(7.unaryMinus())
                    Bonuses.Bonus.ATT_STAB(7.unaryMinus())
                    Bonuses.Bonus.ATT_CRUSH(7.unaryMinus())
                    Bonuses.Bonus.ATT_MAGIC(20.unaryMinus())
                    Bonuses.Bonus.ATT_RANGED(20.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(103)
                    Bonuses.Bonus.DEF_SLASH(89)
                    Bonuses.Bonus.DEF_CRUSH(55)
                    Bonuses.Bonus.DEF_MAGIC(25.unaryMinus())
                    Bonuses.Bonus.DEF_RANGE(112)

                    Bonuses.Bonus.PRAYER(1)
                    Bonuses.Bonus.STRENGTH(7)
                }
                requirements {
                    DEFENCE(99)
                }
            }
        }

        PRIMAL_PLATESKIRT {
            slot = EquipmentSlot.LEGS.slot
            equipment(FULL_LEGS) {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(7.unaryMinus())
                    Bonuses.Bonus.ATT_STAB(7.unaryMinus())
                    Bonuses.Bonus.ATT_CRUSH(7.unaryMinus())
                    Bonuses.Bonus.ATT_MAGIC(20.unaryMinus())
                    Bonuses.Bonus.ATT_RANGED(20.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(103)
                    Bonuses.Bonus.DEF_SLASH(89)
                    Bonuses.Bonus.DEF_CRUSH(55)
                    Bonuses.Bonus.DEF_MAGIC(25.unaryMinus())
                    Bonuses.Bonus.DEF_RANGE(112)

                    Bonuses.Bonus.PRAYER(1)
                    Bonuses.Bonus.STRENGTH(7)
                }
                requirements {
                    DEFENCE(99)
                }
            }
        }

        PRIMAL_GAUNTLETS {
            slot = EquipmentSlot.HANDS.slot
            equipment {
                bonuses {
                    Bonuses.Bonus.ATT_SLASH(3.unaryMinus())
                    Bonuses.Bonus.ATT_STAB(3.unaryMinus())
                    Bonuses.Bonus.ATT_CRUSH(3.unaryMinus())
                    Bonuses.Bonus.ATT_MAGIC(20.unaryMinus())
                    Bonuses.Bonus.ATT_RANGED(20.unaryMinus())

                    Bonuses.Bonus.DEF_STAB(20)
                    Bonuses.Bonus.DEF_SLASH(20)
                    Bonuses.Bonus.DEF_CRUSH(6)
                    Bonuses.Bonus.DEF_MAGIC(6.unaryMinus())
                    Bonuses.Bonus.DEF_RANGE(20)
                }
                requirements {
                    DEFENCE(99)
                }
            }
        }

        PRIMAL_BOOTS {
            slot = EquipmentSlot.BOOTS.slot
            equipment {
                bonuses {
                    Bonuses.Bonus.DEF_STAB(44)
                    Bonuses.Bonus.DEF_SLASH(44)
                    Bonuses.Bonus.DEF_CRUSH(14)
                    Bonuses.Bonus.DEF_MAGIC(14.unaryMinus())
                    Bonuses.Bonus.DEF_RANGE(44)

                    Bonuses.Bonus.STRENGTH(8)
                    Bonuses.Bonus.PRAYER(5)
                }
                requirements {
                    DEFENCE(99)
                }
            }
        }
    }
}
