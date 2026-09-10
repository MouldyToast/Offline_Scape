package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.PaneType;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.utils.TimeUnit;

import static org.jesse.game.GameInterface.EXTRA_JOURNAL_TAB;

public class ExtraQuestTabInterface extends Interface {

	@Override
	protected void attach() {

	}

	@Override
	public void open(Player player) {
		player.getInterfaceHandler().sendInterface(getInterface().getId(), 43, PaneType.JOURNAL_TAB_HEADER, true);
		update(player);
	}

	public static void update(Player player) {
	}
	@Override
	protected void build() {

	}

	@Override
	public GameInterface getInterface() {
		return EXTRA_JOURNAL_TAB;
	}

}
