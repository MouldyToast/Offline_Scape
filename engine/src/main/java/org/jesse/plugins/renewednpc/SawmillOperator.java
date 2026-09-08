package org.jesse.plugins.renewednpc;

import org.jesse.game.content.skills.construction.Plank;
import org.jesse.game.content.skills.construction.Sawmill;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;
import org.jesse.plugins.dialogue.varrock.SawmillOperatorD;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 25/11/2018 16:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SawmillOperator extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new SawmillOperatorD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new TileEvent(player, new TileStrategy(npc.getLocation().transform(npc.getSpawnDirection(), 1), 0), () -> {
                    player.stopAll();
                    player.setFaceEntity(npc);
                    this.handle(player, npc);
                }));
            }
        });
        bind("Buy-plank", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                List<Item> list = new ArrayList<>();
                for (Plank plank : Plank.values) {
                    list.add(plank.getBase());
                }
                player.getDialogueManager().start(new SkillDialogue(player, "How many do you wish to make?", list.toArray(new Item[0])) {

                    @Override
                    public void run(int slotId, int amount) {
                        player.getActionManager().setAction(new Sawmill(Plank.values[slotId], amount));
                    }
                });
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new TileEvent(player, new TileStrategy(npc.getLocation().transform(npc.getSpawnDirection(), 1), 0), () -> {
                    player.stopAll();
                    player.setFaceEntity(npc);
                    this.handle(player, npc);
                }));
            }
        });
        bind("Trade", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.openShop("Construction supplies");
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new TileEvent(player, new TileStrategy(npc.getLocation().transform(npc.getSpawnDirection(), 1), 0), () -> {
                    player.stopAll();
                    player.setFaceEntity(npc);
                    this.handle(player, npc);
                }));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 3101 };
    }
}
