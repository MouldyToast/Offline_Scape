package org.jesse.game.content.follower.impl;

import org.jesse.game.content.follower.Follower;
import org.jesse.game.content.follower.Pet;
import org.jesse.game.content.follower.PetWrapper;
import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.broadcasts.BroadcastType;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.dialogue.followers.BloodHoundD;
import org.jesse.plugins.dialogue.followers.ChompyChickD;
import org.jesse.plugins.dialogue.followers.HerbiD;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Arrays;

/**
 * @author Tommeh | 23-11-2018 | 18:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum MiscPet implements Pet {
    //TODO kittens and other variations of these.
    HERBI(21509, 7760, HerbiD.class),
    CHOMPY_CHICK(13071, 4002, ChompyChickD.class),
    BLOODHOUND(19730, 7232, BloodHoundD.class),
    TOY_CAT(14924, 2782),
    OVERGROWN_HELLCAT(7581, 5604),
    WILY_HELLCAT(7585, 5590),
    BLUEFISH(6670, -1),
    GREENFISH(6671, -1),
    SPINEFISH(6672, -1),
    CAT_1(1561, 1619),
    CAT_2(1562, 1620),
    CAT_3(1563, 1621),
    CAT_4(1564, 1622),
    CAT_5(1565, 1623),
    CAT_6(1566, 1624);

    public static final MiscPet[] VALUES = values();
    private final int itemId;
    private final int petId;
    private final Class<? extends Dialogue> dialogue;
    /* Do not re-arrange the above pets */
    MiscPet(final int itemId, final int petId) {
        this(itemId, petId, null);
    }
    MiscPet(final int itemId, final int petId, final Class<? extends Dialogue> dialogue) {
        this.itemId = itemId;
        this.petId = petId;
        this.dialogue = dialogue;
    }
    public static final Int2ObjectOpenHashMap<MiscPet> PETS_BY_ITEM_ID = new Int2ObjectOpenHashMap<>(VALUES.length);

    @Override
    public int itemId() {
        return itemId;
    }

    @Override
    public int petId() {
        return petId;
    }

    @Override
    public String petName() {
        return name();
    }

    @Override
    public boolean hasPet(final Player player) {
        final int petItemId = getItemId();
        if (player.containsItem(petItemId)) {
            return true;
        }
        return PetWrapper.checkFollower(player) && player.getFollower().getPet().petId() == getPetId();
    }

    @Override
    public Class<? extends Dialogue> dialogue() {
        return dialogue;
    }

    public boolean roll(final Player player, int rarity) {
        if (this != BLOODHOUND || rarity == -1 || Utils.random(rarity) != 0) {
            return false;
        }
        final Item item = new Item(itemId);
        player.getCollectionLog().add(item);
        if (hasPet(player)) {
            player.sendMessage("<col=ff0000>You have a funny feeling like you would have been followed...</col>");
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
            return false;
        }
        if (player.getFollower() != null) {
            if (player.getInventory().addItem(item).isFailure()) {
                if (player.getBank().add(item).isFailure()) {
                    player.sendMessage("There was not enough space in your bank, and therefore the pet was lost.");
                    return false;
                }
                player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed - The pet has " +
                    "been added to your bank.</col>");
                return true;
            }
            player.sendMessage("<col=ff0000>You feel something weird sneaking into your backpack.</col>");
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
        }
        else {
            player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.</col>");
            player.setFollower(new Follower(petId, player));
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
        }
        return true;
    }

    public static MiscPet getByItem(final int itemId) {
        return PETS_BY_ITEM_ID.get(itemId);
    }

    static {
        Arrays.stream(VALUES).forEach(pet -> PETS_BY_ITEM_ID.put(pet.itemId, pet));
    }

    public int getItemId() {
        return itemId;
    }

    public int getPetId() {
        return petId;
    }

    public Class<? extends Dialogue> getDialogue() {
        return dialogue;
    }
}
