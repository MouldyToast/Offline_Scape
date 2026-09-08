package org.jesse.game.content.skills.agility.pyramid.area;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.plugins.events.ServerLaunchEvent;

import java.util.EnumMap;
import java.util.Map;

public enum MovingBlock {
    FIRST_LEVEL_BLOCK(5788, 10872, new ImmutableLocation(3372, 2847, 1), Direction.EAST), THIRD_LEVEL_BLOCK(5788, 10873, new ImmutableLocation(3366, 2845, 3), Direction.NORTH);
    public static final MovingBlock[] values = values();
    private final int npcId;
    private final int objectId;
    private final Location spawn;
    private final Direction direction;
    private static final EnumMap<MovingBlock, MovingBlockNPC> map = new EnumMap<>(MovingBlock.class);

    @Subscribe
    public static void onServerLaunch(final ServerLaunchEvent event) {
        for (final MovingBlock block : values()) {
            final MovingBlockNPC npc = new MovingBlockNPC(block.npcId, block.spawn, block.direction, 0);
            map.put(block, npc);
        }

        WorldTasksManager.scheduleCreation(() -> {
            for (final MovingBlockNPC npc : map.values()) {
                npc.spawn();
            }
        });
    }

    static void moveBlocks() {
        for (final Map.Entry<MovingBlock, MovingBlockNPC> entry : map.entrySet()) {
            final MovingBlock key = entry.getKey();
            final MovingBlockNPC value = entry.getValue();
            value.slide(key.direction);
            World.sendSoundEffect(key.spawn, new SoundEffect(1395, 5));
            WorldTasksManager.schedule(() -> value.slide(key.direction.getCounterClockwiseDirection(4)), 6);
        }
    }

    MovingBlock(int npcId, int objectId, Location spawn, Direction direction) {
        this.npcId = npcId;
        this.objectId = objectId;
        this.spawn = spawn;
        this.direction = direction;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getObjectId() {
        return objectId;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Direction getDirection() {
        return direction;
    }
}
