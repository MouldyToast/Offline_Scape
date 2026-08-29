package mgi.tools.parser;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.Regions;

import java.util.function.Predicate;

public class MapChanges {
    public static byte[] modifyRegionData(int region, byte[] inputData) {
        byte[] newData = inputData;
        switch (region) {
            case 13139 -> newData = addObjects(inputData,
                    new WorldObject(35020, 10, 0, new Location(3279, 5345, 2)),
                    new WorldObject(35020, 10, 0, new Location(3312, 5344, 2))
            );
            case 6582 -> {
                newData = addObjects(inputData,
                    new WorldObject(36594, 10, 0, new Location(1650, 11680, 0)),
                    new WorldObject(60501, 10, 0, new Location(1655, 11679, 0)),
                    new WorldObject(34856, 10, 0, new Location(1654, 11681, 0)),
                    new WorldObject(34856, 10, 0, new Location(1654, 11678, 0)),
                    new WorldObject(34856, 10, 0, new Location(1657, 11678, 0)),
                    new WorldObject(34856, 10, 0, new Location(1657, 11681, 0)),
                    new WorldObject(60502, 10, 0, new Location(1654, 11683, 0)));

                newData = editObjects(newData,
                        o -> {
                            if(o.getId() == 33318)
                                return true;
                            if(o.getId() == 660)
                                return true;
                            if(o.getId() == 661)
                                return true;
                            if(o.getId() == 1457)
                                return true;
//                            if(o.getId() == 197 && !o.matches(new Location(1650, 11680, 0))){
//                                o.setType(10);
//                                o.setId(34856);
//                            }
                            if(o.getId() == 197) {
                                return true;
                            }
                            return false;
                        });
            }
            case 13395 -> newData = addObjects(inputData,
                    new WorldObject(35020, 10, 0, new Location(3343, 5346, 2))
            );
            // Duke Sucellus Instance
            case 12132 -> newData =  editObjects(inputData,
                    o -> {
                        if (o.getId() == 47534) {
                            o.setId(47528);
                        } else if (o.getId() == 47532) {
                            o.setId(47524);
                        } else if (o.getId() == 47523) {
                            o.setId(47522);
                        }else if (o.getId() == 47537) {
                            o.setId(47536);
                        }
                        return false;
                    });
        }
        return newData;
    }

    private static byte[] addObjects(byte[] input, WorldObject... objs) {
        return Regions.inject(input, null, objs);
    }

    private static byte[] editObjects(byte[] input, Predicate<WorldObject> pred) {
        return Regions.inject(input, pred);
    }
}
