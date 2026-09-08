package org.jesse.plugins.dialogue;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceBarFinishD extends Dialogue {
    
    private final int amount;
    private final String payload;
    
    public BlastFurnaceBarFinishD(final Player player, final int amount, final String payload) {
        super(player);
        
        this.amount = amount;
        this.payload = payload;
        player.getBlastFurnace().setProcessBarsFromDialogue(true);
    }
    
    @Override
    public void buildDialogue() {
        plain("You take " + amount + " " + payload + " from the dispenser.");
    }
}
