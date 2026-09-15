package org.jesse.game.content.dt2.area

import org.jesse.game.content.dt2.npc.DT2BossDifficulty
import org.jesse.game.content.dt2.npc.get
import org.jesse.game.content.dt2.npc.instanceArea
import org.jesse.game.content.dt2.npc.leviathan.LeviathanInstance
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

class DT2Commands {
    companion object {
        fun register() {
            Command(PlayerPrivilege.DEVELOPER, "dt2ve", "Teleport to Vardorvis entrance.") { p, _ ->
                p.setLocation(Location(1150, 3444, 0))
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2vi", "Create a Vardorvis instance.") { p, _ ->
                VardorvisInstance.createInstance(p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "whisperer", "Teleport to the Whisperer.") { p, _ ->
                p.setLocation(Location(2656, 6399, 0))
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2wi", "Create a Whisperer instance.") { p, _ ->
                WhispererInstance(p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2lev", "Create a Leviathan instance.") { p, _ ->
                LeviathanInstance.construct(p, false)
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "scoord", "Teleport to instance coordinates.") { p, _ ->
                val instance = p.instanceArea as? WhispererInstance ?: return@Command
                p.sendMessage("Teleported to ${instance[p.position]}")
                p.teleport(instance[p.position])
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2di", "Create a Duke Sucellus instance.") { p, _ ->
                DukeSucellusInstance.createInstance(DT2BossDifficulty.NORMAL, p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2dia", "Create an Awakened Duke instance.") { p, _ ->
                DukeSucellusInstance.createInstance(DT2BossDifficulty.AWAKENED, p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dukeprep", "Give Duke Sucellus prep supplies.") { p, _ ->
                p.inventory.addItem(SALAX_SALT, 6)
                p.inventory.addItem(MUSCA_POWDER, 6)
                p.inventory.addItem(ARDER_POWDER, 6)
            }
        }
    }
}