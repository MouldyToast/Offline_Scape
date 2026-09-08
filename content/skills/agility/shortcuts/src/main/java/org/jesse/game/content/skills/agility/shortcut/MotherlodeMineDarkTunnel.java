package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Kris | 10/05/2019 18:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MotherlodeMineDarkTunnel implements Shortcut {

    private static final Animation CRAWL = new Animation(844);

    private static final Location WEST_CREVICE = new Location(3760, 5670, 0);

    private static final Location WEST_EXIT = new Location(3759, 5670, 0);
    private static final Location EAST_EXIT = new Location(3765, 5671, 0);

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if(!DiaryUtil.eligibleFor(DiaryReward.FALADOR_SHIELD2, player)) {
            player.getDialogueManager().start(new PlainChat(player, "You must complete the Medium Tier tasks from the Falador Achievement Diary before Percy will let you use that tunnel."));
            return false;
        }
        return true;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean west = object.getPositionHash() == WEST_CREVICE.getPositionHash(); // west going east
        player.lock(2);
        WorldTasksManager.schedule(() -> player.setAnimation(CRAWL));
        WorldTasksManager.schedule(() -> player.setLocation(west ? EAST_EXIT : WEST_EXIT), 1);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb your way through the dark tunnel." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 54;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.DARK_TUNNEL_10047};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
