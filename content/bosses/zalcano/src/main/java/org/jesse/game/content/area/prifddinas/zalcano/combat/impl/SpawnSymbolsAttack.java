package org.jesse.game.content.area.prifddinas.zalcano.combat.impl;

import org.jesse.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import org.jesse.game.content.area.prifddinas.zalcano.combat.ZalcanoAttack;
import org.jesse.game.content.area.prifddinas.zalcano.combat.symbols.DefaultDemonicSymbolGenerator;
import org.jesse.game.content.area.prifddinas.zalcano.combat.symbols.DemonicSymbolGenerator;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;

public class SpawnSymbolsAttack implements ZalcanoAttack {

    public static final Animation ANIM = new Animation(8433);
    private final DemonicSymbolGenerator generator;
    private WorldTask spawnTask;
    public SpawnSymbolsAttack() {
        generator = new DefaultDemonicSymbolGenerator();
    }

    @Override
    public void execute(ZalcanoInstance instance) {
        instance.getZalcano().setAnimation(ANIM);
        instance.getZalcano().freeze(3);
        this.spawnTask = () -> {
            var generateDemonicSymbols = generator.generateDemonicSymbols(instance);
            instance.registerSymbols(generateDemonicSymbols);
        };

        WorldTasksManager.schedule(this.spawnTask, 2);
    }

    @Override
    public boolean canProcess(ZalcanoInstance instance) {
        return instance.getSymbols().size() == 0;
    }

    @Override
    public void interrupt() {
        this.spawnTask.stop();
    }
}
