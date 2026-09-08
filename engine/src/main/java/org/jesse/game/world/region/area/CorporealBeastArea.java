package org.jesse.game.world.region.area;

import org.jesse.game.content.boss.corporealbeast.CorporealBeastNPC;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author Kris | 28. mai 2018 : 22:08:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class CorporealBeastArea extends CorporealBeastCavern {

    private final CorporealBeastNPC beast = new CorporealBeastNPC(new Location(2991, 4381, 2), this);

    public CorporealBeastArea() {
        WorldTasksManager.scheduleCreation(beast::spawn);
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(
                new int[][]{
                        {2944, 4416},
                        {2944, 4352},
                        {3008, 4352},
                        {3008, 4416}
                },
                2)};
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.setForceMultiArea(true);
        player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 13);
        beast.refreshDamageDealt(player, 0);
        player.setViewDistance(Player.SCENE_DIAMETER);
    }

    @Override
    public void leave(final Player player, boolean logout) {
    	player.resetViewDistance();
        player.setForceMultiArea(false);
        player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
        clearIfEmpty();
    }

    public void clearIfEmpty() {
        if (isEmpty()) {
            beast.getReceivedDamage().clear();
            beast.setHitpoints(beast.getMaxHitpoints());
        }
    }

    private boolean isEmpty() {
        return players.isEmpty();
    }

    @Override
    public String name() {
        return "Corporeal Beast";
    }

}
