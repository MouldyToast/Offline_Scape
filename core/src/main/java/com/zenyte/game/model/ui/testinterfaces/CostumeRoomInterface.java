package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.content.skills.construction.ConstructionKeys;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.construction.costume.*;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
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
                        ConstructionKeys.construction(player).getArmourCase().takeSet(itemId);
                        break;
                    case "CAPE_RACK":
                        ConstructionKeys.construction(player).getCapeRack().takeSet(itemId);
                        break;
                    case "FANCY_DRESS_BOX":
                        ConstructionKeys.construction(player).getFancyDressBox().takeSet(itemId);
                        break;
                    case "MAGIC_WARDROBE":
                        ConstructionKeys.construction(player).getMagicWardrobe().takeSet(itemId);
                        break;
                    case "TOY_BOX":
                        ConstructionKeys.construction(player).getToyBox().takeSet(itemId);
                        break;
                    case "TREASURE_CHEST":
                        ConstructionKeys.construction(player).getTreasureChest().takeSet(itemId);
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
