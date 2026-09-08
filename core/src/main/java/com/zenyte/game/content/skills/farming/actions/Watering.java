package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchState;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;

/**
 * @author Kris | 03/02/2019 22:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Watering extends Action {
    public static final Animation WATERING_ANIM = new Animation(2293);
    public static final Graphics WATERING_GRAPHICS = new Graphics(410, 0, 92);
    public static final SoundEffect WATERING_SOUND = new SoundEffect(2446, 0, 0);
    private final Item item;
    private final FarmingSpot spot;

    public Watering(final Item item, final FarmingSpot spot) {
        this.item = item;
        this.spot = spot;
    }

    @Override
    public boolean start() {
        if (spot.getState() != PatchState.GROWING || spot.getProduct().getWateredStage() == null) {
            player.sendMessage("This patch doesn't need watering.");
            return false;
        }
        player.setAnimation(WATERING_ANIM);
        player.setGraphics(WATERING_GRAPHICS);
        player.getPacketDispatcher().sendSoundEffect(WATERING_SOUND);
        delay(2);
        return true;
    }

    @Override
    public boolean process() {
        return spot.getState().equals(PatchState.GROWING);
    }

    @Override
    public int processWithDelay() {
        final int id = item.getId();
        if (id != ItemId.GRICOLLERS_CAN) {
            assert id >= ItemId.WATERING_CAN1 && id <= ItemId.WATERING_CAN8;
            player.getInventory().deleteItem(item);
            player.getInventory().addItem(id == ItemId.WATERING_CAN1 ? ItemId.WATERING_CAN : (id - 1), 1);
        }
        spot.setWatered();
        return -1;
    }
}
