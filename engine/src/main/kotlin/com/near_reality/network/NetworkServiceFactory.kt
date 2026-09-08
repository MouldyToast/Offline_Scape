package com.near_reality.network

import cloud.rsps.rsprot.RsprotGameConnectionHandler
import cloud.rsps.rsprot.Session
import com.github.michaelbull.logging.InlineLogger
import com.near_reality.game.world.info.WorldProfile
import com.zenyte.game.net.packet.GameMessageConsumers
import com.zenyte.game.world.World
import io.netty.buffer.Unpooled
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.Cache
import net.rsprot.compression.HuffmanCodec
import net.rsprot.compression.provider.DefaultHuffmanCodecProvider
import net.rsprot.compression.provider.HuffmanCodecProvider
import net.rsprot.crypto.rsa.RsaKeyPair
import net.rsprot.protocol.api.AbstractNetworkServiceFactory
import net.rsprot.protocol.api.ChannelExceptionHandler
import net.rsprot.protocol.api.GameConnectionHandler
import net.rsprot.protocol.api.handlers.ExceptionHandlers
import net.rsprot.protocol.api.handlers.LoginHandlers
import net.rsprot.protocol.api.js5.Js5GroupProvider
import net.rsprot.protocol.api.suppliers.NpcInfoSupplier
import net.rsprot.protocol.api.suppliers.WorldEntityInfoSupplier
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatarExceptionHandler
import net.rsprot.protocol.game.outgoing.info.worldentityinfo.WorldEntityAvatarExceptionHandler
import net.rsprot.protocol.loginprot.incoming.pow.NopProofOfWorkProvider
import net.rsprot.protocol.message.codec.incoming.provider.DefaultGameMessageConsumerRepositoryProvider
import net.rsprot.protocol.message.codec.incoming.provider.GameMessageConsumerRepositoryProvider
import java.io.IOException
import java.math.BigInteger

/**
 * @author Kris | 20/08/2024
 * @author Jire
 */
@OptIn(ExperimentalUnsignedTypes::class)
class NetworkServiceFactory(
    private val worldProfile: WorldProfile,
    private val cache: Cache,
) : AbstractNetworkServiceFactory<Session>() {

    override val ports: List<Int> = listOf(worldProfile.port)
    override val supportedClientTypes: List<OldSchoolClientType> = listOf(OldSchoolClientType.DESKTOP)

    private val js5Info = Js5Info.of(cache)

    override fun getExceptionHandlers(): ExceptionHandlers<Session> {
        return ExceptionHandlers(channelExceptionHandler())
    }

    override fun getNpcInfoSupplier(): NpcInfoSupplier {
        return NpcInfoSupplier(npcExceptionHandler())
    }

    private fun channelExceptionHandler(): ChannelExceptionHandler {
        return ChannelExceptionHandler { ctx, cause ->
            val channel = ctx.channel()
            if (channel.isOpen) {
                channel.close()
            }
            if (cause !is IOException) {
                logger.error(cause) { "Exception in netty handlers" }
            }
        }
    }

    override fun getGameConnectionHandler(): GameConnectionHandler<Session> =
        RsprotGameConnectionHandler

    override fun getGameMessageConsumerRepositoryProvider(): GameMessageConsumerRepositoryProvider<Session> {
        val consumers = GameMessageConsumers()
        return DefaultGameMessageConsumerRepositoryProvider(consumers.build())
    }

    override fun getHuffmanCodecProvider(): HuffmanCodecProvider {
        val archive = cache.getArchive(ArchiveType.BINARY)
        val huffmanBytes = archive.findGroupByName("huffman").findFileByID(0).data.buffer
        val codec = HuffmanCodec.create(Unpooled.wrappedBuffer(huffmanBytes))
        return DefaultHuffmanCodecProvider(codec)
    }

    override fun getJs5GroupProvider(): Js5GroupProvider {
        return Js5GroupProvider { archive, group ->
            js5Info.getResponse(archive, group) ?: throw IllegalArgumentException("Group $archive:$group not available")
        }
    }

    private fun npcExceptionHandler(): NpcAvatarExceptionHandler {
        return NpcAvatarExceptionHandler { index, exception ->
            World.getNPCs().get(index)?.remove()
            logger.error(exception) { "Error with NPC $index" }
        }
    }

    override fun getRsaKeyPair(): RsaKeyPair {
        return RsaKeyPair(EXPONENT, MODULUS)
    }

    override fun getWorldEntityInfoSupplier(): WorldEntityInfoSupplier {
        return WorldEntityInfoSupplier(worldEntityExceptionHandler())
    }

    private fun worldEntityExceptionHandler(): WorldEntityAvatarExceptionHandler {
        return WorldEntityAvatarExceptionHandler { _, exception ->
            logger.error(exception) { "Error with world entity" }
        }
    }

    override fun getLoginHandlers(): LoginHandlers {
        return LoginHandlers(
            proofOfWorkProvider = NopProofOfWorkProvider,
        )
    }

    private companion object {
        private val logger = InlineLogger()

        private val EXPONENT = BigInteger(
            "26781759428714257745926204650858013786819891765634310135195812163794735628178630809644942278785396727126846640150455639652830288060025374140084673396621883876766244440469739961670602668317454857300328479139263204849661670417403060396476630662069177539709095264382718814903801500821637435825808424305333694741"
        )
        private val MODULUS = BigInteger(
            "103362356026126041451903048949018411727625890503761603164143922135246192147926678486055036930967466303852078691216089232431985076767556854426637373558353969427346865990497526057518703707221339943932891657119248088639831736963714773178842541484518732451092114460982344500216842020574280863079504608232963628257"
        )
    }

}
