package com.zenyte.game.content.advent;

import com.google.common.reflect.TypeToken;
import com.zenyte.cores.CoresManager;
import com.zenyte.cores.ScheduledExternalizable;
import com.zenyte.logger.NearRealityLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdventCalendarRaffle implements ScheduledExternalizable {

	private static final Logger log = NearRealityLogger.getLogger(AdventCalendarRaffle.class);
	private static final Map<Integer, String> raffleWinners = Collections.synchronizedMap(new HashMap<>());

	@Override
	public Logger getLog() {
		return log;
	}

	@Override
	public int writeInterval() {
		return -1;
	}

	@Override
	public void read(@NotNull BufferedReader reader) {
		Map<Integer, String> map = getGSON().fromJson(reader, new TypeToken<Map<Integer, String>>(){}.getType());
		raffleWinners.clear();
		raffleWinners.putAll(map);
	}

	@Override
	public void write() {
		out(getGSON().toJson(raffleWinners));
	}

	@Override
	public String path() {
		return "data/advent_raffle.json";
	}

	public static void setAdventRaffleWinner(int day, String name) {
		raffleWinners.put(day, name);
		CoresManager.slowExecutor.execute(() -> new AdventCalendarRaffle().write());
	}

	public static String getRaffleWinner(int day) {
		return raffleWinners.getOrDefault(day, "");
	}

}
