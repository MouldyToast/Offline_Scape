package org.jesse.plugins.item;

import org.jesse.game.content.skills.magic.spells.teleports.TeleportCollection;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CATierType;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Savions.
 */
public class GhommalHilt extends ItemPlugin {

	@Override public void handle() {
		bind("Trollheim", (player, item, container, slotId) -> teleportTrollheim(player));
		bind("Mor Ul Rek", (player, item, container, slotId) -> teleportMorUlRek(player));
		bind("Destinations", (player, item, container, slotId) -> {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override public void buildDialogue() {
					options("Select a destination", new DialogueOption("Trollheim"), new DialogueOption("Mor Ul Rek"))
							.onOptionOne(() -> teleportTrollheim(player)).onOptionTwo(() -> teleportMorUlRek(player));
				}
			});
		});
		bind("Dismantle", (player, item, container, slotId) -> {
			if (player.getInventory().getFreeSlots() < 1) {
				player.sendMessage("Not enough space in your inventory.");
				return;
			}
			player.getInventory().ifDeleteItem(item, () -> {
				player.getInventory().addItem(new Item(ItemId.AVERNIC_DEFENDER, 1));
				player.getInventory().addItem(new Item(item.getId() == ItemId.GHOMMALS_AVERNIC_DEFENDER_6 ? ItemId.GHOMMALS_HILT_6 : ItemId.GHOMMALS_HILT_5));
				player.sendMessage("You dismantle Ghommal's avernic defender and receive the components.");
			});
		});
	}

	private static void teleportMorUlRek(final Player player) {
		if (!player.getCombatAchievements().hasTierCompleted(CATierType.GRANDMASTER)) {
			final int charges = player.getVariables().getMorUikRekTeleports() + 1;
			final int limit = player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) ? 5 : 3;
			if (charges > limit) {
				player.sendMessage("You have used your daily charges for today.");
				return;
			}
			player.getVariables().setMorUikRekTeleports(charges);
			player.sendMessage(Colour.RED.wrap("You have used up " + charges + "/" + limit + " of your Mor Ul Rek teleports for today."));
		}
		TeleportCollection.CA_MORUIREK_TELEPORT.teleport(player);
	}

	private static void teleportTrollheim(final Player player) {
		if (!player.getCombatAchievements().hasTierCompleted(CATierType.HARD)) {
			final int charges = player.getVariables().getTrollheimTeleports() + 1;
			final int limit = player.getCombatAchievements().hasTierCompleted(CATierType.MEDIUM) ? 5 : 3;
			if (charges > limit) {
				player.sendMessage("You have used your daily charges for today.");
				return;
			}
			player.getVariables().setTrollheimTeleports(charges);
			player.sendMessage(Colour.RED.wrap("You have used up " + charges + "/" + limit + " of your Trollheim teleports for today."));
		}
		TeleportCollection.CA_TROLLHEIM_TELEPORT.teleport(player);
	}

	@Override public int[] getItems() {
		return new int[] {ItemId.GHOMMALS_HILT_1, ItemId.GHOMMALS_HILT_2, ItemId.GHOMMALS_HILT_3, ItemId.GHOMMALS_HILT_4, ItemId.GHOMMALS_HILT_5,
				ItemId.GHOMMALS_HILT_6, ItemId.GHOMMALS_AVERNIC_DEFENDER_5, ItemId.GHOMMALS_AVERNIC_DEFENDER_6};
	}
}
