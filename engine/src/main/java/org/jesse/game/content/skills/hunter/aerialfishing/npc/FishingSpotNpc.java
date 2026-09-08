package org.jesse.game.content.skills.hunter.aerialfishing.npc;

import org.jesse.game.content.skills.hunter.aerialfishing.LakeMolchArea;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;

/**
 * @author Cresinkel
 */

public class FishingSpotNpc extends NPC {

    public FishingSpotNpc(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean isMovementRestricted() {
        return true;
    }

    @Override
    public NPC spawn() {
        NPC npc = super.spawn();
        WorldTasksManager.schedule(() -> {
            if (isDead() || isFinished()) {
                return;
            }
            LakeMolchArea.removeUsedSpawnLocation(getLocation());
            remove();
            LakeMolchArea.spawnSpot();
        }, Utils.random(10,30));
        return npc;
    }
}
