package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;

import java.util.concurrent.ThreadLocalRandom;

public class Mixologist extends Boon {

    public static boolean roll() {
        return ThreadLocalRandom.current().nextDouble() <= 0.3;
    }

    @Override
    public String name() {
        return "Mixologist";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_Mixologist;
    }

    @Override
    public String description() {
        return "Provides a 30% chance to make two potions instead of one.";
    }

    @Override
    public int item() {
        return 2434;
    }
}
