package org.jesse.network

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.timeout.ReadTimeoutException
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.channels.ClosedChannelException

/**
 * @author Jire
 */
interface BootstrapFactory {

    fun createEventLoopGroup(threads: Int = 0): EventLoopGroup =
        when {
//            IOUring.isAvailable() -> IOUringEventLoopGroup(threads)
//            Epoll.isAvailable() -> EpollEventLoopGroup(threads)
//            KQueue.isAvailable() -> KQueueEventLoopGroup(threads)
            else -> NioEventLoopGroup(threads)
        }

    fun getServerSocketChannelClass(): Class<out ServerChannel> =
        when {
//            IOUring.isAvailable() -> IOUringServerSocketChannel::class.java
//            Epoll.isAvailable() -> EpollServerSocketChannel::class.java
//            KQueue.isAvailable() -> KQueueServerSocketChannel::class.java
            else -> NioServerSocketChannel::class.java
        }

    fun createServerBootstrap(
        parentGroup: EventLoopGroup = createEventLoopGroup(1),
        childGroup: EventLoopGroup = createEventLoopGroup(),
        channel: Class<out ServerChannel> = getServerSocketChannelClass()
    ) = ServerBootstrap().apply {
        group(parentGroup, childGroup)
        channel(channel)
    }

}

class DefaultExceptionHandler : ChannelDuplexHandler() {

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        ctx.close()

        if (ReadTimeoutException.INSTANCE == cause
            || cause is ClosedChannelException
            || ignoredExceptionMessages.contains(cause.message)
        ) return

        logger.error("", cause)
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(DefaultExceptionHandler::class.java)

        val ignoredExceptionMessages: ObjectSet<String> = ObjectSet.of(
            "An established connection was aborted by the software in your host machine",
            "Connection reset"
        )
    }

}