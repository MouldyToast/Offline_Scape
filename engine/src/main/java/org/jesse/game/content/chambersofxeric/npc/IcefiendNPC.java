package org.jesse.game.content.chambersofxeric.npc;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;

/**
 * @author Kris | 17. nov 2017 : 19:45.08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class IcefiendNPC extends NPC {

	private static final Animation death = new Animation(1580);
	private static final SoundEffect deathSound = new SoundEffect(532, 10, 0);
	
	public IcefiendNPC(final Location tile) {
		super(7586, tile, true);
	}
	
	@Override
	public final void processNPC() {
		
	}
	
	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		return false;
	}
	
	@Override
	public void sendDeath() {
		resetWalkSteps();
		getCombat().removeTarget();
		setAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			private int loop;
			@Override
			public void run() {
				if (loop == 0) {
					setAnimation(death);
                    World.sendSoundEffect(getLocation(), deathSound);
				} else if (loop == 1) {
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

}
