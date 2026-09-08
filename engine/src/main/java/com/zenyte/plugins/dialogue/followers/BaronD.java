package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.Random;

/**
 * @author William Fuhrman | 02-19-2025 | 12:59
 */
public class BaronD extends Dialogue {

    public BaronD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        if (new Random().nextBoolean()) {
            player("You're so cute!");
            npc("I will devour everything you know and love!");
            player("Well then.");
        }
        else {
            player("You okay, Baron?");
            npc("I must grow! I must eat!");
            player("Keep those little teeth away from me please. I'm keeping an eye on you.");
            npc("And I'm keeping all my eyes on you.");
            player("*gulp*");
        }
    }
}
