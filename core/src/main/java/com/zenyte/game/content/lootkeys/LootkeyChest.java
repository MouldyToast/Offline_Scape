package com.zenyte.game.content.lootkeys;

import com.zenyte.game.GameInterface;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import kotlin.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import static com.zenyte.game.content.lootkeys.LootkeyConstants.LOOT_KEY_ORDER;

public class LootkeyChest implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Loot")) {
            if (LootkeySettingsKeys.lootkeySettings(player) == null) {
                player.getDialogueManager().start(new Dialogue(player, 10382) {
                    @Override
                    public void buildDialogue() {
                        npc("You might want to speak to myself first, mate.");
                    }
                });
                return;
            }

            boolean containsItems = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest() != null;
            if (containsItems && LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest().isEmpty()) {
                LootkeySettings.clear(player);
                containsItems = LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest() != null;
            }

            if (!containsItems) {
                var amountOfKeys = player.getInventory().getAmountOf(LOOT_KEY_ORDER);
                if (amountOfKeys == 0) {
                    player.getDialogueManager().start(new PlainChat(player, "You do not currently have any Wilderness Loot Keys."));
                    return;
                }
                else { // What to do when we have more than 1 key...
                    player.getDialogueManager().start(selectKey(player));
                }
            }
            else {
                openInterface(player);
            }
        }
    }

    private static Dialogue selectKey(Player player) {
        return new Dialogue(player) {
            @Override
            public void buildDialogue() {
                var keys = player.getInventory().getContainer().findAllByIds(LootkeyConstants.LOOT_KEY_ORDER);
                var keysToIndex = keys.values().stream().map(item -> Arrays.binarySearch(LOOT_KEY_ORDER, item.getId())).collect(Collectors.toUnmodifiableSet());
                var values = keysToIndex.stream().map(index -> new Pair<>(index, LootkeySettingsKeys.lootkeySettings(player).getContainer(index)))
                        .sorted(Comparator.comparingInt(Pair::getFirst)).toArray(Pair[]::new);

                var options = new String[values.length];
                for (int i = 0; i < options.length; i++) {
                    var pair = values[i];

                    var key = (Integer) pair.getFirst();
                    var container = (Container) pair.getSecond();

                    options[i] = "Key " + (key + 1) + " - " + Utils.formatNumberWithCommas(container.calculateValue());
                }

                if (options.length == 1) {
                    var pair = values[0];
                    claimLootkey(player, (Integer) pair.getFirst());
                } else {
                    options("Which loot key would you like to use?", options).setOptions(
                            Arrays.stream(values).map(pair -> (Runnable) () ->
                                    claimLootkey(player, (Integer) pair.getFirst())).toArray(Runnable[]::new)
                    );
                }
            }
        };
    }


    private static void claimLootkey(Player player, int itemSlotOrder) {
        if (LootkeySettingsKeys.lootkeySettings(player).getCurrentItemsInChest() != null) {
            return;
        }

        Analytics.flagInteraction(player, Analytics.InteractionType.LOOT_CHEST);
        player.setAnimation(new Animation(832));

        var key = LOOT_KEY_ORDER[itemSlotOrder];
        var container = LootkeyConstants.getContainer(player, key);
        if (container == null || container.isEmpty()) {
            player.getInventory().deleteItem(key, 1);
            player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
            return;
        }

        var result = player.getInventory().deleteItem(key, 1);
        if (result.getSucceededAmount() != 1) {
            return;
        }

        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);

        LootkeySettingsKeys.lootkeySettings(player).setCurrentItemsInChest(container.getItems());
        LootkeySettingsKeys.lootkeySettings(player).incrementKeysClaimed();

        LootkeySettings.sendOpenChest(player);
        WorldTasksManager.schedule(() -> openInterface(player));
    }


    private static void openInterface(Player player) {
        GameInterface.WILDERNESS_LOOT_KEY.open(player);
    }


    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.LOOT_CHEST, ObjectId.LOOT_CHEST_43469, ObjectId.LOOT_CHEST_43484,
                ObjectId.LOOT_CHEST_43485, 43470};
    }
}
