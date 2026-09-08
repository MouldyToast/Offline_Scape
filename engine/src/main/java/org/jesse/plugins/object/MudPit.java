package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.VarManager;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since July 20 2020
 */
public class MudPit implements ObjectAction {

    public static final int FILLED_PIT_VARBIT = 5809;

    private static final Animation fillAnim = new Animation(2292);

    private static final int MUSHROOMS_REQUIRED = 9;

    static {
        VarManager.appendPersistentVarbit(FILLED_PIT_VARBIT);
    }

    public static void attemptFill(@NotNull final Player player) {
        if (canFill(player)) {
            fill(player);
        }
    }

    private static boolean canFill(@NotNull final Player player) {
        if (player.getInventory().getAmountOf(ItemId.MUSHROOM) < MUSHROOMS_REQUIRED) {
            player.sendMessage("You require " + MUSHROOMS_REQUIRED + " mushrooms to fill the pit.");
            return false;
        }
        return true;
    }

    private static void fill(@NotNull final Player player) {
        player.lock(2);
        player.setAnimation(fillAnim);
        player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.MUSHROOM, 1), "You fill the pit with Bittercap mushrooms."));
        player.getVarManager().sendBit(FILLED_PIT_VARBIT, 1);
        player.getInventory().deleteItem(ItemId.MUSHROOM, MUSHROOMS_REQUIRED);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Fill")) {
            attemptFill(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 31426 };
    }
}
