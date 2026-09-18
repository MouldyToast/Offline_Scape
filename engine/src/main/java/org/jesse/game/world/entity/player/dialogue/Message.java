package org.jesse.game.world.entity.player.dialogue;

import org.jesse.game.world.entity.player.Player;

public interface Message {

    default void executeAction(final Runnable runnable) {
    	
    }

    void display(Player player);

	default void execute(final Player player) {
		
	}
    
    default String continueMessage(final Player player) {
        return player.isOnMobile() ? "Tap here to continue" : "Click here to continue";
    }

    static void setupChatModal(Player player, int unclampMode) {
        player.getVarManager().sendBit(10670, unclampMode);
        player.getPacketDispatcher().sendClientScript(2379);
    }

    static void resetChatboxBackground(Player player) {
        player.getPacketDispatcher().sendClientScript(2379);
    }

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

    static int chatLineHeight(String text) {
        return switch (countLines(text)) {
            case 2 -> 28;
            case 3 -> 20;
            default -> 16;
        };
    }

    static int mesboxLineHeight(String text) {
        return switch (countLines(text)) {
            case 2 -> 31;
            case 3 -> 24;
            case 4 -> 17;
            default -> 0;
        };
    }

}