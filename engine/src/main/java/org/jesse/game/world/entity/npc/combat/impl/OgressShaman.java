package org.jesse.game.world.entity.npc.combat.impl;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.HitEntry;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combat.Default;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jesse.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect;

/**
 * @author Kris | 18/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OgressShaman extends NPC implements CombatScript, Spawnable {
    public OgressShaman(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.attackDistance = 4;
    }

    @Override
    public int attack(Entity target) {
        animate();
        final CombatSpell spell = CombatSpell.EARTH_BOLT;
        final Projectile projectile = spell.getProjectile();
        int delay = 1;
        int clientDelay = 30;
        if (projectile != null) {
            clientDelay = projectile.getProjectileDuration(getLocation(), target.getLocation());
            if (projectile.getGraphicsId() != -1) {
                delay = World.sendProjectile(this, target, projectile);
            } else {
                delay = projectile.getTime(this, target);
            }
        }
        final SoundEffect sound = spell.getHitSound();
        final Graphics gfx = spell.getHitGfx();
        final SpellEffect effect = spell.getEffect();
        final HitEntry hitEntry = new HitEntry(this, delay, magic(target, combatDefinitions.getMaxHit()));
        target.appendHitEntry(hitEntry);
        if (hitEntry.getHit().getDamage() > 0) {
            if (gfx != null) {
                target.setGraphics(new Graphics(gfx.getId(), clientDelay, gfx.getHeight()));
            }
            if (sound != null) {
                World.sendSoundEffect(target.getLocation(), new SoundEffect(sound.getId(), sound.getRadius(), clientDelay));
            }
            delayHit(delay, target, hitEntry.getHit().onLand(hit -> {
                if (effect != null) {
                    effect.spellEffect(this, target, hit.getDamage());
                }
            }));
        } else {
            if (sound != null) {
                World.sendSoundEffect(target.getLocation(), new SoundEffect(227, 10, clientDelay));
            }
            if (gfx != null) {
                target.setGraphics(new Graphics(Default.SPLASH_GRAPHICS.getId(), clientDelay, Default.SPLASH_GRAPHICS.getHeight()));
            }
        }
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.OGRESS_SHAMAN || id == NpcId.OGRESS_SHAMAN_7992;
    }
}
