package org.jesse.plugins.item;

import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.AnimationUtil;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Emote;

/**
 * @author Kris | 15/05/2019 22:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpinningPlate extends ItemPlugin {
    @Override
    public void handle() {
        bind("Spin", (player, item, slotId) -> {
            final long emoteDelay = player.getNumericTemporaryAttribute("emote_delay").longValue();
            if (emoteDelay > Utils.currentTimeMillis()) {
                return;
            }
            final Animation animation = new Animation(1902 + Utils.random(5));
            final int duration = AnimationUtil.getCeiledDuration(animation);
            final Emote nextEmote = animation.getId() <= 1903 ? Emote.CHEER : Emote.CRY;
            final int nextDuration = AnimationUtil.getCeiledDuration(nextEmote.getAnimation());
            player.addTemporaryAttribute("emote_delay", Utils.currentTimeMillis() + duration + nextDuration);
            player.setAnimation(animation);
            final int location = player.getLocation().getPositionHash();
            WorldTasksManager.schedule(() -> {
                if (player.getLocation().getPositionHash() != location) {
                    return;
                }
                player.setAnimation(nextEmote.getAnimation());
            }, duration / 600);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {4613};
    }
}
