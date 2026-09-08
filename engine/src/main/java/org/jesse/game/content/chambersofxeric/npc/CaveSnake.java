package org.jesse.game.content.chambersofxeric.npc;

import org.jesse.game.content.chambersofxeric.Raid;
import org.jesse.game.content.chambersofxeric.room.ResourcesRoom;
import org.jesse.game.content.consumables.Consumable;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/07/2019 11:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CaveSnake extends RaidNPC<ResourcesRoom> {
    private static final SoundEffect attackSound = new SoundEffect(791, 10, 0);
    private static final SoundEffect diveSound = new SoundEffect(2496, 10, 0);

    public CaveSnake(final Raid raid, final ResourcesRoom room, final Location tile, final Location fishingObject) {
        super(raid, room, 7594, tile);
        this.fishingObject = fishingObject;
    }

    private final Location fishingObject;

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        return false;
    }

    public void bite() {
        if (isFinished() || isLocked()) {
            return;
        }
        lock(4);
        for (final Player player : raid.getPlayers()) {
            if (player.getLocation().withinDistance(fishingObject, 1)) {
                player.applyHit(new Hit(Utils.random(3, 6), HitType.DEFAULT));
                World.sendSoundEffect(getMiddleLocation(), attackSound);
                final Inventory inventory = player.getInventory();
                for (int i = 0; i < 28; i++) {
                    final Item item = inventory.getItem(i);
                    if (item == null || !Consumable.food.containsKey(item.getId())) {
                        continue;
                    }
                    inventory.deleteItem(item);
                    player.sendMessage("The snake stole some food off you!");
                    break;
                }
            }
        }
    }

    public void rise() {
        if (!isFinished()) {
            return;
        }
        spawn();
        setAnimation(new Animation(7333));
        this.lock(3);
    }

    public void dive() {
        if (isFinished()) {
            return;
        }
        setAnimation(new Animation(4039));
        World.sendSoundEffect(getMiddleLocation(), diveSound);
        WorldTasksManager.schedule(this::finish, 2);
        this.lock(10);
    }

    public void executeSequence(@NotNull final Runnable runnable) {
        rise();
        WorldTasksManager.scheduleOrExecute(this::bite, 3);
        WorldTasksManager.scheduleOrExecute(() -> {
            dive();
            runnable.run();
        }, 5);
    }
}
