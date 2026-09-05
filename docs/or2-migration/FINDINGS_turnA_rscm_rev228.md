# FINDINGS — Turn A: RSCM/gameval symbol tables for rev-228

Feeds `PLAN_or2_definition_swap.md` §4. Follows FINDINGS_turn1 and FINDINGS_turn2.
All claims below verified against: OpenRS2 cache #2043 on disk, osrs-dumps @ `f54d6da7`
(rev-228) and @ `95304496` (master, 2026-09-02-rev240), OpenRune-Server @ HEAD,
OpenRune-FileStore @ HEAD.

## A. Verdict up front

**OpenRune's own gameval dump path is impossible for rev-228, and the handoff's
suggested straight symbols→.rscm conversion is impossible too.** The chosen mechanism
is a **backport converter**: take Jagex's real gameval names from a post-gameval-era
osrs-dumps commit (master, rev-240 — OSRS IDs are never renumbered), restrict them to
rev-228's ID universe, guard against the small set of genuinely repurposed IDs with
mechanical safety diffs, and fall back to Joshua-style placeholders (`npc_124`)
elsewhere. **The converter is built and run** (`make_rscm_228.py`, attached); it
produced the full artifact set with verified round-trip against OpenRune's binary
format. Coverage: 99%+ real names for npc/obj/loc/seq/spotanim/inv/component, ~58%/47%
for varbit/varp (Jagex leaves older ones unnamed), placeholders for enum/struct/param
(no real names exist anywhere — Jagex gamevals do not cover these types).

## B. Why the two handoff-suggested paths are dead

1. **`freshCache`/GamevalDumper against the rev-228 cache cannot work.** The dumper
   chain is `GamevalDumper.dumpGamevals` → `GameValHandler.readGameVal(group, cache,
   rev)` → `cache.files(GAMEVALS, type.id)` where `GAMEVALS = 24`
   (`OpenRune-FileStore/filestore/.../ArchiveIndex.kt:27`). **The rev-228 cache has no
   index 24** — extracted OpenRS2 #2043 contains exactly
   `main_file_cache.idx{0-15,17-21,255}`. Jagex added the gamevals archive to the
   client cache after rev-228 (consistent with `GameValGroupTypes` marking
   `IFTYPES_V2`/`VARCS` as revision-232 additions and with the dump history below).

2. **The f54d6da7 `symbols/` tables cannot be converted as-is because they contain no
   real names.** The handoff (§Turn A item 2) cited `graphic.sym: 15→magicon,0` as
   evidence of named tables — but `graphic.sym` is **sprites** (client "Graphic" =
   sprite; spot animations are `config/dump.spot`), one of the few hand-curated tables.
   The tables that matter are pure placeholders at f54d6da7:
   npc.sym 14,162/14,162 placeholder, obj.sym 30,646/30,646, loc/varbit/varp/seq/
   struct/enum/param/component/interface likewise (only varp uses `varplayer_N`
   naming, graphic/stat/clientscript/midi/fontmetrics carry real curated names). Real
   names for the config tables simply did not exist publicly in Feb 2025. At osrs-dumps
   master (rev-240, gameval era) the same tables are 100% named for
   npc/obj/loc/seq/inv/dbrow/dbtable and partially for varbit (13,245/19,116) and varp
   (2,935/5,732).

## C. Why backporting rev-240 names to rev-228 IDs is sound

OSRS config IDs are append-only; Jagex does not renumber existing content. Verified,
not assumed — mechanical cross-revision diffs on the dumps:

- **npc/obj/loc:** compared per-ID display names (`name=`) between f54d6da7 and
  master, normalized (lowercase, strip `<col>` tags, alnum only) so pure renames and
  recapitalizations don't count. True mismatches — genuinely repurposed or
  significantly renamed IDs: **npc 21 / 30,646-scale obj 60 / loc 462** (examples:
  npc 124 `Abyssal demon`→`Yama`, obj 1459 `Null`→`Auto-weed`, loc 2884
  `Ladder`→`Carrot scraps`). These get placeholder fallback.
- **varbit:** compared structural definitions (basevar,startbit,endbit) between
  revisions: **650 of 15,988 common varbits changed** (Jagex does repack bit layouts).
  550 of those had real 240 names and were forced to placeholder fallback.
