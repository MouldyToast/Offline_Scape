package com.zenyte.game.world.entity.player.privilege;

public record ExpConfiguration(int combatModifier, int skillingExpModifier, int dropRateIncrease) {

    public boolean matches(int combatModifier, int expModifier) {
        return this.combatModifier == combatModifier && this.skillingExpModifier == expModifier;
    }

    public String getString() {
        return combatModifier + "x Combat, " + skillingExpModifier + "x Skilling & " + dropRateIncrease + "% Drop Rate";
    }

    @Override
    public String toString() {
        return "ExpConfiguration{" +
            "skillingExpModifier=" + skillingExpModifier +
            ", combatModifier=" + combatModifier +
            ", dropRateIncrease=" + dropRateIncrease +
            '}';
    }
}