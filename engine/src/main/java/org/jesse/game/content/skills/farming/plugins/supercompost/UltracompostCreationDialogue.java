package org.jesse.game.content.skills.farming.plugins.supercompost;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Christopher
 * @since 02/24/2020
 */
public class UltracompostCreationDialogue extends SkillDialogue {
    public UltracompostCreationDialogue(Player player, Item... items) {
        super(player, items);
    }

    @Override
    public void run(int slotId, int amount) {
        player.getActionManager().setAction(new UltracompostCreationAction(amount));
    }
}
