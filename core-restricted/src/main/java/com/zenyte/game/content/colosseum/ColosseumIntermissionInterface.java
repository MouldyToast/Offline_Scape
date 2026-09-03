package com.zenyte.game.content.colosseum;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RegionArea;

/**
 * Interface handler for the Colosseum intermission screen (interface 865).
 * <p>
 * Modifier buttons (components 15/16/17) set varbit 9788 server-side to match
 * the CS2 script's client-side toggle. The Continue button (component 41)
 * triggers wave start via the instance.
 * <p>
 * Component IDs verified from osrs-dumps rev-228 interface_865.if3.
 */
@SuppressWarnings("unused")
public class ColosseumIntermissionInterface extends Interface {

    @Override
    protected void attach() {
        put(15, "mod-1");          // mod_button_1, op1=Set
        put(16, "mod-2");          // mod_button_2, op1=Set
        put(17, "mod-3");          // mod_button_3, op1=Set
        put(22, "forfeit-confirm"); // confirm_confirm, op1=Confirm (forfeit overlay)
        put(41, "confirm");        // right_button, op1=Continue
    }

    @Override
    public void build() {
        bind("mod-1", player -> selectModifier(player, 1));
        bind("mod-2", player -> selectModifier(player, 2));
        bind("mod-3", player -> selectModifier(player, 3));

        bind("forfeit-confirm", player -> {
            RegionArea area = player.getArea();
            if (!(area instanceof ColosseumInstance instance)) {
                return;
            }
            instance.forfeitRun();
        });

        bind("confirm", player -> {
            RegionArea area = player.getArea();
            if (!(area instanceof ColosseumInstance instance)) {
                return;
            }
            int selected = player.getVarManager().getBitValue(9788);
            if (selected < 1 || selected > 3) {
                return; // No modifier selected yet
            }
            instance.confirmModifierAndStartWave(selected);
        });
    }

    private void selectModifier(Player player, int choice) {
        // Set varbit 9788 server-side (CS2 4938 already set it client-side for visual toggle)
        player.getVarManager().sendBit(9788, choice);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.COLOSSEUM_INTERMISSION;
    }
}