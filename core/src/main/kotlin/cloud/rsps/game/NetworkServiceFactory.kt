package cloud.rsps.game

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.rsprot.compression.HuffmanCodec
import net.rsprot.compression.provider.DefaultHuffmanCodecProvider
import net.rsprot.crypto.rsa.RsaKeyPair
import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.api.AbstractNetworkServiceFactory
import net.rsprot.protocol.api.GameConnectionHandler
import net.rsprot.protocol.api.handlers.LoginHandlers
import net.rsprot.protocol.api.js5.Js5GroupProvider
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.game.incoming.misc.client.WindowStatus
import net.rsprot.protocol.loginprot.incoming.pow.NopProofOfWorkProvider
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import net.rsprot.protocol.loginprot.outgoing.util.AuthenticatorResponse
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepositoryBuilder
import net.rsprot.protocol.message.codec.incoming.provider.DefaultGameMessageConsumerRepositoryProvider
import net.rsprot.protocol.message.codec.incoming.provider.GameMessageConsumerRepositoryProvider
import java.math.BigInteger

@OptIn(ExperimentalUnsignedTypes::class)
class NetworkServiceFactory : AbstractNetworkServiceFactory<Player>() {

    override val ports = listOf(43594/*, 443*/)

    override val supportedClientTypes = listOf(OldSchoolClientType.DESKTOP)

    override fun getGameConnectionHandler() = object : GameConnectionHandler<Player> {
        override fun onLogin(
            responseHandler: GameLoginResponseHandler<Player>,
            block: LoginBlock<AuthenticationType>
        ) {
            val player = Game.addPlayer(
                responseHandler.networkService,
                name = block.username
            )
            if (player == null) {
                responseHandler.writeFailedResponse(
                    LoginResponse.ServerFull
                )
                return
            }

            val session = responseHandler.writeSuccessfulResponse(
                LoginResponse.Ok(
                    AuthenticatorResponse.NoAuthenticator,
                    2,
                    true,
                    player.index,
                    true,
                    0, 0, 0
                ), block
            )
            player.session = session

            player.onLogin()
            player.updateExtendedInfo()
        }

        override fun onReconnect(
            responseHandler: GameLoginResponseHandler<Player>,
            block: LoginBlock<XteaKey>
        ) {
        }
    }

    override fun getGameMessageConsumerRepositoryProvider(): GameMessageConsumerRepositoryProvider<Player> {
        return DefaultGameMessageConsumerRepositoryProvider(
            GameMessageConsumerRepositoryBuilder<Player>()
                .addListener<WindowStatus> {

                }
                .build()
        )
    }

    override fun getHuffmanCodecProvider() = DefaultHuffmanCodecProvider(
        HuffmanCodec.create(
            Unpooled.wrappedBuffer(
                HuffmanBuffer.huffmanBufferData
            )
        )
    )

    override fun getJs5GroupProvider() = object : Js5GroupProvider {
        override fun provide(archive: Int, group: Int): ByteBuf? {
            return null
        }
    }

    override fun getRsaKeyPair() = RsaKeyPair(
        BigInteger(
            "26781759428714257745926204650858013786819891765634310135195812163794735628178630809644942278785396727126846640150455639652830288060025374140084673396621883876766244440469739961670602668317454857300328479139263204849661670417403060396476630662069177539709095264382718814903801500821637435825808424305333694741"
        ),
        BigInteger(
            "103362356026126041451903048949018411727625890503761603164143922135246192147926678486055036930967466303852078691216089232431985076767556854426637373558353969427346865990497526057518703707221339943932891657119248088639831736963714773178842541484518732451092114460982344500216842020574280863079504608232963628257"
        )
    )

    override fun getLoginHandlers(): LoginHandlers {
        return LoginHandlers(
            proofOfWorkProvider = NopProofOfWorkProvider,
        )
    }

}