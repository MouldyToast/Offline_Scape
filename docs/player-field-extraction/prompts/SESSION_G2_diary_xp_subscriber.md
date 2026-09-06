# SESSION PROMPT — G2: achievementDiaries XP-gained subscriber

> Paste this into a fresh session. Run AFTER F1–F5 are merged; independent
> of Rotation and G1 (bootstrap says how to re-derive if they landed first).
> IMPORTANT: this session is allowed — encouraged — to conclude "mostly not
> worth it" if the census says so. A truthful classification table is a
> valid deliverable; a forced migration is not.

Continue the Player field-extraction campaign (OpenRune-Server alignment) in
MouldyToast/Offline_Scape. Phases through F5 are MERGED TO MAIN and
play-tested. Develop on the branch this session was assigned, created from
Main. Rotation is NOT authorized here.

CONTEXT — read in this order:
1. docs/player-field-extraction/HANDOVER_after_phase_F.md (§3 defers this
   work; §1 baselines).
2. docs/player-field-extraction/HANDOVER_after_phase_E.md (§3 census
   methodology — token counts, string templates, receiver shapes).
3. The attached master spec PLAN_player_field_extraction_EXECUTABLE.md
   (§Phase F, achievementDiaries bullet). If not attached, stop and ask.
   NOTE: the master plan claims "the ~100+ update(...) calls are replaced by
   an XP-gained subscriber" — treat this as an ANCHOR, NOT GOSPEL. The
   campaign has already proven the plan's censuses wrong several times.
4. Any HANDOVER_after_rotation.md / HANDOVER_after_G1.md that exists.

BOOTSTRAP (stop and report on failure):
- `git merge-base --is-ancestor fc106725 HEAD` → exit 0; clean tree;
  `./gradlew clean compileJava compileKotlin` → BUILD SUCCESSFUL.
- Baselines at fc106725 (re-derive with explanation if Main moved):
  persistenceKey 15; Player @Deprecated 30;
  `AchievementDiariesKeys.achievementDiaries(` in *.java = 397, Kotlin
  accessor calls 18 (+1 declaration).

GOAL — replace diary `update(...)` calls that are PURELY experience-driven
with one `ExperienceGained` subscriber, and ONLY those. Verified anchors at
fc106725:
- `PlayerEvent.ExperienceGained(player, skill, baseExperience,
  finalExperience)` — core/src/main/kotlin/com/near_reality/game/world/PlayerEvent.kt:36
  (WorldHooks family, NOT the org.rsmod EventBus — find where it is posted
  and how subscribers register on WorldHooks before building on it, and
  confirm it fires for EVERY xp grant: normal training, lamps, bonus xp,
  quest rewards).
- The diary API: AchievementDiaries.update/finish/setFinished, Diary
  interface (objectiveName/objectiveLength/autoCompleted), per-area enums
  under com.zenyte.game.content.achievementdiary.diaries.

TASK — INVESTIGATION FIRST:

1. **Full census + classification.** Enumerate every
   `AchievementDiariesKeys.achievementDiaries(...)` call site (Java + Kotlin,
   token counts per handover §3 methodology) and classify each `update`/
   `finish` call:
   (a) LEVEL/XP-THRESHOLD — the diary objective is literally "reach level X
       in skill Y" and the call sits at an XP-award site solely to re-check
       the threshold;
   (b) ACTION-SPECIFIC — "make a mind tiara", "enter the KBD lair", kill
       counts, interactions: NOT migratable by an XP event, stays a direct
       accessor call;
   (c) XP-COINCIDENT — the call sits at an XP-award site and the objective
       is "perform the action that awards this XP" (e.g. "catch a shark in
       Catherby"): migratable ONLY if the event carries enough context
       (skill + location test reproducible in the subscriber) — judge
       case-by-case, default to NOT migrating.
   Paste the classification table into the mini-plan. If category (a)+(safe
   c) is small, STOP AND REPORT the table and recommendation — Jesse decides
   whether the subscriber is worth landing at all.
2. **Mini-plan, then execute** for the approved categories only:
   - One subscriber (WorldHooks idiom — find an existing
     `PlayerEvent.ExperienceGained` or other PlayerEvent subscriber in-repo
     and copy its registration shape exactly; if none exists, stop and
     report rather than inventing one).
   - Per migrated site: delete the update call, prove the subscriber
     reproduces it — SAME diary, SAME trigger conditions, no
     double-counting (site call removed in the same commit the subscriber
     covers it), no early/late-timing change that a diary check could
     observe (update before vs after skills.addXp applies the level).
   - Per-edit occurrence-count assertions; one commit for the subscriber +
     migrated sites, or split by skill if large — state the split in the
     mini-plan.
3. Non-XP diary triggers stay accessor-routed — untouched.

GATES: clean compile; accessor-call token count drops by EXACTLY the number
of migrated sites (record before/after per file); no plugin-scanner run
unless an annotated method/class was added (WorldHooks registration may not
be scanner-relevant — verify how existing subscribers register);
persistenceKey/@Deprecated unchanged; commit message
`refactor(diaries): XP-driven updates -> ExperienceGained subscriber (G2)`.

QUALITY BAR — a diary that completes when it shouldn't, fails to complete
when it should, or completes at a different moment is a gameplay change =
moving backwards. When in doubt a site stays direct. END with: the
classification table, what migrated vs stayed and why, play-test steps for
Jesse (progress each migrated diary objective the normal way; verify
threshold diaries tick on level-up incl. via lamp; verify an untouched
action diary still works), updated baselines, and a committed
docs/player-field-extraction/HANDOVER_after_G2.md.
