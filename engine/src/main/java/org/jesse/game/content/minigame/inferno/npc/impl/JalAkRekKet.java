package org.jesse.game.content.minigame.inferno.npc.impl;

import org.jesse.game.content.minigame.inferno.instance.Inferno;
import org.jesse.game.content.minigame.inferno.npc.InfernoNPC;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 06/12/2019 | 17:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalAkRekKet extends InfernoNPC {

    private static final Animation attackAnimation = new Animation(7582);

    public JalAkRekKet(final Location location, final Inferno inferno) {
        super(7696, location, inferno);
    }

    @Override
    public int attack(final Player player) {
        setAnimation(attackAnimation);
        delayHit(0, player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), CRUSH, player), HitType.MELEE));
        return combatDefinitions.getAttackSpeed();
    }

}
