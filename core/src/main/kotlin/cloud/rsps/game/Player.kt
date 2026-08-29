package cloud.rsps.game

import com.github.michaelbull.logging.InlineLogger
import net.rsprot.protocol.api.Session
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcInfo
import net.rsprot.protocol.game.outgoing.info.npcinfo.SetNpcUpdateOrigin
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerAvatar
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerInfo
import net.rsprot.protocol.game.outgoing.interfaces.IfOpenTop
import net.rsprot.protocol.game.outgoing.map.RebuildLogin
import net.rsprot.protocol.game.outgoing.map.util.XteaProvider
import net.rsprot.protocol.game.outgoing.misc.client.ServerTickEnd
import net.rsprot.protocol.game.outgoing.worldentity.SetActiveWorld

class Player(
    val index: Int,

    val avatar: PlayerAvatar,

    val playerInfo: PlayerInfo,
    val npcInfo: NpcInfo,

    val name: String,

    var worldId: Int,

    var level: Int,

    var x: Int,
    var z: Int,
) {

    val zoneX: Int
        get() = x shr 3
    val zoneZ: Int
        get() = z shr 3

    var session: Session<Player>? = null

    var hidden = false

    var combatLevel = 3

    var skullIcon = -1
    var overheadIcon = -1

    var bodyType = 0
    var pronoun = 0

    val colours: IntArray = IntArray(5) { 0 }
    val identKit: IntArray = intArrayOf(0, 10, 18, 26, 33, 36, 42)

    var readyAnim: Int = 808
    var turnAnim: Int = 823
    var walkAnim: Int = 819
    var walkAnimBack: Int = 820
    var walkAnimLeft: Int = 821
    var walkAnimRight: Int = 822
    var runAnim: Int = 824

    fun onLogin() {
        val session = session ?: return
        session.queue(
            RebuildLogin(
                zoneX, zoneZ,
                worldId,
                XteaProvider.ZERO_XTEA_KEY_PROVIDER,
                playerInfo
            )
        )
        session.queue(
            SetActiveWorld(
                if (worldId == PlayerInfo.ROOT_WORLD) {
                    SetActiveWorld.RootWorldType(level)
                } else {
                    SetActiveWorld.DynamicWorldType(worldId, level)
                }
            )
        )
        session.queue(
            IfOpenTop(164)
        )
    }

    fun updateExtendedInfo() {
        avatar.extendedInfo.run {
            setHidden(hidden)
            setName(name)
            setCombatLevel(combatLevel)
            setSkullIcon(skullIcon)
            setOverheadIcon(overheadIcon)
            setBodyType(bodyType)
            setPronoun(pronoun)
            colours.forEachIndexed { slot, value ->
                setColour(slot, value)
            }
            identKit.forEachIndexed { identKitSlot, value ->
                setIdentKit(identKitSlot, value)
            }
            setBaseAnimationSet(
                readyAnim, turnAnim, walkAnim, walkAnimBack, walkAnimLeft, walkAnimRight, runAnim
            )
        }
    }

    fun tick() {
        playerInfo.updateCoord(level, x, z)
        npcInfo.updateCoord(worldId, level, x, z)

        playerInfo.updateRenderCoord(worldId, level, x, z)

        val session = session
        if (session != null) {
            session.queue(SetActiveWorld(SetActiveWorld.RootWorldType(level)))

            session.queue(SetNpcUpdateOrigin(zoneX, zoneZ))

            session.queue(playerInfo.toPacket())
            session.queue(npcInfo.toPacket(worldId))

            //session.queue(MessageGame(0, "Welcome to the game!"))

            session.queue(ServerTickEnd)

            session.flush()
        }

        //println("Player tick: level=$level, x=$x, z=$z")
    }

    private companion object {
        private val logger = InlineLogger()
    }

}