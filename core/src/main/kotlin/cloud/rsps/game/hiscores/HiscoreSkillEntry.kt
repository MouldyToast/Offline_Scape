package cloud.rsps.game.hiscores

import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
abstract class HiscoreSkillEntry(
    id: Int,
    name: String,
    spriteId: Int
) : HiscoreEntry(id, name, spriteId) {

    abstract fun getLevel(player: Player): Short

    abstract fun getXp(player: Player): Int

}
