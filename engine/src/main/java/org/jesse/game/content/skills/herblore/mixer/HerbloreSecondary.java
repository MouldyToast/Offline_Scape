package org.jesse.game.content.skills.herblore.mixer;

import org.jesse.game.item.ids.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-22
 */
enum HerbloreSecondary {

    UNICORN_HORN(237, 235),
    CHOCOLATE_BAR(1973, 1975),
    KEBBIT_TEETH(10109, 10111),
    GORAK_CLAWS(9016, 9018),
    BIRD_NEST(5075, 6693),
    DESERT_GOAT_HORN(9735, 9736),
    SPRING_SQIRK(10844, 10848),
    SUMMER_SQIRK(10845, 10849),
    AUTUMN_SQIRK(10846, 10850),
    WINTER_SQIRK(10847, 10851),
    CHARCOAL(973, 704),
    RUNE_SHARDS(6466, 6467),
    ASHES(592, 8865),
    RAW_KARAMBWAN(3142, 3152),
    POISON_KARAMBWAN(3146, 3153),
    COOKED_KARAMBWAN(3147, 3154),
    LAVA_SCALE(11992, 11994),
    BLUE_DRAGON_SCALE(ItemId.BLUE_DRAGON_SCALE, ItemId.DRAGON_SCALE_DUST),
    SUPERIOR_DRAGON_BONES(22124, 21975);

    final int rawItem;
    final int processedItem;
    final int rawNotedItem;
    final int processedNotedItem;

    static final HerbloreSecondary[] values = values();

    HerbloreSecondary(final int rawItem, final int processedItem) {
        this.rawItem = rawItem;
        this.processedItem = processedItem;
        final int notedRaw = ItemDefinitions.getOrThrow(rawItem).getNotedOrDefault();
        final int notedProcessed = ItemDefinitions.getOrThrow(processedItem).getNotedOrDefault();
        this.rawNotedItem = notedRaw == rawItem ? -1 : notedRaw;
        this.processedNotedItem = notedProcessed == processedItem ? -1 : notedProcessed;
    }
}
