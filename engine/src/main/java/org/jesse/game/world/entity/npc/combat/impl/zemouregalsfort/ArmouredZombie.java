package org.jesse.game.world.entity.npc.combat.impl.zemouregalsfort;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.AggressionType;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.object.WorldObject;
import org.jetbrains.annotations.Nullable;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/28/2025
 */
public class ArmouredZombie extends NPC implements Spawnable, CombatScript {
    private static final Projectile PROJ = new Projectile(2652, 42, 30, 40, 15, 3, 64, 5);

    public ArmouredZombie(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius, false);
    }

    @Override
    public NPC spawn() {
        super.spawn();
        this.combatDefinitions.setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
        return this;
    }

    @Override
    public boolean isMultiArea() {
        return true;
    }

    @Override
    public int attack(final Entity target) {
        animate();
        var isMelee = getCombatDefinitions().getAttackStyle() == AttackType.CRUSH;
        if (isMelee)
            executeMeleeHit(target, CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), AttackType.CRUSH, MELEE, target));
        else {
            animate();
            var isRanged = getCombatDefinitions().getAttackStyle() == AttackType.RANGED;
            if (isRanged)
                this.delayHit(World.sendProjectile(this, target, PROJ), target, ranged(target, combatDefinitions.getMaxHit()));
        }
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    protected void onFinish(@Nullable Entity source) {
        super.onFinish(source);
        var redMist = new WorldObject(50145, 10, 0, this.getLocation());
        World.spawnTemporaryObjectWithoutClip(redMist, 100);
    }

    @Override
    public boolean validate(int id, String name) {
        return (id >= NpcId.ARMOURED_ZOMBIE && id <= NpcId.ARMOURED_ZOMBIE_12764) ||
                (id >= NpcId.ARMOURED_ZOMBIE_14113 && id <= NpcId.ARMOURED_ZOMBIE_14122);
    }
}
