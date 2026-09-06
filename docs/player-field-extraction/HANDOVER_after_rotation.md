# HANDOVER — Player Field Extraction, after Rotation (all 15 shims deleted)

**Audience:** the next session on this campaign (G1–G3 event-era work, or
follow-up cleanup).
**Branch:** `claude/player-field-extraction-rotation-hqg5n8`, 16 commits on top
of Main tip `0b965fb9` (PR #16 merge), pushed. Clean tree.
**Supplements** `HANDOVER_after_phase_F.md` and `HANDOVER_after_phase_E.md`
(§2 house pattern and §3 census methodology remain the historical record; the
shim-window mechanics they describe are now GONE).
**Authorization context:** Jesse deleted ALL legacy saves in
`data/characters/` and authorized rotation for all 15 persisted fields — there
was no legacy data to preserve, so the per-key
`grep -L '"<key>"' data/characters/*.json` gate was vacuously satisfied.

---

## 1. What rotated (all 15, one commit each)

For every field: the `@Deprecated` Player field + javadoc, the `@Deprecated`
getter + javadoc, and the onInit legacy-fallback branch (everything after the
`if (hadPersistedAttr || savedPlayer == null) return;` line, the then-unused
locals, and the `@SuppressWarnings("deprecation")` parser read) are DELETED.
Every onInit subscriber kept its `@Subscribe` annotation and exact signature
and collapsed to the eager accessor call — the call that rehydrates the raw
attr at login before game code runs. No plugin-scanner run was needed
(body/javadoc-only subscriber edits).

| Field | Commit | Player type import | Notes |
|---|---|---|---|
| seedVault | `1f3a405d` | deleted | |
| presetManager | `db00247c` | deleted | |
| barrows | `86d3df56` | deleted | copyFrom stays (attr rehydration) |
| blastFurnace | `3eaa78ec` | deleted | |
| dwarfMulticannon | `5e5908a5` | deleted | stray `@Expose` deleted with field; LOGIN re-place listener untouched |
| gravestone | `41f8cabe` | deleted | + scanGravestone trimmed, see §3 |
| hunter | `3d17be0d` | deleted | + dead `Player.setHunter` deleted (flagged housekeeping) |
| bountyHunter | `0fcd644e` | deleted | onInit javadoc rewritten (was "sets the fields") |
| grandExchange | `01511d81` | deleted | stray `@Expose` deleted; onInit javadoc rewritten (carried 2 getter mentions) |
| lootkeySettings | `1fc5e28c` | **KEPT** | live uses remain (chest path: accessor + static `sendOpenChest`); stray `@Expose` deleted |
| farming | `43fd2a22` | deleted | + CurePlant/Geomancy commented blocks deleted, see §3 |
| construction | `96af05bf` | **KEPT** | `getCurrentHouse()` still instanceof-checks/casts Construction; + Brazier commented block deleted |
| slayer | `c59b34ff` | deleted | + 3 hidden getter reads FIXED, see §2 — read this |
| prayerManager | `b745631c` | deleted | setPrayer stays (attr rehydration) |
| achievementDiaries | `98e79c04` | deleted | copyFrom stays (attr rehydration) |

## 2. Deviations and findings (in severity order)

1. **F3 census miss — latent live-player NPE, found and fixed
   (`c59b34ff`).** `SlayerHelper.kt` had three BARE IMPLICIT-RECEIVER
   property reads inside `Player` extension functions (`slayer.isUnlocked(...)`
   at :50, `slayer.master` at :62 and :64). These resolved to the synthetic
   Kotlin property for the deprecated `getSlayer()` — invisible to the
   Java-token census (F-handover pinned `getSlayer()` at 2) AND to
   dotted-receiver Kotlin scans (`\.slayer\b` needs a leading dot; these have
   none). Since F3 landed, the legacy field is null on LIVE players, so slayer
   task assignment through these paths (master read on regular/boss task
   generation; extended-amount unlock check) was a latent NPE at Main.
   Deleting the getter surfaced them as compile errors; they now invoke the
   `slayer()` accessor extension the file already imported and used correctly
   elsewhere (line 36). This restores pre-F3 behavior. **Play-test slayer task
   assignment first** (§5). Lesson for any future census: also grep for BARE
   field-name reads inside `fun Player.*` extensions —
   `(^|[^.\w])fieldName\.` — not just dotted receivers.
2. **Per-field compile gate ran incremental, not clean.** Each field's gate
   was `./gradlew compileJava compileKotlin` (incremental); full
   `clean compileJava compileKotlin` ran at bootstrap (BUILD SUCCESSFUL,
   5m18s) and again after the last field (BUILD SUCCESSFUL). Rationale: 15
   clean builds ≈ 80+ minutes for edits (member deletions) that incremental
   ABI tracking handles reliably; the final clean build is the authoritative
   gate and passed.
3. **`FarmingKeys.adoptFarming` is now orphaned.** Its only caller was
   Farming.onInit's deleted fallback branch; its javadoc says as much. Keys
   files were explicitly out of this session's scope, so it stays. Cheap
   future cleanup (Jesse's call): delete the function + javadoc.
4. **Cosmetic javadoc staleness left in place:** the `copyFrom` javadocs in
   Barrows/BlastFurnace/DwarfMultiCannon/Gravestone/Hunter/BountyHunter/
   AchievementDiaries still say "used by the legacy load path above and by
   XKeys' attr rehydration" — the first half now points at nothing. Not
   authorized to touch beyond the fallback branches; fold into any future
   cleanup pass.
5. **Brazier:** the stale commented-out block's `// TODO only award
   construction exp if user has a house already` header was deleted together
   with the commented code it described (the TODO is meaningless without it).
6. **GE/lootkeys getter-token bookkeeping:** their "extra" pinned tokens
   (F-handover: 4 and 3 vs. 2) were javadoc mentions inside the classes' own
   onInit docs; they died with the javadoc rewrites, so both gates hit 0.
7. **Main moved before this session:** `416094ee` (G1–G3/R session-prompt
   docs) and `893445ce` ("your message here" — Jesse's hand-fix to
   Construction POH coordinate math/yardOffset + a data-driven
   `refreshObject` rebuild path). Neither touches the persistence path;
   baselines at branch point matched fc106725 exactly. The construction
   play-test below doubles as a test of Jesse's fix.

## 3. Collateral surfaces handled

- **`GravestoneKeys.scanGravestone`** (offline save scans) is now
  attrPersistence-raw-only; its legacy-field branch and
  `@Suppress("DEPRECATION")` died with the getter, javadoc updated.
  WealthScanner.kt, EcoSearch.kt and tools/discord's DiscordEcoSearch.kt all
  call the facade, whose SIGNATURE IS UNCHANGED — zero edits needed, all
  still count gravestone wealth (from the attr key only; a hypothetical
  pre-migration save would now scan as null — moot, all saves deleted).
  Reminder: `tools/discord` is not a Gradle module; compilation proves
  nothing there.
- **Commented-out legacy-getter references deleted** (pre-authorized):
  CurePlant.java `/*...*/` block (3 `getFarming()` tokens), Geomancy.java
  `/*sendInterface...*/` block, lines 43–168 (2 tokens; the block was ONE
  comment despite looking like live methods — `/*` at :43, `*/` at :168),
  Brazier.java `//` block (1 `getConstruction()` token).
- **`api/.../PlayerModel.kt`** still maps legacy top-level JSON keys — inert
  (nothing consumes `:api`), untouched, now doubly dead: post-rotation saves
  never carry those keys. Re-point at attrPersistence if `:api` ever revives.
- **`InitializationEvent.getSavedPlayer()`** still has 9 callers — the
  out-of-scope self-init subscribers (stash, privateStorage,
  gauntletItemStorage, retrievalService, godBooks, …). The event API is NOT
  dead; do not delete it.

## 4. Updated baselines (the new bootstrap numbers)

```bash
grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build | wc -l   # 15 (unique)
grep -c "@Deprecated" core/src/main/java/com/zenyte/game/world/entity/player/Player.java   # 0
grep -c 'import com.zenyte.game.content' core/src/main/java/com/zenyte/game/world/entity/player/Player.java   # 38
grep -rn "import com.zenyte.game.content\." core/src/main --include="*.java" --include="*.kt" | grep -v "/content/" | wc -l   # 1182
```

- persistenceKey lines: **15**, unchanged, all unique.
- Player.java `@Deprecated`: **30 → 0**.
- Player.java content imports: **51 → 38** (−1 per field for 13 fields;
  lootkeySettings and construction types remain live, see §1).
- Repo-wide core→content imports (excl. `/content/` paths): **1195 → 1182**.
  The master plan's monotone non-increasing expectation is REINSTATED as of
  this session (E-handover §1's suspension is over): every commit's delta was
  −1 or 0, never +.
- Legacy getter tokens: **0 for all 15 getters** repo-wide. Sole permanent
  exception: `getFarming()` = 4 on the unrelated `RaidFarming` class
  (Raid.java decl, RaidFarmingIOOA ×2, RaidFarmingPatchOA ×1).
- `hadPersistedAttr`: 0 occurrences; rotation-related deprecation
  suppressions: 0 (the one repo hit is InfoboxCleaner.kt's unrelated
  String.trim suppression).

## 5. Play-test steps for Jesse (all saves were deleted → every account is fresh)

The "migrated save loads intact" checks from the master plan are moot — no
pre-migration saves exist. What replaces them: per field, (a) exercise the
system on a fresh account, (b) log out, (c) inspect
`data/characters/<name>.json`: the attr key present under `attrPersistence`,
the legacy top-level key ABSENT (`seedVault`, `presetManager`, `barrows`,
`blastFurnace`, `dwarfMulticannon`, `gravestone`, `hunter`, `bountyHunter`,
`grandExchange`, `lootkeySettings`, `farming`, `construction`, `slayer`,
`prayerManager`, `achievementDiaries` must not appear as top-level JSON
keys), (d) log back in: state intact.

1. **slayer — FIRST, it had the real fix (§2.1):** get a task from Turael,
   a mid master, Krystilia and Sumona (boss task) — every assignment now runs
   the fixed `slayer().master` reads; with an extension unlock purchased, get
   a task whose amount range is extended (runs the fixed
   `slayer().isUnlocked` path at SlayerHelper:50). Relog → task/master/amount
   persist. Block/unblock, store/unstore, co-op partner, wilderness drops,
   rewards interface.
2. **farming:** plant a few patches, relog, growth continues; cast Cure Plant
   on a diseased patch (file was edited — live path untouched, comment-only).
3. **construction:** build rooms + deposit in a POH storage box, relog →
   house and box intact. This also exercises Jesse's own `893445ce` rebuild
   fix. Fix a Wintertodt brazier (Brazier edit was comment-only): points
   awarded, no construction XP (unchanged behavior).
4. **prayerManager:** set quick prayers, relog → selection persists
   (`attrPersistence.prayer_manager`); drain/deactivate at 0; protect item on
   death.
5. **achievementDiaries:** progress steps, relog → progress persists;
   complete a tier → congrats + reward at next login.
6. **gravestone:** die with items → grave spawns; relog near it (varbit
   re-spawn); reclaim at grave and Death's Office; coffer deposit/withdraw.
   Then run WealthScanner and EcoSearch over the saves — no NPE, gravestone
   wealth counted.
7. **seedVault / presetManager / barrows / blastFurnace:** deposit seeds /
   save+load a preset / partial barrows run (slain wights persist over
   relog) / ore in, bars out over a relog.
8. **dwarfMulticannon:** place, relog (re-placed by the LOGIN listener),
   pick up; let one decay for the Nulodion message.
9. **hunter / bountyHunter / grandExchange / lootkeySettings:** birdhouses
   persist + traps dismantle at logout / target skips persist / trade
   history shows after relog / enable via Skully → persists, fresh character
   stays null.

Any mismatch: JSON snippet + stack trace before anything else is built.

## 6. What this unlocks / next work

- The shim window is closed: Player.java has zero `@Deprecated` members and
  the import gates are monotone again. Do not re-introduce parser-fallback
  branches for these 15 keys.
- Event-era follow-ups have prepared prompts in
  `docs/player-field-extraction/prompts/` (G1 process event, G2 diary XP
  subscriber, G3 prayer varbits) — each still needs its own
  investigate → mini-plan → execute cycle.
- Cheap cleanup candidates for any future pass (NOT done here, see §2):
  `FarmingKeys.adoptFarming`, the stale "legacy load path above" javadoc
  halves, `api/PlayerModel.kt`.
- Out-of-scope inventory unchanged: godBooks, retrievalService,
  privateStorage, gauntletItemStorage, petInsurance, stash, duel,
  puzzleBox/lightBox, respawnPoint, toaPlayerData, non-field content usages.
  `petId` stays a plain int by design.
