package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.serverevent.WorldBoost;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.PaneType;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.utils.TimeUnit;

import static org.jesse.game.GameInterface.SERVER_EVENTS;

public class ServerEventsInterface extends Interface {

	@Override
	protected void attach() {

	}

	@Override
	public void open(Player player) {
		player.getInterfaceHandler().sendInterface(getInterface().getId(), 43, PaneType.JOURNAL_TAB_HEADER, true);
		update(player);
	}

	public static void update(Player player) {
		StringBuilder sb = new StringBuilder();
		for (WorldBoost worldBoost : World.getWorldBoosts()) {
			long boostEnd = worldBoost.boostEnd();
			long hourTicks = TimeUnit.HOURS.toTicks(worldBoost.getDurationHours());
			long ticksleft = TimeUnit.MILLISECONDS.toTicks(boostEnd - System.currentTimeMillis());
			sb.append(worldBoost.getBoostType().getMssg());
			sb.append("|0|");
			sb.append(ticksleft);
			sb.append("|");
			sb.append(hourTicks);
			sb.append("|");
		}
		player.getPacketDispatcher().sendClientScript(10612, sb.toString());
	}
	@Override
	protected void build() {

	}

	@Override
	public GameInterface getInterface() {
		return SERVER_EVENTS;
	}

}
