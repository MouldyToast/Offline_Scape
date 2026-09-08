package org.jesse.game.world.object;

import org.jesse.game.parser.Parse;
import org.jesse.game.util.Examine;
import org.jesse.game.util.LabelledExamine;
import org.jesse.game.world.DefaultGson;
import org.jesse.logger.NearRealityLogger;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ObjectExamineLoader implements Parse {
	private static final Logger log = NearRealityLogger.getLogger(ObjectExamineLoader.class);
	public static final Map<Integer, Examine> DEFINITIONS = new HashMap<>();

	@Override
	public void parse() throws Throwable {
		final Examine[] examines = DefaultGson.fromGson("data/examines/Object examines.json", Examine[].class);
		for (final Examine def : examines) {
			if (def != null) DEFINITIONS.put(def.getId(), def);
		}
		parseOverrides();
	}

	private void parseOverrides() throws Throwable {
		final LabelledExamine[] examines = DefaultGson.fromGson("data/examines/Forced object examines.json", LabelledExamine[].class);
		for (final LabelledExamine def : examines) {
			DEFINITIONS.put(def.getId(), def);
		}
	}

	public static final void loadExamines() {
		try {
			new ObjectExamineLoader().parse();
		} catch (final Throwable e) {
			log.error("", e);
		}
	}
}
