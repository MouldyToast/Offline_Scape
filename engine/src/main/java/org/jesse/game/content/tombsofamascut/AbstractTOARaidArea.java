package org.jesse.game.content.tombsofamascut;

import org.jesse.game.world.region.DynamicArea;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;

public abstract class AbstractTOARaidArea extends DynamicArea {
    protected AbstractTOARaidArea(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
    }

    public boolean isOverlyDraining(){
        return false;
    }

    public boolean isQuietPrayers() {
        return false;
    }

    public boolean isDeadlyPrayers() {
        return false;
    }

    public boolean isDeHydration() {
        return false;
    }
}
