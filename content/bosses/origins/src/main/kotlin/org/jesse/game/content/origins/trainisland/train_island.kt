package org.jesse.game.content.origins.trainisland

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class TrainIsland : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.24.2025
         */

        2592(2391, 4768, 0, walkRadius = 0)
        2592(2393, 4770, 0, walkRadius = 0)
        2592(2393, 4768, 0, walkRadius = 0)


        HOBGOBLIN(2399, 4768, 0, walkRadius = 0)
        HOBGOBLIN(2400, 4767, 0, walkRadius = 0)
        HOBGOBLIN(2399, 4766, 0, walkRadius = 0)
        HOBGOBLIN(2402, 4768, 0, walkRadius = 0)
        HOBGOBLIN(2403, 4768, 0, walkRadius = 0)
        HOBGOBLIN(2398, 4770, 0, walkRadius = 0)

        DWARF(2407, 4772, 0, walkRadius = 0)
        DWARF(2408, 4773, 0, walkRadius = 0)
        DWARF(2409, 4772, 0, walkRadius = 0)
        DWARF(2408, 4771, 0, walkRadius = 0)
        DWARF(2408, 4770, 0, walkRadius = 0)
        DWARF(2411, 4774, 0, walkRadius = 0)

        GUARD(2409, 4776, 0, walkRadius = 0)
        GUARD(2406, 4778, 0, walkRadius = 0)
        GUARD(2407, 4779, 0, walkRadius = 0)
        GUARD(2411, 4778, 0, walkRadius = 0)

        GUARD(2398, 4782, 0, walkRadius = 0)
        GUARD(2400, 4780, 0, walkRadius = 0)

        FIRE_GIANT(2411, 4782, 0, walkRadius = 0)
        FIRE_GIANT(2413, 4781, 0, walkRadius = 0)
        FIRE_GIANT(2409, 4783, 0, walkRadius = 0)
        FIRE_GIANT(2407, 4785, 0, walkRadius = 0)
        FIRE_GIANT(2407, 4782, 0, walkRadius = 0)

        ICE_GIANT(2397, 4788, 0, walkRadius = 0)
        ICE_GIANT(2399, 4788, 0, walkRadius = 0)
        ICE_GIANT(2401, 4788, 0, walkRadius = 0)
        ICE_GIANT(2403, 4787, 0, walkRadius = 0)
        ICE_GIANT(2400, 4786, 0, walkRadius = 0)

        MOSS_GIANT(2396, 4785, 0, walkRadius = 0)
        MOSS_GIANT(2394, 4786, 0, walkRadius = 0)
        MOSS_GIANT(2392, 4786, 0, walkRadius = 0)
        MOSS_GIANT(2390, 4785, 0, walkRadius = 0)
        MOSS_GIANT(2392, 4783, 0, walkRadius = 0)

        JELLY(2388, 4783, 0, walkRadius = 0)
        JELLY(2386, 4782, 0, walkRadius = 0)
        JELLY(2388, 4785, 0, walkRadius = 0)

        LESSER_DEMON(2387, 4780, 0, walkRadius = 0)
        LESSER_DEMON(2388, 4778, 0, walkRadius = 0)
        LESSER_DEMON(2390, 4778, 0, walkRadius = 0)
        LESSER_DEMON(2386, 4778, 0, walkRadius = 0)
        LESSER_DEMON(2388, 4776, 0, walkRadius = 0)
        LESSER_DEMON(2387, 4780, 0, walkRadius = 0)

        ENRAGED_BARBARIAN_SPIRIT(2385, 4773, 0, walkRadius = 0)
        ENRAGED_BARBARIAN_SPIRIT(2387, 4773, 0, walkRadius = 0)
        ENRAGED_BARBARIAN_SPIRIT(2386, 4771, 0, walkRadius = 0)
        ENRAGED_BARBARIAN_SPIRIT(2385, 4771, 0, walkRadius = 0)

        GIANT_RAT(2400, 4779, 0, walkRadius = 0)
        GIANT_RAT(2401, 4776, 0, walkRadius = 0)
        GIANT_RAT(2402, 4773, 0, walkRadius = 0)

        MAN(2398, 4777, 0, walkRadius = 0)
        MAN(2397, 4775, 0, walkRadius = 0)
        MAN(2395, 4775, 0, walkRadius = 0)
    }
}
