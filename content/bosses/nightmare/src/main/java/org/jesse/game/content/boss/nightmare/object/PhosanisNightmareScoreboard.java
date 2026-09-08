package org.jesse.game.content.boss.nightmare.object;

import org.jesse.game.GameInterface;
import org.jesse.game.content.boss.nightmare.NightmareGlobal;
import org.jesse.game.world.entity.player.BossTimer;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class PhosanisNightmareScoreboard implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		GameInterface.NIGHTMARE_STATISTICS.open(player);
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 5, "Phosani's Nightmare Statistics");
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 8, player.getNotificationSettings().getKillcount("Phosani's Nightmare"));
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 10, player.getNumericAttribute("phosanis_nightmare_death"));
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 12, player.getBossTimer().personalBest("Phosani's Nightmare"));

		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 14, NightmareGlobal.phosanisStatistics.getGlobalKillCount());
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 16, NightmareGlobal.phosanisStatistics.getGlobalDeathCount());
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 18, BossTimer.formatBestTime(NightmareGlobal.phosanisStatistics.getGlobalBestKillTimeSeconds()));
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.SCOREBOARD_29708};
	}

}
