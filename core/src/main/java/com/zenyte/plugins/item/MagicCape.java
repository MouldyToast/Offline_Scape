package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.item.capes.NewMaxCapes;

import java.util.Objects;

/**
 * @author Kris | 16/03/2019 02:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Spellbook", (player, item, slotId) -> NewMaxCapes.sendSpellbookDialogue(player));

//        bind("Spellbook","Standard", (player, item, slotId) -> attemptToSwapPlayersSpellbook(player, "Standard"));
//        bind("Spellbook","Ancient", (player, item, slotId) -> attemptToSwapPlayersSpellbook(player, "Ancient"));
//        bind("Spellbook","Lunar", (player, item, slotId) -> attemptToSwapPlayersSpellbook(player, "Lunar"));
//        bind("Spellbook","Arceuus", (player, item, slotId) -> attemptToSwapPlayersSpellbook(player, "Arceuus"));
    }

    private void attemptToSwapPlayersSpellbook(Player player, String spellbook) {
        final var count = player.getVariables().getSpellbookSwaps();
        if (count >= 5) {
            player.getDialogueManager().plain("You may only switch spellbooks five times per day. Try again tomorrow.");
            return;
        }
        final var currentSpellbook = player.getCombatDefinitions().getSpellbook();
        final var newSpellbook = Spellbook.valueOf(spellbook.toUpperCase());
        if (Objects.equals(currentSpellbook, newSpellbook)) {
            player.sendMessage("You're already on this spellbook.");
            return;
        }
        player.getCombatDefinitions().setSpellbook(newSpellbook, true);
        player.getVariables().setSpellbookSwaps(count + 1);
        player.sendMessage(Colour.RED.wrap("You have changed your spellbook " + count + "/5 times today."));
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.MAGIC.getSkillCapes();
    }
}
