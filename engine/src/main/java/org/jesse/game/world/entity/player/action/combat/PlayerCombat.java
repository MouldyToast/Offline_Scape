package org.jesse.game.world.entity.player.action.combat;

import com.google.common.collect.ImmutableList;
import org.jesse.game.world.entity.player.action.combat.ranged.MorriganBHWeaponsCombat;
import org.jesse.game.content.crystal.recipes.chargeable.CrystalTool;
import org.jesse.game.world.entity.CombatCooldownKt;
import org.jesse.game.world.entity.player.action.combat.ISpecialAttack;
import org.jesse.game.GameConstants;
import org.jesse.game.content.chambersofxeric.npc.IceDemon;
import org.jesse.game.content.skills.hunter.npc.ImplingNPC;
import org.jesse.game.content.skills.magic.spells.MagicSpell;
import org.jesse.game.content.skills.magic.spells.arceuus.GreaterCorruptionKt;
import org.jesse.game.content.tombsofamascut.AbstractTOARaidArea;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.util.Colour;
import org.jesse.game.util.ProjectileUtils;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Position;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Entity.EntityType;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.npc.combatdefs.StatType;
import org.jesse.game.world.entity.player.*;
import org.jesse.game.world.entity.player.action.combat.AttackStyle.AttackExperienceType;
import org.jesse.game.world.entity.player.action.combat.magic.*;
import org.jesse.game.world.entity.player.action.combat.melee.*;
import org.jesse.game.world.entity.player.action.combat.ranged.*;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.entity.player.variables.TickVariable;
import org.jesse.game.world.region.CharacterLoop;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.RegionArea;
import org.jesse.game.world.region.area.apeatoll.Greegree;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;
import org.jesse.game.world.region.area.plugins.HitProcessPlugin;
import org.jesse.game.world.region.area.wilderness.WildernessArea;
import org.jesse.game.world.region.areatype.AreaType;
import org.jesse.game.world.region.areatype.AreaTypes;
import org.jesse.plugins.dialogue.PlainChat;
import org.jesse.plugins.item.DragonfireShield;
import org.jesse.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.types.config.enums.EnumDefinitions;
import net.runelite.api.ItemID;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.Consumer;

public abstract class PlayerCombat extends Action {
    public int lastProjectileIndex = 0;

    public interface IPlayerCombatAttackHook {
        enum Result {
            Return,
            Pass
        }

        Result onPlayerAttack(final Player player, final Entity entity, final MagicSpell spell);
    }

    private static final List<IPlayerCombatAttackHook> attackHooks = new ArrayList<>(0);

    public static void registerAttackHook(IPlayerCombatAttackHook hook) {
        attackHooks.add(hook);
    }

    public static void unregisterAttackHook(IPlayerCombatAttackHook hook) {
        attackHooks.remove(hook);
    }

    public static final Set<Integer> obsidianWeaponry = Set.of(6527, 6528, 6523, 6525, 6526, 20554, ItemId.TZHAARKETOM_T);

    public static final List<Integer> INSTANT_SPEC_WEAPONS = ImmutableList.of(
            4153, 12848, 24227, 24225, 20557, 1377, 11920, 12797,
            13242, 13243, 13244, 20014, 6739, 13241, 20011, 21028,
            21031, 21033, 20849, 21207, 35, ItemId.STAFF_OF_THE_DEAD, ItemId.TOXIC_STAFF_OF_THE_DEAD, 11037, 22296,
            ItemId.STAFF_OF_BALANCE,
            ItemId.DRAGON_BATTLEAXE_CR, ItemId.NOXIOUS_HALBERD,
            CrystalTool.Harpoon.INSTANCE.getProductItemId(),
            CrystalTool.Axe.INSTANCE.getProductItemId(),
            CrystalTool.Pickaxe.INSTANCE.getProductItemId(),
            32161,
            ItemID.KERIS_PARTISAN_OF_THE_SUN
    );
    /**
     * Animations which stall when in PvP combat, but do not stall when in PvM combat.
     */
    public static final Int2IntMap nonstallingToStallingAnimations = Int2IntMaps.unmodifiable(new Int2IntOpenHashMap() {
        {
            put(5248, 5245);
            put(387, 3044);
            put(4410, 8147);
            put(7554, 6600);
            put(7552, 4230);
            put(7555, 7218);
            put(7617, 929);
            put(7618, 2779);
            put(7558, 2614);
            put(7557, 4199);
            put(7556, 7222);
            put(9168, 9166);
        }
    });
    public static final Int2IntMap stallingToNonStallingAnimations = Int2IntMaps.unmodifiable(new Int2IntOpenHashMap() {
        {
            for (final Int2IntMap.Entry entry : nonstallingToStallingAnimations.int2IntEntrySet()) {
                put(entry.getIntValue(), entry.getIntKey());
            }
        }
    });

    public static int getAttackAnimation(final boolean pvp, final int animationId) {
        if (pvp) {
            return nonstallingToStallingAnimations.getOrDefault(animationId, animationId);
        }
        return stallingToNonStallingAnimations.getOrDefault(animationId, animationId);
    }

    public static final Projectile ZARYTE_SPEC_PROJ = new Projectile(1995, 38, 36, 41, 7, 5, 11, 5);
    public static final Graphics BLOOD_FURY_GFX = new Graphics(1542);
    private static final String[] RANGED_WEAPONS = new String[]{"bow", "javelin", "thrownaxe", "throwing axe", "knife", "knives"
            , "chinchompa", "toktz-xil-ul", "holy water", "dart", "ballista", "blowpipe", "seercull", "mud pie"};
    private static final IntSet nonRangedWeapons = new IntOpenHashSet(new int[]{ItemId.KITCHEN_KNIFE});
    private static final Animation DRAGONFIRE_SPECIAL_ANIM = new Animation(6696);
    private static final Graphics DRAGONFIRE_START_GFX = new Graphics(1165);
    private static final Graphics DRAGONFIRE_HIT_GFX = new Graphics(1167, 0, 96);
    private static final Projectile DRAGONFIRE_PROJ = new Projectile(1166, 25, 25, 80, 15, 10, 0, 5);
    private static final Graphics WYVERN_DRAGONFIRE_START_GFX = new Graphics(1401);
    private static final Projectile WYVERN_DRAGONFIRE_PROJ = new Projectile(500, 25, 25, 80, 15, 10, 0, 5);
    private static final Graphics WYVERN_DRAGONFIRE_HIT_GFX = new Graphics(367, 0, 96);
    private static final EnumDefinitions SPECIAL_ENUM = EnumDefinitions.get(906);
    protected final String name;
    protected Entity target;
    protected float accuracyModifier = 1;
    protected float maxhitModifier = 1;
    protected boolean minimapFlag;

    public PlayerCombat(final Entity target) {
        this.target = target;
        name = target.getEntityType() == EntityType.NPC ? ((NPC) target).getDefinitions().getName().toLowerCase() :
                null;
    }

    public static boolean slayerHelmIOrStatue(Player player) {
        final Item helm = player.getEquipment().getItem(EquipmentSlot.HELMET);
        if (helm == null) {
            return false;
        }

        final String name = helm.getName().toLowerCase();
        return (name.contains("black mask") || name.contains("slayer helm")) && name.endsWith("(i)");
    }

    public static boolean slayerHelmOrStatue(Player player) {
        final Item helm = player.getEquipment().getItem(EquipmentSlot.HELMET);
        if (helm == null) {
            return false;
        }

        final String name = helm.getName().toLowerCase();
        return name.contains("black mask") || name.contains("slayer helm");
    }

