package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.rewards.RaidRewards;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 04:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientChest implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            final RaidRewards rewards = raid.getRewards();
            if (rewards != null) {
                rewards.open(player);
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ANCIENT_CHEST };
    }
}
