package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.firemaking.BonfireAction;
import org.jesse.game.content.skills.firemaking.Firemaking;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.WorldObject;

public class AnyLogsOnFlamesAction implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        Firemaking logs = null;
        for (Firemaking value : Firemaking.VALUES) {
            if(item.getId() == value.getLogs().getId())
                logs = value;
        }
        if(logs != null && logs.getLevel() > player.getSkills().getLevel(SkillConstants.FIREMAKING)) {
            player.sendMessage("You lack the required firemaking level to light these.");
            return;
        }
        if(logs != null)
            player.getActionManager().setAction(new BonfireAction(logs));
        else
            player.sendMessage("You don't have any logs to add to the fire!");
    }

    @Override
    public Object[] getItems() {
        return Firemaking.MAP.keySet().toArray(new Integer[0]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{"Fire", "Stove", "Bonfire", "Oven", "Furnace", "Brazier"};
    }
}
