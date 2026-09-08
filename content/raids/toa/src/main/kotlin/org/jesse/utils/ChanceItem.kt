package org.jesse.utils

import kotlin.random.Random

data class ChanceItem<T>(val item: T, val probability: Double)

fun <T> List<ChanceItem<T>>.roll(): T? {
    if (isEmpty()) return null

    val total = sumOf { it.probability }
    val pick = Random.nextDouble(total)

    var cursor = 0.0
    for (entry in this) {
        cursor += entry.probability
        if (pick < cursor) {
            return entry.item
        }
    }
    return null
}