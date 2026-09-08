package org.jesse.game.content.chambersofxeric.greatolm.scripts;

import org.jesse.game.content.chambersofxeric.ScalingMechanics;
import org.jesse.game.content.chambersofxeric.greatolm.GreatOlm;
import org.jesse.game.content.chambersofxeric.greatolm.LeftClaw;
import org.jesse.game.content.chambersofxeric.greatolm.OlmCombatScript;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.content.skills.prayer.PrayerManager;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.MovementLock;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.CharacterLoop;
import org.jesse.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kris | 16. jaan 2018 : 5:08.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Lightning implements OlmCombatScript {
	private static final IntArrayList tiles = new IntArrayList(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
	private static final Graphics lightningGraphics = new Graphics(1356);
	private final List<LightningSpiral> spirals = new ArrayList<>();
	private static final Animation electrocutionAnimation = new Animation(3170);
	private static final SoundEffect sound = new SoundEffect(3887, 15, 0);
	public static final SoundEffect walkFailSound = new SoundEffect(154);

	@Override
	public void handle(final GreatOlm olm) {
		olm.getScripts().add(this.getClass());
		if (olm.getRoom().getLeftClaw() != null) {
			olm.getRoom().getLeftClaw().displaySign(LeftClaw.LIGHTNING_SIGN);
		}
		int amount = ScalingMechanics.getOlmLightningCount(olm);
		final IntArrayList xAxis = new IntArrayList(tiles);
		int northCount = 0;
		int southCount = 0;
		while (amount-- > 0) {
			final int x = xAxis.removeInt(Utils.random(xAxis.size() - 1));
			//If north has (amount - 1) lightnings, it means this is the last roll and it must be put to the south & vice versa. There needs to be at least one lightning on each side.
			final boolean north = (northCount != amount - 1) && ((southCount == amount - 1) || Utils.random(1) == 0);
			final Location corner = north ? olm.getLightningLowerCorner() : olm.getLightningUpperCorner();
			final Location tile = new Location(corner.getX() + x, corner.getY(), corner.getPlane());
			spirals.add(new LightningSpiral(tile, north));
			if (north) {
				northCount++;
			} else {
				southCount++;
			}
		}
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks = 18;
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed()) {
					stop();
					return;
				}
				if (--ticks == 0) {
					stop();
					olm.getScripts().remove(Lightning.this.getClass());
					return;
				}
				CharacterLoop.forEach(olm.getMiddleLocation(), 30, Player.class, p -> {
					LightningSpiral northern = null;
					LightningSpiral southern = null;
					for (final Lightning.LightningSpiral lightning : spirals) {
						if (lightning.isNorth()) {
							if (northern == null || northern.getTile().getDistance(p.getLocation()) > lightning.getTile().getDistance(p.getLocation())) {
								northern = lightning;
							}
						} else {
							if (southern == null || southern.getTile().getDistance(p.getLocation()) > lightning.getTile().getDistance(p.getLocation())) {
								southern = lightning;
							}
						}
					}
					if (northern != null) {
						World.sendSoundEffect(new Location(northern.getTile()), sound);
					}
					if (southern != null) {
						World.sendSoundEffect(new Location(southern.getTile()), sound);
					}
				});
				spirals.forEach(spiral -> {
					final Location tile = new Location(spiral.getTile());
					World.sendGraphics(lightningGraphics, tile);
					for (final Player player : olm.everyone(GreatOlm.ENTIRE_CHAMBER)) {
						if (player.getLocation().getPositionHash() == spiral.getTile().getPositionHash()) {
							player.applyHit(new Hit(olm, Utils.random(15, 25), HitType.REGULAR));
							player.sendMessage("<col=ff0000>You've been electrocuted to the spot!");
							deactivateOverheadProtectionPrayers(player, player.getPrayerManager(), true);
							player.setAnimation(electrocutionAnimation);
							player.addMovementLock(new MovementLock(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5), null, () -> player.sendSound(walkFailSound)));
						}
					}
					spiral.getTile().moveLocation(0, spiral.isNorth() ? 1 : -1, 0);
				});
			}
		}, 0, 0);
	}

	public static void deactivateOverheadProtectionPrayers(final Player player, final PrayerManager prayer, final boolean injure) {
		if (injure) {
			player.getTemporaryAttributes().put("prayer delay", WorldThread.getCurrentCycle() + TimeUnit.SECONDS.toTicks(3));
		}
		boolean bool = false;
		if (prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
			prayer.deactivatePrayer(Prayer.PROTECT_FROM_MAGIC);
			bool = true;
		}
		if (prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			prayer.deactivatePrayer(Prayer.PROTECT_FROM_MELEE);
			bool = true;
		}
		if (prayer.isActive(Prayer.PROTECT_FROM_MISSILES)) {
			prayer.deactivatePrayer(Prayer.PROTECT_FROM_MISSILES);
			bool = true;
		}
		if (bool) {
			player.sendMessage("You've been injured and can't use protection prayers!");
		}
	}


	private static final class LightningSpiral {
		private final Location tile;
		private final boolean north;

		LightningSpiral(final Location tile, final boolean north) {
			this.tile = tile;
			this.north = north;
		}

		public Location getTile() {
			return tile;
		}

		public boolean isNorth() {
			return north;
		}
	}
}
