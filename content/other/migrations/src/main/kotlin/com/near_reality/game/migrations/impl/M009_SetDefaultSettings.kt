package com.near_reality.game.migrations.impl

import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.model.music.Music
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface
import com.zenyte.game.world.entity.player.GameSetting
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Setting
import java.util.function.Consumer

@ActiveMigration
class M009_SetDefaultSettings : GameMigration {
    override fun run(player: Player) {
        player.settings.setSettingNoRefresh(Setting.AUTO_MUSIC, 1)
        for (setting in GameSetting.ALL) {
            if (setting == GameSetting.YELL_FILTER || setting == GameSetting.ALWAYS_SHOW_LATEST_UPDATE || setting == GameSetting.CONFIRMATION_WHEN_NOTING_OR_UNNOTING
            ) {
                continue
            }
            player.attributes[setting.toString()] = 1
        }

        player.varManager.sendBit(4084, 0)
        player.attributes["LEVEL_UP_DIALOGUES"] = 99
        player.varManager.sendVar(3601, 99)
        player.attributes["quest_points"] = 300
        player.questPoints = 300
        player.attributes["RING_OF_RECOIL"] = 40
        player.attributes["RING_OF_FORGING"] = 140
        player.attributes["checking combat in slayer"] = 1
        player.attributes["recoil effect"] = 1
        player.attributes["looting_bag_amount_prompt"] = 1
        player.varManager.sendVar(SettingVariables.PLAYER_ATTACK_OPTIONS_VARP_ID, 2)
        player.varManager.sendVar(SettingVariables.NPC_ATTACK_OPTIONS_VARP_ID, 2)
        player.varManager.sendBit(SettingsInterface.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID, 3)
        player.varManager.sendBit(SettingVariables.ESC_CLOSES_THE_CURRENT_INTERFACE_VARBIT_ID, 1)

        player.varManager.sendBit(SettingVariables.SHOW_ACTIVITY_ADVISER_VARBIT_ID, 1)
        player.varManager.sendBit(SettingVariables.SHIFT_CLICK_TO_DROP_ITEMS_VARBIT_ID, 1)
        player.varManager.sendBit(SettingVariables.SHOW_DATA_ORBS_VARBIT_ID, 0)
        player.varManager.sendBit(SettingVariables.HIDE_ROOFS_VARBIT_ID, 1)

        player.varManager.sendBit(SettingVariables.SHOW_WARNING_WHEN_CASTING_TELEPORT_TO_TARGET_VARBIT_ID, 1)
        player.varManager.sendBit(SettingVariables.SHOW_WARNING_WHEN_CASTING_DAREEYAK_TELEPORT_VARBIT_ID, 1)
        player.varManager.sendBit(SettingVariables.SHOW_WARNING_WHEN_CASTING_CARRALLANGAR_TELEPORT_VARBIT_ID, 1)
        player.varManager.sendBit(SettingVariables.SHOW_WARNING_WHEN_CASTING_ANNAKARL_TELEPORT_VARBIT_ID, 1)
        player.varManager.sendBit(SettingVariables.SHOW_WARNING_WHEN_CASTING_GHORROCK_TELEPORT_VARBIT_ID, 1)
        Player.getDefaultMusicTracks().forEach(Consumer { trackName: String? ->
            player.music.unlock(
                Music.get(
                    trackName!!
                )
            )
        }
        )
    }

    override fun id(): Int = 9
}