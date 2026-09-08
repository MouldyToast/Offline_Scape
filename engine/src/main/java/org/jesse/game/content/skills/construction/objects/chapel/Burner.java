package org.jesse.game.content.skills.construction.objects.chapel;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 25. veebr 2018 : 17:17.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Burner implements ObjectInteraction {

    private static final class BurnerObject extends WorldObject {

        public BurnerObject(final WorldObject object) {
            super(object);
        }

        private BurnerTask task;
    }

    private static abstract class BurnerTask implements WorldTask {

        protected int ticks;
    }

    private static final Animation ANIMATION = new Animation(3687);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.INCENSE_BURNER, ObjectId.INCENSE_BURNER_13209, ObjectId.INCENSE_BURNER_13210, ObjectId.INCENSE_BURNER_13211, ObjectId.INCENSE_BURNER_13212, ObjectId.INCENSE_BURNER_13213 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (construction.isBuildingMode()) {
            player.sendMessage("You can't do this in building mode!");
            return;
        }
        if (option.equals("Light")) {
            if (!player.getInventory().containsItem(590, 1)) {
                player.sendMessage("You need a tinderbox to light the burner.");
                return;
            }
            if (!player.getInventory().containsItem(251, 1)) {
                player.sendMessage("You need a clean marrentill to light to burner.");
                return;
            }
            final int time = 217 + (int) ((player.getSkills().getLevel(SkillConstants.FIREMAKING)) / 200f * 217);
            player.lock();
            object.setLocked(true);
            player.getInventory().deleteItem(251, 1);
            player.setAnimation(ANIMATION);
            final BurnerObject obj = new BurnerObject(object);
            final BurnerTask task = new BurnerTask() {

                private boolean first;

                @Override
                public void run() {
                    if (!first) {
                        ticks = time;
                        obj.setId(obj.getId() + 1);
                        World.spawnObject(obj);
                        player.unlock();
                        first = true;
                    } else if (--ticks <= 0) {
                        if (World.containsSpawnedObject(obj)) {
                            object.setLocked(false);
                            World.spawnObject(object);
                        }
                        stop();
                    }
                }
            };
            obj.task = task;
            WorldTasksManager.schedule(task, 1, 0);
        } else if (option.equals("re-light")) {
            if (!(object instanceof BurnerObject)) {
                return;
            }
            final BurnerObject obj = (BurnerObject) object;
            if (obj.task == null) {
                return;
            }
            if (!player.getInventory().containsItem(251, 1)) {
                player.sendMessage("You need a clean marrentill to relight to burner.");
                return;
            }
            final int time = 217 + (int) ((player.getSkills().getLevel(SkillConstants.FIREMAKING)) / 200f * 217);
            player.lock(1);
            player.setAnimation(ANIMATION);
            player.getInventory().deleteItem(251, 1);
            if (obj.task.ticks < time) {
                obj.task.ticks = time;
            }
        }
    }
}
