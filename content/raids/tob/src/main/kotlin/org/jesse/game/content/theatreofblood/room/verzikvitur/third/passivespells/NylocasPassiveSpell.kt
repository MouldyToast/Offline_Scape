package org.jesse.game.content.theatreofblood.room.verzikvitur.third.passivespells

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.second.spawnCrabs
import org.jesse.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell

/**
 * @author Jire
 */
internal object NylocasPassiveSpell : PassiveSpell {

    override val nextSpell = WebPassiveSpell

    override fun VerzikVitur.cast() = spawnCrabs()

}