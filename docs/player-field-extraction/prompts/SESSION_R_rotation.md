# SESSION PROMPT — Rotation (delete the legacy save-compat shims)

> Paste this into a fresh session. Fill in every `[JESSE: ...]` slot first —
> the session must stop and report if any slot is unfilled.

Continue the Player field-extraction campaign (OpenRune-Server alignment) in
MouldyToast/Offline_Scape. Phases 0/A/B/B.5/C1/C2, D1–D5, E1–E6, and F1–F5 are
MERGED TO MAIN and play-tested. Develop on the branch this session was
assigned, created from Main.

CONTEXT — read these before doing ANYTHING else, in this order:
1. docs/player-field-extraction/HANDOVER_after_phase_F.md (state, baselines,
   getter-pinning table, §4 reentrancy map).
2. docs/player-field-extraction/HANDOVER_after_phase_E.md (§2 house pattern,
   §3 census methodology, §4 rotation ruling).
3. The attached master spec PLAN_player_field_extraction_EXECUTABLE.md
   (§D3.5 and §Rotation). If it is not attached, stop and ask.

AUTHORIZATION — rotation is authorized ONLY for the fields listed here.
The live save files exist only on Jesse's machine, so the session CANNOT
verify save rotation itself; Jesse's list below IS the gate:

- [JESSE: for each field, run locally
    `grep -L '"<persistenceKey>"' data/characters/*.json`
  and list here every field whose result was EMPTY (all saves carry the attr
  key), or write "accept loss for <field>". The 15 keys: seed_vault,
  preset_manager, barrows, blast_furnace, dwarf_multicannon, gravestone,
  hunter, bounty_hunter, grand_exchange, lootkey_settings, farming,
  construction, slayer, prayer_manager, achievement_diaries.]

Any field NOT listed above is NOT rotated this session — no exceptions.

BOOTSTRAP (stop and report if any check fails):
- `git merge-base --is-ancestor fc106725 HEAD` → exit 0 (Phase F + handover
  merged); HANDOVER_after_phase_F.md exists.
- Clean tree; `./gradlew clean compileJava compileKotlin` → BUILD SUCCESSFUL.
- Baselines AT fc106725 (re-derive and explain from `git log` if Main moved,
  e.g. if any G-session landed first): persistenceKey lines 15 (unique);
  Player.java `@Deprecated` 30; Player content imports 51; repo-wide
  core→content imports (excl. `/content/` paths) 1195. Getter pinning per
  HANDOVER_after_phase_F §1 (farming 11, construction 3, slayer 2,
  prayer 2, achievementDiaries 2, plus the D/E getters at decl+parser-read).

TASK — per authorized field: INVESTIGATE, mini-plan, execute, ONE COMMIT PER
FIELD. What rotation deletes and what it must NOT touch:

DELETE per field:
1. The `@Deprecated` Player field + its javadoc.
2. The `@Deprecated` getter + javadoc. Before deleting the field's TYPE
   import from Player.java, grep the type name in Player — several types
   have other remaining uses (e.g. Prayer enum stays regardless); delete the
   import only when unused.
3. The onInit legacy-fallback branch: everything AFTER the
   `if (hadPersistedAttr || savedPlayer == null) return;` line, the
   `hadPersistedAttr`/`savedPlayer` locals where now unused, and the
   `@SuppressWarnings("deprecation")` parser read.
4. `scanGravestone` (GravestoneKeys.kt:69): its legacy-field branch dies with
   the gravestone getter; it becomes attrPersistence-raw-only. Re-check
   WealthScanner.kt / EcoSearch.kt / tools/discord DiscordEcoSearch.kt
   compile and still count gravestone wealth (handover E §3.3 — the discord
   module is NOT compiled by Gradle; fix it anyway by hand).
5. Pre-authorized housekeeping, each flagged in its commit: the dead
   `Player.setHunter` (E2 remnant, zero callers); the stale commented-out
   code blocks referencing deleted getters (CurePlant ×3 lines, Geomancy
   block ×2, Brazier ×1) for fields rotated this session.

DO NOT touch:
- The onInit subscribers themselves. After rotation each collapses to the
  eager accessor call (`XKeys.x(player);`) — that call is what rehydrates the
  raw attr at login BEFORE game code runs; deleting the subscriber would
  defer rehydration to first touch and change when varbits/containers
  initialize. Keep `@Subscribe` + signature intact (body-only edits ⇒ no
  plugin-scanner run needed; if you believe an annotation must change, stop
  and report).
- The copyFrom / copy-constructor / setFields / setPrayer / initialize
  copy-intos — the accessor rehydration path still uses every one of them.
- The Keys files, accessors, rawXAttr helpers.
- Any field not authorized above; anything in HANDOVER_after_phase_F §3
  (event-era work is a different session).

GATES per field (occurrence counts, `grep -o | wc -l`, never line counts):
- clean compile;
- `getX()` repo-wide → 0 (farming excepted: 4 tokens remain on the UNRELATED
  RaidFarming class — Raid.java decl, RaidFarmingIOOA ×2,
  RaidFarmingPatchOA ×1; anything else nonzero is a stop);
- Player `@Deprecated` decremented by exactly 2;
- persistenceKey count UNCHANGED at 15;
- Player content imports and repo-wide count: record the drop per field —
  from this session onward the master plan's monotone non-increasing
  expectation is REINSTATED (E-handover §1's suspension ends here).

QUALITY BAR — unchanged: behavior byte-identical for every migrated save; a
pre-migration save that skipped rotation loses the rotated field's data BY
DESIGN (that is what Jesse's gate authorized) — but a MIGRATED save must be
untouched. Zero/multiple FIND matches, unexplained census deltas, any
ambiguity → stop and report. Do not improvise.

END with: what rotated, every deviation, exact play-test steps for Jesse
(per field: migrated save loads intact; fresh account; WealthScanner +
EcoSearch over migrated saves; JSON shows attr key only), updated baselines
(persistenceKey / @Deprecated / both import counts), and write + commit
docs/player-field-extraction/HANDOVER_after_rotation.md in the style of the
existing handovers.
