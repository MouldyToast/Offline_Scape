package org.jesse.game.content.minigame.castlewars;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.object.DoorHandler;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class LockpickCastleDoorAction extends Action {

    private final CastleWarsTeam team;
    private final WorldObject door;

    @Override
    public boolean start() {
        return CastleWars.getVarbit(team, CastleWarsOverlay.CWarsOverlayVarbit.DOOR_LOCK) == 0;
    }

    @Override
    public boolean process() {
        return CastleWars.getVarbit(team, CastleWarsOverlay.CWarsOverlayVarbit.DOOR_LOCK) == 0;
    }

    @Override
    public int processWithDelay() {
        if(Utils.random(5) == 0) {
            player.sendMessage("You successfully pick the lock.");
            CastleWars.setVarbit(team, CastleWarsOverlay.CWarsOverlayVarbit.DOOR_LOCK, 1);
            DoorHandler.handleDoor(door, 500, false);
        } else {
            player.sendMessage("You fail to pick the door lock.");
        }

        return -1;
    }
    
    public LockpickCastleDoorAction(CastleWarsTeam team, WorldObject door) {
        this.team = team;
        this.door = door;
    }
}
