package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.skills.construction.costume.*;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;

public class CostumeRoomInterface extends Interface {

    @Override
    protected void attach() {
        put(4, "Items");
    }

    @Override
    public void open(Player player) {
        // Opening is handled by each costume object directly, not through this handler.
    }

    @Override
    protected void build() {
        bind("Items", (player, slotId, itemId, option) -> {
            if (option == 10 && itemId > 0) {
                player.sendMessage(ItemDefinitions.get(itemId).getExamine());
                return;
            }
            if (option == 1 && itemId > 0) {
                final Object type = player.getTemporaryAttributes().get("costumeRoomObject");
                if (type == null) return;
                switch (type.toString()) {
                    case "ARMOUR_CASE":
                        player.getConstruction().getArmourCase().takeSet(itemId);
                        break;
                    case "CAPE_RACK":
                        player.getConstruction().getCapeRack().takeSet(itemId);
                        break;
                    case "FANCY_DRESS_BOX":
                        player.getConstruction().getFancyDressBox().takeSet(itemId);
                        break;
                    case "MAGIC_WARDROBE":
                        player.getConstruction().getMagicWardrobe().takeSet(itemId);
                        break;
                    case "TOY_BOX":
                        player.getConstruction().getToyBox().takeSet(itemId);
                        break;
                    case "TREASURE_CHEST":
                        player.getConstruction().getTreasureChest().takeSet(itemId);
                        break;
                }
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.COSTUME_ROOM;
    }
}
