package org.jesse.api.service.store

import org.jesse.api.model.CreditPackageOrder
import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player

internal var Player.storeClaimedOrders : MutableSet<Int> by persistentAttribute("store_claimed_orders", mutableSetOf())
internal var Player.storeClaimedOrdersToMentionOnNextLogin : MutableList<CreditPackageOrder> by persistentAttribute("store_claimed_orders_to_mention_on_next_login", mutableListOf())
