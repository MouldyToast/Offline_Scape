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
   - ONE subscriber preserving today's inter-driver order. Verify
     EventBus.kt's subscribeUnbound invocation-order semantics from source;
     regardless of the answer, the safe shape is a single
     `bus.subscribeUnbound(PlayerProcessEvent::class.java)` lambda calling
     farming → hunter → prayer in today's exact order, registered from one
     `@Subscribe fun onServerLaunch(ServerLaunchEvent)` in a new hooks file
     (suggested: core/src/main/kotlin/com/zenyte/game/content/PlayerTickHooks.kt
     or per-skill files — pick ONE shape, justify in the mini-plan, match
     TOAAccess idiom).
   - Exception semantics: check what a throw inside today's tick-block calls
     does (propagates to what catch?) vs what EventBus.publish does with a
     throwing subscriber (read EventBus.kt). If they differ, wrap the
     subscriber body to reproduce today's behavior; note it.
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
