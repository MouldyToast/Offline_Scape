package com.zenyte.game.content.scar_essence_mine.dialogue;

import com.zenyte.game.content.scar_essence_mine.action.ExtractOrificeAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

import static com.zenyte.game.item.ids.ItemId.*;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/19/2025
 */
public class ExtractOrificeActionD extends SkillDialogue {

    public ExtractOrificeActionD(Player player) {
        super(player, "Which extract should be created?", new Item(WARPED_EXTRACT), new Item(TWISTED_EXTRACT), new Item(MANGLED_EXTRACT), new Item(SCARRED_EXTRACT));
    }

    @Override
    public void run(int slotId, int amount) {
        var item = getSlotItem(slotId);
        player.getActionManager().setAction(new ExtractOrificeAction(player, item, amount));
    }

    private Item getSlotItem(int slotId) {
        return switch (slotId) {
            case 0 -> new Item(WARPED_EXTRACT);
            case 1 -> new Item(TWISTED_EXTRACT);
            case 2 -> new Item(MANGLED_EXTRACT);
            case 3 -> new Item(SCARRED_EXTRACT);
            default -> throw new IllegalStateException("Unexpected value: " + slotId);
        };
    }
}
