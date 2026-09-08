package org.jesse.plugins.renewednpc;

import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Kris | 26/04/2019 23:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessGodwarsDungeonBoulder extends NPCPlugin {

    @Override
    public void handle() {
        bind("Move", (player, npc) -> {
            if (npc.getTemporaryAttributes().containsKey("Moving lock")) {
                return;
            }
            if (player.getSkills().getLevelForXp(SkillConstants.STRENGTH) < 60) {
                player.getDialogueManager().start(new PlainChat(player, "You need a Strength level of at least 60 to lift the boulder."));
                return;
            }
            player.lock();
            npc.getTemporaryAttributes().put("Moving lock", true);
            player.setAnimation(new Animation(player.getX() >= 3055 ? 3065 : 6130));
            WorldTasksManager.schedule(new TickTask() {

                @Override
                public void run() {
                    switch(ticks++) {
                        case 1:
                            npc.addWalkSteps(3053, 10166, 1, false);
                            break;
                        case 3:
                            player.setFaceEntity(null);
                            player.addWalkSteps(player.getX() >= 3055 ? 3052 : 3055, 10165, 3, false);
                            break;
                        case 5:
                            npc.addWalkSteps(3053, 10165, 1, false);
                            break;
                        case 6:
                            player.unlock();
                            npc.getTemporaryAttributes().remove("Moving lock");
                            stop();
                            break;
                    }
                }
            }, 0, 0);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.BOULDER_6621 };
    }
}
