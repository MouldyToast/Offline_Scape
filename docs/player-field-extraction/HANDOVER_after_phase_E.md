# HANDOVER — Player Field Extraction, after Phase E (D1–D5 + E1–E6 complete)

**Audience:** the next planning/execution session on this campaign.
**Branch:** `claude/new-session-0kurz2`, HEAD `d5898053`, pushed. Clean tree.
**Master spec:** `PLAN_player_field_extraction_EXECUTABLE.md` (v2 — lives with
Jesse, uploaded per session; this doc supplements it, does not replace it).
**Prime directive, restated:** this campaign moves the codebase TOWARD the
OpenRune-Server format. A phase that changes gameplay behavior, loses player
data, or degrades tooling is moving backwards and is worse than no phase.
**Investigate before you edit. Every count in every plan is an anchor, not
gospel — this session proved the master plan's censuses wrong three times.**

---

## 1. State of the campaign (verified at `d5898053`)

Phases 0, A, B, B.5, C1, C2 landed before this branch (merged via PR #14,
anchor `d9d01594`). This branch adds eleven commits, one field each:

| Phase | Field | persistenceKey | Keys file (all in core Kotlin) | Commit |
|---|---|---|---|---|
| D1 | seedVault | `seed_vault` | `content/skills/farming/seedvault/SeedVaultKeys.kt` | `77eb6d48` |
| D2 | presetManager | `preset_manager` | `content/preset/PresetManagerKeys.kt` | `2e3a0fef` |
| D3 | barrows | `barrows` | `content/minigame/barrows/BarrowsKeys.kt` | `677aeac0` |
| D4 | blastFurnace | `blast_furnace` | `content/minigame/blastfurnace/BlastFurnaceKeys.kt` | `2481b4b6` |
| D5 | dwarfMulticannon | `dwarf_multicannon` | `content/multicannon/DwarfMultiCannonKeys.kt` | `831ce3d2` |
| E1 | gravestone | `gravestone` | `content/gravestones/GravestoneKeys.kt` | `2dcf18f7` |
| E2 | hunter | `hunter` | `content/skills/hunter/HunterKeys.kt` | `d6fe5a1c` |
| E3 | bountyHunter | `bounty_hunter` | `content/bountyhunter/BountyHunterKeys.kt` | `d7ef404e` |
| E4 | grandExchange | `grand_exchange` | `content/grandexchange/GrandExchangeKeys.kt` | `5a3fcdc8` |
| E5 | lootkeySettings | `lootkey_settings` | `content/lootkeys/LootkeySettingsKeys.kt` | `acfbb2ae` |
| E6 | follower | *(none — transient)* | `content/follower/FollowerKeys.kt` | `d5898053` |

- 10 persisted keys + 2 transient (`GARG_INSTANCE_KEY`, `TOA_MANAGER_KEY`
  from Phase C) + follower. `grep -rh 'persistenceKey = "' --include="*.kt"
  . --exclude-dir=build` must return exactly 10 lines; uniqueness is a gate
  that grows by one per future persisted field.
- Player.java `@Deprecated` count: **20** (10 fields + 10 getters). The
  follower field/getter/setter are fully DELETED (transient ⇒ no parser
  role). The lootkeySettings public setter is DELETED (no parser role).
