package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.SkipPluginScan;
import org.jesse.game.content.boss.giantmole.GiantMoleInstance;

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
