package org.jesse.game.content.pyramidplunder.npc;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.utils.TimeUnit;
import org.apache.commons.lang3.mutable.MutableInt;

import java.lang.ref.WeakReference;

/**
 * @author Christopher
 * @since 4/5/2020
 */
public class PlunderNPC extends NPC {
    private static final int removalTicks = (int) TimeUnit.MINUTES.toTicks(1);
    private final MutableInt ticksLeft = new MutableInt(removalTicks);
    private final WeakReference<Player> player;

    public PlunderNPC(int id, Player player) {
        super(id, player.getLocation(), Direction.NORTH, 0);
        super.spawned = true;
        super.setTarget(player);
        this.player = new WeakReference<>(player);
    }

    @Override
    public void processNPC() {
        final Player target = player.get();
        if (ticksLeft.decrementAndGet() <= 0 || target == null || target.isFinished() || target.getLocation().getTileDistance(getLocation()) >= 20) {
            finish();
            return;
        }
        if (isUnderCombat()) {
            ticksLeft.setValue(removalTicks);
        }
        super.processNPC();
    }

    @Override
    public boolean isAcceptableTarget(final Entity entity) {
        return entity == player.get();
    }

    @Override
    public boolean canAttack(final Player target) {
        return player.get() == target;
    }
}
