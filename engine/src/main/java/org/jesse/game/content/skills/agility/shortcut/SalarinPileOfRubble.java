package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SalarinPileOfRubble implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getSkills().getLevel(SkillConstants.AGILITY) < 67) {
            player.getDialogueManager().start(new PlainChat(player, "You need an Agility level of at least 67 to climb up the rubble."));
            return;
        }
        player.setAnimation(new Animation(828));
        player.lock();
        player.sendFilteredMessage("You climb up the pile of rubble...");
        WorldTasksManager.schedule(() -> {
            player.setLocation(new Location(2615, 9506));
            player.getSkills().addXp(SkillConstants.AGILITY, 5.5);
            player.lock(1);
        });

    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.PILE_OF_RUBBLE_23563
        };
    }
}
