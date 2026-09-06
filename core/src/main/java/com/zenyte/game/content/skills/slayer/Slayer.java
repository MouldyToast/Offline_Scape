package com.zenyte.game.content.skills.slayer;

import com.near_reality.game.content.slayer.*;
import com.near_reality.game.content.slayer.Assignment;
import com.near_reality.game.content.slayer.BossTask;
import com.near_reality.game.content.slayer.RegularTask;
import com.near_reality.game.content.slayer.SlayerMaster;
import com.near_reality.game.content.slayer.SlayerTask;
import com.near_reality.game.world.PlayerEvent.SlayerTaskCompleted;
import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.net.packet.PacketDispatcher;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.*;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.Keldagrim;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import mgi.types.config.enums.Enums;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Kris | 22. juuli 2018 : 15:01:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
@StaticInitializer
public class Slayer {
    private static final short[] BANNED_SLOT_VARBITS = new short[] {3209, 3210, 3211, 3212, 4441, 5023};
    private static final long FULL_EXTENSION_UNLOCK_HASH;
    public static final int LUMBRIDGE_ELITE_DIARY_COMPLETED_BIT = 4538;
    private static final int POINTS_REQUIRED_FOR_BLOCKING = 100;
    private static final int POINTS_REQUIRED_FOR_STORE_TASK = 50;
    private static final int SLAYER_POINTS_BIT = 4068;
    public static final int TASK_AMOUNT_VAR = 394;
    private static final int TASK_INDEX_VAR = 395;
    private static final int STORED_TASK_AMOUNT_VAR = 264;
    private static final int STORED_TASK_INDEX_VAR = 265;
    private static final int UNLOCK_REWARDS_FIRST_VARP = 1076;
    private static final int UNLOCK_REWARDS_SECOND_VARP = 1344;
    private static final boolean DOUBLE_POINTS = true;

    static {
        long hash = 0;
        try {
            for (final Integer value : Enums.TASK_EXTENSION_ENUM.getValues().values()) {
                hash |= 1L << value;
            }
        } catch (Throwable ignored) {
            hash = -1;
        } finally {
            FULL_EXTENSION_UNLOCK_HASH = hash;
        }

    }

    private com.near_reality.game.content.slayer.Assignment assignment;
    public Int2ObjectOpenHashMap<com.near_reality.game.content.slayer.RegularTask> bannedTasks;
    private com.near_reality.game.content.slayer.SlayerMaster master;
    private transient Player partner;
    private transient Player player;
    private String lastAssignmentName;
    private com.near_reality.game.content.slayer.Assignment storedAssignment;

    public void setAssignment(final com.near_reality.game.content.slayer.Assignment assignment) {
        this.assignment = assignment;
        lastAssignmentName = assignment.getTaskName();
        refreshCurrentAssignment();
    }

    public Slayer(final Player player) {
        this.player = player;
        bannedTasks = new Int2ObjectOpenHashMap<>(6);
        master = com.near_reality.game.content.slayer.SlayerMaster.TURAEL;
    }

    @Listener(type = ListenerType.LOGOUT)
    private static void onLogout(final Player player) {
        final Slayer slayer = SlayerKeys.slayer(player);
        if (slayer.partner != null) {
            SlayerKeys.slayer(slayer.partner).setPartner(null);
            slayer.partner.sendMessage("Your Slayer partner has logged out.");
        }
    }

    @Subscribe
    public static void onInit(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final boolean hadPersistedAttr = SlayerKeys.rawSlayerAttr(player) != null;
        final Slayer slayer = SlayerKeys.slayer(player);
        if (hadPersistedAttr || savedPlayer == null) {
            return;
        }
        // Legacy path: pre-migration saves keep the slayer state under the
        // top-level "slayer" JSON key on the parser player. The copy below
        // migrates it into the attr (running the same copy the legacy
        // setFields initialize always ran); the next save persists it under
        // attrPersistence["slayer"] and drops the legacy key.
        @SuppressWarnings("deprecation")
        final Slayer savedSlayer = savedPlayer.getSlayer();
        if (savedSlayer == null) {
            return;
        }
        slayer.copyFrom(savedSlayer);
    }

