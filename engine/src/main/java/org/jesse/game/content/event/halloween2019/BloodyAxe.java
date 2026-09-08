package org.jesse.game.content.event.halloween2019;

import org.jesse.ContentConstants;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import mgi.custom.halloween.HalloweenObject;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BloodyAxe implements ObjectAction {

    public static final JonasNPC jonas = new JonasNPC();

    static {
        WorldTasksManager.scheduleCreation(jonas::spawn);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Look-at")) {
            if (!ContentConstants.HALLOWEEN) {
                return;
            }
            player.lock();
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    player("The axe is covered in blood. This doesn't look good...").setCantContinue();
                }
            });
            WorldTasksManager.schedule(() -> jonas.strike(player), 2);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                HalloweenObject.BLOODY_AXE.getRepackedObject()
        };
    }
}
