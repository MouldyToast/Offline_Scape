# PLAN: Stage 2 — self-contained custom monsters & support slices

**Roadmap:** ROADMAP_custom_cache_removal v3, Stage 2.
**Verified against:** branch `claude/new-session-lo0m56` @ `cd479db0` (Stage 1 executed). Every FIND block below was read from source and verified unique by the dry-run in Part D on this tree.
**Removal order enforced (rule 1):** server content code (Part A) → TypeParser invocations (Part B1) → packer classes (Part B2–B8) → asset files (Part C).

---

## 0. Scope decisions & roadmap corrections — read before executing

### 0.1 Approved scope (user-confirmed)
1. **Wilderness-event framework: remove entirely.** All 17 files under `content/areas/wilderness/.../event/` (framework + ganodermic_beast + the disabled hot_zone/chest stubs), the `::wevent`/`::gano`/`::managewevents` commands and the world-tick hook (all live inside the deleted files). Ganodermic was the only registered event; the manager would otherwise warn every 600ms tick on an empty pool.
2. **Ganodermic booster 32150/33150: remove now**, from all 10 distribution points, plus the persisted `PlayerVariables.ganoBoosterKillsLeft` (orphaned JSON key in old saves is tolerated by the Gson loader).
3. **Revival-skull Stage 1 regression: strip the code.** Stage 1's "zero readers" claim for sprites 439/21–30 was wrong — `PlayerSkulls.getSkull()` resolves `REVIVE_<n>` from the live PvM-Arena `pvmArenaRevivalCount` attribute. Fix: remove `REVIVE_1..10` and that branch; the attribute and the revive-timer logic (`PvmArenaReviveState`) stay. Downed players simply show no countdown skull (it has rendered garbage since Stage 1 anyway).
4. **`NearRealityRaidsItemDefinitions`: remove, with the CoX fix landed first.** The roadmap pre-check FAILED: `CreatureKeeperRoom` adds grubs with no free-slot check and ignores its own clamp — safe only while grubs are stackable. Part A6 fixes that before the packer (and stackability) goes. `RaidWoodcutting` needs no change; its pre-existing `experience[-1]` AIOOBE (full inventory in IceDemonRoom) is flagged in Part G, not fixed.

### 0.2 Roadmap corrections (verified this session)
- **Effigy map edits: KEEP — the roadmap's Stage 2 listing was wrong.** `NearRealityEffigyMapEdits.kt` (TypeParser.java:1518) is live and load-bearing: DT2 Duke/Leviathan/Vardorvis scoreboards, Phantom Muspah scoreboard, Araxxor scoreboard, the wilderness gem rocks (region 12088), the mineable ancient-essence wall (46702→46701), rubble clearing. "Effigy" is an upstream author handle, not a feature. No rename this stage. Instead delete the **dead strict-superset duplicate** `NearRealityVanillaMapEdits.kt` (zero references; its extra edits serve dying DI/donator-island content).
- **Origins packer: SPLIT, not delete.** `packCustoms()` (items 32884–32961: primal weapons/armour, pernix, elite black, divine sigil/spirit shield, lord marshall, tokens, promo bundles) and the Remnant portion of `packPets()` (NPCs 16095–16123 + `DRIFTER` 16133) serve surviving content: 12 revenant drop processors, gauntlet rewards, ChaosChest, Araxxor rewards, `RareDrop.java`, `BossDropItem.java`, the REMN_* `BossPet` entries. The trim keeps them; the 11 boss sub-packers and the five origins boss-pet blocks go.
- **Hiscores gano rows: KEEP until Stage 6.** `HiscoresInterface.java` serializes entries by array index (`category.getEntries()[i]`, L141/L188) against the packed CS2 blob `assets/packed/misc/archive_12/2735` — removing `GANODERMIC_BEAST` from `HiscoresCategory.java`/`HiscoresCategoryEntry.java` shifts every later boss label. The row stays (frozen killcounts) and dies in Stage 6 with the blob. The dry-run guards these files against editing.
- **`PackerExt.kt`: untouched.** The surviving `packCustoms()` calls all five `default*()` helpers (`assets/origins/customs/` has all five sub-dirs). Nothing becomes unreferenced.
- **Slayer save guard REQUIRED (new).** Slayer assignments persist `taskName` strings and `Assignment.getTask()` **throws** `IllegalStateException("Task not found")` for unknown names (Assignment.kt:280) — a save with an active STRYKEWYRMS task would crash at login. Part A5 adds a migration guard remapping to `RATS` (verified present in `RegularTask.kt`).
- The `NearRealityCustomAnimationsPacker` is an **empty shell** ("All entries removed" — upstream state) — free kill. The other shared packers (`CustomGraphics`, `CustomSpecialAttacks`, `CustomHeadIcons`) contain **zero** Stage 1–2 entries and are NOT touched.
- **32957 collision resolves itself:** `DIVINE_SPIRIT_SHIELD.createPlaceholder(32957)` in `packCustoms()` is currently clobbered by `packWildMole()` re-packing 32957 as the Baby wild mole pet item. After the trim the placeholder is the sole owner — the shield's bank placeholder starts working. Any banked pet 32957 morphs into that placeholder.

---

## Part A — server content code

### A-1. Wilderness-event framework

```
git rm -r content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/event/
```
(17 files: `WildernessEvent.kt`, `WildernessEventExt.kt`, `WildernessEventManager.kt`, `WildernessEventModule.kt`, `WildernessEventSource.kt`, `chest/`×4, `ganodermic_beast/`×5, `hot_zone/`×4.)

Then edit the one external consumer, `content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/slayer/WildernessSlayerModule.kt`:

A-1a — FIND:
```kotlin
import org.jesse.game.content.wilderness.event.hot_zone.WildernessHotZoneEvent
```
REPLACE: *(nothing — delete the line)*

A-1b — FIND:
```kotlin
        if (WildernessHotZoneEvent.inHotZone(player)) {
            bloodMoneyRewardAmount *= 2
            player.sendMessage("You received double blood money for completing the task in a hot zone.")
        }
```
REPLACE: *(nothing — delete the block; the hot-zone event never ran, its registration was commented out)*

### A-2. Broadcast orphans

`engine/src/main/java/org/jesse/game/world/broadcasts/BroadcastType.java` — FIND:
```java
    WILDERNESS_EVENT("B22222", 68, Optional.empty()),
```
REPLACE: *(nothing — delete the line; sole live caller was `WildernessEventManager.kt:143`, deleted in A-1)*

`engine/src/main/java/org/jesse/game/world/broadcasts/WorldBroadcasts.java` — two edits:

A-2b — FIND:
```java
//            case WILDERNESS_EVENT -> sendGanodermicBeast(args);
```
REPLACE: *(nothing — delete the line; it sits inside an already-commented block)*

A-2c — FIND:
```java
            case WILDERNESS_EVENT: {
                builder.append("Event: ");
                secondaryBuilder.append(args[0]);
                builder.append(secondaryBuilder);
                break;
            }
```
REPLACE: *(nothing — delete the block)*

### A-3. Ganodermic engine/server references

`core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt` — FIND (keep the `/* Custom NPC ids */` header above it):
```kotlin
const val GANODERMIC_BEAST = 14696
const val GANODERMIC_RUNT = 14698
const val GANODERMIC_RUNT_32000 = 32000// pet
```
REPLACE: *(nothing — delete all three; `GANODERMIC_RUNT_32000` was a verified orphan even before this stage)*

`core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt` — delete these five whole lines (each verified unique; **KEEP** `POLYPORE_STAFF = 32042` — the charged staff survives via `PvpTourneyMysteryBox.java:115`, and no `DegradableItem` link between 32042 and 32040 exists anywhere):
```kotlin
const val ANCIENT_EYE = 32002
const val POLYPORE_SPORES = 32038
const val POLYPORE_STAFF_DEG = 32040
const val GANODERMIC_RUNT = 32100
const val GANODERMIC_BOOSTER = 32150
```
> Neither `ItemId.kt` nor `NpcId.kt` has an in-repo generator (the "automatically generated" header is vestigial for the hand-merged custom tail) — hand-edit both.

**Booster distribution points (10)** — delete each listed line:
1. `core-model/src/main/kotlin/org/jesse/game/content/universalshop/UniversalShopTable.kt` — `                UnivShopItem(32150, buyPrice = 150),`
2. same file — `                UnivShopItem(32150, buyPrice=5),`
3. `content/other/unused/src/main/kotlin/org/jesse/game/content/unused/universalshop/UnivShopTable.kt` — `                UnivShopItem(32150, buyPrice = 150),`
4. same file — `                UnivShopItem(32150, buyPrice=5),`
5. `engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvpMysteryBox.java` — `                new MysterySupplyItem(32150, 3, 5), // Ganodermic Booster`
6. `engine/src/main/java/org/jesse/plugins/item/mysteryboxes/RegalMysteryBox.java` — `                new MysterySupplyItem(32150, 2, 5), // Ganodermic Booster`
7. `engine/src/main/java/org/jesse/plugins/item/mysteryboxes/SuperMysteryBox.java` — `                new MysteryItem(32150, 3, 6, 1000), // Gano booster`
8. `engine/src/main/java/org/jesse/plugins/item/mysteryboxes/UltimateMysteryBox.java` — `//                new MysteryItem(32150, 5, 10, 1000), // Ganodermic Booster` (already commented)
9. `engine/src/main/kotlin/org/jesse/game/content/bountyhunter/crate/BountyCrate.kt` — `          else if (Random.nextInt(16) == 0) Item(32150) // Gano Booster` (one arm of an else-if chain; the surrounding arms remain syntactically intact)
10. `content/other/death-mechanics/src/main/kotlin/org/jesse/plugins/item/actions/death_items/kept_items.kt` — FIND:
```kotlin
            32149, 32150, 32151, 32152, 32153, 32154, 32155, 32156,
```
REPLACE:
```kotlin
            32149, 32151, 32152, 32153, 32154, 32155, 32156,
```

