package org.jesse.game.content.colosseum;

import org.jesse.game.content.scoreboard.Score;

public class ColosseumStatistics extends Score {

	public static ColosseumStatistics statistics = new ColosseumStatistics();

	long globalAttempts = 0L;

	public void incrementGlobalAttempts() {
		globalAttempts++;
	}

}
