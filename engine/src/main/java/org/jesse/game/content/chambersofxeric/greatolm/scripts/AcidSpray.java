package org.jesse.game.content.chambersofxeric.greatolm.scripts;

import org.jesse.game.content.chambersofxeric.greatolm.AcidPool;
import org.jesse.game.content.chambersofxeric.greatolm.GreatOlm;
import org.jesse.game.content.chambersofxeric.greatolm.OlmCombatScript;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;

/**
 * @author Kris | 16. jaan 2018 : 0:19.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class AcidSpray implements OlmCombatScript {
	private static final SoundEffect startSound = new SoundEffect(1784, 15, 0);
	private static final SoundEffect splatterSound = new SoundEffect(3308, 5, 0);

	@Override
	public void handle(final GreatOlm olm) {
		final Location face = olm.getFaceCoordinates();
		olm.performAttack();
		World.sendSoundEffect(olm.getMiddleLocation(), startSound);
		int count = Utils.random(7, 12);
		int trycount = 100;
		while (trycount-- > 0 && count > 0) {
			final Location t = olm.randomLocation(olm.getDirection());
			if (t == null) {
				continue;
			}
			if (olm.getRoom().containsPool(t)) {
				continue;
			}
			count--;
			final Projectile projectile = new Projectile(1364, 260, 0, 30, 15, 0, 0, 10);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (olm.getRoom().getRaid().isDestroyed()) {
						stop();
						return;
					}
					final AcidPool pool = new AcidPool(t);
					olm.getRoom().getAcidPools().add(pool);
					pool.process();
					World.sendSoundEffect(t, splatterSound);
				}
			}, World.sendProjectile(face, t, projectile));
		}
	}
}
