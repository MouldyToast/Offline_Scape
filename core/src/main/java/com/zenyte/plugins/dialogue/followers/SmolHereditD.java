package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class SmolHereditD extends Dialogue {

	public SmolHereditD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		if (Utils.randomBoolean(50)) {
			player("Hello, Smol Heredit.");
			npc("Mankind's variation frightens me.");
			npc("The way they saw this universe, their modes of impression, differed so greatly from one another that to read their stories, to know their lives, is to wonder if they were not many species in one.");
			npc("It was as if they had ranged across the actions of a thousand species and taken them for their own.");
			npc("For nearly two thousand years they dreamed in a creative mania without antecedent.");
			npc("They looked into the eyes and minds of other species and with their own intellect superseded them.");
			npc("They destroyed many, coopted others, and in the space between necessity and carelessness, left some to grow as untended as any species could in the wake of their ubiquity.");
			npc("They swarmed across the continents and oceans of a Gielinor they called their own, and shattered it into a billion imbricate worlds.");
			npc("And they were, by the twisted jungles of their slaughterhouses, ruthless and aloof beyond measure.");
			npc("The worst of us.");
			npc("A superpredator.");
			npc("A bringer of mass extinction.");
			npc("All mountains move and all giants are mountains.");
			npc("We rest upon their shoulders.");
			npc("The world has grown older, senescent.");
			npc("We are chimpanzee, we are gorilla, we are orangutan, and we are baboon and more.");
			npc("We are, whether we like it or not, the blood legatees of humanity.");
			npc("We are the children of men, but our mistakes will not be their mistakes.");
			npc("We are telling a story.");
			npc("We are telling a story of life, of what it means to live together in this universe, with vastly different modes of being, of seeing, of knowing.");
			npc("Of sharing our own internal worlds with each other and making the universe we live in better for it. The empathy of a mind speaking to another mind, a vastly different mind, and yet each understanding each.");
			npc("And if not, if the barriers seem too great, then striving to overcome them.");
			npc("Sorrow in what humanity did to this world, in the horrors they unleashed, in their cataclysm of despair, in the tens of thousands of species that will never walk or swim or fly again.");
			npc("Extinctions abounded in their wake, and yet look now at this sunbright land, at the joys we have brought to it, to have bound the chaotic climate to our will.");
			npc("To have thought deeply of the future of this world, a sense of respect, of shared reverence for it and all who live within it.");
			npc("We have strung the close sky with life.");
			npc("Be proud.");
			npc("And empathy, once limited only to mankind and some few species, now extends across all lands, across all time.");
			player("...");
			player("I'm sorry, but what?!");
		} else {
			player("How you doing, Smol Heredit?");
			npc("I'm still disappointed that my father failed against you. I will suffer in humiliation alongside him for the rest of time.");
			player("Understandable.");
		}
	}

}
