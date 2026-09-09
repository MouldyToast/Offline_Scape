package org.jesse.game.content.minigame.barrows.wights;

import org.jesse.game.content.minigame.barrows.BarrowsWightNPC;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;


/**
 * @author Kris | 29. sept 2018 : 05:45:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class AhrimTheBlighted extends BarrowsWightNPC implements Spawnable, CombatScript {
	private static final Graphics AHRIMS_GFX = new Graphics(400, 0, 96);
	private static final Graphics SPLASH_GRAPHICS = new Graphics(85, 0, 124);

	public AhrimTheBlighted(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		final CombatSpell spell = Utils.random(6) != 0 ? CombatSpell.FIRE_WAVE : Utils.getRandomElement(CombatSpell.CONFUSE, CombatSpell.WEAKEN, CombatSpell.CURSE);
		setAnimation(spell.getAnimation());
		setGraphics(spell.getCastGfx());
		this.delayHit(World.sendProjectile(this, target, spell.getProjectile()), target, new Hit(this, this.getRandomMaxHit(this, combatDefinitions.getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> {
			if (hit.getDamage() <= 0) {
				target.setGraphics(SPLASH_GRAPHICS);
				return;
			}
			target.setGraphics(spell.getHitGfx());
			if (spell != CombatSpell.FIRE_WAVE) {
				spell.getEffect().spellEffect(this, target, hit.getDamage());
				return;
			}
			if (target instanceof Player) {
				final Player player = (Player) target;
				if (!player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					if (Utils.random(3) == 0) {
						target.setGraphics(AHRIMS_GFX);
						target.drainSkill(SkillConstants.STRENGTH, 5);
					}
				}
			}
		}));
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		switch (id) {
			case 1672: {
				return true;
			}
			default:
				return false;
		}
	}

}
