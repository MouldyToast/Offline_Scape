package org.jesse.game.content.boss.nightmare.object;

import org.jesse.game.GameInterface;
import org.jesse.game.content.boss.nightmare.NightmareGlobal;
import org.jesse.game.world.entity.player.BossTimer;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class NightmareScoreboard implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		GameInterface.NIGHTMARE_STATISTICS.open(player);
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 8, player.getNotificationSettings().getKillcount("The Nightmare"));
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 10, player.getNumericAttribute("nightmare_death"));
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 12, player.getBossTimer().personalBest("The Nightmare"));

		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 14, NightmareGlobal.statistics.getGlobalKillCount());
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 16, NightmareGlobal.statistics.getGlobalDeathCount());
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 18, BossTimer.formatBestTime(NightmareGlobal.statistics.getGlobalBestKillTimeSeconds()));

	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.SCOREBOARD_37949};
	}

}
