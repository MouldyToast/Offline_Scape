package com.zenyte.game.world.region.area;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

public class RDIArea extends PolygonRegionArea implements CannonRestrictionPlugin {
    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "RDI Areas";
    }

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new Location(2884, 5380), new Location(2941, 5500)),
        };
    }

    @Override
    public boolean inside(Location location) {
        return super.inside(location);
    }

    @Override
    public Location getRandomPosition() {
        return null;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }


}
