package com.zenyte.game.world.entity.player;

/**
 * @author Jire
 */
public enum LogoutType {

    NONE(true),

    FORCE(false, 1),
    REQUESTED(true),
    CONNECTION_LOST(false),

    UPDATING(false, 2),

    ;

    private final boolean processSession;
    private final int logoutReasonType;

    LogoutType(final boolean processSession, final int logoutReasonType) {
        this.processSession = processSession;
        this.logoutReasonType = logoutReasonType;
    }

    LogoutType(final boolean processSession) {
        this(processSession, NO_LOGOUT_REASON_TYPE);
    }

    public boolean isProcessSession() {
        return processSession;
    }

    public int getLogoutReasonType() {
        return logoutReasonType;
    }

    public static final LogoutType[] values = values();

    public static final int NO_LOGOUT_REASON_TYPE = -1;

}
