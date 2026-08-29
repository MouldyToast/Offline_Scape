package com.zenyte.game.content.scar_essence_mine.npc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class HagusDialogue extends Dialogue {
	public HagusDialogue(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc(1, "Shaz berith an-gor f-");
		player(2, "Hello!");
		npc(3, "Shaz berith an-gor fo gah-");
		player(4, "Hey!");
		npc(5, "Shaz. Berith. An-");
		player(6, "Can you hear me?");
		npc(7, "What?! What do you want?");
		options(8, "Select an Option", "I just wanted to say hello. I didn't expect to find anyone here.", "Whoa, no need to be rude.", "What is this place?", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(10))
				.onOptionTwo(() -> setKey(20))
				.onOptionThree(() -> setKey(30))
				.onOptionFour(() -> setKey(65))
				.onOptionFive(() -> setKey(115));

		player(10, "I just wanted to say hello. I didn't expect to find anyone here.");
		npc(11, "Well, you found me. I'm here. And I was just in the middle of a very important conversation!");
		player(12, "With who? There's no one else here...");
		npc(13, "With Hagus.");
		player(14, "Who's Hagus?");
		npc(15, "I'm Hagus!");
		player(16, "Right...");
		options(17, "Select an Option", "What is this place?", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(30))
				.onOptionTwo(() -> setKey(65))
				.onOptionThree(() -> setKey(115));

		player(20, "Whoa, no need to be rude.");
		npc(21, "You just interrupted a very important conversation!");
		player(22, "With who? There's no one else here...");
		npc(23, "With Hagus.");
		player(24, "Who's Hagus?");
		npc(25, "I'm Hagus!");
		player(26, "Right...");
		options(27, "Select an Option", "What is this place?", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(30))
				.onOptionTwo(() -> setKey(65))
				.onOptionThree(() -> setKey(115));

		player(30, "What is this place?");
		npc(31, "Why should I tell you? You could be one of them.");
		player(32, "One of them? What are you talking about?");
		npc(33, "An abyssal creature, of course!");
		player(34, "Last time I checked I was not an abyssal creature.");
		npc(35, "Hmph. We'll see about that.");
		player(36, "So will you tell me where we are?");
		npc(37, "This is the Abyss.");
		player(38, "I know that. Though I've never been to this part of it before.");
		npc(39, "Have you heard of the Scar?");
		player(40, "Well yes, I just came through there-");
		npc(41, "Well then you know where we are.");
		options(42, "Select an Option", "What is that thing over there?", "What is an extract?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(45))
				.onOptionTwo(() -> setKey(80))
				.onOptionThree(() -> setKey(115));

		player(45, "What is that thing over there?");
		npc(46, "That's Ventriculus.");
		player(47, "Ventriculus? That thing has a name?");
		npc(48, "Yes, it's my friend.");
		player(49, "I... I can't tell if you're joking or not.");
		npc(50, "Ventriculus is an extractor. Feed it and you'll see.");
		player(51, "Feed it?");
		npc(52, "Yes. Just put your arm in.");
		player(53, "That doesn't sound safe.");
		npc(54, "It likes the tainted essence you can find in here.");
		item(55, 28591, "Hagus shows you some tainted essence.");
		npc(56, "A pickaxe can help you split off the essence from the amalgamations.");
		player(57, "Right... So I mine tainted essence from amalgamations, then feed it to this Ventriculus.");
		player(58, "But why should I do that?");
		npc(59, "Remember how I said it's an extractor?");
		npc(60, "Ventriculus will remove excess parts from the essence, chew it up a little, and spit out the rest.");
		player(61, "So you're saying it refines the essence?");
		npc(62, "Yes, it can refine it into pure essence. However, it can also give you extracts.");
		options(63, "Select an Option", "What is an 'extract'?", "What are you doing here?", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(80))
				.onOptionTwo(() -> setKey(101))
				.onOptionThree(() -> setKey(65))
				.onOptionFour(() -> setKey(115));

		player(65, "Can you tell me more about yourself?");
		npc(66, "My name is Hagus.");
		player(67, "Yes...So you've said. Where are you from?");
		npc(68, "My upbringing is none of your concern.");
		player(69, "I don't mean to pry. I just thought I'd get to know you a little bit.");
		player(70, "How long have you been in here?");
		npc(71, "Many years. Years. Time... Yes, time. It's time I showed them. I need to find the rune.");
		player(72, "Rune? What rune?");
		npc(73, "Unless you're a member of the Institute, I cannot share that with you.");
		options(74, "Select an Option", "What is that thing over there?", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(45))
				.onOptionTwo(() -> setKey(65))
				.onOptionThree(() -> setKey(115));

		player(80, "What is an 'extract'?");
		npc(81, "Extracts are powerful, condensed runes. They function as catalysts to make more runes.");
		item(82, 28593, "Hagus shows you one of the extracts.");
		player(83, "Make more runes? How exactly does that work?");
		npc(84, "If you bring an extract when you runecraft, it multiplies the amount of runes you make.");
		player(85, "That's neat! I can make so much money from selling these runes!");
		npc(86, "Now hold on. I didn't say this was free.");
		player(87, "Ah... I suppose that makes sense.");
		npc(88, "I don't care about pure essence. You can extract as much as you want of that.");
		npc(89, "But for extracts, you've got to pay up. There's a coffer over there. Leave some money and I'll charge you for the extract you want.");
		options(90, "Select an Option", "Can you explain the Ventriculus again?", "What are you doing here?", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(91))
				.onOptionTwo(() -> setKey(101))
				.onOptionThree(() -> setKey(50))
				.onOptionFour(() -> setKey(115));

		player(91, "Can you explain the Ventriculus again?");
		npc(92, "Ventriculus is an extractor. You can feed it essence from the amalgamations here.");
		npc(93, "Once fed, it churns away, picking apart the essence, leftover runes and flesh.");
		npc(94, "The final product is either pure essence or dense rune extracts.");
		player(95, "And how do the extracts work?");
		npc(96, "Extracts function as catalysts to make more runes.");
		npc(97, "If you bring an extract when you runecraft, it multiplies the amount of runes you make.");
		npc(98, "However, extracts are valuable. I'll demand some payment for those.");
		player(99, "Thanks for explaining.");
		options(100, "Select an Option", "What are you doing here?", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(101))
				.onOptionTwo(() -> setKey(65))
				.onOptionThree(() -> setKey(115));

		player(101, "What are you doing here?");
		npc(102, "I'm hanging out with my friend.");
		player(103, "The... Ventriculus? It's really your friend?");
		npc(104, "It's helping me search for knowledge on powerful runes.");
		player(105, "I guess you could just... not answer my question.");
		plain(106, "Hagus' eyes peer at the Ventriculus.");
		npc(107, "We heard rumours of a strong one... but the altar was nowhere to be found.");
		player(108, "You're not really talking to me at all, are you.");
		npc(109, "Herbert, Finnigan and I... We were all assigned important missions. I must fulfill mine.");
		player(110, "What mission? Who's Finnigan?");
		plain(111, "Hagus snaps out of it.");
		npc(112, "Ah, where were we. Did you feed Ventriculus yet? We need more-");
		npc(113, "I mean. I'm sure you'd like some extracts.");
		options(114, "Select an Option", "Can you tell me more about yourself?", "Never mind. Goodbye.")
				.onOptionOne(() -> setKey(65))
				.onOptionTwo(() -> setKey(115));

		player(115, "Never mind. Goodbye.");
		npc(116, "Hagus walks away, chanting to himself.");
	}
}