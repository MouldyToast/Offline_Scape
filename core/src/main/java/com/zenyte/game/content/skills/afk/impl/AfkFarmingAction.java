package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.content.skills.construction.objects.superiorgarden.TopiaryBush;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.dialogue.PlainChat;

public class AfkFarmingAction extends BasicAfkAction {


    public AfkFarmingAction() {
    }

    @Override
    public Animation actionAnimation() {
        return new Animation(2273);
    }

    @Override
    public int getSkill() {
        return SkillConstants.FARMING;
    }

    @Override
    public String getMessage() {
        return "You rake some weeds.";
    }

    @Override
    public boolean hasRequiredItem() {
        if (!player.getInventory().containsItem(ItemId.RAKE)) {
            player.getDialogueManager().start(new PlainChat(player, "You need a rake to clear this patch."));
            return false;
        }
        return true;
    }
}
