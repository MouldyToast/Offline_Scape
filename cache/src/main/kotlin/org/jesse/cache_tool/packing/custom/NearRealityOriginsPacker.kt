package org.jesse.cache_tool.packing.custom

import org.jesse.cache_tool.packing.assetsBase
import org.jesse.game.npc.ids.*
import org.jesse.game.item.ids.*
import mgi.types.config.npcs.NPCDefinitions
import net.runelite.api.NpcID
import net.runelite.api.NpcID.*

object NearRealityOriginsPacker {

    @JvmStatic fun pack() {
        packCustoms()
    }

    @JvmStatic fun packCustoms() {
        assetsBase("assets/origins/customs/") {
            defaultModels()
            defaultSequences()
            defaultSkeletons()
            defaultGraphics()
            defaultSkins()
        }

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

    }
}
