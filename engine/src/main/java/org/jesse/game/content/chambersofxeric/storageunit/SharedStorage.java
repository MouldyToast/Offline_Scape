package org.jesse.game.content.chambersofxeric.storageunit;

import org.jesse.game.GameInterface;
import org.jesse.game.content.chambersofxeric.Raid;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.ContainerPolicy;
import org.jesse.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.types.config.enums.Enums;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/**
 * @author Kris | 4. mai 2018 : 18:59:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class SharedStorage implements Storage {
    private final Raid raid;

    public SharedStorage(Raid raid) {
        this.raid = raid;
    }
    /**
     * The container holding all the items in this shared storage.
     */
    private final Container container = new Container(ContainerType.SHARED_STORAGE, ContainerPolicy.ALWAYS_STACK, Enums.RAIDS_ONLY_ITEMS.getSize(), Optional.empty());
    /**
     * The hashset containing all the players who have the shared storage interface currently open.
     */
    private final Set<Player> viewingPlayers = new ObjectOpenHashSet<>();

    /**
     * Opens the shared storage interface for the player.
     *
     * @param player the player who is opening the shared storage.
     */
    public void open(@NotNull final Player player) {
        GameInterface.RAIDS_SHARED_STORAGE.open(player);
    }

    @Override
    public void refresh() {
        for (final Player player : raid.getPlayers()) {
            if (player == null || player.isNulled()) {
                continue;
            }
            container.setFullUpdate(true);
            player.getPacketDispatcher().sendUpdateItemsPartial(container);
        }
    }

    @Override
    public void deposit(@NotNull final Player player, final int slotId, final int amount) {
        final Container inventory = player.getInventory().getContainer();
        final Item item = inventory.get(slotId);
        if (item == null) {
            return;
        }
        final OptionalInt field = Enums.RAIDS_ONLY_ITEMS.getKey(item.getId());
        if (field.isEmpty()) {
            player.sendMessage("The shared storage unit cannot hold that item.");
            return;
        }
        container.deposit(player, inventory, slotId, amount);
        player.getInventory().refresh();
        refresh();
    }

    public void depositFromGod(Item item, boolean refresh) {
        container.add(item);
        if(refresh)
            refresh();
    }

    @Override
    public void withdraw(@NotNull final Player player, final int slot, int amount, final boolean sendMessages) {
        if(raid.usingFakeScale) {
            Item item = container.get(slot).copy();
            item.setAmount(amount);
            depositFromGod(item, false);
        }
        container.withdraw(sendMessages ? player : null, player.getInventory().getContainer(), slot, amount);
        player.getInventory().refresh();
        refresh();
    }

    public Container getContainer() {
        return container;
    }

    public Set<Player> getViewingPlayers() {
        return viewingPlayers;
    }
}
