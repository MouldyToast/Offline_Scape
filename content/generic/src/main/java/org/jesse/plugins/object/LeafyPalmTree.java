package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 27/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LeafyPalmTree implements ObjectAction {

    private final Animation animation = new Animation(810);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        object.setLocked(true);
        player.setAnimation(animation);
        World.spawnFloorItem(new Item(ItemId.PALM_LEAF), player, object.transform(Direction.values[Utils.random(Direction.values.length - 1)]));
        World.spawnObject(new WorldObject(ObjectId.LEAFY_PALM_TREE_2976, object.getType(), object.getRotation(), object));
        WorldTasksManager.schedule(() -> {
            object.setLocked(false);
            World.spawnObject(object);
        }, 45);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.LEAFY_PALM_TREE
        };
    }
}
