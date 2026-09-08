package org.jesse.plugins.renewednpc;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.plugins.events.LoginEvent;

/**
 * @author Kris | 12/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MountainGuide extends NPCPlugin {

    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        // Enable the "Travel" option on the mountain guide for everyone.
        event.getPlayer().getVarManager().sendBit(5421, 1);
    }

    private static final Location westernGuide = new ImmutableLocation(1277, 3560, 0);

    @Override
    public void handle() {
        bind("Travel", (player, npc) -> new FadeScreen(player, () -> player.setLocation(player.getLocation().withinDistance(westernGuide, 15) ? new Location(1401, 3536, 0) : new Location(1277, 3558, 0))).fade(3));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.MOUNTAIN_GUIDE, NpcId.MOUNTAIN_GUIDE_7600, NpcId.TRADER_CREWMEMBER_9346 };
    }
}
