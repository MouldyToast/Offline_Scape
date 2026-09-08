package org.jesse.game.content.pyramidplunder.object;

import org.jesse.game.content.pyramidplunder.PlunderRewardTier;
import org.jesse.game.content.pyramidplunder.PlunderRoom;
import org.jesse.game.content.pyramidplunder.PlunderableObject;
import org.jesse.game.content.pyramidplunder.npc.Mummy;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SarcophagusOpenAction extends Action implements PlunderableObject {
    public static final int UNLOOTED = 0;
    public static final int SARCOPHAGUS_VARBIT = 2362;
    private static final int LOOTED = 1;
    private static final int OPENING = 2;
    private static final Animation attemptOpenAnim = new Animation(4344);
    private static final Animation openAnim = new Animation(4345);
    private static final Animation sarcophagusOpeningAnim = new Animation(4336);
    private static final Animation sarcophagusOpeningAnim2 = new Animation(4337); //dont ask i dont knw
    private static final SoundEffect sarcophagusOpeningSound = new SoundEffect(1218, 5);
    private static final List<PlunderRewardTier> rewards = new ArrayList<>();

    static {
        rewards.addAll(Collections.nCopies(3, PlunderRewardTier.POTTERY));
        rewards.addAll(Collections.nCopies(7, PlunderRewardTier.STONE));
        rewards.addAll(Collections.nCopies(2, PlunderRewardTier.GOLD));
    }

    private final WorldObject sarcophagusObject;
    private final PlunderRoom currentRoom;
    private int ticks;

    @Override
    public boolean start() {
        if (player.getVarManager().getBitValue(SARCOPHAGUS_VARBIT) == LOOTED) {
            player.sendFilteredMessage("You have already looted this Sarcophagus.");
            return false;
        }
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        switch (ticks++) {
            case 0:
                player.setAnimation(attemptOpenAnim);
                World.sendObjectAnimation(sarcophagusObject, sarcophagusOpeningAnim);
                break;
            case 5:
                player.lock(6);
                player.getSkills().addXp(SkillConstants.STRENGTH, currentRoom.getSarcophagusExp());
                player.setAnimation(openAnim);
                player.sendSound(sarcophagusOpeningSound);
                player.getVarManager().sendBit(SARCOPHAGUS_VARBIT, OPENING);
                World.sendObjectAnimation(sarcophagusObject, sarcophagusOpeningAnim2);
                break;
            case 6:
                World.sendObjectAnimation(sarcophagusObject, sarcophagusOpeningAnim);
                break;
            case 10:
                if (rollForSceptre(player, sarcophagusObject)) {
                    return -1;
                }
                if (Utils.random(3) == 0) {
                    new Mummy(player).spawn();
                }
                reward(player);
                player.getVarManager().sendBit(SARCOPHAGUS_VARBIT, LOOTED);
                player.unlock();
                return -1;
        }
        return 0;
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    @Override
    public List<PlunderRewardTier> getRewardTiers() {
        return rewards;
    }

    public SarcophagusOpenAction(WorldObject sarcophagusObject, PlunderRoom currentRoom) {
        this.sarcophagusObject = sarcophagusObject;
        this.currentRoom = currentRoom;
    }
}
