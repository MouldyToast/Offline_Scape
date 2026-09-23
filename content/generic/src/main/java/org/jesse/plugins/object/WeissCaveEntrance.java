package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public class WeissCaveEntrance implements ObjectAction {

    private static final int CAVE_ENTRANCE = 33329;
    private static final int TOWN_HOLE = 33227;
    private static final int MINE_TO_MUSPAH = 46905;
    private static final int MUSPAH_ESCAPE = 46599;

    private static final int SANITY_VARBIT = 15064;
    private static final int MINIMAP_STATE_VARBIT = 6719;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (object.getId()) {
            case CAVE_ENTRANCE:
                new FadeScreen(player, () -> player.setLocation(new Location(2854, 3941))).fade(3);
                break;
            case TOWN_HOLE:
                new FadeScreen(player, () -> player.setLocation(new Location(2859, 3968))).fade(3);
                break;
            case MINE_TO_MUSPAH:
                new FadeScreen(player, () -> {
                    player.setLocation(new Location(2902, 10337, 0));
                    player.getVarManager().sendBit(SANITY_VARBIT, 100);
                    player.getVarManager().sendBit(MINIMAP_STATE_VARBIT, 2);
                }).fade(3);
                break;
            case MUSPAH_ESCAPE:
                new FadeScreen(player, () -> {
                    player.setLocation(new Location(2846, 10333, 0));
                    player.getVarManager().sendBit(SANITY_VARBIT, 0);
                    player.getVarManager().sendBit(MINIMAP_STATE_VARBIT, 0);
                }).fade(3);
                break;
        }
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == TOWN_HOLE) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(2854, 3941, 0)), getRunnable(player, object, name,
                    optionId, option), getDelay()));
        } else {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), getRunnable(player, object, name,
                    optionId, option), getDelay()));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{CAVE_ENTRANCE, TOWN_HOLE, MINE_TO_MUSPAH, MUSPAH_ESCAPE};
    }
    
}
