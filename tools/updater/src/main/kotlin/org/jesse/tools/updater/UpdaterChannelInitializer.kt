package org.jesse.tools.updater

import org.jesse.network.DefaultExceptionHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LineBasedFrameDecoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.util.CharsetUtil

/**
 * @author Jire
 */
object UpdaterChannelInitializer : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline().run {
            addLast(ReadTimeoutHandler(30))

            addLast(LineBasedFrameDecoder(255))
            addLast(StringDecoder(CharsetUtil.UTF_8))

            addLast(UpdaterChannelHandler())

            addLast(DefaultExceptionHandler())
        }
    }

}
