package org.jesse.game.content.skills.hunter.plugins;

import org.jesse.game.content.skills.hunter.object.Birdhouse;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 25/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicOrbOnBirdhouse implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        final Birdhouse birdhouse = player.getHunter().findBirdhouse(object.getId()).orElseThrow(RuntimeException::new);
        final long fillTime = birdhouse.getFillTime();
        if (fillTime != 0 && fillTime != Long.MAX_VALUE) {
            birdhouse.setFillTime(1);
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.MAGICAL_ORB_A };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 30565, 30566, 30567, 30568 };
    }
}
