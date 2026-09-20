package org.jesse.game.content.skills.construction.constants;

import org.jesse.utils.TextUtils;

public enum RoomType {

	LAND(-1, -1, 233, 880, -1, -1),
	PARLOUR(1, 1000, 232, 887, 1, 1433),
	GARDEN(1, 1000, 232, 881, 2, 1453),
	KITCHEN(5, 5000, 234, 887, 3, 1434),
	DINING_ROOM(10, 5000, 236, 887, 4, 1435),
	WORKSHOP(15, 10000, 232, 885, 12, 1444),
	BEDROOM(20, 10000, 238, 887, 5, 1436),
	SKILL_HALL(25, 15000, 233, 886, 7, 1439),
	GAMES_ROOM(30, 25000, 237, 884, 6, 1437),
	COMBAT_ROOM(32, 25000, 235, 884, 22, 1438),
	QUEST_HALL(35, 25000, 237, 886, 9, 1441),
	MENAGERIE_OUTDOORS(37, 30000, 239, 880, 25, 1457),
	MENAGERIE_INDOORS(37, 30000, 239, 882, 24, 1456),
	STUDY(40, 50000, 236, 885, 13, 1445),
	COSTUME_ROOM(42, 50000, 238, 881, 23, 1455),
	CHAPEL(45, 50000, 234, 885, 11, 1443),
	PORTAL_CHAMBER(50, 100000, 233, 884, 14, 1446),
	FORMAL_GARDEN(55, 75000, 234, 881, 21, 1454),
	THRONE_ROOM(60, 150000, 238, 885, 15, 1447),
	OUBLIETTE(65, 150000, 238, 883, 16, 1448),
	SUPERIOR_GARDEN(65, 75000, 237, 880, 26, 1458),
	DUNGEON_CORRIDOR(70, 7500, 232, 883, 17, 1450),
	DUNGEON_JUNCTION(70, 7500, 236, 883, 18, 1449),
	DUNGEON_STAIRS_ROOM(70, 7500, 234, 883, 19, 1451),
	TREASURE_ROOM(75, 250000, 239, 884, 20, 1452),
	ACHIEVEMENT_GALLERY(80, 200000, 233, 888, 27, 1459),
	PORTAL_NEXUS(72, 200000, 235, 888, 28, -1),
	LEAGUE_HALL(27, 15000, 237, 888, 29, -1),
	DUNGEON_LAND(-1, -1, 235, 880, -1, -1),
	ROOFS_A(-1, -1, 233, 882, -1, -1),
	ROOFS_B(-1, -1, 241, 882, -1, -1),
	QUEST_HALL_DS(35, 25000, 239, 886, 10, 1441),
	SKILL_HALL_DS(25, 15000, 235, 886, 8, 1439);

	private final int level;
    private final int price;
    private final int chunkX;
    private final int chunkY;
    private final int interfaceSlot;
    private final int enumMap;
	
	public static final RoomType[] VALUES = values();

    RoomType(int level, int price, int chunkX, int chunkY, int interfaceSlot, int enumMap) {
        this.level = level;
        this.price = price;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.interfaceSlot = interfaceSlot;
        this.enumMap = enumMap;
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

    public int getEnumMap() {
        return enumMap;
    }
}
