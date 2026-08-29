package com.zenyte.game.world.entity.player.update;

import mgi.types.config.items.ItemDefinitions;

import java.util.Arrays;

public class ObjTypeCustomisation {
    public ItemDefinitions objType;

    public ObjTypeCustomisation(ItemDefinitions objType) {
        this.objType = objType;
        if (objType == null || objType.getReplacementColours() == null) {
            recols = new short[0];
            extraRecols = new short[0];
        } else {
            short[] replacementColours = objType.getReplacementColours();
            int length = replacementColours.length;

            recols = new short[Math.min(2, length)];
            extraRecols = new short[Math.max(0, length - 2)];

            for (int i = 0; i < length; i++) {
                if (i < 2) {
                    recols[i] = replacementColours[i];
                } else {
                    extraRecols[i - 2] = replacementColours[i];
                }
            }
        }
    }

    public short[] recols;

    public void recol(int index, int color) {
        if (index < recols.length) {
            recols[index] = (short) color;
        }
    }

    public short[] recols() {
        return recols;
    }

    public short[] extraRecols;

    public void recolExtra(int index, int color) {
        if (index < extraRecols.length) {
            extraRecols[index] = (short) color;
        }
    }

    public short[] extraRecols() {
        return extraRecols;
    }
}
