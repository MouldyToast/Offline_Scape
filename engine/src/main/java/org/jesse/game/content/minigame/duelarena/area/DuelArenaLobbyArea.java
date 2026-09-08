package org.jesse.game.content.minigame.duelarena.area;

import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.DuelArenaArea;
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Tommeh | 30-11-2018 | 15:26
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DuelArenaLobbyArea extends DuelArenaArea implements CannonRestrictionPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 3361, 3264 }, { 3361, 3267 }, { 3355, 3267 }, { 3355, 3280 }, { 3380, 3280 }, { 3380, 3267 }, { 3374, 3267 }, { 3374, 3264 }}) };
    }

    @Override
    public String name() {
        return "Duel arena lobby";
    }
}
