package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.FillContainer;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

public class CowObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.toLowerCase().equals("milk")) {
            if (player.getInventory().containsItem(1925, 1))
                player.getActionManager().setAction(new FillContainer(object, new Item(1925)));
            else
                player.getDialogueManager().start(new PlainChat(player, "You need a bucket to milk this cow."));
            return;
        }
        if (option.toLowerCase().equals("steal-cowbell")) {
            player.getDialogueManager().start(new PlainChat(player, "You have no reason to steal her cowbell.."));
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.DAIRY_COW, ObjectId.DAIRY_COW_12111};
    }

}
