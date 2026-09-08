package org.jesse.game.content.area.prifddinas.zalcano.combat;

import org.jesse.game.content.area.prifddinas.zalcano.ZalcanoInstance;

/**
 * Zalcano's attack
 */
public interface ZalcanoAttack {

    void execute(ZalcanoInstance instance);

    boolean canProcess(ZalcanoInstance instance);

    /**
     * This will interrupt the current attack
     */
    void interrupt();

}
