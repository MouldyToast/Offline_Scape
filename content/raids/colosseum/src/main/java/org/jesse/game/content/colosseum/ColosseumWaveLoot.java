package org.jesse.game.content.colosseum;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;

/**
 * Per-wave drop tables for the Fortis Colosseum (waves 1–12).
 * <p>
 * Each wave has a flat weighted table. One roll per wave completion produces
 * exactly one Item.  Wave 12 additionally grants a guaranteed Dizana's quiver
 * and a 1/200 Smol Heredit pet roll (handled externally in ColosseumInstance).
 * <p>
 * Sources: OSRS Wiki "Rewards Chest (Fortis Colosseum)", all weights verified.
 */
public final class ColosseumWaveLoot {

    private ColosseumWaveLoot() {}

    // Sentinel: when rolled, resolve to an unowned Sunfire Fanatic piece
    private static final int FANATIC_PIECE = -1;

    // ── Drop entry ──────────────────────────────────────────────────────
    private record Drop(int itemId, int minQty, int maxQty, int weight) {
        Drop(int itemId, int qty, int weight) { this(itemId, qty, qty, weight); }
    }

    // ── Wave tables ─────────────────────────────────────────────────────
    // Wave 1: guaranteed 80 sunfire splinters, no table.

    private static final Drop[] WAVE_2 = { // total 7
        new Drop(ItemId.RUNE_PLATEBODY,    1,  1),
        new Drop(ItemId.DEATH_RUNE,      150,  1),
        new Drop(ItemId.CHAOS_RUNE,      150,  1),
        new Drop(ItemId.SUNFIRE_SPLINTERS,150, 1),
        new Drop(ItemId.CANNONBALL,       80,  1),
        new Drop(ItemId.RUNE_KITESHIELD,   4,  1),
        new Drop(ItemId.RUNE_CHAINBODY,    1,  1),
    };

    private static final Drop[] WAVE_3 = { // total 70
        // Common (9 each × 7 = 63)
        new Drop(ItemId.RUNE_PLATEBODY,    1,  9),
        new Drop(ItemId.DEATH_RUNE,      150,  9),
        new Drop(ItemId.CHAOS_RUNE,      150,  9),
        new Drop(ItemId.SUNFIRE_SPLINTERS,150, 9),
        new Drop(ItemId.CANNONBALL,       80,  9),
        new Drop(ItemId.RUNE_KITESHIELD,   4,  9),
        new Drop(ItemId.RUNE_CHAINBODY,    1,  9),
        // Uncommon (1 each × 7 = 7)
        new Drop(ItemId.ONYX_BOLTS,       30,  1),
        new Drop(ItemId.SNAPDRAGON_SEED,   1,  1),
        new Drop(ItemId.DRAGON_PLATELEGS,  1,  1),
        new Drop(ItemId.SUNFIRE_SPLINTERS,500, 1),
        new Drop(ItemId.EARTH_ORB,        80,  1),
        new Drop(ItemId.RUNE_2H_SWORD,     2,  1),
        new Drop(ItemId.GOLD_ORE,        150,  1),
        // Token (Varlamore) — item not in rev-228 cache, skipped. TODO: add when available.
    };

