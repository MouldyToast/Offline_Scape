package org.jesse.game.content.skills.construction;

import org.jesse.game.content.skills.construction.dialogue.RemoveObjectD;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.controller.Controller;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 6. march 2018 : 16:47.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ConstructionController extends Controller {

	private static final Animation ANIM = new Animation(834);
	
	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == ConstructionConstants.EXIT_PORTAL) {
			player.getConstruction().leaveHouse();
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick5(final WorldObject object) {
		final String option = object.getDefinitions().getOption(5);
		if (option == null) {
			return true;
		}
		if (option.equalsIgnoreCase("Build")) {
			if (ConstructionConstants.isDoor(object)) {
				player.getConstruction().sendRoomCreationMenu(object);
				return false;
			} else {
				player.getConstruction().sendFurnitureCreationMenu(object);
				return false;
			}
		} else if (option.equals("Remove")) {
			player.getDialogueManager().start(new RemoveObjectD(player, object));
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processNPCClick3(final NPC npc) {
		final int id = npc.getId();
		if (id == 2668 || id == 7413) {
			if (!(npc instanceof CombatDummyNPC)) {
				return false;
			}
			final CombatDummyNPC n = (CombatDummyNPC) npc;
			player.setAnimation(ANIM);
			WorldTasksManager.schedule(() -> {
				n.finish();
				World.spawnObject(n.getObject());
			});
			return false;
		}
		return true;
	}
	
	@Override
	public boolean removeOnLogout() {
		player.forceLocation(player.getConstruction().getHouse().getLocation());
		return true;
	}

}
