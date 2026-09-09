package org.jesse.game.world.region.area;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;
import org.jesse.game.world.region.area.plugins.RandomEventRestrictionPlugin;

/**
 * @author Kris | 15/04/2019 17:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CorporealBeastCavern extends PolygonRegionArea implements RandomEventRestrictionPlugin, LootBroadcastPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2944, 4416 },
                        { 2944, 4352 },
                        { 3008, 4352 },
                        { 3008, 4416 }
                }, 2)
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
        return "Corporeal Beast Cavern";
    }
}
