package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.farming.BasketData;
import org.jesse.game.content.skills.farming.actions.FillBasket;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class FillBasketD extends SkillDialogue {

    private final Item clickedItem;

    public FillBasketD(final Player player, final Item click, final Item... items) {
        super(player, "What would you like to fill your basket with?", items);
        this.clickedItem = click;
    }

    @Override
    public void run(int slotId, int amount) {
        final BasketData data = BasketData.getBasket(items[slotId].getId());
        if(data != null) {
            player.getActionManager().setAction(new FillBasket(amount, data, clickedItem));
        }
    }
}
