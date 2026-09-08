package com.zenyte.game.content.lootkeys;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.runelite.api.ItemID;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LootkeyConstants {

    public static final int[] LOOT_KEY_ORDER = {
            ItemID.LOOT_KEY,
            ItemID.LOOT_KEY_26652,
            ItemID.LOOT_KEY_26653,
            ItemID.LOOT_KEY_26654,
            ItemID.LOOT_KEY_26655
    };

    public static void addDroppedItems(Player player, Player playerKiller, List<Item> lost) {
        if (lost.isEmpty())
            return;
        if (playerKiller != null) {
            var killerSettings = playerKiller.getLootkeySettings();
            var lootKeyUnlocked = !playerKiller.isIronman() && killerSettings != null;
            if (lootKeyUnlocked) {
                var lootKeyEnabled = killerSettings.isEnabled();
                if (lootKeyEnabled)
                    transferLootkey(player, playerKiller, lost, true);
                else
                    addStandardDeath(player, playerKiller, lost);
            } else
                addStandardDeath(player, playerKiller, lost);
        } else
            addStandardDeath(player, null, lost);
    }

    private static void addStandardDeath(Player from, Player to, List<Item> lost) {
        for (var item : lost) {
            var index = to == null ? -1 : Arrays.binarySearch(LOOT_KEY_ORDER, item.getId());
            if (index >= 0)
                transferLootkey(from, to, item);
            else if (to != null && to.isIronman())
                    World.spawnFloorItem(item, from.getPosition(), null, 0, 300);
            else
                World.spawnFloorItem(item, to, from.getPosition());
        }
    }

    private static void transferLootkey(Player from, Player to, List<Item> fromContainer, boolean checkItemFromSettings) {
        var choices = new IntLinkedOpenHashSet(LOOT_KEY_ORDER);
        // Check over the killers inventory for any keys, removing the choice if they have the key
        choices.removeIf(id -> to.getInventory().containsItem(id));

        var settings = to.getLootkeySettings();
        if (settings == null || !settings.isEnabled())
            choices.clear();

        // if there are no key choice to fill with items, spawn them on the floor
        if (choices.isEmpty()) {
            fromContainer.stream()
                .filter(Objects::nonNull)
                .forEach(item -> World.spawnFloorItem(item, to, from.getPosition()));
            return;
        }
        var selectedKeyId = choices.firstInt();
        // If the killer doesn't have space for the key, spawn the loot onto the floor
        if (!to.getInventory().hasSpaceFor(selectedKeyId)) {
            fromContainer.stream()
                .filter(Objects::nonNull)
                .forEach(item -> World.spawnFloorItem(item, to, from.getPosition()));
            return;
        }
        // Add the key to the killer's inventory
        to.getInventory().addItem(selectedKeyId, 1);
        // Grab the Key's Inventory
        var toContainer = getContainer(to, selectedKeyId);
        if (toContainer == null)
            return;
        // Clear the container if it has items
        if (checkItemFromSettings && !toContainer.isEmpty())
            toContainer.clear();
        // loop over each item in the victims inventory
        fromContainer.stream()
            .filter(Objects::nonNull)
            .forEach(item -> {
                // Check if the item is a loot key
                var index = Arrays.binarySearch(LOOT_KEY_ORDER, item.getId());
                // if it is a loot key, then transfer the items from the key
                if (index >= 0)
                    transferLootkey(from, to, item);
                // else we're confirming addition to the killer
                else {
                    if (checkItemFromSettings) {
                        var def = item.getDefinitions();
                        // If the setting is to drop consumables
                        if (isConsumable(def.getId()) && settings.isDropFood())
                            World.spawnFloorItem(item, to, from.getPosition());
                        // else if the value of the item is higher than the threshold; drop to the floor
                        else if (settings.isDropValuables() && item.getSellPrice() >= settings.getThreshold())
                            World.spawnFloorItem(item, to, from.getPosition());
                        // otherwise we're adding it to the key
                        else
                            toContainer.add(item);
                    }
                    // if there is no settings to filter over, add the item
                    else
                        toContainer.add(item);
                }
            });
    }

    private static void transferLootkey(Player from, Player to, Item key) {
        var index = Arrays.binarySearch(LOOT_KEY_ORDER, key.getId());
        if (index < 0) return;

        var fromContainer = getContainer(from, index);
        if (fromContainer == null) return;

        transferLootkey(from, to, new ObjectArrayList<>(fromContainer.getItems().values()), false);
        fromContainer.clear();
    }

    public static Container getContainer(Player player, int key) {
        var index = Arrays.binarySearch(LOOT_KEY_ORDER, key);
        if (index < 0) {
            return null;
        }
        var settings = player.getLootkeySettings();
        return settings == null ? null : settings.getContainer(index);
    }

    public static boolean isConsumable(int id) {
        return Consumable.consumables.containsKey(id);
    }
}
