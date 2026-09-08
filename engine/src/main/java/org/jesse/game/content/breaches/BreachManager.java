package org.jesse.game.content.breaches;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.content.breaches.entity.impl.*;
import org.jesse.game.model.HintArrow;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.broadcasts.BroadcastType;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.utils.TimeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BreachManager {

    private static BreachManager instance;
    private final BreachLocations breachLocation;
    private final List<BreachEntity> breachEntities;
    private int bossQuantity;
    private final WorldObject breachMainPortal;

    /*
    Breach instance getter
     */
    public static BreachManager getInstance() {
        return BreachManager.instance;
    }

    /*
    Initializes the breach manager, spawns NPCs and broadcasts
     */
    public BreachManager(BreachLocations breach) {
        this.breachLocation = breach;
        this.breachEntities = new ArrayList<>();
        this.bossQuantity = 0;
        breachMainPortal = new WorldObject(BreachSettings.BREACH_PORTAL_OBJECT_ID, WorldObject.DEFAULT_TYPE, WorldObject.DEFAULT_ROTATION, breach.teleportLocation);
        World.spawnObject(breachMainPortal);
        spawnRandomBoss(60); // spawn first boss after 1 minute
        spawnRandomBoss(120); // spawn second boss after 2 minutes
        spawnRandomBoss(180); // spawn third boss after 3 minutes
        WorldBroadcasts.broadcast(
            null, BroadcastType.BREACHES,
            "A breach has spawned %s! Powerful monsters now roam the area (%s)."
                .formatted(breachLocation.broadcastLocation, breachLocation.multi ? "Multi" : "Single"));
    }

    /*
    Spawns a random boss
    Checks boss spawned quantity for breach instance
     */
    public void spawnRandomBoss(int delay) {
        delay = (int) TimeUnit.SECONDS.toTicks(delay);
        if (this.bossQuantity >= BreachSettings.BOSS_QUANTITY) {
            closeBreach(false);
            return; // If we've spawned all the bosses we can for this breach
        }
        if (breachEntities.size() >= BreachSettings.BOSS_AMOUNT) return; // If there's already 3 bosses within the breach
        /*
        Schedule a boss to spawn with the desired delay
         */
        Location spawnLoc = createRandomSpawnLocation(breachLocation.teleportLocation);
        final WorldObject breachPortal = new WorldObject(BreachSettings.BREACH_SPAWN_BOSS_PORTAL_OBJECT_ID, WorldObject.DEFAULT_TYPE, WorldObject.DEFAULT_ROTATION, spawnLoc);
        final BreachManager breachManager = this;
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                if (!breachActive()) {
                    World.removeObject(breachPortal);
                    stop();
                    return;
                }
                World.spawnObject(breachPortal);
                int bossId = Utils.random(BreachSettings.BREACH_NPCS);
                var entity = getBossForId(bossId, spawnLoc);
                breachEntities.add(entity);
                entity.spawn();
                breachManager.bossQuantity++;
                stop();
            }
        }, delay, 0);
    }

    private BreachEntity getBossForId(int id, Location spawnLocation) {
        return switch (id) {
            case NpcId.GENERAL_GRAARDOR_12444 -> new GeneralGraardor(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.KRIL_TSUTSAROTH_12446 -> new KrilTsutsaroth(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.COMMANDER_ZILYANA_12445 -> new CommanderZilyana(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.KREEARRA_12443 -> new KreeArra(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.CAVE_ABOMINATION_12454 -> new CaveAbomination(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.DAGANNOTH_PRIME_12442,
                 NpcId.DAGANNOTH_SUPREME_12441,
                 NpcId.DAGANNOTH_REX_12439 -> new DagannothKing(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.DERWEN_12450 -> new Derwen(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.DHAROK_THE_WRETCHED_12447 -> new DharokTheWretched(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.GREATER_ABYSSAL_DEMON_12451 -> new GreaterAbyssalDemon(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.JALIMKOT_12455 -> new JalImKot(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.JUSTICIAR_ZACHARIAH_12449 -> new JusticiarZachariah(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.KING_BLACK_DRAGON_12440 -> new KingBlackDragon(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.MALEVOLENT_MAGE_12456 -> new MalevolentMage(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.NIGHT_BEAST_12459 -> new NightBeast(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.VITREOUS_WARPED_JELLY_12457 -> new VitreousWarpedJelly(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.SULPHUR_LIZARD_12458 -> new SulphurLizard(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            case NpcId.DURIAL_321 -> new Durial321(id, spawnLocation, Direction.DEFAULT, BreachSettings.BOSS_RADIUS);
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    /*
    Create a random spawn location based on breach portal location
     */
    private Location createRandomSpawnLocation(Location portalLoc) {
        int x = portalLoc.getX();
        int y = portalLoc.getY();
        x -= Utils.random(BreachSettings.BOSS_RADIUS);
        y -= Utils.random(BreachSettings.BOSS_RADIUS);
        x += Utils.random(BreachSettings.BOSS_RADIUS);
        y += Utils.random(BreachSettings.BOSS_RADIUS);
        Location newLoc = new Location(x, y);
        /*
        So that the breach entity's portal can't 'overwrite' the main breach portal
         */
        if (x == portalLoc.getX() && y == portalLoc.getY())
            return createRandomSpawnLocation(portalLoc);
        /*
        If within 4 tiles of the main breach portal
         */
        if (newLoc.withinDistance(portalLoc, 4))
            return createRandomSpawnLocation(portalLoc);
        return newLoc;
    }

    /*
    Processes players near/in breach
    -Add hint arrow
     */
    public static void processPlayer(Player player) {
        if (BreachManager.instance == null) return;
        Location loc = player.getLocation();
        Location breachLoc = BreachManager.instance.breachLocation.teleportLocation;
        if (withinBreach(loc))
            PlayerAttributesKt.setHasBreachesHintArrow(player, true);
        if (PlayerAttributesKt.getHasBreachesHintArrow(player))
            player.getPacketDispatcher().sendHintArrow(new HintArrow(breachLoc.getX() + 1, breachLoc.getY() + 1, (byte) 100));
    }

    private static boolean withinBreach(Location loc) {
        Location breachLoc = BreachManager.instance.breachLocation.teleportLocation;
        return loc.withinDistance(breachLoc, 40);
    }

    public void closeBreach(boolean finishEntities) {
        // remove bosses
        if (finishEntities)
            for (var entity : breachEntities) {
                if (entity == null || entity.isFinished()) continue;
                entity.finish();
                entity.remove();
            }
        // remove hint arrows from anyone with the breach arrow
        World.getPlayers().stream()
            .filter(Objects::nonNull)
                .forEach(player -> {
                    if (PlayerAttributesKt.getHasBreachesHintArrow(player)) {
                        player.getPacketDispatcher().resetHintArrow();
                        PlayerAttributesKt.setHasBreachesHintArrow(player, false);
                    }
                });
        // Remove portal
        World.removeObject(breachMainPortal);
        // announce it's over... for now
        WorldBroadcasts.broadcast(null,
            BroadcastType.BREACHES,
            "The breach at %s has finished! The portal has despawned, for now..."
            .formatted(BreachManager.getInstance().breachLocation.broadcastLocation));
        // nullify this instance
        BreachManager.instance = null;
    }

    public static boolean breachActive() {
        return BreachManager.instance != null;
    }

    /*
    Creates a new breach
     */
    public static void createNewBreach() {
        if (BreachManager.instance != null) {
            /*
            Close the current breach before creating a new one
             */
            BreachManager.instance.closeBreach(true);
            return;
        }
            /*
            Get a random breach and initialize it
             */
        BreachManager.instance = new BreachManager(Utils.random(BreachLocations.values()));
    }

    public List<BreachEntity> getBreachEntities() {
        return instance.breachEntities;
    }

    public BreachLocations getBreachLocation() {
        return breachLocation;
    }

    public int getSpawnedBosses() {
        return this.bossQuantity;
    }

}
