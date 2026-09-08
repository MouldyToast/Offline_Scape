package org.jesse.game.content.minigame.inferno.plugins;

import org.jesse.game.content.minigame.inferno.instance.Inferno;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.RegionArea;

/**
 * @author Tommeh | 07/12/2019 | 21:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class CaveExit implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final RegionArea area = player.getArea();
        if (!(area instanceof Inferno)) {
            return;
        }
        final Inferno inferno = (Inferno) area;
        if (option.equals("Exit")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Really leave?", "Yes - really leave.", "No, I\'ll stay.").onOptionOne(() -> inferno.leave(false));
                }
            });
        } else {
            inferno.leave(false);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_EXIT_30283 };
    }
}
