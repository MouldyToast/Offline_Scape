package mgi.tools.parser;

import com.esotericsoftware.kryo.Kryo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.near_reality.cache.interfaces.teleports.packing.TeleportsPacker;
import com.near_reality.cache_tool.packing.custom.GenericDataPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityBarrowsItemDefinitions;
import com.near_reality.cache_tool.packing.custom.NearRealityBoneCrusherItemDefinitions;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomAnimationsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomEnumsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomGraphicsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomHeadIconsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomItemPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomMapsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomObjectsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomSpecialAttacksPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomStructsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityCustomWorldMapPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityEaster2024Packer;
import com.near_reality.cache_tool.packing.custom.NearRealityEffigyMapEdits;
import com.near_reality.cache_tool.packing.custom.NearRealityOriginsPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityOriginsWhipPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityRaidsItemDefinitions;
import com.near_reality.cache_tool.packing.custom.NearRealityRebirthPacker;
import com.near_reality.cache_tool.packing.custom.NearRealityReducePricesItemDefinitions;
import com.near_reality.cache_tool.packing.custom.NearRealityRemovePetsFromBossCLs;
import com.near_reality.cache_tool.packing.custom.NearRealityStoreInterfacePacker;
import com.near_reality.cache_tool.packing.custom.PackRevivalHeadIconSprites;
import com.near_reality.cache_tool.packing.custom.UniversalShopPacker;
import com.near_reality.cache_tool.packing.custom.ganodermic_beasts.GanodermicBeastsPacker;
import com.near_reality.cache_tool.packing.custom.inquisitors_great_flail.InquisitorsGreatFlailPacker;
import com.near_reality.cache_tool.packing.custom.mack.ClownStoreInterfacePacker;
import com.near_reality.cache_tool.packing.custom.mack.ClownVoteInterfacePacker;
import static com.near_reality.game.item.CustomObjectId.*;
import com.near_reality.util.gson.Int2ObjectMapDeserializer;
import com.near_reality.util.gson.IntListTypeAdapter;
import com.near_reality.util.gson.Object2IntMapDeserializer;
import com.near_reality.util.gson.ObjectCollectionDeserializer;
import com.zenyte.CacheManager;
import com.zenyte.ContentConstants;
import com.zenyte.game.content.achievementdiary.DiaryArea;
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.content.achievementdiary.DiaryInfo;
import com.zenyte.game.ui.testinterfaces.BountyHunterRewardType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.MapUtils;
import com.zenyte.game.world.region.Regions;
import com.zenyte.game.world.region.XTEALoader;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.awt.image.BufferedImage;
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
import javax.imageio.ImageIO;
import kotlin.text.Charsets;
import mgi.custom.AnimationBase;
import mgi.custom.BountyHunter;
import mgi.custom.CustomTeleport;
import mgi.custom.DiceBag;
import mgi.custom.FramePacker;
import mgi.custom.HighDefinitionPets;
import mgi.custom.Korasi;
import mgi.custom.relaunch.PerkPacker;
import mgi.custom.relaunch.RemnantExchangeValuePacker;
import mgi.custom.relaunch.SoloChallengePacker;
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
import mgi.types.draw.sprite.SpriteGroupDefinitions;
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
import org.jire.wmpacker.CustomWorldMapAreas;
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
    public static final boolean POST_PACK_UNIV_SHOP = true;

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

    public static boolean RUNESPAWN = false;

    public static void main(final String[] args) throws Exception {
        final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        String targetCacheDirectory = "data/cache";

        String type = "";
        boolean isProductionCacheGen = false;
        if (args.length > 0) {
            type = args[0];
        }
        if (args.length > 1) {
            RUNESPAWN = Boolean.parseBoolean(args[1]);
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

//        try {
//            Cache runespawn_cache = Cache.openCache("data/cache-runespawn");
//            RuneSpawnMigration runespawn = new RuneSpawnMigration(cache, runespawn_cache);
//            runespawn.run();
//        } catch (Exception e) {
//            e.printStackTrace(System.err);
//        }


        CacheManager.loadDefinitions(service, true);
        //Definitions.loadDefinitions(Definitions.cacheLowPriorityDefinitions);

        initializeKryo();
        parse(new File("assets/types"));
        if (RUNESPAWN) {
            parse(new File("assets/runespawn/types"));
        }
        pack(NPCDefinitions.class);
        packDynamicConfigs();
        packHighRevision();
        NearRealityCustomAnimationsPacker.pack();
        NearRealityOriginsPacker.pack();
        NearRealityOriginsWhipPacker.pack();
        NearRealityRebirthPacker.pack();
        NearRealityCustomGraphicsPacker.pack();
        NearRealityCustomStructsPacker.pack();

        NearRealityCustomItemPacker.pack();
        removeCATasks();
        pack(
                ArrayUtils.addAll(
                        Definitions.highPriorityDefinitions,
                        Definitions.cacheLowPriorityDefinitions)
        );
        packClientBackground();
        packSprites();
        packClientScripts();
        packInvs();
        packInterfaces();
        packStructs();
        packParams();
        NearRealityCustomWorldMapPacker.pack();
        packMaps(service);

        editObjects();
        addCustomStalls();
        TeleportsPacker.pack();
        increaseVarclientAmount();
        wildernessVault();
        NearRealityCustomObjectsPacker.pack();
        if (ENABLED_MAP_PACKING) {
            NearRealityCustomMapsPacker.pack();
        } else {
            System.out.println("Skipping NearRealityCustomMapsPacker.pack();");
        }
        duelArena(cache);
        new BountyHunter().packAll();
        new PerkPacker().packAll();
        new RemnantExchangeValuePacker().packAll();
        new SoloChallengePacker().packAll();
        if (!isProductionCacheGen) {
            //packPreservedCS2(cache);
        } else {
            // packVCSAssets(cache);
        }
        //packVCSAssets(cache);

        new ClownStoreInterfacePacker(cache).pack();
        new ClownVoteInterfacePacker(cache).pack();
        new PackRevivalHeadIconSprites(cache).patch();
        GenericDataPacker.INSTANCE.packAll(cache, "assets/packed/");
        copyMaps();
        NearRealityCustomEnumsPacker.pack();
        NearRealityCustomSpecialAttacksPacker.pack();
        NearRealityBarrowsItemDefinitions.removeCheckOption();
        NearRealityBoneCrusherItemDefinitions.removeChargingOptions();
        NearRealityRaidsItemDefinitions.makeKindlingStackable();
        NearRealityRaidsItemDefinitions.makeCavernGrubsStackable();
        NearRealityReducePricesItemDefinitions.reducePrices();
        NearRealityEaster2024Packer.pack();
        NearRealityCustomHeadIconsPacker.pack();
        NearRealityRemovePetsFromBossCLs.pack();
        NearRealityStoreInterfacePacker.pack();
        cache.close();

        cache = Cache.openCache("data/cache");
        CacheManager.loadCache(cache);
        CacheManager.loadDefinitions(service, true);
        postPackEdits();
        if (POST_PACK_UNIV_SHOP)
            UniversalShopPacker.INSTANCE.postPack();
        cache.close();

        /*
         * cache = Cache.openCache(targetCacheDirectory);
         * PackFromConsecutively.packAll(cache); // Edenify
         * cache.close();
         */

        log.info("Cache repack took {} milliseconds", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));

        service.shutdown();
    }

    private static void postPackEdits() {
        ComponentDefinitions def = ComponentDefinitions.get(1722, 3);
        def.x = 215;
        def.y = 30;
        def.pack();

        def = ComponentDefinitions.get(1722, 21);
        def.x = 215;
        def.y = 30;
        def.pack();
    }

    private static void portMaps(Cache backport) {
        copyMapRegionFromTargetCache(backport, 13139, 13139);
        copyMapRegionFromTargetCache(backport, 13395, 13395);
        //Triggers repack with new objects
        //copyMapRegionFromTargetCache(CacheManager.getCache(), 12132, 12132);
    }

    private static void copyMaps() {
        log.info("Begin map copy process...");
        log.info("    Beginning Catacombs of Kourend...");
        //copyMapRegion(6302, 6306, 6310);
        copyMapRegion(6301, 6306, 6310, 6314);
        copyMapRegion(6300, 6305, 6309, 6313);
        copyMapRegion(6299, 6304, 6308, 6312);
        //copyMapRegion(6558, 6562, 6566);
        copyMapRegion(6557, 6562, 6566, 6570);
        copyMapRegion(6556, 6561, 6565, 6569);
        copyMapRegion(6555, 6560, 6564, 6568);
        //copyMapRegion(6814, 6818, 6822);
        copyMapRegion(6813, 6818, 6822, 6826);
        copyMapRegion(6812, 6817, 6821, 6825);
        copyMapRegion(6811, 6816, 6820, 6824);
        //copyMapRegion(7070, 7074, 7078);
        copyMapRegion(7069, 7074, 7078, 7082);
        copyMapRegion(7068, 7073, 7077, 7081);
        copyMapRegion(7067, 7072, 7076, 7080);
        log.info("   Finish Catacombs of Kourend...");

        log.info("   Beginning Evil Bob / Train Island...");
        copyMapRegion(10058, 9546);
        log.info("   Finish Evil Bob / Train Island...");

        log.info("   Beginning RDI...");
        copyMapRegion(11605, 11604);
        log.info("   Finish RDI...");
        log.info("End map copy process.");
    }

    private static void packVCSAssets(Cache cache) {
        processExportedCS2s(cache);

    }

    private static void processExportedCS2s(Cache cache) {
        Archive cs2s = cache.getArchive(ArchiveType.CLIENTSCRIPTS);
        for (final File file : Objects.requireNonNull(Paths.get("assets/exports/cs2/").toFile().listFiles())) {
            try {
                final String name = file.getName().replace(".cs2", "");
                final int groupId = Integer.parseInt(name);
                log.info("Packing exported CS2 #{} into prod cache", groupId);
                cs2s.addGroup(new Group(groupId, new mgi.tools.jagcached.cache.File(new ByteBuffer(IOUtils.toByteArray(new FileInputStream(file))))));
            } catch (Exception e) {
                log.error("Could not pack sprite '{}'", file);
                e.printStackTrace(System.err);
            }
        }
    }

