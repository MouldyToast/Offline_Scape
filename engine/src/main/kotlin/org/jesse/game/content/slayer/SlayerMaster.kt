package org.jesse.game.content.slayer

import org.jesse.game.npc.ids.*
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * @author Kris | 5. nov 2017 : 21:22.44
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)}
 */
enum class SlayerMaster(
    val npcId: Int,
    val slayerRequirement: Int,
    val combatRequirement: Int,
    val pointsPerTask: Int,
    val location: String
) {
    TURAEL(org.jesse.game.npc.ids.TURAEL, 1, 1, 0, "in Burthorpe"),
    KRYSTILIA(7663, 1, 1, 25, "in Edgeville"),
    MAZCHNA(org.jesse.game.npc.ids.MAZCHNA, 1, 20, 2, "in Canifis"),
    VANNAKA(org.jesse.game.npc.ids.VANNAKA, 1, 40, 4, "within the Edgeville dungeon"),
    CHAELDAR(org.jesse.game.npc.ids.CHAELDAR, 1, 70, 10, "in Zanaris"),
    NIEVE(org.jesse.game.npc.ids.NIEVE, 1, 85, 12, "in Tree Gnome Stronghold"),
    DURADEL(org.jesse.game.npc.ids.DURADEL, 50, 100, 15, "in Shilo Village"),
    KONAR_QUO_MATEN(org.jesse.game.npc.ids.KONAR_QUO_MATEN, 1, 75, 18, "On Mount Karuulm"),
    SUMONA(org.jesse.game.npc.ids.SUMONA, 99, 100, 15, "at Home"),
    ;

    fun getMultiplier(taskNum: Int): Int {
        if (taskNum % 1000 == 0) {
            return 50
        } else if (taskNum % 250 == 0) {
            return 35
        } else if (taskNum % 100 == 0) {
            return 25
        } else if (taskNum % 50 == 0) {
            return 15
        } else if (taskNum % 10 == 0) {
            return 5
        }
        return 1
    }

    override fun toString(): String {
        if (this == SUMONA) return "Summona"
        return StringUtils.capitalize(name.replace('_', ' ').lowercase(Locale.getDefault()))
    }

    companion object {
        fun isMaster(id: Int): Boolean {
            return mappedMasters.containsKey(id)
        }

        public val values: Array<SlayerMaster> = entries.toTypedArray()
        @JvmStatic val mappedMasters: Int2ObjectMap<SlayerMaster> = Int2ObjectOpenHashMap(values.size)

        init {
            for (master in values) {
                mappedMasters.put(master.npcId, master)
            }
        }
    }
}
