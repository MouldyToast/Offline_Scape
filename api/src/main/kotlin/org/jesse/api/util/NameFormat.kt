package org.jesse.api.util

fun String.formatUsername() =
    lowercase().replace("_", " ")
