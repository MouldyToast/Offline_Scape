package org.jesse.game.parser.impl;

import cloud.rsps.util.io.ByteBufferBackedInputStream;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import org.jesse.CacheManager;
import org.jesse.ContentConstants;
import org.jesse.game.world.DefaultGson;
import org.jesse.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Kris | 7. juuni 2018 : 04:05:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemRequirements {
	private static final Logger log = LoggerFactory.getLogger(ItemRequirements.class);
	/**
	 * The collection holding all the optimized item requirements.
	 */
	private static final Int2ObjectMap<ItemRequirement> requirements = new Int2ObjectOpenHashMap<>();

	public static ItemRequirement getRequirement(final int id) {
		return requirements.get(id);
	}

	/**
	 * Parses the item requirements on server load. Skips the construction requirement if the game is loaded live.
	 * @throws FileNotFoundException if requirements don't exist.
	 */
	public static void parse() throws FileNotFoundException {
		final Stopwatch stopwatch = Stopwatch.createStarted();

		final Gson gson = DefaultGson.getGson();
		//final ObjectMapper objectMapper = JacksonObjectMapper.getObjectMapper();
		final LabelledItemRequirement[] loadedRequirements = ByteBufferBackedInputStream.from(
				Path.of("data", "items", "ItemRequirements.json"),
				inputStream -> gson.fromJson(
						new InputStreamReader(inputStream),
						LabelledItemRequirement[].class
				)/*objectMapper.readValue(inputStream, LabelledItemRequirement[].class)*/
		);
		//final BufferedReader br = new BufferedReader(new FileReader("data/items/ItemRequirements.json"));
		//final LabelledItemRequirement[] loadedRequirements = DefaultGson.getGson().fromJson(br, LabelledItemRequirement[].class);

		//Skip construction requirements on the live game for max capes and hoods.
		final boolean skipConstruction = !CacheManager.DEVELOPMENT_MODE && !ContentConstants.CONSTRUCTION;
		for (final LabelledItemRequirement req : loadedRequirements) {
			final List<LabelledItemRequirement.LabelledRequirement> labelledRequirements = new ObjectArrayList<>(req.requirements);
			final List<ItemRequirement.PrimitiveRequirement> primitiveRequirements = new ObjectArrayList<>(labelledRequirements.size());
			for (final LabelledItemRequirement.LabelledRequirement unidentifiedRequirement : labelledRequirements) {
				final int skill = unidentifiedRequirement.getSkill();
				if (skipConstruction && skill == SkillConstants.CONSTRUCTION) {
					final String name = ItemDefinitions.getOrThrow(req.id).getName().toLowerCase();
					if (name.contains("max cape") || name.contains("max hood")) {
						continue;
					}
				}
				primitiveRequirements.add(new ItemRequirement.PrimitiveRequirement(skill, unidentifiedRequirement.getLevel()));
			}
			requirements.put(req.id, new ItemRequirement(req.id, primitiveRequirements));
		}

		log.info("Loaded {} item requirements in {}ms.", requirements.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
	}

	/**
	 * Clears the item requirements of a specific item.
	 * @param id the id of the item.
	 */
	public static void clear(final int id) {
		requirements.remove(id);
	}

	/**
	 * Adds a new item requirement for the said item.
	 * @param id the id of the item.
	 * @param skill the id of the skill.
	 * @param level the level to set to.
	 */
	public static void add(final int id, final int skill, final int level) {
		Preconditions.checkArgument(level > 0);
		Preconditions.checkArgument(level <= 99);
		Preconditions.checkArgument(skill >= 0);
		Preconditions.checkArgument(skill < SkillConstants.SKILLS.length);
		Preconditions.checkArgument(ItemDefinitions.get(id) != null);
		if (level > 1) {
			final ItemRequirement requirement = requirements.computeIfAbsent(id, __ -> new ItemRequirement(id, new ObjectArrayList<>()));
			requirement.requirements.removeIf(req -> req.skill == skill);
			requirement.requirements.add(new ItemRequirement.PrimitiveRequirement(skill, level));
		}
	}

	/**
	 * Saves the item requirements in a properly formatted file, by their name rather than unambiguous ids.
	 */
	public static void save() {
		if (!CacheManager.DEVELOPMENT_MODE)
			return;

		final ObjectArrayList<LabelledItemRequirement> requirementsList = new ObjectArrayList<>();
		for (final ItemRequirement requirement : requirements.values()) {
			final ObjectArrayList<LabelledItemRequirement.LabelledRequirement> labelledRequirements = new ObjectArrayList<>(requirement.requirements.size());
			for (final ItemRequirement.PrimitiveRequirement req : requirement.requirements) {
				labelledRequirements.add(new LabelledItemRequirement.LabelledRequirement(req.getLabelledSkill(), req.getLevel()));
			}
			requirementsList.add(new LabelledItemRequirement(requirement.id, ItemDefinitions.getOrThrow(requirement.id).getName(), labelledRequirements));
		}
		requirementsList.sort(Comparator.comparingInt(c -> c.id));
		try {
			final PrintWriter pw = new PrintWriter("data/items/ItemRequirements.json", StandardCharsets.UTF_8);
			pw.println(DefaultGson.getGson().toJson(requirementsList));
			pw.close();
		} catch (final Exception e) {
			log.error("", e);
		}
	}


	public static final class ItemRequirement {
		private final int id;
		private final List<PrimitiveRequirement> requirements;

		public ItemRequirement(int id, List<PrimitiveRequirement> requirements) {
			this.id = id;
			this.requirements = requirements;
		}

		public int getId() {
			return id;
		}

		public List<PrimitiveRequirement> getRequirements() {
			return requirements;
		}


		public static final class PrimitiveRequirement extends Requirement {
			private final int skill;

			public PrimitiveRequirement(final int skill, final int level) {
				super(level);
				this.skill = skill;
			}

			@Override
			public String getLabelledSkill() {
				return SkillConstants.SKILLS[skill];
			}

			public int getSkill() {
				return skill;
			}
		}
	}


	private static final class LabelledItemRequirement {
		private final int id;
		@SuppressWarnings("unused")
		private final String description;
		private final List<LabelledRequirement> requirements;

		@JsonCreator
		public LabelledItemRequirement(
				@JsonProperty("id") int id,
				@JsonProperty("description") String description,
				@JsonProperty("requirements") List<LabelledRequirement> requirements
		) {
			this.id = id;
			this.description = description;
			this.requirements = requirements;
		}


		private static final class LabelledRequirement extends Requirement {
			private final String skill;

			@JsonCreator
			public LabelledRequirement(
					@JsonProperty("skill") @NotNull final String skill,
					@JsonProperty("level") final int level
			) {
				super(level);
				this.skill = skill;
			}

			@Override
			public int getSkill() {
				final int skillId = ArrayUtils.indexOf(SkillConstants.SKILLS, skill);
				Preconditions.checkArgument(skillId >= 0);
				Preconditions.checkArgument(skillId < SkillConstants.SKILLS.length);
				return skillId;
			}

			@Override
			public String getLabelledSkill() {
				return skill;
			}
		}
	}


	private static abstract class Requirement {
		private final int level;

		public Requirement(int level) {
			this.level = level;
		}

		public abstract int getSkill();

		@SuppressWarnings("unused")
		public abstract String getLabelledSkill();

		public int getLevel() {
			return level;
		}
	}
}
