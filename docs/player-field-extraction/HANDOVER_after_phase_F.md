# HANDOVER — Player Field Extraction, after Phase F (F1–F5 complete)

**Audience:** the next planning/execution session on this campaign.
**Branch:** `claude/player-extraction-phase-f-farming-8xcgjd`, HEAD `44b07bb7`, pushed.
Clean tree. Base: `1953d6ea` (Main tip = PR #15 campaign merge).
**Supplements** `HANDOVER_after_phase_E.md` (§2 house pattern, §3 census methodology
still authoritative except where amended below) and the master spec
`PLAN_player_field_extraction_EXECUTABLE.md` (lives with Jesse).
**Prime directive unchanged:** behavior byte-identical; a phase that changes gameplay,
loses data, or degrades tooling is worse than no phase.

---

## 1. State of the campaign (verified at `44b07bb7`)

Phase F extracted all five deeply-woven fields, one commit each:

| Phase | Field | persistenceKey | Keys file (core Kotlin) | Commit |
|---|---|---|---|---|
| F1 | farming | `farming` | `content/skills/farming/FarmingKeys.kt` | `567ff4e1` |
| F2 | construction | `construction` | `content/skills/construction/ConstructionKeys.kt` | `a98eccad` |
| F3 | slayer | `slayer` | `content/skills/slayer/SlayerKeys.kt` | `79d1d7bb` |
| F4 | prayerManager | `prayer_manager` | `content/skills/prayer/PrayerManagerKeys.kt` | `f548a115` |
| F5 | achievementDiaries | `achievement_diaries` | `content/achievementdiary/AchievementDiariesKeys.kt` | `44b07bb7` |

**Baselines (the new bootstrap numbers):**
- persistenceKey lines: **15**, all unique (`grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build`).
- Player.java `@Deprecated`: **30** (15 fields + 15 getters).
- Player.java content imports: **51** (F1 +FarmingKeys, F2 +ConstructionKeys,
  F4 +PrayerManagerKeys; slayer/achievementDiaries have no Player-internal uses so no
  import — Slayer setSlayer deleted as dead, AchievementDiaries had none to begin with).
- Repo-wide core→content imports (excl. `/content/` paths): **1195**
  (1021 at E-handover → +2 F1, +13 F2, +36 F3, +45 F4, +78 F5 — every increase is the
  new XKeys import in engine-side files that had call sites; enumerated per phase in the
  commit messages; still suspended-gate territory per E-handover §1).
- Legacy getter pinning (token counts, `grep -o | wc -l` repo-wide):
  - `getFarming()` = 11: Player decl + Farming.onInit parser read + 5 commented-out
    (CurePlant ×3, Geomancy ×2) + 4 on the UNRELATED `RaidFarming` class
    (Raid.java decl, RaidFarmingIOOA ×2, RaidFarmingPatchOA ×1).
  - `getConstruction()` = 3: decl + onInit parser read + 1 commented (Brazier.java).
  - `getSlayer()` / `getPrayerManager()` / `getAchievementDiaries()` = 2 each:
    decl + onInit parser read.
- Play-tests: **F1 (farming) and F2 (construction) passed in-game (Jesse).
  F3/F4/F5 NOT yet play-tested** — see §5.

## 2. Pattern amendments made during F (on top of E-handover §2)

1. **Store-before-copy accessor variant (slayer only).** `Assignment.initialize`
   re-reads `player.slayer()` for its back-ref mid-copy, so `SlayerKeys.slayer` stores
   the fresh instance in the attr BEFORE running copyFrom — reproducing the legacy
   behavior where the live field was already visible during setFields. Every other F
   accessor keeps the E-series store-after-copy shape. Use store-before only when the
   copy path provably re-enters the accessor.
2. **`copyFrom(X other)` replaces `initialize(Player player, Player parser)`**
   (slayer, achievementDiaries): identical body with parser reads inlined to `other`;
   sole caller was the deleted setFields line. Farming adopts via its pre-existing copy
   CONSTRUCTOR (`new Farming(player, other)` = the old wholesale setter body);
   construction and prayerManager reuse their pre-existing `setFields(Construction)` /
   `setPrayer(PrayerManager)` copy-intos.
3. **The one code deviation of the phase** (F2, flagged in its commit):
   `Construction.setFields`'s anomalous self-read
   `player.getConstruction().getArmourCase().setPlayer(player)` was normalized to
   `armourCase.setPlayer(player)` — value-identical on every legacy path (the getter
   always resolved to the instance setFields ran on), but a null-slot read / accessor
   reentry under the attr pattern. Comment at the site explains it.
4. **Legacy quirks preserved, do not "fix":** Slayer.copyFrom does NOT copy
   `lastAssignmentName` (the legacy initialize never did — javadoc'd at the method);
   prayer's always-true null checks in Player.applySmite keep their shape (still
   always-true through the accessor).
5. **Census methodology additions (E-handover §3 amendments):**
   - **Kotlin string templates contain real code** — F3's
     `UniversalShopInterface.kt` had `${...player.slayer.slayerPoints...}` that a
     quote-filtered census missed. Never filter quote-lines; finish with a residual
     scan for `.field` NOT followed by `(`.
   - **Receiver shapes beyond simple identifiers**: `((Player) target).getX()`,
     `(target as Player).x`, `X.getPlayer().getX()`, chained `instance.owner.getX()`,
     and bare implicit-receiver property reads inside Kotlin Player
     extensions/`player.apply {}`/attribute-delegate initializers. The scripted
     retargets end with a per-file remaining==0 assertion so any unhandled shape fails
     loudly instead of surviving silently.
   - Kotlin call sites import the accessor extension explicitly
     (`import com.zenyte.game.content.<pkg>.<accessor>`), same-package files excepted.
6. **Scanner rule refresher:** F1/F3/F4/F5 added a new `@Subscribe onInit`
   (scanner-relevant); F2's was a body-only conversion of the Phase 0 subscriber.
   `data/plugins.dat` regenerated locally each phase, never committed (gitignored).

## 3. Explicitly NOT done in F (future work, per the master plan)

- **prayerManager varbit conversion + drain-to-process-event** and
  **achievementDiaries XP-gained subscriber**: those belong to the later
  "core stops importing the Prayer enum / diary types" work. F4/F5 are the
  behavior-identical extractions only; per-tick `process()` and the ~400 `update()`
  sites are accessor-routed. `PlayerProcessEvent` was therefore NOT added.
- Rotation (§Rotation of the master plan) — Jesse's call. Rotation for F must also
  delete the commented-out legacy-getter references (CurePlant/Geomancy/Brazier) or
  accept the grep noise; the RaidFarming tokens are permanent (different class).
- Out-of-scope inventory unchanged: godBooks, retrievalService, privateStorage,
  gauntletItemStorage, petInsurance, stash, duel, puzzleBox/lightBox, respawnPoint,
  toaPlayerData, non-field content usages. `petId` stays a plain int by design.
- Leftover noted in F3: `Player.setHunter` (E2 remnant) has zero callers and no
  @Deprecated — fold into rotation cleanup.

## 4. Reentrancy map (why each accessor shape is safe) — for the rotation session

- farming: copy constructor touches only spots/storage; no self-reads.
- construction: setFields' self-read normalized (§2.3); costume-box getConstruction
  calls live in deposit/withdraw UI paths, unreachable from the copy.
- slayer: Assignment.initialize reads the accessor (store-before-copy handles it);
  bannedTasks is a PUBLIC field adopted by reference.
- prayerManager: setPrayer = one int assign; nothing reachable.
- achievementDiaries: copyFrom = two putAlls; nothing reachable.

## 5. Pending play-tests for F3–F5 (Jesse, before rotation or further phases)

1. **slayer (F3):** get a task from each master type incl. Sumona boss task and
   Krystilia; relog → task/amount/master persist under `attrPersistence.slayer`;
   block/unblock + banned list persists; store/unstore task; co-op partner set +
   partner logout message; kill count decrements + task completion (points varbit
   unaffected); Turael skip; superior spawns; wilderness slayer drops
   (WildernessSlayerDropProcessor); araxyte drop table bonus with araxyte task;
   slayer rewards interface unlocks (extend/unlock/points spending); slayer helm
   creation; task-only areas admit/refuse correctly; `resettask` dev command.
2. **prayerManager (F4):** quick-prayer selection persists across relog
   (`attrPersistence.prayer_manager`, only quickPrayerSettings inside); prayers
   activate/drain/deactivate at 0 points; protect-item on death; smite drains
   opponent; redemption/retribution trigger; Vile Vigour/thrall/sinister offering
   spells; ToB/Nex/DT2 boss prayer checks; prayer re-render after relog.
3. **achievementDiaries (F5):** progress a few diary steps, relog → progress
   persists (`attrPersistence.achievement_diaries`); complete a tier → congrats
   dialogue + pending reward granted at next login (the LoginEvent subscriber);
   lobby-close refresh shows correct tab values; diary rewards claiming; M008
   migration (Falador haircut) still applies; group-ironman diary tasks evaluate.
4. In every case: inspect `data/characters/<name>.json` — legacy top-level key
   (`slayer`, `prayerManager`, `achievementDiaries`) GONE after first
   post-migration save, attr key present; a PRE-migration save loads intact once
   (legacy fallback); a fresh account is unaffected.

Any mismatch: JSON snippet + stack trace before anything else is built.

## 6. Session bootstrap checklist for the next session

```bash
git log --oneline -1        # expect 44b07bb7 (or dry-verify everything if moved)
git status --short          # expect clean
./gradlew clean compileJava compileKotlin   # expect BUILD SUCCESSFUL
grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build | wc -l  # 15
grep -c "@Deprecated" core/src/main/java/com/zenyte/game/world/entity/player/Player.java  # 30
grep -c 'import com.zenyte.game.content' core/src/main/java/com/zenyte/game/world/entity/player/Player.java  # 51
```

Then: (a) if F3–F5 play-tests are unconfirmed, stop and ask; (b) rotation and the
event-era work (§3) each get their own investigation → mini-plan → execution cycle.
The bar remains D1's: every edit pre-verified, every gate numeric, behavior
byte-identical.
