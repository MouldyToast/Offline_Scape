package com.zenyte.game.world.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zenyte.game.world.entity.player.Player;

public class PrivateMask {

    @JsonIgnore
    protected transient Player[] viewers;

    public boolean canApply(Player player) {
        if (viewers != null) {
            for (Player p : viewers) {
                if (player.equals(p)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @JsonIgnore
    public void setViewers(Player... players) {
        this.viewers = players;
    }

}
