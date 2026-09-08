package org.jesse.game.content.minigame.warriorsguild.plugins;

import org.jesse.game.content.minigame.warriorsguild.magicalanimator.MagicalAnimator;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 23/03/2019 17:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicalAnimatorObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Animate")) {
            int[] armourSet = null;
            loop: for (final int[] set : MagicalAnimator.ARMOUR_SETS) {
                for (final int piece : set) {
                    if (!player.getInventory().containsItem(piece, 1)) {
                        continue loop;
                    }
                }
                armourSet = set;
                break;
            }
            if (armourSet == null) {
                player.sendMessage("You have no armour set to animate.");
                return;
            }
            MagicalAnimator.handleAnimator(player, new Item(armourSet[0]), object);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGICAL_ANIMATOR };
    }
}
