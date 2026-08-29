package com.zenyte.game.content.wildernessVault.reward;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.wildernessVault.WildernessVaultHandler;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zenyte.game.content.wildernessVault.WildernessVaultConstants.*;

public class WildernessVaultRewardHandler {

    private final Player player;
    private final ObjectArrayList<VaultRewardItem> loot;
    private final Container container;
    private boolean looted;

    private final VaultRoomInstance instance;

    public WildernessVaultRewardHandler(Player player) throws OutOfSpaceException {
        this.player = player;
        var rolls = 3;
        var mvps = getTopThree();
        var players = WildernessVaultHandler.getInstance().getAllPlayers();
        // if this player is the MvP, give an extra rolls
        if (mvps.stream().findFirst().isPresent()) {
            var mvp = mvps.stream().findFirst().get();
            if (mvp.getName().equals(player.getName())) {
                mvp.sendMessage(Colour.RED.wrap("You are the MVP! You have been awarded an extra roll!"));
                players.forEach(participant -> participant.sendMessage(Colour.RED.wrap(player.getName() + " is the MVP!")));
                rolls += 1;
            }
        }
        // if this player is in the top 3, give an extra roll
        if (mvps.contains(player)) {
            player.sendMessage(Colour.RED.wrap("You were in the top 3! You have been awarded an extra roll!"));
            rolls += 1;
        }

        if(player.remnantPetManager.checkDoubleDropsWildy()) {
            player.sendMessage(Colour.GREEN.wrap("Your pet has granted you an extra roll!"));
            rolls += 1;
        }

        this.loot = getLoot(rolls);
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.THEATRE_OF_BLOOD, Optional.of(player));
        loot.stream()
            .filter(Objects::nonNull)
            .map(r -> new Item(r.getId(), Utils.random(r.getMin(), r.getMax())))
            .forEach(container::add);
        final AllocatedArea area = MapBuilder.findEmptyChunk(64, 64);
        this.instance = new VaultRoomInstance(area);
        this.instance.constructRegion();
    }

    private Collection<Player> getTopThree() {
        var players = WildernessVaultHandler.getInstance().getAllPlayers();
        return players.stream()
            .sorted(Comparator.comparingInt(player ->
                ((Player) player).getNumericAttribute("WILDERNESS_VAULT_DAMAGE_COUNTER").intValue()).reversed()
            ).limit(3)
            .collect(Collectors.toList());

    }

    public void onEnterRoom() {
        player.setLocation(instance.getLocation(CHEST_ENTRANCE));
        World.spawnObject(new WorldObject(rare(loot) ? RARE_CHEST : CHEST, 10, 6, instance.getLocation(CHEST_SPAWN)));
    }

    public Container getContainer() {
        return container;
    }

    public boolean isLooted() {
        return looted;
    }

    public void lootChest() {
        if (!WildernessVaultHandler.getLooted().contains(player.getName()))
            WildernessVaultHandler.getLooted().add(player.getName());

        player.getPacketDispatcher().sendUpdateItemContainer(container);
        GameInterface.WILDERNESS_VAULT_REWARDS.open(player);
        if (!looted) {
            var rareChest = new WorldObject(RARE_CHEST_OPEN, 10, 6, instance.getLocation(CHEST_SPAWN));
            var normChest = new WorldObject(CHEST_OPEN, 10, 6, instance.getLocation(CHEST_SPAWN));

            World.replaceObject(
                World.getObjectWithType(CHEST_SPAWN.copy().moveLocation(0, 0, player.getIndex() * 4), 10),
                    new WorldObject(rare(loot) ? rareChest : normChest));

            loot.stream()
                .filter(VaultRewardItem::isRare)
                .forEach(r ->
                    WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, new Item(r.getId()), "Wilderness vault")
                );
        }

        looted = true;
    }

    /**
     * Adds the loot to the player's inventory, or drops it under them. Refreshes the containers.
     */
    public void addLoot() {
        if (container.isEmpty()) return;
        final Container inventory = player.getInventory().getContainer();
        container.getItems().int2ObjectEntrySet().fastForEach(entry -> {
            player.getCollectionLog().add(entry.getValue());
            final boolean addToRunePouch;
            if (player.getInventory().containsAnyOf(RunePouch.POUCHES)) {
                final int amountInRunePouch = player.getRunePouch().getAmountOf(entry.getValue().getId());
                addToRunePouch = amountInRunePouch > 0 && (amountInRunePouch + entry.getValue().getAmount()) < 16000;
            }
            else
                addToRunePouch = false;

            final boolean addToQuiver = (player.getEquipment().getId(EquipmentSlot.AMMUNITION) == entry.getValue().getId() || (entry.getValue().isStackable() && player.getEquipment().getId(EquipmentSlot.WEAPON) == entry.getValue().getId()));
            final Container container = addToQuiver ? player.getEquipment().getContainer() : addToRunePouch ? player.getRunePouch().getContainer() : inventory;
            container.add(entry.getValue()).onFailure(remainder -> World.spawnFloorItem(remainder, player));
        });
        player.getRunePouch().getContainer().refresh(player);
        player.getEquipment().getContainer().refresh(player);
        inventory.refresh(player);
        container.refresh(player);
        container.clear();
    }

    public static ObjectArrayList<VaultRewardItem> getLoot(int amt) {
        ObjectArrayList<VaultRewardItem> loot = new ObjectArrayList<>();
        for (int i = 0; i < amt; i++)
            loot.add(VaultRewardItem.REWARDS.rollItem());
        VaultRewardItem.ALWAYS.forEach(loot::add);
        return loot;
    }

    public static boolean rare(ObjectArrayList<VaultRewardItem> loot) {
        return loot.stream().anyMatch(VaultRewardItem::isRare);
    }




}
