package org.jesse.game.content.skills.magic.spells.teleports.structures;

import org.jesse.game.content.skills.magic.spells.teleports.Teleport;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.AnimationUtil;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;

import static org.jesse.utils.TimeUnit.MILLISECONDS;

/**
 * @author Kris | 22/03/2019 18:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ExplorersRingStructure implements TeleportStructure {
    private static final Animation animation = new Animation(3869);
    private static final Graphics graphics = new Graphics(285, 0, 92);

    @Override
    public Animation getStartAnimation() {
        return animation;
    }

    @Override
    public Graphics getStartGraphics() {
        return graphics;
    }

    @Override
    public void start(Player player, Teleport teleport) {
        final double experience = teleport.getExperience();
        final Animation startAnimation = Utils.getOrDefault(getStartAnimation(), Animation.STOP);
        final Graphics startGraphics = Utils.getOrDefault(getStartGraphics(), Graphics.RESET);
        player.lock();
        final SoundEffect sound = getStartSound();
        if (sound != null) {
            World.sendSoundEffect(player, sound);
        }
        if (experience != 0) {
            player.getSkills().addXp(SkillConstants.MAGIC, experience);
        }
        teleport.onUsage(player);
        player.setAnimation(startAnimation);
        player.setGraphics(startGraphics);
        int durationInTicks = (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(startAnimation)) + 1;
        player.blockIncomingHits(durationInTicks);
        WorldTasksManager.scheduleOrExecute(() -> end(player, teleport), durationInTicks);
    }

    @Override
    public void end(Player player, Teleport teleport) {
        TeleportStructure.super.end(player, teleport);
        if (player.getTemporaryAttributes().remove("cabbage field restricted teleport") != null) {
            final int teleports = player.getVariables().getCabbageFieldTeleports();
            player.sendMessage(Colour.RED.wrap("You have used up " + teleports + "/3 of your Cabbage Field teleports for today."));
        }
    }
}
