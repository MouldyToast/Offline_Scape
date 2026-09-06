package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.content.follower.FollowerKeys;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.var.VarCollection;

import java.util.Map;

/**
 * @author Kris | 16/04/2019 16:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EquipmentTabInterface extends Interface {

    public static final String REMOVE = "Remove";
    public static final String EXAMINE = "Examine";
    public static final String CHECK = "Check";

    @Override
    protected void attach() {
        put(1, "View equipment stats");
        put(3, "Price checker");
        put(5, "View items kept on death");
        put(7, "Call follower");
        put(28, "dizana quiver");
    }

	@Override
    public DefaultClickHandler getDefaultHandler() {
        return (player, componentId, slotId, itemId, optionId) -> {
            if (player.isLocked()) {
                return;
            }
            slotId = Equipment.getIndexByButton(getInterface().getId(), componentId);
            final Item item = player.getEquipment().getItem(slotId);
            if (item == null) {
                return;
            }
            final String opt = getOption(item, optionId);
            final ItemPlugin plugin = ItemPlugin.getPlugin(item.getId());
            final ItemPlugin.OptionHandler handler = plugin.getHandler(opt);
            if (handler != null) {
                handler.handle(player, item, player.getEquipment().getContainer(), slotId);
                return;
            }
            if (opt.equals(EXAMINE)) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            player.sendMessage("Nothing interesting happens.");
        };
    }

    public static String getOption(final Item item, final int optionId) {
        if (optionId == 1) {
            return REMOVE;
        }
        if (optionId == 10) {
            return EXAMINE;
        }
        final Map<Integer, Object> params = item.getDefinitions().getParameters();
        if (params == null) {
            return "null";
        }
        final Object option = params.get(449 + optionId);
        if (!(option instanceof String)) {
            return "null";
        }
        return (String) option;
    }

	public static void handleDizanaQuiverRemoveOption(Player player) {
		int ammoItemId = PlayerAttributesKt.getDizanasQuiverAmmo(player);
		int ammoItemAmount = PlayerAttributesKt.getDizanasQuiverAmmoAmount(player);
		if (ammoItemId == -1 || ammoItemAmount <= 0) {
			VarCollection.DIZANAS_QUIVER_AMMO.updateSingle(player);
			VarCollection.DIZANAS_QUIVER_AMMO_AMOUNT.updateSingle(player);
			return;
		}

		final int inInventory = player.getInventory().getAmountOf(ammoItemId);
		if (inInventory > 0 && ammoItemAmount + inInventory < 0) {
			final int toRemove = Integer.MAX_VALUE - inInventory;
			if (toRemove > 0) {
				PlayerAttributesKt.setDizanasQuiver(player, ammoItemId, ammoItemAmount - toRemove);
				player.getInventory().addItem(ammoItemId, toRemove);
			} else {
				player.sendMessage("Not enough space in your inventory.");
			}
			return;
		}

		PlayerAttributesKt.setDizanasQuiver(player, -1, 0);
		Item ammoItem = new Item(ammoItemId, ammoItemAmount);
		player.getInventory().addItem(ammoItem).onFailure(it -> {
			player.sendMessage("<col=ff0000>Some of the ammunition from the quiver was dropped on the ground.");
			World.spawnFloorItem(it, player);
		});
	}

    @Override
    protected void build() {
		bind("dizana quiver", (player, slotId, itemId, optionId) -> {
			if (player.isLocked()) {
				return;
			}
			if (!CombatUtilities.hasQuiverEquipped(player)) {
				return;
			}
			switch (optionId) {
				case 1: {//Remove
					handleDizanaQuiverRemoveOption(player);
					break;
				}
				case 2: {//Fill/Swap
					Item ammo = player.getAmmo();

					int ammoItemId = PlayerAttributesKt.getDizanasQuiverAmmo(player);
					int ammoItemAmount = PlayerAttributesKt.getDizanasQuiverAmmoAmount(player);
					Item quiverItem = null;
					if (ammoItemId != -1 && ammoItemAmount > 0) {
						quiverItem = new Item(ammoItemId, ammoItemAmount);
					}

					if (ammo == null) {
						if (quiverItem == null) {
							player.sendMessage(Colour.ORANGE_RED.wrap("You have nothing in your worn quiver to fill your Dizana's Quiver with."));
							return;
						}
					} else {
						String ammoName = ammo.getName().toLowerCase();
						if (ammoName.contains("javelin") || ammoName.contains("atlatl")) {//TODO no idea the exact message for this.
							player.sendMessage("You can't store this ammunition in your Dizana's Quiver.");
							return;
						}
					}

					player.getEquipment().set(EquipmentSlot.AMMUNITION, quiverItem);
					player.getEquipment().refresh(EquipmentSlot.AMMUNITION.getSlot());
					if (ammo == null) {
						PlayerAttributesKt.setDizanasQuiver(player, -1, 0);
					} else {
						PlayerAttributesKt.setDizanasQuiver(player, ammo.getId(), ammo.getAmount());
					}
					break;
				}
			}
		});
        bind("View equipment stats", player -> {
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.stopAll();
            player.getEquipment().sendEquipmentStatsInterface();
        });
        bind("Price checker", player -> {
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.stopAll();
            player.getPriceChecker().openPriceChecker();
        });
        bind("View items kept on death", player -> {
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.stopAll();
            GameInterface.ITEMS_KEPT_ON_DEATH.open(player);
        });
        bind("Call follower", player -> {
            if (player.isLocked()) {
                return;
            }
            Follower follower = FollowerKeys.follower(player);
            if (follower == null) {
                player.sendMessage("You do not have a follower.");
                return;
            }
            if (follower.getLocation().withinDistance(player, 5)) {
                player.sendMessage("Your follower is already close enough.");
                return;
            }
            follower.call();
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EQUIPMENT_TAB;
    }
}
