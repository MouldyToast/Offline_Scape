package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

public class BlessedFromAbove extends Boon {
    @Override
    public String name() {
        return "Blessed from above";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_BountifulSacrifice;
    }

    @Override
    public String description() {
        return "Provides a 300% XP Boost when using the Ash Sanctifer, Bonecrusher or Bonecrusher Necklace";
    }

    @Override
    public int item() {
        return 13116;
    }
}