- Spot checks: npc 0 rev-228 "Tool Leprechaun" ↔ gameval `farming_tools_leprechaun`;
  npc 1 "Molanisk" ↔ `molanisk`; obj 4151 ↔ `abyssal_whip`. ✓

Residual risk is confined to tables with no diffable content signal (seq, inv,
interface, component): a repurposed ID there would silently carry a wrong name.
Sequence/inv repurposing is historically rare; **component is the real caveat** —
interface layouts churn, and a component index that exists at both revisions can mean
different widgets. Treat component names as best-effort; verify against the f54d6da7
`interface/` dumps when content starts referencing them.

## D. The OpenRune gameval architecture (traced, for the plan)

Resolution at runtime is a **string→int map lookup, no codegen**:
`RSCM.getRSCM("npc.molanisk")` (or-cache `rscm/RSCM.kt`) → `ConstantProvider.getMapping`
with a per-string HashMap cache. The `startGeneration`/`startEnumGeneration` calls in
`CacheTools.kt` are typed dbtable/enum accessor codegen, not name-table generation.

`GameValProvider` (or-cache) loads five layers in order
(`GameValProvider.sourceFiles`):
1. `.data/gamevals-binary/gamevals.dat` — binary (GameValDat format), the **vanilla
   base layer**, dumped from cache index 24 on gameval-era caches. Sets
   `maxBaseID[table]`.
2. `.data/gamevals-binary/gamevals_generated.dat` — generated layer (component, dbcol).
3. `content/**/gamevals.toml` — per-plugin custom names.
4. `api/**/gamevals.toml`.
5. `.data/gamevals/*.rscm` — text `name=id` custom layer.

**Guard that shapes our output split:** layers 3–5 enforce
`require(value > maxBaseID[table])` — customs cannot name IDs at or below the vanilla
max of any table present in gamevals.dat. Tables *absent* from the dat have
`maxBaseID = -1`, which is exactly how upstream OpenRune hand-names vanilla enums in
`.data/gamevals/enum.rscm` (`settings_search_categories=422`). Therefore:
- **Jagex-group tables** (obj npc inv varp varbit loc seq spotanim dbrow dbtable
  jingle interface) → into `gamevals.dat`, every vanilla ID named (real or
  placeholder). Renames of vanilla IDs happen by regenerating the dat, not via .rscm.
- **Non-Jagex tables** (enum, struct, param, stat, midi, synth, category) → **.rscm
  text only**, so they stay hand-editable per OpenRune convention.

The published OR2 artifact (`dev.or2:all:2.4.17`) already contains the consumer for
the text layer: `ConstantProvider.load(dir)` with `RSCMProvider` (`.rscm`,
`name=id` / v1 `name:id`) and `SymProvider` (`.sym`, `id\tname[\ttype]`). So
Offline_Scape can resolve names **today** with only the OR2 dependency —
`GameValProvider`/`RSCM.kt` (or-cache, source-only) matter only when that module is
ported. `RSCM.kt` itself is ~110 lines and trivially copyable earlier if the
`"npc.x".asRSCM()` idiom is wanted before then.

## E. The converter (built, run, verified)

`make_rscm_228.py` (attached; re-runnable, ~5s). Inputs: symbols + config dumps at
f54d6da7 and master, `/tmp/dump240.{npc,obj,loc,varbit,spot,seq}` fetched from
osrs-dumps master. Per-table rule: rev-228 ID universe → rev-240 name if present,
non-placeholder, safety-diff clean, and unique in table → else curated rev-228 name
(midi/stat/graphic style) → else placeholder `prefix_id`. Component/dbcol values are
packed `(parent<<16)|child` matching `GamevalDumper.dumpComponents`/`dumpCols`.

Outputs (verified byte-level round-trip against a reimplementation of
`GameValDat.read`):

| artifact | contents |
|---|---|
| `out/gamevals/{21 tables}.rscm` | full text layer, `name=id` |
| `out/gamevals-binary/gamevals.dat` | 12 Jagex-group tables, 4.4 MB |
| `out/gamevals-binary/gamevals_generated.dat` | component + dbcol, 0.9 MB |
| `out/REPORT.tsv` | per-table counts |

