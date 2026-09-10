package org.jesse.game.world.entity.player.action.combat;

import org.jesse.game.world.entity.player.action.combat.ISpecialAttack;
import org.jesse.game.content.boss.grotesqueguardians.boss.Dawn;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.degradableitems.DegradeType;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Entity.EntityType;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.Toxins.ToxinType;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.pathfinding.events.player.CombatEntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.PredictedEntityStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.entity.player.variables.TickVariable;
import org.jesse.game.world.region.RegionArea;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;
import org.jesse.game.world.region.area.plugins.PlayerCombatPlugin;
import mgi.types.config.items.ItemDefinitions;

import static org.jesse.game.item.ids.ItemId.EMBERLIGHT;
import static org.jesse.game.npc.ids.NpcId.*;
import static org.jesse.game.world.entity.player.action.combat.AttackStyle.AttackExperienceType.*;

/**
 * @author Kris | 5. jaan 2018 : 2:03.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class MeleeCombat extends PlayerCombat {
    private int extraSpace;

    public MeleeCombat(final Entity target) {
        super(target);
    }

    @Override
    int getAttackDistance() {
        return 0;
    }

    @Override
    int fireProjectile() {
        return 0;
    }

    @Override
    public Hit getHit(final Player player, final Entity target, final double accuracyModifier, final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        final int maxHit = getMaxHit(player, passiveModifier, activeModifier, ignorePrayers);
        final int damage = getRandomHit(player, target, maxHit, accuracyModifier);
        final Hit hit = new Hit(player, damage, HitType.MELEE);
        if (damage == maxHit) {
            hit.setMax(true);
        }
        return hit;
    }

    @Override
    public int getMaxHit(final Player player, final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        final AttackStyle.AttackExperienceType attackExperienceType = player.getCombatDefinitions().getAttackExperienceType();
        final AttackType attackType = player.getCombatDefinitions().isUsingSpecial() ? getSpecialType() : player.getCombatDefinitions().getAttackType();
        float boost = CombatUtilities.hasFullMeleeVoid(player, true) ? 1.12F : CombatUtilities.hasFullMeleeVoid(player, false) ? 1.1F : 1;
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        if (CombatUtilities.lanceEquipped(weaponId) && CombatUtilities.isDraconic(target))
            boost += 0.3F;
        if (CombatUtilities.applyForinthrySurge(player, target))
            boost += 0.15F;
        if (CombatUtilities.applyPvmArenaBoost(player, target))
            boost += 0.05F;
        boost += determineBountyHunterDmgBoost(player, target);
        if (attackType == AttackType.CRUSH)
            boost += CombatUtilities.getInquisitorSetBoost(player);
        final double a = (Math.floor(player.getSkills().getLevel(SkillConstants.STRENGTH) * player.getPrayerManager().getSkillBoost(SkillConstants.STRENGTH)) + (attackExperienceType == STRENGTH_XP ? 3 : attackExperienceType == SHARED_XP ? 1 : 0) + 8) * (boost);
        final float b = (float) player.getBonuses().getBonus(10);
        double result = Math.floor(0.5F + a * (b + 64.0F) / 640.0F);
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        final String amuletName = ItemDefinitions.nameOf(amuletId).toLowerCase();

        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            result *= 1.2F;
        } else if ((amuletName.startsWith("salve amulet")) && (CombatUtilities.SALVE_AFFECTED_NPCS.contains(name) || CombatUtilities.isUndeadCombatDummy(target))) {
            result *= (amuletId == 4081 || amuletId == 12017) ? (7.0F / 6.0F) : 1.2F;
        }

        boolean hasTask = player.getSlayer().isCurrentAssignment(target) || CombatUtilities.isCombatDummy(target);
        result *= determineSlayerHelmetDamageBoost(hasTask, HitType.MELEE, player, target);
        result = Math.floor(result);

        result = Math.floor(result * passiveModifier);

        final boolean wieldingObsidianWeapon = obsidianWeaponry.contains(weaponId);
        if (wieldingObsidianWeapon && CombatUtilities.hasFullObisidian(player)) {
            result *= 1.1F;
        }
        result = Math.floor(result);
        if (!ignorePrayers) {
            if (target instanceof Player) {
                if (((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
                    result *= target.getMeleePrayerMultiplier();
                    result = Math.floor(result);
                }
            }
        }
        //Darklight or arclight
        if (isDemonbaneWeapon(weaponId) && CombatUtilities.isDemon(target)) {
            if(target instanceof NPC npc && npc.getId() == NpcId.DUKE_SUCELLUS_12191) {
                result *= 1.5F;
            } else result *= (weaponId == ItemId.ARCLIGHT || weaponId == EMBERLIGHT) ? 1.7F : 1.6F;
        }
        //Berserker necklace
        if ((amuletId == ItemId.BERSERKER_NECKLACE || amuletId == ItemId.BERSERKER_NECKLACE_OR) && wieldingObsidianWeapon) {
            result *= 1.2F;
        } else
        //Dharok's set effect
        if (CombatUtilities.hasFullBarrowsSet(player, "Dharok's")) {
            result *= CombatUtilities.getDharokModifier(player);
        } else
        //Gadderhammer
        if (weaponId == 7668 && CombatUtilities.isShade(target)) {
            result *= Utils.random(99) < 5 ? 2.0F : 1.25F;
        }
        result *= activeModifier;
        result = Math.floor(result);

        //Castle wars bracelet effect
        if (player.getTemporaryAttributes().containsKey("castle wars bracelet effect") && player.inArea("Castle Wars")) {
            if (target instanceof Player) {
                final int targetWeapon = ((Player) target).getEquipment().getId(EquipmentSlot.WEAPON);
                if (targetWeapon == 4037 || targetWeapon == 4039) {
                    result = Math.floor(result * 1.2F);
                }
            }
        }
        if (target instanceof final Player tp) {
            if (tp.getVariables().getTime(TickVariable.POWER_OF_DEATH) > 0) {
                result *= 0.5F;
            }
        }
        return (int) Math.floor(result);
    }

    @Override
    public final int getRandomHit(final Player player, final Entity target, final int maxhit, final double modifier) {
        return getRandomHit(player, target, maxhit, modifier, player.getCombatDefinitions().getAttackType());
    }

    private AttackType getSpecialType() {
        final ISpecialAttack special = SpecialAttack.SPECIAL_ATTACKS.get(player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot()));
        if (special == null) {
            return player.getCombatDefinitions().getAttackType();
        }
        return special.getAttackType();
    }

    @Override
    public int getRandomHit(final Player player, final Entity target, final int maxhit, final double modifier, final AttackType attackType) {

        if (CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MELEE) || CombatUtilities.isWardenCore(target)) {
            return maxhit;
        }
        final int accuracy = getAccuracy(player, target, modifier);
        final int targetRoll = getTargetDefenceRoll(player, target, attackType);
        sendDebug(accuracy, targetRoll, maxhit);
        final int accRoll = accuracy > 0 ? Utils.random(accuracy) : 0;
        final int defRoll = targetRoll > 0 ? Utils.random(targetRoll) : 0;
        if (accRoll <= defRoll) {
            return 0;
        }
        int playerMinimum = calculateMinimumHit(player, maxhit);
        return Utils.random(playerMinimum, maxhit);
    }


    @Override
    public int getAccuracy(final Player player, final Entity target, final double resultModifier) {
        final AttackStyle.AttackExperienceType type = player.getCombatDefinitions().getAttackExperienceType();
        final AttackType attackType = player.getCombatDefinitions().isUsingSpecial() ? getSpecialType() : player.getCombatDefinitions().getAttackType();
        float boost = CombatUtilities.hasFullMeleeVoid(player, true) ? 1.125F : CombatUtilities.hasFullMeleeVoid(player, false) ? 1.1F : 1.0F;
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        if (CombatUtilities.lanceEquipped(weaponId) && CombatUtilities.isDraconic(target))
            boost += 0.3F;
        if (CombatUtilities.dragonSlayerGlovesEquipped(player) && CombatUtilities.isDraconic(target))
            boost += 0.15F;
        if (attackType == AttackType.CRUSH)
            boost += CombatUtilities.getInquisitorSetBoost(player);
        if (CombatUtilities.applyForinthrySurge(player, target))
            boost += 0.15F;
        if (CombatUtilities.applyPvmArenaBoost(player, target))
            boost += 0.05F;
        boost += determineBountyHunterAccBoost(player, target);
        final double a = Math.floor(Math.floor(player.getSkills().getLevel(SkillConstants.ATTACK) * player.getPrayerManager().getSkillBoost(SkillConstants.ATTACK)) + (type == ATTACK_XP ? 3 : type == SHARED_XP ? 1 : 0) + 8.0F) * (boost);
        final int b = player.getBonuses().getBonus(attackType.ordinal());
        double result = a * (b + 64.0F);
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            result *= 1.2F;
        } else if ((amuletId == 4081 || amuletId == 12017 || amuletId == 10588 || amuletId == 12018) && CombatUtilities.SALVE_AFFECTED_NPCS.contains(name)) {
            result *= (amuletId == 4081 || amuletId == 12017) ? (7.0F / 6.0F) : 1.2F;
        }

        boolean hasTask = player.getSlayer().isCurrentAssignment(target) || CombatUtilities.isUndeadCombatDummy(target);
        result *= determineSlayerHelmetAccuracyBoost(hasTask, HitType.MELEE, player, target);
        result = Math.floor(result);

        result *= resultModifier;
        if (isDemonbaneWeapon(weaponId) && CombatUtilities.isDemon(target)) {
            if(target instanceof NPC npc && npc.getId() == NpcId.DUKE_SUCELLUS_12191) {
                result *= 1.5F;
            } else result *= (weaponId == ItemId.ARCLIGHT || weaponId == EMBERLIGHT) ? 1.7F : 1.6F;
        }
        if (obsidianWeaponry.contains(weaponId) && CombatUtilities.hasFullObisidian(player)) {
            result *= 1.1F;
        }
        return (int) result;
    }



    @Override
    public boolean process() {
        return initiateCombat(player);
    }

    @Override
    public int processWithDelay() {
        return 0;
    }

    @Override
    public int processAfterMovement() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!canAttack()) {
            return -1;
        }
        final RegionArea area = player.getArea();
        if (area instanceof PlayerCombatPlugin) {
            ((PlayerCombatPlugin) area).onAttack(player, target, "Melee", null, false);
        }
        addAttackedByDelay(player, target);
        final int delay = special();
        if (delay != -2) {
            return delay == SpecialAttackScript.WEAPON_SPEED ? getSpeed() : delay;
        }
        sendSoundEffect();
        final Hit hit = getHit(player, target, 1, 1, 1, false);
        extra(hit);
        addPoisonTask(hit.getDamage(), 0);
        delayHit(0, hit);
        if (hit.getDamage() > 0 && player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.ARCLIGHT) {
            player.getChargesManager().removeCharges(player.getWeapon(), 1, player.getEquipment().getContainer(), EquipmentSlot.WEAPON.getSlot());
        }
        animate();
        if (player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.CORRUPTED_VOLATILE_NIGHTMARE_STAFF) {
            player.getChargesManager().removeCharges(DegradeType.SPELL);
        }
        player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
        resetFlag();
        checkIfShouldTerminate(HitType.MELEE);
        if (player.getTemporaryAttributes().containsKey("combat debug")) {
            player.sendMessage("Weapon Speed: " + getSpeed());
        }
        return getSpeed();
    }

    protected void extra(final Hit hit) {
        bloodFury(HitType.MELEE, hit);
    }

    protected int special() {
        if (!player.getCombatDefinitions().isUsingSpecial()){
            return -2;
        }
        return useSpecial(player, SpecialType.MELEE);
    }

    @Override
    public boolean start() {
        extraSpace = isExtendedMeleeDistance(player);
        player.setCombatEvent(new CombatEntityEvent(player, new PredictedEntityStrategy(target)));
        player.setLastTarget(target);
        if (target.getEntityType() == EntityType.NPC) {
            final NPC npc = (NPC) target;
            final int id = npc.getId();
            if (id == KREEARRA_12443 || id >= KREEARRA && id <= AVIANSIE_3183 || id == REANIMATED_AVIANSIE || id == ARMADYLEAN_GUARD || npc instanceof Dawn) {
                player.sendMessage("You cannot use melee against this creature.");
                return false;
            }
        }
        if (player.isFrozen()) {
            player.sendMessage("A magical force stops you from moving.");
        }
        player.setFaceEntity(target);
        if (initiateCombat(player)) {
            return true;
        }
        player.setFaceEntity(null);
        return false;
    }

    protected void addPoisonTask(final int damage, final int delay) {
        if (damage <= 0) return;

        final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON);
        if (weapon == null) return;

        switch (weapon.getId()) {
            case ItemId.ABYSSAL_TENTACLE, 26484 -> {
                if (Utils.random(3) == 0)
                    WorldTasksManager.scheduleOrExecute(() -> target.getToxins().applyToxin(ToxinType.POISON, 4, player), delay);
                return;
            }
        }

        final String name = weapon.getName();
        if (!name.contains("(p"))
            return;

        WorldTasksManager.scheduleOrExecute(() -> target.getToxins().applyToxin(ToxinType.POISON, name.contains("p++") ? 6 : name.contains("p+") ? 5 : 4, player), delay);
    }

    protected void animate() {
        final int id = player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle());
        final Animation animation = new Animation(getAttackAnimation(target instanceof Player, id == 393 ? (player.getShield() == null ? 414 : id) : id));
        player.setAnimation(animation);
    }

    protected boolean canAttack() {
        if (!attackable()) return false;
        if (!target.canAttack(player)) {
            return false;
        }
        final RegionArea area = player.getArea();
        if ((area instanceof EntityAttackPlugin && !((EntityAttackPlugin) area).attack(player, target, this))) {
            return false;
        }
        return (!(area instanceof PlayerCombatPlugin) || ((PlayerCombatPlugin) area).processCombat(player, target, "Melee")) && player.getControllerManager().processPlayerCombat(target, "Melee");
    }

    public static int PLAYER_DEFAULT_ATTACK_SPEED = 4;
    public static int MIN_ATTACK_SPEED = 1;

    public int getSpeed() {
        int speed = PLAYER_DEFAULT_ATTACK_SPEED;
        final Item weapon = player.getWeapon();
        if (weapon != null) {
            final ItemDefinitions def = weapon.getDefinitions();
            speed = Math.max(MIN_ATTACK_SPEED, def.getAttackSpeed());
        }

        return adjustAttackSpeed(player, speed);
    }

    protected boolean initiateCombat(final Player player) {
        if (player.isDead() || player.isFinished() || player.isLocked() || player.isStunned() || player.isFullMovementLocked()) {
            return false;
        }
        if (target.isFinished() || target.isCantInteract() || target.isDead()) {
            return false;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        final int viewDistance = player.getViewDistance();
        if (player.getPlane() != target.getPlane() || distanceX > size + viewDistance || distanceX < -1 - viewDistance || distanceY > size + viewDistance || distanceY < -1 - viewDistance) {
            return false;
        }
        if (target.getEntityType() == EntityType.PLAYER) {
            if (!player.isCanPvp() || !((Player) target).isCanPvp()) {
                player.sendMessage("You can't attack someone in a safe zone.");
                return false;
            }
        }
        if (player.isFrozen() || player.isMovementLocked(false)) {
            return true;
        }
        if (!canInitiate()) {
            return false;
        }
        if (!target.hasWalkSteps() && CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), target.getX(), target.getY(), target.getSize())) {
            player.getCombatEvent().process();
            return true;
        }
        if (handleDragonfireShields(player, false)) {
            if (!canAttack()) {
                return false;
            }
            handleDragonfireShields(player, true);
            player.getActionManager().addActionDelay(4);
            return true;
        }
        if(canAttack() && handleDragonfireShieldsRNG(player)) {
            player.getActionManager().addActionDelay(1);
            return true;
        }
        player.resetWalkSteps();
        final Location nextLocation = target.getLocation();
        if (player.isProjectileClipped(target, extraSpace <= 0) || !(withinRange(target, extraSpace, target.getSize())) || target.hasWalkSteps() && (target instanceof Player || !CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()))) {
            appendWalksteps();
        }
        if (!player.hasWalkSteps() && !isWithinAttackDistance()) {
            player.sendMessage("I can't reach that!");
            return false;
        }
        return true;
    }

    protected boolean canInitiate() {
        return true;
    }

    protected boolean isWithinAttackDistance() {
        if(target instanceof NPC npc && hasManualDistanceDefined(npc))
            return checkManualDistance(npc);
        if (target.checkProjectileClip(player, true) && isProjectileClipped(true, extraSpace <= 0)) {
            return false;
        }
        final Location nextTile = target.getNextLocation();
        final Location tile = nextTile != null ? nextTile : target.getLocation();
        final int distanceX = player.getX() - tile.getX();
        final int distanceY = player.getY() - tile.getY();
        final int size = target.getSize();
        int maxDistance = extraSpace;
        final Location nextLocation = target.getLocation();
        if ((player.isFrozen() || player.isStunned()) && (CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()) || !withinRange(target, maxDistance, target.getSize()))) {
            return false;
        }
        return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
    }

    private boolean checkManualDistance(NPC target) {
        final Location nextTile = target.getNextLocation();
        final Location tile = nextTile != null ? nextTile : target.getLocation();
        final int distanceX = player.getX() - tile.getX();
        final int distanceY = player.getY() - tile.getY();
        final int size = target.getSize();
        int maxDistance = 3;
        if ((player.isFrozen() || player.isStunned()) && (!withinRange(target, maxDistance, target.getSize()))) {
            return false;
        }
        return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
    }

    private boolean hasManualDistanceDefined(NPC target) {
        if(target.getId() == 12191 || target.getId() == 12195) //Duke
            return true;
        return false;
    }

    protected void resetFlag() {
        if (!minimapFlag) {
            return;
        }
        player.getPacketDispatcher().resetMapFlag();
        minimapFlag = false;
    }

    protected void sendSoundEffect() {
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        final CombatSoundEffect sound = CombatSoundEffect.getSound(weaponId);
        if (sound == null) {
            final SoundEffect fallbackSound = CombatSoundEffect.getDefaultSoundEffect(weaponId);
            if (fallbackSound == null) {
                return;
            }
            World.sendSoundEffect(new Location(player.getLocation()), fallbackSound);
            return;
        }
        World.sendSoundEffect(new Location(player.getLocation()), sound.getSound());
    }
}
