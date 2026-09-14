package org.jesse.game.content.chambersofxeric.greatolm.scripts;

import org.jesse.game.content.chambersofxeric.ScalingMechanics;
import org.jesse.game.content.chambersofxeric.greatolm.GreatOlm;
import org.jesse.game.content.chambersofxeric.greatolm.OlmCombatScript;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

import java.util.List;

/**
 * @author Kris | 18. jaan 2018 : 23:45.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class StandardAttack implements OlmCombatScript {
	private static final Projectile rangedProjectile = new Projectile(1340, 260, 60, 30, 15, 18, 0, 5);
	private static final Projectile magicProjectile = new Projectile(1339, 260, 60, 30, 15, 18, 0, 5);
	private static final SoundEffect rangedSound = new SoundEffect(1784, 15, 0);
	private static final SoundEffect magicSound = new SoundEffect(3749, 15, 0);

	@Override
	public void handle(final GreatOlm olm) {
		if (Utils.random(2) == 0) {
			olm.setRanging(!olm.isRanging());
		}
		final boolean ranged = olm.isRanging();
		final Location face = olm.getFaceCoordinates();
		final List<Player> everyone = olm.everyone(olm.getDirection());
		if (everyone.isEmpty()) {
			return;
		}
		olm.performAttack();
		World.sendSoundEffect(olm.getMiddleLocation(), ranged ? rangedSound : magicSound);
		for (final Player player : everyone) {
			World.sendProjectile(face, player, ranged ? rangedProjectile : magicProjectile);
			CombatUtilities.delayHit(olm, rangedProjectile.getTime(face, player.getLocation()), player, new Hit(olm, CombatUtilities.getRandomMaxHit(olm, ScalingMechanics.getOlmMaxHitStandard(olm.getRaid()), ranged ? CombatScript.RANGED : CombatScript.MAGIC, player), ranged ? HitType.RANGED : HitType.MAGIC));
		}
	}
}
