package org.jesse.game.world.entity.player.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.ToNumberPolicy;
import com.google.gson.*;
import org.jesse.game.world.entity.player.AreaManagerExtKt;
import org.jesse.game.world.entity.player.FakePlayer;
import org.jesse.game.world.entity.player.container.impl.SinglePlayerBank;
import org.jesse.util.gson.Int2ObjectMapDeserializer;
import org.jesse.util.gson.IntListTypeAdapter;
import org.jesse.util.gson.Object2IntMapDeserializer;
import org.jesse.util.gson.ObjectCollectionDeserializer;
import org.jesse.cores.CoresManager;
import org.jesse.game.GameConstants;
import org.jesse.game.content.skills.farming.Farming;
import org.jesse.game.content.skills.farming.FarmingSpot;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.PlayerInformation;
import org.jesse.game.world.entity.player.VarManager;
import org.jesse.game.world.entity.player.dailychallenge.ChallengeAdapter;
import org.jesse.game.world.entity.player.dailychallenge.challenge.DailyChallenge;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;
import org.jesse.logger.NearRealityPrintStream;
import org.jesse.plugins.PluginManager;
import org.jesse.plugins.events.InitializationEvent;
import org.jesse.plugins.events.PostInitializationEvent;
import org.jesse.utils.TimeUnit;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.utilities.StringFormatUtil;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author Kris | 25/02/2019 00:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LoginManager {

    public static final Logger log = LoggerFactory.getLogger(LoginManager.class);

    private enum Response {
        VOID,
        CANCELLED
    }

    /**
     * A map of cached loaded up players; the player is cached whenever it is loaded up externally outside of a login
     * . The entry is cleared if either more than 10 minutes have
     * passed since its caching, or the user logs in.
     */
    private static final Map<String, CachedEntry> cachedPlayers = new ConcurrentHashMap<>();

    /**
     * The player save directory that all serialized files are placed.
     */
    public static  Path PLAYER_SAVE_DIRECTORY = Path.of("data", "characters");

    /**
     * The file format extension we are serailizing information in.
     */
    static final String EXTENSION = ".json";

    /**
     * The gson object we initiate upon calling our game loader.
     */
    public static final ThreadLocal<Gson> gson = ThreadLocal.withInitial(() -> new GsonBuilder()
            .disableHtmlEscaping()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .registerTypeAdapter(FarmingSpot.class, Farming.deserializer())
            .registerTypeAdapter(VarManager.class, VarManager.deserializer())
            .registerTypeAdapter(DailyChallenge.class, new ChallengeAdapter())
            .registerTypeAdapter(IntList.class, IntListTypeAdapter.INSTANCE)
            .registerTypeAdapter(Object2IntMap.class, Object2IntMapDeserializer.INSTANCE)
            .registerTypeAdapter(Int2ObjectMap.class, Int2ObjectMapDeserializer.INSTANCE)
            .registerTypeAdapter(ObjectCollection.class, ObjectCollectionDeserializer.INSTANCE)
            .create());

    /**
     * The forkjoin pool that will be processing load and save requests of the characters.
     */
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * A concurrent queue used for passing on requests to load the user; thread safety reasons.
     */
    private final Queue<Function<Void, Response>> loadRequests = new ConcurrentLinkedQueue<>();

    /**
     * A concurrent queue used for passing on requests to save the user; thread safety reasons.
     */
    private final Queue<Runnable> saveRequests = new ConcurrentLinkedQueue<>();

    /**
     * A map for user saving requests, wherein key is the username of the account which will also be the name of the
     * file we save, thus eliminating any sort of overwriting or other sort of problems. Additionally ensures there
     * are no duplicate requests.
     */
    private final Map<String, Callable<Void>> saveRequestMap = new Object2ObjectOpenHashMap<>();

    /**
     * A map for user load requests, wherein key is the username of the account which will also be the name of the
     * file we load, thus eliminating any sort of problems with file loading. Additionally ensures there are no
     * duplicate requests.
     */
    private final Map<String, Callable<Void>> loadRequestsMap = new Object2ObjectOpenHashMap<>();

    /**
     * The current stage of the login system, starting off in the {@link ShutdownStage#RUNNING} stage.
     */
    @NotNull
    private ShutdownStage shutdownStage = ShutdownStage.RUNNING;

    public static final Object writeLock = new Object();

    /**
     * The default thread sleep frequency - if there are no pending requests, the thread will sleep for the defined
     * duration(milliseconds).
     */
    private static final int THREAD_SLEEP_FREQUENCY = 20;

    /**
     * The maximum number of requests the server may process per a single tick.
     */
    private static final int MAXIMUM_ALLOWED_REQUESTS_PER_SECOND =
            (int) TimeUnit.SECONDS.toMillis(1) / THREAD_SLEEP_FREQUENCY;

    /**
     * The remaining allowed logins count at this moment.
     */
    private int loads = MAXIMUM_ALLOWED_REQUESTS_PER_SECOND;

    /**
     * The thread that executes the loading and saving of the accounts.
     */
    private Thread thread;

    private final Set<Player> awaitingSave = new ObjectOpenHashSet<>();

    MutableBoolean status = new MutableBoolean();

    static {
        if (Files.notExists(PLAYER_SAVE_DIRECTORY)) {
            try {
                Files.createDirectories(PLAYER_SAVE_DIRECTORY);

                log.info("Characters folder created.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Launches the login processing.
     */
    public void launch() {
        thread = new Thread(new Runnable() {
            private long timer;

            @Override
            public void run() {
                log.info("Login Manager has been launched.");
                while (!Thread.interrupted()) {
                    try {
                        while (!Thread.interrupted() && loadRequests.isEmpty() && saveRequests.isEmpty()) {
                            try {
                                Thread.sleep(THREAD_SLEEP_FREQUENCY);
                            } catch (InterruptedException ie) {
                                log.error("", ie);
                                break;
                            }
                        }
                        LoginManager.this.process();
                        if (LoginManager.this.isShutdown()) {
                            log.info("Login Manager has been shutdown.");
                            return;
                        }
                        int interval = 1000 / THREAD_SLEEP_FREQUENCY;
                        if (timer++ % interval == 0) {
                            cachedPlayers.values()
                                    .removeIf(cachedEntry ->
                                            cachedEntry.time() < (System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10))
                                                    || cachedEntry.cachedAccount().isNulled());
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
                log.info("Login Manager has been shutdown.");
            }
        }, "Login Manager");
        thread.start();
        CoresManager.getServiceProvider().scheduleRepeatingTask(this::increment, 1, 1);
    }

    /**
     * Increments the allowed logins counter if it is below the allowed maximum per second.
     */
    private void increment() {
        loads = MAXIMUM_ALLOWED_REQUESTS_PER_SECOND;
    }

    /**
     * Decrements the allowed logins counted per second if it is above 0.
     *
     * @return whether or not the logins counter was decremented.
     */
    private boolean decrementLoads() {
        if (isShutdown() || loads <= 0) {
            return false;
        }
        loads--;
        return true;
    }

    /**
     * Decrements the allowed logins counted per second if it is above 0.
     *
     * @return whether or not the logins counter was decremented.
     */
    private boolean decrementSaves() {
        if (shutdownStage == ShutdownStage.SHUTTING_DOWN) {
            return true;
        }
        return !isShutdown();
    }

    /**
     * Passes on a request to save the player. The request is then placed inside the save requests map which ensures
     * there are no duplicates - which will then be processed whenever {@link this#process()} is referenced.
     *
     * @param player the player whose character is being saved.
     */
    public void save(@NotNull final Player player) {
        if (player.isNulled()) {
            return;
        }
        saveRequests.add(() -> saveRequestMap.put(player.getUsername(), () -> {
            try {
                serializePlayerToFile(player);
            } catch (Throwable e) {
                log.error("Something went wrong whilst saving player {}", player, e);
                e.printStackTrace(NearRealityPrintStream.getErrorStream());
            }
            return null;
        }));
    }

    /**
     * Submits a save request to execute at the end of the tick.
     *
     * @param player the player submitted.
     */
    public void submitSaveRequest(@NotNull final Player player) {
        awaitingSave.add(player);
    }

    /**
     * Passes on a request to load the player. The request is then placed inside the load requests map which ensures
     * there are no duplicates - which will then be processed whenever {@link this#process()} is referenced.
     *
     * @param playerInformation the user details of the character being loaded.
     * @param consumer          the consumer that will use the loaded player object.
     */
    public void load(
            final long startTimeOfLoad,
            @NotNull final PlayerInformation playerInformation,
            @NotNull final Consumer<Player> consumer
    ) {

        final String username = StringFormatUtil.formatUsername(playerInformation.getUsername());

        loadRequests.add(voidObject -> {

            if (cannotContinueLoadingPlayer(startTimeOfLoad, playerInformation))
                return Response.CANCELLED;

            loadRequestsMap.put(username, () -> {
                try {

                    if (cannotContinueLoadingPlayer(startTimeOfLoad, playerInformation))
                        return null;

                    // Player instance loaded using GSON from the file system.
                    final Player deserializedPlayer = deserializePlayerFromFile(username);

                    // Actual player instance stored in the world list.
                    final Player player = new Player(
                            playerInformation,
                            deserializedPlayer == null ? null :
                            deserializedPlayer.getAuthenticator()
                    );

                    if (deserializedPlayer != null) {
                        setFields(player, deserializedPlayer);
                    } else
                        setDefaults(player);

                    if (GameConstants.isOwner(player) && !player.getPrivilege().inherits(PlayerPrivilege.TRUE_DEVELOPER))
                        player.setPrivilege(PlayerPrivilege.TRUE_DEVELOPER, false);
                    else if (GameConstants.WORLD_PROFILE.isDevelopment()) {
                        if (!player.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                            player.setPrivilege(PlayerPrivilege.DEVELOPER, false);
                        }
                    }

                    cachedPlayers.remove(username);

                    consumer.accept(player);

                } catch (Throwable e) {
                    log.error("Failed to load player {}", playerInformation, e);
                    consumer.accept(null);
                }
                return null;
            });
            return Response.VOID;
        });
    }

    public void loadFake(
            final long startTimeOfLoad,
            @NotNull final PlayerInformation playerInformation
    ) {
        final String username = StringFormatUtil.formatUsername(playerInformation.getUsername());

        loadRequests.add(voidObject -> {

            if (cannotContinueLoadingPlayer(startTimeOfLoad, playerInformation))
                return Response.CANCELLED;

            loadRequestsMap.put(username, () -> {
                try {

                    if (cannotContinueLoadingPlayer(startTimeOfLoad, playerInformation))
                        return null;

                    // Player instance loaded using GSON from the file system.
                    final Player deserializedPlayer = deserializePlayerFromFile(username);

                    // Actual player instance stored in the world list.
                    final Player player = new Player(
                            playerInformation,
                            deserializedPlayer == null ? null :
                                    deserializedPlayer.getAuthenticator()
                    );

                    setDefaults(player);

                    if (GameConstants.isOwner(player))
                        player.setPrivilege(PlayerPrivilege.TRUE_DEVELOPER, false);

                    cachedPlayers.remove(username);
                } catch (Exception e) {
                    log.error("Failed to load player {}", playerInformation, e);
                }
                return null;
            });
            return Response.VOID;
        });
    }


    private static boolean cannotContinueLoadingPlayer(long startTimeOfLoad, @NotNull PlayerInformation playerInformation) {
        final boolean worldFull = World.isFull();
        if (worldFull || loadRequestExpired(startTimeOfLoad)) {
            final String reason = worldFull
                    ? "because the world is full."
                    : "because the request timed out.";
            log.info("Cancelled load request for player {} {}", playerInformation, reason);
            return true;
        }
        return false;
    }

    private static boolean loadRequestExpired(long startTimeOfLoad) {
        return System.currentTimeMillis() - startTimeOfLoad >= java.util.concurrent.TimeUnit.SECONDS.toMillis(30);
    }

    /**
     * Passes on a request to load the player. The request is then placed inside the load requests map which ensures
     * there are no duplicates - which will then be processed whenever {@link this#process()} is referenced.
     * The difference between this method and {@link this#load(long, PlayerInformation, Consumer)} is that this one
     * simply
     * loads the file through json and doesn't do anything with it, unlike the latter which will construct a new
     * player object, set the fields correctly as well as sets the defaults if the account is new.
     *
     * @param sync              whether or not the consumer function should be synchronized through a world task, or
     *                          executed directly from the {@link this#pool}
     * @param requestedUsername the username of the player requested.
     * @param consumer          the optional consumer that will use the loaded player object.
     */
    public void load(
            final boolean sync,
            @NotNull final String requestedUsername,
            @NotNull final Consumer<Optional<Player>> consumer
    ) {

        final String username = StringFormatUtil.formatUsername(requestedUsername);
        final CachedEntry cached = cachedPlayers.get(username);

        if (cached != null && !cached.cachedAccount().isNulled()) {
            consumer.accept(Optional.of(cached.cachedAccount()));
            return;
        }

        loadRequests.add(voidObject -> {
            loadRequestsMap.put(username, () -> {
                try {
                    final Player player = deserializePlayerFromFile(username);
                    if (player != null)
                        cachedPlayers.put(username, new CachedEntry(System.currentTimeMillis(), player));

                    WorldTasksManager.scheduleOrExecute(() ->
                            consumer.accept(Optional.ofNullable(player)), sync ? 0 : -1);
                } catch (Exception e) {
                    log.error("Failed to load player {}", requestedUsername, e);
                }
                return null;
            });
            return Response.VOID;
        });
    }

    public CompletableFuture<Player> loadFuture(
            @NotNull final String requestedUsername
    ) {
        final String username = StringFormatUtil.formatUsername(requestedUsername);

        final CachedEntry cached = cachedPlayers.get(username);
        if (cached != null) {
            final Player cachedAccount = cached.cachedAccount();
            if (cachedAccount != null && !cachedAccount.isNulled()) {
                return CompletableFuture.completedFuture(cachedAccount);
            }
        }

        final CompletableFuture<Player> future = new CompletableFuture<>();
        loadRequests.add(voidObject -> {
            loadRequestsMap.put(username, () -> {
                try {
                    final Player player = deserializePlayerFromFile(username);
                    if (player == null) {
                        future.complete(null);
                    } else {
                        cachedPlayers.put(username, new CachedEntry(System.currentTimeMillis(), player));
                        future.complete(player);
                    }
                } catch (final Exception e) {
                    log.error("Failed to load player {}", requestedUsername, e);
                }
                return null;
            });
            return Response.VOID;
        });

        return future;
    }

    /**
     * Sets the default fields for the newly-created account.
     *
     * @param player the player that was created.
     */
    private void setDefaults(final Player player) {
        player.setDefaultSettings();
    }

    /**
     * Processes all of the requests pushed, which maps them accordingly to either save or load requests. Continues
     * on to process the save requests if there are any - blocking the thread until all of the requests have actually
     * finished executing. Finally, continues off to process all of the load requests.
     */
    public void process() {
        synchronized (writeLock) {
            status.setTrue();
            Runnable request;
            while (decrementSaves() && (request = saveRequests.poll()) != null) {
                request.run();
            }
            Function<Void, Response> function;
            while ((function = loadRequests.poll()) != null) {
                final LoginManager.Response returnCode = function.apply(null);
                if (returnCode == Response.CANCELLED) {
                    continue;
                }
                if (!decrementLoads()) {
                    break;
                }
            }
            if (!saveRequestMap.isEmpty()) {
                try {
                    pool.invokeAll(saveRequestMap.values());
                } catch (InterruptedException e) {
                    log.error("", e);
                    return;
                }
                saveRequestMap.clear();
            }
            if (!loadRequestsMap.isEmpty()) {
                try {
                    pool.invokeAll(loadRequestsMap.values());
                } catch (InterruptedException e) {
                    log.error("", e);
                    return;
                }
                loadRequestsMap.clear();
            }
            if (shutdownStage == ShutdownStage.SHUTTING_DOWN && loadRequests.isEmpty()) {
                shutdownStage = ShutdownStage.SHUT_DOWN;
            }
            status.setFalse();
        }
    }

    public void waitForShutdown() {
        if (shutdownStage.equals(ShutdownStage.RUNNING)) {
            throw new IllegalStateException("Cannot request for shutdown waiting until the shutdown has commenced.");
        }
        while (!Thread.interrupted() && !shutdownStage.equals(ShutdownStage.SHUT_DOWN)) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
    }

    /**
     * Sets the stage to shutting down, indicating the services should no longer be accepting new load requests.
     */
    public void shutdown() {
        if (shutdownStage == ShutdownStage.RUNNING) {
            shutdownStage = ShutdownStage.SHUTTING_DOWN;
        }
    }

    /**
     * Checks to see if the services have finally shut down.
     *
     * @return whether or not the services have shut down.
     */
    private boolean isShutdown() {
        return shutdownStage == ShutdownStage.SHUT_DOWN;
    }

    /**
     * Loads up the player object based on the input username. Returns null if the player doesn't exist.
     *
     * @param username the username of the player.
     * @return the player object.
     */
    public static Player deserializePlayerFromFile(@NotNull final String username) {
        return deserializePlayerFromFile(username, gson.get());
    }

    public static Player deserializePlayerFromFile(@NotNull final String username, @NotNull final Gson gson) {
        final String formattedUsername = StringFormatUtil.formatUsername(username);
        final String fileName = formattedUsername + EXTENSION;
        final Path filePath = PLAYER_SAVE_DIRECTORY.resolve(fileName);
        if (Files.notExists(filePath)) {
            return null;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            return gson.fromJson(reader, Player.class);
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Writes the player object in json to the output folder.
     *
     * @param player the player object being written.
     */
    public void serializePlayerToFile(final Player player) {

        if (player.isNulled()) {
            log.warn("Not saving player {} because the player is nulled.", player);
            return;
        }

        String json = null;
        int attempts = 0;
        while(json == null && ++attempts <= 3) {
            try {
                json = gson.get().toJson(player);
            } catch (Throwable throwable) {
                log.error("Failed to serialize player {} on attempt {}", player, attempts, throwable);
                try {
                    Thread.sleep(3L * attempts);
                } catch (InterruptedException ignored) {

                }
            }
        }
        if (json == null)
            throw new IllegalStateException("Failed to serialize player " + player + " after 3 attempts.");
        if (attempts > 1)
            log.warn("Successfully serialized player {} after {} attempts", player, attempts);

        final String username = player.getUsername();
        final String fileName = username + EXTENSION;
        try {
            final Path tempFile = Files.createTempFile(PLAYER_SAVE_DIRECTORY, fileName, ".tmp");
            Files.writeString(tempFile, json, StandardCharsets.UTF_8);

            final Path finalFile = PLAYER_SAVE_DIRECTORY.resolve(fileName);
            Files.move(tempFile, finalFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        final Runnable runnable = player.getPostSaveFunction();
        if (runnable != null) {
            CoresManager.getServiceProvider().executeWithDelay(runnable, 300);
        }
    }

    /**
     * An enum containing the shutdown stages used for the forkjoinpool to determine whether or not the system has
     * fully shut down.
     */
    private enum ShutdownStage {
        RUNNING,
        SHUTTING_DOWN,
        SHUT_DOWN
    }

    /**
     * Sets the serialized fields of a player based on the deserialized fields from the loaded account.
     *
     * @param player the constructed played that will be logging in.
     * @param parser the loaded deserialized account.
     */

    public static void setFields(final Player player, final Player parser) {
        setFields(player, parser, false);
    }
    public static void setFields(final Player player, final Player parser, boolean skipInitEvents) {
        Location location = AreaManagerExtKt.fixLocationIfRemovedRegion(AreaManagerExtKt.fixLocationIfInstanceDC(parser, parser.getLocation()));
        player.setLastLocation(location);
        if (!(player instanceof FakePlayer))
            player.getPlayerInformation().setPlayerInformation(parser.getPlayerInformation());
        player.setPersonalBank(new SinglePlayerBank(player, parser.getPersonalBank()));
        player.forceLocation(new Location(location));
        player.setUUID();
        player.setFarming(parser.getFarming());
        player.getToxins().initialize(parser.getToxins());
        player.setPetId(parser.getPetId());
        player.getSkills().setSkills(parser.getSkills());
        player.getCombatDefinitions().setSpellbook(parser.getCombatDefinitions().getSpellbook(), false);
        player.getInventory().setInventory(parser.getInventory());
        player.getEquipment().setEquipment(parser.getEquipment());
        player.getSlayer().initialize(player, parser);
        player.getAchievementDiaries().initialize(player, parser);
        player.getVariables().set(parser.getVariables());
        player.getPrayerManager().setPrayer(parser.getPrayerManager());
        player.init(parser);
        player.getInterfaceHandler().initialize(parser.getInterfaceHandler());
        player.getBossTimer().setBossTimers(parser.getBossTimer());
        player.getMusic().getUnlockedTracks().putAll(parser.getMusic().getUnlockedTracks());
        if (parser.getTolerancePositionQueue() != null) {
            player.getTolerancePositionQueue().addAll(parser.getTolerancePositionQueue());
        }
        player.getSettings().initialize(parser.getSettings());
        player.getAppearance().initialize(parser.getAppearance());
        player.getSocialManager().initalize(parser.getSocialManager());
        player.getControllerManager().initalize(parser.getControllerManager());
        player.getCombatDefinitions().initialize(parser.getCombatDefinitions());
        player.getRunePouch().initialize(parser.getRunePouch());
        player.getSeedBox().initialize(parser.getSeedBox());
        player.getLootingBag().initialize(parser.getLootingBag());
        player.getHerbSack().initialize(parser.getHerbSack());
        player.getBonePouch().initialize(parser.getBonePouch());
        player.getDragonhidePouch().initialize(parser.getDragonhidePouch());
        player.getGemBag().initialize(parser.getGemBag());
        player.getGrandExchange().initialize(parser.getGrandExchange());
        player.getPetInsurance().initialize(parser.getPetInsurance());
        player.getKillstreakLog().initialize(parser.getKillstreakLog());
        player.getTOAManager().initialize(parser.getTOAManager());
        player.getTeleportsManager().initialize(parser.getTeleportsManager());
        player.getAttributes().putAll(parser.getAttributes());
        if (player.getGrotesqueGuardiansInstance() == null) {
            player.setGrotesqueGuardiansInstance(null);
        }

        player.setLootkeySettings(parser.getLootkeySettings());

        player.getBankPin().initialize(parser.getBankPin());
        if(!skipInitEvents) {
            PluginManager.post(new InitializationEvent(player, parser));
            PluginManager.post(new PostInitializationEvent(player));
        }
    }

    public Thread getThread() {
        return thread;
    }

    public Set<Player> getAwaitingSave() {
        return awaitingSave;
    }

    public boolean isInSaveQueue(final Player player) {
        if (player == null) {
            return false;
        }
        return saveRequestMap.containsKey(player.getUsername()) || awaitingSave.contains(player);
    }

    static class DateTypeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
        private static final String DATE_FORMAT = "MMM dd, yyyy, h:mm:ss a";
        private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        // Deserialize a JSON date string into a Java Date object
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return dateFormat.parse(json.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException("Failed to parse date: " + json.getAsString(), e);
            }
        }

        // Serialize a Java Date object into a JSON date string
        @Override
        public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormat.format(date));
        }
    }

}
