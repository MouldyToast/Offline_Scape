package com.zenyte.game.content.colosseum;

import com.near_reality.game.content.scoreboard.Score;

public class ColosseumStatistics extends Score {

	public static ColosseumStatistics statistics = new ColosseumStatistics();

	long globalAttempts = 0L;

	public void incrementGlobalAttempts() {
		globalAttempts++;
	}

}
