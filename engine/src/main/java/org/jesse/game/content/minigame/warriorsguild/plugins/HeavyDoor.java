package org.jesse.game.content.minigame.warriorsguild.plugins;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.TemporaryDoubleDoor;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 23/03/2019 17:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HeavyDoor implements ObjectAction {

    private static final Animation PUSH = new Animation(4188);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Open")) {
            player.lock();
            player.setAnimation(PUSH);
            player.sendMessage("The doors creak slowly open - you rush through before they close behind you.");
            player.getSkills().addXp(SkillConstants.STRENGTH, 1);
            WorldTasksManager.schedule(() -> TemporaryDoubleDoor.handleBarrowsDoubleDoor(player, object), 1);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HEAVY_DOOR_15660, ObjectId.HEAVY_DOOR };
    }
}
