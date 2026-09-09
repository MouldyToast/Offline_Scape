package org.jesse.game;

import org.jesse.game.world.info.WorldConfig;
import org.jesse.game.world.info.WorldProfile;
import org.jesse.ContentConstants;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Kris | 5. march 2018 : 17:05.26
 * @author Jire
 */
public class GameConstants {

	/**
	 * The current cache revision.
	 */
	public static final int REVISION = 228;
	public static final int CLIENT_VERSION = 1;
	public static final int WORLD_CYCLE_TIME = 600;
	public static final int LOGIN_PORT = 43596;
	public static final boolean DEV_DEBUG = true;
	public static final Location REGISTRATION_LOCATION = new Location(3093, 3107, 0);
	public static final String SERVER_NAME = ContentConstants.SERVER_NAME;
	public static final String SERVER_CHANNEL_NAME = "help";
	public static final String SERVER_WEBSITE_URL = "";
	public static final String SERVER_ACCOUNT_URL = "";
	public static final String SERVER_FORUMS_URL = "";
	public static final String SERVER_STORE_URL = "";
	public static final String SERVER_VOTE_URL = "";
	public static final String SERVER_RULES_URL = "";
	public static final String DISCORD_INVITE = "";

	public static WorldConfig WORLD_CONFIG;

    /**
	 * The instance world profile.
	 */
	public static WorldProfile WORLD_PROFILE;

	public static boolean CYCLE_DEBUG = false;

	public static boolean CHECK_HUNTER_TRAPS_QUANTITY = true;

	public static boolean ANTIKNOX = false;
	public static boolean WHITELISTING = false;
	public static boolean DUEL_ARENA = true;
	public static boolean GROTESQUE_GUARDIANS = true;
	public static boolean PURGING_CHUNKS = true;

	public static final Set<String> whitelistedUsernames = new ObjectOpenHashSet<>();
	public static final Set<String> blacklistedNpcNamesForGold = new ObjectOpenHashSet<>();
	public static final Set<Integer> blacklistedNpcIdsForGold = new ObjectOpenHashSet<>();

	static {
		blacklistedNpcIdsForGold.addAll(List.of(
				NpcId.MANIACAL_MONKEY, NpcId.MANIACAL_MONKEY_ARCHER, NpcId.MANIACAL_MONKEY_7213,
				NpcId.MANIACAL_MONKEY_7214, NpcId.MANIACAL_MONKEY_7215, NpcId.MANIACAL_MONKEY_7216,
				NpcId.MANIACAL_MONKEY_7118
		));
	}

	public static final boolean BOUNTY_HUNTER = false;

	public static double defenceMultiplier = 0.825;

	public static int randomEvent = (int) TimeUnit.HOURS.toTicks(5);

	public static boolean CHAMBERS_OF_XERIC = true;
	public static boolean ALCHEMICAL_HYDRA = true;

	//!Case sensitive usernames
	public static final String[] owners = new String[] {
			"jesse"
	};

	static {
		whitelistedUsernames.addAll(Arrays.asList(owners));
	}

	public static boolean isOwner(final Player player) {
		return ArrayUtils.contains(owners, player.getUsername().toLowerCase());
	}

	/**
	 * Whether the game should connect the forums database and validate the
	 * login on a new registration or not.
	 */
	public static final boolean REGISTER_ON_FORUMS = false;

	public static final float TICK = WORLD_CYCLE_TIME;

	public static final float CLIENT_CYCLE = 20;

	public static final float CYCLES_PER_TICK = TICK / CLIENT_CYCLE;


	public static boolean BOOSTED_XP = false;

	public static boolean BOOSTED_SKILLING_PETS = false;
	public static final double BOOSTED_SKILLING_PET_RATE = 0.25; // this is a 15% boost

	public static boolean BOOSTED_BOSS_PETS = false;
	public static final double BOOSTED_BOSS_PET_RATE = 0.25; // this is a 15% boost

	public static int BOOSTED_XP_MODIFIER = 50;

	public static boolean FILTERING_DUPLICATE_JS5_REQUESTS = false;

	public static final String UPDATE_LOG_BROADCAST = "You can do this, believe in yourself!";

	public static final String UPDATE_LOG_URL = "";

	/**
	 * Item ids added to this list are restricted from trade, selling to shops, and posting on the GE. It is primarily used as an economy control measure
	 */
	public static final IntArrayList RESTRICTED_TRADE_ITEMS = new IntArrayList();
	public static boolean EXTRA_GP_ON_NPC_KILL_ENABLED = true;
	public static int EXTRA_GP_ON_NPC_KILL_RATE = 2000;
	public static int EXTRA_GP_ON_NPC_CANNON_MULTIPLIER = 10;
	public static boolean REMOVE_RESTRICTIONS_ON_POOLS = false;
}
