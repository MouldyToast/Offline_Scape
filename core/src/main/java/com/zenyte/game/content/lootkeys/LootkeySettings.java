package com.zenyte.game.content.lootkeys;

import com.google.common.eventbus.Subscribe;
import com.google.gson.annotations.Expose;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.events.InitializationEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

import java.util.Optional;

public class LootkeySettings {

    @Expose
    private boolean enabled;
    @Expose
    private boolean dropFood;
    @Expose
    private boolean dropValuables;
    @Expose
    private int threshold;
    @Expose
    private int keysClaimed;
    @Expose
    private long totalValueClaimed;
    @Expose
    private long destroyedValue;
    @Expose
    private Container[] keyContainers;
    @Expose
    private Int2ObjectLinkedOpenHashMap<Item> currentItemsInChest;
    @Expose
    private boolean withdrawAsNote;

    public boolean isWithdrawAsNote() {
        return withdrawAsNote;
    }

    public void setWithdrawAsNote(boolean withdrawAsNote) {
        this.withdrawAsNote = withdrawAsNote;
    }

    public LootkeySettings(boolean enabled, boolean dropFood, boolean dropValuables, int threshold, int keysClaimed, long totalValueClaimed) {
        this.enabled = enabled;
        this.dropFood = dropFood;
        this.dropValuables = dropValuables;
        this.threshold = threshold;
        this.keysClaimed = keysClaimed;
        this.totalValueClaimed = totalValueClaimed;
        this.keyContainers = new Container[LootkeyConstants.LOOT_KEY_ORDER.length];
        for (int index = 0; index < keyContainers.length; index++) {
            this.keyContainers[index] = new Container(ContainerPolicy.NORMAL, ContainerType.WILDERNESS_LOOT_KEY_FAKE, Optional.empty());
        }
    }

    public long getDestroyedValue() {
        return destroyedValue;
    }

    public void incrementDestroyedValue(long amount) {
        this.destroyedValue += amount;
    }

    public Int2ObjectLinkedOpenHashMap<Item> getCurrentItemsInChest() {
        return currentItemsInChest;
    }

    public Container getCurrentItemsInChest(Player player) {
        var container = new Container(ContainerPolicy.NORMAL, ContainerType.WILDERNESS_LOOT_KEY, Optional.of(player));
        container.setItems(currentItemsInChest);
        return container;
    }

    public void setCurrentItemsInChest(Int2ObjectLinkedOpenHashMap<Item> currentItemsInChest) {
        this.currentItemsInChest = currentItemsInChest == null ? null : currentItemsInChest.clone();
    }

    public int getKeysClaimed() {
        return keysClaimed;
    }

    public void incrementKeysClaimed() {
        this.keysClaimed += 1;
    }


    public long getTotalValueClaimed() {
        return totalValueClaimed;
    }

    public void incrementTotalValueClaimed(long total) {
        this.totalValueClaimed += total;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDropFood() {
        return dropFood;
    }

    public boolean isDropValuables() {
        return dropValuables;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDropFood(boolean dropFood) {
        this.dropFood = dropFood;
    }

    public void setDropValuables(boolean dropValuables) {
        this.dropValuables = dropValuables;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public Container getContainer(int index) {
        return keyContainers[index];
    }

    /**
     * Eager attr rehydration at login: converts the raw attrPersistence shape
     * into the typed instance before any game code touches the key. Null stays
     * null: loot keys were never enabled for this player.
     */
    @Subscribe
    public static void onInit(final InitializationEvent event) {
        LootkeySettingsKeys.lootkeySettings(event.getPlayer());
    }

    public static void sendOpenChest(Player player) {
        player.getVarManager().sendBit(13651, 1);
    }

    public static void sendClosedChest(Player player) {
        player.getVarManager().sendBit(13651, 0);
    }

    public static void clear(Player player) {
        sendClosedChest(player);
        LootkeySettingsKeys.lootkeySettings(player).setCurrentItemsInChest(null);
    }

}
