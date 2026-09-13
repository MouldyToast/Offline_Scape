package org.jesse.game.content.quest;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.VarManager;
import org.jesse.logger.NearRealityPrintStream;
import org.jesse.plugins.Listener;
import org.jesse.plugins.ListenerType;
import mgi.types.config.DBRowDefinition;

/**
 * Currently static class, just to unlock all the quests. Will be turned
 * into player-based quest manager once we start adding quests.
 * @author Kris | 23. veebr 2018 : 2:38.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public final class QuestManager {

	@Listener(type = ListenerType.LOBBY_CLOSE)
	public void unlock(final Player player) {
		final VarManager vars = player.getVarManager();
		vars.sendVarInstant(145, 7);
		vars.sendVarInstant(299, 1048576);
		vars.sendVarInstant(302, 61);
		vars.sendBitInstant(821, 1);
		vars.sendBitInstant(1391, 2047);
		vars.sendBitInstant(395, 26);
		vars.sendBitInstant(1908, 1);
		vars.sendBitInstant(598, 3);
		vars.sendBitInstant(340, 2);
		// frozen door quest
		vars.sendBitInstant(13175, 10);
		vars.sendBitInstant(12296, 150);
		// Boss scoreboards — resolve multiloc objects to interactable variants.
		// DT2 scoreboards (Duke 46091, Leviathan 49475, Vardorvis 49476,
		// Whisperer 49474) share varbit 15175; value 2 = Read + Read (Awakened).
		vars.sendBitInstant(15175, 2);
        // Vardorvis entrance rocks — multiloc 49495 uses varbit 15125. States
        // 0-36 show 48741 (no ops). Any value > 36 hits the default: 48740
        // (Climb-over). 37 is the lowest value that resolves correctly.
        vars.sendBitInstant(15125, 37);
        // Kings' ladder (3831) is a multiloc on varbit 11707.
        // Value 0 → 40417 (Standard + Slayer + Peek only).
        // Value 1 → 40418 (Standard + Slayer + Private + Peek).
        vars.sendBitInstant(11707, 1);
        // GWD boss doors (26502–26505) are multilocs on varbit 11708.
        // Value 0 → 40419 (Open + Peek only).
        // Value 1 → 40420 (Open (normal) + Open (private) + Peek).
        // In vanilla this unlocks after hard Combat Achievements tier.
        vars.sendBitInstant(11708, 1);
		// Araxxor scoreboard (54270) uses varp 4260 (kill count); any non-zero
		// value resolves the multiloc to the Read scoreboard.
		vars.sendVarInstant(4260, 1);
		// Muspah scoreboard (46901) needs varbit 14722 = 90, which is already
		// set by SECRETS_OF_THE_NORTH quest completion in the loop below.

		int completed = 0;
		for (final Quest quest : Quest.values) {
			int varId = quest.getVariable();
			DBRowDefinition dbRowDefinition = DBRowDefinition.get(quest.getDbTableIndex());
			if (dbRowDefinition == null || !dbRowDefinition.columns.containsKey(19)) {
				continue;
			}
			try {
				final int questFinishStage = (int) dbRowDefinition.getValueFromRow(19, 0);
				if (quest.isVarbit()) {
					int currentValue = vars.getBitValue(varId);
					if (currentValue < questFinishStage) {
						vars.sendBitInstant(varId, questFinishStage);
					}
				} else {
					int currentValue = vars.getValue(varId);
					if (currentValue < questFinishStage) {
						vars.sendVarInstant(varId, questFinishStage);
					}
				}
				if (quest.isCounted()) {
					completed++;
				}
			} catch (final Exception e) {
				System.err.println("Error while unlocking quest " + quest.name() + " for player " + player.getUsername());
				e.printStackTrace(NearRealityPrintStream.getErrorStream());
			}
		}
		// completed-quests count shown as x/y on the character summary
		vars.sendBitInstant(6347, completed);
	}

}
