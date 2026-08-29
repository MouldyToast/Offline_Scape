package com.zenyte.game.world.entity.masks;

import org.jetbrains.annotations.Nullable;

/**
 * @author Kris | 6. nov 2017 : 14:28.18
 * @author Jire
 */
public final class ChatMessage {

    private int colors;
    private int effects;
    private String chatText;
    private boolean autotyper;

    @Nullable
    private byte[] patterns;

    public ChatMessage() {
        this("", 0, 0, false, null);
    }

    public ChatMessage(
            final String chatText,
            final int colors,
            final int effects,
            final boolean autotyper,
            @Nullable final byte[] patterns) {
        this.effects = effects;
        this.colors = colors;
        this.chatText = chatText;
        this.autotyper = autotyper;
        this.patterns = patterns;
    }

    public ChatMessage set(final @Nullable String text,
                           final int colors,
                           final int effects,
                           final boolean autotyper,
                           @Nullable byte[] patterns) {
        this.chatText = text;
        this.colors = colors;
        this.effects = effects;
        this.autotyper = autotyper;
        this.patterns = patterns;
        return this;
    }

    public int getEffects() {
        return effects;
    }

    public String getChatText() {
        return chatText;
    }

    public int getColors() {
        return colors;
    }

    @Nullable
    public byte[] getPatterns() {
        return patterns;
    }

    public boolean isAutotyper() {
        return autotyper;
    }

}
