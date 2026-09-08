package org.jesse.plugins.renewednpc;

import org.jesse.game.content.skills.slayer.dialogue.KrystiliaAssignmentD;
import org.jesse.game.content.skills.slayer.dialogue.KrystiliaD;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 26/11/2018 17:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Krystilia extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new KrystiliaD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Assignment", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new KrystiliaAssignmentD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Trade", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.openShop("Slayer Equipment");
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Rewards", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getSlayer().openInterface();
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KRYSTILIA };
    }
}
