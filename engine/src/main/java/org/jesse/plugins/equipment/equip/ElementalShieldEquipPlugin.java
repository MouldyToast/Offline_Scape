package org.jesse.plugins.equipment.equip;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ElementalShieldEquipPlugin implements EquipPlugin {

    private static final Animation anim = new Animation(3996);
    private static final Graphics gfx = new Graphics(244, 15, 96);
    private static final SoundEffect sound = new SoundEffect(1539);

    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        player.sendSound(sound);
        if (slotId == -1)
            return true;
        player.setGraphics(gfx);
        player.setAnimation(anim);
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.ELEMENTAL_SHIELD };
    }

}
