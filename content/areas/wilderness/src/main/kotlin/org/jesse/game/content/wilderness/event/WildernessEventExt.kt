package org.jesse.game.content.wilderness.event

fun WildernessEvent.isActive() = WildernessEventManager.stateOf(this) is WildernessEvent.State.Active
