package org.jesse.game.content.origins.di

import org.jesse.game.npc.ids.*
import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Die : NPCSpawnsScript() {

    init {
        BLUE_DRAGON(2323, 9841, 0, walkRadius = 3)
        BLUE_DRAGON(2325, 9868, 0, walkRadius = 3)
        BLUE_DRAGON(2331, 9868, 0, walkRadius = 3)
        BLUE_DRAGON(2323, 9856, 0, walkRadius = 3)
        BLUE_DRAGON(2325, 9859, 0, walkRadius = 3)

        ANCIENT_WYVERN(2312, 9854, 0, walkRadius = 3)
        ANCIENT_WYVERN(2818, 9858, 0, walkRadius = 3)

        BLACK_DRAGON(2329, 9837, 0, walkRadius = 3)
        BLACK_DRAGON(2334, 9840, 0, walkRadius = 3)

        GREEN_DRAGON(2339, 9844, 0, walkRadius = 3)
        GREEN_DRAGON(2345, 9853, 0, walkRadius = 3)
        GREEN_DRAGON(2340, 9854, 0, walkRadius = 3)

        RED_DRAGON(2348, 9845, 0, walkRadius = 3)
        RED_DRAGON(2344, 9840, 0, walkRadius = 3)
        RED_DRAGON(2352, 9827, 0, walkRadius = 3)
        RED_DRAGON(2356, 9837, 0, walkRadius = 3)

        ABYSSAL_DEMON_416(2356, 9847, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2356, 9849, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2356, 9844, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2358, 9844, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2360, 9844, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2362, 9844, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2358, 9846, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2360, 9846, 0, walkRadius = 0)
        ABYSSAL_DEMON_416(2362, 9846, 0, walkRadius = 0)

        DUST_DEVIL(2358, 9857, 0, walkRadius = 0)
        DUST_DEVIL(2358, 9859, 0, walkRadius = 0)
        DUST_DEVIL(2356, 9857, 0, walkRadius = 0)
        DUST_DEVIL(2356, 9859, 0, walkRadius = 0)
        DUST_DEVIL(2356, 9861, 0, walkRadius = 0)
        DUST_DEVIL(2354, 9857, 0, walkRadius = 0)
        DUST_DEVIL(2354, 9859, 0, walkRadius = 0)
        DUST_DEVIL(2354, 9861, 0, walkRadius = 0)
        DUST_DEVIL(2352, 9857, 0, walkRadius = 0)
        DUST_DEVIL(2352, 9861, 0, walkRadius = 0)

        DARK_BEAST(2345, 9871, 0, walkRadius = 0)
        DARK_BEAST(2349, 9871, 0, walkRadius = 0)
        DARK_BEAST(2340, 9868, 0, walkRadius = 0)
        DARK_BEAST(2344, 9868, 0, walkRadius = 0)
        DARK_BEAST(2340, 9865, 0, walkRadius = 0)
        DARK_BEAST(2344, 9865, 0, walkRadius = 0)

        NECHRYAEL(2342, 9879, 0, walkRadius = 0)
        NECHRYAEL(2344, 9879, 0, walkRadius = 0)
        NECHRYAEL(2346, 9879, 0, walkRadius = 0)
        NECHRYAEL(2348, 9879, 0, walkRadius = 0)
        NECHRYAEL(2350, 9879, 0, walkRadius = 0)
        NECHRYAEL(2352, 9879, 0, walkRadius = 0)
        NECHRYAEL(2343, 9877, 0, walkRadius = 0)
        NECHRYAEL(2345, 9877, 0, walkRadius = 0)
        NECHRYAEL(2347, 9877, 0, walkRadius = 0)
        NECHRYAEL(2349, 9877, 0, walkRadius = 0)

        NEZIKCHENED_6379(2354, 9884, 0, walkRadius = 3)
        NEZIKCHENED_6379(2360, 9884, 0, walkRadius = 3)
        NEZIKCHENED_6379(2362, 9892, 0, walkRadius = 3)
        NEZIKCHENED_6379(2356, 9878, 0, walkRadius = 3)

        IRON_DRAGON(2330, 9876, 0, walkRadius = 3)
        IRON_DRAGON(2334, 9878, 0, walkRadius = 3)

        STEEL_DRAGON(2326, 9879, 0, walkRadius = 3)
        STEEL_DRAGON(2332, 9883, 0, walkRadius = 3)

        TORRMENTED_DEMON(2311, 9870, 0, walkRadius = 3)
        TORRMENTED_DEMON(2316, 9873, 0, walkRadius = 3)
        TORRMENTED_DEMON(2316, 9878, 0, walkRadius = 3)
        TORRMENTED_DEMON(2314, 9882, 0, walkRadius = 3)

        BORK(2321, 9888, 0, walkRadius = 3)
        BORK(2323, 9894, 0, walkRadius = 3)

        NOMAD_16086(2323, 9913, 0, walkRadius = 3)

        KING_BLACK_DRAGON(2335, 9899, 0, walkRadius = 3)
        KING_BLACK_DRAGON(2334, 9891, 0, walkRadius = 3)

        KALPHITE_QUEEN_965(2328, 9898, 0, walkRadius = 3)
        KALPHITE_QUEEN_965(2312, 9892, 0, walkRadius = 3)
    }
}
