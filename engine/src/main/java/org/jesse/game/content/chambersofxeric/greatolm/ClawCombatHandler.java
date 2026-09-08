package org.jesse.game.content.chambersofxeric.greatolm;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.NPCCombat;

/**
 * A special combat handler class for the claws to keep them out of combat.
 * @author Kris | 14. jaan 2018 : 3:05.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ClawCombatHandler extends NPCCombat {

	ClawCombatHandler(NPC npc) {
		super(npc);
	}

	@Override
	public void setTarget(final Entity target, TargetSwitchCause cause) {

	}

}
