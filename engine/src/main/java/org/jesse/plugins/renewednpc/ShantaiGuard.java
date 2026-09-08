package org.jesse.plugins.renewednpc;

import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 16:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShantaiGuard extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Go talk to Shantay. I'm on duty and I don't have time to talk to the likes of you!");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SHANTAY_GUARD };
    }
}
