package org.jesse.game.parser.impl;

import cloud.rsps.util.JacksonObjectMapper;
import cloud.rsps.util.io.ByteBufferBackedInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Stopwatch;
import org.jesse.game.parser.Parse;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.items.JSONItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

public final class JSONItemDefinitionsLoader implements Parse {

    private static final Logger log = LoggerFactory.getLogger(JSONItemDefinitionsLoader.class);

    private static final Path PATH = Path.of("data", "items", "ItemDefinitions.json");

    public static final Int2ObjectMap<JSONItemDefinitions> definitions = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectMap<JSONItemDefinitions> originalDefinitions = new Int2ObjectOpenHashMap<>();

    @Override
    public void parse() throws Throwable {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final ObjectMapper objectMapper = JacksonObjectMapper.getObjectMapper();
        final ObjectReader objectReader = objectMapper.readerFor(JSONItemDefinitions[].class);

        final JSONItemDefinitions[] defs;

        try (final FileChannel channel = FileChannel.open(PATH, StandardOpenOption.READ)) {
            final MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            final InputStream inputStream = new ByteBufferBackedInputStream(buf);
            defs = objectReader.readValue(inputStream);
        }

        for (final JSONItemDefinitions def : defs) {
            if (def != null) {
                definitions.put(def.getId(), def);
            }
        }

        log.info("Loaded {} item definitions in {}ms.", definitions.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * Looks up a definition based on the key value in the map.
     *
     * @param itemId the key value we using to search for the respective
     *               definition.
     * @return
     */
    public static JSONItemDefinitions lookup(final int itemId) {
        return getDefinitions().get(itemId);
    }

    /**
     * Gets the definitions map.
     *
     * @return
     */
    public static Int2ObjectMap<JSONItemDefinitions> getDefinitions() {
        return definitions;
    }

}
