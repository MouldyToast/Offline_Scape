package com.zenyte.game.model.item.actions;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorMonster;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.plugins.dialogue.ItemChat;

import java.util.Arrays;
import java.util.Optional;

import static com.near_reality.game.item.CustomItemId.SUPERIOR_BELL;

/**
 * Author: Leviticus
 * Contact: Discord - leviticusplays
 * Date: 2025-01-24
 */
public class SuperiorBell extends ItemPlugin {
    private final Animation ANIMATION = new Animation(7268, 15);
    private final SoundEffect SOUND = new SoundEffect(3522, 0, 0, 3);

    @Override
    public void handle() {
        bind("Ring", (player, item, slotId) -> {
            if (player.getBooleanTemporaryAttribute("superior monster")) {
                player.getDialogueManager().start(new ItemChat(player, item, "You can not use this when you have an active superior."));
                return;
            }

            if (!player.getSlayer().isBiggerAndBadder()) {
                player.getDialogueManager().start(new ItemChat(player, item, "You can only use this item when you have the slayer unlock Bigger and Badder."));
                return;
            }

            var slayerAssignment = player.getSlayer().getAssignment();
            if (slayerAssignment == null) {
                player.getDialogueManager().start(new ItemChat(player, item, "You can only use this when on a slayer task."));
                return;
            }

            var task = player.getSlayer().getAssignment().getTask();

            Optional<NPC> matchingNpc = player.getNpcViewport().stream()
                    .filter(npc -> task.validate(npc.getName(player), npc))
                    .findFirst();

            if (matchingNpc.isPresent()) {
                NPC inferior = matchingNpc.get();
                final Optional<Class<? extends SuperiorNPC>> superior = SuperiorMonster.getSuperior(inferior.getDefinitions().getName());
                if (superior.isPresent()) {
                    WorldTasksManager.schedule(() -> inferior.spawnSuperiorFromBell(player), 2);
                    player.setAnimation(ANIMATION);
                    player.sendSound(SOUND);
                    player.getInventory().deleteItem(SUPERIOR_BELL, 1);
                }
            } else {
                player.getDialogueManager().start(new ItemChat(player, item, "You need to be in your slayer assignment location to use this item."));
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{SUPERIOR_BELL};
    }
}
