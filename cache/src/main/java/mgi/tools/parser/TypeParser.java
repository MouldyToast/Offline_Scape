package mgi.tools.parser;

import org.jesse.CacheManager;
import org.jesse.game.content.achievementdiary.DiaryArea;
import org.jesse.game.content.achievementdiary.DiaryComplexity;
import org.jesse.game.content.achievementdiary.DiaryInfo;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.Regions;
import org.jesse.game.world.region.XTEALoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.types.config.VarbitDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.utilities.ByteBuffer;
import net.lingala.zip4j.ZipFile;
import net.runelite.cache.definitions.loaders.LocationsLoader;
import net.runelite.cache.definitions.loaders.MapLoader;
import net.runelite.cache.definitions.loaders.MapLoaderPre209;
import net.runelite.cache.definitions.savers.LocationSaver;
import net.runelite.cache.definitions.savers.MapSaver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings({"unchecked", "DataFlowIssue"})
public class TypeParser {
    public static final Logger log = LoggerFactory.getLogger(TypeParser.class);
    private static final List<Definitions> definitions = new ArrayList<>();
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

        packDynamicConfigs();
        pack(EnumDefinitions.class);
        packMaps(service);
        increaseVarclientAmount();
        copyMaps();
        cache.close();

        log.info("Cache repack took {} milliseconds", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));

        service.shutdown();
    }

    private static void copyMaps() {
        log.info("Begin map copy process...");

        log.info("   Beginning Evil Bob / Train Island...");
        copyMapRegion(10058, 9546);
        log.info("   Finish Evil Bob / Train Island...");

        log.info("   Beginning RDI...");
        copyMapRegion(11605, 11604);
        log.info("   Finish RDI...");
        log.info("End map copy process.");
    }

    public static void pack(final Class<?>... types) {
        final ArrayList<Definitions> filtered =
                definitions.stream().filter(d -> ArrayUtils.contains(types,
                        d.getClass())).collect(Collectors.toCollection(ArrayList::new));
        filtered.forEach(Definitions::pack);
        if (!filtered.isEmpty()) {
            log.info("Finished packing {} type{}", filtered.size(), filtered.size() == 1 ? "" : "s.");
        }
    }

    private static void packDynamicConfigs() {
        EnumDefinitions enumDef;
        final DiaryInfo[][] diaries = DiaryInfo.load(null);
        for (final DiaryInfo[] diaryEnum : diaries) {
            final HashMap<Integer, Object> values = new HashMap<>();
            DiaryArea area = null;
            for (final DiaryInfo diary : diaryEnum) {
                if (diary.isAutoCompleted()) {
                    continue;
                }
                final DiaryComplexity complexity = diary.getType();
                area = diary.getArea();
                values.put(complexity.ordinal(),
                        (int) (values.get(complexity.ordinal()) == null ? 0 : values.get(complexity.ordinal()))
                                + 1);
            }
            enumDef = new EnumDefinitions();
            enumDef.setId(2501 + area.getIndex());
            enumDef.setKeyType("int");
            enumDef.setValueType("int");
            enumDef.setDefaultInt(-1);
            enumDef.setValues(values);
            definitions.add(enumDef);
        }
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

    public static void packMapRawPre209(Cache cache, int regionID, byte[] locsBlock, byte[] mapBlock) {
        int x = (regionID >> 8) & 0xFF;
        int y = regionID & 0xFF;

        byte[] outputMapData = Optional.ofNullable(mapBlock)
                .map(data -> MapSaver.save(MapLoaderPre209.load(x, y, data)))
                .orElse(null);

        byte[] outputLandData = Optional.ofNullable(locsBlock)
                .map(data -> LocationSaver.save(new LocationsLoader().load(x, y, data)))
                .orElse(null);

        packMap(cache, regionID, outputMapData, outputLandData);
    }

    public static void packMapPre209(final int id, final byte[] landscape, final byte[] map) {
        try {
            if (map == null) return;

            packMapRawPre209(CacheManager.getCache(), id, map, landscape);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.err.println("Failed to pack map[" + id + "]");
        }
    }

    public static void copyMapRegion(final int source_region, final int... target_regions) {
        final int sourceRegionX = source_region >> 8;
        final int sourceRegionY = source_region & 255;

        Cache cache = CacheManager.getCache();
        Archive maps = cache.getArchive(ArchiveType.MAPS);

        final int[] xteas = XTEALoader.getXTEAKeys(source_region);
        Group mGroup = maps.findGroupByName("m" + sourceRegionX + "_" + sourceRegionY);
        Group lGroup = maps.findGroupByName("l" + sourceRegionX + "_" + sourceRegionY, xteas);

        for (int target_region : target_regions) {

            byte[] outputMapData = Optional.ofNullable(mGroup.getFiles()[0].getData().getBuffer())
                    .map(data -> MapSaver.save(new MapLoader().load(sourceRegionX, sourceRegionY, data)))
                    .orElse(null);

            byte[] outputLandData = Optional.ofNullable(lGroup.getFiles()[0].getData().getBuffer())
                    .map(data -> LocationSaver.save(new LocationsLoader().load(sourceRegionX, sourceRegionY, data)))
                    .orElse(null);

            packMap(target_region, outputLandData, outputMapData);
            log.info("Copying region {} into region {}", source_region, target_region);
        }
    }

    public static void packMap(final int id, final byte[] l_data, final byte[] m_data) {
        packMap(CacheManager.getCache(), id, m_data, l_data);
    }

    public static void packMap(final Cache cache, final int id, final byte[] m_data,
                               byte[] l_data) {
        try {

            final Archive archive = cache.getArchive(ArchiveType.MAPS);
            final int[] xteas = XTEALoader.getXTEAKeys(id);
            final int regionX = id >> 8;
            final int regionY = id & 255;
            final Group mapGroup = archive.findGroupByName("m" + regionX + "_" + regionY);
            Group landGroup = archive.findGroupByName("l" + regionX + "_" + regionY, null, false);
            if (landGroup != null) {
                archive.deleteGroup(landGroup);
            }
            int length_l = 0;
            if (l_data != null) {
                length_l = l_data.length;
                final Group newLandGroup = new Group(archive.getFreeGroupID(),
                        new mgi.tools.jagcached.cache.File(new ByteBuffer(l_data)));
                newLandGroup.setName("l" + regionX + "_" + regionY);
                newLandGroup.setXTEA(xteas);
                archive.addGroup(newLandGroup);
            }
            int length_m = 0;
            if (m_data != null) {
                length_m = m_data.length;
                if (mapGroup != null) {
                    mapGroup.findFileByID(0).setData(new ByteBuffer(m_data));
                    mapGroup.setXTEA(null);
                } else {
                    final Group newMapGroup = new Group(archive.getFreeGroupID() + 1,
                            new mgi.tools.jagcached.cache.File(new ByteBuffer(m_data)));
                    newMapGroup.setName("m" + regionX + "_" + regionY);
                    newMapGroup.setXTEA(null);
                    archive.addGroup(newMapGroup);
                }
            }
            System.out.println("Packed map[" + id + "] sizes l[" + length_l + "], m[" + length_m + "]");
        } catch (Exception e) {
            System.err.println("Failed to pack map[" + id + "]");
            e.printStackTrace(System.err);
        }
    }

    private static void packMaps(final ExecutorService service) throws IOException {
        packMapPre209(11567, null,
                Regions.inject(11567, null, new WorldObject(187, 10, 1, new Location(2919, 3054, 0))));
        packMapPre209(11595, null,
                Regions.inject(11595, null, new WorldObject(26254, 10, 0, new Location(2931, 4822, 0)),
                        new WorldObject(26254, 10, 0, new Location(2896, 4821, 0)),
                        new WorldObject(26254, 10, 1,
                                new Location(2900, 4845, 0)),
                        new WorldObject(26254, 10, 3, new Location(2920, 4848, 0))));
        packMapPre209(13109, null,
                Regions.inject(13109, null, new WorldObject(187, 10, 1, new Location(3322, 3428, 0))));
    }

}
