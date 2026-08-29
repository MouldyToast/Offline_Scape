package com.near_reality.game.content.origins.di

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Di : NPCSpawnsScript() {

    init {
        DI_AHRIM_THE_BLIGHTED(2330, 9818, 0, walkRadius = 0)
        DI_AHRIM_THE_BLIGHTED(2330, 9820, 0, walkRadius = 0)
        DI_AHRIM_THE_BLIGHTED(2330, 9822, 0, walkRadius = 0)
        DI_AHRIM_THE_BLIGHTED(2330, 9824, 0, walkRadius = 0)
        DI_AHRIM_THE_BLIGHTED(2330, 9826, 0, walkRadius = 0)

        DI_DHAROK_THE_WRETCHED(2332, 9818, 0, walkRadius = 0)
        DI_DHAROK_THE_WRETCHED(2332, 9820, 0, walkRadius = 0)
        DI_DHAROK_THE_WRETCHED(2332, 9822, 0, walkRadius = 0)
        DI_DHAROK_THE_WRETCHED(2332, 9824, 0, walkRadius = 0)
        DI_DHAROK_THE_WRETCHED(2332, 9826, 0, walkRadius = 0)

        DI_GUTHAN_THE_INFESTED(2334, 9818, 0, walkRadius = 0)
        DI_GUTHAN_THE_INFESTED(2334, 9820, 0, walkRadius = 0)
        DI_GUTHAN_THE_INFESTED(2334, 9822, 0, walkRadius = 0)
        DI_GUTHAN_THE_INFESTED(2334, 9824, 0, walkRadius = 0)
        DI_GUTHAN_THE_INFESTED(2334, 9826, 0, walkRadius = 0)

        DI_KARIL_THE_TAINTED(2336, 9818, 0, walkRadius = 0)
        DI_KARIL_THE_TAINTED(2336, 9820, 0, walkRadius = 0)
        DI_KARIL_THE_TAINTED(2336, 9822, 0, walkRadius = 0)
        DI_KARIL_THE_TAINTED(2336, 9824, 0, walkRadius = 0)
        DI_KARIL_THE_TAINTED(2336, 9826, 0, walkRadius = 0)

        DI_TORAG_THE_CORRUPTED(2338, 9818, 0, walkRadius = 0)
        DI_TORAG_THE_CORRUPTED(2338, 9820, 0, walkRadius = 0)
        DI_TORAG_THE_CORRUPTED(2338, 9822, 0, walkRadius = 0)
        DI_TORAG_THE_CORRUPTED(2338, 9824, 0, walkRadius = 0)
        DI_TORAG_THE_CORRUPTED(2338, 9826, 0, walkRadius = 0)

        DI_VERAC_THE_DEFILED(2340, 9818, 0, walkRadius = 0)
        DI_VERAC_THE_DEFILED(2340, 9820, 0, walkRadius = 0)
        DI_VERAC_THE_DEFILED(2340, 9822, 0, walkRadius = 0)
        DI_VERAC_THE_DEFILED(2340, 9824, 0, walkRadius = 0)
        DI_VERAC_THE_DEFILED(2340, 9826, 0, walkRadius = 0)
    }
}
