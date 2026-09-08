package org.jesse.plugins.object;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/21/2020
 */
public class Vines implements ObjectAction {
    private static final Animation rakingAnim = new Animation(2273);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.carryingItem(ItemId.RAKE)) {
            player.sendMessage("You need a rake to do this.");
            return;
        }
        player.lock(2);
        player.setAnimation(rakingAnim);
        WorldTasksManager.schedule(() -> {
            World.removeObject(object);
            player.setAnimation(Animation.STOP);
            player.getSkills().addXp(SkillConstants.FARMING, 2);
        });
        WorldTasksManager.schedule(() -> World.spawnObject(object), 99);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.VINES_30644};
    }
}
