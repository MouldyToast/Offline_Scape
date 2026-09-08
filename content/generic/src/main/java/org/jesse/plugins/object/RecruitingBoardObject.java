package org.jesse.plugins.object;

import org.jesse.game.content.chambersofxeric.party.RaidingPartiesInterface;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 15. nov 2017 : 21:23.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RecruitingBoardObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getNumericAttribute("aware of raids layouts").intValue() == 0) {
            player.sendMessage(Colour.RED.wrap("Before you start a party - perhaps you should speak with Captain Rimor about the new layouts feature."));
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("Before you start a party - perhaps you should speak with Captain Rimor about the new layouts feature.").executeAction(() -> player.addAttribute("aware of raids layouts", 1));
                }
            });
            return;
        }
        RaidingPartiesInterface.refresh(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.RECRUITING_BOARD };
    }
}
