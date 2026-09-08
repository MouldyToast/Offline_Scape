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
            Command(PlayerPrivilege.DEVELOPER, "dt2ve") { p, _ ->
                p.setLocation(Location(1150, 3444, 0))
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2vi") { p, _ ->
                VardorvisInstance.createInstance(p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "whisperer") { p, _ ->
                p.setLocation(Location(2656, 6399, 0))
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2wi") { p, _ ->
                WhispererInstance(p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2lev") { p, _ ->
                LeviathanInstance.construct(p, false)
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "scoord") { p, _ ->
                val instance = p.instanceArea as? WhispererInstance ?: return@Command
                p.sendMessage("Teleported to ${instance[p.position]}")
                p.teleport(instance[p.position])
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2di") { p, _ ->
                DukeSucellusInstance.createInstance(DT2BossDifficulty.NORMAL, p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2dia") { p, _ ->
                DukeSucellusInstance.createInstance(DT2BossDifficulty.AWAKENED, p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dukeprep") { p, _ ->
                p.inventory.addItem(SALAX_SALT, 6)
                p.inventory.addItem(MUSCA_POWDER, 6)
                p.inventory.addItem(ARDER_POWDER, 6)
            }
        }
    }
}