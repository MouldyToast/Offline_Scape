package org.jesse.game.world.region.area.godwars;

import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;

public class ArmadylChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{2824, 5309}, {2824, 5296}, {2843, 5296}, {2843, 5309}}, 2)};
    }

    @Override
    public Location gravestoneLocation() {
        return new ImmutableLocation(2830, 5288, 2);
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Armadyl Chamber";
    }

}
