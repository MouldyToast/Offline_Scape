package org.jesse.game.world.entity.player.calog;

import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.VarManager;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Savions.
 */
public class CALog {

	public static final int CA_EASY_COMPLETION_TOTAL_VARBIT = 12885;
	public static final int CA_MEDIUM_COMPLETION_TOTAL_VARBIT = 12886;
	public static final int CA_HARD_COMPLETION_TOTAL_VARBIT = 12887;
	public static final int CA_ELITE_COMPLETION_TOTAL_VARBIT = 12888;
	public static final int CA_MASTER_COMPLETION_TOTAL_VARBIT = 12889;
	public static final int CA_GRANDMASTER_COMPLETION_TOTAL_VARBIT = 12890;

	public static final int[] CA_TASK_VARS = { 3116, 3117, 3118, 3119, 3120, 3121, 3122, 3123, 3124, 3125, 3126, 3127, 3128, 3387, 3718, 3773, 3774 };
	public static final int[] CA_TIER_REWARD_STATUS_VARBITS = { 12863, 12864, 12865, 12866, 12867, 12868 };
	private static final int[] CA_TASK_TIER_ENUMS = { 3981, 3982, 3983, 3984, 3985, 3986 };

	private static final int STRUCT_POINTER_TASK_INDX = 1306;
	private static final int STRUCT_POINTER_TASK_NAME = 1308;
	private static final int STRUCT_POINTER_TASK_TIER = 1310;
	private static final int STRUCT_POINTER_TASK_BOSS = 1312;

	private static final int[] TOTAL_TASK_COUNT = new int[CATierType.values().length];

	private static final Set<String> INCOMPLETE_TASKS = new HashSet<>(Arrays.asList(
			"fighting as intended ii", "fighting as intended", "fragment of seren speed-trialist",
			"galvek speed-trialist", "glough speed-trialist", "the flame skipper", "arooo no more",
			"perfect olm (solo)", "perfect olm (trio)", "a not so special lizard",
			"chambers of xeric: cm (5-scale) speed-chaser", "chambers of xeric: cm (solo) speed-chaser",
			"moving collateral", "perfect corrupted hunllef", "perfect crystalline hunllef",
			"perfect nightmare", "perfect maiden", "pop it", "perfect nylocas", "perfect verzik",
			"perfect sotesteg", "perfect bloat", "can't drain this", "perfect xarpus",
			"nibblers, begone!", "you didn't say anything about a bat", "fight caves speed-chaser",
			"denying the healers", "the walk", "perfect zulrah",
			"chambers of xeric (solo) speed-runner", "chambers of xeric (5-scale) speed-runner",
			"chambers of xeric (trio) speed-runner", "chambers of xeric: cm (solo) speed-runner",
			"egniol diet ii", "corrupted gauntlet speed-runner", "perfect nex",
			"perfect phosani's nightmare", "phosani's speedrunner",
			"nightmare (5-scale) speed-runner", "terrible parent", "a long trip",
			"perfect theatre", "theatre (5-scale) speed-runner", "theatre (duo) speed-runner",
			"theatre (4-scale) speed-runner", "theatre (trio) speed-runner",
			"wasn't even close", "nibbler chaser", "the floor is lava", "no luck required",
			"jad? what are you doing here?", "budget setup", "playing with jads",
			"facing jad head-on ii", "denying the healers ii", "no time for a drink",
			"expert tomb explorer", "something of an expert myself", "expert tomb looter",
			"ba-bananza", "rockin' around the croc", "doesn't bug me", "all out of medics",
			"warden't you believe it", "resourceful raider", "but... damage", "fancy feet",
			"tombs speed runner ii", "tombs speed runner iii", "amascut's remnant",
			"maybe i'm the boss.", "expert tomb raider", "akkhan't do it", "all praise zebak",
			"perfection of het", "perfection of apmeken", "perfection of crondis",
			"perfection of scabaras", "insanity", "tomb explorer", "hardcore raiders",
			"hardcore tombs", "helpful spirit who?", "dropped the ball", "no skipping allowed",
			"down do specs", "perfect het", "perfect apmeken", "perfect crondis",
			"i'm in a rush", "you are not prepared", "tomb looter", "tomb raider",
			"tombs speed runner", "better get movin'", "chompington", "perfect akkha",
			"perfect ba-ba", "perfect zebak", "perfect scabaras", "perfect kephri",
			"perfect wardens", "novice tomb explorer", "novice tomb looter", "movin' on up",
			"confident raider", "novice tomb raider", "into the den of giants",
			"not so great after all", "tempoross novice", "master of buckets",
			"calm before the storm", "fire in the hole!", "tempoross champion",
			"the lone angler", "dress like you mean it", "why cook?",
			"theatre of blood: sm adept", "anticoagulants", "appropriate tools",
			"they won't expect this", "chally time", "nylocas, on the rocks",
			"just to be safe", "don't look at me!", "no-pillar", "attack, step, wait",
			"pass it on", "theatre of blood: sm speed-chaser", "the ii jad challenge",
			"tzhaar-ket-rak's speed-trialist", "facing jad head-on iii",
			"the iv jad challenge", "tzhaar-ket-rak's speed-chaser", "facing jad head-on iv",
			"supplies? who needs 'em?", "multi-style specialist", "hard mode? completed it",
			"the vi jad challenge", "tzhaar-ket-rak's speed-runner", "it wasn't a fluke",
			"versatile drainer", "blind spot", "stop right there!", "personal space",
			"royal affairs", "harder mode i", "harder mode ii", "nylo sniper",
			"team work makes the dream work", "harder mode iii", "pack like a yak",
			"theatre: hm (trio) speed-runner", "theatre: hm (4-scale) speed-runner",
			"theatre: hm (5-scale) speed-runner", "theatre of blood: hm grandmaster",
			"pray for success", "sorry, what was that?", "mage of the ruins",
			"i'd rather not learn", "claw clipper", "praying to the gods", "weed whacker",
			"chitin penetrator", "insect repellent", "i can't reach that", "guardians no more",
			"zulrah adept", "vet'ion adept", "perfect sire", "unrequired antifire",
			"anti-bite mechanics", "hot on your feet", "3, 2, 1 - mage", "3, 2, 1 - range",
			"egniol diet", "crystalline warrior", "prayer smasher", "hard hitter",
			"nightmare (5-scale) speed-trialist", "from one king to another", "reminisce",
			"zulrah veteran", "snake rebound", "hazard prevention", "vet'eran",
			"together we'll fall", "redemption enthusiast", "mutta-diet",
			"dancing with statues", "cryo no more", "blizzard dodger", "kill it with fire",
			"demonic defence", "the bane of demons", "phantom muspah speed-runner",
			"phantom muspah manipulator", "can't wake up", "inferno speed-runner",
			"inferno grandmaster", "chambers of xeric grandmaster",
			"chambers of xeric: cm (trio) speed-runner",
			"chambers of xeric: cm (5-scale) speed-runner", "vorkath speed-runner",
			"the fremennik way", "faithless encounter", "theatre of blood grandmaster",
			"grotesque guardians speed-runner", "quick cutter", "whack-a-mole",
			"avoiding those little arms", "shayzien protector", "... 'til dawn",
			"ready to pounce", "inspect repellent", "walk straight pray true",
			"demon evasion", "precise positioning", "space is tight",
			"the worst ranged weapon", "wolf puncher", "wolf puncher ii"
	));

