package cloud.rsps.rsprot

import com.zenyte.game.world.entity.player.Player
import net.rsprot.protocol.ServerProtCategory
import net.rsprot.protocol.message.OutgoingGameMessage
import net.rsprot.protocol.api.Session as RsprotApiSession

/**
 * @author Jire
 */
interface Session {

    val rsprot: RsprotApiSession<in Session>

    val seed: IntArray

    val player: Player

    fun getHostAddress(): String

    fun process(currentCycle: Long): Boolean

    fun queue(message: OutgoingGameMessage, category: ServerProtCategory): Boolean

    fun queue(message: OutgoingGameMessage): Boolean =
        queue(message, message.category)

    fun flush()

    fun requestClose(): Boolean

    fun isActive(): Boolean

    fun isExpired(currentCycle: Long): Boolean

    fun startPing(): Boolean

    fun endPing(): Boolean

}
