package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.util.WorldUtil;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.utils.TimeUnit;

import java.util.Optional;

public class JalImKot extends BreachEntity implements Spawnable, CombatScript {
    private static final Animation attackAnimation = new Animation(7597);
    private static final Animation burrowStartAnimation = new Animation(7600);
    private static final Animation burrowEndAnimation = new Animation(7601);
    private boolean burrowing;

    public JalImKot(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean canAttack(final Player source) {
        if (burrowing) {
            source.sendMessage("You cannot attack it while it's burrowing.");
            return false;
        }
        return true;
    }

    @Override
    public void processNPC() {
        if (isUnderCombat() && !isDead() && Utils.currentTimeMillis() - getAttackingDelay() >= TimeUnit.TICKS.toMillis(10) && !hasWalkSteps()) {
            burrowing = true;
            lock(9);
            setAnimation(burrowStartAnimation);
            setAttackingDelay(Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(25));
            final Location burrowLocation = getBurrowLocation();
            WorldTasksManager.schedule(() -> {
                burrowing = false;
                combat.setCombatDelay(6);
                setLocation(burrowLocation);
                setAnimation(burrowEndAnimation);
            }, 4);
            return;
        }
        super.processNPC();
    }

    private Location getBurrowLocation() {
        final Player player = ((Player) getCombat().getTarget());
        final Optional<Location> optionalLocation = WorldUtil.findEmptySquare(player.getLocation(), player.getSize() + getSize(), getSize(), Optional.of(l -> CombatUtilities.isWithinMeleeDistance(l, getSize(), player)));
        return optionalLocation.orElseGet(this::getLocation);
    }

    @Override
    public int attack(Entity target) {
        setAnimation(attackAnimation);
        delayHit(0, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), CRUSH, target), HitType.MELEE));
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.JALIMKOT_12594;
    }
}
