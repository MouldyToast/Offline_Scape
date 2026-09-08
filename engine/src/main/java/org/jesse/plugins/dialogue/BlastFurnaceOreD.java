package org.jesse.plugins.dialogue;

import org.jesse.game.content.minigame.blastfurnace.BlastFurnaceOre;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceOreD extends Dialogue {
    private final List<String> page1 = new ArrayList<>();
    private final List<String> page2 = new ArrayList<>();

    public BlastFurnaceOreD(final Player player) {
        super(player);
        int counter = 0;
        for (final BlastFurnaceOre ore : BlastFurnaceOre.VALUES) {
            final String payload = TextUtils.capitalizeFirstCharacter(ore.toString().toLowerCase().replace("_", " ")) + ": " + player.getBlastFurnace().getOre(ore) + "<br>";
            (counter < 5 ? page1 : page2).add(payload);
            counter++;
        }
    }

    @Override
    public void buildDialogue() {
        plain(page1.toString().replace(", ", "").replace("]", "").replace("[", ""), true);
        plain(page2.toString().replace(", ", "").replace("]", "").replace("[", ""), true);
    }
}
