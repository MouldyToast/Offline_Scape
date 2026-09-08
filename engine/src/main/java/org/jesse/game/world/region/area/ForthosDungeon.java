package org.jesse.game.world.region.area;

import org.jesse.game.world.Position;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;

public class ForthosDungeon extends PolygonRegionArea {

	@Override
	protected RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(7322), new RSPolygon(7323) };
	}

	@Override
	public void enter(Player player) {

	}

	@Override
	public void leave(Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Forthos Dungeon";
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

}
