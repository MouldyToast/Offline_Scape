package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12342 : NPCSpawnsScript() {

        init {
        // Edgeville bank
        BANKER_1613(3096, 3489, 0, WEST, 0)
        BANKER_1618(3096, 3491, 0, WEST, 0)
        BANKER_1618(3096, 3492, 0, NORTH, 0)
        BANKER_1613(3098, 3492, 0, NORTH, 0)

        // Edgeville general store
        SHOP_KEEPER_2821(3079, 3512, 0, SOUTH, 0)
        SHOP_ASSISTANT_2822(3081, 3512, 0, SOUTH, 0)

        // Edgeville inhabitants
        DORIS(3079, 3491, 0, NORTH, 0)
        EMBLEM_TRADER(3096, 3505, 0, SOUTH, 0)
        RICHARD_2200(3101, 3518, 0, NORTH, 0)
        KRYSTILIA(3109, 3516, 0, SOUTH, 0)
        LESSER_FANATIC(3121, 3516, 0, SOUTH, 3)
        LUNA(3121, 3516, 0, SOUTH, 3)
        HARI(3132, 3509, 0, SOUTH, 3)
        MARLEY_10662(3088, 3471, 0, SOUTH, 3)

        // Men
        MAN_3106(3097, 3508, 0, NORTH, 5)
        MAN_3106(3102, 3509, 0, EAST, 5)
        MAN_3107(3098, 3511, 0, NORTH, 5)
        MAN_3108(3093, 3511, 0, EAST, 5)
        MAN_3108(3095, 3509, 0, WEST, 5)
        MAN_3108(3092, 3508, 0, SOUTH, 5)

        // Krystilia — corrected from (3109, 3514, SOUTH)
        KRYSTILIA(3109, 3516, 0, EAST, 0)

        // Rats
        RAT_2854(3124, 3488, 0, EAST, 5)
        RAT_2854(3121, 3485, 0, WEST, 5)
        RAT_2854(3121, 3482, 0, NORTH, 5)

        // Guards
        GUARD_11923(3114, 3512, 0, NORTH, 5)
        GUARD_11923(3085, 3518, 0, SOUTH, 5)
        GUARD_11924(3114, 3517, 0, EAST, 5)
        GUARD_3254(3110, 3515, 0, WEST, 5)
        GUARD_3254(3093, 3518, 0, WEST, 5)

        // Outlaws
        OUTLAW(3118, 3474, 0, EAST, 5)
        OUTLAW_4168(3119, 3472, 0, EAST, 5)
        OUTLAW_4169(3121, 3472, 0, NORTH, 5)
        OUTLAW_4170(3123, 3473, 0, EAST, 5)
        OUTLAW_4171(3124, 3476, 0, SOUTH, 5)
        OUTLAW_4172(3123, 3477, 0, WEST, 5)
        OUTLAW_4173(3119, 3477, 0, EAST, 5)
        OUTLAW_4174(3117, 3477, 0, WEST, 5)
        OUTLAW_4175(3116, 3473, 0, EAST, 5)
        OUTLAW_4176(3118, 3474, 0, NORTH, 5)

        // Giant spiders
        GIANT_SPIDER_3017(3134, 3484, 0, EAST, 10)
        GIANT_SPIDER_3017(3134, 3488, 0, EAST, 10)
        GIANT_SPIDER_3017(3132, 3485, 0, WEST, 10)

        // Existing spawns
        IMP_5007(3134, 3487, 0, SOUTH, 100)
    }
}
