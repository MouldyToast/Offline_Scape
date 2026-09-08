package org.jesse.plugins.renewednpc;

import org.jesse.game.content.skills.fishing.Fishing;
import org.jesse.game.content.skills.fishing.SpotDefinitions;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

import java.util.ArrayList;

/**
 * @author Kris | 26/11/2018 18:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FishingSpot extends NPCPlugin {
    @Override
    public void handle() {
        final ArrayList<String> list = new ArrayList<String>();
        for (final SpotDefinitions def : SpotDefinitions.values) {
            for (final String action : def.getActions()) {
                if (!list.contains(action)) list.add(action);
            }
        }
        for (final String op : list) {
            bind(op, (player, npc) -> Fishing.init(player, npc, op));
        }
    }

    @Override
    public int[] getNPCs() {
        return SpotDefinitions.getNpcs().toIntArray();
    }
}
