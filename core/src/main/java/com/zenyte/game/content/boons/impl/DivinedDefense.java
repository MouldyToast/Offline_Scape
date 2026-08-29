package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class DivinedDefense extends Boon {
    @Override
    public String name() {
        return "Divined Defense";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_DivinedDefense;
    }

    @Override
    public String description() {
        return "Removes the prayer drain side effect from the Divine Spirit Shield passive ability. (PVM only)";
    }

    @Override
    public int item() {
        return ItemId.DIVINE_SPIRIT_SHIELD;
    }
}
