package org.jesse.api.dao

import kotlinx.serialization.Serializable

@Serializable
sealed class GlobalValue {
    @Serializable
    data class IntValue(val value: Int) : GlobalValue()
    @Serializable
    data class LongValue(val value: Long) : GlobalValue()
    @Serializable
    data class BooleanValue(val value: Boolean) : GlobalValue()
    @Serializable
    data class StringListValue(val value: List<String>) : GlobalValue()
    @Serializable
    data class IntListValue(val value: List<Int>) : GlobalValue()
}