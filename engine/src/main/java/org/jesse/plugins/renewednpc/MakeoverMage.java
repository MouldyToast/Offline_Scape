package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.MakeOverMageD;

/**
 * @author Kris | 25/11/2018 20:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MakeoverMage extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                player.getDialogueManager().start(new MakeOverMageD(player, npc, true));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                if (npc.getRadius() > 0)
                    npc.setInteractingWith(player);
            }
        });
        bind("Makeover", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                GameInterface.CHARACTER_DESIGN.open(player);
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                if (npc.getRadius() > 0)
                    npc.setInteractingWith(player);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                1306, 1307, 8487, 10021
        };
    }
}
