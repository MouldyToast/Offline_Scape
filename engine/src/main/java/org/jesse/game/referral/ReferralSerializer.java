package org.jesse.game.referral;

import org.jesse.cores.ScheduledExternalizable;
import org.jesse.logger.NearRealityLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.Arrays;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-30
 */
public class ReferralSerializer implements ScheduledExternalizable {
    @Override
    public Logger getLog() {
        return NearRealityLogger.getLogger(ReferralSerializer.class);
    }

    @Override
    public int writeInterval() {
        return 0;
    }

    @Override
    public void read(@NotNull BufferedReader reader) {
        var referralDatabase = ReferralDatabase.getInstance();
        var referrals = getGSON().fromJson(reader, Referral[].class);
        Arrays.stream(referrals).forEach(referralDatabase::addReferral);
    }

    @Override
    public void write() {
        var referralDatabase = ReferralDatabase.getInstance();
        out(getGSON().toJson(referralDatabase.getReferrals()));
    }

    @Override
    public String path() {
        return "data/referrals/referrals.json";
    }

    @Override
    public void ifFileNotFoundOnRead() {
        write();
    }
}
