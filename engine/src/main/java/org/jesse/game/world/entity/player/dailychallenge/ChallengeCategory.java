package org.jesse.game.world.entity.player.dailychallenge;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Skills;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.utils.TextUtils;

import static org.jesse.game.world.entity.player.dailychallenge.ChallengeDifficulty.*;

public enum ChallengeCategory {
    SKILLING(player -> {
        int skill = 0;
        while (Skills.isCombatSkill(skill) || skill == SkillConstants.CONSTRUCTION || skill >= SkillConstants.SAILING) {
            skill = Utils.random(SkillConstants.CONSTRUCTION);
        }
        final int level = player.getSkills().getLevelForXp(skill);
        final ChallengeDifficulty difficulty = level >= 75 ? ELITE : level <= 74 && level >= 45 ? HARD : level <= 44 && level >= 20 ? MEDIUM : EASY;
        return new ChallengeDetails(difficulty, skill);
    }), COMBAT(player -> {
        final int combatLevel = player.getCombatLevel();
        final ChallengeDifficulty difficulty = combatLevel >= 3 && combatLevel <= 40 ? EASY : combatLevel >= 41 && combatLevel <= 75 ? MEDIUM : combatLevel >= 76 && combatLevel <= 100 ? HARD : ELITE;
        return new ChallengeDetails(difficulty, combatLevel);
    }), MINIGAME(player -> new ChallengeDetails(EASY));
    private final DetailsDetermination determination;
    public static final ChallengeCategory[] all = values();

    public ChallengeDetails getDetails(final Player player) {
        return determination.getDetails(player);
    }


    private interface DetailsDetermination {
        ChallengeDetails getDetails(final Player player);
    }

    @Override
    public String toString() {
        return TextUtils.formatName(name().toLowerCase());
    }

    ChallengeCategory(DetailsDetermination determination) {
        this.determination = determination;
    }
}
