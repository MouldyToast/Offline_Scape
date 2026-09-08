package com.zenyte.game.world.entity.player.dailychallenge;

import com.zenyte.game.world.entity.player.dailychallenge.challenge.CombatChallenge;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.DailyChallenge;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @author Tommeh | 03/05/2019 | 22:55
 * @author Jire
 */
public final class ChallengeWrapper {

    public static final Map<String, DailyChallenge> challenges = new Object2ObjectOpenHashMap<>();

    public static void init() {
        for (final SkillingChallenge challenge : SkillingChallenge.all) {
            challenges.put(challenge.name(), challenge);
        }
        for (final CombatChallenge challenge : CombatChallenge.all) {
            challenges.put(challenge.name(), challenge);
        }
    }

    @Nullable
    public static DailyChallenge get(final String name) {
        return challenges.get(name);
    }

}
