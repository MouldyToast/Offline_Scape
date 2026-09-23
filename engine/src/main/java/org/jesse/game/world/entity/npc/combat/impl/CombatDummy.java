package org.jesse.game.world.entity.npc.combat.impl;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.NPCCombat;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combatdefs.ImmunityType;
import org.jesse.game.world.object.WorldObject;

import java.util.EnumSet;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CombatDummy extends NPC implements Spawnable {

    public CombatDummy(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override public boolean isAlwaysTakeMaxHit(HitType type) { return true; }

    @Override
    protected boolean isMovableEntity() {
        return false;
    }

    @Override
    public boolean isMultiArea() {
        return true;
    }

    @Override
    public boolean isForceAttackable() {
        return true;
    }

    @Override
    public float getXpModifier(Hit hit) {
        return 0;
    }

    @Override
    public void addHitbar() {

    }

    public void heal(final int amount) {

    }

    @Override
    public void removeHitpoints(final Hit hit) {

    }

    @Override
    protected void updateCombatDefinitions() {
        super.updateCombatDefinitions();
        combatDefinitions.setHitpoints(Short.MAX_VALUE);
        combatDefinitions.setImmunityTypes(EnumSet.allOf(ImmunityType.class));
        setHitpoints(combatDefinitions.getHitpoints());
    }

    @Override
    public boolean ignoreUnderneathProjectileCheck() {
        return true;
    }

    private WorldObject dummyObject;

    @Override
    public NPC spawn() {
        final NPC npc = super.spawn();
        dummyObject = new WorldObject(0, 10, 0, getLocation());
        World.spawnObject(dummyObject);
        return npc;
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        World.removeObject(dummyObject);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.COMBAT_DUMMY || id == NpcId.UNDEAD_COMBAT_DUMMY;
    }
}