    private static final Drop[] WAVE_4 = { // total 43,400
        // Common (5,535 each × 7 = 38,745)
        new Drop(ItemId.RUNE_PLATEBODY,     1,  5_535),
        new Drop(ItemId.DEATH_RUNE,       150,  5_535),
        new Drop(ItemId.CHAOS_RUNE,       150,  5_535),
        new Drop(ItemId.SUNFIRE_SPLINTERS,150,  5_535),
        new Drop(ItemId.CANNONBALL,        80,  5_535),
        new Drop(ItemId.RUNE_KITESHIELD,    4,  5_535),
        new Drop(ItemId.RUNE_CHAINBODY,     1,  5_535),
        // Uncommon (615 each × 7 = 4,305)
        new Drop(ItemId.ONYX_BOLTS,        30,    615),
        new Drop(ItemId.SNAPDRAGON_SEED,    1,    615),
        new Drop(ItemId.DRAGON_PLATELEGS,   1,    615),
        new Drop(ItemId.SUNFIRE_SPLINTERS,500,    615),
        new Drop(ItemId.EARTH_ORB,         80,    615),
        new Drop(ItemId.RUNE_2H_SWORD,      2,    615),
        new Drop(ItemId.GOLD_ORE,         150,    615),
        // Unique (350)
        new Drop(ItemId.ECHO_CRYSTAL,       1,    126), // bonus roll: 1/10 for ×2-3
        new Drop(FANATIC_PIECE,             1,    210), // 70×3 combined
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,        14),
    };

    private static final Drop[] WAVE_5 = { // total 19,250
        // Uncommon (2,725 each × 7 = 19,075)
        new Drop(ItemId.ONYX_BOLTS,        30,  2_725),
        new Drop(ItemId.SNAPDRAGON_SEED,    1,  2_725),
        new Drop(ItemId.DRAGON_PLATELEGS,   1,  2_725),
        new Drop(ItemId.SUNFIRE_SPLINTERS,500,  2_725),
        new Drop(ItemId.EARTH_ORB,         80,  2_725),
        new Drop(ItemId.RUNE_2H_SWORD,      2,  2_725),
        new Drop(ItemId.GOLD_ORE,         150,  2_725),
        // Unique (175)
        new Drop(ItemId.ECHO_CRYSTAL,       1,     63),
        new Drop(FANATIC_PIECE,             1,    105), // 35×3
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,         7),
    };

    private static final Drop[] WAVE_6 = { // total 16,800
        // Uncommon (2,375 each × 7 = 16,625)
        new Drop(ItemId.ONYX_BOLTS,        30,  2_375),
        new Drop(ItemId.SNAPDRAGON_SEED,    1,  2_375),
        new Drop(ItemId.DRAGON_PLATELEGS,   1,  2_375),
        new Drop(ItemId.SUNFIRE_SPLINTERS,500,  2_375),
        new Drop(ItemId.EARTH_ORB,         80,  2_375),
        new Drop(ItemId.RUNE_2H_SWORD,      2,  2_375),
        new Drop(ItemId.GOLD_ORE,         150,  2_375),
        // Unique (175)
        new Drop(ItemId.ECHO_CRYSTAL,       1,     63),
        new Drop(FANATIC_PIECE,             1,    105), // 35×3
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,         7),
    };

    private static final Drop[] WAVE_7 = { // total 45,920
        // Common (5,832 each × 7 = 40,824)
        new Drop(ItemId.ONYX_BOLTS,         30,  5_832),
        new Drop(ItemId.SNAPDRAGON_SEED,     1,  5_832),
        new Drop(ItemId.DRAGON_PLATELEGS,    1,  5_832),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 500,  5_832),
        new Drop(ItemId.EARTH_ORB,          80,  5_832),
        new Drop(ItemId.RUNE_2H_SWORD,       2,  5_832),
        new Drop(ItemId.GOLD_ORE,          150,  5_832),
        // Mid (756 each × 6 = 4,536)
        new Drop(ItemId.DRAGON_BOLTS_UNF,  200,    756),
        new Drop(ItemId.RANARR_SEED,         4,    756),
        new Drop(ItemId.SUNFIRE_SPLINTERS,1_100,   756),
        new Drop(ItemId.DRAGON_ARROWTIPS,  150,    756),
        new Drop(ItemId.ADAMANTITE_ORE,    100,    756),
        new Drop(ItemId.DEATH_RUNE,        250,    756),
        // Unique (560) — Tonalztics enters
        new Drop(ItemId.ECHO_CRYSTAL,        1,    189),
        new Drop(FANATIC_PIECE,              1,    315), // 105×3
        new Drop(ItemId.TONALZTICS_OF_RALOS_UNCHARGED, 1, 35),
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,         21),
    };

    private static final Drop[] WAVE_8 = { // total 114,240
        // Common (14,472 each × 7 = 101,304)
        new Drop(ItemId.ONYX_BOLTS,          30, 14_472),
        new Drop(ItemId.SNAPDRAGON_SEED,      1, 14_472),
        new Drop(ItemId.DRAGON_PLATELEGS,     1, 14_472),
        new Drop(ItemId.SUNFIRE_SPLINTERS,  500, 14_472),
        new Drop(ItemId.EARTH_ORB,           80, 14_472),
        new Drop(ItemId.RUNE_2H_SWORD,        2, 14_472),
        new Drop(ItemId.GOLD_ORE,           150, 14_472),
        // Mid (1,876 each × 6 = 11,256)
        new Drop(ItemId.ONYX_BOLTS,          50,  1_876),
        new Drop(ItemId.DRAGON_PLATELEGS,     2,  1_876),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 1_750, 1_876),
        new Drop(ItemId.RUNE_KITESHIELD,      5,  1_876),
        new Drop(ItemId.RUNITE_ORE,          12,  1_876),
        new Drop(ItemId.DEATH_RUNE,         300,  1_876),
        // Unique (1,680)
        new Drop(ItemId.ECHO_CRYSTAL,         1,    567),
        new Drop(FANATIC_PIECE,               1,    945), // 315×3
        new Drop(ItemId.TONALZTICS_OF_RALOS_UNCHARGED, 1, 105),
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,          63),
    };

    private static final Drop[] WAVE_9 = { // total 21,600
        // Common (3,180 each × 6 = 19,080)
        new Drop(ItemId.DRAGON_BOLTS_UNF,   200,  3_180),
        new Drop(ItemId.RANARR_SEED,          4,  3_180),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 1_100, 3_180),
        new Drop(ItemId.DRAGON_ARROWTIPS,   150,  3_180),
        new Drop(ItemId.ADAMANTITE_ORE,     100,  3_180),
        new Drop(ItemId.DEATH_RUNE,         250,  3_180),
        // Mid (424 each × 5 = 2,120)
        new Drop(ItemId.ONYX_BOLTS,          75,    424),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 2_500,   424),
        new Drop(ItemId.DRAGON_PLATELEGS,     3,    424),
        new Drop(ItemId.DEATH_RUNE,         300,    424),
        new Drop(ItemId.RUNE_WARHAMMER,       5,    424),
        // Unique (400)
        new Drop(ItemId.ECHO_CRYSTAL,         1,    135),
        new Drop(FANATIC_PIECE,               1,    225), // 75×3
        new Drop(ItemId.TONALZTICS_OF_RALOS_UNCHARGED, 1, 25),
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,          15),
    };

    private static final Drop[] WAVE_10 = { // total 16,000
        // Common (2,340 each × 6 = 14,040)
        new Drop(ItemId.ONYX_BOLTS,          50,  2_340),
        new Drop(ItemId.DRAGON_PLATELEGS,     2,  2_340),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 1_750, 2_340),
        new Drop(ItemId.RUNE_KITESHIELD,      5,  2_340),
        new Drop(ItemId.RUNITE_ORE,          12,  2_340),
        new Drop(ItemId.DEATH_RUNE,         300,  2_340),
        // Mid (312 each × 5 = 1,560)
        new Drop(ItemId.ONYX_BOLTS,         100,    312),
        new Drop(ItemId.RUNE_WARHAMMER,       8,    312),
        new Drop(ItemId.DRAGON_PLATELEGS,     3,    312),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 3_500,   312),
        new Drop(ItemId.DRAGON_ARROWTIPS,   250,    312),
        // Unique (400)
        new Drop(ItemId.ECHO_CRYSTAL,         1,    135),
        new Drop(FANATIC_PIECE,               1,    225), // 75×3
        new Drop(ItemId.TONALZTICS_OF_RALOS_UNCHARGED, 1, 25),
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,          15),
    };

    private static final Drop[] WAVE_11 = { // total 2,080
        // Common (360 each × 5 = 1,800)
        new Drop(ItemId.ONYX_BOLTS,          75,    360),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 2_500,   360),
        new Drop(ItemId.DRAGON_PLATELEGS,     3,    360),
        new Drop(ItemId.DEATH_RUNE,         300,    360),
        new Drop(ItemId.RUNE_WARHAMMER,       5,    360),
        // Mid (50 each × 4 = 200)
        new Drop(ItemId.CANNONBALL,        2_000,    50),
        new Drop(ItemId.DRAGON_PLATESKIRT,     5,    50),
        new Drop(ItemId.DRAGON_ARROWTIPS,    350,    50),
        new Drop(ItemId.ONYX_BOLTS,          150,    50),
        // Unique (80)
        new Drop(ItemId.ECHO_CRYSTAL,          1,    27),
        new Drop(FANATIC_PIECE,                1,    45), // 15×3
        new Drop(ItemId.TONALZTICS_OF_RALOS_UNCHARGED, 1, 5),
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,           3),
    };

    private static final Drop[] WAVE_12 = { // total 4,800
        // Common (792 each × 5 = 3,960)
        new Drop(ItemId.ONYX_BOLTS,         100,    792),
        new Drop(ItemId.RUNE_WARHAMMER,       8,    792),
        new Drop(ItemId.DRAGON_PLATELEGS,     3,    792),
        new Drop(ItemId.SUNFIRE_SPLINTERS, 3_500,   792),
        new Drop(ItemId.DRAGON_ARROWTIPS,   250,    792),
        // Mid (110 each × 4 = 440)
        new Drop(ItemId.UNCUT_ONYX,           1,    110),
        new Drop(ItemId.DRAGON_PLATESKIRT,    5,    110),
        new Drop(ItemId.RUNE_2H_SWORD,        9,    110),
        new Drop(ItemId.RUNITE_ORE,          35,    110),
        // Unique (400)
        new Drop(ItemId.ECHO_CRYSTAL,         1,    135),
        new Drop(FANATIC_PIECE,               1,    225), // 75×3
        new Drop(ItemId.TONALZTICS_OF_RALOS_UNCHARGED, 1, 25),
        new Drop(ItemId.ECHO_CRYSTAL, 2, 3,          15),
    };

    // ── Indexed access ──────────────────────────────────────────────────
    private static final Drop[][] TABLES = {
        null,     // index 0 unused
        null,     // wave 1 is guaranteed (no table roll)
        WAVE_2,  WAVE_3,  WAVE_4,  WAVE_5,  WAVE_6,
        WAVE_7,  WAVE_8,  WAVE_9,  WAVE_10, WAVE_11, WAVE_12,
    };

    private static final int[] TOTAL_WEIGHTS = {
        0, 0, 7, 70, 43_400, 19_250, 16_800,
        45_920, 114_240, 21_600, 16_000, 2_080, 4_800,
    };

    // ── Public API ──────────────────────────────────────────────────────

    /**
     * Roll the drop table for the given wave.  Returns exactly one Item.
     * <p>
     * Wave 12 callers should additionally grant the guaranteed Dizana's quiver
     * and roll for Smol Heredit separately — this method only rolls the
     * regular weighted table.
     */
    public static Item roll(Player player, int wave) {
        if (wave < 1 || wave > 12) {
            throw new IllegalArgumentException("Invalid wave: " + wave);
        }
        if (wave == 1) {
            return new Item(ItemId.SUNFIRE_SPLINTERS, 80);
        }

        Drop[] table = TABLES[wave];
        int totalWeight = TOTAL_WEIGHTS[wave];
        int roll = Utils.random(totalWeight - 1);
        int cumulative = 0;

        for (Drop drop : table) {
            cumulative += drop.weight();
            if (roll < cumulative) {
                return resolve(player, drop);
            }
        }

        // Fallback — should never reach here if weights are correct
        return new Item(ItemId.SUNFIRE_SPLINTERS, 80);
    }

    /**
     * Convenience: calculate the GP value of an item using sell price.
     * Used for the intermission UI CS2 4931 args.
     */
    public static long gpValue(Item item) {
        return (long) item.getSellPrice() * item.getAmount();
    }

    // ── Resolution logic ────────────────────────────────────────────────

    private static Item resolve(Player player, Drop drop) {
        // Sunfire Fanatic piece — dupe-avoidance resolution
        if (drop.itemId() == FANATIC_PIECE) {
            return resolveFanaticPiece(player);
        }

        int qty;
        if (drop.minQty() == drop.maxQty()) {
            qty = drop.minQty();
        } else {
            qty = Utils.random(drop.minQty(), drop.maxQty());
        }

        // Echo crystal bonus roll: ×1 entries get a 1/10 chance of becoming ×2–3
        if (drop.itemId() == ItemId.ECHO_CRYSTAL && drop.maxQty() == 1) {
            if (Utils.random(9) == 0) {
                qty = Utils.random(2, 3);
            }
        }

        return new Item(drop.itemId(), qty);
    }

    /**
     * Sunfire Fanatic dupe-avoidance: give the piece the player owns the
     * fewest of.  Guarantees a full set before duplicates.
     */
    private static Item resolveFanaticPiece(Player player) {
        int[] pieces = {
            ItemId.SUNFIRE_FANATIC_HELM,
            ItemId.SUNFIRE_FANATIC_CUIRASS,
            ItemId.SUNFIRE_FANATIC_CHAUSSES,
        };
        int bestId = pieces[0];
        int bestCount = Integer.MAX_VALUE;
        for (int pieceId : pieces) {
            int owned = player.getAmountOf(pieceId);
            if (owned < bestCount) {
                bestCount = owned;
                bestId = pieceId;
            }
        }
        return new Item(bestId, 1);
    }
}
