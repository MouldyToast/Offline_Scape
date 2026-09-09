package mgi.tools.parser;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.Regions;

import java.util.function.Predicate;

public class MapChanges {
    public static byte[] modifyRegionData(int region, byte[] inputData) {
        byte[] newData = inputData;
        switch (region) {
            case 13139 -> newData = addObjects(inputData,
                    new WorldObject(35020, 10, 0, new Location(3279, 5345, 2)),
                    new WorldObject(35020, 10, 0, new Location(3312, 5344, 2))
            );
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
