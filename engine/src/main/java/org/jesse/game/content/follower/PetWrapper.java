package org.jesse.game.content.follower;

import org.jesse.game.content.follower.impl.BossPet;
import org.jesse.game.content.follower.impl.MiscPet;
import org.jesse.game.content.follower.impl.SkillingPet;
import org.jesse.game.content.follower.plugin.Probita;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;

import static org.jesse.game.npc.ids.NpcId.*;

/**
 * @author Tommeh | 23-11-2018 | 18:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PetWrapper {
    private static final Logger log = NearRealityLogger.getLogger(PetWrapper.class);
    public static final Int2ObjectOpenHashMap<Pet> PETS_BY_ITEM_ID = new Int2ObjectOpenHashMap<>(BossPet.VALUES.length + SkillingPet.VALUES.length + MiscPet.VALUES.length);
    public static final Int2ObjectOpenHashMap<Pet> PETS_BY_PET_ID = new Int2ObjectOpenHashMap<>(BossPet.VALUES.length + SkillingPet.VALUES.length + MiscPet.VALUES.length);
    public static final Animation DROP_ANIMATION = new Animation(827);

    static {
        for (final BossPet pet : BossPet.VALUES) {
            PETS_BY_ITEM_ID.put(pet.getItemId(), pet);
            PETS_BY_PET_ID.put(pet.getPetId(), pet);
        }
        for (final SkillingPet pet : SkillingPet.VALUES) {
            PETS_BY_ITEM_ID.put(pet.getItemId(), pet);
            PETS_BY_PET_ID.put(pet.getPetId(), pet);
        }
        for (final MiscPet pet : MiscPet.VALUES) {
            PETS_BY_ITEM_ID.put(pet.getItemId(), pet);
            PETS_BY_PET_ID.put(pet.getPetId(), pet);
        }
        PETS_BY_PET_ID.put(6717, SkillingPet.BEAVER);
    }

    public static Pet getByItem(final int itemId) {
        return PETS_BY_ITEM_ID.get(itemId);
    }

    public static Pet getByPet(final int petId) {
        return PETS_BY_PET_ID.get(petId);
    }

    private static final Class[] invocationClasses = new Class[] {Player.class, NPC.class};

    public static boolean checkFollower(final Player player) {
        return player.getFollower() != null && player.getFollower().getPet() != null;
    }

    public static int fromInventory(final Player player) {
        for (int slot = 0; slot < 28; slot++) {
            final Item item = player.getInventory().getItem(slot);
            if (item == null) {
                continue;
            }
            final int itemId = item.getId();
            if (Probita.insurablePets.containsKey(itemId) && !player.getPetInsurance().isInsured(Probita.insurablePets.get(itemId))) {
                return itemId;
            }
        }
        return -1;
    }

    @SuppressWarnings("rawtypes")
    public static void startDialogue(final Player player, final NPC npc, final Class<? extends Dialogue> dialogue) {
        try {
            if (dialogue != null) {
                final Dialogue d = dialogue.getDeclaredConstructor(invocationClasses).newInstance(player, npc);
                player.getDialogueManager().start(d);
            } else {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        if(npc.getId() == NID || npc.getId() == RAX ||
                           npc.getId() == NID_13683 || npc.getId() == RAX_13684) {
                            player("Perhaps I shouldn't be talking to a spider...");
                            return;
                        }
                        npc("*Chirp* *Chirp*");
                        player("Huh?");
                    }
                });
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }
}
