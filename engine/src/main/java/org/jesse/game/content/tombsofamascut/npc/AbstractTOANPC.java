package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;

public abstract class AbstractTOANPC extends NPC {
    public AbstractTOANPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }
}
