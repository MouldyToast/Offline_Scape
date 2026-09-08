package org.jesse.game.world.entity.npc.impl.misc;

import org.jesse.game.util.Direction;
import org.jesse.game.util.ProjectileUtils;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 27/01/2019 21:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Bird extends NPC implements Spawnable {
    public Bird(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    private final Location next = new Location(0);

    @Override
    public boolean validate(final int id, String name) {
        name = name.toLowerCase();
        return name.equals("gull") || name.equals("eagle") || name.equals("butterfly");
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    protected boolean canMove(final int lastX, final int lastY, final int dir) {
        final byte x = Utils.DIRECTION_DELTA_X[dir];
        final byte y = Utils.DIRECTION_DELTA_Y[dir];
        next.setLocation(lastX + x, lastY + y, getPlane());
        return !ProjectileUtils.isProjectileClipped(null, null, getLocation(), next, false);
    }
}
