package org.jesse.game.content.gauntlet.plugins.resources;

import org.jesse.game.content.gauntlet.actions.FillVialAction;
import org.jesse.game.content.gauntlet.actions.FishingSpotAction;
import org.jesse.game.content.gauntlet.objects.FishingSpot;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Andys1814.
 * @since 1/25/2022.
 */
public final class GauntletFishingSpot implements ObjectAction, ItemOnObjectAction {

    private static final int FISHING_SPOT = 36068;

    private static final int FISHING_SPOT_DEPLETED = 36069;

    private static final int FISHING_SPOT_CORRUPTED = 35971;

    private static final int FISHING_SPOT_CORRUPTED_DEPLETED = 35972;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!(object instanceof FishingSpot)) {
            return;
        }
        final FishingSpot spot = (FishingSpot) object;

        if (object.getId() == FISHING_SPOT || object.getId() == FISHING_SPOT_CORRUPTED) {
            boolean corrupted = object.getId() == FISHING_SPOT_CORRUPTED;
            player.getActionManager().setAction(new FishingSpotAction(spot, corrupted));
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getActionManager().setAction(new FillVialAction());
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 23879 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { FISHING_SPOT, FISHING_SPOT_DEPLETED, FISHING_SPOT_CORRUPTED, FISHING_SPOT_CORRUPTED_DEPLETED };
    }

}
