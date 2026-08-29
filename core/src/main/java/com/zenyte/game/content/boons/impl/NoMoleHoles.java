package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.item.ItemId;

public class NoMoleHoles extends Boon {
    @Override
    public String name() {
        return "No Mole Holes";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_NoMoleHoles;
    }

    @Override
    public String description() {
        return "The Giant Mole will stop digging when fighting you.";
    }

    @Override
    public int item() {
        return ItemId.BABY_MOLE;
    }
}
