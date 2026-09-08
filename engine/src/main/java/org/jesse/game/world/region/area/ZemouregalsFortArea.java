package org.jesse.game.world.region.area;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin;

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