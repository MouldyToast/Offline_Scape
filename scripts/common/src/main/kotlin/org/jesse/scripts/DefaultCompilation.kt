package org.jesse.scripts

import org.jesse.scripts.KtsScriptTemplate
import kotlin.script.experimental.api.ScriptAcceptedLocation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.acceptedLocations
import kotlin.script.experimental.api.ide
import kotlin.script.experimental.api.isStandalone
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

/**
 * @author Jire
 */
object DefaultCompilation : ScriptCompilationConfiguration({
    isStandalone(false)
    ide {
        acceptedLocations(ScriptAcceptedLocation.Sources)
    }
    jvm {
        dependenciesFromCurrentContext(wholeClasspath = true)
        dependenciesFromClassContext(KtsScriptTemplate::class, wholeClasspath = true)
    }
}) {
    private fun readResolve(): Any = DefaultCompilation
}
