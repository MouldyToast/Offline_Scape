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

class CustomItems : ItemDefinitionsScript() {

    fun Int.godBow(build: JSONItemDefinitions.() -> Unit = {}) = invoke {
    	this.weight = 1.5F
    	this.slot = EquipmentSlot.WEAPON.slot
    	equipment {
    		weapon {
    			isTwoHanded = true
    			attackSpeed = 5
    			accurateAnimation = 426
    			aggressiveAnimation = 426
    			controlledAnimation = 426
    			defensiveAnimation = 426
    			interfaceVarbit = 3
    			normalAttackDistance = 9
    			longAttackDistance = 9
    		}
    		bonuses {
    			Bonuses.Bonus.ATT_RANGED(128)

    			Bonuses.Bonus.RANGE_STRENGTH(106)
    		}
    		requirements {
    			AGILITY(70)
    			RANGED(80)
    		}
    	}
    	build()
    }

    fun Int.mageCape(build: JSONItemDefinitions.() -> Unit = {}) =
    	invoke(IMBUED_GUTHIX_CAPE) {
    		build()
    		tradable = false
    	}

    fun Int.polyporeStaff(build: JSONItemDefinitions.() -> Unit = {}) =
    	invoke(TOXIC_STAFF_OF_THE_DEAD, build)

    fun Int.key(build: JSONItemDefinitions.() -> Unit = {}) =
    	invoke(KEY, build)

    fun Int.whip() =
    	invoke(ABYSSAL_WHIP) {}

    fun Int.partyHat() =
    	invoke(WHITE_PARTYHAT) {}

    fun Int.donatorPin() = invoke(OLD_SCHOOL_BOND_UNTRADEABLE){
    	tradable = true
    }

    fun Int.abyssalWhip() = invoke(ABYSSAL_WHIP) {
    	equipment {
    		weapon {
    			attackSpeed = 4
    		}
    		bonuses {
    			Bonuses.Bonus.ATT_SLASH(82)
    			Bonuses.Bonus.STRENGTH(82)
    		}
    	}
    }

