package org.jesse.game.content.colosseum;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;

/**
 * Wave NPC for the Fortis Colosseum. Holds a reference to the instance
 * and reports death via {@link ColosseumInstance#onWaveNpcDeath(ColosseumWaveNpc)}.
 * <p>
 * Pattern taken from {@link org.jesse.game.content.minigame.inferno.npc.InfernoNPC}.
 */
public class ColosseumWaveNpc extends NPC {

    private final ColosseumInstance instance;

    public ColosseumWaveNpc(int id, Location tile, ColosseumInstance instance) {
        super(id, tile, Direction.SOUTH, 0);
        this.instance = instance;
        setMaxDistance(64);
        setAggressionDistance(64);
        setForceAggressive(true);
        supplyCache = false;
        randomWalkDelay = Integer.MAX_VALUE >> 1;
    }

    @Override
    public void setRespawnTask() {
        // Wave NPCs do not respawn — they are dynamically spawned per wave.
    }

    @Override
    protected void drop(final Location tile) {
        // Colosseum wave NPCs do not drop items on the ground.
        // All rewards come from ColosseumWaveLoot via the wave completion system.
    }

    @Override
    protected void onFinish(final Entity source) {
        super.onFinish(source);
        instance.onWaveNpcDeath(this);
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    public ColosseumInstance getInstance() {
        return instance;
    }
}