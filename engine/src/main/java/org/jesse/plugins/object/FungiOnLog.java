package org.jesse.plugins.object;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.item.Item;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 14/06/2019 11:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FungiOnLog implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Pick")) {
            player.setAnimation(new Animation(827));
            final boolean doubleLoot = DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS3, player);
            player.getInventory().addOrDrop(new Item(2970, doubleLoot ? 2 : 1));
            World.spawnObject(new WorldObject(3508, object.getType(), object.getRotation(), object));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.FUNGI_ON_LOG };
    }
}
