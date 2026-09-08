package com.zenyte.game.content.tombsofamascut.encounter;

import com.near_reality.game.content.toa.TOARewardHelper;
import com.near_reality.game.content.toa.TOARewardRoomManager;
import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import static com.zenyte.game.npc.ids.NpcId.OSMUMTEN_11693;

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
