package mgi.tools.parser;

import com.esotericsoftware.kryo.Kryo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jesse.cache_tool.packing.custom.KeepSetDefinitionOverrides;
import org.jesse.util.gson.Int2ObjectMapDeserializer;
import org.jesse.util.gson.IntListTypeAdapter;
import org.jesse.util.gson.Object2IntMapDeserializer;
import org.jesse.util.gson.ObjectCollectionDeserializer;
import org.jesse.CacheManager;
import org.jesse.ContentConstants;
import org.jesse.game.content.achievementdiary.DiaryArea;
import org.jesse.game.content.achievementdiary.DiaryComplexity;
import org.jesse.game.content.achievementdiary.DiaryInfo;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.MapUtils;
import org.jesse.game.world.region.Regions;
import org.jesse.game.world.region.XTEALoader;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import kotlin.text.Charsets;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.types.component.ComponentDefinitions;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.StructDefinitions;
import mgi.types.config.VarbitDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.Buffer;
import mgi.utilities.ByteBuffer;
import net.lingala.zip4j.ZipFile;
import net.runelite.cache.definitions.loaders.LocationsLoader;
import net.runelite.cache.definitions.loaders.MapLoader;
import net.runelite.cache.definitions.loaders.MapLoaderPre209;
import net.runelite.cache.definitions.savers.LocationSaver;
import net.runelite.cache.definitions.savers.MapSaver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tommeh | 16/01/2020 | 01:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
@SuppressWarnings({"unchecked", "DataFlowIssue"})
public class TypeParser {
    public static final Logger log = LoggerFactory.getLogger(TypeParser.class);
    private static final List<Definitions> definitions = new ArrayList<>();
    public static final Kryo KRYO = new Kryo();
    public static final File CACHE_DIRECTORY = new File("data/cache");
    public static final File CACHE_STAGING_DIRECTORY = new File("data/cache-staging");
    public static final String CACHE_VERSION = "cache-228";
    public static final File CACHE_ORIGINAL_DIRECTORY = new File("data/" + CACHE_VERSION);
    public static final boolean ENABLED_MAP_PACKING = true;

    private static final ThreadLocal<Gson> gson = ThreadLocal.withInitial(() ->
            new GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .registerTypeAdapter(IntList.class, IntListTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Object2IntMap.class, Object2IntMapDeserializer.INSTANCE)
                    .registerTypeAdapter(Int2ObjectMap.class, Int2ObjectMapDeserializer.INSTANCE)
                    .registerTypeAdapter(ObjectCollection.class, ObjectCollectionDeserializer.INSTANCE)
                    .create());

    public static Gson getGson() {
        return gson.get();
    }

