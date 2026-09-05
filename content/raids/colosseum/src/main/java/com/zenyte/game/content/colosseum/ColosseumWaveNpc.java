package com.zenyte.game.content.colosseum;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

/**
 * Wave NPC for the Fortis Colosseum. Holds a reference to the instance
 * and reports death via {@link ColosseumInstance#onWaveNpcDeath(ColosseumWaveNpc)}.
 * <p>
 * Pattern taken from {@link com.zenyte.game.content.minigame.inferno.npc.InfernoNPC}.
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