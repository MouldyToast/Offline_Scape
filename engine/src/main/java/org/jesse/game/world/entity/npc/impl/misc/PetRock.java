package org.jesse.game.world.entity.npc.impl.misc;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 27/01/2019 22:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PetRock extends NPC {

    public PetRock(final Player owner, final Location tile) {
        super(5983, tile, Direction.SOUTH, 0);
        this.owner = owner.getUsername();
    }

    private final String owner;

    @Override
    protected boolean isMovableEntity() {
        return false;
    }

    public String getOwner() {
        return owner;
    }
}
