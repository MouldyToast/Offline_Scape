package org.jesse.game.world.region.area.godwars;

import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;

public class SaradominChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{2889, 5276}, {2889, 5258}, {2908, 5258}, {2908, 5276}}, 0)};
    }

    @Override
    public Location gravestoneLocation() {
        return new ImmutableLocation(2910, 5265, 0);
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Saradomin Chamber";
    }

}
