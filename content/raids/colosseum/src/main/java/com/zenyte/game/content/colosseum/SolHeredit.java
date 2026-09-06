package com.zenyte.game.content.colosseum;

import com.zenyte.game.content.skills.prayer.PrayerManagerKeys;
import com.zenyte.game.content.chambersofxeric.greatolm.scripts.Lightning;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SolHeredit extends NPC implements CombatScript {

    public static final Animation JUMP_DOWN_ANIMATION = new Animation(10877);
    private static final Animation ATTACK_SPEAR_ANIMATION = new Animation(10883);
    private static final Animation ATTACK_GRAPPLE_ANIMATION = new Animation(10884);
    private static final Animation ATTACK_SHIELD_ANIMATION = new Animation(10885);
    private static final Animation ATTACK_TRIPLE_LONG_ANIMATION = new Animation(10886);
    private static final Animation ATTACK_TRIPLE_SHORT_ANIMATION = new Animation(10887);

    public static final Graphics JUMP_DOWN_GFX = new Graphics(2666);
    private static final Graphics ATTACK_TRIPLE_LONG_GFX = new Graphics(2667);
    private static final Graphics ATTACK_TRIPLE_SHORT_GFX = new Graphics(2668);
    private static final Graphics BEAM_GRAPHIC = new Graphics(2698);

    public static final String TIMER_NAME = "Sol Heredit";
    private static final int SMOKE_DELAY_MOD = 2;
    private static final int[] SMOKE_SPOTANIMS = {2669, 2670, 2671, 2672};
    private static final int SPECIAL_ATTACK_COOLDOWN = 2;
    private static final int MIN_LASER_ORB_COOLDOWN = 25;
    private static final int MAX_LASER_ORB_COOLDOWN = 35;
    private static final int ENRAGE_LASER_ORB_COOLDOWN = 12;
    private static final String[] DEATH_MESSAGES = {
            "Your light shines... brightly...",
            "A worthy... opponent...",
            "Ralos has smiled upon you...",
            "My compliments, champion..."
    };
    public static final String[] KILL_PLAYER_MESSAGES = {
            "How disappointing...",
            "I knew you weren't the one.",
            "You had me excited for a moment.",
            "Your lack of coordination is concerning.",
            "Your light shines no more.",
            "Maybe next time...",
            "Pathetic, really...",
            "I was just getting into my rhythm...",
    };

    private final ColosseumInstance instance;
    private final Player target;
    private boolean firstSpear = true;
    private boolean firstShield = true;
    private int phaseId = -1;
    private Attack forceAttack = Attack.SPEAR;
    private int specialAttackCooldown;
    private final List<Attack> attackPool = new ArrayList<>(10);
    private final Map<Location, SolPool> pools = new HashMap<>();
    private int laserOrbCooldown = MIN_LASER_ORB_COOLDOWN;
    private final List<LaserOrb> laserOrbs = new ArrayList<>(4);
    private GrappleStyle grappleStyle = null;
    private int clickedSlot = -1;
    private int finalPhasePoolTimer = 7;//phase transition delay
    private long maxHitTicks = -1L;

    public SolHeredit(Location tile, ColosseumInstance instance) {
        super(NpcId.SOL_HEREDIT, tile, Direction.SOUTH, 5, true);
        this.maxDistance = 64;
        this.instance = instance;
        this.target = instance.getPlayer();
        this.deathDelay = 5;
    }

    @Override
    public void processNPC() {
        super.processNPC();

        //need these checks because it doesn't do them in the code!
        if (isDead() || isLocked()) {
            return;
        }

        laserOrbCooldown--;
        //Sol can change phases at any time, even if not attacking
        if ((phaseId + 1) < Phase.phases.length && getHitpoints() <= Phase.phases[phaseId + 1].hp) {
            if (phaseId >= 0) {
                forceAttack = Attack.PHASE_TRANSITION;
            }

            phaseId++;
            say(Phase.phases[phaseId].message);
        }
        if (!pools.isEmpty() || target.isDead() || target.isFinished() || target.isNulled()) {
            for (Map.Entry<Location, SolPool> entry : pools.entrySet()) {
                SolPool pool = entry.getValue();
                if (pool == null || !pool.getLocation().matches(target.getLocation()) || !pool.spawned) {
                    continue;
                }

                //TODO DMG? is it correct?
                target.applyHit(new Hit(this, Utils.random(6, 8), HitType.REGULAR));
            }
        }
        if (this.phaseId == 5 && --this.finalPhasePoolTimer == 0) {
            this.tryToPlacePools(target.getLocation().getX(), target.getLocation().getY(), 1);
            this.finalPhasePoolTimer = 3;
        }
    }

    public void say(String line) {
        setForceTalk(line);
        String lineToSay = getName() + ": " + Colour.BLUE.wrap(line);
        target.sendMessage(lineToSay);
    }

    @Override
    public int attack(Entity target) {
        Attack nextAttack = selectAttack();
        forceAttack = null;
        if (nextAttack != Attack.PHASE_TRANSITION && !laserOrbs.isEmpty() && laserOrbCooldown < 0) {
            fireOrbs();
        }
        return switch (nextAttack) {
            case SPEAR -> {
                specialAttackCooldown--;
                yield attackSpear();
            }
            case SHIELD -> {
                specialAttackCooldown--;
                yield attackShield();
            }
            case PHASE_TRANSITION -> {
                forceAttack = Attack.SPEAR;
                yield phaseTransition(phaseId);
            }
            case TRIPLE_SHORT -> {
                specialAttackCooldown = SPECIAL_ATTACK_COOLDOWN;
                yield attackTripleShort();
            }
            case TRIPLE_LONG -> {
                specialAttackCooldown = SPECIAL_ATTACK_COOLDOWN;
                yield attackTripleLong();
            }
            case GRAPPLE -> {
                specialAttackCooldown = SPECIAL_ATTACK_COOLDOWN;
                yield attackGrapple();
            }
        };
    }

    private Attack selectAttack() {
        if (this.forceAttack != null) {
            return this.forceAttack;
        }

        attackPool.clear();
        attackPool.add(Attack.SHIELD);
        attackPool.add(Attack.SHIELD);
        attackPool.add(Attack.SPEAR);
        attackPool.add(Attack.SPEAR);
        if (this.specialAttackCooldown <= 0) {
            if (this.phaseId >= 3) {
                attackPool.add(Attack.TRIPLE_LONG);
            } else if (this.phaseId >= 1) {
                attackPool.add(Attack.TRIPLE_SHORT);
            }

            if (this.phaseId >= 2) {
                attackPool.add(Attack.GRAPPLE);
            }
        }

        return Utils.random(attackPool);
    }

    private int attackGrapple() {
        freeze(5);
        firstSpear = true;
        firstShield = true;
        clickedSlot = -1;
        grappleStyle = Utils.random(GrappleStyle.values);
        say(grappleStyle.message);
        WorldTasksManager.schedule(new WorldTask() {

            private void checkParry() {
                if (clickedSlotTemp == -1) {
                    clickedSlotTemp = clickedSlot;
                }
            }

            int clickedSlotTemp = -1;
            int ticks = 0;

            @Override
            public void run() {
                if (ticks == 0) {
                    checkParry();
                    setAnimation(ATTACK_GRAPPLE_ANIMATION);
                } else if (ticks == 1) {
                    checkParry();
                } else if (ticks == 2) {
                    checkParry();
                } else if (ticks == 3) {
                    if (clickedSlot == grappleStyle.slot.getSlot()) {
                        if (clickedSlotTemp != -1) {
                            target.sendMessage(Colour.RS_GREEN.wrap("You successfully defend your " + grappleStyle.bodyPart + " from Sol Heredit's grapple!"));
                        } else {
                            target.sendMessage(Colour.RS_GREEN.wrap("You perfectly parry Sol Heredit's grapple!"));
                            maxHitTicks = WorldThread.getCurrentCycle() + 5;
                        }
                    } else {
                        delayHit(SolHeredit.this, -1, target, new Hit(SolHeredit.this, 20 + Utils.random(25), HitType.REGULAR));
                        String bodyPart = grappleStyle.bodyPart;
                        Item item = target.getEquipment().getItem(grappleStyle.slot);
                        if (item != null) {
                            bodyPart = item.getName();
                        }

                        target.sendMessage("Sol Heredit grabs hold of your " + Colour.TURQOISE.wrap(bodyPart) + " and deals massive damage!");
                    }
                    grappleStyle = null;
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
        return 7;
    }

    private void fireOrbs() {
        for (LaserOrb laserOrb : laserOrbs) {
            laserOrb.queueFire();
        }
        if (this.phaseId < 5) {
            this.laserOrbCooldown = Utils.random(MIN_LASER_ORB_COOLDOWN, MAX_LASER_ORB_COOLDOWN);
        } else {
            this.laserOrbCooldown = ENRAGE_LASER_ORB_COOLDOWN;
        }
        WorldTasksManager.schedule(() -> target.getPacketDispatcher().sendSoundEffect(new SoundEffect(8253)), 3);
        WorldTasksManager.schedule(() -> target.getPacketDispatcher().sendSoundEffect(new SoundEffect(8230)), 7);
    }

    private int attackTripleShort() {
        this.firstShield = true;
        this.firstSpear = true;
        // used above 50%
        this.setAnimation(ATTACK_TRIPLE_SHORT_ANIMATION);
        setGraphics(ATTACK_TRIPLE_SHORT_GFX);
        this.attackTriple(true);
        return this.phaseId >= 2 ? 11 : 12; // should be 11 between 50% and 75%
    }

    private int attackTripleLong() {
        this.firstShield = true;
        this.firstSpear = true;
        // used above 50%
        this.setAnimation(ATTACK_TRIPLE_LONG_ANIMATION);
        setGraphics(ATTACK_TRIPLE_LONG_GFX);
        this.attackTriple(false);
        return 12;
    }

    private void attackTriple(boolean isShort) {
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            boolean prayerWasActive = false;

            private void checkPrayer() {
                if (!prayerWasActive && PrayerManagerKeys.prayerManager(target).isActive(Prayer.PROTECT_FROM_MELEE)) {
                    prayerWasActive = true;
                    target.sendMessage(Colour.RED.wrap("Sol Heredit doesn't take kindly to your eager prayer."));
                }
            }

            private void injure() {
                Lightning.deactivateOverheadProtectionPrayers(target, PrayerManagerKeys.prayerManager(target), false);
            }

            @Override
            public void run() {
                if (target.isDead() || target.isFinished() || target.isNulled() || isFinished() || isDead()) {
                    stop();
                    return;
                }

                //setForceTalk("ticks = " + ticks);
                if (ticks == 0) {
                    checkPrayer();
                } else if (ticks == 1) {
                    checkPrayer();
                    if (prayerWasActive) {
                        injure();
                    }
                } else if (ticks == 2) {
                    if (prayerWasActive || !PrayerManagerKeys.prayerManager(target).isActive(Prayer.PROTECT_FROM_MELEE)) {
                        delayHit(SolHeredit.this, -1, target, new Hit(SolHeredit.this, 15, HitType.REGULAR));
                    }
                    prayerWasActive = false;
                } else if (ticks == 3) {
                    checkPrayer();
                } else if (ticks == 4) {
                    checkPrayer();
                    if (prayerWasActive) {
                        injure();
                    }
                } else if (ticks == 5) {
                    if (prayerWasActive || !PrayerManagerKeys.prayerManager(target).isActive(Prayer.PROTECT_FROM_MELEE)) {
                        delayHit(SolHeredit.this, -1, target, new Hit(SolHeredit.this, isShort ? 25 : 30, HitType.REGULAR));
                    }
                    prayerWasActive = false;
                } else if (ticks == 6) {
                    checkPrayer();
                }
                if (isShort) {
                    if (ticks == 6) {
                        checkPrayer();
                    } else if (ticks == 7) {
                        checkPrayer();
                        if (prayerWasActive) {
                            injure();
                        }
                    } else if (ticks == 8) {
                        if (prayerWasActive || !PrayerManagerKeys.prayerManager(target).isActive(Prayer.PROTECT_FROM_MELEE)) {
                            delayHit(SolHeredit.this, -1, target, new Hit(SolHeredit.this, 35, HitType.REGULAR));
                        }
                    } else if (ticks == 9) {
                    } else if (ticks == 10) {
                        stop();
                    }
                } else {
                    if (ticks == 6) {
                        checkPrayer();
                    } else if (ticks == 7) {
                        checkPrayer();
                    } else if (ticks == 8) {
                        checkPrayer();
                        if (prayerWasActive) {
                            injure();
                        }
                    } else if (ticks == 9) {
                        if (prayerWasActive || !PrayerManagerKeys.prayerManager(target).isActive(Prayer.PROTECT_FROM_MELEE)) {
                            delayHit(SolHeredit.this, -1, target, new Hit(SolHeredit.this, 45, HitType.REGULAR));
                        }
                    } else if (ticks == 10) {
                    } else if (ticks == 11) {
                        stop();
                    }
                }
                ticks++;
            }
        }, 0, 0);
    }

    private int phaseTransition(int toPhase) {
        freeze(5);
        //TODO lock or unago sol? here?
        int playerX = target.getLocation().getX();
        int playerY = target.getLocation().getY();
        WorldTasksManager.schedule(() -> {
            tryPlacePool(playerX, playerY, true);
            int numOtherPools = toPhase == 5 ? 4 : 5;
            tryToPlacePools(playerX, playerY, numOtherPools);

            if (toPhase >= 1 && toPhase <= 4) {
                this.createLaserOrb();
            } else if (toPhase >= 5) {
                this.laserOrbCooldown = ENRAGE_LASER_ORB_COOLDOWN; // force laser
            }
        }, 0);
        return 7;
    }

    private void createLaserOrb() {
        int size = laserOrbs.size();
        if (size >= 4) {
            return;
        }

        LaserOrb laserOrb = new LaserOrb(instance, LaserOrb.SpawnData.values[size]);
        laserOrbs.add(laserOrb);
        laserOrb.spawn();
    }

    private void tryToPlacePools(int x, int y, int amount) {
        target.getPacketDispatcher().sendSoundEffect(new SoundEffect(8053));
        Location sw = instance.getArenaSw();
        Location ne = instance.getArenaNe();

        for (int i = 0; i < amount; i++) {
            int xx = Utils.clamp(x - 4 + Utils.random(9), sw.getX(), ne.getX());
            int yy = Utils.clamp(y - 4 + Utils.random(9), sw.getY(), ne.getY());
            tryPlacePool(xx, yy, false);
        }

        WorldTasksManager.schedule(() -> {
            target.getPacketDispatcher().sendSoundEffect(new SoundEffect(8093));
            for (SolPool solPool : pools.values()) {
                solPool.spawned = true;
                World.spawnObject(solPool);
            }
        }, 2);
    }

    private void tryPlacePool(int x, int y, boolean individual) {
        Location location = new Location(x, y);
        if (pools.containsKey(location)) {
            return;
        }

        SolPool solPool = new SolPool(location);
        pools.put(location, solPool);
        World.sendGraphics(BEAM_GRAPHIC, location);
        if (individual) {
            WorldTasksManager.schedule(() -> {
                if (isDead() || isFinished()) {//Sol is already dead, don't spawn the pool
                    return;
                }

                solPool.spawned = true;
                World.spawnObject(solPool);
            }, 2);
        }
    }

    private int attackSpear() {
        freeze(6);
        setAnimation(ATTACK_SPEAR_ANIMATION);
        //copy value pre set :)
        final boolean firstSpear = this.firstSpear;
        WorldTasksManager.schedule(() -> {
            if (firstSpear)
                doFirstSpear(target);
            else
                doSecondSpear(target);
        }, 2);
        this.firstSpear = !this.firstSpear;
        this.firstShield = true;
        return phaseId < 2 ? 7 : 6;
    }

    private int attackShield() {
        freeze(4);
        setAnimation(ATTACK_SHIELD_ANIMATION);
        //copy value pre set :)
        final boolean firstShield = this.firstShield;
        WorldTasksManager.schedule(() -> {
            if (firstShield)
                doFirstShield();
            else
                doSecondShield();
        }, 2);
        this.firstSpear = true;
        this.firstShield = !this.firstShield;
        return phaseId < 2 ? 6 : 5;
    }

    private void doFirstSpear(Entity target) {
        int LINE_LENGTH = 7;
        // slam under boss
        int centerX = getMiddleLocation().getX();
        int centerY = getMiddleLocation().getY();
        int delay = this.fillRectCenter(centerX, centerY, (size - 1) / 2);
        // slam line facing player
        int x = getLocation().getX();
        int y = getLocation().getY();
        Direction direction = getAttackDirection(target);
        switch (direction) {
            case WEST: {
                delay = this.fillRect(x - 1, y, x, y + getSize(), delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x - 2, y + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x - 2, y + 3, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case EAST: {
                delay = this.fillRect(x + getSize(), y, x + getSize() + 1, y + getSize(), delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + getSize() + 1, y + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() + 1, y + 3, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case NORTH: {
                delay = this.fillRect(x, y + getSize(), x + getSize(), y + getSize() + 1, delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + 1, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 3, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case SOUTH: {
                delay = this.fillRect(x, y - 1, x + getSize(), y, delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + 1, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 3, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case NORTH_EAST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + getSize() - 1, y + getSize(), direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize(), y + getSize() - 1, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case SOUTH_EAST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + getSize(), y, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() - 1, y - 1, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case SOUTH_WEST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x - 1, y, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x, y - 1, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case NORTH_WEST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x - 1, y + getSize() - 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x, y + getSize(), direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
        }
    }

    private void doSecondSpear(Entity target) {
        Direction direction = getAttackDirection(target);
        int LINE_LENGTH = 7;
        // slam under boss
        int centerX = getMiddleLocation().getX();
        int centerY = getMiddleLocation().getY();
        int radius = (size - 1) / 2;
        if (direction.isDiagonal()) {
            radius++;
        }
        int delay = this.fillRectCenter(centerX, centerY, radius);
        // slam line facing player
        int x = getLocation().getX();
        int y = getLocation().getY();
        switch (direction) {
            case WEST: {
                delay = this.fillRect(x - 1, y, x, y + getSize(), delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x - 2, y, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x - 2, y + 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x - 2, y + 4, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case EAST: {
                delay = this.fillRect(x + getSize(), y, x + getSize() + 1, y + getSize(), delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + getSize() + 1, y, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() + 1, y + 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() + 1, y + 4, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case NORTH: {
                delay = this.fillRect(x, y + getSize(), x + getSize(), y + getSize() + 1, delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 2, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 4, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case SOUTH: {
                delay = this.fillRect(x, y - 1, x + getSize(), y, delay);
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 2, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 4, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case NORTH_EAST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + getSize() + 1, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() - 2, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() + 1, y + getSize() - 2, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case SOUTH_EAST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x + getSize() + 1, y + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() + 1, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + getSize() - 2, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case SOUTH_WEST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x - 2, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x - 2, y + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 1, y - 2, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
            case NORTH_WEST: {
                int finalDelay = delay;
                BiConsumer<Location, Integer> tileConsumer = (tile, n) -> {
                    World.sendGraphics(new Graphics(Utils.random(SMOKE_SPOTANIMS), finalDelay + (n * SMOKE_DELAY_MOD), 0), tile);
                    applySmokeHit(tile);
                };
                instance.fillLine(x - 2, y + getSize() - 2, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x - 2, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                instance.fillLine(x + 1, y + getSize() + 1, direction, LINE_LENGTH, tileConsumer, null);
                break;
            }
        }
    }

    private void doFirstShield() {
        int centerX = getMiddleLocation().getX();
        int centerY = getMiddleLocation().getY();
        fillRectCenter(centerX, centerY, 10, 4);
    }

    private void doSecondShield() {
        int centerX = getMiddleLocation().getX();
        int centerY = getMiddleLocation().getY();
        fillRectCenter(centerX, centerY, 10, 5);
    }

    private int fillRectCenter(int centerX, int centerY, int radius) {
        return fillRectCenter(centerX, centerY, radius, -1);
    }

    private int fillRectCenter(int centerX, int centerY, int radius, int exceptRadius) {
        for (int r = 0; r <= radius; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dy = -r; dy <= r; dy++) {
                    int absDx = Math.abs(dx);
                    int absDy = Math.abs(dy);

                    // Only process outer edge of current radius shell
                    if (Math.max(absDx, absDy) != r) {
                        continue;
                    }

                    int x = centerX + dx;
                    int y = centerY + dy;

                    // Check if tile is outside borders
                    if (instance.outsideOfArena(x, y)) {
                        continue;
                    }

                    // Skip tiles on the "except radius" cross
                    if (((absDx == exceptRadius && absDy <= exceptRadius) ||
                            (absDy == exceptRadius && absDx <= exceptRadius))) {
                        continue;
                    }

                    Location tile = new Location(x, y);
                    World.sendGraphics(
                            new Graphics(Utils.random(SMOKE_SPOTANIMS), r * SMOKE_DELAY_MOD, 0),
                            tile
                    );
                    applySmokeHit(tile);
                }
            }
        }

        return radius * SMOKE_DELAY_MOD;
    }

    private int fillRect(int fromX, int fromY, int toX, int toY, int startDelay) {
        int midX = (toX + fromX) / 2;
        int midY = (toY + fromY) / 2;
        int maxRadius = Math.max(toX - fromX, toY - fromY) / 2;

        for (int r = 0; r <= maxRadius; r++) {
            for (int x = fromX; x < toX; x++) {
                for (int y = fromY; y < toY; y++) {
                    if (instance.outsideOfArena(x, y)) {
                        continue;
                    }

                    int radX = Math.abs(midX - x);
                    int radY = Math.abs(midY - y);
                    int ring = Math.max(radX, radY);

                    if (ring != r) {
                        continue;
                    }

                    Location tile = new Location(x, y);
                    World.sendGraphics(
                            new Graphics(Utils.random(SMOKE_SPOTANIMS), startDelay + (r * SMOKE_DELAY_MOD), 0),
                            tile
                    );
                    applySmokeHit(tile);
                }
            }
        }

        return startDelay + (maxRadius * SMOKE_DELAY_MOD);
    }

    private void applySmokeHit(Location location) {
        if (!location.matches(target.getLocation())) {
            return;
        }

        //Making 20 minimum so it's little harder, not sure on the actual damage numbers if it can hit less than 20
        delayHit(SolHeredit.this, -1, target, new Hit(this, getRandomMaxHit(this, Utils.random(20, 45), MELEE, target), HitType.REGULAR));
    }

    private Location getClosestTileTo(int x, int y) {
        int thisX = getLocation().getX();
        int thisY = getLocation().getY();
        int size = getSize() - 1;
        return new Location(Utils.clamp(x, thisX, thisX + size), Utils.clamp(y, thisY, thisY + size));
    }

    private Direction getAttackDirection(Entity entity) {
        int entityX = entity.getLocation().getX();
        int entityY = entity.getLocation().getY();
        Location closest = getClosestTileTo(entityX, entityY);
        int dx = entityX - closest.getX();
        int dy = entityY - closest.getY();
        if (dx < 0 && dy == 0) {
            return Direction.WEST;
        } else if (dx < 0 && dy < 0) {
            return Direction.SOUTH_WEST;
        } else if (dx == 0 && dy < 0) {
            return Direction.SOUTH;
        } else if (dx > 0 && dy < 0) {
            return Direction.SOUTH_EAST;
        } else if (dx > 0 && dy == 0) {
            return Direction.EAST;
        } else if (dx > 0 && dy > 0) {
            return Direction.NORTH_EAST;
        } else if (dx == 0 && dy > 0) {
            return Direction.NORTH;
        } else {
            // dx == 0 && dy == 0 or fallback
            return Direction.NORTH_WEST;
        }
    }

    public GrappleStyle getGrappleStyle() {
        return grappleStyle;
    }

    public void setClickedSlot(int clickedSlot) {
        if (this.clickedSlot != -1) {
            return; // already set, gg
        }
        this.clickedSlot = clickedSlot;
    }

    @Override
    protected void postHitProcess(Hit hit) {
        super.postHitProcess(hit);

        target.getHpHud().updateValue(getHitpoints());
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);

        long time = target.getBossTimer().getCurrentTracker();
        ColosseumStatistics.statistics.updateStatistics(time);

        instance.grantRewards();
        target.getBossTimer().finishTracking(TIMER_NAME);
        target.getHpHud().close();
        //There should be despawn animation, can't find it! TODO
        for (SolPool solPool : pools.values()) {
            if (!solPool.spawned) continue;
            World.removeObject(solPool);
        }
        for (LaserOrb laserOrb : laserOrbs) {
            laserOrb.sendDeath();
        }

        WorldTasksManager.schedule(() -> {
            say(Utils.random(DEATH_MESSAGES));
            target.sendMessage(Colour.RS_GREEN.wrap("Search the chest nearby to retrieve your earned rewards!"));
        });

        laserOrbs.clear();
        pools.clear();
    }

    @Override
    protected void drop(final Location tile) {
        // Sol Heredit does not drop items on the ground.
        // Rewards are granted via grantRewards() → reward chest.
    }

    @Override
    protected void onFinish(@Nullable Entity source) {
        super.onFinish(source);

        instance.spawnChest();
    }

    @Override
    public void performDefenceAnimation(Entity attacker) {
        // sol doesn't have a defence animation
    }

    // Does typeless melee damage, so no prayer multiplier reduction, we don't have a way to make it typeless melee yet
    @Override
    public double getMeleePrayerMultiplier() {
        return 1.0;
    }

    //Intelligent pf so it doesn't follow from sw tile and uses actual middle tile
    @Override
    public boolean isIntelligent() {
        return true;
    }

    @Override
    public boolean isAlwaysTakeMaxHit(HitType type) {
        // Parry attack logic, if successful, it will always maxhit
        if (maxHitTicks > WorldThread.getCurrentCycle()) {
            maxHitTicks = - 1;
            return true;
        }

        return false;
    }

    private enum Attack {
        SPEAR,
        SHIELD,
        TRIPLE_LONG,
        TRIPLE_SHORT,
        GRAPPLE,
        PHASE_TRANSITION
    }

    private enum Phase {
        FIRST(1500, "Let's start by testing your footwork."),
        SECOND(1350, "Not bad. Let's try something else..."),
        THIRD(1125, "Impressive. Let's see how you handle this..."),
        FOURTH(750, "You can't win!"),
        FIFTH(375, "Ralos guides my hand!"),
        SIXTH(150, "LET'S END THIS!");

        public static final Phase[] phases = values();
        private final int hp;
        private final String message;

        Phase(int hp, String message) {
            this.hp = hp;
            this.message = message;
        }

    }

    private static class SolPool extends WorldObject {

        private boolean spawned;

        public SolPool(Location location) {
            super(50746, 10, 0, location);
        }

    }

    public enum GrappleStyle {
        BODY("<col=ff0000>I'LL CRUSH YOUR </col><col=ffffff>BODY</col><col=ff0000>!</col>", EquipmentSlot.PLATE, "body"),
        CAPE("<col=ff0000>I'LL BREAK YOUR </col><col=ffffff>BACK</col><col=ff0000>!</col>", EquipmentSlot.CAPE, "back"),
        GLOVES("<col=ff0000>I'LL TWIST YOUR </col><col=ffffff>HANDS</col><col=ff0000> OFF!</col>", EquipmentSlot.HANDS, "hands"),
        LEGS("<col=ff0000>I'LL BREAK YOUR </col><col=ffffff>LEGS</col><col=ff0000>!</col>", EquipmentSlot.LEGS, "legs"),
        BOOTS("<col=ff0000>I'LL CUT YOUR </col><col=ffffff>FEET</col><col=ff0000> OFF!</col>", EquipmentSlot.BOOTS, "feet");

        private static final GrappleStyle[] values = values();

        private final String message;
        private final EquipmentSlot slot;
        private final String bodyPart;

        GrappleStyle(String message, EquipmentSlot slot, String bodyPart) {
            this.message = message;
            this.slot = slot;
            this.bodyPart = bodyPart;
        }

        public EquipmentSlot getSlot() {
            return slot;
        }

    }

}