package org.jesse.plugins.item;

import org.jesse.game.content.minigame.castlewars.CastleWars;
import org.jesse.game.content.minigame.castlewars.CastleWarsTeam;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.impl.BarricadeNPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.region.CharacterLoop;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastlewarsBarricade extends ItemPlugin {
    @Override
    public void handle() {
        bind("Set-up", new OptionHandler() {
            @Override
            public void handle(final Player player, final Item item, final Container container, final int slotId) {
                if (!player.inArea("Castle Wars")) {
                    player.getInventory().deleteItem(item.getId(), player.getInventory().getAmountOf(item.getId()));
                    player.sendMessage("You cannot have or use these outside of Castle Wars!");
                    return;
                }
                if (World.containsObjectWithId(player.getLocation(), 4411)) {
                    player.sendMessage("You can\'t put a barricade here!");
                    return;
                }
                if (!CharacterLoop.find(player.getLocation(), 0, NPC.class, npc -> npc.getId() >= 5722 && npc.getId() <= 5725).isEmpty()) {
                    player.sendMessage(Colour.RS_RED.wrap("There\'s already a barricade here!"));
                    return;
                }
                // npc 5722 for saradomin barricade
                // npc 5724 for zamorak barricade
                final boolean saradomin = CastleWars.getTeam(player).equals(CastleWarsTeam.SARADOMIN);
                if (saradomin && CastleWarsTeam.getSaraBarricades() >= 10 || !saradomin && CastleWarsTeam.getZamBarricades() >= 10) {
                    player.sendMessage("Your team has already placed 10 barricades!");
                    return;
                }
                player.getInventory().deleteItem(slotId, item);
                World.spawnNPC(new BarricadeNPC((saradomin ? 5722 : 5724), player.getLocation(), Direction.SOUTH, 0));
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {4053};
    }
}
