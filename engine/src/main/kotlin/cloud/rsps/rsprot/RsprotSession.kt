package cloud.rsps.rsprot

import cloud.rsps.net.haproxy.HAProxy.hostAddress
import org.jesse.game.world.WorldThread
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.area.wilderness.WildernessArea
import org.jesse.utils.TimeUnit
import net.rsprot.protocol.ServerProtCategory
import net.rsprot.protocol.message.OutgoingGameMessage
import java.util.concurrent.atomic.AtomicLong
import net.rsprot.protocol.api.Session as RsprotApiSession

/**
 * @author Jire
 */
class RsprotSession(
    override val seed: IntArray,
    override val player: Player,

    override val rsprot: RsprotApiSession<in Session>,
) : Session {

    private val channel = rsprot.ctx.channel()

    private val lastPacketProcessedTick = AtomicLong(WorldThread.getCurrentCycle())

    override fun getHostAddress(): String =
        runCatching {
            channel.hostAddress
        }.getOrNull() ?: UNKNOWN_HOST_ADDRESS

    override fun process(currentCycle: Long): Boolean {
        val processedPackets = rsprot.processIncomingPackets(this)
        val processed = processedPackets > 0
        if (processed) {
            lastPacketProcessedTick.set(currentCycle)
        }
        return processed
    }

    override fun queue(message: OutgoingGameMessage, category: ServerProtCategory): Boolean {
        rsprot.queue(message, category)
        return true
    }

    override fun flush() {
        rsprot.flush()
    }

    override fun requestClose(): Boolean {
        if (rsprot.requestClose()) {
            endPing()
            return true
        }
        return false
    }

    override fun isActive(): Boolean {
        return channel.isActive
    }

    override fun isExpired(currentCycle: Long): Boolean {
        val timeout = when {
            WildernessArea.isWithinWilderness(player) -> SESSION_EXPIRATION_TIMEOUT_WILDERNESS
            else -> SESSION_EXPIRATION_TIMEOUT_SAFE
        }
        return currentCycle - lastPacketProcessedTick.get() >= timeout
    }

    override fun startPing(): Boolean {
        return RsprotPingManager.register(this)
    }

    override fun endPing(): Boolean {
        return RsprotPingManager.unregister(this)
    }

    companion object {

        const val UNKNOWN_HOST_ADDRESS = "unknown"

        private val SESSION_EXPIRATION_TIMEOUT_WILDERNESS = TimeUnit.SECONDS.toTicks(60)
        private val SESSION_EXPIRATION_TIMEOUT_SAFE = TimeUnit.SECONDS.toTicks(20)

    }

}
