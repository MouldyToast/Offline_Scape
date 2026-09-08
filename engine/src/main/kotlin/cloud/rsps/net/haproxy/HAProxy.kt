package cloud.rsps.net.haproxy

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.haproxy.HAProxyMessage
import io.netty.util.AttributeKey
import java.net.InetSocketAddress

object HAProxy {
    private val HAPROXY_ADDRESS: AttributeKey<String> =
        AttributeKey.valueOf("haproxy_address")

    val Channel.hostAddress: String
        get() {
            attr(HAPROXY_ADDRESS).get()?.let { return it }
            return (remoteAddress() as? InetSocketAddress)?.address?.hostAddress ?: "unknown"
        }

    val net.rsprot.protocol.api.Session<*>.hostAddress: String
        get() = ctx.channel().hostAddress

    val ChannelHandlerContext.hostAddress: String
        get() = channel().hostAddress

    fun setAddress(channel: Channel, message: HAProxyMessage) {
        channel.attr(HAPROXY_ADDRESS).set(message.sourceAddress())
    }
}
