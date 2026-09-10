package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 21 mei 2018 | 15:11:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class GrandExchangeBoothObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Bank")) {
            GameInterface.BANK.open(player);
        } else if (option.equals("Exchange")) {
            player.getGrandExchange().openOffersInterface();
        } else if (option.equals("Collect")) {
            GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player);
        } else if (option.equals("Offers Viewer")) {
            GameInterface.GRAND_EXCHANGE_OFFERS_VIEWER.open(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 30389, ObjectId.GRAND_EXCHANGE_BOOTH, ObjectId.GRAND_EXCHANGE_BOOTH_10061 };
    }
}
