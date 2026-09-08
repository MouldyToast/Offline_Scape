package org.jesse.game.world.region.area;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
public class IsleOfSoulsDungeon extends PolygonRegionArea {
    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(8593)};
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Isle of Souls Dungeon";
    }
}
