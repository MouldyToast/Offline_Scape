package com.zenyte.game.content.follower.plugin;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.AnimationDefinitions;

/**
 * @author Kris | 27/11/2018 11:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Dt2BossesActions extends NPCPlugin {

    private static final int[][] ANIMATIONS = new int[][]{
            {10234},
            {10254},
            {10250, 10251, 10252}
    };

    @Override
    public void handle() {
        bind("Emote", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                if (npc.getId() == NpcId.WISP) {
                    playAnimationSet(npc, ANIMATIONS);
                }
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[]{
                NpcId.LILVIATHAN,
                NpcId.WISP
        };
    }

    public static void playAnimationSet(NPC npc, int[][] animationsSet) {
        if (animationsSet == null || animationsSet.length == 0) {
            return;
        }

        npc.lock();
        int animationIndex = (int) npc.getTemporaryAttributes().getOrDefault("pet_emote_index", 0);
        if (animationIndex >= animationsSet.length) {
            animationIndex = 0;
        }

        npc.getTemporaryAttributes().put("pet_emote_index", animationIndex + 1);

        var animations = animationsSet[animationIndex];
        var totalDelay = 0;
        for (int i = 0; i < animations.length; i++) {
            var index = i;
            var animation = animations[i];
            var def = AnimationDefinitions.get(animation);
            if (def == null) {
                continue;
            }

            WorldTasksManager.schedule(() -> {
                npc.setAnimation(new Animation(def.getId()));
                if (index == animations.length - 1) {
                    npc.unlock();
                }
            }, totalDelay);
            totalDelay += Math.max(2, def.getClientTicks() / 30);
        }
    }
}
