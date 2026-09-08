package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

public class NightBeast extends BreachEntity implements Spawnable, CombatScript {

    private static final Projectile MAGICAL_PROJ = new Projectile(130, 35, 30, 40, 5, 38, 64, 5);
    private static final Graphics SPLASH = new Graphics(85, 0, 92);
    private static final Graphics GFX = new Graphics(131, 0, 92);

    private int areaAttacks = 0;

    public NightBeast(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        if(areaAttacks == 0 && Utils.random(5) == 1)
            areaAttacks = 3;
        if(areaAttacks > 0) {
            areaAttacks --;
            return sendGroupMagicAttack(target);
        }
        if (Utils.random(1) == 0 || !isWithinMeleeDistance(this, target))
            return sendMagicAttack(target);
        this.setAnimation(this.getCombatDefinitions().getAttackAnim());
        delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 31, MELEE, target), HitType.MELEE));
        return this.getCombatDefinitions().getAttackSpeed();
    }

    private int sendMagicAttack(final Entity target) {
        this.setAnimation(this.getCombatDefinitions().getAttackAnim());
        CombatUtilities.delayHit(this, World.sendProjectile(this, target, MAGICAL_PROJ), target, new Hit(this, CombatUtilities.getRandomMaxHit(this, 31, MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(hit.getDamage() <= 0 ? SPLASH : GFX)));
        return this.getCombatDefinitions().getAttackSpeed();
    }

    private int sendGroupMagicAttack(final Entity target) {
        this.setAnimation(this.getCombatDefinitions().getAttackAnim());
        final Location tile = new Location(target.getLocation());
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                final Location t = tile.transform(x, y, 0);
                if (!World.isFloorFree(t, 1)) continue;
                World.sendProjectile(this, t, MAGICAL_PROJ);
                final int specificTime = MAGICAL_PROJ.getProjectileDuration(getLocation(), t);
                World.sendGraphics(new Graphics(GFX.getId(), GFX.getHeight(), specificTime), t);
            }
        }
        WorldTasksManager.schedule(() -> {
            if (target.getLocation().withinDistance(tile, 1)) {
                CombatUtilities.delayHit(this, -1, target, new Hit(this, (int) (target.getHitpoints() * 0.3F), HitType.MAGIC));
            }
        }, MAGICAL_PROJ.getTime(getLocation(), target.getLocation()));
        return this.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.NIGHT_BEAST_12459;
    }
}
