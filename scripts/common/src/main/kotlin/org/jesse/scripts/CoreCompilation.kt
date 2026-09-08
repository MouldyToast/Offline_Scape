package org.jesse.scripts

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object CoreCompilation : ScriptCompilationConfiguration({
    defaultImports(
        "org.jesse.game.world.entity.masks.Animation",
        "org.jesse.game.world.entity.masks.Graphics",
        "org.jesse.game.world.entity.SoundEffect",
        "org.jesse.game.world.Projectile"
    )
}) {
    private fun readResolve(): Any = CoreCompilation
}
