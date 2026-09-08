package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Kris | 19/04/2019 19:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AvaNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Devices", (player, npc) -> GameInterface.AVAS_DEVICES.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 4408, 4407 };
    }
}
