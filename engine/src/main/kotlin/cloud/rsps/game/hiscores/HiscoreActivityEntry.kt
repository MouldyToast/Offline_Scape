package cloud.rsps.game.hiscores

import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
abstract class HiscoreActivityEntry(
    id: Int,
    name: String,
    spriteId: Int
) : HiscoreEntry(id, name, spriteId) {

    abstract fun getScore(player: Player): Long

}
