package org.jesse.game.content.consumables;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ConsumableAnimation {

    public static final Animation EAT_ANIM = new Animation(829);
    public static final Animation EAT_ON_SLED_ANIM = new Animation(1469);

    @NotNull
    public static Animation getEatAnimation(Player player) {
        return player.getEquipment().containsItem(ItemId.SLED)
                ? EAT_ON_SLED_ANIM
                : EAT_ANIM;
    }

    @NotNull
    public static Animation transformEatAnimation(Player player, Animation baseAnimation) {
        return player.getEquipment().containsItem(ItemId.SLED)
                ? EAT_ON_SLED_ANIM
                : baseAnimation;
    }
}
