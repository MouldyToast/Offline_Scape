package org.jesse.game.content.skills.hunter.npc.plugins;

import org.jesse.game.content.skills.hunter.HunterUtils;
import org.jesse.game.content.skills.hunter.npc.PitfallHunterNPC;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 30/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PitfallNPC extends NPCPlugin implements ItemOnNPCAction {
    private static final Animation npcTeaseAnimation = new Animation(5227);
    private static final Animation playerTeaseAnimation = new Animation(5236);

    @Override
    public void handle() {
        bind("Tease", PitfallNPC::tease);
    }

    private static final void tease(@NotNull final Player player, @NotNull final NPC npc) {
        player.getActionManager().setAction(new Action() {
            @Override
            public boolean start() {
                final String name = npc.getName(player).toLowerCase();
                if (!player.getInventory().containsItem(new Item(ItemId.TEASING_STICK, 1))) {
                    player.sendMessage("You need a teasing stick to tease the " + name + ".");
                    return false;
                }
                return true;
            }
            @Override
            public boolean process() {
                return true;
            }
            @Override
            public int processWithDelay() {
                final Optional<PitfallHunterNPC> teasedNPC = HunterUtils.getTeasedNPC(player);
                if (teasedNPC.isPresent()) {
                    final PitfallHunterNPC creature = teasedNPC.get();
                    if (creature != npc && player.isUnderCombat()) {
                        player.sendMessage("I'm already under attack!");
                        return -1;
                    }
                    creature.getCombat().removeTarget();
                    HunterUtils.setTeasedNPC(player, null);
                }
                player.setAnimation(playerTeaseAnimation);
                npc.setAnimation(npcTeaseAnimation);
                player.sendSound(2524);
                HunterUtils.setTeasedNPC(player, npc);
                WorldTasksManager.schedule(() -> npc.getCombat().forceTarget(player), 1);
                delay(2);
                return -1;
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.SPINED_LARUPIA, NpcId.HORNED_GRAAHK, NpcId.SABRETOOTHED_KYATT};
    }

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        tease(player, npc);
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemId.TEASING_STICK};
    }

    @Override
    public Object[] getObjects() {
        return new Object[0];
    }
}
