package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.slayer.SlayerKeys;
import com.near_reality.game.content.slayer.Assignment;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 25. aug 2018 : 22:22:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class EnchantedGem extends ItemPlugin {
	@Override
	public void handle() {
		bind("Check", (player, item, slotId) -> SlayerKeys.slayer(player).sendTaskInformation());
		bind("Log", (player, item, slotId) -> player.getNotificationSettings().sendKillLog(NotificationSettings.SLAYER_NPC_NAMES, true));
		bind("Partner", (player, item, slotId) -> {
			player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 68);
			SlayerKeys.slayer(player).refreshPartnerInterface();
		});
		bind("Activate", (player, item, slotId) -> player.getDialogueManager().start(new ActivateDialogue(player)));
	}

	@Override
	public int[] getItems() {
		return new int[] {4155, 21270};
	}


	public static final class ActivateDialogue extends Dialogue {
		public ActivateDialogue(final Player player) {
			super(player, SlayerKeys.slayer(player).getMaster().getNpcId());
		}

		@Override
		public void buildDialogue() {
			npc("Hello there " + player.getName() + ", what can I help you with?");
			options();
			final Assignment assignment = SlayerKeys.slayer(player).getAssignment();
			player(5, "How am I doing so far?");
			if (assignment == null) {
				npc("You're not assigned to kill anything right now. Come find me to get a new assignment.");
			} else {
				npc("You're currently assigned to kill " + assignment.getTask().getTaskName() + "; only " + assignment.getAmount() + " more to go. Your reward point tally is " + player.getNumericAttribute("slayer_points").intValue() + ".");
			}
			options();
			player(100, "Who are you?");
			npc("My name's " + StringFormatUtil.formatString(SlayerKeys.slayer(player).getMaster().toString()) + ", I'm your" +
                    " Slayer Master.");
			options();
			player(200, "Where are you?");
			npc("You'll find me " + SlayerKeys.slayer(player).getMaster().getLocation() + ".<br>I'l be here when you need a " +
                    "new task.");
			options();
			player(300, "Got any tips for me?");
			npc(assignment == null ? "You'll need to get an assignment first." : assignment.getTask().getTip());
			player("Great, thanks!");
			options();
			player(400, "That's all, thanks.");
		}

		private void options() {
			options(TITLE, new DialogueOption("How am I doing so far?", key(5)), new DialogueOption("Who are you?", key(100)), new DialogueOption("Where are you?", key(200)), new DialogueOption("Got any tips for me?", key(300)), new DialogueOption("Nothing really.", key(400)));
		}
	}
}
