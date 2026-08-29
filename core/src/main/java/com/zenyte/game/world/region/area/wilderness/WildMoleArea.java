package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

public class WildMoleArea extends WildernessArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new Location(3129, 3904), new Location(3143, 3963)),
                new RSPolygon(new Location(3143, 3904), new Location(3168, 3916))
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.EASTERN_DRAGONS);
    }

    @Override
    public String name() {
        return "Wilderness - Wild Mole Area";
    }

    @Override
    public boolean isSinglesPlusArea(Position position) {
        return true;
    }
}
