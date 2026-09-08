package org.jesse.game.content.tormented_demon.items

import org.jesse.game.content.seq
import org.jesse.game.content.consumables.Consumable
import org.jesse.game.item.ids.*
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.flooritem.FloorItem
import org.jesse.plugins.flooritem.FloorItemPlugin
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-19
 */
class SmoulderingGland : FloorItemPlugin {

    override fun getItems(): IntArray =
        intArrayOf(SMOULDERING_GLAND)

    override fun handle(player: Player?, item: FloorItem?, optionId: Int, option: String?) {
        if (player == null || item == null) return
        player seq Animation.STOMP.id
        player.graphics = Graphics(2112)
        schedule(PrayerPointRegeneration(player), 0, 0)
        player.sendMessage("You crush the gland, releasing its unholy blessing.")
        World.destroyFloorItem(item)
    }

    class PrayerPointRegeneration(val player: Player) : WorldTask {
        var ticks: Int = 0
        private val demonRegions = intArrayOf(16196, 16197, 16452, 16453)
        override fun run() {
            if (!demonRegions.contains(player.location.regionId)) {
                stop()
                return
            }
            if (ticks++ % 10 == 0)
                Consumable.Restoration(SkillConstants.PRAYER, 0.25f, Random.nextInt(10, 15))

            if (ticks >= 85)
                stop()
        }
    }
}