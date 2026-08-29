package com.zenyte.game.content.colosseum;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.EquipmentPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.logger.NearRealityPrintStream;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.Optional;
import java.util.function.BiConsumer;

public class ColosseumInstance extends DynamicArea implements EquipmentPlugin, CannonRestrictionPlugin, DeathPlugin {

	public static final String DEATHS_ATTRIBUTE = "colosseum_deaths";
	public static final String ATTEMPTS_ATTRIBUTE = "colosseum_attempts";

	private static final Animation FALL_BACK_ANIMATION = new Animation(2390);
	public static final Location SPAWN_LOCATION = new Location(1800, 9505);
	private static final int[] BORDER_LOC_IDS = { 50753, 50754, 50755, 50756, 50757, 50758 };
	private static final Location[] BORDER_LOCATIONS = {
			new Location(1817, 3099),//southwest
			new Location(1832, 3114)//northeast
	};

	private final Player player;
	private SolHeredit solHeredit;
	private final Location[] arena = new Location[2];
	private final Container rewards;

	protected ColosseumInstance(AllocatedArea allocatedArea, Player player) {
		super(allocatedArea, 7216);
		this.player = player;
		this.rewards = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.COLOSSEUM_REWARDS, Optional.of(player));
	}

	@Override
	public void constructed() {
		createBorder();
		solHeredit = new SolHeredit(getBaseLocation(31, 36), this);
	}

	@Override
	public void enter(Player player) {
	}

	public void startFight() {
		Location spawnLocation = getBaseLocation(33, 31);
		player.setLocation(spawnLocation);

		solHeredit.lock();
		solHeredit.spawn();
		solHeredit.lock(3);
		solHeredit.setAnimation(SolHeredit.JUMP_DOWN_ANIMATION);
		solHeredit.setGraphics(SolHeredit.JUMP_DOWN_GFX);

		ColosseumStatistics.statistics.incrementGlobalAttempts();
		player.incrementNumericAttribute(ATTEMPTS_ATTRIBUTE, 1);

		player.getBossTimer().startTracking(SolHeredit.TIMER_NAME);
		player.getHpHud().open(solHeredit.getId(), solHeredit.getMaxHitpoints());
	}

	@Override
	public void leave(Player player, boolean logout) {
		player.getHpHud().close();
	}

	@Override
	public String name() {
		return "Sol Heredit instance";
	}

	@Override
	public boolean unequip(Player player, Item item, int slot) {
		if (solHeredit == null) {
			return true;
		}

		SolHeredit.GrappleStyle grappleStyle = solHeredit.getGrappleStyle();
		if (grappleStyle == null) {
			return true;
		}

		if (grappleStyle.getSlot().getSlot() != slot) {
			player.sendMessage(Colour.RED.wrap("You defended the wrong body part!"));
		}

		solHeredit.setClickedSlot(slot);
		return false;
	}

	@Override
	public boolean isSafe() {
		return false;
	}

	@Override
	public String getDeathInformation() {
		return "";
	}

	@Override
	public Location getRespawnLocation() {
		return new Location(SPAWN_LOCATION.getX() + Utils.random(4), SPAWN_LOCATION.getY() + Utils.random(4));
	}

	@Override
	public boolean sendDeath(Player player, Entity source) {
		ColosseumStatistics.statistics.increaseDeathCount();
		player.incrementNumericAttribute(DEATHS_ATTRIBUTE, 1);
		solHeredit.say(Utils.random(SolHeredit.KILL_PLAYER_MESSAGES));
		return false;
	}

	@Override
	public Location gravestoneLocation() {
		return new Location(SPAWN_LOCATION.getX() + Utils.random(4), SPAWN_LOCATION.getY() + Utils.random(4));
	}

	private void createBorder() {
		Location sw = getLocation(BORDER_LOCATIONS[0]);
		Location ne = getLocation(BORDER_LOCATIONS[1]);

		//Trim down borders so it doesn't touch the guards with shields
		arena[0] = sw.transform(1, 1);
		arena[1] = ne.transform(-1, -1);

		int minX = sw.getX();
		int minY = sw.getY();
		int maxX = ne.getX();
		int maxY = ne.getY();

		// Bottom edge (left to right)
		for (int x = minX + 2; x <= maxX - 2; x++) {
			World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 2, new Location(x, minY)));
		}

		// Right edge (bottom to top)
		for (int y = minY + 2; y <= maxY - 2; y++) {
			World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 1, new Location(maxX, y)));
		}

		// Top edge (right to left)
		for (int x = maxX - 2; x >= minX + 2; x--) {
			World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 0, new Location(x, maxY)));
		}

		// Left edge (top to bottom)
		for (int y = maxY - 2; y >= minY + 2; y--) {
			World.spawnObject(new WorldObject(Utils.random(BORDER_LOC_IDS), 10, 3, new Location(minX, y)));
		}
	}

	public boolean outsideOfArena(int x, int y) {
		Location sw = getArenaSw();
		Location ne = getArenaNe();
		return x < Math.min(sw.getX(), ne.getX()) || x > Math.max(sw.getX(), ne.getX()) || y < Math.min(sw.getY(), ne.getY()) || y > Math.max(sw.getY(), ne.getY());
	}

	public Player getPlayer() {
		return player;
	}

	public Location getArenaSw() {
		return arena[0];
	}

	public Location getArenaNe() {
		return arena[1];
	}

	public Container getRewards() {
		return rewards;
	}

	public void grantRewards() {
		rewards.clear();

		Item item = null;
		int dryStreak = PlayerAttributesKt.getSolHereditQuiverDryStreak(player);
		int newDryStreak = dryStreak + 1;
		//Unlike other boss pets which are generally a tertiary drop after defeating the boss, Smol Heredit is not, being awarded as a main drop if rolled on.
		if (Utils.randomBoolean(200)) {
			BossPet.SMOL_HEREDIT.roll(player, BossPet.SMOL_HEREDIT.getRarity(player, -1));
		} else if (newDryStreak >= 30 || Utils.randomBoolean(30)) {
			item = new Item(ItemId.DIZANAS_QUIVER_UNCHARGED);
			newDryStreak = 0;
		} else {
			ColosseumRewards colosseumRewards = ColosseumRewards.getRandom();
			if (colosseumRewards != null) {//Should never be null, but just in sanity of intellij code checker
				if (colosseumRewards == ColosseumRewards.DROP_12) {
					final Int2IntOpenHashMap pieces = new Int2IntOpenHashMap(3);
					pieces.addTo(ItemId.SUNFIRE_FANATIC_HELM, player.getAmountOf(ItemId.SUNFIRE_FANATIC_HELM));
					pieces.addTo(ItemId.SUNFIRE_FANATIC_CHAUSSES, player.getAmountOf(ItemId.SUNFIRE_FANATIC_CHAUSSES));
					pieces.addTo(ItemId.SUNFIRE_FANATIC_CUIRASS, player.getAmountOf(ItemId.SUNFIRE_FANATIC_CUIRASS));
					int smallestAmountItemId = -1;
					int smallestAmountItemAmount = Integer.MAX_VALUE;
					for (final Int2IntMap.Entry entry : pieces.int2IntEntrySet()) {
						if (entry.getIntValue() <= smallestAmountItemAmount) {
							smallestAmountItemId = entry.getIntKey();
							smallestAmountItemAmount = entry.getIntValue();
						}
					}
					item = new Item(smallestAmountItemId, Utils.random(colosseumRewards.getMin(), colosseumRewards.getMax()));
				} else {
					item = new Item(colosseumRewards.getItemId(), Utils.random(colosseumRewards.getMin(), colosseumRewards.getMax()));
				}
			}
		}

		PlayerAttributesKt.setSolHereditQuiverDryStreak(player, newDryStreak);
		if (item != null) {
			rewards.add(new Item(ItemId.SUNFIRE_SPLINTERS, 1_500 + Utils.random(50)));
			rewards.add(item);
		}
	}

	public void fillLine(int startX, int startY, Direction direction, int length, BiConsumer<Location, Integer> tileConsumer, BiConsumer<Location, Integer> endConsumer) {
		int endX = startX + direction.getOffsetX() * length;
		int endY = startY + direction.getOffsetY() * length;
		int dx = Math.abs(endX - startX);
		int dy = Math.abs(endY - startY);
		int sx = Integer.signum(endX - startX);
		int sy = Integer.signum(endY - startY);
		int err = dx - dy;
		int n = 0;
		while (true) {
			if (tileConsumer != null) {
				tileConsumer.accept(new Location(startX, startY), n);
			}
			n++;
			if (startX == endX && startY == endY)
				break;
			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				startX += sx;
			}
			if (e2 < dx) {
				err += dx;
				startY += sy;
			}

			if (outsideOfArena(startX, startY)) {
				break;
			}
		}

		if (endConsumer != null) {
			endConsumer.accept(new Location(startX, startY), n);
		}
	}

	public static void checkUnderChest(Player player, WorldObject object) {
		Location playerLocation = player.getLocation();
		if (playerLocation.getX() >= object.getX() && playerLocation.getY() >= object.getY() &&
				playerLocation.getX() <= object.getX() + object.getDefinitions().getSizeX() && playerLocation.getY() <= object.getY() + object.getDefinitions().getSizeY()) {
			Location destination = object.transform(-1, 1);
			final int direction = DirectionUtil.getFaceDirection(player.getX() - destination.getX(), player.getY() - destination.getY());
			player.setAnimation(FALL_BACK_ANIMATION);
			player.setForceMovement(new ForceMovement(destination, 30, direction));
			WorldTasksManager.schedule(() -> player.setLocation(destination));
		}
	}

	public void spawnChest() {
		WorldObject chest = new WorldObject(ObjectId.REWARDS_CHEST_50741, 10, 1, getLocation(1829, 3105));
		World.spawnObject(chest);
		checkUnderChest(player, chest);
		NPC minimus = new NPC(NpcId.MINIMUS_12808, getLocation(1830, 3103), Direction.WEST, 0);
		minimus.spawn();
		//Needs to be 1 tick later?
		WorldTasksManager.schedule(() -> minimus.setOptionMask(30));
	}

	public static void createInstance(Player player) {
		AllocatedArea allocatedArea;
		try {
			allocatedArea = MapBuilder.findEmptyChunk(64, 64);
		} catch (OutOfSpaceException e) {
			e.printStackTrace(NearRealityPrintStream.getErrorStream());
			return;
		}

		ColosseumInstance instance = new ColosseumInstance(allocatedArea, player);
		instance.constructRegion();
		if (player.getNotificationSettings().getKillcount(SolHeredit.TIMER_NAME) > 0) {
			new FadeScreen(player, instance::startFight).fade(3);
		} else {
			new FadeScreen(player).fade(4, false);
			WorldTasksManager.schedule(() -> player.getCutsceneManager().play(new ColosseumCutscene(instance)), 1);
		}
	}

}
