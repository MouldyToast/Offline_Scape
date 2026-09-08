package org.jesse.plugins.itemonnpc;

import org.jesse.game.content.follower.Follower;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.ItemChat;

import java.util.Objects;

/**
 * @author Savions.
 */
public class ChargedIceMuphin implements ItemOnNPCAction {

	@Override public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
		if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
			player.sendMessage("This is not your pet.");
			return;
		}
		if (player.getNumericAttribute("Muspah charged ice").intValue() > 0) {
			player.sendMessage("You've already used charged ice on your muphin pet. There's no need to do that any more.");
			return;
		}
		player.getDialogueManager().start(new ItemChat(player, item, "Congratulations! You've unlocked new metamorphosis options for your pet."));
		player.addAttribute("Muspah charged ice", 1);
		player.getInventory().deleteItem(item);
	}

	@Override public Object[] getItems() {
		return new Object[] {ItemId.CHARGED_ICE};
	}

	@Override public Object[] getObjects() {
		return new Object[] {NpcId.MUPHIN};
	}
}
