package org.jesse.game.model.item.actions;

import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorMonster;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.plugins.dialogue.ItemChat;

import java.util.Arrays;
import java.util.Optional;

import static org.jesse.game.item.ids.ItemId.SUPERIOR_BELL;

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
