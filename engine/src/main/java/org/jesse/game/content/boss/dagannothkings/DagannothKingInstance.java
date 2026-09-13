package org.jesse.game.content.boss.dagannothkings;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Position;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.spawns.NPCSpawn;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.DynamicArea;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.RegionArea;
import org.jesse.game.world.region.area.plugins.*;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.logger.NearRealityLogger;
import org.jesse.plugins.events.ClanLeaveEvent;
import org.slf4j.Logger;

public class DagannothKingInstance extends DynamicArea implements CannonRestrictionPlugin, DeathPlugin, LogoutRestrictionPlugin, DropPlugin, LootBroadcastPlugin {
    private static final Logger log = NearRealityLogger.getLogger(DagannothKingInstance.class);

    public boolean solo;

    public DagannothKingInstance(final String username, AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, boolean solo) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
        this.username = username;
        this.solo = solo;
    }

    private final String username;
    private static final NPCSpawn[] spawns = new NPCSpawn[] {
            new NPCSpawn(5948, 2903, 4435, 0, Direction.SOUTH, 2),
            new NPCSpawn(5948, 2903, 4435, 0, Direction.SOUTH, 4),
            new NPCSpawn(5962, 2898, 4439, 0, Direction.SOUTH, 3),
            new NPCSpawn(5962, 2898, 4446, 0, Direction.SOUTH, 4),
            new NPCSpawn(5948, 2897, 4446, 0, Direction.SOUTH, 2),
            new NPCSpawn(5948, 2897, 4455, 0, Direction.SOUTH, 2),
            new NPCSpawn(5946, 2903, 4464, 0, Direction.SOUTH, 4),
            new NPCSpawn(2265, 2905, 4443, 0, Direction.SOUTH, 3),
            new NPCSpawn(5962, 2909, 4464, 0, Direction.SOUTH, 5),
            new NPCSpawn(5948, 2917, 4433, 0, Direction.SOUTH, 3),
            new NPCSpawn(5962, 2919, 4436, 0, Direction.SOUTH, 8),
            new NPCSpawn(2266, 2913, 4456, 0, Direction.SOUTH, 3),
            new NPCSpawn(2267, 2920, 4445, 0, Direction.SOUTH, 3),
            new NPCSpawn(5962, 2922, 4461, 0, Direction.SOUTH, 4),
            new NPCSpawn(5946, 2927, 4457, 0, Direction.SOUTH, 2),
            new NPCSpawn(5946, 2923, 4464, 0, Direction.SOUTH, 2),
            new NPCSpawn(5948, 2920, 4464, 0, Direction.SOUTH, 2),
            new NPCSpawn(5962, 2928, 4439, 0, Direction.SOUTH, 2),
            new NPCSpawn(5962, 2930, 4441, 0, Direction.SOUTH, 2),
            new NPCSpawn(5946, 2928, 4453, 0, Direction.SOUTH, 2),
    };

    @Override
    public void constructed() {
        for (final NPCSpawn spawn : spawns) {
            World.spawnNPC(spawn, spawn.getId(), getLocation(spawn.getX(), spawn.getY(), spawn.getZ()), spawn.getDirection(), spawn.getRadius());
        }
        DagannothKingsInstanceManager.getManager().addInstance(username, this);
    }

    @Override
    public void destroyRegion() {
        if (isDestroyed() || !players.isEmpty()) {
            return;
        }
        try {
            DagannothKingsInstanceManager.getManager().removeInstance(username, this);
        } catch (Exception e) {
            log.error("", e);
        }
        setDestroyed(true);
        GlobalAreaManager.remove(this);
        MapBuilder.destroy(area);
    }

    @Override
    public Location onLoginLocation() {
        return new Location(1912, 4367, 0);
    }

    @Override
    public void enter(Player player) {
        player.setForceMultiArea(true);
        player.setViewDistance(Player.SCENE_DIAMETER);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.setForceMultiArea(false);
        player.resetViewDistance();
    }

    @Override
    public String name() {
        return username + "'s clan Dagannoth Kings instance";
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public Location gravestoneLocation() {
        return onLoginLocation();
    }

    @Override
    public boolean manualLogout(final Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Are you sure you wish to log out?<br>If there are no players left, " + Colour.RED.wrap("the instance will be destroyed") + ".", new DialogueOption("Yes", () -> player.logout(false)), new DialogueOption("No."));
            }
        });
        return false;
    }

    @Subscribe
    public static void onClanLeave(final ClanLeaveEvent event) {
        final Player player = event.getPlayer();
        if (!(player.getArea() instanceof DagannothKingInstance)) {
            return;
        }
        player.sendMessage(Colour.RED.wrap("As you are no longer part of the clan, you will be removed from the instance within the next 3-10 seconds."));
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (++ticks >= 100 || player.isNulled() || player.isFinished()) {
                    stop();
                    return;
                }
                if (player.isDead() || player.isLocked()) {
                    return;
                }
                final RegionArea area = player.getArea();
                if (!(area instanceof DagannothKingInstance)) {
                    stop();
                    return;
                }
                player.lock(1);
                player.stopAll();
                player.sendMessage(Colour.RED.wrap("You have been removed from the instance."));
                player.blockIncomingHits();
                player.setLocation(new Location(((DagannothKingInstance) area).onLoginLocation()));
                stop();
            }
        }, Utils.random(5, 15), 0);
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }
}
