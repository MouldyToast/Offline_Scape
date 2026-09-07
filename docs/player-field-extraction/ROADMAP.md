# Player Extraction / OpenRune Alignment — ROADMAP (the one living document)

> **This file supersedes the HANDOVER_after_*.md files and the spent
> prompts/ files for day-to-day use.** Those remain as historical record;
> the one still load-bearing is HANDOVER_after_G3.md (§3 prayer
> equivalence table for DEFER-1; §7 nightmare re-audit for T3.1). Update
> THIS file at the end of every session; do not write new handover
> documents. Finished work is PRUNED from this file, not struck through —
> git history (branch claude/five-quality-plans-7firff and earlier) is
> the record of what landed and how.

**The finish line:** `Player.java` at ZERO `com.zenyte.game.content.*`
imports and the OpenRune end-state patterns in place. Everything between
here and there is enumerated below — if work appears that isn't on this
list, add it here first.

---

## 0. Current state (updated 2026-09-07, post InitializationEvent deletion)

**Done (see git history for the how):** Phases 0–F + Rotation 1; G1; E6
rider; Track 2 campaign (T2-a–d); T3.1a/b (nightmare curse fix + all
engine prayer reads on varbits); T3.2 soft timers (PlayerProcessEvent
retired); Track 1 waves 1/2a/2b (all eleven field types extracted or
resolved); PHASE_TOA_PERSIST v2 (attrPersistence["toa_player_data"];
PlayerPreSaveEvent save-half published from serializePlayerToFile before
refreshAttrPersistence); Rotation 2 (all six shims deleted;
GauntletItemStorage deleted); InitializationEvent deleted (the four core
parser subscribers became setFields adopt calls; 20 eager-rehydration
subscribers moved to PostInitializationEvent; **the parser Player is now
consumed only inside LoginManager**).

Player's 7 remaining content imports are exactly the deferral Keys:
PrayerManagerKeys (DEFER-1), ConstructionKeys/RoomReference (DEFER-2),
FarmingKeys (DEFER-3), PrivateStorageKeys/RetrievalServiceKeys
(DEFER-4a/4b), DuelKeys (DEFER-5). See §2 for the ledger.

**⚠ Deploy caveat (live until merged/deployed):** Rotation 2 deleted the
legacy-save fallbacks. Any environment carrying pre-migration character
saves must let each account log in once on the last pre-rotation commit
(`6298f876`) BEFORE running at or past `e92a06ab` — after it the legacy
top-level keys (`stash`, `petInsurance`, `godBooks`, `privateStorage`,
`retrievalService`, `gauntletItemStorage`) are no longer read and their
data would be silently dropped. Dev-only throwaway saves: ignore.

**Baselines (gate greps — every session re-runs these; monotone
non-increasing, a surprise increase is stop-and-report):**
```bash
grep -c 'import com.zenyte.game.content' core/src/main/java/com/zenyte/game/world/entity/player/Player.java      # 7 (all deferral Keys)
grep -rn "import com.zenyte.game.content\." core/src/main --include="*.java" --include="*.kt" | grep -v "/content/" | wc -l   # 1112 (includes the 40 planned content-import lines in core paths: 1 TeleportType structures.* wildcard, 6 RaidAccess, 5 wave-1 Keys, 10 wave-2a Keys, 18 duel; minus Player's 6 rotated slot imports)
grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build | wc -l                                      # 21 (unique)
grep -c "@Deprecated" core/src/main/java/com/zenyte/game/world/entity/player/Player.java                          # 0
```
plugins.dat: 4353 (untracked; regenerate with
`./gradlew :app:runPluginScanner` after any change to an annotated/
scanned shape — ALL PluginType shapes count, e.g. Interface subclasses,
not just Guava @Subscribe; subscriber counting is per-CLASS, so a class
keeping any @Subscribe stays registered; the scanner now truncates the
file after write; never commit it).

**Play-tests outstanding (Jesse — this sweep gates the merge):**
- Boot log clean: zero "Failed to load plugin" lines; the 20
  PostInitializationEvent subscribers register.
- Fresh account: create → play → relog; save JSON has attrPersistence
  keys and NONE of the six legacy top-level keys.
- Existing-account relog: vars/varbits intact (quest points, diary
  steps, prayer unlocks — serializedVars is broad); collection log,
  daily challenge progress, loyalty session credit all intact.
- STASH build/fill/empty + relog; pet insurance (Probita insure +
  reclaim) + relog; god books (Jossik page add/check/claim) + relog;
  puzzle/light box mid-session (transient — resets on relog, as before).
- CoX private storage deposit/withdraw + relog. Item retrieval: die to
  Zulrah/Vorkath/Hydra → reclaim at each NPC + relog with items held.
- Gravestone path (hotfixed code): die with items → gravestone → reclaim
  / incinerate / pay unlock; expire → Death's Office; Nex chest +
  barrier prompts; ToB retrieval chest.
- Respawn points: Krystilia/Merlin/Tiffy dialogues, max-cape switching,
  ring of returning, an actual death at each point.
- ToA v2: presets survive restart (+ JSON carries
  attrPersistence["toa_player_data"].partySettingData); X-log mid-raid
  same session; X-log + restart → placed outside, no NPE; fresh-account
  lobby; no-ToA session round-trips presets untouched.
- Duel: full loop (challenge→settings→stake→accept→fight→payout→
  scoreboard); forfeit object; X-log mid-duel; mithril seeds +
  teleother/lunar spells blocked; orbs/bank/combat-tab displays; phoenix
  necklace NOT consumed at low HP in duel; degradable weapons; credit
  store + middleman blocked.
