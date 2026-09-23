package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.FairyRing;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.SpiritTreeMenuD;
import org.jesse.plugins.interfaces.FairyRingCombination;
import org.jesse.plugins.interfaces.FairyRingLog;

/**
 * @author Kris | 12/04/2019 22:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpiritualFairyTree implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Tree")) {
            player.getDialogueManager().start(new SpiritTreeMenuD(player));
        } else if (option.equalsIgnoreCase("Ring-zanaris")) {
            FairyRing.handle(player, object, FairyRing.codes.get("BKS"));
        } else if (option.equalsIgnoreCase("Ring-configure")) {
            FairyRingCombination.open(player, object);
            FairyRingLog.open(player);
        } else if (option.equalsIgnoreCase("Ring-last-destination")) {
            final int number = player.getNumericAttribute("lastFairyRing").intValue();
            if (number == 0) {
                player.sendFilteredMessage("You haven't used the fairy ring teleportation system yet.");
                return;
            }
            FairyRing.handle(player, object, FairyRing.getRing(number));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SPIRITUAL_FAIRY_TREE, ObjectId.SPIRITUAL_FAIRY_TREE_29229, ObjectId.SPIRITUAL_FAIRY_TREE_40779 };
    }
}
