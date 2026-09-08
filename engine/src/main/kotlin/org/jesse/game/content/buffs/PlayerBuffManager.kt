package org.jesse.game.content.buffs

import org.jesse.game.world.entity.player.Player

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.13.2025
 */
class PlayerBuffManager(val player: Player) {

    companion object {
        @JvmStatic
        var buffs: MutableList<Buff> = mutableListOf()

        /** Register a new buff */
        fun register(buff: Buff) {
            buffs += buff
        }

        init {
            MagicBuffs
        }
    }

    /**
     * List buffs that apply to this player,
     * filtered by optional category/subcategory.
     */
    fun listBuffs(
        player: Player,
        category: BuffCategory? = null,
        subcategory: BuffSubcategory? = null
    ): List<Buff> = buffs.filter { buff ->
        buff.predicate(player) &&
                (category   ?: buff.category   ) == buff.category &&
                (subcategory?: buff.subcategory) == buff.subcategory &&
                // if player is in a PvP match, only include PvP-valid buffs
                (!player.inCombatWithPlayer() || buff.validInPvp)
    }

    /**
     * Apply all matching buffs to a baseValue.
     * 1) If any full‐exclusive buffs exist, pick the one that gives the highest result.
     * 2) Else handle semi‐exclusive groups: in each conflict group, only the single
     *    top‐performer survives.
     * 3) Chain all remaining buffs in registration order.
     */
    fun applyBuffs(
        category: BuffCategory,
        subcategory: BuffSubcategory,
        baseValue: Double
    ): Double {
        val applicable = listBuffs(player, category, subcategory)

        // 1) Full exclusives
        applicable.filter { it.exclusive }.takeIf { it.isNotEmpty() }?.let { exBuffs ->
            return exBuffs
                .maxByOrNull { it.modify(player, baseValue) }!!
                .modify(player, baseValue)
        }

        // 2) Semi-exclusive
        val resultBuffs = mutableListOf<Buff>()
        val visited = mutableSetOf<Buff>()

        val semi = applicable.filter { it.exclusiveWith.isNotEmpty() }
        for (buff in semi) {
            if (buff in visited) continue

            // find all buffs in this conflict group
            val group = semi.filter { other ->
                other.id == buff.id
                    || other.id in buff.exclusiveWith
                    || buff.id in other.exclusiveWith
            }
            visited += group

            // pick the one that transforms baseValue the most
            val winner = group.maxByOrNull { it.modify(player, baseValue) }!!
            resultBuffs += winner
        }

        // 3) Non-exclusive buffs
        val normals = applicable.filter {
            !it.exclusive && it.exclusiveWith.isEmpty()
        }

        // Chain them all
        return (normals + resultBuffs).fold(baseValue) { acc, b -> b.modify(player, acc) }
    }

}