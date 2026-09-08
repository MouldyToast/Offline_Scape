package org.jesse.game.content.skills.smithing.zombie_item;

import org.jesse.game.content.skills.smithing.Smithing;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 2/8/2025
 */
public class ZombieHelmetCreationAction extends ZombieItem {

    private static final Item BROKEN_ZOMBIE_HELMET = new Item(30324);
    private static final Item ZOMBIE_HELMET = new Item(30321);

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (hasPreReqs(player)) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Repair your zombie helmet?", "Yes", "No")
                        .onOptionOne(() -> WorldTasksManager.schedule(getHelmetCreationTask(player), 0, 0))
                        .onOptionTwo(() -> {});
                }
            });
        }
    }

    private TickTask getHelmetCreationTask(Player player) {
        player.lock();
        return new TickTask() {
            @Override
            public void run() {
                if (ticks == 0)
                    player.setAnimation(Smithing.ANIMATION);
                if (ticks == 5)
                    player.setAnimation(Smithing.ANIMATION);
                if (ticks == 9) {
                    if (player.getInventory().deleteItem(BROKEN_ZOMBIE_HELMET).getResult() == RequestResult.SUCCESS) {
                        player.getDialogueManager().start(new ItemChat(player, ZOMBIE_HELMET, "You successfully repair your zombie helmet."));
                        player.getInventory().addItem(ZOMBIE_HELMET);
                        player.getSkills().addXp(SkillConstants.SMITHING, 500);
                    }
                    stop();
                }
                ticks++;
            }

            @Override
            public void stop() {
                super.stop();
                player.unlock();
            }
        };
    };

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.BROKEN_ZOMBIE_HELMET };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Anvil" };
    }
}
