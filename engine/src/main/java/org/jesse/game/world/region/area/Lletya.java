package org.jesse.game.world.region.area;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Lletya extends Tirannwn {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2315, 3198 },
                        { 2315, 3148 },
                        { 2364, 3148 },
                        { 2364, 3198 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
    }

    @Override
    public String name() {
        return "Lletya";
    }
}
