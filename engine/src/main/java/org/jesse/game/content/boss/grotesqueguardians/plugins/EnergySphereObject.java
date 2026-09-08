package org.jesse.game.content.boss.grotesqueguardians.plugins;

import org.jesse.game.content.boss.grotesqueguardians.EnergySphere;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 01/08/2019 | 21:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class EnergySphereObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final var instance = player.getGrotesqueGuardiansInstance();
        if (instance == null) {
            return;
        }
        final EnergySphere sphere = instance.getEnergySphere(object.getId());
        if (sphere == null) {
            return;
        }
        final Projectile projectile = new Projectile(1437 + sphere.getState().ordinal(), 0, 50, 20, 0);
        World.sendProjectile(object, player, projectile);
        World.removeObject(sphere);
        WorldTasksManager.schedule(sphere::absorb, projectile.getTime(object, player));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 31686, 31687, 31688 };
    }
}
