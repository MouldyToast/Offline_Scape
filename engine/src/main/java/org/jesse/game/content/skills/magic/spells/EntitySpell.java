package org.jesse.game.content.skills.magic.spells;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 15. juuli 2018 : 22:15:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface EntitySpell extends PlayerSpell, NPCSpell {

	@Override
	@Deprecated
    default boolean spellEffect(final Player player, final Player target) {
		return false;
	}
	
	@Override
	@Deprecated
    default boolean spellEffect(final Player player, final NPC target) {
		return false;
	}
	
	@Override
    default void execute(final Player player, final Player target) {
		execute(player, (Entity) target);
	}
	
	@Override
    default void execute(final Player player, final NPC target) {
		execute(player, (Entity) target);
	}
	
	void execute(final Player player, final Entity target);
	
}
