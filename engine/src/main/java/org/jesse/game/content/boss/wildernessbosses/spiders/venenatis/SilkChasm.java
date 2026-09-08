package org.jesse.game.content.boss.wildernessbosses.spiders.venenatis;

import org.jesse.game.world.Position;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;
import org.jesse.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Cresinkel
 */
public class SilkChasm extends WildernessArea implements LootBroadcastPlugin {

    private boolean first = true;

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 3404, 10183 },
                { 3442, 10183 },
                { 3442, 10226 },
                { 3404, 10226 }
        })};
    }

    @Override
    public void enter(Player player) {
        if (first) {
            World.spawnNPC(NpcId.VENENATIS_6610, new Location(3423, 10203, 2));
            first = false;
        }
        player.getHpHud().open(NpcId.VENENATIS_6610, 850);
        super.enter(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        super.leave(player, logout);
        if(player.getHpHud() != null)
            player.getHpHud().close();
    }

    @Override
    public String name() {
        return "Silk Chasm";
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }
}
