package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

import java.util.Set;

public class DagannothKing extends BreachEntity implements Spawnable, CombatScript {

    public DagannothKing(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (target instanceof final Player player) {
            player.getAttributes().put("dagannoth_last_attacked_" + getId(), Utils.currentTimeMillis());
        }
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.DAGANNOTH_PRIME_12442 || id == NpcId.DAGANNOTH_REX_12439 || id == NpcId.DAGANNOTH_SUPREME_12441;
    }

    private static final Projectile magicProj = new Projectile(162, 63, 25, 27, 15, 33, 64, 5);

    private static final Projectile rangedProj = new Projectile(475, 50, 30, 25, 30, 28, 5, 5);

    @Override
    public void handleIngoingHit(Hit hit) {
        if(hit.getSource() instanceof Player player) {
            if (getId() == NpcId.DAGANNOTH_SUPREME_12441 && hit.getHitType() != HitType.MELEE) {
                hit.setDamage(0);
            }
            if (getId() == NpcId.DAGANNOTH_REX_12439 && hit.getHitType() != HitType.MAGIC) {
                hit.setDamage(0);
            }
            if (getId() == NpcId.DAGANNOTH_PRIME_12442 && hit.getHitType() != HitType.RANGED) {
                hit.setDamage(0);
            }
        }
        super.handleIngoingHit(hit);
    }

    @Override
    public int attack(final Entity target) {
        final DagannothKing npc = this;

        final Set<Entity> targets = Set.of(target);

        if (getId() == NpcId.DAGANNOTH_PRIME_12442) {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            targets.forEach(t -> delayHit(npc, World.sendProjectile(npc, t, magicProj), t, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, t), HitType.MAGIC)));
        }
        else if (getId() == NpcId.DAGANNOTH_REX_12439) {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        else {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            targets.forEach(t -> delayHit(npc, World.sendProjectile(npc, t, rangedProj), t, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, t), HitType.RANGED)));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
