# Player Extraction / OpenRune Alignment — ROADMAP (the one living document)

> **This file supersedes the HANDOVER_after_*.md files and the spent
> prompts/ files for day-to-day use.** Those remain as historical record;
> nothing in them is needed to execute what's below — where a detail
> matters it has been folded in here. Update THIS file at the end of every
> session (state table + baselines + strike out finished rows); do not
> write new handover documents.

**The finish line:** `Player.java` at ZERO `com.zenyte.game.content.*`
imports, `InitializationEvent` deleted, and the OpenRune end-state patterns
in place (per-system soft timers instead of a broadcast process event;
varbits as prayer truth). Everything between here and there is enumerated
below — if work appears that isn't on this list, add it here first.

---

## 0. Current state (updated 2026-09-07, Track 1 wave 1 landed on top of Main `5d25c393`)

**Done and merged to Main:** Phases 0, A, B, B.5, C, D, E (all 20 fields
extracted to AttributeMap), F1–F5, Rotation (all 15 shims deleted), G1
(per-tick drivers), E6 rider (pet login/logout events), Track 2 campaign
(T2-a–T2-d), T3.1a/b (nightmare curse + engine varbit reads), T3.2 (soft
timers). The G1/E6 play-test sweep is done.

**Done this session (Track 1, wave 1 — ROADMAP items 1.1–1.6, 7 commits):**
- 1.2 toaPlayerData: dead scaffolding DELETED (D-W1-1) — zero writers, the
  one reader consumed construction defaults; behavior identical. No key
  added; see the PHASE_TOA_PERSIST open item below.
- 1.1 gauntletItemStorage: converted to a parser-only `@Deprecated` legacy
  drain slot (D-W1-2) — zero live writers, the live gauntlet never
  references the class; only the onInitialization drain (stranded-item
  return for pre-rework saves) remains. No key, no accessor.
- 1.4 puzzleBox + lightBox: transient attr keys (PuzzleBoxKeys/
  LightBoxKeys, no persistenceKey, no shim — never persisted).
- 1.3 stash → attrPersistence["stash"] (StashKeys), 1.5 petInsurance →
  attrPersistence["pet_insurance"] (PetInsuranceKeys; the LoginManager
  setFields copy line replaced by an attr-first onInitialization), 1.6
  godBooks → attrPersistence["god_books"] (GodBooksKeys) — all three on
  the SeedVault D1 recipe: rehydrating accessor + adopt/initialize,
  attr-first onInit with parser-legacy fallback, `@Deprecated`
  field+getter shims joining Rotation 2.

**Wave 2a also landed (items 1.7–1.9, 4 commits, same session):**
- 1.7 respawnPoint: RespawnPoint enum MOVED to core
  (com.zenyte.game.world.entity.player; T2-c precedent) — pure engine
  enum, field stays on Player as core-typed, no key/shim/rotation entry
  (enum persists by name, package move cannot touch saves). 7 imports
  retargeted.
- 1.8 privateStorage → attrPersistence["private_storage"]
  (PrivateStorageKeys), 1.9 retrievalService →
  attrPersistence["item_retrieval"] (RetrievalServiceKeys) — D1 recipe,
  @Deprecated field+getter shims joining Rotation 2. Player's internal
  item-sweep/forcedRemoved reads now go through the Keys accessors:
  **DEFER-4a/4b** (see Open items).

**Baselines (gate greps — every session re-runs these; monotone
non-increasing, a surprise increase is stop-and-report):**
```bash
grep -c 'import com.zenyte.game.content' core/src/main/java/com/zenyte/game/world/entity/player/Player.java      # 13
grep -rn "import com.zenyte.game.content\." core/src/main --include="*.java" --include="*.kt" | grep -v "/content/" | wc -l   # 1100 (includes 22 planned content-import lines in core paths: the prior 12 — TeleportType structures.* wildcard, 6 T2-d D-3 RaidAccess lines, wave 1's 5 — plus wave 2a's 10: PrivateStorageKeys in StorageUnitOPlugin/SharedStorageUI/Player(DEFER-4a), RetrievalServiceKeys in ItemRetrievalServiceInterface/CollectionLog/DeathMechanics/VarCollection/StrangeOldMan/Torfinn/Player(DEFER-4b); net +4 after wave 2a's −6 RespawnPoint retargets)
grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build | wc -l                                      # 21 (unique; 19 + "private_storage" + "item_retrieval")
grep -c "@Deprecated" core/src/main/java/com/zenyte/game/world/entity/player/Player.java                          # 12 (= the six Rotation-2 shims × field+getter: gauntlet, stash, petInsurance, godBooks, privateStorage, retrievalService)
```
plugins.dat: 4356 plugins (untracked file; regenerate with
`./gradlew :app:runPluginScanner` after any @Subscribe/annotation change,
never commit it; wave 1 added PetInsurance.onInitialization).