`content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt` — three edits:
- delete `    private const val ganoBooster = GANODERMIC_BOOSTER`
- delete `    private const val ganodermicBeast = 10300`
- FIND (delete whole block):
```kotlin
                CollectionLogReward(
                    ganodermicBeast,
                    arrayOf(dpin25 x 1, ultraMB x 1, bloodMoney x 5_000, ganoBooster x 5)
                ),
```

`engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java` — delete the line:
```java
        info.add("Ganodermic booster: " + (vars.getGanoBoosterKillsLeft() > 0 ? green + "Active for " + vars.getGanoBoosterKillsLeft() + " kills" : red + "Inactive"));
```

`engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java` — three edits:
- delete `        ganoBoosterKillsLeft = copy.ganoBoosterKillsLeft;` (copy-ctor)
- delete `    private int ganoBoosterKillsLeft;` (persisted field — see Part F)
- FIND (delete whole block incl. trailing blank line):
```java
    public int getGanoBoosterKillsLeft() {
        return ganoBoosterKillsLeft;
    }

    public void setGanoBoosterKillsLeft(int ganoBoosterKillsLeft) {
        this.ganoBoosterKillsLeft = ganoBoosterKillsLeft;
    }

```

`engine/src/main/java/org/jesse/game/world/entity/player/NotificationSettings.java` — FIND:
```java
        "ganodermic beast", "lizardman",
```
REPLACE:
```java
        "lizardman",
```

`engine/src/main/java/org/jesse/game/world/entity/player/collectionlog/CollectionLogCategoryType.java` — delete the line (name-keyed registration, not ordinal-coupled — safe, unlike hiscores):
```java
    GANODERMIC_BEAST(-1, player -> get(player, "Ganodermic Beast")),
```

`engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java` — delete the line:
```java
    GANODERMIC_RUNT(32100, 14698),
```

`engine/src/main/java/org/jesse/game/model/item/enums/RareDrop.java` — FIND (delete incl. trailing blank):
```java
    //Ganodermic beast
    GANODERMIC_RUNT(ItemId.GANODERMIC_RUNT),

```

`engine/src/main/java/org/jesse/game/world/entity/npc/spawns/NPCSpawnLoader.java` — delete the line:
```java
        dropViewerNPCs.add(NpcId.GANODERMIC_BEAST);
```

`engine/src/main/kotlin/org/jesse/game/content/commands/CustomCommands.kt` — delete the line:
```kotlin
                        add(Item(GANODERMIC_RUNT, 10))
```

```
git rm engine/src/main/java/org/jesse/plugins/dialogue/GanodermicRuntD.java
git rm cache/data/npcs/combat/14696.npc.json
```
(`GanodermicRuntD` verified referenced nowhere; no combat json exists for 14698.)

**Hiscores files are deliberately NOT edited** (`HiscoresCategory.java`, `HiscoresCategoryEntry.java`) — see §0.2; the dry-run enforces this.

### A-4. Revival-skull fix — `engine/src/main/java/org/jesse/game/world/entity/player/PlayerSkulls.java`

A-4a — FIND:
```java
    BLACK_SKULL(20),

    REVIVE_1(21),
    REVIVE_2(22),
    REVIVE_3(23),
    REVIVE_4(24),
    REVIVE_5(25),
    REVIVE_6(26),
    REVIVE_7(27),
    REVIVE_8(28),
    REVIVE_9(29),
    REVIVE_10(30);
```
REPLACE:
```java
    BLACK_SKULL(20);
```

A-4b — FIND:
```java
        final int revivalTimer = PlayerAttributesKt.getPvmArenaRevivalCount(player);
        if (revivalTimer > 0) {
            PlayerSkulls skull = PlayerSkulls.valueOf("REVIVE_" + revivalTimer);
            return skull.skullStatusId;
        }

```
REPLACE: *(nothing — delete the block; the `PlayerAttributesKt` import stays, still used by `getBlackSkulled` below. The `pvmArenaRevivalCount` attribute and all its PvM-Arena writers are untouched — they drive the revive timer itself.)*

### A-5. Origins boss module + engine references

```
git rm -r content/bosses/origins/
```
(28 `.kt` files + `build.gradle.kts` — combat defs, 9 drop tables, all spawns incl. the DI/DIE/RDI/RDI2/train-island vanilla-NPC farms, `RDIGate`, `RopeExitSlashBash`. The module is auto-discovered by `settings.gradle.kts`'s content walk — **no settings edit needed**. Also `rm -rf content/bosses/origins/` afterwards if an untracked `build/` output dir lingers, so stale classes can't be plugin-scanned.)

`engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java` — two block deletions (**keep** every `REMN_*`/`PET_DARK_*` entry between them):
```java
    PET_JUNGLE_STRYKEWYRM(ItemId.PET_JUNGLE_STRYKEWYRM, NpcId.BABY_JUNGLE_STRYKEWYRM),
    PET_ICE_STRYKEWYRM(ItemId.PET_ICE_STRYKEWYRM, NpcId.BABY_ICE_STRYKEWYRM),
    PET_DESERT_STRYKEWYRM(ItemId.PET_DESERT_STRYKEWYRM, NpcId.BABY_DESERT_STRYKEWYRM),
    PET_SLASH_BASH(ItemId.PET_SLASH_BASH, NpcId.LIL_SLASH_BASH),
    PET_AGED_BARRELCHEST(ItemId.PET_BARRELCHEST, NpcId.BABY_AGED_BARRELCHEST),
```
and
```java
    PET_WILDY_IMP           (ItemId.PET_WILDY_IMP,           NpcId.LIL_WILDY_IMP),
    OFF_BALANCE_ELEMENTAL           (ItemId.OFF_BALANCE_ELEMENTAL,           NpcId.OFF_BALANCE_ELEMENTAL),
    BABY_WILD_MOLE(ItemId.PET_BABY_WILD_MOLE, NpcId.BABY_WILD_MOLE),
```
both REPLACE: *(nothing — delete)*

`engine/src/main/kotlin/org/jesse/game/content/slayer/RegularTask.kt` — FIND (delete the whole enum entry):
```kotlin
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
```
REPLACE: *(nothing — delete; verified `STRYKEWYRMS` has no other code reference)*

**Slayer save migration guard (REQUIRED)** — `engine/src/main/kotlin/org/jesse/game/content/slayer/Assignment.kt`, FIND:
```kotlin
        taskName = old.taskName.stripSumona()
        task = getTask(taskName)
```
REPLACE:
```kotlin
        taskName = old.taskName.stripSumona()
        if (taskName == "STRYKEWYRMS") {
            // Stage 2: strykewyrm slayer task removed; retire stale saved assignments.
            taskName = "RATS"
            amount = 0
            initialAmount = 0
        }
        task = getTask(taskName)
```
(A zeroed RATS assignment completes/clears through the normal flow; without the guard, `getTask` throws at login for any save holding a strykewyrm task.)

`engine/src/main/java/org/jesse/game/world/entity/player/NotificationSettings.java` — two edits:
- FIND `"sol heredit", "strykewyrm");` → REPLACE `"sol heredit");`
- FIND `"drake", "strykewyrm");` → REPLACE `"drake");`

**Barrows wights (6 files, `engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/`)** — in each, delete the static import line and the case-label token (whitespace-independent substring edits):
| File | Delete import | Delete case token |
|---|---|---|
| `AhrimTheBlighted.java` | `import static org.jesse.game.npc.ids.NpcId.DI_AHRIM_THE_BLIGHTED;` | `, DI_AHRIM_THE_BLIGHTED` |
| `DharokTheWretched.java` | `...DI_DHAROK_THE_WRETCHED;` | `, DI_DHAROK_THE_WRETCHED` |
| `GuthanTheInfested.java` | `...DI_GUTHAN_THE_INFESTED;` | `, DI_GUTHAN_THE_INFESTED` |
| `KarilTheTainted.java` | `...DI_KARIL_THE_TAINTED;` | `, DI_KARIL_THE_TAINTED` |
| `ToragTheCorrupted.java` | `...DI_TORAG_THE_CORRUPTED;` | `, DI_TORAG_THE_CORRUPTED` |
| `VeracTheDefiled.java` | `...DI_VERAC_THE_DEFILED;` | `, DI_VERAC_THE_DEFILED` |

```
git rm engine/src/main/java/org/jesse/game/world/region/area/wilderness/WildMoleArea.java
```
(Verified: `PortalTeleport.EASTERN_DRAGONS` is also unlocked by `EasternDragonsArea.java` — no unlock regression; area classes are classpath-registered.)

`engine/src/main/kotlin/org/jesse/game/content/commands/DeveloperCommands.kt` — FIND (delete):
```kotlin
        Command(PlayerPrivilege.TRUE_DEVELOPER, "barrelchest") { player, args  ->
            if(isOwner(player)) {
                player.teleport(Location(1887, 2717, 3))
            } else {
                player.sendMessage("Try again next time.")
            }
        }

```

`CollectionLogRewards.kt` (same file as A-3) — four edits (**keep** `primal = 10501` and its reward row — primal items survive to 5c):
- delete `    private const val strykeWyrms = 10500`
- delete `    private const val wildMole = 10502`
- delete `                CollectionLogReward(strykeWyrms, arrayOf(superMB x 1, standardMB x 1)),`
- delete `                CollectionLogReward(wildMole, arrayOf(ultraMB x 1, superMB x 2, standardMB x 3)),`

