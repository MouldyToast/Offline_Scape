package org.jesse.game.content.skills.construction.objects.costumeroom;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.content.skills.construction.costume.CapeRackData;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 27. veebr 2018 : 19:39.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CapeRackOA implements ObjectInteraction, ItemOnObjectAction {

    private static final Animation ANIM = new Animation(834);

    @Override
    public Object[] getObjects() {
        return new Object[] { 18766, 18767, 18768, 18769, 18770, ObjectId.MAGIC_CAPE_RACK };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("search")) {
            if (player.getCurrentHouse() != construction) {
                player.sendMessage("Only the owner of the house can use the cape rack.");
                return;
            }
            construction.getCapeRack().open();
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (CapeRackData.MAP.get(item.getId()) == null) {
            player.sendMessage("You can't put this on the cape rack.");
            return;
        }
        if (player.getConstruction().getCapeRack().addSet(item.getId())) {
            player.setAnimation(ANIM);
            return;
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }
}
