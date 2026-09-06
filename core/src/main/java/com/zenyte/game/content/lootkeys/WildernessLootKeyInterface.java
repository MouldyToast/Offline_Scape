package com.zenyte.game.content.lootkeys;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class WildernessLootKeyInterface extends Interface {

    @Override
    protected void attach() {
        put(2, 1, "Wilderness Loot Key");
        put(3, "Take");
        put(20, "Withdraw all to inventory");
        put(34, "Withdraw all to bank");
        put(9, "Destroy");


        put(25, "Item");
        put(26, "Note");
    }

    @Override
    protected void build() {
        bind("Item", player -> LootkeySettingsKeys.lootkeySettings(player).setWithdrawAsNote(false));
        bind("Note", player -> LootkeySettingsKeys.lootkeySettings(player).setWithdrawAsNote(true));

        bind("Withdraw all to inventory", player -> {
            if (player.isIronman()) return;
            // TODO: withdraw all to inventory
            var container = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest(player);
            for (int i = 0; i <= container.getContainerSize(); i++) {
                var item = container.get(i);
                if (item == null) continue;
                withdrawItemsToInventory(player, item.getId(), item.getAmount());
            }
        });
        bind("Withdraw all to bank", player -> {
            if (player.isIronman()) return;
            var container = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest(player);
            for (int i = 0; i <= container.getContainerSize(); i++) {
                var item = container.get(i);
                if (item == null) continue;
                withdrawItemToBank(player, item.getId(), item.getAmount());
            }
        });

        var here = this;
        bind("Destroy", player -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Are you sure you want to destroy the items?", "<col=ff0000>DESTROY!", "Cancel")
                        .onOptionOne(() -> {
                            var container = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest(player);
                            for (int i = 0; i <= container.getContainerSize(); i++) {
                                var item = container.get(i);
                                if (item == null) continue;
                                destroyItemsFromContainer(player, item.getId(), item.getAmount());
                            }
                        })
                        .onOptionTwo(() -> here.open(player));
            }
        }));

        bind("Take", (player, slotId, itemId, option) -> {
            if (option == 1 || option == 2 || option == 5 || option == 3 || option == 4 )
                withdrawToInventory(player, itemId, option);
            // 6 = bank x | 7 = bank all
            if (option == 6 || option == 7)
                withdrawToBank(player, itemId, option);
            // 8 = destroy x | 9 = destroy y
            if (option == 8 || option == 9)
                destroyOption(player, itemId, option);
            // 10 = examine
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
            }
        });

    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);

        var container = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest(player);
        player.getPacketDispatcher().sendUpdateItemContainer(container, ContainerType.WILDERNESS_LOOT_KEY);

        if (player.isIronman()) {
            player.getPacketDispatcher().sendComponentVisibility(getInterface(), 22, true);
            player.getPacketDispatcher().sendComponentVisibility(getInterface(), 23, false);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), 3, 0, 27, AccessMask.CLICK_OP10);
            player.getPacketDispatcher().sendClientScript(150, 48627715, 797, 7, 4, 0, -1, "", "", "", "", "", "", "", "", "");
        } else {
            int size = player.getBank().getContainer().getContainerSize();
            player.getPacketDispatcher().sendComponentText(getInterface(), 29, size - player.getBank().getContainer().getFreeSlotsSize());
            player.getPacketDispatcher().sendComponentText(getInterface(), 31, size);
            player.getPacketDispatcher().sendComponentSettings(getInterface(), 3, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP6, AccessMask.CLICK_OP7, AccessMask.CLICK_OP8, AccessMask.CLICK_OP9, AccessMask.CLICK_OP10);
            player.getVarManager().sendBit(4843, LootkeySettingsKeys.lootkeySettings(player).isWithdrawAsNote() ? 1 : 0);
            player.getPacketDispatcher().sendClientScript(150, 48627715, 797, 7, 4, 0, -1, "Take", "Take-5", "Take-10", "Take-X", "Take-All", "Bank-X", "Bank-All", "Destroy-X", "Destroy-All");
        }

        var value = getValue(container);
        player.getPacketDispatcher().sendComponentText(getInterface(), 6, "Value in chest: " + Utils.formatNumberWithCommas(value) + "gp");

    }

    private void withdrawToInventory(final Player player, final int itemId, final int option) {
        if (player.isIronman()) return;

        var amountToTake = 1;
        switch (option) {
            case 1:
                break;
            case 2:
                amountToTake = 5;
                break;
            case 3:
                amountToTake = 10;
                break;
            case 5: // Take All
                amountToTake = Integer.MAX_VALUE;
                break;
            case 4:
                player.sendInputInt("Enter amount to withdraw", (input) -> withdrawItemsToInventory(player, itemId, input));
                return;
        }
        if (option == 1 || option == 2 || option == 3 || option == 5)
            withdrawItemsToInventory(player, itemId, amountToTake);
    }


    private void destroyOption(final Player player, final int itemId, final int option) {
        if (option == 8)
            player.sendInputInt("Enter amount to <col=ff0000>destroy", (input) -> destroyItemsFromContainer(player, itemId, input));
        if (option == 9)
            destroyItemsFromContainer(player, itemId, Integer.MAX_VALUE);
    }

    private void withdrawToBank(final Player player, final int itemId, final int option) {
        if (player.isIronman()) return;
        switch (option) {
            case 6:
                player.sendInputInt("Enter amount to withdraw to bank", (input) -> withdrawItemToBank(player, itemId, input));
                break;
            case 7:
                withdrawItemToBank(player, itemId, Integer.MAX_VALUE);
                break;
        }
    }


    private long getValue(Container container) {
        long value = 0;

        for (Item item : container.getItems().values()) {
            if (item == null) continue;
            value += (long) item.getDefinitions().getPrice() * item.getAmount();
        }
        return value;
    }

    private void withdrawItemsToInventory(Player player, int itemId, int amountToTake) {
        var container = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest(player);
        var removalAmount = container.remove(new Item(itemId, amountToTake)).getSucceededAmount();

        if (removalAmount == 0) return;

        var itemToAdd = new Item(itemId, removalAmount);

        if(LootkeySettingsKeys.lootkeySettings(player).isWithdrawAsNote()) {
            itemToAdd.setId(itemToAdd.getDefinitions().getNotedOrDefault());
        }

        LootkeySettingsKeys.lootkeySettings(player).incrementTotalValueClaimed((long) itemToAdd.getDefinitions().getPrice() * itemToAdd.getAmount());
        player.getInventory().addOrDrop(itemToAdd);
        LootkeySettingsKeys.lootkeySettings(player).setCurrentItemsInChest(container.getItems());

        updateContainer(player, container);
    }

    private void destroyItemsFromContainer(Player player, int itemId, int amountToTake)
    {
        var container = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest(player);
        var removalAmount = container.remove(new Item(itemId, amountToTake)).getSucceededAmount();

        if (removalAmount == 0) return;
        var itemToAdd = new Item(itemId, removalAmount);

        LootkeySettingsKeys.lootkeySettings(player).incrementDestroyedValue((long) itemToAdd.getDefinitions().getPrice() * itemToAdd.getAmount());
        LootkeySettingsKeys.lootkeySettings(player).setCurrentItemsInChest(container.getItems());

        updateContainer(player, container);
    }

    private void withdrawItemToBank(Player player, int itemId, int amountToTake) {
        var container = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest(player);
        var removalAmount = container.remove(new Item(itemId, amountToTake)).getSucceededAmount();

        if (removalAmount == 0) return;
        var itemToAdd = new Item(itemId, removalAmount);
        container.getItems().remove(itemId);

        LootkeySettingsKeys.lootkeySettings(player).incrementTotalValueClaimed((long) itemToAdd.getDefinitions().getPrice() * itemToAdd.getAmount());
        player.getBank().add(itemToAdd);
        LootkeySettingsKeys.lootkeySettings(player).setCurrentItemsInChest(container.getItems());

        container.refresh(player);
        updateContainer(player, container);
    }


    private void updateContainer(Player player, Container container) {
        LootkeySettingsKeys.lootkeySettings(player).setCurrentItemsInChest(container.getItems());
        container.refresh(player);
        var value = getValue(container);
        player.getPacketDispatcher().sendComponentText(getInterface(), 6, "Value in chest: "
                + Utils.formatNumberWithCommas(value) + "gp");

        // When there's no items in the container left
        if (container.getSize() == 0) {
            this.close(player);
            LootkeySettings.clear(player);
        }
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WILDERNESS_LOOT_KEY;
    }


}
