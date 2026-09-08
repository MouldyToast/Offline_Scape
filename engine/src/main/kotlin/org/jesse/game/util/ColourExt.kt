package org.jesse.game.util

import org.jesse.game.util.Colour

operator fun Colour.invoke(string: String): String = wrap(string)
operator fun Colour.invoke(number: Number) = invoke("$number")
