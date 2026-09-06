# SESSION PROMPT — G3: prayer hot-path checks → varbit reads

> Paste this into a fresh session. Run LAST of the G-series — it is the
> highest-risk one (silent combat-behavior changes). Independent of
> Rotation. This session is allowed to conclude "not safe / not worth it"
> with an equivalence table as the deliverable.

Continue the Player field-extraction campaign (OpenRune-Server alignment) in
MouldyToast/Offline_Scape. Phases through F5 are MERGED TO MAIN and
play-tested. Develop on the branch this session was assigned, created from
Main. Rotation is NOT authorized here.

CONTEXT — read in this order:
1. docs/player-field-extraction/HANDOVER_after_phase_F.md (§3 defers this
   work; §1 baselines).
2. docs/player-field-extraction/HANDOVER_after_phase_E.md (§2, §3).
3. The attached master spec PLAN_player_field_extraction_EXECUTABLE.md
   (§Phase F, prayerManager bullet: varbit reads via **getBitValue**, not
   getBit; id-lifting note). If not attached, stop and ask.
4. Any later HANDOVER_after_*.md files that exist.

BOOTSTRAP (stop and report on failure):
- `git merge-base --is-ancestor fc106725 HEAD` → exit 0; clean tree;
  `./gradlew clean compileJava compileKotlin` → BUILD SUCCESSFUL.
- Baselines at fc106725 (re-derive with explanation if Main moved):
  persistenceKey 15; Player @Deprecated 30;
  `PrayerManagerKeys.prayerManager(` in *.java = 293.

GOAL — in CORE ENGINE HOT PATHS ONLY (combat classes, NPC.java, Player.java
protection/effect checks), replace
`PrayerManagerKeys.prayerManager(x).isActive(Prayer.Y)` with a server-side
varbit read, so the engine stops calling into prayer content per hit.
Content modules keep their accessor calls — they are NOT in scope.
Verified anchors at fc106725:
- `Prayer.getVarbit()` — Prayer.java:99; the enum maps varbit→prayer at :46.
- `VarManager.getBitValue(int)` — VarManager.java:216 (server-side value,
  NOT a client round-trip — re-verify by reading it).
- PrayerManager holds active prayers in a TRANSIENT
  `Object2IntOpenHashMap<Prayer> activePrayers` and calls sendBit 13 times.

TASK — INVESTIGATION FIRST; the equivalence proof IS the phase:

1. **Prove `isActive(p)` ⟺ `getBitValue(p.getVarbit()) == 1` at every
   moment the engine reads it.** Read PrayerManager end to end and write an
   equivalence table covering:
   - activation (handleActivation → is the varbit set synchronously, before
     control returns to the caller? collision prayers clearing each other's
     bits?);
   - deactivation: manual toggle, points hitting 0
     (deactivateActivePrayers), death, logout, deactivateWithoutRemoving
     (what does "without removing" mean for the BIT vs the MAP? — this is
     the likely divergence point, name it explicitly);
   - quick prayers on/off; the isActive representation (is the map value a
     cycle/counter — does isActive check presence or value?);
   - login/relog: bits re-sent vs map rebuilt — any window where map and
     varbit disagree while a hit can process.
   ANY state where map and varbit can disagree during a combat read →
   STOP AND REPORT with the table. Do not paper over it.
2. **Site census.** Enumerate every core-engine
   `PrayerManagerKeys.prayerManager(x).isActive(Prayer.Y)` token (combat
   classes, NPC.java, Player.java, npc impls under core) — token counts per
   handover §3. Sites doing MORE than isActive (drain, restore, effects,
   deactivate) are NOT converted — they need the manager and stay
   accessor-routed. List the exact convert set in the mini-plan with
   before/after per site.
3. **Id source decision.** Converted sites still need the varbit id. Two
   authorized shapes — pick one in the mini-plan and justify:
   (a) keep `Prayer.getVarbit()` at the call site (the Prayer enum import
       remains in core; smaller diff; defers enum removal to the later RSCM
       phase, per the master plan's note); or
   (b) lift the needed ids into a core-side constants holder (names matching
       the enum entries, values byte-verified against Prayer.java, with a
       comment naming the source) and remove the Prayer import from files
       that then no longer need it.
   Do NOT invent varbits rev-228 lacks; every id comes from the Prayer enum
   as it exists today.
4. **Execute** with per-edit occurrence assertions; one commit:
   `refactor(combat): prayer protection checks -> varbit reads (G3)`.

GATES: clean compile; converted-site count matches the mini-plan exactly;
`PrayerManagerKeys.prayerManager(` count drops by exactly that number;
non-isActive prayer calls untouched (count unchanged); no plugin-scanner
run needed (no annotation changes — verify); persistenceKey/@Deprecated
unchanged.

QUALITY BAR — this is live combat math. A protection prayer that reads
active one tick early/late changes PvP and boss fights; that is moving
backwards and worse than no phase. The equivalence table ships in the
report even on success. END with: what converted vs stayed and why,
deviations, play-test steps for Jesse (protect-from-melee/missiles/magic
vs NPC and PvP hits incl. prayer flicking on the tick; smite; redemption at
low HP; points hitting 0 mid-fight; quick-prayer toggle mid-combat; relog
mid-fight), updated baselines, and a committed
docs/player-field-extraction/HANDOVER_after_G3.md.
