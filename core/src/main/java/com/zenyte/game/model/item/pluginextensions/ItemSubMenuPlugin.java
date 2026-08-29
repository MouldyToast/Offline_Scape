package com.zenyte.game.model.item.pluginextensions;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.SubMenuAction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Plugin;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-09
 */
public abstract class ItemSubMenuPlugin implements Plugin {
    public abstract void handle(Player player, Item item, int optionId, int subOptionId);
    public abstract int[] getItems();

    private final Map<Integer, SubOptionHandler> delegatedHandlers = new HashMap<>();

    public static ItemSubMenuPlugin getPlugin(final int id) {
        return Utils.getOrDefault(SubMenuAction.intActions.get(id), SubMenuAction.defaultActions);
    }

    public SubOptionHandler getHandler(final Integer option) {
        return delegatedHandlers.get(option);
    }

    public void setDefaultHandlers() {
    }

    public void bind(Integer option, final SubOptionHandler handler) {
        if (verifyIfSubOptionExists(option))
            getDelegatedHandlers().put(option, handler);
    }

    private Boolean verifyIfSubOptionExists(final Integer subOption) {
        if (getItems().length == 0) return false;
        for (final int id : getItems()) {
            final ItemDefinitions definitions = ItemDefinitions.get(id);
            if (definitions == null) continue;
            if (definitions.containsSubOption(subOption))
                return true;
        }
        return false;
    }

    public Map<Integer, SubOptionHandler> getDelegatedHandlers() {
        return delegatedHandlers;
    }

    @FunctionalInterface
    public interface SubOptionHandler {
        void handle(final Player player, int optionId, int slotId);
    }
}
