package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.MakeType;
import com.zenyte.plugins.dialogue.SkillDialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-23
 */
public class LobbyChest implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        var expert = player.getNumericAttribute("tombs of amascut: expert mode").intValue();
        var entry = player.getNumericAttribute("tombs of amascut: entry mode").intValue();
        var normal = player.getNumericAttribute("tombs of amascut: normal mode").intValue();

        var totalCompletions = expert + entry + normal;

        var remainingDailyClaims = player.getNumericAttributeOrDefault("daily-toa-capes-remaining", 1).intValue();

        if (totalCompletions < 10) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("There doesn't seem to be anything inside.");
                }
            });
            return;
        }

        if(remainingDailyClaims < 1) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("You would feel wrong to take any more today.");
                }
            });
            return;
        }

        var capeList = new ObjectArrayList<Item>();
            capeList.add(new Item(ItemId.ICTHLARINS_SHROUD_TIER_1));
        if (totalCompletions >= 50)
            capeList.add(new Item(ItemId.ICTHLARINS_SHROUD_TIER_2));
        if (totalCompletions >= 100)
            capeList.add(new Item(ItemId.ICTHLARINS_SHROUD_TIER_3));
        if (totalCompletions >= 150)
            capeList.add(new Item(ItemId.ICTHLARINS_SHROUD_TIER_4));
        if (totalCompletions >= 200)
            capeList.add(new Item(ItemId.ICTHLARINS_SHROUD_TIER_5));

        player.getDialogueManager().start(new SkillDialogue(player, capeList.toArray(new Item[0])) {
            @Override
            public void run(final int slotId, final int amount) {
                var item = capeList.get(slotId);
                player.getCollectionLog().add(item);
                player.getInventory().addItem(item);
                player.getAttributes().put("daily-toa-capes-remaining", remainingDailyClaims - 1);
            }
            @Override
            public void buildDialogue() {
                skill(1, MakeType.TAKE, "Select the cape to claim", items);
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 46080 };
    }
}
