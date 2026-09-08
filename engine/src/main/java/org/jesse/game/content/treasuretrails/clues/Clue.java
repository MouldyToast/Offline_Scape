package org.jesse.game.content.treasuretrails.clues;

import org.jesse.game.content.treasuretrails.ClueType;
import org.jesse.game.content.treasuretrails.TreasureTrailType;
import org.jesse.game.content.treasuretrails.challenges.ClueChallenge;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 06/04/2019 17:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface Clue extends ClueType {

    void view(@NotNull final Player player, @NotNull final Item item);

    default TreasureTrailType getType() {
        return null;
    }

    default String getEnumName() {
        return null;
    }

    default String getText() {
        return null;
    }

    ClueChallenge getChallenge();

}
