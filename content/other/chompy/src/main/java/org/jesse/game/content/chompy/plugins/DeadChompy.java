package org.jesse.game.content.chompy.plugins;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.content.follower.Follower;
import org.jesse.game.content.follower.PetWrapper;
import org.jesse.game.content.follower.impl.MiscPet;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.broadcasts.BroadcastType;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;

public class DeadChompy extends NPCPlugin {
    public static final int DEAD_CHOMPY_ID = 1476;

    @Override
    public void handle() {
        bind("Pluck", (player, npc) -> {
            final Inventory inventory = player.getInventory();
            if (inventory.checkSpace()) {
                inventory.addItem(ItemId.FEATHER, Utils.random(1, 15));
            }
            World.spawnFloorItem(new Item(ItemId.RAW_CHOMPY), player, npc.getLocation());
            World.spawnFloorItem(new Item(ItemId.BONES), player, npc.getLocation());
            if (DiaryUtil.eligibleFor(DiaryReward.WESTERN_BANNER4, player)) {
                roll(player, 499);
            }
            npc.finish();
        });
    }

    public static boolean roll(final Player player, int rarity) {
        if (rarity == -1 || Utils.random(rarity) != 0) {
            return false;
        }
        final MiscPet pet = MiscPet.CHOMPY_CHICK;
        final Item item = new Item(pet.getItemId());
        player.getCollectionLog().add(item);
        if ((PetWrapper.checkFollower(player) && player.getFollower().getPet().equals(pet)) || player.containsItem(pet.getItemId())) {
            player.sendMessage("<col=ff0000>You have a funny feeling like you would have been followed...</col>");
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
                return false;
            }
            player.sendMessage("<col=ff0000>You feel something weird sneaking into your backpack.</col>");
            WorldBroadcasts.broadcast(player, BroadcastType.PET, pet);
        } else {
            player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.</col>");
            player.setFollower(new Follower(pet.getPetId(), player));
            WorldBroadcasts.broadcast(player, BroadcastType.PET, pet);
        }
        return true;
    }

    @Override
    public int[] getNPCs() {
        return new int[] {DEAD_CHOMPY_ID};
    }
}
