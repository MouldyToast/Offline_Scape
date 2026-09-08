package org.jesse.game.content.skills.hunter;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Kris | 14/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FancyDressShopOwner extends NPCPlugin {
    @Override
    public void handle() {
        bind("Fur Clothing", (player, npc) -> GameInterface.CUSTOM_FUR_CLOTHING.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                1023
        };
    }
}
