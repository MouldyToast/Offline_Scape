package org.jesse.game.content.minigame.motherlode;

import org.jesse.game.content.skills.mining.OreDefinitions;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 29/06/2019 16:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OreVein extends WorldObject {
    OreVein(final WorldObject parent) {
        super(parent.getId() - 4, parent.getType(), parent.getRotation(), parent.getX(), parent.getY(), parent.getPlane());
    }

    private WorldTask task;

    public void start() {
        if (task != null || !UpperMotherlodeArea.polygon.contains(this)) {
            return;
        }
        task = () -> {
            if (!World.exists(this)) {
                cancel();
                return;
            }
            final int emptyId = getId() + 4;
            final WorldObject empty = new WorldObject(emptyId, getType(), getRotation(), getX(), getY(), getPlane());
            World.spawnObject(empty);
            WorldTasksManager.schedule(() -> {
                if (!World.exists(empty)) {
                    cancel();
                    return;
                }
                World.spawnObject(this);
                cancel();
            }, OreDefinitions.PAYDIRT.getTime());
        };
        WorldTasksManager.schedule(task, Utils.random(25, 45));
    }

    private final void cancel() {
        task = null;
    }
}
