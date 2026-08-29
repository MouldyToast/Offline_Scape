package com.zenyte.logger

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-21
 */

fun Throwable.toFormattedString(): String {
    val builder = StringBuilder()
    repeat(5) { index ->
        val element = stackTrace[index]
        val split = element.className.split(".")
        builder.append("at ${split[split.lastIndex]}.${element.methodName}(${element.fileName}:${element.lineNumber})\n")
    }
    return builder.toString()
}