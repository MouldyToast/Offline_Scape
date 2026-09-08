package org.jesse.plugins.renewednpc;

import org.jesse.ContentConstants;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsShopNPC extends NPCPlugin {

    @Override
    public void handle() {
        if (!ContentConstants.CASTLE_WARS) {
            return;
        }
        bind("Talk-to", (player, npc) -> player.openShop("Castle Wars Store"));
        bind("Trade", (player, npc) -> player.openShop("Castle Wars Store"));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 5721 };
    }
}
