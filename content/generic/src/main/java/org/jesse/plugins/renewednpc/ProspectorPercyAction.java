package org.jesse.plugins.renewednpc;

import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.ProspectorPercyD;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class ProspectorPercyAction extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                if (!TreasureTrail.talk(player, npc)) {
                    player.getDialogueManager().start(new ProspectorPercyD(player, npc.getId()));
                }
            }
        });
        bind("Trade", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                player.openShop("Prospector Percy's Nugget Shop");
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.PROSPECTOR_PERCY };
    }
}
