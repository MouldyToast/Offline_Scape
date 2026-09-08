package org.jesse.game.world.entity.npc.impl.wilderness;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 25/01/2019 16:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Ent extends NPC implements Spawnable {
    public Ent(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 6594 || id == 7234;
    }

    protected void onFinish(final Entity source) {
        final Trunk trunk = new Trunk(this, 9474, getLocation(), getSpawnDirection(), 0);
        trunk.spawn();
        drop(getMiddleLocation());
        reset();
        finish();
        if (source != null) {
            if (source instanceof Player) {
                final Player player = (Player) source;
                sendNotifications(player);
            }
        }
    }
}
