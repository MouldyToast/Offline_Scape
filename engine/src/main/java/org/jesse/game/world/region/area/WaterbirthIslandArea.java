package org.jesse.game.world.region.area;

import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author Kris | 29. mai 2018 : 00:49:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WaterbirthIslandArea extends PolygonRegionArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][]{
                { 2496, 3776 },
                { 2496, 3712 },
                { 2560, 3712 },
                { 2560, 3776 }
        }) };
	}

	@Override
	public void enter(final Player player) {
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 167);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
	}

	@Override
	public String name() {
		return "Waterbirth Island";
	}

}
