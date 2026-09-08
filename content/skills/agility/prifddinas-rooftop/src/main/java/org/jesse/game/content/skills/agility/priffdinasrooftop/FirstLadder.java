package org.jesse.game.content.skills.agility.priffdinasrooftop;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author R-Y-M-R
 * @date 2/10/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class FirstLadder extends AgilityCourseObstacle {
    private static final int CLIMB = 828;
    private static final Location START_LOC = new Location(3253, 6109, 0);
    private static final Location END_LOC = new Location(3255, 6109, 2);

    public FirstLadder() {
        super(PriffdinasRooftopCourse.class, 1);
    }

    private void determineRandomPortal(Player player) {
        int random = Utils.random(1, 6);
        player.getTemporaryAttributes().put("prif_portal", random);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        determineRandomPortal(player);
        if (player.getNumericTemporaryAttribute("prif_portal").intValue() == 1) {
            player.getVarManager().sendBit(9298, 1);
        }
        player.useStairs(CLIMB, new Location(END_LOC), 1, 0);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{36221};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 11.5;
    }
}