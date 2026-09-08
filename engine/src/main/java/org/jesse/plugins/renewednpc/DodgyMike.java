package org.jesse.plugins.renewednpc;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.pathfinding.events.RouteEvent;
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 10/05/2019 14:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DodgyMike extends NPCPlugin {
    @Override
    public void handle() {
        bind("Trade", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.openShop("Dodgy Mike's Second-hand Clothing");
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc, 2, RouteEvent.WEST_EXIT), () -> execute(player, npc), false));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                4022
        };
    }
}