**Play-tests outstanding (Jesse, after merging wave 1):**
- STASH: build/fill/empty + relog persistence; a pre-migration save
  migrating (legacy top-level "stash" key adopted, next save moves it
  under attrPersistence["stash"]).
- Pet insurance: insure at Probita + reclaim + relog persistence.
- God books: page add/check/claim at Jossik + relog persistence.
- Clue boxes: puzzle box open/shift mid-session + light box open/press
  (transient — state resets on relog, as before).
- TOA: one raid start (loadData path).
- Gauntlet: a pre-rework save with stranded items still gets them
  returned (if any such save exists).
- PHASE_TOA_PERSIST v2 (landed on top of wave 1 — invocation presets and
  X-log state now persist under attrPersistence["toa_player_data"]):
  1. invocation presets survive a server restart (board state + the save
     JSON carries `attrPersistence["toa_player_data"].partySettingData`);
  2. X-log mid-raid, same session — rejoin message + placement inside;
  3. X-log mid-raid + server restart — placed OUTSIDE the entrance via
     the unable-to-rejoin path, no NPE (now reachable cross-session for
     the first time);
  4. fresh account — ToA lobby opens with defaults, no NPE;
  5. no-ToA session — saved presets round-trip untouched when ToA is
     never touched (save-half skips on the null manager).
- Wave 2a: CoX private storage deposit/withdraw + relog + a pre-migration
  save migrating; die to Zulrah/Vorkath/Hydra → reclaim at each retrieval
  NPC + relog with items held + a pre-migration save; Krystilia/Merlin/
  Tiffy respawn dialogues + max cape respawn switching + ring of
  returning + an actual death respawn at each point.

