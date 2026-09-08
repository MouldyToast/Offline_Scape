package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Analytics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.dailychallenge.ChallengeProgress;
import org.jesse.game.world.entity.player.dailychallenge.DailyChallengeManager;
import org.jesse.game.world.entity.player.dailychallenge.challenge.DailyChallenge;
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import org.jesse.game.world.entity.player.dailychallenge.reward.ChallengeReward;
import org.jesse.game.world.entity.player.dailychallenge.reward.RewardType;
import org.jesse.game.world.entity.player.dailychallenge.reward.impl.ExperienceReward;
import org.jesse.game.world.entity.player.dailychallenge.reward.impl.ItemReward;
import org.jesse.utils.TextUtils;

import java.util.Map;
import java.util.Optional;

/**
 * @author Tommeh | 06/05/2019 | 16:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DailyChallengesOverviewInterface extends Interface {
    @Override
    protected void attach() {
        put(10, "0");
        put(11, "1");
        put(12, "2");
        put(13, "3");
        put(15, "4");
    }

    @Override
    public void open(Player player) {
        Analytics.flagInteraction(player, Analytics.InteractionType.DAILY_CHALLENGES);
        final DailyChallengeManager manager = player.getDailyChallengeManager();
        final Map<DailyChallenge, ChallengeProgress> challenges = manager.getChallengeProgression();
        player.getInterfaceHandler().sendInterface(this);
        if (challenges.isEmpty()) {
            player.getPacketDispatcher().sendClientScript(10501, "");
            return;
        }

        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (final DailyChallenge challenge : challenges.keySet()) {
            transmitDailyChallenge(player, challenge, index, sb);
            index++;
        }

        player.getPacketDispatcher().sendClientScript(10501, sb.toString());
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        player.addTemporaryAttribute("daily_challenge_claimable", 0);
    }

    private void transmitDailyChallenge(final Player player, final DailyChallenge challenge, final int index, final StringBuilder sb) {
        final DailyChallengeManager manager = player.getDailyChallengeManager();
        final ChallengeProgress progression = manager.getProgress(challenge);
        if (progression == null) {
            return;
        }
        final int progress = progression.getProgress();
        final int extra = challenge instanceof SkillingChallenge ? ((SkillingChallenge) challenge).getSkill() : -1;
        final StringBuilder builder = new StringBuilder();
        final ChallengeReward[] rewards = challenge.getRewards();
        for (final ChallengeReward reward : rewards) {
            if (reward.getType().equals(RewardType.ITEM)) {
                final ItemReward itemReward = (ItemReward) reward;
                builder.append(itemReward.getItem().getId()).append("x");
                builder.append(itemReward.getItem().getAmount()).append(" ");
            } else {
                final ExperienceReward experienceReward = (ExperienceReward) reward;
                builder.append(TextUtils.formatCurrency(experienceReward.getExperience(player) * player.getExperienceRate(experienceReward.getSkill()))).append(" ");
                builder.append(SkillConstants.SKILLS[experienceReward.getSkill()]).append(" XP");
            }
        }

        sb.append(challenge.getName());
        sb.append("|");
        sb.append(SkillConstants.SKILLS_ICONS[extra]);
        sb.append("|");
        sb.append(20);
        sb.append("|");
        sb.append(20);
        sb.append("|");
        sb.append(progress);
        sb.append("|");
        sb.append(challenge.getLength());
        sb.append("|");
        if (challenge instanceof SkillingChallenge) {
            sb.append("Skilling");
        } else {
            sb.append("Combat");
        }
        sb.append("|");
        sb.append(challenge.getDifficulty());
        sb.append("|");
        sb.append(builder);
        sb.append("|");
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent(Integer.toString(index)), 0, 10, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("0", player -> claim(player, 0));
        bind("1", player -> claim(player, 1));
        bind("2", player -> claim(player, 2));
        bind("3", player -> claim(player, 3));
        bind("4", player -> claim(player, 4));
    }

    private void claim(Player player, int index) {
        final DailyChallengeManager manager = player.getDailyChallengeManager();
        final DailyChallenge challenge = manager.getChallenge(index);
        if (manager.claim(challenge)) {
            PlayerAttributesKt.setDailyChallengePoints(player, challenge.getDifficulty().ordinal() + 1);
            open(player);
        }
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.DAILY_CHALLENGES_OVERVIEW;
    }
}
