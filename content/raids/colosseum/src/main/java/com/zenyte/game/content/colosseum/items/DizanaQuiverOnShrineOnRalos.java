package com.zenyte.game.content.colosseum.items;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.item.ItemId.*;

@SuppressWarnings("unused")
public class DizanaQuiverOnShrineOnRalos implements ItemOnObjectAction {

	private static final Item BLESSED_QUIVER = new Item(BLESSED_DIZANAS_QUIVER);

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		int blessedId = linkItem(item);
		if (blessedId == -1) {
			return;
		}

		Item requiredSplinters = new Item(SUNFIRE_SPLINTERS, DizanaQuiverItemPlugin.CHARGES_FOR_BLESS - item.getNumericAttribute(DizanaQuiverItemPlugin.BLESSSING_CHARGE_ATTRIBUTE).intValue());
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				item(item, "Blessing Dizana's Quiver will cost " + Utils.format(requiredSplinters.getAmount()) + " sunfire splinters. Once blessed it will no longer need to be charged.");
				options("Bless Dizana's Quiver?<br>You will NOT be able to get your splinters back.", "No.", "Yes.").onOptionTwo(() -> {
					setKey(10);
					player.getInventory().deleteItemsIfContains(new Item[] {item, requiredSplinters}, () -> {
						player.getInventory().addItem(new Item(linkItem(item)));
						setKey(5);
					});
				});

				item(5, BLESSED_QUIVER, "You bless " + item.getName() + ", allowing it to not need charges.");
				item(10, item, "You don't have enough sunfire splinters to bless your " + item.getName() + ".");//TODO message idk real one? or is there is one
			}
		});
	}

	private static int linkItem(Item item) {
		return switch (item.getId()) {
			case DIZANAS_QUIVER_UNCHARGED, DIZANAS_QUIVER -> BLESSED_DIZANAS_QUIVER;
			case DIZANAS_QUIVER_UNCHARGED_L, DIZANAS_QUIVER_L -> BLESSED_DIZANAS_QUIVER_L;
			default -> -1;
		};
	}

	@Override
	public Object[] getItems() {
		return new Object[]{
				DIZANAS_QUIVER_UNCHARGED, DIZANAS_QUIVER_UNCHARGED_L,
				DIZANAS_QUIVER, DIZANAS_QUIVER_L,
		};
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.SHRINE_OF_RALOS};
	}

}
