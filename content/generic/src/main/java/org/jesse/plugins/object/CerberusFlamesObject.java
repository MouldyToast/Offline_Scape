package org.jesse.plugins.object;

import org.jesse.game.content.boss.cerberus.CerberusRoom;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 3 mei 2018 | 21:36:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class CerberusFlamesObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final CerberusRoom position = object.getX() >= 1239 && object.getX() <= 1241 ? CerberusRoom.WEST : object.getX() >= 1367 && object.getX() <= 1369 ? CerberusRoom.EAST : CerberusRoom.NORTH;
        final Location destination = player.getY() > position.getExit().getY() + 16 ? new Location(player.getLocation().transform(0, -2, 0)) : new Location(player.getLocation().transform(0, 2, 0));
        if (optionId == 1) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Do you wish to pass through the flames?", "Yes - I know I'll get hurt.", "No way!").onOptionOne(() -> walk(player, destination));
                }
            });
        }
        if (optionId == 2)
            walk(player, destination);
    }

    private void walk(final Player player, final Location destination) {
        player.setRunSilent(true);
        player.addWalkSteps(destination.getX(), destination.getY(), 2, false);
        player.applyHit(new Hit(null, 5, HitType.REGULAR));
        WorldTasksManager.schedule(() -> player.setRunSilent(false));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.FLAMES };
    }
}
