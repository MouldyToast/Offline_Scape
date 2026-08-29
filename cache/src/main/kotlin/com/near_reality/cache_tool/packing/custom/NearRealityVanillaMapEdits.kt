package com.near_reality.cache_tool.packing.custom

import com.near_reality.game.item.CustomObjectId.*
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.ObjectId.*
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.Regions
import mgi.tools.parser.TypeParser
import mgi.types.config.ObjectDefinitions
import java.util.function.Predicate

object NearRealityVanillaMapEdits {

    val coalRock = ROCKS_11366
    val mithrilRock = ROCKS_11372
    val adamantiteRock = ROCKS_11374
    val runiteRock = ROCKS_11376
    val spear = 849
    val crate = 358
    val altar = 26257
    val bankChest = 47345
    val magicTree = 36685
    val yewTree = 36683
    val furnace = 4304

    @JvmStatic
    fun apply() {
        edit(12088) {
            ObjectId.ROCKS_11381(3009, 3594, 0)
            ObjectId.ROCKS_11381(3009, 3595, 0)
            ObjectId.ROCKS_11381(3011, 3598, 0)
            ObjectId.ROCKS_11381(3012, 3599, 0)
            ObjectId.ROCKS_11381(3013, 3598, 0)
            ObjectId.ROCKS_11381(3016, 3601, 0)
            ObjectId.ROCKS_11381(3017, 3601, 0)
            ObjectId.ROCKS_11381(3025, 3594, 0)
            ObjectId.ROCKS_11381(3026, 3595, 0)
            ObjectId.ROCKS_11381(3020, 3585, 0)
            ObjectId.ROCKS_11381(3021, 3584, 0)
            ObjectId.ROCKS_11381(3017, 3591, 0)
            ObjectId.ROCKS_11381(3018, 3591, 0)
        }

        edit(9369) {
            coalRock(2351, 9818, 0)
            coalRock(2352, 9818, 0)
            coalRock(2353, 9818, 0)
            coalRock(2354, 9818, 0)
            coalRock(2355, 9818, 0)

            mithrilRock(2358, 9817, 0)
            mithrilRock(2359, 9817, 0)
            mithrilRock(2360, 9817, 0)

            adamantiteRock(2362, 9816, 0)
            adamantiteRock(2363, 9816, 0)
            adamantiteRock(2364, 9816, 0)

            runiteRock(2365, 9815, 0)
            runiteRock(2365, 9814, 0)
            runiteRock(2365, 9813, 0)

            crate(2347, 9828, 0)
            for(i in 9823..9827 step 1)
                crate(2347, i, 0, rotation = 2)
            crate(2347, 9822, 0)

            for(x in 2337..2343 step 1)
                crate(x, 9829, 0, rotation = 1)

            for(x in 2306..2333 step 1)
                crate(x, 9829, 0, rotation = 1)

            altar(2339, 9815, 0, rotation = 2)
            bankChest(2334, 9815, 0)
            remove(3353)
            remove(3354)
        }

        edit(9370) {
            crate(2345, 9882, 0)
            crate(2346, 9882, 0)

            crate(2312, 9907, 0)
            crate(2312, 9908, 0)

            altar(2338, 9886, 0, rotation = 3)
            bankChest(2338, 9883, 0, rotation = 1)
        }

        /* RDI */
        edit(11605) {
            remove(12613, 12614, 12619, 12620, 12621, 12622, 12623) //winter
            remove(11989, 11990, 11991, 11993, 11994, 11995) // spring
            remove(12726, 12727, 12728, 12729, 12721) // summer
            remove(12635, 12636, 12641, 12642, 12643, 12644, 12645, 12646, 12647) //desert
            remove(4982, 4979, 4980, 4981) /* herbs */
            remove(13406, 13405, 13407, 12943) /* trees */
            remove(12716, 11963) /* floating arches */
            remove(11962, 12715) /* floating arch support */
            remove(12941) /* fountain */
            remove(11962, 12637, 12615, 11964) /* Entrance archways */
            60426(2911, 5471, 0)
            bankChest(2911, 5465, 0, rotation = 2)
            bankChest(2905, 5472, 0, rotation = 3)
            bankChest(2912, 5478, 0, rotation = 0)
            bankChest(2918, 5471, 0, rotation = 1)

            magicTree(2915, 5458, 0)
            magicTree(2915, 5454, 0)
            magicTree(2915, 5450, 0)

            yewTree(2910, 5448, 0)
            yewTree(2906, 5448, 0)
            yewTree(2902, 5448, 0)

            furnace(2898, 5450, 0)

            runiteRock(2898, 5453, 0)
            runiteRock(2898, 5454, 0)
            runiteRock(2898, 5455, 0)
            coalRock(2898, 5456, 0)
            coalRock(2898, 5457, 0)
            coalRock(2898, 5458, 0)
            coalRock(2898, 5459, 0)
            coalRock(2898, 5460, 0)
        }

        edit(11681) {
            replace(46702, 46701)
        }

        edit(11168) {
            replace(50598, -1)
            replace(50586, -1)
            replace(50587, -1)
            replace(50603, -1)
            replace(50604, -1)
            replace(55781, -1)
            replace(55782, -1)
            replace(55783, -1)
        }

        edit(12132) {
            ObjectId.DUKE_SCOREBOARD(3041, 6430, 0, 4)
        }
        edit(11681) {
            ObjectId.PHANTOM_MUSPAH_SCOREBOARD(2914, 10317, 0, 4)
        }

        edit(8292) {
            replace(49475, 47589)
        }

        edit(4405) {
            replace(49476, 47598)
        }

        edit(14745) {
            replace(54270, 54149)
        }

        edit(6696) {
            replace(REJUVENATION_FANCY, REJUVENATION_DIVINE)
        }
        edit(12598) {
            AFK_FARMING_PLOT(3141, 3492, 0)
        }
        edit(6696) {
            47345(1695, 2573, 0)
            47345(1679, 2591, 0)
        }
        edit(6440) {
            47345(1645, 2585, 0)
        }
    }

    class MapEdit(val regionId: Int) {
        private val objects = mutableListOf<WorldObject>()
        val replacements = mutableMapOf<Int, Int>()
        val removals = mutableListOf<Int>()
        operator fun Int.invoke(x: Int, y: Int, z: Int, type: Int = 10, rotation: Int = 0) {
            objects += WorldObject(this, type, rotation, Location(x, y, z))
        }

        fun replace(oldId: Int, newId: Int) {
            replacements[oldId] = newId
        }

        fun remove(vararg ids: Int) {
            removals.addAll(ids.toList())
        }

        private fun buildPredicate() : Predicate<WorldObject> =
            Predicate<WorldObject> {
                if(replacements.containsKey(it.id))
                    it.id = replacements[it.id]!!
                if(removals.contains(it.id))
                    return@Predicate true
                false
            }

        fun pack() {
            TypeParser.packMapPre209(regionId, null, Regions.inject(regionId, buildPredicate(), *objects.toTypedArray()))
        }
    }

    fun edit(regionId: Int, block: MapEdit.() -> Unit) {
        MapEdit(regionId).apply(block).pack()
    }
}
