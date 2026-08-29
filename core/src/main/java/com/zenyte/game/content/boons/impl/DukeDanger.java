package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class DukeDanger extends Boon {
    @Override
    public String name() {
        return "Duke Danger";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_AllGassedUp;
    }

    @Override
    public String description() {
        return "Reduce Duke special attack by 66% & wearing a gas mask of any form at Duke prevents gas vent damage";
    }

    @Override
    public int item() {
        return ItemId.GAS_MASK;
    }
}
