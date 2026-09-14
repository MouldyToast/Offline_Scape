package org.jesse.plugins.object;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.Skills;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;
import org.jesse.plugins.events.LoginEvent;

/**
 * @author Kris | 10/05/2019 20:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ObservatoryGrapple implements ObjectAction {

    private static final int MITH_GRAPPLE = 9419;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getVarManager().getBitValue(5810) == 1) {
            new FadeScreen(player, () -> player.setLocation(new Location(2444, 3165, 0))).fade(3);
            return;
        }
        final Skills skills = player.getSkills();
        if (skills.getLevel(SkillConstants.AGILITY) < 23 || skills.getLevel(SkillConstants.RANGED) < 24 || skills.getLevel(SkillConstants.STRENGTH) < 28) {
            player.getDialogueManager().start(new PlainChat(player, "You need an Agility level of at least 23, a Ranged level of at least 24 & a Strength level of at least 28 to use this shortcut."));
            return;
        }
        if (player.getWeapon() == null || !player.getWeapon().getName().toLowerCase().contains("crossbow")) {
            player.sendMessage("You need a crossbow equipped to do that.");
            return;
        }
        if (player.getAmmo() == null || player.getAmmo().getId() != MITH_GRAPPLE) {
            player.sendMessage("You need a mithril grapple tipped bolt with a rope to do that.");
            return;
        }
        player.lock(2);
        player.getEquipment().set(EquipmentSlot.AMMUNITION, null);
        player.setAnimation(new Animation(7552));
        World.sendProjectile(player, new Location(2446, 3158, 0), new Projectile(762, 136, 136, 40, 65, 20, 0, 5));
        WorldTasksManager.schedule(() -> {
            player.getVarManager().sendBit(5810, 1);
            player.addAttribute("observatory grapple", 1);
        }, 1);
    }

    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        if (event.getPlayer().getNumericAttribute("observatory grapple").intValue() == 1) {
            event.getPlayer().getVarManager().sendBit(5810, 1);
        }
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(2448, 3155, 0)), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 31850, 31852, ObjectId.ROPE_31853, 31849 };
    }
}
