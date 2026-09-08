package org.jesse.game.world.region.area;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author Kris | 21/04/2019 13:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Ungael extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2240, 4096 },
                        { 2240, 4032 },
                        { 2304, 4032 },
                        { 2304, 4096 }
                })
        };
    }

    @Override
    public void enter(final Player player) {

    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Ungael";
    }
}
