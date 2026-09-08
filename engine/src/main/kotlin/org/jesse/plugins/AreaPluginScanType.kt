package org.jesse.plugins

import org.jesse.game.world.region.DynamicArea
import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult

/**
 * @author Jire
 */
object AreaPluginScanType : PluginScanType {

    override fun scan(result: ScanResult, scanClass: Class<*>): ClassInfoList =
        PluginScanTypes.IMPLEMENTING.scan(result, scanClass)
            .filter {
                !it.isInnerClass
                        && !it.isAbstract
                        && !it.extendsSuperclass(DynamicArea::class.java)
            }

}