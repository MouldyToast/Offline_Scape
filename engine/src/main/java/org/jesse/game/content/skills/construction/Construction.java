package org.jesse.game.content.skills.construction;

import com.google.gson.annotations.Expose;
import org.jesse.game.GameConstants;
import org.jesse.game.content.achievementdiary.diaries.ArdougneDiary;
import org.jesse.game.content.skills.construction.constants.Furniture;
import org.jesse.game.content.skills.construction.constants.FurnitureSpace;
import org.jesse.game.content.skills.construction.constants.House;
import org.jesse.game.content.skills.construction.constants.RoomType;
import org.jesse.game.content.skills.construction.costume.*;
import org.jesse.game.content.skills.construction.dialogue.BuildStaircaseD;
import org.jesse.game.item.Item;
import org.jesse.game.model.MinimapState;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.DirectionUtil;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity._Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.ContainerPolicy;
import org.jesse.game.world.entity.player.container.impl.ContainerType;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.Region;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.CoordinateUtilities;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.logger.NearRealityLogger;
import org.jesse.plugins.dialogue.PlainChat;
import org.jesse.plugins.dialogue.RoomCreationD;
import org.jesse.plugins.dialogue.RoomRemovingD;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.DBRowDefinition;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.jesse.game.content.skills.construction.ConstructionConstants.*;

/**
 * @author Tommeh 13 nov. 2017 : 22:36:26
 * @author Kris | 21. nov 2017 : 2:32.54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>} TODO: Dungeon floor space reminder: If rocnar exists,
 * leave 15351 empty (and spawn rocnar there) Otherwise, spawn whatever floor space you had. TODO: Configure tip jar to work with other
 * people!
 */
public final class Construction {

    private static final Logger log = NearRealityLogger.getLogger(Construction.class);

    /** furniture dbtable (110) item ID → dbrow ID, for cs2 1404. */
    private static final Map<Integer, Integer> ITEM_TO_DBROW = new HashMap<>();
    static {
        List<DBRowDefinition> rows = DBRowDefinition.tableRows.get(110);
        if (rows != null) {
            for (DBRowDefinition row : rows) {
                Object obj = row.getValueFromRow(0);
                if (obj instanceof Integer) {
                    ITEM_TO_DBROW.put((Integer) obj, row.getId());
                }
            }
        }
    }

    @Expose
    private final List<RoomReference> references = new ArrayList<RoomReference>();

    private final transient Map<Player, Integer> catsOnBlanket = new HashMap<Player, Integer>();

    @Expose
    private final List<Integer> displayJars = new ArrayList<Integer>();

    @Expose
    private final ArmourCase armourCase = new ArmourCase();

    @Expose
    private final FancyDressBox fancyDressBox = new FancyDressBox();

    @Expose
    private final ToyBox toyBox = new ToyBox();

    @Expose
    private final MagicWardrobe magicWardrobe = new MagicWardrobe();

    @Expose
    private final CapeRack capeRack = new CapeRack();

    @Expose
    private final TreasureChest treasureChest = new TreasureChest();

    private final transient Player player;

    private transient boolean buildingMode;

    private transient boolean renderDoorsOpen;

    private transient boolean teleportInside;

    private transient int chunkX;

    private transient int chunkY;

    private transient int yardSpace;

    private transient int yardOffset;

    @Expose
    private int decoration;

    @Expose
    private House house = House.RIMMINGTON;

    private transient HouseViewer houseViewer;

    @Expose
    private TipJar tipJar;

    private transient Location portal;

    @Expose
    private int servantsCash;

    private transient AllocatedArea allocatedArea;

    public Construction(final Player player) {
        this.player = player;
        houseViewer = new HouseViewer(player);
        tipJar = new TipJar(player);
    }

    public final void setFields(final Construction construction) {
        if (construction.displayJars != null) {
            displayJars.addAll(construction.displayJars);
        }
        if (construction.armourCase != null) {
            armourCase.getItems().putAll(construction.armourCase.getItems());
        }
        armourCase.setPlayer(player);
        if (construction.fancyDressBox != null) {
            fancyDressBox.getItems().putAll(construction.fancyDressBox.getItems());
        }
        fancyDressBox.setPlayer(player);
        if (construction.toyBox != null) {
            toyBox.getItems().putAll(construction.toyBox.getItems());
        }
        toyBox.setPlayer(player);
        if (construction.capeRack != null) {
            capeRack.getItems().putAll(construction.capeRack.getItems());
        }
        capeRack.setPlayer(player);
        if (construction.magicWardrobe != null) {
            magicWardrobe.getItems().putAll(construction.magicWardrobe.getItems());
        }
        magicWardrobe.setPlayer(player);
        if (construction.treasureChest != null) {
            treasureChest.getItems().putAll(construction.treasureChest.getItems());
        }
        treasureChest.setPlayer(player);
        house = construction.house;
        decoration = construction.decoration;
        servantsCash = construction.servantsCash;
        if (construction.tipJar != null) {
            tipJar.set(construction.tipJar);
        }
        for (final RoomReference roomReference : construction.getReferences()) {
            if (roomReference == null) {
                continue;
            }
            references.add(roomReference);
        }
    }

    public void refreshHouseOptions() {
        player.getVarManager().sendBit(2176, buildingMode ? 1 : 0);
        player.getVarManager().sendBit(3983, renderDoorsOpen ? 1 : 0);
        player.getVarManager().sendBit(4744, teleportInside ? 0 : 1);
    }

    /**
     * TODO fix clicking off multiple times.
     */
    public Location getRelationalSpawnTile() {
        final int xInChunk = player.getLocation().getXInChunk();
        final int yInChunk = player.getLocation().getYInChunk();
        final int relChunkX = player.getLocation().getChunkX() - chunkX;
        final int relChunkY = player.getLocation().getChunkY() - chunkY;
        return new Location(relChunkX * 8 + xInChunk, relChunkY * 8 + yInChunk, player.getPlane());
    }

