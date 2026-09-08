package org.jesse.game.content.advent;

import org.jesse.game.world.entity.Location;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class ReindeerObject extends WorldObject {

	private final int index;

	public ReindeerObject(int index, Location location) {
		super(ObjectId.REINDEER, 10, 0, location);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
