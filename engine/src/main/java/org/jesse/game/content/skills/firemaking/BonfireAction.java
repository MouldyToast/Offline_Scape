package org.jesse.game.content.skills.firemaking;

import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.SkillConstants;

public class BonfireAction extends Action {


    private final Firemaking firemaking;


    public BonfireAction(Firemaking firemaking) {
        this.firemaking = firemaking;
    }

    public boolean check() {
        Firemaking logs = firemaking;
        if(logs != null && logs.getLevel() > (player.getSkills().getLevel(SkillConstants.FIREMAKING))) {
            player.sendMessage("You lack the required firemaking level to light these.");
            return false;
        }
        return player.getInventory().containsItem(firemaking.getLogs());
    }

    @Override
    public boolean start() {
        return check();
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        player.getInventory().deleteItem(firemaking.getLogs().getId(), 1);
        player.setAnimation(new Animation(827));
        player.getSkills().addXp(SkillConstants.FIREMAKING, firemaking.getXp());
        return 4;
    }

    @Override
    public void stop() {
        delay(3);
    }
}
