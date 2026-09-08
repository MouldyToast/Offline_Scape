package org.jesse.game.content.skills.construction.objects.workspace;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 21:40.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Workbench implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WORKBENCH_6791, ObjectId.WORKBENCH_6792, ObjectId.WORKBENCH_6793, ObjectId.WORKBENCH_6794, ObjectId.WORKBENCH_6795 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        player.sendMessage("Flatpacks aren't implemented yet.");
    }
}
