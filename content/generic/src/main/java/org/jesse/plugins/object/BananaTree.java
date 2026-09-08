package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Kris | 27/04/2019 03:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BananaTree implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Search")) {
            player.sendMessage("The banana tree is empty of bananas right now.");
        } else if (option.equals("Pick")) {
            if (!player.getInventory().hasFreeSlots()) {
                player.getDialogueManager().start(new ItemChat(player, new Item(1963), "You need some free inventory space to pick more bananas."));
                return;
            }
            player.getInventory().addOrDrop(new Item(1963));
            player.sendMessage("You pick a banana from the tree.");
            player.sendSound(new SoundEffect(2582));
            final WorldObject obj = new WorldObject(object);
            obj.setId(Math.min(2078, obj.getId() + 1));
            World.spawnObject(obj);
            if (object.getId() != ObjectId.BANANA_TREE) {
                return;
            }
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    final WorldObject o = World.getObjectWithType(object, object.getType());
                    if (o.getId() > 2073) {
                        final WorldObject replacement = new WorldObject(o);
                        replacement.setId(Math.max(2073, replacement.getId() - 1));
                        World.spawnObject(replacement);
                        if (replacement.getId() == ObjectId.BANANA_TREE) {
                            stop();
                        }
                        return;
                    }
                    stop();
                }
            }, 50, 50);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BANANA_TREE, ObjectId.BANANA_TREE_2074, ObjectId.BANANA_TREE_2075, ObjectId.BANANA_TREE_2076, ObjectId.BANANA_TREE_2077, ObjectId.BANANA_TREE_2078 };
    }
}
