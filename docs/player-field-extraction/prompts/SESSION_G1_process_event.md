# SESSION PROMPT — G1: PlayerProcessEvent + per-tick drivers off Player

> Paste this into a fresh session. Fill in the `[JESSE: ...]` slot (or leave
> the default). Run AFTER F1–F5 are merged; independent of Rotation — the
> bootstrap says how to re-derive if Rotation landed first.

Continue the Player field-extraction campaign (OpenRune-Server alignment) in
MouldyToast/Offline_Scape. Phases through F5 are MERGED TO MAIN and
play-tested. Develop on the branch this session was assigned, created from
Main. Rotation (§Rotation) is NOT authorized in this session — do not delete
any @Deprecated slot, getter, or fallback branch.

CONTEXT — read in this order:
1. docs/player-field-extraction/HANDOVER_after_phase_F.md (§3 lists this as
   deferred work; §1 baselines).
2. docs/player-field-extraction/HANDOVER_after_phase_E.md (§2 house pattern).
3. The attached master spec PLAN_player_field_extraction_EXECUTABLE.md
   (§Phase F "Process-tick hook"). If not attached, stop and ask.
4. If docs/player-field-extraction/HANDOVER_after_rotation.md exists,
   Rotation landed first — read it and use ITS baselines.

BOOTSTRAP (stop and report on failure):
- `git merge-base --is-ancestor fc106725 HEAD` → exit 0; clean tree;
  `./gradlew clean compileJava compileKotlin` → BUILD SUCCESSFUL.
- Baselines at fc106725 (re-derive with explanation if Main moved):
  persistenceKey 15; Player @Deprecated 30; Player content imports 51.

ANSWERS FROM OPENRUNE (verified against OpenRune-Server @ `abc80a8`,
cloned 2026-09-06 with the master plan's B5.1 command; re-clone to
`/tmp/openrune` if you need to re-check — its `api/attr` files are still
39+184 lines, exactly what our B5 copy expected, so upstream has not
drifted). These settle questions earlier drafts left open — do not
re-litigate them, but DO flag if the repo contradicts them:

- **OpenRune has NO per-player broadcast process event.** Its
  `GameCycle.tick()` (api/game-process/.../GameCycle.kt) publishes only
  cycle-level `GameLifecycle.StartCycle/LateCycle/EndCycle` and runs a fixed
  processor pipeline. Per-player periodic content work is TIMER-driven:
  content registers `softTimer(...)` at login and handles
  `onPlayerSoftTimer("timer.x")` (precedents: StatRegenScript,
  PrayerDrainScript), processed at a fixed slot in PlayerMainProcess's
  per-player order (queues → timers → areas → engineQueues → interactions),
  with each player wrapped in `tryOrDisconnect` (a throwing player is
  disconnected; others are unaffected).
- **Consequence:** `PlayerProcessEvent` is a TRANSITIONAL zenyte-bridge
  sanctioned by the master plan, not the OpenRune end-state (which is
  per-system timers — record that as follow-up work in your handover).
  Since WorldThread-loop placement buys nothing toward that end-state,
  DEFAULT to the Player-position publish (byte-identical ordering, see
  Task 1) unless the ordering investigation proves the loop placement
  identical anyway.
