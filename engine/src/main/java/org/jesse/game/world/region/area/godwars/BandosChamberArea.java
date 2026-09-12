package org.jesse.game.world.region.area.godwars;

import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;

public class BandosChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2864, 5370 }, { 2864, 5351 }, { 2877, 5351 }, { 2877, 5370 } }, 2) };
    }

    @Override
    public Location gravestoneLocation() {
        return new ImmutableLocation(2857, 5363, 2);
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Bandos Chamber";
    }

}
