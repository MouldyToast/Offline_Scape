package org.jesse.plugins.object;

import org.jesse.game.content.skills.woodcutting.actions.Woodcutting;
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
 * @since 3/20/2020
 */
public class ThickVine implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Woodcutting.AxeResult axe = Woodcutting.getAxe(player).get();
        if (axe == null) {
            player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
            return;
        }
        player.lock(2);
        player.setAnimation(axe.getDefinition().getTreeCutAnimation());
        WorldTasksManager.schedule(() -> {
            World.removeObject(object);
            player.setAnimation(Animation.STOP);
            player.getSkills().addXp(SkillConstants.WOODCUTTING, 2);
        }, 1);
        WorldTasksManager.schedule(() -> World.spawnObject(object), 99);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.THICK_VINE, ObjectId.THICK_VINES};
    }
}
