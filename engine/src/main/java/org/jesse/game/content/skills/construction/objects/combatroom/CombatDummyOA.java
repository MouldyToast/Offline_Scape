package org.jesse.game.content.skills.construction.objects.combatroom;

import org.jesse.game.content.skills.construction.CombatDummyNPC;
import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 6. march 2018 : 16:17.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CombatDummyOA implements ObjectInteraction {

    private static final Animation ANIM = new Animation(834);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.COMBAT_DUMMY, 29337 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (option.equals("attach")) {
            player.setAnimation(ANIM);
            WorldTasksManager.schedule(() -> {
                World.removeObject(object);
                new CombatDummyNPC(object.getId() == 29336 ? 2668 : 7413, object).spawn();
            });
        }
    }
}
