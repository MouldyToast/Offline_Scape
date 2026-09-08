/**
 * 
 */
package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;

/**
 * @author Noele | May 31, 2018 : 6:17:50 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public class Sheep extends NPC implements Spawnable {
	
	private static final ForceTalk BAA = new ForceTalk("Baa!");
	private static final ForceTalk FAKE = new ForceTalk("Baa?");
	
	private int ticks = 30;
	private final int original;
	
	public Sheep(final int id, final Location location, final Direction direction, final int radius) {
		super(id, location, direction, radius);
		original = id;
	}
	
	
	@Override
	public void processNPC() {
		super.processNPC();
		if(id == 2698) {
			if(--ticks == 0) {
				setTransformation(original);
				ticks = 30;
			}
		}	
		
		if(Utils.random(0, 25) == 0) {
			setForceTalk(id != 731 ? BAA : FAKE);
		}
	}


	@Override
	public boolean validate(final int id, final String name) {
		return name.equals("sheep");
	}
}
