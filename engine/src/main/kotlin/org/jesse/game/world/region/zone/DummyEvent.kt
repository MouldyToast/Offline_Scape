package org.jesse.game.world.region.zone

import org.jesse.game.world.flooritem.FloorItem

/**
 * @author Kris | 22/08/2024
 */
sealed interface DummyZoneEvent

class ObjAddDummyEvent(val floorItem: FloorItem) : DummyZoneEvent
class ObjTurnPublicDummyEvent(val floorItem: FloorItem) : DummyZoneEvent
class ObjUpdateDummyEvent(val floorItem: FloorItem, val oldQuantity: Int) : DummyZoneEvent
