package org.jesse.game.content.treasuretrails.plugins;

import org.jesse.game.content.treasuretrails.npcs.mimic.MimicInstance;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 04/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MimicKeyhole implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Use")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Really leave the instance?", new DialogueOption("Yes.", () -> exit(player)), new DialogueOption("No."));
                }
            });
        } else if (option.equalsIgnoreCase("Exit")) {
            exit(player);
        }
    }

    private final void exit(@NotNull final Player player) {
        new FadeScreen(player, () -> {
            player.blockIncomingHits();
            player.setLocation(MimicInstance.strangeCasketLocation);
        }).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.KEYHOLE_34727 };
    }
}
