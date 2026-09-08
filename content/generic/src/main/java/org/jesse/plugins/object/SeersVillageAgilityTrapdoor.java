/**
 */
package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.SeersTrapdoorD;

/**
 * @author Noele | May 1, 2018 : 3:51:25 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public class SeersVillageAgilityTrapdoor implements ObjectAction {

    private static final Location TOP = new Location(2714, 3472, 3);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.LADDER_26118) {
            player.useStairs(828, TOP, 1, 1);
            player.addAttribute("SeersTrapdoor", 1);
            return;
        }
        if (object.getId() == ObjectId.TRAPDOOR_26119) {
            player.getDialogueManager().start(new SeersTrapdoorD(player));
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_26118, ObjectId.TRAPDOOR_26119 };
    }
}
