package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.npc.DeathlyNPC;
import org.jesse.game.content.chambersofxeric.room.DeathlyRoom;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;
import org.jesse.utils.TimeUnit;

import static org.jesse.game.content.chambersofxeric.room.DeathlyRoom.forceChats;
import static org.jesse.game.content.chambersofxeric.room.DeathlyRoom.renderAnimation;

/**
 * @author Kris | 06/07/2019 04:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Tightrope implements ObjectAction {

    private static final SoundEffect sound = new SoundEffect(2495, 5, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, DeathlyRoom.class, room -> {
            if (player.getSkills().getLevel(SkillConstants.AGILITY) < room.getRequirement()) {
                player.getDialogueManager().start(new PlainChat(player, "You need an Agility level of " + room.getRequirement() + " to cross this tightrope."));
                return;
            }
            if (player.getNumericTemporaryAttribute("tightrope_cox_delay").longValue() > System.currentTimeMillis()) {
                return;
            }
            player.getTemporaryAttributes().put("tightrope_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(15));
            player.lock();
            player.setRunSilent(true);
            player.getAppearance().setRenderAnimation(renderAnimation);
            int count = Utils.random(1, 4);
            for (final DeathlyNPC n : room.getNpcs()) {
                if (n.getCombat().getTarget() != null) {
                    continue;
                }
                if (count-- > 0) {
                    n.setForceTalk(forceChats[Utils.random(forceChats.length - 1)]);
                }
            }
            room.getNpcs().forEach(npc -> {
                if (!npc.isDead() && npc.getCombat().getTarget() == null) {
                    npc.getCombat().setTarget(player);
                }
            });
            if (object.getRotation() == 0 || object.getRotation() == 2) {
                if (player.getY() < object.getY()) {
                    player.addWalkSteps(object.getX(), object.getY() + 9, -1, false);
                } else {
                    player.addWalkSteps(object.getX(), object.getY() - 9, -1, false);
                }
            } else {
                if (player.getX() < object.getX()) {
                    player.addWalkSteps(object.getX() + 9, object.getY(), -1, false);
                } else {
                    player.addWalkSteps(object.getX() - 9, object.getY(), -1, false);
                }
            }
            player.sendMessage("You walk carefully across the tightrope...");
            WorldTasksManager.schedule(new WorldTask() {

                private int ticks = 0;

                @Override
                public void run() {
                    if (ticks > 0 && ticks < 9) {
                        World.sendSoundEffect(player.getLocation(), sound);
                    }
                    if (raid.isDestroyed()) {
                        player.getAppearance().resetRenderAnimation();
                        stop();
                        return;
                    }
                    if (ticks++ == 10) {
                        player.getTemporaryAttributes().put("tightrope_cox_delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(2));
                        player.unlock();
                        player.setRunSilent(false);
                        player.getAppearance().resetRenderAnimation();
                        player.sendMessage("...You make it safely to the other side.");
                        player.getSkills().addXp(SkillConstants.AGILITY, 7.5);
                        stop();
                    }
                }
            }, 0, 0);
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TIGHTROPE_29750 };
    }
}
