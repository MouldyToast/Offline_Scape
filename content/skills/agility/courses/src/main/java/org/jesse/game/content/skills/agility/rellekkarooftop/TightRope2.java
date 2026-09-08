package org.jesse.game.content.skills.agility.rellekkarooftop;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.RenderAnimation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 09/06/2019 | 15:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class TightRope2 extends AgilityCourseObstacle {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);

    private static final Animation ANIM1 = new Animation(1995, 15);
    private static final Animation ANIM2 = new Animation(1603);

    private static final Location LOCATION1 = new Location(2622, 3668, 3);
    private static final SoundEffect EFFECT1 = new SoundEffect(2495, 6, 0);

    public TightRope2() {
        super(RellekkaRooftopCourse.class, 6);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 80;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.TIGHTROPE_14992};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 8;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.addWalkSteps(2647, 3663, -1, false);
        player.addWalkSteps(2654, 3670, -1, false);
        MarkOfGrace.spawn(player, RellekkaRooftopCourse.MARK_LOCATIONS, 40, 80);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 105;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }
}
