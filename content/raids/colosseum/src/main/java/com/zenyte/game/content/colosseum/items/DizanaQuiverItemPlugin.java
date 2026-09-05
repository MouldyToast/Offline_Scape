package com.zenyte.game.content.colosseum.items;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.testinterfaces.EquipmentTabInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.DestroyItemDialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import static com.zenyte.game.item.ItemId.*;

@SuppressWarnings("unused")
public class DizanaQuiverItemPlugin extends ItemPlugin implements ItemOnItemAction, ChargeExtension {

	private static final int BLESSING_PERCENT_PER_CHARGE = 1_500;
	public static final int CHARGES_FOR_BLESS = BLESSING_PERCENT_PER_CHARGE * 100;
	private static final int MAX_CHARGES = 20_000;
	public static final String BLESSSING_CHARGE_ATTRIBUTE = "dizanas_quiver_blessing_charge";

	@Override
	public void handle() {
		bind("Open", (player, item, container, slotId) -> {
			player.addTemporaryAttribute(DizanaQuiverInterface.OPENED_ITEM_ATTRIBUTE, item.getCharges());
			GameInterface.DIZANAS_QUIVER.open(player);
		});
		bind("Empty", (player, item, container, slotId) -> EquipmentTabInterface.handleDizanaQuiverRemoveOption(player));
		bind("Uncharge", (player, item, container, slotId) -> {
			int itemId = item.getId();
			int charges = item.getCharges();
			if (charges <= 0) {
				item.setId(chargedToUncharged(item));
				player.getInventory().refresh(slotId);
			} else {
				String chargeText = Utils.format(charges);
				int blessingCharge = item.getNumericAttribute(BLESSSING_CHARGE_ATTRIBUTE).intValue() / BLESSING_PERCENT_PER_CHARGE;
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						if (blessingCharge > 0) {
							options("Uncharging will lose you " + blessingCharge + "% progress towards blessing this quiver. Do you wish to proceed?", "No.", "Yes.").onOptionTwo(() -> {
								if (!player.getInventory().addItem(new Item(SUNFIRE_SPLINTERS, charges)).isFailure()) {
									player.getInventory().set(slotId, new Item(chargedToUncharged(item)));
									setKey(5);
								} else {
									setKey(10);
								}
							});
						} else {
							options("Uncharge Dizana's quiver?", "Yes.", "No.").onOptionOne(() -> {
								if (!player.getInventory().addItem(new Item(SUNFIRE_SPLINTERS, charges)).isFailure()) {
									player.getInventory().set(slotId, new Item(chargedToUncharged(item)));
									setKey(5);
								} else {
									setKey(10);
								}
							});
						}

						item(5, DIZANAS_QUIVER_UNCHARGED, "You fully uncharge Dizana's Quiver, regaining " + chargeText + " sunfire splinters in the process.");
						plain(10, "You do not have enough inventory space to uncharge Dizana's quiver.");
					}
				});
			}
		});
		bind("Check", (player, item, container, slotId) -> {
			int blessingCharge = item.getNumericAttribute(BLESSSING_CHARGE_ATTRIBUTE).intValue() / BLESSING_PERCENT_PER_CHARGE;
			player.sendMessage("Dizana's quiver has " + Utils.pluralizedFormattedColorized("charge", item.getCharges(), Colour.RED) + " remaining. It has made " + blessingCharge + "% progress towards being blessed.");
		});
		bind("Destroy", (player, item, container, slotId) -> player.getDialogueManager().start(new DestroyItemDialogue(player, item, slotId, () -> PlayerAttributesKt.setDizanasQuiver(player, -1, 0))));
	}

	@Override
	public int[] getItems() {
		return new int[]{DIZANAS_QUIVER_UNCHARGED, DIZANAS_QUIVER_UNCHARGED_L, DIZANAS_QUIVER, DIZANAS_QUIVER_L, BLESSED_DIZANAS_QUIVER, BLESSED_DIZANAS_QUIVER_L};
	}

	@Override
	public boolean allItems() {
		return true;
	}

	private static boolean isQuiver(int id) {
		return id == DIZANAS_QUIVER_UNCHARGED || id == DIZANAS_QUIVER_UNCHARGED_L
				|| id == DIZANAS_QUIVER || id == DIZANAS_QUIVER_L
				|| id == BLESSED_DIZANAS_QUIVER || id == BLESSED_DIZANAS_QUIVER_L;
	}

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final boolean fromIsQuiver = isQuiver(from.getId());
		final Item quiver = fromIsQuiver ? from : to;
		final int quiverSlot = fromIsQuiver ? fromSlot : toSlot;
		final Item other = fromIsQuiver ? to : from;
		final int otherSlot = fromIsQuiver ? toSlot : fromSlot;

		if (other.getId() == SUNFIRE_SPLINTERS) {
			// Blessed quivers cannot be charged with splinters.
			if (quiver.getId() == BLESSED_DIZANAS_QUIVER || quiver.getId() == BLESSED_DIZANAS_QUIVER_L) {
				player.sendMessage("Nothing interesting happens.");
				return;
			}
			handleSplinterCharging(player, quiver, other, quiverSlot);
			return;
		}

		// Ammo storage — validate the item is an arrow or bolt (not javelin/atlatl).
		if (!other.isRangedAmmo()) {
			player.sendMessage("Nothing interesting happens.");
			return;
		}
		final String ammoName = other.getName().toLowerCase();
		if (ammoName.contains("javelin") || ammoName.contains("atlatl")) {
			player.sendMessage("You can't store this ammunition in your Dizana's Quiver.");
			return;
		}

		handleAmmoStorage(player, other, otherSlot);
	}

	private void handleSplinterCharging(Player player, Item quiver, Item splinters, int quiverSlot) {
		if (quiver.getCharges() == MAX_CHARGES) {
			player.sendMessage("Your quiver is already fully charged.");
			return;
		}
		final int chargesToAdd = Math.min(MAX_CHARGES - quiver.getCharges(), splinters.getAmount());
		player.sendInputInt("How many charges do you wish to add to Dizana's quiver? (0 - " + chargesToAdd + ")", value -> {
			if (value < 1) {
				return;
			}
			final int addedCharges = Math.min(chargesToAdd, value);
			player.getInventory().ifDeleteItem(new Item(SUNFIRE_SPLINTERS, addedCharges), () -> {
				int idSwap = unchargedToCharged(quiver);
				if (idSwap != -1) {
					quiver.setId(idSwap);
					player.getInventory().refresh(quiverSlot);
				}
				quiver.setCharges(quiver.getCharges() + addedCharges);
				player.getDialogueManager().start(new ItemChat(player, quiver, "You use " + addedCharges + " " + splinters.getName() + " to charge Dizana's quiver. It now has " + Utils.pluralizedFormattedColorized("charge", quiver.getCharges(), Colour.RED) + "."));
			});
		});
	}

	private void handleAmmoStorage(Player player, Item ammo, int ammoSlot) {
		final int currentAmmoId = PlayerAttributesKt.getDizanasQuiverAmmo(player);
		final int currentAmmoAmount = PlayerAttributesKt.getDizanasQuiverAmmoAmount(player);
		final boolean hasStoredAmmo = currentAmmoId != -1 && currentAmmoAmount > 0;

		if (hasStoredAmmo && currentAmmoId != ammo.getId()) {
			// Different ammo type stored — swap: delete new ammo, return old ammo, store new.
			final int newAmmoId = ammo.getId();
			final int newAmmoAmount = ammo.getAmount();
			player.getInventory().deleteItem(ammoSlot, ammo);
			player.getInventory().addItem(new Item(currentAmmoId, currentAmmoAmount)).onFailure(overflow -> {
				player.sendFilteredMessage("Some ammunition was dropped on the ground.");
				World.spawnFloorItem(overflow, player);
			});
			PlayerAttributesKt.setDizanasQuiver(player, newAmmoId, newAmmoAmount);
		} else if (hasStoredAmmo) {
			// Same ammo type — add to existing count.
			final long newTotal = (long) currentAmmoAmount + ammo.getAmount();
			if (newTotal > Integer.MAX_VALUE) {
				final int canStore = Integer.MAX_VALUE - currentAmmoAmount;
				if (canStore <= 0) {
					player.sendMessage("Your quiver cannot hold any more of this ammunition.");
					return;
				}
				player.getInventory().deleteItem(ammoSlot, new Item(ammo.getId(), canStore));
				PlayerAttributesKt.setDizanasQuiver(player, ammo.getId(), Integer.MAX_VALUE);
			} else {
				player.getInventory().deleteItem(ammoSlot, ammo);
				PlayerAttributesKt.setDizanasQuiver(player, ammo.getId(), (int) newTotal);
			}
		} else {
			// Nothing stored — straight store.
			player.getInventory().deleteItem(ammoSlot, ammo);
			PlayerAttributesKt.setDizanasQuiver(player, ammo.getId(), ammo.getAmount());
		}

		player.sendFilteredMessage("You put the ammo into Dizana's Quiver.");
		player.sendSound(2244);
	}

	private static int chargedToBlessed(final Item item) {
		return switch (item.getId()) {
			case DIZANAS_QUIVER -> BLESSED_DIZANAS_QUIVER;
			case DIZANAS_QUIVER_L -> BLESSED_DIZANAS_QUIVER_L;
			default -> -1;
		};
	}

	private static int chargedToUncharged(final Item item) {
		return switch (item.getId()) {
			case DIZANAS_QUIVER -> DIZANAS_QUIVER_UNCHARGED;
			case DIZANAS_QUIVER_L -> DIZANAS_QUIVER_UNCHARGED_L;
			default -> -1;
		};
	}

	private static int unchargedToCharged(final Item item) {
		return switch (item.getId()) {
			case DIZANAS_QUIVER_UNCHARGED -> DIZANAS_QUIVER;
			case DIZANAS_QUIVER_UNCHARGED_L -> DIZANAS_QUIVER_L;
			default -> -1;
		};
	}

	@Override
	public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
		if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) && Utils.random(19) == 0) {
			player.sendFilteredMessage("As you've completed the master combat achievements, you have been prevented from losing a charge.");
			return;
		}

		// degrades of rate 1/3
		if (Utils.randomNoPlus(3) == 0) {
			return;
		}

		// Increase blessing of quiver
		int newBlessingCharge = item.getNumericAttribute(BLESSSING_CHARGE_ATTRIBUTE).intValue() + 1;
		if (newBlessingCharge >= CHARGES_FOR_BLESS) {
			item.setId(chargedToBlessed(item));
			item.resetAttributes();
			wrapper.refresh(slotId);
			player.sendMessage("<col=ef1020>Your " + item.getName() + " has become blessed!");
			return;
		} else {
			item.setAttribute(BLESSSING_CHARGE_ATTRIBUTE, newBlessingCharge);
		}

		int newCharges = Math.max(item.getCharges() - amount, 0);
		item.setCharges(newCharges);
		if (newCharges == 0) {
			int newId = chargedToUncharged(item);
			if (newId != -1) {
				player.sendMessage("<col=ef1020>Your " + item.getName() + " has ran out of charges.</col>");
				item.setId(newId);
				wrapper.refresh(slotId);
			}
		}
	}

}
