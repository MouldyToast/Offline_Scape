package com.zenyte.game.content.ZemouregalsFort;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

import static com.zenyte.game.item.ids.ItemId.BROKEN_ZOMBIE_HELMET;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 2/8/2025
 */
public class BrokenZombieHelmet extends ItemPlugin {

    @Override
    public void handle() {
        bind("Inspect", (player, item, slotId) -> player.getDialogueManager().start(new ZombieHelmetD(player)));
    }

    @Override
    public int[] getItems() {
        return new int[] {BROKEN_ZOMBIE_HELMET};
    }
}