    public static int adjustAttackSpeed(Player player, int speed) {
        if (player.getBooleanTemporaryAttribute("nightmare_drowsy")) {
            speed++;
            if (player.getBooleanTemporaryAttribute("nightmare_drowsy_phosanis")) {
                speed++;
            }
        }

        return speed;
    }

    @Override
    protected void onInterruption() {
    }

    private static boolean isRangedWeapon(final int id, final String name) {
        if (nonRangedWeapons.contains(id)) {
            return false;
        }
        for (final String s : RANGED_WEAPONS) {
            if (name.contains(s)) {
                return true;
            }
        }
        return false;
    }

    protected final void sendDebug(final int accuracy, final int targetRoll, final int maximumHit) {
        if (player.getTemporaryAttributes().containsKey("combat debug")) {
            player.sendMessage("Accuracy: " + accuracy + ", Target Defence: " + targetRoll + ", Maxhit: " + maximumHit);
        }
    }

    public static boolean isKerisWeapon(final int weaponId) {
        return switch (weaponId) {
            case ItemId.KERIS, ItemId.KERISP, ItemId.KERISP_10583, ItemId.KERISP_10584, ItemId.KERIS_PARTISAN, ItemId.KERIS_PARTISAN_OF_BREACHING, ItemId.KERIS_PARTISAN_OF_CORRUPTION, ItemId.KERIS_PARTISAN_OF_THE_SUN -> true;
            default -> false;
        };
    }

    public static boolean isKerisDagger(final int weaponId) {
        return switch (weaponId) {
            case ItemId.KERIS, ItemId.KERISP, ItemId.KERISP_10583, ItemId.KERISP_10584 -> true;
            default -> false;
        };
    }

    public static boolean isOsmumtenFang(final int weaponId) {
        return switch (weaponId) {
            case ItemId.OSMUMTENS_FANG, ItemId.OSMUMTENS_FANG_OR -> true;
            default -> false;
        };
    }

    public static void appendDragonfireShieldCharges(final Player player) {
        final int id = player.getEquipment().getId(EquipmentSlot.SHIELD);
        switch (id) {
            case ItemId.DRAGONFIRE_SHIELD, ItemId.DRAGONFIRE_SHIELD_11284, ItemId.DRAGONFIRE_WARD, ItemId.DRAGONFIRE_WARD_22003 -> {
                final int charges = player.getShield().getCharges();
                if (charges < 50) {
                    player.setAnimation(new Animation(6695));
                    player.setGraphics(new Graphics(1164));
                    final Item shield = player.getShield();
                    shield.setCharges(charges + 1);
                    if (id == 11284) {
                        shield.setId(11283);
                    } else if (id == 22003) {
                        shield.setId(22002);
                    }
                    player.getEquipment().refresh(EquipmentSlot.SHIELD.getSlot());
                }
            }
        }
    }

    public static void performInstantSpecial(final Player player, final int weaponId) {
        final SpecialAttack special = (SpecialAttack) SpecialAttack.SPECIAL_ATTACKS.get(weaponId);
        if (special == null) {
            player.getCombatDefinitions().setSpecial(false, true);
            return;
        }

        switch (special) {
            case QUICK_SMASH:
                final boolean usingSpecial = player.getCombatDefinitions().isUsingSpecial();
                if (usingSpecial && player.getLastOutgoingAttackCycle() < WorldThread.getCurrentCycle() - 8) {
                    player.sendMessage("Warning: Since the maul's special attack is an instant attack, it will be " +
                            "wasted when used on a first strike.");
                }
                final Object attribute = player.getTemporaryAttributes().get(PlayerCombat.QUEUED_SPECS_ATTRIBUTE);
                /* Special count is incremented even when the player turns it off. */
                final int cachedMaulSpecials = (attribute instanceof Integer ? (Integer) attribute : 0) + 1;
                player.addTemporaryAttribute(PlayerCombat.QUEUED_SPECS_ATTRIBUTE, cachedMaulSpecials);
                return;
            case MOMENTUM_THROW:
                if (player.getCombatDefinitions().getSpecialEnergy() < 25) {
                    player.sendMessage("You don't have enough special energy.");
                    player.getCombatDefinitions().setSpecial(false, true);
                    return;
                }
                if (player.getCombatDefinitions().isUsingSpecial()) {
                    player.getTemporaryAttributes().put("dragonThrownaxe", player.getActionManager().getActionDelay());
                    if (player.getActionManager().getActionDelay() > 0) {
                        player.getActionManager().setActionDelay(0);
                    }
                } else {
                    final Object delay = player.getTemporaryAttributes().get("dragonThrownaxe");
                    if (delay instanceof Integer) {
                        final int del = (int) delay;
                        if (del > player.getActionManager().getActionDelay()) {
                            player.getActionManager().setActionDelay(del);
                        }
                    }
                }
                return;
            case LIQUEFY:
                if (!player.getInterfaceHandler().isVisible(169)) {
                    player.sendMessage("You need to be underwater to use the special attack.");
                    player.getCombatDefinitions().setSpecial(false, true);
                    return;
                }
                return;
            case TUMEKENS_LIGHT:
                if (false) {//TODO add ToA check here.
                    player.sendMessage("You need to be inside the Tomb of Amascut to use the special attack.");
                    player.getCombatDefinitions().setSpecial(false, true);
                    return;
                }
                if (player.getPrayerManager().getPrayerPoints() < 50) {
                    player.sendMessage("You need to have at least 50 prayer points to use the special attack.");
                    player.getCombatDefinitions().setSpecial(false, true);
                    return;
                }
            default:
                useInstantSpecial(player, special);
        }
    }

    public static void magicAttack(final Player player, final Entity entity, final CombatSpell spell,
                                   final boolean autocast) {
        final boolean isFreezingImpling =
                (spell == CombatSpell.BIND || spell == CombatSpell.SNARE || spell == CombatSpell.ENTANGLE || spell == CombatSpell.DARK_LURE) && entity instanceof ImplingNPC;
        if (!isFreezingImpling && !entity.canAttack(player)) {
            return;
        }
        final MagicCombat.CastType castType = autocast ? MagicCombat.CastType.AUTO_CAST :
                MagicCombat.CastType.MANUAL_CAST;
        if (spell == CombatSpell.TRIDENT_OF_THE_SEAS) {
            player.getActionManager().setAction(new SeasTridentCombat(entity, spell, castType));
            return;
        } else if (spell == CombatSpell.TRIDENT_OF_THE_SWAMP) {
            player.getActionManager().setAction(new SwampTridentCombat(entity, spell, castType));
            return;
        } else if (spell == CombatSpell.WARPED_SCEPTRE) {
            player.getActionManager().setAction(new WarpedSceptreCombat(entity, spell, castType));
            return;
        }else if (spell == CombatSpell.SANGUINESTI_STAFF) {
            player.getActionManager().setAction(new SanguinestiStaffCombat(entity, spell, castType));
            return;
        } else if (spell == CombatSpell.TUMEKENS_SHADOW) {
            player.getActionManager().setAction(new TumekensShadowCombat(entity, spell, castType));
            return;
        } else if (spell == CombatSpell.DAWNBRINGER) {
            player.getActionManager().setAction(new DawnbringerCombat(entity, spell, castType));
            return;
        } else if (spell == CombatSpell.CRYSTAL_STAFF || spell == CombatSpell.CORRUPTED_STAFF) {
            player.getActionManager().setAction(new GauntletStaffSpell(entity, spell, castType));
            return;
        }

        if (player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.HARMONISED_NIGHTMARE_STAFF) {
            player.getActionManager().setAction(new HarmonisedStaffCombat(entity, spell, castType));
            return;
        }
        if (player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.ZURIELS_STAFF) {
            player.getActionManager().setAction(new ZurielStaffCombat(entity, spell, castType));
            return;
        }

        player.getActionManager().setAction(new MagicCombat(entity, spell, castType));
    }

