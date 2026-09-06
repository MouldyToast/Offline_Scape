# HANDOVER — G2: diary XP-gained subscriber — VERDICT: NOT WORTH IT (0 sites)

Audience: the next session on this campaign (G3, or follow-up cleanup).
Supplements HANDOVER_after_G1.md (same session, same branch:
`claude/player-field-extraction-rotation-54v409`). **No code changed in G2**
— this document is the deliverable, exactly the outcome the G2 prompt
pre-declared valid ("a truthful classification table is a valid
deliverable; a forced migration is not").

## 1. Bootstrap / baselines (re-derived post-G1)

Ancestor check green, clean tree, clean build green (G1's gate build, same
session). Accessor tokens: `AchievementDiariesKeys.achievementDiaries(` in
*.java = **397** (matches the fc106725 baseline — G1 touched none), Kotlin
`.achievementDiaries()` = 18 calls + 1 declaration = 19 hits (matches).
`update(`/`finish(` call sites repo-wide (Java + Kotlin, excl. build):
**382** — 372 Java + 10 Kotlin.

## 2. The census and classification (the deliverable)

| Category | Definition (from the prompt) | Sites | Verdict |
|---|---|---|---|
| (a) LEVEL/XP-THRESHOLD | objective is "reach level X in skill Y", call sits at an XP-award site to re-check the threshold | **0** | nothing to migrate |
| (b) ACTION-SPECIFIC | "make a mind tiara", kill counts, laps, prayers at altars, interactions | ~370 (the overwhelming majority of the 380 enum-constant sites) | stays direct, by definition |
| (c) XP-COINCIDENT | call sits at an XP-award site, objective is "perform the action that awards this XP" | the remainder (skilling-embedded sites; see §3) | NOT migratable safely — every examined one is a compound/flag or item-specific objective |
| framework | GameCommands.java:2478 admin `finish(d)`; AchievementDiaries.java internal (finish→update, LOBBY_CLOSE refresh, LoginEvent reward-revoke) | 2 | not content triggers |

**Why category (a) is provably empty** (three independent confirmations):

1. `Skills.java` and `Player.java` contain **zero** diary accessor calls —
   there is no XP-award-site threshold re-check anywhere in the engine.
2. The 12 diary enums contain **no** level-threshold constants and **zero**
   references to `getLevel`/`getSkills()` in their predicates. The only
   "level" string in any enum is KandarinDiary.READ_BARBARIAN_ASSAULT_
   BLACKBOARD ("level 5 in every role") — an action task, and
   `autoCompleted = true` anyway.
3. The system's design handles unimplemented/threshold-style OSRS tasks via
   `autoCompleted() == true`, which `isCompleted()` short-circuits to
   `true` — such tasks never need, and never receive, `update()` calls.

**The master plan's premise is FALSE for this codebase.** Its
achievementDiaries bullet claims "the ~100+ update(...) calls across skills
are replaced by an XP-gained subscriber". The ~100+ calls embedded in
skilling code are category (c) action objectives that merely *coincide*
with XP awards; none is a threshold re-check. The G2 prompt already
flagged this claim as doubly suspect after the OpenRune findings (no
diaries upstream, no per-XP broadcast upstream, level-advance-only engine
queues); the census now falsifies it outright.

## 3. Why the (c) sites cannot ride ExperienceGained (worked examples)

`PlayerEvent.ExperienceGained(player, skill, baseExperience,
finalExperience)` is posted at Skills.java:346 — after
`getExperience()[skill] += exp` but BEFORE the level array updates, and
only when `!fake`. It carries skill id and xp amounts, nothing else.

- **KourendDiary.CATCH_ANGLERFISH** — flag-based compound: Fishing.java:184
  sets flag 1 (the catch), Cooking.java:102 sets flag 2 (the cook). A
  subscriber seeing (FISHING, 120.0, xN) would have to infer "that was an
  anglerfish" from XP amounts — baseXp collides across catch tables and
  finalXp varies with every modifier. Not reproducible.
- **VarrockDiary.CHOP_AND_BURN_YEW_LOGS** — same shape: Woodcutting
  .java:252 flag 1, FiremakingAction.java:121 flag 2, plus the diary's own
  area predicate. Same inference problem, doubled.
- The pattern generalizes: the (c) population is dominated by
  CHOP_AND_BURN_*/CATCH_AND_COOK_*/SMITH_AND_FLETCH_*/item-specific
  objectives whose trigger is "this exact action on this exact item (in
  this exact place)" — which is an ACTION hook, precisely what OpenRune
  would model it as (the prompt's ANSWERS raised the bar for (c)
  accordingly). Default NOT migrating applies to every one.

**Recommendation: do not land an ExperienceGained diary subscriber at all.**
There are zero sites for it to serve; landing it would be dead
infrastructure plus 382 accessor calls left untouched either way. The
existing accessor-routed calls are already post-extraction clean (they go
through AchievementDiariesKeys, not a Player field) — G2's real work was
done by F5/Rotation.

## 4. Registration-shape note (for any future subscriber work)

If a WorldHooks PlayerEvent subscriber is ever needed: live precedents are
`event.worldThread.hook<PlayerEvent.Process> { ... }` (NexModule.kt:84),
`hook<PlayerEvent.Died>` (RevenantModule.kt:29), `hook<PlayerEvent
.SlayerTaskCompleted>` (WildernessSlayerModule.kt:26) — all inside
ServerLaunchEvent subscribers. The only in-repo ExperienceGained
subscriber is commented out (WildernessHotZoneModule.kt:29). WorldHooks
registration happens at runtime inside an already-scanned @Subscribe — a
new hook<> call in an existing module is NOT scanner-relevant by itself.

## 5. Baselines — UNCHANGED (no code touched)

persistenceKey 15; Player @Deprecated 0; Player content imports 37;
repo-wide core→content imports (excl. /content/) 1181; accessor tokens 397
Java / 18+1 Kotlin. No play-test needed for G2: nothing changed.

## 6. Next work

- G3 (prayer hot-path varbit reads) — run per its prompt; allowed to
  conclude "not safe / not worth it" with an equivalence table.
- The cheap-cleanup candidates and out-of-scope inventory carry over
  unchanged from HANDOVER_after_G1.md §6.
- If Jesse ever wants diary progress event-driven, the OpenRune-aligned
  shape is per-action content events (catch-fish, burn-log, …), not an XP
  broadcast — that is new-event-design work, far beyond this campaign.
