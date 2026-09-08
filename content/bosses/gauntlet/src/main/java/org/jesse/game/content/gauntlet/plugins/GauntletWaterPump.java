package org.jesse.game.content.gauntlet.plugins;

import org.jesse.game.content.gauntlet.actions.FillVialAction;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public final class GauntletWaterPump implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new FillVialAction());
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getActionManager().setAction(new FillVialAction());
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 23879 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 36078, 35981 };
    }

}