    init {
        OCCULT_NECKLACE(OCCULT_NECKLACE) {
        	equipment {
        		bonuses {
        			MAGIC_DAMAGE(5)
        		}
        	}
        }

        OCCULT_NECKLACE_OR(OCCULT_NECKLACE) {
        	equipment {
        		bonuses {
        			MAGIC_DAMAGE(5)
        		}
        	}
        }

        MALEDICTION_WARD(MALEDICTION_WARD) {
        	equipment {
        		bonuses {
        			MAGIC_DAMAGE(2)
        		}
        	}
        }

        MALEDICTION_WARD_12806(MALEDICTION_WARD) {
        	equipment {
        		bonuses {
        			MAGIC_DAMAGE(2)
        		}
        	}
        }


        ANCIENT_EYE(EYE_OF_NEWT) {}
        ANCIENT_BOOK_32004(MAGES_BOOK) {
        	equipment {
        		bonuses {
        			MAGIC_DAMAGE(2)
        		}
        	}
        }

        ARMADYL_SOUL_CRYSTAL {}
        ARMADYL_BOW.godBow()

        BANDOS_SOUL_CRYSTAL {}
        BANDOS_BOW.godBow()

        SARADOMIN_SOUL_CRYSTAL {}
        SARADOMIN_BOW.godBow()

        ZAMORAK_SOUL_CRYSTAL {}
        ZAMORAK_BOW.godBow()

        CHAOTIC_KITESHIELD {
        	equipment {
        		bonuses {
        			Bonuses.Bonus.ATT_STAB(8)
        			Bonuses.Bonus.ATT_SLASH(8)
        			Bonuses.Bonus.ATT_CRUSH(8)
        			Bonuses.Bonus.ATT_RANGED(0)
        			Bonuses.Bonus.ATT_MAGIC(15.unaryMinus())

        			Bonuses.Bonus.DEF_STAB(97)
        			Bonuses.Bonus.DEF_SLASH(87)
        			Bonuses.Bonus.DEF_CRUSH(81)
        			Bonuses.Bonus.DEF_RANGE(91)
        			Bonuses.Bonus.DEF_MAGIC(15.unaryMinus())

        			Bonuses.Bonus.STRENGTH(5)
        			Bonuses.Bonus.PRAYER(3)
        		}
        		requirements {
        			DEFENCE(80)
        		}
        		slot = EquipmentSlot.SHIELD.slot
        	}
        }

        FARSEER_KITESHIELD {
        	equipment {
        		bonuses {
        			Bonuses.Bonus.ATT_STAB(0)
        			Bonuses.Bonus.ATT_SLASH(0)
        			Bonuses.Bonus.ATT_CRUSH(0)
        			Bonuses.Bonus.ATT_RANGED(15.unaryMinus())
        			Bonuses.Bonus.ATT_MAGIC(15)

        			Bonuses.Bonus.DEF_STAB(55)
        			Bonuses.Bonus.DEF_SLASH(55)
        			Bonuses.Bonus.DEF_CRUSH(55)
        			Bonuses.Bonus.DEF_RANGE(40)
        			Bonuses.Bonus.DEF_MAGIC(15)

        			Bonuses.Bonus.MAGIC_DAMAGE(5)
        			Bonuses.Bonus.PRAYER(3)
        		}
        		requirements {
        			DEFENCE(80)
        			MAGIC(80)
        		}
        		slot = EquipmentSlot.SHIELD.slot
        	}
        }

        EAGLE_EYE_KITESHIELD {
        	equipment {
        		bonuses {
        			Bonuses.Bonus.ATT_STAB(10.unaryMinus())
        			Bonuses.Bonus.ATT_SLASH(10.unaryMinus())
        			Bonuses.Bonus.ATT_CRUSH(10.unaryMinus())
        			Bonuses.Bonus.ATT_RANGED(15)
        			Bonuses.Bonus.ATT_MAGIC(0)

        			Bonuses.Bonus.DEF_STAB(50)
        			Bonuses.Bonus.DEF_SLASH(50)
        			Bonuses.Bonus.DEF_CRUSH(70)
        			Bonuses.Bonus.DEF_RANGE(50)
        			Bonuses.Bonus.DEF_MAGIC(25)

        			Bonuses.Bonus.RANGE_STRENGTH(5)
        			Bonuses.Bonus.PRAYER(3)
        		}
        		requirements {
        			DEFENCE(80)
        			RANGED(80)
        		}
        		slot = EquipmentSlot.SHIELD.slot
        	}
        }

        DRAGON_KITE(DRAGONFIRE_SHIELD) {
        	equipment {
        		bonuses {
        			Bonuses.Bonus.ATT_STAB(13)
        			Bonuses.Bonus.ATT_SLASH(12)
        			Bonuses.Bonus.ATT_CRUSH(8)
        			Bonuses.Bonus.ATT_RANGED(8.unaryMinus())
        			Bonuses.Bonus.ATT_MAGIC(9.unaryMinus())

        			Bonuses.Bonus.DEF_STAB(85)
        			Bonuses.Bonus.DEF_SLASH(90)
        			Bonuses.Bonus.DEF_CRUSH(87)
        			Bonuses.Bonus.DEF_RANGE(87)
        			Bonuses.Bonus.DEF_MAGIC(15)

        			Bonuses.Bonus.STRENGTH(10)
        		}
        	}
        }

        ANCIENT_MEDALLION_32024(OCCULT_NECKLACE) {
        	equipment {
        		bonuses {
        			MAGIC_DAMAGE(12)
        		}
        	}
        }

        IMBUED_ANCIENT_CAPE.mageCape()
        IMBUED_ARMADYL_CAPE.mageCape()
        IMBUED_BANDOS_CAPE.mageCape()
        //ZAMORAK_MAGE_CAPE.mageCape()
        IMBUED_SEREN_CAPE.mageCape()

        DEATH_CAPE(FIRE_CAPE) {
        	equipment {
        		bonuses {
        			Bonuses.Bonus.STRENGTH(6)
        		}
        	}
        }

        GAUNTLET_SLAYER_HELM(SLAYER_HELMET_I) {}
        CORRUPTED_GAUNTLET_SLAYER_HELM(SLAYER_HELMET_I) {}

        POLYPORE_SPORES(MUSHROOM_SPORE) {}
        POLYPORE_STAFF_DEG.polyporeStaff()
        POLYPORE_STAFF.polyporeStaff()

        BRONZE_KEY.key()
        SILVER_KEY.key()
        GOLD_KEY.key()
        PLATINUM_KEY.key()
        DIAMOND_KEY.key()
        NR_TABLET.invoke(VARROCK_TELEPORT) {}

        LIME_WHIP.invoke(ABYSSAL_WHIP) {
        	equipment {
        		weapon {
        			attackSpeed = 3
        		}
        	}
        }

        ELEMENTAL_WHIP.invoke(ABYSSAL_WHIP) {
        	equipment {
        		weapon {
        			attackSpeed = 4
        		}
        		bonuses {
        			Bonuses.Bonus.ATT_SLASH(86)
        			Bonuses.Bonus.STRENGTH(86)
        		}
        	}
        }

        BARROWS_WHIP.invoke(ABYSSAL_WHIP) {
        	equipment {
        		weapon {
        			attackSpeed = 4
        		}
        		bonuses {
        			Bonuses.Bonus.ATT_SLASH(85)
        			Bonuses.Bonus.STRENGTH(85)
        		}
        	}
        }

        DRAGON_WHIP.invoke(ABYSSAL_WHIP) {
        	equipment {
        		weapon {
        			attackSpeed = 4
        		}
        		bonuses {
        			Bonuses.Bonus.ATT_SLASH(84)
        			Bonuses.Bonus.STRENGTH(84)
        		}
        	}
        }

        RUNE_WHIP.abyssalWhip()
        ADAMANT_WHIP.abyssalWhip()
        MITHRIL_WHIP.abyssalWhip()
        BLACK_WHIP.abyssalWhip()
        WHITE_WHIP.abyssalWhip()
        PINK_WHIP.abyssalWhip()
        GREEN_WHIP.abyssalWhip()
        IRON_WHIP.abyssalWhip()
        STEEL_WHIP.abyssalWhip()

        LIME_WHIP_SPECIAL.invoke(ABYSSAL_TENTACLE) {
        	tradable = false
        	equipment {
        		weapon {
        			attackSpeed = 4
        		}
        	}
        }
        LAVA_WHIP.invoke(ABYSSAL_WHIP) {
        	equipment {
        		weapon {
        			attackSpeed = 4
        		}
        	}
        }

        PINK_PARTYHAT.partyHat()
        ORANGE_PARTYHAT.partyHat()
        NEAR_REALITY_PARTY_HAT.partyHat()

        DONATOR_PIN_10.donatorPin()
        DONATOR_PIN_25.donatorPin()
        DONATOR_PIN_50.donatorPin()
        DONATOR_PIN_100.donatorPin()



        OSNR_MYSTERY_BOX.invoke(MYSTERY_BOX) {}

        BLUE_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
        BLUE_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
        BLUE_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
        BLUE_ANKOU_MASK.invoke(ANKOU_MASK) {}
        BLUE_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
        GREEN_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
        GREEN_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
        GREEN_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
        GREEN_ANKOU_MASK.invoke(ANKOU_MASK) {}
        GREEN_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
        GOLD_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
        GOLD_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
        GOLD_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
        GOLD_ANKOU_MASK.invoke(ANKOU_MASK) {}
        GOLD_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
        WHITE_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
        WHITE_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
        WHITE_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
        WHITE_ANKOU_MASK.invoke(ANKOU_MASK) {}
        WHITE_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
        BLACK_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
        BLACK_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
        BLACK_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
        BLACK_ANKOU_MASK.invoke(ANKOU_MASK) {}
        BLACK_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
        PURPLE_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
        PURPLE_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
        PURPLE_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
        PURPLE_ANKOU_MASK.invoke(ANKOU_MASK) {}
        PURPLE_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }

        BLUE_TWISTED_BOW.invoke(TWISTED_BOW) {}
        PURPLE_TWISTED_BOW.invoke(TWISTED_BOW) {}
        RED_TWISTED_BOW.invoke(TWISTED_BOW) {}
        WHITE_TWISTED_BOW.invoke(TWISTED_BOW) {}
        DIVINE_KIT.invoke(ANGUISH_ORNAMENT_KIT) {}
        ELYSIAN_SPIRIT_SHIELD_OR.invoke(ELYSIAN_SPIRIT_SHIELD) {}
        32216.invoke(ANKOU_SOCKS) {}
        32217.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
        32218.invoke(ANKOU_GLOVES) {}
        32219.invoke(CHRISTMAS_SCYTHE) {}
        32220.invoke(ANKOUS_LEGGINGS) {}
        32221.invoke(ANKOU_MASK) {}
        32222.invoke(WOODEN_SHIELD) {}
        32223.invoke(WOODEN_SHIELD) {}

        32224.invoke(RED_PARTYHAT) {}
        32225.invoke(SANTA_HAT) {}
        32226.invoke(RED_HALLOWEEN_MASK) {}
        32415.invoke(RED_HALLOWEEN_MASK) {}
        32417.invoke(RED_HALLOWEEN_MASK) {}
        32419.invoke(RED_HALLOWEEN_MASK) {}

