package com.near_reality.game.content.slayer

import com.near_reality.game.content.area.IorwerthDungeon
import com.zenyte.game.GameConstants
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra
import com.zenyte.game.content.kebos.konar.map.KaruulmSlayerDungeon
import com.zenyte.game.content.minigame.fightcaves.FightCaves
import com.zenyte.game.content.minigame.fightcaves.npcs.TzTokJad
import com.zenyte.game.content.minigame.inferno.instance.Inferno
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.TzKalZuk
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Setting
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.area.*
import com.zenyte.game.world.region.area.godwars.GodwarsDungeonArea
import com.zenyte.game.world.region.area.kourend.*
import com.zenyte.game.world.region.area.taskonlyareas.KalphiteCave
import com.zenyte.game.world.region.area.taskonlyareas.KrakenCove
import com.zenyte.game.world.region.area.taskonlyareas.StrongholdSlayerDungeon
import mgi.utilities.StringFormatUtil
import java.util.*
import java.util.function.Predicate

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
enum class RegularTask(
    val taskSet: Array<Task>,
    override val taskId: Int,
    override val slayerRequirement: Int,
    val combatRequirement: Int,
    override val tip: String,
    override val predicate: Predicate<Player>,
    val extendedRange: Range?,
    val wildernessLevel: Int = 0,
    private val monsterData: SlayerMonsterInfo
) : SlayerTask {
    ARAXYTES(
        buildTasks {
            SlayerMaster.NIEVE weight 8 min 40 max 60
            SlayerMaster.DURADEL weight 10 min 60 max 80
        },
        buildInfo {
            id(124)
            slayer(92)
            combat(0)
            tip("Araxytes are a rare species of arachnid found exclusively in the Morytania Spider Cave.")
            extension(Range.of("More eyes than sense", 200, 250))
            registerNames("araxxor", "araxyte", "dreadborn araxyte")
        }
    ),
    BANSHEES(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 15 max 30
            SlayerMaster.MAZCHNA weight 8 min 30 max 50
        },
        buildInfo {
            id(38)
            slayer(15)
            combat(20)
            tip("You'll need something to protect yourself from their screams.")
            registerNames("Banshee", "Twisted Banshee", "Screaming banshee", "Screaming twisted banshee")
        }
    ),
    BATS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
            SlayerMaster.MAZCHNA weight 7 min 30 max 50
        },
        buildInfo {
            id(8)
            slayer(1)
            combat(5)
            tip("Bats are low level flappy monsters, commonly found within dark areas.")
            registerNames("Bat", "Giant bat", "Albino bat", "Deathwing")
        }
    ),
    BIRDS(
        buildTasks {
            SlayerMaster.TURAEL weight 6 min 15 max 30
        },
        buildInfo {
            id(5)
            slayer(1)
            combat(0)
            tip("Birds can be found all across " + GameConstants.SERVER_NAME + ". You should have no problems knocking them out.")
            registerNames(
                "Duck", "Chicken", "Undead Chicken", "Rooster", "Seagull",
                "Penguin", "Bird", "Chompy bird", "Vulture",
                "Terrorbird", "Mounted Terrorbird", "Mounted terrorbird gnome",
            )
        }
    ),
    BEARS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 10 max 20
            SlayerMaster.MAZCHNA weight 6 min 30 max 50
            SlayerMaster.KRYSTILIA weight 6 min 65 max 100
        },
        buildInfo {
            id(13)
            slayer(1)
            combat(13)
            wildernessLvl(22)
            tip("Bears are fierce monsters found within the forests of " + GameConstants.SERVER_NAME + ".")
            registerNames(
                "Black bear", "Bear cub", "Grizzly bear", "Grizzly bear cub", "Callisto",
                "Bear", "Reanimated bear", "Artio"
            )
        }
    ),
    CAVE_BUGS(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 10 max 30
            SlayerMaster.MAZCHNA weight 8 min 10 max 20
        },
        buildInfo {
            id(63)
            slayer(7)
            combat(0)
            tip("Cave bugs are found only in the darkest, moist caverns. Thou shall not forget a light source, or thou will be bit by bugs.")
            registerNames("Cave bug")
        }
    ),

    CAVE_CRAWLERS(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 15 max 30
            SlayerMaster.MAZCHNA weight 8 min 30 max 50
        },
        buildInfo {
            id(37)
            slayer(10)
            combat(10)
            tip("Cave crawlers are spiky poisonous critters. Slay them fast or they'll heal right back up.")
            registerNames("Cave crawler", "Chasm crawler")
        }
    ),
    CAVE_SLIMES(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 10 max 20
            SlayerMaster.MAZCHNA weight 8 min 10 max 20
        },
        buildInfo {
            id(62)
            slayer(17)
            combat(15)
            tip("Cave slime, a poisonous blob can be found in the darkest moist caverns. Beware of explosions!")
            registerNames("Cave slime")
        }
    ),
    COWS(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 15 max 30
        },
        buildInfo {
            id(6)
            slayer(1)
            combat(5)
            tip("Cows are commonly in large farms. You should have no problems defeating them.")
            registerNames("Cow", "Cow calf", "Undead cow", "Unicow", "Buffalo")
        }
    ),
    CRAWLING_HANDS(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 15 max 30
            SlayerMaster.MAZCHNA weight 8 min 30 max 50
        },
        buildInfo {
            id(39)
            slayer(5)
            combat(0)
            tip("Crawling hands can only be found in the Slayer tower.")
            registerNames("Crawling hand", "Crushing hand")
        }
    ),
    DESERT_LIZARDS(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 15 max 30
            SlayerMaster.MAZCHNA weight 8 min 30 max 50
        },
        buildInfo {
            id(68)
            slayer(22)
            combat(15)
            tip("Lizards can be found in the desert (ice coolers req.) or the swamp.")
            registerNames("Desert lizard", "Lizard", "Small lizard", "Sulphur Lizard", "Grimy Lizard")
        }
    ),
    DOGS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
            SlayerMaster.MAZCHNA weight 7 min 30 max 50
        },
        buildInfo {
            id(22)
            slayer(1)
            combat(15)
            tip("Dogs can be found all across " + GameConstants.SERVER_NAME + ". Don't take them lightly though, while they may be a man's best friend, they can become a fierce predators.")
            registerNames("Wild dog", "Guard dog", "Jackal", "Dog", "Reanimated dog")
        }
    ),
    DWARVES(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 10 max 25
        },
        buildInfo {
            id(57)
            slayer(1)
            combat(6)
            tip("Dwarves are characterised by their short stature. They can often be found mining rocks or drinking beer.")
            registerNames("Dwarf", "Dwarf gang member", "Chaos dwarf", "Black Guard")
        }
    ),
    GHOSTS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
            SlayerMaster.MAZCHNA weight 7 min 30 max 50
        },
        buildInfo {
            id(12)
            slayer(1)
            combat(13)
            tip("Ghosts can be found in all haunted, dark places. You should have no trouble defeating them.")
            registerNames("Ghost", "Tortured soul", "Forgotten Soul", "Revenants", "Death wing")
        }
    ),
    GOBLINS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
        },
        buildInfo {
            id(2)
            slayer(1)
            combat(0)
            tip("Goblins are mainly found in their village, however you may also find some clusters in other locations across " + GameConstants.SERVER_NAME + ".")
            registerNames(
                "Goblin", "Reanimated goblin", "Cave goblin guard", "Goblin Champion",
                "Sergeant strongstack", "Sergeant grimspike", "Sergeant steelwill"
            )
        }
    ),
    ICEFIENDS(
        buildTasks {
            SlayerMaster.TURAEL weight 8 min 15 max 20
        },
        buildInfo {
            id(75)
            slayer(1)
            combat(20)
            tip("Icefiends are small demons often found in icy areas. Their attacks shouldn't be underestimated, as deal damage in an odd fashion.")
            registerNames("Icefiend")
        }
    ),
    KALPHITE(
        buildTasks {
            SlayerMaster.TURAEL weight 6 min 15 max 30
            SlayerMaster.MAZCHNA weight 6 min 30 max 50
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.CHAELDAR weight 11 min 70 max 130
            SlayerMaster.NIEVE weight 9 min 120 max 185
            SlayerMaster.DURADEL weight 9 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 9 min 120 max 170 area KalphiteLair::class.java area KalphiteCave::class.java
        },
        buildInfo {
            id(53)
            slayer(1)
            combat(15)
            tip("Kalphite are large bugs found within the depths of the desert. You might want to defeat them with a Keris.")
            registerNames(
                "Kalphite worker",
                "Kalphite soldier",
                "Kalphite guardian",
                "Kalphite queen",
                "Reanimated kalphite"
            )
        }
    ),
    MINOTAURS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 10 max 20
        },
        buildInfo {
            id(76)
            slayer(1)
            combat(7)
            tip("Minotaurs are accurate, yet weak creatures. Their high accuracy however makes up for the lack of defence.")
            registerNames("Minotaur", "Reanimated minotaur")
        }
    ),

    MONKEYS(
        buildTasks {
            SlayerMaster.TURAEL weight 6 min 15 max 30
        },
        buildInfo {
            id(1)
            slayer(1)
            combat(0)
            tip("Monkeys are small creatures often found inhabiting leafy, thick jungles.")
            registerNames(
                "Monkey", "Reanimated monkey", "Monkey guard", "Monkey archer", "Zombie monkey",
                "Elder guard", "Sleeping monkey", "Tortured gorilla", "Maniacal monkey",
                "Maniacal monkey archer", "Demonic gorilla"
            )
        }
    ),

    RATS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
        },
        buildInfo {
            id(3)
            slayer(1)
            combat(0)
            tip("Rats are tiny animals found around cities, sewers and dungeons. You should have no problems crushing them.")
            registerNames(
                "Rat", "Giant rat", "Dungeon rat", "Crypt rat", "Brine rat",
                "Giant crypt rat", "Blessed giant rat", "Angry giant rat", "Scurrius"
            )
        }
    ),
    SCORPIONS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
            SlayerMaster.MAZCHNA weight 7 min 30 max 50
            SlayerMaster.KRYSTILIA weight 6 min 65 max 100
        },
        buildInfo {
            id(7)
            slayer(1)
            combat(7)
            wildernessLvl(43)
            tip("Scorpions are vicious monsters found all across " + GameConstants.SERVER_NAME + ".")
            registerNames(
                "Scorpion",
                "Reanimated scorpion",
                "King scorpion",
                "Poison scorpion",
                "Pit scorpion",
                "Scorpia",
                "Scorpia's offspring"
            )
        }
    ),
    SKELETONS(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
            SlayerMaster.MAZCHNA weight 6 min 30 max 50
            SlayerMaster.KRYSTILIA weight 5 min 65 max 100
        },
        buildInfo {
            id(11)
            slayer(1)
            combat(15)
            wildernessLvl(29)
            tip("Skeletons are undead monsters found all across " + GameConstants.SERVER_NAME + ", often inhabiting graveyards and dungeons.")
            registerNames(
                "Skeleton",
                "Skeleton mage",
                "Skeleton champion",
                "Calvar'ion",
                "Calvar'ion reborn",
                "Vet'ion",
                "Vet'ion reborn"
            )
        }
    ),
    SPIDERS(
        buildTasks {
            SlayerMaster.TURAEL weight 6 min 15 max 30
            SlayerMaster.KRYSTILIA weight 6 min 65 max 100
        },
        buildInfo {
            id(4)
            slayer(1)
            combat(0)
            wildernessLvl(40)
            tip("Spiders are usually small, eight legged creatures found roaming all across " + GameConstants.SERVER_NAME + ".")
            registerNames(
                "Spider", "Giant spider", "Shadow spider", "Giant crypt spider",
                "Venenatis", "Jungle spider", "Deadly red spider",
                "Blessed spider", "Crypt spider", "Poison spider", "Temple spider",
                "Sarachnis", "Spindel", "araxxor", "araxyte", "dreadborn araxyte"
            )
        }
    ),
    WOLVES(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 30
            SlayerMaster.MAZCHNA weight 7 min 30 max 50
        },
        buildInfo {
            id(9)
            slayer(1)
            combat(20)
            tip("Wolves are dog-like animals usually found in a herd.")
            registerNames("Wolf", "White wolf", "Big wolf", "Dire wolf", "Jungle wolf", "Desert wolf", "Ice wolf")
        }
    ),
    ZOMBIES(
        buildTasks {
            SlayerMaster.TURAEL weight 7 min 15 max 50
            SlayerMaster.MAZCHNA weight 7 min 40 max 70
            SlayerMaster.KRYSTILIA weight 3 min 75 max 125
        },
        buildInfo {
            id(10)
            slayer(1)
            combat(10)
            wildernessLvl(25)
            tip("Zombies are undead monsters often found in dungeons and sewers.")
            registerNames(
                "Zombie", "Summoned zombie", "Zombie pirate", "Zombie swab",
                "Zombies champion", "Zombie rat", "Monkey zombie",
                "Small zombie monkey", "Large zombie monkey",
                "Undead cow", "Undead chicken", "Armoured zombie", "Vorkath"
            )
        }
    ),

    CATABLEPON(
        buildTasks {
            SlayerMaster.MAZCHNA weight 8 min 20 max 30
        },
        buildInfo {
            id(78)
            slayer(1)
            combat(35)
            tip("Catablepons are cow-like creatures held within the stronghold of security, draining the strength of their enemies.")
            registerNames("Catablepon")
        }
    ),
    COCKATRICE(
        buildTasks {
            SlayerMaster.MAZCHNA weight 8 min 30 max 50
            SlayerMaster.VANNAKA weight 8 min 40 max 90
        },
        buildInfo {
            id(44)
            slayer(25)
            combat(25)
            tip("Cockatrice are winged reptiles with a piercing gaze. Don't forget a mirror shield before fighting them.")
            predicate { player -> player.skills.getLevelForXp(SkillConstants.DEFENCE) >= 20 }
            registerNames("Cockatrice", "Cockathrice", "Moonlight Cockatrice")
        }
    ),
    EARTH_WARRIORS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 6 min 75 max 125
        },
        buildInfo {
            id(54)
            slayer(1)
            combat(35)
            wildernessLvl(10)
            tip("Earth warriors are humanoid elementals of earth, found in a dark dirt-lined chamber in Edgeville.")
            registerNames("Earth warrior")
        }
    ),
    FLESH_CRAWLERS(
        buildTasks {
            SlayerMaster.MAZCHNA weight 7 min 15 max 25
        },
        buildInfo {
            id(77)
            slayer(1)
            combat(15)
            tip("Flesh crawlers are six-legged insects found in the Stronghold of security.")
            registerNames("Flesh crawler")
        }
    ),
    GHOULS(
        buildTasks {
            SlayerMaster.MAZCHNA weight 7 min 10 max 20
            SlayerMaster.VANNAKA weight 7 min 10 max 40
        },
        buildInfo {
            id(23)
            slayer(1)
            combat(25)
            tip("Ghouls are zombie-like creatures found in dark places.")
            registerNames("Ghoul", "Ghoul champion")
        }
    ),
    HILL_GIANTS(
        buildTasks {
            SlayerMaster.MAZCHNA weight 7 min 30 max 50
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.KRYSTILIA weight 3 min 75 max 125
        },
        buildInfo {
            id(14)
            slayer(1)
            combat(25)
            wildernessLvl(23)
            tip("Hill giants are large foes with high, but inaccurate attacks.")
            registerNames("Hill giant", "Giant champion", "Cyclops", "Obor")
        }
    ),
    HOBGOBLIN(
        buildTasks {
            SlayerMaster.MAZCHNA weight 7 min 30 max 50
            SlayerMaster.VANNAKA weight 7 min 40 max 90
        },
        buildInfo {
            id(21)
            slayer(1)
            combat(20)
            tip("Hobgoblins are smelly creatures with strong attacks.")
            registerNames("Hobgoblin", "Hobgoblin champion")
        }
    ),
    ICE_WARRIORS(
        buildTasks {
            SlayerMaster.MAZCHNA weight 7 min 40 max 50
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.KRYSTILIA weight 7 min 100 max 150
        },
        buildInfo {
            id(19)
            slayer(1)
            combat(45)
            tip("Ice warriors are cold-hearted elemental warriors, often found in icy caverns.")
            registerNames("Ice warrior", "Icelords")
        }
    ),
    MOGRES(
        buildTasks {
            SlayerMaster.MAZCHNA weight 6 min 30 max 50
            SlayerMaster.VANNAKA weight 7 min 40 max 90
        },
        buildInfo {
            id(67)
            slayer(32)
            combat(30)
            tip("Mogres are large foes often found inhabiting areas close to water.")
            registerNames("Mogre")
        }
    ),
    PYREFIEND(
        buildTasks {
            SlayerMaster.MAZCHNA weight 8 min 30 max 50
            SlayerMaster.VANNAKA weight 8 min 40 max 90
        },
        buildInfo {
            id(47)
            slayer(30)
            combat(25)
            tip("Pyrefiends are small demons of fire, striking magical attacks close-up.")
            registerNames("Pyrefiend", "Flaming pyrelord", "pyrelord", "infernal pyrelord")
        }
    ),
    ROCKSLUGS(
        buildTasks {
            SlayerMaster.MAZCHNA weight 8 min 30 max 50
        },
        buildInfo {
            id(51)
            slayer(20)
            combat(20)
            tip("Rockslugs are slimy small creatures who can only be defeated with the use of brine sabre or salt.")
            registerNames("Rockslug", "Giant rockslug")
        }
    ),
    SHADES(
        buildTasks {
            SlayerMaster.MAZCHNA weight 8 min 30 max 70
            SlayerMaster.VANNAKA weight 8 min 40 max 90
        },
        buildInfo {
            id(64)
            slayer(1)
            combat(30)
            tip("Shades are undead, shadowy remains of a long departed soul.")
            registerNames("Shade", "Loar shade", "Phrin shade", "Riyl shade", "Asyn shade", "Fiyr shade")
        }
    ),
    WALL_BEASTS(
        buildTasks {
            SlayerMaster.MAZCHNA weight 7 min 10 max 20
        },
        buildInfo {
            id(61)
            slayer(35)
            combat(30)
            tip("Wall beasts are found in the darkest of caverns around " + GameConstants.SERVER_NAME + ". Bring a spiny helmet to avoid its deathly grasp.")
            predicate { player -> player.skills.getLevelForXp(SkillConstants.DEFENCE) >= 5 }
            registerNames("Wall beast")
        }
    ),
    ABYSSAL_DEMONS(
        buildTasks {
            SlayerMaster.VANNAKA weight 5 min 40 max 90
            SlayerMaster.KRYSTILIA weight 5 min 75 max 125
            SlayerMaster.CHAELDAR weight 12 min 70 max 130
            SlayerMaster.NIEVE weight 9 min 120 max 185
            SlayerMaster.DURADEL weight 12 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 9 min 120 max 170 area CatacombsOfKourend::class.java area SlayerTower::class.java
        },
        buildInfo {
            id(42)
            slayer(85)
            combat(85)
            wildernessLvl(40)
            tip("Abyssal demons are demons from the abyss, possessing the ability to teleport itself as well as its target.")
            extension(Range.of("Augment my abbies", 200, 250))
            registerNames("Abyssal demon", "Abyssal sire", "Greater abyssal demon", "Reanimated abyssal")
        }
    ),
    ABERRANT_SPECTRES(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
            SlayerMaster.CHAELDAR weight 8 min 70 max 130
            SlayerMaster.NIEVE weight 6 min 120 max 185
            SlayerMaster.DURADEL weight 7 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 6 min 120 max 170 area CatacombsOfKourend::class.java area SlayerTower::class.java area StrongholdSlayerDungeon::class.java
        },
        buildInfo {
            id(41)
            slayer(60)
            combat(65)
            tip("Aberrant spectres are large smelly ghosts. It's suggested that you use a nose peg to avoid the foul smell.")
            extension(Range.of("Smell ya later", 200, 250))
            registerNames("Aberrant spectre", "Deviant spectre", "Abhorrent spectre", "Repugnant spectre")
        }
    ),
    ANKOU(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 25 max 35
            SlayerMaster.NIEVE weight 5 min 50 max 90
            SlayerMaster.DURADEL weight 5 min 50 max 80
            SlayerMaster.KRYSTILIA weight 6 min 75 max 125
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 50 max 50 area StrongholdOfSecurity::class.java area StrongholdSlayerDungeon::class.java area CatacombsOfKourend::class.java
        },
        buildInfo {
            id(79)
            slayer(1)
            combat(40)
            wildernessLvl(35)
            tip("Ankou are skeletal, ghostly undead humanoids, found in numerous locations across " + GameConstants.SERVER_NAME + ".")
            extension(Range.of("Ankou very much", 91, 150))
            registerNames("Ankou", "Dark Ankou")
        }
    ),
    BASILISKS(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
            SlayerMaster.CHAELDAR weight 7 min 110 max 170
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 110 max 170 area FremennikSlayerDungeon::class.java //TODO : Jormungand Prison - needs area defined first
            SlayerMaster.NIEVE weight 6 min 120 max 185
            SlayerMaster.DURADEL weight 7 min 130 max 200
        },
        buildInfo {
            id(43)
            slayer(40)
            combat(40)
            tip("Basilisks are deadly creatures with possessing the eyes of evil. A mirror shield is suggested when fighting them.")
            predicate { player -> player.skills.getLevelForXp(SkillConstants.DEFENCE) >= 20 && player.slayer.isUnlocked("Basilocked") }
            registerNames("Basilisk", "Monstrous basilisk", "Basilisk Knight", "basilisk sentinel")
            extension(Range.of("Basilonger", 200, 250))
        }
    ),
    BLUE_DRAGONS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.CHAELDAR weight 8 min 70 max 130
            SlayerMaster.NIEVE weight 4 min 120 max 185
            SlayerMaster.DURADEL weight 4 min 110 max 170
            SlayerMaster.KONAR_QUO_MATEN weight 4 min 120 max 170 area CatacombsOfKourend::class.java area TaverleyDungeon::class.java area MythsGuildBasement::class.java area IsleOfSoulsDungeon::class.java //TODO: Ruins of Tapoyauik & Isle of Souls
        },
        buildInfo {
            id(25)
            slayer(1)
            combat(65)
            tip("Blue dragons are one of the weakest chromatic dragons, but should not be underestimated as their breath can defeat even the strongest of opponents.")
            registerNames("Blue dragon", "Baby blue dragon", "Vorkath", "Brutal blue dragon", "Baby blue dragon")
            registerIds(241, 242, 243)
        }
    ),
    BLOODVELD(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
            SlayerMaster.CHAELDAR weight 8 min 70 max 130
            SlayerMaster.NIEVE weight 9 min 120 max 185
            SlayerMaster.DURADEL weight 8 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 9 min 120 max 170 area CatacombsOfKourend::class.java area GodwarsDungeonArea::class.java area SlayerTower::class.java area StrongholdSlayerDungeon::class.java area MeiyerditchLaboratoriesArea::class.java area IorwerthDungeon::class.java
        },
        buildInfo {
            id(48)
            slayer(50)
            combat(50)
            tip("Bloodveld are creatures with a large tongue.")
            extension(Range.of("Bleed me dry", 200, 250))
            registerNames(
                "Bloodveld", "Mutated bloodveld", "Insatiable bloodveld",
                "Insatiable mutated bloodveld", "Reanimated bloodveld"
            )
        }
    ),
    BRONZE_DRAGONS(
        buildTasks {
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 30 max 50 area CatacombsOfKourend::class.java area BrimhavenDungeon::class.java
        },
        buildInfo {
            id(58)
            slayer(1)
            combat(75)
            tip("Bronze dragons are the weakest metallic dragons, but should definitely not be taken lightly.")
            extension(Range.of("Pedal to the metals", 30, 50))
            registerNames("Bronze dragon")
        }
    ),
    CROCODILES(
        buildTasks {
            SlayerMaster.VANNAKA weight 6 min 40 max 90
        },
        buildInfo {
            id(65)
            slayer(1)
            combat(50)
            tip("Crocodiles are mostly found in desert areas, near water. Their bite can be quite dangerous.")
            registerNames("Crocodile", "Zebak")
        }
    ),
    DAGANNOTH(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.CHAELDAR weight 11 min 70 max 130
            SlayerMaster.NIEVE weight 8 min 120 max 185
            SlayerMaster.DURADEL weight 9 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 8 min 120 max 170 area CatacombsOfKourend::class.java area LighthouseDungeon::class.java area WaterbirthDungeon::class.java
        },
        buildInfo {
            id(35)
            slayer(1)
            combat(75)
            tip("Dagannoth are sea monsters, often found near Fremennik.")
            registerNames(
                "Dagannoth fledgeling", "Dagannoth spawn", "Dagannoth rex",
                "Dagannoth prime", "Dagannoth supreme", "Dagannoth mother",
                "Dagannoth", "Reanimated dagannoth"
            )
        }
    ),
    DUST_DEVILS(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
            SlayerMaster.KRYSTILIA weight 5 min 75 max 125
            SlayerMaster.CHAELDAR weight 9 min 70 max 130
            SlayerMaster.NIEVE weight 6 min 120 max 185
            SlayerMaster.DURADEL weight 5 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 6 min 120 max 170 area CatacombsOfKourend::class.java area SmokeDungeonArea::class.java
        },
        buildInfo {
            id(49)
            slayer(65)
            combat(70)
            wildernessLvl(24)
            tip("Dust devils are creatures made of a lot of dust, sand and ash. You might want to protect yourself from their dust with a face mask.")
            extension(Range.of("To dust you shall return", 200, 250))
            registerNames("Dust devil", "Choke devil", "Thermonuclear Smoke Devil")
        }
    ),
    FEVER_SPIDERS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 30 max 90
            SlayerMaster.CHAELDAR weight 7 min 70 max 130
        },
        buildInfo {
            id(69)
            slayer(42)
            combat(40)
            tip("Fever spiders are large spiders known to only be found on Braindeath island. It's suggested that you protect your hands with slayer gloves while fighting them, to avoid their fierce diseasing attacks.")
            registerNames("Fever spider")
        }
    ),
    FIRE_GIANTS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.CHAELDAR weight 12 min 70 max 130
            SlayerMaster.NIEVE weight 9 min 120 max 185
            SlayerMaster.DURADEL weight 7 min 130 max 200
            SlayerMaster.KRYSTILIA weight 7 min 75 max 125
            SlayerMaster.KONAR_QUO_MATEN weight 9 min 120 max 170 area KaruulmSlayerDungeon::class.java area BrimhavenDungeon::class.java area WaterfallDungeon::class.java area StrongholdSlayerDungeon::class.java area CatacombsOfKourend::class.java area IsleOfSoulsDungeon::class.java
        },
        buildInfo {
            id(16)
            slayer(1)
            combat(65)
            wildernessLvl(53)
            tip("Fire giants are large fiery foes found all across " + GameConstants.SERVER_NAME + ".")
            registerNames("Fire giant")
        }
    ),
    GARGOYLES(
        buildTasks {
            SlayerMaster.VANNAKA weight 5 min 40 max 90
            SlayerMaster.CHAELDAR weight 11 min 70 max 130
            SlayerMaster.NIEVE weight 6 min 120 max 185
            SlayerMaster.DURADEL weight 8 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 120 max 170 area SlayerTower::class.java
        },
        buildInfo {
            id(46)
            slayer(75)
            combat(80)
            tip("Gargoyles are large winged bat-like creatures known to only be found in the Slayer tower. You'll need to finish them off with a rock hammer.")
            extension(Range.of("Get smashed", 200, 250))
            registerNames("Gargoyle", "Dawn", "Dusk", "Marble gargoyle")
        }
    ),
    GREEN_DRAGONS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 4 min 60 max 100
        },
        buildInfo {
            id(24)
            slayer(1)
            combat(52)
            wildernessLvl(25)
            tip("Green dragons are the weakest of chromatic dragons, found across the Wilderness.")
            registerNames("Green dragon", "Baby green dragon", "Brutal green dragon", "5194", "5872", "5873")
        }
    ),
    HARPIE_BUG_SWARMS(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
        },
        buildInfo {
            id(70)
            slayer(33)
            combat(45)
            tip("Harpie bug swarms are accurate, small insects bundled up. You'll need to wield a lit bug lantern to shed some light on them and land a successful hit.")
            predicate { player -> player.skills.getLevelForXp(SkillConstants.FIREMAKING) >= 33 }
            registerNames("Harpie bug swarm")
        }
    ),
    HELLHOUNDS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 30 max 60
            SlayerMaster.CHAELDAR weight 9 min 70 max 130
            SlayerMaster.NIEVE weight 8 min 120 max 185
            SlayerMaster.DURADEL weight 10 min 130 max 200
            SlayerMaster.KRYSTILIA weight 7 min 75 max 125
            SlayerMaster.KONAR_QUO_MATEN weight 8 min 120 max 170 area CatacombsOfKourend::class.java area StrongholdSlayerDungeon::class.java area TaverleyDungeon::class.java area WitchavenDungeon::class.java area KaruulmSlayerDungeon::class.java
        },
        buildInfo {
            id(31)
            slayer(1)
            combat(75)
            wildernessLvl(25)
            tip("Hellhounds are fierce demonic dogs, found in the darkest dungeons.")
            registerNames("Hellhound", "Cerberus")
        }
    ),
    ICE_GIANTS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 30 max 80
            SlayerMaster.KRYSTILIA weight 6 min 100 max 150
        },
        buildInfo {
            id(15)
            slayer(1)
            combat(50)
            wildernessLvl(55)
            tip("Ice giants are large foes only found in cold, icy caverns.")
            registerNames("Ice giant")
        }
    ),
    INFERNAL_MAGES(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
        },
        buildInfo {
            id(40)
            slayer(45)
            combat(40)
            tip("Infernal mages are evil magicians, only known to be found in the Slayer tower.")
            registerNames("Infernal mage", "Malevolent mage")
        }
    ),
    JELLIES(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
            SlayerMaster.KRYSTILIA weight 5 min 100 max 150
            SlayerMaster.CHAELDAR weight 10 min 70 max 130
            SlayerMaster.KONAR_QUO_MATEN weight 6 min 120 max 170 area FremennikSlayerDungeon::class.java area CatacombsOfKourend::class.java //TODO - Ruins of Tapoyauik
        },
        buildInfo {
            id(50)
            slayer(52)
            combat(57)
            wildernessLvl(23)
            tip("Jellies are wobbly cubes of jelly, known to be found in the Fremennik slayer dungeon.")
            registerNames("Jelly", "Warped jelly", "Vitreous jelly", "Vitreous warped jelly", "chilled jelly")
        }
    ),
    JUNGLE_HORRORS(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
            SlayerMaster.CHAELDAR weight 10 min 70 max 130
        },
        buildInfo {
            id(81)
            slayer(1)
            combat(65)
            tip("Jungle horrors are horrible emaciated apes, found on the Mos Le'Harmless island.")
            registerNames("Jungle horror")
        }
    ),
    KURASK(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.CHAELDAR weight 12 min 70 max 130
            SlayerMaster.NIEVE weight 3 min 120 max 185
            SlayerMaster.DURADEL weight 4 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 3 min 120 max 170 area FremennikSlayerDungeon::class.java area IorwerthDungeon::class.java
        },
        buildInfo {
            id(45)
            slayer(70)
            combat(65)
            tip("Kurasks are large horned apes, dealing inaccurate but wild strikes. You'll need a leaf-bladed spear to damage them.")
            registerNames("Kurask", "King kurask")
        }
    ),
    LESSER_DEMONS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.CHAELDAR weight 9 min 70 max 130
            SlayerMaster.KRYSTILIA weight 6 min 80 max 120
        },
        buildInfo {
            id(28)
            slayer(1)
            combat(60)
            tip("Lesser demons are one of the smallest standard demons, yet they shouldn't be underestimated.")
            registerNames("Lesser demon")
        }
    ),
    MOSS_GIANTS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.KRYSTILIA weight 4 min 100 max 150
        },
        buildInfo {
            id(17)
            slayer(1)
            combat(40)
            wildernessLvl(35)
            tip("Moss giants are large, humanoid creatures tied to nature. They can mostly be found in dungeons and caverns.")
            registerNames("Moss giant")
        }
    ),
    NECHRYAEL(
        buildTasks {
            SlayerMaster.VANNAKA weight 5 min 40 max 90
            SlayerMaster.KRYSTILIA weight 5 min 75 max 125
            SlayerMaster.CHAELDAR weight 12 min 70 max 130
            SlayerMaster.NIEVE weight 7 min 110 max 170
            SlayerMaster.DURADEL weight 9 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 7 min 110 max 110 area CatacombsOfKourend::class.java area SlayerTower::class.java area IorwerthDungeon::class.java
        },
        buildInfo {
            id(52)
            slayer(80)
            combat(85)
            wildernessLvl(22)
            tip("Nechryael are humanoid demons, possessing the power to spawn small death spawns.")
            extension(Range.of("Nechs please", 200, 250))
            registerNames("Nechryael", "Nechryarch", "Greater Nechryael")
        }
    ),
    OGRES(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
        },
        buildInfo {
            id(20)
            slayer(1)
            combat(40)
            tip("Ogres are large humanoids found all across " + GameConstants.SERVER_NAME + ".")
            registerNames(
                "Ogre", "Ogre chieftain", "Enclave ogre", "Reanimated ogre",
                "Ogress warrior", "Ogress shaman"
            )
        }
    ),
    OTHERWORDLY_BEINGS(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
        },
        buildInfo {
            id(55)
            slayer(1)
            combat(40)
            tip("Otherwordly beings are ghostly, invisible creatures only to be found on Zanaris.")
            registerNames("Otherworldly being")
        }
    ),
    SPIRITUAL_CREATURES(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 40 max 90
            SlayerMaster.CHAELDAR weight 12 min 70 max 130
            SlayerMaster.NIEVE weight 6 min 120 max 185
            SlayerMaster.DURADEL weight 7 min 130 max 200
            SlayerMaster.KRYSTILIA weight 6 min 100 max 150
        },
        buildInfo {
            id(89)
            slayer(63)
            combat(60)
            wildernessLvl(29)
            predicate { p -> !p.slayer.bannedTasks.containsValue(SPIRITUAL_MAGE) }
            extension(Range.of("Spiritual Fervour", 181, 250))
            tip("Spiritual creatures are religious monsters found in the god wars dungeon.")
            registerNames("Spiritual ranger", "Spiritual mage", "Spiritual warrior")
        }
    ),
    TROLLS(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 40 max 90
            SlayerMaster.CHAELDAR weight 11 min 70 max 130
            SlayerMaster.NIEVE weight 6 min 120 max 185
            SlayerMaster.DURADEL weight 6 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 6 min 120 max 170 area TrollStrongholdArea::class.java area Keldagrim::class.java area DeathPlateau::class.java //TODO: South of CoX
        },
        buildInfo {
            id(18)
            slayer(1)
            combat(60)
            tip("Trolls are ugly, small creatures found all across " + GameConstants.SERVER_NAME + ".")
            registerNames(
                "Mountain troll", "Troll", "Ice troll grunt", "Ice troll runt", "Ice troll male",
                "Ice troll female", "Ice troll", "River troll", "Troll spectator", "Thrower troll",
                "Stick", "Kraka", "Pee Hat", "Troll general", "Reanimated troll"
            )
        }
    ),
    TUROTH(
        buildTasks {
            SlayerMaster.VANNAKA weight 8 min 30 max 90
            SlayerMaster.CHAELDAR weight 10 min 70 max 130
            SlayerMaster.NIEVE weight 3 min 120 max 185
            SlayerMaster.KONAR_QUO_MATEN weight 3 min 120 max 170 area FremennikSlayerDungeon::class.java
        },
        buildInfo {
            id(36)
            slayer(55)
            combat(60)
            tip("Turoth are three-legged hairless creatures found in dark dungeons. You'll need a leaf-bladed spear to inflict damage on them.")
            registerNames("Turoth", "Spiked turoth")
        }
    ),
    WEREWOLVES(
        buildTasks {
            SlayerMaster.VANNAKA weight 7 min 30 max 60
        },
        buildInfo {
            id(33)
            slayer(1)
            combat(60)
            tip("Werewolves are part human, part wolf creatures found in Canifis. You might find wolfbane to be quite useful there.")
            registerNames("Werewolf")
        }
    ),
    AVIANSIES(
        buildTasks {
            SlayerMaster.CHAELDAR weight 9 min 70 max 130
            SlayerMaster.NIEVE weight 6 min 120 max 185
            SlayerMaster.DURADEL weight 8 min 120 max 200
            SlayerMaster.KRYSTILIA weight 7 min 75 max 125
            SlayerMaster.KONAR_QUO_MATEN weight 6 min 120 max 170 area GodwarsDungeonArea::class.java
        },
        buildInfo {
            id(94)
            slayer(1)
            combat(0)
            wildernessLvl(29)
            tip("Aviansies are strong vicious birds known to only be found in the Godwars dungeon.")
            predicate { player -> player.slayer.isUnlocked("Watch the Birdie") }
            extension(Range.of("Birds of a feather", 200, 250))
            registerNames(
                "Aviansie", "Kree'arra", "Flight kilisa",
                "Wingman skree", "Flockleader geerin", "Reanimated aviansie"
            )
        }
    ),
    BLACK_DEMONS(
        buildTasks {
            SlayerMaster.CHAELDAR weight 10 min 70 max 130
            SlayerMaster.NIEVE weight 9 min 120 max 185
            SlayerMaster.DURADEL weight 8 min 130 max 200
            SlayerMaster.KRYSTILIA weight 7 min 100 max 150
            SlayerMaster.KONAR_QUO_MATEN weight 9 min 120 max 170 area CatacombsOfKourend::class.java area ChasmOfFire::class.java area TaverleyDungeon::class.java area BrimhavenDungeon::class.java
        },
        buildInfo {
            id(30)
            slayer(1)
            combat(80)
            tip("Black demons are large winged demons, found in many areas around " + GameConstants.SERVER_NAME + ".")
            extension(Range.of("It's dark in here", 200, 250))
            registerNames("Black demon", "Demonic gorilla", "Balfrug kreeyath", "Skotizo", "Porazdir", "Nezikchened")
        }
    ),
    CAVE_HORRORS(
        buildTasks {
            SlayerMaster.CHAELDAR weight 10 min 70 max 130
            SlayerMaster.NIEVE weight 5 min 120 max 180
            SlayerMaster.DURADEL weight 4 min 130 max 200
        },
        buildInfo {
            id(80)
            slayer(58)
            combat(85)
            tip("Cave horrors are large horrible apes found on the Mos Le'Harmless island. You'll need a witchwood icon to fight them.")
            extension(Range.of("Horrorific", 200, 250))
            registerNames("Cave horror", "Cave abomination", "Reanimated horror")
        }
    ),
    CAVE_KRAKEN(
        buildTasks {
            SlayerMaster.CHAELDAR weight 12 min 30 max 50
            SlayerMaster.NIEVE weight 6 min 100 max 120
            SlayerMaster.DURADEL weight 9 min 100 max 120
            SlayerMaster.KONAR_QUO_MATEN weight 9 min 80 max 100 area KrakenCove::class.java
        },
        buildInfo {
            id(92)
            slayer(87)
            combat(80)
            tip("Cave kraken are large monsters found in their cove. You'll find that magic is the most suitable style for defeating them.")
            extension(Range.of("Krack on", 150, 200))
            registerNames("Cave kraken", "Kraken", "Whirlpool")
        }
    ),
    FOSSIL_ISLAND_WYVERN(
        buildTasks {
            SlayerMaster.CHAELDAR weight 7 min 10 max 20
            SlayerMaster.NIEVE weight 5 min 5 max 25
            SlayerMaster.DURADEL weight 5 min 20 max 50
            SlayerMaster.KONAR_QUO_MATEN weight 7 min 15 max 30 area WyvernCave::class.java
        },
        buildInfo {
            id(106)
            slayer(66)
            combat(80)
            tip("Fossil island wyvern are flying wyverns found only on the Fossil island. They're large and have many different shapes.")
            predicate { player -> !player.slayer.isUnlocked("Stop the Wyvern") || player.settings.valueOf(Setting.STOP_THE_WYVERN_SLAYER_REWARD) != 0 }
            extension(Range.of("Wyver-nother two", 55, 75))
            registerNames("Spitting wyvern", "Taloned wyvern", "Long-tailed wyvern", "Ancient wyvern")
        }
    ),
    GREATER_DEMONS(
        buildTasks {
            SlayerMaster.CHAELDAR weight 8 min 70 max 130
            SlayerMaster.NIEVE weight 7 min 120 max 185
            SlayerMaster.DURADEL weight 9 min 130 max 200
            SlayerMaster.KRYSTILIA weight 8 min 100 max 150
            SlayerMaster.KONAR_QUO_MATEN weight 7 min 120 max 170 area KaruulmSlayerDungeon::class.java area CatacombsOfKourend::class.java area ChasmOfFire::class.java area BrimhavenDungeon::class.java area IsleOfSoulsDungeon::class.java
        },
        buildInfo {
            id(29)
            slayer(1)
            combat(75)
            tip("Greater demons are large winged demons found all across " + GameConstants.SERVER_NAME + ".")
            extension(Range.of("Greater challenge", 200, 250))
            registerNames("Tormented demon", "Greater demon", "K'ril tsutsaroth", "Tstanon Karlak", "Skotizo")
        }
    ),
    LIZARDMEN(
        buildTasks {
            SlayerMaster.CHAELDAR weight 8 min 50 max 90
            SlayerMaster.NIEVE weight 8 min 90 max 120
            SlayerMaster.DURADEL weight 10 min 130 max 210
            SlayerMaster.KONAR_QUO_MATEN weight 8 min 90 max 110 area KourendBattlefront::class.java area LizardmanCanyon::class.java area LizardmanSettlement::class.java area KebosSwamp::class.java area MolchAndLizardmanTemple::class.java
        },
        buildInfo {
            id(90)
            slayer(1)
            combat(0)
            tip("Lizardmen are reptilian humanoids found on their canyon and settlement.")
            predicate { player -> player.slayer.isUnlocked("Reptile got ripped") }
            registerNames("Lizardman", "Lizardman shaman", "Lizardman brute")
        }
    ),
    MUTATED_ZYGOMITES(
        buildTasks {
            SlayerMaster.CHAELDAR weight 7 min 8 max 15
            SlayerMaster.NIEVE weight 2 min 10 max 25
            SlayerMaster.DURADEL weight 2 min 20 max 30
            SlayerMaster.KONAR_QUO_MATEN weight 2 min 10 max 25 area ZanarisArea::class.java area FossilIsland::class.java
        },
        buildInfo {
            id(74)
            slayer(57)
            combat(60)
            tip("Mutated zygomites are a giant mushroom found in Zanaris. You'll need a fungicide spray to finish them off.")
            registerNames("Zygomite", "Ancient zygomite", "Mutated zygomite")
        }
    ),
    IRON_DRAGONS(
        buildTasks {
            SlayerMaster.NIEVE weight 5 min 30 max 60
            SlayerMaster.DURADEL weight 5 min 40 max 60
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 30 max 50 area CatacombsOfKourend::class.java area BrimhavenDungeon::class.java area IsleOfSoulsDungeon::class.java
        },
        buildInfo {
            id(59)
            slayer(1)
            combat(80)
            tip("Iron dragons are fierce metallic dragons found mainly in the Brimhaven dungeon.")
            extension(Range.of("Pedal to the metals", 60, 100))
            registerNames("Iron dragon")
        }
    ),
    SKELETAL_WYVERNS(
        buildTasks {
            SlayerMaster.CHAELDAR weight 7 min 10 max 20
            SlayerMaster.NIEVE weight 5 min 5 max 15
            SlayerMaster.DURADEL weight 7 min 20 max 40
            SlayerMaster.KONAR_QUO_MATEN weight 9 min 24 max 36 area AsgarnianIceDungeon::class.java
        },
        buildInfo {
            id(72)
            slayer(72)
            combat(70)
            tip("Skeletal wyverns are large winged reptiles, known to only be found in icy dungeons.")
            extension(Range.of("Wyver-nother one", 50, 70))
            registerNames("Skeletal wyvern")
        }
    ),
    STEEL_DRAGONS(
        buildTasks {
            SlayerMaster.NIEVE weight 5 min 30 max 60
            SlayerMaster.DURADEL weight 7 min 10 max 20
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 30 max 50 area CatacombsOfKourend::class.java area BrimhavenDungeon::class.java
        },
        buildInfo {
            id(60)
            slayer(1)
            combat(85)
            tip("Steel dragons are fierce metallic dragons found mainly in the Brimhaven dungeon.")
            extension(Range.of("Pedal to the metals", 40, 60))
            registerNames("Steel dragon")
        }
    ),
    TZHAAR(
        buildTasks {
            SlayerMaster.CHAELDAR weight 8 min 90 max 150
            SlayerMaster.NIEVE weight 10 min 110 max 180
            SlayerMaster.DURADEL weight 10 min 130 max 200
        },
        buildInfo {
            id(96)
            slayer(1)
            combat(0)
            tip("TzHaar are google-like creatures found inhabiting the city of fire.")
            predicate { player -> player.slayer.isUnlocked("Hot stuff") }
            registerNames(
                "TzHaar-Ket", "TzHaar-Xil", "TzHaar-Mej", "TzHaar-Hur", "Tz-Kih",
                "Tz-Kek", "Tok-Xil", "Yt-MejKot", "Ket-Zek", "TzTok-Jad",
                "Yt-HurKot", "Jal-Nib", "Jal-MejRah", "Jal-Ak", "Jal-ImKot",
                "Jal-Xil", "Jal-Zek", "JalTok-Jad", "TzKal-Zuk", "Jal-MejJak",
                "Reanimated TzHaar"
            )
        }
    ) {
        override fun validate(name: String, npc: NPC): Boolean {
            val location = GlobalAreaManager.getArea(npc.location)
            if (location is Inferno || location is FightCaves) {
                return false
            }
            return super.validate(name, npc)
        }
    },
    BLACK_DRAGONS(
        buildTasks {
            SlayerMaster.NIEVE weight 6 min 10 max 20
            SlayerMaster.DURADEL weight 9 min 10 max 20
            SlayerMaster.KRYSTILIA weight 4 min 8 max 16
            SlayerMaster.KONAR_QUO_MATEN weight 6 min 10 max 15 area CatacombsOfKourend::class.java area MythsGuildBasement::class.java area EvilChickenLair::class.java area TaverleyDungeon::class.java
        },
        buildInfo {
            id(27)
            slayer(1)
            combat(80)
            wildernessLvl(45)
            tip("Black dragons are the most fierce chromatic dragons around " + GameConstants.SERVER_NAME + ". Do not treat them lightly.")
            extension(Range.of("Fire & Darkness", 40, 60))
            registerNames("Black dragon", "Baby black dragon", "King black dragon", "Brutal black dragon")
            registerIds(1871, 1872, 7955)
        }
    ),
    DARK_BEASTS(
        buildTasks {
            SlayerMaster.NIEVE weight 5 min 10 max 20
            SlayerMaster.DURADEL weight 11 min 10 max 20
            SlayerMaster.KONAR_QUO_MATEN weight 8 min 10 max 15 area MournerTunnels::class.java area IorwerthDungeon::class.java
        },
        buildInfo {
            id(66)
            slayer(90)
            combat(90)
            tip("Dark beasts are horned beasts from a darker dimension.")
            extension(Range.of("Need more darkness", 110, 135))
            registerNames("Dark beast", "Night beast")
        }
    ),
    MITHRIL_DRAGONS(
        buildTasks {
            SlayerMaster.NIEVE weight 5 min 4 max 9
            SlayerMaster.DURADEL weight 9 min 5 max 10
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 3 max 6 area AncientCavern::class.java
        },
        buildInfo {
            id(93)
            slayer(1)
            combat(0)
            tip("Mithril dragons are one of the most dangerous dragons in all of " + GameConstants.SERVER_NAME + ". You might want to use magic to defeat them.")
            predicate { player -> player.slayer.isUnlocked("I hope you mith me") }
            extension(Range.of("I really mith you", 25, 35))
            registerNames("Mithril dragon")
        }
    ),
    RED_DRAGONS(
        buildTasks {
            SlayerMaster.NIEVE weight 5 min 30 max 80
            SlayerMaster.DURADEL weight 8 min 30 max 65
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 30 max 50 area BrimhavenDungeon::class.java area CatacombsOfKourend::class.java area MythsGuildBasement::class.java //TODO Forthos Dungeon
        },
        buildInfo {
            id(26)
            slayer(1)
            combat(68)
            tip("Red dragons are powerful chromatic dragons found in the Brimhaven dungeon.")
            predicate { player -> player.slayer.isUnlocked("Seeing red") }
            registerNames("Red dragon", "Baby red dragon", "Brutal red dragon")
            registerIds(244, 245, 246)
        }
    ),
    /* Stop Place */
    SMOKE_DEVILS(
        buildTasks {
            SlayerMaster.NIEVE weight 7 min 120 max 185
            SlayerMaster.DURADEL weight 9 min 130 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 7 min 120 max 170 area YanilleUndergroundArea::class.java
        },
        buildInfo {
            id(95)
            slayer(93)
            combat(85)
            tip("Smoke devils are creatures made of smoke, dust and dirt. They are found in their own dungeon. You might want to use some type of face protection to protect yourself from the smoke.")
            registerNames("Smoke devil", "Thermonuclear smoke devil", "Nuclear smoke devil")
        }
    ),
    SUQAHS(
        buildTasks {
            SlayerMaster.NIEVE weight 8 min 130 max 185
            SlayerMaster.DURADEL weight 8 min 60 max 90
        },
        buildInfo {
            id(83)
            slayer(1)
            combat(85)
            tip("Suqahs are strange creatures unique to the Lunar isle.")
            extension(Range.of("Suq-a-nother one", 185, 250))
            registerNames("Suqah")
        }
    ),
    WATERFIENDS(
        buildTasks {
            SlayerMaster.DURADEL weight 2 min 90 max 200
            SlayerMaster.KONAR_QUO_MATEN weight 2 min 120 max 170 area AncientCavern::class.java area KrakenCove::class.java area IorwerthDungeon::class.java
        },
        buildInfo {
            id(88)
            slayer(1)
            combat(75)
            tip("Waterfiends are fiendish embodiments of water. You'll find that crushing them is the easiest way to slay them.")
            registerNames("Waterfiend")
        }
    ),
    BANDITS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 6 min 75 max 125
        },
        buildInfo {
            id(102)
            slayer(1)
            combat(0)
            tip("Bandits are wilderness outlaws. You'll find them at their camp.")
            registerNames("Bandit", "Guard bandit", "Black Heather", "Speedy Keith", "Donny the lad")
        }
    ),
    DARK_WARRIORS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 6 min 70 max 125
        },
        buildInfo {
            id(103)
            slayer(1)
            combat(0)
            tip("Dark warriors are chaotic warriors found in their own fortress.")
            registerNames("Dark warrior")
        }
    ),
    ENTS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 6 min 35 max 60
        },
        buildInfo {
            id(101)
            slayer(1)
            combat(0)
            tip("Ents are large living trees near the Woodcutting guild.")
            registerNames("Ent")
        }
    ),
    LAVA_DRAGONS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 6 min 35 max 60
        },
        buildInfo {
            id(104)
            slayer(1)
            combat(0)
            tip("Lava dragons are large dragons found deep in the Wilderness. You might want to protect yourself from their fierce fire.")
            registerNames("Lava dragon")
        }
    ),
    MAGIC_AXES(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 7 min 70 max 125
        },
        buildInfo {
            id(91)
            slayer(1)
            combat(0)
            tip("Magic axes are animated steel battleaxes, found deep in the Wilderness. You'll need a lockpick to reach them.")
            registerNames("Magic axe")
        }
    ),
    MAMMOTHS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 6 min 75 max 125
        },
        buildInfo {
            id(99)
            slayer(1)
            combat(0)
            tip("Mammoths are large beautiful creatures found in the wilderness.")
            registerNames("Mammoth")
        }
    ),
    ROGUES(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 5 min 75 max 125
        },
        buildInfo {
            id(100)
            slayer(1)
            combat(0)
            tip("Rogues are humans gone rogue. They can be found deep in the Wilderness.")
            registerNames("Rogue")
        }
    ),
    SPIRITUAL_MAGE(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 6 min 130 max 200
        },
        buildInfo {
            id(89)
            slayer(83)
            combat(0)
            tip("Spiritual mages are strong mages found in the godwars dungeon.")
            extension(Range.of("Spiritual fervour", 181, 250))
            registerNames("Spiritual mage")
        }
    ),
    REVENANTS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 5 min 40 max 100
        },
        buildInfo {
            id(107)
            slayer(1)
            combat(30)
            tip("Revenants are the ghostly versions of creatures slain during the God Wars. You may wish to protect yourself with a bracelet of ethereum.")
            registerNames(
                "Revenant imp", "Revenant goblin", "Revenant pyrefiend", "Revenant hobgoblin", "Revenant cyclops",
                "Revenant hellhound", "Revenant demon", "Revenant ork", "Revenant dark beast", "Revenant knight",
                "Revenant dragon"
            )
        }
    ),

    VAMPYRE(
        buildTasks {
            SlayerMaster.MAZCHNA weight 5 min 10 max 20
            SlayerMaster.VANNAKA weight 5 min 10 max 20
            SlayerMaster.CHAELDAR weight 5 min 80 max 120
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 100 max 160
            SlayerMaster.NIEVE weight 5 min 110 max 170
            SlayerMaster.DURADEL weight 5 min 100 max 210
        },
        buildInfo {
            id(109)
            slayer(0)
            combat(35)
            predicate { player -> player.slayer.isUnlocked("Actual Vampyre Slayer") }
            tip("")
            registerNames(
                "Vampyre Juvinate", "Vampyre Juvenile", "Vyre",
                "Vyrewatch Sentinel", "Vyrewatch", "Feral Vampyre", "Vanstrom Klause"
            )
        }
    ),

    WARPED_CREATURES(
        buildTasks {
            SlayerMaster.CHAELDAR weight 5 min 80 max 120
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 100 max 160
            SlayerMaster.NIEVE weight 5 min 110 max 170
            SlayerMaster.DURADEL weight 5 min 100 max 210
        },
        buildInfo {
            id(122)
            slayer(56)
            combat(35)
            tip("")
            registerNames("Warped terrorbird", "Warped tortoise", "mutated tortoise", "mutated terrorbird")
        }
    ),

    ADAMANT_DRAGONS(
        buildTasks {
            SlayerMaster.NIEVE weight 2 min 3 max 7
            SlayerMaster.DURADEL weight 2 min 4 max 9
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 3 max 6 area LithkrenVault::class.java
        },
        buildInfo {
            id(108)
            slayer(1)
            combat(0)
            tip("Adamant dragons are metallic dragons created by Zorgoth. You may wish to protect yourself against their powerful dragonfire.")
            extension(Range.of("Ada'mind some more", 20, 30))
            registerNames("Adamant dragon")
        }
    ),

    RUNE_DRAGONS(
        buildTasks {
            SlayerMaster.NIEVE weight 2 min 3 max 6
            SlayerMaster.DURADEL weight 2 min 3 max 8
            SlayerMaster.KONAR_QUO_MATEN weight 5 min 3 max 6 area LithkrenVault::class.java
        },
        buildInfo {
            id(109)
            slayer(1)
            combat(0)
            tip("Rune dragons are metallic dragons created by Zorgoth. You may wish to protect yourself against their powerful dragonfire.")
            extension(Range.of("RUUUUUNE", 30, 60))
            registerNames("Rune dragon")
        }
    ),

    CHAOS_DRUIDS(
        buildTasks {
            SlayerMaster.KRYSTILIA weight 5 min 50 max 85
        },
        buildInfo {
            id(110)
            slayer(1)
            combat(0)
            tip("Chaos druids are followers of Guthix and zamorak. I've found that ranging them works the best.")
            registerNames("Chaos druid", "Elder chaos druid", "Reanimated chaos druid")
        }
    ),

    DRAKES(
        buildTasks {
            SlayerMaster.KONAR_QUO_MATEN weight 10 min 125 max 140 area KaruulmSlayerDungeon::class.java
        },
        buildInfo {
            id(112)
            slayer(84)
            combat(0)
            tip("Drakes are wingless dragons found in the middle level of the Karuulm Slayer Dungeon in Mount Karuulm.")
            registerNames("Drake", "Guardian drake")
        }
    ),

    HYDRAS(
        buildTasks {
            SlayerMaster.KONAR_QUO_MATEN weight 10 min 125 max 190 area KaruulmSlayerDungeon::class.java
        },
        buildInfo {
            id(113)
            slayer(95)
            combat(0)
            tip("Hydras are draconic creatures found in the lower level of the Karuulm Slayer Dungeon in Mount Karuulm.")
            registerNames("Hydra", "Colossal Hydra", "Alchemical Hydra")
        }
    ) {
        override fun getExperience(npc: NPC): Float {
            if (npc is AlchemicalHydra) {
                return 1320f
            }
            return (npc.maxHitpoints * (if (npc is SuperiorNPC) 10 else 1)).toFloat()
        }
    },

    WYRMS(
        buildTasks {
            SlayerMaster.KONAR_QUO_MATEN weight 10 min 125 max 190 area KaruulmSlayerDungeon::class.java
        },
        buildInfo {
            id(111)
            slayer(62)
            combat(0)
            tip("Wyrms are draconic creatures found in the lower level of the Karuulm Slayer Dungeon in Mount Karuulm.")
            registerNames("Wyrm", "Shadow wyrm")
        }
    ),
    BOSS(
        buildTasks {
            SlayerMaster.DURADEL weight 8 min 3 max 35
            SlayerMaster.NIEVE weight 8 min 3 max 35
            SlayerMaster.KRYSTILIA weight 8 min 3 max 35
            SlayerMaster.KONAR_QUO_MATEN weight 8 min 3 max 35
        },
        buildInfo {
            id(98)
            slayer(1)
            combat(0)
            tip("")
            predicate { player -> player.slayer.isUnlocked("Like a Boss") }
        }
    ),

    TZTOK_JAD(
        buildTasks {
        },
        buildInfo {
            id(97)
            slayer(1)
            combat(0)
            tip("TzTok-Jad is a fierce monster found at the end of the Fight Caves.")
            registerNames(
                "Tz-Kih", "Tz-Kek", "Tok-Xil",
                "Yt-MejKot", "Ket-Zek", "TzTok-Jad"
            )
        }
    ) {
        override fun getExperience(npc: NPC): Float {
            if (npc is TzTokJad) return 25250f
            return super.getExperience(npc)
        }

        override fun validate(name: String, npc: NPC): Boolean {
            if (GlobalAreaManager.getArea(npc.location) !is FightCaves) return false
            return super.validate(name, npc)
        }

        override fun toString(): String = "TzTok-Jad"
    },

    TZKAL_ZUK(
        buildTasks {
        },
        buildInfo {
            id(105)
            slayer(1)
            combat(0)
            tip("TzKal-Zuk is a fierce monster found at the end of the Inferno.")
            registerNames(
                "TzKal-Zuk", "Jal-Nib", "Jal-MejRah", "Jal-Ak", "Jal-AkRek-Mej",
                "Jal-AkRek-Xil", "Jal-AkRek-Ket", "Jal-ImKot", "Jal-Xil",
                "Jal-Zek", "JalTok-Jad", "Yt-HurKot", "Jal-MejJak"
            )
        }
    ) {
        override fun getExperience(npc: NPC): Float {
            if (npc is TzKalZuk) return 101890f
            return super.getExperience(npc)
        }

        override fun validate(name: String, npc: NPC): Boolean {
            val location = GlobalAreaManager.getArea(npc.location) as? Inferno ?: return false
            if (location.isPracticeMode) return false
            return super.validate(name, npc)
        }

        override fun toString(): String = "TzKal-Zuk"
    },


    STRYKEWYRMS(
        buildTasks {
            SlayerMaster.VANNAKA weight 5 min 20 max 50
            SlayerMaster.CHAELDAR weight 10 min 30 max 70
            SlayerMaster.NIEVE weight 7 min 70 max 85
            SlayerMaster.DURADEL weight 15 min 75 max 125
        },
        buildInfo {
            id(82)
            slayer(90)
            combat(65)
            tip("Strykewyrms are powerful creatures from an alternate dimension")
            registerNames("Ice Strykewyrm", "Desert Strykewyrm", "Jungle Strykewyrm")
        }
    ),
    ;

    override val monsters: Set<String> = monsterData.names
    override val monsterIds: Set<Int> = monsterData.ids

    constructor(
        taskSet: Array<Task>,
        info: SlayerInfo,
    ): this(
        taskSet = taskSet,
        taskId = info.taskId,
        slayerRequirement = info.slayerRequirement,
        combatRequirement = info.combatRequirement,
        tip = info.tip,
        predicate = Predicate { true },
        extendedRange = null,
        wildernessLevel = info.wildernessLevel,
        monsterData = info.monsterData
    )

    fun getCertainTaskSet(slayerMaster: SlayerMaster): Task? {
        for (set in taskSet) {
            if (set.slayerMaster == slayerMaster) {
                return set
            }
        }
        return null
    }

    val singularName: String
        get() {
            if (equals(TZHAAR)) {
                return "TzHaar"
            }
            if (equals(DWARVES)) {
                return "Dwarf"
            }
            if (equals(WOLVES)) {
                return "Wolf"
            }
            if (equals(WEREWOLVES)) {
                return "Werewolf"
            }
            var name = name.lowercase(Locale.getDefault()).replace("_", " ")
            if (name[name.length - 1] == 's') {
                name = name.substring(0, name.length - 1)
            }
            return name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        }

    override fun toString(): String {
        if (equals(TZHAAR)) {
            return "TzHaar"
        }
        val name = name.lowercase(Locale.getDefault()).replace("_", " ")
        return name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
    }

    override fun validate(name: String, npc: NPC): Boolean {
        if (this.monsterIds.contains(npc.id)) {
            return true
        }
        for (match in monsters) {
            if (name.equals(match, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    override fun getExperience(npc: NPC): Float {
        val bossAssignment = BossTask.MAPPED_VALUES[npc.definitions.name.lowercase(
            Locale.getDefault()
        )]
        if (bossAssignment != null) {
            return bossAssignment.xp
        }
        return (npc.maxHitpoints * (if (npc is SuperiorNPC) 10 else 1)).toFloat()
    }

    @JvmRecord
    data class Range(val extensionName: String, val min: Int, val max: Int) {
        companion object {
            fun of(extensionName: String, min: Int, max: Int): Range {
                return Range(extensionName, min, max)
            }
        }
    }

    override val taskName: String
        get() = toString()

    override val enumName: String
        get() = name

    fun hasTaskWith(slayerMaster: SlayerMaster): Boolean {
        for (task in taskSet) {
            if (task.slayerMaster == slayerMaster) {
                return true
            }
        }
        return false
    }

    companion object {
        val VALUES: Array<RegularTask> = entries.toTypedArray()

        @JvmStatic fun isAssignable(name: String): String? {
            val lowercaseName = name.lowercase(Locale.getDefault())
            val bossTask = BossTask.MAPPED_VALUES[lowercaseName]
            if (bossTask != null) {
                return StringFormatUtil.formatString(bossTask.taskName)
            }
            for (assignable in VALUES) {
                for (matchingName in assignable.monsters) {
                    if (matchingName == name) {
                        return assignable.singularName
                    }
                }
            }
            return null
        }
    }
}