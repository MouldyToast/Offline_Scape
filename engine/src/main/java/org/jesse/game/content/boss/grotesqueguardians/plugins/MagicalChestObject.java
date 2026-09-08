package org.jesse.game.content.boss.grotesqueguardians.plugins;

import org.jesse.game.GameInterface;
import org.jesse.game.content.ItemRetrievalService;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 05/08/2019 | 13:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class MagicalChestObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Search")) {
            final ItemRetrievalService service = player.getRetrievalService();
            if (service.getType() != ItemRetrievalService.RetrievalServiceType.MAGICAL_CHEST || service.getContainer().getSize() == 0) {
                player.getDialogueManager().start(new PlainChat(player, "The chest seems to be empty. If I had any of your items, but you died before collecting them from me, they would be lost."));
                return;
            }
            GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGICAL_CHEST };
    }
}
