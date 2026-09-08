package org.jesse.game.content.achievementdiary.plugins.drop;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 19-11-2018 | 14:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BrimhavenDungeonBarProcessor extends DropProcessor {

    private static final int BRONZE_BAR = 2349;
    private static final int IRON_BAR = 2351;
    private static final int STEEL_BAR = 2353;

    @Override
    public void attach() {
        put(BRONZE_BAR, new PredicatedDrop("This drop will be noted in the Brimhaven Dungeon if you have the elite Karamja diary completed."));
        put(IRON_BAR, new PredicatedDrop("This drop will be noted in the Brimhaven Dungeon if you have the elite Karamja diary completed."));
        put(STEEL_BAR, new PredicatedDrop("This drop will be noted in the Brimhaven Dungeon if you have the elite Karamja diary completed."));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (killer.inArea("Brimhaven Dungeon") && killer.getBooleanAttribute("noted_bars_metalic_dragons") && DiaryUtil.eligibleFor(DiaryReward.KARAMJA_GLOVES4, killer)) {//TODO area for brimhaven dungeon
            if (item.getId() == BRONZE_BAR || item.getId() == IRON_BAR || item.getId() == STEEL_BAR) {
                item.setId(item.getId() + 1);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 270, 271, 272, 273, 274, 275 };
    }
}
