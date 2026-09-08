package org.jesse.scripts

import org.jesse.plugins.Event
import org.jesse.plugins.PluginManager

/**
 * @author Jire
 */
interface Script {

    operator fun Event.invoke(handle: Event.() -> Unit) =
        PluginManager.register(javaClass, handle)

}