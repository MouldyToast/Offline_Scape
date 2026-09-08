package org.jesse.game.content.drops;

import org.jesse.game.content.drops.table.DropTable;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Fossil_drop_table>Fossil drop table</a>
 * @since 24/11/2019
 */
public class FossilTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.UNIDENTIFIED_SMALL_FOSSIL, 10)
                .append(ItemId.UNIDENTIFIED_MEDIUM_FOSSIL, 5)
                .append(ItemId.UNIDENTIFIED_LARGE_FOSSIL, 4)
                .append(ItemId.UNIDENTIFIED_RARE_FOSSIL, 1);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
