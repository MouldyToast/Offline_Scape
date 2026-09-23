package org.jesse.plugins.dialogue;

import org.jesse.game.content.consumables.Consumable;
import org.jesse.game.item.Item;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.UpdateFlag;
import org.jesse.game.world.entity.player.LogLevel;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.runelite.api.ItemID;

import static org.jesse.game.item.ids.ItemId.*;

/**
 * @author Tommeh | 24 apr. 2018 | 17:23:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DestroyItemDialogue extends Dialogue {

	private final Item item;
	private final int slot;
	private final Runnable onYes;

	public DestroyItemDialogue(final Player player, final Item item, final int slot) {
		this(player, item, slot, null);
	}

	public DestroyItemDialogue(final Player player, final Item item, final int slot, final Runnable onYes) {
		super(player);
		this.item = item;
		this.slot = slot;
		this.onYes = onYes;
	}

	@Override
	public void buildDialogue() {
		destroyItem(item, getMessage(item)).onYes(() -> {
			if (onYes != null) {
				onYes.run();
			}

			final Inventory inventory = player.getInventory();
			final Item inSlot = inventory.getItem(slot);
			player.sendSound(2381);
			if (inSlot == item) {
				inventory.deleteItem(slot, item);
				player.log(LogLevel.INFO, "Destroying item '" + item + "'.");
			} else {
				for (final Int2ObjectMap.Entry<Item> entry : inventory.getContainer().getItems().int2ObjectEntrySet()) {
					if (entry.getValue() == item) {
						inventory.deleteItem(entry.getIntKey(), item);
						player.log(LogLevel.INFO, "Destroying item '" + item + "'.");
						break;
					}
				}
			}
			handle();
		});
	}

	private String getMessage(final Item item) {
		final int id = item.getId();
		return switch (id) {
			case COLLECTION_LOG -> "You can get another Collection log from the Collector in the Varrock Museum.";
			case DIZANAS_QUIVER_BROKEN, BLESSED_DIZANAS_QUIVER_BROKEN, DIZANAS_QUIVER_UNCHARGED,
				 DIZANAS_QUIVER_L_UNCHARGED, DIZANAS_QUIVER, DIZANAS_QUIVER_L, BLESSED_DIZANAS_QUIVER,
				 BLESSED_DIZANAS_QUIVER_L -> "You can get another from The Colosseum.";
			case SKELETON_BOOTS, SKELETON_GLOVES, SKELETON_LEGGINGS, SKELETON_MASK,
				 SKELETON_SHIRT -> "You can reclaim this item from Diango in Draynor Village.";
			default ->
					"Destroying is a permanent process. You will be able to reacquire the item where you obtained it in the first place.";
		};
	}

	private void handle() {
		switch (item.getId()) {
			case ItemID.LOOT_KEY, ItemID.LOOT_KEY_26652, ItemID.LOOT_KEY_26653, ItemID.LOOT_KEY_26654,
				 ItemID.LOOT_KEY_26655:
				player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
				break;
			case SEED_BOX:
			case OPEN_SEED_BOX:
				player.getSeedBox().clear();
				break;
			case HERB_SACK:
			case OPEN_HERB_SACK:
				player.getHerbSack().clear();
				break;
			case GEM_BAG_12020:
			case OPEN_GEM_BAG:
				player.getGemBag().clear();
				break;
			case RUNE_POUCH:
			case DIVINE_RUNE_POUCH:
				player.getRunePouch().clear();
				break;
			case 26306:
			case 26304:
				player.getBonePouch().clear();
				break;
			case LOOTING_BAG:
			case LOOTING_BAG_22586:
				final boolean inWilderness = WildernessArea.isWithinWilderness(player);
				if (inWilderness) {
					for (final Int2ObjectMap.Entry<Item> entry : player.getLootingBag().getContainer().getItems().int2ObjectEntrySet()) {
						final Item item = entry.getValue();
						final boolean consumable = item.getDefinitions().containsOption("Eat") || item.getDefinitions().containsOption("Drink") || Consumable.consumables.containsKey(item.getId());
						World.spawnFloorItem(item, player, !consumable && item.isTradable() ? -1 : 300, item.isTradable() ? 500 : -1);
					}
				}
				player.getLootingBag().clear();
				player.getLootingBag().setOpen(false);
				break;
			case DODGY_NECKLACE:
				player.addAttribute("dodgy necklace uses", 0);
				break;
			case AMULET_OF_BOUNTY:
				player.addAttribute("amulet of bounty uses", 0);
				break;
			case AMULET_OF_CHEMISTRY:
				player.addAttribute("amulet of chemistry uses", 0);
				break;
		}
	}
}
