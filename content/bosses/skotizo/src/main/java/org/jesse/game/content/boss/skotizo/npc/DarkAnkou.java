package org.jesse.game.content.boss.skotizo.npc;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;

/**
 * @author Tommeh | 07/03/2020 | 11:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DarkAnkou extends NPC {

    private final Skotizo skotizo;

    public DarkAnkou(final Location location, final Skotizo skotizo) {
        super(NpcId.DARK_ANKOU, location, Direction.SOUTH, 128);
        this.skotizo = skotizo;
        this.maxDistance = 64;
        this.aggressionDistance = 3;
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
