package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * Author: Zei
 * Project: near-reality-server
 * Social: Discord: Z
 * Social: Github: https://github.com/Zeighe
 * Since: 1/18/2025
 */
public class WarpedSceptre extends ItemPlugin implements ItemOnItemAction {

    private static final Item WARPED_SCEPTRE_UNCHARGED_PLACEHOLDER = new Item(ItemId.WARPED_SCEPTRE_UNCHARGED, 1);
    private static final Item WARPED_SCEPTRE_PLACEHOLDER = new Item(ItemId.WARPED_SCEPTRE, 1);

    @Override
    public void handle() {
        bind("Charge", (player, item, slotId) -> chargeStaff(player, item));

        bind("Uncharge", this::unchargeStaff);
    }

    public static void chargeStaff(Player player, Item staff) {
        int chaosRunes = player.getInventory().getAmountOf(ItemId.CHAOS_RUNE);
        int earthRunes = player.getInventory().getAmountOf(ItemId.EARTH_RUNE);

        if (chaosRunes < 2 || earthRunes < 5) {
            player.getDialogueManager().start(new PlainChat(player, "You don't have enough runes to charge your warped sceptre. You need 2 chaos runes and 5 earth runes per charge."));
            return;
        }

        int currentCharges = staff.getCharges();
        int maxCharges = 20000; // Maximum charges a warped sceptre can hold
        int existingCharges = maxCharges - currentCharges;

        int maxChargesFromRunes = Math.min(chaosRunes / 2, earthRunes / 5);
        int maxChargesAbleToAdd = Math.min(existingCharges, maxChargesFromRunes);

        player.sendInputInt("How many charges do you wish to add? (0-" + maxChargesAbleToAdd + ")", number -> {
            int amountToAdd = Math.min(maxChargesAbleToAdd, number);

            player.getInventory().deleteItem(new Item(ItemId.CHAOS_RUNE, amountToAdd * 2));
            player.getInventory().deleteItem(new Item(ItemId.EARTH_RUNE, amountToAdd * 5));

            staff.setCharges(staff.getCharges() + amountToAdd);
            if (staff.getId() == ItemId.WARPED_SCEPTRE_UNCHARGED) {
                staff.setId(ItemId.WARPED_SCEPTRE);
            }
            player.getInventory().refreshAll();

            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(WARPED_SCEPTRE_PLACEHOLDER, "You add " + amountToAdd + " charges to your warped sceptre.");
                }
            });
        });
    }

    public void unchargeStaff(Player player, Item item, int slotId) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options(Colour.RS_RED.wrap("Fully uncharge your warped sceptre?"), "Yes.", "No.")
                        .onOptionOne(() -> {
                            if (player.getInventory().getItem(slotId) == item) {
                                unchargeSceptre(player, item, slotId);
                            }
                        });
            }

            private void unchargeSceptre(Player player, Item item, int slotId) {
                int chargeAmount = item.getCharges();
                int chaosRunes = chargeAmount * 2;
                int earthRunes = chargeAmount * 5;
                item.setCharges(0);
                if (item.getId() == ItemId.WARPED_SCEPTRE) {
                    item.setId(ItemId.WARPED_SCEPTRE_UNCHARGED);
                }
                player.getInventory().refresh(slotId);
                player.getInventory().addItem(new Item(ItemId.CHAOS_RUNE, chaosRunes));
                player.getInventory().addItem(new Item(ItemId.EARTH_RUNE, earthRunes));
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        item(WARPED_SCEPTRE_UNCHARGED_PLACEHOLDER, "You uncharge your warped sceptre, regaining " + chaosRunes + " chaos runes and " + earthRunes + " earth runes in the process.");
                    }
                });
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.WARPED_SCEPTRE_UNCHARGED, ItemId.WARPED_SCEPTRE, ItemId.CHAOS_RUNE, ItemId.EARTH_RUNE};
    }

    public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
        item.setCharges(Math.max(0, item.getCharges() - amount));
        if (item.getCharges() <= 0) {
            if (item.getId() == ItemId.WARPED_SCEPTRE) {
                item.setId(ItemId.WARPED_SCEPTRE_UNCHARGED);
            }
            player.getEquipment().refreshAll();
            player.getCombatDefinitions().refresh();
        }
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        var staff = player.getInventory().getItemById(ItemId.WARPED_SCEPTRE_UNCHARGED);
        if (staff == null)
            staff = player.getInventory().getItemById(ItemId.WARPED_SCEPTRE);
        var chaosRune = player.getInventory().getItemById(ItemId.CHAOS_RUNE);
        var earthRune = player.getInventory().getItemById(ItemId.EARTH_RUNE);
        if (staff == null || chaosRune == null || earthRune == null) {
            player.sendMessage("You don't have enough runes to charge your warped sceptre. You need 2 chaos runes and 5 earth runes per charge.");
            return;
        }
        chargeStaff(player, staff);
    }
}
