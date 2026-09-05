package com.zenyte.game.content.colosseum.objects;

import com.zenyte.game.content.colosseum.ColosseumInstance;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

@SuppressWarnings("unused")
public class ColosseumInstanceEntranceObject implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		boolean talkedToMinimus = player.getVarManager().getBitValue(9807) == 1;
		if (!talkedToMinimus) {
			player.getDialogueManager().start(new Dialogue(player, NpcId.MINIMUS) {
				@Override
				public void buildDialogue() {
					npc("Oi! Where d'you think you're going? Come and speak to me before you go wondering through there. You'll get yourself killed!");
				}
			});
			return;
		}

		ColosseumInstance.createInstance(player);
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.ENTRANCE_50751};
	}

}
