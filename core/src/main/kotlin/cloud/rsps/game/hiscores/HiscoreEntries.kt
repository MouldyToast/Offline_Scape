package cloud.rsps.game.hiscores

import com.zenyte.game.content.treasuretrails.ClueLevel
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
import net.runelite.api.gameval.SpriteID.IconActivities25x25
import net.runelite.api.gameval.SpriteID.IconBoss25x25
import net.runelite.api.gameval.SpriteID.Staticons
import net.runelite.api.gameval.SpriteID.Staticons2

/**
 * @author Jire
 */
object HiscoreEntries {

    @JvmStatic
    val skills: Byte2ObjectMap<HiscoreSkillEntry> =
        Byte2ObjectOpenHashMap()

    @JvmStatic
    val activities: Byte2ObjectMap<HiscoreActivityEntry> =
        Byte2ObjectOpenHashMap()

    @JvmStatic
    inline fun activity(
        id: Int,
        name: String,
        spriteId: Int = -1,
        crossinline getScore: Player.() -> Long = { -1L }
    ) {
        activities.put(id.toByte(), object : HiscoreActivityEntry(id, name, spriteId) {
            override fun getScore(player: Player): Long =
                getScore(player)
        })
    }

    @JvmStatic
    @JvmOverloads
    @JvmName("addSkillEntry")
    fun skill(
        id: Int,
        name: String,
        spriteId: Int = -1,
        skillId: Int
    ) {
        skills.put(id.toByte(), object : HiscoreSkillEntry(id, name, spriteId) {
            override fun getLevel(player: Player): Short =
                player.skills.getLevelForXp(skillId).toShort()

            override fun getXp(player: Player): Int =
                player.skills.getExperience(skillId).toInt()
        })
    }

    @JvmStatic
    @JvmOverloads
    @JvmName("addBossEntry")
    fun boss(id: Int, name: String, spriteId: Int = -1, bossName: String = name) {
        activity(id, name, spriteId) {
            notificationSettings.getKillcount(this, bossName).toLong()
        }
    }

    @JvmStatic
    fun initOverall() {
        val id = 0
        skills.put(id.toByte(), object : HiscoreSkillEntry(id, "Overall", Staticons2.TOTAL) {
            override fun getLevel(player: Player): Short =
                player.skills.totalLevel.toShort()

            override fun getXp(player: Player): Int =
                player.skills.totalXp
        })
    }

    @JvmStatic
    fun initSkills() {
        skill(1, "Attack", Staticons.ATTACK, SkillConstants.ATTACK)
        skill(2, "Defence", Staticons.DEFENCE, SkillConstants.DEFENCE)
        skill(3, "Strength", Staticons.STRENGTH, SkillConstants.STRENGTH)
        skill(4, "Hitpoints", Staticons.HITPOINTS, SkillConstants.HITPOINTS)
        skill(5, "Ranged", Staticons.RANGED, SkillConstants.RANGED)
        skill(6, "Prayer", Staticons.PRAYER, SkillConstants.PRAYER)
        skill(7, "Magic", Staticons.MAGIC, SkillConstants.MAGIC)
        skill(8, "Cooking", Staticons.COOKING, SkillConstants.COOKING)
        skill(9, "Woodcutting", Staticons.WOODCUTTING, SkillConstants.WOODCUTTING)
        skill(10, "Fletching", Staticons.FLETCHING, SkillConstants.FLETCHING)
        skill(11, "Fishing", Staticons.FISHING, SkillConstants.FISHING)
        skill(12, "Firemaking", Staticons.FIREMAKING, SkillConstants.FIREMAKING)
        skill(13, "Crafting", Staticons.CRAFTING, SkillConstants.CRAFTING)
        skill(14, "Smithing", Staticons.SMITHING, SkillConstants.SMITHING)
        skill(15, "Mining", Staticons.MINING, SkillConstants.MINING)
        skill(16, "Herblore", Staticons.HERBLORE, SkillConstants.HERBLORE)
        skill(17, "Agility", Staticons.AGILITY, SkillConstants.AGILITY)
        skill(18, "Thieving", Staticons.THIEVING, SkillConstants.THIEVING)
        skill(19, "Slayer", Staticons2.SLAYER, SkillConstants.SLAYER)
        skill(20, "Farming", Staticons2.FARMING, SkillConstants.FARMING)
        skill(21, "Runecraft", Staticons2.RUNECRAFT, SkillConstants.RUNECRAFTING)
        skill(22, "Hunter", Staticons2.HUNTER, SkillConstants.HUNTER)
        skill(23, "Construction", Staticons2.CONSTRUCTION, SkillConstants.CONSTRUCTION)
    }

