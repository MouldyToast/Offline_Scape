package org.jesse.game.content.skills.hunter.actions;

import org.jesse.game.content.skills.hunter.TrapState;
import org.jesse.game.content.skills.hunter.node.TrapType;
import org.jesse.game.content.skills.hunter.object.HunterTrap;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 29/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DismantlePlacedTrap extends BuiltHunterTrapAction {
    private static final Animation snareAndNetTrapAnimation = new Animation(5207);
    private static final Animation boxAnimation = new Animation(5212);
    private final HunterTrap trap;

    @Override
    public boolean start() {
        if (!preconditions(player, true)) {
            return false;
        }
        final TrapType type = trap.getType();
        player.setAnimation(type == TrapType.BIRD_SNARE || type.isNetTrap() ? snareAndNetTrapAnimation : boxAnimation);
        player.sendFilteredMessage("You dismantle the trap.");
        player.lock(3);
        trap.setState(TrapState.DISMANTLING);
        WorldTasksManager.schedule(() -> {
            if (!preconditions(player, true)) {
                return;
            }
            final Inventory inventory = player.getInventory();
            if (type.isNetTrap()) {
                inventory.addOrDrop(ItemId.ROPE, 1);
                inventory.addOrDrop(ItemId.SMALL_FISHING_NET, 1);
            } else {
                final int trapId = type.getItemId();
                if (trapId != -1) {
                    inventory.addOrDrop(new Item(trapId));
                }
            }
            player.sendSound(type.getTakeSound());
            trap.remove();
        }, 1);
        return false;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        return -1;
    }

    @Override
    protected HunterTrap getTrap() {
        return trap;
    }

    public DismantlePlacedTrap(HunterTrap trap) {
        this.trap = trap;
    }
}
