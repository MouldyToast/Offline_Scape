package cloud.rsps.worlds.database

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.sql.kotlin.datetime.timestampWithTimeZone

/**
 * @author Jire
 */
object WorldsTable : IdTable<UShort>("worlds") {

    override val id: Column<EntityID<UShort>> = ushort("id").entityId()
    override val primaryKey = PrimaryKey(id)

    val createdDate = timestampWithTimeZone("created_date")
        .defaultExpression(CurrentTimestampWithTimeZone)

    val lastUpdatedDate = timestampWithTimeZone("last_updated_date")
        .defaultExpression(CurrentTimestampWithTimeZone)

    val host = varchar("host", 255)
    val activity = varchar("activity", 255)
    val settings = integer("settings")
    val countryFlag = byte("country_flag")

    val playerCount = ushort("player_count")

}