    void addSlayerPoints(int amount) {
        final int currentPoints = getSlayerPoints();
        final int modifiedPoints = currentPoints + amount;
        player.addAttribute("slayer_points", Math.max(0, modifiedPoints));
        refreshSlayerPoints();
    }

    boolean removeSlayerPoints(int amount) {
        final int currentPoints = getSlayerPoints();
        if(currentPoints < amount) return false;
        final int modifiedPoints = currentPoints - amount;
        player.addAttribute("slayer_points", Math.max(0, modifiedPoints));
        refreshSlayerPoints();
        return true;
    }

    public void checkAssignment(final NPC npc) {
        if (assignment == null) {
            return;
        }
        assignment.checkAssignment(npc.getName(player), npc);
    }

    void confirmTaskStore() {
        if (!isUnlocked("Task Storage")) {
            player.sendMessage("You need to unlock Task Storage to store tasks.");
            return;
        }

        if (assignment == null) {
            player.sendMessage("You need a Slayer assignment to store it.");
            return;
        }

        if (storedAssignment != null) {
            player.sendMessage("You already have an assignment stored.");
            return;
        }

        final int points = getSlayerPoints();
        if (points < POINTS_REQUIRED_FOR_STORE_TASK) {
            player.sendMessage("You do not have enough Slayer Points to store your task. You need " + POINTS_REQUIRED_FOR_STORE_TASK + " Slayer Points.");
            return;
        }

        final com.near_reality.game.content.slayer.SlayerMaster master = assignment.getMaster();
        if (master.equals(com.near_reality.game.content.slayer.SlayerMaster.KRYSTILIA)) {
            player.sendMessage("You can't store a Wilderness task.");
            return;
        }

        removeSlayerPoints(POINTS_REQUIRED_FOR_STORE_TASK);
        storedAssignment = new com.near_reality.game.content.slayer.Assignment();
        storedAssignment.initialize(player, assignment);
        assignment = null;
        refreshCurrentAssignment();
    }

    void confirmTaskUnstore() {
        if (!isUnlocked("Task Storage")) {
            player.sendMessage("You need to unlock Task Storage to unstore tasks.");
            return;
        }

        if (storedAssignment == null) {
            player.sendMessage("You don't have a stored assignment.");
            return;
        }

        if (assignment != null) {
            player.sendMessage("You can't unstore an assignment if you have a task.");
            return;
        }

        setAssignment(storedAssignment);
        storedAssignment = null;
        refreshCurrentAssignment();
    }

    void confirmFullExtensionUnlock() {
        final int cost = getRemainingExtensionsCost();
        final int currentPoints = getSlayerPoints();
        if (currentPoints < cost) {
            player.sendMessage("You don't have enough Slayer Points to unlock all the extensions. You need " + cost + " Slayer Points.");
            return;
        }
        if (FULL_EXTENSION_UNLOCK_HASH == -1) {
            player.sendMessage("Something went wrong. Please contact a developer.");
            return;
        }
        setUnlocksHash(getUnlocksHash() | FULL_EXTENSION_UNLOCK_HASH);
        removeSlayerPoints(cost);
        refreshRewards();
        player.sendMessage("Congratulations, you've unlocked all the extensions.");
    }

