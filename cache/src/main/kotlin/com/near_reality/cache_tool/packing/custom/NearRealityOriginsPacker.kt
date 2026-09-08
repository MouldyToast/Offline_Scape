package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import com.near_reality.game.item.CustomNpcId.*
import com.zenyte.game.item.ids.*
import mgi.types.config.npcs.NPCDefinitions
import net.runelite.api.NpcID
import net.runelite.api.NpcID.*

object NearRealityOriginsPacker {

    @JvmStatic fun pack() {
        packStrykewyrms()
        packBalanceElementals()
        packBork()
        packPlaneFreezer()
        packNomad()
        packPhoenix()
        packSlashBash()
        packBarrelchest()
        packPets()
        packWildyImp()
        packCustoms()
        packBarrowsCopies()
        packWildMole()
    }

    private fun packWildMole() {
        NPCDefinitions.get(GIANT_MOLE_6499).clone().toBuilder()
            .id(WILD_MOLE)
            .combatLevel(370)
            .name("Wild mole")
            .models(intArrayOf("origins_wildy_mole".model()))
            .rescale(140)
            .packNew()

        NPCDefinitions.get(BABY_MOLE).clone().toBuilder()
            .id(BABY_WILD_MOLE)
            .makePet()
            .name("Baby wild mole")
            .models(intArrayOf("origins_wildy_mole".model()))
            .rescale(30)
            .packNew()

        PET_BABY_WILD_MOLE.newItem()
            .models("origins_wildy_mole_pet_inv".model())
            .name("Baby wild mole")
            .petOps()
            .rotateInv(176, 242, 0)
            .zoom(2452)
            .offsetInv(11, -37)
            .packNew()
    }

    @JvmStatic fun packBarrowsCopies() {
        NPCDefinitions.get(AHRIM_THE_BLIGHTED).clone().toBuilder()
            .id(DI_AHRIM_THE_BLIGHTED)
            .packNew()

        NPCDefinitions.get(DHAROK_THE_WRETCHED).clone().toBuilder()
            .id(DI_DHAROK_THE_WRETCHED)
            .packNew()

        NPCDefinitions.get(GUTHAN_THE_INFESTED).clone().toBuilder()
            .id(DI_GUTHAN_THE_INFESTED)
            .packNew()

        NPCDefinitions.get(KARIL_THE_TAINTED).clone().toBuilder()
            .id(DI_KARIL_THE_TAINTED)
            .packNew()

        NPCDefinitions.get(TORAG_THE_CORRUPTED).clone().toBuilder()
            .id(DI_TORAG_THE_CORRUPTED)
            .packNew()

        NPCDefinitions.get(VERAC_THE_DEFILED).clone().toBuilder()
            .id(DI_VERAC_THE_DEFILED)
            .packNew()
    }

