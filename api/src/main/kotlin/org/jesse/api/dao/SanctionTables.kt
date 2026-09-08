package org.jesse.api.dao

import org.jesse.api.model.*
import org.jesse.api.util.currentTime
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.duration
import kotlin.time.Duration

object AccountSanctions : LongIdTable("sanctions_account") {
    val time = datetime("time").index()
    val type = enumeration<SanctionType>("type").index()
    val reporter = optReference("reporter", Users).index()
    val reporterName = username("reporter_name").index().nullable()
    val offender = reference("offender", Users).index()
    val offenderName = username("offender_name").index().nullable()
    val reason = varchar("reason", 255).nullable()
    val duration = duration("duration").nullable()
}

class AccountSanctionEntity(id: EntityID<Long>) : SanctionModelEntity<AccountSanction>(id) {

    companion object : LongEntityClass<AccountSanctionEntity>(AccountSanctions) {
        fun new(sanction: AccountSanction) = new {
            time = sanction.time.toLocalDateTime(defaultTimeZone)
            type = sanction.type
            reporter = sanction.reporter?.let { UserEntity.findByUsername(it) }
            reporterName = sanction.reporter
            offender = UserEntity.findByUsername(sanction.offender)!!
            offenderName = sanction.offender
            reason = sanction.reason
            duration = sanction.duration
        }
        fun findFor(userEntity: UserEntity) = find { AccountSanctions.offender eq userEntity.id }
    }

    var offender by UserEntity referencedOn AccountSanctions.offender
    var offenderName by AccountSanctions.offenderName
    override var time by AccountSanctions.time
    override var type by AccountSanctions.type
    override var reporter by UserEntity optionalReferencedOn AccountSanctions.reporter
    override var reporterName by AccountSanctions.reporterName
    override var reason by AccountSanctions.reason
    override var duration by AccountSanctions.duration

    override fun toModel() =  AccountSanction(
        offender = offender.username,
        id = id.value.toInt(),
        time = time.toInstant(defaultTimeZone),
        type = type,
        reporter = reporter?.username,
        reason = reason,
        duration = duration
    )
}

object HardwareHashSanctions : LongIdTable("sanctions_hardware_hash") {
    val time = datetime("time").index()
    val type = enumeration<SanctionType>("type").index()
    val reporter = optReference("reporter", Users).index()
    val reporterName = username("reporter_name").index().nullable()
    val hardwareHash = integer("hardware_hash")
    val offenderName = username("offender_name").index().nullable()
    val reason = varchar("reason", 255).nullable()
    val duration = duration("duration").nullable()
}

class HardwareHashSanctionEntity(id: EntityID<Long>) : SanctionModelEntity<HardwareHashSanction>(id) {

    companion object : LongEntityClass<HardwareHashSanctionEntity>(HardwareHashSanctions) {
        fun new(sanction: HardwareHashSanction) = new {
            time = sanction.time.toLocalDateTime(defaultTimeZone)
            type = sanction.type
            reporter = sanction.reporter?.let { UserEntity.findByUsername(it) }
            reporterName = sanction.reporter
            hardwareHash = sanction.hardwareHash
            offender = sanction.offender
            reason = sanction.reason
            duration = sanction.duration
        }
        // TODO: findForUser()
        fun findFor(hash: Int) = find { HardwareHashSanctions.hardwareHash eq hash }
    }

    var offender by HardwareHashSanctions.offenderName
    var hardwareHash by HardwareHashSanctions.hardwareHash
    override var time by HardwareHashSanctions.time
    override var type by HardwareHashSanctions.type
    override var reporter by UserEntity optionalReferencedOn HardwareHashSanctions.reporter
    override var reporterName by HardwareHashSanctions.reporterName
    override var reason by HardwareHashSanctions.reason
    override var duration by HardwareHashSanctions.duration

    override fun toModel() =  HardwareHashSanction(
        hardwareHash = hardwareHash,
        offender = offender ?: "",
        id = id.value.toInt(),
        time = time.toInstant(defaultTimeZone),
        type = type,
        reporter = reporter?.username,
        reason = reason,
        duration = duration
    )
}


