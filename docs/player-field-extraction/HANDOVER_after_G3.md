# HANDOVER — G3: prayer hot-path varbit reads — VERDICT: NOT SAFE (curse divergence)

Audience: the next session on this campaign / follow-up cleanup. Supplements
HANDOVER_after_G1.md and HANDOVER_after_G2.md (same session, same branch:
`claude/player-field-extraction-rotation-54v409`). **No code changed in G3**
— the prompt's stop condition ("ANY state where map and varbit can disagree
during a combat read → STOP AND REPORT with the table") was met on a
by-design game mechanic. This document, with its equivalence table, is the
deliverable the prompt pre-declared valid.

## 1. Bootstrap / baselines (re-derived post-G1/G2)

Ancestor green, clean tree, clean build green (this session's G1 gate).
`PrayerManagerKeys.prayerManager(` in *.java = **292** — the fc106725
baseline was 293; G1 removed exactly one (the `process()` per-tick driver
line in Player.java, now in PlayerTickHooks.kt). persistenceKey 15;
Player @Deprecated 0 (rotation landed). `VarManager.getBitValue` verified
at source: a pure server-side read of the values array via
VarbitDefinitions (no client round-trip) — that half of the premise holds.

## 2. Site census (for the record)

`.isActive(Prayer.X)` call sites repo-wide (Java+Kotlin, excl. build):
**116**. In core engine hot paths (Player.java protection/effect checks,
core npc/combat classes): **~25**, of which 13 in Player.java + core npc
impls — these would have been the convert set. None was converted.

## 3. The equivalence table — `isActive(p)` vs `getBitValue(p.getVarbit()) == 1`

isActive(p) = `activePrayers.containsKey(p)` (presence check; the map VALUE
is the activation cycle, consumed only by the drain accumulator). All state
transitions audited from PrayerManager.java end to end:

| # | Transition | Map | Varbit | Equivalent during an engine read? |
|---|---|---|---|---|
| 1 | Normal activation (handleActivation) | put(p) | sendBit(p.varbit, 1), same call, synchronously before return | YES |
| 2 | **Activation under `nightmare_curse`** | put(p) | **sendBit(p.varbit, 0); sendBit(reverse(p).varbit, 1)** — the REVERSED protection prayer's bit is set instead (PrayerManager.handleActivation, :122-128) | **NO — FATAL, BY DESIGN** |
| 3 | Collision clearing (deactivateCollisions) | removeInt per collision | raw sendBit(varbit, 0) per collision — NOT curse-reversed, unlike rows 2/5 | YES uncursed; under curse the varbit surface is not even internally consistent |
| 4 | Manual deactivation (deactivatePrayer) | removeInt(p) | sendBit(…, 0) synchronously (via deactivateWithoutRemoving) | YES (uncursed) |
| 5 | deactivateWithoutRemoving under curse | (map untouched by this half) | zeroes **reverse(p)**'s varbit | mirrors row 2 |
| 6 | Points hit 0 (process → deactivateActivePrayers; setPrayerPoints ≤0) | forEach zeroes bits then clear() | bits 0 first, map cleared microseconds later | YES for engine reads — single world thread, no read can interleave the window |
| 7 | Death | Player's death reset calls deactivateActivePrayers (Player.java ~1418) | both sides cleared | YES |
| 8 | Logout/relog | map is transient → empty on login | prayer varbits are NOT in VarManager.persistentVars (registrations audited: interface settings, cape customizer, soulreaper charges — no prayer ids); non-persisted vars start 0 server-side | YES — both sides reset |
| 9 | Quick prayers on/off | routes through activatePrayer/deactivateActivePrayers | same writes as rows 1/6 (plus varbit 4103 for the orb) | YES |
| 10 | "deactivateWithoutRemoving" semantics | the name is about the MAP (bit zeroed, map entry retained by the caller's choice); sole caller outside deactivatePrayer is deactivateActivePrayers, which clears the map itself right after | benign given row 6 |

**Row 2 is the stop condition.** `nightmare_curse` is live content: set by
BaseNightmareNPC.java:402 (removed :407) during The Nightmare's curse
attack, checked by NightmareBossArea.java:89. While cursed, the map records
the prayer whose EFFECT the player has, and the varbits display the
scrambled state — that is the boss mechanic. The engine's protection checks
(Player.java protection block, reading PROTECT_FROM_MELEE/MISSILES/MAGIC —
exactly the hot-path reads G3 targets) would, as varbit reads, apply the
WRONG protection prayer for the entire cursed window of every Nightmare
fight. That is a silent combat-behavior change, the precise thing the
quality bar forbids. Not convertible while the curse is implemented as
map-vs-varbit divergence. No paper-over attempted.

## 4. The follow-up path (recorded, per the OpenRune ANSWERS)

OpenRune has no map — the varbit IS the truth (engine-level
`enabledPrayers by intVarBit(...)`, drain as a content soft-timer script).
Under that model a Nightmare-style curse must be implemented by scrambling
the effect mapping (or the activation routing), never by letting display
and truth diverge. The real G3-enabling refactor is therefore: reimplement
the nightmare curse so that varbits always reflect true effect (e.g. route
the reversal at togglePrayer input, as cursePrayerTypeReverse already
hints), THEN convert the ~25 engine reads. That is Nightmare-content
surgery + a re-run of this equivalence audit — its own session, needing its
own play-test at the boss. Until then every isActive read stays
accessor-routed.

## 5. Baselines — UNCHANGED by G3 (G1's numbers stand)

persistenceKey 15; Player @Deprecated 0; Player content imports 37;
repo-wide core→content (excl. /content/) 1181; PrayerManagerKeys tokens
292; isActive(Prayer.…) sites 116. No play-test needed: nothing changed.

## 6. Campaign state after this session (G1+G2+G3 all resolved)

- **G1 SHIPPED** (PlayerProcessEvent; see HANDOVER_after_G1.md — play-test
  steps there are the ones that matter).
- **G2 CLOSED, nothing to land** (census falsified the premise; see
  HANDOVER_after_G2.md).
- **G3 CLOSED, not safe** (this document; re-openable only after the
  nightmare-curse reimplementation above).
- The G-series is complete. The cheap-cleanup pass LANDED in this session
  (same branch, commit after this doc): FarmingKeys.adoptFarming deleted
  (orphaned since rotation), the nine stale "legacy load path above/in
  onInit" copyFrom javadoc halves rewritten to name only the Keys attr
  rehydration, and api/PlayerModel.kt's two doubly-dead legacy top-level
  key mappings (seedVault, gravestone) removed from PlayerData and
  containerWrapperList (note: :api IS compiled — core → core-model → api —
  contrary to the rotation handover's "nothing consumes :api"; nothing
  references those two properties, verified). Historical "mirrors the
  legacy semantics" provenance notes were deliberately kept — they are
  still true and explain non-obvious copy behavior.
- Remaining campaign threads: the out-of-scope Player member inventory
  (HANDOVER_after_rotation.md §6), the E6 follower rider (still
  unauthorized — needs Jesse's ruling between the two spawn-timing
  options), and the recorded soft-timer end-state for PlayerProcessEvent
  and the nightmare-curse reimplementation (§4 above).
