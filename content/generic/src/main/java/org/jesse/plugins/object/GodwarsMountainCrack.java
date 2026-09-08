package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Kris | 27/04/2019 02:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GodwarsMountainCrack implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Crawl-through")) {
            if (player.getSkills().getLevel(SkillConstants.AGILITY) < 60) {
                player.getDialogueManager().start(new PlainChat(player, "You need an Agility level of at least 60 to use this shortcut."));
                return;
            }
            player.lock(4);
            player.setAnimation(new Animation(844));
            new FadeScreen(player, () -> player.setLocation(player.getLocation().matches(new Location(2904, 3720, 0)) ? new Location(2899, 3713, 0) : new Location(2904, 3720, 0))).fade(4);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LITTLE_CRACK };
    }
}
