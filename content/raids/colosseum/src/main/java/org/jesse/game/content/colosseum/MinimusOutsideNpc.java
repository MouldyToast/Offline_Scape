package org.jesse.game.content.colosseum;

import org.jesse.game.content.follower.impl.BossPet;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.VarManager;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class MinimusOutsideNpc extends NPCPlugin implements ItemOnNPCAction {

	static {
		VarManager.appendPersistentVarbit(9807);
	}

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> {
			player.getDialogueManager().start(new Dialogue(player, npc) {
				@Override
				public void buildDialogue() {
					boolean talkedToMinimus = player.getVarManager().getBitValue(9807) == 1;
					if (!talkedToMinimus) {
						player("Hello there.");
						npc("Welcome, Rookie.");
						player("What is this place?");
						npc("This place...? Why, This is the great Fortis Colosseum!");
						npc("Adventurers come from near and far to test their might within these walls.");
						player("That sounds exciting!");
						npc("Indeed it is. If you are victorious in the Colosseum, the whole of Varlamore shall hear of your name.");
						player("I imagine its quite the challenge then?");
						npc("It'll put your skills to the test alright.");
						player("How do I participate?");
						npc("Just head through that tunnel behind me and we'll get the show started! Make sure you've got everything you need. Once you're in you can't leave until the end of a wave!");
						npc("Come and ask if you have any other questions.").executeAction(() -> {
							setKey(50);
							player.getVarManager().sendBit(9807, 1);
						});
					} else {
						player("Hello there.");
						npc("Welcome, Rookie.").executeAction(() -> setKey(50));
					}

					//We don't have the content for the options below yet, so they're commented out.
					options(50,
							//new DialogueOption("Can you tell me about modifiers?"),
							//new DialogueOption("How do I get rewarded?"),
							//new DialogueOption("What is Glory?"),
							new DialogueOption("What do you do around here?", () -> setKey(100)),
							new DialogueOption("Goodbye.", () -> setKey(150))
					);

					player(100, "What do you do around here?");
					npc("I'm Minimus. It's not my real name of course, but it seems to have stuck. I started out competing here as a gladiator when I was a young'n. After that I became one of the lanistas.");
					player("Lanistas?");
					npc("The ones responsible for overseeing the selecting and training of the gladiators.");
					npc("Eventually, the kingdom gave me the role of Colosseum Master. I still make sure to fit some fighting in, but now I have the honor[sic] of managing the fights themselves.");
					player("So are you not training like the rest of the gladiators?");
					npc("I have no need! I'm already one of the greatest fighters to grace this arena.");
					player("Are you at a disadvantage because of your height?");
					npc("No! How dare you even suggest that! Those spindly human bodies are unfit to go toe to toe with a fine dwarf like myself!");
					npc("Now, did you need anything else?").executeAction(() -> setKey(50));

					player(150, "Goodbye.");
					npc("Farewell, Rookie.");
				}
			});
		});
	}

	@Override
	public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
		int itemId = item.getId();
		if (itemId != ItemId.DIZANAS_QUIVER_UNCHARGED && itemId != ItemId.DIZANAS_QUIVER_UNCHARGED_L) {
			player.getDialogueManager().start(new Dialogue(player, npc) {
				@Override
				public void buildDialogue() {
					npc("I'm afraid you can only offer spare uncharged versions of the quiver to me.");
				}
			});
			return;
		}

		player.getDialogueManager().start(new Dialogue(player, npc) {
			@Override
			public void buildDialogue() {
				boolean hasPet = BossPet.SMOL_HEREDIT.hasPet(player);
				player("I have a spare Dizana's quiver (uncharged).");
				if (hasPet) {
					npc("You want to offer it for some Sunfire splinters? I could also try to convince Smol Heredit to follow you.");
				} else {
					npc("You want to offer it for some Sunfire splinters?");
				}
				npc("But be aware, you will not get the quiver back.");
				if (hasPet) {
					options(new DialogueOption("Trade Dizana's Quiver?", () -> setKey(50)), new DialogueOption("Gamble for a Smol Heredit.", () -> setKey(100)), new DialogueOption("No, keep it."));
				} else {
					options(new DialogueOption("Trade Dizana's Quiver?", () -> setKey(50)), new DialogueOption("No, keep it."));
				}

				options(50, "Are you REALLY sure?", new DialogueOption("No, keep Dizana's quiver.", () -> setKey(110)), new DialogueOption("Yes, exchange Dizana's quiver for 4,000 Sunfire splinters?", () -> {
					Item splinters = new Item(ItemId.SUNFIRE_SPLINTERS, 4000);
					if (!player.getInventory().hasSpaceFor(splinters)) {
						setKey(140);
						return;
					}

					if (player.getInventory().deleteItem(item).getResult() == RequestResult.SUCCESS) {
						player.getInventory().addItem(splinters);
						setKey(150);
					} else {
						finish();
					}
				}));
				options(100, "Are you REALLY sure?", new DialogueOption("No, keep Dizana's quiver.", () -> setKey(110)), new DialogueOption("Yes, gamble for a Smol Heredit?", () -> {
					if (player.getInventory().deleteItem(item).getResult() == RequestResult.SUCCESS) {
						boolean rolledPet = BossPet.SMOL_HEREDIT.roll(player, BossPet.SMOL_HEREDIT.getRarity(player, -1));
						if (rolledPet) {
							setKey(130);
						} else {
							setKey(120);
						}
					} else {
						finish();
					}
				}));

				npc(110, "Very well, Grand Champion. Keep your trophy.");
				npc(120, "He doesn't want to go with you. Sorry.");
				npc(130, "He seems to like you. Smol heredit is yours.");
				npc(140, "It doesn't actually look like you have enough space to carry 4,000 Sunfire splinters. Come back when you have room.");
				doubleItem(150, item, new Item(ItemId.SUNFIRE_SPLINTERS, 4000), "You exchange Dizana's Quiver for 4,000 Sunfire Splinters.");
			}
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[]{NpcId.MINIMUS};
	}

	@Override
	public Object[] getItems() {
		return new Object[] {ItemId.BLESSED_DIZANAS_QUIVER, ItemId.BLESSED_DIZANAS_QUIVER_L, ItemId.DIZANAS_QUIVER, ItemId.DIZANAS_QUIVER_L, ItemId.DIZANAS_QUIVER_UNCHARGED, ItemId.DIZANAS_QUIVER_UNCHARGED_L};
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {NpcId.MINIMUS};
	}
}
