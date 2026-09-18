package org.jesse.game.packet

import org.jesse.game.item.Item
import org.jesse.game.model.HintArrowPosition
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.WalkStep
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container
import org.jesse.game.world.entity.player.container.impl.ContainerType
import org.jesse.game.world.region.DynamicRegion
import org.jesse.game.world.region.Region
import org.jesse.game.world.region.XTEALoader
import io.netty.buffer.ByteBuf
import io.netty.util.ReferenceCountUtil
import io.netty.util.ReferenceCounted
import mgi.types.component.ComponentDefinitions
import net.rsprot.protocol.internal.game.outgoing.info.CoordGrid
import net.rsprot.protocol.common.game.outgoing.inv.InventoryObject
import net.rsprot.protocol.game.outgoing.GameServerProtCategory
import net.rsprot.protocol.game.outgoing.camera.CamLookAtV3
import net.rsprot.protocol.game.outgoing.camera.CamRotateToCoordinateV1
import net.rsprot.protocol.game.outgoing.camera.CamMode
import net.rsprot.protocol.game.outgoing.camera.CamMoveToV3
import net.rsprot.protocol.game.outgoing.camera.CamMoveToArcV3
import net.rsprot.protocol.game.outgoing.camera.CamMoveToCyclesV3
import net.rsprot.protocol.game.outgoing.camera.CamReset
import net.rsprot.protocol.game.outgoing.camera.CamRotateBy
import net.rsprot.protocol.game.outgoing.camera.CamRotateTo
import net.rsprot.protocol.game.outgoing.camera.CamShake
import net.rsprot.protocol.game.outgoing.camera.CamSmoothReset
import net.rsprot.protocol.game.outgoing.camera.CamTargetV4
import net.rsprot.protocol.game.outgoing.camera.OculusSync
import net.rsprot.protocol.game.outgoing.camera.*
import net.rsprot.protocol.game.outgoing.camera.util.CameraEaseFunction
import net.rsprot.protocol.game.outgoing.clan.ClanChannelDelta
import net.rsprot.protocol.game.outgoing.clan.ClanChannelFull
import net.rsprot.protocol.game.outgoing.clan.ClanSettingsDelta
import net.rsprot.protocol.game.outgoing.clan.ClanSettingsFull
import net.rsprot.protocol.game.outgoing.clan.MessageClanChannel
import net.rsprot.protocol.game.outgoing.clan.MessageClanChannelSystem
import net.rsprot.protocol.game.outgoing.clan.VarClan
import net.rsprot.protocol.game.outgoing.clan.VarClan.VarClanData
import net.rsprot.protocol.game.outgoing.clan.VarClan.VarClanIntData
import net.rsprot.protocol.game.outgoing.clan.VarClan.VarClanLongData
import net.rsprot.protocol.game.outgoing.clan.VarClan.VarClanStringData
import net.rsprot.protocol.game.outgoing.clan.VarClanDisable
import net.rsprot.protocol.game.outgoing.clan.VarClanEnable
import net.rsprot.protocol.game.outgoing.clan.*
import net.rsprot.protocol.game.outgoing.clan.VarClan.*
import net.rsprot.protocol.game.outgoing.friendchat.MessageFriendChannel
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelFull
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelFullV2
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelSingleUser
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelSingleUser.AddedFriendChatUser
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelSingleUser.RemovedFriendChatUser
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcInfo
import net.rsprot.protocol.game.outgoing.info.npcinfo.SetNpcUpdateOrigin
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerInfo
import net.rsprot.protocol.game.outgoing.info.util.BuildArea
import net.rsprot.protocol.game.outgoing.info.worldentityinfo.WorldEntityInfo
import net.rsprot.protocol.game.outgoing.interfaces.IfClearInv
import net.rsprot.protocol.game.outgoing.interfaces.IfCloseSub
import net.rsprot.protocol.game.outgoing.interfaces.IfMoveSub
import net.rsprot.protocol.game.outgoing.interfaces.IfOpenSub
import net.rsprot.protocol.game.outgoing.interfaces.IfOpenTop
import net.rsprot.protocol.game.outgoing.interfaces.IfResyncV2
import net.rsprot.protocol.game.outgoing.interfaces.IfSetAngle
import net.rsprot.protocol.game.outgoing.interfaces.IfSetAnim
import net.rsprot.protocol.game.outgoing.interfaces.IfSetColour
import net.rsprot.protocol.game.outgoing.interfaces.IfSetEventsV2
import net.rsprot.protocol.game.outgoing.interfaces.IfSetHide
import net.rsprot.protocol.game.outgoing.interfaces.IfSetModelV2
import net.rsprot.protocol.game.outgoing.interfaces.IfSetNpcHead
import net.rsprot.protocol.game.outgoing.interfaces.IfSetNpcHeadActive
import net.rsprot.protocol.game.outgoing.interfaces.IfSetObject
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPlayerHead
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPlayerModelBaseColour
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPlayerModelBodyType
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPlayerModelObj
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPlayerModelSelf
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPosition
import net.rsprot.protocol.game.outgoing.interfaces.IfSetRotateSpeed
import net.rsprot.protocol.game.outgoing.interfaces.IfSetScrollPos
import net.rsprot.protocol.game.outgoing.interfaces.IfSetText
import net.rsprot.protocol.game.outgoing.interfaces.*
import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull
import net.rsprot.protocol.game.outgoing.inv.UpdateInvPartial
import net.rsprot.protocol.game.outgoing.inv.UpdateInvStopTransmit
import net.rsprot.protocol.game.outgoing.logout.Logout
import net.rsprot.protocol.game.outgoing.logout.LogoutTransfer
import net.rsprot.protocol.game.outgoing.logout.LogoutWithReason
import net.rsprot.protocol.game.outgoing.map.RebuildLoginV2
import net.rsprot.protocol.game.outgoing.map.RebuildNormalV2
import net.rsprot.protocol.game.outgoing.map.RebuildRegionV2
import net.rsprot.protocol.game.outgoing.map.RebuildWorldEntityV4
import net.rsprot.protocol.game.outgoing.map.util.RebuildRegionZone
import net.rsprot.protocol.game.outgoing.misc.client.*
import net.rsprot.protocol.game.outgoing.misc.client.HideLocOps
import net.rsprot.protocol.game.outgoing.misc.client.HideNpcOps
import net.rsprot.protocol.game.outgoing.misc.client.HideObjOps
import net.rsprot.protocol.game.outgoing.misc.client.HintArrow
import net.rsprot.protocol.game.outgoing.misc.client.HiscoreReply
import net.rsprot.protocol.game.outgoing.misc.client.MinimapToggle
import net.rsprot.protocol.game.outgoing.misc.client.PacketGroupStart
import net.rsprot.protocol.game.outgoing.misc.client.ReflectionChecker
import net.rsprot.protocol.game.outgoing.misc.client.ReflectionChecker.InvokeMethod
import net.rsprot.protocol.game.outgoing.misc.player.*
import net.rsprot.protocol.game.outgoing.misc.client.ResetAnims
import net.rsprot.protocol.game.outgoing.misc.client.ResetInteractionMode
import net.rsprot.protocol.game.outgoing.misc.client.SendPing
import net.rsprot.protocol.game.outgoing.misc.client.ServerTickEnd
import net.rsprot.protocol.game.outgoing.misc.client.SetHeatmapEnabled
import net.rsprot.protocol.game.outgoing.misc.client.SetInteractionMode
import net.rsprot.protocol.game.outgoing.misc.client.SiteSettings
import net.rsprot.protocol.game.outgoing.misc.client.UpdateRebootTimerV2
import net.rsprot.protocol.game.outgoing.misc.client.UpdateUid192
import net.rsprot.protocol.game.outgoing.misc.client.UrlOpen
import net.rsprot.protocol.game.outgoing.misc.player.ChatFilterSettings
import net.rsprot.protocol.game.outgoing.misc.player.ChatFilterSettingsPrivateChat
import net.rsprot.protocol.game.outgoing.misc.player.MessageGame
import net.rsprot.protocol.game.outgoing.misc.player.RunClientScript
import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlagV2
import net.rsprot.protocol.game.outgoing.misc.player.SetPlayerOp
import net.rsprot.protocol.game.outgoing.misc.player.TriggerOnDialogAbort
import net.rsprot.protocol.game.outgoing.misc.player.UpdateRunEnergy
import net.rsprot.protocol.game.outgoing.misc.player.UpdateRunWeight
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStatV2
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStockMarketSlot
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStockMarketSlot.ResetStockMarketSlot
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStockMarketSlot.SetStockMarketSlot
import net.rsprot.protocol.game.outgoing.social.*
import net.rsprot.protocol.game.outgoing.social.FriendListLoaded
import net.rsprot.protocol.game.outgoing.social.MessagePrivate
import net.rsprot.protocol.game.outgoing.social.MessagePrivateEcho
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList.OfflineFriend
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList.OnlineFriend
import net.rsprot.protocol.game.outgoing.sound.*
import net.rsprot.protocol.game.outgoing.specific.*
import net.rsprot.protocol.game.outgoing.social.UpdateIgnoreList
import net.rsprot.protocol.game.outgoing.sound.MidiJingle
import net.rsprot.protocol.game.outgoing.sound.MidiSongStop
import net.rsprot.protocol.game.outgoing.sound.MidiSongV2
import net.rsprot.protocol.game.outgoing.sound.MidiSongWithSecondary
import net.rsprot.protocol.game.outgoing.sound.MidiSwap
import net.rsprot.protocol.game.outgoing.sound.SynthSound
import net.rsprot.protocol.game.outgoing.specific.LocAnimSpecific
import net.rsprot.protocol.game.outgoing.specific.MapAnimSpecific
import net.rsprot.protocol.game.outgoing.specific.NpcAnimSpecific
import net.rsprot.protocol.game.outgoing.specific.NpcHeadIconSpecific
import net.rsprot.protocol.game.outgoing.specific.NpcSpotAnimSpecific
import net.rsprot.protocol.game.outgoing.specific.AnimSpecific
import net.rsprot.protocol.game.outgoing.specific.PlayerSpotAnimSpecific
import net.rsprot.protocol.game.outgoing.specific.ProjAnimSpecificV4
import net.rsprot.protocol.game.outgoing.varp.VarpLarge
import net.rsprot.protocol.game.outgoing.varp.VarpReset
import net.rsprot.protocol.game.outgoing.varp.VarpSmall
import net.rsprot.protocol.game.outgoing.varp.VarpSync
import net.rsprot.protocol.game.outgoing.worldentity.SetActiveWorldV2
import net.rsprot.protocol.game.outgoing.zone.header.UpdateZoneFullFollows
import net.rsprot.protocol.game.outgoing.zone.header.UpdateZonePartialEnclosed
import net.rsprot.protocol.game.outgoing.zone.header.UpdateZonePartialFollows
import net.rsprot.protocol.game.outgoing.zone.payload.*
import net.rsprot.protocol.message.ConsumableMessage
import net.rsprot.protocol.game.outgoing.zone.payload.LocAddChange
import net.rsprot.protocol.game.outgoing.zone.payload.LocAnim
import net.rsprot.protocol.game.outgoing.zone.payload.LocDel
import net.rsprot.protocol.game.outgoing.zone.payload.LocMerge
import net.rsprot.protocol.game.outgoing.zone.payload.MapAnim
import net.rsprot.protocol.game.outgoing.zone.payload.MapProjAnimV2
import net.rsprot.protocol.game.outgoing.zone.payload.ObjAdd
import net.rsprot.protocol.game.outgoing.zone.payload.ObjCount
import net.rsprot.protocol.game.outgoing.zone.payload.ObjCustomise
import net.rsprot.protocol.game.outgoing.zone.payload.ObjDel
import net.rsprot.protocol.game.outgoing.zone.payload.ObjEnabledOps
import net.rsprot.protocol.game.outgoing.zone.payload.ObjUncustomise
import net.rsprot.protocol.game.outgoing.zone.payload.SoundArea
import net.rsprot.protocol.message.OutgoingGameMessage
import net.rsprot.protocol.message.ZoneProt
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class PacketSender(private val player: Player) {

    companion object {
        private val REBUILD_REGION_ZONE_PROVIDER = object : RebuildRegionV2.RebuildRegionZoneProvider {
            override fun provide(zoneX: Int, zoneZ: Int, level: Int): RebuildRegionZone? {
                val mapsquareId = (zoneX shr 3 shl 8) + (zoneZ shr 3)
                val region: Region? = World.regions.get(mapsquareId)
                var displayedChunkX = zoneX
                var displayedChunkY = zoneZ
                var displayedPlane = level
                var rotation = 0
                if (region is DynamicRegion) {
                    val hash: Int = region.getLocationHash(
                        zoneX - (zoneX shr 3 shl 3),
                        zoneZ - (zoneZ shr 3 shl 3), level
                    )
                    displayedChunkX = hash and 2047
                    displayedChunkY = hash shr 11 and 2047
                    displayedPlane = hash shr 22 and 3
                    rotation = hash shr 24 and 3
                }
                if (displayedChunkX == 0 && displayedChunkY == 0) {
                    return null
                }
                return RebuildRegionZone(
                    displayedChunkX,
                    displayedChunkY,
                    displayedPlane,
                    rotation,
                )
            }
        }
    }

    private fun isValidInterface(interfaceId: Int, componentId: Int): Boolean {
        if (!ComponentDefinitions.containsInterface(interfaceId, componentId)) {
            return false
        }
        return player.interfaceHandler.isVisible(interfaceId)
    }

    /**
     * Cam lookat packet is used to make the camera look towards
     * a certain coordinate in the build area.
     * It is important to note that if this is sent together with
     * a map reload, whether this packet comes before or after the
     * map reload makes a difference - as the build area itself changes.
     *
     * @param xInBuildArea the dest x coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param yInBuildArea the dest y coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param height the height of the camera
     * @param speed the constant speed at which the camera looks towards
     * to the new coordinate
     * @param acceleration the speed increase as the camera looks
     * towards the end coordinate.
     */
    fun camLookAt(
        xInBuildArea: Int,
        yInBuildArea: Int,
        height: Int,
        speed: Int,
        acceleration: Int,
    ) {
        send {
            CamLookAtV3(
                xInBuildArea,
                yInBuildArea,
                height,
                speed,
                acceleration,
                false,
            )
        }
    }

    /**
     * Cam look at eased angle relative is used to make the camera look towards
     * an angle relative to the current camera angle.
     * One way to think of this packet is that it **adds** values to the
     * x and y angles of the camera.
     *
     * @param xAngle the x angle of the camera to set to.
     * Note that the angle is coerced into a range of 128..383,
     * and incorrectly excludes the third and fifth least significant bits
     * before doing so (by doing [xAngle] & 2027, rather than 2047).
     * @param yAngle the x angle of the camera to set to.
     * Note that the angle incorrectly excludes the third and fifth least significant bits
     * (by doing [xAngle] & 2027, rather than 2047).
     * @param cycles the duration of the movement in client cycles (20ms/cc)
     * @param easing the camera easing function, allowing for finer
     * control over the way it moves from the start coordinate to the end.
     */
    fun camRotateTo(
        xAngle: Int,
        yAngle: Int,
        cycles: Int,
        easing: CameraEaseFunction,
    ) {
        send {
            CamRotateTo(
                xAngle,
                yAngle,
                cycles,
                easing.id,
            )
        }
    }

    /**
     * Cam look at eased angle relative is used to make the camera look towards
     * an angle relative to the current camera angle.
     * One way to think of this packet is that it **adds** values to the
     * x and y angles of the camera.
     *
     * @param xAngle the additional angle to add to the x-axis of the camera.
     * It's worth noting that the x angle of the camera ranges between 128 and
     * 383 (inclusive), and the resulting value is coerced in that range.
     * Negative values are also accepted.
     * Additionally, there is currently a bug in the client that causes the
     * third and the fifth least significant bits of the resulting angle to
     * be discarded due to the code doing (cameraXAngle + [xAngle] & 2027),
     * which is further coerced into the 128-383 range.
     * @param yAngle the additional angle to add to the y-axis of the camera.
     * Unlike the x-axis angle, this one ranges from 0 to 2047 (inclusive),
     * and does not get coerced - instead it will just roll over (e.g. 2047 -> 0).
     * @param cycles the duration of the movement in client cycles (20ms/cc)
     * @param easing the camera easing function, allowing for finer
     * control over the way it moves from the start coordinate to the end.
     */
    fun camRotateBy(
        xAngle: Int,
        yAngle: Int,
        cycles: Int,
        easing: CameraEaseFunction,
    ) {
        send {
            CamRotateBy(
                xAngle,
                yAngle,
                cycles,
                easing.id,
            )
        }
    }

    /**
     * Cam look at eased coord is used to make the camera look towards
     * a certain coordinate with various easing functions.
     *
     * @param xInBuildArea the dest x coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param yInBuildArea the dest y coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param height the height of the camera
     * @param duration the duration of the movement in client cycles (20ms/cc)
     * @param function the camera easing function, allowing for finer
     * control over the way it moves from the start coordinate to the end.
     */
    fun camLookAtEasedCoord(
        xInBuildArea: Int,
        yInBuildArea: Int,
        height: Int,
        duration: Int,
        function: Int,
    ) {
        send {
            CamRotateToCoordinateV1(
                xInBuildArea,
                yInBuildArea,
                height,
                duration,
                function,
            )
        }
    }

    /**
     * Cam mode is used to set the camera into an orb-of-oculus mode,
     * or out of it.
     * @param mode the mode to set in, with the only valid values being
     * 0 for "out of oculus" and 1 for "into oculus".
     */
    fun camMode(mode: Boolean) {
        send {
            CamMode(if (mode) 1 else 0)
        }
    }

    /**
     * Cam move to packet is used to move the position of the camera
     * to a specific coordinate within the current build area.
     * It is important to note that if this is sent together with
     * a map reload, whether this packet comes before or after the
     * map reload makes a difference - as the build area itself changes.
     *
     * @param xInBuildArea the dest x coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param yInBuildArea the dest y coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param height the height of the camera
     * @param speed the constant speed at which the camera moves
     * to the new coordinate
     * @param acceleration the speed increase as the camera moves
     * towards the end coordinate.
     */
    fun camMoveTo(
        xInBuildArea: Int,
        yInBuildArea: Int,
        height: Int,
        speed: Int,
        acceleration: Int,
    ) {
        send {
            CamMoveToV3(
                xInBuildArea,
                yInBuildArea,
                height,
                speed,
                acceleration,
                false,
            )
        }
    }

    /**
     * Camera move to cycles packet is used to move camera
     * to a new coordinate with finer control behind it.
     * @param xInBuildArea the dest x coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param yInBuildArea the dest y coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param height the height of the camera once it arrives at the destination
     * @param duration the duration of the movement in client cycles (20ms/cc)
     * @param maintainFixedAltitude whether the camera moves along the terrain,
     * moving up and down according to bumps in the terrain.
     * If false, the camera will move in a straight line from the starting position
     * towards the end position, ignoring any changes in the terrain.
     * @param function the camera easing function, allowing for finer
     * control over the way it moves from the start coordinate to the end.
     */
    fun camMoveToCycles(
        xInBuildArea: Int,
        yInBuildArea: Int,
        height: Int,
        duration: Int,
        maintainFixedAltitude: Boolean,
        function: CameraEaseFunction,
    ) {
        send {
            CamMoveToCyclesV3(
                xInBuildArea,
                yInBuildArea,
                height,
                duration,
                maintainFixedAltitude,
                function.id,
                false,
            )
        }
    }

    /**
     * Camera move to arc packet is used to move camera
     * to a new coordinate with finer control behind it.
     * This packet differs from [CamMoveTo] in that it will first
     * move through a center coordinate before going towards the destination,
     * creating a `)`-shape movement. An example image of this can be seen
     * [here](https://media.z-kris.com/2024/04/cam%20move%20eased%20circular.png)
     *
     * @param centerXInBuildArea the center x coordinate within the build area,
     * in range of 0 to 103 (inclusive). This marks the middle point between the
     * camera movement through which the camera has to go.
     * @param centerYInBuildArea the center y coordinate within the build area,
     * in range of 0 to 103 (inclusive). This marks the middle point between the
     * camera movement through which the camera has to go.
     * @param destinationXInBuildArea the dest x coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param destinationyInBuildArea the dest y coordinate within the build area,
     * in range of 0 to 103 (inclusive)
     * @param height the height of the camera once it arrives at the destination
     * @param duration the duration of the movement in client cycles (20ms/cc)
     * @param maintainFixedAltitude whether the camera moves along the terrain,
     * moving up and down according to bumps in the terrain.
     * If false, the camera will move in a straight line from the starting position
     * towards the end position, ignoring any changes in the terrain.
     * @param function the camera easing function, allowing for finer
     * control over the way it moves from the start coordinate to the end.
     */
    fun camMoveToArc(
        centerXInBuildArea: Int,
        centerYInBuildArea: Int,
        destinationXInBuildArea: Int,
        destinationyInBuildArea: Int,
        height: Int,
        duration: Int,
        maintainFixedAltitude: Boolean,
        function: Int,
    ) {
        send {
            CamMoveToArcV3(
                centerXInBuildArea,
                centerYInBuildArea,
                destinationXInBuildArea,
                destinationyInBuildArea,
                height,
                duration,
                maintainFixedAltitude,
                function,
                false,
            )
        }
    }

    fun camShake(
        type: Int,
        amount: Int,
    ) = camShake(type, amount, amount, amount)

    /**
     * Cam shake packet is used to make the camera shake around.
     * It is worth noting that multiple different types of shakes
     * can be executed simultaneously, making the camera more and
     * more volatile as a result.
     *
     * The properties of this class are in the exact order as
     * the client reads them, which is consistent across revisions!
     *
     * Camera movements table:
     * ```
     * | Id |   Type  |    Observed Movement   |
     * |----|:-------:|:----------------------:|
     * | 0  |  X-axis |     Left and right     |
     * | 1  |  Y-axis |       Up and down      |
     * | 2  |  Z-axis | Forwards and backwards |
     * | 3  | Y-angle | Panning left and right |
     * | 4  | X-angle |   Panning up and down  |
     * ```
     *
     * @param type the type of the shake (see table above)
     * @param randomAmount the amount of randomness involved.
     * The client will generate a random double from 0.0 to 1.0
     * and multiply it with the [randomAmount] as part of the shaking.
     * This property is called 'shakeIntensity' in the event inspector.
     * @param sineAmount the amount of randomness generated by the
     * sine. Unlike [randomAmount], this is multiplied against the
     * [sineFrequency].
     * This property is called 'movementIntensity' in the event inspector.
     * @param sineFrequency the sine frequency.
     * This property is called 'speed' in the event inspector.
     */
    fun camShake(
        type: Int,
        randomAmount: Int,
        sineAmount: Int,
        sineFrequency: Int,
    ) {
        send {
            CamShake(
                type,
                randomAmount,
                sineAmount,
                sineFrequency,
            )
        }
    }

    /**
     * Cam reset is used to clear out any camera shaking or
     * any sort of movements that might've been previously set.
     * Additionally, unlocks the camera if it has been locked in place.
     */
    fun camReset() {
        send {
            CamReset
        }
    }

    /**
     * Cam smooth reset is used to smoothly reset camera back to the
     * state where the user is in control, instead of it happening
     * instantaneously.
     *
     * Note that the properties of this packet are unused in the Java client.
     *
     * **WARNING:** The client code __requires__ that the camera is in
     * a locked state for this packet's code to be executed in **Java**.
     * If the camera isn't in a locked state, an error condition is hit
     * at the bottom of the function and the player will be kicked out of
     * the game!
     */
    fun camSmoothReset(
        cameraMoveConstantSpeed: Int,
        cameraMoveProportionalSpeed: Int,
        cameraLookConstantSpeed: Int,
        cameraLookProportionalSpeed: Int,
    ) {
        send {
            CamSmoothReset(
                cameraMoveConstantSpeed,
                cameraMoveProportionalSpeed,
                cameraLookConstantSpeed,
                cameraLookProportionalSpeed,
            )
        }
    }

    /**
     * Oculus sync is used to re-synchronize the orb of oculus
     * camera to the local player in the client, if the value
     * does not match up with the client's value.
     * The client initializes this property as zero.
     * @param value the synchronization value, if the client's
     * value is different, oculus camera is moved to the client's local player.
     * Additionally, this value is sent by the client in the
     * [net.rsprot.protocol.game.incoming.misc.user.Teleport] packet whenever
     * the oculus causes the player to teleport.
     */
    fun oculusSync(value: Int) {
        send {
            OculusSync(value)
        }
    }

    /**
     * Player info packet is used to synchronize the state of all players in the world.
     */
    internal fun playerInfo(info: PlayerInfo) {
        val packets = player.infos?.getPackets() ?: return
        val packet = packets.rootWorldInfoPackets.playerInfo.getOrNull() ?: return
        sendOrLogout { packet }
    }



    internal fun worldEntityInfo(info: WorldEntityInfo) {
        val packets = player.infos?.getPackets() ?: return
        val packet = packets.rootWorldInfoPackets.worldEntityInfo.getOrNull() ?: return
        sendOrLogout {
            packet
        }
    }

    internal fun rebuildWorldEntity(
        index: Int,
        baseX: Int,
        baseZ: Int,
        sizeX: Int,
        sizeZ: Int,
        zoneProvider: RebuildWorldEntityV4.RebuildWorldEntityZoneProvider,
    ) {
        send {
            RebuildWorldEntityV4(
                baseX,
                baseZ,
                sizeX,
                sizeZ,
                zoneProvider,
            )
        }
    }

    internal fun npcInfo(
        worldId: Int,
        info: NpcInfo,
    ) {
        val packets = player.infos?.getPackets() ?: return
        val packet = packets.rootWorldInfoPackets.npcInfo.getOrNull() ?: return
        sendOrLogout { packet }
    }

    /**
     * The set npc update origin packet is used to set the relative coordinate for npc info packet.
     * As of revision 222, with the introduction of world entities, it is no longer viable to solely
     * rely on the local player's coordinate, as it may be impacted by a specific world entity.
     * As such, npc info updates should now be prefaced with the origin update to mark the relative coord.
     * For no-world-entity use cases, just pass the player's coordinate in the current build area to
     * get the old behavior.
     *
     * @param originX the x coordinate within the current build area of the player relative
     * to which NPCs will be placed within NPC info packet.
     * @param originY the y coordinate within the current build area of the player relative
     * to which NPCs will be placed within NPC info packet.
     */
    internal fun setNpcUpdateOrigin(
        originX: Int,
        originY: Int,
    ) {
        send {
            SetNpcUpdateOrigin(
                originX,
                originY,
            )
        }
    }

    /**
     * Camera target packet is used to attach to camera on another entity in the scene.
     * If the entity by the specified index cannot be found in the client, the camera
     * will always be focused back on the local player.
     * @param index the index of the player to focus on
     */
    fun playerCamTarget(index: Int) {
        send {
            CamTargetV4(CamTargetV4.PlayerCamTarget(index))
        }
    }

    fun highPriorityPlayerCamTarget(index: Int) {
        sendWithPriority(true) {
            CamTargetV4(CamTargetV4.PlayerCamTarget(index))
        }
    }

    /**
     * Camera target packet is used to attach to camera on another entity in the scene.
     * If the entity by the specified index cannot be found in the client, the camera
     * will always be focused back on the local player.
     * @param index the index of the npc to focus on
     */
    fun npcCamTarget(index: Int) {
        send {
            CamTargetV4(CamTargetV4.NpcCamTarget(index))
        }
    }

    /**
     * Set active world packet is used to set the currently active world in the client,
     * allowing for various world-specific packets to perform changes to a different world
     * than the usual root.
     * Packets such as zone updates, player info, NPC info are a few examples of what may be sent afterwards.
     * @param worldId the world id to update next, -1 for root
     * @param level the level to update at
     */
    fun setActiveWorld(
        worldId: Int,
        level: Int,
    ) {
        send {
            SetActiveWorldV2(
                if (worldId == -1) {
                    SetActiveWorldV2.RootWorldType(level)
                } else {
                    SetActiveWorldV2.DynamicWorldType(worldId, level)
                },
            )
        }
    }

    /**
     * Camera target packet is used to attach to camera on another entity in the scene.
     * If the entity by the specified index cannot be found in the client, the camera
     * will always be focused back on the local player.
     * Furthermore, depth buffering (z-buffer) will be enabled.
     * @param index the index of the worldentity to focus on
     * @param cameraLockedPlayerIndex the index of the player on the local player's world entity whom
     * to lock the camera onto.
     */
    fun worldEntityCamTarget(
        index: Int,
        cameraLockedPlayerIndex: Int,
    ) {
        sendWithPriority(false) {
            CamTargetV4(CamTargetV4.WorldEntityTarget(index))
        }
    }

    /**
     * Hide npc ops packet is used to hide the right-click menu of all NPCs across the game.
     * @param hidden whether to hide all the click options of NPCs.
     */
    fun hideNpcOps(hidden: Boolean) {
        send {
            HideNpcOps(hidden)
        }
    }

    /**
     * Hide obj ops packet is used to hide the right-click menu of all ground items.
     * @param hidden whether to hide all the click options of ground items.
     */
    fun hideObjOps(hidden: Boolean) {
        send {
            HideObjOps(hidden)
        }
    }

    /**
     * Hide loc ops packet is used to hide the right-click menu of all locs across the game.
     * @param hidden whether to hide all the click options of locs.
     */
    fun hideLocOps(hidden: Boolean) {
        send {
            HideLocOps(hidden)
        }
    }

    /**
     * Sets the interaction mode for a specific world.
     *
     * Tile interaction modes table:
     *
     * ```md
     * | Id |   Type   |
     * |:--:|:--------:|
     * |  0 | Disabled |
     * |  1 |   Walk   |
     * |  2 |  Heading |
     * ```
     *
     * Entity interaction modes table:
     *
     * ```md
     * | Id |     Type     |
     * |:--:|:------------:|
     * |  0 |   Disabled   |
     * |  1 |    Enabled   |
     * |  2 | Examine Only |
     * ```
     *
     * @param worldId the id of the world to modify. If the value is -2, the default
     * behaviour for all worlds is changed.
     * @param tileInteractionMode sets the tile interaction mode. See the table above.
     * @param entityInteractionMode sets the entity interaction mode. See the table above.
     */
    fun setInteractionMode(
        worldId: Int,
        tileInteractionMode: Int,
        entityInteractionMode: Int,
    ) {
        send {
            SetInteractionMode(worldId, tileInteractionMode, entityInteractionMode)
        }
    }

    /**
     * Resets the interaction mode for a specific world.
     * @param worldId the id of the world to modify.
     */
    fun resetInteractionMode(worldId: Int) {
        send {
            ResetInteractionMode(worldId)
        }
    }

    /**
     * If resync is a way to compress sending initial interfaces
     * to the client in a single packet, reducing the overall packet count
     * sent, as well as bandwidth used by a tiny amount (by not sending packet
     * headers and size with each one).
     * @param topLevelInterface the top-level interface being opened
     * @param subInterfaces the sub interfaces being opened in this batch
     * @param events the interface events being set in this batch
     */
    internal fun ifResync(
        topLevelInterface: Int,
        subInterfaces: List<IfResyncV2.SubInterfaceMessage>,
        events: List<IfResyncV2.InterfaceEventsMessage>,
    ) {
        send {
            IfResyncV2(
                topLevelInterface,
                subInterfaces,
                events,
            )
        }
    }

    internal fun ifResync(message: IfResyncV2) {
        send {
            message
        }
    }

    /**
     * If clear-inv messaged are used to clear all objs on any if-1 type
     * component. As there are very few if-1 type old interfaces remaining,
     * this packet is mostly unused nowadays.
     * @param interfaceId the id of the interface on which the inv exists
     * @param componentId the id of the component on the [interfaceId] to be cleared
     */
    fun ifClearInv(
        interfaceId: Int,
        componentId: Int,
    ) {
        send {
            IfClearInv(
                interfaceId,
                componentId,
            )
        }
    }

    /**
     * If close-sub messages are used to close sublevel interfaces.
     * @param interfaceId the interface on which the sublevel interface is opened
     * @param componentId the component on which the sublevel interface is opened
     */
    fun ifCloseSub(
        interfaceId: Int,
        componentId: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfCloseSub(
                interfaceId,
                componentId,
            )
        }
    }

    /**
     * If move-sub messages are used to move a sublevel interface from
     * one position to another, typically when changing top-level interfaces.
     * @param sourceInterfaceId the current interface on which the interface that's
     * being moved is opened on
     * @param sourceComponentId the current component of the [sourceInterfaceId] on which
     * the interface that's being moved is opened on
     * @param destinationInterfaceId the destination interface on which the sub-interface
     * should be opened
     * @param destinationComponentId the component id on the [destinationInterfaceId] on
     * which the sub-interface should be opened
     */
    fun ifMoveSub(
        sourceInterfaceId: Int,
        sourceComponentId: Int,
        destinationInterfaceId: Int,
        destinationComponentId: Int,
    ) {
        /*player.moveVisibleInterface(
            sourceInterfaceId,
            sourceComponentId,
            destinationInterfaceId,
            destinationComponentId
        )*/

        send {
            IfMoveSub(
                sourceInterfaceId,
                sourceComponentId,
                destinationInterfaceId,
                destinationComponentId,
            )
        }
    }

    /**
     * If open-sub messages are used to open non-root interfaces
     * on root interfaces.
     * @param destinationInterfaceId the destination interface on which the sub
     * interface is being opened
     * @param destinationComponentId the component on the destination interface
     * on which the sub interface is being opened
     * @param interfaceId the sub interface id
     * @param type the type of the interface to be opened as (modal, overlay, client)
     */
    fun ifOpenSub(
        destinationInterfaceId: Int,
        destinationComponentId: Int,
        interfaceId: Int,
        type: Int,
    ) {
        send {
            val event =
                IfOpenSub(
                    destinationInterfaceId,
                    destinationComponentId,
                    interfaceId,
                    type,
                )
            event
        }
    }

    /**
     * If open-top messages are sent to open 'root' interfaces.
     * These root interfaces are the base to everything, and every
     * other sub-interface will be opened on a component on this top interface.
     * @param interfaceId the id of the top-level interface to open
     */
    fun ifOpenTop(interfaceId: Int) {
        send {
            IfOpenTop(interfaceId)
        }
    }

    /**
     * If set-angle is used to change the angle of a model on an interface component.
     * @param interfaceId the interface id on which the component resides
     * @param componentId the component id on which the model resides
     * @param angleX the new x model angle to set to, a value from 0 to 2047 (inclusive)
     * @param angleY the new y model angle to set to, a value from 0 to 2047 (inclusive)
     * @param zoom the zoom of the model, defaults to a value of 100 in the client.
     * The greater the [zoom] value, the smaller the model will appear - it is inverted.
     */
    fun ifSetAngle(
        interfaceId: Int,
        componentId: Int,
        angleX: Int,
        angleY: Int,
        zoom: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetAngle(
                interfaceId,
                componentId,
                angleX,
                angleY,
                zoom,
            )
        }
    }

    /**
     * If set-anim is used to make a model animate on a component.
     * @param interfaceId the id of the interface on which the model resides
     * @param componentId the id of the component on which the model resides
     * @param anim the id of the animation to play, or -1 to reset the animation
     */
    fun ifSetAnim(
        interfaceId: Int,
        componentId: Int,
        anim: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetAnim(
                interfaceId,
                componentId,
                anim,
            )
        }
    }

    fun ifSetColour(
        interfaceId: Int,
        componentId: Int,
        packed: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        val red: Int = packed ushr 10 and 0x1F
        val green: Int = packed ushr 5 and 0x1F
        val blue: Int = packed and 0x1F

        ifSetColour(
            interfaceId,
            componentId,
            red,
            green,
            blue
        )
    }

    /**
     * If set-colour is used to set the colour of a text component.
     * @param interfaceId the id of the interface on which the text resides
     * @param componentId the id of the component on which the text resides
     * @param red the value of the red colour, ranging from 0 to 31 (inclusive)
     * @param green the value of the green colour, ranging from 0 to 31 (inclusive)
     * @param blue the value of the blue colour, ranging from 0 to 31 (inclusive)
     */
    fun ifSetColour(
        interfaceId: Int,
        componentId: Int,
        red: Int,
        green: Int,
        blue: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetColour(
                interfaceId,
                componentId,
                red,
                green,
                blue,
            )
        }
    }

    /**
     * Interface events are sent to set/unlock various options on a component,
     * such as button clicks and dragging.
     * @param interfaceId the interface id on which to set the events
     * @param componentId the component on that interface to set the events on
     * @param start the start subcomponent id
     * @param end the end subcomponent id (inclusive)
     * @param events the bitpacked events
     */
    fun ifSetEvents(
        interfaceId: Int,
        componentId: Int,
        start: Int,
        end: Int,
        events: Int,
    ) {
// Breaks any interface that sends events before opening like item exchange
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            // V2 split: button ops (old bits 1-10) move to events2, zero-indexed
            val events2 = (events ushr 1) and 0x3FF
            // events1: everything except button ops (clear bits 1-10)
            val events1 = events and ((0x3FF shl 1).inv())
            val packet =
                IfSetEventsV2(
                    interfaceId,
                    componentId,
                    start,
                    end,
                    events1,
                    events2,
                )
            /*player.appendIfSetEvent(packet)*/ // TODO: Implement this
            packet
        }
    }

    fun ifSetEvents(event: IfSetEventsV2) {
        send {
            event
        }
    }

    /**
     * If set-hide is used to hide or unhide a component and its children on an interface.
     * @param interfaceId the interface id on which the component to hide or unhide resides on
     * @param componentId the component on the [interfaceId] to hide or unhide
     * @param visible whether to unhide the component
     */
    fun ifSetHide(
        interfaceId: Int,
        componentId: Int,
        visible: Boolean,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetHide(
                interfaceId,
                componentId,
                !visible,
            )
        }
    }

    /**
     * If set-hide is used to hide or unhide a component and its children on an interface.
     * @param interfaceId the interface id on which the component to hide or unhide resides on
     * @param componentId the component on the [interfaceId] to hide or unhide
     * @param hidden whether to hide or unhide the component
     */
    fun ifSetHideHidden(
        interfaceId: Int,
        componentId: Int,
        hidden: Boolean,
    ) = ifSetHide(interfaceId, componentId, !hidden)

    /**
     * If set model packet is used to set a model to render on an interface.
     * The component must be of model type for this to succeed.
     * @param interfaceId the interface id on which to set the events
     * @param componentId the component on that interface to set the events on
     * @param model the id of the model to render.
     */
    fun ifSetModel(
        interfaceId: Int,
        componentId: Int,
        model: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetModelV2(
                interfaceId,
                componentId,
                model,
            )
        }
    }

    /**
     * If set-npc-head is used to set a npc's chathead on an interface, commonly
     * in dialogues.
     * @param interfaceId the interface id on which the model resides
     * @param componentId the component id on which the model resides
     * @param npc the id of the npc config whose head to set as the model
     */
    fun ifSetNpcHead(
        interfaceId: Int,
        componentId: Int,
        npc: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetNpcHead(
                interfaceId,
                componentId,
                npc,
            )
        }
    }

    /**
     * If set-npc-head-active is used to set a npc's chathead on an interface, commonly
     * in dialogues. Rather than taking the id of the npc config, this function
     * takes the index of the npc in the world. Npc's model is looked up from the
     * client through npc info, allowing for the chatbox to render a custom-built
     * npc with completely dynamic models, rather than the pre-defined configs.
     * @param interfaceId the interface id on which the model resides
     * @param componentId the component id on which the model resides
     * @param index the index of the npc in the world
     */
    fun ifSetNpcHeadActive(
        interfaceId: Int,
        componentId: Int,
        index: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetNpcHeadActive(
                interfaceId,
                componentId,
                index,
            )
        }
    }

    /**
     * Sets an object on an interface component.
     * @param interfaceId the interface on which the component resides
     * @param componentId the component on which the obj resides
     * @param obj the id of the obj to set on the component
     * @param count the count of the obj, used to obtain a different variant
     * of the model of the obj
     */
    fun ifSetItem(
        interfaceId: Int,
        componentId: Int,
        obj: Int,
        count: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetObject(
                interfaceId,
                componentId,
                obj,
                count,
            )
        }
    }

    /**
     * If set-player-head is used to set the local player's chathead on an interface,
     * commonly used for dialogues.
     * @param interfaceId the id of the interface on which the chathead model resides
     * @param componentId the id of the component on which the chathead model resides
     */
    fun ifSetPlayerHead(
        interfaceId: Int,
        componentId: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetPlayerHead(
                interfaceId,
                componentId,
            )
        }
    }

    /**
     * If set-player-model basecolour packet is used to set the ident kit colour
     * of a customized player model on an interface. This allows one to build
     * a completely unique player model up without using anyone as reference.
     * The colouring logic is identical to that found within Appearance for players.
     * @param interfaceId the id of the interface on which the model resides
     * @param componentId the id of the component on which the model resides
     * @param index the index of the colour, ranging from 0 to 4 (inclusive)
     * @param colour the value of the colour, ranging from 0 to 255 (inclusive)
     */
    fun ifSetPlayerModelBaseColour(
        interfaceId: Int,
        componentId: Int,
        index: Int,
        colour: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetPlayerModelBaseColour(
                interfaceId,
                componentId,
                index,
                colour,
            )
        }
    }

    /**
     * If setplayermodel bodytype is used to change the current body-type of
     * a player model on an interface, making the client prefer swap out
     * the models for the respective type.
     * @param interfaceId the id of the interface on which the model resides
     * @param componentId the id of the component on which the model resides
     * @param bodyType the new body-type to set to the player model
     */
    fun ifSetPlayerModelBodyType(
        interfaceId: Int,
        componentId: Int,
        bodyType: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetPlayerModelBodyType(
                interfaceId,
                componentId,
                bodyType,
            )
        }
    }

    /**
     * If setplayermodel obj is used to set a worn obj on a player model.
     * @param interfaceId the id of the interface on which the model resides
     * @param componentId the id of the component on which the model resides
     * @param obj the id of the obj. Interestingly, the client reads a 32-bit int
     * for the obj, even though configs having a strict 32767/65535 limitation elsewhere
     * in the client.
     */
    fun ifSetPlayerModelObj(
        interfaceId: Int,
        componentId: Int,
        obj: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetPlayerModelObj(
                interfaceId,
                componentId,
                obj,
            )
        }
    }

    /**
     * If setplayermodel self is used to set the player model on an interface
     * to that of the local player.
     * @param interfaceId the id of the interface on which the model resides
     * @param componentId the id of the component on which the model resides
     * @param copyObjs whether to copy all the worn objs over as well
     */
    fun ifSetPlayerModelSelf(
        interfaceId: Int,
        componentId: Int,
        copyObjs: Boolean,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetPlayerModelSelf(
                interfaceId,
                componentId,
                copyObjs,
            )
        }
    }

    /**
     * If set-position events are used to move a component on an interface.
     * @param interfaceId the interface on which the component to move exists
     * @param componentId the component id to move
     * @param x the x coordinate to move to
     * @param y the y coordinate to move to
     */
    fun ifSetPosition(
        interfaceId: Int,
        componentId: Int,
        x: Int,
        y: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetPosition(
                interfaceId,
                componentId,
                x,
                y,
            )
        }
    }

    /**
     * If set-rotate-speed packet is used to make a model rotate
     * according to the client's update counter. This only has an effect
     * on model-type components.
     * @param interfaceId the id of the interface on which the component to rotate
     * lives.
     * @param componentId the component on which the model to rotate lives
     * @param xSpeed the speed of the x angle of the model to rotate by
     * each client cycle (20ms/cc), with a value of 1 being equal to 1/2048th of a
     * full circle
     * @param ySpeed the speed of the y angle of the model to rotate by
     * each client cycle (20ms/cc), with a value of 1 being equal to 1/2048th of a
     * full circle
     */
    fun ifSetRotateSpeed(
        interfaceId: Int,
        componentId: Int,
        xSpeed: Int,
        ySpeed: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetRotateSpeed(
                interfaceId,
                componentId,
                xSpeed,
                ySpeed,
            )
        }
    }

    /**
     * If set scroll pos messages are used to force the scroll position
     * of a layer component.
     * @param interfaceId the interface on which the scroll layer exists
     * @param componentId the component id of the scroll layer
     * @param scrollPos the scroll position to set to
     */
    fun ifSetScrollPos(
        interfaceId: Int,
        componentId: Int,
        scrollPos: Int,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetScrollPos(
                interfaceId,
                componentId,
                scrollPos,
            )
        }
    }

    /**
     * If set-text packet is used to set the text on a text component.
     * @param interfaceId the interface id on which the text resides
     * @param componentId the component id on the interface on which the text
     * resides
     * @param text the text to assign
     */
    fun ifSetText(
        interfaceId: Int,
        componentId: Int,
        text: String,
    ) {
//        if (!isValidInterface(interfaceId, componentId)) {
//            return
//        }

        send {
            IfSetText(
                interfaceId,
                componentId,
                text,
            )
        }
    }

    fun updateInvFull(container: Container) {
        updateInvFull(container, container.type)
    }

    fun updateInvFull(container: Container, type: ContainerType) {
        updateInvFull(
            type.id,
            container.containerSize
        ) { slot ->
            val item = container.get(slot)
            if (item == null) {
                InventoryObject.NULL
            } else {
                InventoryObject(item.id, item.amount)
            }
        }
    }

    fun updateInvFull(
        interfacePacked: Int,
        inventoryId: Int,
        vararg items: Item,
    ) = updateInvFull(
        interfacePacked shr 16,
        interfacePacked and 0xFFFF,
        inventoryId,
        *items
    )

    fun updateInvFull(
        interfaceId: Int,
        componentId: Int,
        inventoryId: Int,
        vararg items: Item,
    ) {
        send {
            UpdateInvFull(
                interfaceId,
                componentId,
                inventoryId,
                items.size,
            ) { slot ->
                val item = items.getOrNull(slot)
                if (item == null) {
                    InventoryObject.NULL
                } else {
                    InventoryObject(item.id, item.amount)
                }
            }
        }
    }

    /**
     * Update inv full is used to perform a full synchronization of an inventory's
     * contents to the client.
     * The client will wipe any existing cache of this inventory prior to performing
     * an update.
     * While not very well known, it is possible to send less objs than the inventory's
     * respective capacity in the cache. As an example, if the inventory's capacity
     * in the cache is 500, but the inv only has a single object at the first slot,
     * a simple compression method is to send the capacity as 1 to the client,
     * and only inform of the single object that does exist - all others would be
     * presumed non-existent. There is no need to transmit all 500 slots when
     * the remaining 499 are not filled, saving considerable amount of space in the
     * process.
     *
     * @param inventoryId the id of the inventory to update
     * @param capacity the capacity of the inventory being transmitted in this
     * update.
     */
    internal fun updateInvFull(
        inventoryId: Int,
        capacity: Int,
        provider: UpdateInvFull.ObjectProvider,
    ) {
        send {
            UpdateInvFull(
                inventoryId,
                capacity,
                provider,
            )
        }
    }

    /**
     * Update inv full is used to perform a full synchronization of an inventory's
     * contents to the client.
     * The client will wipe any existing cache of this inventory prior to performing
     * an update.
     * While not very well known, it is possible to send less objs than the inventory's
     * respective capacity in the cache. As an example, if the inventory's capacity
     * in the cache is 500, but the inv only has a single object at the first slot,
     * a simple compression method is to send the capacity as 1 to the client,
     * and only inform of the single object that does exist - all others would be
     * presumed non-existent. There is no need to transmit all 500 slots when
     * the remaining 499 are not filled, saving considerable amount of space in the
     * process.
     *
     * @param inventoryId the id of the inventory to update
     * @param capacity the capacity of the inventory being transmitted in this
     * update.
     */
    internal fun updateInvFull(
        interfaceId: Int,
        componentId: Int,
        inventoryId: Int,
        capacity: Int,
        provider: UpdateInvFull.ObjectProvider,
    ) {
        send {
            UpdateInvFull(
                interfaceId,
                componentId,
                inventoryId,
                capacity,
                provider,
            )
        }
    }

    /**
     * Update inv partial is used to send an update of an inventory
     * that doesn't include the entire inventory.
     * This is typically used after the first [UpdateInvFull]
     * update has been performed, as subsequent updates tend to
     * be smaller, such as picking up an object - only
     * a single slot in the player's inventory would change, not warranting
     * the full 28-slot update as a result.
     *
     * A general rule of thumb for when to use partial updates is
     * if the percentage of modified objects is less than two thirds
     * of that of the highest modified index.
     * So, if our inventory has a capacity of 1,000, and we have sparsely
     * modified 500 indices throughout that, including the 999th index,
     * it is more beneficial to use the partial inventory update,
     * as the total bandwidth used by it is generally going to be less
     * than what the full update would require.
     * If, however, there is a continuous sequence of indices that
     * have been modified, such as everything from indices 0 to 100,
     * it is more beneficial to use the full update and set the
     * capacity to 100 in that.
     *
     * Below is a percentage-breakdown of how much more bandwidth the partial update
     * requires per object basis given different criteria, compared to the full update:
     * ```
     * 14.3% if slot < 128 && count >= 255
     * 28.6% if slot >= 128 && count >= 255
     * 33.3% if slot < 128 && count < 255
     * 66.6% if slot >= 128 && count < 255
     * ```
     *
     * While it is impossible to truly estimate what the exact threshold is,
     * this provides a good general idea of when to use either packet.
     *
     * @param inventoryId the id of the inventory to update
     * @param provider the indexed object provider
     */
    internal fun updateInvPartial(
        inventoryId: Int,
        provider: UpdateInvPartial.IndexedObjectProvider,
    ) {
        send {
            UpdateInvPartial(
                inventoryId,
                provider,
            )
        }
    }

    fun updateInvPartial(
        container: Container
    ) {
        updateInvPartial(
            container.type.id,
            object : UpdateInvPartial.IndexedObjectProvider(container.modifiedSlots.iterator()) {
                override fun provide(slot: Int): Long {
                    val item = container.get(slot)
                    return if (item == null) {
                        InventoryObject(slot, -1, -1)
                    } else {
                        InventoryObject(slot, item.id, item.amount)
                    }
                }
            }
        )
    }

    /**
     * Update inv partial is used to send an update of an inventory
     * that doesn't include the entire inventory.
     * This is typically used after the first [UpdateInvFull]
     * update has been performed, as subsequent updates tend to
     * be smaller, such as picking up an object - only
     * a single slot in the player's inventory would change, not warranting
     * the full 28-slot update as a result.
     *
     * A general rule of thumb for when to use partial updates is
     * if the percentage of modified objects is less than two thirds
     * of that of the highest modified index.
     * So, if our inventory has a capacity of 1,000, and we have sparsely
     * modified 500 indices throughout that, including the 999th index,
     * it is more beneficial to use the partial inventory update,
     * as the total bandwidth used by it is generally going to be less
     * than what the full update would require.
     * If, however, there is a continuous sequence of indices that
     * have been modified, such as everything from indices 0 to 100,
     * it is more beneficial to use the full update and set the
     * capacity to 100 in that.
     *
     * Below is a percentage-breakdown of how much more bandwidth the partial update
     * requires per object basis given different criteria, compared to the full update:
     * ```
     * 14.3% if slot < 128 && count >= 255
     * 28.6% if slot >= 128 && count >= 255
     * 33.3% if slot < 128 && count < 255
     * 66.6% if slot >= 128 && count < 255
     * ```
     *
     * While it is impossible to truly estimate what the exact threshold is,
     * this provides a good general idea of when to use either packet.
     *
     * @param inventoryId the id of the inventory to update
     * @param provider the indexed object provider
     */
    internal fun updateInvPartial(
        interfaceId: Int,
        componentId: Int,
        inventoryId: Int,
        provider: UpdateInvPartial.IndexedObjectProvider,
    ) {
        send {
            UpdateInvPartial(
                interfaceId,
                componentId,
                inventoryId,
                provider,
            )
        }
    }

    /**
     * Update inv stop transmit is used by the server to inform the client
     * that no more updates for a given inventory are expected.
     * In OldSchool RuneScape, this is sent whenever an interface that's
     * linked to the inventory is sent.
     * In doing so, the client will wipe its cache of the given inventory.
     * There is no technical reason to send this, however, as it doesn't
     * prevent anything from functioning as normal.
     * @param inventoryId the id of the inventory to stop listening to
     */
    fun updateInvStopTransmit(inventoryId: Int) {
        send {
            UpdateInvStopTransmit(inventoryId)
        }
    }

    /**
     * Log out messages are used to tell the client the player
     * has finished playing, which then causes the client to close
     * the socket, and reset a lot of properties as a result.
     */
    fun logout() {
        send {
            Logout
        }
    }

    /**
     * Logout transfer packet is used for world-hopping purposes,
     * making the client connect to a different world instead.
     *
     * World properties table:
     * ```
     * | Flag       |           Type          |
     * |------------|:-----------------------:|
     * | 0x1        |         Members         |
     * | 0x2        |        Quick chat       |
     * | 0x4        |        PvP world        |
     * | 0x8        |        Lootshare        |
     * | 0x10       |    Dedicated activity   |
     * | 0x20       |       Bounty world      |
     * | 0x40       |        PvP Arena        |
     * | 0x80       | High level only - 1500+ |
     * | 0x100      |         Speedrun        |
     * | 0x200      |  Existing players only  |
     * | 0x400      |  Extra-hard wilderness  |
     * | 0x800      |      Dungeoneering      |
     * | 0x1000     |      Instance shard     |
     * | 0x2000     |         Rentable        |
     * | 0x4000     |    Last man standing    |
     * | 0x8000     |       New players       |
     * | 0x10000    |        Beta world       |
     * | 0x20000    |      Staff IP only      |
     * | 0x40000    | High level only - 2000+ |
     * | 0x80000    | High level only - 2400+ |
     * | 0x100000   |        VIPs only        |
     * | 0x200000   |       Hidden world      |
     * | 0x400000   |       Legacy only       |
     * | 0x800000   |         EoC only        |
     * | 0x1000000  |       Behind proxy      |
     * | 0x2000000  |       No save mode      |
     * | 0x4000000  |     Tournament world    |
     * | 0x8000000  |    Fresh start world    |
     * | 0x10000000 | High level only - 1750+ |
     * | 0x20000000 |      Deadman world      |
     * | 0x40000000 |      Seasonal world     |
     * | 0x80000000 |  External partner only  |
     * ```
     *
     * @param host the ip address of the new world
     * @param id the id of the new world
     * @param properties the flags of the new world
     */
    fun logoutTransfer(
        host: String,
        id: Int,
        properties: Int,
    ) {
        send {
            LogoutTransfer(host, id, properties)
        }
    }

    /**
     * Logout with reason, much like [Logout], is used to
     * log the player out of the game. The only difference here
     * is that the user will be given a reason for why they were
     * logged out of the game, e.g. inactive for too long.
     *
     * Logout reasons table:
     * ```
     * | Id |   Type   |
     * |----|:--------:|
     * | 1  |  Kicked  |
     * | 2  | Updating |
     * ```
     *
     * @param reason the id of the reason to display (see table above)
     */
    fun logoutWithReason(reason: Int) {
        send {
            LogoutWithReason(reason)
        }
    }

    /**
     * Rebuild login is sent as part of the login procedure as the very first packet,
     * as this one contains information about everyone's low resolution position, allowing
     * the player information packet to be initialized properly.
     * @param zoneX the x coordinate of the local player's current zone.
     * @param zoneY the y coordinate of the local player's current zone.
     * @param playerInfo player info object used to initialize player info upon logging in.
     */
    internal fun rebuildLogin(
        zoneX: Int,
        zoneY: Int,
        worldArea: Int,
        playerInfo: PlayerInfo,
    ) {
        syncBuildArea()
        send {
            RebuildLoginV2(
                zoneX,
                zoneY,
                worldArea,
                playerInfo,
            )
        }

        setActiveWorld(player.worldEntityId, player.position.plane)
    }

    /**
     * Rebuild normal is sent when the game requires a map reload without being in instances.
     * @param zoneX the x coordinate of the local player's current zone.
     * @param zoneY the y coordinate of the local player's current zone.
     */
    fun rebuildNormal(
        zoneX: Int,
        zoneY: Int,
        worldArea: Int,
    ) {
        // No world areas are properly defined by Jagex right now, so falling back to our
        // implementation that gets rid of the debug messages.

        syncBuildArea()
        send {
            RebuildNormalV2(
                zoneX,
                zoneY,
                worldArea,
            )
        }
        setActiveWorld(player.worldEntityId, player.position.plane)
    }

    /**
     * Rebuild region is used to send a dynamic map to the client,
     * built up out of zones (8x8x1 tiles), allowing for any kind
     * of unique instancing to occur.
     * @param zoneX the x coordinate of the center zone around
     * which the build area is built
     * @param zoneY the y coordinate of the center zone around
     * which the build area is built
     * @param reload whether to forcibly reload the map client-sided.
     * If this property is false, the client will only reload if
     * the last rebuild had difference [zoneX] or [zoneY] coordinates
     * than this one.
     * @param zoneProvider the function for providing zones
     */
    internal fun rebuildRegion(
        zoneX: Int,
        zoneY: Int,
        reload: Boolean,
    ) {
        syncBuildArea()
        send {
            RebuildRegionV2(
                zoneX,
                zoneY,
                reload,
                REBUILD_REGION_ZONE_PROVIDER
            )
        }
        setActiveWorld(player.worldEntityId, player.position.plane)
    }

    internal fun syncBuildArea() {
        val worldId = player.worldEntityId
        val location = player.position
        val buildArea = BuildArea(
            ((location.x ushr 3) - 6).coerceAtLeast(0),
            ((location.y ushr 3) - 6).coerceAtLeast(0)
        )

        val rsPlayerInfo = player.playerInfo
        val rsProtNpcInfo = player.npcInfo

        player.infos?.updateRootBuildArea(buildArea)
    }

    /**
     * Set heatmap enabled packet is used to either enable or
     * disabled the heatmap, which is rendered over the
     * world map in OldSchool.
     * This packet utilizes high resolution coordinate info
     * about all the players of the game through player info
     * packet, so in order for it to properly function,
     * high resolution information must be sent for everyone
     * in the game.
     */
    fun setHeatmapEnabled(enabled: Boolean) {
        send {
            SetHeatmapEnabled(enabled)
        }
    }

    /**
     * Hint arrow packets are used to render a hint arrow
     * at a specific player, NPC or a tile.
     * Only a single hint arrow can exist at a time in OldSchool.
     * @param type the hint arrow type to render.
     */
    private fun hintArrow(type: HintArrow.HintArrowType) {
        send {
            HintArrow(type)
        }
    }

    /**
     * Sets a hint arrow on a specific NPC in the world.
     * @param index the index of the npc to set the hint arrow on.
     */
    fun npcHintArrow(index: Int) {
        hintArrow(HintArrow.NpcHintArrow(index))
    }

    /**
     * Sets a hint arrow on a specific player in the world.
     * @param index the index of the player to set the hint arrow on.
     */
    fun playerHintArrow(index: Int) {
        hintArrow(HintArrow.PlayerHintArrow(index))
    }

    fun tileHintArrowCentered(
        x: Int,
        y: Int,
        height: Int,
    ) {
        hintArrow(HintArrow.TileHintArrow(x, y, height, HintArrow.TileHintArrow.HintArrowTilePosition.CENTER))
    }

    /**
     * Sets a hint arrow on a specific coordinate in the world.
     * @param x the absolute x coordinate of the hint arrow.
     * @param y the absolute y coordinate of the hint arrow.
     * @param height the height of the hint arrow.
     * @param position the fine position within the tile of the hint arrow.
     */
    fun tileHintArrow(
        x: Int,
        y: Int,
        height: Int,
        position: HintArrow.TileHintArrow.HintArrowTilePosition,
    ) {
        hintArrow(HintArrow.TileHintArrow(x, y, height, position.id))
    }

    fun tileHintArrow(
        x: Int,
        y: Int,
        height: Int,
        position: HintArrowPosition,
    ) {
        hintArrow(HintArrow.TileHintArrow(x, y, height, position.positionHash))
    }

    /**
     * Resets any currently active hint arrow.
     */
    fun resetHintArrow() {
        hintArrow(HintArrow.ResetHintArrow)
    }

    /**
     * Hiscore reply is a packet used in the enhanced clients to do
     * lookups of nearby players, to find out their stats and rankings
     * on the high scores.
     * This packet is sent as a response to the hiscore request packet.
     * @param requestId the id of the request that was made.
     * @param response the response to be written to the client.
     */
    internal fun hiscoreReply(
        requestId: Int,
        response: HiscoreReply.HiscoreReplyResponse,
    ) {
        send {
            HiscoreReply(
                requestId,
                response,
            )
        }
    }

    /**
     * Minimap toggle is used to modify the state of the minimap
     * and the attached compass.
     *
     * Minimap states table:
     * ```
     * | Id |           Description           |
     * |----|:-------------------------------:|
     * | 0  |             Enabled             |
     * | 1  |       Minimap unclickable       |
     * | 2  |          Minimap hidden         |
     * | 3  |          Compass hidden         |
     * | 4  | Map unclickable, compass hidden |
     * | 5  |             Disabled            |
     * ```
     *
     * @param minimapState the minimap state to set (see table above)
     */
    fun minimapToggle(minimapState: Int) {
        send {
            MinimapToggle(minimapState)
        }
    }

    /**
     * Reflection checker packet will attempt to use [java.lang.reflect] to
     * perform a lookup or invocation on a method or field in the client,
     * using information provided in this packet.
     * These invocations/lookups may fail completely, which is fully supported,
     * as various exceptions get caught and special return codes are provided
     * in such cases.
     * An important thing to note, however, is that the server is responsible
     * for not requesting too much, as the client's reply packet has a var-byte
     * size, meaning the entire reply for a reflection check must fit into 255
     * bytes or fewer. There is no protection against this.
     * Additionally worth noting that the [InvokeMethod] variant, while very
     * powerful, is not utilized in OldSchool, and is rather dangerous to
     * invoke due to the aforementioned size limitation.
     *
     * @param id the id of the reflection check, sent back in the reply and
     * used to link together the request and reply, which is needed to fully
     * decode the respective replies.
     * @param checks the list of reflection checks to perform.
     */
    internal fun reflectionChecker(
        id: Int,
        checks: List<ReflectionChecker.ReflectionCheck>,
    ): ReflectionChecker? {
        var check: ReflectionChecker? = null
        send {
            val packet =
                ReflectionChecker(
                    id,
                    checks,
                )
            check = packet
            packet
        }
        return check
    }

    /**
     * Reset anims message is used to reset the currently playing
     * animation of all NPCs and players. This does not impact
     * base animations (e.g. standing, walking).
     * It is unclear what the purpose of this packet actually is.
     */
    fun resetAnims() {
        send {
            ResetAnims
        }
    }

    /**
     * Send ping packet is used to request a ping response from the client.
     * The client will send these [value1] and [value2] variables back to the
     * server in exchange.
     * These integer identifiers do not appear to have any known structure to
     * them - they are not epoch time in any form. Seemingly random as the value
     * can change drastically between different logins.
     * @param value1 the first 32-bit integer identifier.
     * @param value2 the second 32-bit integer identifier.
     */
    fun sendPing(
        value1: Int,
        value2: Int,
    ) {
        send {
            SendPing(
                value1,
                value2,
            )
        }
    }

    /**
     * Server tick end packets are used by the C++ client
     * for ground item settings, in order to decrement
     * visible ground item's timers. Without it, all ground
     * items' timers will remain frozen once dropped.
     */
    fun serverTickEnd() {
        send {
            ServerTickEnd
        }
    }

    /**
     * Update reboot timer is used to start the shut-down timer
     * in preparation of an update.
     * @param gameCycles the number of game cycles (600ms/gc)
     * until the shut-down is complete.
     * If the number is set to zero, any existing reboot timers
     * will be cleared out.
     * The maximum possible value is 65535, which is equal to just
     * below 11 hours.
     */
    fun updateRebootTimer(gameCycles: Int) {
        send {
            UpdateRebootTimerV2(gameCycles, UpdateRebootTimerV2.IgnoreUpdateMessage)
        }
    }

    /**
     * Site settings packet is used to identify the given client.
     * The settings are sent as part of the URL when connecting to services
     * or secure RuneScape URLs.
     * @param settings the settings string to assign
     */
    fun siteSettings(settings: String) {
        send {
            SiteSettings(settings)
        }
    }

    /**
     * Update UID 192 packed is used to update the random 192-bit
     * id that is found in the random.dat file within the player's
     * cache directory.
     * The 192-bit UID will be accompanied by a 32-bit CRC of the
     * block, which the client will verify before changing the
     * contents of the random.dat file.
     */
    fun updateUid192(uid: ByteArray) {
        send {
            UpdateUid192(uid)
        }
    }

    /**
     * URL open packets are used to open a site on the target's default
     * browser.
     * @param url the url to connect to
     */
    fun urlOpen(url: String) {
        send {
            UrlOpen(url)
        }
    }

    /**
     * Chat filter settings packed is used to set the public and
     * trade chat filters to the specified values.
     *
     * Chat filters table:
     * ```
     * | Id |   Type   |
     * |----|:--------:|
     * | 0  |    On    |
     * | 1  |  Friends |
     * | 2  |    Off   |
     * | 3  |   Hide   |
     * | 4  | Autochat |
     * ```
     *
     * @param publicChatFilter the public chat filter value, allowed values
     * include everything in the table above.
     * @param tradeChatFilter the trade chat filter value, allowed values include
     * 'On', 'Friends' and 'Off' (see table above)
     */
    fun chatFilterSettings(
        publicChatFilter: Int,
        tradeChatFilter: Int,
    ) {
        send {
            ChatFilterSettings(
                publicChatFilter,
                tradeChatFilter,
            )
        }
    }

    /**
     * Chat filter settings packed is used to set the private
     * chat filter.
     *
     * Chat filters table:
     * ```
     * | Id |   Type   |
     * |----|:--------:|
     * | 0  |    On    |
     * | 1  |  Friends |
     * | 2  |    Off   |
     * ```
     *
     * @param privateChatFilter the private chat filter value.
     */
    fun chatFilterSettingsPrivateChat(privateChatFilter: Int) {
        send {
            ChatFilterSettingsPrivateChat(privateChatFilter)
        }
    }

    /**
     * Message game packet is used to send a normal game message in
     * the player's chatbox.
     *
     * Game message types (note: names without asterisk are official from a leak):
     * ```
     * | Id  |                Type                |
     * |-----|:----------------------------------:|
     * | 0   |        chattype_gamemessage        |
     * | 1   |          chattype_modchat          |
     * | 2   |         chattype_publicchat        |
     * | 3   |        chattype_privatechat        |
     * | 4   |           chattype_engine          |
     * | 5   |  chattype_loginlogoutnotification  |
     * | 6   |       chattype_privatechatout      |
     * | 7   |       chattype_modprivatechat      |
     * | 9   |        chattype_friendschat        |
     * | 11  |  chattype_friendschatnotification  |
     * | 14  |         chattype_broadcast         |
     * | 26  |      chattype_snapshotfeedback     |
     * | 27  |        chattype_obj_examine        |
     * | 28  |        chattype_npc_examine        |
     * | 29  |        chattype_loc_examine        |
     * | 30  |     chattype_friendnotification    |
     * | 31  |     chattype_ignorenotification    |
     * | 41  |           chattype_clan*           |
     * | 43  |        chattype_clan_system*       |
     * | 44  |        chattype_clan_guest*        |
     * | 46  |     chattype_clan_guest_system*    |
     * | 90  |         chattype_autotyper         |
     * | 91  |        chattype_modautotyper       |
     * | 99  |          chattype_console          |
     * | 101 |          chattype_tradereq         |
     * | 102 |           chattype_trade           |
     * | 103 |       chattype_chalreq_trade       |
     * | 104 |    chattype_chalreq_friendschat    |
     * | 105 |            chattype_spam           |
     * | 106 |       chattype_playerrelated       |
     * | 107 |        chattype_10sectimeout       |
     * | 108 |          chattype_welcome*         |
     * | 109 | chattype_clan_creation_invitation* |
     * | 110 |    chattype_clan_wars_challenge*   |
     * | 111 |      chattype_gim_form_group*      |
     * | 112 |      chattype_gim_group_with*      |
     * ```
     *
     * @param type the type of the message to send (see table above)
     * @param name the name of the target player who is making a request.
     * This property is only for messages such as "X wishes to trade with you.",
     * where there is a player at the other end that is making some sort of request.
     * Upon interacting with these chat messages, the client will invoke the respective
     * op-player packet if it can find that player in local player's high resolution
     * list of players.
     * It is important to note, however, that only opplayer 1, 4, 6 and 7 will ever
     * be fired in this manner.
     * @param message the message itself to render in the chatbox
     */
    fun messageGame(
        type: Int,
        name: String?,
        message: String,
    ) {
        val finalMes =
            if (message.length > 238) {
                message.substring(0, 238)
            } else {
                message
            }
        send {
            MessageGame(
                type,
                name,
                finalMes,
            )
        }
    }


    fun runClientScriptBackwards(
        id: Int,
        vararg values: Any?,
    ) = runClientScript(id, values.reversedArray())

    /**
     * Run clientscript packet is used to execute a clientscript in the client
     * with the provided arguments.
     * The types are inferred based on the values, and delegated to either int or string.
     * @param id the id of the script to invoke
     * @param values the list of int or string values to be sent to the
     * client script.
     */
    fun runClientScript(
        id: Int,
        vararg values: Any,
    ) {
        val filteredValues = values.map {
            when (it) {
                is Int -> it
                is String -> it
                is Boolean -> if (it) 1 else 0
                else -> throw IllegalArgumentException("Unsupported value type: ${it::class.simpleName}")
            }
        }
        send {
            RunClientScript(
                id,
                filteredValues,
            )
        }
    }

    /**
     * Set map flag is used to set the red map flag on the minimap.
     * Use values 255, 255 to remove the map flag.
     * @param xInBuildArea the x coordinate within the build area
     * to render the map flag at.
     * @param yInBuildArea the y coordinate within the build area
     * to render the map flag at.
     */
    fun setMapFlag(
        xInBuildArea: Int,
        yInBuildArea: Int,
    ) {
        send {
            SetMapFlagV2(
                xInBuildArea,
                yInBuildArea,
            )
        }
    }

    fun setMapFlag() {
        val walkSteps = player.walkSteps
        if (walkSteps == null || walkSteps.isEmpty) {
            setMapFlag(255, 255)
        } else {
            val lastStep = walkSteps.last
            val tile = Location(
                WalkStep.getNextX(lastStep),
                WalkStep.getNextY(lastStep),
                player.plane
            )

            val xInBuildArea = player.getXInScene(tile)
            val yInBuildArea = player.getYInScene(tile)

            setMapFlag(xInBuildArea, yInBuildArea)
        }
    }

    /**
     * Set player op packet is used to set the right-click
     * option on all players to a specific option.
     * @param id the id of the option to change, a value in range of
     * 1 to 8 (inclusive)
     * @param priority whether the option should get priority
     * over the 'Walk here' option.
     * @param op the option string to set, or null if removing an op.
     */
    fun setPlayerOp(
        id: Int,
        priority: Boolean,
        op: String?,
    ) {
        send {
            SetPlayerOp(
                id,
                priority,
                op,
            )
        }
    }

    /**
     * Trigger on dialog abort is used to invoke any ondialogabort
     * scripts that have been set up on interfaces, typically to close
     * any dialogues.
     */
    fun triggerOnDialogAbort() {
        send {
            TriggerOnDialogAbort
        }
    }

    /**
     * Update runenergy packet is used to modify the player's current
     * run energy. 100 units equals one percentage on the run orb,
     * meaning a value of 10,000 is equal to 100% run energy.
     */
    fun updateRunEnergy(runenergy: Int) {
        send {
            UpdateRunEnergy(runenergy)
        }
    }

    /**
     * Update runweight packet is used to modify the player's current
     * equipment and inventory weight, in grams.
     */
    fun updateRunWeight(runweight: Int) {
        send {
            UpdateRunWeight(runweight)
        }
    }

    /**
     * Update stat packet is used to set the current experience
     * and levels of a skill for a given player.
     * @param stat the id of the stat to update
     * @param currentLevel player's current level in that stat,
     * e.g. boosted or drained.
     * @param invisibleBoostedLevel player's level in the stat
     * with invisible boosts included
     * @param experience player's experience in the skill,
     * in its integer form - expected value range 0 to 200,000,000.
     */
    fun updateStat(
        stat: Int,
        currentLevel: Int,
        invisibleBoostedLevel: Int,
        experience: Int,
    ) {
        send {
            UpdateStatV2(
                stat,
                currentLevel,
                invisibleBoostedLevel,
                experience,
            )
        }
    }

    /**
     * Update stockmarket slot packet is used to set up
     * an offer on the Grand Exchange, or to clear out an
     * offer.
     * @param update the update type to perform, either
     * [ResetStockMarketSlot] or [SetStockMarketSlot].
     */
    internal fun updateStockMarketSlot(
        slot: Int,
        update: UpdateStockMarketSlot.StockMarketUpdateType,
    ) {
        send {
            UpdateStockMarketSlot(
                slot,
                update,
            )
        }
    }

    /**
     * Friend list loaded is used to mark the friend list
     * as loaded if there are no friends to be sent.
     * If there are friends to be sent, use the [UpdateFriendList]
     * packet instead without this.
     */
    internal fun friendListLoaded() {
        send {
            FriendListLoaded
        }
    }

    /**
     * Message private packets are used to send private messages between
     * players across multiple worlds.
     * This specific packet results in the `From name: message` being shown
     * on the target's client.
     * @param sender name of the player who is sending the message
     * @param worldId the id of the world from which the message is sent
     * @param worldMessageCounter the world-local message counter.
     * Each world must have its own message counter which is used to create
     * a unique id for each message. This message counter must be
     * incrementing with each message that is sent out.
     * If two messages share the same unique id (which is a combination of
     * the [worldId] and the [worldMessageCounter] properties),
     * the client will not render the second message if it already has one
     * received in the last 100 messages.
     * It is additionally worth noting that servers with low population
     * should probably not start the counter at the same value with each
     * game boot, as the probability of multiple messages coinciding
     * is relatively high in that scenario, given the low quantity of
     * messages sent out to begin with.
     * Additionally, only the first 24 bits of the counter are utilized,
     * meaning a value from 0 to 16,777,215 (inclusive).
     * A good starting point for message counting would be to take the
     * hour of the year and multiply it by 50,000 when the server boots
     * up. This means the roll-over happens roughly after every two weeks.
     * Fine-tuning may be used to make it more granular, but the overall
     * idea remains the same.
     * @param chatCrownType the id of the crown to render next to the
     * name of the sender.
     * @param message the message to be forwarded to the recipient.
     */
    fun messagePrivate(
        sender: String,
        worldId: Int,
        worldMessageCounter: Int,
        chatCrownType: Int,
        message: String,
    ) {
        send {
            MessagePrivate(
                sender,
                worldId,
                worldMessageCounter,
                chatCrownType,
                message,
            )
        }
    }

    /**
     * Message private echo is used to show the messages
     * the given player has sent out to others,
     * in a "To name: message" format.
     * @param recipient the name of the player who received
     * the private message.
     * @param message the message to be forwarded.
     */
    fun messagePrivateEcho(
        recipient: String,
        message: String,
    ) {
        send {
            MessagePrivateEcho(
                recipient,
                message,
            )
        }
    }

    /**
     * Update friendlist is used to send the initial friend list on login,
     * as well as any additions to the friend list over time.
     * @param friends the list of friends to be added/set to this friend list.
     * For instances of this class, use [OnlineFriend] and [OfflineFriend]
     * respectively.
     */
    internal fun updateFriendList(friends: List<UpdateFriendList.Friend>) {
        send {
            UpdateFriendList(friends)
        }
    }

    /**
     * Update ignorelist is used to perform changes to the ignore list.
     * Unlike friend list, it is possible to delete ignore list entries
     * from the server's perspective.
     * @param ignores the list of ignores to add or remove.
     */
    internal fun updateIgnoreList(ignores: List<UpdateIgnoreList.IgnoredPlayer>) {
        // We need to give this priority to ensure that it always sends before player info
        // so that our titles do not mess up
        sendWithPriority(true) {
            UpdateIgnoreList(ignores)
        }
    }

    /**
     * Midi jingle packet is used to play a short midi song, typically when
     * the player accomplishes something. The normal song that was playing
     * will be resumed after the jingle finishes playing.
     * @param id the id of the jingle to play
     */
    fun midiJingle(id: Int) {
        send {
            MidiJingle(id)
        }
    }

    /**
     * Midi song packets are used to play songs through the music player.
     * @param id the id of the midi song
     * @param fadeOutDelay the delay in client cycles (20ms/cc) until the old song
     * begins fading out. The default value for this, based on the old midi song packet, is 0.
     * @param fadeOutSpeed the speed at which the old song fades out in client cycles (20ms/cc).
     * The default value for this, based on the old midi song packet, is 60.
     * @param fadeInDelay the delay until the new song begins playing, in client cycles (20ms/cc).
     * The default value for this, based on the old midi song packet is 60.
     * @param fadeInSpeed the speed at which the new song fades in, in client cycles (20ms/cc).
     * The default value for this, based on the old midi song packet is 0.
     */
    fun midiSong(
        id: Int,
        fadeOutDelay: Int,
        fadeOutSpeed: Int,
        fadeInDelay: Int,
        fadeInSpeed: Int,
    ) {
        send {
            MidiSongV2(
                id,
                fadeOutDelay,
                fadeOutSpeed,
                fadeInDelay,
                fadeInSpeed,
            )
        }
    }

    fun midiSong(
        id: Int
    ) = midiSong(id, 0, 60, 60, 0)

    /**
     * Midi song stop is used to stop playing an existing midi song.
     * @param fadeOutDelay the delay in client cycles (20ms/cc) until the song begins fading out.
     * @param fadeOutSpeed the speed at which the song fades out in client cycles (20ms/cc).
     */
    fun midiSongStop(
        fadeOutDelay: Int,
        fadeOutSpeed: Int,
    ) {
        send {
            MidiSongStop(
                fadeOutDelay,
                fadeOutSpeed,
            )
        }
    }

    /**
     * Midi song packets are used to play songs through the music player.
     * This packet pre-queues a secondary song which can be hot-swapped at any point.
     * The intended use case here is to swap the song out mid-playing between identical
     * songs that have different tones playing, e.g. a more up-beat vs a more somber song,
     * while letting the song play on from where it was, rather than re-starting the song.
     * @param primaryId the primary id of the song that will be playing
     * @param secondaryId the secondary id that will play if the `MIDI_SWAP` packet
     * is sent.
     * @param fadeOutDelay the delay in client cycles (20ms/cc) until the old song
     * begins fading out. The default value for this, based on the old midi song packet, is 0.
     * @param fadeOutSpeed the speed at which the old song fades out in client cycles (20ms/cc).
     * The default value for this, based on the old midi song packet, is 60.
     * @param fadeInDelay the delay until the new song begins playing, in client cycles (20ms/cc).
     * The default value for this, based on the old midi song packet is 60.
     * @param fadeInSpeed the speed at which the new song fades in, in client cycles (20ms/cc).
     * The default value for this, based on the old midi song packet is 0.
     */
    fun midiSongWithSecondary(
        primaryId: Int,
        secondaryId: Int,
        fadeOutDelay: Int,
        fadeOutSpeed: Int,
        fadeInDelay: Int,
        fadeInSpeed: Int,
    ) {
        send {
            MidiSongWithSecondary(
                primaryId,
                secondaryId,
                fadeOutDelay,
                fadeOutSpeed,
                fadeInDelay,
                fadeInSpeed,
            )
        }
    }

    /**
     * Midi swap packet allows one to hot-swap a song mid-playing with a different one
     * that was pre-queued with the [MidiSongWithSecondary] packet.
     * This hot-swapping only works if the secondary packet was used, as that defines
     * the id of the secondary song to swap to.
     * @param fadeOutDelay the delay in client cycles (20ms/cc) until the old song
     * begins fading out.
     * @param fadeOutSpeed the speed at which the old song fades out in client cycles (20ms/cc).
     * @param fadeInDelay the delay until the new song begins playing, in client cycles (20ms/cc).
     * @param fadeInSpeed the speed at which the new song fades in, in client cycles (20ms/cc).
     */
    fun midiSwap(
        fadeOutDelay: Int,
        fadeOutSpeed: Int,
        fadeInDelay: Int,
        fadeInSpeed: Int,
    ) {
        send {
            MidiSwap(
                fadeOutDelay,
                fadeOutSpeed,
                fadeInDelay,
                fadeInSpeed,
            )
        }
    }

    /**
     * Synth sound is used to play a short sound effect locally for the given player.
     * @param id the id of the sound effect to play
     * @param loops the number of times to loop the sound effect
     * @param delay the delay in client cycles (20ms/cc) until the sound effect begins playing
     */
    fun synthSound(
        id: Int,
        loops: Int,
        delay: Int,
    ) {
        send {
            SynthSound(
                id,
                loops,
                delay,
            )
        }
    }

    /**
     * Loc anim specific packets are used to make a loc play an animation,
     * specific to one player and not the entire world.
     * @param id the id of the animation to play
     * @param shape the shape of the loc, a value of 0 to 22 (inclusive) is expected.
     * @param rotation the rotation of the loc, a value of 0 to 3 (inclusive) is expected.
     */
    internal fun locAnimSpecific(
        id: Int,
        xInBuildArea: Int,
        yInBuildArea: Int,
        shape: Int,
        rotation: Int,
    ) {
        send {
            LocAnimSpecific(
                id,
                xInBuildArea,
                yInBuildArea,
                shape,
                rotation,
            )
        }
    }

    /**
     * Map anim specific is sent to play a graphical effect/spotanim on a tile,
     * local to a single user, and not the entire world.
     * @param id the id of the spotanim
     * @param delay the delay in client cycles (20ms/cc) until the spotanim begins playing
     * @param height the height at which the spotanim will play
     */
    internal fun spotAnimSpecific(
        id: Int,
        delay: Int,
        height: Int,
        xInBuildArea: Int,
        yInBuildArea: Int,
    ) {
        send {
            MapAnimSpecific(
                id,
                delay,
                height,
                xInBuildArea,
                yInBuildArea,
            )
        }
    }

    /**
     * Npc anim specifics are used to play an animation on a NPC for a specific player,
     * and not the entire world.
     * @param index the index of the npc in the world
     * @param id the id of the animation
     * @param delay the delay of the animation before it begins playing in client cycles (20ms/cc)
     */
    internal fun npcAnimSpecific(
        index: Int,
        id: Int,
        delay: Int,
    ) {
        send {
            NpcAnimSpecific(
                index,
                id,
                delay,
            )
        }
    }

    /**
     * Npc head-icon specific packets are used to render a head icon over
     * a given NPC to one user alone, and not the rest of the world.
     * It is worth noting, however, that the head icon will only be set
     * if the given NPC was already registered by the NPC INFO packet.
     * If a given NPC is removed from the local view through NPC INFO,
     * the head icon goes alongside, and will not be automatically
     * restored should that NPC re-enter the local view.
     * @param index the index of the npc in the world
     * @param headIconSlot the slot of the head icon, a value of 0 to 7 (inclusive)
     * @param spriteGroup the cache group id of the sprite.
     * While the client reads a 32-bit integer for this value, the client
     * does not allow for a value greater than 65535 to be used due to cache limitations,
     * thus, in order to compress the packet even further, we also limit the id to a maximum
     * of 65535.
     * @param spriteIndex the index of the sprite within the sprite file in the cache.
     * Note that this is not the id of the file in the cache group, as for sprites, this is always
     * zero. Each sprite file itself defines a number of sprites - this is the index in that list
     * of sprites.
     * @throws IllegalArgumentException if the [headIconSlot] is not in range of 0 to 7 (inclusive)
     */
    internal fun npcHeadIconSpecific(
        index: Int,
        headIconSlot: Int,
        spriteGroup: Int,
        spriteIndex: Int,
    ) {
        send {
            NpcHeadIconSpecific(
                index,
                headIconSlot,
                spriteGroup,
                spriteIndex,
            )
        }
    }

    /**
     * Npc spot-anim specific packets are used to play a spotanim on a NPC
     * for a specific player and not the entire world.
     * @param index the index of the NPC in the world
     * @param id the id of the spotanim to play
     * @param slot the slot of the spotanim
     * @param height the height of the spotanim
     * @param delay the delay of the spotanim in client cycles (20ms/cc)
     */
    internal fun npcSpotAnimSpecific(
        index: Int,
        id: Int,
        slot: Int,
        height: Int,
        delay: Int,
    ) {
        send {
            NpcSpotAnimSpecific(
                index,
                id,
                slot,
                height,
                delay,
            )
        }
    }

    /**
     * Player anim specifics are used to play an animation on the local player for the local player,
     * not the entire world.
     * Note that unlike most other packets, this one does not provide the index, so it can only
     * be played on the local player and no one else.
     * @param id the id of the animation
     * @param delay the delay of the animation before it begins playing in client cycles (20ms/cc)
     */
    internal fun playerAnimSpecific(
        id: Int,
        delay: Int,
    ) {
        send {
            AnimSpecific(
                id,
                delay,
            )
        }
    }

    /**
     * Npc spot-anim specific packets are used to play a spotanim on a player
     * for a specific player and not the entire world.
     * @param index the index of the player in the world
     * @param id the id of the spotanim to play
     * @param slot the slot of the spotanim
     * @param height the height of the spotanim
     * @param delay the delay of the spotanim in client cycles (20ms/cc)
     */
    internal fun playerSpotAnimSpecific(
        index: Int,
        id: Int,
        slot: Int,
        height: Int,
        delay: Int,
    ) {
        send {
            PlayerSpotAnimSpecific(
                index,
                id,
                slot,
                height,
                delay,
            )
        }
    }

    /**
     * Proj anim specific packets are used to send a projectile for a specific user,
     * without anyone else in the world seeing it.
     * Unlike the regular [net.rsprot.protocol.game.outgoing.zone.payload.MapProjAnim]
     * zone packet, this packet does not support transmitting the source index.
     *
     * While it is possible to compress this message further as the [targetIndex]
     * only needs to be a 24-bit integer, the entire payload of this packet
     * sums to just 19 bytes, which is 1 less than the 8-byte padding that will
     * be performed by the JVM anyway - so there are no benefits in doing so.
     *
     * @param id the id of the spotanim that is this projectile
     * @param startHeight the height of the projectile as it begins flying
     * @param endHeight the height of the projectile as it finishes flying
     * @param startTime the start time in client cycles (20ms/cc) until the
     * projectile begins moving
     * @param endTime the end time in client cycles (20ms/cc) until the
     * projectile arrives at its destination
     * @param angle the angle that the projectile takes during its flight
     * @param progress the fine coord progress that the projectile
     * has made before it begins flying. If the value is 0, the projectile begins flying
     * at the defined start coordinate. For every 128 units of value, the projectile
     * is moved 1 game square towards the end position. Interpolate between 0-128 for
     * units smaller than 1 game square.
     * This is commonly set to 128 to make a projectile appear as if it's flying
     * straight down, as the projectile will not render if its defined start and
     * end coords are equal. So, in order to avoid that, one solution is to put the
     * end coordinate 1 game square away from the start in a cardinal direction,
     * and set the value of this property to 128 - ensuring that the projectile
     * will appear to fly completely vertically, with no horizontal movement whatsoever.
     * In the event inspector, this property is called 'distanceOffset'.
     * @param targetIndex the index of the pathing entity at whom the projectile is shot.
     * If the value is 0, the projectile will not be locked to any target entity.
     * If the target avatar is a player, add 0x10000 to the real index value (0-2048).
     * If the target avatar is a NPC, set the index as it is.
     * @param deltaX the x coordinate delta that the projectile will move to
     * relative to the starting position.
     * @param deltaZ the y coordinate delta that the projectile will move to
     * relative to the starting position.
     */
    internal fun projAnimSpecific(
        id: Int,
        startHeight: Int,
        endHeight: Int,
        startTime: Int,
        endTime: Int,
        angle: Int,
        progress: Int,
        targetIndex: Int,
        xInBuildArea: Int,
        yInBuildArea: Int,
        deltaX: Int,
        deltaZ: Int,
    ) {
        send {
            val bx = ((player.position.x shr 3) - 6).coerceAtLeast(0) shl 3
            val bz = ((player.position.y shr 3) - 6).coerceAtLeast(0) shl 3
            val startX = bx + xInBuildArea
            val startZ = bz + yInBuildArea
            ProjAnimSpecificV4(
                id,
                startHeight,
                endHeight,
                startTime,
                endTime,
                angle,
                progress,
                startX,
                startZ,
                player.position.plane,
                0,
                startX + deltaX,
                startZ + deltaZ,
                player.position.plane,
                targetIndex,
            )
        }
    }

    fun varp(id: Int, value: Int) {
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            varpSmall(id, value)
        } else {
            varpLarge(id, value)
        }
    }

    fun varpLowPriority(id: Int, value: Int) {
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            varpSmallLowPriority(id, value)
        } else {
            varpLargeLowPriority(id, value)
        }
    }

    /**
     * Varp small messages are used to send a varp to the client that
     * has a value which does not fit in the range of a byte, being -128..127.
     * For values which do fit in the aforementioned range, the [VarpSmall]
     * packed is preferred as it takes up less bandwidth, although nothing
     * prevents one from sending all varps using this variant.
     * @param id the id of the varp
     * @param value the value of the varp
     */
    internal fun varpLarge(
        id: Int,
        value: Int,
    ) {
        send {
            VarpLarge(
                id,
                value,
            )
        }
    }

    /**
     * Varp small messages are used to send a varp to the client that
     * has a value which fits in the range of a byte, being -128..127.
     * Note that this class does not verify that the value is in the correct
     * range - instead any bits beyond the range of a byte get ignored.
     * @param id the id of the varp
     * @param value the value of the varp, in range of -128 to 127 (inclusive)
     */
    internal fun varpSmall(
        id: Int,
        value: Int,
    ) {
        send {
            VarpSmall(
                id,
                value,
            )
        }
    }

    /**
     * Varp small messages are used to send a varp to the client that
     * has a value which does not fit in the range of a byte, being -128..127.
     * For values which do fit in the aforementioned range, the [VarpSmall]
     * packed is preferred as it takes up less bandwidth, although nothing
     * prevents one from sending all varps using this variant.
     * @param id the id of the varp
     * @param value the value of the varp
     */
    internal fun varpLargeLowPriority(
        id: Int,
        value: Int,
    ) {
        sendWithPriority(false) {
            VarpLarge(
                id,
                value,
            )
        }
    }

    /**
     * Varp small messages are used to send a varp to the client that
     * has a value which fits in the range of a byte, being -128..127.
     * Note that this class does not verify that the value is in the correct
     * range - instead any bits beyond the range of a byte get ignored.
     * @param id the id of the varp
     * @param value the value of the varp, in range of -128 to 127 (inclusive)
     */
    internal fun varpSmallLowPriority(
        id: Int,
        value: Int,
    ) {
        sendWithPriority(false) {
            VarpSmall(
                id,
                value,
            )
        }
    }

    /**
     * The varp reset packet is used to set the values of every
     * varplayer type to 0.
     * It is worth noting that the client will only reset the varps
     * up until the last one which has a respective cache config.
     * So if the varps array is extended, but respective configs
     * are not made, the extended ones will not be zero'd out.
     */
    internal fun varpReset() {
        send {
            VarpReset
        }
    }

    /**
     * The varp sync packet is used to synchronize the client's cache
     * of varps back up with the server's version.
     *
     * The client keeps two int arrays for varps one that it modifies,
     * and one that is a perfect replica of what the server has sent.
     * This packet provides a means to sync the modified variant up
     * with what the server has sent.
     */
    internal fun varpSync() {
        send {
            VarpSync
        }
    }

    /**
     * Update zone full-follows packets are used to clear a zone (8x8x1 tiles space)
     * from any modifications done to it prior, wiping any obj and loc changes in
     * the process. This packet additionally sets the 'current zone pointer' to this
     * zone, allowing one to follow it with any other zone payload packet, commonly
     * used to synchronize the zone to the observer (restoring all the objs in it,
     * loc changes and so on).
     * @param zoneX the x coordinate of the zone's south-western corner in the
     * build area.
     * @param zoneY the y coordinate of the zone's south-western corner in the
     * build area.
     * @param level the height level of the zone, typically equal to the player's
     * own height level.
     *
     * It should be noted that the [zoneX] and [zoneY] coordinates are relative
     * to the build area in their absolute form, not in their shifted zone form.
     * If the player is at an absolute coordinate of 50, 40 within the build area(104x104),
     * the expected coordinates to transmit here would be 48, 40, as that would
     * point to the south-western corner of the zone in which the player is standing in.
     */
    internal fun updateZoneFullFollows(
        zoneX: Int,
        zoneY: Int,
        level: Int,
    ) {
        send {
            UpdateZoneFullFollows(
                zoneX,
                zoneY,
                level,
            )
        }
    }

    /**
     * Update zone partial-enclosed is used to send a batch of updates for a given
     * zone all in one packet. This results in less bandwidth being used, as well as
     * avoiding the client limitations of 100 packets/client cycle (20ms/cc).
     * @param zoneX the x coordinate of the zone's south-western corner in the
     * build area.
     * @param zoneY the y coordinate of the zone's south-western corner in the
     * build area.
     * @param level the height level of the zone, typically equal to the player's
     * own height level.
     *
     * It should be noted that the [zoneX] and [zoneY] coordinates are relative
     * to the build area in their absolute form, not in their shifted zone form.
     * If the player is at an absolute coordinate of 50, 40 within the build area(104x104),
     * the expected coordinates to transmit here would be 48, 40, as that would
     * point to the south-western corner of the zone in which the player is standing in.
     */
    internal fun updateZonePartialEnclosed(
        zoneX: Int,
        zoneY: Int,
        level: Int,
        payload: ByteBuf,
    ) {
        send {
            UpdateZonePartialEnclosed(
                zoneX,
                zoneY,
                level,
                payload,
            )
        }
    }

    /**
     * Update zone partial follows packets are used to set the 'current zone pointer' to this
     * zone, allowing one to follow it with any other zone payload packet.
     * This packet is more efficient to use over the partial-enclosed variant
     * when there is only a single zone packet following it, in any other scenario,
     * it is more bandwidth-friendly to use the enclosed packet.
     * @param zoneX the x coordinate of the zone's south-western corner in the
     * build area.
     * @param zoneY the y coordinate of the zone's south-western corner in the
     * build area.
     * @param level the height level of the zone, typically equal to the player's
     * own height level.
     *
     * It should be noted that the [zoneX] and [zoneY] coordinates are relative
     * to the build area in their absolute form, not in their shifted zone form.
     * If the player is at an absolute coordinate of 50, 40 within the build area(104x104),
     * the expected coordinates to transmit here would be 48, 40, as that would
     * point to the south-western corner of the zone in which the player is standing in.
     */
    internal fun updateZonePartialFollows(
        zoneX: Int,
        zoneY: Int,
        level: Int,
    ) {
        send {
            UpdateZonePartialFollows(
                zoneX,
                zoneY,
                level,
            )
        }
    }

    /**
     * Loc add-change packed is used to either add or change a loc in the world.
     * The client will add a new loc if none exists by this description,
     * or overwrites an old one with the same layer (layer is obtained through the [shape]
     * property of the loc).
     * @param id the id of the loc to add
     * @param xInZone the x coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param shape the shape of the loc, a value of 0 to 22 (inclusive) is expected.
     * @param rotation the rotation of the loc, a value of 0 to 3 (inclusive) is expected.
     * @param opFlags the right-click options enabled on this loc.
     */
    internal fun locAddChange(
        id: Int,
        xInZone: Int,
        zInZone: Int,
        shape: Int,
        rotation: Int,
        opFlags: Byte,
    ) {
        send {
            LocAddChange(
                id,
                xInZone,
                zInZone,
                shape,
                rotation,
                opFlags,
            )
        }
    }

    /**
     * Loc anim packets are used to make a loc play an animation.
     * @param id the id of the animation to play
     * @param xInZone the x coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param shape the shape of the loc, a value of 0 to 22 (inclusive) is expected.
     * @param rotation the rotation of the loc, a value of 0 to 3 (inclusive) is expected.
     */
    internal fun locAnim(
        id: Int,
        xInZone: Int,
        zInZone: Int,
        shape: Int,
        rotation: Int,
    ) {
        send {
            LocAnim(
                id,
                xInZone,
                zInZone,
                shape,
                rotation,
            )
        }
    }

    /**
     * Loc del packets are used to delete locs from the world.
     * @param xInZone the x coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param shape the shape of the loc, a value of 0 to 22 (inclusive) is expected.
     * @param rotation the rotation of the loc, a value of 0 to 3 (inclusive) is expected.
     */
    internal fun locDel(
        xInZone: Int,
        zInZone: Int,
        shape: Int,
        rotation: Int,
    ) {
        send {
            LocDel(
                xInZone,
                zInZone,
                shape,
                rotation,
            )
        }
    }

    /**
     * Loc merge packets are used to merge a given loc's model with the player's
     * own model, preventing any visual clipping problems in the process.
     * This is commonly done with obstacle pipes in agility courses, as
     * the player model will otherwise render through the pipes.
     *
     * The merge will cover a rectangle defined by the [minX], [minY], [maxX] and [maxY]
     * properties, relative to the player who is being merged. It should be noted
     * that the client adds an extra 1 to the total width/height values here,
     * so having all these properties at zero would still create a single
     * tile square to be merged.
     *
     * @param index the index of the player who is being merged
     * @param id the id of the loc that is being merged with the player
     * @param xInZone the x coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the loc within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param shape the shape of the loc, a value of 0 to 22 (inclusive) is expected.
     * @param rotation the rotation of the loc, a value of 0 to 3 (inclusive) is expected.
     * @param start the delay until the loc merging begins, in client cycles (20ms/cc).
     * @param end the client cycle (20ms/cc) at which the merging ends.
     * @param minX the min x coordinate at which the merge occurs (see explanation above)
     * @param minY the min y coordinate at which the merge occurs (see explanation above)
     * @param maxX the max x coordinate at which the merge occurs (see explanation above)
     * @param maxY the max y coordinate at which the merge occurs (see explanation above)
     */
    internal fun locMerge(
        index: Int,
        id: Int,
        xInZone: Int,
        zInZone: Int,
        shape: Int,
        rotation: Int,
        start: Int,
        end: Int,
        minX: Int,
        minY: Int,
        maxX: Int,
        maxY: Int,
    ) {
        send {
            LocMerge(
                index,
                id,
                xInZone,
                zInZone,
                shape,
                rotation,
                start,
                end,
                minX,
                minY,
                maxX,
                maxY,
            )
        }
    }

    /**
     * Map anim is sent to play a graphical effect/spotanim on a tile.
     * @param id the id of the spotanim
     * @param delay the delay in client cycles (20ms/cc) until the spotanim begins playing
     * @param height the height at which the spotanim will play
     * @param xInZone the x coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     */
    internal fun mapAnim(
        id: Int,
        delay: Int,
        height: Int,
        xInZone: Int,
        zInZone: Int,
    ) {
        send {
            MapAnim(
                id,
                delay,
                height,
                xInZone,
                zInZone,
            )
        }
    }

    /**
     * Map projectile anim packets are sent to render projectiles
     * from one coord to another.
     * @param id the id of the spotanim that is this projectile
     * @param startHeight the height of the projectile as it begins flying
     * @param endHeight the height of the projectile as it finishes flying
     * @param startTime the start time in client cycles (20ms/cc) until the
     * projectile begins moving
     * @param endTime the end time in client cycles (20ms/cc) until the
     * projectile arrives at its destination
     * @param angle the angle that the projectile takes during its flight
     * @param progress the fine coord distance offset that the projectile
     * begins flying at. If the value is 0, the projectile begins flying
     * at the defined start coordinate. For every 128 units of value, the projectile
     * is moved 1 game square towards the end position. Interpolate between 0-128 for
     * units smaller than 1 game square.
     * This is commonly set to 128 to make a projectile appear as if it's flying
     * straight down, as the projectile will not render if its defined start and
     * end coords are equal. So, in order to avoid that, one solution is to put the
     * end coordinate 1 game square away from the start in a cardinal direction,
     * and set the value of this property to 128 - ensuring that the projectile
     * will appear to fly completely vertically, with no horizontal movement whatsoever.
     * @param sourceIndex the index of the pathing entity from whom the projectile comes.
     * If the value is 0, the projectile will not be locked to any source entity.
     * If the target avatar is a player, add 0x10000 to the real index value (0-2048).
     * If the target avatar is a NPC, set the index as it is.
     * @param targetIndex the index of the pathing entity at whom the projectile is shot.
     * If the value is 0, the projectile will not be locked to any target entity.
     * If the target avatar is a player, add 0x10000 to the real index value (0-2048).
     * If the target avatar is a NPC, set the index as it is.
     * @param xInZone the start x coordinate of the projectile within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param yInZone the start y coordinate of the projectile within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param deltaX the x coordinate delta that the projectile will move to
     * relative to the starting position.
     * @param deltaZ the y coordinate delta that the projectile will move to
     * relative to the starting position.
     */
    internal fun mapProjAnim(
        id: Int,
        startHeight: Int,
        endHeight: Int,
        startTime: Int,
        endTime: Int,
        angle: Int,
        progress: Int,
        sourceIndex: Int,
        targetIndex: Int,
        xInZone: Int,
        yInZone: Int,
        deltaX: Int,
        deltaZ: Int,
    ) {
        send {
            val zoneX = player.position.x shr 3
            val zoneZ = player.position.y shr 3
            val endX = zoneX * 8 + xInZone + deltaX
            val endZ = zoneZ * 8 + yInZone + deltaZ
            MapProjAnimV2(
                id,
                startHeight / 4,
                endHeight / 4,
                startTime,
                endTime,
                angle,
                progress,
                sourceIndex,
                targetIndex,
                xInZone,
                yInZone,
                endX,
                endZ,
                player.position.plane,
            )
        }
    }

    /**
     * Obj add packets are used to spawn an obj on the ground.
     *
     * Ownership table:
     * ```
     * | Id | Ownership Type |
     * |----|:--------------:|
     * | 0  |      None      |
     * | 1  |   Self Player  |
     * | 2  |  Other Player  |
     * | 3  |  Group Ironman |
     * ```
     *
     * @param id the id of the obj config
     * @param quantity the quantity of the obj to be spawned
     * @param xInZone the x coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param opFlags the right-click options enabled on this obj.
     * @param timeUntilPublic how many game cycles until the obj turns public.
     * This property is only used on the C++-based clients.
     * @param timeUntilDespawn how many game cycles until the obj disappears.
     * This property is only used on the C++-based clients.
     * @param ownershipType the type of ownership of this obj (see table above).
     * This property is only used on the C++-based clients.
     * @param neverBecomesPublic whether the item turns public in the future.
     * This property is only used on the c++-based clients.
     */
    internal fun objAdd(
        id: Int,
        quantity: Int,
        xInZone: Int,
        zInZone: Int,
        opFlags: Byte,
        timeUntilPublic: Int,
        timeUntilDespawn: Int,
        ownershipType: Int,
        neverBecomesPublic: Boolean,
    ) {
        send {
            ObjAdd(
                id,
                quantity,
                xInZone,
                zInZone,
                opFlags,
                timeUntilPublic,
                timeUntilDespawn,
                ownershipType,
                neverBecomesPublic,
            )
        }
    }

    /**
     * Obj customise is a packet that allows the server to modify an item on the ground, by either changing
     * the model, the colours and the textures of it.
     * @param id the id of the obj to update
     * @param quantity the quantity of the obj to update
     * @param model the model id to assign to this obj
     * @param recolIndex the index of the colour to override
     * @param recol the colour value to assign at the [recolIndex] index
     * @param retexIndex the index of the texture to override
     * @param retex the texture value to assign at the [retexIndex] index
     * @param xInZone the x coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the z coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     */
    internal fun objCustomise(
        id: Int,
        quantity: Int,
        model: Int,
        recolIndex: Int,
        recol: Int,
        retexIndex: Int,
        retex: Int,
        xInZone: Int,
        zInZone: Int,
    ) {
        send {
            ObjCustomise(
                id,
                quantity,
                model,
                recolIndex,
                recol,
                retexIndex,
                retex,
                xInZone,
                zInZone,
            )
        }
    }

    /**
     * Obj uncustomise resets any customisations done to an obj via the [ObjCustomise] packet.
     * @param id the id of the obj to update
     * @param quantity the quantity of the obj to update
     * @param xInZone the x coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the z coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     */
    internal fun objUncustomise(
        id: Int,
        quantity: Int,
        xInZone: Int,
        zInZone: Int,
    ) {
        send {
            ObjUncustomise(
                id,
                quantity,
                xInZone,
                zInZone,
            )
        }
    }

    /**
     * Obj count is a packet used to update the quantity of an obj that's already
     * spawned into the build area. This is only done for objs which are private
     * to a specific user - doing so merges the stacks together into one rather
     * than having two distinct stacks of the same item.
     * @param id the id of the obj to merge
     * @param oldQuantity the old quantity of the obj to find, if no obj
     * by this quantity is found, this packet has no effect client-side
     * @param newQuantity the new quantity to be set to this obj
     * @param xInZone the x coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     */
    internal fun objCount(
        id: Int,
        oldQuantity: Int,
        newQuantity: Int,
        xInZone: Int,
        zInZone: Int,
    ) {
        send {
            ObjCount(
                id,
                oldQuantity,
                newQuantity,
                xInZone,
                zInZone,
            )
        }
    }

    /**
     * Obj del packets are used to delete an existing obj from the build area,
     * assuming it exists in the first place.
     * @param id the id of the obj to delete. Note that the client does bitwise-and
     * on the id to cap it to the lowest 15 bits, meaning the maximum id that can be
     * transmitted is 32767.
     * @param quantity the quantity of the obj to be deleted. If there is no obj
     * with this quantity, nothing will be deleted.
     * @param xInZone the x coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     */
    internal fun objDel(
        id: Int,
        quantity: Int,
        xInZone: Int,
        zInZone: Int,
    ) {
        send {
            ObjDel(
                id,
                quantity,
                xInZone,
                zInZone,
            )
        }
    }

    /**
     * Obj enabled ops is used to change the right-click options on an obj
     * on the ground. This packet is currently unused in OldSchool RuneScape.
     * It works by finding the first obj in the stack with the provided [id],
     * and modifying the right-click ops on that. It does not verify quantity.
     * @param id the id of the obj that needs to get its ops changed
     * @param opFlags the right-click options to set enabled on that obj
     * @param xInZone the x coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the obj within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     */
    internal fun objEnabledOps(
        id: Int,
        opFlags: Byte,
        xInZone: Int,
        zInZone: Int,
    ) {
        send {
            ObjEnabledOps(
                id,
                opFlags,
                xInZone,
                zInZone,
            )
        }
    }

    /**
     * Sound area packed is sent to play a sound effect at a specific coord.
     * Any players within [radius] tiles of the destination coord will
     * hear this sound effect played, if they have sound effects enabled.
     * The volume will change according to the player's distance to the
     * origin coord of the sound effect itself.
     * It is worth noting there is a maximum quantity of 50 area sound effects
     * that can play concurrently in the client across all the zones.
     * Therefore, a potential optimization one can do is prevent appending
     * any more area sound effects once the quantity has reached 50 in a zone.
     * @param id the id of the sound effect to play
     * @param delay the delay in client cycles (20ms/cc) until the
     * sound effect starts playing
     * @param loops how many loops the sound effect should do.
     * If the [loops] property is 0, the sound effect will not play.
     * @param radius the radius from the originating coord how far the sound
     * effect can be heard. Note that the client ignores the 4 higher bits of
     * this value, meaning the maximum radius is 31 tiles - anything above has
     * no effect.
     * @param size the size of the origin. In most cases, this should be
     * a value of 1. However, if a larger value is provided, it means the
     * client will treat the south-western coord provided here as the
     * south-western corner of the 'box' that is made with this size in mind,
     * for the purpose of having an evenly-spreading volume around this
     * element.
     * This size property is primarily used for larger NPCs, to make their
     * sound effects flow out smoothly from all sides.
     * @param xInZone the x coordinate of the sound effect within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     * @param zInZone the y coordinate of the sound effect within the zone it is in,
     * a value in range of 0 to 7 (inclusive) is expected. Any bits outside that are ignored.
     */
    internal fun soundArea(
        id: Int,
        delay: Int,
        loops: Int,
        radius: Int,
        size: Int,
        xInZone: Int,
        zInZone: Int,
    ) {
        send {
            SoundArea(
                id,
                delay,
                loops,
                radius,
                size,
                xInZone,
                zInZone,
            )
        }
    }

    /**
     * Clan channel delta is a packet used to transmit partial updates
     * to an existing clan channel. This prevents sending a full update for everything
     * as that can get rather wasteful.
     * @param clanType the type of the clan the player is in
     * @param key the 64-bit hash of the clan
     * @param updateNum the update counter/timestamp for the clan.
     * The exact behaviours behind this are not known, but the value appears to be
     * an epoch time millis, with each minor change resulting in the value incrementing
     * by +1; e.g. each member joining seems to increment the value by 1.
     * @param events the list of channel delta events to perform in this update
     */
    internal fun clanChannelDelta(
        clanType: Int,
        key: Long,
        updateNum: Long,
        events: List<ClanChannelDelta.Event>,
    ) {
        send {
            ClanChannelDelta(
                clanType,
                key,
                updateNum,
                events,
            )
        }
    }

    /**
     * Clan channel full packets are used to update
     * the state of a clan upon first joining it, or when the player is leaving it.
     * @param clanType the type of the clan the player is joining or leaving,
     * such as guest or normal.
     * @param update the type of update to perform, either [ClanChannelFull.JoinUpdate]
     * or [ClanChannelFull.LeaveUpdate].
     */
    internal fun clanChannelFull(
        clanType: Int,
        update: ClanChannelFull.Update,
    ) {
        send {
            ClanChannelFull(
                clanType,
                update,
            )
        }
    }

    /**
     * Clan settings delta updates are used to modify a sub-set of this clan's settings.
     * @param clanType the type of the clan to modify, e.g. guest or normal,
     * @param owner the hash of the owner.
     * As the value of this property is never assigned in the client, but it is compared,
     * this property should always be assigned the value 0.
     * @param updateNum the number of updates this clan's settings has had.
     * If the value does not match up, the client will throw an exception!
     */
    internal fun clanSettingsDelta(
        clanType: Int,
        owner: Long,
        updateNum: Int,
        updates: List<ClanSettingsDelta.Update>,
    ) {
        send {
            ClanSettingsDelta(
                clanType,
                owner,
                updateNum,
                updates,
            )
        }
    }

    /**
     * Clan settings full packet is used to update the clan's primary settings.
     * @param clanType the clan being updated
     * @param update the clan settings update to be performed
     */
    internal fun clanSettingsFull(
        clanType: Int,
        update: ClanSettingsFull.Update,
    ) {
        send {
            ClanSettingsFull(
                clanType,
                update,
            )
        }
    }

    /**
     * Message clan channel is used to send messages within a clan channel
     * that the player is in.
     * @param clanType the type of the clan the player is in
     * @param name the name of the player sending the message
     * @param worldId the id of the world from which the message is sent
     * @param worldMessageCounter the world-local message counter.
     * Each world must have its own message counter which is used to create
     * a unique id for each message. This message counter must be
     * incrementing with each message that is sent out.
     * If two messages share the same unique id (which is a combination of
     * the [worldId] and the [worldMessageCounter] properties),
     * the client will not render the second message if it already has one
     * received in the last 100 messages.
     * It is additionally worth noting that servers with low population
     * should probably not start the counter at the same value with each
     * game boot, as the probability of multiple messages coinciding
     * is relatively high in that scenario, given the low quantity of
     * messages sent out to begin with.
     * Additionally, only the first 24 bits of the counter are utilized,
     * meaning a value from 0 to 16,777,215 (inclusive).
     * A good starting point for message counting would be to take the
     * hour of the year and multiply it by 50,000 when the server boots
     * up. This means the roll-over happens roughly after every two weeks.
     * Fine-tuning may be used to make it more granular, but the overall
     * idea remains the same.
     * @param chatCrownType the chat crown type to be rendered next to the name
     * @param message the message to send
     */
    fun messageClanChannel(
        clanType: Int,
        name: String,
        worldId: Int,
        worldMessageCounter: Int,
        chatCrownType: Int,
        message: String,
    ) {
        send {
            MessageClanChannel(
                clanType,
                name,
                worldId,
                worldMessageCounter,
                chatCrownType,
                message,
            )
        }
    }

    /**
     * Message clan channel system is used to send system messages
     * within a clan channel that the player is in
     * @param clanType the type of the clan the player is in
     * @param worldId the id of the world from which the message is sent
     * @param worldMessageCounter the world-local message counter.
     * Each world must have its own message counter which is used to create
     * a unique id for each message. This message counter must be
     * incrementing with each message that is sent out.
     * If two messages share the same unique id (which is a combination of
     * the [worldId] and the [worldMessageCounter] properties),
     * the client will not render the second message if it already has one
     * received in the last 100 messages.
     * It is additionally worth noting that servers with low population
     * should probably not start the counter at the same value with each
     * game boot, as the probability of multiple messages coinciding
     * is relatively high in that scenario, given the low quantity of
     * messages sent out to begin with.
     * Additionally, only the first 24 bits of the counter are utilized,
     * meaning a value from 0 to 16,777,215 (inclusive).
     * A good starting point for message counting would be to take the
     * hour of the year and multiply it by 50,000 when the server boots
     * up. This means the roll-over happens roughly after every two weeks.
     * Fine-tuning may be used to make it more granular, but the overall
     * idea remains the same.
     * @param message the message to send
     */
    fun messageClanChannelSystem(
        clanType: Int,
        worldId: Int,
        worldMessageCounter: Int,
        message: String,
    ) {
        send {
            MessageClanChannelSystem(
                clanType,
                worldId,
                worldMessageCounter,
                message,
            )
        }
    }

    /**
     * Var clans are used to transmit a variable of a clan to the user.
     * It is important to note that the data type must align with what
     * is defined in the cache, or the client will not be decoding it
     * correctly, which will most likely lead to a disconnection.
     * @param id the id of the varclan
     * @param value the varclan data value.
     * Use [VarClanIntData], [VarClanLongData] or [VarClanStringData] to
     * transmit the payload, depending on the defined type in the cache.
     */
    internal fun varClan(
        id: Int,
        value: VarClanData,
    ) {
        send {
            VarClan(
                id,
                value,
            )
        }
    }

    /**
     * Var clan enable packet is used to initialize a new var domain
     * in the client, intended to be sent as the player joins a clan.
     */
    fun varClanEnable() {
        send {
            VarClanEnable
        }
    }

    /**
     * Var clan disable packet is used to clear out a var domain
     * in the client, intended to be sent as the player leaves a clan.
     */
    fun varClanDisable() {
        send {
            VarClanDisable
        }
    }

    /**
     * Message friendchannel is used to transmit messages within a friend
     * chat channel.
     * @param sender the name of the player who is sending the message
     * @param channelName the name of the friend chat channel
     * @param worldMessageCounter the world-local message counter.
     * Each world must have its own message counter which is used to create
     * a unique id for each message. This message counter must be
     * incrementing with each message that is sent out.
     * If two messages share the same unique id (which is a combination of
     * the [worldId] and the [worldMessageCounter] properties),
     * the client will not render the second message if it already has one
     * received in the last 100 messages.
     * It is additionally worth noting that servers with low population
     * should probably not start the counter at the same value with each
     * game boot, as the probability of multiple messages coinciding
     * is relatively high in that scenario, given the low quantity of
     * messages sent out to begin with.
     * Additionally, only the first 24 bits of the counter are utilized,
     * meaning a value from 0 to 16,777,215 (inclusive).
     * A good starting point for message counting would be to take the
     * hour of the year and multiply it by 50,000 when the server boots
     * up. This means the roll-over happens roughly after every two weeks.
     * Fine-tuning may be used to make it more granular, but the overall
     * idea remains the same.
     * @param chatCrownType the id of the crown to render next to the
     * name of the sender.
     * @param message the message to be sent in the friend chat
     * channel.
     */
    fun messageFriendChannel(
        sender: String,
        channelName: String,
        worldId: Int,
        worldMessageCounter: Int,
        chatCrownType: Int,
        message: String,
    ) {
        send {
            MessageFriendChannel(
                sender,
                channelName,
                worldId,
                worldMessageCounter,
                chatCrownType,
                message,
            )
        }
    }

    fun messageFriendChannel(
        sender: String,
        channelName: String,
        worldId: Int,
        chatCrownType: Int,
        message: String,
    ) {
        val worldMessageCounter = ThreadLocalRandom.current().nextInt(0, 16_777_215)
        messageFriendChannel(sender, channelName, worldId, worldMessageCounter, chatCrownType, message)
    }

    /**
     * Update friendchat channel full (V2) is used to send full channel updates
     * where the list of entries has a size of more than 255.
     * It can also support sizes below that, but for sizes in range of 128..255,
     * it is more efficient by 1 byte to use V1 of this packet.
     * @param channelOwner the name of the player who owns this channel
     * @param channelName the name of the friend chat channel.
     * This name must be compatible with base-37 encoding, meaning
     * it cannot have special symbols, and it must be 12 characters of less.
     * @param kickRank the minimum rank id to kick another player from
     * the friend chat.
     * @param entries the list of friend chat entries to be added.
     */
    internal fun updateFriendChatChannelFull(
        channelOwner: String,
        channelName: String,
        kickRank: Int,
        entries: List<UpdateFriendChatChannelFull.FriendChatEntry>,
    ) {
        send {
            UpdateFriendChatChannelFullV2(
                UpdateFriendChatChannelFullV2.JoinUpdate(
                    channelOwner,
                    channelName,
                    kickRank,
                    entries,
                ),
            )
        }
    }

    /**
     * Update friendchat singleuser is used to perform a change
     * to a friend chat for a single user, whether that be
     * adding the user to the friend chat, or removing them.
     * @param user the user entry being removed or added.
     * Use [AddedFriendChatUser] and [RemovedFriendChatUser]
     * respectively to perform different updates.
     */
    internal fun updateFriendChatChannelSingleUser(user: UpdateFriendChatChannelSingleUser.FriendChatUser) {
        send {
            UpdateFriendChatChannelSingleUser(user)
        }
    }

    fun leaveFriendChatChannel() {
        send {
            UpdateFriendChatChannelFullV2(
                UpdateFriendChatChannelFullV2.LeaveUpdate
            )
        }
    }

    fun sendZoneProt(prot: ZoneProt) {
        send {
            prot
        }
    }

    /**
     * Packet group start is a packet which tells the client to wait until the entire
     * payload of a packet group has arrived, then process all of it in a single client cycle,
     * bypassing the usual 100 packets per client cycle limitation that the client has.
     * @param messages the messages to wait for and process instantly. Note that the
     * size of all these messages combined must be <= 32,767 bytes. Exceeding this limit
     * will cause the protocol to crash for that user, disconnecting them. This is due to
     * ISAAC cipher being modified during the encoding of the payload, which we cannot
     * recover from without complex state tracking, which will not be supported.
     */
    fun packetGroupStart(messages: List<OutgoingGameMessage>) {
        send {
            PacketGroupStart(messages)
        }
    }

    private inline fun sendOrLogout(block: () -> OutgoingGameMessage) {
        try {
            send(block)
        } catch (e: Exception) {
            player.logout(true, "Exception in packet handling for stack ${e.stackTrace}")
            throw e
        }
    }

    private inline fun send(block: () -> OutgoingGameMessage) {
        val message = block()

        val session = player.session
        if (session == null) {
            message.safeRelease()
        } else {
            session.queue(message)
        }
    }

    private inline fun sendWithPriority(
        priority: Boolean,
        block: () -> OutgoingGameMessage,
    ) {
        player.session?.queue(
            block(),
            if (priority) GameServerProtCategory.HIGH_PRIORITY_PROT else GameServerProtCategory.LOW_PRIORITY_PROT,
        )
    }
}
