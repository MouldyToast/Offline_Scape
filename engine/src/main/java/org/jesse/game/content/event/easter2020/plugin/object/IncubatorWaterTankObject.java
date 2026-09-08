package org.jesse.game.content.event.easter2020.plugin.object;

import org.jesse.game.content.event.easter2020.EasterConstants;
import org.jesse.game.content.event.easter2020.EasterConstants.EasterItem;
import org.jesse.game.content.event.easter2020.SplittingHeirs;
import org.jesse.game.content.event.easter2020.Stage;
import org.jesse.game.content.event.easter2020.plugin.npc.Incubator;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.SkipPluginScan;
import org.jesse.plugins.dialogue.PlainChat;
import org.jesse.plugins.dialogue.PlayerChat;

/**
 * @author Corey
 * @since 08/04/2020
 */
@SkipPluginScan
public class IncubatorWaterTankObject implements ObjectAction {
    
    public static boolean isFixed(final Player player) {
        return player.getVarManager().getBitValue(EasterConstants.WATER_TANK_VARBIT) == 1;
    }
    
    public static void fix(final Player player) {
        player.getVarManager().sendBit(EasterConstants.WATER_TANK_VARBIT, 1);
    }
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new PlainChat(player, "This looks like the supply of water which feeds the coal burner."));
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{EasterConstants.INCUBATOR_WATER_TANK};
    }
    
    public static class ItemOnCoalSupply implements ItemOnObjectAction {
        
        @Override
        public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
            if (isFixed(player)) {
                player.getDialogueManager().start(new PlainChat(player, "This machine is already repaired."));
                return;
            }
    
            if (!SplittingHeirs.progressedAtLeast(player, Stage.SPOKEN_WITH_INCUBATOR_WORKER)) {
                player.getDialogueManager().start(new PlayerChat(player, "I should speak with the Impling working this machine to see what they want me to do."));
                return;
            }
    
            if (Incubator.State.getCurrentState(player) != Incubator.State.WITH_CHIMNEY) {
                player.getDialogueManager().start(new PlainChat(player, "The main incubator needs to be repaired first."));
                return;
            }
    
            if (item.getId() != EasterItem.WET_PIPE.getItemId()) {
                player.getDialogueManager().start(new PlainChat(player, "Doesn't look like that will fit anywhere on this section. I wonder if it goes on the main incubator, or one of the other units."));
                return;
            }
            
            player.getInventory().deleteItem(item);
            player.sendMessage("You connect the water tank to the coal burner with the wet pipe.");
            fix(player);
            Incubator.fixIncubator(player);
        }
        
        @Override
        public Object[] getItems() {
            return new Object[]{
                    EasterItem.COG.getItemId(),
                    EasterItem.PISTONS.getItemId(),
                    EasterItem.CHIMNEY.getItemId(),
                    EasterItem.CLEAN_PIPE.getItemId(),
                    EasterItem.SOOTY_PIPE.getItemId(),
                    EasterItem.WET_PIPE.getItemId()
            };
        }
        
        @Override
        public Object[] getObjects() {
            return new Object[]{EasterConstants.INCUBATOR_WATER_TANK};
        }
    }
    
}
