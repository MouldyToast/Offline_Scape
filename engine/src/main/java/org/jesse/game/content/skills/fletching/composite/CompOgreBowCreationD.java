package org.jesse.game.content.skills.fletching.composite;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Christopher
 * @since 3/20/2020
 */
public class CompOgreBowCreationD extends SkillDialogue {
    public CompOgreBowCreationD(final Player player, final Item item) {
        super(player, item);
    }

    @Override
    public void run(final int slotId, final int amount) {
        player.getActionManager().setAction(new CompOgreBowCreationAction(amount, true));
    }
}
