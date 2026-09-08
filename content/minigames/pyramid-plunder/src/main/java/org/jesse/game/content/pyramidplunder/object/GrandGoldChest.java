package org.jesse.game.content.pyramidplunder.object;

import org.jesse.game.content.pyramidplunder.PlunderRewardTier;
import org.jesse.game.content.pyramidplunder.PlunderRoom;
import org.jesse.game.content.pyramidplunder.PlunderableObject;
import org.jesse.game.content.pyramidplunder.PyramidPlunderConstants;
import org.jesse.game.content.pyramidplunder.npc.ScarabSwarm;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Christopher
 * @since 4/3/2020
 */
public class GrandGoldChest implements ObjectAction, PlunderableObject {
    private static final int UNLOOTED_STAGE = 0;
    private static final int LOOTED_STAGE = 1;
    private static final Animation openAnim = new Animation(832);
    private static final SoundEffect openSound = new SoundEffect(1215);
    private static final int CHEST_VARBIT = 2363;
    private static final List<PlunderRewardTier> rewards = new ArrayList<>();

    static {
        rewards.addAll(Collections.nCopies(7, PlunderRewardTier.STONE));
        rewards.addAll(Collections.nCopies(2, PlunderRewardTier.GOLD));
    }

    public static void reset(Player player) {
        player.getVarManager().sendBit(CHEST_VARBIT, UNLOOTED_STAGE);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final boolean spawnScarab = Utils.random(4) == 0;
        final PlunderRoom currentRoom = PlunderRoom.get(player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT));
        player.lock(2);
        player.setAnimation(openAnim);
        player.sendSound(openSound);
        if (!spawnScarab) {
            player.getSkills().addXp(SkillConstants.THIEVING, currentRoom.getChestExp());
        }
        player.getVarManager().sendBit(CHEST_VARBIT, LOOTED_STAGE);
        if (rollForSceptre(player, object)) {
            return;
        }
        reward(player);
        if (Utils.random(4) == 0) {
            new ScarabSwarm(player).spawn();
        }
    }

    @Override
    public List<PlunderRewardTier> getRewardTiers() {
        return rewards;
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {PyramidPlunderConstants.GRAND_GOLDEN_CHEST};
    }
}
