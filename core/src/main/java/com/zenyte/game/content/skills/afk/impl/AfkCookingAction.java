package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.content.skills.cooking.CookingDefinitions;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;

public class AfkCookingAction extends BasicAfkAction {
    @Override
    public Animation actionAnimation() {
        return CookingDefinitions.STOVE;
    }

    @Override
    public int getSkill() {
        return SkillConstants.COOKING;
    }

    @Override
    public String getMessage() {
        return "You cook some bacon...";
    }

    @Override
    public boolean hasRequiredItem() {
        return true;
    }
}