//    private static void handleSavor() throws Exception {
//        Cache cache = Cache.openCache("data/cache-staging");
//        /* Perks Purchase */
//        preserveCS2s(cache, 12575, 12576, 12577, 12578, 12579, 12580, 12581, 12582, 12583, 12584, 12585, 12586);
//        /* Remnant Exchange */
//        preserveCS2s(cache, 34000, 34001, 34002, 34003, 34004, 34005, 34006, 34007, 34008, 34009, 34010);
//        /* Next-Gen Store */
//        preserveCS2s(cache, 12538, 12539, 12540, 12541, 12542, 12543, 12544, 12545, 12546, 12547, 12548, 12549, 12550,
//                12551, 12552, 12553, 12554, 12555, 12556, 12557, 12558, 12559, 12560, 12561, 12562, 12563, 12564, 12565, 12566,
//                12567, 12568, 12569, 12570, 12571, 12572);
//        preserveCS2s(cache, /*3178,*/ 34020, 34021, 34030, 34031, 34032, 34033, 34034, 34035, 34036, 34037, 34038);
//        /* Duel Arena */
//        preserveCS2s(cache, 205, 10590, 10675, 10676, 10677, 10682, 10693, 10694);
//        preserveCS2s(cache, 45998, 45999, 243);
//        /* Rune Pouch */
//       // preserveCS2s(cache, 1456);
//        cache.close();
//    }

//    static LinkedHashMap<Integer, Group> PRESERVED_CS2S = new LinkedHashMap<>();
//
//    private static void preserveCS2s(Cache cache, int... ids) throws IOException {
//        Archive cs2s = cache.getArchive(ArchiveType.CLIENTSCRIPTS);
//        for (int id : ids) {
//            Group preserved1 = cs2s.findGroupByID(id);
//            PRESERVED_CS2S.put(id, preserved1.copy());
//
//            mgi.tools.jagcached.cache.File preserved1File = preserved1.getFiles()[0];
//            DataOutputStream fw = new DataOutputStream(new FileOutputStream("assets/exports/cs2/" + id + ".cs2"));
//            fw.write(preserved1File.getData().getBuffer());
//            fw.close();
//        }
//    }

