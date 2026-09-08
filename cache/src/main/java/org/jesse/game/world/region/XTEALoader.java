package org.jesse.game.world.region;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jesse.game.world.DefaultGson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Kris | 7. jaan 2018 : 21:44.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class XTEALoader {
	private static final Logger log = LoggerFactory.getLogger(XTEALoader.class);

	private static final Int2ObjectMap<XTEA> regionToXTEA = new Int2ObjectOpenHashMap<>();
	private static final int[] defaultKeys = new int[4];

	public static void load(String path) throws FileNotFoundException {
		regionToXTEA.clear();
		final Path filePath = Path.of(path);
		if (!Files.exists(filePath)) {
			log.warn("XTEA file not found at {}, map decryption will fail for encrypted regions.", path);
			return;
		}
		try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
			Gson gson = DefaultGson.getGson();
			final XTEA[] xteas = gson.fromJson(br, XTEA[].class);
			for (final XTEA xtea : xteas) {
				if (xtea == null) continue;
				regionToXTEA.put(xtea.getMapsquare(), xtea);
			}
			log.info("Loaded {} XTEA keys from {}.", regionToXTEA.size(), path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the XTEA keys for a region, or default {0,0,0,0} if not found.
	 */
	public static int[] getXTEAs(final int region) {
		return getXTEAKeys(region);
	}

	/**
	 * Gets the actual xtea keys.
	 */
	public static int[] getXTEAKeys(final int regionId) {
		final XTEA xtea = regionToXTEA.get(regionId);
		if (xtea == null) {
			return defaultKeys;
		}
		return xtea.getKey();
	}

	public static void save() {
		final File saveFile = new File("filtered xteas.json");

		final Int2ObjectMap<XTEA> xteas = new Int2ObjectRBTreeMap<>();
		for (final Int2ObjectMap.Entry<XTEA> entry : regionToXTEA.int2ObjectEntrySet()) {
			final int k = entry.getIntKey();
			final XTEA v = entry.getValue();

			if (v.getKey()[0] != 0 && v.getKey()[1] != 0 && v.getKey()[2] != 0 && v.getKey()[3] != 0) {
				xteas.put(k, v);
			}
		}

		final String toJson = new GsonBuilder().setPrettyPrinting().create().toJson(xteas.values());
		PrintWriter writer;
		try {
			writer = new PrintWriter(saveFile, StandardCharsets.UTF_8);
			writer.println(toJson);
			writer.close();
		} catch (final Exception e) {
			log.error("", e);
		}
	}

}
