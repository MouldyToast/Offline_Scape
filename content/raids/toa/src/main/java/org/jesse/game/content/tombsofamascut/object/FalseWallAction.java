package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;


public class FalseWallAction implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getTOAManager().getRewardContainer() == null || player.getTOAManager().getRewardContainer().isEmpty()) {
            player.getDialogueManager().start(new PlainChat(player, "There is nothing to claim"));
            player.getVarManager().sendBit(14139, 0);
            return;
        }
        GameInterface.TOA_LOOT.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
            46082, 46083, 46224
        };
    }
}
