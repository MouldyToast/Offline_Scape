package org.jesse.game.content.theatreofblood

import cloud.rsps.game.hiscores.HiscoreEntries
import com.google.common.eventbus.Subscribe
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikConfigs
import org.jesse.game.world.entity.player.collectionlog.CollectionLogCategories
import org.jesse.plugins.events.ServerLaunchEvent
import net.runelite.api.gameval.SpriteID
import java.util.function.Function

/**
 * @author Jire
 */
object ServerLoadSubscriber {

    @Subscribe
    @JvmStatic
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
        VerzikConfigs.configs()
        TheatreOfBloodScoresSerializer.read()
        CollectionLogCategories.register(
            "theatre of blood",
            { player -> player.tobStats.completions },
            { 0 },
            { player -> player.tobStatsHard.completions })

        HiscoreEntries.activity(
            71,
            "Theatre of Blood",
            SpriteID.IconBoss25x25.THEATRE_OF_BLOOD
        ) {
            tobStats.completions.toLong()
        }
        HiscoreEntries.activity(
            72,
            "Theatre of Blood: Hard Mode",
            SpriteID.IconBoss25x25.THEATRE_OF_BLOOD
        ) {
            tobStatsHard.completions.toLong()
        }
    }

}
