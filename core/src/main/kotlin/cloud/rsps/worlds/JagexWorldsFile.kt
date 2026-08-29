package cloud.rsps.worlds

import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.PooledByteBufAllocator
import net.rsprot.buffer.extensions.pjstrnull
import net.rsprot.buffer.extensions.toByteArray

/**
 * @author Jire
 */
object JagexWorldsFile {

    @JvmStatic
    @JvmOverloads
    fun generateJagexWorldsFile(
        allocator: ByteBufAllocator = PooledByteBufAllocator.DEFAULT,
        worlds: Collection<World>
    ): ByteArray {
        val buf = allocator.buffer()
        try {
            val lengthIndex = buf.writerIndex()
            buf.writeInt(0) // placeholder for length

            val countIndex = buf.writerIndex()
            buf.writeShort(0) // placeholder for count

            var count = 0
            worlds.forEach {
                with(it) {
                    buf.writeShort(id.toInt())
                    buf.writeInt(settings)
                    buf.pjstrnull(host)
                    buf.pjstrnull(activity)
                    buf.writeByte(countryFlag.toInt())
                    buf.writeShort(playerCount.toInt())

                    count++
                }
            }

            val length = buf.writerIndex()
            buf.setInt(lengthIndex, length) // set length

            buf.setShort(countIndex, count) // set count

            return buf.toByteArray()
        } finally {
            buf.release()
        }
    }

}
