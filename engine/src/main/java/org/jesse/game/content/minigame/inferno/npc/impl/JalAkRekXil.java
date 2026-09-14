package org.jesse.game.content.minigame.inferno.npc.impl;

import org.jesse.game.content.minigame.inferno.instance.Inferno;
import org.jesse.game.content.minigame.inferno.npc.InfernoNPC;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 06/12/2019 | 17:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalAkRekXil extends InfernoNPC {

    private static final Animation attackAnimation = new Animation(7583);
    private static final Projectile attackProjectile = new Projectile(1379, 60, 96, 15, 17, 30, 0, 5);

    public JalAkRekXil(final Location location, final Inferno inferno) {
        super(7695, location, inferno);
        setAttackDistance(14);
    }

    @Override
    public int attack(final Player player) {
        setAnimation(attackAnimation);
        delayHit(World.sendProjectile(this, player, attackProjectile), player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, player), HitType.RANGED));
        return combatDefinitions.getAttackSpeed();
    }

}