`core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt` — three region deletions (**keep** `REMN_*` 16095–16123 and `DRIFTER = 16133` between them):
- the 18 lines `ICE_STRYKEWYRM = 16077` … `LIL_SLASH_BASH = 16094`
- the 9 lines `WILDY_IMP = 16124` … `DI_VERAC_THE_DEFILED = 16132`
- the 2 lines `WILD_MOLE = 16134`, `BABY_WILD_MOLE = 16135`
(exact FIND text is in the dry-run script, tags `A5-npc1..3`)

`core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt` — delete the origins pet item constants (**keep** `CHAOTIC_CROSSBOW`/`CHAOTIC_KITESHIELD`/`EAGLE_EYE_KITESHIELD`/`FARSEER_KITESHIELD` 32810–32816 — deferred to 5c; they have no source after the wyrms die but stay defined for banked copies):
```kotlin
const val PET_JUNGLE_STRYKEWYRM = 32821
const val PET_ICE_STRYKEWYRM = 32822
const val PET_DESERT_STRYKEWYRM = 32823
const val PET_BARRELCHEST = 32824
const val PET_SLASH_BASH = 32825
```
```kotlin
const val PET_WILDY_IMP = 32855
const val OFF_BALANCE_ELEMENTAL = 32856
```
```kotlin
const val PET_BABY_WILD_MOLE = 32957
```

### A-6. Chambers of Xeric fix (lands BEFORE Part B removes the stackability packer)

`engine/src/main/java/org/jesse/game/content/chambersofxeric/room/CreatureKeeperRoom.java` — FIND:
```java
                        final int intAmount = amount.intValue();
                        final int amountToAdd = Math.max(0, Math.min(intAmount, CAP_MAXIMUM_CAVERN_GRUBS_STACK - amountInInventory));
                        if (amountToAdd > 0) {
                            player.getSkills().addXp(SkillConstants.THIEVING, amountToAdd == 1 ? 40 : amountToAdd == 2 ? 60 : 73);
                            player.getInventory().addItem(new Item(ItemId.CAVERN_GRUBS, intAmount));
                        }
```
REPLACE:
```java
                        final int intAmount = amount.intValue();
                        final int amountToAdd = Math.max(0, Math.min(Math.min(intAmount, player.getInventory().getFreeSlots()), CAP_MAXIMUM_CAVERN_GRUBS_STACK - amountInInventory));
                        if (amountToAdd > 0) {
                            player.getSkills().addXp(SkillConstants.THIEVING, amountToAdd == 1 ? 40 : amountToAdd == 2 ? 60 : 73);
                            player.getInventory().addItem(new Item(ItemId.CAVERN_GRUBS, amountToAdd));
                        }
```
Fixes both defects: the add now uses the computed clamp (previously `intAmount` — over-cap while stackable, silently lossy once unstackable), and the free-slot bound makes unstackable grubs partial-add-safe with XP staying in sync. The trough-deposit path and the 28 cap (== one inventory) are safe for unstackable items as-is. `RaidWoodcutting.addLog` already bounds by `getFreeSlots()` — no change.

---

## Part B — cache module (TypeParser invocations, then packer classes)

### B-1. `cache/src/main/java/mgi/tools/parser/TypeParser.java`

Delete three imports:
```java
import org.jesse.cache_tool.packing.custom.NearRealityCustomAnimationsPacker;
import org.jesse.cache_tool.packing.custom.NearRealityRaidsItemDefinitions;
import org.jesse.cache_tool.packing.custom.ganodermic_beasts.GanodermicBeastsPacker;
```
(they are non-adjacent — delete each line individually; **keep** the `NearRealityOriginsPacker` and `NearRealityEffigyMapEdits` imports)

