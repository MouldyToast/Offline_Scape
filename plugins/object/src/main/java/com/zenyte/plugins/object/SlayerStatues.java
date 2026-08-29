package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.slayer.Slayer;
import com.zenyte.game.content.skills.slayer.SlayerMountType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import static com.near_reality.game.item.CustomObjectId.*;
import static com.zenyte.game.item.ItemId.*;

@SuppressWarnings("unused")
public class SlayerStatues implements ObjectAction, ItemOnObjectAction {

    private static final Animation DISMOUNT_ANIM = new Animation(3132);
    private static final Animation MOUNT_ANIM = new Animation(3071);
    private static final Item IMBUED_HELM = new Item(SLAYER_HELMET_I);

    private void openInfoDialogue(Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                var slayerMountType = SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR));
                if (slayerMountType != null && slayerMountType.getItem() != null)
                    item(slayerMountType.getItem(), "You have currently mounted the %s helmet.".formatted(slayerMountType.getItem().getName()));
                else
                    item(IMBUED_HELM, "You can mount this statue with your Imbued Slayer Helmet, which will allow you to receive the Slayer Helm boost for your tasks without having to wear it.");
            }
        });
    }

    private void removeSlayerHelmet(Player player) {
        final var slayerMountType = SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR));
        if (slayerMountType == null) return;

        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need to have some empty space in your inventory to dismount the helmet.");
            return;
        }
        final var helmet = slayerMountType.getItem();
        if (helmet == null) return;

        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Do you want to dismount your current Slayer Helmet?",
                    new DialogueOption("Yes.", () -> {
                        player.lock(1);
                        player.setAnimation(DISMOUNT_ANIM);
                        WorldTasksManager.schedule(() -> {
                            player.getInventory().addItem(helmet);
                            player.getVarManager().sendVar(Slayer.SLAYER_STATUES_VAR, SlayerMountType.NONE.getIndex());
                        });
                    }),
                    new DialogueOption("No."));
            }
        });
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Information"))
            openInfoDialogue(player);
        else if (option.equalsIgnoreCase("Dismount"))
            removeSlayerHelmet(player);
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        SlayerMountType slayerMountType = SlayerMountType.helmToType.get(item.getId());
        if (slayerMountType == null) {
            return;
        }

        SlayerMountType currentSlayerMountType = SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR));
        if (currentSlayerMountType != null && currentSlayerMountType.getItem() != null) {
            player.sendMessage("You cannot mount another helmet unless you dismount your current one.");
            return;
        }

        if (!player.getInventory().containsItem(slayerMountType.getItem())) {
            return;
        }

        player.lock(2);
        player.setAnimation(MOUNT_ANIM);
        WorldTasksManager.schedule(() -> {
            player.getInventory().deleteItem(slayerMountType.getItem());
            player.getVarManager().sendVar(Slayer.SLAYER_STATUES_VAR, slayerMountType.getIndex());
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(slayerMountType.getItem(), "You mount your " + slayerMountType.getItem().getName() + " to your statue.");
                }
            });
        }, 1);
    }

    @Override
    public Object[] getItems() {
        return new Object[] {
            RED_SLAYER_HELMET_I,
            HYDRA_SLAYER_HELMET_I,
            TZTOK_SLAYER_HELMET_I,
            GREEN_SLAYER_HELMET_I,
            BLACK_SLAYER_HELMET_I,
            TWISTED_SLAYER_HELMET_I,
            SLAYER_HELMET_I,
            PURPLE_SLAYER_HELMET_I,
            TURQUOISE_SLAYER_HELMET_I,
            VAMPYRIC_SLAYER_HELMET_I,
            TZKAL_SLAYER_HELMET_I,
            ARAXYTE_SLAYER_HELMET_I
        };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
            SLAYER_HELM_STAND,
            SLAYER_HELM_STAND_EMPTY,
            SLAYER_HELM_STAND_ABYSSAL,
            SLAYER_HELM_STAND_HYDRA,
            SLAYER_HELM_STAND_JAD,
            SLAYER_HELM_STAND_KALPHITE,
            SLAYER_HELM_STAND_KBD,
            SLAYER_HELM_STAND_OLM,
            SLAYER_HELM_STAND_REGULAR,
            SLAYER_HELM_STAND_SKOTIZO,
            SLAYER_HELM_STAND_VORKY,
            SLAYER_HELM_STAND_VERZIK,
            SLAYER_HELM_STAND_ZUK,
            SLAYER_HELM_STAND_ARAXYTE
        };
    }

}
