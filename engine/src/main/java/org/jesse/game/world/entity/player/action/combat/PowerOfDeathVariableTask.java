package org.jesse.game.world.entity.player.action.combat;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.entity.player.variables.TickVariable;
import org.jesse.game.world.entity.player.variables.VariableTask;

/**
 * @author Jire
 */
public enum PowerOfDeathVariableTask implements VariableTask {
    INSTANCE;

    public static final int[] POWER_OF_DEATH_ITEMS = {
            ItemId.STAFF_OF_THE_DEAD, ItemId.TOXIC_STAFF_OF_THE_DEAD,
            ItemId.TOXIC_STAFF_UNCHARGED, ItemId.STAFF_OF_LIGHT, ItemId.STAFF_OF_BALANCE
    };

    @Override
    public void run(Player player, int ticks) {
        final boolean lastTick = ticks == 0;
        boolean stop = lastTick;
        if (!stop) {
            boolean hasWeapon = false;
            final int weaponID = player.getEquipment().getId(EquipmentSlot.WEAPON);
            for (int itemID : PowerOfDeathVariableTask.POWER_OF_DEATH_ITEMS) {
                if (weaponID == itemID) {
                    hasWeapon = true;
                    break;
                }
            }
            if (!hasWeapon) stop = true;
        }
        if (stop) {
            if (!lastTick) {
                player.getVariables().cancel(TickVariable.POWER_OF_DEATH);
            }
            player.sendSound(new SoundEffect(1598));
            player.sendMessage(Colour.RS_GREEN.wrap("Your protection fades away."));
        }
    }

}
