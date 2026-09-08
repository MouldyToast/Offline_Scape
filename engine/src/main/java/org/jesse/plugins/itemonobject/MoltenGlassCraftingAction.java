package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.DoubleItemChat;
import org.jesse.plugins.dialogue.skills.MoltenGlassD;

/**
 * @author Tommeh | 23 dec. 2017 : 18:57:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class MoltenGlassCraftingAction implements ItemOnObjectAction {

	private static final Item SODA_ASH = new Item(1781);
	private static final Item BUCKET_OF_SAND = new Item(1783);

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		if ((item.getId() == SODA_ASH.getId() && !player.getInventory().containsItem(BUCKET_OF_SAND)) || (item.getId() == BUCKET_OF_SAND.getId() && !player.getInventory().containsItem(SODA_ASH))) {
			player.getDialogueManager().start(new DoubleItemChat(player, BUCKET_OF_SAND, SODA_ASH, "You need sand and soda ash to make glass."));
			return;
		}
		player.getDialogueManager().start(new MoltenGlassD(player));
	}

	@Override
	public Object[] getItems() {
		return new Object[] { SODA_ASH.getId(), BUCKET_OF_SAND.getId() };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Furnace" };
	}

}
