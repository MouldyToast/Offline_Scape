package com.zenyte.game.model.ui;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.component.ComponentDefinitions;
import org.slf4j.Logger;

import java.util.Optional;

import static com.zenyte.game.GameConstants.DEV_DEBUG;
import static com.zenyte.game.GameConstants.WORLD_PROFILE;

public enum ButtonAction {
    ;

    private static final Logger logger = NearRealityLogger.getLogger(ButtonAction.class);

    public static final Int2ObjectMap<UserInterface> interfaces = new Int2ObjectOpenHashMap<>();

    public static void handleComponentAction(final Player player, final int interfaceId, final int componentId,
                                             final int slotId, final int itemId, final int option, final int format) {
        if (!player.getInterfaceHandler().getVisible().inverse().containsKey(interfaceId)) {
            return;
        }
        if (!player.getControllerManager().canButtonClick(interfaceId, componentId, slotId)) {
            return;
        }
        final boolean isDebugEnabled = logger.isDebugEnabled();
        player.getInterfaceHandler().closeInput(true);
        final ComponentDefinitions defs = ComponentDefinitions.get(interfaceId, componentId);
        final String op = defs.getActions() == null || defs.getActions().length <= (option - 1) ? "null" :
                defs.getActions()[option - 1];
        /*
         * Temporarily
         */
        {
            final Interface plugin = NewInterfaceHandler.getInterface(interfaceId);
            if (plugin != null) {
                Optional<String> opt = plugin.getComponentName(componentId, slotId);
                if (opt.isEmpty()) {
                    opt = plugin.getComponentName(componentId, -1);
                }
                if (WORLD_PROFILE.isDevelopment()) {
                    logger.info("[{}] IF{}: {}({}::{}) | Slot: {} | Option: {} | Item: {}", plugin.getClass().getSimpleName(), format, opt.orElse("Absent"), interfaceId, componentId, slotId, option, itemId);
                }
                plugin.click(player, componentId, slotId, itemId, option);
                return;
            }
        }
        final UserInterface inter = interfaces.get(interfaceId);
        if (inter != null) {
            if (WORLD_PROFILE.isDevelopment()) {
                logger.info("[Interface(IF{}):{}], interfaceId={}, component={}, slot={}, item={}, option={}({})", format, inter.getClass().getSimpleName(), interfaceId, componentId, slotId, itemId, op, option);
            }
            inter.handleComponentClick(player, interfaceId, componentId, slotId, itemId, option, op);
            return;
        }
        if (WORLD_PROFILE.isDevelopment()) {
            logger.info("[Unhandled(IF{})]: interfaceId={}, component={}, slot={}, item={}, option={}({})", format, interfaceId, componentId, slotId, itemId, op, option);
        }
    }

    public static void add(final Class<?> c) {
        final boolean isErrorEnabled = logger.isErrorEnabled();
        try {
            final UserInterface userInterface = (UserInterface) c.getDeclaredConstructor().newInstance();
            for (final int key : userInterface.getInterfaceIds()) {
                if (isErrorEnabled && interfaces.containsKey(key)) {
                    logger.error("<col=ff0000>FATAL: Overriding an interface handler. ID: " + key + ", Class: " + userInterface.getClass().getSimpleName());
                }
                interfaces.put(key, userInterface);
            }
        } catch (final Exception e) {
            if (isErrorEnabled)
                logger.error("Failed to add class \"" + c + "\"", e);
        }
    }

}
