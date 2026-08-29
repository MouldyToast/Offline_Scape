package org.jire.wmpacker

import com.zenyte.game.world.entity.Location
import org.jire.wmpacker.WorldMapAreas.changeArea
import java.util.concurrent.ExecutorService

/**
 * @author Jire
 */
object CustomWorldMapAreas {

    @JvmStatic
    fun changeGodwarsArea(service: ExecutorService) = changeArea(service, "godwars") {
        updateFullChunks(11602, 11601, 0, 1, 4)
        updateFullChunks(11603, 2)
        updateFullChunks(11346, 2)
        updateFullChunks(11347, 2)
    }

    @JvmStatic
    fun changeMainArea(service: ExecutorService) = changeArea(service, "main") {
        name = "Surface"

        /* Here be a wild mole */
        addMapElement(2500, Location(3136, 3916, 0))

//        /* Donator Island */
//        addMapElement(2503, Location(1663, 2615, 0))
//        addMapElement(2504, Location(1627, 2615, 0))
//        addMapElement(2505, Location(1663, 2663, 0))
//        addMapElement(2506, Location(1699, 2623, 0))
//        addMapElement(2507, Location(1663, 2594, 0))
//        addMapElement(2502, Location(1714, 2652, 0))
//        addMapElement(2501, Location(1755, 2687, 0))
//        addMapElement(2510, Location(1759, 2716, 0))
//        addMapElement(2508, Location(1824, 2716, 0))
//        addMapElement(2509, Location(1824, 2653, 0))
//
//        /* Edge Island */
//        addMapElement(2511, Location(3412, 3697, 0))
//        /* 317 Mobs in Wildy */
//        addMapElement(2512, Location(3201, 3880, 0))
//        addMapElement(2513, Location(2975, 3954, 0))
//        addMapElement(2514, Location(2982, 3907, 0))
//        addMapElement(2514, Location(2985, 3798, 0))

        update(9517, 0)
        update(11828, 0)
        update(11829, 0)
        update(11830, 0)
        update(11831, 0)
        update(12084, 0)
        update(12085, 0)
        update(12086, 0)
        update(12087, 0)
        update(12340, 0)
        update(12341, 0)
        update(12342, 0)
        update(12343, 0)
        update(12596, 0)
        update(12597, 0)
        update(12598, 0)
        update(12599, 0)
//        update(13625, 0)
//        update(13369, 0)
//        update(6441, 0)
//        update(6440, 0)
//        update(6697, 0)
//        update(6696, 0)
    }

}
