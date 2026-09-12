package org.jesse.game.world.region.area.godwars;

import org.jesse.game.content.godwars.GodType;
import org.jesse.game.content.godwars.instance.GodwarsInstance;
import org.jesse.game.content.godwars.npcs.*;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.*;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin;
import org.jesse.game.world.region.area.plugins.DeathPlugin;
import org.jesse.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import org.jetbrains.annotations.NotNull;

public class GodwarsDungeonArea extends PolygonRegionArea implements CannonRestrictionPlugin, RandomEventRestrictionPlugin, DeathPlugin {
	private static final int KILLCOUNT_VISIBLE = 3625;
	public static final RSPolygon polygon = new RSPolygon(new int[][] {
			{2816, 5184}, {2815, 5375}, {2943, 5376}, {2943, 5184}
	});

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { polygon };
	}

	public static final void enterArea(@NotNull final Player player) {
		CharacterLoop.forEach(player.getLocation(), 25, NPC.class, npc -> {
			if (!isChamberNPC(npc)) {
				if (npc.getCombat().getTarget() == player) {
					npc.getCombat().removeTarget();
				}
			}
		});
		player.setForceMultiArea(true);
		player.getVarManager().sendVar(KILLCOUNT_VISIBLE, 1);
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 406);
		refreshKillcount(player);
	}

	@Override
	public void enter(final Player player) {
		enterArea(player);
	}

	public static final void leaveArea(@NotNull final Player player) {
		player.setForceMultiArea(false);
		player.getHpHud().close();
		CharacterLoop.forEach(player.getLocation(), 25, NPC.class, npc -> {
			if (isChamberNPC(npc)) {
				if (npc.getCombat().getTarget() == player) {
					npc.getCombat().removeTarget();
				}
			}
		});
		final RegionArea area = GlobalAreaManager.getArea(player.getLocation());
		if (area instanceof GodwarsDungeonArea || area instanceof GodwarsInstance) {
			return;
		}
		resetKillcount(player);
		player.getVarManager().sendVar(KILLCOUNT_VISIBLE, 0);
		player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
		if (player.getTemporaryAttributes().get("last hint arrow") != null) {
			player.getPacketDispatcher().resetHintArrow();
		}
	}

	@Override
	public void leave(final Player player, boolean logout) {
		leaveArea(player);
	}

	public static final boolean isChamberNPC(final NPC npc) {
		return npc instanceof GodwarsBossMinion || npc instanceof KreeArra
				|| npc instanceof GeneralGraardor || npc instanceof CommanderZilyana
				|| npc instanceof KrilTsutsaroth;
	}

	public static void refreshKillcount(final Player player) {
		player.getVarManager().sendBit(GodType.SARADOMIN.killcountVarbit(), player.getNumericAttribute(GodType.SARADOMIN.getKey()).intValue());
		player.getVarManager().sendBit(GodType.ARMADYL.killcountVarbit(), player.getNumericAttribute(GodType.ARMADYL.getKey()).intValue());
		player.getVarManager().sendBit(GodType.BANDOS.killcountVarbit(), player.getNumericAttribute(GodType.BANDOS.getKey()).intValue());
		player.getVarManager().sendBit(GodType.ZAMORAK.killcountVarbit(), player.getNumericAttribute(GodType.ZAMORAK.getKey()).intValue());
		player.getVarManager().sendBit(GodType.ANCIENT.killcountVarbit(), player.getNumericAttribute(GodType.ANCIENT.getKey()).intValue());
	}

	public static final void resetKillcount(final Player player) {
		player.getAttributes().remove(GodType.SARADOMIN.getKey());
		player.getAttributes().remove(GodType.ARMADYL.getKey());
		player.getAttributes().remove(GodType.BANDOS.getKey());
		player.getAttributes().remove(GodType.ZAMORAK.getKey());
		player.getAttributes().remove(GodType.ANCIENT.getKey());
	}

	@Override
	public String name() {
		return "Godwars Dungeon";
	}

  public RSPolygon chamberArea() {
    return this.polygons()[0];
  }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }
}