	static {
		VarManager.appendPersistentVarbit(CA_EASY_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_MEDIUM_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_HARD_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_ELITE_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_MASTER_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_GRANDMASTER_COMPLETION_TOTAL_VARBIT);
		for (int varpBit : CA_TIER_REWARD_STATUS_VARBITS) {
			VarManager.appendPersistentVarbit(varpBit);
		}
		for (int varp : CA_TASK_VARS) {
			VarManager.appendPersistentVarp(varp);
		}

		for (int enumIndx = 0; enumIndx < CA_TASK_TIER_ENUMS.length; enumIndx++) {
			final IntEnum categoryEnum = EnumDefinitions.getIntEnum(CA_TASK_TIER_ENUMS[enumIndx]);
			final ObjectSet<Int2IntMap.Entry> entrySet = categoryEnum.getValues().int2IntEntrySet();
			int completableCount = 0;
			for (final Int2IntMap.Entry entry : entrySet) {
				final StructDefinitions struct = StructDefinitions.get(entry.getIntValue());
				if (struct == null) continue;
				final String taskName = struct.getParamAsString(STRUCT_POINTER_TASK_NAME);
				if (taskName != null && INCOMPLETE_TASKS.contains(taskName.toLowerCase())) {
					continue;
				}
				completableCount++;
			}
			TOTAL_TASK_COUNT[enumIndx] = completableCount;
		}
	}

	private final transient Player player;

	//current flag set used for multi-stage tasks, doesn't save on logout and reset on other task started/completed
	private final transient Int2IntMap currentTaskFlagMap = new Int2IntOpenHashMap();

	public CALog(final Player player) {
		this.player = player;
	}