    void confirmPurchase(final int slotId) {
        if (isUnlocked(slotId)) return;
        Optional<String> unlockNameOptional = Enums.SLAYER_PERK_REWARD_NAMES.getValue(slotId);
        if (unlockNameOptional.isEmpty()) return;
        String unlockName = unlockNameOptional.get();
        final int pointsRequired = Enums.TASK_COST_ENUM.getValue(slotId).orElseThrow(Enums.exception());
        final int currentPoints = getSlayerPoints();
        if (currentPoints < pointsRequired) {
            player.sendMessage("You do not have enough Slayer Points to purchase '" + unlockName + "'.");
            return;
        }
        removeSlayerPoints(pointsRequired);
        setUnlocksHash(getUnlocksHash() | (1L << slotId));
        refreshRewards();
        player.sendMessage("Congratulations, you've unlocked '" + unlockName + "'.");
    }

    void confirmTaskBlock() {
        if (assignment == null) {
            player.sendMessage("You do not have a Slayer assignment right now.");
            return;
        }
        final int points = getSlayerPoints();
        if (points < POINTS_REQUIRED_FOR_BLOCKING) {
            player.sendMessage("You do not have enough Slayer Points to block your task. You need " + POINTS_REQUIRED_FOR_BLOCKING + " Slayer Points.");
            return;
        }
        final IntAVLTreeSet availableSlots = getAvailableBlockSlots();
        if (availableSlots.isEmpty()) {
            player.sendMessage("You don't have any empty slots to block this task!");
            return;
        }
        com.near_reality.game.content.slayer.RegularTask task;
        if (assignment.getTask() instanceof com.near_reality.game.content.slayer.BossTask) {
            task = com.near_reality.game.content.slayer.RegularTask.BOSS;
        } else {
            task = (com.near_reality.game.content.slayer.RegularTask) assignment.getTask();
        }
        bannedTasks.put(availableSlots.firstInt(), task);
        removeSlayerPoints(POINTS_REQUIRED_FOR_BLOCKING);
        assignment = null;
        lastAssignmentName = null;
        refreshCurrentAssignment();
        refreshBlockedTasks();
    }

    void confirmTaskCancellation() {
        if (assignment == null) {
            player.sendMessage("You do not have a Slayer assignment right now.");
            return;
        }
        final int points = getSlayerPoints();
        if (points < getPointsRequiredForCancellation(player.getMemberRank())) {
            player.sendMessage("You do not have enough Slayer Points to cancel your task. You need " + getPointsRequiredForCancellation(player.getMemberRank()) + " Slayer Points.");
            return;
        }
        assignment = null;
        lastAssignmentName = null;
        removeSlayerPoints(getPointsRequiredForCancellation(player.getMemberRank()));
        player.sendMessage("Your Slayer assignment has been cancelled.");
        refreshCurrentAssignment();
    }

    private int getPointsRequiredForCancellation(MemberRank rank) {
        return switch (rank) {
            case TOPAZ, SAPPHIRE -> 25;
            case EMERALD, RUBY, DIAMOND, DRAGONSTONE -> 20;
            case ONYX -> 18;
            case ZENYTE -> 14;
            case ENCHANTED -> 10;
            case GOLD, ETERNAL -> 5;
            case NEBULA, CATALYTIC -> 0;
            default -> 30;
        };
    }

    public void removeTask() {
        assignment = null;
        lastAssignmentName = null;
        player.sendMessage("Your Slayer assignment has been cancelled.");
        refreshCurrentAssignment();
    }

    void confirmTaskUnblock(final int slotId) {
        if (!bannedTasks.containsKey(slotId)) {
            player.sendMessage("You don't have a Slayer task blocked in that slot.");
            return;
        }
        bannedTasks.remove(slotId);
        refreshBlockedTasks();
    }

