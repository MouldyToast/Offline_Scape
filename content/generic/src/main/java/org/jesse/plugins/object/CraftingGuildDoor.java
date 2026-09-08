package org.jesse.plugins.object;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.SkillcapePerk;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.Door;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 29. mai 2018 : 17:25:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CraftingGuildDoor implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final int crafting = player.getSkills().getLevelForXp(SkillConstants.CRAFTING);
		final int body = player.getEquipment().getId(EquipmentSlot.PLATE);
		if (player.inArea("Crafting guild") || crafting >= 40 && body == ItemId.BROWN_APRON || SkillcapePerk.CRAFTING.isEffective(player)) {
			player.lock(3);
			player.addWalkSteps(object.getX(), object.getY());
			WorldTasksManager.schedule(new WorldTask() {
				private int ticks;
				private WorldObject door;
				@Override
				public void run() {
					switch (ticks++) {
					case 0: 
						door = Door.handleGraphicalDoor(object, null);
						return;
					case 1: 
						if (!player.inArea("Crafting guild")) {
							player.addWalkSteps(door.getX(), door.getY(), 1, false);
						} else {
							player.addWalkSteps(object.getX(), object.getY(), 1, false);
						}
						return;
					case 3: 
						Door.handleGraphicalDoor(door, object);
						stop();
						return;
					}
				}
			}, 0, 0);
			return;
		} else {
			World.sendClosestNPCDialogue(player, NpcId.MASTER_CRAFTER, new Dialogue(player) {
				@Override
				public void buildDialogue() {
					npc("Sorry. Only the finest craftsman are allowed in here. Get your crafting level up to 40 and come back wearing a brown apron.");
				}
			});
			if (crafting < 32) {
				player.sendMessage("You need a Crafting level of 40 to enter the Crafting guild.");
			}
			return;
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.GUILD_DOOR_14910};
	}
}
