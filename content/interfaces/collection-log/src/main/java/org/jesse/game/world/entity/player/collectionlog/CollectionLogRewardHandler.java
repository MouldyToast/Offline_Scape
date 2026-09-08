package org.jesse.game.world.entity.player.collectionlog;

import org.jesse.game.content.collectionlog.CollectionLogRewards;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import static org.jesse.game.world.entity.player.collectionlog.CollectionLogConstants.*;
import static org.jesse.game.world.entity.player.collectionlog.CollectionLogInterface.*;

public class CollectionLogRewardHandler {

    public static Boolean isCompleted(Player player, ObjectSet<Int2IntMap.Entry> logItems) {
        return logItems.stream()
            .map(entry -> new Item(entry.getIntValue()))
            .allMatch(item -> player.getCollectionLog().getContainer().contains(item));
    }

    public static void evaluateAndSend(int struct, Player player, CLCategoryType type) {
        var logItems = getCollectionLogItems(struct);
        var completed = isCompleted(player, logItems);
        var hasClaimed = player.getCollectionLogRewardManager().hasClaimed(struct);
        player.addTemporaryAttribute(validation_select, struct);
        var rewards = CollectionLogRewards.getRewardSet(struct);
        if (hasClaimed)
            player.addTemporaryAttribute(validation_key, 3);
        else if (completed)
            player.addTemporaryAttribute(validation_key, 2);
        else
            player.addTemporaryAttribute(validation_key, 1);
        checkCompletedLogs(player, getEnum(type));
        player.getPacketDispatcher().sendClientScript(34021, completed ? 1 : 0, hasClaimed ? 1 : 0, rewards.getItem0(), rewards.getQuantity0(), rewards.getItem1(), rewards.getQuantity1(), rewards.getItem2(), rewards.getQuantity2(), rewards.getItem3(), rewards.getQuantity3());
    }

    private static void checkCompletedLogs(Player player, IntEnum categoryEnum) {
        IntStream.range(0, categoryEnum.getSize())
            .mapToObj(categoryEnum::getValue)
            .filter(OptionalInt::isPresent)
            .mapToInt(OptionalInt::getAsInt)
            .forEach(structId -> sendVarbitForCompletedLog(player, structId));
    }

    private static void sendVarbitForCompletedLog(Player player, int structId) {
        var varbit = CollectionLogVarManager.getVarbitFromStrut(structId);
        if (varbit == -1) return;
        if (CollectionLogRewardHandler.isCompleted(player, getCollectionLogItems(structId)))
            player.getVarManager().sendBit(varbit, 1);
    }

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
