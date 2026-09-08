package org.jesse.game.content.tombsofamascut;

import org.jesse.game.content.tombsofamascut.raid.TOAPlayerLogoutState;
import org.jesse.game.world.entity.player.container.Container;

public class TOAPlayerData {
    public int individualDeaths = 0;
    public boolean canClaimSupplies = true;
    public Container suppliesContainer = null;
    public TOAPartySettingData partySettingData = new TOAPartySettingData();
    public TOAPlayerLogoutState toaPlayerLogoutState;
    public Container rewardContainer;
    public int points = 0;
    public int damageDone = 0;
    public int damageTaken = 0;

}
