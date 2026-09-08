package org.jesse.game.world;

import org.jesse.game.world.entity.Location;

public class GraphicsObjectRS {

    GraphicsObjectRS(int id, Location location, int delay) {
        this.id = id;
        this.location = location;
        this.delay = delay;
    }
    private final Location location;
    private final int id;
    private final int delay;

    public int getId() {
        return id;
    }

    public int getDelay() {
        return delay;
    }

    public Location getLocation() {
        return location;
    }
}
