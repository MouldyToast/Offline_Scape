package com.zenyte.game.content.boons;

import com.zenyte.game.item.Item;
import it.unimi.dsi.fastutil.Pair;
import org.jetbrains.annotations.NotNull;

public class RemnantExchange {
    public static boolean eligibleItem(@NotNull Item itemInSlot) {
        return RemnantValueManager.isPresent(itemInSlot.getId());
    }

    public static boolean ineligibleItem(@NotNull Item itemInSlot) {
        return !RemnantValueManager.isPresent(itemInSlot.getId());
    }

    public static Pair<Integer, Boolean> getValueAndStatusForItem(@NotNull Item itemInSlot) {
        return RemnantValueManager.getValue(itemInSlot);
    }
}
