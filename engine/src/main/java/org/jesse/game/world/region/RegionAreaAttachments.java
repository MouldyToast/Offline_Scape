package org.jesse.game.world.region;

import org.jesse.game.world.entity.player.Player;
import org.jesse.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Jire
 */
public enum RegionAreaAttachments {
    ;

    private static final Logger logger = NearRealityLogger.getLogger(RegionAreaAttachments.class);

    private static final Map<String, List<Consumer<Player>>> loginAttachments = new Object2ObjectOpenHashMap<>();

    public static void onLogin(String areaClassName, Consumer<Player> consumer) {
        loginAttachments.computeIfAbsent(areaClassName, k -> new ObjectArrayList<>(1)).add(consumer);
    }

    public static void runLogin(String areaClassName, Player player) {
        final List<Consumer<Player>> consumers = loginAttachments.get(areaClassName);
        if (consumers == null) return;
        for (final Consumer<Player> consumer : consumers) {
            try {
                consumer.accept(player);
            } catch (Exception e) {
                logger.error("Failed to run login attachment for area \"" + areaClassName + "\"", e);
            }
        }
    }

}
