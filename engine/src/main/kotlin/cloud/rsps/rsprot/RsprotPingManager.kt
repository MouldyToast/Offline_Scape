package cloud.rsps.rsprot

import net.rsprot.protocol.game.outgoing.GameServerProtCategory
import net.rsprot.protocol.game.outgoing.misc.client.SendPing
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jire
 */
object RsprotPingManager {

    private val sessions: MutableMap<IntArray, Session> = ConcurrentHashMap()

    @JvmStatic
    fun createSendPing(nanoTime: Long): SendPing {
        val value1 = (nanoTime ushr 32).toInt()
        val value2 = (nanoTime and 0xFFFF_FFFF).toInt()

        return SendPing(value1, value2)
    }

    @JvmStatic
    fun sendPing(sendPing: SendPing, session: Session): Boolean {
        if (session.isActive()
            && session.queue(sendPing, GameServerProtCategory.HIGH_PRIORITY_PROT)
        ) {
            session.flush()
            return true
        }
        return false
    }

    @JvmStatic
    fun process(currentCycle: Long, currentCycleNano: Long): Boolean {
        val sessions = sessions
        if (sessions.isEmpty()) return false

        val nanoTime = System.nanoTime()

        val maxNanoTime = currentCycleNano + 500_000_000L // 500 milliseconds
        if (maxNanoTime < nanoTime) return false

        val iterator = sessions.iterator()
        while (iterator.hasNext()) {
            val (_, session) = iterator.next()
            val created = System.nanoTime()
            if (sendPing(createSendPing(created), session)) {
                val channel = session.rsprot.ctx.channel()
                channel.config().isAutoRead = true
                channel.flush()
                channel.read()
            } else {
                iterator.remove()
            }
        }

        while (!sessions.isEmpty()
            && System.nanoTime() < maxNanoTime
        ) {
            val iterator = sessions.iterator()
            while (iterator.hasNext()) {
                val (_, session) = iterator.next()
                if (!session.isActive()) {
                    iterator.remove()
                    continue
                }

                val channel = session.rsprot.ctx.channel()
                channel.read()
                session.process(currentCycle)
            }
        }

        sessions.clear()
        return true
    }

    @JvmStatic
    fun register(session: Session): Boolean {
        return sessions.putIfAbsent(session.seed, session) == null
    }

    @JvmStatic
    fun unregister(session: Session): Boolean {
        return sessions.remove(session.seed) != null
    }

}
