package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 26/01/2019 17:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkAltar implements ObjectAction {

    private static final Animation PRAY_ANIM = new Animation(645);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Venerate")) {
            final Inventory inventory = player.getInventory();
            int count = 0;
            for (int i = 0; i < 28; i++) {
                final Item item = inventory.getItem(i);
                if (item == null || item.getId() != 13445)
                    continue;
                item.setId(13446);
                count++;
            }
            if (count == 0) {
                player.sendMessage("You haven\'t got any dense essence blocks.");
                return;
            }
            player.getSkills().addXp(SkillConstants.RUNECRAFTING, 2.5F * count);
            player.getPrayerManager().drainPrayerPoints(count);
            player.setAnimation(PRAY_ANIM);
            inventory.refreshAll();
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DARK_ALTAR };
    }
}
