package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.impl.NPCChat;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class PestControlShipsLadder implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new NPCChat(player, 1769, "Hey! You can't go there."));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_287 };
    }
}
