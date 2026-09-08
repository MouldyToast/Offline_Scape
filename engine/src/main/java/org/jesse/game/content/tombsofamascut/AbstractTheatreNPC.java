package org.jesse.game.content.tombsofamascut;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;

public abstract class AbstractTheatreNPC extends NPC {
    public AbstractTheatreNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }
}
