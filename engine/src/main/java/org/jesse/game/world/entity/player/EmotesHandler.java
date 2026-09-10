package org.jesse.game.world.entity.player;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 22. veebr 2018 : 22:43.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class EmotesHandler {

    EmotesHandler(final Player player) {
        this.player = player;
    }

    private final transient Player player;

    /**
     * Unlocks a specific requested emote.
     *
     * @param emote Emote to unlock.
     */
    public void unlock(@NotNull final Emote emote) {
        if (emote.getConfig() == -1) {
            return;
        }
        player.getVarManager().sendBit(emote.getConfig(), emote == Emote.GOBLIN_BOW || emote == Emote.GOBLIN_SALUTE ? 7 : 1);
    }

    /**
     * Whether the requested emote is unlocked or not.
     *
     * @param emote Emote to check
     * @return unlocked or not.
     */
    public boolean isUnlocked(@NotNull final Emote emote) {
        return emote.getConfig() == -1 || player.getVarManager().getBitValue(emote.getConfig()) > 0;
    }

}