    public static int isExtendedMeleeDistance(final Player player) {
        final Item weapon = player.getWeapon();
        if (weapon == null) {
            return 0;
        }

        String weaponName = weapon.getName().toLowerCase();
        if (weaponName.contains("halberd")) {
            return 1;
        }

        if (weaponName.contains("nightmare staff") && player.getCombatDefinitions().isUsingSpecial()) {
            return 10;
        }

        return 0;
    }

    public int getTileDistance(final boolean predicting) {
        final Location fromTile = player.getLocation();
        final int distanceX = fromTile.getX() - target.getX();
        final int distanceY = fromTile.getY() - target.getY();
        final int size = target.getSize();
        final int distX = distanceX > size ? (distanceX - size) : distanceX < -1 ? (distanceX + 1) : 0;
        final int distY = distanceY > size ? (distanceY - size) : distanceY < -1 ? (distanceY + 1) : 0;
        return Math.max(Math.abs(distX), Math.abs(distY));
        //final int deltaX = other.getX() - from.getX(), deltaY = other.getY() - from.getY();
        //return Math.max(Math.abs(deltaX), Math.abs(deltaY));
    }

    abstract int getAttackDistance();

    protected final boolean colliding() {
        if (target.hasWalkSteps()) return false;
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        return distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1;
    }

    protected boolean isProjectileClipped(final boolean checkFurther, final boolean checkMelee) {
        return ProjectileUtils.isProjectileClipped(player, target, player.getLocation(), target.getLocation(),
                checkMelee, target.ignoreUnderneathProjectileCheck());
    }

    protected boolean pathfind() {
        final int maxDistance = getAttackDistance();

        // NOTE: this check needs to happen BEFORE resetting player's walk steps
        // otherwise, it won't even consider our proper position since it keeps resetting
        // (and thus, peeked positions are null) - Jire
        final boolean appendWalkSteps =
                (target.checkProjectileClip(player, false)
                        && isProjectileClipped(false, false))
                        || !withinRange(target, maxDistance, target.getSize());

        player.resetWalkSteps();
        if (appendWalkSteps) appendWalksteps();

        return true;
    }

    protected void checkIfShouldTerminate(HitType hitType) {
        if (CombatUtilities.isAlwaysTakeMaxHit(target, hitType)) {
            Analytics.flagInteraction(player, Analytics.InteractionType.COMBAT_DUMMY);
            WorldTasksManager.schedule(() -> {
                if (player.getActionManager().getAction() == this) {
                    player.getActionManager().forceStop();
                }
            });
        }
    }

    protected final void appendWalksteps() {
        minimapFlag = true;
        player.getCombatEvent().process();
    }

