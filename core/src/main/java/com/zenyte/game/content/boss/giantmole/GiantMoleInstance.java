package com.zenyte.game.content.boss.giantmole;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.npc.impl.GiantMoleInstanced;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

public class GiantMoleInstance extends DynamicArea implements EntityAttackPlugin, DeathPlugin, LogoutPlugin, LootBroadcastPlugin {

    public static final Location OUTSIDE_TILE = new Location(3005, 3380, 0);
    public static final Location INSIDE_TILE = new Location(1759, 5186, 0);
    private final Player player;

    public GiantMoleInstance(final Player player, final AllocatedArea allocatedArea, final int copiedChunkX, final int copiedChunkY) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
        this.player = player;
    }

    @Override
    public void constructed() {
        player.setLocation(getLocation(INSIDE_TILE));
        new GiantMoleInstanced(NpcId.GIANT_MOLE, getLocation(INSIDE_TILE), Direction.SOUTH, 64, this).spawn();
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
        if (logout) {
            player.forceLocation(OUTSIDE_TILE);
        }
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        player.setLocation(OUTSIDE_TILE);
    }

    @Override
    public Location onLoginLocation() {
        return OUTSIDE_TILE;
    }

    @Override
    public String name() {
        return "'Falador Mole Lair";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        return true;
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
