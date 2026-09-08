package org.jesse.game.content.skills.agility.pollnivneach;

import org.jesse.game.content.skills.agility.AbstractAgilityCourse;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;

/**
 * @author Christopher
 * @since 3/30/2020
 */
public final class PollnivneachRooftopCourse extends AbstractAgilityCourse {

    public static final Animation runningStartAnim = new Animation(1995);
    public static final Animation landAnim = new Animation(2588);
    public static final Location[] MARK_LOCATIONS = {
            new Location(3346, 2968, 1), new Location(3354, 2974, 1),
            new Location(3361, 2993, 2)};

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}
