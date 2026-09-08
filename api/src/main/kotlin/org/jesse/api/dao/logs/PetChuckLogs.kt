package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.model.PetChuckLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PetChuckLogs : LongIdTable("pet_chuck_logs") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val petName = varchar("pet", 255).index()
    val success = bool("success")
}

class PetChuckEntity(id: EntityID<Long>):  ModelEntity<PetChuckLog>(id) {
    companion object : LongEntityClass<PetChuckEntity>(PetChuckLogs) {
        fun new(log: PetChuckLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            petName = log.pet
            success = log.success
        }
    }

    var time by PetChuckLogs.time
    var username by PetChuckLogs.username
    var petName by PetChuckLogs.petName
    var success by PetChuckLogs.success

    override fun toModel() = PetChuckLog (
        time = time.toInstant(defaultTimeZone),
        username = username,
        pet= petName,
        success = success
    )
}