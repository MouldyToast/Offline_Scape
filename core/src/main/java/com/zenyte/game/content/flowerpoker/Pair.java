package com.zenyte.game.content.flowerpoker;

public record Pair(int amount) {
	public static Pair of(int pairs) {
		return new Pair(pairs);
	}
}