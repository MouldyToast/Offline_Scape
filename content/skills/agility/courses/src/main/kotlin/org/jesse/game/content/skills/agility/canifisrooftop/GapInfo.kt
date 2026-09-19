package org.jesse.game.content.skills.agility.canifisrooftop

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.jesse.game.obj.ids.*
import org.jesse.game.world.entity.Location

enum class GapInfo(val id: Int, start: Location, finish: Location) {
    FIRST(GAP_14844, Location(3505, 3497, 2), Location(3502, 3504, 2)),
    SECOND(GAP_14845, Location(3498, 3504, 2), Location(3492, 3504, 2)),
    THIRD(GAP_14846, Location(3478, 3493, 3), Location(3478, 3486, 2)),
    FOURTH(GAP_14847, Location(3502, 3476, 3), Location(3510, 3476, 2)),
    FIFTH(GAP_14897, Location(3510, 3482, 2), Location(3510, 3485, 0)),
    ;

    @JvmField
    val start: Location?
    @JvmField
    val finish: Location?

    init {
        this.start = start
        this.finish = finish
    }

    companion object {
        val values: Array<GapInfo> = entries.toTypedArray()
        private val idToInfo: Int2ObjectMap<GapInfo?> = Int2ObjectOpenHashMap<GapInfo?>(values.size)

        @JvmStatic
        fun get(id: Int): GapInfo? {
            return idToInfo.get(id)
        }

        init {
            for (entry in values) {
                idToInfo.put(entry.id, entry)
            }
        }
    }
}
