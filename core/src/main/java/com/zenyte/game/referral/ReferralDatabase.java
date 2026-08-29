package com.zenyte.game.referral;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-30
 */
public class ReferralDatabase extends ReferralSerializer {

    private final List<Referral> referrals;

    public ReferralDatabase() {
        referrals = new ArrayList<>();
    }

    private static final class InstanceHolder {
        private static final ReferralDatabase instance = new ReferralDatabase();
    }

    public static ReferralDatabase getInstance() {
        return InstanceHolder.instance;
    }

    public List<Referral> getReferrals() {
        return referrals;
    }

    public void addReferral(Referral referral) {
        referrals.add(referral);
    }

    public void removeReferral(Referral referral) {
        referrals.remove(referral);
    }

    public void setReferrals() {
        write();
    }

    public void replaceReferral(Referral referral) {
        var existingReferral = getReferral(referral.referral());
        if (existingReferral != null)
            referrals.remove(existingReferral);
        referrals.add(referral);
    }

    public Referral getReferral(String referral) {
        return referrals.stream()
            .filter(r -> r.referral().equals(referral))
            .findFirst()
            .orElse(null);
    }

    public boolean referralClaimed(Player player) {
        var database = ReferralDatabase.getInstance();
        var referrals = database.getReferrals();
        var found = false;
        for (var referral : referrals) {
            var UUID = player.getPlayerInformation().getUUID();
            if (referral.referredPlayerMap().containsValue(UUID)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public void handleReferral(Player player, String name) {
        var database = ReferralDatabase.getInstance();
        // if the referral already exists, add to the list that was referred
        if (database.getReferral(name) != null) {
            var cached = database.getReferral(name);
                cached.putPlayer(player);
            database.replaceReferral(cached);
            database.write();
            return;
        }
        // else this is a new referral
        var referral = new Referral();
            referral.withReferral(name);
            referral.putPlayer(player);
        database.replaceReferral(referral);
        database.write();
    }

    public void handleReward(Player player, String name) {
        switch (name.toLowerCase()) {
            case "wizard",
                 "wetwizard",
                 "wet wizard",
                 "cursed out",
                 "vihtic",
                 "bonkloots",
                 "fewb",
                 "artz",
                 "effigy",
                 "eggy",
                 "lano",
                 "lanors",
                 "sohan" -> player.getInventory().addOrDrop(new Item(ItemId.MYSTERY_BOX, 1));
        }
    }

}
