package cloud.rsps.util.io;

import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class ByteBufferBackedInputStream extends InputStream {
    private final ByteBuffer buffer;

    public ByteBufferBackedInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        return buffer.hasRemaining() ? buffer.get() & 0xFF : -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (!buffer.hasRemaining()) return -1;
        int toRead = Math.min(len, buffer.remaining());
        buffer.get(b, off, toRead);
        return toRead;
    }

    @Override
    public int available() {
        return buffer.remaining();
    }

    public static <T> T from(Path path, Function<InputStream, T> consumer) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            return consumer.apply(new ByteBufferBackedInputStream(buf));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read: " + path, e);
        }
    }
}
