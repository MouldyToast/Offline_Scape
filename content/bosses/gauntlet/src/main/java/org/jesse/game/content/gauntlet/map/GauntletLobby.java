package org.jesse.game.content.gauntlet.map;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;

public class GauntletLobby extends PolygonRegionArea implements LootBroadcastPlugin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[]{new RSPolygon(new int[][]{{3008, 6080},
				{3008, 6143},
				{3072, 6144},
				{3072, 6080}})};
	}

	@Override
	public void enter(Player player) {

	}

	@Override
	public void leave(Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Gauntlet Lobby";
	}
}
