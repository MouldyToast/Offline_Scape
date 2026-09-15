package org.jesse.game.content.boss.nex

import org.jesse.game.content.godwars.objects.GodwarsBossDoorObject.BossDoor
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege.ADMINISTRATOR

object NexCommands {

    fun register() {
        GameCommands.Command(ADMINISTRATOR, "nex", "Nex testing menu.") { p, _ ->
            p.options {
                "Teleport-to" { p.teleport(Location(2924, 5202, 0)) }
                "Force Spawn Nex" { NexModule.spawnNex() }
                "Set Nex Essence (KC)" {
                    p.sendInputInt("How many kills?") {
                        p.addAttribute(BossDoor.ANCIENT.formattedName + "Kills", it)
                    }
                }
                "Set spawn delay" {
                    p.sendInputInt("How many ticks?") {
                        NexModule.NEX_SPAWN_DELAY = it.coerceAtLeast(1)
                    }
                }
                "Kill nex" {
                    NexModule.getNexNPC()?.run {
                        removeHitpoints(Hit(p, hitpoints, HitType.MELEE))
                    }
                }
            }
        }
    }
}
