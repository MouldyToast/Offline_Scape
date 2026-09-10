package org.jesse.game.world;

import cloud.rsps.rsprot.RsprotPingManager;
import cloud.rsps.rsprot.Session;
import org.jesse.game.content.bountyhunter.BountyHunterController;
import org.jesse.game.content.commands.DeveloperCommands;
import org.jesse.game.world.PlayerEvent;
import org.jesse.game.world.WorldEvent;
import org.jesse.game.world.WorldHooks;
import org.jesse.game.world.entity.player.FakePlayer;
import org.jesse.threads.MainThread;
import org.jesse.Main;
import org.jesse.cores.CoresManager;
import org.jesse.game.GameConstants;
import org.jesse.game.model.BonusXpManager;
import org.jesse.game.model.shop.Shop;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.LogoutType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.login.LoginManager;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.PrebuiltDynamicAreaManager;
import org.jesse.game.world.region.zone.ZoneManager;
import org.jesse.logger.NearRealityLogger;
import org.jesse.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.rsprot.protocol.api.NetworkService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public final class WorldThread extends MainThread {

    private static final Logger log = NearRealityLogger.getLogger(WorldThread.class);
    private static final Logger tickLogger = LoggerFactory.getLogger("Tick Logger");

    private static final int CHUNK_PURGE_TICK_INTERVAL = 500;

    public static final AtomicLong currentCycle = new AtomicLong(0);

    public static long getCurrentCycle() {
        return currentCycle.get();
    }

    private static volatile long currentCycleNano = System.nanoTime();

    public static long getCurrentCycleNano() {
        return currentCycleNano;
    }

    private static boolean skipPlayer(final Player player) {
        return player == null
                || player.isNulled()
                || !player.isInitialized()
                || player.isFinished();
    }

    private static int randomPIDSwapDelay() {
        return Utils.random(100, 150);
    }

    @Nullable
    private final NetworkService<Session> networkService;

    private final WorldHooks hooks = new WorldHooks();

    private boolean purgeMoreChunksNextCycle;

    private int pidSwapDelay = randomPIDSwapDelay();

    public WorldThread(
            final @NotNull String name,
            final boolean enableAffinityLock,

            @Nullable final NetworkService<Session> networkService
    ) {
        super(name, enableAffinityLock);

        this.networkService = networkService;
    }

    @Override
    public void cycle() {
        try {
            worldCycle(networkService);
        } catch (final Throwable t) {
            log.error("Failed to complete world cycle", t);
        }
    }

    private void worldCycle(
            final NetworkService<Session> networkService
    ) {
        final long currentCycle = WorldThread.currentCycle.getAndIncrement();
        final long currentCycleNano = WorldThread.currentCycleNano = System.nanoTime();

        try {
            var players = World.getPlayers().stream().filter(Objects::isNull);
            for(Player player: players.toList()) {
                World.getPlayers().remove(player);
            }
        } catch (final Throwable t) {
            log.error("Failed to remove null players", t);
        }

        try {
            if (hooks.hasListenersFor(WorldEvent.Tick.class)) {
                hooks.post(new WorldEvent.Tick(currentCycle));
            }
        } catch (final Throwable t) {
            log.error("Failed to post world tick", t);
        }

        try {
            Container.resetContainer();
        } catch (final Throwable e) {
            log.error("Failed to reset containers", e);
        }

        final long shopNano = System.nanoTime();
        try {
            Shop.process();
        } catch (final Throwable e) {
            log.error("Failed to process shops", e);
        }

        final long areaManagerNano = System.nanoTime();
        try {
            GlobalAreaManager.process();
        } catch (final Throwable e) {
            log.error("Failed to process global areas", e);
        }

        try {
            PrebuiltDynamicAreaManager.process();
        } catch (final Throwable e) {
            log.error("Failed to process advanced dynamic areas", e);
        }

        final long worldTaskProcessNano = System.nanoTime();
        try {
            WorldTasksManager.processTasks();
        } catch (final Throwable e) {
            log.error("Failed to process world tasks", e);
        }

        try {
            BountyHunterController.process();
        } catch (final Throwable e) {
            log.error("Failed to process bounty hunter", e);
        }

        final long gameClockNano = System.nanoTime();
        try {
            BonusXpManager.checkIfFlip();
        } catch (final Exception e) {
            log.error("Failed to process gameclock and bonus xp manager", e);
        }

        final long readPacketsNano = System.nanoTime();
        processPlayerSessions(currentCycle);

        final long npcProcessNano = System.nanoTime();
        processNPCs(npcProcessNano);

        final long npcRemovalNano = System.nanoTime();
        processNPCRemoval();

        try {
            NPC.clearPendingAggressions();
        } catch (final Throwable e) {
            log.error("Failed to clear pending aggressions", e);
        }

        final long playerProcessNano = System.nanoTime();
        processPlayers();

        updatePlayerFollowTiles();
        processPlayerFollowing();

        final long playerPostProcessNano = System.nanoTime();
        postProcessPlayers();

        final long areaPostProcessNano = System.nanoTime();
        try {
            GlobalAreaManager.postProcess();
        } catch (final Throwable e) {
            log.error("Failed to post process global areas", e);
        }

        preUpdateEntities();
        postProcessUpdates();

        final long playerEntityUpdateNano = System.nanoTime();
        processPlayerEntityUpdate();

        final long playerLogoutNano = System.nanoTime();
        processPlayerLogouts(networkService, currentCycle);

        final long maskResetNano = System.nanoTime();
        resetNpcMasks();

        resetPlayerMasks();

        //World.getQueueList().cycle();

        shufflePids();

        final long purgeNano = System.nanoTime();

        purgeWorldChunks(currentCycle);

        final long playerSaveNano = System.nanoTime();
        savePlayers();

        try {
            RsprotPingManager.process(currentCycle, currentCycleNano);
        } catch (final Throwable t) {
            log.error("Failed to process ping manager", t);
        }

        final long finishNano = System.nanoTime();

        final long totalT = finishNano - currentCycleNano;
        final long totalMillis = TimeUnit.NANOSECONDS.toMillis(totalT);
        final boolean slowTick = totalMillis > GameConstants.WORLD_CYCLE_TIME / 2;
        final int playerCount = World.getPlayers().size();
        final int npcCount = World.getNPCs().size();
        final int taskCount = WorldTasksManager.getCount();

        if (slowTick) {
            log.error("SLOW CYCLE took: {}ms. Players: {}. NPCs: {}", totalMillis, playerCount, npcCount);
        }

        if (slowTick || GameConstants.CYCLE_DEBUG) {
            final TickLog tickLog = new TickLog(
                    new Date(),
                    currentCycle,
                    NANOSECONDS.toMillis(shopNano - currentCycleNano),
                    NANOSECONDS.toMillis(areaManagerNano - shopNano),
                    NANOSECONDS.toMillis(worldTaskProcessNano - areaManagerNano),
                    NANOSECONDS.toMillis(gameClockNano - worldTaskProcessNano),
                    NANOSECONDS.toMillis(readPacketsNano - gameClockNano),
                    NANOSECONDS.toMillis(npcProcessNano - readPacketsNano),
                    NANOSECONDS.toMillis(npcRemovalNano - npcProcessNano),
                    NANOSECONDS.toMillis(playerProcessNano - npcRemovalNano),
                    NANOSECONDS.toMillis(playerPostProcessNano - playerProcessNano),
                    NANOSECONDS.toMillis(areaPostProcessNano - playerPostProcessNano),
                    NANOSECONDS.toMillis(playerEntityUpdateNano - areaPostProcessNano),
                    NANOSECONDS.toMillis(playerLogoutNano - playerEntityUpdateNano),
                    NANOSECONDS.toMillis(maskResetNano - playerLogoutNano),
                    NANOSECONDS.toMillis(purgeNano - maskResetNano),
                    NANOSECONDS.toMillis(currentCycle % 500 == 0 ? (playerSaveNano - purgeNano) : 0),
                    NANOSECONDS.toMillis(finishNano - playerSaveNano),
                    NANOSECONDS.toMillis(totalT),
                    playerCount,
                    npcCount,
                    taskCount);
            if (GameConstants.CYCLE_DEBUG) {
                tickLogger.warn("Cycle took: {}ms. Players: {}. NPCs: {}", totalMillis, playerCount, npcCount);
            } else
                tickLogger.info("Cycle took: {}ms. Players: {}. NPCs: {}", totalMillis, playerCount, npcCount);
            ForkJoinPool.commonPool().execute(() -> {
                final String tickGSON = DefaultGson.getGson().toJson(tickLog);
                if (slowTick) tickLogger.error(tickGSON);
                else if (GameConstants.CYCLE_DEBUG) tickLogger.warn(tickGSON);
                else tickLogger.info(tickGSON);
            });
        }

        if (CoresManager.isShutdown()) {
            try {
                World.shutdown();
            } catch (Throwable e) {
                log.error("Failed to shutdown work", e);
            }
        }
    }

    private void preUpdateEntities() {
        try {
            for (final NPC npc : World.getNPCs()) {
                try {
                    if (npc == null) {
                        continue;
                    }
                    npc.beforeEntityUpdate();
                } catch (final Exception e) {
                    log.error("", e);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        for (final Player player : World.usedPIDs.values()) {
            try {
                if (skipPlayer(player)) {
                    continue;
                }
                player.beforeEntityUpdate();
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private void postProcessUpdates() {
        try {
            ZoneManager.INSTANCE.computeSharedEvents();
        } catch (final Throwable t) {
            log.error("Failed to compute shared events", t);
        }

        final NetworkService<Session> service = this.networkService;
        if (service != null) {
            synchronized (Main.getNetworkServiceLock()) {
                try {
                    service.getWorldEntityInfoProtocol().update();
                } catch (final Throwable t) {
                    log.error("Failed to update world entity info protocol", t);
                }
                try {
                    service.getPlayerInfoProtocol().update();
                } catch (final Throwable t) {
                    log.error("Failed to update player entity info protocol", t);
                }
                try {
                    service.getNpcInfoProtocol().update();
                } catch (final Throwable t) {
                    log.error("Failed to update npc entity info protocol", t);
                }
            }
        }
    }
    private Object2IntMap<Player> pendingUsers = new Object2IntOpenHashMap<>();
    private void processPlayerSessions(final long currentCycle) {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) {
                        log.warn("Skipping and recording for logout for player {} isNulled={}, isInitialized={}, isFinished={}", player, player.isNulled(), player.isInitialized(), player.isFinished());
                        if(player != null) {
                            if (pendingUsers.getOrDefault(player, 0) >= 3) {
                                pendingUsers.remove(player);
                                player.logout(true, "Player Session is unable to process.");
                            } else {
                                int curr = pendingUsers.getOrDefault(player, 0);
                                pendingUsers.put(player, curr + 1);
                            }
                        }
                        continue;
                    } else {
                        pendingUsers.remove(player);
                    }

                    final Session session = player.getSession();
                    if (session != null) {
                        session.process(currentCycle);
                    } else if (!(player instanceof FakePlayer)) {
                        log.error("Player {} has no session", player);
                    }
                } catch (final Throwable e) {
                    log.error("Failed to process player " + player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process player sessions", e);
        }
    }

    private void processNPCs(final long npcProcessNano) {
        long singleNpcProcessNano = npcProcessNano;
        try {
            for (final NPC npc : World.getNPCs()) {
                try {
                    if (npc == null) continue;

                    npc.processEntity();

                    if (DeveloperCommands.INSTANCE.getNpcProcessTimeLogging()) {
                        final long currentTime = System.nanoTime();
                        final long npcDuration = singleNpcProcessNano - currentTime;
                        singleNpcProcessNano = currentTime;
                        if (npcDuration > 1_000_000) {
                            log.warn(
                                    "Slow NPC.processEntity() invocation ({}ms) for NPC (id = {}, index = {}, " +
                                            "location = {})",
                                    NANOSECONDS.toMillis(npcDuration), npc.getId(), npc.getIndex(),
                                    npc.getLocation());
                        }
                    }
                } catch (final Throwable e) {
                    log.error("Failed to process NPC" +
                                    " (id = " + npc.getId()
                                    + ", index = " + npc.getIndex()
                                    + ", location = " + npc.getLocation()
                                    + ")",
                            e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process NPCs", e);
        }
    }

    private void processNPCRemoval() {
        try {
            for (final NPC removed : World.pendingRemovedNPCs) {
                try {
                    if (removed == null) continue;

                    World.getNPCs().remove(removed);
                } catch (final Throwable e) {
                    log.error("Failed to remove npc " + removed, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to remove " + World.pendingRemovedNPCs.size() + " NPCs pending for removal", e);
        }

        try {
            World.pendingRemovedNPCs.clear();
        } catch (final Throwable e) {
            log.error("Failed to clear " + World.pendingRemovedNPCs.size() + " NPCs pending for removal", e);
        }
    }

    private void processPlayers() {
        try {
            final boolean postEvents = hooks.hasListenersFor(PlayerEvent.Process.class);
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    if (postEvents) {
                        hooks.post(new PlayerEvent.Process(player));
                    }
                    player.processEntity();
                } catch (final Throwable e) {
                    log.error("Failed to process player " + player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process players", e);
        }
    }

    private void updatePlayerFollowTiles() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    player.updateFollowTile();
                } catch (final Throwable e) {
                    log.error("Failed to update follow tile for player " + player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to update follow tiles", e);
        }
    }

    private void processPlayerFollowing() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    player.processFollowing();
                } catch (final Throwable e) {
                    log.error("Failed to process following for player " + player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to process following for players", e);
        }
    }

    private void postProcessPlayers() {
        try {
            final boolean postEvents = hooks.hasListenersFor(PlayerEvent.PostProcess.class);
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    if (postEvents) {
                        hooks.post(new PlayerEvent.PostProcess(player));
                    }

                    player.postProcess();
                } catch (final Throwable e) {
                    log.error("Failed to post process player " + player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to post process players", e);
        }
    }

    private void processPlayerEntityUpdate() {
        try {
            final boolean postEvents = hooks.hasListenersFor(PlayerEvent.Update.class);
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    if (postEvents) {
                        hooks.post(new PlayerEvent.Update(player));
                    }
                    player.processEntityUpdate();
                } catch (final Throwable e) {
                    log.error("Failed to perform entity update for player " + player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to perform entity update for players", e);
        }
    }

    private void processPlayerLogouts(
            final NetworkService<Session> networkService,
            final long currentCycle
    ) {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (skipPlayer(player)) continue;

                    if (player.isSessionExpired(currentCycle)) {
                        if (player.isDeveloper()) {
                            log.info("Player {} has an expired session, unregistering...", player);
                        }
                        World.unregisterPlayer(player, networkService);
                    } else {
                        final LogoutType logoutType = player.getLogoutType();
                        processPlayerLogoutType(networkService, player, logoutType);
                    }
                } catch (final Throwable e) {
                    log.error("Failed to logout player {}", player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to logout players", e);
        }
    }

    private void processPlayerLogoutType(
            final NetworkService<Session> networkService,

            final Player player,
            final LogoutType logoutType
    ) {
        switch (logoutType) {
            case CONNECTION_LOST -> {
                if (player.isDeveloper()) {
                    log.trace("Player {} has lost connection, waiting for session to expire...", player);
                }
            }

            case FORCE, UPDATING -> {
                if (player.isDeveloper()) {
                    log.info("Player {} is being forcefully logged out", player);
                }
                player.getPacketDispatcher().sendLogout(logoutType);
                player.getSession().flush();
                World.unregisterPlayer(player, networkService);
            }

            case REQUESTED -> {
                if (player.canLogout(false)) {
                    if (player.isDeveloper()) {
                        log.info("Player {} is being logged out", player);
                    }

                    player.getPacketDispatcher().sendLogout(logoutType);
                    player.getSession().flush();
                    World.unregisterPlayer(player, networkService);
                    return;
                } else {
                    if (player.isDeveloper()) {
                        log.info("Player {}'s logout has been cancelled as they currently cannot.", player);
                    }

                    player.setLogoutType(LogoutType.NONE);
                }

                player.getSession().flush();
            }

            case NONE -> player.getSession().flush();
        }
    }

    private void resetNpcMasks() {
        try {
            for (final NPC npc : World.getNPCs()) {
                try {
                    if (npc == null || npc.isFinished()) {
                        continue;
                    }
                    npc.resetMasks();
                } catch (final Throwable e) {
                    log.error("Failed to reset masks for NPC " + npc, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to reset NPC masks", e);
        }
    }

    private void resetPlayerMasks() {
        try {
            for (final Player player : World.usedPIDs.values()) {
                try {
                    if (player == null || player.isFinished()) continue;
                    player.resetMasks();
                } catch (final Throwable e) {
                    log.error("Failed to reset masks for player " + player, e);
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to reset player masks.", e);
        }
    }

    private void shufflePids() {
        try {
            if (--pidSwapDelay == 0) {
                try {
                    World.shufflePids();
                } catch (final Throwable e) {
                    log.error("Failed to shuffle PIDs, inner", e);
                }
                pidSwapDelay = randomPIDSwapDelay();
            }
        } catch (final Throwable e) {
            log.error("Failed to shuffle PIDs, outer", e);
        }
    }

    private void purgeWorldChunks(final long currentCycle) {
        ZoneManager.INSTANCE.postUpdate();

        try {
            if (GameConstants.PURGING_CHUNKS) {
                if (currentCycle % CHUNK_PURGE_TICK_INTERVAL == 0 || purgeMoreChunksNextCycle) {
                    purgeMoreChunksNextCycle = World.purgeChunks();
                }
            }
        } catch (final Throwable e) {
            log.error("Failed to purge world chunks", e);
        }
    }

    private void savePlayers() {
        try {
            final LoginManager loginManager = CoresManager.getLoginManager();
            final Set<Player> awaitingSave = loginManager.getAwaitingSave();
            if (!awaitingSave.isEmpty()) {
                for (final Player player : awaitingSave) {
                    loginManager.save(player);
                }
                awaitingSave.clear();
            }
        } catch (final Throwable e) {
            log.error("Failed to save players", e);
        }
    }

    public record TickLog(Date date, long tick, long containerT, long shopT, long areaT, long worldTaskT,
                          long gameClockT, long readPacketsT, long npcProcessT, long npcRemovalT, long playerProcessT,
                          long playerPostProcessNano, long areaPostProcessNano, long playerEntityUpdateNano,
                          long playerLogoutT, long maskResetT, long purgeT, long playerSaveT, long totalT, int players,
                          int npcs, int tasks) {
    }

    public WorldHooks getHooks() {
        return hooks;
    }

}
