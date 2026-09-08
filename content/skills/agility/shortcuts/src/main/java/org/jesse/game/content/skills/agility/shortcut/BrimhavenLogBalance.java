package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.world.entity.masks.RenderAnimation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 25-3-2019 | 01:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BrimhavenLogBalance implements Shortcut {

    private static final RenderAnimation RENDER = new RenderAnimation(763, 762, 762, 762, 762, 762, 762);

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 30;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 20882, 20884 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 4;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (object.getId() == ObjectId.LOG_BALANCE_20882) {
            player.addWalkSteps(2687, 9506, -1, false);
        } else {
            player.addWalkSteps(2682, 9506, -1, false);
        }
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 10;
    }
}
