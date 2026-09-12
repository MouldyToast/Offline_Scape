package org.jesse.game.world.region.area.godwars;

import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;

public class ZamorakChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{2918, 5332}, {2918, 5318}, {2937, 5318}, {2937, 5332}}, 2)};
    }

    @Override
    public Location gravestoneLocation() {
        return new ImmutableLocation(2934, 5350, 2);
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Zamorak Chamber";
    }

}
