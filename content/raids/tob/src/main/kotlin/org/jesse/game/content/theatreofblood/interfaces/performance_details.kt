package org.jesse.game.content.theatreofblood.interfaces

import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.content.theatreofblood.room.TheatreRoom
import org.jesse.game.content.theatreofblood.room.TheatreRoomType
import org.jesse.game.content.theatreofblood.theatreDeathCount
import org.jesse.game.util.Utils
import org.jesse.scripts.interfaces.InterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface
import org.jesse.game.GameInterface.*
import org.jesse.game.util.AccessMask
import org.jesse.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class PerformanceDetailsInterface : InterfaceScript() {

    init {
        /**
         * Upon entering the treasure vault, players may read the strategy table by the grand bookshelves
         * to view the overall performance of the team, while spectators can read the war table to view it.
         *
         * @author Stan van der Bend
         */
        TOB_PERFORMANCE_DETAILS {

            opened {
                sendInterface()
                var mvpPlayerName by textComponent(14, "Player -")
                var challengeTime by textComponent(11, "-")
                var overallTime by textComponent(36, "-")
                var totalDeaths by textComponent(33, "0")

                var maidenOfSugadintiTime by textComponent(45, "-")
                var pestilentBloatTime by textComponent(47, "-")
                var nylocasTime by textComponent(49, "-")
                var sotelsegTime by textComponent(51, "-")
                var xarpusTime by textComponent(53, "-")
                var ladyVerzikViturTime by textComponent(55, "-")

                val party = VerSinhazaArea.getParty(this)
                if (party != null) {
                    val raid = party.raid
                    if (raid != null) {
                        mvpPlayerName = name//TODO change mvp to proper when raid ends.

                        maidenOfSugadintiTime = raid.getRoomTime(TheatreRoomType.THE_MAIDEN_OF_SUGADINTI)
                        pestilentBloatTime = raid.getRoomTime(TheatreRoomType.THE_PESTILENT_BLOAT)
                        nylocasTime = raid.getRoomTime(TheatreRoomType.THE_NYLOCAS)
                        sotelsegTime = raid.getRoomTime(TheatreRoomType.SOTETSEG)
                        xarpusTime = raid.getRoomTime(TheatreRoomType.XARPUS)
                        ladyVerzikViturTime = raid.getRoomTime(TheatreRoomType.VERZIK_VITUR)
                        challengeTime = Utils.ticksToTime(raid.totalRoomTicks)
                        overallTime = Utils.ticksToTime(raid.enterTick)

                        val participants = party.players
                        var participantIndex = 0
                        for (componentId in 22..30 step 2) {
                            val (name, deathCount) = participants.getOrNull(participantIndex++)
                                ?.let { it.name to it.theatreDeathCount.toString() }
                                ?: ("-" to "0")
                            packetDispatcher.sendComponentText(`interface`, componentId, name)
                            packetDispatcher.sendComponentText(`interface`, componentId + 1, deathCount)
                        }
                        totalDeaths = participants.sumOf { it.theatreDeathCount }.toString()
                    } else {
                        sendDeveloperMessage("Not in a raid.")
                    }
                } else {
                    sendDeveloperMessage("Not in Party.")
                }
            }
        }
    }
}
