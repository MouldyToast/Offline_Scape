# FINDINGS — Turn 2: Type mapping + server-side definition pipeline

Feeds `PLAN_or2_definition_swap.md` §2 and §3. Follows FINDINGS_turn1.

## A. Correction to Turn 1

Opcode 122/123 scale was understated. Base cache ground truth (dump keys):
**217 NPCs carry op122** (`follower` in dump / `lowPriorityFollowerOps` in OR2) and
**202 carry op123** (`lowpriorityops` in dump / `isFollower` in OR2); 202 carry both,
15 carry only op122 (those 15 were the visible comparator mismatches).
`mgi.types` decodes **neither** opcode — at rev-228 the cache never emits legacy op111,
so `NPCDefinitions.isFollower` is **always false** for all 217 vanilla follower/pet NPCs.
Packed cache shows 217/240 (NR TOML adds ~38 more via its own packer path).
ACTION for plan: find what content reads `NPCDefinitions.isFollower` (pet render
priority / left-click walk behavior) — it is currently dead at rev-228.

## B. LIVE BUG FOUND: TypeParser strips item subops (dueling rings broken)

- Rev-228 item configs carry sub-options via **opcode 43** (e.g. Amulet of glory "Rub" →
  Edgeville/Karamja/…; 106 items in the base cache). Verified by raw byte dump of item
  1706: op43 sits after op249 params, last before terminator.
- `ItemDefinitionsDecoding.kt` **decodes** op43 correctly (no corruption — my initial
  corruption hypothesis was wrong; retracted).
- `ItemDefinitions.encode()` **never writes op43** (fixed-opcode writes jump 42→94).
  It also never writes **op3 (examine)**; ground/inventory ops and stack chains use
  computed opcodes (30+i / 35+i / 100+i) and are fine.
- Consequence: any item TypeParser re-encodes loses its subops + cache examine.
  **Proven casualty:** `cache/assets/types/item/dueling_rings.toml`
  (`inherit=[2552,2554,2556,2558,2560,2562,2564,2566]`) → all 8 Rings of dueling lose
  their right-click teleport submenu in the packed cache (base 106 subop items → packed 98;
  diff = exactly those 8 ids). Examine loss is masked because NR serves examines
  server-side from `data/examines`.
