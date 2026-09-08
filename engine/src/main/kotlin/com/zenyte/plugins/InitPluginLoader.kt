package com.zenyte.plugins

import com.zenyte.logger.NearRealityLogger
import org.slf4j.Logger

/**
 * @author Jire
 */
object InitPluginLoader : PluginTypeLoader {

    private val logger: Logger = NearRealityLogger.getLogger(InitPluginLoader::class.java)

    override fun loadClass(pluginClass: Class<*>) {
        try {
            val constructor = pluginClass.getDeclaredConstructor()
            val instance = constructor.newInstance()

            val initPlugin = instance as InitPlugin
            initPlugin.init()
        } catch (e: Exception) {
            logger.error("Failed to init plugin class $pluginClass", e)
        }
    }

}