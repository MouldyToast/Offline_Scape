package mgi.tools.parser;

import org.jesse.CacheManager;
import org.jesse.game.world.region.XTEALoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Cache;
import mgi.types.config.VarbitDefinitions;
import mgi.utilities.ByteBuffer;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TypeParser {
    public static final Logger log = LoggerFactory.getLogger(TypeParser.class);
    public static final File CACHE_DIRECTORY = new File("data/cache");
    public static final String CACHE_VERSION = "cache-228";

    public static void main(final String[] args) throws Exception {
        final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        final long startTime = System.nanoTime();
        final File cacheZip = new File("data/" + CACHE_VERSION + ".zip");
        if (!CACHE_DIRECTORY.exists())
            CACHE_DIRECTORY.mkdir();
        if (cacheZip.exists()) {
            log.info("Extracting cache from {}...", cacheZip.getPath());
            FileUtils.cleanDirectory(CACHE_DIRECTORY);
            final ZipFile originalZip = new ZipFile(cacheZip);
            originalZip.extractAll("data/cache");
        } else if (CACHE_DIRECTORY.exists() && CACHE_DIRECTORY.list() != null
                && Arrays.stream(CACHE_DIRECTORY.list()).anyMatch(f -> f.startsWith("main_file_cache"))) {
            log.info("Cache files already present in {}, skipping extraction.", CACHE_DIRECTORY.getPath());
        } else {
            throw new FileNotFoundException(
                    "No cache found. Either place " + cacheZip.getPath()
                            + " or run './gradlew :cache:setupCache' to download from OpenRS2.");
        }

        Cache cache = Cache.openCache("data/cache");
        CacheManager.loadCache(cache);
        XTEALoader.load("data/objects/xteas.json");
        CacheManager.loadDefinitions(service, true);

        increaseVarclientAmount();

        cache.close();
        log.info("Cache repack took {} milliseconds", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
        service.shutdown();
    }

    public static void increaseVarclientAmount() {
        {
            final ByteBuffer buffer = new ByteBuffer(1);
            buffer.writeByte(0);
            CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.VARCLIENT).addFile(new mgi.tools.jagcached.cache.File(2000, buffer));
        }
        for (int i = 0; i <= 20_000; i++) {
            var baseDef = VarbitDefinitions.get(i);
            if (baseDef != null) continue;

            VarbitDefinitions varbit = new VarbitDefinitions(i);
            varbit.setBaseVar(20_000 - 1);
            varbit.setStartBit(0);
            varbit.setEndBit(0);
            varbit.pack();
        }

        for (int i = 0; i < 20_000; i++) {
            final ByteBuffer buffer = new ByteBuffer(1);
            buffer.writeByte(0);
            var group = CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.VARPLAYER);
            if (group.findFileByID(i) != null) {
                continue;
            }
            group.addFile(new mgi.tools.jagcached.cache.File(i, buffer));
        }
    }
}
