package com.zenyte.game.content.breaches;

import com.zenyte.game.world.entity.Location;

public enum BreachLocations {
    FEROX_ENCLAVE(new Location(3168, 3562, 0), "south of the Ferox Enclave", true),
    NW_HOME(new Location(3037, 3550, 0), "north-west of Edgeville (home)", false),
    NORTH_CRAZY_ARC(new Location(2978, 3722, 0), "north of the Crazy Archaeologist", false),
    EAST_BONEYARD(new Location(3321, 3739, 0), "east of the Wilderness Boneyard", true),
    NORTH_CANOE(new Location(3142, 3832, 0), "north of the Black Chinchompas", false),
    WEST_50_OBELISK(new Location(3262, 3914, 0), "west of the level-50 obelisk", true),
    SOUTH_PIRATES_HIDEOUT(new Location(3026, 3929, 0), "south of the Pirate's Hideout", false);

    public final Location teleportLocation;
    public final String broadcastLocation;
    public final Boolean multi;

    BreachLocations(Location teleportLocation, String broadcastLocation, Boolean multi) {
        this.teleportLocation = teleportLocation;
        this.broadcastLocation = broadcastLocation;
        this.multi = multi;
    }
}