    void disable(final int slotId) {
        final Optional<String> entry = Enums.SLAYER_PERK_REWARD_NAMES.getValue(slotId);
        if (!entry.isPresent()) {
            throw new RuntimeException("Incorrect task index: " + slotId);
        }
        final String name = entry.get();
        if (name.equalsIgnoreCase("Bigger and Badder")) {
            player.getSettings().toggleSetting(Setting.BIGGER_AND_BADDER_SLAYER_REWARD);
            return;
        } else if (name.equalsIgnoreCase("Stop the Wyvern")) {
            player.getSettings().toggleSetting(Setting.STOP_THE_WYVERN_SLAYER_REWARD);
            return;
        }
        if (!Enums.TASK_DISABLE_ENUM.getValue(slotId).isPresent() || !isUnlocked(slotId)) {
            return;
        }
        player.sendMessage("You've disabled the extension '" + name + "'.");
        setUnlocksHash(getUnlocksHash() & ~(1L << slotId));
        refreshRewards();
    }

    public boolean isBiggerAndBadder() {
        return isUnlocked("Bigger and Badder") && !player.getBooleanSetting(Setting.BIGGER_AND_BADDER_SLAYER_REWARD);
    }

    public boolean isLikeABoss() {
        return isUnlocked("Like a Boss");
    }

