package org.jesse.game.content.wheeloffortune;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.ContainerPolicy;
import org.jesse.game.world.entity.player.container.impl.ContainerType;
import org.jesse.plugins.events.InitializationEvent;
import org.jesse.plugins.item.mysteryboxes.MysteryBox;
import org.jesse.plugins.item.mysteryboxes.MysteryItem;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Tommeh | 27/02/2020 | 20:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class WheelOfFortune {

    private final transient Player player;
    private final Container container;
    private int spins;
    private boolean pendingReward;

    public WheelOfFortune(final Player player) {
        this.player = player;
        container = new Container(ContainerPolicy.NORMAL, ContainerType.SHOP, Optional.of(player));
    }

    @Subscribe
    public static final void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final WheelOfFortune wheel = savedPlayer.getWheelOfFortune();
        if (wheel == null) {
            return;
        }
        final WheelOfFortune thisWheel = player.getWheelOfFortune();
        if (wheel.container != null) {
            thisWheel.container.setContainer(wheel.container);
        }
        thisWheel.spins = wheel.spins;
        thisWheel.pendingReward = wheel.pendingReward;
    }

    public void roll() {
        container.clear();
        int count = 1000;
        while (--count > 0 && container.getSize() < 24) {
            final MysteryItem item = MysteryItem.generateItem(player, MysteryBox.rewards, MysteryBox.totalWeight);
            if (!container.contains(item.getId(), 1)) {
                container.add(new Item(item.getId(), Utils.random(item.getMinAmount(), item.getMaxAmount())));
            }
        }
    }

    public void spin() {
        if (spins <= 0) {
            player.sendMessage("You don't have enough spins to do that right now.");
            return;
        }
        spins--;
        pendingReward = true;
        refreshSpins();
    }

    public void claim(final boolean bank) {
        if (!pendingReward) {
            return;
        }
        final Item prize = getPrize();
        if (prize == null) {
            return;
        }
        if (bank) {
            if (!player.getBank().checkSpace()) {
                return;
            }
            pendingReward = false;
            GameInterface.WHEEL_OF_FORTUNE.open(player);
            player.getBank().add(prize);
        } else {
            if (!player.getInventory().checkSpace()) {
                return;
            }
            pendingReward = false;
            GameInterface.WHEEL_OF_FORTUNE.open(player);
            player.getInventory().addItem(prize);
        }
    }

    public void refreshSpins() {
        player.getVarManager().sendVar(3622, spins);
    }

    @NotNull
    public Item getPrize() {
        return container.get(15);
    }

    public Container getContainer() {
        return container;
    }

    public int getSpins() {
        return spins;
    }

    public void setSpins(int spins) {
        this.spins = spins;
    }

    public boolean isPendingReward() {
        return pendingReward;
    }

    public void setPendingReward(boolean pendingReward) {
        this.pendingReward = pendingReward;
    }
}
