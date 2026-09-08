package org.jesse.game.content.creaturecreation;

import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Chris
 * @since August 22 2020
 */
public class SymbolOfLifeObjectAction implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final SymbolOfLife symbol = SymbolOfLife.of(new ImmutableLocation(object.getPosition()));
        switch (option.toLowerCase()) {
        case "activate": 
            if (SymbolOfLifeActivateDialogue.playerCreature.containsKey(player.getUsername())) {
                return;
            }
            player.getDialogueManager().start(new SymbolOfLifeActivateDialogue(player, symbol));
            break;
        case "inspect": 
            player.getDialogueManager().start(new SymbolOfLifeInspectDialogue(player, symbol));
            break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.SYMBOL_OF_LIFE};
    }
}
