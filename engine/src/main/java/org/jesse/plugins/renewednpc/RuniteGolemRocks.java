package org.jesse.plugins.renewednpc;

import org.jesse.game.content.skills.mining.OreDefinitions;
import org.jesse.game.content.skills.mining.MiningDefinitions;
import org.jesse.game.content.skills.mining.actions.Mining;
import org.jesse.game.content.skills.mining.actions.Prospect;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 8-2-2019 | 20:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RuniteGolemRocks extends NPCPlugin {

    @Override
    public void handle() {
        bind("Mine", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getActionManager().setAction(new Mining(OreDefinitions.RUNITE_GOLEM_ROCKS, npc));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
        bind("Prospect", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getActionManager().setAction(new Prospect(OreDefinitions.RUNITE_GOLEM_ROCKS));
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
        return new int[] { NpcId.RUNITE_ROCKS };
    }
}
