package org.jesse.game.content.dt2.npc.whisperer

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.area.WhispererInstance
import org.jesse.game.content.dt2.npc.*
import org.jesse.game.content.dt2.npc.leviathan.awakened
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.item.ids.*
import org.jesse.game.npc.ids.TENTACLE
import org.jesse.game.npc.ids.FLOATING_COLUMN
import org.jesse.game.task.WorldTask
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils.random
import org.jesse.game.world.Position
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.WorldThread
import org.jesse.game.world.entity.*
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.CombatScriptsHandler
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.npc.NPCCombat
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combat.CombatScript
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.NotificationSettings
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.DynamicArea
import mgi.types.config.HitbarDefinitions
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @author Khaled Abdeljaber
 */
class WhispererNPC(
    id: Int, tile: Location, facing: Direction, val instance: WhispererInstance
) : NPC(id, tile, facing, 0), CombatScript {

    private var combatTask: WorldTask? = null
    private var rootTask: WorldTask? = null
    private var tentacleTask: WorldTask? = null
    private var timerHitbar: HitBar = WhispererTimerHitBar(this)
    private var meleeActive = false

    init {
        setAttackDistance(64)
        setMaxDistance(256)
        setDeathDelay(4)
        setAggressionDistance(64)
        setDamageCap(200)
        this.hitBar = object : EntityHitBar(this) {
            override fun getType(): Int {
                return 19
            }
        }
        forceAggressive = true

        combat = object : NPCCombat(this) {
            override fun combatAttack(): Int {
                if (target == null) {
                    return 0
                }
                addAttackedByDelay(target)
                return CombatScriptsHandler.specialAttack(npc, target)
            }
        }
    }

    override fun spawn(): NPC {
        return super.spawn()
    }

    override fun setRespawnTask() {
        super.setRespawnTask()

        setTransformation(ODD_FIGURE)
    }

    override fun sendNotifications(player: Player?) {}

    override fun attack(target: Entity?): Int {
        if (id == ODD_FIGURE) {
            return -1
        } else if (combatTask?.exists() == true) {
            return 1
        }

        val player = target as? Player? ?: return -1

        val phase = phase
        val awakened = awakened
        val specials = specials

        if (phase?.isSpecial == true) return -1
        if (meleeActive) return 1
        when {
            phase == SpecialPhase.ENRAGED -> performBasicAttackEnraged(player)
            specials == 0 -> performBasicAttackPrePhase(player)
            specials >= if (awakened) 7 else 3 -> performBasicAttackPostAllPhases(player)
            else -> performBasicAttackPostPhase(player)
        }

        return if (isWithinMeleeDistance(this, target)) {
            0
        } else {
            2
        }
    }

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)
        handleHitApplied(hit)
    }

    override fun postHitProcess(hit: Hit) {
        instance.players.forEach {
            it.getHpHud().updateValue(hitpoints)
        }
    }

    override fun sendDeath() {
        if (!isEnraged) {
            hitpoints = 1
            return enrage()
        }
        super.sendDeath()
        onDeath()
    }

    private fun NPC.onDeath() {
        stopTask(combatTask)
        stopTask(tentacleTask)
        stopTask(rootTask)

        // Find all npcs and remove them
        val npcs = position.findNpcs(radius = 30) {
            id == TENTACLE_12208 || id == LOST_SOUL || id == FLOATING_COLUMN
        }
        npcs.forEach {
            it.remove()
        }

        val objects = allObjects?.filter {
            it.id == CORRUPTED_SEED_47575 ||
                    it.id == CORRUPTED_SEED ||
                    it.id == CORRUPTED_SEED_47574
        }
        objects?.forEach {
            it.remove()
        }

        rotation = null
        specials = 0

        instance.players.forEach {
            it.sanity = 100
            it.shadowRealmTicks = 0
            it.hpHud.close()

            val name = if (awakened) "awakened whisperer" else "whisperer"
            if (NotificationSettings.isKillcountTracked(name)) {
                it.notificationSettings.increaseKill(name)
                if (NotificationSettings.BOSS_NPC_NAMES.contains(name.lowercase(Locale.getDefault())))
                    it.notificationSettings.sendBossKillCountNotification(name)
            }
            DT2Module.updateWhispererStatistics(it.bossTimer.currentTracker, awakened = awakened)
            it.bossTimer.finishTracking(name)

            if (!it.position.inArea(WhispererConstants.LASSAR_UNDERGROUND_SHADOW_REALM_AREA, instanceArea)) {
                return@forEach
            }

            it.activateBlackstone(instance, force = true)
        }
    }

    private fun stopTask(task: WorldTask?) {
        task?.stop()
    }

    fun addObject(obj: WorldObject) {
        if (allObjects == null) {
            allObjects = mutableListOf()
        }
        allObjects?.add(obj)
    }

    fun removeObjects(obj: WorldObject) {
        allObjects?.remove(obj)
    }

    fun NPC.handleHitApplied(hit: Hit) {
        if (waiting || isEnraged || phase?.isSpecial == true) return
        val beforePercentage = lowestHitpointsAsPercentage
        val beforeHitpoints = lowestHitpoints
        lowestHitpoints = minOf(lowestHitpoints, hitpoints - hit.damage)
        val afterPercentage = lowestHitpointsAsPercentage
        val afterHitpoints = lowestHitpoints
        if (awakened) {
            when {
                beforeHitpoints > 2400 && afterHitpoints <= 2400 -> selectNextPhase(0)
                beforeHitpoints > 2120 && afterHitpoints <= 2120 -> selectNextPhase(1)
                beforeHitpoints > 1820 && afterHitpoints <= 1820 -> selectNextPhase(2)
                beforeHitpoints > 1600 && afterHitpoints <= 1600 -> selectNextPhase(3)
                beforeHitpoints > 1310 && afterHitpoints <= 1310 -> selectNextPhase(4)
                beforeHitpoints > 1050 && afterHitpoints <= 1050 -> selectNextPhase(5)
                beforeHitpoints > 800 && afterHitpoints <= 800 -> selectNextPhase(6)
                beforeHitpoints > 500 && afterHitpoints <= 500 -> selectNextPhase(7)
                !isEnraged && beforeHitpoints > 0 && afterHitpoints <= 0 -> enrage()
            }
        } else {
            when {
                beforePercentage > 80 && afterPercentage <= 80 -> selectNextPhase(0)
                beforePercentage > 55 && afterPercentage <= 55 -> selectNextPhase(1)
                beforePercentage > 30 && afterPercentage <= 30 -> selectNextPhase(2)
                !isEnraged && beforePercentage > 0 && afterPercentage <= 0 -> enrage()
            }
        }
    }

    fun NPC.selectNextPhase(index: Int) {
        if (isEnraged) return

        val rotation = rotation ?: return
        val nextPhase = rotation.rotations[index % rotation.rotations.size]
        if (phase == nextPhase) {
            return
        }

        stopTask(combatTask)
        stopTask(rootTask)

        schedule {
            combat.combatDelay = 4
        }

        phase = nextPhase
        specials++

        when (phase) {
            SpecialPhase.SHADOW_LEECHES -> performShadowLeeches()
            SpecialPhase.SOUL_SIPHON -> performSoulSiphon()
            SpecialPhase.SCREECH -> performScreech()
            else -> return
        }
    }

    fun NPC.performSoulSiphon() {
        val combatTask = object : WorldTask {
            var tick = 0
            var soulSiphonFailed = false
            val soulNPCs = mutableListOf<NPC>()

            override fun stop() {
                super.stop()
            }

            override fun run() {
                setFaceEntity(null)
                when (tick) {
                    0 -> {
                        playAnimation(WhispererConstants.WHISPERER_SWITCH_PHASE_START)
                    }

                    1 -> {
                        setLocation(respawnTile)
                        playAnimation(WhispererConstants.WHISPERER_PRAY_RISE_START)
                        instance.players?.forEach { player ->
                            faceEntity(player)
                            player.sendMessage("<col=6800bf>The Whisperer begins to call upon the lost...")
                        }
                    }

                    3 -> {
                        playAnimation(WhispererConstants.WHISPERER_PRAY_RISE_CONTINUE)
                        instance.players?.forEach { player ->
                            faceEntity(player)
                            setFaceEntity(player)
                        }
                    }

                    5 -> {
                        playAnimation(WhispererConstants.WHISPERER_PRAY_RISE_CONTINUE)
                        instance.players?.forEach { player ->
                            setFaceEntity(player)
                        }
                    }

                    7 -> {
                        whispererTimerMax = 75
                        whispererTimerCurrent = whispererTimerMax
                        launchWhispererTimer()

                        playAnimation(WhispererConstants.WHISPERER_PRAY_RISE_CONTINUE)
                        instance.prepareDarkFragment() // Assuming this function exists.
                        val positions =
                            if (awakened) WhispererConstants.SOUL_SPAWNS_AWAKENED else WhispererConstants.SOUL_SPAWNS_STANDARD
                        val proj = ProjConstants.WHISPERER_SOUL_SIPHON
                        positions.forEach { pos ->
                            proj.build(this@performSoulSiphon, instanceArea[pos])
                        }
                    }

                    8 -> {
                        val positions =
                            if (awakened) WhispererConstants.SOUL_SPAWNS_AWAKENED else WhispererConstants.SOUL_SPAWNS_STANDARD
                        val remainingColors = SoulColour.entries.flatMap { color ->
                            val amount = if (awakened) 4 else color.amount
                            List(amount) { color }
                        }.toMutableList()
                        positions.forEach { pos ->
                            val soulMain = WhispererSoulNPC(
                                LOST_SOUL,
                                instanceArea[pos],
                                Direction.NORTH
                            )
                            soulMain.spawn()

                            soulMain.setFaceEntity(this@performSoulSiphon)
                            soulMain.playAnimation(WhispererConstants.WHISPERER_SOUL_SPAWN)
                            val soulShadow =
                                WhispererSoulNPC(
                                    LOST_SOUL_12212,
                                    instanceArea[pos.transform(-256, 0, 0)],
                                    Direction.fromJagAngle(soulMain.direction),
                                )
                            soulShadow.spawn()

                            val soulColor = remainingColors.removeAt(remainingColors.lastIndex)
                            soulShadow.soulColour = soulColor
                            soulShadow.setColourCustomization(*soulColor.colours.toIntArray())
                            soulNPCs.add(soulMain)
                            soulNPCs.add(soulShadow)
                        }
                    }

                    9 -> {
                        if (whispererTimerCurrent > 0 && soulNPCs.any { !it.isFinished && !it.isDead }) {
                            soulNPCs.forEach { soul ->
                                soul.setForceTalk(soul.soulColour?.say ?: "")
                                soul.playAnimation(WhispererConstants.WHISPERER_SOUL_LOOP)
                            }
                            return
                        }
                    }

                    10 -> {
                        soulSiphonFailed = soulNPCs.any { !it.isFinished && it.id == LOST_SOUL }
                        soulNPCs.forEach { soul ->
                            soul.setForceTalk("")
                            soul.playAnimation(WhispererConstants.WHISPERER_SOUL_DEATH)
                            val proj = ProjConstants.WHISPERER_SOUL_SIPHON_RETURN.withGraphic(
                                if (soul.soulColour != null) WhispererConstants.WHISPERER_SOUL_SIPHON_RETURN_SHADOW_REALM
                                else WhispererConstants.WHISPERER_SOUL_SIPHON_RETURN
                            )
                            proj.build(soul, this@performSoulSiphon)
                        }

                        playAnimation(WhispererConstants.WHISPERER_SOUL_END.id, delay = 60)
                        instance.players?.forEach { player ->
                            player.playSound(WhispererConstants.WHISPERER_SOUL_SIPHON_END_3_SYNTH)
                            player.playSound(WhispererConstants.WHISPERER_SOUL_SIPHON_END_4_SYNTH)
                            player.playSound(WhispererConstants.WHISPERER_SOUL_SIPHON_END_5_SYNTH)
                        }
                    }

                    11 -> {
                        instance.clearDarkFragment()
                    }

                    12 -> {
                        soulNPCs.forEach { it.remove() }
                    }

                    13 -> {
                        if (soulSiphonFailed) {
                            instance.players?.forEach { player ->
                                player.reduceSanity(50)
                                player.removeHitpoints(Hit(50, HitType.REGULAR))
                                applyHit(Hit(100, HitType.HEALED))
                                player.sendMessage("<col=ff3045>You fail to disrupt the Whisperer's chant.")
                                player.sendMessage("Strange Whisper| Sanitas Mors Vita Oratio.")
                            }
                        } else {
                            performSoulEffects(soulNPCs.filter { it.soulColour != null && !it.isFinished && !it.isDead }
                                .mapNotNull { it.soulColour }.toSet())
                        }
                    }

                    14 -> {
                        val full = soulNPCs.filter { it.soulColour != null && !it.isFinished && !it.isDead }.isEmpty()
                        val gfx =
                            if (full) WhispererConstants.WHISPERER_SOUL_SIPHON_EFFECT_FULLY else WhispererConstants.WHISPERER_SOUL_SIPHON_EFFECT
                        instance.players?.forEach { player ->
                            player.playSound(WhispererConstants.WHISPERER_SOUL_SIPHON_EFFECT_SYNTH)
                            player.playGraphics(gfx)
                        }
                        playGraphics(gfx)
                    }

                    15 -> {
                        instance.returnToOverworld()
                    }

                    16 -> {
                        val target = instance.players?.randomOrNull()
                        if (target != null) {
                            performBindingSpecial(target)
                        } else {
                            phase = if (isEnraged) SpecialPhase.ENRAGED else SpecialPhase.STANDARD
                        }
                    }
                }
                tick++
            }
        }
        this@WhispererNPC.combatTask = schedule(combatTask, 0, 0)
    }


    fun Npc.performSoulEffects(coloursAlive: Set<SoulColour>) {
        val coloursDead = SoulColour.entries - coloursAlive
        val full = coloursAlive.isEmpty()

        if (SoulColour.YELLOW in coloursDead) restoreHitpoints(0.20)
        if (SoulColour.BLUE in coloursDead) restorePrayer(0.20)
        if (SoulColour.CYAN in coloursDead) restoreSanity(15)
        if (SoulColour.GREEN in coloursDead) dealDamage(if (full) 75 else 50)

        if (full) {
            instance.players?.forEach {
                it.sendMessage("<col=06600c>You fully disturb the Whisperer's chant.")
            }
        } else {
            instance.players?.forEach {
                it.sendMessage("<col=ff3045>You manipulated the Whisperer's chant, but hse takes advantage of the souls you left behind.")
            }
        }
    }

    fun Npc.dealDamage(amount: Int) {
        applyHit(Hit(amount, HitType.REGULAR))
    }

    fun Npc.restoreHitpoints(percentage: Double) {
        instance.players?.forEach {
            it.applyHit(Hit((maxHitpoints * percentage).roundToInt(), HitType.HEALED))
        }
    }

    fun Npc.restorePrayer(percentage: Double) {
        instance.players?.forEach {
            val prayerLevelFixed = it.skills.getLevelForXp(SkillConstants.PRAYER) * percentage
            it.prayerManager.restorePrayerPoints(prayerLevelFixed.roundToInt())
        }
    }

    fun Npc.restoreSanity(amount: Int) {
        instance.players?.forEach { it.increaseSanity(amount) }
    }

    fun NPC.performScreech() {
        val reps = if (awakened) 5 else 3
        var pillars: List<NPC> = emptyList()
        val task = object : WorldTask {
            var tick = 0

            override fun stop() {
                super.stop()
            }

            override fun run() {
                when (tick) {
                    0 -> {
                        // Tick 0: Start phase switch.
                        setFaceEntity(null)
                        faceDirection(Direction.NORTH)
                        playAnimation(WhispererConstants.WHISPERER_SWITCH_PHASE_START)
                    }

                    1 -> {
                        // Tick 1: Set face location and reposition.
                        faceLocation = instanceArea[2656, 6376, 0]
                        setLocation(instanceArea[2655, 6356, 0])
                        playAnimation(WhispererConstants.WHISPERER_SWITCH_PHASE_END_PAUSE)
                        instance.players?.forEach { player ->
                            player.sendMessage("<col=6800bf>The Whisperer prepares to let out a powerful screech...")
                        }
                    }

                    2 -> {
                        // Tick 2: Pause (simulate pause(1)).
                    }

                    3 -> {
                        // Tick 3: Begin rising animation and play screech start sounds.
                        playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_START)
                        instance.players?.forEach { player ->
                            player.playSound(WhispererConstants.WHISPERER_SCREECH_START_1_SYNTH)
                            player.playSound(WhispererConstants.WHISPERER_SCREECH_START_2_SYNTH, delay = 2)
                            player.playSound(WhispererConstants.WHISPERER_SCREECH_START_3_SYNTH, delay = 3)
                        }
                    }

                    4 -> {
                        // Tick 4: Pause.
                    }

                    5 -> {
                        // Tick 5: Play pause animation, prepare dark fragment and pillars, and start the headbar timer.
                        playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_PAUSE)
                        instance.prepareDarkFragment()
                        pillars = spawnPillars()
                        whispererTimerMax = 72
                        whispererTimerCurrent = whispererTimerMax
                        launchWhispererTimer()
                    }

                    6 -> {
                        // Tick 6: Check timer low; if so, clear dark fragment.
                        if (whispererTimerCurrent <= 10) {
                            instance.clearDarkFragment()
                        }
                        // Wait until the headbar timer is finished.
                        if (containsTimer(WhispererConstants.WHISPERER_HEADBAR_TIMER)) {
                            return  // Do not increment tick; wait here.
                        }
                    }

                    // Repetition loop – each rep now lasts 8 ticks instead of 6
                    in 7 until 7 + reps * 8 -> {
                        // For each rep:
                        //   Tick 0 of rep: launch scream.
                        //   Tick 2: play rising animation – if last rep, play rising end; otherwise, rising start with sounds.
                        //   Tick 4: if not last rep, play pause animation.
                        // Other ticks (1,3,5,6,7) are extra pauses.
                        val repTick = tick - 7
                        val repIndex = repTick / 8
                        val tickInRep = repTick % 8
                        when (tickInRep) {
                            0 -> {
                                addHitbar(RemoveHitBar(timerHitbar.type))
                                launchScream(pillars)
                            }
                            4 -> {
                                if (repIndex == reps - 1) {
                                    // Final rep: use rising end animation.
                                    playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_END)
                                } else {
                                    // Otherwise, use rising start with sounds.
                                    playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_START)
                                    instance.players?.forEach { player ->
                                        player.playSound(WhispererConstants.WHISPERER_SCREECH_START_1_SYNTH)
                                        player.playSound(WhispererConstants.WHISPERER_SCREECH_START_2_SYNTH, delay = 2)
                                        player.playSound(WhispererConstants.WHISPERER_SCREECH_START_3_SYNTH, delay = 3)
                                    }
                                }
                            }
                            6 -> {
                                if (repIndex < reps - 1) {
                                    playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_PAUSE)
                                }
                            }
                            // Ticks 1,3,5,6,7 are left as pauses to give a larger delay between animations.
                        }
                    }

                    7 + reps * 8 -> {
                        // Pause for 2 ticks after the repetition loop.
                        // Tick: 7 + reps*8 (pause)
                    }

                    7 + reps * 8 + 1 -> {
                        // Tick: 7 + reps*8 + 1 (pause)
                    }

                    7 + reps * 8 + 2 -> {
                        // Tick: Return players to overworld and play the namaste animation.
                        instance.returnToOverworld()
                        playAnimation(WhispererConstants.WHISPERER_NAMASTE)
                    }

                    7 + reps * 8 + 7 -> {
                        // Finally, perform binding special (this tick has been shifted by an extra 2 ticks compared to before).
                        val target = instance.players?.randomOrNull()
                        if (target != null) {
                            performBindingSpecial(target)
                        } else {
                            phase = if (isEnraged) SpecialPhase.ENRAGED else SpecialPhase.STANDARD
                        }
                    }
                }
                tick++
            }
        }
        combatTask = schedule(task, 0, 0)
    }

    fun NPC.spawnPillars(): MutableList<NPC> {
        val maxHp = if (awakened) 100 else 60
        // Assume these constants are defined in WhispererConstants.
        val possibleHitpoints = if (awakened) WhispererConstants.PILLARS_AWAKENED_HITPOINTS.toList()
        else WhispererConstants.PILLARS_NON_AWAKENED_HITPOINTS.toList()
        val shuffledHitpoints = possibleHitpoints.shuffled()
        val pillars = mutableListOf<NPC>()
        WhispererConstants.PILLARS_TO_MODEL.entries.forEachIndexed { index, entry ->
            val staticPos = entry.key
            val model = entry.value
            val overworldPos = instanceArea[staticPos]
            val underworldPos = instanceArea[staticPos.transform(-256, 0, 0)]
            val hp = shuffledHitpoints.getOrNull(index) ?: maxHp
            val overworldPillar =
                FloatingColumnNPC(FLOATING_COLUMN, overworldPos, Direction.NORTH, this@WhispererNPC)
            overworldPillar.spawn()
            overworldPillar.setModelCustomization(model)
            overworldPillar.combatDefinitions.hitpoints = maxHp
            overworldPillar.hitpoints = hp
            overworldPillar.occupiedArea.forEachTile { tile ->
                tile.spotanim(WhispererConstants.WHISPERER_LARGE_POOL_SPLASH)
            }
            val obj = MapObject(47577, overworldPos, 10, 0).spawn()
            addObject(obj)

            pillars.add(overworldPillar)
            val underworldPillar =
                FloatingColumnNPC(FLOATING_COLUMN, underworldPos, Direction.NORTH, this@WhispererNPC)
            underworldPillar.spawn()

            underworldPillar.combatDefinitions.hitpoints = maxHp
            underworldPillar.hitpoints = hp
            underworldPillar.occupiedArea.forEachTile { tile ->
                tile.spotanim(WhispererConstants.WHISPERER_LARGE_POOL_SPLASH)
            }
            val obj2 = MapObject(
                47577,
                underworldPos,
                10,
                0
            ).spawn()
            underworldPillar.launchTimer(WhispererConstants.WHISPERER_PILLAR_HEADBAR_TIMER, 1) {
                underworldPillar.overrideHitbarType(19)
                underworldPillar.addHitbar()
                true
            }
            addObject(obj2)

            pillars.add(underworldPillar)
        }
        return pillars
    }

    /**
     * Returns all pillars that physically cover [loc],
     * ignoring whether they have the lowest HP or not.
     *
     * "Covering" means:
     * - [loc] in the pillar's occupiedArea, or
     * - [loc] in the squares directly behind the pillar, etc.
     */
    fun getCoveringPillars(loc: Location, pillars: List<Npc>): List<Npc> {
        return pillars.filter { pillar ->
            // Must not be "dead" or "finished" to be relevant
            if (pillar.isDead || pillar.isFinished) {
                return@filter false
            }
            // If location is actually in the pillar's area
            if (loc in pillar.occupiedArea) {
                return@filter true
            }
            // Or in the squares "behind" it
            loc in listOf(
                pillar.position.transform(Direction.NORTH, 2),
                pillar.position.transform(Direction.NORTH, 2).transform(Direction.EAST, 1),
                pillar.position.transform(Direction.NORTH, 3),
                pillar.position.transform(Direction.NORTH, 3).transform(Direction.EAST, 1)
            )
        }
    }

    fun getLowestHpPillars(pillars: List<Npc>): List<Npc> {
        val stillUp = pillars.filter { !it.isDead && !it.isFinished && it.hitpoints > 0 }
        if (stillUp.isEmpty()) return emptyList()
        val minHp = stillUp.minOf { it.hitpoints }
        return stillUp.filter { it.hitpoints == minHp }
    }

    fun Npc.launchScream(pillars: List<Npc>) {
        playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_END)
        playGraphics(WhispererConstants.WHISPERER_SOUL_SIPHON_EFFECT_FULLY.id, height = 125)

        // -- Step 1: Screech sounds to players
        instance.players.forEach {
            it.playSound(WhispererConstants.WHISPERER_SCREECH_MIDDLE_1_SYNTH)
            it.playSound(WhispererConstants.WHISPERER_SCREECH_MIDDLE_2_SYNTH)
            it.playSound(WhispererConstants.WHISPERER_SCREECH_MIDDLE_3_SYNTH, delay = 5)
            it.playSound(WhispererConstants.WHISPERER_SCREECH_MIDDLE_4_SYNTH, delay = 2)
            it.playSound(WhispererConstants.WHISPERER_SCREECH_MIDDLE_5_SYNTH, delay = 15)
        }

        val lowestHpPillars = getLowestHpPillars(pillars)
        WhispererConstants.SCREECH_SPOTANIMS.entries.forEach { (staticPosition, delay) ->
            val position = instance[staticPosition.transform(256, 0, 0)]

            val covering = getCoveringPillars(position, pillars)
            if (covering.isEmpty()) {
                position.spotanim(WhispererConstants.WHISPERER_MEDIUM_POOL_BUBBLES, delay = delay)
            }
        }

        WhispererConstants.SCREECH_SPOTANIMS.forEach { (staticPos, gfxDelay) ->
            val pos = instance[staticPos]

            val covering = getCoveringPillars(pos, pillars)
            if (covering.isEmpty()) {
                pos.spotanim(WhispererConstants.WHISPERER_MEDIUM_POOL_BUBBLES, delay = gfxDelay)
            }
        }

        val pillarsToDoubleDamage = mutableSetOf<Npc>() // We'll keep track of "wrong" pillars here
        val playerDamage = if (awakened) 70 else 45

        instance.players.forEach { player ->
            val coveringPillars = getCoveringPillars(player.position, pillars)
            if (coveringPillars.isEmpty()) {
                player.removeHitpoints(Hit(playerDamage, HitType.REGULAR))
                player.reduceSanity(playerDamage)
            } else {
                val coveredByLowest = coveringPillars.any { it in lowestHpPillars }
                if (!coveredByLowest) {
                    player.removeHitpoints(Hit(playerDamage, HitType.REGULAR))
                    player.reduceSanity(playerDamage)

                    pillarsToDoubleDamage.addAll(coveringPillars)
                }
            }
        }

        pillars.forEach { pillar ->
            val damageAmount = if (pillar in pillarsToDoubleDamage) 40 else 20
            val hit = Hit(damageAmount, HitType.REGULAR)

            if (pillar.position.inArea(
                    WhispererConstants.LASSAR_UNDERGROUND_SHADOW_REALM_AREA,
                    instanceArea
                )
            ) {
                pillar.applyHit(hit)
            } else {
                pillar.removeHitpoints(hit)
            }
        }
    }


    /** Enrages the Whisperer by canceling current combat, healing a bit, and scheduling a delayed task to notify players. */
    fun NPC.enrage() {
        val players = instance.players

        blockIncomingHits()
        applyHit(Hit(if (awakened) 250 else 140, HitType.HEALED))
        attackCounterStandard = 0
        phase = SpecialPhase.ENRAGED
        schedule {
            combat.combatDelay = 4
        }

        schedule(2) {
            players?.forEach { player ->
                if (!player.location.inArea(
                        WhispererConstants.LASSAR_UNDERGROUND_SHADOW_REALM_AREA,
                        instanceArea
                    )
                ) {
                    player.sendMessage("<col=ff289d>The Whisperer pulls you into the Shadow Realm...")
                    player.activateBlackstone(instance, force = true)
                }
            }
        }
    }

    /** Performs the SHADOW_LEECHES special attack using a WorldTask. */
    fun NPC.performShadowLeeches() {
        val combatTask = object : WorldTask {
            var tick = 0
            val objects = mutableListOf<WorldObject>()

            override fun stop() {
                super.stop()
            }

            override fun run() {
                setFaceEntity(null)

                when (tick) {
                    0 -> {
                        playAnimation(WhispererConstants.WHISPERER_SWITCH_PHASE_START)
                    }

                    1 -> {
                        setLocation(respawnTile)
                        playAnimation(WhispererConstants.WHISPERER_SWITCH_PHASE_END_PAUSE)
                        instance.players?.forEach { player ->
                            player.sendMessage("<col=a53fff>Leeches begin to grow from the shadows...")
                        }
                    }

                    2 -> {
                        playAnimation(WhispererConstants.WHISPERER_NAMASTE)
                    }

                    4 -> {
                        instance.players?.forEach {
                            it.cancelCombat()
                        }

                        val rot = random(0, WhispererConstants.WHISPERER_LEECHES.size - 1)
                        val staticPosition = instance[position]
                        objects += position.addLeechRotation(rot)
                        objects += instance[staticPosition.transform(-256, 0, 0)].addLeechRotation(rot)

                        objects.forEach {
                            addObject(it)
                        }

                        instance.prepareDarkFragment()
                        instance.players?.forEach { it.playSound(2401) }
                    }

                    5 -> {
                        whispererTimerMax = (77.0 * (if (awakened) 1.5 else 2.0)).roundToInt()
                        whispererTimerCurrent = whispererTimerMax
                        launchWhispererTimer()
                        playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_START)
                    }

                    7 -> {
                        whispererTimerMax = (90.0 * (if (awakened) 1.5 else 2.0)).roundToInt()
                        whispererTimerCurrent = whispererTimerMax

                        launchWhispererTimer()

                        playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_START)
                    }

                    9 -> {
                        playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_PAUSE)

                        val timerCondition = containsTimer(WhispererConstants.WHISPERER_HEADBAR_TIMER)
                        val objectCondition =
                            objects.any { it.exists() && it.id == CORRUPTED_SEED }

                        if (timerCondition && objectCondition) {
                            instance.players?.forEach {
                                it.checkForLeech(instance)
                            }
                            return
                        }

                    }

                    10 -> {
                        val failed = objects.any { it.exists() && it.id == CORRUPTED_SEED }
                        if (failed) {
                            val projectile = ProjConstants.WHISPERER_LEECH
                            instance.players?.forEach { player ->
                                objects.filter {
                                    player.hasLineOfSightTo(it.position)
                                }.forEach {
                                    projectile.build(it.position, player)
                                }

                                player.sendMessage("<col=e00a19>Leeches hatch, draining your health and mind!")
                                player.reduceSanity(75)
                                player.removeHitpoints(Hit(75, HitType.REGULAR))
                            }
                        } else {
                            instance.players?.forEach { player ->
                                player.increaseSanity(if (awakened) 20 else 15)
                                player.sendMessage("<col=06600c>You deter the shadow leeches, regaining some sanity.")
                            }
                        }

                        instance.clearDarkFragment()
                        playAnimation(WhispererConstants.WHISPERER_RISE_GLOWING_END)

                        objects.forEach {
                            if (!it.exists()) return@forEach

                            it.position.spotanim(
                                when (it.id) {
                                    CORRUPTED_SEED_47575 -> WhispererConstants.WHISPERER_LEECH_BLUE_BALL_REMOVE
                                    CORRUPTED_SEED_47574 -> WhispererConstants.WHISPERER_LEECH_BLUE_REALM_BALL_REMOVE
                                    CORRUPTED_SEED -> WhispererConstants.WHISPERER_LEECH_GREEN_BALL_EXPLODE
                                    else -> return@forEach
                                }
                            )
                            it.remove()
                            removeObjects(it)
                        }
                        objects.clear()
                    }

                    12 -> {
                        instance.returnToOverworld()
                    }

                    14 -> {
                        val target = instance.players?.randomOrNull()
                        if (target != null) {
                            performBindingSpecial(target)
                        } else {
                            phase = if (isEnraged) SpecialPhase.ENRAGED else SpecialPhase.STANDARD
                        }

                        stop()
                    }
                }
                tick++
            }
        }
        this@WhispererNPC.combatTask = schedule(combatTask, 0, 0)
    }

    fun Location.addLeechRotation(rotation: Int): List<WorldObject> {
        val regionSouthWest = Location((regionX shl 6) - 24, (regionY shl 6) - 24, plane)
        val overworld = !inArea(WhispererConstants.LASSAR_UNDERGROUND_SHADOW_REALM_AREA, instance)
        return WhispererConstants.WHISPERER_LEECHES[rotation].map { (offset, alive) ->
            val targetLocation = regionSouthWest.transform(offset.first, offset.second, 0)
            targetLocation.spotanim(WhispererConstants.WHISPERER_LEECH_POOL)
            val objectId = when {
                overworld -> CORRUPTED_SEED_47575
                alive -> CORRUPTED_SEED
                else -> CORRUPTED_SEED_47574
            }
            MapObject(objectId, targetLocation, 10, 0).spawn()
        }
    }

    private fun NPC.launchWhispererTimer() {
        whispererTimerMax = (whispererTimerMax * 1.2).roundToInt()
        whispererTimerCurrent = whispererTimerMax

        addHitbar(RemoveHitBar(hitBar.type))
        addHitbar(timerHitbar)

        launchTimer(WhispererConstants.WHISPERER_HEADBAR_TIMER, 1) {
            if (whispererTimerCurrent <= 0 || whispererTimerMax <= 0) {
                addHitbar(RemoveHitBar(timerHitbar.type))
                addHitbar()

                false
            } else {
                whispererTimerCurrent -= 5

                addHitbar(RemoveHitBar(hitBar.type))
                addHitbar(timerHitbar)

                whispererTimerCurrent > 0
            }
        }
    }

    /** Performs the binding special attack: plays animations, sends a bind projectile, roots the target, then moves in. */
    fun NPC.performBindingSpecial(target: Player) {
        addHitbar(RemoveHitBar(timerHitbar.type))
        addHitbar()

        val rootTask = object : WorldTask {
            var tick = 0

            override fun stop() {
                super.stop()

                this@WhispererNPC.phase = if (isEnraged) SpecialPhase.ENRAGED else SpecialPhase.STANDARD
                this@WhispererNPC.combatTask = null
            }

            override fun run() {
                when (tick) {
                    0 -> {
                        this@WhispererNPC.combatTask = null

                        phase = SpecialPhase.BINDING
                        playAnimation(WhispererConstants.WHISPERER_NAMASTE)
                        target.playSound(WhispererConstants.WHISPERER_BIND_SYNTH)
                    }

                    1 -> {
                        ProjConstants.WHISPERER_BIND.build(this@performBindingSpecial, target)
                    }

                    4 -> {
                        target.freeze(secondsToTicks(30))
                        target.playGraphics(WhispererConstants.WHISPERER_BIND_HIT.id, height = 100)
                        target.sendMessage("<col=ff3045>The Whisperer binds you in place!")
                        target.playSound(WhispererConstants.WHISPERER_BIND_SYNTH)
                    }

                    5 -> {
                        if (!isFrozen && !occupiedArea.isInMeleeDistance(target.occupiedArea)) {
                            moveToTarget(target)
                            return
                        }

                        phase = if (isEnraged) SpecialPhase.ENRAGED else SpecialPhase.STANDARD
                        combat.combatDelay = 0
                    }

                    6 -> {
                        resetWalkSteps()
                    }

                    7 -> {
                        target.resetFreeze()
                        stop()
                    }
                }
                tick++
            }
        }
        schedule(rootTask, 0, 0, target)
    }

    fun Npc.moveToTarget(target: Player) {
        setRunSilent(awakened)
        addWalkStepsInteract(
            target.position.x, target.position.y, if (awakened) 2 else 1, size, true
        )
    }

    private fun performBasicAttackPrePhase(target: Player) {
        val attackStyles: List<AttackType> = if (randomBool()) {
            if (awakened)
                listOf(AttackType.RANGED, AttackType.RANGED, AttackType.MAGIC, AttackType.MAGIC, AttackType.MAGIC)
            else
                listOf(AttackType.RANGED, AttackType.RANGED, AttackType.RANGED)
        } else {
            if (awakened)
                listOf(AttackType.MAGIC, AttackType.MAGIC, AttackType.RANGED, AttackType.RANGED, AttackType.RANGED)
            else
                listOf(AttackType.MAGIC, AttackType.MAGIC, AttackType.MAGIC)
        }
        val attacks = sequenceBasics(BasicAttackSide.RANDOM, BasicAttackSpeed.FASTER, *attackStyles.toTypedArray())
        scheduleBasicAttacks(attacks, target) {
            tentaclesXCross(target)
        }
    }

    private fun performBasicAttackPostPhase(target: Player) {
        val attackStyles: List<AttackType> = if (randomBool()) {
            if (awakened)
                listOf(AttackType.RANGED, AttackType.MAGIC, AttackType.RANGED, AttackType.RANGED)
            else
                listOf(AttackType.RANGED, AttackType.RANGED, AttackType.MAGIC)
        } else {
            if (awakened)
                listOf(AttackType.MAGIC, AttackType.RANGED, AttackType.MAGIC, AttackType.MAGIC)
            else
                listOf(AttackType.MAGIC, AttackType.MAGIC, AttackType.RANGED)
        }
        val attacks = sequenceBasics(BasicAttackSide.RANDOM, BasicAttackSpeed.FASTER, *attackStyles.toTypedArray())
        scheduleBasicAttacks(attacks, target) {
            tentaclesXCross(target)
        }
    }

    private fun performBasicAttackPostAllPhases(target: Player) {
        val attackStyles: List<AttackType> = if (randomBool()) {
            if (awakened)
                listOf(AttackType.RANGED, AttackType.MAGIC, AttackType.RANGED, AttackType.MAGIC, AttackType.RANGED)
            else
                listOf(AttackType.RANGED, AttackType.MAGIC, AttackType.RANGED)
        } else {
            if (awakened)
                listOf(AttackType.MAGIC, AttackType.RANGED, AttackType.MAGIC, AttackType.RANGED, AttackType.MAGIC)
            else
                listOf(AttackType.MAGIC, AttackType.RANGED, AttackType.MAGIC)
        }
        val attacks = sequenceBasics(BasicAttackSide.RANDOM, BasicAttackSpeed.FASTEST, *attackStyles.toTypedArray())
        scheduleBasicAttacks(attacks, target) {
            tentaclesPlusCross(target)
        }
    }

    private fun performBasicAttackEnraged(target: Player) {
        val firstStyle = if (awakened) AttackType.MAGIC else AttackType.RANGED
        val oppositeStyle = if (firstStyle == AttackType.RANGED) AttackType.MAGIC else AttackType.RANGED
        val attackStyles = if (attackCounterStandard % 2 == 0)
            listOf(firstStyle, firstStyle)
        else
            listOf(oppositeStyle, oppositeStyle)
        val attacks = sequenceBasics(BasicAttackSide.RANDOM, BasicAttackSpeed.FASTEST, *attackStyles.toTypedArray())
        scheduleBasicAttacks(attacks, target) {
            tentaclesSingle(target)
        }
        attackCounterStandard++
    }

    fun scheduleBasicAttacks(
        attacks: List<BasicAttackVariant>,
        target: Player,
        tentacleBlock: () -> WorldTask
    ) {
        val task = object : WorldTask {
            var index = 0
            var delayCounter = 0
            override fun run() {
                if (index < attacks.size) {
                    val currentVariant = attacks[index]
                    val delayPerAttack = when (currentVariant.speed) {
                        BasicAttackSpeed.SLOW -> 3
                        BasicAttackSpeed.FASTER -> 2
                        BasicAttackSpeed.FASTEST -> 1
                    }
                    if (delayCounter < delayPerAttack - 1) {
                        delayCounter++
                    } else {
                        if (meleeActive || !performBasicAttack(currentVariant, target)) {
                            stop()
                            return
                        }
                        index++
                        delayCounter = 0
                    }
                } else {
                    tentacleTask = schedule(tentacleBlock(), 5, 0, target)
                    stop()
                }
            }
        }

        if (phase?.isSpecial == true) {
            return
        }
        combatTask = schedule(task, 0, 0, target)
    }

    fun removeTentacles() {
        val tentacles = location.findNpcs { id == TENTACLE }
        tentacles.forEach { it.remove() }
    }

    fun performBasicMelee(target: Player) {
        val maximumDamageBase = if (awakened) 57 else 42
        val maximumDamage = if (target.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) {
            maximumDamageBase / 2
        } else {
            maximumDamageBase
        }
        val minimumDamage = 15
        val calculatedDamage = random(minimumDamage, maximumDamage)
        meleeActive = true

        val combatTask = object : WorldTask {
            var tick = 0

            override fun run() {
                tick++

                if (tick == 1) {
                    playAnimation(WhispererConstants.WHISPERER_ATTACK_MELEE)
                    target.applyHit(
                        Hit(this@WhispererNPC, calculatedDamage, HitType.MELEE)
                    )
                } else if (tick == 2) {
                    playAnimation(WhispererConstants.WHISPERER_ATTACK_MELEE)
                    target.applyHit(
                        Hit(this@WhispererNPC, calculatedDamage, HitType.MELEE)
                    )

                } else if (tick != -1) {
                    if (!isWithinMeleeDistance(this@WhispererNPC, target)) {
                        tick = -1
                        val combatTask = object : WorldTask {
                            override fun run() {
                                meleeActive = false
                                stop()
                            }
                        }
                        this@WhispererNPC.combatTask = schedule(combatTask, 1, 0, target)
                    } else {
                        meleeActive = false
                    }

                    stop()
                }
            }
        }
        this.combatTask = schedule(combatTask, 0, 0, target)
    }

    private fun performBasicAttack(variant: BasicAttackVariant, target: Player) : Boolean {
        if (phase?.isSpecial == true) {
            return false
        }

        if (isWithinMeleeDistance(this, target)) {
            stopTask(combatTask)
            performBasicMelee(target)
            return false
        } else {
            playAnimation(variant.anim)
            val projDelay = variant.projectile.build(this, target)
            schedule(delay = projDelay) {
                val minimumDamage = 10
                val maximumDamage = if (awakened) {
                    if (variant.style == AttackType.MAGIC) 45 else 53
                } else {
                    if (variant.style == AttackType.MAGIC) 36 else 40
                }

                target.scheduleHit(
                    this, Hit(
                        this,
                        random(minimumDamage, maximumDamage),
                        if (variant.style == AttackType.RANGED) HitType.RANGED else HitType.MAGIC,
                    ), 0
                )
            }
            return true
        }
    }


    fun tentaclesXCross(target: Player): WorldTask {
        val dirs = if (awakened) Direction.entries.toList() else Direction.intercardinalDirections.toList()
        return tentacleFormation(target, dirs)
    }

    fun tentaclesPlusCross(target: Player): WorldTask {
        val dir = Direction.fromJagAngle(jagAngle(target).toInt())
        val primary = if (dir == Direction.NORTH || dir == Direction.SOUTH) Direction.EAST else Direction.NORTH
        val positions = listOf(
            primary to target.position.transform(primary, 4),
            null to target.position.copy(),
            primary.opposing to target.position.transform(primary.opposing, 4)
        )
        return tentacleFormation(*positions.toTypedArray())
    }

    fun tentaclesSingle(target: Player): WorldTask {
        val chosen = Direction.entries.shuffled().take(if (awakened) random(2, 3) else random(1, 2))
        val positions = chosen.map { it to target.position.transform(it, 4) }
        return tentacleFormation(*positions.toTypedArray())
    }

    // Helper: Overloaded tentacleFormation – accepts a Player and a list of Directions.
    fun tentacleFormation(target: Player, directions: List<Direction>): WorldTask {
        val positions = directions.map { it to target.position.transform(it, 4) }
        return tentacleFormation(*positions.toTypedArray())
    }

    fun Npc.tentacleFormation(vararg entries: Pair<Direction?, Location>): WorldTask {
        val tentacles = mutableListOf<Npc>()
        val task = object : WorldTask {
            var tick = 0
            override fun run() {
                when (tick) {
                    0 -> {
                        // Spawn tentacles at each entry’s location.
                        for ((direction, pos) in entries) {
                            if (!pos.isTileFree ||
                                pos.occupiedArea.intersectsWith(occupiedArea) ||
                                !pos.inArea(WhispererConstants.THE_WHISPERER_FIGHT_AREA, instance)
                            ) continue
                            val tentacle = NPC(
                                TENTACLE_12208,
                                pos,
                                direction?.opposing ?: Direction.fromJagAngle(this@WhispererNPC.direction),
                                0
                            ).spawn()
                            tentacle.playAnimation(WhispererConstants.WHISPERER_TENTACLE_SPAWN)
                            tentacle.playGraphics(WhispererConstants.WHISPERER_MEDIUM_POOL_SPLASH_2)
                            tentacles.add(tentacle)
                        }
                    }

                    1 -> {
                        for (tentacle in tentacles) {
                            tentacle.playAnimation(WhispererConstants.WHISPERER_TENTACLE_DESPAWN)
                            tentacle.playGraphics(WhispererConstants.WHISPERER_WAVE_PUSH)
                        }
                        instance.players.forEach { player ->
                            player.playSound(WhispererConstants.WHISPERER_WAVE_1_SYNTH, delay = 2)
                            player.playSound(WhispererConstants.WHISPERER_WAVE_2_SYNTH, delay = 7)
                            player.playSound(WhispererConstants.WHISPERER_WAVE_3_SYNTH, delay = 18)
                            player.playSound(WhispererConstants.WHISPERER_WAVE_4_SYNTH, delay = 21)
                        }
                    }

                    2 -> {
                        for (tentacle in tentacles) {
                            for (step in 1..4) {
                                val translated =
                                    tentacle.location.transform(Direction.fromJagAngle(tentacle.direction), step)
                                translated.spotanim(
                                    when (step) {
                                        1 -> WhispererConstants.WHISPERER_SMALL_POOL_BUBBLES
                                        2 -> WhispererConstants.WHISPERER_SMALL_POOL_SPLASH
                                        3 -> WhispererConstants.WHISPERER_MEDIUM_POOL_SPLASH
                                        4 -> WhispererConstants.WHISPERER_LARGE_POOL_SPLASH
                                        else -> error("Invalid step: $step")
                                    },
                                    delay = 5 * step
                                )
                                if (step == 4) {
                                    translated.playSound(
                                        WhispererConstants.WHISPERER_WAVE_REACHED_SYNTH,
                                        radius = 5,
                                        delay = 21
                                    )
                                }
                            }
                        }
                    }

                    4 -> {
                        // Pause 1 tick, then remove all tentacles.
                        for (tentacle in tentacles) {
                            tentacle.remove()
                        }
                        tentacles.clear()

                        val damage = if (awakened) 30 else 20
                        val affectedPositions = entries.flatMap { (direction, pos) ->
                            listOfNotNull(
                                pos.transform(direction?.opposing, 4),
                                if (!awakened) null else pos.transform(direction?.opposing, 3)
                            )
                        }.toSet()
                        for (pos in affectedPositions) {
                            for (player in pos.players) {
                                player.removeHitpoints(Hit(damage, HitType.REGULAR))
                                player.reduceSanity(amount = damage)
                            }
                        }
                        stop()
                    }
                }
                tick++
            }
        }
        return schedule(task, 0, 0)
    }


    override fun isForceAggressive(): Boolean = true

    override fun remove() {
        super.remove()
        stopTask(combatTask)
    }

    override fun isMovableEntity(): Boolean {
        val movable = phase == SpecialPhase.BINDING || phase?.isSpecial == false
        if (movable) {
            return true
        }

        resetWalkSteps()
        return false
    }

    override fun addWalkSteps(destX: Int, destY: Int, maxStepsCount: Int, check: Boolean): Boolean {
        if (!isMovableEntity()) {
            resetWalkSteps()
            return false
        }
        return super.addWalkSteps(destX, destY, maxStepsCount, check)
    }

    override fun addWalkStepsInteract(
        destX: Int, destY: Int, maxStepsCount: Int, size: Int,
        calculate: Boolean
    ): Boolean {
        if (!isMovableEntity()) {
            resetWalkSteps()
            return false
        }
        return WalkStep.addWalkStepsInteract(this, destX, destY, maxStepsCount, size, size, calculate)
    }

    override fun calcFollow(
        target: Position?, maxStepsCount: Int, calculate: Boolean,
        intelligent: Boolean, checkEntities: Boolean
    ): Boolean {
        if (!isMovableEntity()) {
            resetWalkSteps()
            return false
        }
        return super.calcFollow(target, maxStepsCount, calculate, intelligent, checkEntities)
    }

    override fun canMove(fromX: Int, fromY: Int, direction: Int): Boolean {
        if (!isMovableEntity()) {
            resetWalkSteps()
            return false
        }
        return super.canMove(fromX, fromY, direction)
    }

    override fun setFaceEntityCombat(entity: Entity?) {
        if (!isMovableEntity()) {
            resetWalkSteps()
            setFaceEntity(null)
        } else {
            super.setFaceEntityCombat(entity)
        }
    }

    override fun performDefenceAnimation(attacker: Entity?) {

    }
}

