# HANDOVER — G1: per-tick content drivers → PlayerProcessEvent

Audience: the next session on this campaign (G2 diary XP census, G3 prayer
varbits, or follow-up cleanup). Supplements HANDOVER_after_rotation.md (whose
§4 baselines were this session's starting point), HANDOVER_after_phase_F.md
and HANDOVER_after_phase_E.md. Branch:
`claude/player-field-extraction-rotation-54v409` (name says "rotation" — it
is the auto-assigned branch of the session that ran G1 after rotation had
already merged as PR #17), created from Main tip `c37503a5` (PR #18 merge).

## 1. What landed (one commit)

`refactor(core): per-tick content drivers -> PlayerProcessEvent (G1)`

- **PlayerEvents.kt** — added `class PlayerProcessEvent(val player: Player) :
  UnboundEvent`, javadoc pins the publish position and names it a
  transitional zenyte bridge (OpenRune's end-state is per-system soft
  timers — see §5).
- **Player.java** — the three driver lines (`FarmingKeys.farming(this)
  .processAll(); HunterKeys.hunter(this).process(); PrayerManagerKeys
  .prayerManager(this).process();`, formerly :1833-1835) replaced IN PLACE
  by the standard guarded publish
  (`if (CoresManager.worldThread != null) ... publish(new
  PlayerProcessEvent(this))`). Unconditional publish (locked decision:
  EventBus has no hasListenersFor; zero-subscriber publish is a cheap map
  miss). The now-unused `HunterKeys` import deleted;
  `org.rsmod.game.events.PlayerProcessEvent` import added.
- **PlayerTickHooks.kt** (new,
  `core/src/main/kotlin/com/zenyte/game/content/PlayerTickHooks.kt`,
  TOAAccess idiom: `@file:JvmName` + top-level `@Subscribe fun
  onServerLaunch(ServerLaunchEvent)`) — ONE
  `bus.subscribeUnbound(PlayerProcessEvent::class.java)` lambda calling
  `player.farming().processAll()` → `player.hunter().process()` →
  `player.prayerManager().process()`, today's exact order, NO internal
  try/catch (deliberate — see §2 exception semantics).

## 2. The ordering trace (the make-or-break deliverable)

**WorldThread side** (`WorldThread.processPlayers()`, ~:447-465): per player,
inside a per-player `catch (Throwable)`: `skipPlayer` check →
`hooks.post(new PlayerEvent.Process(player))` (gated on hasListenersFor) →
`player.processEntity()`. So the WorldHooks Process event fires BEFORE the
entire processEntity body.

**Player.processEntity() intra-tick order** (verified at this session's
base):
1. granite-maul auto-aggression (own try/catch)
2. routeEvent.process (own try/catch)
3. postPacketProcessingRunnables (own try/catch)
4. ONE shared `try { ... } catch (Exception e) { log.error }` containing, in
   order: actionManager.process() [own inner catch] → variables.process() →
   AvasDevice.collectMetal → controllerManager.process() [own catch] →
   gravestone process [own catch] → cutsceneManager → music →
   charge-degradation (tickDegradable) block → **THE THREE DRIVERS** →
   acid-pool venom check → Whisperer sanity block (which itself calls
   `prayerManager().drainPrayerPoints`) → burn ticks → aranea boots →
   araxxor acid drip → run-energy restore
5. `super.processEntity()` (movement etc.)
6. post-movement: routeEvent.processAfterMovement, actionManager
   .processAfterMovement, appendNearbyNPCs, damage sound, faced entity,
   world map.

**Verdict:** publishing from the WorldThread loop is NOT ordering-identical —
it would move the drivers before actionManager.process() (combat processing
/ hit application), controller processing, and charge degradation, or after
movement, and would swap the exception envelope from "skip the rest of the
shared try block" to "skip the player's whole tick" (WorldThread's
catch(Throwable)). Prayer drain relative to hits, and farming/hunter timing
relative to controllers and movement, would all shift. Therefore the
PRE-AUTHORIZED fallback was used: publish from Player.java at EXACTLY the
old position. Byte-identical ordering by construction.

**Exception semantics (repo-local half, settled):** the three calls sat
un-wrapped inside the shared step-4 try/catch — a throw in farming skipped
hunter + prayer + the rest of the block, was logged at the shared catch, and
the tick continued at super.processEntity(). EventBus.publish has no
try/catch (settled from our verbatim copy), so a subscriber throw propagates
to the publish site — the same position inside the same try. The subscriber
body therefore has NO internal catch, and behavior is identical: a throwing
driver skips the remaining drivers and the rest of the block, logged by the
same handler. The `worldThread != null` guard never skips in practice
(processEntity is only invoked from WorldThread) — kept for consistency with
the three existing publish sites.

## 3. Deviations and findings

- **Session provenance:** this session was started from the rotation
  handover (not the G1 prompt file); the master plan
  PLAN_player_field_extraction_EXECUTABLE.md was attached by Jesse
  mid-session. The G1 prompt (post-c0ea2dcb version, with the OpenRune
  ANSWERS) was then followed as written.
- **E6 follower rider: NOT AUTHORIZED** (Jesse left the default) — not done.
- **Cleanup accounting, honest:** the import-count win is exactly −1
  (HunterKeys). FarmingKeys stays in Player (3 `refresh()` calls at ~1071,
  ~1213, ~4417); PrayerManagerKeys stays (~20 uses: protection checks,
  smite, redemption, retribution, Whisperer drain, quick-prayer refresh…).
  The goal achieved is the architecture (drivers off Player), not the
  count.
- **Per-tick drivers NOT fully off Player:** the Whisperer sanity block
  still calls `prayerManager().drainPrayerPoints` inline one statement
  after the publish — it is boss-mechanic damage, not the prayer drain
  driver, and was out of G1's stated scope. Noting it so nobody mistakes
  the grep hit for a missed driver.
- **plugins.dat:** regenerated via `:app:runPluginScanner` (the new
  top-level `@Subscribe fun` is scanner-relevant); the file is untracked in
  git and stays uncommitted, per the G1 prompt.
- Both clean builds (pre-edit bootstrap and post-edit authoritative gate)
  BUILD SUCCESSFUL.

## 4. Updated baselines

```
grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build | wc -l   # 15 (unchanged)
grep -c "@Deprecated" core/.../player/Player.java                              # 0 (unchanged)
grep -c 'import com.zenyte.game.content' core/.../player/Player.java           # 37 (was 38; -1 HunterKeys)
grep -rn "import com.zenyte.game.content\." core/src/main --include="*.java" --include="*.kt" | grep -v "/content/" | wc -l   # 1181 (was 1182)
```

PlayerProcessEvent: published from exactly ONE site (Player.java, the old
driver position); subscribed in exactly ONE place (PlayerTickHooks.kt).
Monotone non-increasing import expectation holds (−1). Note:
PlayerTickHooks.kt itself imports the three Keys accessors, but its path is
under `/content/`, so it is rightly excluded from the repo-wide gate.

## 5. Play-test steps for Jesse

Per-tick behavior is the whole risk surface; ordering is proven identical on
paper, so this is confirmation, not exploration:

- **prayer**: activate an overhead + offensive prayers, fight something —
  drain proceeds at the normal rate; flick prayers (drain accounting per
  tick unchanged); drain to 0 → prayers deactivate; relog mid-drain →
  quick-prayer setup intact, drain resumes.
- **farming**: plant, watch weeds/growth tick on schedule; patches refresh
  varbits when walking into a farming area (the refresh() calls in Player
  are untouched).
- **hunter**: set box/bird traps — they collapse/expire on time; birdhouses
  tick to completion; traps still dismantle at logout (LogoutEvent
  subscriber untouched).
- **Whisperer sanity** (edited block's neighborhood): sanity drains in the
  shadow realm, restores outside, prayer drains 3/tick during shadow-realm
  hits — unchanged.
- **throw-isolation spot check (optional)**: nothing to test manually;
  reasoning in §2 covers it.

Any mismatch: stack trace + which driver, before anything else is built.

## 6. What this unlocks / next work

- G2 (diary XP subscriber census) and G3 (prayer varbit reads — run LAST)
  prompts remain in docs/player-field-extraction/prompts/, each still
  needing its own investigate → mini-plan → execute cycle.
- **Recorded follow-up (from the OpenRune ANSWERS):** PlayerProcessEvent is
  a transitional bridge. OpenRune's end-state is per-system soft timers
  (softTimer registered at login, handled at a fixed slot in the per-player
  process order with tryOrDisconnect isolation). When a timer system
  exists, farming/hunter/prayer each become their own timer and
  PlayerProcessEvent dies.
- Cheap cleanup candidates unchanged from the rotation handover §6:
  FarmingKeys.adoptFarming (orphaned), the stale "legacy load path above"
  javadoc halves, api/PlayerModel.kt.
- Out-of-scope inventory unchanged (godBooks, retrievalService,
  privateStorage, gauntletItemStorage, petInsurance, stash, duel,
  puzzleBox/lightBox, respawnPoint, toaPlayerData, non-field content
  usages).
