package org.jesse.game.content.creaturecreation;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 22 2020
 */
public class SymbolOfLifeInspectDialogue extends Dialogue {
    private final SymbolOfLife symbolOfLife;

    public SymbolOfLifeInspectDialogue(@NotNull final Player player, @NotNull final SymbolOfLife symbolOfLife) {
        super(player);
        this.symbolOfLife = symbolOfLife;
    }

    @Override
    public void buildDialogue() {
        plain("You see some text scrolled above the altar on a symbol...");
        doubleItem(symbolOfLife.getFirstMaterial(), symbolOfLife.getSecondMaterial(), symbolOfLife.getInspectMessage());
    }
}