    public static void main(final String[] args) throws Exception {
        final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        String targetCacheDirectory = "data/cache";

        String type = "";
        boolean isProductionCacheGen = false;
        if (args.length > 0) {
            type = args[0];
        }
        if (args.length > 2 && Objects.equals(args[2], "production")) {
            isProductionCacheGen = true;
        }

        final long startTime = System.nanoTime();
        final File cacheZip = new File("data/" + CACHE_VERSION + ".zip");
        if (!CACHE_DIRECTORY.exists())
            CACHE_DIRECTORY.mkdir();
        if (cacheZip.exists()) {
            // Legacy path: extract from zip (e.g. Jire's cache-228.zip)
            log.info("Extracting cache from {}...", cacheZip.getPath());
            FileUtils.cleanDirectory(CACHE_DIRECTORY);
            final ZipFile originalZip = new ZipFile(cacheZip);
            originalZip.extractAll(targetCacheDirectory);
        } else if (CACHE_DIRECTORY.exists() && CACHE_DIRECTORY.list() != null
                && java.util.Arrays.stream(CACHE_DIRECTORY.list()).anyMatch(f -> f.startsWith("main_file_cache"))) {
            // OpenRS2 path: cache files already downloaded by :cache:setupCache
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
        //Definitions.loadDefinitions(Definitions.cacheLowPriorityDefinitions);

        initializeKryo();
        pack(NPCDefinitions.class);
        packDynamicConfigs();
        KeepSetDefinitionOverrides.pack();
        removeCATasks();
        pack(
                ArrayUtils.addAll(
                        Definitions.highPriorityDefinitions,
                        Definitions.cacheLowPriorityDefinitions)
        );
        packClientScripts();
        packMaps(service);
        increaseVarclientAmount();
        KeepSetDefinitionOverrides.packObjects();
        copyMaps();
        KeepSetDefinitionOverrides.packEnums();
        cache.close();

        /*
         * cache = Cache.openCache(targetCacheDirectory);
         * PackFromConsecutively.packAll(cache); // Edenify
         * cache.close();
         */

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

    public static void initializeKryo() {
        for (final Class<?> d : Definitions.cacheLowPriorityDefinitions) {
            KRYO.register(d);
        }
        for (final Class<?> d : Definitions.highPriorityDefinitions) {
            KRYO.register(d);
        }
        KRYO.register(int[].class);
        KRYO.register(short[].class);
        KRYO.register(String[].class);
        KRYO.register(String[][].class);
        KRYO.register(Int2ObjectOpenHashMap.class);
    }

    public static void parse(final File folder) {
        parse(folder, true, TypeReader.readers);
    }

    public static void parse(File folder, boolean throwExceptions, TypeReader... readers) {
        Map<String, TypeReader> readersMap = Arrays.stream(readers)
                .collect(Collectors.toMap(TypeReader::getType, e -> e));
        parse(folder, throwExceptions, readersMap,
                new File(folder, "component").getPath(),
                readersMap.get("component"));
    }

    public static void parse(final File folder,
                             boolean throwExceptions,
                             Map<String, TypeReader> readers,
                             String componentFolderPath,
                             TypeReader componentTypeReader) {
        final ObjectMapper mapper = new ObjectMapper(new TomlFactory());

        File f = null;
        try {
            for (final File file : Objects.requireNonNull(folder.listFiles())) {
                f = file;
                if (file.getPath().endsWith("exclude"))
                    continue;
                if (file.isDirectory()) {
                    parse(file, throwExceptions, readers, componentFolderPath, componentTypeReader);
                } else {
                    if (!Files.getFileExtension(file.getName()).equals("toml"))
                        continue;

                    String fileString = FileUtils.readFileToString(file, Charsets.UTF_8);
                    fileString = fileString.replace("%SERVER_NAME%", ContentConstants.SERVER_NAME);

                    if (componentTypeReader != null && file.getPath().startsWith(componentFolderPath)) {
                        final JsonNode tree = mapper.readTree(fileString);
                        final List<Definitions> readDefinitions = componentTypeReader.read(mapper, tree);
                        definitions.addAll(readDefinitions);
                    } else {
                        final JsonNode tree = mapper.readTree(fileString);
                        final Iterator<Map.Entry<String, JsonNode>> fieldsIterator = tree.properties().iterator();
                        while (fieldsIterator.hasNext()) {
                            final Map.Entry<String, JsonNode> entry = fieldsIterator.next();

                            final TypeReader reader = readers.get(entry.getKey());
                            if (reader == null) {
                                if (!throwExceptions)
                                    continue;
                                System.err.println(readers);
                                throw new RuntimeException("Could not find a reader for: " + entry.getKey());
                            }
                            //System.out.println("reader " + reader.getClass().getSimpleName() + " for key \"" + entry.getKey() + "\" for file " + file.getName());

                            final JsonNode value = entry.getValue();
                            if (value.isArray()) {
                                for (final JsonNode node : value) {
                                    final Map<String, Object> properties =
                                            mapper.convertValue(node, new TypeReference<>() {
                                            });
                                    //System.out.println("value \"" + entry.getKey() + "\" is array: " + properties);
                                    definitions.addAll(reader.read(properties));
                                }
                            } else if (value.isObject()) {
                                final Map<String, Object> properties =
                                        mapper.convertValue(value, new TypeReference<>() {
                                        });
                                //System.out.println("value \"" + entry.getKey() + "\" is object: " + properties);
                                definitions.addAll(reader.read(properties));
                            } else {
                                final Map<String, Object> properties =
                                        mapper.convertValue(value, new TypeReference<>() {
                                        });
                                //System.out.println("value \"" + entry.getKey() + "\" is unknown (" + value.getNodeType().name() + "): " + properties);
                                definitions.addAll(reader.read(properties));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Something went wrong in {}", f == null ? null : f.getPath());
            e.printStackTrace(System.err);
            System.exit(0);
        }
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
        //for (Int2IntMap.Entry entry : EnumDefinitions.getIntEnum(1002).getValues().int2IntEntrySet()) {
        //    System.out.println(entry.getIntKey()+"="+entry.getIntValue());
        //}
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

    public static ObjectDefinitions cloneObject(int from, int to) {
        ObjectDefinitions def = new ObjectDefinitions(to, new ByteBuffer(new byte[1]));
        def.copy(from);
        return def;
    }

    public static void packSound(final int id, final byte[] bytes) {
        CacheManager.getCache().getArchive(ArchiveType.SYNTHS).addGroup(new Group(id,
                new mgi.tools.jagcached.cache.File(new ByteBuffer(bytes))));
    }

    private static void packClientScripts() throws Exception {
    }

    private static void packCs2FromDirectory(String first) throws IOException {
        packCs2FromDirectory(first, false);
    }

    private static void packCs2FromDirectory(String first, boolean verify) throws IOException {
        var cs2Files = Paths.get(first).toFile().listFiles();
        for (var file : cs2Files) {
            if (file.isDirectory()) {
                var id = Integer.parseInt(file.getName());
                var child = Paths.get(first + id + "/").toFile().listFiles();
                for (var file2 : child) {
                    var name = file2.getName().replaceAll(".cs2", "");
                    packClientScriptNamed(id, name, java.nio.file.Files.readAllBytes(file2.toPath()));
                    log.info("Packed named CS2 \"{}\" with ID: {}", name, id);
                }
            } else {
                var id = Integer.parseInt(file.getName().replaceAll(".cs2", ""));
                var bytes = java.nio.file.Files.readAllBytes(file.toPath());
                packClientScript(id, bytes);
                if (verify) {
                    var buffer = new Buffer(bytes);
                    buffer.offset = bytes.length - 2;
                    int length = buffer.readUnsignedShort();
                    int onset = bytes.length - 2 - length - 12;
                    buffer.offset = onset;
                    int a = buffer.readInt();
                    int localIntCount = buffer.readUnsignedShort();
                    int localStringCount = buffer.readUnsignedShort();
                    int intArgumentCount = buffer.readUnsignedShort();
                    int stringArgumentCount = buffer.readUnsignedShort();
                    int var6 = buffer.readUnsignedByte();
                    System.out.println("ID: " + id + ", a: " + a + ", LocalIntCount: " + localIntCount + " LocalStringCount: " + localStringCount + " IntArgumentCount: " + intArgumentCount + " StringArgumentCount: " + stringArgumentCount + " Var6: " + var6);
                }
                log.info("Packed CS2 with ID: {}", id);
            }
        }
    }

    public static void packRustyScripts(String folderPath) {
        for (File file : Objects.requireNonNull(Paths.get(folderPath).toFile().listFiles())) {
            try {
                final int id = Integer
                        .parseInt(file.getName().replace("-0.bin", "").replace("12-", ""));
                packClientScript(id, java.nio.file.Files.readAllBytes(file.toPath()));
            } catch (Exception e) {
                System.err.println("File name of " + file + " must be an integer!");
                e.printStackTrace(System.err);
            }
        }
    }

    public static void packClientScriptsRecursive(String path) throws IOException {
        java.nio.file.Files.walk(Path.of(path)).forEach(p -> {
            File f = p.toFile();
            if(f.getName().endsWith(".cs2")) try {
                int id = Integer.parseInt(f.getName().split(".cs2")[0]);
                byte[] bytes = java.nio.file.Files.readAllBytes(p);
                //System.err.println(f.getName());
                packClientScript(id, bytes);
            } catch(Exception e) {
                throw new RuntimeException(f.getName(), e);
            }
        });
    }

    public static void packClientScript(final int id, final byte[] bytes) {
        CacheManager.getCache().getArchive(ArchiveType.CLIENTSCRIPTS).addGroup(new Group(id,
                new mgi.tools.jagcached.cache.File(new ByteBuffer(bytes))));
    }

    public static void packClientScriptNamed(final int id, final String archive_name, final byte[] bytes) {
        CacheManager.getCache().getArchive(ArchiveType.CLIENTSCRIPTS).addGroup(new Group(id, archive_name, 1,
                new mgi.tools.jagcached.cache.File(new ByteBuffer(bytes))));
    }

    public static void packMapsRSPSi(int baseRegionID, String packFilePath) throws IOException {
        packMapsRSPSi(CacheManager.getCache(), baseRegionID, packFilePath);
    }

    public static void packMapsRSPSi(Cache cache, int baseRegionID, String packFilePath)
            throws IOException {
        byte[] packBytes = java.nio.file.Files.readAllBytes(Path.of(packFilePath));
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(packBytes);

        int baseRegionX = (baseRegionID >> 8) & 0xFF;
        int baseRegionY = baseRegionID & 0xFF;

        int mapSquareCount = buffer.getInt();

        for (int i = 0; i < mapSquareCount; i++) {
            buffer.getInt(); // locGroupID
            buffer.getInt(); // mapGroupID

            int localMapSqGridX = buffer.getInt();
            int localMapSqGridZ = buffer.getInt();

            int locsBlockLength = buffer.getInt();
            byte[] locsBlock = new byte[locsBlockLength];
            buffer.get(locsBlock);

            int mapBlockLength = buffer.getInt();
            byte[] mapBlock = new byte[mapBlockLength];
            buffer.get(mapBlock);

            int regionX = baseRegionX + localMapSqGridX;
            int regionY = baseRegionY + localMapSqGridZ;

            int regionID = (regionX << 8) | regionY;
            locsBlock = modifyRegions(regionID, locsBlock);
            packMapRawPre209(cache, regionID, locsBlock, mapBlock);
        }
    }

    private static byte[] modifyRegions(int regionID, byte[] locsBlock) {
        if (regionID == 8036) {
            return Regions.inject(locsBlock,
                    null,
                    new WorldObject(49210, 22, 2, new Location(2041, 6427, 0)), //Stepping stone
                    new WorldObject(49207, 10, 1, new Location(2040, 6423, 0)) //Cave Passage
            );
        }

        return locsBlock;
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

    public static void packMap(final int id, String landscapeFilePath, String mapFilePath)
            throws IOException {
        try {
            packMap(CacheManager.getCache(), id,
                    java.nio.file.Files.readAllBytes(Paths.get(landscapeFilePath)),
                    java.nio.file.Files.readAllBytes(Paths.get(mapFilePath)));
            System.err.println("Packed map[" + id + "] land = " + landscapeFilePath + ", map = " + mapFilePath);
        } catch (Exception e) {
            System.err.println("Failed to pack map[" + id + "] land = " + landscapeFilePath + ", map = " + mapFilePath);
            e.printStackTrace(System.err);
        }
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

    public static void copyMapRegionFromTargetCache(final Cache source_cache, final int source_region, final int target_region) {
        final int sourceRegionX = source_region >> 8;
        final int sourceRegionY = source_region & 255;

        Archive maps = source_cache.getArchive(ArchiveType.MAPS);

        final int[] xteas = XTEALoader.getXTEAKeys(source_region);
        Group mGroup = maps.findGroupByName("m" + sourceRegionX + "_" + sourceRegionY);
        Group lGroup = maps.findGroupByName("l" + sourceRegionX + "_" + sourceRegionY, xteas);


        byte[] outputMapData = Optional.ofNullable(mGroup.getFiles()[0].getData().getBuffer())
                .map(data -> MapSaver.save(new MapLoader().load(sourceRegionX, sourceRegionY, data)))
                .orElse(null);

        byte[] outputLandData = Optional.ofNullable(lGroup.getFiles()[0].getData().getBuffer())
                .map(data -> LocationSaver.save(new LocationsLoader().load(sourceRegionX, sourceRegionY, data)))
                .orElse(null);

        packMap(target_region, outputLandData, outputMapData);
        log.info("Copying old region {} into new cache region {}", source_region, target_region);
    }

    public static void packMap(final int id, final byte[] l_data, final byte[] m_data) {
        packMap(CacheManager.getCache(), id, m_data, l_data);
    }

    public static void packMap(final Cache cache, final int id, final byte[] m_data,
                               byte[] l_data) {

        if (!ENABLED_MAP_PACKING) {
            System.out.println("Skipping packing map[" + id + ']');
            return;
        }
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
                l_data = MapChanges.modifyRegionData(id, l_data);
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
        KeepSetDefinitionOverrides.applyMapEdits();
    }

    public static void removeCATasks() {
        final String[] tasksToRemove = {
                "Fighting as Intended II",
                "Fighting as Intended",
                "Fragment of Seren Speed-Trialist",
                "Galvek Speed-Trialist",
                "Glough Speed-Trialist",
                "The Flame Skipper",
                "Arooo No More",
                "Perfect Olm (Solo)",
                "Perfect Olm (Trio)",
                "A Not So Special Lizard",
                "Chambers of Xeric: CM (5-Scale) Speed-Chaser",
                "Chambers of Xeric: CM (Solo) Speed-Chaser",
                "Moving Collateral",
                "Perfect Corrupted Hunllef",
                "Perfect Crystalline Hunllef",
                "Perfect Nightmare",
                "Perfect Maiden",
                "Pop It",
                "Perfect Nylocas",
                "Perfect Verzik",
                "Perfect Sotesteg",
                "Perfect Bloat",
                "Can't Drain This",
                "Perfect Xarpus",
                "Nibblers, Begone!",
                "You Didn't Say Anything About a Bat",
                "Fight Caves Speed-Chaser",
                "Denying the Healers",
                "The Walk",
                "Perfect Zulrah",
                "Chambers of Xeric (Solo) Speed-Runner",
                "Chambers of Xeric (5-Scale) Speed-Runner",
                "Chambers of Xeric (Trio) Speed-Runner",
                "Chambers of Xeric: CM (Solo) Speed-Runner",
                "Egniol Diet II",
                "Corrupted Gauntlet Speed-Runner",
                "Perfect Nex",
                "Perfect Phosani's Nightmare",
                "Phosani's Speedrunner",
                "Nightmare (5-Scale) Speed-Runner",
                "Terrible Parent",
                "A Long Trip",
                "Perfect Theatre",
                "Theatre (5-Scale) Speed-Runner",
                "Theatre (Duo) Speed-Runner",
                "Theatre (4-Scale) Speed-Runner",
                "Theatre (Trio) Speed-Runner",
                "Wasn't Even Close",
                "Nibbler Chaser",
                "The Floor Is Lava",
                "No Luck Required",
                "Jad? What Are You Doing Here?",
                "Budget Setup",
                "Playing with Jads",
                "Facing Jad Head-on II",
                "Denying the Healers II",
                "No Time for a Drink",
                "Expert Tomb Explorer",
                "Something of an expert myself",
                "Expert tomb looter",
                "Ba-bananza",
                "Rockin' around the croc",
                "Doesn't bug me",
                "All out of medics",
                "Warden't you believe it",
                "Resourceful raider",
                "But... Damage",
                "Fancy feet",
                "Tombs speed runner ii",
                "Tombs speed runner iii",
                "Amascut's remnant",
                "Maybe I'm the boss.",
                "Expert tomb raider",
                "Akkhan't do it",
                "All praise zebak",
                "Perfection of het",
                "Perfection of apmeken",
                "Perfection of crondis",
                "Perfection of scabaras",
                "Insanity",
                "Tomb Explorer",
                "Hardcore raiders",
                "Hardcore tombs",
                "Helpful spirit who?",
                "Dropped the ball",
                "No skipping allowed",
                "Down do specs",
                "Perfect het",
                "Perfect apmeken",
                "Perfect crondis",
                "I'm in a rush",
                "You are not prepared",
                "Tomb looter",
                "Tomb raider",
                "Tombs speed runner",
                "Better get movin'",
                "Chompington",
                "Perfect akkha",
                "Perfect ba-ba",
                "Perfect zebak",
                "Perfect scabaras",
                "Perfect kephri",
                "Perfect Wardens",
                "Novice Tomb explorer",
                "Novice tomb looter",
                "Movin' on up",
                "Confident raider",
                "Novice tomb raider",
                "Into the den of giants",
                "Not so great after all",
                "Tempoross novice",
                "Master of buckets",
                "Calm before the storm",
                "Fire in the hole!",
                "Tempoross Champion",
                "The Lone Angler",
                "Dress Like You Mean It",
                "Why Cook?",
                "Theatre of Blood: SM Adept",
                "Anticoagulants",
                "Appropriate Tools",
                "They Won't Expect This",
                "Chally Time",
                "Nylocas, On the Rocks",
                "Just To Be Safe",
                "Don't Look at Me!",
                "No-Pillar",
                "Attack, Step, Wait",
                "Pass It On",
                "Theatre of Blood: SM Speed-Chaser",
                "The II Jad Challenge",
                "TzHaar-Ket-Rak's Speed-Trialist",
                "Facing Jad Head-on III",
                "The IV Jad Challenge",
                "TzHaar-Ket-Rak's Speed-Chaser",
                "Facing Jad Head-on IV",
                "Supplies? Who Needs 'em?",
                "Multi-Style Specialist",
                "Hard Mode? Completed It",
                "The VI Jad Challenge",
                "TzHaar-Ket-Rak's Speed-Runner",
                "It Wasn't a Fluke",
                "Versatile Drainer",
                "Blind Spot",
                "Hard Mode? Completed It",
                "Stop Right There!",
                "Personal Space",
                "Royal Affairs",
                "Harder Mode I",
                "Harder Mode II",
                "Nylo Sniper",
                "Team Work Makes the Dream Work",
                "Harder Mode III",
                "Pack Like a Yak",
                "Theatre: HM (Trio) Speed-Runner",
                "Theatre: HM (4-Scale) Speed-Runner",
                "Theatre: HM (5-Scale) Speed-Runner",
                "Theatre of Blood: HM Grandmaster",
                "Pray for Success",
                "Sorry, What Was That?",
                "Mage of the Ruins",
                "I'd Rather Not Learn",
                "Claw Clipper",
                "Praying to the Gods",
                "Weed Whacker",
                "Chitin Penetrator",
                "Insect Repellent",
                "I Can't Reach That",
                "Guardians No More",
                "Zulrah Adept",
                "Vet'ion Adept",
                "Perfect Sire",
                "Unrequired Antifire",
                "Anti-Bite Mechanics",
                "Hot on Your Feet",
                "3, 2, 1 - Mage",
                "3, 2, 1 - Range",
                "Egniol Diet",
                "Crystalline Warrior",
                "Prayer Smasher",
                "Hard Hitter",
                "Nightmare (5-Scale) Speed-Trialist",
                "From One King to Another",
                "Reminisce",
                "Zulrah Veteran",
                "Snake Rebound",
                "Hazard Prevention",
                "Vet'eran",
                "Together We'll Fall",
                "Redemption Enthusiast",
                "Mutta-diet",
                "Dancing with Statues",
                "Cryo No More",
                "Blizzard Dodger",
                "Kill It with Fire",
                "Demonic Defence",
                "The Bane of Demons",
                "Phantom Muspah Speed-Runner",
                "Phantom Muspah Manipulator",
                "Can't Wake Up",
                "Inferno Speed-runner",
                "Inferno Grandmaster",
                "Chambers of Xeric Grandmaster",
                "Chambers of Xeric: CM (Trio) Speed-runner",
                "Chambers of Xeric: CM (5-scale) Speed-runner",
                "Vorkath Speed-Runner",
                "The Fremennik Way",
                "Faithless Encounter",
                "Theatre of Blood Grandmaster",
                "Grotesque Guardians Speed-Runner",
                "Quick Cutter",
                "Whack-a-Mole",
                "Avoiding Those Little Arms",
                "Shayzien Protector",
                "... 'til Dawn",
                "Ready to Pounce",
                "Inspect Repellent",
                "Walk Straight Pray True",
                "Demon Evasion",
                "Precise Positioning",
                "Space is Tight",
                "The Worst Ranged Weapon",
                "Wolf Puncher",
                "Wolf Puncher II"
        };
        //TODO add desc rewriting for some tasks based on the desc in the document
        final int[] tierEnums = {3981, 3982, 3983, 3984, 3985, 3986};
        for (int enumId : tierEnums) {
            EnumDefinitions enumDefs = EnumDefinitions.get(enumId);
            final HashMap<Integer, Object> values = new HashMap<>();
            final int highestKey = enumDefs.getValues().size() + 1;
            for (Map.Entry<Integer, Object> entry : enumDefs.getValues().entrySet()) {
                StructDefinitions struct = StructDefinitions.get((int) entry.getValue());
                final String name = struct.getParamAsString(1308);
                values.put(entry.getKey(), entry.getValue());
                for (String task : tasksToRemove) {
                    if (task.equalsIgnoreCase(name)) {
                        values.remove(entry.getKey());
                        System.out.printf("Removed task -> %s\n", name);
                        break;
                    }
                }
            }
            final HashMap<Integer, Object> filteredValues = new HashMap<>();
            int filterIndx = 0;
            for (int i = 0; i <= highestKey; i++) {
                if (values.containsKey(i)) {
                    filteredValues.put(filterIndx++, values.get(i));
                }
            }
            enumDefs.setValues(filteredValues);
            definitions.add(enumDefs);
        }
    }

    public static List<Definitions> getDefinitions() {
        return definitions;
    }

    private static final IntArrayList regionsChanged = new IntArrayList();

    public static void regionChanged(int i) {
        regionsChanged.add(i);
    }
}