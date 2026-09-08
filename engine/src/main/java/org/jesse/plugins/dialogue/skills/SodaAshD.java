package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.crafting.actions.SodaAshCrafting;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class SodaAshD extends SkillDialogue {

    private final boolean range;

    public SodaAshD(final Player player, final boolean range) {
        super(player, SodaAshCrafting.SODA_ASH);
        this.range = range;
    }

    @Override
    public void run(int slotId, int amount) {
        player.getActionManager().setAction(new SodaAshCrafting(amount, range));
    }
}