        32221.invoke(HELM_OF_NEITIZNOT) {}
        32217.invoke(BANDOS_CHESTPLATE) {}
        32220.invoke(BANDOS_TASSETS) {}
        32223.invoke(DRAGONFIRE_SHIELD) {}
        32222.invoke(AVERNIC_DEFENDER) {}
        32218.invoke(BARROWS_GLOVES) {}
        32216.invoke(DRAGON_BOOTS) {}
        32219 {
        	tradable = true
        	slot = EquipmentSlot.WEAPON.slot
        	equipment {
        		weapon {
        			attackSpeed = 4
        			accurateAnimation = 390
        			aggressiveAnimation = 390
        			controlledAnimation = 412
        			defensiveAnimation = 390
        			interfaceVarbit = 9
        		}
        		bonuses {
        			Bonuses.Bonus.ATT_STAB(55)
        			Bonuses.Bonus.ATT_SLASH(94)

        			Bonuses.Bonus.STRENGTH(89)
        		}
        		requirements {
        			ATTACK(80)
        		}
        	}
        }
        CARROT_SPEAR.invoke(BRONZE_SPEAR) {}
        CARROT_CROWN.invoke(ROYAL_CROWN) {}
        BROKEN_EGG_SHELLS.invoke(BROKEN_CRAB_SHELL) {}
        EASTER_MYSTERY_BOX.invoke(MYSTERY_BOX) {}
        EASTER_CARDS.invoke(MYSTIC_CARDS) {}

        GHOSTLY_PARTYHAT.invoke(RED_PARTYHAT) {}
        DEMONHORN_NECKLACE.invoke(AMULET_OF_FURY) {
        	equipment {
        		bonuses {
        			Bonuses.Bonus.ATT_SLASH(-30)
        			Bonuses.Bonus.ATT_STAB(-30)
        			Bonuses.Bonus.ATT_CRUSH(-30)
        			Bonuses.Bonus.ATT_MAGIC(-20)
        			Bonuses.Bonus.ATT_RANGED(-20)

        			Bonuses.Bonus.DEF_STAB(-30)
        			Bonuses.Bonus.DEF_SLASH(-30)
        			Bonuses.Bonus.DEF_CRUSH(-30)
        			Bonuses.Bonus.DEF_MAGIC(-20)
        			Bonuses.Bonus.DEF_RANGE(-20)

        			Bonuses.Bonus.STRENGTH(20)
        			Bonuses.Bonus.RANGE_STRENGTH(10)
        			Bonuses.Bonus.MAGIC_DAMAGE(10)
        		}
        	}
        }
        ELITE_BLACK_HELM.invoke(BLACK_FULL_HELM) {}
        ELITE_BLACK_PLATEBODY.invoke(BLACK_PLATEBODY) {}
        ELITE_BLACK_PLATELEGS.invoke(BLACK_PLATELEGS) {}

        BANDOS_CHESTPLATE_OR.invoke(BANDOS_CHESTPLATE) {}
        BANDOS_TASSETS_OR.invoke(BANDOS_TASSETS) {}
        BANDOS_ORNAMENT_KIT.invoke(ANGUISH_ORNAMENT_KIT) {}
    }
}
