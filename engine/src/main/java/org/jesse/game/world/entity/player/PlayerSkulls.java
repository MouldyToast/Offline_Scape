package org.jesse.game.world.entity.player;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.content.lootkeys.LootkeyConstants;
import org.jesse.game.world.entity.player.variables.TickVariable;

public enum PlayerSkulls {

    NONE(-1),
    DEFAULT(0, 3),
    RED_DEFAULT(1),
    HIGH_RISK(2),
    LOOT_KEY_1(8, 15),
    LOOT_KEY_2(9, 16),
    LOOT_KEY_3(10, 17),
    LOOT_KEY_4(11, 18),
    LOOT_KEY_5(12, 19),
    BLACK_SKULL(20);

    private final int skullStatusId;
    private final int forinthrySurgeSkullStatusId;

    PlayerSkulls(int skullStatusId, int forinthrySurgeSkullStatusId) {
        this.skullStatusId = skullStatusId;
        this.forinthrySurgeSkullStatusId = forinthrySurgeSkullStatusId;
    }

    PlayerSkulls(int skullStatusId) {
        this.skullStatusId = skullStatusId;
        this.forinthrySurgeSkullStatusId = skullStatusId;
    }

    public static int getSkull(Player player) {

        var skulled = player.getVariables().isSkulled();
        var skull = getLootKeySkull(player);
        if (skull != NONE) {
            return skull.skullStatusId;
        }

        if (skulled) {
            if (PlayerAttributesKt.getBlackSkulled(player)) {
                return BLACK_SKULL.skullStatusId;
            }
            int time = player.getVariables().getTime(TickVariable.FORINTHRY_SURGE);
            if (time > 0) {
                // If the player has Forinthry surge active, then use that skull instead
                return skull.forinthrySurgeSkullStatusId;
            }
            return DEFAULT.skullStatusId;
        }

        return NONE.skullStatusId;
    }

    private static PlayerSkulls getLootKeySkull(Player player) {
        var keys = player.getInventory().getAmountOf(LootkeyConstants.LOOT_KEY_ORDER);
        return switch ((int) keys) {
            case 0 -> NONE;
            case 1 -> LOOT_KEY_1;
            case 2 -> LOOT_KEY_2;
            case 3 -> LOOT_KEY_3;
            case 4 -> LOOT_KEY_4;
            default -> LOOT_KEY_5;
        };
    }

}
