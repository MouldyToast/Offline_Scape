package com.zenyte.game.world.entity.player.cutscene.impl;

import com.zenyte.game.GameConstants;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.HintArrowPosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.renewednpc.ZenyteGuide;

/**
 * @author Kris | 26/01/2019 01:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EdgevilleCutscene extends Cutscene {

    @Override
    public void build() {
        int time = 0;
        addActions(time+=6, () -> player.lock(), () -> player.setViewDistance(30), () -> player.getAppearance().setInvisible(true),
            () -> player.setLocation(ZenyteGuide.SPAWN_LOCATION), () -> chat(player, "Very well! Let's begin then.."));

        addActions(time+=12, () -> action(player, "Here you can find the universal hub where you can find everything from shopping with Frank, and a Universal Chest, to Reagent Mixer, and more..", 3089, 3510),
            new CameraPositionAction(player, new Location(3090, 3505), 750, 5, 10),
            new CameraLookAction(player, new Location(3089, 3510), 0, 5, 10));

        addActions(time+=12, () -> action(player, "Over here we have a small mine for ores, a furnace where you can smelt your ores into bars, as well, we have anvils to smith your bars into equipment.", 3106, 3497),
            new CameraPositionAction(player, new Location(3111, 3496), 1000, 5, 10),
            new CameraLookAction(player, new Location(3106, 3497, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "This the Bounty Hunter hub, here you can trade in your emblems, and spend your BH Points.", 3105, 3514),
            new CameraPositionAction(player, new Location(3105, 3507), 1000, 5, 10),
            new CameraLookAction(player, new Location(3105, 3513, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "In here you will find the <col=00080>Iron Man Tutor</col> for all your ironman related inquiries like reverting your ironman mode and claiming armour.", 3086, 3497),
            new CameraPositionAction(player, new Location(3087, 3490), 500, 5, 10),
            new CameraLookAction(player, new Location(3086, 3497, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "And over here you can find the Slayer hub. With all of the Slayer Masters on " + GameConstants.SERVER_NAME + ".", 3076, 3491),
            new CameraPositionAction(player, new Location(3079, 3489), 1000, 5, 10),
            new CameraLookAction(player, new Location(3076, 3491, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "Here you can find farming and thieving needs to get you started in " + GameConstants.SERVER_NAME + ".", 3078, 3475),
            new CameraPositionAction(player, new Location(3086, 3475), 1000, 5, 10),
            new CameraLookAction(player, new Location(3078, 3475, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "Visit our gambling area but remember to gamble responsibly!", 3090, 3459),
            new CameraPositionAction(player, new Location(3090, 3470), 2000, 5, 10),
            new CameraLookAction(player, new Location(3090, 3459, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "Here is the Church, you will return here when you die. You can also find Deaths Domain if you want to get your items back.", 3104, 3474),
            new CameraPositionAction(player, new Location(3097, 3472), 800, 5, 10),
            new CameraLookAction(player, new Location(3104, 3474, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "Here is our AFK zone, gain xp and AFK Points to spend while you’re away.", 3133, 3484),
            new CameraPositionAction(player, new Location(3119, 3484), 2000, 5, 10),
            new CameraLookAction(player, new Location(3133, 3484, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "Here you can find the Tournament Portal, Shop, and our Combat Dummies.", 3110, 3487),
            new CameraPositionAction(player, new Location(3104, 3480), 800, 5, 10),
            new CameraLookAction(player, new Location(3110, 3487, 0), 0, 5, 10));

        addActions(time+=8, () -> action(player, "Here you can find Challenges, Daily, Group, or Solo challenges " + GameConstants.SERVER_NAME + ".", 3095, 3494),
            new CameraPositionAction(player, new Location(3090, 3490), 750, 5, 10),
            new CameraLookAction(player, new Location(3095, 3494, 0), 0, 5, 10));

        addActions(time+=8, () -> action(player, "This is the well of sacrifice, where you can exchange items and remnant vouchers for some amazing permanent account perks.", 3097, 3490),
            new CameraPositionAction(player, new Location(3090, 3490), 750, 5, 10),
            new CameraLookAction(player, new Location(3097, 3490, 0), 0, 5, 10));

        addActions(time+=8, () -> action(player, "Here you can find the HiScore of " + GameConstants.SERVER_NAME + ".", 3095, 3486),
            new CameraPositionAction(player, new Location(3090, 3490), 750, 5, 10),
            new CameraLookAction(player, new Location(3095, 3486, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "And finally over here we have the main hub of the home area where you can find <col=00080>Bankers & Grand Exchange Clerks</col>.", 3089, 3490, HintArrowPosition.EAST),
            new CameraPositionAction(player, new Location(3090, 3485), 1000, 5, 10),//
            new CameraLookAction(player, new Location(3089, 3493, 0), 0, 5, 10), () -> player.setLocation(ZenyteGuide.HOME_ZENYTE_GUIDE));

        addActions(time+=12, () -> player.setViewDistance(15), () -> player.getPacketDispatcher().resetHintArrow(), () -> ZenyteGuide.finishTutorial(player),
            new CameraResetAction(player));
    }

    private void action(final Player player, final String message, final int x, final int y) {
        action(player, message, x, y, HintArrowPosition.CENTER);
    }

    private void action(final Player player, final String message, final int x, final int y, final HintArrowPosition position) {
        player.getPacketDispatcher().sendHintArrow(new HintArrow(x, y, (byte) 50, position));
        chat(player, message);
    }

    private void chat(final Player player, final String message) {
        player.getDialogueManager().start(new NPCChat(player, ZenyteGuide.NPC_ID, message, false));
    }
}
