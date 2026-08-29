package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

public class OGDonatorArea extends PolygonRegionArea implements CannonRestrictionPlugin {
    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(9369), new RSPolygon(9370)};
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "OGDI";
    }
}
