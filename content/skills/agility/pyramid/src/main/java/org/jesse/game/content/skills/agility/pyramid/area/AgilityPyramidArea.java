package org.jesse.game.content.skills.agility.pyramid.area;

import org.jesse.game.content.skills.agility.pyramid.AgilityPyramid;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.SouthernDesertArea;
import org.jesse.game.world.region.area.plugins.CycleProcessPlugin;
import org.jesse.game.world.region.area.plugins.FullMovementPlugin;
import org.jesse.game.world.region.area.plugins.TeleportMovementPlugin;

public class AgilityPyramidArea extends SouthernDesertArea implements CycleProcessPlugin, FullMovementPlugin, TeleportMovementPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{3353, 2830}, {3353, 2853}, {3376, 2853}, {3376, 2830}}), new RSPolygon(new int[][] {{3033, 4686}, {3033, 4709}, {3056, 4709}, {3056, 4686}})};
    }

    private int ticks;

    @Override
    public void process() {
        super.process();
        if (++ticks % 16 == 0) {
            MovingBlock.moveBlocks();
        }
    }

    @Override
    public boolean processMovement(Player player, int x, int y) {
        RollingBlock.roll(player, x, y);
        return true;
    }

    @Override
    public String name() {
        return "Agility Pyramid";
    }

    public static Location getHigherTile(final Location location) {
        if (location.getPlane() == 3) {
            return location.transform(-320, 1856, -1);
        }
        return location.transform(0, 0, 1);
    }

    public static Location getLowerTile(final Location location) {
        if (location.getY() >= 4686 && location.getY() <= 4709 && location.getPlane() == 2) {
            return location.transform(320, -1856, 1);
        }
        return location.transform(0, 0, -1);
    }

    @Override
    public void processMovement(Player player, Location destination) {
        final boolean insideLowerPyramid = getPolygon(0).contains(destination);
        final boolean insideHigherPyramid = getPolygon(1).contains(destination);
        player.getVarManager().sendBit(AgilityPyramid.MOVING_BLOCK_VARBIT, insideLowerPyramid ? destination.getPlane() : insideHigherPyramid ? destination.getPlane() + 2 : 0);
    }
}
