package com.zenyte.game.referral

import com.zenyte.cores.ScheduledExternalizable
import com.zenyte.logger.NearRealityLogger
import org.slf4j.Logger
import java.io.BufferedReader
import java.util.concurrent.CopyOnWriteArraySet

object ReferralIPDatabase : ScheduledExternalizable {
    private val log: Logger = NearRealityLogger.getLogger(ReferralIPDatabase::class.java)

    val ips: MutableSet<String> = CopyOnWriteArraySet()

    override fun getLog(): Logger = log

    override fun writeInterval(): Int = 5

    override fun read(reader: BufferedReader) {
        val loaded = getGSON().fromJson(reader, Array<String>::class.java) ?: return
        loaded.forEach { ips.add(it) }
    }

    override fun write() {
        out(getGSON().toJson(ips.toTypedArray()))
    }

    override fun path(): String = "data/referrals/ips.json"

    override fun ifFileNotFoundOnRead() {
        write()
    }

    fun contains(ip: String): Boolean = ips.contains(ip)

    fun addIp(ip: String) {
        ips.add(ip)
    }
}
