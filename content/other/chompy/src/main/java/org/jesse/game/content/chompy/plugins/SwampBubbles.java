package org.jesse.game.content.chompy.plugins;

import org.jesse.game.content.chompy.BellowsAction;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public class SwampBubbles implements ObjectAction {
    public static final int SWAMP_BUBBLES_ID = 684;
    public static final Animation suckingAnim = new Animation(1026);
    public static final Graphics bellowsGfx = new Graphics(241, 0, 90);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new BellowsAction());
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{SWAMP_BUBBLES_ID};
    }
}