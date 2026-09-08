package org.jesse.game.content.godwars.npcs;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 10/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DyingKnightNPC extends NPC implements Spawnable {

    public DyingKnightNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 10023;
    }

    @Override
    public void setInteractingWith(final Entity entity) {}

    @Override
    public void finishInteractingWith(final Entity entity) {}

    @Override
    public void setFaceEntity(final Entity entity) {}

    @Override
    public void setFaceLocation(final Location tile) {}
}
