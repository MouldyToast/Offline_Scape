package org.jesse.scripts.item.equip

/**
 * Represents a handler response for an intercepted equip action.
 *
 * @author Stan van der Bend
 */
sealed class EquipHandlerResponse {

    object Continue : EquipHandlerResponse()

    object Negate : EquipHandlerResponse()
}
