package com.zenyte.game.world.object.ladders;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

public class PoisonWasteLadders implements ObjectAction {
    private static final Animation CLIMB_UP = new Animation(828);
    private static final Animation CLIMB_DOWN = new Animation(827);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        Location location;

        switch (option) {
            case "Climb-up":
                location = handleClimbUpCoords(object);
                climb(player, object, location, CLIMB_UP);
                break;
            case "Climb-down":
                location = handleClimbDownCoords(object);
                climb(player, object, location, CLIMB_DOWN);
                break;
            case "Climb":
                player.getDialogueManager().start(new OptionDialogue(player, "Which way do you want to go?", new String[] {
                        "Climb up", "Climb down", "Cancel."
                }, new Runnable[] {
                        () -> climb(player, object, handleClimbUpCoords(object), CLIMB_UP),
                        () -> climb(player, object, handleClimbDownCoords(object), CLIMB_DOWN),
                        null }));
                break;
        }
    }

    private Location handleClimbUpCoords(WorldObject object) {
        switch (object.getId()) {
            case 49700:
                if (object.getX() == 1479 && object.getY() == 4239) return new Location(1479, 4240, 1);
                if (object.getX() == 1529 && object.getY() == 4253) return new Location(1530, 4253, 1);
                if (object.getX() == 1499 && object.getY() == 4282) return new Location(1499, 4283, 1);
                if (object.getX() == 1486 && object.getY() == 4282) return new Location(1486, 4283, 1);
                if (object.getX() == 1529 && object.getY() == 4236) return new Location(1530, 4236, 1);
        }
        return new Location(object.getX(), object.getY(), object.getPlane() + 1);
    }

    private Location handleClimbDownCoords(WorldObject object) {
        switch (object.getId()) {
            case 49701:
                if (object.getX() == 1530 && object.getY() == 4236) return new Location(1529, 4236, 0);
                if (object.getX() == 1486 && object.getY() == 4283) return new Location(1486, 4282, 0);
        }
        return new Location(object.getX(), object.getY(), object.getPlane() - 1);
    }

    private void climb(Player player, WorldObject object, Location location, Animation animation) {
        player.lock(2);
        player.setAnimation(animation);
        WorldTasksManager.schedule(() -> player.setLocation(location));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                49700,
                49701
        };
    }
}
