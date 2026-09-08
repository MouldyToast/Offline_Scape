package org.jesse.game.content.minigame.barrows;

import org.jesse.game.GameInterface;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.MinimapState;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.net.packet.PacketDispatcher;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.calog.CAType;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.CycleProcessPlugin;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;
import org.jesse.game.world.region.area.plugins.PartialMovementPlugin;
import org.jesse.game.world.region.area.plugins.TeleportMovementPlugin;

import java.util.Optional;

/**
 * @author Kris | 31. mai 2018 : 01:57:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BarrowsChambersArea extends PolygonRegionArea implements CycleProcessPlugin, PartialMovementPlugin, TeleportMovementPlugin, EntityAttackPlugin {
	private static final Graphics ROCKS_FALL_GFX = new Graphics(60);
	private static final int WIGHT_SCRIPT = 894;
	private static final int BASE_PRAYER_DRAIN = 8;

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {{3524, 9723}, {3524, 9667}, {3580, 9667}, {3580, 9724}}, 0, 3)};
	}

	@Override
	public void enter(final Player player) {
		final Barrows barrows = player.getBarrows();
		barrows.refreshLadder(player.getLocation());
		barrows.refreshDoors();
		barrows.refreshShaking();
		player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
		GameInterface.BARROWS_OVERLAY.open(player);
		player.getCombatAchievements().removeCurrentTaskFlag(CAType.DEFENCE_WHAT_DEFENCE, Barrows.CA_TASK_MAGIC_DAMAGE_ONLY);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		player.getInterfaceHandler().closeInterface(GameInterface.BARROWS_OVERLAY);
		final PacketDispatcher dispatcher = player.getPacketDispatcher();
		dispatcher.sendMinimapState(MinimapState.ENABLED);
		final Barrows barrows = player.getBarrows();
		barrows.removeTarget();
		barrows.resetTimer();
		if (barrows.isLooted()) {
			dispatcher.resetCamera();
		}
		player.getCombatAchievements().removeCurrentTaskFlag(CAType.DEFENCE_WHAT_DEFENCE, Barrows.CA_TASK_MAGIC_DAMAGE_ONLY);
	}

	private int getRandomComponent() {
		return GameInterface.BARROWS_OVERLAY.getId() << 16 | Utils.random(2, 7);
	}

	@Override
	public String name() {
		return "Barrows chambers";
	}

	@Override
	public void process() {
		if (players.isEmpty()) return;
		for (final Player player : players) {
			if (player.getPrayerManager().getPrayerPoints() > 0
					&& player.getCombatAchievements().hasCurrentTaskFlags(CAType.FAITHLESS_CRYPT_RUN, Barrows.CA_TASK_FAITHLESS_RUN)) {
				player.getCombatAchievements().removeCurrentTaskFlag(CAType.FAITHLESS_CRYPT_RUN, Barrows.CA_TASK_FAITHLESS_RUN);
			}
			final Barrows barrows = player.getBarrows();
			final int timer = barrows.getAndDecrementTimer();
			if (timer > 0) continue;
			if (barrows.isLooted()) {
				if (player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) continue;
				player.setGraphics(ROCKS_FALL_GFX);
				player.applyHit(new Hit(Utils.random(6), HitType.REGULAR));
				continue;
			}
			final Optional<BarrowsWight> optionalWight = barrows.getRandomSlainWight();
			final int itemId = player.getEquipment().getId(EquipmentSlot.SHIELD);
			if (!((itemId >= ItemId.GHOMMALS_HILT_2 && itemId <= ItemId.GHOMMALS_HILT_6)
					|| (itemId >= ItemId.GHOMMALS_AVERNIC_DEFENDER_5 && itemId <= ItemId.GHOMMALS_AVERNIC_DEFENDER_6_L))) {
				player.getPrayerManager().drainPrayerPoints(BASE_PRAYER_DRAIN + Utils.random(barrows.getSlainWights().size()));
			}
			optionalWight.ifPresent(wight -> player.getPacketDispatcher().sendClientScript(WIGHT_SCRIPT, getRandomComponent(), wight.getModel(player)));
		}
	}

	@Override
	public boolean processMovement(Player player, int x, int y) {
		player.getBarrows().refreshLadder(player.getLocation());
		return true;
	}

	@Override
	public void processMovement(Player player, Location destination) {
		player.getBarrows().refreshLadder(destination);
	}

	@Override
	public boolean attack(Player player, Entity entity, PlayerCombat combat) {
		if (entity instanceof BarrowsWightNPC) {
			if (((BarrowsWightNPC) entity).owner.get() != player) {
				player.sendMessage("This is not your target.");
				return false;
			}
		}
		return true;
	}
}
