package org.jesse.game.content.colosseum;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.BossTimer;
import org.jesse.game.world.entity.player.Player;

@SuppressWarnings("unused")
public class ColosseumScoreboardInterface extends Interface {

	@Override
	public void open(Player player) {
		super.open(player);

		// We don't have waves so lets change it to attempts
		player.getPacketDispatcher().sendComponentText(getInterface(), 9, "Sol Heredit Attempts");
		player.getPacketDispatcher().sendComponentText(getInterface(), 17, "Global Sol Heredit Attempts");

		player.getPacketDispatcher().sendComponentText(getInterface(), 10, player.getNumericAttribute(ColosseumInstance.ATTEMPTS_ATTRIBUTE));
		player.getPacketDispatcher().sendComponentText(getInterface(), 12, player.getNotificationSettings().getKillcount(SolHeredit.TIMER_NAME));
		player.getPacketDispatcher().sendComponentText(getInterface(), 14, player.getNumericAttribute(ColosseumInstance.DEATHS_ATTRIBUTE));
		player.getPacketDispatcher().sendComponentText(getInterface(), 16, player.getBossTimer().personalBest(SolHeredit.TIMER_NAME));

		player.getPacketDispatcher().sendComponentText(getInterface(), 18, Long.toString(ColosseumStatistics.statistics.globalAttempts));
		player.getPacketDispatcher().sendComponentText(getInterface(), 20, Long.toString(ColosseumStatistics.statistics.getGlobalKillCount()));
		player.getPacketDispatcher().sendComponentText(getInterface(), 22, Long.toString(ColosseumStatistics.statistics.getGlobalDeathCount()));
		player.getPacketDispatcher().sendComponentText(getInterface(), 24, BossTimer.formatBestTime(ColosseumStatistics.statistics.getGlobalBestKillTimeSeconds()));
	}

	@Override
	protected void attach() {

	}

	@Override
	protected void build() {

	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.COLOSSEUM_SCOREBOARD;
	}

}
