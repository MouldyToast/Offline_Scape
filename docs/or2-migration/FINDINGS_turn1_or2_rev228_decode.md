# FINDINGS — Turn 1: OR2 decodes rev-228 (VERIFIED)

Investigation date: 2026-09-05. Feeds `PLAN_or2_definition_swap.md` §1.
Environment state: all repos + harness live under `/home/claude/` (see "Reproduction" below).

## Verdict

**OR2 (`dev.or2:all:2.4.17`) decodes the rev-228 cache correctly. Verified empirically, twice:**

1. **Stock base cache** (OpenRS2 archive #2043, exactly what `:cache:setupCache` downloads):
   all 14 config archive types decode with zero errors. Field-level diff against the
   matching osrs-dumps snapshot shows **zero decode errors** across all 14,162 NPCs
   and 30,646 items — every mapped field byte-equivalent (details below).
2. **NR-packed production cache** (after `:cache:generateCache` / TypeParser):
   decodes cleanly with NR customs included — 14,272 NPCs (+110), 31,395 items (+749),
   56,083 locs (+5), 5,807 enums (+91), 6,348 structs (+520), 12,098 seqs (+73),
   20,029 varbits, 20,000 varps (NR pads varps to 20k — matches RSProx `varp_count: 20000`),
   4,860 dbrows (+542), 3,254 spotanims, 2,311 params. Vanilla definitions unchanged.

Rev-228 falls inside a clean codec branch window: `revisionIsOrAfter(220)` true,
`revisionIsOrBefore(232)` true. The empirical diff confirms every branch taken is correct.

## Decode counts — base cache vs osrs-dumps @ f54d6da7 (2025-02-12-rev228)

| Type | OR2 | dump | match |
|---|---|---|---|
| npc | 14,162 | 14,162 | ✔ |
| item (obj) | 30,646 | 30,646 | ✔ |
| loc (object) | 56,078 | 56,078 | ✔ |
| enum | 5,716 | 5,716 | ✔ |
| struct | 5,828 | 5,828 | ✔ |
| seq | 12,025 | 12,025 | ✔ |
| varbit | 17,262 | 17,262 | ✔ |
| varp | 4,661 | 4,661 | ✔ |
| spotanim | 3,230 | 3,230 | ✔ |
| dbrow | 4,318 | 4,318 | ✔ |
| dbtable | 87 | 87 | ✔ |
| healthbar | 79 | 79 | ✔ |
| hitsplat | 78 | 78 | ✔ |

Field-level diff (NPCs: name, size, all 8 movement anims, 6 combat stats, vislevel,
category, ops 1–5, multivar/multivarbit, resize, ambient/contrast, flags, models,
heads, headicons+indexes, full param maps; items: name, cost, members, stackability,
wearpos, tradeable, GE flag, category, ground ops, inventory ops, cert/placeholder
links, team, examine, model, zoom, count chains, full param maps):
**zero mismatches** after accounting for dump symbolization (see "Divergences" —
all are representation differences, not decode differences).

## CORRECTION: the project's cache reference commit is wrong

- OpenRS2 **#2043** (what `setupCache` downloads, per `cache/build.gradle.kts:62`) is the
  **2025-02-12** build of rev-228.
- The project's stated reference, osrs-dumps `6abc0d7`, is **2025-01-15** — four weekly
  game updates earlier. Diffing against it produces false mismatches (+15 npcs … +107 locs).
- **The correct reference commit is `f54d6da7` ("2025-02-12-rev228").** Verified: all 13
  comparable type counts match OR2's decode of #2043 exactly.
- ACTION: update PROJECT_MAP / plan docs / memory. Any "verify against osrs-dumps" step
  must pin `f54d6da7`, not `6abc0d7`.

## Integration requirements discovered

1. **Revision must be passed explicitly.** OR2's `readCacheRevision(cache)` reads a
   `version.dat` file from the CLIENTSCRIPT archive — an OpenRune packing convention
   absent from stock/NR caches. Use `OsrsCacheProvider(cache, 228)` (or later, pack
   version.dat and centralize the revision constant — aligns with the revision-config goal).
2. **API shape** (all verified working):
   - `dev.openrune.filesystem.Cache.load(Path)` opens dat2/idx caches directly.
   - `OsrsCacheProvider(cache, 228).init()` populates `npcs/items/objects/enums/structs/
     anims/varbits/varps/healthBars/hitsplats/dbrows/dbtables` maps.
   - `SpotAnimDecoder(rev)`, `ParamDecoder(rev)`, `InventoryDecoder()`, `AreaDecoder()`,
     `ComponentDecoder`-equivalents etc. are separate decoder classes, not in `init()`.
3. **Maven**: repo `https://raw.githubusercontent.com/OpenRune/hosting/master`,
   artifact `dev.or2:all:2.4.17` (umbrella; same coordinates OpenRune-Server uses via
   `libs.versions.toml`). Resolves fine alongside mavenCentral + jitpack.

## Divergences found (all benign for decoding; some matter later)

1. **NPC opcodes 122/123 naming.** OR2: 122→`lowPriorityFollowerOps`, 123→`isFollower`.
   RuneLite-convention dumps: 122→`follower`. Same bit, different label. 15 NPCs in the
   base cache carry opcode 122 (Zanik, pets, etc.).
   **Bigger finding:** `mgi.types` NPCDefinitions **does not handle 122/123 at all** —
   only legacy opcode 111 (`isFollower`), and its unknown-opcode default silently ignores
   (commented-out throw, NPCDefinitions.java:383). Harmless only because 122/123 carry no
   payload; the 15 NPCs currently load with the flag silently dropped on the mgi side.
   Concrete example of mgi.types lagging rev-228's real opcode set — motivation evidence
   for the swap, and a field-mapping row to get right (check what content code reads
   `isFollower` for pet behavior before mapping it).
2. **"Hidden" ops normalized.** OR2's `EntityOpsLoader` drops op strings equal to
   "Hidden" (client semantics; ~400 defs affected). Runtime-equivalent, but a repack via
   OR2 would not be byte-identical for those defs. Matters only for byte-exact cache
   verification, not behavior.
3. **cp1252 not applied to strings.** Item 7972's examine: cache byte 0x99 (cp1252 ™)
   comes through as raw 0x99 instead of U+2122. One def affected in the base cache.
   Cosmetic; note for any NR content with special chars in names/examines.
4. **Dump symbolization** (for anyone re-running diffs): dumps print typed params
   resolved (`stat`→skill name, `boolean`→true/false, `graphic`→"group,index" via
   graphic.sym, `obj` id −1→"null"); ops packed 4-per-line for opcodes 17/115/117;
   headicons as "group_name,index" (headicons_prayer=group 440,
   league_4_bloodthirsty_headicon=group 5529). OR2 stores raw ints for all of these.

## Architectural observation for §3 (server-side fields)

Rev-228's **client cache** already carries fields Offline_Scape treats as server config:
- NPC: attack/defence/strength/magic/ranged/hitpoints (combat-stat opcodes, added ~rev 220)
  plus footprintSize, height — decoded straight from the cache and confirmed against dumps.
- Item: `equipSlot` (wearpos) and `weight` are cache fields.
The `mgi.types` "bolted-on server fields" analysis must therefore three-way split:
(a) genuine cache fields at rev-228 (no server config needed), (b) server config
(bonuses[], attackSpeed, attackDistance, blockAnimation…), (c) NR-packed custom fields.
This shrinks the server-config surface vs the handoff's assumption.

## Environment notes

- `generateCache` OOMs at default JavaExec heap (~1GB); needed `maxHeapSize = "3g"`.
  Added locally to `cache/build.gradle.kts` (UNCOMMITTED local edit — decide whether to
  upstream it; it's a reasonable fix for any machine).
- TypeParser packs **in place**; a failed run leaves the cache partially packed.
  Re-extract from OpenRS2 before retrying (repo keeps no pristine copy).
- XTEA keys: `cache/data/objects/xteas.json` from
  `https://archive.openrs2.org/caches/runescape/2043/keys.json`.

## Reproduction

- Harness: `/home/claude/or2-harness` — `./gradlew run -PmainCls=MainKt
  -PappArgs="<cachedir> 228 out"` (counts + spot checks) or `-PmainCls=DumpTsvKt`
  (full NPC/item TSVs to `out/`).
- Comparator: `python3 /home/claude/compare_defs.py` (expects dumps at f54d6da7 in
  `/home/claude/osrs-dumps`, TSVs in `/home/claude/or2-harness/out`).
- Cache: stream-extract `https://archive.openrs2.org/caches/runescape/2043/disk.zip`
  into `Offline_Scape/cache/data/cache/` (contents of the zip's inner `cache/` dir).