    @JvmStatic
    fun initClueScrolls() {
        activity(6, "Clue Scrolls (all)", spriteId = IconActivities25x25.CLUE_SCROLL_ALL) {
            var count = 0L
            for (level in ClueLevel.entries) {
                count += getNumericAttribute("completed ${level.name.lowercase()} treasure trails").toInt()
            }
            count
        }
        activity(7, "Clue Scrolls (beginner)") {
            getNumericAttribute("completed beginner treasure trails").toLong()
        }
        activity(8, "Clue Scrolls (easy)") {
            getNumericAttribute("completed easy treasure trails").toLong()
        }
        activity(9, "Clue Scrolls (medium)") {
            getNumericAttribute("completed medium treasure trails").toLong()
        }
        activity(10, "Clue Scrolls (hard)") {
            getNumericAttribute("completed hard treasure trails").toLong()
        }
        activity(11, "Clue Scrolls (elite)") {
            getNumericAttribute("completed elite treasure trails").toLong()
        }
        activity(12, "Clue Scrolls (master)") {
            getNumericAttribute("completed master treasure trails").toLong()
        }
    }

    @JvmStatic
    fun initBosses() {
        boss(19, "Abyssal Sire", IconBoss25x25.ABYSSAL_SIRE)
        boss(20, "Alchemical Hydra", IconBoss25x25.ALCHEMICAL_HYDRA)
        boss(21, "Amoxliatl", IconBoss25x25.AMOXLIATL)
        boss(22, "Araxxor", IconBoss25x25.ARAXXOR)
        boss(23, "Artio", IconBoss25x25.ARTIO_CALLISTO)
        boss(24, "Barrows Chests", IconBoss25x25.BARROWS_CHESTS)
        boss(25, "Bryophyta", IconBoss25x25.BRYOPHYTA)
        boss(26, "Callisto", IconBoss25x25.ARTIO_CALLISTO)
        boss(27, "Calvar'ion", IconBoss25x25.CALVARION_VETION)
        boss(28, "Cerberus", IconBoss25x25.CERBERUS)
        activity(29, "Chambers of Xeric", spriteId = IconBoss25x25.CHAMBERS_OF_XERIC) {
            getNumericAttribute("chambersofxeric").toLong()
        }
        activity(30, "Chambers of Xeric: Challenge Mode", spriteId = IconBoss25x25.CHAMBERS_OF_XERIC_CHALLENGE_MODE) {
            getNumericAttribute("challengechambersofxeric").toLong()
        }
        boss(31, "Chaos Elemental", IconBoss25x25.CHAOS_ELEMENTAL)
        boss(32, "Chaos Fanatic", IconBoss25x25.CHAOS_FANATIC)
        boss(33, "Commander Zilyana", IconBoss25x25.COMMANDER_ZILYANA)
        boss(34, "Corporeal Beast", IconBoss25x25.CORPOREAL_BEAST)
        boss(35, "Crazy Archaeologist", IconBoss25x25.CRAZY_ARCHAEOLOGIST)
        boss(36, "Dagannoth Prime", IconBoss25x25.DAGANNOTH_PRIME)
        boss(37, "Dagannoth Rex", IconBoss25x25.DAGANNOTH_REX)
        boss(38, "Dagannoth Supreme", IconBoss25x25.DAGANNOTH_SUPREME)
        boss(39, "Deranged Archaeologist", IconBoss25x25.DERANGED_ARCHAEOLOGIST)
        boss(40, "Doom of Mokhaiotl"/*, IconBoss25x25.DOOM_OF_MOKHAIOTL*/)
        activity(41, "Duke Sucellus", spriteId = IconBoss25x25.DUKE_SUCELLUS) {
            notificationSettings.getKillcount(this, "duke sucellus").toLong() +
                    notificationSettings.getKillcount(this, "awakened duke sucellus").toLong()
        }
        boss(42, "General Graardor", IconBoss25x25.GENERAL_GRAARDOR)
        boss(43, "Giant Mole", IconBoss25x25.GIANT_MOLE)
        boss(44, "Grotesque Guardians", IconBoss25x25.GROTESQUE_GUARDIANS)
        boss(45, "Hespori", IconBoss25x25.HESPORI)
        boss(46, "Kalphite Queen", IconBoss25x25.KALPHITE_QUEEN)
        boss(47, "King Black Dragon", IconBoss25x25.KING_BLACK_DRAGON)
        boss(48, "Kraken", IconBoss25x25.KRAKEN)
        boss(49, "Kree'Arra", IconBoss25x25.KREEARRA)
        boss(50, "K'ril Tsutsaroth", IconBoss25x25.KRIL_TSUTSAROTH)
        boss(51, "Lunar Chests", IconBoss25x25.LUNAR_CHESTS)
        boss(52, "Mimic", IconBoss25x25.MIMIC)
        boss(53, "Nex", IconBoss25x25.NEX)
        boss(54, "Nightmare", IconBoss25x25.NIGHTMARE, "The Nightmare")
        boss(55, "Phosani's Nightmare", IconBoss25x25.NIGHTMARE)
        boss(56, "Obor", IconBoss25x25.OBOR)
        boss(57, "Phantom Muspah", IconBoss25x25.PHANTOM_MUSPAH)
        boss(58, "Sarachnis", IconBoss25x25.SARACHNIS)
        boss(59, "Scorpia", IconBoss25x25.SCORPIA)
        boss(60, "Scurrius", IconBoss25x25.SCURRIUS)
        boss(61, "Skotizo", IconBoss25x25.SKOTIZO)
        boss(62, "Sol Heredit", IconBoss25x25.SOL_HEREDIT)
        boss(63, "Spindel", IconBoss25x25.SPINDEL_VENENATIS)
        boss(64, "Tempoross", IconBoss25x25.TEMPOROSS)
        activity(65, "The Gauntlet", spriteId = IconBoss25x25.THE_GAUNTLET) {
            getNumericAttribute("gauntlet_completions").toLong()
        }
        activity(66, "The Corrupted Gauntlet", spriteId = IconBoss25x25.THE_CORRUPTED_GAUNTLET) {
            getNumericAttribute("corrupted_gauntlet_completions").toLong()
        }
        boss(67, "The Hueycoatl", IconBoss25x25.THE_HUEYCOATL)
        activity(68, "The Leviathan", spriteId = IconBoss25x25.THE_LEVIATHAN) {
            notificationSettings.getKillcount(this, "leviathan").toLong() +
                    notificationSettings.getKillcount(this, "awakened leviathan").toLong()
        }
        boss(69, "The Royal Titans", IconBoss25x25.ROYAL_TITANS)
        activity(70, "The Whisperer", spriteId = IconBoss25x25.THE_WHISPERER) {
            notificationSettings.getKillcount(this, "whisperer").toLong() +
                    notificationSettings.getKillcount(this, "awakened whisperer").toLong()
        }
        boss(
            73,
            "Thermonuclear Smoke Devil",
            IconBoss25x25.THERMONUCLEAR_SMOKE_DEVIL,
            "Thermonuclear smoke devil"
        )
        boss(
            74,
            "Tombs of Amascut",
            IconBoss25x25.TOMBS_OF_AMASCUT,
            "tombs of amascut: normal mode"
        )
        boss(
            75,
            "Tombs of Amascut: Expert Mode",
            IconBoss25x25.TOMBS_OF_AMASCUT_EXPERT,
            "tombs of amascut: expert mode"
        )
        boss(76, "TzKal-Zuk", IconBoss25x25.TZKAL_ZUK, "Tzkal-zuk")
        boss(77, "TzTok-Jad", IconBoss25x25.TZTOK_JAD, "Tztok-jad")
        activity(78, "Vardorvis", spriteId = IconBoss25x25.VARDORVIS) {
            notificationSettings.getKillcount(this, "vardorvis").toLong() +
                    notificationSettings.getKillcount(this, "awakened vardorvis").toLong()
        }
        boss(79, "Venenatis", IconBoss25x25.SPINDEL_VENENATIS)
        boss(80, "Vet'ion", IconBoss25x25.CALVARION_VETION, "Vet'Ion")
        boss(81, "Vorkath", IconBoss25x25.VORKATH)
        boss(82, "Wintertodt", IconBoss25x25.WINTERTODT)
        boss(83, "Yama", IconBoss25x25.YAMA)
        boss(84, "Zalcano", IconBoss25x25.ZALCANO)
        boss(85, "Zulrah", IconBoss25x25.ZULRAH)
    }

