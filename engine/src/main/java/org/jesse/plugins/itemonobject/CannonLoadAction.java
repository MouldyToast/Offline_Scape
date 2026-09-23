package org.jesse.plugins.itemonobject;

import org.jesse.game.content.multicannon.DwarfMultiCannon;
import org.jesse.game.content.multicannon.DwarfMultiCannonType;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 1/23/2020
 */
@SuppressWarnings("unused")
public class CannonLoadAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (object.equals(DwarfMultiCannon.placedCannons.get(player.getUsername()))) {
            player.getDwarfMulticannon().loadCannon();
        } else {
            player.sendMessage("This is not your cannon.");
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.GRANITE_CANNONBALL, ItemId.STEEL_CANNONBALL };
    }

    @Override
    public Object[] getObjects() {
        DwarfMultiCannonType type1 = DwarfMultiCannonType.REGULAR;
        DwarfMultiCannonType type2 = DwarfMultiCannonType.ORNAMENT;
        return new Object[] {
                type1.getBaseLoc(), type1.getStandLoc(), type1.getBarrelsLoc(), type1.getCannonLoc(), type1.getBrokenCannonLoc(),
                type2.getBaseLoc(), type2.getStandLoc(), type2.getBarrelsLoc(), type2.getCannonLoc(), type2.getBrokenCannonLoc(),
        };
    }

}
