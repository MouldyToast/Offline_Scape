package com.zenyte.game.content.skills.smithing.zombie_item;

import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-09
 */
public abstract class ZombieItem implements ItemOnObjectAction {

    protected boolean hasPreReqs(Player player) {
        if (!player.getInventory().containsItem(Smithing.HAMMER)) {
            player.getDialogueManager().start(new PlainChat(player, "You need to have a hammer to do this."));
            return false;
        }
        if ((player.getSkills().getLevel(SkillConstants.SMITHING)) < 70) {
            player.getDialogueManager().start(new PlainChat(player, "You need to have a Smithing level of at least 70 to do this."));
            return false;
        }
        return true;
    }
}
