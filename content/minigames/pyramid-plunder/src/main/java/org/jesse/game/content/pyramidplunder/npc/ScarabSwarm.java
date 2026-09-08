package org.jesse.game.content.pyramidplunder.npc;

import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 4/5/2020
 */
public class ScarabSwarm extends PlunderNPC {
    public ScarabSwarm(Player player) {
        super(NpcId.SCARAB_SWARM, player);
    }
}
