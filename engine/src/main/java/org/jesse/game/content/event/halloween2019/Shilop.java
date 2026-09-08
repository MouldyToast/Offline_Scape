package org.jesse.game.content.event.halloween2019;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.pathfinding.events.player.UncheckedEntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Shilop extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Help me out of here!");
                    }
                });
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new UncheckedEntityEvent(player, new EntityStrategy(npc), () -> execute(player, npc), true));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
               // HalloweenNPC.SHILOP.getRepackedNPC()
        };
    }
}
