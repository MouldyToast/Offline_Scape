package org.jesse.game.content.drops.table.slot

import org.jesse.game.content.drops.table.RollResult
import org.jesse.game.content.drops.table.TableSlot
import org.jesse.game.util.Utils
import java.util.*

/**
 * @author Corey
 * @since 17/07/2020
 */
data class BasicSlot(val id: Int, val minAmount: Int, val maxAmount: Int) : TableSlot() {
    
    @JvmOverloads
    constructor(id: Int, amount: Int = 1) : this(id, amount, amount)
    
    override fun evaluate(random: Random): RollResult {
        if (minAmount == maxAmount) {
            return RollResult(id, minAmount)
        }
        
        return RollResult(id, Utils.random(random, minAmount, maxAmount))
    }
    
}
