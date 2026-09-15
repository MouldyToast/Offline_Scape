package org.jesse.game.content.tog;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.content.tog.juna.JunaEnterDialogue;
import org.jesse.game.content.tog.juna.JunaOutsideOptionDialogue;
import org.jesse.game.model.RuneDate;
import org.jesse.game.util.Colour;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.GameCommands;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;
import org.jesse.plugins.events.ServerLaunchEvent;

public final class TearsOfGuthixServerLaunchSubscriber {

    @Subscribe
    public static void onServerStart(ServerLaunchEvent e) {
        new GameCommands.Command(PlayerPrivilege.DEVELOPER, "resettog", "Reset a player's Tears of Guthix cooldown. Args: name", (p, args) ->
                p.sendInputName("Whose " + "Tears" + " of Guthix restriction to remove?",
                        name -> World.getPlayer(name).ifPresent(targetPlayer ->
                                targetPlayer.getAttributes().remove(JunaEnterDialogue.LAST_ATTEMPT_DATE_ATTR))));

        RuneDate.addOnCheckDate(player -> {
            if (JunaEnterDialogue.hasTimePassed(player) && player.getBooleanAttribute(JunaOutsideOptionDialogue.TOG_REMINDER_ENABLED_ATTR)) {
                player.sendMessage(Colour.RED.wrap("You are eligible to drink from the Tears of Guthix."));
            }
        });
    }

}
