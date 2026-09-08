package org.jesse.api.service.sanction

import org.jesse.api.model.SanctionLevel
import org.jesse.api.model.SanctionType
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

object SanctionCommands {

    fun register() {
        submitSanctionCommand(command = "mute", level = SanctionLevel.ACCOUNT, type = SanctionType.MUTE)
        submitSanctionCommand(command = "yellmute", level = SanctionLevel.ACCOUNT, type = SanctionType.YELL_MUTE)
        submitSanctionCommand(command = "ban", level = SanctionLevel.ACCOUNT, type = SanctionType.BAN)
        submitSanctionCommand(command = "macban", privilege = PlayerPrivilege.ADMINISTRATOR, level = SanctionLevel.MAC, type = SanctionType.HARDWARE_HASH)
        submitSanctionCommand(command = "ipmute", level = SanctionLevel.IP, type = SanctionType.MUTE)
        submitSanctionCommand(command = "ipban",  privilege = PlayerPrivilege.ADMINISTRATOR, level = SanctionLevel.IP, type = SanctionType.BAN)
        submitSanctionCommand(command = "uuidban", privilege = PlayerPrivilege.ADMINISTRATOR, level = SanctionLevel.UUID, type = SanctionType.BAN)
        revokeSanctionCommand(command = "revoke")
    }

    private fun submitSanctionCommand(
        privilege: PlayerPrivilege = PlayerPrivilege.MODERATOR,
        command: String,
        level: SanctionLevel,
        type: SanctionType
    ): Command {
        return Command(privilege, command, "Submit $type at $level level") { player, _ ->
            SanctionPlayerHandler.startSanctionSubmission(player, type, level)
        }
    }
    private fun revokeSanctionCommand(command: String): Command {
        return Command(PlayerPrivilege.MODERATOR, command, "Revoke a sanction") { player, _ ->
            SanctionPlayerHandler.startSanctionRevoke(player)
        }
    }
}
