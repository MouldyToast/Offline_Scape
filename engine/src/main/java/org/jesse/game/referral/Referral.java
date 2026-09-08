package org.jesse.game.referral;

import org.jesse.game.world.entity.player.Player;

import java.util.*;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-30
 */
public final class Referral {
    private String referral;

    @Deprecated(forRemoval = true, since = "2025-04-11")
    private final List<String> referredPlayers = new ArrayList<>();

    private final Map<String, byte[]> referredPlayerMap = new HashMap<>();

    public Referral() {}

    public void withReferral(String referral) {
        this.referral = referral;
    }

    public String referral() {
        return referral;
    }

    /**
     * Retrieves a deprecated list of referred players, represented as a collection of player UUIDs
     * stored as strings.
     *
     * @deprecated As of 2025-04-11, this method is scheduled for removal in future versions.
     * Use {@link #referredPlayerMap()} instead, which provides a more flexible way to manage
     * referred players with additional data.
     *
     * <p> To migrate:
     * <pre>{@code
     * referral.referredPlayers().forEach(uuid -> {
     *     var players = ...; // logic to get a List<player> based on a UUID
     *     players.forEach(player -> {
     *         var name = player.getName();
     *         referral.referredPlayerMap().put(name, uuid);
     *     });
     * });
     * }</pre>
     *
     * @return the list of referred player UUIDs.
     */
    @Deprecated(forRemoval = true, since = "2025-04-11")
    public List<String> referredPlayers() {
        return referredPlayers;
    }

    /**
     * Retrieves a map of referred players, where each entry uses the player's name as the key
     * and UUID as the value.
     *
     * <p>This method is the recommended way to manage referred players, replacing older approaches
     * that relied on a simple list of UUIDs. The map structure provides greater flexibility.
     *
     * @return a map where the keys are player names represented as strings, and the values are
     *         the players UUID.
     */
    public Map<String, byte[]> referredPlayerMap() {
        return referredPlayerMap;
    }


    public void putPlayer(Player player) {
        var uuid = player.getPlayerInformation().getUUID();
        var name = player.getName().toLowerCase();
        referredPlayerMap.put(name, uuid);
    }
}
