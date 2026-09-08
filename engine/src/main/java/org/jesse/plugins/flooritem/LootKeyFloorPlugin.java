package org.jesse.plugins.flooritem;

import org.jesse.game.content.lootkeys.LootkeyConstants;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.UpdateFlag;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.flooritem.FloorItem;
import org.jesse.plugins.SkipPluginScan;
import org.jetbrains.annotations.NotNull;

@SkipPluginScan
public class LootKeyFloorPlugin implements FloorItemPlugin {

    @Override
    public void handle(Player player, FloorItem item, int optionId, String option) {
        World.takeFloorItem(player, item);
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    @Override
    public void telegrab(@NotNull Player player, @NotNull FloorItem item) {
        FloorItemPlugin.super.telegrab(player, item);
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    @Override
    public boolean overrideTake() {
        return true;
    }

    @Override
    public int[] getItems() {
        return LootkeyConstants.LOOT_KEY_ORDER;
    }
}
