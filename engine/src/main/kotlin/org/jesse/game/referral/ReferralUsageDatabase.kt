package org.jesse.game.referral

import com.google.gson.reflect.TypeToken
import org.jesse.cores.ScheduledExternalizable
import org.jesse.logger.NearRealityLogger
import org.slf4j.Logger
import java.io.BufferedReader
import java.util.concurrent.ConcurrentHashMap

object ReferralUsageDatabase : ScheduledExternalizable {
    private val log: Logger = NearRealityLogger.getLogger(ReferralUsageDatabase::class.java)

    private val usage: MutableMap<String, Int> = ConcurrentHashMap()

    override fun getLog(): Logger = log

    override fun writeInterval(): Int = 5

    override fun read(reader: BufferedReader) {
        val type = object : TypeToken<Map<String, Int>>() {}.type
        val loaded: Map<String, Int> = getGSON().fromJson(reader, type) ?: return
        usage.putAll(loaded)
    }

    override fun write() {
        out(getGSON().toJson(usage))
    }

    override fun path(): String = "data/referrals/code_usage.json"

    override fun ifFileNotFoundOnRead() {
        write()
    }

    fun increment(code: String) {
        val key = code.lowercase()
        usage.merge(key, 1) { a, b -> a + b }
    }

    fun getUsage(code: String): Int = usage[code.lowercase()] ?: 0
}