- **EventBus semantics (answered from OUR verbatim copy, EventBus.kt +
  EventMap.kt):** `subscribeUnbound` appends to a per-type MutableList ⇒
  subscribers run in REGISTRATION ORDER; `publish` has NO try/catch ⇒ a
  throwing subscriber propagates to the publish site. The single-subscriber
  shape below therefore preserves inter-driver order by construction; the
  remaining exception question is repo-local only (Task 2's last bullet:
  what try/catch surrounds today's three calls in Player).
- OpenRune has no farming or hunter content yet — no upstream tick
  precedent for those two beyond the general timer idiom.

GOAL — move the three per-tick content drivers out of Player's tick block
onto the OpenRune EventBus, matching how C2 moved the ToA hooks. Verified
anchors at fc106725 (re-anchor by grep before editing):
- The tick block: Player.java:2025-2027 —
  `FarmingKeys.farming(this).processAll();`
  `HunterKeys.hunter(this).process();`
  `PrayerManagerKeys.prayerManager(this).process();`
- WorldThread.java:449-455 — `hooks.hasListenersFor(PlayerEvent.Process...)`
  gate + `hooks.post(new PlayerEvent.Process(player))`; EventBus field at
  :83, getter at :701.
- Event definitions: core/src/main/kotlin/org/rsmod/game/events/PlayerEvents.kt.
- Registration precedent: content/raids/toa/.../TOAAccess.kt:28-45 —
  `@Subscribe fun onServerLaunch(event: ServerLaunchEvent)` →
  `bus.subscribeUnbound(PlayerLoginEvent::class.java) { ... }`.
- Publish precedent with null guard: Player.java:1168 / 2372 / 4525
  (`CoresManager.worldThread.getEventBus().publish(...)`).

TASK — INVESTIGATION FIRST, then mini-plan, then execute:

1. **Ordering investigation (the make-or-break step).** Map the exact
   execution order of the three driver calls INSIDE Player's tick method
   (what runs before/after them in that method: toxins, charges, hits,
   movement?) versus where WorldThread posts `PlayerEvent.Process` relative
   to invoking that Player method. Write the trace down.
   - Publishing the new event from the WorldThread loop is authorized ONLY
     if the intra-tick ordering is proven identical (prayer drain relative
     to incoming hits, farming growth vs varbit sends, hunter trap timing).
   - Otherwise use the PRE-AUTHORIZED fallback: publish
     `PlayerProcessEvent(this)` from Player.java at EXACTLY the position of
     the three lines (replacing them), with the standard
     `CoresManager.worldThread != null` guard. Ordering stays byte-identical
     and the decoupling goal is still met. Anything else → stop and report.
2. **Event + subscriber shape.**
   - Add `class PlayerProcessEvent(val player: Player) : UnboundEvent` to
     PlayerEvents.kt. Publish unconditionally (locked decision: EventBus has
     no hasListenersFor; a zero-subscriber publish is a cheap map miss).
   - ONE subscriber preserving today's inter-driver order: a single
     `bus.subscribeUnbound(PlayerProcessEvent::class.java)` lambda calling
     farming → hunter → prayer in today's exact order, registered from one
     `@Subscribe fun onServerLaunch(ServerLaunchEvent)` in a new hooks file
     (suggested: core/src/main/kotlin/com/zenyte/game/content/PlayerTickHooks.kt
     — match the TOAAccess idiom). Registration-order and no-catch publish
     semantics are already settled (see ANSWERS above).
   - Exception semantics, repo-local half: find what try/catch surrounds
     today's three calls in Player's tick method (and WorldThread's
     per-player loop). EventBus.publish propagates subscriber throws to the
     publish site (settled); if today's calls sit inside a catch that the
     publish site would not reproduce, wrap the subscriber body to match
     today's behavior; note it.
3. **Cleanup accounting.** After the move, check which Keys imports Player
   still needs: FarmingKeys stays (three refresh() calls remain),
   PrayerManagerKeys stays (~19 other uses); HunterKeys may become unused —
   delete only if grep proves zero remaining Player uses. Be honest in the
   report: the import-count win is small; the goal is the OpenRune
   process-event architecture.
4. **Plugin scanner**: the new top-level `@Subscribe fun` is scanner-relevant
   ⇒ `./gradlew :app:runPluginScanner`; data/plugins.dat stays uncommitted.

OPTIONAL RIDER — E6 follower event move (E-handover §4 ruling 1):
[JESSE: default NOT AUTHORIZED. To authorize, state the ruling here —
either "move pet spawn behind PlayerLoginEvent, timing change accepted" or
"add a dedicated lobby-close event and move spawn behind it".]

GATES: clean compile; the three driver calls gone from Player's tick block
(grep occurrence counts before/after); `PlayerProcessEvent` published from
exactly ONE site; subscriber registered once; per-tick behavior spot-check
reasoning written down; scanner run; persistenceKey/@Deprecated counts
UNCHANGED; commit
`refactor(core): per-tick content drivers -> PlayerProcessEvent (G1)`.

QUALITY BAR — byte-identical behavior; the ordering trace is part of the
deliverable. END with: what landed, deviations, play-test steps for Jesse
(prayer drains at normal rate under flicking and combat; farming
grows/weeds on schedule; hunter traps collapse on time; birdhouses tick;
relog mid-drain), updated baselines, and a committed
docs/player-field-extraction/HANDOVER_after_G1.md.
