package com.zenyte.game.content.colosseum;

import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.obj.ids.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.EquipmentPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.logger.NearRealityPrintStream;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class ColosseumInstance extends DynamicArea implements EquipmentPlugin, CannonRestrictionPlugin, DeathPlugin, CycleProcessPlugin {

    public static final String DEATHS_ATTRIBUTE = "colosseum_deaths";
    public static final String ATTEMPTS_ATTRIBUTE = "colosseum_attempts";

    private static final Animation FALL_BACK_ANIMATION = new Animation(2390);
    public static final Location SPAWN_LOCATION = new Location(1800, 9505);
    private static final int[] BORDER_LOC_IDS = { 50753, 50754, 50755, 50756, 50757, 50758 };
    private static final Location[] BORDER_LOCATIONS = {
            new Location(1817, 3099),//southwest
            new Location(1832, 3114)//northeast
    };

    private final Player player;
    private SolHeredit solHeredit;
    private final Location[] arena = new Location[2];
    private final Container rewards;
    private final Container rewardsFuture;
    private final Container rewardsPrevious;

    // Wave state
    private int currentWave = 0;
    private final java.util.List<ColosseumWaveNpc> waveNpcs = new java.util.ArrayList<>();
    private NPC minimusInside;
    private NPC solSeated;
    private long waveStartTick;
    private boolean reinforcementsSpawned;

    // Modifier state
    private int activeModifierBitmask = 0;
    private int[] offeredModifiers = new int[3];

    // Loot GP tracking for CS2 4931 intermission UI
    private int currentWaveLootGp = 0;
    private int nextWaveLootGp = 0;
    private int totalLootGp = 0;

    protected ColosseumInstance(AllocatedArea allocatedArea, Player player) {
        super(allocatedArea, 7216);
        this.player = player;
        this.rewards = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.COLOSSEUM_REWARDS, Optional.of(player));
        this.rewardsFuture = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.COLOSSEUM_REWARDS_FUTURE, Optional.of(player));
        this.rewardsPrevious = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.COLOSSEUM_REWARDS_PREVIOUS, Optional.of(player));
    }

    @Override
    public void constructed() {
        // Border is NOT created here — it only appears for the Sol Heredit fight (wave 12).
        // During waves 1-11, the colosseum walls are the boundary.
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getHpHud().close();
    }

    @Override
    public String name() {
        return "Sol Heredit instance";
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public boolean unequip(Player player, Item item, int slot) {
        if (solHeredit == null) {
            return true;
        }

        SolHeredit.GrappleStyle grappleStyle = solHeredit.getGrappleStyle();
        if (grappleStyle == null) {
            return true;
        }

        if (grappleStyle.getSlot().getSlot() != slot) {
            player.sendMessage(Colour.RED.wrap("You defended the wrong body part!"));
        }

        solHeredit.setClickedSlot(slot);
        return false;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return "";
    }

    @Override
    public Location getRespawnLocation() {
        return new Location(SPAWN_LOCATION.getX() + Utils.random(4), SPAWN_LOCATION.getY() + Utils.random(4));
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        ColosseumStatistics.statistics.increaseDeathCount();
        player.incrementNumericAttribute(DEATHS_ATTRIBUTE, 1);
        if (solHeredit != null) {
            solHeredit.say(Utils.random(SolHeredit.KILL_PLAYER_MESSAGES));
        }
        // Death during waves = all accumulated rewards lost. No chest spawns.
        rewards.clear();
        rewardsFuture.clear();
        rewardsPrevious.clear();
        return false;
    }

    @Override
    public Location gravestoneLocation() {
        return new Location(SPAWN_LOCATION.getX() + Utils.random(4), SPAWN_LOCATION.getY() + Utils.random(4));
    }

    private void createBorder() {
        Location sw = getLocation(BORDER_LOCATIONS[0]);
        Location ne = getLocation(BORDER_LOCATIONS[1]);

        //Trim down borders so it doesn't touch the guards with shields
        arena[0] = sw.transform(1, 1);
        arena[1] = ne.transform(-1, -1);

        int minX = sw.getX();
        int minY = sw.getY();
        int maxX = ne.getX();
        int maxY = ne.getY();

        // Bottom edge (left to right)
        for (int x = minX + 2; x <= maxX - 2; x++) {
            World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 2, new Location(x, minY)));
        }

        // Right edge (bottom to top)
        for (int y = minY + 2; y <= maxY - 2; y++) {
            World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 1, new Location(maxX, y)));
        }

        // Top edge (right to left)
        for (int x = maxX - 2; x >= minX + 2; x--) {
            World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 0, new Location(x, maxY)));
        }

        // Left edge (top to bottom)
        for (int y = maxY - 2; y >= minY + 2; y--) {
            World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 3, new Location(minX, y)));
        }
    }

    public boolean outsideOfArena(int x, int y) {
        Location sw = getArenaSw();
        Location ne = getArenaNe();
        return x < Math.min(sw.getX(), ne.getX()) || x > Math.max(sw.getX(), ne.getX()) || y < Math.min(sw.getY(), ne.getY()) || y > Math.max(sw.getY(), ne.getY());
    }

    public Player getPlayer() {
        return player;
    }

    public Location getArenaSw() {
        return arena[0];
    }

    public Location getArenaNe() {
        return arena[1];
    }

    public Container getRewards() {
        return rewards;
    }

    public Container getRewardsFuture() {
        return rewardsFuture;
    }

    public Container getRewardsPrevious() {
        return rewardsPrevious;
    }

    /**
     * Grant wave 12 (Sol Heredit completion) rewards.
     * Appends to the existing inv 843 which already contains waves 1–11 loot.
     * <p>
     * Wave 12 grants:
     * 1. Guaranteed Dizana's quiver (uncharged)
     * 2. Flat 1/200 Smol Heredit pet roll
     * 3. One item from the wave 12 weighted table
     */
    public void grantRewards() {
        // 1. Guaranteed Dizana's quiver
        rewards.add(new Item(ItemId.DIZANAS_QUIVER_UNCHARGED, 1));

        // 2. Flat 1/200 Smol Heredit pet roll
        BossPet.SMOL_HEREDIT.roll(player, 200);

        // 3. Regular wave 12 table roll
        Item tableDrop = ColosseumWaveLoot.roll(player, 12);
        rewards.add(tableDrop);

        // Update total GP for the reward chest interface
        totalLootGp = (int) rewards.calculateValue();
    }

    public void fillLine(int startX, int startY, Direction direction, int length, BiConsumer<Location, Integer> tileConsumer, BiConsumer<Location, Integer> endConsumer) {
        int endX = startX + direction.getOffsetX() * length;
        int endY = startY + direction.getOffsetY() * length;
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        int sx = Integer.signum(endX - startX);
        int sy = Integer.signum(endY - startY);
        int err = dx - dy;
        int n = 0;
        while (true) {
            if (tileConsumer != null) {
                tileConsumer.accept(new Location(startX, startY), n);
            }
            n++;
            if (startX == endX && startY == endY)
                break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                startX += sx;
            }
            if (e2 < dx) {
                err += dx;
                startY += sy;
            }

            if (outsideOfArena(startX, startY)) {
                break;
            }
        }

        if (endConsumer != null) {
            endConsumer.accept(new Location(startX, startY), n);
        }
    }

    public static void checkUnderChest(Player player, WorldObject object) {
        Location playerLocation = player.getLocation();
        if (playerLocation.getX() >= object.getX() && playerLocation.getY() >= object.getY() &&
                playerLocation.getX() <= object.getX() + object.getDefinitions().getSizeX() && playerLocation.getY() <= object.getY() + object.getDefinitions().getSizeY()) {
            Location destination = object.transform(-1, 1);
            final int direction = DirectionUtil.getFaceDirection(player.getX() - destination.getX(), player.getY() - destination.getY());
            player.setAnimation(FALL_BACK_ANIMATION);
            player.setForceMovement(new ForceMovement(destination, 30, direction));
            WorldTasksManager.schedule(() -> player.setLocation(destination));
        }
    }

    public void spawnChest() {
        WorldObject chest = new WorldObject(ObjectId.REWARDS_CHEST_50741, 10, 1, getLocation(1829, 3105));
        World.spawnObject(chest);
        checkUnderChest(player, chest);
        NPC minimus = new NPC(NpcId.MINIMUS_12808, getLocation(1830, 3103), Direction.WEST, 0);
        minimus.spawn();
        //Needs to be 1 tick later?
        WorldTasksManager.schedule(() -> minimus.setOptionMask(30));
    }

    /**
     * Forfeit flow: player clicked Claim → Confirm on the intermission UI.
     * RSProx sequence:
     * 1. Message "Search the chest nearby to retrieve your earned rewards!"
     * 2. Despawn intermission Minimus, respawn near exit at (1830, 3103) facing west
     * 3. Close intermission interface
     * 4. Spawn reward chest at (1829, 3105)
     */
    public void forfeitRun() {
        // Close intermission interface
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);

        // Message
        player.sendMessage(Colour.RS_GREEN.wrap("Search the chest nearby to retrieve your earned rewards!"));

        // Despawn intermission Minimus and respawn near exit
        if (minimusInside != null) {
            minimusInside.finish();
            minimusInside = null;
        }

        // Spawn chest and exit Minimus (reuses existing spawnChest method)
        spawnChest();
    }

    public static void createInstance(Player player) {
        AllocatedArea allocatedArea;
        try {
            allocatedArea = MapBuilder.findEmptyChunk(64, 64);
        } catch (OutOfSpaceException e) {
            e.printStackTrace(NearRealityPrintStream.getErrorStream());
            return;
        }

        ColosseumInstance instance = new ColosseumInstance(allocatedArea, player);
        instance.constructRegion();
        new FadeScreen(player, instance::enterArena).fade(3);
    }

    private void enterArena() {
        // Reset colosseum varps (RSProx: all zeroed at entry)
        player.getVarManager().sendVar(4132, 0);  // colosseum_current_glory
        player.getVarManager().sendVar(4133, 0);  // colosseum_wave_start_time
        player.getVarManager().sendVar(4134, 0);  // colosseum_wave_damage_taken
        player.getVarManager().sendVar(4136, 0);  // colosseum_last_wave_duration
        player.getVarManager().sendVar(4137, 0);  // colosseum_total_duration
        player.getVarManager().sendBit(9788, 0);  // colosseum_selected_modifier
        player.getVarManager().sendBit(9790, 0);  // blasphemy stacks
        player.getVarManager().sendBit(9799, 0);  // volatility stacks

        // Preload custom animations (CS2 1846 = seq_prefetch)
        preloadAnimations();

        // Spawn Sol Heredit SEATED at top of arena (RSProx: (1823, 3123), spawnangle=south)
        solSeated = new NPC(NpcId.SOL_HEREDIT_12827, getLocation(1823, 3123), Direction.SOUTH, 0);
        solSeated.spawn();

        // Spawn Minimus at centre-south (RSProx: (1824, 3106), spawnangle=south)
        minimusInside = new NPC(NpcId.MINIMUS_12808, getLocation(1824, 3106), Direction.SOUTH, 0);
        minimusInside.spawn();

        // Teleport player to arena floor (RSProx: player arrives at (1824, 3101), south of Minimus)
        player.setLocation(getLocation(1824, 3101));

        // Reset wave state
        currentWave = 0;
        activeModifierBitmask = 0;
        currentWaveLootGp = 0;
        nextWaveLootGp = 0;
        totalLootGp = 0;
        rewards.clear();
        rewardsFuture.clear();
        rewardsPrevious.clear();
        waveNpcs.clear();
    }

    private void preloadAnimations() {
        // CS2 1846 = seq_prefetch($seq). Preloads colosseum-specific animations.
        // Anim IDs from RSProx entry sequence (Run 1, ~60 calls).
        int[] anims = {
                10820, 10834, 10835, 10836, 10837,
                10863, 10864, 10865, 10866, 10867, 10868, 10869, 10870, 10871,
                10903, 10902, 10899, 10879, 10880, 10881,
                10889, 10890, 10891, 10892, 10893, 10894, 10895, 10896,
                10882, 10883, 10884, 10885, 10886, 10887, 10888,
                10840, 10841, 10842, 10843, 10844, 10845, 10846,
                10800, 10801, 10802, 10803, 10804, 10805, 10806,
                10807, 10808, 10809, 10810, 10811, 10812,
                10876, 10877, 10878
        };
        for (int animId : anims) {
            player.getPacketDispatcher().sendClientScript(1846, animId);
        }
    }

    public void openIntermission() {
        // Face Minimus toward player
        if (minimusInside != null) {
            minimusInside.faceEntity(player);
        }

        // Pick 3 random modifiers to offer (placeholder — full modifier system is future work)
        offeredModifiers = pickModifiers();

        // Reset selection varbit
        player.getVarManager().sendBit(9788, 0);

        // Open interface 865 as modal
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 865);

        // Send full inventory updates for all three reward containers (RSProx: update_inv_full on intermission open)
        player.getPacketDispatcher().sendUpdateItemContainer(rewards);
        player.getPacketDispatcher().sendUpdateItemContainer(rewardsFuture);
        player.getPacketDispatcher().sendUpdateItemContainer(rewardsPrevious);

        // Fire CS2 4931 with modifier args
        // [completed_wave, mod1, mod2, mod3, current_gp, next_gp, total_gp, bitmask]
        player.getPacketDispatcher().sendClientScript(4931,
                currentWave,
                offeredModifiers[0],
                offeredModifiers[1],
                offeredModifiers[2],
                currentWaveLootGp,
                nextWaveLootGp,
                totalLootGp,
                activeModifierBitmask
        );

        // Enable button events on interface components (RSProx verified)
        player.getPacketDispatcher().sendComponentSettings(865, 8, 0, 3, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(865, 39, 0, 3, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(865, 34, 0, 26, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentSettings(865, 37, 0, 30, AccessMask.CLICK_OP1);
    }

    public void confirmModifierAndStartWave(int selectedIndex) {
        // Resolve which modifier was picked (1-indexed)
        int modifierId = offeredModifiers[selectedIndex - 1];
        activeModifierBitmask |= (1 << modifierId);

        // Reset selection varbit
        player.getVarManager().sendBit(9788, 0);

        // Despawn Minimus
        if (minimusInside != null) {
            minimusInside.finish();
            minimusInside = null;
        }

        // Close interface
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);

        // Advance wave
        currentWave++;

        if (currentWave == 12) {
            startSolFight();
            return;
        }

        // 5-tick delay before NPCs spawn (RSProx: confirm at clock N, NPCs spawn at clock N+5).
        // Gives the player time to eat, pray, and reposition after the interface closes.
        WorldTasksManager.schedule(this::startWave, 4); // 0-indexed: 4 = 5 ticks later
    }

    /**
     * Factory: returns the right ColosseumWaveNpc subclass for NPCs with custom
     * combat scripts, or plain ColosseumWaveNpc for NPCs without one yet.
     * Add one case per combat script as they're built (Phase A, sections 1A–1F).
     */
    private ColosseumWaveNpc createWaveNpc(int npcId, Location spawnLoc) {
        return switch (npcId) {
            case NpcId.MANTICORE -> new ManticoreCombat(npcId, spawnLoc, this);
            case NpcId.FREMENNIK_WARBAND_BERSERKER,
                 NpcId.FREMENNIK_WARBAND_ARCHER,
                 NpcId.FREMENNIK_WARBAND_SEER -> new FremennikWarbandCombat(npcId, spawnLoc, this);
            // case NpcId.SERPENT_SHAMAN -> new SerpentShamanCombat(npcId, spawnLoc, this);
            // case NpcId.JAGUAR_WARRIOR -> new JaguarWarriorCombat(npcId, spawnLoc, this);
            case NpcId.JAVELIN_COLOSSUS -> new JavelinColossusCombat(npcId, spawnLoc, this);
            // case NpcId.SHOCKWAVE_COLOSSUS -> new ShockwaveColossusCombat(npcId, spawnLoc, this);
            // case NpcId.MINOTAUR_12812 -> new MinotaurCombat(npcId, spawnLoc, this);
            // case NpcId.MINOTAUR_12813 -> new MinotaurCombat(npcId, spawnLoc, this);
            default -> new ColosseumWaveNpc(npcId, spawnLoc, this);
        };
    }

    private void startWave() {
        // Set wave start tick
        waveStartTick = WorldThread.getCurrentCycle();
        player.getVarManager().sendVar(4133, (int) waveStartTick);

        // Send wave start message (RSProx: "<col=ff3045>Wave: N</col>")
        player.sendMessage(Colour.RED.wrap("Wave: " + currentWave));

        // Get spawn list
        java.util.List<Integer> npcIds = WaveData.getStartingNpcs(currentWave, activeModifierBitmask);

        // Pre-filter spawn points by 4-tile exclusion zone
        Set<Integer> usedSpawnIndices = new HashSet<>();
        Set<Integer> excludedIndices = new HashSet<>();
        Location playerLoc = player.getLocation();
        for (int i = 0; i < WaveData.SPAWN_POINTS.length; i++) {
            int[] point = WaveData.SPAWN_POINTS[i];
            Location spawnLoc = getLocation(point[0], point[1]);
            if (isWithinExclusionZone(playerLoc, spawnLoc, 3, 4)) {
                excludedIndices.add(i);
            }
        }

        // Spawn NPCs
        waveNpcs.clear();
        reinforcementsSpawned = false;
        // Fremennik spawn at a random base tile within the centre zone, then each NPC
        // is placed at a fixed offset forming an inverted-V (RSProx-verified all 11 waves):
        //     B (0, +1)       Berserker at north tip
        // A (-1, 0)  S (+1, 0)   Archer west, Seer east
        //    ExA (0, -1)      Quartet 4th archer at south point (diamond)
        int fremBaseX = Utils.random(WaveData.FREMENNIK_ZONE_MIN_X, WaveData.FREMENNIK_ZONE_MAX_X);
        int fremBaseY = Utils.random(WaveData.FREMENNIK_ZONE_MIN_Y, WaveData.FREMENNIK_ZONE_MAX_Y);
        boolean fremBerserkerPlaced = false;
        boolean fremArcherPlaced = false;
        boolean fremSeerPlaced = false;
        for (int npcId : npcIds) {
            Location spawnLoc;
            int fremDx = 0, fremDy = 0;
            if (WaveData.isFremennik(npcId)) {
                if (npcId == NpcId.FREMENNIK_WARBAND_BERSERKER && !fremBerserkerPlaced) {
                    fremDy = 1;              // (0, +1) north
                    fremBerserkerPlaced = true;
                } else if (npcId == NpcId.FREMENNIK_WARBAND_ARCHER && !fremArcherPlaced) {
                    fremDx = -1;             // (-1, 0) west
                    fremArcherPlaced = true;
                } else if (npcId == NpcId.FREMENNIK_WARBAND_SEER && !fremSeerPlaced) {
                    fremDx = 1;              // (+1, 0) east
                    fremSeerPlaced = true;
                } else {
                    fremDy = -1;             // (0, -1) south — quartet extra archer
                }
                spawnLoc = getLocation(fremBaseX + fremDx, fremBaseY + fremDy);
            } else {
                int[] point = WaveData.getRandomSpawnPoint(usedSpawnIndices, excludedIndices);
                spawnLoc = getLocation(point[0], point[1]);
            }
            ColosseumWaveNpc npc = createWaveNpc(npcId, spawnLoc);
            // Set preferred standing direction (same offsets as spawn formation).
            // Defaults match for normal trio; Quartet extra archer needs the override.
            if (npc instanceof FremennikWarbandCombat fwc) {
                fwc.setPreferredOffset(fremDx, fremDy);
            }
            npc.spawn();
            // Fremennik stagger (Wiki + RSProx waves 3-5): berserker(+0) → seer(+1) → archer(+2), 6-tick cycle
            int staggerOffset = switch (npcId) {
                case NpcId.FREMENNIK_WARBAND_SEER -> 1;
                case NpcId.FREMENNIK_WARBAND_ARCHER -> 2;
                default -> 0;
            };
            npc.getCombat().setCombatDelay(WaveData.SPAWN_ATTACK_DELAY_TICKS + staggerOffset);
            npc.getCombat().setTarget(player);
            waveNpcs.add(npc);
        }
    }

    @Override
    public void process() {
        if (currentWave < 1 || currentWave > 11 || waveNpcs.isEmpty()) {
            return;
        }

        // Tick-based wave completion check: if every NPC in the list is dead or
        // finished, the wave is over.  This catches simultaneous AoE kills —
        // GlobalAreaManager.process() runs BEFORE WorldTasksManager.processTasks()
        // in the game loop, so dead NPCs whose onFinish hasn't fired yet still
        // sit in waveNpcs when this method runs.  Without this check, process()
        // would see a non-empty list and spawn reinforcements into it; those
        // reinforcements then survive the subsequent onFinish removals, leaving
        // the wave stuck.
        if (waveNpcs.stream().allMatch(n -> n.isDead() || n.isFinished())) {
            waveNpcs.clear();
            completeWave();
            return;
        }

        // Handle reinforcement spawning
        if (!reinforcementsSpawned) {
            long elapsed = WorldThread.getCurrentCycle() - waveStartTick;
            if (elapsed >= WaveData.REINFORCEMENT_DELAY_TICKS) {
                spawnReinforcements();
                reinforcementsSpawned = true;
            }
        }
    }

    private void spawnReinforcements() {
        java.util.List<Integer> reinforcements = WaveData.getReinforcementNpcs(currentWave, activeModifierBitmask);
        if (reinforcements.isEmpty()) {
            return;
        }

        // Determine gate: north or south based on player position relative to arena centre
        int gateY = player.getY() > getY(3107) ? 3123 : 3090;
        int gateX = 1824; // Centre of gate

        for (int npcId : reinforcements) {
            Location spawnLoc = getLocation(gateX + Utils.random(-2, 2), gateY);
            ColosseumWaveNpc npc = createWaveNpc(npcId, spawnLoc);
            npc.spawn();
            npc.getCombat().setTarget(player);
            waveNpcs.add(npc);
        }
    }

    public void onWaveNpcDeath(ColosseumWaveNpc npc) {
        if (waveNpcs.remove(npc) && waveNpcs.isEmpty()) {
            completeWave();
        }
    }

    /** Live wave NPCs (read-only). Used by warbanders to see siblings' claimed tiles. */
    java.util.List<ColosseumWaveNpc> getWaveNpcs() {
        return java.util.Collections.unmodifiableList(waveNpcs);
    }

    private void completeWave() {
        long durationTicks = WorldThread.getCurrentCycle() - waveStartTick;
        double durationSeconds = durationTicks * 0.6;
        int minutes = (int) (durationSeconds / 60);
        double seconds = durationSeconds % 60;
        String formatted = minutes > 0
                ? String.format("%d:%05.2f", minutes, seconds)
                : String.format("0:%05.2f", seconds);

        player.sendMessage("Wave " + currentWave + " completed! Duration: " + Colour.RED.wrap(formatted));

        // ── Roll loot for this wave and pre-roll next wave ──
        Item thisWaveLoot = ColosseumWaveLoot.roll(player, currentWave);

        // Add to cumulative rewards (inv 843)
        rewards.add(thisWaveLoot);

        // Set "previous" container (inv 845) — what the player just earned
        rewardsPrevious.clear();
        rewardsPrevious.add(new Item(thisWaveLoot));

        // Set "future" container (inv 844) — preview of next wave
        rewardsFuture.clear();
        if (currentWave < 11) {
            Item nextWaveLoot = ColosseumWaveLoot.roll(player, currentWave + 1);
            rewardsFuture.add(nextWaveLoot);
        } else if (currentWave == 11) {
            // Wave 12 preview: guaranteed Dizana's quiver (RSProx-verified)
            rewardsFuture.add(new Item(ItemId.DIZANAS_QUIVER_UNCHARGED, 1));
        }
        // After wave 12 there is no next wave — rewardsFuture stays empty

        // Calculate GP values for the intermission UI (CS2 4931 args)
        currentWaveLootGp = (int) ColosseumWaveLoot.gpValue(thisWaveLoot);
        nextWaveLootGp = rewardsFuture.isEmpty() ? 0 : (int) ColosseumWaveLoot.gpValue(rewardsFuture.get(0));
        totalLootGp = (int) rewards.calculateValue();

        // Send inventory updates (RSProx: update_inv_partial on wave complete)
        player.getPacketDispatcher().sendUpdateItemContainer(rewards);
        player.getPacketDispatcher().sendUpdateItemContainer(rewardsFuture);
        player.getPacketDispatcher().sendUpdateItemContainer(rewardsPrevious);

        // Respawn Minimus one tile north of the player (RSProx-verified all 11 waves).
        // Try north first, fall back to other cardinals if the tile is blocked (pillars).
        Location spawnLoc = new Location(player.getX(), player.getY() + 1, player.getPlane());
        if (!World.isFloorFree(spawnLoc)) {
            int[][] fallbacks = {{0, -1}, {1, 0}, {-1, 0}};
            for (int[] off : fallbacks) {
                Location candidate = new Location(player.getX() + off[0], player.getY() + off[1], player.getPlane());
                if (World.isFloorFree(candidate)) {
                    spawnLoc = candidate;
                    break;
                }
            }
        }
        minimusInside = new NPC(NpcId.MINIMUS_12808, spawnLoc, Direction.SOUTH, 0);
        minimusInside.spawn();

        // Player clicks Start-wave on Minimus to continue (opnpc1 → openIntermission)
    }

    public void startSolFight() {
        currentWave = 12;

        // Spawn border — only present during Sol Heredit fight
        createBorder();

        // Despawn seated Sol
        if (solSeated != null) {
            solSeated.finish();
            solSeated = null;
        }

        // First-time players get the Sol jump-down cutscene (varbit 9809 = colosseum_boss_cutscene_seen)
        if (player.getNotificationSettings().getKillcount(SolHeredit.TIMER_NAME) == 0) {
            new FadeScreen(player).fade(4, false);
            WorldTasksManager.schedule(() -> player.getCutsceneManager().play(new ColosseumCutscene(this)), 1);
            return;
        }

        beginSolCombat();
    }

    /** Shared Sol combat setup — called directly for returning players, or by the cutscene for first-timers. */
    void beginSolCombat() {

        Location spawnLocation = getBaseLocation(33, 31);
        player.setLocation(spawnLocation);

        solHeredit = new SolHeredit(getLocation(1823, 3108), this);
        solHeredit.lock();
        solHeredit.spawn();
        solHeredit.lock(3);
        solHeredit.setAnimation(SolHeredit.JUMP_DOWN_ANIMATION);
        solHeredit.setGraphics(SolHeredit.JUMP_DOWN_GFX);

        ColosseumStatistics.statistics.incrementGlobalAttempts();
        player.incrementNumericAttribute(ATTEMPTS_ATTRIBUTE, 1);

        player.getBossTimer().startTracking(SolHeredit.TIMER_NAME);
        player.getHpHud().open(solHeredit.getId(), solHeredit.getMaxHitpoints());

        player.sendMessage(Colour.RED.wrap("Sol Heredit jumps down from his seat..."));
    }

    private int[] pickModifiers() {
        // Placeholder: offer 3 random modifiers (0-13) that aren't already active
        java.util.List<Integer> available = new java.util.ArrayList<>();
        for (int i = 0; i <= 13; i++) {
            if ((activeModifierBitmask & (1 << i)) == 0) {
                available.add(i);
            }
        }
        java.util.Collections.shuffle(available);
        return new int[]{
                available.size() > 0 ? available.get(0) : 0,
                available.size() > 1 ? available.get(1) : 1,
                available.size() > 2 ? available.get(2) : 2,
        };
    }

    /**
     * Check if a spawn point is within the exclusion zone of the player.
     * Uses Chebyshev distance from the player to the spawn point's SW corner coordinate,
     * ignoring NPC size. RSProx W11 confirms this: a size-3 Manticore spawned at
     * (1824, 3099) with the player at (1824, 3104) — edge-to-edge distance was 3
     * (would be excluded), but SW corner distance was 5 (correctly allowed).
     */
    private boolean isWithinExclusionZone(Location player, Location spawnSW, int npcSize, int range) {
        int dist = Math.max(Math.abs(player.getX() - spawnSW.getX()), Math.abs(player.getY() - spawnSW.getY()));
        return dist <= range;
    }

    public int getCurrentWave() {
        return currentWave;
    }

}