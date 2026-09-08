package org.jesse.game.content.area.prifddinas.zalcano.combat.symbols;

import org.jesse.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;

import java.util.HashSet;
import java.util.Set;

public class DefaultDemonicSymbolGenerator implements DemonicSymbolGenerator{

    private final int amount;

    public DefaultDemonicSymbolGenerator(int amount) {
        this.amount = amount;
    }

    public DefaultDemonicSymbolGenerator() {
        this(Utils.random(50, 80));
    }

    @Override
    public Set<DemonicSymbol> generateDemonicSymbols(ZalcanoInstance instance) {
        Set<DemonicSymbol> symbols = new HashSet<>(amount);
        for (int i = 0; i <= amount; i++) {
            Location newLoc = instance.getLair().getRandomPosition();

            if (!World.isTileClipped(newLoc, 1)) {
                continue;
            }

            if (!World.isFloorFree(newLoc, 3)) {
                continue;
            }

            if (!canPlaceSymbol(newLoc, instance, symbols)) {
                continue;
            }

            boolean isRed = Utils.random(1, 2) == 1;
            symbols.add(new DemonicSymbol(instance, newLoc, isRed));
        }

        return symbols;
    }


    public boolean canPlaceSymbol(Location newLocation, ZalcanoInstance instance, Set<DemonicSymbol> symbols) {
        final var  currentX = newLocation.getX();
        final var currentY = newLocation.getY();

        for (int x = currentX - 3; x <= (currentX + 3); x++) {
            for (int y = currentY - 3; y <= (currentY + 3); y++) {
                for (DemonicSymbol symbol : symbols) {
                    if (symbol.getX() == x && symbol.getY() == y){
                        return false;
                    }
                }
                if (World.getObjectWithType(new Location(x, y), 10) != null) {
                    return false;
                }
            }
        }

        return true;
    }
}
