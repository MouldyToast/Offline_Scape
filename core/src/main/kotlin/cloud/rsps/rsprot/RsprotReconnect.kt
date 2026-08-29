package cloud.rsps.rsprot

import cloud.rsps.net.haproxy.HAProxy.hostAddress
import com.github.michaelbull.logging.InlineLogger
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.UpdateFlag
import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import net.rsprot.protocol.loginprot.outgoing.LoginResponse

/**
 * @author Jire
 */
object RsprotReconnect {

    private val logger = InlineLogger()

    fun handle(
        responseHandler: GameLoginResponseHandler<Session>,
        block: LoginBlock<XteaKey>
    ) {
        WorldTasksManager.scheduleCreation {
            val playerOptional = World.getPlayer(block.username)
            if (playerOptional.isEmpty) {
                responseHandler.writeFailedResponse(LoginResponse.BadSessionId)
                return@scheduleCreation
            }

            val seed = block.authentication.key

            val player = playerOptional.get()
            val previousSession = player.session

            if (previousSession == null
                || !seed.contentEquals(previousSession.seed)
            ) {
                logger.warn {
                    "Seeds didn't match," +
                            " ours: ${seed.joinToString(",")}" +
                            " vs theirs: ${previousSession?.seed?.joinToString(",")}"
                }

                responseHandler.writeFailedResponse(LoginResponse.BadSessionId)
                return@scheduleCreation
            }

            val playerInfo = player.playerInfo
            playerInfo.onReconnect()
            player.npcInfo.onReconnect()
            player.worldEntityInfo.onReconnect()

            val response = LoginResponse.ReconnectOk(playerInfo)
            val handlerSession = responseHandler.writeSuccessfulResponse(response, block)

            player.session = RsprotSession(block.seed, player, handlerSession)
            player.playerInformation.ip = handlerSession.hostAddress

            player.inventory.refreshAll()
            player.equipment.refreshAll()
            player.skills.refresh()

            val sender = player.packetDispatcher.sender
            sender.playerCamTarget(player.index)
            sender.setMapFlag()

            val updateFlags = player.updateFlags
            //updateFlags.flag(UpdateFlag.MOVEMENT_TYPE)
            updateFlags.flag(UpdateFlag.FACE_ENTITY)
            updateFlags.flag(UpdateFlag.FACE_COORDINATE)

            logger.info { "Player \"${player.name}\" reconnected successfully." }
        }
    }

}
