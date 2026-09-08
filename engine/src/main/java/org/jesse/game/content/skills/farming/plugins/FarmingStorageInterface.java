package org.jesse.game.content.skills.farming.plugins;

import org.jesse.game.GameInterface;
import org.jesse.game.content.skills.farming.FarmingStorage;
import org.jesse.game.content.skills.farming.Storable;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.net.packet.PacketDispatcher;
import org.jesse.game.util.AccessMask;
import org.jesse.game.util.ItemUtil;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.var.VarCollection;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 04/02/2019 01:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FarmingStorageInterface extends Interface {
    @Override
    protected void attach() {
        put(4, "Quantity 1");
        put(5, "Quantity 5");
        put(6, "Quantity X");
        put(7, "Quantity All");
        put(8, "Rake");
        put(9, "Seed dibber");
        put(10, "Spade");
        put(11, "Secateurs");
        put(12, "Watering can");
        put(13, "Gardening trowel");
        put(14, "Plant cure");
        put(15, "Bottomless bucket");
        put(16, "Empty buckets");
        put(17, "Normal compost");
        put(18, "Supercompost");
        put(19, "Ultracompost");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getInterfaceHandler().sendInterface(GameInterface.FARMING_STORAGE_INVENTORY);
        player.getFarming().getStorage().refresh();
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentSettings(getInterface(), getComponent("Quantity 1"), -1, -1, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Quantity 5"), -1, -1, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Quantity X"), -1, -1, AccessMask.CLICK_OP1);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Quantity All"), -1, -1, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Quantity 1", player -> setDefaultQuantityOption(player, 0));
        bind("Quantity 5", player -> setDefaultQuantityOption(player, 1));
        bind("Quantity X", player -> setDefaultQuantityOption(player, 3));
        bind("Quantity All", player -> setDefaultQuantityOption(player, 2));
        bind("Rake", (player, slotId, itemId, option) -> remove(player, Storable.FARMING_RAKE, option));
        bind("Seed dibber", (player, slotId, itemId, option) -> remove(player, Storable.SEED_DIBBER, option));
        bind("Spade", (player, slotId, itemId, option) -> remove(player, Storable.SPADE, option));
        bind("Secateurs", (player, slotId, itemId, option) -> remove(player, Storable.SECATEURS, option));
        bind("Watering can", (player, slotId, itemId, option) -> remove(player, Storable.WATERING_CAN, option));
        bind("Gardening trowel", (player, slotId, itemId, option) -> remove(player, Storable.GARDENING_TROWEL, option));
        bind("Plant cure", (player, slotId, itemId, option) -> remove(player, Storable.PLANT_CURE, option));
        bind("Bottomless bucket", (player, slotId, itemId, option) -> remove(player, Storable.BOTTOMLESS_BUCKET, option));
        bind("Empty buckets", (player, slotId, itemId, option) -> remove(player, Storable.EMPTY_BUCKET, option));
        bind("Normal compost", (player, slotId, itemId, option) -> remove(player, Storable.NORMAL_COMPOST, option));
        bind("Supercompost", (player, slotId, itemId, option) -> remove(player, Storable.SUPER_COMPOST, option));
        bind("Ultracompost", (player, slotId, itemId, option) -> remove(player, Storable.ULTRACOMPOST, option));
    }

    private void setDefaultQuantityOption(final Player player, final int option) {
        player.getAttributes().put("farming equipment default", option);
        VarCollection.FARMING_EQUIPMENT_STORAGE_DEFAULT.updateSingle(player);
    }

    private static final void remove(@NotNull final Player player, @NotNull final Storable storable, int option) {
        final FarmingStorage storage = player.getFarming().getStorage();
        final FarmingStorageInterface.Option op = storage.getOption(option);
        final Item item = storage.getItem(storable);
        switch (op) {
        case EXAMINE: 
            if (item == null) {
                ItemUtil.sendItemExamine(player, storable.getBaseId());
            } else {
                ItemUtil.sendItemExamine(player, item);
            }
            return;
        case REMOVE_X: 
            player.sendInputInt("How many would you like to remove?", i -> storage.removeItem(storable, item, i));
            return;
        case REMOVE_1: 
            storage.removeItem(storable, item, 1);
            return;
        case REMOVE_5: 
            storage.removeItem(storable, item, 5);
            return;
        case REMOVE_ALL: 
            storage.removeItem(storable, item, Integer.MAX_VALUE);
            return;
        case BANKNOTES: 
            storage.removeItem(storable, item, Integer.MIN_VALUE);
            return;
        default: 
            throw new RuntimeException();
        }
    }


    public enum Option {
        REMOVE_1(1), REMOVE_5(2), REMOVE_X(3), REMOVE_ALL(4), BANKNOTES(9), EXAMINE(10);
        public static final Option[] values = values();
        final int optionId;

        Option(int optionId) {
            this.optionId = optionId;
        }

        public int getOptionId() {
            return optionId;
        }
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.FARMING_STORAGE;
    }
}
