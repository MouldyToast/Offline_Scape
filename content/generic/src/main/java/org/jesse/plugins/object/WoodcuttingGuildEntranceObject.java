package org.jesse.plugins.object;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.DoubleDoor;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 6 jun. 2018 | 17:10:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WoodcuttingGuildEntranceObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final boolean inside = player.inArea("Woodcutting Guild");
        if (!inside && player.getSkills().getLevel(SkillConstants.WOODCUTTING) < 60) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Woodcutting level of at least 60 to enter the guild."));
            return;
        }
        final DoubleDoor gate = DoubleDoor.handleGraphicalDoubleDoor(player, object, null);
        final int objX = object.getX();
        final int objY = object.getY();
        player.lock();
        player.setRunSilent(true);
        player.addWalkSteps(objX, objY, -1, false);
        player.addWalkSteps(objX + (inside ? (objX == 1562 ? 0 : 1) : (objX == 1562 ? 1 : 0)), objY, -1, false);
        WorldTasksManager.schedule(() -> {
            player.unlock();
            player.setRunSilent(false);
            DoubleDoor.handleGraphicalDoubleDoor(player, object, gate);
        }, player.getWalkSteps().size());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_28851, ObjectId.GATE_28852 };
    }
}
