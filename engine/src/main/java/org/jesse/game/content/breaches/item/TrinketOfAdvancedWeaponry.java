package org.jesse.game.content.breaches.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.degradableitems.DegradableItem;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.AnimationUtil;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.object.WorldObject;
import kotlin.Pair;

import static org.jesse.game.item.ids.ItemId.*;

public class TrinketOfAdvancedWeaponry extends ItemPlugin {
    private final Item trinket = new Item(ItemId.TRINKET_OF_ADVANCED_WEAPONRY, 1);

    @Override
    public void handle() {
        bind("Open", this::openAction);
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.TRINKET_OF_ADVANCED_WEAPONRY};
    }

    private Pair<Integer, Integer> getOffset(Player player) {
        var direction = facingDirection(player.getDirection());
        return switch (direction) {
            case NORTH -> new Pair<>(0, -1);
            case SOUTH -> new Pair<>(0, 1);
            case EAST -> new Pair<>(-1, 0);
            case WEST -> new Pair<>(1, 0);
            case SOUTH_WEST -> new Pair<>(1, 1);
            case SOUTH_EAST -> new Pair<>(-1, 1);
            case NORTH_EAST -> new Pair<>(-1, -1);
            case NORTH_WEST -> new Pair<>(1, -1);
        };
    }

    private Direction facingDirection(int i) {
        if (i >= 1590 && i <= 1793)
            return Direction.NORTH;
        else if (i >= 1025 && i <= 1255)
            return Direction.SOUTH;
        else if (i >= 511 && i <= 977)
            return Direction.EAST;
        else if (i >= 1306 && i <= 1583)
            return Direction.WEST;
        else
            return Direction.NORTH;
    }

    private int getObjectRotation(Player player) {
        return switch (facingDirection(player.getDirection())) {
            case SOUTH -> 2;
            case EAST -> 1;
            case WEST -> 3;
            default -> 0;
        };
    }

    public static int[] CORRUPTED_WEAPONS = {
        ItemId.CORRUPTED_VOIDWAKER,
        ItemId.CORRUPTED_DRAGON_CLAWS,
        ItemId.CORRUPTED_ARMADYL_GODSWORD,
        ItemId.CORRUPTED_DARK_BOW,
        ItemId.CORRUPTED_VOLATILE_NIGHTMARE_STAFF,
        CORRUPTED_TWISTED_BOW,
        CORRUPTED_SCYTHE_OF_VITUR,
        CORRUPTED_TUMEKENS_SHADOW
    };

    private void openAction(Player player, Item item, int slotId) {
        var offset = getOffset(player);
        var spawnLocation = player.getLocation().transform(offset.getFirst(), offset.getSecond());
        if (isTileSafeToSpawn(spawnLocation)) {
            if (player.getInventory().deleteItem(trinket).getResult() == RequestResult.SUCCESS)
                WorldTasksManager.schedule(getTrinketOpenTask(player, spawnLocation), 0, 0);
        }
        else
            player.sendMessage("You cannot do this while facing that object.");
    }

    private boolean isTileSafeToSpawn(Location spawnLocation) {
        return World.getObjectWithType(spawnLocation, 10) == null &&
            World.getObjectWithType(spawnLocation, 11) == null &&
            World.getObjectWithType(spawnLocation, 1) == null &&
            World.getObjectWithType(spawnLocation, 3) == null &&
            World.getObjectWithType(spawnLocation, 9) == null;
    }

    private TickTask getTrinketOpenTask(Player player, Location spawnLocation) {
        player.lock();
        var reward = Utils.random(CORRUPTED_WEAPONS);
        return new TickTask() {
            WorldObject lootLoc = null;

            @Override
            public void run() {
                ticks++;
                if (player.isDead()) {
                    stop();
                    return;
                }
                if (ticks == 1) {
                    var animationId = 4069;
                    player.setAnimation(new Animation(animationId, AnimationUtil.getSynchronizedAnimationDelay(animationId)));
                }
                if (ticks == 2) {
                    lootLoc = new WorldObject(49564, 10, getObjectRotation(player), spawnLocation);
                    World.spawnObject(lootLoc);
                    World.sendGraphics(new Graphics(1982), spawnLocation);
                }
                if (ticks == 4) {
                    World.sendGraphics(new Graphics(1982), spawnLocation);
                    var animation = getAnimationForItem(reward);
                    World.sendObjectAnimation(lootLoc, animation);
                }
                if (ticks == 5) player.setAnimation(Animation.GRAB);
                if (ticks == 6) {
                    World.removeObject(lootLoc);
                    var maxWeaponCharges = reward == CORRUPTED_SCYTHE_OF_VITUR ? 1_000 :
                                           reward == CORRUPTED_TUMEKENS_SHADOW ? 1_000 :
                                           reward == CORRUPTED_TWISTED_BOW ? 1_000 : 100;

                    var rewardedWeapon = new Item(reward, 1);
                        rewardedWeapon.setCharges(maxWeaponCharges);
                    player.getInventory().addItem(rewardedWeapon);
                    stop();
                }
            }

            @Override
            public void stop() {
                super.stop();
                player.unlock();
            }
        };
    }

    private Animation getAnimationForItem(int item) {
        return switch (item) {
            case ItemId.CORRUPTED_ARMADYL_GODSWORD -> new Animation(9508);
            case ItemId.CORRUPTED_VOIDWAKER -> new Animation(9510);
            case CORRUPTED_TWISTED_BOW -> new Animation(9511);
            case ItemId.CORRUPTED_DRAGON_CLAWS -> new Animation(9512);
            case CORRUPTED_SCYTHE_OF_VITUR -> new Animation(9513);
            case CORRUPTED_TUMEKENS_SHADOW -> new Animation(9514);
            case ItemId.CORRUPTED_DARK_BOW -> new Animation(10425); // loops
            case ItemId.CORRUPTED_VOLATILE_NIGHTMARE_STAFF -> new Animation(10426); // loops
            default -> null;
        };
    }

}
