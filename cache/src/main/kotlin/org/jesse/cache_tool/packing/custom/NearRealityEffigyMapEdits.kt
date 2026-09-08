package org.jesse.cache_tool.packing.custom


import org.jesse.game.world.entity.Location
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.Regions
import mgi.tools.parser.TypeParser
import java.util.function.Predicate

object NearRealityEffigyMapEdits {

    @JvmStatic
    fun apply() {
        edit(12088) {
            ROCKS_11381(3009, 3594, 0)
            ROCKS_11381(3009, 3595, 0)
            ROCKS_11381(3011, 3598, 0)
            ROCKS_11381(3012, 3599, 0)
            ROCKS_11381(3013, 3598, 0)
            ROCKS_11381(3016, 3601, 0)
            ROCKS_11381(3017, 3601, 0)
            ROCKS_11381(3025, 3594, 0)
            ROCKS_11381(3026, 3595, 0)
            ROCKS_11381(3020, 3585, 0)
            ROCKS_11381(3021, 3584, 0)
            ROCKS_11381(3017, 3591, 0)
            ROCKS_11381(3018, 3591, 0)
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
            DUKE_SCOREBOARD(3041, 6430, 0, 4)
        }
        edit(11681) {
            PHANTOM_MUSPAH_SCOREBOARD(2914, 10317, 0, 4)
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


    }

    class MapEdit(val regionId: Int) {
        private val objects = mutableListOf<WorldObject>()
        val replacements = mutableMapOf<Int, Int>()
        operator fun Int.invoke(x: Int, y: Int, z: Int, type: Int = 10, rotation: Int = 0) {
            objects += WorldObject(this, type, rotation, Location(x, y, z))
        }

        fun replace(oldId: Int, newId: Int) {
            replacements[oldId] = newId
        }

        private fun buildPredicate() : Predicate<WorldObject> =
            Predicate<WorldObject> {
                if(replacements.containsKey(it.id))
                    it.id = replacements[it.id]!!
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
