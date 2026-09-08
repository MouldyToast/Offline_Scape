package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.content.boss.nex.NexGodwarsInstance
import org.jesse.game.content.godwars.GodType
import org.jesse.game.content.godwars.instance.GodwarsInstance
import org.jesse.game.content.skills.magic.spells.teleports.RegularTeleport
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import java.util.concurrent.TimeUnit
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class AncientAltarObjectaction : ObjectActionScript() {

    val Player.ancientAltarCooldownCompleted: Boolean
        get() {
            val currentTime = Utils.currentTimeMillis()
            val cooldownTime = getNumericAttribute("ancient-altar-delay").toLong()
            return if (cooldownTime > currentTime) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(cooldownTime - currentTime)
                sendMessage("The gods blessed you recently. They will ignore your prayers for another " + if (minutes <= 1) "minute." else "$minutes minutes.")
                false
            } else {
                attributes["ancient-altar-delay"] = currentTime + TimeUnit.MINUTES.toMillis(10L)
                true
            }
        }

    init {
        ALTAR_42965 {
            when (option) {
                "Teleport" -> {
                    val location = if (player.area is NexGodwarsInstance) Location((player.area as NexGodwarsInstance).getX(2897), (player.area as NexGodwarsInstance).getY(5203)) else Location(2897, 5203)
                    RegularTeleport(location).teleport(player)
                    player.sendMessage("You are teleported to a place of relative safety.")
                }
                "Pray" -> {
                    if (player.isUnderCombat) {
                        player.sendMessage("You need to be out of combat to use this altar.")
                        return@ALTAR_42965
                    }
                    if (!player.ancientAltarCooldownCompleted)
                        return@ALTAR_42965
                    player.lock(2)
                    player.sendMessage("You recharge your prayer.")
                    player.sendSound(2674)
                    player.animation = Animation(645)
                    player.prayerManager.restorePrayerPoints(99)
                    if (GodType.ANCIENT.isWieldingProtectiveItem(player)) {
                        player.heal(player.maxHitpoints - player.hitpoints)
                        player.variables.runEnergy = 100.0
                        player.combatDefinitions.specialEnergy = 100
                        player.sendMessage("Your affinity to the Empty Lord energizes you further.")
                    }
                }
            }
        }
    }
}
