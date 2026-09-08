package org.jesse.game.content.minigame.inferno.npc.impl;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.content.minigame.inferno.instance.Inferno;
import org.jesse.game.content.minigame.inferno.model.RockySupport;
import org.jesse.game.content.minigame.inferno.model.RockySupportLocation;
import org.jesse.game.content.minigame.inferno.npc.InfernoNPC;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.world.Position;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.npc.CombatScriptsHandler;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.NPCCombat;
import org.jesse.game.world.entity.npc.combatdefs.AttackDefinitions;
import org.jesse.game.world.entity.pathfinding.events.npc.NPCCollidingEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;

/**
 * @author Tommeh | 29/11/2019 | 20:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalNib extends InfernoNPC {
    public JalNib(final Location location, final Inferno inferno) {
        super(7691, location, inferno);
        setTargetType(EntityType.BOTH);
        freeze(1);
        resetWalkSteps();
        combat = new NPCCombat(this) {
            @Override
            public int combatAttack() {
                if (target == null || target.isDead() || target.getLocation().getDistance(getLocation()) >= 64 || (target.getNextLocation() != null && target.getNextLocation().getDistance(getLocation()) >= 64)) {
                    return 0;
                }
                final boolean melee = npc.getCombatDefinitions().isMelee();
                int distance = melee || npc.isForceFollowClose() ? 0 : npc.getAttackDistance();
                if (CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize())) {
                    return 0;
                }
                if (outOfRange(target, distance, target.getSize(), melee)) {
                    return 0;
                }
                addAttackedByDelay(target);
                return CombatScriptsHandler.specialAttack(npc, target);
            }
            @Override
            protected boolean checkAll() {
                if (target.isFinished() || npc.isDead() || npc.isFinished()) {
                    return false;
                }
                if (target.isDead() || npc.isMovementRestricted()) {
                    return true;
                }
                if (colliding()) {
                    //TODO: Change into a more efficent pathfinding formula or write a non-pf structure.
                    npc.setRouteEvent(new NPCCollidingEvent(npc, new EntityStrategy(target)));
                    return true;
                }
                return appendMovement();
            }
        };
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (combat.getTarget() instanceof RockySupport) {
            final RockySupport support = (RockySupport) combat.getTarget();
            if (support.getType() == RockySupportLocation.NORTH) {
                if (getX() == support.getX() - 1 && getY() == support.getY() - 1) {
                    resetWalkSteps();
                    addWalkSteps(getX(), getY() + 1, 1, true);
                }
            }
        }
    }

    @Override
    public NPC spawn() {
        final NPC npc = super.spawn();
        setTarget(inferno.getNibblerTarget());
        return npc;
    }

    @Override
    public boolean isAcceptableTarget(final Entity entity) {
        return entity == inferno.getPlayer() || entity instanceof RockySupport;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public boolean isProjectileClipped(final Position target, final boolean closeProximity) {
        for (final RockySupport support : inferno.getRockySupports()) {
            if (support.getLocation().matches(target)) {
                return false;
            }
        }
        return super.isProjectileClipped(target, closeProximity);
    }

    @Override
    public void autoRetaliate(final Entity source) {
    }

    @Override
    public void setTarget(final Entity target, TargetSwitchCause cause) {
        if (target == null) {
            return;
        }
        combat.setTarget(target);
    }

    @Override
    public void playAttackSound(final Entity target) {
        final AttackDefinitions attDefs = combatDefinitions.getAttackDefinitions();
        final SoundEffect sound = attDefs.getStartSound();
        if (sound != null) {
            World.sendSoundEffect(this, sound);
        }
    }

    @Override
    public int attack(final Entity target) {
        playAttackSound(inferno.getPlayer());
        return CombatScriptsHandler.DEFAULT_SCRIPT.attack(this, target);
    }
}
