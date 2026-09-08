package org.jesse.plugins.renewednpc;

import org.jesse.game.content.area.tzhaar.TzHaar;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.TzHaarMejJalD;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class TzHaarMejJal extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk to", (player, npc) -> player.getDialogueManager().start(new TzHaarMejJalD(player, npc, false)));
        bind("Exchange fire cape", (player, npc) -> player.getDialogueManager().start(new TzHaarMejJalD(player, npc, true)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { TzHaar.TZHAAR_MEJ_JAL };
    }
}
