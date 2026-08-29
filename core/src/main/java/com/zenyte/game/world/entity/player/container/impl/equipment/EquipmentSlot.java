package com.zenyte.game.world.entity.player.container.impl.equipment;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kris | 4. mai 2018 : 20:57:55
 * @author Jire
 */
public enum EquipmentSlot {

    HELMET(0),
    CAPE(1),
    AMULET(2),
    WEAPON(3),
    PLATE(4),
    SHIELD(5),
    LEGS(7),
    HANDS(9),
    BOOTS(10),
    RING(12),
    AMMUNITION(13);

    private final int slot;

    EquipmentSlot(final int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public static final EquipmentSlot[] values = values();

    private static final Int2ObjectMap<EquipmentSlot> map =
            new Int2ObjectOpenHashMap<>(values.length);

    static {
        for (final EquipmentSlot slot : values) {
            map.put(slot.getSlot(), slot);
        }
    }

    @Nullable
    public static EquipmentSlot forSlot(final int slot) {
        return map.get(slot);
    }

}
