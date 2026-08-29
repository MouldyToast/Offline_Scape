package com.near_reality.game.content

import com.near_reality.game.content.tormented_demon.attacks.Attack
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player

infix fun Player.isProtectedAgainst(type: AttackType): Boolean {
    return when(type) {
        AttackType.STAB -> this.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)
        AttackType.SLASH -> this.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)
        AttackType.CRUSH -> this.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)
        AttackType.RANGED -> this.prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES)
        AttackType.MAGIC -> this.prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)
        AttackType.MELEE -> this.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)
    }
}