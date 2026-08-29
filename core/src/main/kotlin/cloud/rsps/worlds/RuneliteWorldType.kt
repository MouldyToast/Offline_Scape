package cloud.rsps.worlds

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
enum class RuneliteWorldType(
    bitIndex: Int,
    private val bitMask: Int = 1 shl bitIndex
) {

    MEMBERS(0),
    PVP(2),
    BOUNTY(5),
    PVP_ARENA(6),
    SKILL_TOTAL(7),
    QUEST_SPEEDRUNNING(8),
    HIGH_RISK(10),
    LAST_MAN_STANDING(14),

    //BETA_WORLD,
    //LEGACY_ONLY,
    //EOC_ONLY,
    NOSAVE_MODE(25),
    TOURNAMENT(26),
    FRESH_START_WORLD(27),
    DEADMAN(29),
    SEASONAL(30),

    ;

    internal companion object {

        @JvmStatic
        fun forSettings(settings: Int): List<RuneliteWorldType> {
            val types: MutableList<RuneliteWorldType> =
                ObjectArrayList(entries.size)
            for (type in entries) {
                if (settings and type.bitMask != 0) {
                    types.add(type)
                }
            }
            return types
        }

        @JvmStatic
        fun toInt(types: List<RuneliteWorldType>): Int {
            var mask = 0
            for (type in types) {
                mask = mask or type.bitMask
            }
            return mask
        }

    }

}
