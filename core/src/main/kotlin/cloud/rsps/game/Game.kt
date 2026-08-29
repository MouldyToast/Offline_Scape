package cloud.rsps.game

import com.github.michaelbull.logging.InlineLogger
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.rsprot.protocol.api.NetworkService
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerInfo
import net.rsprot.protocol.game.outgoing.info.util.BuildArea
import java.util.concurrent.locks.LockSupport
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.random.Random

object Game {

    private val logger = InlineLogger()

    const val TICK_RATE_NS = 600 * 1_000_000L // 600ms tick rate

    val players: MutableList<Player> = ObjectArrayList(2000)

    lateinit var networkService: NetworkService<Player>

    fun addPlayer(
        networkService: NetworkService<Player>,
        name: String,
        oldSchoolClientType: OldSchoolClientType = OldSchoolClientType.DESKTOP,
        worldId: Int = PlayerInfo.ROOT_WORLD,
        level: Int = 0,
        x: Int = 3222, z: Int = 3222,
    ): Player? {
        val index = players.size + 1

        val playerInfo = networkService.playerInfoProtocol.alloc(index, oldSchoolClientType)
        val npcInfo = networkService.npcInfoProtocol.alloc(index, oldSchoolClientType)

        val avatar = playerInfo.avatar

        avatar.updateCoord(level, x, z)

        playerInfo.updateCoord(level, x, z)
        npcInfo.updateCoord(worldId, level, x, z)

        playerInfo.updateRenderCoord(worldId, level, x, z)

        val buildArea = BuildArea(
            ((x ushr 3) - 6).coerceAtLeast(0),
            ((z ushr 3) - 6).coerceAtLeast(0)
        )
        playerInfo.updateBuildArea(worldId, buildArea)
        npcInfo.updateBuildArea(worldId, buildArea)

        val player = Player(
            index,
            avatar,
            playerInfo, npcInfo,
            name,
            worldId,
            level,
            x, z,
        )

        return if (players.add(player)) player
        else null
    }

    @OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val start = System.nanoTime()
        networkService = NetworkServiceFactory().build()
        networkService.start()

        /*for (i in 1..1999) {
            val player = addPlayer(
                name = "Player$i",
                oldSchoolClientType = OldSchoolClientType.DESKTOP,
                worldId = PlayerInfo.ROOT_WORLD,
                level = 0,
                x = 3222 + randomFromNegativeXToX(63), z = 3222 + randomFromNegativeXToX(63)
            )!!
            player.updateExtendedInfo()
        }*/

        thread(name = "Game Tick", priority = Thread.MAX_PRIORITY) {
            while (!Thread.interrupted()) {
                val startNanoTime = System.nanoTime()
                try {
                    for (player in players) {
                        player.session?.processIncomingPackets(player)
                    }

                    networkService.playerInfoProtocol.update()
                    networkService.npcInfoProtocol.update()

                    for (player in players) {
                        player.tick()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val endNanoTime = System.nanoTime()
                val elapsedNanoTime = endNanoTime - startNanoTime
                val parkNanoTime = TICK_RATE_NS - elapsedNanoTime
                logger.info {
                    "Tick took ${elapsedNanoTime / 1_000_000L}ms," +
                            " parking for ${parkNanoTime / 1_000_000L}ms"
                }
                if (parkNanoTime > 0) {
                    LockSupport.parkNanos(parkNanoTime)
                }
            }
        }
        val end = System.nanoTime()
        val elapsed = end - start
        logger.info { "Game started in ${elapsed / 1_000_000L}ms" }
    }

    fun randomFromNegativeXToX(x: Int): Int {
        if (x == 0) return 0
        val absX = abs(x)
        return Random.nextInt(from = -absX, until = absX + 1)
    }

}