package com.zenyte.game.content.skills.afk;

import com.zenyte.game.content.skills.afk.impl.*;

import static com.near_reality.game.item.CustomObjectId.*;

public enum AFKSkillingObject {
    FARMING(AFK_FARMING_PLOT, AfkFarmingAction.class),
    FIRE_MAKING(AFK_FIREMAKING, AfkFiremakingAction.class),
    CRAFTING(AFK_CRAFTING_WHEEL, AfkCraftingAction.class),
    MINING(AFK_MINING, AfkMiningAction.class),
    SMITHING(AFK_FURNACE, AfkSmithingAction.class),
    WOODCUTTING(AFK_WOODCUTTING, AfkWoodCuttingAction.class),
    AGILITY(AFK_AGILITY, AfkAgilityAction.class),
    THIEVING(AFK_THIEVING, AfkThievingAction.class),
    RUNECRAFTING(AFK_RUNECRAFT, AfkRunecraftingAction.class),
    FISHING(AFK_FISHING, AfkFishingAction.class),
    COOKING(AFK_RANGE, AfkCookingAction.class),
    ;

    private final int objectId;
    private final Class<? extends BasicAfkAction> clazz;

    AFKSkillingObject(int objectId, Class<? extends BasicAfkAction> clazz) {
        this.objectId = objectId;
        this.clazz = clazz;
    }

    public int getObjectId() {
        return objectId;
    }

    public Class<? extends BasicAfkAction> getClazz() {
        return clazz;
    }

    public static AFKSkillingObject forId(int id) {
        for (AFKSkillingObject value : VALUES) {
            if(value.getObjectId() == id)
                return value;
        }
        return null;
    }

    public static final AFKSkillingObject[] VALUES = values();
}
