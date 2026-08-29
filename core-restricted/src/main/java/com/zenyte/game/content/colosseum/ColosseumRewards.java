package com.zenyte.game.content.colosseum;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;

public enum ColosseumRewards {

	DROP_1(ItemId.ONYX_BOLTS, 792, 100),
	DROP_2(ItemId.RUNE_WARHAMMER, 792, 8),
	DROP_3(ItemId.DRAGON_PLATELEGS, 792, 3),
	DROP_4(ItemId.SUNFIRE_SPLINTERS, 792, 3_500),
	DROP_5(ItemId.DRAGON_ARROWTIPS, 792, 250),

	DROP_6(ItemId.UNCUT_ONYX, 110),
	DROP_7(ItemId.DRAGON_PLATESKIRT, 110,5),
	DROP_8(ItemId.RUNE_2H_SWORD, 110, 9),
	DROP_9(ItemId.RUNITE_ORE, 110, 35),

	DROP_10(ItemId.ECHO_CRYSTAL, 135),
	DROP_11(ItemId.ECHO_CRYSTAL, 15, 2, 3),

	//Sunfire pieces, 75 weight for each of it, custom logic that makes sure you get all 3 before dupes
	DROP_12(-1, 75 + 75 + 75),

	DROP_13(ItemId.TONALZTICS_OF_RALOS_UNCHARGED, 25),
	;

	public static final ColosseumRewards[] values = values();
	private static int totalWeight;
	private final int itemId;
	private final int weight, min, max;

	ColosseumRewards(int id, int weight, int max) {
		this(id, weight, 1, max);
	}

	ColosseumRewards(int id, int weight) {
		this(id, weight, 1, 1);
	}

	ColosseumRewards(int itemId, int weight, int min, int max) {
		this.itemId = itemId;
		this.weight = weight;
		this.min = min;
		this.max = max;
	}

	public int getItemId() {
		return itemId;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public static ColosseumRewards getRandom() {
		int value = Utils.random(totalWeight);
		int weight = 0;

		for (ColosseumRewards dropEntry : values) {
			weight += dropEntry.weight;
			if (value < weight) {
				return dropEntry;
			}
		}

		return null;
	}

	static {
		for (ColosseumRewards dropEntry : values) {
			totalWeight += dropEntry.weight;
		}
	}

}