class WhispererTimerHitBar(val npc: Npc) : HitBar() {
    override fun getType(): Int {
        return 35
    }

    override fun getPercentage(): Int {
        val multiplier = getMultiplier()
        val current = npc.whispererTimerCurrent
        val maximum = npc.whispererTimerMax

        val mod: Float = maximum.toFloat() / multiplier
        return (multiplier - min(((current + mod) / mod).toInt().toDouble(), multiplier.toDouble())).toInt()
    }

    private fun getMultiplier(): Int {
        val type = type
        return HitbarDefinitions.get(type).size
    }
}

enum class BasicAttackVariant(
    val style: AttackType,
    val side: BasicAttackSide,
    val speed: BasicAttackSpeed,
    val glowing: Boolean,
    val anim: Animation,
    val projectile: Projectile
) {
    RANGED_LEFT_SLOW(
        style = AttackType.RANGED,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.SLOW,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_RIGHT_SLOW(
        style = AttackType.RANGED,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.SLOW,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_LEFT_FASTER(
        style = AttackType.RANGED,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTER,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_RIGHT_FASTER(
        style = AttackType.RANGED,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTER,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_LEFT_FASTEST(
        style = AttackType.RANGED,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED_FASTEST
    ),
    RANGED_RIGHT_FASTEST(
        style = AttackType.RANGED,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED_FASTEST
    ),
    MAGIC_LEFT_SLOW(
        style = AttackType.MAGIC,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.SLOW,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_RIGHT_SLOW(
        style = AttackType.MAGIC,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.SLOW,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_LEFT_FASTER(
        style = AttackType.MAGIC,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTER,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_RIGHT_FASTER(
        style = AttackType.MAGIC,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTER,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_LEFT_FASTEST(
        style = AttackType.MAGIC,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC_FASTEST
    ),
    MAGIC_RIGHT_FASTEST(
        style = AttackType.MAGIC,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = false,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC_FASTEST
    ),

    // Glowing variants:
    RANGED_LEFT_SLOW_GLOWING(
        style = AttackType.RANGED,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.SLOW,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_GLOWING_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_RIGHT_SLOW_GLOWING(
        style = AttackType.RANGED,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.SLOW,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_LEFT_FASTER_GLOWING(
        style = AttackType.RANGED,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTER,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_GLOWING_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_RIGHT_FASTER_GLOWING(
        style = AttackType.RANGED,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTER,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED
    ),
    RANGED_LEFT_FASTEST_GLOWING(
        style = AttackType.RANGED,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_GLOWING_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED_FASTEST
    ),
    RANGED_RIGHT_FASTEST_GLOWING(
        style = AttackType.RANGED,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_RANGED_FASTEST
    ),
    MAGIC_LEFT_SLOW_GLOWING(
        style = AttackType.MAGIC,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.SLOW,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_GLOWING_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_RIGHT_SLOW_GLOWING(
        style = AttackType.MAGIC,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.SLOW,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_SLOW,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_LEFT_FASTER_GLOWING(
        style = AttackType.MAGIC,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTER,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_GLOWING_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_RIGHT_FASTER_GLOWING(
        style = AttackType.MAGIC,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTER,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_FASTER,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC
    ),
    MAGIC_LEFT_FASTEST_GLOWING(
        style = AttackType.MAGIC,
        side = BasicAttackSide.LEFT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_LEFT_GLOWING_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC_FASTEST
    ),
    MAGIC_RIGHT_FASTEST_GLOWING(
        style = AttackType.MAGIC,
        side = BasicAttackSide.RIGHT,
        speed = BasicAttackSpeed.FASTEST,
        glowing = true,
        anim = WhispererConstants.WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_FASTEST,
        projectile = ProjConstants.WHISPERER_BASIC_MAGIC_FASTEST
    )
}

fun <T : Enum<T>> sequenceBasics(
    initialSide: BasicAttackSide,
    speed: BasicAttackSpeed,
    vararg attackStyles: AttackType
): List<BasicAttackVariant> {
    val glowingStates = attackStyles.mapIndexed { index, _ -> index % 2 == 1 }
    val firstSide = if (initialSide == BasicAttackSide.RANDOM) {
        if (randomBool()) BasicAttackSide.LEFT else BasicAttackSide.RIGHT
    } else {
        initialSide
    }
    val sides = glowingStates.mapIndexed { index, _ ->
        if (index % 2 == 0) firstSide else if (firstSide == BasicAttackSide.LEFT)
            BasicAttackSide.RIGHT else BasicAttackSide.LEFT
    }
    return attackStyles.mapIndexed { index, style ->
        getAttackVariant(style, sides[index], speed, glowingStates[index])
    }
}

fun getAttackVariant(
    style: AttackType,
    side: BasicAttackSide,
    speed: BasicAttackSpeed,
    glowing: Boolean
): BasicAttackVariant {
    return BasicAttackVariant.entries.first {
        it.style == style && it.side == side && it.speed == speed && it.glowing == glowing
    }
}

fun Player.checkForLeech(instance: WhispererInstance) {
    val insideUnderworld = position.inArea(WhispererConstants.LASSAR_UNDERGROUND_SHADOW_REALM_AREA, instance)
    // Get the static position from the dynamic instance.
    val staticPos = instance[position] ?: return
    // Calculate the overworld and underworld positions based on a 256–tile offset.
    val overworldPos = instance[staticPos.transform(if (insideUnderworld) 256 else 0, 0, 0)]
    val underworldPos = instance[staticPos.transform(if (insideUnderworld) 0 else -256, 0, 0)]
    if (overworldPos == null || underworldPos == null) return

    // Attempt to find a world object with a given shape (e.g. CENTREPIECE) at each location.
    val overworldObj: WorldObject = overworldPos.findObject {
        type == 10
    } ?: return
    val underworldObj: WorldObject = underworldPos.findObject {
        type == 10
    } ?: return

    playSound(WhispererConstants.WHISPERER_STEP_OVER_LEECH_SYNTH)

    val isGreen = underworldObj.id == CORRUPTED_SEED
    if (isGreen) {
        overworldPos.spotanim(WhispererConstants.WHISPERER_LEECH_BLUE_BALL_DISARM)
        underworldPos.spotanim(WhispererConstants.WHISPERER_LEECH_GREEN_BALL_DISARM)
    } else {
        overworldPos.spotanim(WhispererConstants.WHISPERER_LEECH_BLUE_BALL_EXPLODE)
        underworldPos.spotanim(WhispererConstants.WHISPERER_LEECH_BLUE_BALL_EXPLODE)
        applyHit(
            Hit(if (awakenedEncounter) random(40, 60) else random(15, 25), HitType.REGULAR)
        )
    }

    World.getObjectWithType(overworldObj.position, 10)?.remove()
    World.getObjectWithType(underworldObj.position, 10)?.remove()
}

fun Player.increaseSanity(amount: Int, hitsplat: Boolean = true) {
    val remainingTo100 = 100 - sanity
    if (remainingTo100 <= 0) return

    val increaseAmount = amount.coerceAtMost(remainingTo100)
    sanity += increaseAmount

    if (!hitsplat) return
    applyHit(Hit(increaseAmount, HitType.SANITY_RESTORE))
}

fun Player.reduceSanity(amount: Int, hitsplat: Boolean = true) {
    sanity -= amount
    if (sanity > 0) return

    sanity = 0

    launchTimer(WhispererConstants.WHISPERER_INSANE_DAMAGE_TIMER, 2) {
        if (sanity > 0) return@launchTimer false

        val currentTick = WorldThread.getCurrentCycle()
        if (it == currentTick) {
            sendMessage("<col=e00a19>The darkness of the Shadow Realm begins to consume your mind!")
        }

        val count = ((currentTick - it) / 2) + 1
        if (count > 20) {
            sanity = 100
            return@launchTimer false
        }

        applyHit(Hit(5 * count.toInt(), HitType.SANITY_RESTORE))
        return@launchTimer true
    }

    if (!hitsplat) return
    applyHit(Hit(amount, HitType.SANITY_RESTORE))
}

fun Player.activateBlackstone(instance: DynamicArea, force: Boolean = false) {
    if (!force && (!position.inArea(
            WhispererConstants.THE_WHISPERER_FIGHT_AREA,
            instance
        ) || !whispererBlackstoneReady)
    ) {
        sendMessage("The fragment does not respond to your touch.")
        return
    }

    lock()

    clientscript(1833, 0, 255, 0, 0, 20)
    playSound(3963)

    val task = object : WorldTask {
        var tick = 0

        override fun stop() {
            super.stop()

            unlock()
        }

        override fun run() {
            when (tick) {
                0 -> {
                    // Perform teleportation.
                    val wNpc = whisperer
                    if (wNpc != null) {
                        val staticPosPlayer: Location = instance[position]
                        val staticPosWhisperer: Location = instance[wNpc.position]
                        val insideShadowRealmArea = position.inArea(
                            WhispererConstants.LASSAR_UNDERGROUND_SHADOW_REALM_AREA,
                            instance
                        )

                        if (insideShadowRealmArea) {
                            teleport(instance[staticPosPlayer.transform(256, 0, 0)])
                            wNpc.setLocation(instance[staticPosWhisperer.transform(256, 0, 0)])
                        } else {
                            teleport(instance[staticPosPlayer.transform(-256, 0, 0)])
                            wNpc.setLocation(instance[staticPosWhisperer.transform(-256, 0, 0)])
                        }

                        wNpc.resetWalkSteps()
                    }
                }

                1 -> {
                    clientscript(1833, 0, 0, 0, 255, 20)
                    stop()
                }
            }
            tick++
        }
    }
    schedule(task, delay = 0, period = 0)
}

/**
 * Recalls the blackstone fragment.
 * If the player is in the proper area and the fragment is ready, a message is shown.
 */
fun Player.recallBlackstone() {
    if (!position.inArea(WhispererConstants.THE_WHISPERER_FIGHT_AREA) || !whispererBlackstoneReady) {
        sendMessage("The fragment does not respond to your touch.")
        return
    }
    dialogue {
        plain("You have nothing to recall.")
    }
}

fun DynamicArea.returnToOverworld() {
    players.forEach { player ->
        if (!player.position.inArea(WhispererConstants.LASSAR_UNDERGROUND_SHADOW_REALM_AREA, this) || player.isLocked) {
            return@forEach
        }
        player.activateBlackstone(this, force = true)
    }
}

fun DynamicArea.clearDarkFragment() {
    players.forEach { player ->
        if (!player.whispererBlackstoneReady) return@forEach
        player.sendMessage("<col=ff3045>Your blackstone fragment loses all its energy.")

        for (i in 0 until 28) {
            val slot = player.inventory.container.getSlotOf(BLACKSTONE_FRAGMENT)
            if (slot == -1) break
            player.inventory.replaceItem(BLACKSTONE_FRAGMENT_28357, 1, slot)
        }
        player.whispererBlackstoneReady = false
        player.playSound(WhispererConstants.WHISPERER_SOUL_SIPHON_END_SYNTH)
        player.playSound(WhispererConstants.WHISPERER_SOUL_SIPHON_END_2_SYNTH)
        player.playSound(WhispererConstants.WHISPERER_SOUL_SIPHON_RETURN_SYNTH, delay = 76)
    }
}

fun DynamicArea.prepareDarkFragment() {
    players.forEach { player ->
        if (player.whispererBlackstoneReady) return@forEach

        player.whispererBlackstoneReady = true
        player.sendMessage("<col=ef0083>Your blackstone fragment pulses with dark energy...")
        player.playSound(1664)

        for (i in 0 until 28) {
            val slot = player.inventory.container.getSlotOf(BLACKSTONE_FRAGMENT_28357)
            if (slot == -1) break
            player.inventory.replaceItem(BLACKSTONE_FRAGMENT, 1, slot)
        }
    }
}
