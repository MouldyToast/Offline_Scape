package com.zenyte.game.content.tombsofamascut;

import com.zenyte.game.content.tombsofamascut.raid.TOAPlayerLogoutState;
import com.zenyte.game.world.entity.player.container.Container;

public class TOAPlayerData {
    /*
     * Persisted under attrPersistence["toa_player_data"] (see TOAAccess).
     * Only the durable fields round-trip: the transient per-raid scratch
     * fields are reset on raid entry, and carrying them across sessions
     * would be a bug — transient enforces that structurally. Gson uses the
     * implicit no-args constructor, so field initializers run on
     * deserialize and scratch fields get the same defaults a fresh
     * instance has.
     */
    public transient int individualDeaths = 0;
    public transient boolean canClaimSupplies = true;
    public transient Container suppliesContainer = null;
    public TOAPartySettingData partySettingData = new TOAPartySettingData();
    public TOAPlayerLogoutState toaPlayerLogoutState;
    public Container rewardContainer;
    public transient int points = 0;
    public transient int damageDone = 0;
    public transient int damageTaken = 0;

}
