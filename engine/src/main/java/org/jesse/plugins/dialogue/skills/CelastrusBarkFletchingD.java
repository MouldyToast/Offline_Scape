package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.fletching.actions.CelastrusBarkFletching;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 28-4-2019 | 16:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CelastrusBarkFletchingD extends SkillDialogue {

    public CelastrusBarkFletchingD(final Player player) {
        super(player, "How many do you wish to make?", CelastrusBarkFletching.battlestaff);
    }

    @Override
    public void run(final int slotId, final int amount) {
        player.getActionManager().setAction(new CelastrusBarkFletching(amount));
    }

}