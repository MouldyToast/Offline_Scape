package org.jesse.game.content.compcapes;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

@SuppressWarnings("unused")
public class CompSelectionInterface extends Interface {

	@Override
	public void open(Player player) {
		super.open(player);

		player.awaitInputIntNoClose(System.out::println);
	}

	@Override
	protected void attach() {

	}

	@Override
	protected void build() {

	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.COMP_SELECTION;
	}

}
