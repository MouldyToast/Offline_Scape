package org.jesse.game.content.skills.hunter.plugins;

import org.jesse.game.content.skills.hunter.node.TrapType;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.flooritem.FloorItem;
import org.jesse.plugins.flooritem.FloorItemPlugin;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 26/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HunterTrapItem extends ItemPlugin implements FloorItemPlugin {
    @Override
    public void handle() {
        bind("Lay", (player, item, slotId) -> TrapType.findByItem(item.getId()).ifPresent(type -> player.getActionManager().setAction(new ItemTrapSetupAction(type, null, slotId, new Location(player.getLocation())))));
    }

    @Override
    public void handle(Player player, FloorItem item, int optionId, String option) {
        if (option.equalsIgnoreCase("Lay")) {
            TrapType.findByItem(item.getId()).ifPresent(type -> player.getActionManager().setAction(new ItemTrapSetupAction(type, item, -1, new Location(player.getLocation()))));
        }
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final TrapType type : TrapType.getValues()) {
            final int id = type.getItemId();
            if (id < 0) {
                continue;
            }
            list.add(id);
        }
        return list.toIntArray();
    }
}
