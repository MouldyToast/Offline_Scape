package org.jesse.game.content.skills.agility.wildernesscourse;

import org.jesse.game.content.skills.agility.AbstractAgilityCourse;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;

import java.util.function.Consumer;

/**
 * @author Tommeh | 24 feb. 2018 : 23:24:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class WildernessCourse extends AbstractAgilityCourse {

    private final Consumer<Player> completeConsumer = player -> player.getInventory().addItem(new Item(ItemId.BLOOD_MONEY, Utils.random(2, 6)));

    @Override
    public double getAdditionalCompletionXP() {
        return 499;
    }

    @Override
    public Consumer<Player> onComplete() {
        return completeConsumer;
    }

}
