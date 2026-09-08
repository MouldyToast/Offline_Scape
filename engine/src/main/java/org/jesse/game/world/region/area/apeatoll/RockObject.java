package org.jesse.game.world.region.area.apeatoll;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Kris | 21/03/2019 19:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RockObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Search")) {
            player.getDialogueManager().start(new PlainChat(player, "The rock appears to move slightly as you touch " + "it... It's a trap! You don't see any way to disarm it; maybe you can find a way to cross over " + "it."));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.A_ROCK };
    }
}
