package com.zenyte.game.content.area.prifddinas.zalcano.actions;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import com.zenyte.game.content.skills.smithing.Smelting;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.RequestResult;
import mgi.types.config.items.ItemDefinitions;

/**
 * Handles smelting tephra
 */
public class TephraSmeltAction extends Action {

    private int ticks = 3;

    @Override
    public boolean start() {
        if (!playerHasTephra()) {
            player.sendMessage("You must have Tephra to use this furnace.");
            return false;
        }
        var tempAttr = player.getTemporaryAttributes().get("zalcano_smelting_tephra");
        if (tempAttr == Boolean.TRUE) {
            player.sendFilteredMessage("Slow down... you can only do one thing at a time.");
            return false;
        }
        player.getTemporaryAttributes().put("zalcano_smelting_tephra", Boolean.TRUE);
        return true;
    }

    @Override
    public boolean process() {
        if (!playerHasTephra()) return false;
        var backpack = player.getInventory();
        if (ticks >= 1) {
            if (backpack.deleteItem(ZalcanoConstants.TEPHRA_ITEM_ID, 1).getResult() == RequestResult.SUCCESS) {
                player.getInventory().addItem(ZalcanoConstants.REFINED_TEPHRA_ITEM_ID, 1);
                player.sendFilteredMessage(ZalcanoConstants.REFINE_TEPHRA);
                player.getSkills().addXp(SkillConstants.SMITHING, 10);
                player.setAnimation(Smelting.ANIMATION);
                player.sendSound(Smelting.soundEffect);
                ticks = 0;
                player.getTemporaryAttributes().put("zalcano_smelting_tephra", Boolean.FALSE);
                return true;
            }
        }
        ticks++;
        return true;
    }

    public boolean playerHasTephra() {
        return player.getInventory().containsItem(ZalcanoConstants.TEPHRA_ITEM_ID);
    }

    @Override
    public int processWithDelay() {
        return 0;
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

}
