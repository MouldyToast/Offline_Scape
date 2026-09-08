package org.jesse.game.content.skills.construction.objects.superiorgarden;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.WorldObject;

import static org.jesse.game.obj.ids.ObjectId.*;

/**
 * @author Kris | 26. veebr 2018 : 4:21.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Pool implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { POOL_OF_RESTORATION, POOL_SPACE, POOL_OF_REJUVENATION };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("drink")) {
            final int id = object.getId();
            if (id == POOL_OF_RESTORATION)
                drink(player, 0);
            else if (id == POOL_OF_REJUVENATION)
                drink(player, 2);
            else if (id == POOL_SPACE)
                drink(player, 3);
        }
    }

    private void drink(final Player player, final int type) {
        if (type >= 0)
            player.getCombatDefinitions().setSpecialEnergy(100);
        if (type >= 1)
            player.getVariables().setRunEnergy(100);
        if (type >= 2)
            player.getPrayerManager().restorePrayerPoints(99);
        if (type >= 3) {
            for (int i = 0; i < 22; i++) {
                if (i == SkillConstants.HITPOINTS || i == SkillConstants.PRAYER)
                    continue;
                if (player.getSkills().getLevel(i) < player.getSkills().getLevelForXp(i))
                    player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
            }
        }
        if (type >= 4)
            player.heal(99);
        player.sendMessage("You feel replenished.");
    }
}