    @JvmStatic
    fun init() {
        initOverall()
        initSkills()
        activity(0, "League Points", spriteId = IconActivities25x25.LEAGUE_POINTS)
        activity(1, "Deadman Points", spriteId = IconActivities25x25.DEADMAN_POINTS)
        activity(2, "Bounty Hunter - Hunter", spriteId = IconActivities25x25.BOUNTY_HUNTER_HUNTER)
        activity(3, "Bounty Hunter - Rogue", spriteId = IconActivities25x25.BOUNTY_HUNTER_ROGUE)
        activity(4, "Bounty Hunter (Legacy) - Hunter", spriteId = IconActivities25x25.BOUNTY_HUNTER_HUNTER)
        activity(5, "Bounty Hunter (Legacy) - Rogue", spriteId = IconActivities25x25.BOUNTY_HUNTER_ROGUE)
        initClueScrolls()
        activity(13, "LMS - Rank", spriteId = IconActivities25x25.LAST_MAN_STANDING)
        activity(14, "PvP Arena - Rank", spriteId = IconActivities25x25.PVP_ARENA_RANK)
        activity(15, "Soul Wars Zeal", spriteId = IconActivities25x25.SOUL_WARS_ZEAL)
        activity(16, "Rifts closed", spriteId = IconActivities25x25.RIFTS_CLOSED)
        activity(17, "Colosseum Glory", spriteId = IconActivities25x25.COLOSSEUM_GLORY)
        activity(18, "Collections Logged", spriteId = IconActivities25x25.COLLECTIONS_LOGGED)
        initBosses()
    }

}
