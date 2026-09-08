package org.jesse.game.content.partyroom;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;

/**
 * @author Kris | 03/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyRoomDancingKnight extends NPC {
    public PartyRoomDancingKnight(final int index) {
        super(NpcId.KNIGHT_5793, new Location(3043 + index, 3378, 0), Direction.SOUTH, 0);
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        return false;
    }

    @Override
    public void processNPC() {

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
