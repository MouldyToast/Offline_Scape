package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 30 aug. 2018 | 17:29:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KeldragrimTrapdoorObject implements ObjectAction {

    private static final Location DESTINATION = new Location(2911, 10176, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Inspect")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    npc(2385, "Ahoy up there! Would you like to ride our minecart to<br><br>Keldagrim? It's free.", 1);
                    options(TITLE, "Yes please.", "No thanks.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10));
                    player(5, "Yes please.").executeAction(() -> {
                        new FadeScreen(player, () -> {
                            player.setLocation(DESTINATION);
                            player.sendMessage("You arrive in Keldagrim.");
                        }).fade(3);
                    });
                    player(10, "No thanks.");
                }
            });
        } else if (option.equals("Travel")) {
            new FadeScreen(player, () -> {
                player.setLocation(DESTINATION);
                player.sendMessage("You arrive in Keldagrim.");
            }).fade(3);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TRAPDOOR_16168 };
    }
}
