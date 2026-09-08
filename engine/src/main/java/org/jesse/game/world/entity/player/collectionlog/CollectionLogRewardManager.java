package org.jesse.game.world.entity.player.collectionlog;

import org.jesse.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class CollectionLogRewardManager {
    private transient final Player player;
    private List<Integer> claimed = new ArrayList<>();
    public static final String claimInProgress = "cl-reward-claim-inprogress";

    public CollectionLogRewardManager(Player player) {
        this.player = player;
    }

    public boolean hasClaimed(int struct) {
        return claimed.contains(struct);
    }

    public boolean claim(int struct) {
        if(player.getBooleanTemporaryAttribute(claimInProgress))
            return false;
        if(claimed.contains(struct))
            return false;
        else {
            player.putBooleanTemporaryAttribute(claimInProgress, true);
            claimed.add(struct);
            return true;
        }
    }

    public void initialize(final CollectionLogRewardManager log) {
        if (log != null && log.claimed != null) {
            claimed = log.claimed;
        }
    }

    public void reset() {
        claimed.clear();
    }

}
