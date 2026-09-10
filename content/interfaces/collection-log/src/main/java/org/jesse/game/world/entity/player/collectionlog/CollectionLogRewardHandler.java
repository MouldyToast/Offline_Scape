package org.jesse.game.world.entity.player.collectionlog;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;

import java.util.Objects;

import static org.jesse.game.world.entity.player.collectionlog.CollectionLogConstants.*;

public class CollectionLogRewardHandler {

    public static void forceComplete(int struct, Player player) {
        final ObjectSet<Int2IntMap.Entry> logItems = getCollectionLogItems(struct);
        for(Int2IntMap.Entry entry: logItems) {
            if(!player.getCollectionLog().getContainer().contains(entry.getIntValue(), 1)) {
                player.getCollectionLog().add(new Item(entry.getIntValue()));
            }
        }
    }

    public static ObjectSet<Int2IntMap.Entry> getCollectionLogItems(int struct) {
        final StructDefinitions sub = Objects.requireNonNull(StructDefinitions.get(struct));
        final int subEnumId = Integer.parseInt(sub.getValue(STRUCT_POINTER_SUB_ENUM_CAT).orElseThrow(RuntimeException::new).toString());
        final IntEnum subEnum = EnumDefinitions.getIntEnum(subEnumId);
        return subEnum.getValues().int2IntEntrySet();
    }
}
