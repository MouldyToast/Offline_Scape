package org.jesse.game.content.gauntlet.objects;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;

import java.util.Optional;

public final class PhrenRoots extends DepletingResourceObject {

    private static final int INITIAL_AMOUNT = 3;

    public PhrenRoots(Location location, boolean corrupted) {
        super(corrupted ? 35969 : 36066, location, Utils.random(0, 3), corrupted ? 23838 : 23878, INITIAL_AMOUNT);
    }

    @Override
    public Optional<String> getDepletionMessage() {
        return Optional.of("You chop the last of the salvageable bark from the roots.");
    }

}