- Every legacy getter is grep-pinned to exactly its declaration + one parser
  read in the owning class's onInit — plus, for gravestone only, the Kotlin
  property read inside `GravestoneKeys.scanGravestone` (documented in the
  getter's javadoc).
- Play-tests: **D1–D5 passed in-game (Jesse). E1–E6 NOT yet play-tested.**
  See §6. Do not build Phase F on the E pattern until Jesse confirms E.

### Metric honesty — the import gates are suspended during the shim window

The master plan's standing verification expects monotonically non-increasing
content imports. That does not hold mid-campaign **by design of the D-series
shim pattern**: each phase keeps the type import (deprecated field) AND adds
a Keys import wherever Player/core internals call the accessor.

- Player.java content imports: 53 (plan start) → 43 (after A–D1) → **48** now.
- Repo-wide core→content imports (excl. `/content/` paths): 996 (plan start)
  → **1021** now.

These reverse at §Rotation (field+getter+type-import deletion) and when the
F-era process events remove Player's accessor calls. Until then, per-phase
expectation is: +1 Keys import per field with Player-internal uses, nothing
else. A surprise increase beyond that is still stop-and-report.

---

## 2. The locked house pattern (as actually evolved through D+E)

The next execution session must match this exactly — consistency is part of
the OpenRune-alignment goal:

1. **Keys file**: `@file:JvmName("XKeys")`, same package as the content
   class, `@JvmField val X_KEY: AttributeKey<X> =
   AttributeKey(persistenceKey = "x_snake")`.
2. **Accessor**: E-series style is a Kotlin **extension fun**
   `fun Player.x(): X` — it compiles to the identical
   `XKeys.x(player)` static for Java while giving Kotlin call sites safe
   implicit-receiver ergonomics (D-series used `fun x(player)`; same JVM
   signature, do not churn them). Body: raw-attr check → if typed, return →
   construct `X(player)` → if raw non-null, `LoginManager.gson.get()`,
   `fromJson(toJsonTree(raw), X::class.java)`, copy via `copyFrom`/existing
   initializer → store in attr → return.
3. **`rawXAttr(player)`**: reads through `AttributeKey<Any>` so the
   pre-rehydration raw Map flows out without CHECKCAST. Never inline a typed
   read of a persisted key before rehydration.
4. **Never store an unparented Gson snapshot** when the class has transient
   back-refs (player/WeakReference/containers). Copy into a
   properly-constructed instance via a `copyFrom` that lives IN the content
   class (private-field access stays in-class). `copyFrom` copies EXACTLY
   what the legacy load path copied — no more (Barrows deliberately copies 6
   of 9 persisted fields; "fixing" that changes behavior). Pure data holders
   (LootkeySettings) may be stored directly.
5. **onInit shape**: `hadPersistedAttr` flag read BEFORE the accessor call →
   accessor (eager conversion) → `if (hadPersistedAttr || savedPlayer ==
   null) return;` → legacy-path comment block →
   `@SuppressWarnings("deprecation")` parser read → null-guard →
   `copyFrom`. Attr wins when both exist.
6. **Player.java**: field becomes a null-initialized `@Deprecated` slot with
   the standard javadoc (non-transient so the PARSER can deserialize old
   saves; live player never populates it ⇒ the legacy JSON key vanishes from
   post-migration saves). Getter kept `@Deprecated`, pinned by grep to the
   parser read(s). Drop `final` where present. Setters with no parser role
   are deleted outright.
7. **setFields whitelist conversions** (GE, lootkeys did this): delete the
   LoginManager line, add a new static `@Subscribe onInit` to the content
   class. `putAllFromPersistence` runs before `InitializationEvent`
   (LoginManager.setFields), so the ordering is safe.
8. **Gates per phase, all must pass before commit**:
   `./gradlew clean compileJava compileKotlin`; legacy-getter grep pinned;
   accessor-adoption occurrence count; `new X(this)` count 0 in Player;
   persistenceKey uniqueness count; `@Deprecated` count; `copyFrom` count 2.
   Counts are **occurrences** (`grep -o | wc -l`), never lines — multiple
   calls per line are common (D4 lesson).
9. **Plugin scanner**: body-only `@Subscribe` changes need nothing. Any
   added/removed/renamed annotated method or class (including top-level
   Kotlin `@Subscribe fun`) ⇒ `./gradlew :app:runPluginScanner`.
   `data/plugins.dat` is **gitignored** — regenerate, never commit.
10. **One commit per field**, `refactor(player): <field> -> persisted
    AttributeMap key (Phase Xn)` message shape as in the log.
11. **Numbers through the raw path**: `putAllFromPersistence` narrows
    whole-number Longs that fit Int → Int (the single sanctioned OpenRune
    divergence). Longs like epoch millis exceed Int range and survive; JSON
    numbers deserialize by DECLARED field type on the typed pass, so long
    fields are safe either way. Doubles with whole values would narrow —
    check any F payload carrying doubles (the master plan's §D2/D-series
    risk notes).

---

## 3. Census methodology — the three lessons this session paid for

The master plan's ref counts were Java-getter greps. **Reality is bigger.**
For EVERY future field, run all of these before writing the mini-plan:

1. **Java getter/setter occurrences**: `grep -rn "getX()\|setX(" --include
   "*.java" . --exclude-dir=build` — then count occurrences per file with
   `grep -o`, and watch for multiple receivers (`player.`, `target.`,
   `owner.`, `p.`, `((Player) source).` — barrows/bountyHunter had them).
2. **Kotlin property syntax**: `grep -rn "\.x\b" --include="*.kt" .
   --exclude-dir=build` — Kotlin calls Java getters as properties and
   SETTERS AS ASSIGNMENTS (`player.follower = Follower(...)`). Gravestone
   had ~23 such sites (GravestoneExt + the death-interfaces content module),
   follower had 3 files (araxxor Reward.kt, tormented_demon.kt,
   Arancini.kt). Beware string literals containing the field name — use
   targeted swaps or receiver-aware regexes with per-file count assertions,
   never a blind substring pass.
3. **Parser-player consumers** — the killer class of bug: code that
   deserializes saves OUTSIDE the login path reads legacy fields off
   Unsafe-allocated players whose transient `attr` is NULL and whose legacy
   field is ABSENT in post-migration saves. Known consumers:
   - `core/.../com/near_reality/tools/WealthScanner.kt` (+ `EcoSearch.kt`)
     via `LoginManager.deserializePlayerFromFile` — fixed for gravestone
     with `GravestoneKeys.scanGravestone(parser)` (legacy field → raw
     `attrPersistence[key]` → null). **Any F field these tools touch needs
     the same scan-helper treatment.** Check them for
     farming/slayer/construction refs before planning F.
   - `tools/discord/.../DiscordEcoSearch.kt` — NOTE: `tools/discord` has no
     `build.gradle.kts` and is NOT a Gradle module (dead code; `./gradlew
     projects` lists only tools:analyzer/backups/updater). Fixed anyway for
     consistency; do not rely on compilation to catch breakage there.
   - `api/src/main/kotlin/com/near_reality/api/model/PlayerModel.kt` — maps
     legacy top-level JSON keys (seedVault, gravestone containers). Inert:
     nothing in-repo consumes `:api`. Leave it; log it. If `:api` is ever
     revived it must re-point at attrPersistence.
4. **The migrations module**: `content/other/migrations/.../impl/M*.kt` —
   `GameMigration.run(player)` mutates LIVE players (M007 assigned
   `player.lootkeySettings = null` and broke the build when the setter was
   deleted). Grep it for every field before editing.
5. **GameCommands.java** — dev commands touch most systems; it appeared in
   barrows, GE, lootkeys, WoF.
6. **In-Player internal uses** — per-tick `x.process()` calls
   (gravestone:1903-region, hunter), login-sequence calls
   (`grandExchange.updateOffers()`), onLobbyClose (follower), logout blocks.
   `grep -n "\bx\b" Player.java` — the FIELD name, not just the getter.

---

## 4. Open rulings for Jesse (do not proceed past these silently)

1. **E6 follower — event-subscriber move NOT done (deviation).** The master
   plan prescribed moving pet spawn behind `PlayerLoginEvent` and finish
   behind `PlayerLogoutEvent`. At HEAD the spawn lives in
   `Player.onLobbyClose()` (fires on the lobby "Play" click; callers:
   InterfaceHandler:145, LobbyInterface:70), which `PlayerLoginEvent` (published
   in the login sequence, Player ~4496) does NOT cover — moving it would
   change when pets appear. Kept behavior identical: blocks stay in Player,
   accessor-routed; field/getter/setter deleted. **If Jesse rules the timing
   acceptable, the event move is a small follow-up; otherwise consider a
   dedicated lobby-close event.** Also preserved verbatim: `setFollower`'s
   legacy early-return branch that clears an existing follower WITHOUT
   updating varp 447 (looks like a latent quirk; do not "fix" silently).
2. **Rotation timing** (§Rotation of the master plan) is Jesse's call, per
   field or batched. Gate before deleting any legacy slot:
   `grep -L '"x_snake"' data/characters/*.json` → empty (or accept the
   loss). Rotation deletes: the `@Deprecated` field + getter, the
   `@SuppressWarnings("deprecation")` fallback branch in onInit, the type
   import where unused — and must UPDATE `scanGravestone`-style helpers
   (their legacy-field branch dies too).
3. **Import-count gates suspended** (see §1). Reinstate the monotone
   expectation after rotation.

---

## 5. Next work: Phase F — DO NOT EXECUTE WITHOUT A MINI-PLAN

The master plan mandates a per-field mini-plan written by a planning session
before each F execution ("Jesse's planning/execution split"). Order:
**farming (38) → construction (158) → slayer (178) → prayerManager (252) →
achievementDiaries (399)** — counts are stale Java-only anchors; re-census
per §3 first.

Per-field investigation targets the planner must resolve (from the master
plan + this session's experience):

- **farming**: setFields does WHOLESALE `player.setFarming(parser.getFarming())`
  — different from anything D/E handled (a setter-replace, not a copy-into).
  The Gson builder registers a custom `Farming.deserializer()` for
  `FarmingSpot`; the raw path uses the same `LoginManager.gson` instance so
  the adapter applies on the typed pass — but VERIFY the adapter tolerates
  the map→tree→typed round-trip (adapter-written shapes must re-read from
  their own output). Find the per-tick driver.
- **construction**: Phase 0's `onInit` + `setFields(parser)` already own the
  load path — conversion is mechanical, but 158 refs; census the Kotlin side.
  Construction also appears in Player's logout path (`construction.getTipJar()
  .onLogout()` at ~2311) — an internal use the plan didn't list.
- **slayer**: setFields `initialize(player, parser)`; engine checks in
  combat/drops. Do not invent varbits rev-228 lacks.
- **prayerManager**: 252 refs, hot combat path. Varbit reads via
  `getBitValue` (not `getBit`). Drain logic needs the F-era
  `PlayerProcessEvent` publish in WorldThread (add in the first F sub-phase
  that needs it, next to `hooks.post(new PlayerEvent.Process(player))`).
- **achievementDiaries**: 399 refs; XP-gained subscriber replaces the
  update() blast — verify the exact `PlayerEvent.ExperienceGained` type in
  WorldHooks.kt before building on it.

Also still out of scope (untouched, per master plan §1): godBooks,
retrievalService, privateStorage, gauntletItemStorage, petInsurance, stash,
duel, puzzleBox/lightBox, respawnPoint, and the non-field content usages.
`petId` deliberately REMAINS a plain int on Player with its setFields line —
that is correct, not an oversight.

---

## 6. Pending play-tests for Phase E (Jesse, before F executes)

1. **gravestone**: die with items → gravestone spawns, timer varbit runs;
   log out/in near it (LoginEvent re-spawn via varbit 10465); reclaim at
   grave and at Death's Office; coffer deposit/withdraw (sacrifice + retrieval
   interfaces — these went through the Kotlin retarget); JSON shows
   `attrPersistence.gravestone` (location/container/coinsInCoffer), no
   top-level `"gravestone"`. Also run WealthScanner/EcoSearch once over a
   migrated save — must not NPE and must still count gravestone items.
2. **hunter**: birdhouses persist + varbits refresh at login; traps lay and
   dismantle at logout (transient); relog round-trip.
3. **bountyHunter**: acquire/skip targets, relog, skip-list persists
   (`bounty_hunter` key); points varp untouched.
4. **grandExchange**: history shows after relog (`grand_exchange` key);
   offers unaffected; history interface + collection box.
5. **lootkeySettings**: enable via Skully, relog (settings persist under
   `lootkey_settings`), chest claim, disable; fresh character stays null;
   M007 migration still disables where applicable.
6. **follower**: pet out → relog → pet returns at lobby close; pick up/drop;
   metamorphosis (MuphinD, rock golem — these maintain petId); dismissal at
   PetNPCPlugin; death drops pet (DeathMechanics setFollower(null)); duel
   stakes; varp 447 behavior unchanged.
7. In every case: inspect `data/characters/<name>.json` before/after — the
   legacy top-level key must be GONE after the first post-migration save and
   the attr key present.

Any mismatch: JSON snippet + stack trace before anything else is built.

---

## 7. Session bootstrap checklist for the next session

```bash
git log --oneline -1        # expect d5898053 (or dry-verify everything if moved)
git status --short          # expect clean
./gradlew clean compileJava compileKotlin   # expect BUILD SUCCESSFUL
grep -rh 'persistenceKey = "' --include="*.kt" . --exclude-dir=build | wc -l  # 10
grep -c "@Deprecated" core/src/main/java/com/zenyte/game/world/entity/player/Player.java  # 20
grep -c 'import com.zenyte.game.content' core/src/main/java/com/zenyte/game/world/entity/player/Player.java  # 48
```

Then: (a) if E play-tests have not been confirmed, stop and ask; (b) if
planning F, run the §3 census for the target field and write the mini-plan
with byte-verified FINDs and occurrence counts at the current HEAD; (c) if
executing a mini-plan, verify its anchor commit first. Zero or multiple
matches on any FIND ⇒ stop and report. The bar is D1's: every edit
pre-verified, every gate numeric, behavior byte-identical.
