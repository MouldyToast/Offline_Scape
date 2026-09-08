package com.near_reality.game.content.slayer

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import mgi.utilities.StringFormatUtil
import java.util.*
import java.util.function.Predicate

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
enum class BossTask(
    val xp: Float,
    override val predicate: Predicate<Player>,
    override val taskName: String,
    override val monsters: Set<String>,
    val isAssignableByKrystilia: Boolean,
    val wilderness: Boolean,
    val isAssignableBySumonaOnly: Boolean
) :
    SlayerTask {
    KREE_ARRA(
        buildBossTask {
            name("Kree'arra")
            staticXp(357.0f)
            require { it.skills.getLevelForXp(SkillConstants.RANGED) >= 70 }
        }
    ),
    KRIL_TSUTSAROTH(
        buildBossTask {
            name( "K'ril Tsutsaroth")
            staticXp(350.5f)
            require { it.skills.getLevelForXp(SkillConstants.HITPOINTS) >= 70 }
        }
    ),
    COMMANDER_ZILYANA(
        buildBossTask {
            name("Commander Zilyana")
            staticXp(350.0f)
            require { it.skills.getLevelForXp(SkillConstants.AGILITY) >= 70 }
        }
    ),
    GENERAL_GRAARDOR(
        buildBossTask {
            name("General Graardor")
            staticXp(338.0f)
            require { it.skills.getLevelForXp(SkillConstants.STRENGTH) >= 70 }
        }
    ),
    DAGANNOTH_KINGS(
        buildBossTask {
            name("Dagannoth Kings")
            staticXp(331.5f)
            validate { name, _ ->
                name.equals("Dagannoth Rex",    ignoreCase = true) ||
                name.equals("Dagannoth Prime",  ignoreCase = true) ||
                name.equals("Dagannoth Supreme",ignoreCase = true)
            }
            monsters("Dagannoth Rex", "Dagannoth Prime", "Dagannoth Supreme")
            dynamicXp { npc ->
                if (npc.definitions.name.equals("Dagannoth Supreme", ignoreCase = true))
                    255f
                else
                    331.5f
            }
        }
    ),
    KING_BLACK_DRAGON(
        buildBossTask {
            name("King Black Dragon")
            staticXp(258f)
        }
    ),
    VORKATH(
        buildBossTask {
            name("Vorkath")
            staticXp(750f)
        }
    ),
    VETION(
        buildBossTask {
            name("Vet'ion")
            staticXp(312f)
            wilderness(true)
            krystilia(true)
            monsters("Vet'ion", "Calvar'ion")
        }
    ),
    CRAZY_ARCHEAOLOGIST(
        buildBossTask {
            name("Crazy archaeologist")
            staticXp(275f)
            wilderness(true)
            krystilia(true)
        }
    ),
    ZULRAH(
        buildBossTask {
            name("Zulrah")
            staticXp(500f)
        }
    ),
    CERBERUS(
        buildBossTask {
            name("Cerberus")
            staticXp(690f)
            require { p -> p.skills.getLevelForXp(SkillConstants.SLAYER) >= 91 }
        }
    ),
    GIANT_MOLE(
        buildBossTask {
            name("Giant Mole")
            staticXp(215f)
        }
    ),
    CALLISTO(
        buildBossTask {
            name("Callisto")
            staticXp(312f)
            wilderness(true)
            krystilia(true)
            monsters("Artio", "Callisto")
        }
    ),
    CHAOS_ELEMENTAL(
        buildBossTask {
            name("Chaos elemental")
            staticXp(250f)
            wilderness(true)
            krystilia(true)
        }
    ),
    SCORPIA(
        buildBossTask {
            name("Scorpia")
            staticXp(260f)
            wilderness(true)
            krystilia(true)
        }
    ),
    KRAKEN(
        buildBossTask {
            name("Kraken")
            staticXp(255f)
            require { p -> p.skills.getLevelForXp(SkillConstants.SLAYER) >= 87 }
        }
    ),
    ABYSSAL_SIRE(
        buildBossTask {
            name("Abyssal sire")
            staticXp(450f)
            require { p -> p.skills.getLevelForXp(SkillConstants.SLAYER) >= 85 }
        }
    ),
    KALPHITE_QUEEN(
        buildBossTask {
            name("Kalphite Queen")
            staticXp(535.5f)
        }
    ),
    PHANTOM_MUSPAH(
        buildBossTask {
            name("Phantom Muspah")
            staticXp(1763.7f)
        }
    ),
    VENENATIS(
        buildBossTask {
            name("Venenatis")
            staticXp(388.8f)
            wilderness(true)
            krystilia(true)
            monsters("Venenatis", "Spindel")
        }
    ),
    CHAOS_FANATIC(
        buildBossTask {
            name("Chaos Fanatic")
            staticXp(253f)
            wilderness(true)
            krystilia(true)
        }
    ),
    THERMONUCLEAR_SMOKE_DEVIL(
        buildBossTask {
            name("Thermonuclear smoke devil")
            staticXp(240f)
            require { p -> p.skills.getLevelForXp(SkillConstants.SLAYER) >= 93 }
        }
    ),
    GROTESQUE_GUARDIANS(
        buildBossTask {
            name("Grotesque Guardians")
            staticXp(1350f)
            require { it.skills.getLevelForXp(SkillConstants.SLAYER) >= 75 && it.getNumericAttribute("brittle-entrance_unlocked").toInt() == 1 }
            validate { name, _ -> name.equals("Dusk", ignoreCase = true) }
        }
    ),
    BARROWS(
        buildBossTask {
            name("Barrows Brothers")
            staticXp(255f)
            validate { name, _ ->
                setOf(
                    "Ahrim the Blighted",
                    "Dharok the Wretched",
                    "Guthan the Infested",
                    "Karil the Tainted",
                    "Torag the Corrupted",
                    "Verac the Defiled"
                ).any { it.equals(name, ignoreCase = true) }
            }
            monsters(
                "Ahrim the Blighted",
                "Dharok the Wretched",
                "Guthan the Infested",
                "Karil the Tainted",
                "Torag the Corrupted",
                "Verac the Defiled"
            )
        }
    ),
    SKOTIZO(
        buildBossTask {
            name("Skotizo")
            staticXp(618.5f)
            require { _ -> false }
        }
    ),
    TZKAL_ZUK(
        buildBossTask {
            name("TzKal-Zuk")
            staticXp(25000f)
            require { _ -> false }
        }
    ),
    TZTOK_JAD(
        buildBossTask {
            name("TzTok-Jad")
            staticXp(10000f)
            require { _ -> false }
        }
    ),
    ARAXXOR(
        buildBossTask {
            name("Araxxor")
            staticXp(1708f)
            require { p -> p.skills.getLevelForXp(SkillConstants.SLAYER) >= 92 }
        }
    ),
    CORPOREAL_BEAST(
        buildBossTask {
            name("Corporeal beast")
            staticXp(700f)
            sumonaOnly(true)
        }
    ),
    ALCHEMICAL_HYDRA(
        buildBossTask {
            name("Alchemical hydra")
            staticXp(1320f)
            sumonaOnly(true)
        }
    ),
    GAUNTLET_REG(
        buildBossTask {
            name("Crystalline Hunllef")
            staticXp(600f)
            sumonaOnly(true)
        }
    ),
    GAUNTLET_CORRUPT(
        buildBossTask {
            name("Corrupted Hunllef")
            staticXp(800f)
            sumonaOnly(true)
        }
    ),
    JAD(
        buildBossTask {
            name("TzTok-Jad")
            staticXp(1000f)
            sumonaOnly(true)
        }
    ),
    INFERNO(
        buildBossTask {
            name("TzKal-Zuk")
            staticXp(5000f)
            sumonaOnly(true)
        }
    ),
    SARACHNIS(
        buildBossTask {
            name("Sarachnis")
            staticXp(200f)
            sumonaOnly(true)
        }
    ),
    NIGHTMARE(
        buildBossTask {
            name("The Nightmare")
            staticXp(1000f)
            sumonaOnly(true)
        }
    ),
    NIGHTMARE_PHOSANIS(
        buildBossTask {
            name("Phosani's Nightmare")
            staticXp(700f)
            sumonaOnly(true)
        }
    ),
    NEX(
        buildBossTask {
            name("Nex")
            staticXp(1300f)
            sumonaOnly(true)
        }
    ),
    OLM(
        buildBossTask {
            name("Great Olm")
            staticXp(2500f)
            sumonaOnly(true)
        }
    ),
    TOB(
        buildBossTask {
            name("Verzik Vitur")
            staticXp(2500f)
            sumonaOnly(true)
        }
    );

    constructor(info: BossTaskInfo) : this(info.xp, info.predicate, info.taskName, info.monsters, info.assignableByKrystilia, info.isWildernessTask, info.assignableBySumonaOnly)

    var validateBlock: ((String, NPC) -> Boolean)? = null
    var experienceBlock: ((NPC) -> Float)? = null

    override val enumName = name
    override val taskId: Int = 98
    override val monsterIds: Set<Int> = emptySet()
    override val slayerRequirement: Int = 0
    override val tip: String = "Not available."

    override fun toString(): String = StringFormatUtil.formatString(enumName)
    override fun validate(name: String, npc: NPC): Boolean {
        if(monsters.any{ it.lowercase().equals(name, true) }) return true
        return validateBlock?.invoke(name, npc) ?: name.equals(taskName, ignoreCase = true)
    }

    override fun getExperience(npc: NPC): Float = experienceBlock?.invoke(npc) ?: xp

    companion object {
        @JvmField
        val VALUES: Array<BossTask> = entries.toTypedArray()
        val MAPPED_VALUES: MutableMap<String, BossTask> = HashMap(VALUES.size)

        init {
            for (value in VALUES) {
                MAPPED_VALUES[value.enumName.lowercase(Locale.getDefault())] =
                    value
            }
        }
    }
}
