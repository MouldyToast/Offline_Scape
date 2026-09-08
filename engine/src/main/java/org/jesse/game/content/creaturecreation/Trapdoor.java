package org.jesse.game.content.creaturecreation;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.events.LoginEvent;

/**
 * @author Chris
 * @since August 22 2020
 */
public class Trapdoor implements ObjectAction {

    private static final ImmutableLocation destination = new ImmutableLocation(3038, 4376, 0);

    private static final int TRAPDOOR_VARBIT = 3372;

    @Subscribe
    public static void onLogin(final LoginEvent event) {
        event.getPlayer().getVarManager().sendBit(TRAPDOOR_VARBIT, 1);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setAnimation(Animation.LADDER_DOWN);
        player.teleport(destination);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 21944 };
    }
}
