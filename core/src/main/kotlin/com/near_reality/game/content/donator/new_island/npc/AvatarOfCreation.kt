package com.near_reality.game.content.donator.new_island.npc

import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.EntityHitBar
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.Spawnable
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.CharacterLoop
import java.util.function.Consumer

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-26
 */
class AvatarOfCreation(
    val spawnLocation: Location = Location(1711, 2648, 0)
): NPC(
    NpcId.AVATAR_OF_CREATION_10531,
    spawnLocation,
    Direction.SOUTH,
    0
), Spawnable, CombatScript {

    @Suppress("UNUSED_PARAMETER")
    constructor(id: Int, location: Location?, direction: Direction, radius: Int): this()

    override fun spawn(): NPC {
        super.spawn()
        attackDistance = 16
        maxDistance = 8
        hitBar = object : EntityHitBar(this) {
            override fun getType(): Int {
                return 21
            }
        }
        WorldBroadcasts.sendMessage("<img=68><col=00FF00><shad=000000>Avatar of Creation has spawned at the onyx member area! (::onyx dz)", BroadcastType.WORLD_BOSS, true)
        return this
    }

    override fun setRespawnTask() {}
    override fun getRespawnDelay(): Int = 1

    override fun isMultiArea(): Boolean = true

    override fun canMove(fromX: Int, fromY: Int, direction: Int): Boolean = true

    private fun regularAttack(player: Player) {
        if (player.nextLocation != null && player.nextLocation.regionId != location.regionId) return
        if (player.location.regionId == location.regionId && !player.isDying) {
            val maxHit = getRandomMaxHit(this, getCombatDefinitions().maxHit, AttackType.CRUSH, player)
            val hit = Hit(this, maxHit, HitType.MELEE)
            delayHit(this, 0, player, hit)
        }
    }

    override fun attack(target: Entity?): Int {
        val players = CharacterLoop.find(location, 4, Player::class.java) { p ->
            p != null && !p.isDead && !p.isDying && !p.isFinished && p.isInitialized
        }
        players.forEach(Consumer { player: Player -> this.regularAttack(player) })
        return 5
    }

    override fun validate(id: Int, name: String?): Boolean =
        id == NpcId.AVATAR_OF_CREATION_10531
}