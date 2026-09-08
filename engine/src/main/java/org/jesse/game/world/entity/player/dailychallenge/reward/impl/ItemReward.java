package org.jesse.game.world.entity.player.dailychallenge.reward.impl;

import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dailychallenge.reward.ChallengeReward;
import org.jesse.game.world.entity.player.dailychallenge.reward.RewardType;

/**
 * @author Tommeh | 04/05/2019 | 13:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ItemReward implements ChallengeReward {

    private final Item item;

    public ItemReward(final int id, final int amount) {
        this.item = new Item(id, amount);
    }

    @Override
    public void apply(Player player) {
        player.getInventory().addItem(item);
        player.sendMessage("<col=ce8500><shad=000000>You have been awarded with " + Utils.formatNumberWithCommas(item.getAmount()) + " x " + item.getName() + "!");
    }

    @Override
    public RewardType getType() {
        return RewardType.ITEM;
    }

    public Item getItem() {
        return item;
    }
}
