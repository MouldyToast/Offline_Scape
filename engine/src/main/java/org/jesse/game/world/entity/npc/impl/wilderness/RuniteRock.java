package org.jesse.game.world.entity.npc.impl.wilderness;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;

/**
 * @author Tommeh | 8-2-2019 | 20:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RuniteRock extends NPC {

    public RuniteRock(int id, Location tile, boolean spawned) {
        super(id, tile, spawned);
    }

    @Override
    public NPC spawn() {
        WorldTasksManager.schedule(() -> {
            if (!isFinished()) {
                finish();
            }
        }, 99);
        return super.spawn();
    }

    @Override
    public void onFinish(final Entity source) { }
}
