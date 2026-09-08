package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.content.minigame.castlewars.CastleWars;
import org.jesse.game.content.minigame.castlewars.CastleWarsOverlay.CWarsOverlayVarbit;
import org.jesse.game.content.minigame.castlewars.CastleWarsTeam;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

import static org.jesse.plugins.itemonobject.ItemOnCastleWarsCatapult.*;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsCatapult implements ObjectAction {
    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final boolean isSaradomin = CastleWars.getTeam(player).equals(CastleWarsTeam.SARADOMIN);
        final boolean saradominObject = object.getId() == SARADOMIN_CATAPULT.getId() || object.getId() == BROKEN_SARADOMIN_CATAPULT.getId();
        if (option.toLowerCase().equals("repair")) {
            if (!player.getInventory().containsItem(CastleWars.TOOLKIT)) {
                player.sendMessage("You need a toolkit to repair the catapult.");
                return;
            }
            // check if other team is attempting to repair the catapult
            if (!isSaradomin && saradominObject || isSaradomin && !saradominObject) {
                player.sendMessage("You don\'t want to repair the other team\'s catapult.");
                return;
            }
            player.getInventory().deleteItem(CastleWars.TOOLKIT.getId(), 1);
            World.removeObject(saradominObject ? BROKEN_SARADOMIN_CATAPULT : BROKEN_ZAMORAK_CATAPULT);
            World.spawnObject(saradominObject ? SARADOMIN_CATAPULT : ZAMORAK_CATAPULT);
            CastleWars.setVarbit(CastleWars.getTeam(player), CWarsOverlayVarbit.CATAPULT, 0);
            return;
        }
        if (option.toLowerCase().equals("operate")) {
            if (!player.getInventory().containsItem(CastleWars.CATAPULT_ROCK)) {
                player.sendMessage("You need rocks to launch from the catapult.");
                return;
            }
            if (isSaradomin && !saradominObject || !isSaradomin && saradominObject) {
                player.sendMessage("You cannot use the other team\'s catapult.");
                return;
            }
            GameInterface.CASTLE_WARS_CATAPULT.open(player);
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {SARADOMIN_CATAPULT.getId(), BROKEN_SARADOMIN_CATAPULT.getId(), ZAMORAK_CATAPULT.getId(), BROKEN_ZAMORAK_CATAPULT.getId()};
    }
}
