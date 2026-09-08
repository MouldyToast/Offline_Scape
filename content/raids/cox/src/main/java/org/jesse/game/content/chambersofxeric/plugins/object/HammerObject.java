package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 31/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HammerObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to take a hammer.");
                return;
            }
            player.setAnimation(new Animation(827));
            player.sendSound(new SoundEffect(2582));
            player.getInventory().addItem(ItemId.HAMMER, 1);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HAMMER_29711 };
    }
}
