package org.jesse.game.content.skills.construction.constants;

import org.jesse.utils.TextUtils;

public enum RoomType {

    //                                                          interfaceSlot
    //                                                          |   pohRoomDbrow
    //                                                          |   |     doorMask (bit per wall side: 0=S,1=W,2=N,3=E)
    LAND(-1, -1, 233, 880,                                    -1, -1,     0),
    PARLOUR(1, 1000, 232, 887,                                  1, 4166, 14),  // doors: W,N,E
    GARDEN(1, 1000, 232, 881,                                   2, 4167, 15),  // doors: S,W,N,E
    KITCHEN(5, 5000, 234, 887,                                  3, 4168, 12),  // doors: N,E
    DINING_ROOM(10, 5000, 236, 887,                             4, 4169, 14),  // doors: W,N,E
    WORKSHOP(15, 10000, 232, 885,                              12, 4177,  5),  // doors: S,N
    BEDROOM(20, 10000, 238, 887,                                5, 4170, 12),  // doors: N,E
    SKILL_HALL(25, 15000, 233, 886,                             7, 4172, 15),  // doors: S,W,N,E
    GAMES_ROOM(30, 25000, 237, 884,                             6, 4171, 14),  // doors: W,N,E
    COMBAT_ROOM(32, 25000, 235, 884,                           22, 4187, 14),  // doors: W,N,E
    QUEST_HALL(35, 25000, 237, 886,                             9, 4174, 15),  // doors: S,W,N,E
    MENAGERIE_OUTDOORS(37, 30000, 239, 880,                    25, 4190, 15),  // doors: S,W,N,E
    MENAGERIE_INDOORS(37, 30000, 239, 882,                     24, 4189, 15),  // doors: S,W,N,E
    STUDY(40, 50000, 236, 885,                                 13, 4178, 14),  // doors: W,N,E
    COSTUME_ROOM(42, 50000, 238, 881,                          23, 4188,  4),  // doors: N
    CHAPEL(45, 50000, 234, 885,                                11, 4176, 12),  // doors: N,E
    PORTAL_CHAMBER(50, 100000, 233, 884,                       14, 4179,  4),  // doors: N
    FORMAL_GARDEN(55, 75000, 234, 881,                         21, 4186, 15),  // doors: S,W,N,E
    THRONE_ROOM(60, 150000, 238, 885,                          15, 4180,  4),  // doors: N
    OUBLIETTE(65, 150000, 238, 883,                            16, 4181, 11),  // doors: S,W,E
    SUPERIOR_GARDEN(65, 75000, 237, 880,                       26, 4191, 15),  // doors: S,W,N,E
    DUNGEON_CORRIDOR(70, 7500, 232, 883,                       17, 4182,  5),  // doors: S,N
    DUNGEON_JUNCTION(70, 7500, 236, 883,                       18, 4183, 15),  // doors: S,W,N,E
    DUNGEON_STAIRS_ROOM(70, 7500, 234, 883,                    19, 4184, 15),  // doors: S,W,N,E
    TREASURE_ROOM(75, 250000, 239, 884,                        20, 4185,  4),  // doors: N
    ACHIEVEMENT_GALLERY(80, 200000, 233, 888,                  27, 4192,  5),  // doors: S,N
    PORTAL_NEXUS(72, 200000, 235, 888,                         28, 4193, 15),  // doors: S,W,N,E
    LEAGUE_HALL(27, 15000, 237, 888,                           29, 4194, 14),  // doors: W,N,E
    DUNGEON_LAND(-1, -1, 235, 880,                             -1, -1,    0),
    ROOFS_A(-1, -1, 233, 882,                                  -1, -1,    0),
    ROOFS_B(-1, -1, 241, 882,                                  -1, -1,    0),
    QUEST_HALL_DS(35, 25000, 239, 886,                         10, 4175, 15),  // doors: S,W,N,E
    SKILL_HALL_DS(25, 15000, 235, 886,                          8, 4173, 15);  // doors: S,W,N,E

    private final int level;
    private final int price;
    private final int chunkX;
    private final int chunkY;
    private final int interfaceSlot;
    /** Rev-240 poh_room dbtable dbrow ID. Replaces the rev-228 enum ID. */
    private final int pohRoomDbrow;
    /** Bitmask of which wall sides have doors (bit 0=S, 1=W, 2=N, 3=E). */
    private final int doorMask;

    public static final RoomType[] VALUES = values();

    RoomType(int level, int price, int chunkX, int chunkY, int interfaceSlot, int pohRoomDbrow, int doorMask) {
        this.level = level;
        this.price = price;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.interfaceSlot = interfaceSlot;
        this.pohRoomDbrow = pohRoomDbrow;
        this.doorMask = doorMask;
    }

    public static RoomType getRoomBySlot(int slot) {
        for (RoomType room : VALUES) {
            if (slot == room.getInterfaceSlot())
                return room;
        }
        return null;
    }

    @Override
    public String toString() {
        return TextUtils.capitalize(name().replace("_", " "));
    }

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getInterfaceSlot() {
        return interfaceSlot;
    }

    public int getPohRoomDbrow() {
        return pohRoomDbrow;
    }

    public int getDoorMask() {
        return doorMask;
    }
}