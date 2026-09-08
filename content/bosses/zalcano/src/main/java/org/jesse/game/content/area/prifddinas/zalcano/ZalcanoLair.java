package org.jesse.game.content.area.prifddinas.zalcano;

import org.jesse.game.world.Position;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin;
import org.jesse.game.world.region.area.plugins.DeathPlugin;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;

public class ZalcanoLair extends PolygonRegionArea implements DeathPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

    public static final String NAME = "Zalcano Lair";

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {3046, 6036},
                        {3022, 6036},
                        {3022, 6062},
                        {3046, 6062},
                })
        };
    }

    @Override
    public void enter(Player player) {
        player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, 0);
    }

    @Override
    public void leave(Player player, boolean logout) {
        ZalcanoInstance.deleteTephra(player);
        player.getCombatAchievements().removeCurrentTask(CAType.PERFECT_ZALCANO);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return "Zalcano";
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        ZalcanoInstance.deleteTephra(player);
        return DeathPlugin.super.sendDeath(player, source);
    }
}
