package org.jesse.game.content.event.christmas2019.cutscenes;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;

/**
 * @author Kris | 18/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class CutsceneImp extends NPC {

    public CutsceneImp(Location tile) {
        super(15001, tile, Direction.SOUTH, 0);
        this.randomWalkDelay = Integer.MAX_VALUE >> 1;
    }

}
