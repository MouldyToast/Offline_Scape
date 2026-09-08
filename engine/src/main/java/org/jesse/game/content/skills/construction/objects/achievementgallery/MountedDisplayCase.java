package org.jesse.game.content.skills.construction.objects.achievementgallery;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 26. veebr 2018 : 19:42.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MountedDisplayCase implements ObjectInteraction {

    private static final Animation ANIM = new Animation(645);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MOUNTED_EMBLEM, ObjectId.MOUNTED_COINS };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("admire")) {
            player.lock(5);
            player.setAnimation(ANIM);
            player.sendMessage("You admire the " + object.getName().toLowerCase() + ".");
        }
    }
}
