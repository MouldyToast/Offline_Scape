package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.tombsofamascut.encounter.RewardEncounter;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.31.2025
 */
public class TOAChestAction implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if(player.getTOAManager().getRaidParty() instanceof TOARaidParty party && party.getCurrentRaidArea() instanceof RewardEncounter encounter)
            encounter.forwardObject(player, object);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                46217, /* 6 */
                44545, /* 2 */
                46215, /* 4 */
                46219, /* 8 */
                46218, /* 7 */
                44547, /* 3 */
                29994, /* 1 */
                46216, /* 5  */
                46220, /* Closed Sarcophagus */
                44934, /* Open Purple Sarcophagus */
                44826  /* Unknown Sarcophagus */
        };
    }
}
