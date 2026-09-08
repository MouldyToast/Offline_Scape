package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.content.achievementdiary.diaries.FaladorDiary
import org.jesse.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M008_AutoCompleteFaladorHaircutDiary : GameMigration {
    override fun run(player: Player) {
        player.achievementDiaries.update(FaladorDiary.GET_A_HAIRCUT)
    }

    override fun id(): Int = 8
}