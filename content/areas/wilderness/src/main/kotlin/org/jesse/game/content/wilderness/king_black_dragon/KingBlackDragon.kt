package org.jesse.game.content.wilderness.king_black_dragon

import org.jesse.game.content.boss.BossRespawnTimer
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.Toxins
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.Spawnable
import org.jesse.game.world.entity.npc.combat.CombatScript
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.npc.impl.slayer.dragons.Dragonfire
import org.jesse.game.world.entity.npc.impl.slayer.dragons.DragonfireProtection
import org.jesse.game.world.entity.npc.impl.slayer.dragons.DragonfireType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.PlayerCombat
import org.jesse.game.world.entity.player.calog.CAType

/**
 * @author Kris | 23. apr 2018 : 15:46.37
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
open class KingBlackDragon(id: Int, tile: Location?, direction: Direction?, radius: Int) :
    NPC(id, tile, direction, radius),
    Spawnable,
    CombatScript
{
    private var instance: KingBlackDragonInstance? = null

    override fun getRespawnDelay(): Int =
        BossRespawnTimer.KING_BLACK_DRAGON.timer.toInt()

    override fun isEntityClipped(): Boolean =
        false

    override fun isTolerable(): Boolean =
        false

    override fun validate(id: Int, name: String): Boolean =
        name == "king black dragon" && id != KING_BLACK_DRAGON_12440

    init {
        this.aggressionDistance = 64
        this.maxDistance = 64
        this.attackDistance = 10
    }

    override fun attack(target: Entity): Int {
        if (target !is Player) return 0
        val npc = this
        val player = target
        val random = Utils.random(if (isWithinMeleeDistance(npc, target)) 2 else 1)
        if (random == 0) {
            npc.setAnimation(DRAGONFIRE_ANIM)
            World.sendProjectile(npc, target, DRAGONFIRE_PROJ)
            val dragonfire = Dragonfire(DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(this, player))
            delayHit(
                npc,
                DRAGONFIRE_PROJ.getTime(npc, target),
                target,
                Hit(
                    npc,
                    Utils.random(dragonfire.damage),
                    HitType.REGULAR
                ).onLand {
                    player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s fiery breath"))
                    PlayerCombat.appendDragonfireShieldCharges(player)
                    target.setGraphics(DRAGONFIRE_GFX)
                })
        } else if (random == 2) {
            if (Utils.random(1) == 0) {
                npc.setAnimation(ATTACK_ANIM)
            } else {
                npc.setAnimation(SECONDARY_ATTACK_ANIM)
            }
            delayHit(npc, 0, target, Hit(npc, getRandomMaxHit(npc, 25, CombatScript.MELEE, target), HitType.MELEE))
        } else {
            val atk = Utils.random(2)
            when (atk) {
                0 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM)
                    World.sendProjectile(npc, target, POISON_PROJ)
                    val dragonfire: Dragonfire.DragonfireBuilder = object : Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(
                            this, player
                        )
                    ) {
                        override fun getDamage(): Int {
                            val tier = accumulativeTier
                            return if (tier == 0.0f) 65 else if (tier == 0.25f) 60 else if (tier == 0.5f) 35 else if (tier == 0.75f) 25 else 10
                        }
                    }
                    delayHit(
                        npc,
                        POISON_PROJ.getTime(npc, target),
                        target,
                        Hit(
                            npc,
                            Utils.random(dragonfire.damage),
                            HitType.REGULAR
                        ).onLand { hit: Hit? ->
                            player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s poisonous breath"))
                            if (Utils.random(3) == 0) {
                                target.getToxins().applyToxin(Toxins.ToxinType.POISON, 8, npc)
                            }
                            target.setGraphics(POISON_GFX)
                            PlayerCombat.appendDragonfireShieldCharges(player)
                        })
                }

                1 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM)
                    World.sendProjectile(npc, target, FREEZING_PROJ)
                    val dragonfire: Dragonfire.DragonfireBuilder = object : Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(
                            this, player
                        )
                    ) {
                        override fun getDamage(): Int {
                            val tier = accumulativeTier
                            return if (tier == 0.0f) 65 else if (tier == 0.25f) 60 else if (tier == 0.5f) 35 else if (tier == 0.75f) 25 else 10
                        }
                    }
                    delayHit(
                        npc,
                        FREEZING_PROJ.getTime(npc, target),
                        target,
                        Hit(
                            npc,
                            Utils.random(dragonfire.damage),
                            HitType.REGULAR
                        ).onLand { hit: Hit? ->
                            target.setGraphics(FREEZING_GFX)
                            PlayerCombat.appendDragonfireShieldCharges(player)
                            player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s icy breath"))
                            if (Utils.random(3) == 0) {
                                player.freeze(
                                    16,
                                    0
                                ) { entity: Entity? -> player.sendMessage("The dragon\'s icy attack freezes you.") }
                            }
                        })
                }

                2 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM)
                    World.sendProjectile(npc, target, SHOCKING_PROJ)
                    val dragonfire: Dragonfire.DragonfireBuilder = object : Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(
                            this, player
                        )
                    ) {
                        override fun getDamage(): Int {
                            val tier = accumulativeTier
                            return if (tier == 0.0f) 65 else if (tier == 0.25f) 60 else if (tier == 0.5f) 35 else if (tier == 0.75f) 25 else 10
                        }
                    }
                    delayHit(
                        npc,
                        SHOCKING_PROJ.getTime(npc, target),
                        target,
                        Hit(
                            npc,
                            Utils.random(dragonfire.damage),
                            HitType.REGULAR
                        ).onLand { hit: Hit? ->
                            target.setGraphics(SHOCKING_GFX)
                            PlayerCombat.appendDragonfireShieldCharges(player)
                            player.sendFilteredMessage(String.format(dragonfire.message, "dragon\'s shocking breath"))
                            if (Utils.random(3) == 0) {
                                player.skills.drainCombatSkills(2)
                                player.sendMessage("The dragon\'s shocking attack drains your stats.")
                            }
                        })
                }
            }
        }
        return 4
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        if (id != KING_BLACK_DRAGON_12440 && source is Player) {
            if (instance != null) {
                instance!!.increaseKc()
                if (instance!!.kc >= 10) {
                    source.combatAchievements.complete(CAType.WHO_IS_THE_KING_NOW)
                }
            }

            source.combatAchievements.complete(CAType.BIG_BLACK_AND_FIERY)
            source.combatAchievements.checkKcTask("king black dragon", 10, CAType.KING_BLACK_DRAGON_NOVICE)
            source.combatAchievements.checkKcTask("king black dragon", 25, CAType.KING_BLACK_DRAGON_CHAMPION)
            if (source.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) {
                source.combatAchievements.complete(CAType.CLAW_CLIPPER)
            }

            val protections = DragonfireProtection.getProtection(
                this, source
            )
            if ((protections.contains(DragonfireProtection.SUPER_ANTIFIRE_POTION) || protections.contains(
                    DragonfireProtection.ANTIFIRE_POTION
                )) && (protections.contains(DragonfireProtection.ANTI_DRAGON_SHIELD) || protections.contains(
                    DragonfireProtection.DRAGONFIRE_SHIELD
                ))
            ) {
                source.combatAchievements.complete(CAType.ANTIFIRE_PROTECTION)
            }
            if (AttackType.STAB == source.combatDefinitions.attackType) {
                source.combatAchievements.complete(CAType.HIDE_PENETRATION)
            }
        }
    }

    fun setInstance(instance: KingBlackDragonInstance?) {
        this.instance = instance
    }

    companion object {
        private val DRAGONFIRE_PROJ = Projectile(393, 40, 30, 40, 15, 28, 0, 5)
        private val POISON_PROJ = Projectile(394, 40, 30, 40, 15, 28, 0, 5)
        private val FREEZING_PROJ = Projectile(395, 40, 30, 40, 15, 28, 0, 5)
        private val SHOCKING_PROJ = Projectile(396, 40, 30, 40, 15, 28, 0, 5)
        private val DRAGONFIRE_GFX = Graphics(430, 0, 90)
        private val POISON_GFX = Graphics(429, 0, 90)
        private val FREEZING_GFX = Graphics(431, 0, 90)
        private val SHOCKING_GFX = Graphics(428, 0, 90)
        private val ATTACK_ANIM = Animation(80)
        private val SECONDARY_ATTACK_ANIM = Animation(91)
        private val DRAGONFIRE_ANIM = Animation(81)
    }
}
