package org.jesse.plugins.drop.slayer;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 17:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanShamanProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(13576, 1, 1, 1250));
        appendDrop(new DisplayedDrop(13392, 1, 1) {
            public double getRate(final Player player, final int id) {
                return DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING1, player) ? 125 : 250;
            }
        });
        put(13392, new PredicatedDrop("This drop becomes 1/125 if you have the easy Kourend & Kebos diary completed."));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 1250) == 0) {
                return new Item(13576);
            } else if (random(DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING1, killer) ? 125 : 250) == 0) {
                return new Item(13392);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public int[] ids() {
        return new int[] {
                6766, 6767, 8565, 7744, 7745
        };
    }
}
