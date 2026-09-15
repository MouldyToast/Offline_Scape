package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.content.tombsofamascut.encounter.WardenEncounter;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.RemoveHitBar;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.NPCCombat;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.action.combat.MeleeCombat;

public class WardenCoreNPC extends NPC implements IWardenCore {

    private static final int ID = 11771;
    private static final SoundEffect LANDING_SOUND = new SoundEffect(6201, 15);
    private final WardenEncounter encounter;
    private final MovingWardenNPC movingWardenNPC;
    private int aliveTicks;

    public WardenCoreNPC(Location tile, Direction facing, int aliveTicks, WardenEncounter encounter, MovingWardenNPC movingWardenNPC) {
        super(ID, tile, facing, 0);
        this.aliveTicks = aliveTicks;
        this.encounter = encounter;
        this.movingWardenNPC = movingWardenNPC;
        World.sendSoundEffect(location, LANDING_SOUND);
        super.hitBar = new RemoveHitBar(super.hitBar.getType());
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        if (hit.getSource() instanceof Player player) {
            // Melee always hits max — enforced here so every weapon (Fang, etc.) is covered.
            if (HitType.MELEE.equals(hit.getHitType())) {
                final var action = player.getActionManager().getAction();
                if (action instanceof MeleeCombat melee) {
                    hit.setDamage(melee.getMaxHit(player, 1, 1, false));
                }
            }
            // 100% accuracy — convert misses into hits.
            if (hit.getDamage() == 0 && !HitType.HEALED.equals(hit.getHitType())) {
                hit.setDamage(1);
            }
        }
        super.handleIngoingHit(hit);
        movingWardenNPC.applyHit(new Hit(this, hit.getDamage() * 5, HitType.DEFAULT));
    }

    @Override public void processNPC() {
        if (--aliveTicks <= 0 && !isFinished() && encounter.getPhase() == 1) {
            encounter.sendCoreBack();
        }
    }

    @Override public boolean isForceAttackable() { return true; }

    @Override public boolean canAttack(Player source) {
        return !encounter.isStoned(source);
    }

    @Override public void sendDeath() {
        if (!isFinished()) {
            encounter.sendCoreBack();
        }
    }

    @Override
    public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

    @Override public boolean isEntityClipped() { return false; }

    @Override public void setRespawnTask() {}

    @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

    @Override public void setFaceEntity(Entity entity) {}
}
