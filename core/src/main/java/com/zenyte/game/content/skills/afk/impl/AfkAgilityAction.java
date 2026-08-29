package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.SkillConstants;

public class AfkAgilityAction extends BasicAfkAction {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, 762);

    @Override
    public boolean start() {
        if(!super.check())
            return false;

        player.getTemporaryAttributes().put("courseRun", player.isRun());
        player.setRunSilent(true);
        return true;
    }


    public boolean movingSouth() {
        return player.getLocation().getY() == 3479;
    }

    @Override
    public int processWithDelay() {
        player.getAppearance().setRenderAnimation(RENDER);
        player.addWalkSteps(3128, movingSouth() ? 3474 : 3479, -1, false);
        return super.processWithDelay();
    }

    @Override
    public void stop() {
        super.stop();
        player.setRunSilent(false);
        player.getAppearance().resetRenderAnimation();
    }

    @Override
    public Animation actionAnimation() {
        return null;
    }

    @Override
    public int getSkill() {
        return SkillConstants.AGILITY;
    }

    @Override
    public String getMessage() {
        return "You cross the log";
    }

    @Override
    public boolean hasRequiredItem() {
        return true;
    }
}