	public void complete(@NotNull CAType type) {
		if (type.isDisabled()) return;
		final int varId = CA_TASK_VARS[type.getVarIndex() >> 5];
		final int varValue = player.getVarManager().getValue(varId);
		final int bit = 1 << (type.getVarIndex() & 31);
		if ((varValue & bit) != 0) {
			return;
		}
		final CATierType tier = type.getTier();
		final String tierName = tier.name().toLowerCase();
		player.sendMessage("Congratulations! You've completed " + Utils.getAOrAn(tierName) + " " + tierName + " combat task: <col=40ff00>" + type.getName());
		player.notification("Combat Task Completed!", "Task Completed: " + Colour.WHITE.wrap(type.getName()), 16750623);
		player.getVarManager().sendVar(varId, varValue | bit);
		final int tasksOfTierCompleted = player.getVarManager().getBitValue(tier.getVarBit()) + 1;
		player.getVarManager().sendBit(tier.getVarBit(), tasksOfTierCompleted);
		final Optional<CABossType> optionalBossType = Stream.of(CABossType.values).filter(b -> b.ordinal() == type.getBossNumber() - 1).findFirst();
		if (optionalBossType.isPresent()) {
			final CABossType bossType = optionalBossType.get();
			player.getVarManager().sendBit(bossType.getTaskVarBit(), player.getVarManager().getBitValue(bossType.getTaskVarBit()) + 1);
		}
		if (tasksOfTierCompleted >= TOTAL_TASK_COUNT[tier.ordinal()]) {
			// This should be set to 2, to signify a completed task
			// Setting to 1, is a partial complete, or "started" / "progressed"
			player.getVarManager().sendBit(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()], 2); // OLD: 1
			//TODO dialogue
			player.sendMessage("Congratulations! You have finished all the combat achievement of tier: " + tier.name().toLowerCase());
		}
	}

	public void completeSilent(@NotNull CAType type) {
		final int varId = CA_TASK_VARS[type.getVarIndex() >> 5];
		final int varValue = player.getVarManager().getValue(varId);
		final int bit = 1 << (type.getVarIndex() & 31);
		if ((varValue & bit) != 0) {
			return;
		}
		final CATierType tier = type.getTier();
		player.getVarManager().sendVar(varId, varValue | bit);
		final int tasksOfTierCompleted = player.getVarManager().getBitValue(tier.getVarBit()) + 1;
		player.getVarManager().sendBit(tier.getVarBit(), tasksOfTierCompleted);
		final Optional<CABossType> optionalBossType = Stream.of(CABossType.values).filter(b -> b.ordinal() == type.getBossNumber() - 1).findFirst();
		if (optionalBossType.isPresent()) {
			final CABossType bossType = optionalBossType.get();
			player.getVarManager().sendBit(bossType.getTaskVarBit(), player.getVarManager().getBitValue(bossType.getTaskVarBit()) + 1);
		}
		if (tasksOfTierCompleted >= TOTAL_TASK_COUNT[tier.ordinal()])
			setTierCompleted(tier);
	}

	public boolean taskCompleted(@NotNull CAType type) {
		if (type.isDisabled()) {
			return true;
		}

		final int varId = CA_TASK_VARS[type.getVarIndex() >> 5];
		final int varValue = player.getVarManager().getValue(varId);
		final int bit = 1 << (type.getVarIndex() & 31);
		return (varValue & bit) != 0;
	}

	public void resetTask(int varIndex) {
		//temporary
		player.getVarManager().sendVar(CA_TASK_VARS[varIndex >> 5], varIndex & ~(1 << (varIndex & 31)));
		player.sendMessage("Reset task: " + Arrays.stream(CAType.values).filter(t -> t.getVarIndex() == varIndex).findFirst().get().toString().toLowerCase());
	}

	public void checkKcTask(String name, int kcRequired, CAType task) {
		if (player.getNotificationSettings().getKillcount(name) >= kcRequired) {
			complete(task);
		}
	}

	public boolean hasTierCompleted(CATierType tier) {
		return player.getVarManager().getBitValue(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()]) == 2;
	}

	public boolean hasAllTiersCompleted() {
		for (CATierType caTierType : CATierType.values) {
			if (!hasTierCompleted(caTierType)) {
				return false;
			}
		}

		return true;
	}

	public boolean completedPreviousTiers(CATierType tier) {
		for (int indx = 0; indx < tier.ordinal(); indx++) {
			if (!hasTierCompleted(CATierType.values[indx])) {
				return false;
			}
		}

		return true;
	}

	public boolean isEligibleForRewards(CATierType tier) {
		return player.getVarManager().getBitValue(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()]) == 1;
	}

	public void setTierCompleted(CATierType tier) {
		player.getVarManager().sendBit(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()], 2);
	}

	public void setCurrentTaskValue(CAType task, int value) {
		currentTaskFlagMap.put(task.ordinal(), value);
	}

	public void addCurrentTaskFlag(CAType task, int flag) {
		final int key = task.ordinal();
		final int currentFlag = currentTaskFlagMap.getOrDefault(key, 0);
		currentTaskFlagMap.put(key, currentFlag | flag);
	}

	public void removeCurrentTaskFlag(CAType task, int flag) {
		final int key = task.ordinal();
		final int currentFlag = currentTaskFlagMap.getOrDefault(key, 0);
		currentTaskFlagMap.put(key, currentFlag & ~flag);
	}

	public void removeCurrentTask(CAType task) {
		currentTaskFlagMap.remove(task.ordinal());
	}

	public int getCurrentTaskValue(CAType task) {
		return currentTaskFlagMap.getOrDefault(task.ordinal(), 0);
	}

	public boolean hasCurrentTaskFlags(CAType task, int... flags) {
		final int currentFlag = currentTaskFlagMap.getOrDefault(task.ordinal(), 0);
		for (int flag : flags) {
			if ((currentFlag  & flag) == 0) {
				return false;
			}
		}
		return true;
	}

	public void resetCurrentTaskFlagSet(CAType task) {
		currentTaskFlagMap.put(task.ordinal(), 0);
	}

}