package org.jesse.game.content.boss.skotizo.npc;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;

/**
 * @author Tommeh | 07/03/2020 | 11:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ReanimatedDemon extends NPC {

    private final Skotizo skotizo;

    public ReanimatedDemon(final Location location, final Skotizo skotizo) {
        super(NpcId.REANIMATED_DEMON_SPAWN, location, Direction.SOUTH, 128);
        this.skotizo = skotizo;
        this.maxDistance = this.aggressionDistance = 64;
        this.forceAggressive = true;
        this.randomWalkDelay = 3;
        this.spawned = true;
        setForceMultiArea(true);
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
        skotizo.getMinions().remove(this);
    }
}