- Eager-rehydration spot check (one action each): farming, hunter traps,
  slayer task, GE offers, presets, barrows kc, bounty hunter, diaries,
  lootkey settings, blast furnace, cannon, construction, prayer, seed
  vault.
- Nightmare (gates the next code session, §5): curse behavior correct
  post-T3.1a/b.
- (Pre-migration save migration is testable only on `6298f876` — see the
  deploy caveat; skip if no legacy saves exist.)

---

## 1. Session protocol

1. One work item per session; investigate → mini-plan → execute → gates.
2. Re-anchor by grep before every edit; line numbers drift.
3. Gate: `./gradlew clean compileJava compileKotlin` + the baseline greps
   above + per-edit occurrence-count assertions.
4. `@Subscribe` methods must be static (top-level Kotlin `fun` is fine);
   any new/renamed annotated shape ⇒ re-run the plugin scanner.
5. Commit per item; end by updating THIS file (prune finished work).
6. **Census lesson (wave-2a hotfix):** call-site censuses MUST include
   Kotlin synthetic-property access (`player.x` / bare `x` in
   Player-receiver scope), not just Java `getX()` greps — a deprecated
   getter keeps such sites compiling while returning garbage. Offline
   parser tooling uses the `scanX` raw-attr idiom
   (GravestoneKeys.scanGravestone / RetrievalServiceKeys.scanRetrievalService).

**House idioms (copy these, don't invent):**
- *Keys file* (persisted field): `XKeys.kt`, `@file:JvmName`, an
  `AttributeKey<X>(persistenceKey = "x_snake")`, an accessor that lazily
  rehydrates the raw attrPersistence map via `LoginManager.gson` and
  adopts through the class's own copy method (see
  FarmingKeys/HunterKeys/PrayerManagerKeys/SeedVaultKeys).
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
- *Save-time sync*: PlayerPreSaveEvent (published in
  serializePlayerToFile BEFORE refreshAttrPersistence — puts and
  mutations both land in the snapshot; see TOAAccess.kt's save-half).

---

## 2. Deferral ledger (the 7 remaining Player content imports; each unwind deletes its import)

| Deferral | Import(s) | What Player still reads | Unwind |
|---|---|---|---|
| DEFER-1 | PrayerManagerKeys | 5 sites: Elysian getPrayerPoints ~2955, faith-necklace restore ~3159, three drainSkill overrides ~3504/3512/3520 | completes with the T3.1 end-state (id lift; HANDOVER_after_G3.md §3 is the reference) |
| DEFER-2 | ConstructionKeys, RoomReference | roomPreview | own mini-plan |
| DEFER-3 | FarmingKeys | two movement-path refreshes ~1079/~1221 | own mini-plan |
| DEFER-4a/4b | PrivateStorageKeys, RetrievalServiceKeys | item-sweep loops + forcedRemoved's container array | deterministic-order core-side container registry, or move the sweeps off Player (order matters for forcedRemoved — design work, don't improvise) |
| DEFER-5 | DuelKeys | phoenix-necklace check (1 site) | engine duel checks (combat rules, teleother/spell blocks, orbs — 16 engine files also import DuelKeys) behind area/event checks |

**Standing warnings / recorded future design work:**
- ClanChannel.onLogout was deleted as provably dead (T2-a): if a real
  `canLeaveClanChannel` override is ever added, the logout path must
  handle a vetoed leave deliberately.
- TeleportType keeps the structures.* wildcard import; enum→structure
  decoupling is future design work. 19 engine files import the
  non-moved teleport classes (ItemTeleport 6, TeleportCollection 9,
  SpellbookTeleport 2, MinigameGroupFinder 2).
- api PlayerData (kotlinx model, zero in-repo consumers) reads
  attrPersistence["item_retrieval"]/["private_storage"] with the legacy
  keys as archived-save fallback; external consumers reading the raw
  `retrievalService`/`privateStorage` fields directly should switch to
  the effective accessors / containerWrapperList.

## 3. TRACK 3 — OpenRune end-states (what remains)

1. **T3.1 end-state — delete the `activePrayers` map** so varbit becomes
   the sole prayer truth (the OpenRune model). Preconditions all landed
   (T3.1a scramble-at-input, T3.1b all 32 engine reads on getBitValue
   via core-side PrayerVarbits, T3.2 soft timers for the drain
   accumulator). Requires Nightmare play-testing first.
2. *(Optional, out of campaign)* per-action content events (catch-fish,
   burn-log…) if diary progress should ever be event-driven — the G2
   census showed an XP broadcast is the wrong shape.

## 4. Order of operations from today

**Gate first: Jesse's play-test sweep (§0) + boot-log check, then PR +
merge branch claude/five-quality-plans-7firff.** Then:

1. **NEXT CODE SESSION — T3.1 end-state** (§3.1): delete the
   activePrayers map; this session also completes DEFER-1 (the id lift +
   its 5 sites; HANDOVER_after_G3.md §3 is the reference). Nightmare
   play-test must have passed.
2. **Deferral unwinds** (§2, each its own design mini-plan, any order):
   DEFER-2, DEFER-3, DEFER-4a/4b, DEFER-5. Together they take Player's
   content imports 7 → 0 — the literal finish line.
3. **LoginManager double-deserialize simplification** — a campaign, not
   a session: setFields still holds ~40 parser copies of never-extracted
   core fields. Unblocked (parser is confined to LoginManager); plan
   with a fresh census.

**Handoff protocol:** give the next session this file and nothing else.
It starts by re-running the §0 baseline greps (7 / 1112 / 21 / 0;
plugins.dat 4353) and stops on any mismatch.
