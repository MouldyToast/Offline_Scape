package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 12/06/2019 08:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class JossikNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Rewards", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                GameInterface.JOSSIKS_SALVAGED_GODBOOKS.open(player);
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
        return new int[] { NpcId.JOSSIK };
    }
}
