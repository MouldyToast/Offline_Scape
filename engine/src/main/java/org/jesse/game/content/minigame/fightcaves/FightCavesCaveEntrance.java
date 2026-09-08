package org.jesse.game.content.minigame.fightcaves;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 28/10/2018 13:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FightCavesCaveEntrance implements ObjectAction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_ENTRANCE_11833, ObjectId.CAVE_ENTRANCE_11834 };
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.CAVE_ENTRANCE_11833) {
            FightCaves.start(player);
            return;
        }
        if (!player.inArea("Fight caves"))
            return;
        final FightCaves fightCaves = (FightCaves) player.getArea();
        if (option.equals("Leave")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Really leave?", new DialogueOption("Yes - really leave.", fightCaves::leave), new DialogueOption("No, I'll stay"));
                }
            });
        } else if (option.equals("Escape")) {
            fightCaves.leave();
        }
    }
}
