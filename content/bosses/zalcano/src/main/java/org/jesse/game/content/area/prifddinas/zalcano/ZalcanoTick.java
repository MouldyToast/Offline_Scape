package org.jesse.game.content.area.prifddinas.zalcano;

import org.jesse.game.task.TickTask;

public abstract class ZalcanoTick extends TickTask {

    protected ZalcanoInstance instance;

    public ZalcanoTick(ZalcanoInstance instance) {
        this.instance = instance;
    }
}
