package org.jesse.game.world.entity.player.dialogue;

import org.jesse.game.world.entity.player.Player;

/**
 * Created by Arham 4 on 2/15/2016.
 */
public interface Message {

    default void executeAction(final Runnable runnable) {
    	
    }

    void display(Player player);

	default void execute(final Player player) {
		
	}
    
    default String continueMessage(final Player player) {
        return player.isOnMobile() ? "Tap here to continue" : "Click here to continue";
    }

    /**
     * Prepares the chatbox for a dialogue with a chathead (NPC/player chat).
     * Sets chatmodal_unclamp to fixed sizing and resets background.
     */
    static void setupChatModal(Player player, int unclampMode) {
        player.getVarManager().sendBit(10670, unclampMode);
        player.getPacketDispatcher().sendClientScript(2379);
    }

    /**
     * Resets the chatbox background before a mesbox/plain dialogue.
     * Does NOT set the unclamp varbit — mesbox uses whatever mode is current.
     */
    static void resetChatboxBackground(Player player) {
        player.getPacketDispatcher().sendClientScript(2379);
    }

    /**
     * Counts lines in dialogue text by counting {@code <br>} separators.
     */
    private static int countLines(String text) {
        if (text == null || text.isEmpty()) return 1;
        int count = 1;
        int idx = 0;
        while ((idx = text.indexOf("<br>", idx)) != -1) {
            count++;
            idx += 4;
        }
        return count;
    }

    /**
     * Returns the correct lineHeight for chat dialogues (NPC/Player chathead)
     * based on the number of text lines. Values from OpenRune / OSRS client.
     */
    static int chatLineHeight(String text) {
        return switch (countLines(text)) {
            case 2 -> 28;
            case 3 -> 20;
            default -> 16;
        };
    }

    /**
     * Returns the correct lineHeight for mesbox/plain dialogues (no chathead)
     * based on the number of text lines. Values from OpenRune / OSRS client.
     */
    static int mesboxLineHeight(String text) {
        return switch (countLines(text)) {
            case 2 -> 31;
            case 3 -> 24;
            case 4 -> 17;
            default -> 0;
        };
    }

}