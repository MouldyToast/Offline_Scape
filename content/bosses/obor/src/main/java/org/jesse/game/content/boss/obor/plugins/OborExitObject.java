package org.jesse.game.content.boss.obor.plugins;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.actions.FadeScreenAction;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 14/05/2019 | 10:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class OborExitObject implements ObjectAction {

    private static final Location OUTSIDE_INSTANCE = new Location(3095, 9832, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Are you sure you want to leave?", "Yes, I'm sure.", "No.").onOptionOne(() -> {
                    player.lock();
                    new FadeScreenAction(player, 2, () -> {
                        player.unlock();
                        player.setLocation(OUTSIDE_INSTANCE);
                    }).run();
                });
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_29488, ObjectId.GATE_29489 };
    }
}