- Fix options: (a) 10-line patch adding op43 + op3 to `encode()`; (b) structural fix via
  the OR2 swap (OR2's ItemCodec writes both). Recommend (a) now — it's a user-visible
  vanilla regression independent of the migration timeline.

## C. Complete `mgi.types` → OR2 / OpenRune mapping

Import counts re-verified on current HEAD (explicit imports; handoff's numbers were
stale/pre-cleanup): ItemDefinitions 163, NPCDefinitions 46, EnumDefinitions 42,
ObjectDefinitions 39, ComponentDefinitions 30, AnimationDefinitions 19,
StructDefinitions 15, SpotAnimationDefinition 7.

| mgi.types class | OR2 client type | OpenRune server type | Notes |
|---|---|---|---|
| `config.items.ItemDefinitions` | `ItemType` | `ItemServerType` | Client portion verified field-equivalent by Turn-1 diff. See §D for server fields. |
| `config.npcs.NPCDefinitions` | `NpcType` | `NpcServerType` | NpcType includes combat stats/height/footprint (cache fields at 228). mgi lacks ops 61,62,122,123,126,130,145–152; of these only 122/123 (217/202 defs) and height (14 defs) occur at rev-228 — rest are other-revision/absent (presence-probed). |
| `config.ObjectDefinitions` | `ObjectType` | `ObjectServerType` | mgi's missing ops (91,93,95,96,100–102…) occur **zero** times at rev-228 (probed). |
| `config.enums.EnumDefinitions` (+`Enums` typed wrappers) | `EnumType` | — (or-cache `EnumManager`/aconverted helpers) | EnumType has `getInt/getString`; Offline_Scape's typed `IntEnum/StringEnum` wrappers need a thin equivalent or direct use. |
| `config.StructDefinitions` | `StructType` | — | Opcode sets identical. Pure swap. |
| `config.AnimationDefinitions` | `SequenceType` | `SequenceServerType` | mgi has legacy ops 13–15 (other-rev); OR2 adds 18. Verify seq usage sites (priority/speed fields). |
| `config.SpotAnimationDefinition` | `SpotAnimType` | — | Straight swap (arrow-switch parse artifact in my first diff; sets fine). |
| `component.ComponentDefinitions` | `definition.type.widget.ComponentType` | — | OR2 has full widget type + `PackIfType` packer. mgi's `component.custom/*` (13 NR interface builders) are NR content, not decoder — they migrate/die with their features. |
| `config.VarbitDefinitions` | `VarBitType` | `VarnBitType/VarConBitType…` extras | Verified equal in Turn 1. |
| `config.ParamDefinitions` | `ParamType` | `ParamMap` (named access) | OpenRune resolves params by RSCM name (`param("attack_stab")`). |
| `config.InventoryDefinitions` | `InventoryType` | `InventoryServerType` | |
| `config.HitbarDefinitions` | `HealthBarType` | `HealthBarServerType` | Counts verified (79). |
| `config.DBRow/DBTableDefinition` | `DBRowType/DBTableType` | dbcol codecs in or-cache | Counts verified (4318/87). |
| `config.identitykit.*` | `IdentityKitType` | — | |
| `config.OverlayDefinitions/UnderlayDefinitions` | `OverlayType/UnderlayType` | — | |
| `worldmap.*` | `WorldMapAreaType` + or-cache map packing | — | TypeParser's worldmap repack is where it OOM'd; OpenRune has `MapPackers`. Turn-3 topic. |
| `clientscript.ClientScriptDefinitions` | (cs2 via `PackCs2`) | — | Turn-3 (builder pipeline). |
| `draw.*`, `skeleton.*` | n/a (client render code) | — | Only used by NR tooling/packers; migrates with builder pipeline, not runtime. |

Runtime access analogy (mechanical rewrite target):
`ItemDefinitions.get(id)` → `ServerCacheManager.getItem(id)` (or client-layer
`CacheManager` for pure cache data); `Npc` entity holds `type: NpcServerType` directly
(OpenRune) vs Offline_Scape's id-based lookups.

## D. Server-side field analysis (handoff §3)

**OpenRune pipeline (traced end-to-end):**
1. Source TOML: `.data/raw-cache/server/{npcs,items,…}.toml` + `shops/`, `slayer/`,
   examine CSVs, plus per-plugin `content/*/pack/src/main/resources/pack/configs/*.toml`
   (merged via `PluginPacks.configDirectories()`). Entries use RSCM ids:
   `id = "npc.imp"`, `inherit = "npc.imp"`, then server fields (`giveChase = false`,
   `wanderRange = 0`) and named params (`"param.shop_sell_percentage" = 1000`).
2. Pack (`PackServerConfig`, revision-parameterised): seeds each type **from the client
   cache decode** (`OsrsCacheProvider.NPCDecoder(rev)` as `loadBaseInto`), overlays TOML,
   then writes a **second binary cache** (`.data/cache/SERVER`) via opcode codecs
   (`NpcServerCodec` etc. in `or-cache/codec/osrs/impl/`); `MinifyServerCache` finalizes.
3. Runtime: server decodes the SERVER cache into `ServerCacheManager` maps
   (`getNpc/getItem/getNpcOrDefault…`). **Answer to the handoff question: it is a
   separate, standalone enriched object (`NpcServerType`), not the OR2 `NpcType`
   enriched in place.** ServerTypes duplicate the client fields they need.

**Key rev-228 discovery — equipment stats live in cache params:**
Item params 0–11 are attack/defence bonuses, 10/11 str/prayer, 14 attack speed
(verified: glory(1) +10 att all/+3 def all/+6 str/+3 prayer; whip slash 82/str 82/
speed 4 — all match wiki values from raw cache params). OpenRune's combat formulas read
these via named params (`BaseParams.attack_stab`…) and weapon behavior via
`ItemServerType.weaponCategory`. **Offline_Scape's osrsbox `bonuses[]` JSON overlay is
redundant at rev-228** — and a stale-data risk where JSON disagrees with cache.

**Offline_Scape's current server-side sources → OpenRune destination:**

| Offline_Scape source | Loaded by | Fields | OpenRune destination |
|---|---|---|---|
| `data/items/ItemDefinitions.json` (osrsbox-derived) | `JSONItemDefinitionsLoader` → mutates `ItemDefinitions` in place | weight, slot, examine, equipmentType, bonuses[], twoHanded, attack anims/speeds/distances, blockAnimation, render anims | bonuses/speed → **drop (cache params)**; slot → **drop (cache wearpos1-3, already decoded by mgi too)**; weight → drop (cache); examine → examine CSV/TOML; equipmentType/twoHanded → derivable from wearpos2 (=5 means 2h) / weaponCategory; attack+render anims → `WeaponCategory` tables / content config |
| `cache/data/npcs/combat/*.npc.json` (per-NPC) | `NPCCombatDefinitionsLoader` → separate `NPCCombatDefinitions` object | hitpoints, attackSpeed, slayerLevel, attackDistance, aggressionDistance, maximumDistance, targetType, combat/aggressive/defensive stat arrays, attackDefinitions (type, maxHit, anim, gfx, projectile) | hitpoints/att/def/str/ranged/magic → **cache (NpcType, rev-228)** — JSON copies are dup/stale-risk; attackSpeed→NpcServerType.timer-adjacent, wander/aggro ranges→wanderRange/maxRange/attackRange/huntRange/huntMode, respawn→respawnRate, rest (maxHit/anims/projectiles) → content-level combat config (OpenRune keeps these in content scripts/params, not the server type) |
| `cache/assets/types/npc/*.toml`, `…/item/*.toml` | TypeParser (packs INTO client cache) | client-field overrides + NR customs | Splits: client-visual overrides → OR2 `buildCache` TOML; server-behavior fields → `raw-cache/server` TOML. NR customs remain client-cache packs. |

**Structural difference to design around:** Offline_Scape merges server fields into the
one definition object (`ItemDefinitions` carries `bonuses[]`); OpenRune keeps a clean
client type and a standalone server type. The migration can go either way per module,
but the end state is the two-layer model — and rev-228's param-carried stats mean most
of the "merge" can simply be deleted rather than ported.

## E. Presence-probe summary (what mgi actually drops at rev-228)

| Opcode | Meaning | Defs using it (packed cache) | mgi behavior |
|---|---|---|---|
| npc 122 | lowPriorityFollowerOps | 217 | silently ignored (flag lost) |
| npc 123 | isFollower | 240 | silently ignored (flag lost) |
| npc height op | height | 14 | ignored |
| item 43 | subops | 98 (106 in base) | decoded OK; **lost on re-encode** |
| npc 126/130/145–152, item 15/44–54/160, obj 91–102 | various | **0** | moot at rev-228 |

So mgi.types is *decode-complete* for rev-228 content except follower flags and npc
height — small but real, and invisible because unknown-opcode throw is commented out
(`NPCDefinitions.java:383`).

## F. Local uncommitted edits made during investigation

- `cache/build.gradle.kts`: `maxHeapSize = "3g"` on generateCache; added `probeItem` task.
- `cache/src/main/java/ProbeItem.java`: throwaway probe (delete or keep as diagnostic).
Decide: commit the heap fix + delete probe, or leave for the execution session.
