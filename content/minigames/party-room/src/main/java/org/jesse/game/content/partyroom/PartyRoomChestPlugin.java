package org.jesse.game.content.partyroom;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 25/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyRoomChestPlugin implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.inArea(FaladorPartyRoom.class)) {
            return;
        }
        switch(option) {
            case "Open":
                if (object.isLocked()) {
                    return;
                }
                player.setAnimation(new Animation(832));
                player.sendSound(new SoundEffect(52));
                WorldTasksManager.schedule(() -> {
                    final WorldObject obj = new WorldObject(object);
                    obj.setId(2418);
                    World.spawnObject(obj);
                });
                return;
            case "Shut":
                if (object.isLocked()) {
                    return;
                }
                if (!FaladorPartyRoom.getPartyRoom().getVariables().isChestCloseable()) {
                    player.sendMessage("The chest may not currently be closed.");
                    return;
                }
                player.setAnimation(new Animation(832));
                player.sendSound(new SoundEffect(51));
                WorldTasksManager.schedule(() -> {
                    final WorldObject obj = new WorldObject(object);
                    obj.setId(26193);
                    World.spawnObject(obj);
                });
                return;
            case "Deposit":
                GameInterface.PARTY_DROP_CHEST.open(player);
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (object.getId() == ObjectId.CHEST_26193) {
            player.sendFilteredMessage("Nothing interesting happens.");
            return;
        }
        if (!player.inArea(FaladorPartyRoom.class)) {
            return;
        }
        GameInterface.PARTY_DROP_CHEST.open(player);
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHEST_2418, ObjectId.CHEST_26193 };
    }
}
