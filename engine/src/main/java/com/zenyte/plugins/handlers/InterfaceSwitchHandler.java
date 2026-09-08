package com.zenyte.plugins.handlers;

import com.zenyte.game.util.Colour;
import com.zenyte.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;

/**
 * @author Kris | 19. juuli 2018 : 22:24:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum InterfaceSwitchHandler {
    ;

    private static final Logger log = NearRealityLogger.getLogger(InterfaceSwitchHandler.class);

    public static final Int2ObjectMap<InterfaceSwitchPlugin> interfaces = new Int2ObjectOpenHashMap<>();

    public static void add(final Class<? extends InterfaceSwitchPlugin> c) {
        try {
            final InterfaceSwitchPlugin plugin = c.getDeclaredConstructor().newInstance();
            for (final int key : plugin.getInterfaceIds()) {
                if (interfaces.containsKey(key)) {
                    log.error(Colour.RED + "FATAL: Overriding an interface handler. ID: " + key + ", Class: " + plugin.getClass().getSimpleName());
                }
                interfaces.put(key, plugin);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addUnsafe(Class<?> c) {
        add((Class<? extends InterfaceSwitchPlugin>) c);
    }

}
