package org.jesse.game.world.region.area;

import com.google.common.eventbus.Subscribe;
import org.jesse.plugins.events.ServerLaunchEvent;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author Tommeh | 10-3-2019 | 23:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class EssenceMineArea extends PolygonRegionArea {

    @Subscribe
    public static void onServerLaunch(ServerLaunchEvent event) {
        World.spawnObject(new WorldObject(26254, 10, 0, new Location(2931, 4822, 0)));
        World.spawnObject(new WorldObject(26254, 10, 0, new Location(2896, 4821, 0)));
        World.spawnObject(new WorldObject(26254, 10, 1, new Location(2900, 4845, 0)));
        World.spawnObject(new WorldObject(26254, 10, 3, new Location(2920, 4848, 0)));
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2881, 4820 }, { 2894, 4831 }, { 2880, 4852 }, { 2899, 4862 }, { 2910, 4847 }, { 2921, 4859 }, { 2938, 4859 }, { 2938, 4842 }, { 2923, 4836 }, { 2938, 4822 }, { 2938, 4805 }, { 2920, 4805 }, { 2913, 4816 }, { 2898, 4803 }, { 2881, 4803 } })};
    }

    @Override
    public void enter(Player player) {
        if (GlobalAreaManager.getArea(EvilBobIsland.class).inside(player.getLastLocation())) {
            return;
        }
        player.addAttribute("essence_mine_departure", (player.getLastLocation() == null || inside(player.getLastLocation())) ? 0 : player.getLastLocation().getPositionHash());
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getAttributes().remove("essence_mine_departure");
    }

    @Override
    public String name() {
        return "Essence Mine";
    }
}
