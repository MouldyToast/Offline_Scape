package org.jesse.game.world.region.area;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Rimmington extends KingdomOfAsgarnia {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2911, 3230 },
                        { 2911, 3197 },
                        { 2944, 3195 },
                        { 2950, 3191 },
                        { 2971, 3185 },
                        { 2986, 3196 },
                        { 2985, 3224 },
                        { 2965, 3230 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
    }

    @Override
    public String name() {
        return "Rimmington";
    }
}
