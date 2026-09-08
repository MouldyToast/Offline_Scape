package org.jesse.game.content.skills.prayer.ectofuntus;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.equipment.equip.EquipPlugin;
import org.jesse.utils.TimeUnit;

/**
 * The necklace no longer reduces prayer points by half when equipped.
 * Instead, the prayer restore effect does not activate until nine seconds have passed;
 * this is done to prevent players equipping another necklace and only swapping to the necklace
 * each time they kill a monster just for the prayer restoration effect.
 *
 * @author Stan van der Bend
 */
@SuppressWarnings("unused")
public final class DragonBoneNecklaceEquip implements EquipPlugin {

    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        final long activationTime = WorldThread.getCurrentCycle() + TimeUnit.SECONDS.toTicks(9);
        PlayerAttributesKt.setBoneCrusherNecklaceActivationTime(player, activationTime);
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.DRAGONBONE_NECKLACE, ItemId.BONECRUSHER_NECKLACE };
    }
}