    public void enterHouse(final boolean building) {
        enterHouse(building, null);
    }

    public boolean enterHouse(final boolean building, final Location spawnTile) {
        if (!GameConstants.isOwner(player)) {
            player.sendMessage("Construction is incomplete, therefore disabled.");
            return false;
        }
        try {
            if (house.equals(House.YANILLE)) {
                player.getAchievementDiaries().update(ArdougneDiary.ENTER_YOUR_POH);
            }
            player.getTemporaryAttributes().put("VisitingHouse", this);
            buildingMode = building;
            portal = null;
            final long time = System.nanoTime();
            player.getPacketDispatcher().sendComponentText(370, 16, "Number of rooms: " + getAmountOfRooms());
            yardSpace = generateYardSpace();
            yardOffset = generateYardOffset();
            player.getTemporaryAttributes().put("yard", yardOffset);
            player.getControllerManager().startController(new ConstructionController());
            if (references.size() == 0) {
                final RoomReference garden = new RoomReference(RoomType.GARDEN, 4, 4, 1, 1);
                references.add(0, garden);
                garden.getFurnitureData().add(new FurnitureData(FurnitureSpace.CENTREPIECE_SPACE, Furniture.EXIT_PORTAL, new Location(3, 3, 1), 10, 1));
            }
            allocatedArea = MapBuilder.findEmptyChunk(yardSpace, yardSpace);
            chunkX = allocatedArea.getChunkX();
            chunkY = allocatedArea.getChunkY();
            player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 71);
            player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
            final Location location = getCenter();
            if (spawnTile != null) {
                player.setLocation(new Location(chunkX * 8 + spawnTile.getX(), chunkY * 8 + spawnTile.getY(), spawnTile.getPlane()));
            } else {
                player.setLocation(getWorldTile(location.getX(), location.getY(), 1));
            }
            createLandscape();
            createRooms();
            final int regionId = (((chunkX * 8) >> 6) << 8) + ((chunkY * 8) >> 6);
            final Region region = World.getRegion(regionId, true);
            player.lock();
            if (portal == null) {
                portal = getWorldTile(location.getX(), location.getY(), 1);
            }
            fillRooms();
            if (!buildingMode) {
                addDoors();
            }
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    if (player.isLoadingRegion() || region.getLoadStage() != 2) {
                        return;
                    }
                    player.unlock();
                    player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
                    player.getPacketDispatcher().sendSoundEffect(ENTER_SOUND);
                    player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
                    stop();
                }
            }, 0, 0);
        } catch (Exception e) {
            log.error("", e);
        }
        return true;
    }

    public void leaveHouse() {
        MapBuilder.destroy(allocatedArea);
        player.setLocation(new Location(house.getLocation()));
        if (catsOnBlanket.remove(player) != null) {
            if (player.getFollower() != null) {
                player.getFollower().unlock();
            }
        }
    }

    private void createLandscape() {
        for (int x = 0; x < yardSpace; x++) {
            for (int y = 0; y < yardSpace; y++) {
                // final AllocatedArea allocated, final int ratio, final int fromChunkX, final int fromChunkY, final int fromPlane,
                // final int toChunkX, final int toChunkY, final int toPlane, final int rotation
                try {
                    MapBuilder.copySquare(allocatedArea, 1, RoomType.DUNGEON_LAND.getChunkX(), RoomType.DUNGEON_LAND.getChunkY(), decoration == 2 ? 2 : decoration == 3 ? 3 : 0, chunkX + x, chunkY + y, 0, 0);
                    MapBuilder.copySquare(allocatedArea, 1, RoomType.LAND.getChunkX(), RoomType.LAND.getChunkY(), decoration == 2 ? 2 : decoration == 3 ? 3 : 0, chunkX + x, chunkY + y, 1, 0);
                } catch (Exception e) {
                    log.error("", e);
                }
                // MapBuilder.copyChunk(Room.DUNGEON_LAND.getChunkX(), Room.DUNGEON_LAND.getChunkY(), decoration == 2 ?
                // 2 : decoration == 3 ? 3 : 0, chunkX + x, chunkY + y, 0, 0);
                // MapBuilder.copyChunk(Room.LAND.getChunkX(), Room.LAND.getChunkY(), decoration == 2 ? 2 :
                // decoration == 3 ? 3 : 0, chunkX + x, chunkY + y, 1, 0);
            }
        }
    }

    private void createRooms() {
        for (final RoomReference reference : references) {
            if (reference == null) {
                continue;
            }
            addRoof(reference);
            try {
                MapBuilder.copySquare(allocatedArea, 1, reference.getRoom().getChunkX() + (decoration >= 4 ? 8 : 0), reference.getRoom().getChunkY(), decoration & 3, chunkX + reference.getX() - yardOffset, chunkY + reference.getY() - yardOffset, reference.getPlane(), reference.getRotation());
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private void fillRooms() {
        for (final RoomReference reference : references) {
            if (reference == null) {
                continue;
            }
            for (final FurnitureData data : reference.getFurnitureData()) {
                if (data == null) {
                    continue;
                }
                refreshObject(reference, data, false, false);
            }
            refreshWindows(reference);
        }
    }

    private void addDoors() {
        for (final RoomReference room : references) {
            if (room == null) {
                continue;
            }
            if (room.getRoom() == RoomType.FORMAL_GARDEN || room.getRoom() == RoomType.GARDEN || room.getRoom() == RoomType.MENAGERIE_OUTDOORS || room.getRoom() == RoomType.SUPERIOR_GARDEN) {
                continue;
            }
            final int minX = (room.getX() * 8 - (yardOffset * 8)) % 64;
            final int minY = (room.getY() * 8 - (yardOffset * 8)) % 64;
            final int regionId = ((((chunkX + room.getX() - yardOffset) * 8 >> 6) << 8) + (((chunkY + room.getY() - yardOffset) * 8) >> 6));
            final int regionX = (regionId >> 8) << 6;
            final int regionY = (regionId & 255) << 6;
            for (int i = 0; i < DOOR_POSITIONS.length; i++) {
                final int[] coords = DOOR_POSITIONS[i];
                final int x = coords[0];
                final int y = coords[1];
                final int rotation = coords[2];
                final int oppositeX = coords[3];
                final int oppositeY = coords[4];
                final int rx = x == 0 ? -1 : x == 7 ? 1 : 0;
                final int ry = y == 0 ? -1 : y == 7 ? 1 : 0;
                final int offset = decoration == 0 ? (i % 2 == 0 ? 0 : 1) : (i % 2 != 0 ? 0 : 1);
                final WorldObject o = new WorldObject(DOOR_IDS[decoration] + offset, 0, rotation, (regionX + minX + x), regionY + minY + y, room.getPlane());
                final int dir = DirectionUtil.getMoveDirection(oppositeX - x, oppositeY - y);
                final RoomReference ref = getReference(room.getX() + rx, room.getY() + ry, room.getPlane());
                if (ref != null && ref.getRoom() != RoomType.FORMAL_GARDEN && ref.getRoom() != RoomType.GARDEN && ref.getRoom() != RoomType.MENAGERIE_OUTDOORS && ref.getRoom() != RoomType.SUPERIOR_GARDEN) {
                    continue;
                }
                /*if (!RegionMap.checkProjectileStep(room.getPlane(), o.getX(), o.getY(), dir, 1) || room.getPlane()
               == 2 && ref == null) {

                    o.setId(WALL_IDS[decoration]);
                    World.spawnObject(o);
                    continue;
                }*/
                // TODO: Fix clip checks.
                if (!World.isTileFree(o, 1)) {
                    continue;
                }
                if (renderDoorsOpen) {
                    adjustDoor(o, x, y);
                }
                World.spawnObject(o);
            }
        }
    }

    /**
     * Adjusts the door's location and opens it when the render doors open option is enabled.
     *
     * @param door object to adjust.
     */
    private void adjustDoor(final WorldObject door, final int x, final int y) {
        if (y == 0 || y == 7) {
            door.moveLocation(0, y == 7 ? 1 : -1, 0);
            if (x == 3) {
                door.setRotation(0);
            } else if (x == 4) {
                door.setRotation(2);
            }
        } else {
            door.moveLocation(x == 7 ? 1 : -1, 0, 0);
            if (y == 3) {
                door.setRotation(3);
            } else if (y == 4) {
                door.setRotation(1);
            }
        }
        door.setId(door.getId() + 2);
    }

    private void addRoof(final RoomReference reference) {
        final RoomType room = reference.getRoom();
        if (room == RoomType.FORMAL_GARDEN || room == RoomType.GARDEN || room == RoomType.MENAGERIE_OUTDOORS || room == RoomType.SUPERIOR_GARDEN) {
            return;
        }
        for (final RoomReference ref : references) {
            if (ref.getX() == reference.getX() && ref.getY() == reference.getY() && ref.getPlane() == reference.getPlane() + 1) {
                return;
            }
        }
        if (reference.getPlane() > 0) {
            try {
                if (decoration < 4)
                    MapBuilder.copySquare(allocatedArea, 1, RoomType.ROOFS_A.getChunkX(), RoomType.ROOFS_A.getChunkY(), decoration, chunkX + reference.getX() - yardOffset, chunkY + reference.getY() - yardOffset, reference.getPlane() + 1, reference.getRotation());
                else
                    MapBuilder.copySquare(allocatedArea, 1, RoomType.ROOFS_B.getChunkX(), RoomType.ROOFS_B.getChunkY(), decoration, chunkX + reference.getX() - yardOffset, chunkY + reference.getY() - yardOffset, reference.getPlane() + 1, reference.getRotation());
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    public void removeRoom(final RoomReference reference) {
        references.remove(reference);
        enterHouse(true, getRelationalSpawnTile());
    }

    public void removeObject(final RoomReference reference, final WorldObject object) {
        for (final FurnitureData furniture : reference.getFurnitureData()) {
            for (final int i : furniture.getFurniture().getObjectIds()) {
                if (i == object.getId() && object.getXInChunk() == furniture.getLocation().getX() && object.getYInChunk() == furniture.getLocation().getYInChunk()) {
                    refreshObject(reference, furniture, true, true);
                    reference.getFurnitureData().remove(furniture);
                    if (furniture.getSpace() == FurnitureSpace.QUEST_RUG_SPACE || furniture.getSpace() == FurnitureSpace.SKILL_RUG_SPACE) {
                        enterHouse(true, getRelationalSpawnTile());
                    }
                    return;
                }
            }
        }
    }

    public void sendRoomCreationMenu(final WorldObject object) {
        final int xInChunk = player.getLocation().getXInChunk();
        final int yInChunk = player.getLocation().getYInChunk();
        int roomX = player.getLocation().getChunkX() - chunkX + yardOffset;
        int roomY = player.getLocation().getChunkY() - chunkY + yardOffset;
        if (xInChunk == 7) {
            roomX += 1;
        } else if (xInChunk == 0) {
            roomX -= 1;
        } else if (yInChunk == 7) {
            roomY += 1;
        } else if (yInChunk == 0) {
            roomY -= 1;
        }
        final RoomReference reference = getReference(roomX, roomY, object.getPlane());
        if (reference != null) {
            player.getDialogueManager().start(new RoomRemovingD(player, reference));
        } else {
            if (object.getPlane() == 2) {
                final RoomReference bottomRef = getReference(roomX, roomY, 1);
                final RoomType room = bottomRef == null ? null : bottomRef.getRoom();
                if (bottomRef == null || room == RoomType.GARDEN || room == RoomType.FORMAL_GARDEN || room == RoomType.SUPERIOR_GARDEN || room == RoomType.MENAGERIE_OUTDOORS) {
                    player.sendMessage("You can't build a room that would have no room to support it.");
                    return;
                }
            }
            player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 212);
            player.getTemporaryAttributes().put("RoomCreation", new int[] { roomX, roomY, object.getPlane() });
            player.setCloseInterfacesEvent(() -> player.getTemporaryAttributes().remove("RoomCreation"));
        }
    }

    public void buildFurniture(final int slot) {
        if (player.getTemporaryAttributes().get("FurnitureSpace") == null || !(player.getTemporaryAttributes().get("FurnitureSpace") instanceof Object[])) {
            return;
        }
        final Object[] data = (Object[]) player.getTemporaryAttributes().get("FurnitureSpace");
        final Location loc = (Location) data[4];
        final int roomX = loc.getChunkX() - chunkX + yardOffset;
        final int roomY = loc.getChunkY() - chunkY + yardOffset;
        final RoomReference reference = getReference(roomX, roomY, player.getPlane());
        final Location chunkCoords = (Location) data[1];
        final Location objectCoords = getWorldTile(((reference.getX() - yardOffset) * 8) + chunkCoords.getX(), ((reference.getY() - yardOffset) * 8) + chunkCoords.getY(), chunkCoords.getPlane());
        final FurnitureSpace space = (FurnitureSpace) data[0];
        final int type = (int) data[2];
        final int rotation = (int) data[3];
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        final WorldObject object = new WorldObject(space.getFurnitures()[slot].getObjectId(), type, rotation, objectCoords);
        final FurnitureData furnitureData = new FurnitureData(space, space.getFurnitures()[slot], chunkCoords, type, rotation);
        if (player.getPlane() >= 1 && (space == FurnitureSpace.QUEST_HALL_STAIRCASE || space == FurnitureSpace.SKILL_HALL_STAIRCASE)) {
            player.getTemporaryAttributes().put("constStaircase", object);
            player.getTemporaryAttributes().put("constReference", reference);
            player.getTemporaryAttributes().put("constSlot", slot);
            player.getTemporaryAttributes().put("constFurnData", furnitureData);
            player.getDialogueManager().start(new BuildStaircaseD(player, object, reference, furnitureData, slot));
            return;
        }
        build(object, furnitureData, reference, space);
    }

    private void build(final WorldObject object, final FurnitureData data, final RoomReference reference, final FurnitureSpace space) {
        final Furniture furniture = data.getFurniture();
        final int nails = furniture.getNails();
        final int ticks = furniture.getTicks();
        player.lock();
        final Inventory inventory = player.getInventory();
        final boolean water = furniture.isWateringCan();
        final int count = hammer(water ? -1 : object.getType(), nails, inventory, water ? 2 : nails != -1 ? nails : Utils.random(ticks, ticks + 3));
        WorldTasksManager.schedule(new WorldTask() {

            private int amount = count > 10 ? 10 : count;

            private boolean stop;

            @Override
            public void run() {
                if (!stop && amount > 0) {
                    amount = hammer(water ? -1 : object.getType(), nails, inventory, amount);
                }
                if (amount <= 0) {
                    if (!stop) {
                        stop = true;
                        return;
                    }
                    player.getSkills().addXp(SkillConstants.CONSTRUCTION, furniture.getXp());
                    if (water) {
                        player.getSkills().addXp(SkillConstants.FARMING, furniture.getXp());
                    }
                    World.spawnObject(object);
                    reference.getFurnitureData().add(data);
                    refreshObject(reference, data, false, true);
                    player.unlock();
                    if (space == FurnitureSpace.QUEST_RUG_SPACE || space == FurnitureSpace.SKILL_RUG_SPACE) {
                        enterHouse(true, getRelationalSpawnTile());
                    }
                    stop();
                }
            }
        }, 1, 1);
    }

    private int hammer(final int type, final int nails, final Inventory inventory, int count) {
        if (type == 22) {
            player.setAnimation(FLOOR_ANIM);
        } else if (type == 5) {
            player.setAnimation(SHELF_ANIM);
        } else if (type == -1) {
            player.setAnimation(WATERING_ANIM);
        } else {
            player.setAnimation(REG_ANIM);
        }
        if (nails > 0) {
            final int nail = getBestNail(inventory);
            final int id = nail & 16383;
            final boolean stop = nail >> 16 <= 0;
            if (id > 0) {
                inventory.deleteItem(id, 1);
            }
            final int index = ArrayUtils.indexOf(NAILS, id);
            final int chance = Math.max(index * 10, 5);
            if (stop) {
                count = 0;
            } else if (Utils.random(100) >= chance) {
                count--;
            }
        } else {
            count--;
        }
        return count;
    }

    private int getBestNail(final Inventory inventory) {
        int nail = 0;
        long nails = 0;
        for (int i = 27; i >= 0; i--) {
            final Item item = inventory.getItem(i);
            if (item == null) {
                continue;
            }
            final int id = item.getId();
            if (ArrayUtils.contains(NAILS, id)) {
                nails += item.getAmount();
                final int index = ArrayUtils.indexOf(NAILS, id);
                if (index == -1) {
                    continue;
                }
                final int currentIndex = ArrayUtils.indexOf(NAILS, nail);
                if (currentIndex == -1 || currentIndex > index) {
                    nail = NAILS[index];
                }
            }
        }
        if (nails > 16383) {
            nails = 16383;
        }
        return (int) (nail | nails << 16);
    }

    public void buildStaircase(final boolean up, final WorldObject object, final RoomReference ref, final FurnitureData furnitureData, final int slotId) {
        if (object == null || ref == null || furnitureData == null) {
            return;
        }
        if (up) {
            final RoomReference above = this.getReference(ref.getX(), ref.getY(), ref.getPlane() + 1);
            if (above != null) {
                if ((above.getRoom() != RoomType.SKILL_HALL_DS && above.getRoom() != RoomType.QUEST_HALL_DS && above.getRoom() != RoomType.SKILL_HALL && above.getRoom() != RoomType.QUEST_HALL) || ref.getRotation() != above.getRotation()) {
                    player.sendMessage("You cannot connect stairs to the room above.");
                    return;
                }
            }
            ref.getFurnitureData().add(furnitureData);
            enterHouse(true, getRelationalSpawnTile());
        } else {
            final RoomReference below = this.getReference(ref.getX(), ref.getY(), ref.getPlane() - 1);
            if (below != null) {
                if ((below.getRoom() != RoomType.SKILL_HALL_DS && below.getRoom() != RoomType.QUEST_HALL_DS && below.getRoom() != RoomType.SKILL_HALL && below.getRoom() != RoomType.QUEST_HALL && below.getRoom() != RoomType.DUNGEON_STAIRS_ROOM) || ref.getRotation() != below.getRotation()) {
                    player.sendMessage("You cannot connect stairs to the room below.");
                    return;
                }
            }
            final RoomType room = ref.getRoom() == RoomType.SKILL_HALL ? RoomType.SKILL_HALL_DS : ref.getRoom() == RoomType.QUEST_HALL ? RoomType.QUEST_HALL_DS : null;
            if (room == null) {
                return;
            }
            final FurnitureSpace space = room == RoomType.SKILL_HALL_DS ? FurnitureSpace.SKILL_HALL_STAIRCASE_DS : room == RoomType.QUEST_HALL_DS ? FurnitureSpace.QUEST_HALL_STAIRCASE_DS : null;
            if (space == null) {
                return;
            }
            final FurnitureData furniture = new FurnitureData(space, space.getFurnitures()[slotId], furnitureData.getLocation(), furnitureData.getType(), furnitureData.getRotation());
            ref.setRoom(room);
            ref.getFurnitureData().add(furniture);
            enterHouse(true, getRelationalSpawnTile());
        }
    }

    public void removeStaircase(final RoomReference ref, final WorldObject object) {
        removeObject(ref, object);
        final RoomType room = ref.getRoom() == RoomType.SKILL_HALL_DS ? RoomType.SKILL_HALL : ref.getRoom() == RoomType.QUEST_HALL_DS ? RoomType.QUEST_HALL : ref.getRoom();
        if (ref.getRoom() == RoomType.SKILL_HALL || ref.getRoom() == RoomType.SKILL_HALL_DS || ref.getRoom() == RoomType.QUEST_HALL || ref.getRoom() == RoomType.QUEST_HALL_DS || ref.getRoom() == RoomType.DUNGEON_STAIRS_ROOM) {
            if (ref.getPlane() < 2) {
                final RoomReference above = this.getReference(ref.getX(), ref.getY(), ref.getPlane() + 1);
                if (above != null) {
                    final FurnitureData staircase = above.getStaircase();
                    if (staircase != null) {
                        if (staircase.getFurniture().name().contains("_DS")) {
                            above.getFurnitureData().remove(staircase);
                        }
                    }
                }
            }
            if (ref.getPlane() > 0) {
                final RoomReference below = this.getReference(ref.getX(), ref.getY(), ref.getPlane() - 1);
                if (below != null) {
                    final FurnitureData staircase = below.getStaircase();
                    if (staircase != null) {
                        if (!staircase.getFurniture().name().contains("_DS")) {
                            below.getFurnitureData().remove(staircase);
                        }
                    }
                }
            }
        }
        if (room == null) {
            return;
        }
        ref.setRoom(room);
        enterHouse(true, getRelationalSpawnTile());
    }

    /**
     * Transforms the walls where windows are in the house into the real windows, not the transparent crap.
     *
     * @return window id of the respective decoration.
     */
    private int getWindow() {
        switch(decoration) {
            case 0:
                return 13099;
            case 1:
                return 13091;
            case 2:
                return 13005;
            case 3:
                return 13112;
            case 4:
                return 10816;
            case 5:
                return 13117;
            default:
                return 27083;
        }
    }

    /**
     * A separate method to refresh the windows of a built room (for when there are no objects built)
     *
     * @param room room to refresh.
     */
    private void refreshWindows(final RoomReference room) {
        final int minX = (room.getX() * 8 - (yardOffset * 8)) % 64;
        final int minY = (room.getY() * 8 - (yardOffset * 8)) % 64;
        final int regionId = ((((chunkX + room.getX() - yardOffset) * 8 >> 6) << 8) + (((chunkY + room.getY() - yardOffset) * 8) >> 6));
        final Region region = World.getRegion(regionId, true);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                final WorldObject[] objects = region.getObjects(room.getPlane(), minX + x, minY + y);
                if (objects == null) {
                    continue;
                }
                for (final WorldObject object : objects) {
                    if (object == null) {
                        continue;
                    }
                    if (object.getId() == ObjectId.WINDOW_13830) {
                        final WorldObject o = new WorldObject(object);
                        o.setId(getWindow());
                        World.spawnObject(o);
                        continue;
                    }
                    if (!buildingMode) {
                        removeHotspot(object);
                    }
                }
            }
        }
    }

    private void removeHotspot(final WorldObject object) {
        if (object.getDefinitions().containsOption(4, "Build") && World.containsObjectWithId(object, object.getId())) {
            if (object.getDefinitions().getName().equals("Window space")) {
                World.spawnObject(new WorldObject(WALL_IDS[decoration], object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()));
            } else {
                World.removeObject(object);
            }
        }
    }

    /**
     * Refreshes a certain built hotspot.
     */
    private void refreshObject(final RoomReference room, final FurnitureData data, final boolean remove, final boolean adjustingFurniture) {
        // Data-driven rebuild: spawn directly from stored FurnitureData.
        // fillRooms() runs before the dynamic region finishes loading, so
        // refreshObjectsInChunk's region.getObjects() scan finds nothing.
        // buildFurniture's direct World.spawnObject is the only reason live
        // building works; we replicate that here for the initial load path.
        if (!adjustingFurniture && !remove) {
            final Furniture furniture = data.getFurniture();
            final Location loc = data.getLocation();
            final Location worldTile = getWorldTile(((room.getX() - yardOffset) * 8) + loc.getX(), ((room.getY() - yardOffset) * 8) + loc.getY(), loc.getPlane());
            final WorldObject o = new WorldObject(furniture.getObjectId(), data.getType(), data.getRotation(), worldTile);
            if (furniture.getAction() != null) {
                furniture.getAction().onRefresh(this, room, data, o, true);
            }
            if (!buildingMode && o.getId() == 0) {
                World.removeObject(o);
            } else {
                World.spawnObject(o);
            }
            if (o.getId() == 4525 && portal == null) {
                portal = new Location(o.getX() - 1, o.getY() - 1, o.getPlane());
            }
            return;
        }
        final int minX = (room.getX() * 8 - (yardOffset * 8)) % 64;
        final int minY = (room.getY() * 8 - (yardOffset * 8)) % 64;
        final int regionId = ((((chunkX + room.getX() - yardOffset) * 8 >> 6) << 8) + (((chunkY + room.getY() - yardOffset) * 8) >> 6));
        final Region region = World.getRegion(regionId, true);
        if (data.getSpace() == FurnitureSpace.QUEST_HALL_STAIRCASE || data.getSpace() == FurnitureSpace.QUEST_HALL_STAIRCASE_DS || data.getSpace() == FurnitureSpace.SKILL_HALL_STAIRCASE || data.getSpace() == FurnitureSpace.SKILL_HALL_STAIRCASE_DS) {
            refreshHalls(data, true, room.getPlane(), minX, minY, region);
        } else if (data.getSpace() == FurnitureSpace.QUEST_RUG_SPACE || data.getSpace() == FurnitureSpace.SKILL_RUG_SPACE) {
            refreshHalls(data, false, room.getPlane(), minX, minY, region);
        }
        refreshObjectsInChunk(room, data, remove, region, minX, minY, adjustingFurniture);
    }

    /**
     * Refreshes all the tied objects in a specific chunk.
     */
    private void refreshObjectsInChunk(final RoomReference room, final FurnitureData data, final boolean remove, final Region region, final int minX, final int minY, final boolean adjustingFurniture) {
        int refreshType = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                final WorldObject[] objects = region.getObjects(room.getPlane(), minX + x, minY + y);
                if (objects == null) {
                    continue;
                }
                for (final WorldObject object : objects) {
                    if (object == null) {
                        continue;
                    }
                    if (data.getSpace() == null) {
                        continue;
                    }
                    for (int i = 0; i < data.getSpace().getSpotIds().length; i++) {
                        if (object.getId() == data.getSpace().getSpotIds()[i]) {
                            if (remove) {
                                final WorldObject o = new WorldObject(object);
                                o.setId(data.getSpace().getSpotIds()[i]);
                                World.spawnObject(o);
                            } else {
                                final WorldObject o = new WorldObject(object);
                                final Furniture furniture = data.getFurniture();
                                o.setId(data.getFurniture().getObjectIds()[i]);
                                if (furniture.getAction() != null) {
                                    furniture.getAction().onRefresh(this, room, data, o, !adjustingFurniture);
                                }
                                if (adjustingFurniture) {
                                    if (furniture.getRefreshType() == 2 && refreshType != 2) {
                                        refreshType = 2;
                                    } else if (furniture.getRefreshType() == 1 && refreshType != 2) {
                                        refreshType = 1;
                                    }
                                }
                                if (!buildingMode && o.getId() == 0) {
                                    World.removeObject(o);
                                } else {
                                    World.spawnObject(o);
                                }
                                if (o.getId() == 4525 && portal == null) {
                                    portal = new Location(o.getX() - 1, o.getY() - 1, o.getPlane());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (refreshType == 1) {
            for (final FurnitureData dat : room.getFurnitureData()) {
                if (dat == null) {
                    continue;
                }
                refreshObject(room, dat, false, false);
            }
        } else if (refreshType == 2) {
            enterHouse(true, getRelationalSpawnTile());
        }
    }

    private void refreshHalls(final FurnitureData data, final boolean rug, final int plane, final int minX, final int minY, final Region region) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                final WorldObject[] objects = region.getObjects(plane, minX + x, minY + y);
                if (objects == null) {
                    continue;
                }
                for (final WorldObject object : objects) {
                    if (object == null) {
                        continue;
                    }
                    if (rug && object.getDefinitions().getName().equals("Rug space") || !rug && object.getDefinitions().getName().equals("Stair Space")) {
                        World.removeObject(object);
                    }
                }
            }
        }
    }

    public void sendFurnitureCreationMenu(final WorldObject object) {
        final FurnitureSpace space = FurnitureSpace.getSpaceByObject(object);
        if (space == null) {
            player.sendMessage("SPACE IS NULL: " + object.getId());
            return;
        }
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 458);
        String materials = "";
        final List<String> args = new ArrayList<String>();
        for (final Furniture furniture : space.getFurnitures()) {
            materials = ItemDefinitions.get(furniture.getItemId()).getName() + "|";
            for (final Item item : furniture.getMaterials()) {
                if (item == null) {
                    continue;
                }
                materials += item.getDefinitions().getName() + ": " + item.getAmount() + "<br>";
            }
            args.add(materials);
        }
        for (int i = 0; i < space.getFurnitures().length; i++) {
            int dbRow = ITEM_TO_DBROW.getOrDefault(space.getFurnitures()[i].getItemId(), -1);
            player.getPacketDispatcher().sendClientScript(1404, i + 1, dbRow, space.getFurnitures()[i].getLevel(), args.get(i), 1);
        }
        player.getPacketDispatcher().sendClientScript(1406, space.getFurnitures().length, 0);
        final Object[] data = new Object[] { space, new Location(object.getXInChunk(), object.getYInChunk(), object.getPlane()), object.getType(), object.getRotation(), object };
        player.getTemporaryAttributes().put("FurnitureSpace", data);
    }

    public void createRoom(final RoomReference reference) {
        final Location location = new Location(player.getLocation().getXInRegion(), player.getLocation().getYInRegion(), player.getPlane());
        references.add(reference);
        final RoomType room = reference.getRoom();
        if (room == RoomType.SKILL_HALL || room == RoomType.QUEST_HALL || room == RoomType.SKILL_HALL_DS || room == RoomType.QUEST_HALL_DS) {
            if (reference.getPlane() == 1) {
                final RoomReference below = this.getReference(reference.getX(), reference.getY(), reference.getPlane() - 1);
                if (below != null && below.getRotation() == reference.getRotation()) {
                    final RoomType bRoom = below.getRoom();
                    if (bRoom == RoomType.SKILL_HALL || bRoom == RoomType.QUEST_HALL) {
                        final FurnitureData staircase = below.getStaircase();
                        if (staircase != null) {
                            reference.setRoom(room == RoomType.SKILL_HALL ? RoomType.SKILL_HALL_DS : RoomType.QUEST_HALL_DS);
                            final String name = staircase.getFurniture().name();
                            final FurnitureData aboveStaircase = new FurnitureData(room == RoomType.QUEST_HALL ? FurnitureSpace.QUEST_HALL_STAIRCASE_DS : FurnitureSpace.SKILL_HALL_STAIRCASE_DS, Furniture.valueOf(Furniture.class, name + "_DS"), new Location(staircase.getLocation().getX(), staircase.getLocation().getY(), staircase.getLocation().getPlane() + 1), staircase.getType(), staircase.getRotation());
                            reference.getFurnitureData().add(aboveStaircase);
                        }
                    }
                }
            }
        }
        player.getInventory().deleteItem(new Item(995, reference.getRoom().getPrice()));
        player.getPacketDispatcher().sendComponentText(370, 16, "Number of rooms: " + getAmountOfRooms());
        player.getTemporaryAttributes().remove("CreatingRoom");
        if (player.getTemporaryAttributes().get("yard") != null) {
            final int yard = (int) player.getTemporaryAttributes().get("yard");
            final int yardOffset = (10 - generateYardSpace()) / 2;
            if (yard != yardOffset) {
                final int offset = yard - yardOffset;
                location.moveLocation(offset * 8, offset * 8, 0);
            }
        }
        enterHouse(true, getRelationalSpawnTile());
    }

    /**
     * if (reference.getPlane() == 0) { final RoomReference below = this.getReference(reference.getX(), reference.getY(),
     * reference.getPlane() + 1); if (below != null) { final Room bRoom = below.getRoom(); if (bRoom == Room.SKILL_HALL || bRoom ==
     * Room.QUEST_HALL) { final FurnitureData staircase = below.getStaircase(); if (staircase == null) { final FurnitureData carpet
     * = below.getCarpet(); if (carpet == null) { final String name = staircase.getFurniture().name(); final FurnitureData
     * aboveStaircase = new FurnitureData(room == Room.QUEST_HALL ? FurnitureSpace.QUEST_HALL_STAIRCASE :
     * FurnitureSpace.SKILL_HALL_STAIRCASE, Furniture.valueOf(Furniture.class, name.substring(0, name.length() - 3)), new
     * WorldTile(staircase.getLocation().getX(), staircase.getLocation().getY(), staircase.getLocation().getPlane() - 1),
     * staircase.getType(), staircase.getRotation()); reference.getFurnitureData().enqueue(aboveStaircase); } } } } }
     */
    public void createRoom(final int slot) {
        final RoomType[] rooms = RoomType.VALUES;
        final int[] location = (int[]) player.getTemporaryAttributes().get("RoomCreation");
        final int[] offsetLocation = new int[] { location[0] - yardOffset, location[1] - yardOffset };
        final RoomType room = RoomType.getRoomBySlot(slot);
        if (slot - 1 > rooms.length || room == null || location == null) {
            return;
        }
        if (offsetLocation[0] < 0 || offsetLocation[1] < 0 || offsetLocation[0] > yardSpace || offsetLocation[1] > yardSpace) {
            player.sendMessage("You can't place a room here.");
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
            return;
        }
        if (player.getPlane() == 0) {
            if (room == RoomType.FORMAL_GARDEN || room == RoomType.GARDEN || room == RoomType.MENAGERIE_OUTDOORS || room == RoomType.SUPERIOR_GARDEN) {
                player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
                player.getDialogueManager().start(new PlainChat(player, "This room can only be built on the ground."));
                return;
            }
        } else {
            if (room == RoomType.DUNGEON_CORRIDOR || room == RoomType.DUNGEON_JUNCTION || room == RoomType.DUNGEON_STAIRS_ROOM || room == RoomType.OUBLIETTE) {
                player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
                player.getDialogueManager().start(new PlainChat(player, "This room can only be built at the bottom."));
                return;
            }
        }
        if ((player.getSkills().getLevel(SkillConstants.CONSTRUCTION)) < room.getLevel()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Construction level of " + room.getLevel() + " to build that room."));
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
            return;
        }
        if (player.getInventory().getAmountOf(995) < room.getPrice()) {
            player.sendMessage("You can't afford to build that room.");
            return;
        }
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        player.getDialogueManager().start(new RoomCreationD(player, new RoomReference(room, location[0], location[1], location[2], 0)));
    }

    public void roomPreview(final RoomReference reference, final boolean remove) {
        final int realChunkX = reference.getRoom().getChunkX() + (decoration >= 4 ? 8 : 0);
        final int realChunkY = reference.getRoom().getChunkY();
        final int boundX = (chunkX * 8 + reference.getX() * 8) - (yardOffset * 8);
        final int boundY = (chunkY * 8 + reference.getY() * 8) - (yardOffset * 8);
        final Region region = World.getRegion(_Location.getRegionId(realChunkX * 8, realChunkY * 8), true);
        if (reference.getPlane() == 0) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    final WorldObject empty = new WorldObject(-1, 10, reference.getRotation(), boundX + x, boundY + y, reference.getPlane());
                    if (remove) {
                        World.removeObject(empty);
                    } else {
                        World.spawnObject(empty);
                    }
                }
            }
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                final WorldObject[] objects = region.getObjects(decoration & 3, realChunkX * 8 + x & 63, realChunkY * 8 + y & 63);
                if (objects != null) {
                    for (final WorldObject object : objects) {
                        if (object == null) {
                            continue;
                        }
                        final ObjectDefinitions defs = object.getDefinitions();
                        if (object.getType() == 22) {
                            continue;
                        }
                        if (reference.getPlane() == 0 || defs.containsOption(4, "Build")) {
                            final WorldObject previewObject = new WorldObject(object);
                            final int[] coords = CoordinateUtilities.translate(x, y, reference.getRotation(), defs.getSizeX(), defs.getSizeY(), object.getRotation());
                            previewObject.setLocation(new Location(boundX + coords[0], boundY + coords[1], reference.getPlane()));
                            previewObject.setRotation((object.getRotation() + reference.getRotation()) & 3);
                            if (remove) {
                                World.removeObject(previewObject);
                            } else {
                                World.spawnObject(previewObject);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getAmountOfRooms() {
        return references == null ? 1 : references.size();
    }

    /**
     * Gets the offset caused by different size yards. Size of the yard is dependant on player's house size.
     *
     * @return chunk offset by yard size.
     */
    private int generateYardOffset() {
        return (10 - yardSpace) / 2;
    }

    public RoomReference getReference(final int positionX, final int positionY, final int plane) {
        for (final RoomReference reference : references) {
            if (reference.getX() == positionX && reference.getY() == positionY && reference.getPlane() == plane) {
                return reference;
            }
        }
        return null;
    }

    public RoomReference getReference(final Location tile) {
        final int roomX = tile.getChunkX() - chunkX + yardOffset;
        final int roomY = tile.getChunkY() - chunkY + yardOffset;
        return getReference(roomX, roomY, tile.getPlane());
    }

    private int generateYardSpace() {
        final int level = player.getSkills().getLevelForXp(SkillConstants.CONSTRUCTION);
        return level >= 75 ? 9 : 5 + (int) (level / 15.0F);
    }

    private Location getWorldTile(final int mapX, final int mapY, final int plane) {
        return new Location(chunkX * 8 + mapX, chunkY * 8 + mapY, plane);
    }

    public Location getCenter() {
        switch(generateYardSpace()) {
            case 5:
            case 6:
                return SMALL_CENTER;
            case 7:
            case 8:
                return MEDIUM_CENTER;
            default:
                return LARGE_CENTER;
        }
    }

    public List<RoomReference> getReferences() {
        return references;
    }

    public Map<Player, Integer> getCatsOnBlanket() {
        return catsOnBlanket;
    }

    public List<Integer> getDisplayJars() {
        return displayJars;
    }

    public ArmourCase getArmourCase() {
        return armourCase;
    }

    public FancyDressBox getFancyDressBox() {
        return fancyDressBox;
    }

    public ToyBox getToyBox() {
        return toyBox;
    }

    public MagicWardrobe getMagicWardrobe() {
        return magicWardrobe;
    }

    public CapeRack getCapeRack() {
        return capeRack;
    }

    public TreasureChest getTreasureChest() {
        return treasureChest;
    }

    public void sendCostumeContainer(Player player) {
        Container container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.STORAGE_ROOM, Optional.empty());
        for (int[] pieces : armourCase.getItems().values())
            for (int id : pieces) container.add(new Item(id, 1));
        for (int[] pieces : capeRack.getItems().values())
            for (int id : pieces) container.add(new Item(id, 1));
        for (int[] pieces : fancyDressBox.getItems().values())
            for (int id : pieces) container.add(new Item(id, 1));
        for (int[] pieces : magicWardrobe.getItems().values())
            for (int id : pieces) container.add(new Item(id, 1));
        for (int[] pieces : toyBox.getItems().values())
            for (int id : pieces) container.add(new Item(id, 1));
        for (int[] pieces : treasureChest.getItems().values())
            for (int id : pieces) container.add(new Item(id, 1));
        container.setFullUpdate(true);
        player.getPacketDispatcher().sendUpdateItemContainer(32768 + ContainerType.STORAGE_ROOM.getId(), -1, 0, container);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isBuildingMode() {
        return buildingMode;
    }

    public void setBuildingMode(boolean buildingMode) {
        this.buildingMode = buildingMode;
    }

    public boolean isRenderDoorsOpen() {
        return renderDoorsOpen;
    }

    public void setRenderDoorsOpen(boolean renderDoorsOpen) {
        this.renderDoorsOpen = renderDoorsOpen;
    }

    public boolean isTeleportInside() {
        return teleportInside;
    }

    public void setTeleportInside(boolean teleportInside) {
        this.teleportInside = teleportInside;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getYardSpace() {
        return yardSpace;
    }

    public int getYardOffset() {
        return yardOffset;
    }

    public int getDecoration() {
        return decoration;
    }

    public void setDecoration(int decoration) {
        this.decoration = decoration;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public HouseViewer getHouseViewer() {
        return houseViewer;
    }

    public void setHouseViewer(HouseViewer houseViewer) {
        this.houseViewer = houseViewer;
    }

    public TipJar getTipJar() {
        return tipJar;
    }

    public void setTipJar(TipJar tipJar) {
        this.tipJar = tipJar;
    }

    public int getServantsCash() {
        return servantsCash;
    }

    public void setServantsCash(int servantsCash) {
        this.servantsCash = servantsCash;
    }
}
