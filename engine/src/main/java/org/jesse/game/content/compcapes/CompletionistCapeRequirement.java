package org.jesse.game.content.compcapes;

import org.jesse.game.world.entity.player.Player;

import java.util.function.Function;

public class CompletionistCapeRequirement {

	private final String task;
	private final Function<Player, Integer> function;
	private final int requirement;

	CompletionistCapeRequirement(String task, Function<Player, Integer> function, int requirement) {
		this.task = task;
		this.function = function;
		this.requirement = requirement;
	}

	public String getTask() {
		return task;
	}

	public Function<Player, Integer> getFunction() {
		return function;
	}

	public boolean test(Player player) {
		return function.apply(player) >= requirement;
	}

	public int getRequirement() {
		return requirement;
	}

}
