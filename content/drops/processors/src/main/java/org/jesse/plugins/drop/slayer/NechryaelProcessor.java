package org.jesse.plugins.drop.slayer;

import org.jesse.game.content.treasuretrails.clues.SherlockTask;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 10/04/2019 20:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NechryaelProcessor extends DropProcessor {
    @Override
    public void attach() {

    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        SherlockTask.SLAY_A_NECHRYAEL.progress(killer);
    }

    @Override
    public int[] ids() {
        return new int[] {
                8, 11
        };
    }
}