    public void finishAssignment(NPC npc) {
        final com.near_reality.game.content.slayer.SlayerMaster master = assignment.getMaster();
        var taskedAmount = assignment.getInitialAmount();
        var taskedNpcExp = assignment.getTask().getExperience(npc);
        var experience = taskedNpcExp * player.getExperienceRate(SkillConstants.SLAYER);
        player.sendMessage(
                Colour.RED.wrap("You have completed your task! You killed %s. %s %s.")
                        .formatted(
                                Colour.BLACK.wrap(String.valueOf(taskedAmount).concat(" ").concat(assignment.task.getTaskName())),
                                Colour.RED.wrap("You gained"),
                                Colour.BLACK.wrap(Utils.formatNumberWithCommas((long) (experience * taskedAmount)).concat(" xp"))
                        )
        );
        final int completedTasks = player.getNumericAttribute("completed tasks").intValue() + 1;
        player.addAttribute("completed tasks", completedTasks);

        World.postEvent(new SlayerTaskCompleted(player, assignment));

        switch (master) {
            case KRYSTILIA: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_WILDERNESS_ASSIGNMENTS);
                break;
            }
            case NIEVE: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_NIEVE_ASSIGNMENTS);
                extraCoins();
                break;
            }
            case DURADEL: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_DURADEL_ASSIGNMENTS);
                extraCoins();
                break;
            }
            case KONAR_QUO_MATEN: {
                player.getInventory().addOrDrop(new Item(ItemId.BRIMSTONE_KEY));
                extraCoins();
                break;
            }
            case MAZCHNA: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_MAZCHNA_ASSIGNMENTS);
                break;
            }
        }

        final int completedInARow;
        switch (master) {
            case KRYSTILIA -> {
                completedInARow = getKrystiliaStreak() + 1;
                setKrystiliaStreak(completedInARow);
            }
            case SUMONA -> {
                completedInARow = getSumonaStreak() + 1;
                setSumonaStreak(completedInARow);
            }
            default -> {
                completedInARow = player.getNumericAttribute("completed tasks in a row").intValue() + 1;
                player.addAttribute("completed tasks in a row", completedInARow);
            }
        }

        final int multiplier = master.getMultiplier(completedInARow);
        int pointsPerTask = master.getPointsPerTask();
        if (master.equals(com.near_reality.game.content.slayer.SlayerMaster.KONAR_QUO_MATEN) && DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING4, player)) {
            pointsPerTask = 20;
        }
        int amount = pointsPerTask * multiplier;
        if (DOUBLE_POINTS) {
            amount *= 2;
        }
        addSlayerPoints(amount);
        var message =
                "%s %s %s %s%s %s%s"
                        .formatted(
                                Colour.RED.wrap("You've completed"),
                                Colour.BLACK.wrap(completedInARow + " task" + (completedInARow == 1 ? "" : "s")),
                                Colour.RED.wrap("and received"),
                                Colour.BLACK.wrap(amount + " points"),
                                Colour.RED.wrap(", giving you a total of"),
                                Colour.BLACK.wrap(String.valueOf(getSlayerPoints())),
                                Colour.RED.wrap("; return to a Slayer master.")
                        );
        player.sendMessage(message);

        if (master.getPointsPerTask() == 0) {
            player.sendMessage(Colour.RED.wrap("Tasks assigned by Turael do not give any slayer points."));
        }
        if (assignment.getTask() instanceof com.near_reality.game.content.slayer.BossTask) {
            player.getSkills().addXp(SkillConstants.SLAYER, 5000);
        }
        assignment = null;
    }

    private void extraCoins() {
        Item coins = new Item(995, Utils.random(50_000, 200_000));
        player.getInventory().addOrDrop(coins);
        player.sendMessage("You have been awarded with extra " + Utils.formatNumberWithCommas(coins.getAmount()) + " x " + coins.getName() + " for finishing your Slayer assignment!");
    }

    public List<com.near_reality.game.content.slayer.RegularTask> getAvailableAssignments(final com.near_reality.game.content.slayer.SlayerMaster master) {
        return SlayerHelper.INSTANCE.getAvailableAssignments(player, master);
    }

    public List<com.near_reality.game.content.slayer.BossTask> getAvailableBossAssignments() {
        final ArrayList<com.near_reality.game.content.slayer.BossTask> possibleTasks = new ArrayList<>((int) (com.near_reality.game.content.slayer.BossTask.VALUES.length / 2.0F));
        possibleTasks.addAll(Arrays.asList(com.near_reality.game.content.slayer.BossTask.VALUES));
        return possibleTasks;
    }

    public com.near_reality.game.content.slayer.Assignment generateTask(final com.near_reality.game.content.slayer.SlayerMaster master) {
        return SlayerHelper.INSTANCE.generateTask(player, master);
    }

    public com.near_reality.game.content.slayer.SlayerMaster getAdvisedMaster() {
        final Skills skills = player.getSkills();
        for (int i = com.near_reality.game.content.slayer.SlayerMaster.getEntries().size() - 1; i >= 0; i--) {
            final com.near_reality.game.content.slayer.SlayerMaster master = com.near_reality.game.content.slayer.SlayerMaster.getEntries().get(i);
            if (skills.getCombatLevel() >= master.getCombatRequirement() && skills.getLevelForXp(SkillConstants.SLAYER) >= master.getSlayerRequirement()) {
                return master;
            }
        }
        return com.near_reality.game.content.slayer.SlayerMaster.TURAEL;
    }

    public int getCompletedTasks() {
        return player.getNumericAttribute("completed tasks").intValue();
    }

    public int getCurrentStreak() {
        return player.getNumericAttribute("completed tasks in a row").intValue();
    }

    public void setCurrentStreak(final int value) {
        player.addAttribute("completed tasks in a row", value);
    }

    public int getKrystiliaStreak() {
        return player.getNumericAttribute("krystilia completed tasks in a row").intValue();
    }

    public void setKrystiliaStreak(final int value) {
        player.addAttribute("krystilia completed tasks in a row", value);
    }

    public int getSumonaStreak() {
        return player.getNumericAttribute("sumona completed tasks in a row").intValue();
    }

    public void setSumonaStreak(final int value) {
        player.addAttribute("sumona completed tasks in a row", value);
    }

    public int getSlayerPoints() {
        return player.getNumericAttribute("slayer_points").intValue();
    }

    public com.near_reality.game.content.slayer.Assignment getTzTokJadAssignment(final com.near_reality.game.content.slayer.SlayerMaster master) {
        final int amount = player.getCombatAchievements().hasTierCompleted(CATierType.GRANDMASTER) ? 3 : 1;
        return new com.near_reality.game.content.slayer.Assignment(player, this, com.near_reality.game.content.slayer.RegularTask.TZTOK_JAD, com.near_reality.game.content.slayer.RegularTask.TZTOK_JAD.getEnumName(), amount, amount, master);
    }

    public com.near_reality.game.content.slayer.Assignment getTzKalZukAssignment(final com.near_reality.game.content.slayer.SlayerMaster master) {
        final int amount = player.getCombatAchievements().hasTierCompleted(CATierType.GRANDMASTER) ? 3 : 1;
        return new com.near_reality.game.content.slayer.Assignment(player, this, com.near_reality.game.content.slayer.RegularTask.TZKAL_ZUK, com.near_reality.game.content.slayer.RegularTask.TZKAL_ZUK.getEnumName(), amount, amount, master);
    }

    /**
     * Copies the persisted state of another slayer into this one. Used by the
     * legacy load path above and by SlayerKeys' attr rehydration. Mirrors the
     * legacy setFields initialize exactly: the banned-task map is adopted by
     * reference, assignments are rebuilt and re-parented, and
     * lastAssignmentName is deliberately NOT copied (the legacy load path
     * never copied it either — do not "fix" silently).
     */
    public void copyFrom(final Slayer other) {
        bannedTasks = other.bannedTasks;
        master = other.master;
        if (other.assignment != null) {
            assignment = new com.near_reality.game.content.slayer.Assignment();
            assignment.initialize(player, other.assignment);
        }
        if (other.storedAssignment != null) {
            storedAssignment = new com.near_reality.game.content.slayer.Assignment();
            storedAssignment.initialize(player, other.storedAssignment);
        }
    }

    public boolean isAssignable(final SlayerTask task, final com.near_reality.game.content.slayer.SlayerMaster master) {
        final Skills skills = player.getSkills();
        if (skills.getLevel(SkillConstants.SLAYER) < master.getSlayerRequirement() || skills.getCombatLevel() < master.getCombatRequirement()) {
            return false;
        }
        final Predicate<Player> predicate = task.getPredicate();
        return predicate.test(player);
    }

    public boolean isCheckingCombat() {
        return player.getBooleanAttribute("checking combat in slayer");
    }

    public void setCheckingCombat(final boolean value) {
        player.addAttribute("checking combat in slayer", value ? 1 : 0);
    }

    public boolean sumonaAssignWildernessTasks() {
        return player.getBooleanAttribute("sumona wildy tasks");
    }

    public void setSumonaAssignWildernessTasks(final boolean value) {
        player.addAttribute("sumona wildy tasks", value ? 1 : 0);
    }

    public boolean isCurrentAssignment(final Entity target) {
        if (assignment == null || assignment.getAmount() == 0 || !(target instanceof NPC)) {
            return false;
        }
        return assignment.isValid(player, (NPC) target);
    }

    public boolean isUnlocked(final int index) {
        return ((getUnlocksHash() >> index) & 1) == 1;
    }

    public boolean isUnlocked(final String name) {
        return isUnlocked(Enums.SLAYER_PERK_REWARD_NAMES.getKey(name.toLowerCase()).orElseThrow(Enums.exception()));
    }

    public void openInterface() {
        // check if we require a PIN to be "Unlocked"
        if (player.getBankPin().requiresVerification(player, this::openInterface)) return;

        GameInterface.SLAYER_REWARDS.open(player);
    }

    public void refreshPartnerInterface() {
        final PacketDispatcher dispatch = player.getPacketDispatcher();
        if (partner == null) {
            dispatch.sendClientScript(746, "New partner");
            dispatch.sendComponentText(68, 4, "Current partner: <col=ff0000>(none)</col>");
            dispatch.sendComponentText(68, 5, "Use the button to set yourself a Slayer Partner.<br><br>If your " +
                    "partner's Slayer level is <col=ffffff>as high as yours</col>, whenever a task is " +
                    "<col=ffffff>assigned to them</col>, you'll receive the same task, as long as you are eligible " +
                    "for it.<br><br>If your Slayer level is <col=ffffff>as high as your partner's</col>, whenever a " +
                    "task is <col=ffffff>assigned to you</col>, they'll receive the same task, as long as they are " +
                    "eligible for it.");
        } else {
            dispatch.sendClientScript(746, "Dismiss partner");
            dispatch.sendComponentText(68, 4, "Current partner: <col=ffffff>" + partner.getName() + "</col> (" + (partner.getSkills().getLevelForXp(SkillConstants.SLAYER) + ")"));
            final int playerLevel = player.getSkills().getLevelForXp(SkillConstants.SLAYER);
            final int partnerLevel = partner.getSkills().getLevelForXp(SkillConstants.SLAYER);
            final int difference = Integer.compare(playerLevel, partnerLevel);
            switch (difference) {
                case -1:
                    dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>lower</col> than your partner's" +
                            ".<br><br>When a new task is assigned <col=ffffff>to your partner</col>, if you " +
                            "are<br>eligible for it, and have not blocked it, you will receive<br>it too.<br><br>Your " +
                            "partner will not receive copies of tasks that are<br>assigned <col=ffffff>to you</col> " +
                            "because their Slayer level is higher.");
                    return;
                case 0:
                    dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>the same</col> as your partner's" +
                            ".<br><br>When a new task is assigned <col=ffffff>to either of you</col>, if you are both " +
                            "eligible for it, and have not blocked it, you will both receive it.");
                    return;
                case 1:
                    dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>higher</col> than your partner's" +
                            ".<br><br>When a new task is assigned <col=ffffff>to you</col>, if your partner " +
                            "is<br>eligible for it, and has not blocked it, you will receive<br>it too.<br><br>You will " +
                            "not receive copies of tasks that are assigned <col=ffffff>to your partner</col> because your" +
                            " Slayer level is higher.");
            }
        }
    }

    public void sendTaskInformation() {
        if (assignment == null) {
            player.sendMessage("You need something new to hunt.");
            return;
        }
        final Class<? extends RegionArea> clazz = assignment.getArea();
        RegionArea area = null;
        if (clazz != null) {
            area = GlobalAreaManager.getArea(clazz);
        }
        if (assignment.getMaster().equals(com.near_reality.game.content.slayer.SlayerMaster.KONAR_QUO_MATEN) && area != null) {
            if (area.equals(GlobalAreaManager.getArea(Keldagrim.class))) {
                player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + " in " + area.name() + "; only " + assignment.getAmount() + " more to go.");
            } else {
                player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + " in the " + area.name() + "; only " + assignment.getAmount() + " more to go.");
            }
        } else {
            player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + "; only " + assignment.getAmount() + " more to go.");
        }
    }

    public void setSlayerPoints(final int amount, final boolean refresh) {
        player.addAttribute("slayer_points", amount);
        if (refresh) {
            refreshSlayerPoints();
        }
    }

    public com.near_reality.game.content.slayer.Assignment generateSpecificBossTask(final com.near_reality.game.content.slayer.SlayerMaster master, BossTask task) {
        return new com.near_reality.game.content.slayer.Assignment(player, this, task, task.getEnumName(), 0, 0, master);
    }



    private IntAVLTreeSet getAvailableBlockSlots() {
        final IntAVLTreeSet list = new IntAVLTreeSet();
        final int questPoints = player.getQuestPoints();
        for (int i = 0; i < 5; i++) {
            if (questPoints >= ((i + 1) * 50)) {
                list.add(i);
                continue;
            }
            break;
        }
        if (player.getVarManager().getBitValue(LUMBRIDGE_ELITE_DIARY_COMPLETED_BIT) == 1) {
            list.add(5);
        }
        list.removeAll(bannedTasks.keySet());
        return list;
    }

    private int getRemainingExtensionsCost() {
        int cost = 0;
        for (final Integer value : Enums.TASK_EXTENSION_ENUM.getValues().values()) {
            if (isUnlocked(value)) {
                continue;
            }
            cost += Enums.TASK_COST_ENUM.getValue(value).orElseThrow(Enums.exception());
        }
        return (int) (cost * 0.95F);
    }

    private long getUnlocksHash() {
        return player.getNumericAttribute("slayer_unlocked_settings_hash").longValue();
    }

    private void setUnlocksHash(final long value) {
        player.addAttribute("slayer_unlocked_settings_hash", value);
    }

    void refreshBlockedTasks() {
        final VarManager varManager = player.getVarManager();
        for (int i = 0; i < BANNED_SLOT_VARBITS.length; i++) {
            final com.near_reality.game.content.slayer.RegularTask task = bannedTasks.get(i);
            varManager.sendBit(BANNED_SLOT_VARBITS[i], task == null ? 0 : task.getTaskId());
        }
    }

    void refreshCurrentAssignment() {
        final VarManager varManager = player.getVarManager();
        if (assignment == null) {
            varManager.sendVar(TASK_AMOUNT_VAR, 0);
            varManager.sendVar(TASK_INDEX_VAR, 0);
        } else {
            varManager.sendVar(TASK_AMOUNT_VAR, assignment.getAmount());
            varManager.sendVar(TASK_INDEX_VAR, assignment.getTask().getTaskId());
        }

        if (storedAssignment == null) {
            varManager.sendVar(STORED_TASK_AMOUNT_VAR, 0);
            varManager.sendVar(STORED_TASK_INDEX_VAR, 0);
        } else {
            varManager.sendVar(STORED_TASK_AMOUNT_VAR, storedAssignment.getAmount());
            varManager.sendVar(STORED_TASK_INDEX_VAR, storedAssignment.getTask().getTaskId());
        }
    }

    void refreshAssignmentForRewards() {
        final VarManager varManager = player.getVarManager();
        if (assignment == null) {
            varManager.sendVar(261, 0);
            varManager.sendVar(262, 0);
        } else {
            varManager.sendVar(261, assignment.getAmount());
            varManager.sendVar(262, assignment.getTask().getTaskId());
        }

        if (storedAssignment == null) {
            varManager.sendVar(STORED_TASK_AMOUNT_VAR, 0);
            varManager.sendVar(STORED_TASK_INDEX_VAR, 0);
        } else {
            varManager.sendVar(STORED_TASK_AMOUNT_VAR, storedAssignment.getAmount());
            varManager.sendVar(STORED_TASK_INDEX_VAR, storedAssignment.getTask().getTaskId());
        }
    }

    void refreshRewards() {
        final VarManager varManager = player.getVarManager();
        final long hash = getUnlocksHash();
        varManager.sendVar(UNLOCK_REWARDS_FIRST_VARP, (int) (hash & 4294967295L));
        varManager.sendVar(UNLOCK_REWARDS_SECOND_VARP, (int) ((hash >> 32) & 4294967295L));
    }

    public void refreshSlayerPoints() {
        player.getVarManager().sendBit(SLAYER_POINTS_BIT, getSlayerPoints());
    }

    public com.near_reality.game.content.slayer.Assignment getAssignment() {
        return assignment;
    }

    public com.near_reality.game.content.slayer.SlayerMaster getMaster() {
        return master;
    }

    public void setMaster(com.near_reality.game.content.slayer.SlayerMaster master) {
        this.master = master;
    }

    public Player getPartner() {
        return partner;
    }

    public void setPartner(Player partner) {
        this.partner = partner;
    }

    public String getLastAssignmentName() {
        return lastAssignmentName;
    }

    public com.near_reality.game.content.slayer.Assignment getAssignment(@NotNull com.near_reality.game.content.slayer.RegularTask task) {
        return SlayerHelper.INSTANCE.getAssignment(player, task);
    }

    public Assignment getAssignment(@NotNull RegularTask task, SlayerMaster master) {
        return SlayerHelper.INSTANCE.getAssignment(player, task, master);
    }
}
