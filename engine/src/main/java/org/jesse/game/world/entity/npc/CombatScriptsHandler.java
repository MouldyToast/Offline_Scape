package org.jesse.game.world.entity.npc;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combat.Default;
import org.jesse.logger.NearRealityLogger;
import org.slf4j.Logger;

public class CombatScriptsHandler {
	private static final Logger log = NearRealityLogger.getLogger(CombatScriptsHandler.class);
	public static final Default DEFAULT_SCRIPT = new Default();

	public static int specialAttack(final NPC npc, final Entity target) {
		npc.renewFlinch();
		if (npc instanceof CombatScript) {
			return ((CombatScript) npc).attack(target);
		}
		return DEFAULT_SCRIPT.attack(npc, target);
	}
}
