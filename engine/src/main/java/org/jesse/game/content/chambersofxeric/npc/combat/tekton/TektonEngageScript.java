package org.jesse.game.content.chambersofxeric.npc.combat.tekton;

import org.jesse.game.content.chambersofxeric.npc.Tekton;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Entity.EntityType;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import org.jesse.game.world.entity.npc.combatdefs.StatType;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.object.WorldObject;

import java.util.List;

/**
 * @author Kris | 9. mai 2018 : 15:31:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TektonEngageScript implements TektonScript {
	@Override
	public void execute(final Tekton tekton) {
		WorldTasksManager.schedule(new WorldTask() {
			private int stage;
			private boolean engage;
			private Location tile;
			private Entity target;
			@Override
			public void run() {
				if (tekton.getRoom().getRaid().isDestroyed() || tekton.isDead() || tekton.isFinished()) {
					stop();
					return;
				}
				if (stage == 0) {
					tekton.animate("rise from anvil");
					tekton.setTransformation(tekton.isEnraged() ? 7543 : 7541);
				} else if (stage == 2) {
					final List<Entity> possibleTargets = tekton.getPossibleTargets(EntityType.PLAYER);
					if (possibleTargets.isEmpty() || (target = possibleTargets.get(Utils.random(possibleTargets.size() - 1))) == null) {
						tekton.execute("return to anvil");
						stop();
						return;
					}
					tekton.setFaceEntity(target);
					tekton.calcFollow(target, -1, true, false, false);
					tile = new Location(target.getLocation());
				} else if (stage > 2) {
					if (target.getLocation().getPositionHash() != tile.getPositionHash()) {
						tekton.resetWalkSteps();
						tekton.calcFollow(target, -1, true, false, false);
						tile = new Location(target.getLocation());
					}
					if (tekton.hasWalkSteps()) {
						return;
					}
					if (tekton.isEnteredCombat()) {
						if (!tekton.isBoostedStats()) {
							final NPCCombatDefinitions combat = tekton.getCombatDefinitions();
							tekton.setBoostedStats(true);
							combat.getStatDefinitions().set(StatType.DEFENCE, (int) (combat.getStatDefinitions().get(StatType.DEFENCE) * 1.3F));
						}
						tekton.setEnrageTime(18);
					}
					tekton.setFaceEntity(null);
					if (engage) {
						tekton.setTarget(target);
						tekton.setTransformation(tekton.isEnraged() ? 7544 : 7542);
						tekton.execute("combat");
						tekton.setEnteredCombat(true);
						stop();
						return;
					}
					if (CombatUtilities.isWithinMeleeDistance(tekton, target)) {
						tekton.animate("enter combat stance");
						engage = true;
						final WorldObject obj = new WorldObject(30023, 10, 0, tekton.getX(), tekton.getY(), tekton.getPlane());
						tekton.setBlock(obj);
						World.spawnObject(obj);
					} else {
						tekton.execute("return to anvil");
						stop();
						return;
					}
				}
				stage++;
			}
		}, 0, 0);
	}
}
