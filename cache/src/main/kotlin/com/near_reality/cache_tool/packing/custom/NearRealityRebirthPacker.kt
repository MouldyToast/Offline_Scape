package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase

object NearRealityRebirthPacker {
    @JvmStatic fun pack() {
        packMasterCompCape()
        packHome2025()
        packCustoms2023AndMisc()
        packCustoms2024()
        packCustoms2025()
    }

    @JvmStatic fun packMasterCompCape() {
        assetsBase("assets/rebirth/master_comp_cape/") {
            defaultModels()
        }
    }

    @JvmStatic fun packHome2025() {
        assetsBase("assets/rebirth/home_2025/") {
            defaultModels()
        }

        /* GE Corner Booth */
        60400.cloneObject(24393)
            .modelList("rebirth_ge_straight_wall".model(), "rebirth_ge_corner".model())
            .packNew()

        60401.cloneObject(374)
            .modelList("rebirth_slayer_helm_stand_empty".model())
            .named("Slayer helmet stand")
            .sizeScale(2, 1)
            .modelScale(128, 128, 128)
            .changeOption(1, "Information")
            .changeOption(2, null)
            .packNew()

    }

    @JvmStatic fun packCustoms2023AndMisc() {
        assetsBase("assets/rebirth/customs_2023/") {
            defaultModels()
        }

        assetsBase("assets/rebirth/legacy_models/") {
            defaultModels()
        }

        assetsBase("assets/rebirth/misc_models/") {
            defaultModels()
        }
    }

    @JvmStatic fun packCustoms2024() {
        assetsBase("assets/rebirth/customs_2024/") {
            defaultModels()
        }

        50145.newObject()
            .modelList("armoured_zombie_red_mist".model())
            .named("Red mist")
            .clipType(1)
            .projectileClip(false)
            .obstructsGround(false)
            .clipped(false)
            .animationId(10727)
            .nullOps()
            .packNew()
    }

    @JvmStatic fun packCustoms2025() {
        assetsBase("assets/rebirth/customs_2025/") {
            defaultModels()
        }
    }
}