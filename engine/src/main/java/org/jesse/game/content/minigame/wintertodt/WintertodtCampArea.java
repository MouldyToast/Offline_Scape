package org.jesse.game.content.minigame.wintertodt;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.kourend.GreatKourend;
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin;
import org.jesse.game.world.region.area.plugins.LayableTrapRestrictionPlugin;
import org.jesse.game.world.region.area.plugins.RandomEventRestrictionPlugin;

public class WintertodtCampArea extends GreatKourend implements CannonRestrictionPlugin, LayableTrapRestrictionPlugin, RandomEventRestrictionPlugin {
    
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {1623, 3964},
                        {1622, 3953},
                        {1613, 3942},
                        {1614, 3931},
                        {1625, 3926},
                        {1639, 3928},
                        {1648, 3940},
                        {1647, 3947},
                        {1641, 3952},
                        {1636, 3954},
                        {1638, 3964}}, 0)
        };
    }
    
    @Override
    public void enter(Player player) {
        GameInterface.WINTERTODT.open(player);
        player.getPacketDispatcher().sendClientScript(1432); // hide energy/braziers/points
    }
    
    @Override
    public void leave(Player player, boolean logout) {
        player.getInterfaceHandler().closeInterface(GameInterface.WINTERTODT);
    }

    @Override
    public String name() {
        return "Wintertodt Camp";
    }

}
