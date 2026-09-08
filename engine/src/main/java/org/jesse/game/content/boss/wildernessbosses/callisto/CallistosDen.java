package org.jesse.game.content.boss.wildernessbosses.callisto;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Position;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;
import org.jesse.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Andys1814
 */
public final class CallistosDen extends WildernessArea implements LootBroadcastPlugin {

    private Callisto callisto;

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 3341, 10309 },
                { 3377, 10309 },
                { 3377, 10347 },
                { 3341, 10347 }
        })};
    }

    @Override
    public void enter(Player player) {
        if (callisto == null) {
            callisto = new Callisto(NpcId.CALLISTO_6609, new Location(3359, 10328), Direction.SOUTH, 3);
            callisto.spawn();
        }
        if (!callisto.isFinished()) {
            player.getHpHud().open(NpcId.CALLISTO_6609, callisto.getHitpoints());
        }
        super.enter(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getHpHud().close();
        super.leave(player, logout);
    }

    @Override
    public String name() {
        return "Callisto's Den";
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }
}
