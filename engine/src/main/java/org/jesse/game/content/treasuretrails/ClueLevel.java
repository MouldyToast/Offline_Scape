package org.jesse.game.content.treasuretrails;

import org.jesse.game.content.treasuretrails.rewards.ClueReward;
import org.jesse.game.content.treasuretrails.rewards.ClueRewardTable;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.player.Emote;
import org.jesse.game.world.entity.player.Player;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Kris | 29. march 2018 : 20:04.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */

public enum ClueLevel {

    BEGINNER(new IntRange(1, 1), ClueReward.getBeginnerTable(), -1, null),
    EASY(new IntRange(1, 1), ClueReward.getEasyTable(), 500, player -> addItem(player, new Item(ItemId.LARGE_SPADE))),
    MEDIUM(new IntRange(1, 2), ClueReward.getMediumTable(), 400, player -> addItem(player, new Item(ItemId.CLUELESS_SCROLL))),
    HARD(new IntRange(2, 3), ClueReward.getHardTable(), 300, player -> {
        if (!player.getEmotesHandler().isUnlocked(Emote.URI_TRANSFORM)) {
            player.getEmotesHandler().unlock(Emote.URI_TRANSFORM);
            player.sendMessage(Colour.RED.wrap("Congratulations! You have unlocked the Uri Transform milestone reward!"));
        }
    }),
    ELITE(new IntRange(3, 4), ClueReward.getEliteTable(), 200, player -> addItem(player, new Item(ItemId.HEAVY_CASKET))),
    MASTER(new IntRange(4, 5), ClueReward.getMasterTable(), 100, player -> addItem(player, new Item(ItemId.SCROLL_SACK)));

	private final IntRange stepsRange;
	private final ClueRewardTable table;
	private final int milestoneRequirement;
	private final Consumer<Player> milestoneRewardConsumer;

    public static ClueLevel[] values = values();

    ClueLevel(IntRange stepsRange, ClueRewardTable table, int milestoneRequirement, Consumer<Player> milestoneRewardConsumer) {
        this.stepsRange = stepsRange;
        this.table = table;
        this.milestoneRequirement = milestoneRequirement;
        this.milestoneRewardConsumer = milestoneRewardConsumer;
    }

    private static final void addItem(@NotNull final Player player, @NotNull final Item item) {
        if (player.containsItem(item.getId())) {
            return;
        }
        player.sendMessage(Colour.RED.wrap("Congratulations! You have unlocked the " + item.getName().toLowerCase() + " milestone reward."));
        player.getInventory().addOrDrop(item);
        player.getCollectionLog().add(item);
    }

    public IntRange getStepsRange() {
        return stepsRange;
    }

    public ClueRewardTable getTable() {
        return table;
    }

    public int getMilestoneRequirement() {
        return milestoneRequirement;
    }

    public Consumer<Player> getMilestoneRewardConsumer() {
        return milestoneRewardConsumer;
    }

}
