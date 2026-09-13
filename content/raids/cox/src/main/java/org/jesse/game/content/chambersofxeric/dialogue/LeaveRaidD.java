package org.jesse.game.content.chambersofxeric.dialogue;

import org.jesse.game.content.chambersofxeric.Raid;
import org.jesse.game.content.chambersofxeric.greatolm.OlmRoom;
import org.jesse.game.content.chambersofxeric.rewards.RaidRewards;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.Map;

/**
 * @author Kris | 16. nov 2017 : 3:28.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LeaveRaidD extends Dialogue {
	public LeaveRaidD(Player player, final Raid raid) {
		super(player);
		this.raid = raid;
	}

	private final Raid raid;

	@Override
	public void buildDialogue() {
		final MutableBoolean bool = new MutableBoolean();
		final MutableBoolean fullChest = new MutableBoolean();
		raid.ifInRoom(player.getLocation(), OlmRoom.class, room -> {
			bool.setTrue();
			final RaidRewards rewards = raid.getRewards();
			if (rewards != null) {
				final Map<Player, Container> map = rewards.getRewardMap();
				if (map != null) {
					final Container container = map.get(player);
					if (container != null) {
						if (!container.isEmpty()) {
							fullChest.setTrue();
						}
					}
				}
			}
		});
		final String header = fullChest.isTrue() ? "You have stuff in the chest.<br>Are you sure you want to abandon the raid?" : "You will not be able to rejoin this raid again.";
		final boolean canReload = raid.getStage() == 0 && raid.getParty().getPlayer().equalsIgnoreCase(player.getUsername());
		if (canReload) {
			options(header,
					new DialogueOption("Leave the raid.", () -> raid.leaveRaid(player, false, bool.isTrue())),
					new DialogueOption("Reload the layout.", () -> player.getDialogueManager().start(new ReloadRaidD(player, raid))),
					new DialogueOption("Stay."));
		} else {
			options(header, new DialogueOption("Leave the raid.", () -> raid.leaveRaid(player, false, bool.isTrue())), new DialogueOption("Stay."));
		}
	}
}