//    private static void packPreservedCS2(Cache cache) {
//        Archive cs2s = cache.getArchive(ArchiveType.CLIENTSCRIPTS);
//        for (Map.Entry<Integer, Group> entry : PRESERVED_CS2S.entrySet()) {
//            log.info("Packing preserved CS2: {}", entry.getKey());
//            cs2s.addGroup(entry.getValue());
//        }
//    }


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

    private static void duelArena(final Cache cache) throws IOException {
        if (!ENABLED_MAP_PACKING) {
            return;
        }

        packDuelArenaMap(cache, 13362,
                "assets/map/old_duel_arena/duel_arena_land_13362.dat",
                "assets/map/old_duel_arena/duel_arena_map_13362.dat"
        );
        packDuelArenaMap(cache, 13363,
                "assets/map/old_duel_arena/duel_arena_land_13363.dat",
                "assets/map/old_duel_arena/duel_arena_map_13363.dat"
        );
    }

    private static void packDuelArenaMap(
            final Cache cache,
            final int regionId,
            final String landscapeFilePath, final String mapFilePath) throws IOException {
        if (!ENABLED_MAP_PACKING) {
            return;
        }

        final int regionX = regionId >> 8;
        final int regionY = regionId & 255;

        final int[] xteas = XTEALoader.getXTEAKeys(regionId);
        final Archive archiveTo = cache.getArchive(ArchiveType.MAPS);
        final Group mapGroupTo = archiveTo.findGroupByName("m" + regionX + "_" + regionY);
        final Group landGroupTo = archiveTo.findGroupByName("l" + regionX + "_" + regionY, xteas);

        final byte[] outputLandData = java.nio.file.Files.readAllBytes(Path.of(landscapeFilePath));

        if (landGroupTo != null) {
            landGroupTo.findFileByID(0).setData(new ByteBuffer(outputLandData));
        } else {
            final Group newLandGroup = new Group(archiveTo.getFreeGroupID(),
                    new mgi.tools.jagcached.cache.File(new ByteBuffer(outputLandData)));
            newLandGroup.setName("l" + regionX + "_" + regionY);
            archiveTo.addGroup(newLandGroup);
        }

        final byte[] outputMapData = java.nio.file.Files.readAllBytes(Path.of(mapFilePath));

        if (mapGroupTo != null) {
            mapGroupTo.findFileByID(0).setData(new ByteBuffer(outputMapData));
        } else {
            final Group newMapGroup = new Group(archiveTo.getFreeGroupID() + 1,
                    new mgi.tools.jagcached.cache.File(new ByteBuffer(outputMapData)));
            newMapGroup.setName("m" + regionX + "_" + regionY);
            archiveTo.addGroup(newMapGroup);
        }
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

    private static void packClientBackground() throws IOException {
        final byte[] desktop = java.nio.file.Files.readAllBytes(Paths.get("assets/sprites/background" +
                "/background_desktop.png"));
        final Cache cache = CacheManager.getCache();
        final Archive desktopArchive = cache.getArchive(ArchiveType.BINARY);
        desktopArchive.findGroupByID(0).findFileByID(0).setData(new ByteBuffer(desktop));

        final Archive spritesArchive = cache.getArchive(ArchiveType.SPRITES);
        Group logoGroup = spritesArchive.findGroupByName("logo");

        final BufferedImage image = ImageIO.read(Paths.get("assets/sprites/background/background_logo" +
                ".png").toFile());
        final SpriteGroupDefinitions sprite = new SpriteGroupDefinitions(logoGroup.getID(), image.getWidth(),
                image.getHeight());
        sprite.setWidth(image.getWidth());
        sprite.setHeight(image.getHeight());
        sprite.setImage(0, image);
        sprite.pack();
    }

    private static void packSprites() throws IOException {
        for (final File file : Objects
                .requireNonNull(Paths.get("assets/sprites/spellbook_teleport").toFile().listFiles())) {
            try {
                final String[] split = file.getName().replace(".png", "").split("_");
                final int groupId = Integer.parseInt(split[0]);
                final int spriteId = Integer.parseInt(split[1]);
                final BufferedImage image = ImageIO.read(file);
                final SpriteGroupDefinitions sprite = new SpriteGroupDefinitions(groupId, image.getWidth(),
                        image.getHeight());
                sprite.setWidth(image.getWidth());
                sprite.setHeight(image.getHeight());
                sprite.setImage(spriteId, image);
                sprite.pack();
            } catch (Exception e) {
                System.err.println("Could not pack sprite '" + file + "'");
                e.printStackTrace(System.err);
            }
        }
    }

    private static void packHighRevision() throws IOException {
        // new DiceBagPacker().pack();
        // new TrickPacker().pack();
        //new ThanksgivingPacker().pack();
        //new ChristmasMapPacker().pack();
        new HighDefinitionPets().packFull();
        //new EasterMapPacker().packAll();
        new CustomTeleport().packAll();
        //new TrickEmote().packAll();
        new DiceBag().packAll();
        new Korasi().pack();
        InquisitorsGreatFlailPacker.pack();
        //new MusicEnumPacker().pack();
        FramePacker.write();
        AnimationBase.pack();
        // new HalloweenMapPacker().pack();
        GanodermicBeastsPacker.pack();
    }

    private static void packDynamicConfigs() {
        //for (Int2IntMap.Entry entry : EnumDefinitions.getIntEnum(1002).getValues().int2IntEntrySet()) {
        //    System.out.println(entry.getIntKey()+"="+entry.getIntValue());
        //}
        EnumDefinitions enumDef;
        enumDef = new EnumDefinitions();
        enumDef.setId(1974);
        enumDef.setKeyType("int");
        enumDef.setValueType("namedobj");
        enumDef.setDefaultInt(-1);
        enumDef.setValues(new HashMap<>());
        int id = 0;
        for (final BountyHunterRewardType reward : BountyHunterRewardType.values()) {
            enumDef.getValues().put(id++, reward.getId());
        }
        definitions.add(enumDef);
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

    public static void addCustomStalls() {
        var coinsStall = cloneObject(11731, AFK_STALL_COINS);
        coinsStall.setName("Coins stall (Master)");
        coinsStall.pack();
        var fruitStall = cloneObject(11730, AFK_STALL_BAKER);
        fruitStall.setName("Baker's stall (Beginner)");
        fruitStall.pack();
        var fruitStall2 = cloneObject(11732, AFK_STALL_FUR);
        fruitStall2.setName("Fur stall (Easy)");
        fruitStall2.pack();
        var runesStall = cloneObject(11734, AFK_STALL_SILVER);
        runesStall.setName("Silver stall (Medium)");
        runesStall.pack();
        var scimmyStall = cloneObject(11731, AFK_STALL_MAGIC);
        scimmyStall.setModels(new int[]{60483});
        scimmyStall.setName("Magic stall (Hard)");
        scimmyStall.pack();

        var tutorialIslandPortal = cloneObject(4406, 55070);
        tutorialIslandPortal.pack();
        var revEntrance = cloneObject(31555, REVENANT_ENTRANCE_1);
        revEntrance.pack();
        var revEntrance2 = cloneObject(31555, REVENANT_ENTRANCE_2);
        revEntrance2.pack();
//        var donorObelisk = cloneObject(14827, DONATOR_OBELISK);
//        donorObelisk.setOptions(new String[]{"Wilderness-Vault", "Ganodermic-Beast", null, null, null});
//        donorObelisk.pack();
//        var donorObelisk2 = cloneObject(14825, DONATOR_OBELISK_NULL);
//        donorObelisk2.setOptions(new String[]{null, null, null, null, null});
//        donorObelisk2.pack();
    }

    public static void editObjects() {
        var ornate = cloneObject(29241, REJUVENATION_ORNATE);
        ornate.setOptions(new String[]{"Drink", null, null, null, null});
        ornate.pack();

        var overload = cloneObject(29241, REJUVENATION_OVERLOAD);
        overload.setOptions(new String[]{"Drink", "Rejuvenate", null, null, null});
        overload.setName("Overload pool of Rejuvenation");
        overload.setModels(new int[]{60206});
        overload.pack();

        var divine = cloneObject(29241, REJUVENATION_DIVINE);
        divine.setOptions(new String[]{"Drink", "Rejuvenate", null, null, null});
        divine.setName("Divine pool of Rejuvenation");
        divine.setModels(new int[]{60205});
        divine.pack();

        var fancy = cloneObject(29240, REJUVENATION_FANCY);
        fancy.setOptions(new String[]{"Drink", null, null, null, null});
        fancy.pack();
        var poolHome = cloneObject(29241, REJUVINATION_POOL);
        poolHome.setOptions(new String[]{"Drink", "Remove-skull", "Skull", null, null});
        poolHome.pack();

        var nexus = cloneObject(41415, 50082);
        nexus.setName("Teleportation nexus");
        nexus.setOptions(new String[]{"Teleportation-menu", "Previous-teleport", null, null, null});
        nexus.pack();
        var barrier = cloneObject(4469, AFK_ENTRY_OBJECT);
        barrier.setName("AFK Skilling Area");
        barrier.setOptions(new String[]{"Pass", null, null, null, null});
        barrier.pack();
        var flax = cloneObject(14896, AFK_FLAX);
        flax.setOptions(new String[]{"Afk", null, null, null, null});
        flax.pack();
        var r_stall = cloneObject(4877, AFK_THIEVING);
        r_stall.setOptions(new String[]{"Afk", null, null, null, null});
        r_stall.pack();

        var fishing = cloneObject(42, AFK_FISHING);
        fishing.setOptions(new String[]{"Afk", null, null, null, null});
        fishing.pack();


        var score = cloneObject(884, AFK_SCOREBOARD);
        score.setModels(new int[]{60197});
        score.setSizeX(7);
        score.setSizeY(2);
        score.setModelSizeX(128);
        score.setModelSizeY(128);
        score.setName("Scoreboard");
        score.setOptions(new String[]{"View", null, null, null, null});
        score.pack();
    }

    public static ObjectDefinitions cloneObject(int from, int to) {
        ObjectDefinitions def = new ObjectDefinitions(to, new ByteBuffer(new byte[1]));
        def.copy(from);
        return def;
    }

    public static void packModel(final int id, final byte[] bytes) {
        log.info("Packing model: {}", id);
        CacheManager.getCache().getArchive(ArchiveType.MODELS).addGroup(new Group(id,
                new mgi.tools.jagcached.cache.File(new ByteBuffer(bytes))));
    }

    public static void packSound(final int id, final byte[] bytes) {
        CacheManager.getCache().getArchive(ArchiveType.SYNTHS).addGroup(new Group(id,
                new mgi.tools.jagcached.cache.File(new ByteBuffer(bytes))));
    }

    public static void packInvs() throws IOException {
        for (File file : Paths.get("assets/inv").toFile().listFiles()) {
            if (!file.getName().contains(".")) {
                try {
                    final int id = Integer.parseInt(file.getName());
                    packInv(id, java.nio.file.Files.readAllBytes(file.toPath()));
                } catch (Exception e) {
                    System.err.println("Failed to pack enum file " + file + " name must be an int!");
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public static void packStructs() throws IOException {
        for (File file : Paths.get("assets/structs").toFile().listFiles()) {
            if (!file.getName().contains(".")) {
                try {
                    final int id = Integer.parseInt(file.getName());
                    packStruct(id, java.nio.file.Files.readAllBytes(file.toPath()));
                } catch (Exception e) {
                    System.err.println("Failed to pack struct file " + file + " name must be an int!");
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public static void packStruct(final int id, final byte[] bytes) {
        CacheManager.getCache().getArchive(ArchiveType.CONFIGS)
                .findGroupByID(GroupType.STRUCT)
                .addFile(new mgi.tools.jagcached.cache.File(id, new ByteBuffer(bytes)));
    }

    public static void packParams() throws IOException {
        for (File file : Paths.get("assets/params").toFile().listFiles()) {
            if (!file.getName().contains(".")) {
                try {
                    final int id = Integer.parseInt(file.getName());
                    packParam(id, java.nio.file.Files.readAllBytes(file.toPath()));
                } catch (Exception e) {
                    System.err.println("Failed to pack param file " + file + " name must be an int!");
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public static void packParam(final int id, final byte[] bytes) {
        CacheManager.getCache().getArchive(ArchiveType.CONFIGS)
                .findGroupByID(GroupType.PARAMS)
                .addFile(new mgi.tools.jagcached.cache.File(id, new ByteBuffer(bytes)));
    }

    private static void packClientScripts() throws Exception {
        // packClientScript(73,
        // java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/bank_command/73.cs2")));
        // packClientScript(386,
        // java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/tourny_fog/386.cs2")));
//        var files = Paths.get("assets/cs2/old_jagex/").toFile().listFiles();
//        for (var file : files) {
//            var id = Integer.parseInt(file.getName().replaceAll(".cs2", ""));
//            packClientScript(id, java.nio.file.Files.readAllBytes(file.toPath()));
//        }
        // packCs2FromDirectory("assets/cs2/perks/");

//        packClientScript(393,
//               java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/skill_tab_construction/393.cs2")));
    /*packClientScript(395,
        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/skill_tab_construction/395.cs2")));*/
//        packClientScript(687,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/ironman_setup/687.cs2")));
        //packClientScript(1004,
        //        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/experience_drops_multiplier" +
        //                ".cs2")));
//        packClientScript(1261,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/tourny_fog/1261.cs2")));
//        packClientScript(1705,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/edgeville_map_link/1705.cs2")));
        //packClientScript(2066,
        //        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/broadcast_custom_links/2066" +
        //                ".cs2")));
//        packClientScript(2094,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/ironman_setup/2094.cs2")));
//        packClientScript(2096,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/ironman_setup/2096.cs2")));
//        packClientScript(2186,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/tourny_viewer/2186.cs2")));
        //packClientScript(2200,
        //        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/achievement_diary_sizes/2200" +
        //                ".cs2")));
        //packClientScript(699,
        //        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/emote_tab/699.cs2")));
        //packClientScript(701,
        //        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/emote_tab/701.cs2")));
        //packClientScript(702,
        //        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/emote_tab/702.cs2")));
        /*
         * for (int id = 3500; id <= 3505; id++) {
         * packClientScript(id,
         * java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/game_noticeboard/" + id +
         * ".cs2")));
         * }
         */
//        packClientScript(10100,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/ironman_setup/10100.cs2")));
//        for (int i = 10034; i <= 10048; i++) {
//            packClientScript(i,
//                    java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/wheel_of_fortune/" + i + ".cs2")));
//        }
//
//        for (int id = 10200; id <= 10202; id++) {
//            packClientScript(id,
//                    java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/game_settings/" + id + ".cs2")));
//        }
//
//        for (int id = 10400; id <= 10405; id++) {
//            packClientScript(id,
//                    java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/tourny_info/" + id + ".cs2")));
//        }
//        packClientScript(10600,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/tourny_viewer/10600.cs2")));
//        packClientScript(10700,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/hide_roofs/10700.cs2")));

//        packClientScript(336,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/godwars_dungeon/336.cs2")));
//        packClientScript(342,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/godwars_dungeon/342.cs2")));
//        for (int i = 10900; i <= 10912; i++) {
//            packClientScript(i,
//                    java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/eco_presets/" + i + ".cs2")));
//        }

//
//        packClientScript(1311,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/tog_sidepanel_timer.cs2")));

//
//        packClientScript(1816,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1816.cs2")));
//        packClientScript(1818,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1818.cs2")));
//        packClientScript(1819,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1819.cs2")));
//        packClientScript(1820,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1820.cs2")));
//        packClientScript(1821,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1821.cs2")));
//        packClientScript(1822,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1822.cs2")));
//        packClientScript(1823,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1823.cs2")));
//        packClientScript(1824,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1824.cs2")));
//        packClientScript(1825,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1825.cs2")));
//        packClientScript(1826,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/vardovis_ui/script_1826.cs2")));

        /*for (File file : Objects
                .requireNonNull(Paths.get("assets/cs2/duel_staking/").toFile().listFiles())) {
            if (file.getName().endsWith(".cs2")) {
                try {
                    final int id = Integer.parseInt(file.getName().replace(".cs2", ""));
                    packClientScript(id, java.nio.file.Files.readAllBytes(file.toPath()));
                } catch (Exception e) {
                    System.err.println("File name of " + file + " must be an integer!");
                    e.printStackTrace(System.err);

                }
            }
        }*/
        //packClientScript(223, java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/tam/223.cs2")));


        // Gauntlet edenified cs2s
/*    packClientScript(13827,
        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/gauntlet/13827.cs2")));
    packClientScript(13828,
        java.nio.file.Files.readAllBytes(Paths.get("assets/cs2/gauntlet/13828.cs2")));*/

//        packRustyScripts("assets/scripts/out/");

        // NearRealityCustomCS2Packer.pack();
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

    public static void packInv(final int id, final byte[] bytes) {
        CacheManager.getCache().getArchive(ArchiveType.CONFIGS)
                .findGroupByID(GroupType.INV)
                .addFile(new mgi.tools.jagcached.cache.File(id, new ByteBuffer(bytes)));
    }

    private static void packInterfaces() {
        final Cache cache = CacheManager.getCache();
        packInterfacesInner(cache, Paths.get("assets/interfaces").toFile().listFiles());
        if (RUNESPAWN) {
            packInterfacesInner(cache, Paths.get("assets/runespawn/interfaces").toFile().listFiles());
        }
        //packMackInterfaces(cache, 5006);

        cache.getArchive(ArchiveType.INTERFACES).finish();

    }

    private static void packInterfacesInner(final Cache cache, final File[] folders) {
        for (File interfaceFolder : folders) {
            if (interfaceFolder.isDirectory() && interfaceFolder.getName().contains("compressed"))
                continue;
            if (!interfaceFolder.isDirectory()) {
                continue;
            }

            final int groupId = Integer.parseInt(interfaceFolder.getName());
            System.out.println("Creating interface raw [" + groupId + "]");
            final Group group = new Group(groupId);
            Arrays.stream(Objects.requireNonNull(interfaceFolder.listFiles()))
                    .mapToInt(file -> Integer.parseInt(file.getName())).sorted().forEach(id -> {
                        // System.out.println("\t adding["+id+"]");
                        final Path path = interfaceFolder.toPath().resolve(Integer.toString(id));
                        try {
                            group.addFile(new mgi.tools.jagcached.cache.File(
                                    new ByteBuffer(java.nio.file.Files.readAllBytes(path))));
                        } catch (IOException e) {
                            e.printStackTrace(System.err);
                        }
                    });
            cache.getArchive(ArchiveType.INTERFACES).addGroup(group);
        }
    }

    public static void packInterface(final File interfaceFile, final int groupId) {
        if (!interfaceFile.isDirectory()) {
            return;
        }

        final Cache cache = CacheManager.getCache();
        log.debug("Creating interface {} from file {}", groupId, interfaceFile);
        final Group group = new Group(groupId);
        Arrays.stream(Objects.requireNonNull(interfaceFile.listFiles()))
                .mapToInt(file -> Integer.parseInt(file.getName())).sorted().forEach(id -> {
                    // System.out.println("\t adding["+id+"]");
                    final Path path = interfaceFile.toPath().resolve(Integer.toString(id));
                    try {
                        group.addFile(new mgi.tools.jagcached.cache.File(
                                new ByteBuffer(java.nio.file.Files.readAllBytes(path))));
                    } catch(IOException e) {
                        e.printStackTrace(System.err);
                    }
                });
        cache.getArchive(ArchiveType.INTERFACES).addGroup(group);
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

    public static void packMapsRSPSiModern(int baseRegionID, String packFilePath) throws IOException {
        packMapsRSPSiModern(CacheManager.getCache(), baseRegionID, packFilePath);
    }

    public static void packMapsRSPSiModern(Cache cache, int baseRegionID, String packFilePath)
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
            packMap(cache, regionID, mapBlock, locsBlock);
        }
    }

    private static byte[] modifyRegions(int regionID, byte[] locsBlock) {
        if (regionID == 12342) {
            return Regions.inject(locsBlock,
                    o -> o.getId() == 76 || o.getId() == 29165 || o.getId() == 40448 || o.getId() == 2133 || o.getId() == 7439 || o.getId() == 6267 || o.getId() == 41705,
                    //FIX GE booths to have bank near banks
                    new WorldObject(10060, 0, 1, new Location(3094, 3490, 1)),
                    new WorldObject(10060, 0, 1, new Location(3095, 3490, 1)),
                    new WorldObject(10060, 0, 3, new Location(3094, 3495, 1)),
                    new WorldObject(10060, 0, 3, new Location(3095, 3495, 1)),
                    //Fix pottery wheel
                    new WorldObject(4310, 10, 1, new Location(3104, 3497, 0)),
                    new WorldObject(2031, 10, 3, new Location(3108, 3494, 0)),
                    //Xmas stuff
                    //new WorldObject(ChristmasConstants.CHRISTMAS_CUPBOARD_ID, 10, 2, ChristmasConstants.homeChristmasCupboardLocation),
                    //new WorldObject(46077, 10, 0, new Location(3087, 3499, 0))
                    //Slayer
                    new WorldObject(50103, 10, 3, new Location(3073, 3471, 0)),

                    //Combat Dummy Bank
                    new WorldObject(2693, 10, 1, new Location(3072, 3483)),

                    //Stalls
                    new WorldObject(50055, 10, 1, new Location(3094, 3482, 0)),
                    new WorldObject(50056, 10, 1, new Location(3094, 3479, 0)),
                    new WorldObject(50057, 10, 1, new Location(3094, 3476, 0)),
                    new WorldObject(50058, 10, 1, new Location(3094, 3473, 0)),
                    new WorldObject(55000, 10, 3, new Location(3108, 3486, 0)),
                    //Armor repair stand
                    new WorldObject(6802, 10, 0, new Location(3077, 3512, 0))
            );
        }
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

    public static void packMapPre209(final int id, String landscapeFilePath, String mapFilePath)
            throws IOException {
        try {
            packMapRawPre209(CacheManager.getCache(), id,
                    java.nio.file.Files.readAllBytes(Paths.get(mapFilePath)),
                    java.nio.file.Files.readAllBytes(Paths.get(landscapeFilePath)));
            System.err.println("Packed map[" + id + "] land = " + landscapeFilePath + ", map = " + mapFilePath);
        } catch (Exception e) {
            System.err.println("Failed to pack map[" + id + "] land = " + landscapeFilePath + ", map = " + mapFilePath);
            e.printStackTrace(System.err);
        }
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
        packMapPre209(9517, java.nio.file.Files.readAllBytes(Paths.get("assets/map/island_l_regular.dat")),
                Regions.inject(
                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/island_m_regular.dat")), o -> {
                            if (o.getId() == 46087) {
                                o.setId(46089);
                            }
                            return false;
                        }));
        packMapPre209(10388, java.nio.file.Files.readAllBytes(Paths.get("assets/map/yanille/328.dat")),
                java.nio.file.Files.readAllBytes(Paths.get("assets/map/yanille/329.dat")));
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
        packMapPre209(14477, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m56_141.dat")),
                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l56_141.dat")));
        packMapPre209(14478, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m56_142.dat")),
                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l56_142.dat")));
        packMapPre209(14733, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m57_141.dat")),
                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l57_141.dat")));
        packMapPre209(14734, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m57_142.dat")),
                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l57_142.dat")));
        packMapPre209(15245, java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/2.dat")),
                java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/3.dat")));
        packMapPre209(15248, java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/0.dat")),
                Regions.inject(java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/1.dat")),
                        null,
                        new WorldObject(35005, 10, 3, new Location(3806, 9245, 0)),
                        new WorldObject(35006, 10, 1, new Location(3813, 9256, 0)),
                        new WorldObject(35007, 10, 0, new Location(3799, 9256, 0))));
        packMapPre209(6582, java.nio.file.Files.readAllBytes(Paths.get("assets/map/primal_zone/primal_dungeon_l.dat")),
                java.nio.file.Files.readAllBytes(Paths.get("assets/map/primal_zone/primal_dungeon_m.dat")));
//        packMapPre209(4674,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Armadyl/0.dat")),
//                Regions.inject(
//                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Armadyl/1" +
//                                ".dat")),
//                        o -> {
//                            if (o.getId() == 20843) {
//                                o.setId(35016);
//                            } else if (o.getId() == 26769) {
//                                o.setId(35013);
//                            } else if (o.getId() == 23708) {
//                                o.setId(35019);
//                            }
//                            return false;
//                        }));
//        packMapPre209(4675,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Bandos/0.dat")),
//                Regions.inject(java.nio.file.Files
//                        .readAllBytes(Paths.get("assets/map/godwars-instances/Bandos/1.dat")), o -> {
//                    if (o.getId() == 9368) {
//                        o.setId(35014);
//                    } else if (o.getId() == 26769) {
//                        o.setId(35013);
//                    } else if (o.getId() == 23708) {
//                        o.setId(35019);
//                    }
//                    return o.hashInRegion() == new Location(1191, 4306, 0).hashInRegion();
//                }, new WorldObject(35019, 10, 0, new Location(1189, 4313, 0))));
//        packMapPre209(4676,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Zamorak/0.dat")),
//                Regions.inject(
//                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Zamorak/1" +
//                                ".dat")),
//                        o -> {
//                            if (o.getId() == 14845) {
//                                o.setId(35015);
//                            } else if (o.getId() == 26769) {
//                                o.setId(35013);
//                            } else if (o.getId() == 23708) {
//                                o.setId(35019);
//                            }
//                            // Removes object which produces ambient waterfall sound and the stash unit.
//                            return o.getId() == 16399 || o.getId() == 29054;
//                        }));
//        packMapPre209(4677,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Saradomin/0.dat")),
//                Regions.inject(
//                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Saradomin/1" +
//                                ".dat")),
//                        o -> {
//                            if (o.getId() == 26740) {
//                                o.setId(35017);
//                            } else if (o.getId() == 21120) {
//                                o.setId(35018);
//                            } else if (o.getId() == 23708) {
//                                o.setId(35019);
//                            }
//                            return o.getId() == 26375
//                                    || (o.getXInRegion() == (1203 & 63) && o.getYInRegion() == (4422 & 63));
//                        }, new WorldObject(17030, 22, 0, 1195, 4440, 0)));
//        packMapPre209(11346,
//                java.nio.file.Files
//                        .readAllBytes(Paths.get("assets/map/godwars-instances/Armadyl/1858.dat")),
//                Regions.inject(
//                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Armadyl/1859" +
//                                ".dat")),
//                        o -> {
//                            if (o.getId() == 20843) {
//                                o.setId(35016);
//                            } else if (o.getId() == 26769) {
//                                o.setId(35013);
//                            } else if (o.getId() == 23708) {
//                                o.setId(35019);
//                            }
//                            return false;
//                        }, new WorldObject(26502, 10, 3, 2839, 5295, 2),
//                        new WorldObject(0, 10, 0, 2840, 5294, 2),
//                        new WorldObject(0,
//                                10, 0, 2838, 5294, 2)));
//        packMapPre209(11347,
//                java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Bandos/1860.dat")),
//                Regions.inject(
//                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Bandos/1861" +
//                                ".dat")),
//                        o -> {
//                            if (o.getId() == 9368) {
//                                o.setId(35014);
//                            } else if (o.getId() == 26769) {
//                                o.setId(35013);
//                            } else if (o.getId() == 23708) {
//                                o.setId(35019);
//                            }
//                            return o.hashInRegion() == new Location(2856, 5357, 2).hashInRegion();
//                        }, new WorldObject(35019, 10, 0, new Location(2854, 5364, 2))));
//        packMapPre209(11602,
//                java.nio.file.Files
//                        .readAllBytes(Paths.get("assets/map/godwars-instances/Saradomin/1862.dat")),
//                Regions.inject(
//                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances/Saradomin" +
//                                "/1863.dat")),
//                        o -> {
//                            if (o.getId() == 26740) {
//                                o.setId(35017);
//                            } else if (o.getId() == 21120) {
//                                o.setId(35018);
//                            } else if (o.getId() == 23708) {
//                                o.setId(35019);
//                            }
//                            return false;
//                        }, new WorldObject(17030, 22, 0, 2923, 5272, 0)));
//        packMapPre209(11603,
//                MapUtils
//                        .processTiles(new ByteBuffer(java.nio.file.Files.readAllBytes(Paths.get("assets/map" +
//                                "/godwars-instances/Zamorak/1856.dat"))), tile -> {
//                            if (tile.getUnderlayId() == 23) {
//                                tile.setUnderlayId((byte) 0);
//                            }
//                            if (tile.getOverlayId() == 33) {
//                                tile.setOverlayId((byte) 0);
//                            }
//                        })
//                        .getBuffer(),
//                Regions.inject(java.nio.file.Files.readAllBytes(Paths.get("assets/map/godwars-instances" +
//                        "/Zamorak/1857.dat")), o -> {
//                    if (o.getId() == 14845) {
//                        o.setId(35015);
//                    } else if (o.getId() == 26769) {
//                        o.setId(35013);
//                    } else if (o.getId() == 23708) {
//                        o.setId(35019);
//                    }
//                    return false;
//                }));
//    packMapPre209(13420, "assets/map/gamble/gamble_0.dat", "assets/map/gamble/gameble_1.dat.dat");
        packMapPre209(13422, "assets/map/world_boss/worldboss_landscape.dat",
                "assets/map/world_boss/worldboss_objects.dat");
        packMapPre209(13424, "assets/map/tutorial_island/tutorial_landscape.dat",
                "assets/map/tutorial_island/tutorial_objects.dat");
        packMapPre209(13426, "assets/map/osnr_tournament/final_landscape.dat",
                "assets/map/osnr_tournament/final_objects.dat");
        packMapPre209(8314, "assets/map/staff_landscape.dat",
                "assets/map/staff_objects.dat");
        packMapPre209(13428, java.nio.file.Files.readAllBytes(Paths.get("assets/map/osnr_tournament/tourney_landscape.dat")),
                Regions.inject(java.nio.file.Files.readAllBytes(Paths.get("assets/map/osnr_tournament/tourney_objects.dat")), null,
                        new WorldObject(35006, 10, 1, new Location(3363, 7465, 0)),
                        new WorldObject(35007, 10, 0, new Location(3352, 7465, 0))));
//        packMapPre209(11601, null,
//                Regions.inject(11601, null, new WorldObject(50083, 10, 1, new Location(2906, 5206, 0))));
        //packMapPre209(12342, "assets/map/osnr_home/624.dat", "assets/map/osnr_home/625.dat");
        //packMapsRSPSi(13382, "assets/osnr/custom_maps/NR_home.pack");
//        packMapsRSPSi(13430, "assets/map/donator_zones/LDI.pack");
//        packMapsRSPSi(13433, "assets/map/donator_zones/UDI.pack");
//        packMapsRSPSi(13550, "assets/map/donator_zones/rev_dungeon.pack");
//        packMapsRSPSi(13552, "assets/map/donator_zones/rev_dungeon.pack");
//        packMapsRSPSi(11374, "assets/map/donator_zones/barrows.pack");
//        packMapsRSPSi(11375, "assets/map/donator_zones/barrows.pack");
//        packMapsRSPSi(11376, "assets/map/donator_zones/barrows.pack");
//        packMapsRSPSi(11377, "assets/map/donator_zones/barrows.pack");
//        packMapsRSPSi(11378, "assets/map/donator_zones/barrows.pack");
//        packMapsRSPSi(11379, "assets/map/donator_zones/barrows.pack");
//
//        packMapsRSPSi(13436, "assets/map/donator_zones/RDI.pack");
//        packMapsRSPSi(13439, "assets/map/donator_zones/DI.pack");
//        packMapsRSPSi(13441, "assets/map/donator_zones/DIE.pack");
        packMapsRSPSi(14388, "assets/map/Meiyerditch.pack");

//        packMapsRSPSi(13443, "assets/map/donator_zones/nr_dzone.pack");
        packMapsRSPSi(6457, "assets/map/kourend_castle.pack");
        packMapsRSPSi(10803, "assets/map/witchaven.pack");

//        packMapPre209(6441,
//                "assets/map/quad_dono_island/top_left_landscape.dat",
//                "assets/map/quad_dono_island/top_left_objects.dat");
//        packMapPre209(6697,
//                "assets/map/quad_dono_island/top_right_landscape.dat",
//                "assets/map/quad_dono_island/top_right_objects.dat");
//        packMapPre209(6440,
//                "assets/map/quad_dono_island/bottom_left_landscape.dat",
//                "assets/map/quad_dono_island/bottom_left_objects.dat");
//        packMapPre209(6696,
//                "assets/map/quad_dono_island/bottom_right_landscape.dat",
//                "assets/map/quad_dono_island/bottom_right_objects.dat");

        packMapPre209(6954,
                "assets/map/origin_update/desert_island/ls.dat",
                "assets/map/origin_update/desert_island/obj.dat");
        packMap(13625,
                "assets/map/origin_update/edge_island/island2_ls2.dat",
                "assets/map/origin_update/edge_island/island2_obj2.dat");
        packMap(13369, // edited with land bridge
                "assets/map/origin_update/edge_island/wildy2_ls2.dat",
                "assets/map/origin_update/edge_island/wildy2_obj2.dat");
        packMapPre209(7210,
                "assets/map/origin_update/ice_island/ls.dat",
                "assets/map/origin_update/ice_island/obj.dat");
        packMapPre209(7209,
                "assets/map/origin_update/jungle_island/ls.dat",
                "assets/map/origin_update/jungle_island/obj.dat");

        packMapPre209(7466,
                "assets/map/origin_update/barrelchest/ls.dat",
                "assets/map/origin_update/barrelchest/obj.dat");
//        packMapsRSPSi(6440, "assets/map/quad_dono_island/dZone.pack");

//        packMapPre209(6469,
//                "assets/map/quad_dono_island/chin_dungeon/chin_dung_landscape.dat",
//                "assets/map/quad_dono_island/chin_dungeon/chin_dung_objects.dat");


        if (RUNESPAWN) {
            packMapPre209(12342, "assets/runespawn/edgarrock_landscape.dat",
                    "assets/runespawn/edgarrock_objects.dat");
            packMapPre209(13382, "assets/runespawn/darkedge_landscape.dat",
                    "assets/runespawn/darkedge_objects.dat");
        } else {
            packMapPre209(12086, "assets/map/osnr_home/12086_landscape.dat",
                    "assets/map/osnr_home/12086_objects.dat");
            packMapPre209(12342, "assets/map/osnr_home/12342_landscape.dat",
                    "assets/map/osnr_home/12342_objects.dat");
            packMapPre209(12598, "assets/map/osnr_home/12598_landscape.dat",
                    "assets/map/osnr_home/12598_objects.dat");
            packMapPre209(12085, "assets/map/osnr_home/12085_landscape.dat",
                    "assets/map/osnr_home/12085_objects.dat");
            packMapPre209(12341, "assets/map/osnr_home/12341_landscape.dat",
                    "assets/map/osnr_home/12341_objects.dat");

            /*packMapPre209(12087, "assets/map/osnr_home/12087_landscape.dat",
                "assets/map/osnr_home/12087_objects.dat");
            packMapPre209(12343, "assets/map/osnr_home/12343_landscape.dat",
                "assets/map/osnr_home/12343_objects.dat");*/
        }
        packMapsRSPSi(12854, "assets/map/varrock_topr.pack");

//        CustomWorldMapAreas.changeGodwarsArea(service);
        CustomWorldMapAreas.changeMainArea(service);

        NearRealityEffigyMapEdits.apply();
    }

    public static void wildernessVault() throws IOException {
        packMapsRSPSi(7789, "assets/osnr/wilderness_vault/wilderness_vault.pack");
        packMapPre209(8557, "assets/osnr/wilderness_vault/vault_0.dat", "assets/osnr/wilderness_vault/vault_1.dat");
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