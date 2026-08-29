package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.object.memberzones.GiantMoleInstance;

@SkipPluginScan
public final class GiantMoleInstanced extends GiantMole implements Spawnable {

    public GiantMoleInstanced(int id, Location tile, Direction facing, int radius, GiantMoleInstance giantMoleInstance) {
        super(id, tile, facing, radius);
        resurfaceLocations.clear();
        GiantMole.RESURFACE_LOCATIONS.stream()
            .map(giantMoleInstance::getLocation)
            .forEach(resurfaceLocations::add);
    }
}
