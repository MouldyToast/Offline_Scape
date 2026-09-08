package org.jesse.game.content.minigame.castlewars;

import org.jesse.game.content.minigame.castlewars.CastleWarsOverlay.CWarsOverlayVarbit;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Action;
import org.jesse.plugins.object.CastleWarsLargeDoor;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class AttackCastleDoorAction extends Action {
    private final CastleWarsTeam team;

    @Override
    public boolean start() {
        return CastleWars.getVarbit(team, CWarsOverlayVarbit.DOOR_HEALTH) != 0;
    }

    @Override
    public boolean process() {
        return CastleWars.getVarbit(team, CWarsOverlayVarbit.DOOR_HEALTH) != 0;
    }

    @Override
    public int processWithDelay() {
        // currently set as a random amount between 1-15, OSRS showed a varied hit amount, seemingly based on weapon wielded. combat skill levels do not seem to factor in.
        CastleWars.setVarbit(team, CWarsOverlayVarbit.DOOR_HEALTH, Math.max(0, CastleWars.getVarbit(team, CWarsOverlayVarbit.DOOR_HEALTH) - Utils.random(1, 15)));
        player.setAnimation(new Animation(player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle())));
        // This is the code to process destroying the doors
        if (CastleWars.getVarbit(team, CWarsOverlayVarbit.DOOR_HEALTH) == 0) {
            final boolean saradomin = team.equals(CastleWarsTeam.SARADOMIN);
            World.removeObject(saradomin ? CastleWarsLargeDoor.SARADOMIN_DOOR : CastleWarsLargeDoor.ZAMORAK_DOOR);
            World.removeObject(saradomin ? CastleWarsLargeDoor.SARADOMIN_DOOR2 : CastleWarsLargeDoor.ZAMORAK_DOOR2);
            World.spawnObject(saradomin ? CastleWarsLargeDoor.BROKEN_SARADOMIN_DOOR : CastleWarsLargeDoor.BROKEN_ZAMORAK_DOOR);
            World.spawnObject(saradomin ? CastleWarsLargeDoor.BROKEN_SARADOMIN_DOOR2 : CastleWarsLargeDoor.BROKEN_ZAMORAK_DOOR2);
        }
        return 2;
    }

    public AttackCastleDoorAction(CastleWarsTeam team) {
        this.team = team;
    }
}
