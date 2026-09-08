package org.jesse.network

import org.jesse.CacheManager
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import mgi.tools.jagcached.cache.Cache
import net.rsprot.protocol.api.js5.Js5Service

class Js5Info(
    private val responses: Int2ObjectMap<ByteBuf>,
) {
    fun getResponse(
        archive: Int,
        group: Int,
    ): ByteBuf? {
        return responses.get((archive shl 16) or group)
    }

    companion object {

        var checksumbuffer: java.nio.ByteBuffer

        init {
            val buffer: ByteArray = CacheManager.getCache().generateInformationStoreDescriptor().buffer
            checksumbuffer = java.nio.ByteBuffer.allocateDirect(buffer.size)
            checksumbuffer.put(buffer)
            checksumbuffer.flip()
        }

        fun of(cache: Cache): Js5Info {
            val responses = Int2ObjectOpenHashMap<ByteBuf>(1 shl 17 - 1)
            responses.putResponse(0xFF, 0xFF, Unpooled.wrappedBuffer(checksumbuffer))
            populateArchive(0xFF, responses, cache)
            for (archive in cache.archives) {
                if (archive == null || archive.groups == null) {
                    continue
                }
                populateArchive(archive.id, responses, cache)
            }
            return Js5Info(responses)
        }

        private fun populateArchive(id: Int, responses: Int2ObjectOpenHashMap<ByteBuf>, cache: Cache) {
            val archive = cache.getIndex(id) ?: return
            for (groupId in 0..archive.groupCount()) {
                if (id == 0xFF && groupId == 0xFF) {
                    continue
                }
                val buffer: ByteArray = CacheManager.getCache().getIndex(id).get(groupId)?.buffer ?: continue
                val capacity = buffer.size
                var container = Unpooled.directBuffer(capacity, capacity)
                container.writeBytes(buffer)
                if (id != 0xFF && container.readableBytes() > 1) {
                    container = container.slice(0, container.readableBytes() - 2)
                }
                responses.putResponse(id, groupId, container)
            }
        }

        private fun Int2ObjectOpenHashMap<ByteBuf>.putResponse(
            archive: Int,
            group: Int,
            buf: ByteBuf,
        ) {
            val readableBytes = buf.readableBytes()
            val output = Unpooled.directBuffer(readableBytes + 8 + (readableBytes / 512))
            Js5Service.prepareJs5Buffer(archive, group, buf, output)
            put((archive shl 16) or group, Unpooled.unreleasableBuffer(output))
            buf.release(buf.refCnt())
        }
    }
}
