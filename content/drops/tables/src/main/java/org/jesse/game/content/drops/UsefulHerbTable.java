package org.jesse.game.content.drops;

import org.jesse.game.content.drops.table.DropTable;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Useful_herb_drop_table>Useful herb drop table</a>
 * @since 24/11/2019
 */
public class UsefulHerbTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemDefinitions.getOrThrow(ItemId.GRIMY_AVANTOE).getNotedId(), 5)
                .append(ItemDefinitions.getOrThrow(ItemId.GRIMY_SNAPDRAGON).getNotedId(), 4)
                .append(ItemDefinitions.getOrThrow(ItemId.GRIMY_RANARR_WEED).getNotedId(), 4)
                .append(ItemDefinitions.getOrThrow(ItemId.GRIMY_TORSTOL).getNotedId(), 3);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
