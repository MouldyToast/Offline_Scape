package org.jesse.game.net.packet

import cloud.rsps.rsprot.Session
import net.rsprot.protocol.game.incoming.misc.client.SendPingReply
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepository
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepositoryBuilder
import java.util.concurrent.TimeUnit

internal typealias PacketConsumer = GameMessageConsumerRepositoryBuilder<Session>

class GameMessageConsumers {
    private val builder: GameMessageConsumerRepositoryBuilder<Session> = GameMessageConsumerRepositoryBuilder()

    fun build(): GameMessageConsumerRepository<Session> {
        builder.friendListAdd()
        builder.friendListDel()
        builder.ignoreListAdd()
        builder.ignoreListDel()
        builder.fcSetRank()
        builder.fcJoinLeave()
        builder.fcKick()

        builder.chatSetMode()
        builder.messagePrivate()
        builder.messagePublic()

        builder.if1()
        builder.if3()
        builder.ifScriptTrigger()
        builder.ifSubOp()
        builder.ifButtonD()
        builder.ifButtonT()
        builder.resumePauseButton()
        builder.resumePCount()
        builder.resumePName()
        builder.resumePObj()
        builder.resumePString()
        builder.closeModal()

        builder.moveGameClick()
        builder.windowStatus()
        builder.mapBuildComplete()
        builder.soundJingleEnd()

        builder.oploc()
        builder.oploc6()
        builder.oploct()

        builder.opobj()
        builder.opobj6()
        builder.opobjt()

        builder.opnpc()
        builder.opnpc6()
        builder.opnpct()

        builder.opplayer()
        builder.opplayert()

        builder.eventAppletFocus()
        builder.eventMouseIdle()
        builder.eventCameraPosition()
        builder.eventMouseScroll()
        builder.eventMouseClick()
        builder.eventMouseMove()
        builder.eventKeyboard()

        builder.teleport()
        builder.clickWorldMap()
        builder.clientCheat()
        builder.oculusLeave()
        builder.bugReport()
        builder.playerReport()

        builder.addListener<SendPingReply> {
            val now = System.nanoTime()
            if (player.session.endPing()) {
                val fps = it.fps
                val gcPercentTime = it.gcPercentTime

                val start = (it.value1.toLong() shl 32) or (it.value2.toLong() and 0xFFFF_FFFF)

                val elapsedNs = now - start
                val elapsedMs = TimeUnit.NANOSECONDS.toMillis(elapsedNs)

                player.sendMessage("Ping: ${elapsedMs}ms, FPS: $fps, GC: ${gcPercentTime}%")
            }
        }

        builder.opWorldEntity()
        builder.opWorldEntity6()
        builder.opWorldEntityT()

        builder.clanChannelKickUser()
        builder.clanChannelFullRequest()
        builder.clanSettingsFullRequest()
        builder.affinedClanSettingsAddBannedFromChannel()
        builder.affinedClanSettingsSetMutedFromChannel()

        builder.resumePCountLong()

        builder.setHeading()
        builder.ifCrmViewOp()
        builder.hiscoreRequest()

        builder.connectionTelemetry()
        builder.membershipPromotionEligibility()
        builder.reflectionCheckReply()
        builder.detectModifiedClient()
        builder.rSevenStatus()

        builder.noTimeout()
        return builder.build()
    }
}