    protected boolean isWithinAttackDistance() {
        final boolean checkProjectile = target.checkProjectileClip(player, false);
        if (checkProjectile && ProjectileUtils.isProjectileClipped(player, target, player.getLocation(), target,
                false)) {
            return false;
        }
        int maxDistance = getAttackDistance();
        final Location nextLocation = target.getLocation();
        if ((player.isFrozen() || player.isStunned()) && (CollisionUtil.collides(player.getX(), player.getY(),
                player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()) || !withinRange(target,
                maxDistance, target.getSize()))) {
            return false;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
    }

    protected void extra(Hit hit) {
    }

    protected void resetFlag() {
        if (!minimapFlag) {
            return;
        }
        player.getPacketDispatcher().resetMapFlag();
        minimapFlag = false;
    }

    abstract int fireProjectile();

    protected void notifyIfFrozen() {
        if (!player.isFrozen()) {
            return;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        final int maxDistance = getAttackDistance();
        final boolean projectileClipped = player.isProjectileClipped(target, false);
        if (projectileClipped || distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
            player.sendMessage("A magical force stops you from moving.");
        }
    }

    public static void attackEntity(final Player player, final Entity entity, final MagicSpell spell) {
        final Item weapon = player.getWeapon();
        if (!player.getControllerManager().canAttack(entity)) {
            return;
        }
        if (player.getArea() instanceof EntityAttackPlugin attackPlugin) {
            if (!attackPlugin.startAttack(player, entity)) {
                return;
            }
        }
        if (player.isLocked()) {
            return;
        }
        final int weaponId = weapon != null ? weapon.getId() : -1;
        player.faceEntity(entity);
        if (weapon != null) {
            final var area = GlobalAreaManager.getArea(player);
            if (area != null) {
                // make sure we're not in the gauntlet mini-game
                if (!Objects.equals(area.name(), "The Gauntlet")
                        && weapon.getName().startsWith("Corrupted ")
                        || weapon.getName().contains("(bh)")
                        || weapon.getName().contains("(cr)")
                ) {
                    var level = WildernessArea.getWildernessLevel(player.getLocation());
                    // TODO: Check if the target is a player, then allow
                    //       This is also for things like Dueling, or non Wilderness PvP
                    if (level.isEmpty() || level.getAsInt() <= 0) {
                        player.sendMessage("You cannot use this item outside of the wilderness.");
                        return;
                    }
                }
            }
            if (weaponId == 21015) {
                if (spell != null) {
                    player.sendMessage("Your bulwark gets in the way.");
                    return;
                }
                if (player.getCombatDefinitions().getStyle() != 0) {
                    player.sendMessage("You cannot attack with the Dinh's bulwark using block!");
                    return;
                }
            }
            if (Greegree.MAPPED_VALUES.get(weaponId) != null) {
                player.getDialogueManager().start(new PlainChat(player, "You cannot attack as a monkey."));
                return;
            }
        }
        if (entity instanceof NPC npc) {
            if (npc.getCombatDefinitions().getSlayerLevel() > Math.max(1, player.getSkills().getLevel(SkillConstants.SLAYER))) {
                player.sendMessage("Your Slayer level is not high enough to harm this monster.");
                return;
            }
        }
        if (spell != null) {
            if (!(spell instanceof CombatSpell combatSpell))
                return;
            magicAttack(player, entity, combatSpell, false);
            return;
        }

        for (final var hook : attackHooks) {
            final var result = hook.onPlayerAttack(player, entity, spell);
            if (IPlayerCombatAttackHook.Result.Return.equals(result)) {
                return;
            }
        }

        if (weapon == null || weaponId == 6818 || weaponId == ItemId.HUNTING_KNIFE) {
            player.getActionManager().setAction(new MeleeCombat(entity));
            return;
        } else if (player.getCombatDefinitions().getAutocastSpell() != null) {
            magicAttack(player, entity, player.getCombatDefinitions().getAutocastSpell(), true);
            return;
        }
        if (weaponId == ItemId.DAWNBRINGER) {
            magicAttack(player, entity, CombatSpell.DAWNBRINGER, true);
            return;
        }
        if (weaponId == ItemId.SANGUINESTI_STAFF || weaponId == ItemId.HOLY_SANGUINESTI_STAFF) {
            magicAttack(player, entity, CombatSpell.SANGUINESTI_STAFF, true);
            return;
        } else if (weaponId == ItemId.CORRUPTED_TUMEKENS_SHADOW || weaponId == ItemId.TUMEKENS_SHADOW || weaponId == ItemId.TUMEKENS_SHADOW_UNCHARGED) {
            magicAttack(player, entity, CombatSpell.TUMEKENS_SHADOW, true);
            return;
        } else if (weaponId == 11905 || weaponId == 11907 || weaponId == ItemId.TRIDENT_OF_THE_SEAS_E) {
            magicAttack(player, entity, CombatSpell.TRIDENT_OF_THE_SEAS, true);
            return;
        } else if (weaponId == 23898 || weaponId == 23899 || weaponId == 23900) {
            magicAttack(player, entity, CombatSpell.CRYSTAL_STAFF, true);
            return;
        } else if (weaponId == 23852 || weaponId == 23853 || weaponId == 23854) {
            magicAttack(player, entity, CombatSpell.CORRUPTED_STAFF, true);
            return;
        } else if (weaponId == 11908 || weaponId == 22290) {
            player.sendMessage("The weapon has no charges left. You need death runes, chaos runes, fire runes and " +
                    "coins to charge it.");
            return;
        } else if (weaponId == ItemId.TRIDENT_OF_THE_SWAMP || weaponId == ItemId.TRIDENT_OF_THE_SWAMP_E) {
            magicAttack(player, entity, CombatSpell.TRIDENT_OF_THE_SWAMP, true);
            return;
        } else if (weaponId == ItemId.WARPED_SCEPTRE) {
            magicAttack(player, entity, CombatSpell.WARPED_SCEPTRE, true);
            return;
        } else if (weaponId == ItemId.THAMMARONS_SCEPTRE || weaponId == ItemId.THAMMARONS_SCEPTRE_U) {
            magicAttack(player, entity, CombatSpell.THAMMARONS_SCEPTRE, true);
            return;
        } else if (weaponId == 27662 || weaponId == 27665) {
            magicAttack(player, entity, CombatSpell.ACCURSED_SCEPTRE, true);
            return;
        }

        final String weaponName = weapon.getName().toLowerCase();
        if (ArrayUtils.contains(SpecialAttack.SHOVE.getWeapons(), weaponId)) {
            player.getActionManager().setAction(new DragonSpearCombat(entity));
        } else if (weaponName.endsWith("ballista")) {
            player.getActionManager().setAction(new JavelinRangedCombat(entity));
        } else if (weaponName.startsWith("dragon hunter crossbow")) {
            player.getActionManager().setAction(new RangedCombat(entity));
        } else if (weaponName.equals("dragon thrownaxe")) {
            player.getActionManager().setAction(new DragonThrownaxeCombat(entity));
        } else if (weaponName.contains("karil")) {
            player.getActionManager().setAction(new KarilsRangedCombat(entity));
        } else if (weaponName.contains("venator")) {
            player.getActionManager().setAction(new VenatorBowCombat(entity));
        } else if (weaponId == ItemId.MORRIGANS_THROWING_AXE_BH || weaponId == ItemId.MORRIGANS_JAVELIN_BH) {
            player.getActionManager().setAction(new MorriganBHWeaponsCombat(entity));
        } else if (weaponName.equals("corrupted dark bow")) {
            player.getActionManager().setAction(new DarkBowRangedCombat(entity));
        } else if (weaponName.contains("craw's bow") || weaponName.contains("webweaver bow")) {
            player.getActionManager().setAction(new CrawsBowCombat(entity));
        } else if ((weaponName.contains("salamander") || weaponName.equals("swamp lizard"))) {
            player.getActionManager().setAction(new SalamanderCombat(entity));
        } else if (weaponName.equals("toxic blowpipe")) {
            if(entity instanceof Player) {
                player.sendMessage("This weapon has been temporarily disabled in PvP.");
                return;
            }
            player.getActionManager().setAction(new BlowpipeRangedCombat(entity));
        } else if (weaponName.contains("dark bow")) {
            player.getActionManager().setAction(new DarkBowRangedCombat(entity));
        } else if (weaponId == ItemId.RED_CHINCHOMPA_10034 || weaponId == ItemId.BLACK_CHINCHOMPA || weaponId == ItemId.CHINCHOMPA_10033) {
            player.getActionManager().setAction(new ChinchompaRangedCombat(entity));
        } else if (isRangedWeapon(weaponId, weaponName)) {
            player.getActionManager().setAction(new RangedCombat(entity));
        } else if (weaponName.equals("granite maul")) {
            player.getActionManager().setAction(new GraniteMaulCombat(entity));
        } else if (weaponName.equals("sanguine scythe of vitur") ||
                weaponName.equals("holy scythe of vitur") ||
                weaponName.contains("scythe of vitur") ||
                weaponName.equals("corrupted scythe of vitur") ||
                weaponName.equalsIgnoreCase("christmas scythe")
        ) {
            player.getActionManager().setAction(new ScytheOfViturCombat(entity));
        } else if (weaponName.equals("dinh's bulwark")) {
            player.getActionManager().setAction(new DinhsBulwarkCombat(entity));
        } else if (weaponName.contains("guthan")) {
            player.getActionManager().setAction(new GuthanCombat(entity));
        } else if (weaponName.contains("verac")) {
            player.getActionManager().setAction(new VeracCombat(entity));
        } else if (weaponName.contains("torag")) {
            player.getActionManager().setAction(new ToragCombat(entity));
        }
        else if (weaponId == ItemId.SOULREAPER_AXE_28338)
            player.getActionManager().setAction(new SoulreaperCombat(entity));
        else if (weaponId == ItemId.URSINE_CHAINMACE_27660)   player.getActionManager().setAction(new UrsineChainmaceCombat(entity));
        else if (weaponId == ItemId.VIGGORAS_CHAINMACE) player.getActionManager().setAction(new ViggoraChainmaceCombat(entity));
        else if (weaponId == ItemId.BLISTERWOOD_FLAIL)    player.getActionManager().setAction(new BlisterwoodFlailCombat(entity));
        else if (weaponId == ItemId.IVANDIS_FLAIL) {
            player.getActionManager().setAction(new IvandisFlailCombat(entity));
        } else if (isOsmumtenFang(weaponId)) {
            player.getActionManager().setAction(new OsmumtenFangCombat(entity));
        } else if (isKerisWeapon(weaponId)) {
            player.getActionManager().setAction(new KerisCombat(entity));
        } else {
            player.getActionManager().setAction(new MeleeCombat(entity));
        }
    }

    public static void addAttackedByDelay(final Entity entity, final Entity target) {
        final boolean isPlayer = entity instanceof Player;
        final Player player = isPlayer ? (Player) entity : null;
        final boolean targetIsPlayer = target instanceof Player;

        if (isPlayer && !player.wieldingGraniteMaul()) {
            /* Granite maul technically starts a normal queue whenever an outgoing hit happens while not wielding the
             maul.
            This technically also checks to make sure the player is carrying a granite maul, but this can be omitted. */
            player.outgoingHit(target);
        }

        entity.setAttacking(target);
        target.setAttackedBy(player);

        final long currentTick = WorldThread.getCurrentCycle();
        entity.setAttackedAtTick(currentTick);
        target.setAttackedTick(currentTick);

        final long pjTimer = CombatCooldownKt.getStealCooldownDurationInTicks(entity, target);
        target.setAttackedByDelay(currentTick + pjTimer);
        entity.setAttackingDelay(Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(pjTimer));

        if (isPlayer && targetIsPlayer) {
            final VarManager var = player.getVarManager();
            final int targetIndex = target.getIndex();
            if (var.getValue(TARGET_INDEX_VAR) != targetIndex) {
                var.sendVar(TARGET_INDEX_VAR, targetIndex);
            }
        }
    }

    public static final int TARGET_INDEX_VAR = 1075;

    protected static int getRequiredSpecial(final Player player) {
        final Item weapon = player.getWeapon();
        if (weapon == null) {
            return 0;
        }
        int requiredSpecial = SPECIAL_ENUM.getIntValue(weapon.getId());
        if (player.getArea() instanceof final AbstractTOARaidArea area && area.isOverlyDraining()) {
            requiredSpecial = 1000;
        }
        return requiredSpecial / (player.getVariables().getTime(TickVariable.LIQUID_ADRENALINE) > 0 ? 20 : 10);
    }

    private static void useInstantSpecial(final Player player, final ISpecialAttack special) {
        final int specialEnergy = getRequiredSpecial(player);
        if (player.getCombatDefinitions().getSpecialEnergy() < specialEnergy) {
            player.sendMessage("You don't have enough special energy.");
            player.getCombatDefinitions().setSpecial(false, true);
            return;
        }
        player.getCombatDefinitions().setSpecial(false, true);
        player.getCombatDefinitions().setSpecialEnergy(player.getCombatDefinitions().getSpecialEnergy() - specialEnergy);
        player.setAnimation(special.getAnimation());
        if (special.getGraphics() != null) {
            player.setGraphics(special.getGraphics());
        }
        special.getAttack().attack(player, null, null);
    }

    public static int getTargetDefenceRoll(final Entity attacker, final Entity target, AttackType type) {
        double effectiveLevel;
        int equipmentBonus;
        if (target.getEntityType() == EntityType.PLAYER) {
            final Player t = (Player) target;
            effectiveLevel = t.getSkills().getLevel(SkillConstants.DEFENCE);
            //We multiply by all prayer bonuses since you can only have one styles' prayers enabled at once, making
            //it safe to do so.
            effectiveLevel *= t.getPrayerManager().getSkillBoost(SkillConstants.DEFENCE);
            effectiveLevel *= t.getPrayerManager().getRangedBoost(SkillConstants.DEFENCE);
            effectiveLevel = Math.floor(effectiveLevel);
            final AttackStyle.AttackExperienceType attackType = t.getCombatDefinitions().getAttackExperienceType();
            effectiveLevel += (attackType == AttackExperienceType.DEFENCE_XP || attackType == AttackExperienceType.MAGIC_DEFENCE_XP || attackType == AttackExperienceType.RANGED_DEFENCE_XP) ? 3 : attackType == AttackExperienceType.SHARED_XP ? 1 : 0;
            effectiveLevel += 8;
            equipmentBonus = t.getBonuses().getDefenceBonus(type);

            final int amuletId = t.getEquipment().getId(EquipmentSlot.AMULET);
            if (equipmentBonus > 0 && (amuletId == 12851 || amuletId == 12853) && CombatUtilities.hasFullBarrowsSet(t
                    , "Torag's")) {
                final int missingHealth = t.getMaxHitpoints() - t.getHitpoints();
                float defaultPercentage = 1;
                defaultPercentage += (missingHealth / 100.0F);
                equipmentBonus *= defaultPercentage;
            }
            if (type == AttackType.MAGIC) {
                effectiveLevel = Math.floor(effectiveLevel * 0.3F);
                double magicEffectiveDefence = t.getSkills().getLevel(SkillConstants.MAGIC);
                magicEffectiveDefence *= t.getPrayerManager().getMagicBoost(SkillConstants.DEFENCE);
                magicEffectiveDefence = Math.floor(magicEffectiveDefence);
                magicEffectiveDefence *= 0.7F;
                if (attacker.getEntityType() == EntityType.PLAYER) {
                    final Player player = (Player) attacker;
                    final int ringId = player.getEquipment().getId(EquipmentSlot.RING);
                    if (ringId == ItemId.BRIMSTONE_RING && Utils.random(3) == 0) {
                        player.sendFilteredMessage(Colour.RED.wrap("You have reduced your opponent's magic defense."));
                        magicEffectiveDefence *= 0.9;
                    }
                }
                effectiveLevel += magicEffectiveDefence;
            }
            double bonus = effectiveLevel * (equipmentBonus + 64);
            if (attacker instanceof Player) {
                return (int) (bonus * GameConstants.defenceMultiplier);
            }
            return (int) bonus;
        } else {
            final NPC npc = (NPC) target;
            {
                effectiveLevel = npc.getCombatDefinitions().getStatDefinitions().get(type == AttackType.MAGIC && !(npc instanceof IceDemon) ? StatType.MAGIC : StatType.DEFENCE);
                equipmentBonus = npc.getCombatDefinitions().getStatDefinitions().get(StatType.getDefenceType(type));
            }

            if (type == AttackType.MAGIC && attacker.getEntityType() == EntityType.PLAYER) {
                final Player player = (Player) attacker;
                final int ringId = player.getEquipment().getId(EquipmentSlot.RING);
                if (ringId == ItemId.BRIMSTONE_RING && Utils.random(3) == 0) {
                    player.sendFilteredMessage(Colour.RED.wrap("You have reduced your opponent's magic defense."));
                    effectiveLevel *= 0.9;
                }
            }
        }
        return (int) (effectiveLevel * (equipmentBonus + 64));
    }

    @Override
    public void stop() {
        final int faceEntity = player.getFaceEntity();
        final long lastDelay = player.getLastFaceEntityDelay();
        WorldTasksManager.schedule(() -> {
            if (player.getFaceEntity() == faceEntity && player.getLastFaceEntityDelay() == lastDelay) {
                player.setFaceEntity(null);
            }
        });
    }

    public int calculateMinimumHit(Player player, int maxHit) {
        return 1;
    }

    public abstract Hit getHit(Player player, final Entity target, final double accuracyModifier,
                               final double passiveModifier, double activeModifier, final boolean ignorePrayers);

    public abstract int getRandomHit(Player player, final Entity target, final int maxhit, final double modifier);

    public abstract int getRandomHit(Player player, final Entity target, final int maxhit, final double modifier,
                                     final AttackType oppositeIndex);

    /**
     * The true attack roll calculation referenced in OSRS combat formulas.
     * @param player attacker
     * @param target defender
     * @param resultModifier global multi
     * @return attack roll
     */
    public abstract int getAccuracy(Player player, final Entity target, final double resultModifier);


    public boolean isSuccessful(Player player, final Entity target, final double modifier,
                                final AttackType oppositeIndex) {
        final int accuracy = getAccuracy(player, target, modifier);
        final int targetRoll = getTargetDefenceRoll(player, target, oppositeIndex);
        final int accRoll = Utils.random(accuracy);
        final int defRoll = Utils.random(targetRoll);
        return accRoll > defRoll;
    }

    public abstract int getMaxHit(final Player player, final double passiveModifier, double activeModifier,
                                  final boolean ignorePrayers);

    public final void delayHit(final int delay, final Hit... hits) {
        delayHit(target, delay, hits);
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

    private final static SoundEffect DRAGONFIRE_SHIELD_START_SOUND = new SoundEffect(3761, 10, 0);
    private final static SoundEffect DRAGONFIRE_SHIELD_END_SOUND = new SoundEffect(161, 10, -1);
    private final static SoundEffect ANCIENT_WYVERN_SHIELD_END_SOUND = new SoundEffect(170, 10, -1);

    protected final boolean handleDragonfireShieldsRNG(final Player player) {
        boolean randomDfsProc = target instanceof NPC
                && ArrayUtils.contains(DragonfireShield.DRAGONFIRE_SHIELDS, player.getEquipment().getId(EquipmentSlot.SHIELD))
                && Utils.random(34) == 0;

        if(randomDfsProc) {
            shootDragonFire(this, false);
            return true;
        }
        return false;
    }

    protected final boolean handleDragonfireShields(final Player player, final boolean execute) {
        boolean isPossible = player.getNumericTemporaryAttribute("dragonfireBurstDelay").longValue() > Utils.currentTimeMillis();

        final Map<Object, Object> temporaryAttributes = player.getTemporaryAttributes();

        final boolean dfsTrigger = (temporaryAttributes.get("dragonfireBurst") == Boolean.TRUE);

        if (!dfsTrigger)
            return false;

        if (!ArrayUtils.contains(DragonfireShield.DRAGONFIRE_SHIELDS,
                player.getEquipment().getId(EquipmentSlot.SHIELD)))
            return false;

        if (isPossible) {
            temporaryAttributes.remove("dragonfireBurst");
            player.sendMessage("Your shield hasn't finished recharging yet.");
            return false;
        }

        if (!Utils.isOnRange(player.getX(), player.getY(), player.getSize(), target.getX(), target.getY(),
                target.getSize(), 10))
            return false;

        if (!execute)
            return true;

        final boolean wyvernVariant;
        {
            final Item shield = player.getEquipment().getItem(EquipmentSlot.SHIELD);
            temporaryAttributes.put("dragonfireBurstDelay", Utils.currentTimeMillis() + 60000);
            player.getChargesManager().removeCharges(shield, 1, player.getEquipment().getContainer(),
                    EquipmentSlot.SHIELD.getSlot());
            temporaryAttributes.remove("dragonfireBurst");
            wyvernVariant = shield.getName().equals("Ancient wyvern shield");
        }
        shootDragonFire(this, wyvernVariant);
        return true;
    }

    public static void shootDragonFire(PlayerCombat playerCombat, boolean wyvernVariant) {
        final Player player = playerCombat.player;
        World.sendSoundEffect(player, DRAGONFIRE_SHIELD_START_SOUND);
        player.setAnimation(DRAGONFIRE_SPECIAL_ANIM);
        player.setGraphics(wyvernVariant ? WYVERN_DRAGONFIRE_START_GFX : DRAGONFIRE_START_GFX);
        final int clientTicks =
                (wyvernVariant ? WYVERN_DRAGONFIRE_PROJ : DRAGONFIRE_PROJ).getProjectileDuration(player.getLocation()
                        , playerCombat.target);
        final SoundEffect areaSound = wyvernVariant ? ANCIENT_WYVERN_SHIELD_END_SOUND : DRAGONFIRE_SHIELD_END_SOUND;
        World.sendSoundEffect(playerCombat.target, new SoundEffect(areaSound.getId(), areaSound.getRadius(),
                clientTicks));
        World.scheduleProjectile(player, playerCombat.target, wyvernVariant ? WYVERN_DRAGONFIRE_PROJ :
                DRAGONFIRE_PROJ).schedule(() -> {
            playerCombat.target.setGraphics(wyvernVariant ? WYVERN_DRAGONFIRE_HIT_GFX : DRAGONFIRE_HIT_GFX);
            final int damage = playerCombat.getRandomHit(player, playerCombat.target, 25, 1, AttackType.MAGIC);
            final Hit hit = new Hit(player, damage, HitType.REGULAR);
            if (damage == 25) {
                hit.setMax(true);
            }
            hit.putAttribute("dfs special", true);
            playerCombat.delayHit(0, hit);
            if (wyvernVariant && damage > 0) {
                playerCombat.target.freezeWithNotification(8);
            }
        });
    }

    public static boolean DEBUG_ATTACKABLE_STATE = false;

    protected final boolean attackable() {

        CombatCooldownKt.clearAttackedByIfExpired(player);

        if (target.isForceMultiArea() || target.isMultiArea())
            return true;

        final AreaType targetAreaType = target.getAreaType();
        final boolean targetInSingleWay = AreaTypes.SINGLE_WAY.equals(targetAreaType);
        final boolean targetInSinglePlus = AreaTypes.SINGLES_PLUS.equals(targetAreaType);

        final boolean targetingNpc = target instanceof NPC;

        final Entity attackedBy = player.getAttackedBy();
        final boolean attackedByAnyone = attackedBy != null;
        final boolean attackedByNpc = attackedByAnyone && attackedBy instanceof NPC;
        final boolean attackedByAnyoneButTarget = attackedByAnyone && attackedBy != target;
        final boolean attackedByTarget = attackedByAnyone && attackedBy == target;
        final boolean attackedByPlayer =  attackedByAnyone && attackedBy instanceof Player;

        final int targetAttackedByCooldownTicks = CombatCooldownKt.getAttackedByCooldownTicks(target);
        final boolean targetAttackedByCooldown = targetAttackedByCooldownTicks <= 0;

        final Entity targetAttackedBy = target.getAttackedBy();
        final boolean targetAttackedByAnyone = targetAttackedBy != null;
        final boolean targetAttackedByNpc = targetAttackedByAnyone && targetAttackedBy instanceof NPC;
        final boolean attackedByNpcAndNpcDead = attackedByNpc && (attackedBy.isDead() || attackedBy.isFinished());
        final boolean targetAttackedByPlayer = targetAttackedByAnyone && targetAttackedBy instanceof Player;
        final boolean targetAttackedByMe = targetAttackedByAnyone && targetAttackedBy == player;
        final boolean targetAttackedByAnyoneButMe = targetAttackedByAnyone && targetAttackedBy != player;
        final long targetAttackingCooldownTicks = CombatCooldownKt.getAttackCooldownTicks(target);
        final boolean targetAttackingCooldown = targetAttackingCooldownTicks <= 0;

        final Entity targetAttacking = target.getAttacking();
        final boolean targetAttackingAnyone = targetAttacking != null;
        final boolean targetAttackingAnyoneButMe = targetAttackingAnyone && targetAttacking != player;

        final long attackedByCooldownTicks = CombatCooldownKt.getAttackedByCooldownTicks(player);
        final boolean attackedByCooldown = attackedByCooldownTicks <= 0;

        final boolean ignoreCooldownB = target.isForceAttackable();

        if (targetInSinglePlus) {
            if (attackedByAnyoneButTarget) {
                if (attackedByPlayer || (attackedByNpc && targetingNpc && !attackedByNpcAndNpcDead)) {
                    if (!attackedByCooldown && !ignoreCooldownB) {
                        if (DEBUG_ATTACKABLE_STATE)
                            player.setForceTalk(Colour.MAROON.wrap("my cooldown B") + "  is active -> " + attackedByCooldownTicks);
                        player.sendMessage("You are already in combat.");
                        return false;
                    }
                }
            }
            if (targetAttackingAnyoneButMe) {
                if (target instanceof Player && targetAttacking instanceof Player) {
                    if (!targetAttackingCooldown) {
                        if (DEBUG_ATTACKABLE_STATE)
                            player.setForceTalk(Colour.RS_PINK.wrap(target + "'s cooldown A") + " is active -> " + attackedByCooldownTicks + "");
                        player.sendMessage("That " + (target instanceof Player ? "player" : "npc") + " is already in combat.");
                        return false;
                    }
                }
            }
            if (targetAttackedByNpc) {
                if (DEBUG_ATTACKABLE_STATE)
                    player.setForceTalk("Can attack target even though it's attacked by an npc because they are in single plus.");
                if (!ignoreCooldownB)
                    target.getAttackedBy().cancelCombat();
                return true;
            }
            if (targetAttackedByMe) {
//                player.setForceTalk("Can attack target because you are already attacking them.");
                return true;
            }
            if (targetAttackedByPlayer) {
                if (!targetAttackedByCooldown) {
                    if (DEBUG_ATTACKABLE_STATE)
                        player.setForceTalk(Colour.RS_PINK.wrap(target + "'s cooldown B")+" is active "+targetAttackedByCooldownTicks);
                    player.sendMessage("That " + (target instanceof Player ? "player" : "npc") + " is already in combat.");
                    return false;
                }
                if (DEBUG_ATTACKABLE_STATE)
                    player.setForceTalk("Can attack target even though it's attacked by a player because they are in single plus and the attacks have cooled down.");
                return true;
            }
            if (DEBUG_ATTACKABLE_STATE)
                player.setForceTalk("Can attack target because they are not attacked by anyone.");
            return true;
        }
        if (targetInSingleWay) {
            if (attackedByAnyoneButTarget) {
                if (!attackedByCooldown && !attackedByNpcAndNpcDead && !ignoreCooldownB) {
                    if (DEBUG_ATTACKABLE_STATE)
                        player.setForceTalk(Colour.MAROON.wrap("my cooldown B")+"  is active -> "+attackedByCooldownTicks+")");
                    player.sendMessage("You are already in combat.");
                    return false;
                }
            }
            if (targetAttackedByAnyoneButMe) {
                if (!targetAttackedByCooldown) {
                    if (DEBUG_ATTACKABLE_STATE)
                        player.setForceTalk(Colour.RS_PINK.wrap(target + "'s cooldown B")+" is active -> "+targetAttackedByCooldownTicks+"");
                    player.sendMessage("That " + (target instanceof Player ? "player" : "npc") + " is already in combat.");
                    return false;
                }
                if (DEBUG_ATTACKABLE_STATE)
                    player.setForceTalk("Can attack target even though it's attacked by someone else because they are in single way and the attacks have cooled down.");
                return true;
            }
            if (targetAttackedByMe) {
//                player.setForceTalk("Can attack target because you are already attacking them.");
                return true;
            }
            if (DEBUG_ATTACKABLE_STATE)
                player.setForceTalk("Can attack target because they are not attacked by anyone.");
            return true;
        }
        return false;
    }

    protected boolean withinRange(final Position targetPosition,
                                  final int maximumDistance,
                                  final int targetSize) {
        final Location target = targetPosition.getPosition();
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = player.getSize();
        if (distanceX == -size - maximumDistance && distanceY == -size - maximumDistance
                || distanceX == targetSize + maximumDistance && distanceY == targetSize + maximumDistance
                || distanceX == -size - maximumDistance && distanceY == targetSize + maximumDistance
                || distanceX == targetSize + maximumDistance && distanceY == -size - maximumDistance) {
            return false;
        }
        return !(distanceX > targetSize + maximumDistance
                || distanceY > targetSize + maximumDistance
                || distanceX < -size - maximumDistance
                || distanceY < -size - maximumDistance);
    }

    private static boolean entitiesCollide(final Entity first, final Entity second, final int extraDistance) {
        final int distanceX = first.getX() - second.getX();
        final int distanceY = first.getY() - second.getY();
        final int firstSize = first.getSize();
        final int secondSize = second.getSize();
        return distanceX > secondSize + extraDistance || distanceY > secondSize + extraDistance || distanceX < -firstSize - extraDistance || distanceY < -firstSize - extraDistance;
    }

    public final int useSpecial(final Player player, final SpecialType type) {
        final ISpecialAttack special =
                SpecialAttack.SPECIAL_ATTACKS.get(player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot()));
        if (special == null) {
            player.getCombatDefinitions().setSpecial(false, true);
            return -2;
        }
        if (special == SpecialAttack.DAWNBRINGER) {
            if (target instanceof Player || !player.inArea("The Final Challenge")) {
                player.sendMessage("You cannot use the Dawnbringer outside of the Theatre of Blood.");
                return -2;
            }
        }
        final int specialEnergy = getRequiredSpecial(player);
        if (player.getCombatDefinitions().getSpecialEnergy() < specialEnergy) {
            player.sendMessage("You don't have enough special energy.");
            player.getCombatDefinitions().setSpecial(false, true);
            return -2;
        }
        if (!special.testPredicate(player)) {
            player.sendMessage("You are not able to perform this special attack right now.");
            player.getCombatDefinitions().setSpecial(false, true);
            return -2;
        }
        if (special.getType() != type) {
            return -2;
        }
        player.setAnimation(special.getAnimation());
        if (special.getGraphics() != null) {
            player.setGraphics(special.getGraphics());
        }
        special.getAttack().attack(player, this, target);
        player.getCombatDefinitions().setSpecial(false, true);
        player.getCombatDefinitions().setSpecialEnergy(player.getCombatDefinitions().getSpecialEnergy() - specialEnergy);
        return special.getDelay();
    }

    public AttackStyle getAttackStyle() {
        if(player.getCombatDefinitions() != null)
            return player.getCombatDefinitions().getAttackStyle();
        else return new AttackStyle(AttackType.MELEE, AttackExperienceType.NOT_AVAILABLE);
    }

    public final void delayHit(final Entity target, int delay, final Hit... hits) {
        if (hits.length > 0 && hits[0] != null) {
//            addAttackingDelay(hits[0].getSource());
        }
        if (target.getEntityType() == EntityType.PLAYER) {
            if (player.getPid() < ((Player) target).getPid()) {
                delay -= 1;
            }
        }

        final boolean usingSpecial = player.getCombatDefinitions().isUsingSpecial();
        final RegionArea area = player.getArea();

        for (final Hit hit : hits) {

            if (hit == null)
                continue;

            if (hit.getWeapon() == null)
                hit.setWeapon(player.getWeapon());

            if (usingSpecial) {
                hit.setSpecialAttack();
            }

            if (target instanceof final NPC npc) {
                final int cap = npc.getDamageCap();
                if (cap >= 0 && hit.getDamage() > cap) {
                    hit.setDamage(cap);
                }
                npc.listenScheduleHit(hit);
            }

            final float xpModifier = target.getXpModifier(hit);
            if (area instanceof HitProcessPlugin) {
                if (!((HitProcessPlugin) area).hit(player, target, hit, xpModifier)) {
                    continue;
                }
            }

            final int damage = Math.min(hit.getDamage(), target.getHitpoints());
            AttackStyle style = getAttackStyle();
            if(hit.getSource() instanceof Player p && p.getWeapon() != null && p.getWeapon().getId() == ItemId.VOIDWAKER_27690 && usingSpecial)
                style = new AttackStyle(AttackType.MAGIC, AttackExperienceType.MAGIC_XP);
            grantExperience(SkillConstants.HITPOINTS, damage * 1.33F * xpModifier);
            AttackExperienceType type = style.getExperienceType();
            if (player.getCombatDefinitions().getAutocastSpell() != null) {
                if (player.getCombatDefinitions().isDefensiveAutocast()) {
                    type = AttackExperienceType.MAGIC_DEFENCE_XP;
                } else {
                    type = AttackExperienceType.MAGIC_XP;
                }
            } else if (hit.getHitType() == HitType.MAGIC && (type != AttackExperienceType.MAGIC_XP && type != AttackExperienceType.MAGIC_DEFENCE_XP)) {
                type = AttackExperienceType.MAGIC_XP;
            }
            if (hit.containsAttribute("dfs special")) {
                type = target instanceof Player
                        ? AttackExperienceType.DEFENCE_XP
                        : AttackExperienceType.MAGIC_DEFENCE_XP;
            }
            switch (type) {
                case ATTACK_XP -> grantExperience(SkillConstants.ATTACK, 4 * damage * xpModifier);
                case STRENGTH_XP -> grantExperience(SkillConstants.STRENGTH, 4 * damage * xpModifier);
                case DEFENCE_XP -> grantExperience(SkillConstants.DEFENCE, 4 * damage * xpModifier);
                case RANGED_XP -> grantExperience(SkillConstants.RANGED, 4 * damage * xpModifier);
                case MAGIC_XP -> grantExperience(SkillConstants.MAGIC, 2 * damage * xpModifier);
                case RANGED_DEFENCE_XP -> {
                    grantExperience(SkillConstants.RANGED, 2 * damage * xpModifier);
                    grantExperience(SkillConstants.DEFENCE, 2 * damage * xpModifier);
                }
                case MAGIC_DEFENCE_XP -> {
                    grantExperience(SkillConstants.MAGIC, 1.33F * damage * xpModifier);
                    grantExperience(SkillConstants.DEFENCE, damage * xpModifier);
                }
                case SHARED_XP -> {
                    grantExperience(SkillConstants.ATTACK, 1.33F * damage * xpModifier);
                    grantExperience(SkillConstants.STRENGTH, 1.33F * damage * xpModifier);
                    grantExperience(SkillConstants.DEFENCE, 1.33F * damage * xpModifier);
                }
            }
        }

        GreaterCorruptionKt.applyCorruptionEffect(player, target);
        final boolean skipDefenceAnimation = this instanceof MagicCombat;
        if (delay < 0) {
            if (!skipDefenceAnimation) {
                target.performDefenceAnimation(player);
            }
            processDelayedHits(target, player, hits);
        } else {
            if (delay == 0) {
                if (!skipDefenceAnimation) {
                    target.performDefenceAnimation(player);
                }
                WorldTasksManager.schedule(() -> processDelayedHits(target, player, hits), delay);
            } else {
                WorldTasksManager.schedule(new WorldTask() {
                    private int ticks;

                    @Override
                    public void run() {
                        if (ticks == 0) {
                            if (!skipDefenceAnimation) {
                                target.performDefenceAnimation(player);
                            }
                        } else {
                            processDelayedHits(target, player, hits);
                            stop();
                            return;
                        }
                        ticks++;
                    }
                }, delay - 1, 0);
            }
        }
    }

    protected void grantExperience(int skill, double amount) {
        player.getSkills().addXp(skill, amount, true, player.isIronman() && target instanceof Player, true);
    }

    private void processDelayedHits(final Entity target, final Player source, final Hit... hits) {
        final long delay = target.getProtectionDelay();
        if (source.isFinished() || target.isDead() || target.isFinished()) {
            return;
        }

        // process only hits with proper schedule time
        var filteredHits = Arrays.stream(hits)
                .filter(it -> it.getScheduleTime() >= delay)
                .toArray(Hit[]::new);

        // modify hits through source hit hooks
        for (final Hit hit : filteredHits) {
            source.handleOutgoingHit(target, hit);
        }

        // actually apply the damage
        for (final Hit hit : filteredHits) {
            target.applyHit(hit);
            final Consumer<Hit> consumer = hit.getOnLandConsumer();
            if (consumer != null) {
                consumer.accept(hit);
            }
        }

        target.autoRetaliate(source);
    }

    public final void attackTarget(final Set<Entity> targets, final MultiAttack perform) {
        final Entity realTarget = target;
        for (final Entity t : targets) {
            target = t;
            if (!perform.attack(realTarget)) {
                break;
            }
        }
        target = realTarget;
    }

    public final Set<Entity> getMultiAttackTargets(final Player player) {
        return getMultiAttackTargets(player, 1, 9);
    }

    public Set<Entity> getMultiAttackTargets(final Player player, final int maxDistance, final int maxAmtTargets) {
        final boolean multi = target.isMultiArea();
        Set<Entity> possibleTargets = new ObjectOpenHashSet<>();
        possibleTargets.add(target);
        if (multi) {
            final List<Entity> targets = CharacterLoop.find(target.getLocation(), 1, Entity.class,
                    entity -> isPotentialTarget(player, target.getLocation(), entity));
            final Location tile = target.getLocation();
            for (int i = targets.size() - 1; i >= 0; i--) {
                final Entity e = targets.get(i);
                if (e == player || e == target) {
                    continue;
                }
                final Location t = e.getLocation();
                if (!t.withinDistance(tile.getX(), tile.getY(), 1)) continue;
                if (e instanceof Player) {
                    if (!player.canHit((Player) e)) continue;
                }
                possibleTargets.add(e);
                if (possibleTargets.size() >= maxAmtTargets) {
                    break;
                }
            }
        }
        return possibleTargets;
    }

    protected boolean isPotentialTarget(final Entity source, final Location tile, final Entity entity) {
        final int entityX = entity.getX();
        final int entityY = entity.getY();
        final int entitySize = entity.getSize();
        final int x = tile.getX();
        final int y = tile.getY();
        final int size = 1;
        return entity != source && !entity.isDead() && !entity.isMaximumTolerance() && (entity.isMultiArea() || entity.getAttackedBy() == source) && (!ProjectileUtils.isProjectileClipped(null, entity, tile, entity.getLocation(), false, true) || CollisionUtil.collides(x, y, size, entityX, entityY, entitySize)) && (!(entity instanceof NPC) || ((NPC) entity).isAttackableNPC()) && (!(entity instanceof Player) || ((Player) entity).isCanPvp());
    }


    public interface MultiAttack {
        boolean attack(final Entity originalTarget);
    }

    public String getName() {
        return name;
    }

    public Entity getTarget() {
        return target;
    }

    public static boolean hasPoisonousWeapon(Player player) {
        final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON);
        if (weapon == null) {
            return false;
        }

        if (CombatUtilities.isWearingSerpentineHelmet(player) || CombatUtilities.isWearingVenomDamagingEquipment(player)) {
            return true;
        }

        switch (weapon.getId()) {
            case ItemId.ABYSSAL_TENTACLE:
            case 26484: {
                return true;
            }
        }

        return weapon.getName().contains("(p");
    }

    public static final String QUEUED_SPECS_ATTRIBUTE = "queued specs";
    public static final String WRATH_OF_AMASCUT_ATT = "wrath_of_amascut_tick";


    public boolean wearingZaryteCrossbow() {
        return player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.ZARYTE_CROSSBOW;
    }

    public static boolean isDemonbaneWeapon(int weaponId) {
        return switch (weaponId) {
            case ItemId.SILVERLIGHT, ItemId.DARKLIGHT, ItemId.ARCLIGHT, ItemId.EMBERLIGHT, ItemId.SCORCHING_BOW -> true;
            default -> false;
        };
    }

    public double determineSlayerHelmetAccuracyBoost(boolean hasTask, HitType type, Player player, Entity target) {
        if (target instanceof Player)
            return 1.0F;
        var checkImbued = type != HitType.MELEE;

        double baseMultiplier = 1.0F;

        if (!hasTask)
            return baseMultiplier;

        if (!checkImbued && slayerHelmOrStatue(player))
            baseMultiplier *= 1.13F;
        else if (checkImbued && slayerHelmIOrStatue(player))
            baseMultiplier *= 1.15F;

        return baseMultiplier;
    }

    public double determineSlayerHelmetDamageBoost(boolean hasTask, HitType type, Player player, Entity target) {
        if(target instanceof Player)
            return 1.0F;
        var checkImbued = type != HitType.MELEE;

        double baseMultiplier = 1.0F;

        if (!hasTask)
            return baseMultiplier;

        if (!checkImbued && slayerHelmOrStatue(player))
            baseMultiplier *= 1.13F;
        else if (checkImbued && slayerHelmIOrStatue(player))
            baseMultiplier *= 1.15F;

        return baseMultiplier;
    }

    void bloodFury(HitType type, Hit hit) {
        if (!(target instanceof NPC)) {
            return;
        }

        if(type == HitType.MELEE) {
            Item item = player.getEquipment().getItem(EquipmentSlot.AMULET);
            boolean hasItem = item != null && item.getId() == ItemId.AMULET_OF_BLOOD_FURY;
            if (hasItem && hit.getDamage() > 3 && Utils.randomBoolean(4)) {
                target.setGraphics(BLOOD_FURY_GFX);
                player.heal((int) (hit.getDamage() * 0.3D));
                player.getChargesManager().removeCharges(item, 1, player.getEquipment().getContainer(), EquipmentSlot.AMULET.getSlot());
            }
        }
    }


}
