package cloud.rsps.game.hiscores

import org.jetbrains.exposed.sql.Transaction

/**
 * @author Jire
 */
data class HiscoreRankTablePointer(
    val skillTable: String,
    val activityTable: String
) {

    companion object {

        fun Transaction.getTablePointer(mode: HiscoreMode): HiscoreRankTablePointer? =
            exec("SELECT skill_table, activity_table FROM hiscore_rank_pointers WHERE mode_index = ${mode.index}") {
                it.next()
                HiscoreRankTablePointer(
                    it.getString(1),
                    it.getString(2)
                )
            }

        fun Transaction.getCurrentTable(table: String, index: Byte): String? =
            exec("SELECT $table FROM hiscore_rank_pointers WHERE mode_index = $index") {
                it.next()
                it.getString(1)
            }

        fun getToTable(current: String) =
            if (current.endsWith("_a"))
                "${current.dropLast(2)}_b"
            else "${current.dropLast(2)}_a"

        fun Transaction.getToTable(table: String, index: Byte): String? {
            val current = getCurrentTable(table, index) ?: return null
            return getToTable(current)
        }

        fun Transaction.withToTable(table: String, index: Byte, block: (String) -> Unit) {
            val toTable = getToTable(table, index) ?: return
            block(toTable)
            exec("UPDATE hiscore_rank_pointers SET $table = '$toTable' WHERE mode_index = $index")
        }

    }

}
