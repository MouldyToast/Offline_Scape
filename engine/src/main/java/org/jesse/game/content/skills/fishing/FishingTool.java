package org.jesse.game.content.skills.fishing;

import org.jesse.game.content.crystal.recipes.chargeable.CrystalTool;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 04/03/2019 22:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FishingTool {

    LOBSTER_POT(new Tool(new Animation(619), 301), new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)),
    SMALL_FISHING_NET(new Tool(new Animation(621), 303), new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)),
    BIG_FISHING_NET(new Tool(new Animation(620), 305), new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)),
    FISHING_ROD(new Tool(new Animation(622), 307), new Tool(new Animation(8188), 22846), new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)),
    FLY_FISHING_ROD(new Tool(new Animation(623), 309), new Tool(new Animation(8189), 22844), new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)),
    OILY_FISHING_ROD(new Tool(new Animation(623), 1585), new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)),
    HARPOON(new Tool(new Animation(618), 311),
            new Tool(new Animation(618), 10129),
            new Tool(new Animation(7401), 21028, true),
            new Tool(new Animation(7402), 21033, true),
            new Tool(new Animation(7402), 21031, true), // Dragon?
            new Tool(new Animation(8336), CrystalTool.Harpoon.INSTANCE.getProductItemId(), true),
            new Tool(new Animation(8336), CrystalTool.Harpoon.INSTANCE.getInactiveId(), false),
            new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)
    ),
    BARBARIAN_ROD(new Tool(new Animation(623), 11323),
            new Tool(new Animation(8190), 22842),
            new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true)),
    KARAMBWAN_VESSEL(new Tool(new Animation(1193), 3157), new Tool(new Animation(8784), ItemId.ECHO_HARPOON, true));

    FishingTool(@NotNull final Tool... tools) {
        this.tools = tools;
    }

    @NotNull
    final Tool[] tools;

    public Optional<Tool> getTool(@NotNull final Player player) {
        final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
        final Inventory inventory = player.getInventory();
        for (int i = tools.length - 1; i >= 0; i--) {
            final FishingTool.Tool tool = tools[i];
            if (weapon == tool.id || inventory.containsItem(tool.id, 1)) {
                return Optional.of(tool);
            }
        }
        return Optional.empty();
    }


    public static class Tool {
        final Animation animation;
        final int id;
        final boolean increasedSpeed;

        Tool(final Animation animation, final int id) {
            this(animation, id, false);
        }

        Tool(final Animation animation, final int id, final boolean increasedSpeed) {
            this.animation = animation;
            this.id = id;
            this.increasedSpeed = increasedSpeed;
        }

        public int getId() {
            return id;
        }

        public boolean isIncreasedSpeed() {
            return increasedSpeed;
        }
    }
}