Delete calls:
```java
        NearRealityCustomAnimationsPacker.pack();
```
```java
        NearRealityRaidsItemDefinitions.makeKindlingStackable();
        NearRealityRaidsItemDefinitions.makeCavernGrubsStackable();
```
FIND:
```java
        AnimationBase.pack();
        GanodermicBeastsPacker.pack();
```
REPLACE:
```java
        AnimationBase.pack();
```
(`GanodermicBeastsPacker.pack()` was the last statement of `packHighRevision()`; the gano packer's `FramePacker.reset()` ran after the global `write()`, so removal is ordering-safe.)

Delete the 19 commented `origin_update` map-pack lines (the block from `//        packMapPre209(6954,` through `//                "assets/map/origin_update/barrelchest/obj.dat");` — only lines referencing `assets/map/origin_update/`; leave the `quad_dono_island` comments above/below alone; exact block in dry-run tag `B1-maps`).

### B-2. Packer class deletions

```
git rm -r cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/ganodermic_beasts/
git rm cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityRaidsItemDefinitions.kt
git rm cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomAnimationsPacker.kt
git rm cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityVanillaMapEdits.kt
```

### B-3. `NearRealityOriginsPacker.kt` — SPLIT (trim in place, no rename this stage)

Five edits (anchors verified unique by the dry-run):

B-3a — imports, FIND (delete all 8 lines; **keep** `import net.runelite.api.NpcID` and the `NpcID.*` wildcard — `POSTIE_PETE`/`IMP_DEFENDER` still need them):
```kotlin
import net.runelite.api.NpcID.BABY_MOLE
import net.runelite.api.NpcID.GIANT_MOLE_6499
import net.runelite.api.NpcID.AHRIM_THE_BLIGHTED
import net.runelite.api.NpcID.DHAROK_THE_WRETCHED
import net.runelite.api.NpcID.GUTHAN_THE_INFESTED
import net.runelite.api.NpcID.KARIL_THE_TAINTED
import net.runelite.api.NpcID.TORAG_THE_CORRUPTED
import net.runelite.api.NpcID.VERAC_THE_DEFILED
```

B-3b — `pack()`, FIND:
```kotlin
    @JvmStatic fun pack() {
        packStrykewyrms()
        packBalanceElementals()
        packBork()
        packPlaneFreezer()
        packNomad()
        packPhoenix()
        packSlashBash()
        packBarrelchest()
        packPets()
        packWildyImp()
        packCustoms()
        packBarrowsCopies()
        packWildMole()
    }
```
REPLACE:
```kotlin
    @JvmStatic fun pack() {
        packPets()
        packCustoms()
    }
```

B-3c — region R1: delete from the line `    private fun packWildMole() {` up to (but not including) the line `    @JvmStatic fun packPets() {`. Removes `packWildMole`, `packBarrowsCopies`, `packWildyImp` whole.

B-3d — region R2 (inside `packPets`): delete from the `        NPCDefinitions.builder()` line directly above `            .initialize(16090, 1, 60342, 60343)` up to (but not including) the line `        NPCDefinitions.get(NpcID.POSTIE_PETE).clone().toBuilder()`. Removes the five origins boss-pet blocks 16090–16094 (they reference models 60338–60343/60359/60360 and anims 25001/25042/25047 from dirs deleted in Part C). Everything from POSTIE_PETE (16095) through `DRIFTER` (16133) **stays**, including the `assetsBase("assets/origins/pets/")` preamble (the surviving `dark_*`/`fissile_*`/`corrupt_kratos_*` models).

B-3e — region R3: delete from the line `    @JvmStatic fun packBarrelchest() {` up to (but not including) the line `    @JvmStatic fun packCustoms() {`. Removes `packBarrelchest`, `packSlashBash`, `packPhoenix`, `packNomad`, `packPlaneFreezer`, `packBork`, `packStrykewyrms`, `packBalanceElementals` whole.

`packCustoms()` and the `promoBundle` helper are untouched. Compile gate catches any leftover import.

### B-4. `cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt`

Eight deletions (**keep** the `10501   // primals` boss-log line and the whole `/* Primal Items */` enum-10501 block):
- line `            this.values[idx++] = 10300  // gano` (the `idx++` chain stays valid — order shifts down one, matching the removed struct)
- line `            this.values[idx++] = 10500  // strykewyrms`
- line `            this.values[idx++] = 10502  // Wild Mole`
- the `// All Pets log` block (enum 2158, adds `GANODERMIC_RUNT`) — 7 lines incl. trailing blank
- the `// Ganodermic Beast` block (creates enum 10025) — 9 lines incl. trailing blank
- the `/* Strykewyrms */` block (creates enum 10500) — 10 lines incl. trailing blank
- the `/* Wild Mole */` block (creates enum 10502) — 19 lines incl. trailing blank
- the `/* Slayer Task Defs - 82 replaces unused Gorak */` block (enum 693) — 6 lines incl. trailing blank; the client label reverts to vanilla "Gorak", which the server never assigns

(Exact FIND text for each: dry-run tags `B4-1..8`. The item constants those blocks referenced — `DRAGON_KITE`, `PVP_MYSTERY_BOX`, `STAFF_OF_LIGHT`, `GHOSTLY_PARTYHAT`, primal ids, etc. — survive as items; only the enum registrations go.)

### B-5. `cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomStructsPacker.kt`

Delete the 10500 ("Strykewyrms") and 10502 ("Wild Mole") blocks; **keep** 10501 ("Primal Items"). Exact FINDs: dry-run tags `B5-1`, `B5-2`.

### B-6. `cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomWorldMapPacker.kt`

Delete the line:
```kotlin
        2500.createSmallMapLabel("Here be some<br>wild moles")
```
(`pack()` body becomes the existing commented-out labels only — leave those comments.)

### B-7. `cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt`

**Pairing rule:** an on-disk `.dat` in a dir that `defaultModels()` walks MUST keep its map entry (missing mapping = pack error); a map entry without a file is harmless. Every deletion below is paired with a Part C file/dir deletion.

Delete:
- `    "ancient_eye" to CustomDefinition.Model(60000),` (paired: `custom_items/models/ancient_eye.dat`)
- `    "booster_ganodermic" to CustomDefinition.Model(modelId = 60137),` (paired: `booster_ganodermic.dat`)
- the contiguous 28-line block `"desert_strykewyrm_a"` (60338) … `"pet_slash_bash_inv"` (60365) — paired with the nine boss dirs + five pet model files; the block ends immediately before `"corrupt_kratos_body"` (60366) which **stays**
- `    "wildy_imp" to CustomDefinition.Model(modelId = 60401),` (paired: `wildy_imp/` dir)
- the two `gano_beast_1`/`gano_beast_2` lines (62000/62001; paired: `ganodermic_beasts/`)
- the two `origins_wildy_mole`/`origins_wildy_mole_pet_inv` lines (64105/64106; paired: the two customs/models files)
- **optional** (no backing `.dat` exists anywhere — verified): `"polypore_spores"` (60039) and the `"polypore_staff_degraded_equip"`/`"..._drop"` pair (60040/60041). **Keep** `polypore_staff_equip`/`drop` (60042/60043 — item 32042 stays).

**Keep** (files live in surviving dirs — deleting the entry would break `defaultModels()`): `corrupt_kratos_*` (60366+), `dark_*`, `fissile_*`, `origins_primal_workbench` (64097), `origins_nx_store_bundle_inv` (64104), all `origins_*` customs entries.

### B-8. Misc cache-side code

`cache/src/main/java/mgi/tools/dumpers/StructDumper.java` — FIND:
```java
                if (t.getId() == 10300) {
```
REPLACE (retarget the debug dumper to a surviving custom struct):
```java
                if (t.getId() == 10321) {
```

`cache/src/main/kotlin/org/jesse/cache/draw/RuneScapeImageRender.kt` — delete the commented line:
```kotlin
//    val npc = NPCDefinitions.get(CustomNpcId.GANODERMIC_BEAST)
```

Teleport-category comment tidy (optional but included): delete
- `cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/categories/bosses.kt`: `    //"Slash Bash"(-32825, 2436, 4382, 0, "")`
- `.../categories/training.kt`: the three commented `Desert/Ice/Jungle Strykewyrms` lines

---

## Part C — asset deletions (last, per removal-order rule)

```bash
# Ganodermic (181 files)
git rm -r cache/assets/osnr/ganodermic_beasts/

# Origins boss dirs (4,951 files total)
git rm -r cache/assets/origins/strykewyrms/ cache/assets/origins/balance_elementals/ \
          cache/assets/origins/bork/ cache/assets/origins/plane_freezer/ \
          cache/assets/origins/nomad/ cache/assets/origins/phoenix/ \
          cache/assets/origins/slash_bash/ cache/assets/origins/barrelchest/ \
          cache/assets/origins/wildy_imp/

# Origins pets dir — the five dying files ONLY (dark_*/fissile_*/corrupt_kratos_*/origins_primal_workbench stay)
git rm cache/assets/origins/pets/models/pet_barrelchest.dat \
       cache/assets/origins/pets/models/pet_desert_strykewyrm_inv.dat \
       cache/assets/origins/pets/models/pet_ice_strykewyrm_inv.dat \
       cache/assets/origins/pets/models/pet_jungle_strykewyrm_inv.dat \
       cache/assets/origins/pets/models/pet_slash_bash_inv.dat

# Origins customs dir — wild-mole files ONLY
git rm cache/assets/origins/customs/models/origins_wildy_mole.dat \
       cache/assets/origins/customs/models/origins_wildy_mole_pet_inv.dat

# Custom-items models + gano boss-log struct
git rm cache/assets/osnr/custom_items/models/booster_ganodermic.dat \
       cache/assets/osnr/custom_items/models/ancient_eye.dat \
       cache/assets/structs/10300
```

**TOML block deletions.** A "block" = the `# comment` line(s) directly above a `[[item]]` header, the header, and every line up to the next block's comment/header (or EOF). The dry-run parses blocks with exactly this rule and asserts each id below appears in exactly one block.

`cache/assets/osnr/custom_items/item_config/definitions.toml` — delete the blocks for ids:
**32002, 32003, 33002** (Ancient eye + noted + placeholder), **32038, 33038** (Polypore spores + placeholder), **32040, 32041, 33040** (Polypore staff (deg) + noted + placeholder), **32150, 33150** (Ganodermic booster + placeholder), **32821–32825** (wyrm/barrelchest/slash-bash pets), **32855, 32856** (Wildy imp / Off-balance elemental pets — both already ship broken `invmodel=-1`).
**KEEP:** 32042/32043/33042 (charged Polypore staff — the 32040 blocks sit directly adjacent, be precise) and 32954 (Primal components).

`cache/assets/types/pets.toml` — delete the `# Ganodermic runt` block (`id=32100`, runs to EOF).

---

## Part D — dry-run gate (run BEFORE any edit)

Save as `dryrun_stage2.py` in the repo root, run with `python3 dryrun_stage2.py`. Verified CLEAN against `cd479db0`. It asserts: every FIND above matches its file exactly once; the B-3 trim anchors are unique and no kept token (`DRIFTER`, `POSTIE_PETE`, `dark_`, `fissile_`, `32884`) sits inside a removal region; every deletion target exists with the expected asset counts; the partial-dir survivors are on disk and NOT in the manifest; the TOML blocks parse with removed ids present exactly once and kept ids intact; `RegularTask.RATS` (the slayer-guard substitute) exists; and the two hiscores files plus `PackerExt.kt` are outside the edit set.

```python
#!/usr/bin/env python3
"""Stage 2 dry-run gate. Run from the repo root: python3 dryrun_stage2.py
Asserts every FIND block matches its file exactly once, every deletion target
exists, region anchors are unique, TOML blocks are well-formed, and the
keep-guards hold. Exit 0 = CLEAN."""
import re, sys, pathlib

fail = 0
def err(tag, msg):
    global fail
    print(f"[{tag}] {msg}"); fail += 1

# ---------------------------------------------------------------- FIND blocks
FINDS = [
    # A1 — WildernessSlayerModule (framework's one external consumer)
    ("content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/slayer/WildernessSlayerModule.kt",
     "import org.jesse.game.content.wilderness.event.hot_zone.WildernessHotZoneEvent\n", "A1-a"),
    ("content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/slayer/WildernessSlayerModule.kt",
     "        if (WildernessHotZoneEvent.inHotZone(player)) {\n            bloodMoneyRewardAmount *= 2\n            player.sendMessage(\"You received double blood money for completing the task in a hot zone.\")\n        }\n", "A1-b"),
    # A2 — broadcast orphans
    ("engine/src/main/java/org/jesse/game/world/broadcasts/BroadcastType.java",
     "    WILDERNESS_EVENT(\"B22222\", 68, Optional.empty()),\n", "A2-a"),
    ("engine/src/main/java/org/jesse/game/world/broadcasts/WorldBroadcasts.java",
     "//            case WILDERNESS_EVENT -> sendGanodermicBeast(args);\n", "A2-b"),
    ("engine/src/main/java/org/jesse/game/world/broadcasts/WorldBroadcasts.java",
     "            case WILDERNESS_EVENT: {\n                builder.append(\"Event: \");\n                secondaryBuilder.append(args[0]);\n                builder.append(secondaryBuilder);\n                break;\n            }\n", "A2-c"),
    # A3 — ganodermic engine refs
    ("core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt",
     "const val GANODERMIC_BEAST = 14696\nconst val GANODERMIC_RUNT = 14698\nconst val GANODERMIC_RUNT_32000 = 32000// pet\n", "A3-npcid"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val ANCIENT_EYE = 32002\n", "A3-i1"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val POLYPORE_SPORES = 32038\n", "A3-i2"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val POLYPORE_STAFF_DEG = 32040\n", "A3-i3"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val GANODERMIC_RUNT = 32100\n", "A3-i4"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val GANODERMIC_BOOSTER = 32150\n", "A3-i5"),
    ("core-model/src/main/kotlin/org/jesse/game/content/universalshop/UniversalShopTable.kt",
     "                UnivShopItem(32150, buyPrice = 150),\n", "A3-shop1"),
    ("core-model/src/main/kotlin/org/jesse/game/content/universalshop/UniversalShopTable.kt",
     "                UnivShopItem(32150, buyPrice=5),\n", "A3-shop2"),
    ("content/other/unused/src/main/kotlin/org/jesse/game/content/unused/universalshop/UnivShopTable.kt",
     "                UnivShopItem(32150, buyPrice = 150),\n", "A3-shop3"),
    ("content/other/unused/src/main/kotlin/org/jesse/game/content/unused/universalshop/UnivShopTable.kt",
     "                UnivShopItem(32150, buyPrice=5),\n", "A3-shop4"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvpMysteryBox.java",
     "                new MysterySupplyItem(32150, 3, 5), // Ganodermic Booster\n", "A3-box1"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/RegalMysteryBox.java",
     "                new MysterySupplyItem(32150, 2, 5), // Ganodermic Booster\n", "A3-box2"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/SuperMysteryBox.java",
     "                new MysteryItem(32150, 3, 6, 1000), // Gano booster\n", "A3-box3"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/UltimateMysteryBox.java",
     "//                new MysteryItem(32150, 5, 10, 1000), // Ganodermic Booster\n", "A3-box4"),
    ("engine/src/main/kotlin/org/jesse/game/content/bountyhunter/crate/BountyCrate.kt",
     "          else if (Random.nextInt(16) == 0) Item(32150) // Gano Booster\n", "A3-crate"),
    ("content/other/death-mechanics/src/main/kotlin/org/jesse/plugins/item/actions/death_items/kept_items.kt",
     "            32149, 32150, 32151, 32152, 32153, 32154, 32155, 32156,\n", "A3-kept"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     "    private const val ganoBooster = GANODERMIC_BOOSTER\n", "A3-clr1"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     "    private const val ganodermicBeast = 10300\n", "A3-clr2"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     "                CollectionLogReward(\n                    ganodermicBeast,\n                    arrayOf(dpin25 x 1, ultraMB x 1, bloodMoney x 5_000, ganoBooster x 5)\n                ),\n", "A3-clr3"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     "        info.add(\"Ganodermic booster: \" + (vars.getGanoBoosterKillsLeft() > 0 ? green + \"Active for \" + vars.getGanoBoosterKillsLeft() + \" kills\" : red + \"Inactive\"));\n", "A3-info"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java",
     "        ganoBoosterKillsLeft = copy.ganoBoosterKillsLeft;\n", "A3-pv1"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java",
     "    private int ganoBoosterKillsLeft;\n", "A3-pv2"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java",
     "    public int getGanoBoosterKillsLeft() {\n        return ganoBoosterKillsLeft;\n    }\n\n    public void setGanoBoosterKillsLeft(int ganoBoosterKillsLeft) {\n        this.ganoBoosterKillsLeft = ganoBoosterKillsLeft;\n    }\n\n", "A3-pv3"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/NotificationSettings.java",
     "        \"ganodermic beast\", \"lizardman\",", "A3-notif"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/collectionlog/CollectionLogCategoryType.java",
     "    GANODERMIC_BEAST(-1, player -> get(player, \"Ganodermic Beast\")),\n", "A3-clcat"),
    ("engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java",
     "    GANODERMIC_RUNT(32100, 14698),\n", "A3-pet"),
    ("engine/src/main/java/org/jesse/game/model/item/enums/RareDrop.java",
     "    //Ganodermic beast\n    GANODERMIC_RUNT(ItemId.GANODERMIC_RUNT),\n\n", "A3-rare"),
    ("engine/src/main/java/org/jesse/game/world/entity/npc/spawns/NPCSpawnLoader.java",
     "        dropViewerNPCs.add(NpcId.GANODERMIC_BEAST);\n", "A3-dropv"),
    ("engine/src/main/kotlin/org/jesse/game/content/commands/CustomCommands.kt",
     "                        add(Item(GANODERMIC_RUNT, 10))\n", "A3-cc"),
    # A4 — revival skulls
    ("engine/src/main/java/org/jesse/game/world/entity/player/PlayerSkulls.java",
     "    BLACK_SKULL(20),\n\n    REVIVE_1(21),\n    REVIVE_2(22),\n    REVIVE_3(23),\n    REVIVE_4(24),\n    REVIVE_5(25),\n    REVIVE_6(26),\n    REVIVE_7(27),\n    REVIVE_8(28),\n    REVIVE_9(29),\n    REVIVE_10(30);\n", "A4-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/PlayerSkulls.java",
     "        final int revivalTimer = PlayerAttributesKt.getPvmArenaRevivalCount(player);\n        if (revivalTimer > 0) {\n            PlayerSkulls skull = PlayerSkulls.valueOf(\"REVIVE_\" + revivalTimer);\n            return skull.skullStatusId;\n        }\n\n", "A4-b"),
    # A5 — origins engine refs
    ("engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java",
     "    PET_JUNGLE_STRYKEWYRM(ItemId.PET_JUNGLE_STRYKEWYRM, NpcId.BABY_JUNGLE_STRYKEWYRM),\n    PET_ICE_STRYKEWYRM(ItemId.PET_ICE_STRYKEWYRM, NpcId.BABY_ICE_STRYKEWYRM),\n    PET_DESERT_STRYKEWYRM(ItemId.PET_DESERT_STRYKEWYRM, NpcId.BABY_DESERT_STRYKEWYRM),\n    PET_SLASH_BASH(ItemId.PET_SLASH_BASH, NpcId.LIL_SLASH_BASH),\n    PET_AGED_BARRELCHEST(ItemId.PET_BARRELCHEST, NpcId.BABY_AGED_BARRELCHEST),\n", "A5-pets1"),
    ("engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java",
     "    PET_WILDY_IMP           (ItemId.PET_WILDY_IMP,           NpcId.LIL_WILDY_IMP),\n    OFF_BALANCE_ELEMENTAL           (ItemId.OFF_BALANCE_ELEMENTAL,           NpcId.OFF_BALANCE_ELEMENTAL),\n    BABY_WILD_MOLE(ItemId.PET_BABY_WILD_MOLE, NpcId.BABY_WILD_MOLE),\n", "A5-pets2"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/RegularTask.kt",
     "    STRYKEWYRMS(\n        buildTasks {\n            SlayerMaster.VANNAKA weight 5 min 20 max 50\n            SlayerMaster.CHAELDAR weight 10 min 30 max 70\n            SlayerMaster.NIEVE weight 7 min 70 max 85\n            SlayerMaster.DURADEL weight 15 min 75 max 125\n        },\n        buildInfo {\n            id(82)\n            slayer(90)\n            combat(65)\n            tip(\"Strykewyrms are powerful creatures from an alternate dimension\")\n            registerNames(\"Ice Strykewyrm\", \"Desert Strykewyrm\", \"Jungle Strykewyrm\")\n        }\n    ),\n", "A5-task"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/Assignment.kt",
     "        taskName = old.taskName.stripSumona()\n        task = getTask(taskName)\n", "A5-guard-site"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/NotificationSettings.java",
     "\"sol heredit\", \"strykewyrm\");", "A5-notif1"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/NotificationSettings.java",
     "\"drake\", \"strykewyrm\");", "A5-notif2"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/AhrimTheBlighted.java",
     "import static org.jesse.game.npc.ids.NpcId.DI_AHRIM_THE_BLIGHTED;\n", "A5-w1i"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/AhrimTheBlighted.java",
     ", DI_AHRIM_THE_BLIGHTED", "A5-w1c"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/DharokTheWretched.java",
     "import static org.jesse.game.npc.ids.NpcId.DI_DHAROK_THE_WRETCHED;\n", "A5-w2i"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/DharokTheWretched.java",
     ", DI_DHAROK_THE_WRETCHED", "A5-w2c"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/GuthanTheInfested.java",
     "import static org.jesse.game.npc.ids.NpcId.DI_GUTHAN_THE_INFESTED;\n", "A5-w3i"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/GuthanTheInfested.java",
     ", DI_GUTHAN_THE_INFESTED", "A5-w3c"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/KarilTheTainted.java",
     "import static org.jesse.game.npc.ids.NpcId.DI_KARIL_THE_TAINTED;\n", "A5-w4i"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/KarilTheTainted.java",
     ", DI_KARIL_THE_TAINTED", "A5-w4c"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/ToragTheCorrupted.java",
     "import static org.jesse.game.npc.ids.NpcId.DI_TORAG_THE_CORRUPTED;\n", "A5-w5i"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/ToragTheCorrupted.java",
     ", DI_TORAG_THE_CORRUPTED", "A5-w5c"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/VeracTheDefiled.java",
     "import static org.jesse.game.npc.ids.NpcId.DI_VERAC_THE_DEFILED;\n", "A5-w6i"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/VeracTheDefiled.java",
     ", DI_VERAC_THE_DEFILED", "A5-w6c"),
    ("engine/src/main/kotlin/org/jesse/game/content/commands/DeveloperCommands.kt",
     "        Command(PlayerPrivilege.TRUE_DEVELOPER, \"barrelchest\") { player, args  ->\n            if(isOwner(player)) {\n                player.teleport(Location(1887, 2717, 3))\n            } else {\n                player.sendMessage(\"Try again next time.\")\n            }\n        }\n\n", "A5-bc"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     "    private const val strykeWyrms = 10500\n", "A5-clr1"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     "    private const val wildMole = 10502\n", "A5-clr2"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     "                CollectionLogReward(strykeWyrms, arrayOf(superMB x 1, standardMB x 1)),\n", "A5-clr3"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     "                CollectionLogReward(wildMole, arrayOf(ultraMB x 1, superMB x 2, standardMB x 3)),\n", "A5-clr4"),
    ("core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt",
     "const val ICE_STRYKEWYRM = 16077\nconst val JUNGLE_STRYKEWYRM = 16078\nconst val DESERT_STRYKEWYRM = 16079\nconst val WILDY_WYRM = 16080\nconst val BALANCE_ELEMENTAL_MELEE = 16081\nconst val BALANCE_ELEMENTAL_RANGED = 16082\nconst val BALANCE_ELEMENTAL_MAGIC = 16083\nconst val BORK = 16084\nconst val PLANE_FREEZER_LAKHRAHNAZ = 16085\nconst val NOMAD_16086 = 16086\nconst val PHOENIX_16087 = 16087\nconst val SLASH_BASH_16088 = 16088\nconst val AGED_BARRELCHEST = 16089\nconst val BABY_JUNGLE_STRYKEWYRM = 16090\nconst val BABY_ICE_STRYKEWYRM = 16091\nconst val BABY_DESERT_STRYKEWYRM = 16092\nconst val BABY_AGED_BARRELCHEST = 16093\nconst val LIL_SLASH_BASH = 16094\n", "A5-npc1"),
    ("core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt",
     "const val WILDY_IMP = 16124\nconst val LIL_WILDY_IMP = 16125\nconst val OFF_BALANCE_ELEMENTAL = 16126\nconst val DI_AHRIM_THE_BLIGHTED = 16127\nconst val DI_DHAROK_THE_WRETCHED = 16128\nconst val DI_GUTHAN_THE_INFESTED = 16129\nconst val DI_KARIL_THE_TAINTED = 16130\nconst val DI_TORAG_THE_CORRUPTED = 16131\nconst val DI_VERAC_THE_DEFILED = 16132\n", "A5-npc2"),
    ("core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt",
     "const val WILD_MOLE = 16134\nconst val BABY_WILD_MOLE = 16135\n", "A5-npc3"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val PET_JUNGLE_STRYKEWYRM = 32821\nconst val PET_ICE_STRYKEWYRM = 32822\nconst val PET_DESERT_STRYKEWYRM = 32823\nconst val PET_BARRELCHEST = 32824\nconst val PET_SLASH_BASH = 32825\n", "A5-item1"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val PET_WILDY_IMP = 32855\nconst val OFF_BALANCE_ELEMENTAL = 32856\n", "A5-item2"),
    ("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
     "const val PET_BABY_WILD_MOLE = 32957\n", "A5-item3"),
    # A6 — CoX fix
    ("engine/src/main/java/org/jesse/game/content/chambersofxeric/room/CreatureKeeperRoom.java",
     "                        final int intAmount = amount.intValue();\n                        final int amountToAdd = Math.max(0, Math.min(intAmount, CAP_MAXIMUM_CAVERN_GRUBS_STACK - amountInInventory));\n                        if (amountToAdd > 0) {\n                            player.getSkills().addXp(SkillConstants.THIEVING, amountToAdd == 1 ? 40 : amountToAdd == 2 ? 60 : 73);\n                            player.getInventory().addItem(new Item(ItemId.CAVERN_GRUBS, intAmount));\n                        }\n", "A6"),
    # B1 — TypeParser
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "import org.jesse.cache_tool.packing.custom.NearRealityCustomAnimationsPacker;\n", "B1-i1"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "import org.jesse.cache_tool.packing.custom.NearRealityRaidsItemDefinitions;\n", "B1-i2"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "import org.jesse.cache_tool.packing.custom.ganodermic_beasts.GanodermicBeastsPacker;\n", "B1-i3"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "        NearRealityCustomAnimationsPacker.pack();\n", "B1-c1"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "        NearRealityRaidsItemDefinitions.makeKindlingStackable();\n        NearRealityRaidsItemDefinitions.makeCavernGrubsStackable();\n", "B1-c2"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "        AnimationBase.pack();\n        GanodermicBeastsPacker.pack();\n", "B1-c3"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "//        packMapPre209(6954,\n//                \"assets/map/origin_update/desert_island/ls.dat\",\n//                \"assets/map/origin_update/desert_island/obj.dat\");\n//        packMap(13625,\n//                \"assets/map/origin_update/edge_island/island2_ls2.dat\",\n//                \"assets/map/origin_update/edge_island/island2_obj2.dat\");\n//        packMap(13369, // edited with land bridge\n//                \"assets/map/origin_update/edge_island/wildy2_ls2.dat\",\n//                \"assets/map/origin_update/edge_island/wildy2_obj2.dat\");\n//        packMapPre209(7210,\n//                \"assets/map/origin_update/ice_island/ls.dat\",\n//                \"assets/map/origin_update/ice_island/obj.dat\");\n//        packMapPre209(7209,\n//                \"assets/map/origin_update/jungle_island/ls.dat\",\n//                \"assets/map/origin_update/jungle_island/obj.dat\");\n//\n//        packMapPre209(7466,\n//                \"assets/map/origin_update/barrelchest/ls.dat\",\n//                \"assets/map/origin_update/barrelchest/obj.dat\");\n", "B1-maps"),
    # B3 — origins packer trim (exact blocks + region anchors)
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityOriginsPacker.kt",
     "import net.runelite.api.NpcID.BABY_MOLE\nimport net.runelite.api.NpcID.GIANT_MOLE_6499\nimport net.runelite.api.NpcID.AHRIM_THE_BLIGHTED\nimport net.runelite.api.NpcID.DHAROK_THE_WRETCHED\nimport net.runelite.api.NpcID.GUTHAN_THE_INFESTED\nimport net.runelite.api.NpcID.KARIL_THE_TAINTED\nimport net.runelite.api.NpcID.TORAG_THE_CORRUPTED\nimport net.runelite.api.NpcID.VERAC_THE_DEFILED\n", "B3-imports"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityOriginsPacker.kt",
     "    @JvmStatic fun pack() {\n        packStrykewyrms()\n        packBalanceElementals()\n        packBork()\n        packPlaneFreezer()\n        packNomad()\n        packPhoenix()\n        packSlashBash()\n        packBarrelchest()\n        packPets()\n        packWildyImp()\n        packCustoms()\n        packBarrowsCopies()\n        packWildMole()\n    }\n", "B3-pack"),
    # B4 — enums packer
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "            this.values[idx++] = 10300  // gano\n", "B4-1"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "            this.values[idx++] = 10500  // strykewyrms\n", "B4-2"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "            this.values[idx++] = 10502  // Wild Mole\n", "B4-3"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "        // All Pets log\n        EnumDefinitions.get(2158).apply {\n            var index = this.size\n            this.values[index] = GANODERMIC_RUNT\n            this.pack()\n        }\n\n", "B4-4"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "        // Ganodermic Beast\n        EnumDefinitions.create(10025, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {\n            this.values[1] = ANCIENT_EYE\n            this.values[2] = DRAGON_KITE\n            this.values[3] = PVP_MYSTERY_BOX\n            this.values[4] = POLYPORE_SPORES\n            this.pack()\n        }\n\n", "B4-5"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "        /* Strykewyrms */\n        EnumDefinitions.create(10500, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {\n            this.values[0] = STAFF_OF_LIGHT\n            this.values[1] = CHAOTIC_CROSSBOW\n            this.values[2] = CHAOTIC_KITESHIELD\n            this.values[3] = EAGLE_EYE_KITESHIELD\n            this.values[4] = FARSEER_KITESHIELD\n            this.pack()\n        }\n\n", "B4-6"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "        /* Wild Mole */\n        EnumDefinitions.create(10502, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {\n            this.values[0] = GHOSTLY_PARTYHAT\n            this.values[1] = DEMONHORN_NECKLACE\n            this.values[2] = DEATH_CAPE\n            this.values[3] = DRAGON_KITE\n            this.values[4] = SPIRIT_CAPE\n            this.values[5] = MERCENARY_GLOVES\n            this.values[6] = MYSTERY_BOX\n            this.values[7] = SKILLING_MYSTERY_BOX\n            this.values[8] = PRIMAL_FULL_HELM\n            this.values[9] = PRIMAL_CHAINBODY\n            this.values[10] = PRIMAL_PLATESKIRT\n            this.values[11] = PRIMAL_PLATELEGS\n            this.values[12] = PRIMAL_BOOTS\n            this.values[13] = PRIMAL_GAUNTLETS\n            this.pack()\n        }\n\n", "B4-7"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "        /* Slayer Task Defs - 82 replaces unused Gorak */\n        EnumDefinitions.get(693).apply {\n            this.values[82] = \"Strykewyrms\"\n            this.pack()\n        }\n\n", "B4-8"),
    # B5 — structs packer
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomStructsPacker.kt",
     "        StructDefinitions.get(500).copy(10500).apply {\n            this.parameters[689] = \"Strykewyrms\"\n            this.parameters[690] = 10500\n            this.pack()\n        }\n\n", "B5-1"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomStructsPacker.kt",
     "\n        StructDefinitions.get(500).copy(10502).apply {\n            this.parameters[689] = \"Wild Mole\"\n            this.parameters[690] = 10502\n            this.pack()\n        }\n", "B5-2"),
    # B6 — worldmap
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomWorldMapPacker.kt",
     "        2500.createSmallMapLabel(\"Here be some<br>wild moles\")\n", "B6"),
    # B7 — model map
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"ancient_eye\" to CustomDefinition.Model(60000),\n", "B7-1"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"booster_ganodermic\" to CustomDefinition.Model(modelId = 60137),\n", "B7-2"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"desert_strykewyrm_a\" to CustomDefinition.Model(modelId = 60338),\n    \"desert_strykewyrm_b\" to CustomDefinition.Model(modelId = 60339),\n    \"ice_strykewyrm_a\" to CustomDefinition.Model(modelId = 60340),\n    \"ice_strykewyrm_b\" to CustomDefinition.Model(modelId = 60341),\n    \"jungle_strykewyrm_a\" to CustomDefinition.Model(modelId = 60342),\n    \"jungle_strykewyrm_b\" to CustomDefinition.Model(modelId = 60343),\n    \"wildy_wyrm_a\" to CustomDefinition.Model(modelId = 60344),\n    \"wildy_wyrm_b\" to CustomDefinition.Model(modelId = 60345),\n    \"balance_elemental_body\" to CustomDefinition.Model(modelId = 60346),\n    \"balance_elemental_head\" to CustomDefinition.Model(modelId = 60347),\n    \"bork_a\" to CustomDefinition.Model(modelId = 60348),\n    \"bork_b\" to CustomDefinition.Model(modelId = 60349),\n    \"plane_freezer\" to CustomDefinition.Model(modelId = 60350),\n    \"plane_freezer_ranged_proj\" to CustomDefinition.Model(modelId = 60351), /* These models are encoded in binary, do NOT move id */\n    \"plane_freezer_magic_proj\" to CustomDefinition.Model(modelId = 60352), /* These models are encoded in binary, do NOT move id */\n    \"nomad_npc_body\" to CustomDefinition.Model(modelId = 60353),\n    \"nomad_magic_projectile\" to CustomDefinition.Model(modelId = 60354), /* These models are encoded in binary, do NOT move id */\n    \"nomad_ranged_projectile\" to CustomDefinition.Model(modelId = 60355), /* These models are encoded in binary, do NOT move id */\n    \"nomad_magic_send_gfx\" to CustomDefinition.Model(modelId = 60356), /* These models are encoded in binary, do NOT move id */\n    \"nomad_target_hit_gfx\" to CustomDefinition.Model(modelId = 60357), /* These models are encoded in binary, do NOT move id */\n    \"phoenix_body\" to CustomDefinition.Model(modelId = 60358),\n    \"slash_bash\" to CustomDefinition.Model(modelId = 60359),\n    \"barrelchest_old\" to CustomDefinition.Model(modelId = 60360),\n    \"pet_barrelchest\" to CustomDefinition.Model(modelId = 60361),\n    \"pet_desert_strykewyrm_inv\" to CustomDefinition.Model(modelId = 60362),\n    \"pet_ice_strykewyrm_inv\" to CustomDefinition.Model(modelId = 60363),\n    \"pet_jungle_strykewyrm_inv\" to CustomDefinition.Model(modelId = 60364),\n    \"pet_slash_bash_inv\" to CustomDefinition.Model(modelId = 60365),\n", "B7-3"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"wildy_imp\" to CustomDefinition.Model(modelId = 60401),\n", "B7-4"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"gano_beast_1\" to CustomDefinition.Model(modelId = 62000),\n    \"gano_beast_2\" to CustomDefinition.Model(modelId = 62001),\n", "B7-5"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"origins_wildy_mole\" to CustomDefinition.Model(modelId = 64105),\n    \"origins_wildy_mole_pet_inv\" to CustomDefinition.Model(modelId = 64106),\n", "B7-6"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"polypore_spores\" to CustomDefinition.Model(60039),\n", "B7-opt1"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt",
     "    \"polypore_staff_degraded_equip\" to CustomDefinition.Model(60040),\n    \"polypore_staff_degraded_drop\" to CustomDefinition.Model(60041),\n", "B7-opt2"),
    # B8 — misc cache code
    ("cache/src/main/java/mgi/tools/dumpers/StructDumper.java",
     "                if (t.getId() == 10300) {\n", "B8-1"),
    ("cache/src/main/kotlin/org/jesse/cache/draw/RuneScapeImageRender.kt",
     "//    val npc = NPCDefinitions.get(CustomNpcId.GANODERMIC_BEAST)\n", "B8-2"),
    ("cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/categories/bosses.kt",
     "    //\"Slash Bash\"(-32825, 2436, 4382, 0, \"\")\n", "B8-3"),
    ("cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/categories/training.kt",
     "    //\"Desert Strykewyrms\"(-32823, 1761, 2714, 0,  \"\")\n", "B8-4"),
    ("cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/categories/training.kt",
     "    //\"Ice Strykewyrms\"(-32822, 1824, 2714, 0, \"\")\n", "B8-5"),
    ("cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/categories/training.kt",
     "    //\"Jungle Strykewyrms\"(-32821, 1826, 2650, 0, \"\")\n", "B8-6"),
]

for path, needle, tag in FINDS:
    p = pathlib.Path(path)
    if not p.exists():
        err(tag, f"MISSING FILE: {path}"); continue
    n = p.read_text(encoding="utf-8").count(needle)
    if n != 1:
        err(tag, f"FIND matched {n} times (expected 1) in {path}")

# ------------------------------------------------- region anchors (B3 trim)
ORIGINS = pathlib.Path("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityOriginsPacker.kt")
otext = ORIGINS.read_text(encoding="utf-8") if ORIGINS.exists() else ""
for anchor, tag in [
    ("    private fun packWildMole() {", "B3-R1-start"),
    ("    @JvmStatic fun packPets() {", "B3-R1-end"),
    ("            .initialize(16090, 1, 60342, 60343)", "B3-R2-start"),
    ("        NPCDefinitions.get(NpcID.POSTIE_PETE).clone().toBuilder()", "B3-R2-end"),
    ("    @JvmStatic fun packBarrelchest() {", "B3-R3-start"),
    ("    @JvmStatic fun packCustoms() {", "B3-R3-end"),
]:
    n = otext.count(anchor)
    if n != 1:
        err(tag, f"anchor matched {n} times (expected 1)")
# kept content must NOT sit inside removal regions
def region(text, start, end):
    i = text.find(start); j = text.find(end)
    return text[i:j] if 0 <= i < j else ""
r1 = region(otext, "    private fun packWildMole() {", "    @JvmStatic fun packPets() {")
r3 = region(otext, "    @JvmStatic fun packBarrelchest() {", "    @JvmStatic fun packCustoms() {")
for kept in ["DRIFTER", "POSTIE_PETE", "dark_", "fissile_", "32884"]:
    if kept in r1 or kept in r3:
        err("B3-keep", f"kept token '{kept}' found inside a removal region")

# ---------------------------------------------------------------- deletions
DELETIONS = [
    "content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/event",
    "content/bosses/origins",
    "engine/src/main/java/org/jesse/plugins/dialogue/GanodermicRuntD.java",
    "engine/src/main/java/org/jesse/game/world/region/area/wilderness/WildMoleArea.java",
    "cache/data/npcs/combat/14696.npc.json",
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/ganodermic_beasts",
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityRaidsItemDefinitions.kt",
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomAnimationsPacker.kt",
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityVanillaMapEdits.kt",
    "cache/assets/osnr/ganodermic_beasts",
    "cache/assets/origins/strykewyrms",
    "cache/assets/origins/balance_elementals",
    "cache/assets/origins/bork",
    "cache/assets/origins/plane_freezer",
    "cache/assets/origins/nomad",
    "cache/assets/origins/phoenix",
    "cache/assets/origins/slash_bash",
    "cache/assets/origins/barrelchest",
    "cache/assets/origins/wildy_imp",
    "cache/assets/origins/pets/models/pet_barrelchest.dat",
    "cache/assets/origins/pets/models/pet_desert_strykewyrm_inv.dat",
    "cache/assets/origins/pets/models/pet_ice_strykewyrm_inv.dat",
    "cache/assets/origins/pets/models/pet_jungle_strykewyrm_inv.dat",
    "cache/assets/origins/pets/models/pet_slash_bash_inv.dat",
    "cache/assets/origins/customs/models/origins_wildy_mole.dat",
    "cache/assets/origins/customs/models/origins_wildy_mole_pet_inv.dat",
    "cache/assets/osnr/custom_items/models/booster_ganodermic.dat",
    "cache/assets/osnr/custom_items/models/ancient_eye.dat",
    "cache/assets/structs/10300",
]
for path in DELETIONS:
    if not pathlib.Path(path).exists():
        err("DEL", f"MISSING: {path}")

ASSET_DIRS = [
    ("cache/assets/osnr/ganodermic_beasts", 181),
    ("cache/assets/origins/strykewyrms", 243),
    ("cache/assets/origins/balance_elementals", 757),
    ("cache/assets/origins/bork", 573),
    ("cache/assets/origins/plane_freezer", 721),
    ("cache/assets/origins/nomad", 775),
    ("cache/assets/origins/phoenix", 552),
    ("cache/assets/origins/slash_bash", 1159),
    ("cache/assets/origins/barrelchest", 170),
    ("cache/assets/origins/wildy_imp", 1),
]
for d, expected in ASSET_DIRS:
    p = pathlib.Path(d)
    if p.exists():
        count = sum(1 for f in p.rglob("*") if f.is_file())
        if count != expected:
            err("ASSET", f"{d}: {count} files (expected {expected}) — re-verify before deleting")

# survivors that must NOT be in the deletion manifest
for surv in [
    "cache/assets/origins/customs", "cache/assets/origins/pets",
    "cache/assets/origins/pets/models/dark_seren.dat",
    "cache/assets/origins/pets/models/fissile_kratos_body.dat",
    "cache/assets/origins/pets/models/corrupt_kratos_body.dat",
    "cache/assets/origins/pets/models/origins_primal_workbench.dat",
]:
    if surv in DELETIONS:
        err("KEEP", f"survivor listed for deletion: {surv}")
    if not pathlib.Path(surv).exists():
        err("KEEP", f"expected survivor missing from tree: {surv}")

# ------------------------------------------------------------- TOML blocks
def toml_blocks(path):
    """Split a toml file into (start_line_idx, lines) blocks, each beginning at
    the comment line(s) directly above a [[item]] header."""
    lines = pathlib.Path(path).read_text(encoding="utf-8").splitlines(keepends=True)
    starts = []
    for i, ln in enumerate(lines):
        if ln.strip() == "[[item]]":
            j = i
            while j > 0 and lines[j-1].lstrip().startswith("#"):
                j -= 1
            starts.append(j)
    blocks = []
    for k, s in enumerate(starts):
        e = starts[k+1] if k+1 < len(starts) else len(lines)
        blocks.append((s, lines[s:e]))
    return blocks

DEFS = "cache/assets/osnr/custom_items/item_config/definitions.toml"
REMOVE_IDS = {32002, 32003, 33002, 32038, 33038, 32040, 32041, 33040,
              32150, 33150, 32821, 32822, 32823, 32824, 32825, 32855, 32856}
KEEP_IDS = {32042, 32043, 33042, 32954}
seen = {}
for s, blk in toml_blocks(DEFS):
    m = next((re.match(r"id=(\d+)\s*$", ln.strip()) for ln in blk if ln.strip().startswith("id=")), None)
    if m:
        seen.setdefault(int(m.group(1)), 0)
        seen[int(m.group(1))] += 1
for rid in REMOVE_IDS:
    if seen.get(rid, 0) != 1:
        err("TOML", f"definitions.toml id={rid}: {seen.get(rid,0)} blocks (expected 1)")
for kid in KEEP_IDS:
    if seen.get(kid, 0) != 1:
        err("TOML-KEEP", f"definitions.toml kept id={kid}: {seen.get(kid,0)} blocks (expected 1) — verify before editing")

PETS_TOML = "cache/assets/types/pets.toml"
pt = pathlib.Path(PETS_TOML).read_text(encoding="utf-8")
if pt.count("id=32100") != 1 or "# Ganodermic runt" not in pt:
    err("TOML", "pets.toml gano block (id=32100) not found exactly once")

# --------------------------------------------------------------- guards
if "RATS(" not in pathlib.Path("engine/src/main/kotlin/org/jesse/game/content/slayer/RegularTask.kt").read_text(encoding="utf-8"):
    err("GUARD", "RegularTask.RATS (slayer-guard substitute) not found")
for f in ["engine/src/main/java/org/jesse/game/content/hiscores/HiscoresCategory.java",
          "engine/src/main/java/org/jesse/game/content/hiscores/HiscoresCategoryEntry.java"]:
    if "GANODERMIC" not in pathlib.Path(f).read_text(encoding="utf-8"):
        err("GUARD", f"{f}: expected GANODERMIC entry to still exist (index-coupled keep — do NOT edit)")
edited = {p for p, _, _ in FINDS}
for banned in ["engine/src/main/java/org/jesse/game/content/hiscores/HiscoresCategory.java",
               "engine/src/main/java/org/jesse/game/content/hiscores/HiscoresCategoryEntry.java",
               "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/PackerExt.kt"]:
    if banned in edited:
        err("GUARD", f"{banned} must not be in the edit set")

sys.exit(1 if fail else print("DRY RUN CLEAN — Stage 2 FIND blocks unique, deletion targets present, guards hold") or 0)
```

Delete `dryrun_stage2.py` in the same commit as the executed work (self-cleaning model).

---

## Part E — execution order & verification gates

1. `python3 dryrun_stage2.py` → must print CLEAN. Any failure = stop, report the tag.
2. Apply Part A (A-6 CoX fix included) → **compile gate #1:** `./gradlew compileJava compileKotlin` — must succeed.
3. Apply Part B → **compile gate #2:** same command (catches leftover origins-packer imports and the deleted framework's stragglers).
4. Apply Part C (git rm + TOML edits) → belt-and-braces compile once more.
5. **Post-grep gates** (zero hits unless stated; `PROVENANCE_nr.txt` always excluded):
   ```
   git grep -in "ganodermic" -- '*.kt' '*.java' ':!PROVENANCE_nr.txt'
     → ONLY HiscoresCategory.java + HiscoresCategoryEntry.java hits allowed (deliberate keeps)
   git grep -n "WildernessEvent\|WILDERNESS_EVENT\|HotZone\|managewevents" -- '*.kt' '*.java'
   git grep -in "strykewyrm" -- '*.kt' '*.java'
     → ONLY the Assignment.kt migration guard's "STRYKEWYRMS" literal allowed
   git grep -n "REVIVE_" -- '*.java' '*.kt'
   git grep -n "32150\|ganoBoosterKillsLeft\|GANODERMIC_BOOSTER" -- '*.kt' '*.java'
   git grep -n "GanodermicBeastsPacker\|RaidsItemDefinitions\|NearRealityCustomAnimationsPacker\|NearRealityVanillaMapEdits" -- '*.kt' '*.java'
   git grep -n "packStrykewyrms\|packBalanceElementals\|packBork\|packPlaneFreezer\|packNomad\|packPhoenix\|packSlashBash\|packBarrelchest\|packWildyImp\|packWildMole\|packBarrowsCopies" -- '*.kt'
   git grep -n "DI_AHRIM\|DI_DHAROK\|DI_GUTHAN\|DI_KARIL\|DI_TORAG\|DI_VERAC\|LIL_WILDY_IMP\|BABY_WILD_MOLE\|LIL_SLASH_BASH\|BABY_AGED_BARRELCHEST" -- '*.kt' '*.java'
   git grep -n "10300\|10500\|10502" -- cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomStructsPacker.kt
     → zero; 10501 remains in both files
   ```
6. **Cache regen from pristine (rule 2 — NEVER from a previously-packed cache):** `./gradlew :cache:resetCache` → `./gradlew :cache:generateCache`. Success criteria: completes without exceptions; no `no id mapping found` errors from `defaultModels()` (the pairing rule's runtime check); no output lines mention Ganodermic/Origins-boss/Strykewyrm/WildMole/Raids.
7. **Plugin index regen (mandatory before boot):** `./gradlew clean` (or at minimum delete `content/bosses/origins/build/` and `content/areas/wilderness/build/`) then `./gradlew :app:runPluginScanner` — the deleted `@Subscribe` modules and `NPCCombatDefinitionsScript`/`NPCDropTableScript` classes are in the stale `data/plugins.dat`.
8. **Boot gate:** `./gradlew :app:runDev` — clean boot, no `ClassNotFoundException`, no missing-definition errors, no per-tick "No wilderness events available" spam (the manager is gone entirely).
9. **In-game checklist (existing save, then fresh):**
   - `::wevent`, `::gano`, `::managewevents`, `::barrelchest` → unknown-command.
   - Wilderness slayer task completion pays normal blood money (no hot-zone double, no exception).
   - CoX: creature-keeper grub chest with 1–2 free inventory slots grants exactly the slots available, XP matches; kindling chopping/brazier works; grubs and kindling are UNSTACKABLE (vanilla defs back).
   - PvM Arena: down/revive cycle completes; no skull icon over downed players; no `IllegalArgumentException` from `PlayerSkulls.valueOf`.
   - Collection log: no Ganodermic Beast / Strykewyrms / Wild Mole categories; **Primal Items category present** with rewards claimable; boss pages otherwise intact (10300 removal shifts enum 2103 by design — verify page labels align).
   - Hiscores: boss rows correctly labelled; the Ganodermic row present but frozen (deliberate — Stage 6).
   - Drop viewer: no Ganodermic Beast.
   - Slayer: masters never assign strykewyrms; a save doctored to `taskName="STRYKEWYRMS"` logs in cleanly with a zeroed RATS assignment.
   - Old save holding a Ganodermic booster/runt/polypore-deg/pet item: logs in fine; the items render as undefined after cache regen (accepted — Part F).
10. Commit (plan + dry-run script deleted in the same commit):
    ```
    git add -A
    git commit -m "Stage 2 cache cleanup: remove wilderness events, ganodermic beast, origins bosses, raids def edits"
    git push
    ```

---

## Part F — save impact & deliberately left behind

**Save impact:**
- `ganoBoosterKillsLeft` → orphaned JSON key in old saves; Gson loader skips unknown fields. No action.
- Slayer saves with an active STRYKEWYRMS task → migrated by the A-5 guard (without it: `IllegalStateException` at login, verified `Assignment.kt:280`).
- Per-name killcounts ("Ganodermic Beast", wyrm names) persist inertly; the kept hiscores row keeps reading the gano one.
- Items losing cache defs while possibly banked: 32002/32003, 32038, 32040/32041, 32100, 32150, 32821–32825, 32855/32856. They render undefined client-side after regen. Accepted (rare items); no sweep mechanism exists in-repo — flag for ops if a login sweep is ever built.
- Item 32957: banked Baby wild mole pets morph into the Divine spirit shield **placeholder** (which this stage un-breaks — see §0.2). Placeholders can't leave the bank placeholder system; harmless.
- Collection-log obtained-data for removed categories stays in saves as inert entries.

**Deliberately left — do not "fix" while executing:**
- `HiscoresCategory.java` / `HiscoresCategoryEntry.java` GANODERMIC_BEAST entries + packed blobs `assets/packed/misc/archive_2/enum_8/10033`, `archive_12/2735` — index-coupled; **Stage 6**.
- `NearRealityEffigyMapEdits.kt` — live keep (rename belongs to the 5b/6 keep-set pass).
- `PackerExt.kt` `default*()` helpers — still used by the surviving `packCustoms()`.
- `CHAOTIC_CROSSBOW`/kiteshields **32810–32816**: unobtainable after the wyrms die but keep their toml defs, ItemId constants and `AmmunitionDefinitions` entries → **5c** sunset list.
- `POLYPORE_STAFF` 32042 family + `PvpTourneyMysteryBox` (its only source) — kept; revisit when tournament content is culled (Stage 3 checkpoint).
- `NpcId.DRIFTER` (16133) + its packed def — kept with the Remnant pets; note it has zero spawns today (pre-existing orphan).
- `corrupt_kratos_*`, `origins_handcannon_*`, `origins_primal_workbench` assets + map entries — unowned but resident in surviving dirs; pairing rule says keep both halves.
- GFX 6001's dangling `animationId = 25000` in `NearRealityCustomGraphicsPacker` — dangling before this stage too (no sequence 25000 ever existed); not an origins dependency, do not touch.
- Pre-existing bugs, flagged not fixed: `RaidWoodcutting.java` `experience[amount-1]` AIOOBE when `getFreeSlots()==0` in IceDemonRoom; `CreatureKeeperRoom.deposit` deletes `amountInInventory` while crediting `amountToDeposit` (equal in practice at cap 28).
- `data/rewards/blood-keys/diamond-blood-key.json` "Ganodermic visor/poncho/leggings" comments — WRONG (ids 920/922/924 are mithril arrows); rewards untouched.
- Wild Mole had spawns + a CL tab but **no drop table** anywhere — pre-existing gap, now moot.

---

## Part G — roadmap bookkeeping after execution

- Stage 2 exit gate met → next is **Stage 3** (custom maps; tournament checkpoint).
- Record in the roadmap: Effigy correction (KEEP; `NearRealityVanillaMapEdits` deleted instead), origins packer split (survivors carry to a 5b/6 rename — suggestion: `NearRealityCustomsAndPetsPacker`), hiscores gano rows + CS2 2735 + enum blob 10033 assigned to **Stage 6**, kiteshields/crossbow 32810–32816 assigned to **5c**, `NearRealityRaidsItemDefinitions` closed WITH the CoX fix (pre-check outcome: dependency existed and was repaired, not dodged).
- Stage 1 addendum: the revival-sprite removal's "zero readers" claim is corrected by this stage's A-4 (PvM-Arena skull path stripped).