object IPSanctions : LongIdTable("sanctions_ip") {
    val time = datetime("time").index()
    val type = enumeration<SanctionType>("type").index()
    val reporter = optReference("reporter", Users).index()
    val reporterName = username("reporter_name").index().nullable()
    val ip = ip("ip").index()
    val reason = varchar("reason", 255).nullable()
    val duration = duration("duration").nullable()
}

class IPSanctionEntity(id: EntityID<Long>) : SanctionModelEntity<IPSanction>(id) {

    companion object : LongEntityClass<IPSanctionEntity>(IPSanctions) {
        fun new(sanction: IPSanction) = new {
            time = sanction.time.toLocalDateTime(defaultTimeZone)
            type = sanction.type
            reporter = sanction.reporter?.let { UserEntity.findByUsername(it) }
            reporterName = sanction.reporter
            ip = sanction.ip
            reason = sanction.reason
            duration = sanction.duration
        }
        fun findFor(ips: Set<String>) = find { IPSanctions.ip inList ips }
    }

    var ip by IPSanctions.ip
    override var time by IPSanctions.time
    override var type by IPSanctions.type
    override var reporter by UserEntity optionalReferencedOn IPSanctions.reporter
    override var reporterName by IPSanctions.reporterName
    override var reason by IPSanctions.reason
    override var duration by IPSanctions.duration

    override fun toModel() =  IPSanction(
        ip = ip,
        id = id.value.toInt(),
        time = time.toInstant(defaultTimeZone),
        type = type,
        reporter = reporter?.username,
        reason = reason,
        duration = duration
    )
}

object UUIDSanctions : LongIdTable("sanctions_uuid") {
    val time = datetime("time").index()
    val type = enumeration<SanctionType>("type").index()
    val reporter = optReference("reporter", Users).index()
    val reporterName = username("reporter_name").index().nullable()
    val uuid = binary("uuid", 24).index()
    val reason = varchar("reason", 255).nullable()
    val duration = duration("duration").nullable()
}

class UUIDSanctionEntity(id: EntityID<Long>) : SanctionModelEntity<UUIDSanction>(id) {

    companion object : LongEntityClass<UUIDSanctionEntity>(UUIDSanctions) {
        fun new(sanction: UUIDSanction) = new {
            time = sanction.time.toLocalDateTime(defaultTimeZone)
            type = sanction.type
            reporter = sanction.reporter?.let { UserEntity.findByUsername(it) }
            reporterName = sanction.reporter
            uuid = sanction.uuid
            reason = sanction.reason
            duration = sanction.duration
        }
    }

    var uuid by UUIDSanctions.uuid
    override var time by UUIDSanctions.time
    override var type by UUIDSanctions.type
    override var reporter by UserEntity optionalReferencedOn UUIDSanctions.reporter
    override var reporterName by UUIDSanctions.reporterName
    override var reason by UUIDSanctions.reason
    override var duration by UUIDSanctions.duration

    override fun toModel() =  UUIDSanction(
        uuid = uuid,
        id = id.value.toInt(),
        time = time.toInstant(defaultTimeZone),
        type = type,
        reporter = reporter?.username,
        reason = reason,
        duration = duration
    )
}


abstract class SanctionModelEntity<T : Sanction>(id: EntityID<Long>) : ModelEntity<T>(id) {
    abstract var time : LocalDateTime
    abstract var type : SanctionType
    abstract var reporter : UserEntity?
    abstract var reporterName: String?
    abstract var reason : String?
    abstract var duration : Duration?

    fun isExpired() = duration?.let {
        val expirationTime = time.toInstant(defaultTimeZone) + it
        currentTime.toInstant(defaultTimeZone) >= expirationTime
    } ?: false

    companion object {
        fun new(sanction: Sanction) = when(sanction) {
            is AccountSanction -> AccountSanctionEntity.new(sanction)
            is IPSanction -> IPSanctionEntity.new(sanction)
            is UUIDSanction -> UUIDSanctionEntity.new(sanction)
            is HardwareHashSanction -> HardwareHashSanctionEntity.new(sanction)
        }
    }
}
