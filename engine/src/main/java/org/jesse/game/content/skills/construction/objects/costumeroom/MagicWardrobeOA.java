package org.jesse.game.content.skills.construction.objects.costumeroom;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.content.skills.construction.costume.MagicWardrobeData;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 27. veebr 2018 : 19:39.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MagicWardrobeOA implements ObjectInteraction, ItemOnObjectAction {

    private static final Animation ANIM = new Animation(834);

    @Override
    public Object[] getObjects() {
        return new Object[] { 18784, ObjectId.MAGIC_WARDROBE_18785, 18786, ObjectId.MAGIC_WARDROBE_18787, 18788, ObjectId.MAGIC_WARDROBE_18789, 18790, ObjectId.MAGIC_WARDROBE_18791, 18792, ObjectId.MAGIC_WARDROBE_18793, 18794, ObjectId.MAGIC_WARDROBE_18795, ObjectId.MAGIC_WARDROBE_18796, ObjectId.MAGIC_WARDROBE_18797 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("open")) {
            World.spawnObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("close")) {
            World.spawnObject(new WorldObject(object.getId() - 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("search")) {
            if (player.getCurrentHouse() != construction) {
                player.sendMessage("Only the owner of the house can use the magic wardrobe.");
                return;
            }
            construction.getMagicWardrobe().open();
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (MagicWardrobeData.MAP.get(item.getId()) == null) {
            player.sendMessage("You can't put this in the magic wardrobe.");
            return;
        }
        final int id = object.getId();
        if (id % 2 == 0) {
            player.sendMessage("You need to open the magic wardrobe first.");
            return;
        }
        if (player.getConstruction().getMagicWardrobe().addSet(item.getId())) {
            player.setAnimation(ANIM);
            return;
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }
}
