package org.jesse.game.content.zahur;

import org.jesse.game.content.consumables.Drinkable;
import org.jesse.game.item.Item;
import mgi.types.config.items.ItemDefinitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tommeh | 16-3-2019 | 00:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PotionResult {
    private final List<Item> items;
    private final Drinkable drinkable;

    public PotionResult(final Drinkable drinkable, final Item... items) {
        this.items = new ArrayList<>(Arrays.asList(items));
        this.drinkable = drinkable;
    }

    public void add(final Item item) {
        items.add(item);
    }

    public int getTotalDose() {
        int dose = 0;
        for (final Item item : items) {
            final ItemDefinitions def = item.getDefinitions();
            final int id = def.getUnnotedOrDefault();
            dose += drinkable.getDoses(id) * item.getAmount();
        }
        return dose;
    }

    public List<Item> getItems() {
        return items;
    }

}
