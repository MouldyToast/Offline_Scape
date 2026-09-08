package org.jesse.plugins.itemonnpc;

import org.jesse.game.content.minigame.castlewars.CastleWars;
import org.jesse.game.content.minigame.castlewars.CastleWarsTeam;
import org.jesse.game.content.skills.firemaking.FiremakingAction;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.ItemChat;

import static org.jesse.plugins.renewednpc.CastlewarsBarricadeAction.BUCKET;
import static org.jesse.plugins.renewednpc.CastlewarsBarricadeAction.WATER_BUCKET;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class ItemOnBarricadeAction implements ItemOnNPCAction {

    public final static Graphics EXPLOSION = new Graphics(157, 0, 100);

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if (item.getId() == 4045) {
            if (!player.getInventory().containsItem(4045, 1)) {
                player.getDialogueManager().start(new ItemChat(player, CastleWars.EXPLOSIVE_POTION, "You need an explosive potion to blow up this barricade!"));
                return;
            }
            player.getInventory().deleteItem(slot, CastleWars.EXPLOSIVE_POTION);
            World.sendGraphics(EXPLOSION, npc.getLocation());
            if (CastleWars.getTeam(player).equals(CastleWarsTeam.SARADOMIN)) {
                CastleWarsTeam.setSaraBarricades(Math.max(0, CastleWarsTeam.getSaraBarricades() - 1));
            } else {
                CastleWarsTeam.setZamBarricades(Math.max(0, CastleWarsTeam.getZamBarricades() - 1));
            }
            npc.finish();
            return;
        }
        if (item.getId() == 590) {
            if (!player.getInventory().containsItem(FiremakingAction.TINDERBOX)) {
                player.getDialogueManager().start(new ItemChat(player, FiremakingAction.TINDERBOX, "You need a tinderbox to light barricades on fire!"));
                return;
            }
            npc.setTransformation(npc.getId() == 5722 ? 5723 : 5725);
            return;
        }
        if (item.getId() == 1929) {
            if (!player.getInventory().containsItem(WATER_BUCKET)) {
                player.getDialogueManager().start(new ItemChat(player, WATER_BUCKET, "You need a bucket of water to extinguish a flaming barricade!"));
                return;
            }
            player.getInventory().deleteItem(WATER_BUCKET);
            player.getInventory().addItem(BUCKET);
            npc.setTransformation(npc.getId() == 5723 ? 5722 : 5724);
            return;
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1929, 4045, 590 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 5722, 5723, 5724, 5725 };
    }
}
