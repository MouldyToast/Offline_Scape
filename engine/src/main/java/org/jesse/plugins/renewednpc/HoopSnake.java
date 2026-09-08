package org.jesse.plugins.renewednpc;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.container.impl.Inventory;

/**
 * @author Christopher
 * @since 3/21/2020
 */
public class HoopSnake extends NPCPlugin {
    public static final Graphics stunnedGfx = new Graphics(80);

    @Override
    public void handle() {
        bind("Stun", (player, npc) -> {
            final boolean failed = Utils.random(4) != 0 || npc.hasWalkSteps();
            if (failed) {
                player.sendFilteredMessage("The movement of the snake causes you to miss.");
                return;
            }
            npc.setTransformation(NpcId.STUNNED_HOOP_SNAKE);
            npc.setGraphics(stunnedGfx);
            WorldTasksManager.schedule(() -> {
                npc.setId(NpcId.HOOP_SNAKE);
                npc.finish();
            }, 99);
        });
        bind("Pick-up", (player, npc) -> {
            final Inventory inventory = player.getInventory();
            if (!inventory.checkSpace()) {
                return;
            }
            player.setAnimation(Animation.LADDER_DOWN);
            npc.setId(NpcId.HOOP_SNAKE);
            npc.finish();
            inventory.addOrDrop(ItemId.HOOP_SNAKE, 1);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.HOOP_SNAKE, NpcId.STUNNED_HOOP_SNAKE};
    }
}
