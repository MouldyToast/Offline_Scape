package org.jesse.game.content.minigame.barrows;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.TemporaryDoubleDoor;
import org.jesse.game.world.object.WorldObject;
import org.apache.commons.lang3.ArrayUtils;

public class BarrowsDoor implements ObjectAction {

    private static final int[] puzzleDoorTiles = new int[] { Location.hash(3551, 9683, 0), Location.hash(3552, 9683, 0), Location.hash(3540, 9695, 0), Location.hash(3540, 9694, 0), Location.hash(3552, 9706, 0), Location.hash(3551, 9706, 0), Location.hash(3563, 9694, 0), Location.hash(3563, 9695, 0) };

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (ArrayUtils.contains(puzzleDoorTiles, player.getLocation().getPositionHash())) {
            if (!player.getBarrows().isPuzzleSolved()) {
                player.sendMessage("The door is locked with a strange puzzle.");
                player.getBarrows().getPuzzle().reset();
                GameInterface.BARROWS_PUZZLE.open(player);
                return;
            }
        }
        TemporaryDoubleDoor.executeBarrowsDoors(player, object, location -> player.getBarrows().sendRandomTarget(location));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_20679, ObjectId.DOOR_20680, 20681, 20682, 20683, 20684, 20685, 20686, 20687, 20688, 20689, 20690, 20691, 20692, 20693, 20694, 20695, 20696, ObjectId.DOOR_20698, ObjectId.DOOR_20699, 20700, 20701, 20702, 20703, 20704, 20705, 20706, 20707, 20708, 20709, 20710, 20711, 20712, 20713, 20714, 20715, 20717 };
    }

}
