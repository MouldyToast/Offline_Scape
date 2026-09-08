package org.jesse.game.content.skills.hunter.node.tables;

import org.jesse.game.content.treasuretrails.ClueItem;
import org.jesse.game.content.treasuretrails.ClueLevel;
import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.area.Entrana;

import java.util.List;

/**
 * @author Kris | 22/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LuckyImplingJarTable implements ImplingJarTable {
    public static final LuckyImplingJarTable table = new LuckyImplingJarTable();
    private static final ClueLevel[] validLevels = new ClueLevel[] {ClueLevel.EASY, ClueLevel.MEDIUM, ClueLevel.HARD, ClueLevel.ELITE, ClueLevel.MASTER};

    @Override
    public Item roll() {
        return null;
    }

    public Item[] rollRewards(Player player) {
        final List<Item> itemsList = Utils.getRandomElement(validLevels).getTable().roll(1, 1, player.inArea(Entrana.class), false);
        if (Utils.random(20) == 0) {
            Item clue = new Item(ClueItem.MASTER.getScrollBox());
            itemsList.add(clue);
        }

        return itemsList.toArray(new Item[0]);
    }
}
