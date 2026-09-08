package org.jesse.game.content.colosseum;

import com.google.common.reflect.TypeToken;
import org.jesse.cores.ScheduledExternalizable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;

@SuppressWarnings("unused")
public class ColosseumStatisticsSchedule implements ScheduledExternalizable {

	private static final Logger log = LoggerFactory.getLogger(ColosseumStatisticsSchedule.class);

	@Override
	public Logger getLog() {
		return log;
	}

	@Override
	public int writeInterval() {
		return 1;
	}

	@Override
	public void ifFileNotFoundOnRead() {
		ColosseumStatistics.statistics = new ColosseumStatistics();
	}

	@Override
	public void read(@NotNull BufferedReader reader) {
		try {
			ColosseumStatistics.statistics = getGSON().fromJson(reader, new TypeToken<ColosseumStatistics>() {
			}.getType());
			log.info("Loaded {}", ColosseumStatistics.statistics);
		} catch (Exception e) {
			log.error("Failed to load colosseum statistics due to exception", e);
			ColosseumStatistics.statistics = new ColosseumStatistics();
		}
	}

	@Override
	public void write() {
		String serialised = getGSON().toJson(ColosseumStatistics.statistics);
		out(serialised);
	}

	@Override
	public String path() {
		return "data/scoreboard/colosseum_statistics.json";
	}

}