    @JvmStatic fun packWildyImp() {
        assetsBase("assets/origins/wildy_imp/") {
            defaultModels()
        }
        NPCDefinitions.builder()
            .initialize(16124, 1, "wildy_imp".model())
            .optionList("Catch")
            .named("Wildy Imp")
            .makePassive(clip = false)
            .rescale(200)
            .movementAnimationsWithIdle(6614, 6613)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16125, 1, "wildy_imp".model())
            .makePet()
            .named("Lil' Wildy Imp")
            .rescale(150)
            .movementAnimationsWithIdle(6614, 6613)
            .packNew()
    }

    @JvmStatic fun packPets() {
        assetsBase("assets/origins/pets/") {
            defaultModels()
        }

        NPCDefinitions.builder()
            .initialize(16090, 1, 60342, 60343)
            .makePet()
            .named("Baby jungle strykewyrm")
            .movementAnimations(25001)
            .rescale(35)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16091, 1, 60340, 60341)
            .makePet()
            .named("Baby ice strykewyrm")
            .movementAnimations(25001)
            .rescale(35)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16092, 1, 60338, 60339)
            .makePet()
            .named("Baby desert strykewyrm")
            .movementAnimations(25001)
            .rescale(35)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16093, 1, 60360)
            .makePet()
            .named("Baby aged barrelchest")
            .movementAnimationsWithIdle(25047, 25048)
            .rescale(35)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16094, 1, 60359)
            .makePet()
            .named("Lil' slash bash")
            .movementAnimationsWithIdle(25042, 25043)
            .rescale(35)
            .packNew()

        NPCDefinitions.get(NpcID.POSTIE_PETE).clone().toBuilder()
            .id(16095).size(1)
            .makePet()
            .movementAnimationsWithIdle(3947, 3948)
            .packNew()

        NPCDefinitions.get(NpcID.IMP_DEFENDER).clone().toBuilder()
            .id(16096).size(1)
            .makePet()
            .named("Imp")
            .makeRemnPetWithOps(op4 = "Toggle")
            .movementAnimationsWithIdle(168, 171)
            .packNew()

        NPCDefinitions.get(5240).clone().toBuilder()
            .id(16097).size(1)
            .makePet()
            .named("Toucan Sam")
            .movementAnimationsWithIdle(6774, 6772)
            .packNew()

        NPCDefinitions.get(834).clone().toBuilder()
            .id(16098).size(1)
            .makePet()
            .named("King penguin")
            .movementAnimationsWithIdle(5666, 5668)
            .packNew()

        NPCDefinitions.get(1873).clone().toBuilder()
            .id(16099).size(1)
            .makePet()
            .movementAnimationsWithIdle(3346, 3345)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16100, 1, 29266)
            .makePet()
            .rescale(85)
            .named("Shadow Warrior")
            .movementAnimationsWithIdle(8527, 8526)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16101, 1, 29267)
            .makePet()
            .rescale(85)
            .named("Shadow Ranger")
            .movementAnimationsWithIdle(8527, 8526)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16102, 1, 29268)
            .makePet()
            .rescale(85)
            .named("Shadow Wizard")
            .movementAnimationsWithIdle(8527, 8526)
            .packNew()

        NPCDefinitions.get(6723).clone().toBuilder()
            .id(16103).size(1)
            .makeRemnPetWithOps("Restore")
            .rescale(35)
            .named("Healer Death Spawn")
            .movementAnimations(1539)
            .packNew()

        NPCDefinitions.get(6716).clone().toBuilder()
            .id(16104).size(1)
            .makeRemnPetWithOps("Restore")
            .rescale(35)
            .named("Holy Death Spawn")
            .movementAnimations(1539)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16105, 1, 38605)
            .makePet()
            .rescale(65)
            .named("Seren")
            .movementAnimations(8372)
            .packNew()

        NPCDefinitions.get(8709).clone().toBuilder()
            .id(16106).size(1)
            .makePet()
            .rescale(60)
            .named("Corrupt Beast")
            .movementAnimationsWithIdle(5615, 5616)
            .packNew()

        NPCDefinitions.get(763).clone().toBuilder()
            .id(16107).size(1)
            .makePet()
            .rescale(30)
            .named("Roc")
            .movementAnimationsWithIdle(5022, 5021)
            .packNew()

        NPCDefinitions.get(7668).clone().toBuilder()
            .id(16108).size(2)
            .makeRemnPetWithOps("Restore", "Toggle")
            .rescale(90)
            .movementAnimationsWithIdle(7016, 7017)
            .named("Kratos")
            .packNew()

        NPCDefinitions.builder()
            .initialize(16109, 1, "dark_postie_pete".model())
            .makePet()
            .named("Primal Postie Pete")
            .movementAnimationsWithIdle(3947, 3948)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16110, 1, "dark_imp".model())
            .makeRemnPetWithOps(op4 = "Toggle")
            .named("Primal imp")
            .movementAnimationsWithIdle(168, 171)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16111, 1, "dark_toucan_body".model(), "dark_toucan_underlay".model())
            .makePet()
            .named("Primal toucan")
            .movementAnimationsWithIdle(6774, 6772)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16112, 1, "dark_king_penguin".model())
            .makePet()
            .named("Primal king penguin")
            .rescale(65)
            .movementAnimationsWithIdle(5666, 5668)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16113, 1, "dark_kklik_body_a".model(), "dark_kklik_body_b".model(), "dark_kklik_underlay".model())
            .makePet()
            .named("Primal k'klik")
            .movementAnimationsWithIdle(3346, 3346)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16114, 1, "dark_shadow_warrior".model())
            .makePet()
            .named("Primal shadow warrior")
            .movementAnimationsWithIdle(8527, 8526)
            .rescale(85)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16115, 1, "dark_shadow_archer".model())
            .makePet()
            .named("Primal shadow archer")
            .movementAnimationsWithIdle(8527, 8526)
            .rescale(85)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16116, 1, "dark_shadow_wizard".model())
            .makePet()
            .named("Primal shadow wizard")
            .movementAnimationsWithIdle(8527, 8526)
            .rescale(85)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16117, 1,
                "dark_healer_death_a".model(),
                "dark_healer_death_b".model(),
                "dark_healer_death_c".model(),
                "dark_healer_death_d".model(),
                "dark_healer_death_e".model(),
                "dark_healer_death_f".model(),
                "dark_healer_death_g".model()
            )
            .makeRemnPetWithOps("Restore")
            .named("Primal healer death spawn")
            .movementAnimations(1539)
            .rescale(65)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16118, 1,
                "dark_holy_death_a".model(),
                "dark_holy_death_b".model(),
                "dark_holy_death_c".model(),
                "dark_holy_death_d".model(),
                "dark_holy_death_e".model(),
                "dark_holy_death_f".model(),
                "dark_holy_death_g".model()
            )
            .makeRemnPetWithOps("Restore")
            .named("Primal holy death spawn")
            .movementAnimations(1539)
            .rescale(65)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16119, 1, "dark_seren".model())
            .makePet()
            .named("Dark seren")
            .rescale(65)
            .movementAnimations(8372)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16120, 1, "dark_corrupt_beast".model())
            .makePet()
            .named("Primal corrupt beast")
            .rescale(60)
            .movementAnimationsWithIdle(5615, 5616)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16121, 1, "dark_roc_body".model(), "dark_roc_underlay".model())
            .makePet()
            .named("Primal roc")
            .rescale(30)
            .movementAnimationsWithIdle(5022, 5021)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16122, 2, "dark_kratos_body".model(), "dark_kratos_underlay".model(), "dark_kratos_wield".model())
            .makeRemnPetWithOps("Restore", "Toggle")
            .named("Primal kratos")
            .rescale(90)
            .movementAnimationsWithIdle(7016, 7017)
            .packNew()

        NPCDefinitions.builder()
            .initialize(16123, 2, "fissile_kratos_body".model(), "fissile_kratos_underlay".model(), "fissile_kratos_wield".model())
            .makeRemnPetWithOps("Restore", "Toggle")
            .named("Fissile kratos")
            .rescale(90)
            .movementAnimationsWithIdle(7016, 7017)
            .packNew()

        NPCDefinitions.get(13565).clone().toBuilder()
            .id(DRIFTER).size(1)
            .makePassive(true)
            .named("Drifter")
            .options(arrayOf("Talk-to", "Primal Info", "Pet Info", "Upgrade Info", null))
            .packNew()

    }

    @JvmStatic fun packBarrelchest() {
        assetsBase("assets/origins/barrelchest/") {
            defaultSkeletons()
            defaultSkins()
            defaultSequences()
            defaultModels()
        }
        NPCDefinitions.builder()
            .initialize(16089, 3, 60360)
            .combatTarget(170)
            .combatOptions()
            .movementAnimationsWithIdle(25047, 25048)
            .named("Aged Barrelchest")
            .rescale(128)
            .packNew()
    }
    @JvmStatic fun packSlashBash() {
        assetsBase("assets/origins/slash_bash/") {
            defaultSkeletons()
            defaultSkins()
            defaultSequences()
            defaultModels()
        }
        NPCDefinitions.builder()
            .initialize(16088, 3, 60359)
            .combatTarget(198)
            .combatOptions()
            .movementAnimationsWithIdle(25042, 25043)
            .named("Slash Bash")
            .rescale(128)
            .ambience(25)
            .packNew()
    }

    @JvmStatic fun packPhoenix() {
        assetsBase("assets/origins/phoenix/") {
            defaultSequences()
            defaultSkeletons()
            defaultSkins()
            defaultModels()
        }
        NPCDefinitions.builder()
            .initialize(16087, 4, 60358)
            .combatTarget(235)
            .combatOptions()
            .movementAnimationsWithIdle(25038, 25037)
            .named("Phoenix")
            .rescale(128)
            .ambience(32)
            .contrast(375)
            .packNew()
    }

    @JvmStatic fun packNomad() {
        assetsBase("assets/origins/nomad/") {
            defaultSequences()
            defaultSkeletons()
            defaultSkins()
            defaultModels()
            defaultGraphics()
        }

        NPCDefinitions.builder()
            .initialize(16086, 2, 60353)
            .combatTarget(699)
            .combatOptions()
            .movementAnimationsWithIdle(25032, 25031)
            .named("Nomad")
            .rescale(128)
            .ambience(10)
            .contrast(250)
            .packNew()
    }

    @JvmStatic fun packPlaneFreezer() {
        assetsBase("assets/origins/plane_freezer/") {
            defaultSequences()
            defaultSkeletons()
            defaultSkins()
            defaultModels()
            defaultGraphics()
        }

        NPCDefinitions.builder()
            .initialize(16085, 2, 60350)
            .combatTarget(223)
            .combatOptions()
            .movementAnimationsWithIdle(25020, 25021)
            .named("Plane-freezer Lakhrahnaz")
            .rescale(128)
            .packNew()
    }

    @JvmStatic fun packBork() {
        assetsBase("assets/origins/bork/") {
            defaultSkeletons()
            defaultSequences()
            defaultSkins()
            defaultModels()
        }

        NPCDefinitions().toBuilder()
            .initialize(16084, 3, 60348, 60349)
            .combatTarget(267)
            .combatOptions()
            .movementAnimationsWithIdle(25015, 25016)
            .named("Bork")
            .rescale(128)
            .packNew()
    }


    @JvmStatic fun packStrykewyrms() {
        assetsBase("assets/origins/strykewyrms/") {
            defaultSequences()
            defaultSkeletons()
            defaultSkins()
            defaultModels()
        }

        NPCDefinitions().toBuilder()
            .initialize(16077, 3, 60340, 60341)
            .combatTarget(210)
            .movementAnimations(25001)
            .combatOptions()
            .named("Ice strykewyrm")
            .rescale(128)
            .ambience(20)
            .contrast(500)
            .build().pack()

        NPCDefinitions().toBuilder()
            .initialize(16078, 3, 60342, 60343)
            .combatTarget(110)
            .movementAnimations(25001)
            .combatOptions()
            .named("Jungle strykewyrm")
            .rescale(128)
            .ambience(20)
            .contrast(500)
            .build().pack()

        NPCDefinitions().toBuilder()
            .initialize(16079, 3, 60338, 60339)
            .combatTarget(130)
            .movementAnimations(25001)
            .combatOptions()
            .named("Desert strykewyrm")
            .rescale(128)
            .ambience(20)
            .contrast(500)
            .build().pack()

        NPCDefinitions().toBuilder()
            .initialize(16080, 5, 60344, 60345)
            .combatTarget(382)
            .movementAnimations(25001)
            .combatOptions()
            .named("WildyWyrm")
            .rescale(256)
            .ambience(20)
            .contrast(100)
            .build().pack()
    }

    @JvmStatic fun packBalanceElementals() {
        assetsBase("assets/origins/balance_elementals/") {
            defaultSequences()
            defaultSkeletons()
            defaultSkins()
            defaultModels()
        }

        NPCDefinitions().toBuilder()
            .initialize(16081, 3, 60346)
            .combatTarget(454)
            .movementAnimationsWithIdle(25005, 25006)
            .combatOptions()
            .named("Balance Elemental")
            .rescale(128)
            .build().pack()

        NPCDefinitions().toBuilder()
            .initialize(16082, 3, 60346)
            .combatTarget(454)
            .movementAnimationsWithIdle(25009, 25010)
            .combatOptions()
            .named("Balance Elemental")
            .rescale(128)
            .build().pack()

        NPCDefinitions().toBuilder()
            .initialize(16083, 3, 60346)
            .combatTarget(454)
            .movementAnimationsWithIdle(25005, 25006)
            .combatOptions()
            .named("Balance Elemental")
            .rescale(128)
            .build().pack()

        NPCDefinitions().toBuilder()
            .initialize(16126, 1, 60346)
            .makePet()
            .movementAnimationsWithIdle(25005, 25006)
            .named("Off-balance elemental")
            .rescale(35)
            .build().pack()
    }

    @JvmStatic fun packCustoms() {
        assetsBase("assets/origins/customs/") {
            defaultModels()
            defaultSequences()
            defaultSkeletons()
            defaultGraphics()
            defaultSkins()
        }

        32884.newItem()
            .named("Lord marshall cap")
            .models(
                inventory = "origins_lord_m_cap_inv".model(),
                primaryMale = "origins_lord_m_cap_eq_m".model(),
                primaryFemale = "origins_lord_m_cap_eq_f".model(),
            )
            .rotateInv(184, 225, 0)
            .offsetInv(-1, -5)
            .equipmentOps()
            .zoom(919)
            .tradable()
            .createPlaceholder(32885)
            .packNew()

        32886.newItem()
            .named("Lord marshall top")
            .models(
                inventory = "origins_lord_m_top_inv".model(),
                primaryMale = "origins_lord_m_top_eq_m".model(),
                primaryFemale = "origins_lord_m_top_eq_f".model(),
            )
            .rotateInv(526, 0, 0)
            .offsetInv(0, 4)
            .equipmentOps()
            .zoom(1373)
            .tradable()
            .createPlaceholder(32887)
            .packNew()

        32888.newItem()
            .named("Lord marshall trousers")
            .models(
                inventory = "origins_lord_m_legs_inv".model(),
                primaryMale = "origins_lord_m_legs_eq_m".model(),
                primaryFemale = "origins_lord_m_legs_eq_f".model(),
            )
            .rotateInv(444, 0, 0)
            .offsetInv(1, 5)
            .equipmentOps()
            .zoom(1697)
            .tradable()
            .createPlaceholder(32889)
            .packNew()

        32890.newItem()
            .named("Lord marshall boots")
            .models(
                inventory = "origins_lord_m_boots_inv".model(),
                primaryMale = "origins_lord_m_boots_eq_m".model(),
                primaryFemale = "origins_lord_m_boots_eq_f".model(),
            )
            .rotateInv(207, 194, 0)
            .offsetInv(4, -14)
            .equipmentOps()
            .zoom(810)
            .tradable()
            .createPlaceholder(32891)
            .packNew()

        32892.newItem()
            .named("Lord marshall gloves")
            .models(
                inventory = "origins_lord_m_gloves_inv".model(),
                primaryMale = "origins_lord_m_gloves_eq_m".model(),
                primaryFemale = "origins_lord_m_gloves_eq_f".model(),
            )
            .rotateInv(420, 828, 0)
            .offsetInv(0, 1)
            .equipmentOps()
            .zoom(779)
            .tradable()
            .createPlaceholder(32893)
            .packNew()

        32894.newItem()
            .named("Ghostly partyhat")
            .models(
                inventory = "origins_ghostly_phat_inv".model(),
                primaryMale = "origins_ghostly_phat_eq".model(),
            )
            .rotateInv(76, 1852, 0)
            .offsetInv(1, 1)
            .equipmentOps()
            .tradable()
            .zoom(440)
            .createPlaceholder(32895)
            .packNew()

        32896.newItem()
            .named("Pernix cowl")
            .models(
                inventory = "origins_pernix_cowl_inv".model(),
                primaryMale = "origins_pernix_cowl_eq_m".model(),
                primaryFemale = "origins_pernix_cowl_eq_f".model(),
            )
            .rotateInv(532, 14, 0)
            .offsetInv(-1, 1)
            .tradable()
            .zoom(800)
            .equipmentOps()
            .createPlaceholder(32897)
            .packNew()

        32898.newItem()
            .named("Pernix body")
            .models(
                inventory = "origins_pernix_body_inv".model(),
                primaryMale = "origins_pernix_body_eq_m".model(),
                primaryFemale = "origins_pernix_body_eq_f".model(),
            )
            .rotateInv(485, 2042, 0)
            .offsetInv(-1, 7)
            .tradable()
            .zoom(1378)
            .equipmentOps()
            .createPlaceholder(32899)
            .packNew()

        32900.newItem()
            .named("Pernix chaps")
            .models(
                inventory = "origins_pernix_chaps_inv".model(),
                primaryMale = "origins_pernix_chaps_eq_m".model(),
                primaryFemale = "origins_pernix_chaps_eq_f".model(),
            )
            .tradable()
            .rotateInv(504, 0, 0)
            .offsetInv(4, 3)
            .zoom(1740)
            .equipmentOps()
            .createPlaceholder(32901)
            .packNew()

        32902.newItem()
            .named("Spirit cape")
            .models(
                inventory = "origins_spirit_cape_inv".model(),
                primaryMale = "origins_spirit_cape_eq_m".model(),
                primaryFemale = "origins_spirit_cape_eq_f".model()
            )
            .tradable()
            .rotateInv(252, 54, 0)
            .offsetInv(-1, 0)
            .zoom(1616)
            .equipmentOps()
            .createPlaceholder(32903)
            .packNew()

        32904.newItem()
            .named("Demon horn necklace")
            .models(
                inventory = "origins_demonhorn_necklace_inv".model(),
                primaryMale = "origins_demonhorn_necklace_eq_m".model(),
                primaryFemale = "origins_demonhorn_necklace_eq_f".model(),
            )
            .tradable()
            .rotateInv(372, 1409, 0)
            .offsetInv(-4, -4)
            .zoom(848)
            .equipmentOps()
            .createPlaceholder(32905)
            .packNew()

        32906.newItem()
            .named("Mercenary gloves")
            .models(
                inventory = "origins_merc_gloves_inv".model(),
                primaryMale = "origins_merc_gloves_eq_m".model(),
                primaryFemale = "origins_merc_gloves_eq_f".model(),
            )
            .rotateInv(192, 1790, 0)
            .offsetInv(-1, 0)
            .tradable()
            .zoom(848)
            .equipmentOps()
            .createPlaceholder(32907)
            .packNew()

        32908.newItem()
            .named("Elite black full helm")
            .models(
                inventory = "origins_eliteblack_helm_inv".model(),
                primaryMale = "origins_eliteblack_helm_eq_m".model(),
                primaryFemale = "origins_eliteblack_helm_eq_f".model(),
            )
            .rotateInv(138, 1909, 0)
            .offsetInv(0, -5)
            .zoom(789)
            .tradable()
            .equipmentOps()
            .createPlaceholder(32909)
            .packNew()

        32910.newItem()
            .named("Elite black platelegs")
            .models(
                inventory = "origins_eliteblack_legs_inv".model(),
                primaryMale = "origins_eliteblack_legs_eq_m".model(),
                primaryFemale = "origins_eliteblack_legs_eq_f".model(),
            )
            .rotateInv(512, 0, 0)
            .offsetInv(0, 1)
            .equipmentOps()
            .tradable()
            .zoom(1827)
            .createPlaceholder(32911)
            .packNew()

        32912.newItem()
            .named("Elite black platebody")
            .models(
                inventory = "origins_eliteblack_body_inv".model(),
                primaryMale = "origins_eliteblack_body_eq_m1".model(),
                primaryFemale = "origins_eliteblack_body_eq_f1".model(),
                secondaryMale = "origins_eliteblack_body_eq_m2".model(),
                secondaryFemale = "origins_eliteblack_body_eq_f2".model(),
            )
            .rotateInv(512, 0, 0)
            .offsetInv(0, 4)
            .zoom(1827)
            .equipmentOps()
            .tradable()
            .createPlaceholder(32913)
            .packNew()

        32914.newItem()
            .named("Primal 2h sword")
            .models(
                inventory = "origins_primal_2hsw_inv".model(),
                primaryMale = "origins_primal_2hsw_eq".model(),
            )
            .rotateInv(1589, 1576, 956)
            .offsetInv(-1, -3)
            .offsetEq(female = -4)
            .zoom(1776)
            .weaponOps()
            .tradable()
            .createPlaceholder(32915)
            .packNew()

        32916.newItem()
            .named("Primal boots")
            .models(
                inventory = "origins_primal_boots_inv".model(),
                primaryMale = "origins_primal_boots_eq".model(),
            )
            .rotateInv(164, 156, 0)
            .offsetInv(0, -8)
            .zoom(789)
            .tradable()
            .equipmentOps()
            .createPlaceholder(32917)
            .packNew()

        32918.newItem()
            .named("Primal gauntlets")
            .models(
                inventory = "origins_primal_gauntlets_inv".model(),
                primaryMale = "origins_primal_gauntlets_eq_m".model(),
                primaryFemale = "origins_primal_gauntlets_eq_f".model(),
            )
            .rotateInv(420, 828, 97)
            .offsetInv(3, -7)
            .zoom(930)
            .tradable()
            .equipmentOps()
            .createPlaceholder(32919)
            .packNew()

        32920.newItem()
            .named("Primal full helm")
            .models(
                inventory = "origins_primal_helm_inv".model(),
                primaryMale = "origins_primal_helm_eq_m".model(),
                primaryFemale = "origins_primal_helm_eq_f".model(),
            )
            .rotateInv(121, 0, 0)
            .offsetInv(0, -4)
            .zoom(921)
            .tradable()
            .equipmentOps()
            .createPlaceholder(32921)
            .packNew()

        32922.newItem()
            .named("Primal platebody")
            .models(
                inventory = "origins_primal_plate_inv".model(),
                primaryMale = "origins_primal_plate_eq_m".model(),
                primaryFemale = "origins_primal_plate_eq_f".model(),
            )
            .rotateInv(485, 0, 0)
            .offsetInv(0, 13)
            .zoom(1447)
            .tradable()
            .equipmentOps()
            .createPlaceholder(32923)
            .packNew()

        32924.newItem()
            .named("Primal chainbody")
            .models(
                inventory = "origins_primal_chain_inv".model(),
                primaryMale = "origins_primal_chain_eq_m".model(),
                primaryFemale = "origins_primal_chain_eq_f".model(),
            )
            .rotateInv(485, 0, 0)
            .offsetInv(0, 8)
            .zoom(1447)
            .tradable()
            .equipmentOps()
            .createPlaceholder(32925)
            .packNew()

        32926.newItem()
            .named("Primal plateskirt")
            .models(
                inventory = "origins_primal_skirt_inv".model(),
                primaryMale = "origins_primal_skirt_eq_m".model(),
                primaryFemale = "origins_primal_skirt_eq_f".model(),
            )
            .tradable()
            .rotateInv(488, 0, 0)
            .offsetInv(-1, 1)
            .zoom(1711)
            .equipmentOps()
            .createPlaceholder(32927)
            .packNew()

        32928.newItem()
            .named("Primal platelegs")
            .models(
                inventory = "origins_primal_legs_inv".model(),
                primaryMale = "origins_primal_legs_eq_m".model(),
                primaryFemale = "origins_primal_legs_eq_f".model(),
            )
            .tradable()
            .rotateInv(525, 0, 0)
            .offsetInv(7, 3)
            .zoom(1730)
            .equipmentOps()
            .createPlaceholder(32929)
            .packNew()

        32930.newItem()
            .named("Primal longsword")
            .models(
                inventory = "origins_primal_longsword_inv".model(),
                primaryMale = "origins_primal_longsword_eq_m".model(),
                primaryFemale = "origins_primal_longsword_eq_f".model(),
            )
            .rotateInv(1401, 1805, 1037)
            .weaponOps()
            .tradable()
            .offsetInv(3, 0)
            .zoom(1579)
            .createPlaceholder(32931)
            .packNew()

        32932.newItem()
            .named("Primal maul")
            .models(
                inventory = "origins_primal_maul_inv".model(),
                primaryMale = "origins_primal_maul_eq".model(),
            )
            .tradable()
            .rotateInv(525, 350, 40)
            .offsetInv(5, 0)
            .zoom(1513)
            .weaponOps()
            .createPlaceholder(32933)
            .packNew()

        32934.newItem()
            .named("Primal hatchet")
            .models(
                inventory = "origins_primal_hatchet_inv".model(),
                primaryMale = "origins_primal_hatchet_eq_m".model(),
                primaryFemale = "origins_primal_hatchet_eq_f".model(),
            )
            .tradable()
            .rotateInv(546, 142, 0)
            .offsetInv(1, 8)
            .weaponOps()
            .zoom(1520)
            .createPlaceholder(32935)
            .packNew()

        32936.newItem()
            .named("Primal dagger")
            .models(
                inventory = "origins_primal_dagger_inv".model(),
                primaryMale = "origins_primal_dagger_eq".model(),
            )
            .tradable()
            .rotateInv(485, 269, 1145)
            .offsetInv(5, 14)
            .zoom(918)
            .weaponOps()
            .createPlaceholder(32937)
            .offsetEq(-4)
            .packNew()

        32938.newItem()
            .named("Primal rapier")
            .models(
                inventory = "origins_primal_rapier_inv".model(),
                primaryMale = "origins_primal_rapier_eq".model(),
            )
            .tradable()
            .rotateInv(1401, 1724, 1118)
            .offsetInv(11, -11)
            .offsetEq(female = -4)
            .weaponOps()
            .zoom(1447)
            .createPlaceholder(32939)
            .packNew()

        32940.newItem()
            .named("Primal warhammer")
            .models(
                inventory = "origins_primal_warhammer_inv".model(),
                primaryMale = "origins_primal_warhammer_eq".model(),
            )
            .rotateInv(552, 337, 96)
            .offsetInv(-3, 0)
            .offsetEq(female = -4)
            .weaponOps()
            .zoom(1184)
            .tradable()
            .createPlaceholder(32941)
            .packNew()

        32942.newItem()
            .named("Primal spear")
            .models(
                inventory = "origins_primal_spear_inv".model(),
                primaryMale = "origins_primal_spear_eq".model(),
            )
            .weaponOps()
            .tradable()
            .zoom(1711)
            .rotateInv(485, 391, 144)
            .offsetInv(5, -5)
            .offsetEq(female = -4)
            .createPlaceholder(32943)
            .packNew()

        32944.newItem()
            .named("Primal pickaxe")
            .models(
                inventory = "origins_primal_pickaxe_inv".model(),
                primaryMale = "origins_primal_pickaxe_eq".model(),
            )
            .weaponOps()
            .tradable()
            .zoom(1250)
            .rotateInv(224, 1056, 0)
            .offsetInv(-4, -19)
            .offsetEq(female = -4)
            .createPlaceholder(32945)
            .packNew()

        32946.newItem()
            .named("Primal battleaxe")
            .models(
                inventory = "origins_primal_battleaxe_inv".model(),
                primaryMale = "origins_primal_battleaxe_eq".model(),
            )
            .weaponOps()
            .zoom(1330)
            .offsetInv(3, 1)
            .rotateInv(552, 148, 0)
            .offsetEq(female = -4)
            .tradable()
            .createPlaceholder(32947)
            .packNew()

        32948.newItem()
            .named("Primal kiteshield")
            .models(
                inventory = "origins_primal_kiteshield_inv".model(),
                primaryMale = "origins_primal_kiteshield_eq".model(),
            )
            .tradable()
            .zoom(2434)
            .rotateInv(458, 0, 0)
            .offsetInv(-3, 9)
            .offsetEq(female = -4)
            .equipmentOps()
            .createPlaceholder(32949)
            .packNew()

        ELYSIAN_SIGIL.cloneThisTo(32955)
            .named("Divine sigil")
            .models(
                inventory = "origins_divine_sigil_inv".model()
            )
            .tradable()
            .rotateInv(267, 138, 0)
            .offsetInv(5, 0)
            .clearPlaceholder()
            .clearNote()
            .zoom(848)
            .packNew()

        ELYSIAN_SPIRIT_SHIELD.cloneThisTo(32956)
            .named("Divine spirit shield")
            .models(
                inventory = "origins_divine_ss_inv".model(),
                primaryMale = "origins_divine_ss_eq".model(),
            )
            .rotateInv(396, 1050, 0)
            .offsetInv(-3, 4)
            .zoom(1616)
            .createPlaceholder(32957)
            .clearNote()
            .packNew()

        MINECART_TICKET.cloneThisTo(32958)
            .named("Custom Item Token")
            .createPlaceholder(32959)
            .packNew()

        MINECART_TICKET.cloneThisTo(32960)
            .named("Custom Item Set Token")
            .createPlaceholder(32961)
            .packNew()

        DONATOR_PROMO_BUNDLE_1.promoBundle(1)
        DONATOR_PROMO_BUNDLE_2.promoBundle(2)
        DONATOR_PROMO_BUNDLE_3.promoBundle(3)
        DONATOR_PROMO_BUNDLE_4.promoBundle(4)
        DONATOR_PROMO_BUNDLE_5.promoBundle(5)
        DONATOR_PROMO_BUNDLE_6.promoBundle(6)
        DONATOR_PROMO_BUNDLE_7.promoBundle(7)
        DONATOR_PROMO_BUNDLE_8.promoBundle(8)
        DONATOR_PROMO_BUNDLE_9.promoBundle(9)
        DONATOR_PROMO_BUNDLE_10.promoBundle(10)
    }

    private fun Int.promoBundle(number: Int) =
            newItem()
            .named("NR Promo Bundle #$number")
            .models(inventory = "origins_nx_store_bundle_inv".model())
            .rotateInv(121, 1916, 0)
            .zoom(2860)
            .offsetInv(13, -9)
            .inventoryOptions(arrayOf("Unpack", "Inspect", "", "", ""))
            .packNew()
}