package org.jesse.game.content.skills.magic.spells.lunar;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.spells.DefaultSpell;
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.area.wilderness.WildernessArea;

import java.util.List;

/**
 * @author Kris | 19. veebr 2018 : 0:07.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HealGroup implements DefaultSpell {

	private static final Animation ANIM = new Animation(4409);
	private static final Graphics GFX = new Graphics(745, 0, 90);
	private static final SoundEffect sound = new SoundEffect(2894, 10, 51);
	private static final SoundEffect otherSound = new SoundEffect(2892, 10, 67);

	@Override
	public int getDelay() {
		return 4500;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
        if (!hasDefenceRequirement(player)) {
            return false;
        }
		final int minimumRequired = (int) Math.ceil(player.getMaxHitpoints() * 0.11f);
		if (player.getHitpoints() <= minimumRequired) {
			player.sendMessage("You need more hitpoints to cast this spell.");
			return false;
		}


		final List<Player> affectedPlayers = player.findCharacters(1, Player.class, p2 -> {
			if (p2 == player || p2.isDead() || !p2.isInitialized() || p2.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) != 1 || p2.getDuel() != null || p2.getHitpoints() >= p2.getMaxHitpoints()) {
				return false;
			}
			final boolean inWilderness = WildernessArea.isWithinWilderness(p2.getX(), p2.getY());
			return !inWilderness || p2.isMultiArea();
		});

		if (affectedPlayers.size() > 0) {
            World.sendSoundEffect(player, sound);
			player.sendMessage("The spell affected " + affectedPlayers.size() + " nearby people.");
			this.addXp(player, 124);
			player.setAnimation(ANIM);
			int maximumPossibleAmount = 0;
			for (final Player p : affectedPlayers) {
			    maximumPossibleAmount += (p.getMaxHitpoints() - p.getHitpoints());
            }
			final int amount = (int) (player.getHitpoints() * 0.75f);
			final int actualAmount = maximumPossibleAmount > amount ? amount : maximumPossibleAmount;
			player.applyHit(new Hit(actualAmount, HitType.REGULAR));
			final int heal = actualAmount / affectedPlayers.size();
			for (int i = affectedPlayers.size() - 1; i >= 0; i--) {
				final Player p = affectedPlayers.get(i);
				World.sendSoundEffect(p, otherSound);
				p.setGraphics(GFX);
				p.heal(heal);
				p.sendMessage(player.getName() + " has transferred some of their health to you.");
			}
			return true;
		}
		player.sendMessage("There are no players around you with whom you can share health.");
		return false;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}
