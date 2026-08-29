package com.zenyte.game.content.colosseum.items;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.testinterfaces.EquipmentTabInterface;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

@SuppressWarnings("unused")
public class DizanaQuiverInterface extends Interface {

	public static final String OPENED_ITEM_ATTRIBUTE = "opened_dizana_quiver_item";

	@Override
	public void open(Player player) {
		super.open(player);

		int charges = player.getNumericTemporaryAttribute(OPENED_ITEM_ATTRIBUTE).intValue();
		if (charges == 0) {
			player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("charges"), "Charges: None");
		} else {
			player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("charges"), "Charges: " + Utils.format(charges));
		}
	}

	@Override
	public void close(Player player, Optional<GameInterface> replacement) {
		super.close(player, replacement);

		player.getTemporaryAttributes().remove(OPENED_ITEM_ATTRIBUTE);
	}

	@Override
	protected void attach() {
		put(9, "charges");
		put(11, "ammo");
	}

	@Override
	protected void build() {
		bind("ammo", EquipmentTabInterface::handleDizanaQuiverRemoveOption);
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.DIZANAS_QUIVER;
	}

}
