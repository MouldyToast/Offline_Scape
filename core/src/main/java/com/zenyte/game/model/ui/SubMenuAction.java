package com.zenyte.game.model.ui;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemSubMenuPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.logger.NearRealityLogger;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-09
 */
public class SubMenuAction {

    private static final Logger logger = NearRealityLogger.getLogger(SubMenuAction.class);

    public static final ItemSubMenuPlugin defaultActions;

    public static final Map<Integer, ItemSubMenuPlugin> intActions = new HashMap<>();

    static {
        defaultActions = new ItemSubMenuPlugin() {
            @Override
            public void handle(Player player, Item item, int optionId, int subOptionId) {}
            @Override
            public int[] getItems() {
                return new int[0];
            }
        };
        defaultActions.setDefaultHandlers();
    }

    public static void handleSubMenuAction(final Player player, final int slotId, final int itemId, final int optionId, final int subOptionId) {
        final var item = player.getInventory().getItem(slotId);
        if (item == null || item.getId() != itemId) {
            return;
        }
        final var itemDef = item.getDefinitions();
        if (itemDef == null) {
            player.sendMessage("Nothing interesting happens.");
            logger.debug("Item (id: {}, slot: {}) did not have a definition! (option: {})", itemId, slotId, subOptionId);
            return;
        }
        final var plugin = ItemSubMenuPlugin.getPlugin(item.getId());
        if (plugin == null) {
            player.sendMessage("Nothing interesting happens.");
            logger.debug("Item (id: {}, slot: {}) did not have a plugin! (option: {})", itemId, slotId, subOptionId);
            return;
        }
        plugin.handle(player, item, optionId, subOptionId);
    }

    public static void add(final Class<?> clazz) {
        logger.debug("SubMenuAction: add: {}", clazz);
        try {
            var base = (ItemSubMenuPlugin) clazz.getDeclaredConstructor().newInstance();
            Arrays.stream(base.getItems())
                .forEach(item -> intActions.put(item, base));
        }
        catch (InvocationTargetException |
               InstantiationException |
               IllegalAccessException |
               NoSuchMethodException
            e) {
            throw new RuntimeException(e);
        }
    }
}
