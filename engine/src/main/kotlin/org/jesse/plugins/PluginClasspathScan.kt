package org.jesse.plugins

import io.github.classgraph.ScanResult

/**
 * A single classpath scan shared by every plugin-discovery consumer at boot
 * (currently [PluginScanner.scanAndLoad] for [PluginType] entries), so the
 * server doesn't pay for multiple full classpath walks over the same packages.
 *
 * [scan] is computed once, lazily, on first access from any consumer, and is
 * intentionally never closed: plugin discovery only happens during boot, and
 * keeping the scan's in-memory metadata alive for the rest of the process is
 * far cheaper than re-scanning or worrying about a second consumer touching
 * it after another closed it.
 *
 * Matches OpenRune's `org.rsmod.plugin.scan.PluginClasspathScan`.
 */
object PluginClasspathScan {

    val scan: ScanResult by lazy {
        PluginScanner.createClassGraph().scan()
    }
}