Coverage (total / real names / fallbacks):

| table | total | real | fallback (no 240 name / unsafe / dup) |
|---|---|---|---|
| npc | 14,162 | 14,141 | 0 / 21 / 0 |
| obj | 30,646 | 30,504 | 82 / 60 / 0 |
| loc | 56,078 | 55,616 | 0 / 462 / 0 |
| seq | 12,025 | 12,019 | 6 / 0 / 0 |
| spotanim | 3,230 | 3,230 | — |
| inv | 920 | 920 | — |
| varbit | 17,262 | 9,942 | 6,770 / 550 / 0 |
| varp | 4,661 | 2,171 | 2,490 / 0 / 0 |
| dbrow / dbtable / dbcol | 4,318 / 87 / 381 | 4,299 / 86 / 367 | small |
| interface / component | 899 / 25,226 | 898 / 25,021 | 1 / 205 |
| midi / stat | 817 / 24 | 817 / 24 | via curated-228 tier |
| enum / struct / param / jingle / synth / category | 5,716 / 5,828 / 2,249 / 307 / 10,195 / 2,037 | 0 | all placeholder — no real names exist anywhere |

Spot checks in output: `abyssal_whip=4151` ✓; repurposed npc 124 correctly emitted as
`npc_124=124` (not Yama's name) ✓.

## F. Conventions and caveats for the plan

1. **NR custom IDs.** Names for NR-packed customs go in a separate
   `.data/gamevals/{table}.nr.rscm`-style custom layer with an `nr_` name prefix —
   **provided their IDs exceed the vanilla per-table max** (GameValProvider guard).
   Any NR custom that *overwrites* a vanilla ID keeps the vanilla name from the base
   layer unless renamed at converter level. **Open verification task for the execution
   session:** determine actual NR custom ID ranges from TypeParser/TOML inputs
   (turn-2 noted NR TOML adds ~38 follower NPCs; whether into gaps or above-max is
   unverified). Until then no NR names are emitted.
2. **Placeholder names are load-bearing.** Every vanilla ID resolves to *something*
   (`varbit.varplayerbit_281=281`), so content can adopt RSCM references mechanically
   and upgrade to real names later without signature churn. Placeholders intentionally
   match Joshua's dump style — grep parity with osrs-dumps.
3. **Unnamed varbit/varp majority.** 6,770 varbits / 2,490 varps have no Jagex name
   even at rev-240 (older content). Naming them is a converter-level override (add an
   overrides TSV input) — not a .rscm edit for dat-resident tables, per the guard.
4. **Names with spaces** exist in curated tables (midi `scape main=0`). Parses fine in
   both providers (split on `=`), but treat midi/jingle/synth as display tables, not
   code-reference tables.
5. **Component reliability** is best-effort (§C). Offline_Scape content currently uses
   numeric component IDs; adopt component names opportunistically, verifying against
   f54d6da7 `interface/` dumps.
6. **Cache-commit correction carried forward:** everything here is pinned to
   `f54d6da7` for rev-228 and osrs-dumps master `95304496` for the name source. The
   master pin should be recorded in the plan; regenerating from a later master is safe
   (append-only) but re-run the safety diffs.
7. **When Offline_Scape eventually upgrades revision past the gameval introduction,**
   this whole mechanism retires: `freshInstall`'s `GamevalDumper.dumpGamevals` takes
   over, and only layers 3–5 (custom names) carry forward — another reason to keep NR
   names in the custom layer, never in the generated base.

## G. Immediate integration surface (for §6 of the plan)

Nothing in Offline_Scape consumes these yet. First consumers, in migration order:
- Tier-1 definition swap (Struct/Enum) does **not** need RSCM (numeric IDs stay).
- RSCM becomes load-bearing when content modules start using OpenRune-style
  `"npc.x"` references or when `PackServerConfig`-style TOML (RSCM ids in configs) is
  adopted. Wiring for that day: add `.data/gamevals/` (text layer) to the repo, call
  `ConstantProvider.load(File(".data/gamevals"))` at boot, port `RSCM.kt` when the
  `.asRSCM()` idiom is wanted. The binary dat layer becomes relevant only with the
  or-cache port.
