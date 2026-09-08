package org.jesse.game.content.godwars.objects;

import org.jesse.game.content.chambersofxeric.storageunit.StorageUnit;
import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import java.util.Optional;

import static org.jesse.game.content.chambersofxeric.storageunit.StorageUnit.HammerType.HAMMER;

/**
 * @author Tommeh | 24-3-2019 | 13:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class GodwarsBandosObstacleDoorObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final boolean requireHammer = player.getX() >= 2851;
        if (requireHammer) {
            if (player.getSkills().getLevel(SkillConstants.STRENGTH) < 70) {
                player.sendMessage("You need a Strength level of at least 70 to ring the gong.");
                return;
            }

            final Optional<StorageUnit.HammerType> hammer = Optional.of(HAMMER);

            player.lock();
            player.setAnimation(hammer.get().getGongAnimation());
            WorldTasksManager.schedule(new TickTask() {
                @Override
                public void run() {
                    switch (ticks++) {
                        case 0 -> object.setLocked(true);
                        case 1 -> {
                            World.removeObject(object);
                            World.sendSoundEffect(object, new SoundEffect(71, 5, 0));
                            player.addWalkSteps(2850, 5333);
                        }
                        case 2 -> {
                            player.unlock();
                            World.spawnObject(object);
                            object.setLocked(false);
                            stop();
                        }
                    }
                }
            }, 0, 1);
        } else {
            player.lock();
            WorldTasksManager.schedule(new TickTask() {
                @Override
                public void run() {
                    switch (ticks++) {
                        case 0 -> {
                            World.removeObject(object);
                            World.sendSoundEffect(object, new SoundEffect(71, 5, 0));
                            player.addWalkSteps(2851, 5333);
                        }
                        case 1 -> {
                            player.unlock();
                            World.spawnObject(object);
                            stop();
                        }
                    }
                }
            }, 0, 1);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BIG_DOOR };
    }

}
