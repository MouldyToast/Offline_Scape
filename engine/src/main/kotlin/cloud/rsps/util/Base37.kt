package cloud.rsps.util

object Base37 {
    private const val VALID_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789_"

    fun encode(name: String): Long {
        var hash = 0L
        val cleaned = name.lowercase().take(12)
        for (c in cleaned) {
            hash = hash * 37 + (VALID_CHARS.indexOf(c) + 1).toLong()
        }
        return hash
    }

    fun decode(hash: Long): String {
        var remaining = hash
        val sb = StringBuilder()
        while (remaining != 0L) {
            val idx = (remaining % 37).toInt() - 1
            remaining /= 37
            if (idx in VALID_CHARS.indices) sb.append(VALID_CHARS[idx])
        }
        return sb.reverse().toString()
    }
}
