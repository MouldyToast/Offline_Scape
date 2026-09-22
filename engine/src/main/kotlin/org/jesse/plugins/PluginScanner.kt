package org.jesse.plugins

import cloud.rsps.PluginRoot
import com.google.common.base.Stopwatch
import org.jesse.NearReality
import org.jesse.Main
import org.jesse.logger.NearRealityLogger
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult
import java.util.concurrent.TimeUnit

/**
 * Scans the classpath for plugin classes and loads them at boot time.
 *
 * @author Jire
 */
object PluginScanner {

    val defaultPackages = arrayOf(
        Main::class.java.packageName,
        NearReality::class.java.packageName,
        PluginRoot::class.java.packageName,
    )

    private val log = NearRealityLogger.getLogger(PluginScanner::class.java)

    @JvmStatic
    @JvmOverloads
    fun createClassGraph(vararg packages: String = defaultPackages): ClassGraph =
        ClassGraph()
            .ignoreClassVisibility()
            .ignoreMethodVisibility()
            .enableAnnotationInfo()
            .enableMethodInfo()
            .enableClassInfo()
            .enableExternalClasses()
            .acceptPackages(*packages)

    @JvmStatic
    @JvmOverloads
    inline fun scanClassGraph(
        vararg packages: String = defaultPackages,
        use: (ScanResult) -> Unit
    ) = createClassGraph(*packages)
        .scan()
        .use(use)

    @JvmStatic
    @JvmOverloads
    inline fun scanClasses(
        vararg pluginTypes: PluginType = PluginType.values,
        packages: Array<String> = defaultPackages,
        use: (PluginType, ClassInfoList) -> Unit
    ) = scanClassGraph(*packages) { result ->
        val skippedPlugins = result.getSkipPluginScanClasses()
        pluginTypes.forEach {
            use(
                it,
                it.scan(result)
                    .exclude(skippedPlugins)
            )
        }
    }

    @JvmStatic
    fun ScanResult.getSkipPluginScanClasses(): ClassInfoList =
        getClassesWithAnnotation(SkipPluginScan::class.java)

    /**
     * Loads all [PluginType] entries from the given [scanResult].
     * Replaces the old scan() → plugins.dat → PluginLoader.load() pipeline.
     * Classes are sorted globally by [PluginPriority] before loading.
     *
     * @param scanResult a shared [ScanResult] — typically [PluginClasspathScan.scan].
     */
    @JvmStatic
    fun scanAndLoad(scanResult: ScanResult) {
        val stopwatch = Stopwatch.createStarted()
        val plugins = mutableListOf<ScannedPlugin>()
        val skippedPlugins = scanResult.getSkipPluginScanClasses()

        for (pluginType in PluginType.values) {
            val classInfoList = pluginType.scan(scanResult).exclude(skippedPlugins)
            for (classInfo in classInfoList) {
                plugins.add(ScannedPlugin(pluginType, classInfo.name, classInfo.priority))
            }
        }

        for (plugin in plugins.sortedBy { it.priority }) {
            val loadedClass = Class.forName(plugin.className)
            plugin.pluginType.pluginTypeLoader?.loadClass(loadedClass)
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        log.info("Scanned and loaded {} plugins in {} ms.", plugins.size, elapsed)
    }

    private data class ScannedPlugin(
        val pluginType: PluginType,
        val className: String,
        val priority: Int
    )

    val ClassInfo.priority: Int
        get() = getAnnotationInfo(PluginPriority::class.java)
            ?.getParameterValues(true)
            ?.get(0)
            ?.value as? Int
            ?: PluginPriority.DEFAULT
}