**Open items:**
- **DEFER-4a/4b** (wave 2a): Player's item-sweep methods
  (carryingItem-style loops + forcedRemoved's container array) read
  privateStorage/retrievalService internally, so Player carries
  PrivateStorageKeys (DEFER-4a) and RetrievalServiceKeys (DEFER-4b)
  imports — the DEFER-1/2/3 pattern. Unblock = a deterministic-order
  core-side container registry, or moving the sweep methods off Player
  (order matters for forcedRemoved — which containers lose items first —
  so the registry is recorded design work, not improvised).
- PHASE_TOA_PERSIST: landed as v2 (attr key "toa_player_data";
  PlayerPreSaveEvent save-half; dead TOARewardInterface/AbstractTOAClazz/
  AbstractTOAManager deleted).
- ~~api PlayerModel legacy-key gap~~ FIXED same session (found in
  wave-2a review): `PlayerData` now parses
  attrPersistence["item_retrieval"]/["private_storage"] and prefers them
  over the legacy top-level keys (which remain as the pre-migration
  fallback); `containerWrapperList` uses the effective accessors. The raw
  `retrievalService`/`privateStorage` fields stay for source
  compatibility with any external consumer. Note: PlayerData has zero
  in-repo consumers — external API consumers reading the raw fields
  directly (rather than containerWrapperList/effective accessors) should
  switch to the effective ones.
- **Lesson recorded (wave-2a hotfix):** call-site censuses for field
  extractions MUST include Kotlin synthetic-property access
  (`player.x` / bare `x` in Player-receiver scope), not just Java
  `getX()` greps — the wave-2a sweep missed ~35 Kotlin references in 6
  files (gravestone death/reclaim path, Nex chest/barrier, ToB chest,
  EcoSearch, backups tool), all silently compiling against the deprecated
  getter. Fixed by retargeting to the accessor and adding
  scanRetrievalService (GravestoneKeys.scanGravestone idiom) for offline
  parser scans.

---

## 1. Session protocol (unchanged, compressed)

1. One work item per session; investigate → mini-plan → execute → gates.
2. Re-anchor by grep before every edit; line numbers drift.
3. Gate: `./gradlew clean compileJava compileKotlin` + the baseline greps
   above + per-edit occurrence-count assertions.
4. `@Subscribe` methods must be static (top-level Kotlin `fun` is fine);
   any new/renamed annotated shape ⇒ re-run the plugin scanner.
5. Commit per item; end by updating THIS file's state table.

**House idioms (copy these, don't invent):**
- *Keys file* (persisted field): `XKeys.kt`, `@file:JvmName`, an
  `AttributeKey<X>(persistenceKey = "x_snake")`, a `Player.x()` accessor
  that lazily rehydrates the raw attrPersistence map via
  `LoginManager.gson` and adopts through the class's own copy
  constructor/copyFrom (see FarmingKeys/HunterKeys/PrayerManagerKeys).
- *EventBus hooks*: top-level `@Subscribe fun onServerLaunch(event:
  ServerLaunchEvent)` → `event.worldThread.eventBus.subscribeUnbound(...)`
  (see TOAAccess.kt, PlayerTickHooks.kt, FollowerKeys.kt). EventBus
  publish has NO try/catch — a subscriber throw propagates to the publish
  site; subscribers registered in one call run in registration order.
- *WorldHooks* (legacy engine events, e.g. ExperienceGained):
  `event.worldThread.hook<PlayerEvent.X> { ... }` (see NexModule.kt:84).
- *Engine flag lift* (de-instanceof a content type in core): add a
  `public boolean isX() { return false; }` to the core base type
  (RegionArea/NPC), override in content (the Phase B ToA pattern).

---

## 2. TRACK 1 — finish the field extraction (mechanical; recipe proven)

The remaining content-typed Player members. Persisted ones get the Keys
pattern + a `@Deprecated` shim and join Rotation 2; transient ones need no
shim. Ref counts measured 2026-09-06 (`grep -rho "getX(" core content
scripts | wc -l`).

| # | Field | Refs | Persisted? | Notes |
|---|---|---|---|---|
| ~~1.1~~ | ~~gauntletItemStorage~~ | 2 | legacy drain only | DONE wave 1 — legacy parser drain only, no key (D-W1-2): zero live writers; @Deprecated slot + onInit stranded-item return, dies at Rotation 2 |
| ~~1.2~~ | ~~toaPlayerData~~ | 2 | NO (dead) | DONE wave 1 — dead scaffolding deleted, no key (D-W1-1); PHASE_TOA_PERSIST landed as v2 right after: persisted attr key "toa_player_data" in the toa module (durable fields only; scratch transient) |
| ~~1.3~~ | ~~stash~~ | 7 | yes → "stash" | DONE wave 1 (StashKeys, D1 recipe) |
| ~~1.4~~ | ~~puzzleBox + lightBox~~ | 11+10 | NO (transient) | DONE wave 1 (PuzzleBoxKeys/LightBoxKeys, no shim) |
| ~~1.5~~ | ~~petInsurance~~ | 12 | yes → "pet_insurance" | DONE wave 1 (PetInsuranceKeys; LoginManager copy line → onInit) |
| ~~1.6~~ | ~~godBooks~~ | 13 | yes → "god_books" | DONE wave 1 (GodBooksKeys, D1 recipe) |
| ~~1.7~~ | ~~respawnPoint~~ | 16 | yes (plain enum) | DONE wave 2a — resolved by core move, no key (the mini-plan's own alternative): pure engine enum relocated to com.zenyte.game.world.entity.player; field stays on Player |
| ~~1.8~~ | ~~privateStorage~~ | 19 | yes → "private_storage" | DONE wave 2a (PrivateStorageKeys, D1 recipe; DEFER-4a for Player's internal sweep reads) |
| ~~1.9~~ | ~~retrievalService~~ | 37 | yes → "item_retrieval" | DONE wave 2a (RetrievalServiceKeys, D1 recipe; DEFER-4b for Player's internal sweep reads) |
| 1.10 | duel | 105 | NO (transient) | no persistence work, but the largest retarget |

Sequencing: ~~1.1–1.6 batch well (≈2 sessions)~~ landed as wave 1;
~~1.7–1.9 one session each~~ landed as wave 2a; 1.10 its own session.

Then:
- **Rotation 2** (one session, after Jesse cycles/deletes saves or the
  per-key `grep -L '"<key>"' data/characters/*.json` gate passes): delete
  the new shims + fallback branches, exactly like Rotation 1. Wave-1 gate
  additions: `grep -L '"stash"' data/characters/*.json`, same for
  `"pet_insurance"` and `"god_books"`, plus the gauntlet condition — no
  save still carries a non-empty `"gauntletItemStorage"` key (its drain,
  field and getter die together). Wave-2a additions: the same per-key
  greps for `"private_storage"` and `"item_retrieval"`.
- **Delete InitializationEvent** (one session): after 1.1–1.9 its
  subscribers no longer read the parser; remove the event, the
  `setFields` post, and then simplify LoginManager's double-deserialize
  (parser Player) if nothing else consumes it — verify by grep, this is
  the last consumer today.

Track 1 removes ~13 of the 34 imports.

## 3. TRACK 2 — non-field content usages in Player (small designs, mostly Phase-B lifts)

One inventory/sequencing session first, then ~3–4 execution sessions.
Current import list with the expected treatment:

T2-a landed (2026-09-06, 5 commits): PlayerDeathStartEvent (published
from BOTH death paths — Player.sendDeath and PlayerDeathHandler; future
death-work hook) + PlayerPostDamageEvent; retribution/redemption/
death-charge/reset/quick-prayers off Player; prayer drains via own
drainSkill; spellbook-swap + tip-jar onto PlayerLogoutEvent; gravestone
+ Ava's onto the T3.2 soft timers (reserved ids); GE/lootkey/farming
login refreshes onto PlayerLoginEvent; dead PlayerDeathEvent +
BountyHunter.onDeath deleted (D-4). The clan move initially hit the
plan's pre-move audit (ClanChannel.onLogout, a ListenerType.LOGOUT
listener, also removed the player from channel.members — running leave()
after the LOGOUT plugins would have early-returned and dropped
ClanLeaveEvent + empty-channel cleanup) and was RESOLVED in two follow-up
commits: ClanChannel.onLogout deleted as provably dead (the
canLeaveClanChannel veto it backstopped returns true in the Controller
base with zero overrides repo-wide, and ListenerType.LOGOUT fires only
from Player.finish, right after leave() — so its remove was a no-op in
every reachable state; if a real canLeaveClanChannel override is ever
added, the logout path must handle a vetoed leave deliberately), then
leave/first-login join moved onto PlayerLogoutEvent/PlayerLoginEvent
(ClanLifecycleHooks.kt). ClanManager is out of Player; ClanChannel stays
(getRaid body: dies in T2-d). DEFER-1 residue is exactly 5 sites: Elysian
getPrayerPoints ~2955, faith-necklace restore ~3159, and the three
drainSkill overrides ~3504/~3512/~3520.

| Import(s) | Treatment |
|---|---|
| Prayer, PrayerManagerKeys | varbit reads DONE (T3.1a + T3.1b — Player.java no longer imports Prayer); remaining: id lift + DEFER-1 (5 sites above), per the G3 equivalence table (HANDOVER_after_G3.md §3 is the reference — the one historical doc still load-bearing) |
| ~~Teleport, ForceTeleport, TeleportType~~ | DONE in T2-c (package move to core) (~~SpellbookSwap, DeathChargeKt~~ DONE in T2-a) |
| ~~Raid, RaidParty, ClanChannel~~ | DONE in T2-d (RaidAccess accessor; getRaid deleted) (~~Inferno~~ DONE in T2-b) |
| ClanChannel | ~~ClanManager~~ DONE in T2-a follow-up (ClanLifecycleHooks.kt); ClanChannel stays for the getRaid body — dies in T2-d |
| RoomReference, ConstructionKeys | ~~Construction~~ DONE in T2-b (currentHouse accessor); Keys stays for DEFER-2 roomPreview (tip-jar logout DONE in T2-a) |
| ~~CharterLocation, AdventurersLogIcon, AvasDevice~~ | ALL DONE (AvasDevice T2-a; CharterLocation resolver + AdventurersLogIcon deletion T2-b) |
| FarmingKeys | ~~GrandExchangeKeys, GravestoneKeys, LootkeySettingsKeys, LootkeySettings~~ DONE in T2-a; FarmingKeys stays for the two movement-path refreshes ~1079/~1221 (DEFER-3) |

T2-b landed (2026-09-06, 4 commits): Inferno shift-teleport check →
RegionArea.isShiftTeleportationProhibited() flag lift (the flag exists
for future area lifts; Inferno is the sole overrider);
Player.getCurrentHouse → ConstructionKeys.currentHouse(player) (17
sites, 10 files); Trader Stan charter resolution →
CharterLocation.traderStanShopName (openShop is generic; both live call
paths resolve first); dead adventurer's-log surface deleted per D-2 (16
no-op sites, 6 files; AdventurersLogIcon enum retained for the NR
track). Divergence in Barrows: the plan's orphan guard hit — the
equipmentPieces/chestCount/joinedEquipmentLootString computation fed
ONLY the deleted log entry, so the whole dead block went with it
(behavior-neutral, pure reads). Player imports 25 → 21 (−AdventurersLogIcon
−Inferno −CharterLocation −Construction).

T2-c landed (2026-09-06, 1 commit): Teleport/TeleportType/ForceTeleport
moved to core (com.zenyte.game.world.entity.player.teleport; 83-file
import retarget; Magic.logger decoupled first). The structures/
subpackage and the six other teleports classes stay in content.
TeleportType keeps the structures.* wildcard — the one planned content
import in a core-path file; enum→structure decoupling is future design
work. Follow-up inventory: 19 engine files import the non-moved teleport
classes (ItemTeleport 6, TeleportCollection 9, SpellbookTeleport 2,
MinigameGroupFinder 2).

T2-d landed (2026-09-06, 1 commit) — **TRACK 2 CAMPAIGN CLOSED**:
Player.getRaid → RaidAccess.raid(player) (86 Player-receiver sites; 59
files by count-gated sed + 6 verbatim; census corrected — 4 getRaid
definers, only Player's moved; SharedStorageUI left the D-3 list, its
receiver is raidController). Java subpackages needed the RaidAccess
import (50 files — the plan's same-package claim held only for the exact
package); +6 D-3 baseline import lines (the planned 5 + StorageUnitOPlugin,
whose file already had cox imports but the baseline counts lines).
Player's 15 remaining content imports are exactly Track 1's eleven field
types (GodBooks, ItemRetrievalService, RespawnPoint, PrivateStorage,
PetInsurance, GauntletItemStorage, Duel, TOAPlayerData, LightBox,
PuzzleBox, Stash) + the four deferral imports (PrayerManagerKeys
DEFER-1, ConstructionKeys/RoomReference DEFER-2, FarmingKeys DEFER-3).

## 4. TRACK 3 — OpenRune end-states (design work, ordered by payoff)

1. **Nightmare curse reimplementation** → curse fix + engine varbit
   reads DONE (T3.1a routed the scramble at activation input so varbits
   always reflect true effect, G3 re-audited in HANDOVER_after_G3.md §7;
   T3.1b converted all 32 engine `isActive` reads — 31 greppable + 1
   dynamic — to `getBitValue` via the new core-side `PrayerVarbits`
   holder). Remaining: map deletion end-state — delete the
   `activePrayers` map so varbit becomes the sole truth (the OpenRune
   model) — needs soft timers (Track 3.2) for the drain accumulator.
   Requires Nightmare play-testing.
2. **Soft-timer system** → DONE (T3.2, one session): OpenRune-shape
   `PlayerTimerMap`/`PlayerTimers`/`PlayerTimerEvent.Soft`/
   `PlayerTimerProcessor` in org.rsmod.game.timer + events;
   farming/hunter/prayer drain migrated onto soft timers (scheduled in
   one PlayerLoginEvent subscriber, fire order preserved by insertion
   order); `PlayerProcessEvent` retired — zero code references remain.
   Timer ids GRAVESTONE=4 and AVAS_DEVICE=5 are reserved for the T2-a
   tick-driver moves.
3. *(Optional, out of campaign)* per-action content events (catch-fish,
   burn-log…) if diary progress should ever be event-driven — the G2
   census showed an XP broadcast is the wrong shape.

## 5. Order of operations from today

1. PR + merge this branch; run the two outstanding play-tests (§0).
2. Track 1 waves (with Rotation 2 + InitializationEvent deletion at the
   end).
3. Track 2 inventory session, then its execution sessions (Track 3.1 can
   run in parallel any time — it's content-side).
4. Track 3.2 soft timers last; it retires the G1 bridge.

Rough total: **10–14 focused sessions** to the finish line.
