package org.jesse.game.content.tombsofamascut.encounter;

import org.jesse.game.content.toa.TOARewardHelper;
import org.jesse.game.content.toa.TOARewardRoomManager;
import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.content.tombsofamascut.raid.EncounterType;
import org.jesse.game.content.tombsofamascut.raid.TOARaidArea;
import org.jesse.game.content.tombsofamascut.raid.TOARaidParty;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.VarManager;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;

import static org.jesse.game.npc.ids.NpcId.OSMUMTEN_11693;

/**
 * @author Savions
 */
public class RewardEncounter extends TOARaidArea {
    private TOARewardRoomManager roomManager;

    static {
        VarManager.appendPersistentVarbit(14319);
    }

    public RewardEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
        super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
    }

    @Override
    public void constructed() {
        super.constructed();
        new Spirit(getLocation(encounterType.getNpcLocation())).spawn();
        roomManager = new TOARewardRoomManager(party, this);
        TOARewardHelper.rollGroupLoot(roomManager, party);
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        roomManager.onLeave(player);
        roomManager.giveItemsToPlayerUponLeaving(player);
    }

    @Override
    public void destroyRegion() {
        roomManager.onReset();
        super.destroyRegion();
    }

    @Override
    public void onRoomStart() {

    }

    @Override
    public void onRoomEnd() {

    }

    @Override
    public void onRoomReset() {

    }

    public void forwardObject(Player player, WorldObject object) {
        roomManager.handleObject(player, object);
    }

    static class Spirit extends NPC {

        public Spirit(Location tile) {
            super(OSMUMTEN_11693, tile, Direction.NORTH, 0);
        }

        @Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

        @Override public void setTarget(Entity target, TargetSwitchCause cause) { }
    }

}
