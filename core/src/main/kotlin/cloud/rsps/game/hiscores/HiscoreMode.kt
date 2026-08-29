package cloud.rsps.game.hiscores

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

/**
 * @author Jire
 */
enum class HiscoreMode(
    val index: Byte,
    val paramName: String,
) {

    NORMAL(0, "normal"),
    IRONMAN(1, "ironman"),
    HARDCORE_IRONMAN(2, "hardcore_ironman"),
    ULTIMATE_IRONMAN(3, "ultimate_ironman"),
    DEADMAN(4, "deadman"),
    LEAGUE(5, "seasonal"),
    TOURNAMENT(6, "tournament"),
    FRESH_START_WORLD(7, "fresh_start"),
    PURE(8, "skiller_defence"),
    LEVEL_3_SKILLER(9, "skiller"),

    ;

    val skillsTable: HiscoresSkillsTable =
        HiscoresSkillsTable(this)

    val skillsRankTableA: HiscoresSkillsRankTable =
        HiscoresSkillsRankTable(this, "a")
    val skillsRankTableB: HiscoresSkillsRankTable =
        HiscoresSkillsRankTable(this, "b")

    val activityTable: HiscoresActivityTable =
        HiscoresActivityTable(this)

    val activityRankTableA: HiscoresActivityRankTable =
        HiscoresActivityRankTable(this, "a")
    val activityRankTableB: HiscoresActivityRankTable =
        HiscoresActivityRankTable(this, "b")

    val manager =
        HiscoresManager(this)

    companion object {

        private val paramName2Mode: Object2ObjectMap<String, HiscoreMode> =
            Object2ObjectOpenHashMap(entries.size)

        init {
            for (mode in entries) {
                paramName2Mode[mode.paramName] = mode
            }
        }

        @JvmStatic
        fun fromParamName(paramName: String): HiscoreMode? =
            paramName2Mode[paramName]

    }

}
