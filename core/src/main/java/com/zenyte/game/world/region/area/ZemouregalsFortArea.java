package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 2/13/2025
 */
public class ZemouregalsFortArea extends PolygonRegionArea implements CannonRestrictionPlugin {

    public RSPolygon[] polygons() {
        var swPoint = new Location(2752, 10240);
        var nePoint = new Location(2815, 10303);
        return new RSPolygon[]{new RSPolygon(swPoint, nePoint)};
    }

    @Override
    public void enter(Player player) {}

    @Override
    public void leave(Player player, boolean logout) {}

    public String name() {
        return "Zemouregals Fort";
    }
}