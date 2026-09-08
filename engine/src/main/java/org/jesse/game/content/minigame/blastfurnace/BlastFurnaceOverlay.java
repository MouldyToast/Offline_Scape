package org.jesse.game.content.minigame.blastfurnace;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceOverlay extends Interface {
    
    public static final int COFFER_VARBIT = 5357;

    @Override
    protected void attach() {
    
    }
    
    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getVarManager().sendBit(COFFER_VARBIT, player.getBlastFurnace().getCoffer());
    }
    
    @Override
    protected void build() {
    
    }
    
    @Override
    public GameInterface getInterface() {
        return GameInterface.BLAST_FURNACE_COFFER;
    }
}
