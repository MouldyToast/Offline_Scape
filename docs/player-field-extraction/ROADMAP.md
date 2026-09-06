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

## 0. Current state (updated 2026-09-06, branch claude/player-field-extraction-rotation-54v409)

**Done and merged to Main:** Phases 0, A, B, B.5, C, D, E (all 20 fields
extracted to AttributeMap), F1–F5, Rotation (all 15 shims deleted; Player
has zero @Deprecated members).

**Done on this branch (6 commits, awaiting PR/merge + play-test):**
- G1: farming/hunter/prayer per-tick drivers → `PlayerProcessEvent`
  (published from Player.processEntity at the exact old position; ONE
  subscriber in `core/.../game/content/PlayerTickHooks.kt`).
- G2: CLOSED, nothing to land — diary XP-subscriber premise falsified
  (all 382 update sites are action objectives; zero threshold re-checks).
- G3: CLOSED, blocked — prayer varbit reads are NOT safe while the
  nightmare curse scrambles varbits vs. the activePrayers map (§Track 3).
- Cheap cleanup (adoptFarming, stale javadocs, api PlayerModel dead keys).
- E6 rider: pet spawn → `PlayerLoginEvent` (world entry, timing change
  accepted by Jesse), finish → `PlayerLogoutEvent`.

**Baselines (gate greps — every session re-runs these; monotone
non-increasing, a surprise increase is stop-and-report):**
```bash
grep -c 'import com.zenyte.game.content' core/src/main/java/com/zenyte/game/world/entity/player/Player.java      # 26
grep -rn "import com.zenyte.game.content\." core/src/main --include="*.java" --include="*.kt" | grep -v "/content/" | wc -l   # 1151
grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build | wc -l                                      # 15 (unique)
grep -c "@Deprecated" core/src/main/java/com/zenyte/game/world/entity/player/Player.java                          # 0
```
plugins.dat: 4355 plugins (untracked file; regenerate with
`./gradlew :app:runPluginScanner` after any @Subscribe/annotation change,
never commit it).

**Play-tests outstanding (Jesse, after merging this branch):**
- G1: prayer drain rate under combat + flicking, relog mid-drain; farming
  growth/weeds on schedule; hunter traps collapse on time, birdhouses
  tick; Whisperer sanity still drains prayer 3/tick in shadow realm.
- E6: pet out → relog → pet present at world entry; pick up/drop;
  insurance reclaim; metamorphosis; lobby-hop without logout → no
  duplicate pet.

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
| 1.1 | gauntletItemStorage | 2 | yes (self-init) | trivial |
| 1.2 | toaPlayerData | 2 | yes (@Expose field) | plain data holder; TOAManager.loadData is the main consumer |
| 1.3 | stash | 7 | yes (self-init) | |
| 1.4 | puzzleBox + lightBox | 11+10 | NO (transient) | no shim; treat like the C1 garg-instance move |
| 1.5 | petInsurance | 12 | yes | |
| 1.6 | godBooks | 13 | yes (self-init) | |
| 1.7 | respawnPoint | 16 | yes (enum) | consider just moving/keeping the enum core-side as a plain type instead of AttributeMap — decide in mini-plan |
| 1.8 | privateStorage | 19 | yes (self-init) | |
| 1.9 | retrievalService | 37 | yes (self-init) | |
| 1.10 | duel | 105 | NO (transient) | no persistence work, but the largest retarget |

Sequencing: 1.1–1.6 batch well (≈2 sessions), 1.7–1.9 one session each,
1.10 its own session. **≈4–6 sessions.**

Then:
- **Rotation 2** (one session, after Jesse cycles/deletes saves or the
  per-key `grep -L '"<key>"' data/characters/*.json` gate passes): delete
  the new shims + fallback branches, exactly like Rotation 1.
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
BountyHunter.onDeath deleted (D-4). EXCEPTION — clan move NOT done: the
plan's own pre-move audit hit (ClanChannel.onLogout, a
ListenerType.LOGOUT listener, removes the player from channel.members;
running ClanManager.leave after the LOGOUT plugins would early-return on
its members.remove check and silently drop ClanLeaveEvent + empty-channel
cleanup). ClanManager stays in Player (leave ~2150, join ~4340) pending
its own session. DEFER-1 residue is exactly 5 sites: Elysian
getPrayerPoints ~2955, faith-necklace restore ~3159, and the three
drainSkill overrides ~3504/~3512/~3520.

| Import(s) | Treatment |
|---|---|
| Prayer, PrayerManagerKeys | varbit reads DONE (T3.1a + T3.1b — Player.java no longer imports Prayer); remaining: id lift + DEFER-1 (5 sites above), per the G3 equivalence table (HANDOVER_after_G3.md §3 is the reference — the one historical doc still load-bearing) |
| Teleport, ForceTeleport, TeleportType | interface-lift teleport/spell surfaces onto core types, or move the calls behind events — needs the inventory session (~~SpellbookSwap, DeathChargeKt~~ DONE in T2-a) |
| Raid, RaidParty, Inferno | Phase-B flag lift on RegionArea (isRaid…/isInferno…) like ToA |
| ClanChannel, ClanManager | settings-driven; likely a core-side interface + content impl — see the T2-a exception above (ClanChannel.onLogout ordering) |
| Construction, RoomReference, ConstructionKeys | `getCurrentHouse()` instanceof-check — flag lift on RegionArea; Keys import then reviewable (tip-jar logout DONE in T2-a; DEFER-2 roomPreview stays) |
| CharterLocation, AdventurersLogIcon | small one-off lifts/moves; batch into one session (~~AvasDevice~~ DONE in T2-a) |
| FarmingKeys | ~~GrandExchangeKeys, GravestoneKeys, LootkeySettingsKeys, LootkeySettings~~ DONE in T2-a; FarmingKeys stays for the two movement-path refreshes ~1079/~1221 (DEFER-3) |

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
