package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.room.CreatureKeeperRoom;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 03:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CreatureKeeperChest implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, CreatureKeeperRoom.class, room -> room.openChest(player, object)));
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, CreatureKeeperRoom.class, room -> room.openChest(player, object)));
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1523, 11682 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHEST_29742 };
    }
}
