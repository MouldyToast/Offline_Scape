package com.near_reality.game.migrations.impl

import com.zenyte.game.content.achievementdiary.achievementDiaries
import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary
import com.zenyte.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M008_AutoCompleteFaladorHaircutDiary : GameMigration {
    override fun run(player: Player) {
        player.achievementDiaries().update(FaladorDiary.GET_A_HAIRCUT)
    }

    override fun id(): Int = 8
}