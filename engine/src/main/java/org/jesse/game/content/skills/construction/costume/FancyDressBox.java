package org.jesse.game.content.skills.construction.costume;

import com.google.gson.annotations.Expose;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 3. march 2018 : 18:03.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FancyDressBox {

    @Expose
    private final Map<Integer, int[]> items = new HashMap<Integer, int[]>(FancyDressData.VALUES.length);
    private transient Player player;

    /**
     * Takes a costume set from the fancy dress box if possible.
     *
     * @param itemId the display item id of the set on the interface.
     */
    public void takeSet(final int itemId) {
        FancyDressData set = FancyDressData.DISPLAY_MAP.get(itemId);
        if (set == null) set = FancyDressData.MAP.get(itemId);
        if (set == null) {
            return;
        }
        final int display = set.getDisplayItem();
        final int[] items = this.items.get(display);
        if (items == null) {
            return;
        }
        if (player.getInventory().getFreeSlots() < items.length) {
			player.sendMessage("You need at least " + items.length + " free inventory spaces to withdraw this costume.");
			return;
		}
		final EnumDefinitions map = EnumDefinitions.get(380);
		final String val = map.getStringValue(display);
		if (val == null) {
			return;
		}
		for (final int it : items) {
			player.getInventory().addItem(it, 1);
		}
		player.sendMessage("You remove the " + val.toLowerCase() + " from the fancy dress box.");
		this.items.remove(display);
		refresh();
	}
	
	/**
	 * Adds a set to the armour box.
	 * @param itemId a piece of the set to enqueue.
	 * @return whether it was successfully added or not.
	 */
	public boolean addSet(final int itemId) {
		final FancyDressData set = FancyDressData.MAP.get(itemId);
		if (set == null) {
			return false;
		}
		final int display = set.getDisplayItem();
		if (items.containsKey(display)) {
			player.sendMessage("Your fancy dress box already contains that.");
			return false;
		}
		final StorableSetPiece[] pieces = set.getPieces();
		final int[] items = new int[pieces.length];
		for (int i = pieces.length - 1; i >= 0; i--) {
			final StorableSetPiece piece = pieces[i];
			final int p = getPiece(piece);
			if (p == -1) {
				notify(set);
				return false;
			}
			items[i] = p;
		}
		final EnumDefinitions map = EnumDefinitions.get(380);
		final String val = map.getStringValue(display);
		if (val == null) {
			return false;
		}
		for (int i = pieces.length - 1; i >= 0; i--) {
			final int item = items[i];
			player.getInventory().deleteItem(item, 1);
		}
		player.sendMessage("You place the " + val.toLowerCase() + " into the fancy dress box.");
		this.items.put(display, items);
		refresh();
		return true;
	}
	
	/**
	 * Gets the id of the first piece that the player has in their inventory.
	 * @param piece the Piece object from the enum.
	 * @return id of the piece if the player has, otherwise -1.
	 */
	private int getPiece(final StorableSetPiece piece) {
		final int[] ids = piece.getIds();
		for (int i = ids.length - 1; i >= 0; i--) {
			if (player.getInventory().containsItem(ids[i], 1)) {
				return ids[i];
			}
		}
		return -1;
	}
	
	/**
	 * Notifies the player about the pieces required for this set.
	 * Pieces they have are marked green, whereas the missing ones are red.
	 * @param set the armour set to notify about.
	 */
	private void notify(final FancyDressData set) {
		final EnumDefinitions map = EnumDefinitions.get(380);
		final String val = map.getStringValue(set.getDisplayItem());
		if (val == null) {
			return;
		}
		final Inventory inventory = player.getInventory();
		final boolean singular = set == FancyDressData.ROYAL_FROG_COSTUME;
		player.sendMessage("You need the following items to enqueue the " + val.toLowerCase() + " to your fancy dress box:");
		for (final StorableSetPiece piece : set.getPieces()) {
			final int[] ids = piece.getIds();
			if (ids.length == 1 || singular) {
				final String prefix = inventory.containsItem(ids[0], 1) ? "<col=00ff00>" : "<col=ff0000>";
				player.sendMessage(prefix + "1 x " + ItemDefinitions.get(ids[0]).getName());
			} else {
				final String prefix = inventory.containsItem(ids[0], 1) ? "<col=00ff00>" : "<col=ff0000>";
				final String prefix2 = inventory.containsItem(ids[1], 1) ? "<col=00ff00>" : "<col=ff0000>";
				player.sendMessage(prefix + "1 x " + ItemDefinitions.get(ids[0]).getName() + "</col> or " + 
						prefix2 + "1 x " + ItemDefinitions.get(ids[1]).getName());
			}
		}
	}
	
	/**
	 * Refreshes the armour set interface values.
	 */
	private void refresh() {
		player.getConstruction().sendCostumeContainer(player);
		player.getPacketDispatcher().sendClientScript(3532, 3291, 1, 0);
	}

    /**
     * Opens the fancy dress box interface.
     */
    public void open() {
        player.getTemporaryAttributes().put("costumeRoomObject", "FANCY_DRESS_BOX");
        player.getVarManager().sendVarInstant(262, -1);
        player.getVarManager().sendVarInstant(261, 25);
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 675);
        player.getConstruction().sendCostumeContainer(player);
        player.getPacketDispatcher().sendComponentSettings(675, 4, 0, 3311, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendClientScript(3532, 3291, 1, 1);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map<Integer, int[]> getItems() {
        return items;
    }

}
