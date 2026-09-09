# PLAN: Stage 5c — custom item layer removal

**Verified against:** `claude/new-session-lo0m56` @ `1ec6f340` (Stages 1-4, 5a, 5b + CUSTOM_ITEM_IDS manifest committed). Dry-run CLEAN; apply script CHECK-CLEAN (simulated A+B+C in memory with post-simulation sweeps and keep-guards).

This plan is machine-driven: the embedded dry-run computes and freezes every kill set from CUSTOM_ITEM_IDS.txt (169 definitions.toml blocks, 6 whole tomls, 16 store_items blocks, 242 model files + 256 map entries, 173 ItemId + 31 NpcId constants), and the apply script reuses those structures. Design summary, decisions, corrections, per-part edit inventory, gates, save impact, and left-behind ledger are recorded in the session working notes reproduced below; the authoritative kill sets live in the scripts.

**Scope (user-approved):** remove Class C (~350 unobtainable ids: chaotic/nex kites, non-primal origins customs + promo bundles + storebundle system, remnant pets + packPets + DRIFTER, ankou sets, zenyte/FATE, TOA echo, bonds, dragon kite, 4 dead mystery boxes + set, Sumona slice (save-safe: stripSumona + null-master retire to RATS/Turael stays), Primula, barrows-brother clones, easter items, Sir Eldric scroll), Class B dead-verb tier (9 boosters + slayer scrolls + raid orbs + bobbled items, with all distributor rows and 10 read-sites restored to vanilla), stragglers (polypore 32042 from the tourney box, dragon hunter gloves 26255 from slayer shop enums), and comp capes now (7-file engine package, defs, tomls, rebirth dir, interfaces 1617/1618 blobs; CapeCustomizer bridged with local consts for 5d). **Keeps guarded:** primal tier + its 18 live distributor sites, scroll boxes, sherlock, pet mystery box + MiscPet, live boxes, dpins/middleman (5d), death cape, RotS + Lil_* shared models, PET_FRANK, bonfire stanza, rots.toml 16045-50, pvmArena/stew/BH-skip ticks, vanilla sets in CustomItemSet/store_items, holiday anim bases, staff cosmetics 33240-46 (5d).

---

## Session working notes (exploration + decisions record)

# PLAN: Stage 5c — custom item layer

## Context
Stages 1–4 + 5a + 5b executed (`dc9880c7`). 5c = the long tail: build `CUSTOM_ITEM_IDS` manifest (authoritative sources: NearRealityCustomItemPacker's 4 tomls = 665 defs, types/*.toml item blocks, origins packCustoms code-built 32884-32961, in-code edits/makeTradeable), then per-item-group removal: drops → shops → collection logs → constants → packer entries → assets. Save impact heaviest here (banks full of custom items). Accumulated sunset list from prior stages: chaotic crossbow/kiteshields, polypore staff, Sumona slice, barrows-brothers slice, Primula, frank.toml + PET_FRANK, RDI bonfire 29300, item 30031, boosters, 4 NR booster rows never transcribed.

## User decisions (confirmed)
1. **C class: remove all** (~350 unobtainable ids, accepted-ghosts model).
2. **B class dead-verb tier: remove items + all distributor rows** (finishes upstream e3974b64; read-sites restored to vanilla behavior).
3. **Stragglers: remove both** (polypore 32042 from tourney box; dragon hunter gloves 26255 from slayer shop enums).
4. **Comp capes: remove in 5c** (user override of defer-to-5d recommendation — defs, recolour tomls, both ItemPlugins, CompletionistCape system, rebirth master_comp_cape dir).
Design agent running (dryrun_stage5c.py to be validated). CUSTOM_ITEM_IDS.txt to be committed as the pre-5c authoritative record.

## Approach
1. Manifest agent → commit CUSTOM_ITEM_IDS.txt.
2. Distribution-map agent → group classification A(live)/B(dead-functional)/C(unobtainable)/D(keep-set).
3. User decision pass on groups (expect: C-class = kill now; A-class live customs = keep or explicit cull; D = keep).
4. Executable plan slices + execution (possibly multiple commits: 5c-1 orphans/sunset, 5c-2+ per live-group decisions).

## Exploration
### Manifest (agent 1 — complete)
Raw manifest at scratchpad/custom_item_ids.txt (1384 lines: 1071 records + 308 ItemId custom-tail constants). **1057 unique ids: 781 NEW custom defs, 60 VANILLA-ID OVERWRITES, 216 VANILLA EDITS.** Sources: item_config 4 tomls (517+100+26+22), OriginsPacker packCustoms (73 + 10 promo bundles @ 2763-2772), CustomItemPacker in-code (51 vanilla edits incl. ~41 makeTradeable), types/** 42 files (272). ItemReader semantics: id= → new def; no id= → in-place edit of inherit ids. Single-bracket [item] form in 6 files (sled/halos/silverlight/rotten_potato/mystery_box/smouldering_demon). ID-range map produced (2715-2772 store/promos over casket slots, 30000-30500 pets/tournament/Frank, 31300+ packs, 32xxx bulk, 33xxx placeholders, 60017-60501 bonds/zenyte/Frank placeholders in the MODEL-id namespace — hygiene smell).
**Anomalies:** (1) two placeholder clobber collisions: 32801 (master comp cape ph vs grey bunny ears ph — definitions.toml wins) + 32805 (comp cape (t) ph vs chaotic longsword ph); (2) 150 primary defs w/o ItemId constants (all ankou_sets primaries, item packs, bobbleheads, lord marshall…); (3) zero constants w/o defs; (4) 11 custom ids whose constants sit in the "vanilla" auto-gen block — ItemId.kt was regenerated from an already-packed cache (main block NOT safe as vanilla reference); (5) 60 ids collide with modern vanilla (future-revision conflicts: clue/casket block 2678-2813, mixology 30000-30032, zombie helm 30321, raging-echoes 30500…); (6) NearRealityCustomItemPacker.main() dead code-gen.
### Distribution map (agent 2 — complete)
**Reframing finds:** (0a) upstream commit e3974b64 already deleted ALL custom item-action plugins (boosters/scrolls/orbs/bobbled/balmung-death) — no call site sets any booster tick; (0b) runtime defs come from the packed cache; data/items/ItemDefinitions.json is an overlay with a warn-and-skip guard for stale rows (non-fatal).
**Class A (live, KEEP):** primal drop tier 32914-32948 (revenants ×11, gauntlet ×2, Araxxor, Larran's ×2, ChaosChest, ShootingStar + RareDrop + CL enum 10501), 32161 enhanced excalibur (SpecialAttack live), 32612 Balmung (CombatUtilities), 26300/26304 pouches (live plugins), scroll boxes 2803-2813 (LIVE VANILLA CLUES via ClueItem.java — do not touch), sherlock 30210-30214, pet mystery box 30031 + MiscPet 30150-30209 pool, boxes 6199/32080/32164/32165/32231/32212/32423, dpins (middleman currency → 5d), death cape 32058, malevolent energy + RotS drops, **frank 30500 = LIVE Vanstrom pet 1/650 (KEEP — roadmap assumption wrong)**, bonfire 29300 op edit (vanilla-edit!), sled/max_capes tomls.
**Class B (distributed, verb dead):** 10 boosters 32149-32167 (+CL ~25 rows, boxes, BountyCrate, CrystalChest, kept_items, PlayerVariables ticks, ::boosters), slayer picker/reset scrolls 32157/32158, raid orbs 32369-32371, bobbled pets 32186-32191 (recipe deleted; NB npc ids 16045-16050 SHARED with rots.toml RotS brothers — only BossPet rows + item defs die), comp/master comp capes (plugins live, no distributor), dragon hunter gloves 26255 (sold in slayer shop, effect unimplemented), dpin redeem, divine sigil recipe (BossDropItem+SpiritShieldCreation live wiring, no sigil source).
**Class C (unobtainable — clean kills):** chaotic 32802-32816 (+21 AmmunitionDefinitions rows), non-primal origins customs 32884-32913/32954-32961 + promo bundles 2763-2772, remnant pets 32826-32854 + npcs 16095-16123 + packPets remnant blocks, ankou 100+22, zenyte/FATE 2683-2688, TOA echo 22, bonds 30051/30060/60017/60018, dragon kite 32022, dead boxes (cosmetic 32163/easter 32357/3rd-age 32209/enhanced-ult 32206/box-set 32215), Sumona 16064 slice (high blast: ~10 slayer files + _SUMONA suffix contract), Primula 16034, barrows-brother npcs 16052-16057, polypore 32042 (sole source = tournament box), holiday cosmetics, SIR_ELDRICS scroll 32425.
**Class D:** PVP box 32203 + tourney box 32368 + kit (tournament keep-set).
**Bite list:** primal call sites ×18; scroll_boxes live; bonfire toml stanza; rots.toml id-share; CustomItemSet mixes ankou+vanilla sets (delete rows not file); 26300/26304 look vanilla but are custom-packed; stale JSON overlay = warn-noise only.

## Embedded dry-run

```python
#!/usr/bin/env python3
"""Stage 5c dry-run gate. Run from repo root: python3 dryrun_stage5c.py. Exit 0 = CLEAN.

Verifies, against the LIVE tree, every precondition of the Stage 5c plan:
  1. the manifest-derived kill-set resolves to exactly the expected def blocks in each toml;
  2. every byte-exact FIND matches its file exactly once; every regex FIND matches its
     expected count;
  3. the OriginsPacker/RebirthPacker cut regions are anchored, unique, and contain no
     kept tokens;
  4. the model-closure computation: every model .dat scheduled for deletion is owned
     EXCLUSIVELY by dying defs/packer blocks (no surviving toml or packer references it),
     and every deleted .dat has its NearRealityCustomModelMap entry scheduled with it
     (PAIRING RULE) -- and vice versa nothing kept loses its map entry;
  5. ItemId/NpcId custom-tail constants for killed ids exist at their manifest lines and
     have no code references outside the files this plan edits/deletes;
  6. the keep-guards (primal tier, rots brothers, live boxes, recolour cosmetics,
     pvmArena/stew/BH ticks, enum keep rows, CACHE_KEEPSET items) all hold.

The manifest is read from CUSTOM_ITEM_IDS.txt at repo root if present, else from the
session scratchpad copy (pre-commit validation).
"""
import re, sys, pathlib, collections

fail = 0
def err(tag, msg):
    global fail
    print(f"[{tag}] {msg}"); fail += 1

ROOT = pathlib.Path(".")
MANIFEST_CANDIDATES = [
    ROOT/"CUSTOM_ITEM_IDS.txt",
    pathlib.Path("/tmp/claude-0/-home-user-Offline-Scape/ccc702fe-4730-5c35-883e-cec93ceb5547/scratchpad/custom_item_ids.txt"),
]

# ---------------------------------------------------------------- manifest
manifest_path = next((p for p in MANIFEST_CANDIDATES if p.exists()), None)
if manifest_path is None:
    print("[MANIFEST] CUSTOM_ITEM_IDS.txt not found"); sys.exit(1)
records, constants = [], []
for ln in manifest_path.read_text(encoding="utf-8").splitlines():
    if ln.startswith("#") or not ln.strip(): continue
    p = ln.split("\t")
    if len(p) < 5: continue
    if p[1].startswith("CONSTANT"):
        constants.append((int(p[0]), p[3], p[4]))
    else:
        records.append((int(p[0]), p[1], p[2], p[3], p[4]))
by_group = collections.defaultdict(set)
for i, kind, group, name, src in records:
    by_group[group].add(i)

# ---------------------------------------------------------------- kill-set
# definitions.toml blocks (169 blocks / 169 distinct ids; 32805's comp-capes twin dies
# with 'comp capes.toml'):
DEF_KILL = set(
    list(range(32802, 32818))                      # chaotic weapons + nex kiteshields (+placeholders)
    + [32149] + list(range(32151, 32157)) + [32166, 32167]          # boosters
    + [33149] + list(range(33151, 33157)) + [33166, 33167]          # booster placeholders
    + [32157, 32158, 33157, 33158]                 # slayer picker/reset scrolls
    + [32369, 32370, 32371]                        # raid orbs
    + list(range(32186, 32192))                    # barrows bobbleheads
    + [32163, 33163, 32357, 32206, 32207, 32208, 32209, 32210, 32211, 32215]  # dead boxes
    + [32354, 32355, 32356, 32358, 32426, 32427]   # easter event items
    + [32042, 32043, 33042]                        # polypore staff
    + [32022, 32023, 33022]                        # dragon kite
    + [26255, 26256]                               # dragon hunter gloves
    + list(range(32102, 32141, 2)) + list(range(33102, 33141, 2))   # ankou 1-colour + placeholders
    + list(range(32395, 32414, 2)) + list(range(32429, 32439))      # black/purple ankou + placeholders
    + [32615, 32616, 32617, 32818, 32819, 32820]   # comp cape hoods
    + [32425, 32445]                               # Sir Eldric's boost scroll
    + list(range(32826, 32855))                    # origins remnant pet items
    + [32954]                                      # primal components
)
assert len(DEF_KILL) == 169, len(DEF_KILL)
STORE_ANKOU = {2717, 2718, 2720, 2721, 2746, 2748} | set(range(2749, 2759))
WHOLE_FILE_KILLS = {
    "cache/assets/osnr/custom_items/item_config/ankou_sets.toml": 100,
    "cache/assets/osnr/custom_items/item_config/comp capes.toml": 26,
    "cache/assets/osnr/custom_items/item_config/toa.toml": 22,
    "cache/assets/types/item/bonds.toml": 9,
    "cache/assets/types/item/zenyte_armour.toml": 12,
    "cache/assets/types/item/master_cape.toml": 2,
}
PACKER_KILL = set(range(32884, 32914)) | {32955, 32956, 32957, 32958, 32959, 32960, 32961} | set(range(2763, 2773))
PRIMAL_KEEP = set(range(32914, 32950))

# cross-check kill groups against the manifest
def expect_group(group, subset, note=""):
    have = by_group.get(group, set())
    missing = subset - have
    if missing:
        err("MANIFEST", f"{group}: kill ids not in manifest {sorted(missing)[:8]} {note}")
expect_group("origins: lord marshall", set(range(32884, 32894)))
expect_group("origins: cosmetics", {32894, 32895, 32902, 32903, 32904, 32905, 32906, 32907})
expect_group("origins: pernix", set(range(32896, 32902)))
expect_group("origins: elite black", set(range(32908, 32914)))
expect_group("origins: divine sigil/shield", {32955, 32956, 32957})
expect_group("origins: custom item tokens", {32958, 32959, 32960, 32961})
expect_group("promo bundles", set(range(2763, 2773)))
expect_group("origins pet items", set(range(32826, 32855)))
expect_group("barrows bobbleheads", set(range(32186, 32192)))
expect_group("raid orbs", {32369, 32370, 32371})
expect_group("boosters", {32149, 32151, 32152, 32153, 32154, 32155, 32156, 32166, 32167})
expect_group("easter event", {32354, 32355, 32356, 32358, 32426, 32427})
if not PRIMAL_KEEP <= by_group.get("origins: primal", set()):
    err("MANIFEST", "primal keep-set 32914-32949 not fully present in manifest")
if by_group.get("ankou sets (2-colour)", set()) and len(by_group["ankou sets (2-colour)"]) != 100:
    err("MANIFEST", "ankou_sets.toml group != 100 records")

# ---------------------------------------------------------------- toml block machinery
def blocks(path):
    lines = path.read_text(encoding="utf-8").splitlines(keepends=True)
    starts = []
    for i, l in enumerate(lines):
        if l.strip().startswith("[[item]]"):
            j = i
            while j > 0 and lines[j-1].strip().startswith("#"):
                j -= 1
            starts.append((j, i))
    out = []
    for k, (j, i) in enumerate(starts):
        end = starts[k+1][0] if k+1 < len(starts) else len(lines)
        text = "".join(lines[j:end])
        m = re.search(r"^id\s*=\s*(\d+)", text, re.M)
        out.append((j+1, end, int(m.group(1)) if m else None, text))
    return out

DEFS = ROOT/"cache/assets/osnr/custom_items/item_config/definitions.toml"
def_blocks = blocks(DEFS)
def_ids = collections.Counter(b[2] for b in def_blocks if b[2] is not None)
if len(def_blocks) != 517:
    err("TOML", f"definitions.toml has {len(def_blocks)} blocks (expected 517)")
missing = [i for i in DEF_KILL if def_ids[i] == 0]
if missing:
    err("TOML", f"kill ids missing from definitions.toml: {missing[:10]}")
multi = [i for i in DEF_KILL if def_ids[i] > 1]
if multi:
    err("TOML", f"kill ids with multiple definition blocks (need manual review): {multi}")
kill_blocks = [b for b in def_blocks if b[2] in DEF_KILL]
if len(kill_blocks) != 169:
    err("TOML", f"definitions.toml kill blocks = {len(kill_blocks)} (expected 169)")

for f, n in WHOLE_FILE_KILLS.items():
    p = ROOT/f
    if not p.exists():
        err("TOML", f"missing whole-file kill target {f}"); continue
    bl = blocks(p)
    if len(bl) != n:
        err("TOML", f"{f}: {len(bl)} blocks (expected {n})")

store_blocks = blocks(ROOT/"cache/assets/types/store_items.toml")
store_kill = [b for b in store_blocks if b[2] in STORE_ANKOU]
if len(store_kill) != 16:
    err("TOML", f"store_items.toml ankou blocks = {len(store_kill)} (expected 16)")
for keep_id in (2715, 2724, 2726, 2728, 2730, 2732, 2734, 2736, 2738, 2740, 2742, 2744, 19782, 30000, 30001, 30002):
    if not any(b[2] == keep_id for b in store_blocks):
        err("KEEP", f"store_items.toml keep row {keep_id} missing")

# surviving blocks must not inherit from killed ids
KILL_ALL_DEF_IDS = DEF_KILL | STORE_ANKOU | PACKER_KILL | {
    i for f in WHOLE_FILE_KILLS for i in [b[2] for b in blocks(ROOT/f)] if i is not None
}
surviving_texts = []
for toml in list((ROOT/"cache/assets/types").rglob("*.toml")) + [DEFS]:
    rel = str(toml)
    if rel.replace("\\", "/") in WHOLE_FILE_KILLS: continue
    for b in blocks(toml):
        if toml == DEFS and b[2] in DEF_KILL: continue
        if rel.endswith("store_items.toml") and b[2] in STORE_ANKOU: continue
        surviving_texts.append((rel, b))
for rel, b in surviving_texts:
    m = re.search(r"^inherit\s*=\s*(\d+)", b[3], re.M)
    if m and int(m.group(1)) in KILL_ALL_DEF_IDS:
        err("TOML", f"surviving block id={b[2]} in {rel} inherits from killed id {m.group(1)}")

# ---------------------------------------------------------------- model closure
MODEL_KEY = re.compile(
    r"^(?:invmodel|primarymalemodel|primaryfemalemodel|secondarymalemodel|secondaryfemalemodel"
    r"|primarymaleheadmodel|primaryfemaleheadmodel|secondarymaleheadmodel|secondaryfemaleheadmodel)"
    r"\s*=\s*(\d+)", re.M)
killed_model_ids = set()
for b in kill_blocks:
    killed_model_ids.update(int(x) for x in MODEL_KEY.findall(b[3]))
for f in WHOLE_FILE_KILLS:
    killed_model_ids.update(int(x) for x in MODEL_KEY.findall((ROOT/f).read_text(encoding="utf-8")))
for b in store_kill:
    killed_model_ids.update(int(x) for x in MODEL_KEY.findall(b[3]))
killed_model_ids -= {0, 29209}  # -1 sentinel artifacts / vanilla scroll model

MAP = ROOT/"cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt"
maptext = MAP.read_text(encoding="utf-8")
name2id = {m.group(1): int(m.group(2)) for m in re.finditer(
    r'"([^"]+)"\s+to\s+CustomDefinition\.Model\((?:modelId\s*=\s*)?(-?\d+)\)', maptext)}

# packer-owned model names inside the OriginsPacker cut regions
ORIG = ROOT/"cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityOriginsPacker.kt"
origtext = ORIG.read_text(encoding="utf-8")
def region(text, start, end, tag):
    i, j = text.find(start), text.find(end)
    if not (0 <= i < j):
        err(tag, f"cut anchors not found/ordered: {start[:40]!r} .. {end[:40]!r}")
        return ""
    if text.count(start) != 1 or text.count(end) != 1:
        err(tag, "cut anchor not unique")
    return text[i:j]
R1 = region(origtext, "    @JvmStatic fun packPets() {", "    @JvmStatic fun packCustoms() {", "B2-R1")
R2 = region(origtext, "        32884.newItem()", "        32914.newItem()", "B2-R2")
R3 = region(origtext, "        ELYSIAN_SIGIL.cloneThisTo(32955)", "    private fun Int.promoBundle(number: Int) =", "B2-R3")
for kept_token in ["32914", "32916", "32948", "origins_primal_", "packCustoms() {"]:
    for tag, r in (("B2-R2", R2), ("B2-R3", R3)):
        if kept_token in r:
            err(tag, f"kept token {kept_token} inside cut region")
if "packPets" in R2 + R3:
    err("B2", "unexpected packPets token in packCustoms cut regions")
packer_cut_model_names = set(re.findall(r'"([A-Za-z0-9_ ]+)"\.model\(\)', R1 + R2 + R3))
kept_packer_text = origtext
for r in (R1, R2, R3):
    kept_packer_text = kept_packer_text.replace(r, "")
for n in packer_cut_model_names:
    if f'"{n}"' in kept_packer_text:
        err("MODEL", f"model name {n!r} also used by kept packer code")
    if n not in name2id:
        err("MODEL", f"cut-region model name {n!r} has no map entry")

# whole-dir deletions
DIR_KILLS = {
    "cache/assets/origins/pets/models": 39,            # dark_*/fissile_*/corrupt_kratos_* + origins_primal_workbench (orphan)
    "cache/assets/rebirth/master_comp_cape/models": 15,
}
dir_kill_names = set()
for d, n in DIR_KILLS.items():
    p = ROOT/d
    if not p.exists():
        err("ASSET", f"missing dir {d}"); continue
    files = sorted(f.stem for f in p.glob("*.dat"))
    if len(files) != n:
        err("ASSET", f"{d}: {len(files)} files (expected {n})")
    dir_kill_names.update(files)

# model files across the defaultModels() universe
MODEL_DIRS = ["cache/assets/osnr/custom_items/models", "cache/assets/origins/customs/models",
              "cache/assets/origins/pets/models", "cache/assets/rebirth/master_comp_cape/models",
              "cache/assets/rebirth/customs_2023/models", "cache/assets/rebirth/customs_2024/models",
              "cache/assets/rebirth/customs_2025/models", "cache/assets/rebirth/legacy_models/models",
              "cache/assets/rebirth/misc_models/models"]
file2dir = {}
for d in MODEL_DIRS:
    p = ROOT/d
    if p.exists():
        for f in p.glob("*.dat"):
            file2dir[f.stem] = d

# candidate file deletions: (a) map-name resolves to a killed model id, (b) name in a cut
# packer region, (c) name in a whole-dir kill
# exclusivity: no surviving toml refs any killed model id; killed ids referenced only by dying content
surviving_model_ids = set()
for rel, b in surviving_texts:
    surviving_model_ids.update(int(x) for x in MODEL_KEY.findall(b[3]))
# ids shared with SURVIVING defs are excluded from deletion (frozen expected set):
#   60067 nr_launch_box (OSNR box 32080 keeps it), 60290 "Easter M boxb" (PVM Arena box
#   32423 reuses it). Anything else shared is an unplanned overlap -> error.
shared = killed_model_ids & surviving_model_ids
if shared != {60067, 60290}:
    err("MODEL", f"unexpected shared-model set (expected {{60067, 60290}}): {sorted(shared)}")
killed_model_ids -= shared
# npc tomls' models=[...] arrays (Hans wears master comp cape model 60513 -> paired A-edit)
npc_model_refs = collections.Counter()
for toml in (ROOT/"cache/assets/types/npc").glob("*.toml"):
    for arr in re.findall(r"^models\s*=\s*\[([0-9,\s]*)\]", toml.read_text(encoding="utf-8"), re.M):
        for x in arr.split(","):
            x = x.strip()
            if x: npc_model_refs[int(x)] += 1
kill_ids_in_npcs = {i for i in killed_model_ids if npc_model_refs.get(i)}
# 60181-60186: the Lil_* bobble models double as the KEPT rots.toml minion NPC models
#   -> files + map entries STAY (only the item defs die).
# 60513: master comp cape model worn by Hans 16065 -> resolved by the paired Hans A-edit.
if kill_ids_in_npcs != {60181, 60182, 60183, 60184, 60185, 60186, 60513}:
    err("MODEL", f"npc-toml-shared killed models != expected set: {sorted(kill_ids_in_npcs)}")
killed_model_ids -= {60181, 60182, 60183, 60184, 60185, 60186}
if npc_model_refs.get(60513) != 1:
    err("MODEL", "expected exactly one npc-toml ref to master cape model 60513 (Hans)")

# def-orphan additions: the 1-colour ankou defs inherit vanilla equip models and only
# use the custom *_drop invmodels; the 40 custom *_equip .dats are packed-but-unreferenced.
# They belong to the dying feature -- deleted with it (zero-reference asserted below).
ANKOU_EQUIP = re.compile(r"^ankou_(?:blue|green|orange|white)_[a-z]+_(?:male|female)_equip$")
ankou_equip_orphans = {n for n in name2id if ANKOU_EQUIP.match(n)}
if len(ankou_equip_orphans) != 40:
    err("MODEL", f"expected 40 ankou equip orphan entries, got {len(ankou_equip_orphans)}")
all_toml_model_ids = set()
for toml in list((ROOT/"cache/assets/types").rglob("*.toml")) + list((ROOT/"cache/assets/osnr/custom_items/item_config").glob("*.toml")):
    all_toml_model_ids.update(int(x) for x in MODEL_KEY.findall(toml.read_text(encoding="utf-8")))
for n in ankou_equip_orphans:
    if name2id[n] in all_toml_model_ids:
        err("MODEL", f"ankou equip model {n!r} ({name2id[n]}) referenced by a toml -- not an orphan")

kill_names = set(n for n, i in name2id.items() if i in killed_model_ids) \
    | packer_cut_model_names | dir_kill_names | ankou_equip_orphans
kill_names_with_file = {n for n in kill_names if n in file2dir}
# entry-only deletions (map rows without a .dat on this tree): 12 chaotic + 2 polypore
entry_only = {n for n in kill_names if n not in file2dir}
EXPECTED_ENTRY_ONLY = {"chaotic_cbow_eq", "chaotic_cbow_inv", "chaotic_kiteshield_eq",
    "chaotic_kiteshield_inv", "chaotic_ls_eq", "chaotic_ls_inv", "chaotic_maul_eq",
    "chaotic_maul_inv", "chaotic_rapier_eq", "chaotic_rapier_inv", "chaotic_staff_eq",
    "chaotic_staff_inv", "polypore_staff_drop", "polypore_staff_equip"}
if entry_only != EXPECTED_ENTRY_ONLY:
    err("MODEL", f"entry-only deletion set drifted: {sorted(entry_only ^ EXPECTED_ENTRY_ONLY)}")
hans = (ROOT/"cache/assets/types/npc/custom.toml").read_text(encoding="utf-8")
if hans.count("models=[217, 246, 28515, 26630, 176, 28285, 185, 320, 60513]") != 1:
    err("A-HANS", "Hans 16065 models array (with 60513) not found exactly once")

# expected deletion volume (frozen after inspection):
if not (240 <= len(kill_names_with_file) <= 245):
    err("MODEL", f"model .dat deletion count {len(kill_names_with_file)} outside expected 200-235")
by_dir = collections.Counter(file2dir[n] for n in kill_names_with_file)
print("  model .dat deletions by dir: " + ", ".join(f"{d.split('assets/')[1]}={c}" for d, c in sorted(by_dir.items())))
# pairing rule: every deleted file's map entry is deletable; map entries scheduled for
# deletion = kill_names that exist in the map
map_entry_kills = {n for n in kill_names if n in name2id}
orphan_files = {n for n in kill_names_with_file if n not in name2id}
if orphan_files:
    err("MODEL", f"killed files with no map entry (defaultModels would already error): {sorted(orphan_files)[:5]}")
# keep-guard: booster/anubis/ankou/comp-cape names really are in the kill set
for must in ["booster_pet", "slayer_task_picker", "Anubis maskb", "Ice_phatb", "ankou_blue_mask_drop",
             "OS Completionist cape (MALE)b", "master_cape_inv", "dark_imp", "origins_lord_m_cap_inv",
             "zenyte_helmet_inv", "cyan_bond_inv", "red_bond_inv", "dragon_kite_drop",
             "Carrot crownb", "mystery_box_cosmetic", "Dragon_Hunter_Gloves_Maleb",
             "eagle_eye_kiteshield_inv"]:
    if must not in kill_names_with_file:
        err("MODEL", f"expected kill model {must!r} not in computed deletion set")
for keep in ["origins_primal_2hsw_inv", "origins_primal_kiteshield_eq", "reward_casket_inv",
             "pet_cute_creature", "rebirth_scrollbox_a", "sherlock_notes_stack1", "death_cape_equip",
             "enhanced_stew", "near_reality_party_hat_equip", "pink_santab", "mystery_box_super",
             "mystery_box_ultra", "mystery_box_vote", "donator_pin_10", "Malevolent_Energyb",
             "Droprate_Boost_Scrollb", "nr_launch_box", "Easter M boxb",
             "Lil_Ahrimsb", "Lil_Dharokb", "Lil_Guthanb", "Lil_Karilb", "Lil_Toragb", "Lil_Veracb"]:
    if keep in kill_names_with_file:
        err("KEEP", f"keep model {keep!r} wrongly in deletion set")
    if keep not in file2dir:
        err("KEEP", f"keep model file {keep!r} missing from tree")

# ---------------------------------------------------------------- byte-exact FINDs
FINDS = [
    # --- Part A: boosters -------------------------------------------------
    ("content/other/death-mechanics/src/main/kotlin/org/jesse/plugins/item/actions/death_items/kept_items.kt",
     "            32149, 32151, 32152, 32153, 32154, 32155, 32156,\n", "A1-kept"),
    ("engine/src/main/kotlin/org/jesse/game/content/bountyhunter/crate/BountyCrate.kt",
     "          else if (Random.nextInt(16) == 0) Item(32149) // Larran's Key Booster\n"
     "          else if (Random.nextInt(16) == 0) Item(32154) // Blood Money Booster\n"
     "          else if (Random.nextInt(16) == 0) Item(32166) // Revenant Booster\n", "A1-crate"),
    ("engine/src/main/kotlin/org/jesse/game/content/elven/obj/NewCrystalChestLoot.kt",
     "    PET_BOOSTER(LootRarity.JACKPOT, Item(32152)),\n"
     "    SLAYER_BOOSTER(LootRarity.JACKPOT, Item(32151)),\n", "A1-cchest"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvpMysteryBox.java",
     "                new MysterySupplyItem(32149, 3, 5), // Larran's Booster\n"
     "                new MysterySupplyItem(32154, 3, 5), // Blood Money Booster\n", "A1-pvpbox"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/RegalMysteryBox.java",
     "                new MysterySupplyItem(32149, 2, 5), // Larran's Booster\n"
     "                new MysterySupplyItem(32151, 2, 5), // Slayer Booster\n"
     "                new MysterySupplyItem(32152, 2, 5), // Pet Booster\n"
     "                new MysterySupplyItem(32153, 2, 5), // Gauntlet Booster\n"
     "                new MysterySupplyItem(32154, 2, 5), // Blood Money Booster\n"
     "                new MysterySupplyItem(32155, 2, 5), // Clue Scroll Booster\n"
     "                new MysterySupplyItem(32156, 2, 5), // ToB Booster\n"
     "                new MysterySupplyItem(32157, 2, 5), // Slayer Task Picker\n"
     "                new MysterySupplyItem(32158, 2, 5), // Slayer Task Reset\n"
     "                new MysterySupplyItem(32166, 2, 5), // Rev Booster\n"
     "                new MysterySupplyItem(32167, 2, 5), // Nex Booster\n", "A1-regal"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/SuperMysteryBox.java",
     "                new MysteryItem(32149, 3, 6, 1000), // Larrans booster\n"
     "                new MysteryItem(32151, 3, 6, 1000), // Slayer booster\n"
     "                new MysteryItem(32152, 3, 6, 1000), // Pet booster\n"
     "                new MysteryItem(32153, 3, 6, 1000), // Gauntlet booster\n"
     "                new MysteryItem(32154, 3, 6, 1000), // Blood money booster\n"
     "                new MysteryItem(32155, 3, 6, 1000), // Clue booster\n"
     "                new MysteryItem(32156, 3, 6, 1000), // ToB booster\n"
     "                new MysteryItem(32157, 3, 6, 1000), // Task Picker\n"
     "                new MysteryItem(32158, 3, 6, 1000), // Task Reset\n"
     "                new MysteryItem(32166, 3, 6, 1000), // Rev Booster\n"
     "                new MysteryItem(32167, 3, 6, 1000), // Nex Booster\n", "A1-super"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/UltimateMysteryBox.java",
     "//                new MysteryItem(32149, 5, 10, 1000), // Larran's Booster\n"
     "//                new MysteryItem(32151, 5, 10, 1000), // Slayer Booster\n"
     "//                new MysteryItem(32152, 5, 10, 1000), // Pet Booster\n"
     "//                new MysteryItem(32153, 5, 10, 1000), // Gauntlet Booster\n"
     "//                new MysteryItem(32154, 5, 10, 1000), // Blood Money Booster\n"
     "//                new MysteryItem(32155, 5, 10, 1000), // Clue Scroll Booster\n"
     "//                new MysteryItem(32156, 5, 10, 1000), // ToB Booster\n"
     "//                new MysteryItem(32157, 3, 7, 1000), // Slayer Task Picker\n"
     "//                new MysteryItem(32158, 5, 10, 1000), // Slayer Task Reset\n"
     "//\t\t\t\tnew MysteryItem(32166, 5, 10, 1000), // Rev Booster\n"
     "//\t\t\t\tnew MysteryItem(32167, 5, 10, 1000), // Nex Booster\n", "A1-ult"),
    # booster read-sites -> restore vanilla behavior
    ("engine/src/main/java/org/jesse/game/content/killstreak/Killstreaks.java",
     "        if (attacker.getVariables().getBloodMoneyBoosterLeft() > 0) {\n"
     "            attacker.getVariables().setBloodMoneyBoosterLeft(attacker.getVariables().getBloodMoneyBoosterLeft() - 1);\n"
     "            totalBloodmoney *= 1.25;\n"
     "        }\n", "A2-killstreak"),
    ("engine/src/main/java/org/jesse/game/content/treasuretrails/plugins/ClueCasket.java",
     "        boolean booster = false;\n"
     "        if (player.getVariables().getClueBoosterLeft() > 0) {\n"
     "            player.getVariables().setClueBoosterLeft(player.getVariables().getClueBoosterLeft() - 1);\n"
     "            booster = true;\n"
     "        }\n", "A2-casket-a"),
    ("engine/src/main/java/org/jesse/game/content/treasuretrails/plugins/ClueCasket.java",
     "        final List<Item> loot = rewards.roll(player.inArea(Entrana.class), false, booster);\n", "A2-casket-b"),
    ("engine/src/main/java/org/jesse/game/content/treasuretrails/rewards/ClueRewardTable.java",
     "    public final List<Item> roll(final boolean entrana, boolean xamphurBoost, boolean clueBooster) {\n", "A2-cluetable-a"),
    ("engine/src/main/java/org/jesse/game/content/treasuretrails/rewards/ClueRewardTable.java",
     "\n        if (clueBooster) {\n            min += 2;\n            max += 2;\n        }\n", "A2-cluetable-b"),
    ("engine/src/main/java/org/jesse/game/world/entity/npc/impl/slayer/superior/SuperiorNPC.java",
     "        if (killer.getVariables().getSlayerBoosterTick() > 0) {\n"
     "            probability *= 1.2;\n"
     "        }\n", "A2-superior"),
    ("engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java",
     "\n        if (player != null && player.getVariables().getPetBoosterTick() > 0) {\n"
     "            finalRarity *= 0.9;\n        }\n", "A2-bosspet"),
    ("engine/src/main/java/org/jesse/game/content/follower/impl/SkillingPet.java",
     "\n        if (player != null && player.getVariables().getPetBoosterTick() > 0) {\n"
     "            roll *= 0.9;\n        }\n", "A2-skillpet"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerHelper.kt",
     "        if (player.variables.slayerBoosterTick > 0) {\n"
     "            worldRate = 50.coerceAtLeast((worldRate * 0.75).toInt())\n        }\n", "A2-superior-rate"),
    ("content/bosses/nightmare/src/main/kotlin/org/jesse/game/content/boss/nightmare/NightmareDropExt.kt",
     "        if (player.variables.petBoosterTick > 0) {\n"
     "            petRate = (petRate * 0.9).toInt()\n        }\n", "A2-nightmare"),
    ("content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/revenant/npc/Revenant.kt",
     "            if (killer.variables.revenantBoosterTick > 0) {\n"
     "                chanceA = (chanceA * 0.9).toInt()\n            }\n", "A2-revenant"),
    ("content/bosses/nex/src/main/kotlin/org/jesse/game/content/boss/nex/npc/drops/nex.kt",
     "\n                        if ((type == Unique || type == Tertiary) && player.variables.nexBoosterleft > 0)\n"
     "                            rarityScale += 0.15\n", "A2-nex-a"),
    ("content/bosses/nex/src/main/kotlin/org/jesse/game/content/boss/nex/npc/drops/nex.kt",
     "                    if (player.variables.nexBoosterleft > 0) {\n"
     "                        player.variables.nexBoosterleft--\n                    }\n\n", "A2-nex-b"),
    # ::boosters surface
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     '        new Command(PlayerPrivilege.PLAYER, "boosters", "Opens your active boosters.", (p, args) -> {\n'
     "            openBoosters(p);\n        });\n", "A3-cmd"),
    ("engine/src/main/java/org/jesse/game/model/ui/testinterfaces/GameNoticeboardInterface.java",
     '        bind("Boosters", GameCommands::openBoosters);\n', "A3-bind"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Analytics.java",
     "\t\t\tstatement.setBoolean(20, checkInteraction(flags, InteractionType.CHECK_BOOSTERS));\n", "A3-analytics-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Analytics.java",
     "\t\tCHECK_BOOSTERS(16),\n", "A3-analytics-b"),
    # --- Part A: divine spirit shield / origins ---------------------------
    ("engine/src/main/java/org/jesse/game/model/item/BossDropItem.java",
     "    DIVINE_SPIRIT_SHIELD(new Item(ItemId.DIVINE_SPIRIT_SHIELD), new Item(ItemId.DIVINE_SIGIL), new Item(12831)),\n", "A4-bossdrop"),
    ("engine/src/main/java/org/jesse/game/model/item/BossDropItem.java",
     "    AHRIM_THE_BOBBLED(32186, new Item(ItemId.AHRIMS_ARMOUR_SET), new Item(32185, 250)),\n"
     "    DHAROK_THE_BOBBLED(32187, new Item(ItemId.DHAROKS_ARMOUR_SET), new Item(32185, 250)),\n"
     "    GUTHAN_THE_BOBBLED(32188, new Item(ItemId.GUTHANS_ARMOUR_SET), new Item(32185, 250)),\n"
     "    KARIL_THE_BOBBLED(32189, new Item(ItemId.KARILS_ARMOUR_SET), new Item(32185, 250)),\n"
     "    TORAG_THE_BOBBLED(32190, new Item(ItemId.TORAGS_ARMOUR_SET), new Item(32185, 250)),\n"
     "    VERAC_THE_BOBBLED(32191, new Item(ItemId.VERACS_ARMOUR_SET), new Item(32185, 250)),\n", "A4-bobbled"),
    ("engine/src/main/java/org/jesse/game/model/item/enums/RareDrop.java",
     "    DIVINE_SIGIL(ItemId.DIVINE_SIGIL),\n", "A4-raredrop"),
    ("engine/src/main/java/org/jesse/plugins/itemonobject/SpiritShieldCreationPlugin.java",
     "\t\treturn new Object[] {12819, 12823, 12827, 32955};\n", "A4-ssplugin"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/action/combat/CombatUtilities.java",
     "\n    public static boolean isDivineSpiritShield(int itemId) {\n"
     "        return itemId == ItemId.DIVINE_SPIRIT_SHIELD;\n    }\n", "A4-combatutil"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
     "        if (type != HitType.DEFAULT && CombatUtilities.isDivineSpiritShield(shieldId)) {\n"
     "            double drainFactor = 0.2F;\n"
     "            int prayerPointCheck = (int) Math.floor(damage * 0.3F * drainFactor);\n"
     "            if (prayerManager.getPrayerPoints() >= prayerPointCheck) {\n"
     "                final int reduced = (int) (damage * 0.3F);\n"
     "                setGraphics(ELYSIAN_EFFECT_GFX);\n"
     "                damage -= reduced;\n            }\n        }\n", "A4-player-dss"),
    # --- Part A: mystery boxes / sets ------------------------------------
    ("engine/src/main/java/org/jesse/plugins/item/CustomItemSet.java",
     "\n        MYSTERY_BOX(32215, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32165, 32206, 32206),\n", "A5-boxset"),
    ("engine/src/main/java/org/jesse/game/content/alternatetables/AlternateTable.java",
     "    MYSTERY_BOX_3RD_AGE(MysteryBox3ATable.class),\n    MYSTERY_BOX_COSMETIC(MysteryBoxCosmeticTable.class),\n", "A5-alttable"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvpTourneyMysteryBox.java",
     "                new MysteryItem(ItemId.POLYPORE_STAFF, 1, 1, 1000),\n", "A6-polypore"),
    # --- Part A: sumona ----------------------------------------------------
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerMaster.kt",
     "    SUMONA(org.jesse.game.npc.ids.SUMONA, 99, 100, 15, \"at Home\"),\n", "A7-master-row"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerMaster.kt",
     "        if (this == SUMONA) return \"Summona\"\n", "A7-master-tostring"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerHelper.kt",
     "            if(master != SlayerMaster.SUMONA && tsk.isAssignableBySumonaOnly) continue\n", "A7-helper"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerExt.kt",
     "import org.jesse.game.content.slayer.dialogue.SumonaAssignmentD.Companion.SUMMONA_TASK_COST\n", "A7-ext-import"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerExt.kt",
     "fun Player.getSumonaTasks(wildernessIncluded: Boolean = false) = BossTask.entries\n"
     "    .filter { if(wildernessIncluded) !it.wilderness else true }\n"
     "    .filter { it.predicate.test(this) }\n    .toTypedArray()\n\n", "A7-ext-tasks"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerExt.kt",
     "fun Player.underSumonaReqs() = skills.getLevel(SkillConstants.SLAYER) < SlayerMaster.SUMONA.slayerRequirement || skills.combatLevel < SlayerMaster.SUMONA.combatRequirement\n"
     "fun Player.underSumonaGP() = inventory.getAmountOf(COINS_995) < SUMMONA_TASK_COST\n", "A7-ext-reqs"),
    ("engine/src/main/java/org/jesse/game/content/skills/slayer/Slayer.java",
     "            case SUMONA -> {\n"
     "                completedInARow = getSumonaStreak() + 1;\n"
     "                setSumonaStreak(completedInARow);\n            }\n", "A7-slayer-case"),
    ("engine/src/main/java/org/jesse/game/content/skills/slayer/Slayer.java",
     "    public int getSumonaStreak() {\n"
     "        return player.getNumericAttribute(\"sumona completed tasks in a row\").intValue();\n    }\n\n"
     "    public void setSumonaStreak(final int value) {\n"
     "        player.addAttribute(\"sumona completed tasks in a row\", value);\n    }\n\n", "A7-slayer-streak"),
    ("engine/src/main/java/org/jesse/game/content/skills/slayer/Slayer.java",
     "    public boolean sumonaAssignWildernessTasks() {\n"
     "        return player.getBooleanAttribute(\"sumona wildy tasks\");\n    }\n\n"
     "    public void setSumonaAssignWildernessTasks(final boolean value) {\n"
     "        player.addAttribute(\"sumona wildy tasks\", value ? 1 : 0);\n    }\n\n", "A7-slayer-wildy"),
    ("engine/src/main/java/org/jesse/game/content/skills/slayer/Slayer.java",
     "        master = parser.getSlayer().master;\n", "A7-slayer-load"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/SlayerMasterNPC.java",
     "                if (npc.getId() == SlayerMaster.SUMONA.getNpcId()) {\n"
     "                    player.getDialogueManager().start(new SumonaAssignmentD(player, npc));\n"
     "                } else if (npc.getId() == SlayerMaster.TURAEL.getNpcId()) {\n", "A7-npc-assign"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/SlayerMasterNPC.java",
     "                if (npc.getId() == SlayerMaster.SUMONA.getNpcId()) {\n"
     "                    player.getDialogueManager().start(new SumonaD(player, npc));\n"
     "                } else if (npc.getId() == SlayerMaster.TURAEL.getNpcId()) {\n", "A7-npc-talk"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/SlayerMasterNPC.java",
     "                NpcId.SUMONA,\n", "A7-npc-list"),
    ("core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt",
     "const val SUMONA = 16064\n", "A7-npcid"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/Assignment.kt",
     "        task = getTask(taskName)\n", "A7-assign-anchor"),
    # --- Part A: primula / barrows clones / spawn loader -------------------
    ("engine/src/main/java/org/jesse/game/world/entity/npc/spawns/NPCSpawnLoader.java",
     "        //rdi barrows\n"
     "        dropViewerNPCs.add(16052);\n        dropViewerNPCs.add(16053);\n"
     "        dropViewerNPCs.add(16054);\n        dropViewerNPCs.add(16055);\n"
     "        dropViewerNPCs.add(16056);\n        dropViewerNPCs.add(16057);\n", "A8-dropviewer"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/DharokTheWretched.java",
     "            case 1673, 16053 -> true;\n", "A8-dharok"),
    # --- Part A: comp capes ------------------------------------------------
    ("engine/src/main/java/org/jesse/game/world/entity/player/var/VarCollection.java",
     "    COMP_PROGRESS(VARBIT, 6347, CompletionistCape::checkRequirements, POST_LOGIN),//quest points\n", "A9-varcol"),
    ("engine/src/main/java/org/jesse/game/GameInterface.java",
     "    COMP_SELECTION(1617),\n    COMP_PROGRESS(1618),\n", "A9-gameinterface"),
    ("engine/src/main/java/org/jesse/game/model/ui/testinterfaces/CharacterSummaryInterface.java",
     "                case 3 -> COMP_PROGRESS.open(player);\n", "A9-charsummary"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
     "import org.jesse.game.content.compcapes.CompletionistCape;\n", "A9-player-import"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
     "        double compCape = getCompletionistCapeDRBoost();\n"
     "        return ((gameMode + donor + pin + compCape) * 100.0D);\n", "A9-player-dr"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
     "    public double getCompletionistCapeDRBoost() {\n"
     "        Item cape = getCape();\n        if (cape == null)\n            return 0.0D;\n"
     "        int tier = CompletionistCape.getCompletionistCapeTier(getCape().getId());\n"
     "        return switch (tier) {\n            case 1 -> 0.01D;\n            case 2 -> 0.02D;\n"
     "            case 3 -> 0.03D;\n            default -> 0.0D;\n        };\n    }\n", "A9-player-boost"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     "            double compCape = p.getCompletionistCapeDRBoost();\n"
     '            p.sendMessage("DropRate: " + (int) ((gameMode + donor + pin + compCape) * 100D) + "%. Mode: " + (int) (gameMode * 100D) + "%. Donor: " + (int) (donor * 100D) + "%. Pin: " + (int) (pin * 100D) + "%");\n', "A9-gc-droprate"),
    ("engine/src/main/java/org/jesse/game/model/ui/testinterfaces/GameNoticeboardInterface.java",
     "        double compCape = player.getCompletionistCapeDRBoost();\n"
     "        int percent = (int) ((gameMode + donor + pin + compCape) * 100.0D);\n", "A9-gnb-droprate"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/action/combat/RangedCombat.java",
     "        COMP_CAPE_T1(COMPLETIONIST_CAPE, 0),\n"
     "        COMP_CAPE_T2(COMPLETIONIST_CAPE_T, 0),\n"
     "        COMP_CAPE_T3(MASTER_COMP_CAPE, 0),\n", "A9-ranged"),
    ("engine/src/main/kotlin/org/jesse/game/content/gravestone/GravestoneExt.kt",
     "            COMPLETIONIST_CAPE, COMPLETIONIST_CAPE_T, MASTER_COMP_CAPE\n            -> 500_000\n\n", "A9-gravestone"),
    ("engine/src/main/kotlin/org/jesse/game/content/commands/DeveloperCommands.kt",
     "import org.jesse.game.content.compcapes.CompletionistCape\n", "A9-dev-import"),
    # --- Part B: packers ---------------------------------------------------
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityOriginsPacker.kt",
     "    @JvmStatic fun pack() {\n        packPets()\n        packCustoms()\n    }\n", "B1-pack"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityRebirthPacker.kt",
     "        packMasterCompCape()\n", "B3-call"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityRebirthPacker.kt",
     "    @JvmStatic fun packMasterCompCape() {\n"
     "        assetsBase(\"assets/rebirth/master_comp_cape/\") {\n            defaultModels()\n        }\n    }\n\n", "B3-fun"),
    ("engine/src/main/java/org/jesse/plugins/item/StorePurchaseConsume.java",
     "            case 31317: return new Item(32158, 25);\n", "A1-storepack-a"),
    ("engine/src/main/java/org/jesse/plugins/item/StorePurchaseConsume.java",
     "        return new int[]{31300, 31301, 31302, 31303, 31304, 31305, 31306, 31307, 31308, 31309, 31310, 31311, 31312, 31313, 31314, 31315, 31316, 31317};\n", "A1-storepack-b"),
    ("engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvmArenaMysteryBox.java",
     "            new MysteryItem(ItemId.SLAYER_TASK_RESET_SCROLL, 1, 5, UNCOMMON),\n"
     "            new MysteryItem(ItemId.SLAYER_TASK_PICKER_SCROLL, 1, 5, UNCOMMON),\n", "A1-pvmarena"),
    ("cache/src/main/java/mgi/types/config/items/ItemDefinitions.java",
     "import static org.jesse.game.item.ids.ItemId.ORB_OF_BLOOD;\nimport static org.jesse.game.item.ids.ItemId.ORB_OF_XERIC;\n", "A6-itemdefs-a"),
    ("cache/src/main/java/mgi/types/config/items/ItemDefinitions.java",
     "        if (id == ORB_OF_XERIC || id == ORB_OF_BLOOD)\n"
     "            return \"This will let a solo raid, skip to the end fight.\";\n", "A6-itemdefs-b"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "            this.values[idx++] = 26255            // Dragon Hunter Gloves\n", "B4-enum840"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "            this.values[26255] = 400\n", "B4-enum842"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt",
     "            this.values[26255] = \"Has the same stats as Barrows Gloves, in addition gives a 25% slayer xp bonus on dragon slayer tasks, and 15% more accuracy on dragons. Requires 41 defence.\"\n", "B4-enum843"),
]
for path, needle, tag in FINDS:
    p = ROOT/path
    if not p.exists():
        err(tag, f"MISSING FILE: {path}"); continue
    n = p.read_text(encoding="utf-8").count(needle)
    if n != 1:
        err(tag, f"FIND matched {n} times (expected 1) in {path}")

# ---------------------------------------------------------------- regex FINDs (count-asserted)
REGEX_FINDS = [
    ("engine/src/main/java/org/jesse/game/world/entity/player/action/combat/AmmunitionDefinitions.java",
     r", ItemId\.CHAOTIC_CROSSBOW\)", 21, "A6-ammo"),
    # PlayerVariables: 9 copy-ctor lines, 9 fields, 9 getter/setter pairs, 4 tick blocks
    ("engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java",
     r"^        (?:gauntletBoosterCompletionsLeft|bloodMoneyBoosterLeft|clueBoosterLeft|tobBoosterleft|larransKeyBoosterTick|slayerBoosterTick|petBoosterTick|revenantBoosterTick|nexBoosterleft) = copy\.", 9, "A1-pv-copy"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java",
     r"^    private int (?:gauntletBoosterCompletionsLeft|bloodMoneyBoosterLeft|clueBoosterLeft|tobBoosterleft|larransKeyBoosterTick|slayerBoosterTick|petBoosterTick|revenantBoosterTick|nexBoosterleft);", 9, "A1-pv-fields"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java",
     r"^        if \((?:larransKeyBoosterTick|slayerBoosterTick|petBoosterTick|revenantBoosterTick) > 0\) \{", 4, "A1-pv-ticks"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java",
     r"^    public (?:int get|void set)(?:GauntletBoosterCompletionsLeft|BloodMoneyBoosterLeft|ClueBoosterLeft|TobBoosterleft|LarransKeyBoosterTick|SlayerBoosterTick|PetBoosterTick|RevenantBoosterTick|NexBoosterleft)\(", 18, "A1-pv-accessors"),
    # openBoosters: the 10 info.add booster lines + method shell removed whole
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     r"^    public static void openBoosters\(Player p\) \{", 1, "A3-open"),
    # CollectionLogRewards: 12 dead consts + their array elements
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     r"^    private const val (?:petBooster|larransBooster|slayerBooster|bloodMoneyBooster|revenantBooster|clueBooster|nexBooster|slayerTaskPicker|slayerTaskReset|coxSoloOrb|tobSoloOrb|toaSoloOrb) = ", 12, "A1-cl-consts"),
    ("content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt",
     r"(?:petBooster|larransBooster|slayerBooster|bloodMoneyBooster|revenantBooster|clueBooster|nexBooster|slayerTaskPicker|slayerTaskReset|coxSoloOrb|tobSoloOrb|toaSoloOrb) x \d+", 45, "A1-cl-elems"),
    # CustomItemSet: 18 ankou rows
    ("engine/src/main/java/org/jesse/plugins/item/CustomItemSet.java",
     r"^        [A-Z_]*ANKOU\(", 16, "A5-itemset"),
    # CustomCommands: 25 ankou spawn lines
    ("engine/src/main/kotlin/org/jesse/game/content/commands/CustomCommands.kt",
     r"^                        add\(Item\((?:BLUE|GREEN|GOLD|WHITE|BLACK)_ANKOU", 25, "A5-customcmd"),
    # wight validate arms (Ahrim/Guthan/Karil/Torag/Verac style)
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/AhrimTheBlighted.java",  r"case 16052", 1, "A8-w-ahrim"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/GuthanTheInfested.java", r"case 16054", 1, "A8-w-guthan"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/KarilTheTainted.java",   r"case 16055", 1, "A8-w-karil"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/ToragTheCorrupted.java", r"case 16056", 1, "A8-w-torag"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/wights/VeracTheDefiled.java",   r"case 16057", 1, "A8-w-verac"),
    # sumona: dev command block + generateSumonaTask + dsl flag survives
    ("engine/src/main/kotlin/org/jesse/game/content/commands/DeveloperCommands.kt",
     r'Command\(PlayerPrivilege\.DEVELOPER, "sumonatask"\)', 1, "A7-devcmd"),
    ("engine/src/main/kotlin/org/jesse/game/content/slayer/SlayerExt.kt",
     r"^fun Player\.generateSumonaTask\(\): Assignment \{", 1, "A7-ext-gen"),
    ("engine/src/main/kotlin/org/jesse/game/content/commands/DeveloperCommands.kt",
     r'Command\(PlayerPrivilege\.TRUE_DEVELOPER, "allowt3compcape"\)', 1, "A9-dev-allow"),
    # pane binds
    ("engine/src/main/java/org/jesse/game/model/ui/testinterfaces/ResizablePaneInterface.java",
     r'bind\("Character Summary", \(player, slotId, itemId, option\) -> VarCollection\.COMP_PROGRESS\.updateSingle\(player\)\);', 1, "A9-pane-r"),
    ("engine/src/main/java/org/jesse/game/model/ui/testinterfaces/FixedPaneInterface.java",
     r'bind\("Character Summary", .*COMP_PROGRESS\.updateSingle', 1, "A9-pane-f"),
    ("engine/src/main/java/org/jesse/game/model/ui/testinterfaces/SidePanelsResizablePaneInterface.java",
     r'bind\("Character Summary", .*COMP_PROGRESS\.updateSingle', 1, "A9-pane-s"),
    # cape customizer: exactly 3 MASTER_COMP_CAPE + 2 MASTER_COMP_HOOD usages to re-point at local consts
    ("engine/src/main/kotlin/org/jesse/game/model/ui/cape_customizer/CapeCustomizerInterfacePlugin.kt",
     r"MASTER_COMP_CAPE", 3, "A9-capecust-cape"),
    ("engine/src/main/kotlin/org/jesse/game/model/ui/cape_customizer/CapeCustomizerInterfacePlugin.kt",
     r"MASTER_COMP_HOOD\b", 2, "A9-capecust-hood"),
]
for path, pat, n, tag in REGEX_FINDS:
    p = ROOT/path
    if not p.exists():
        err(tag, f"MISSING FILE: {path}"); continue
    c = len(re.findall(pat, p.read_text(encoding="utf-8"), re.M))
    if n is not None and c != n:
        err(tag, f"regex {pat[:50]!r} matched {c} (expected {n}) in {path}")

# CollectionLogRewards: no reward array may become empty after element removal
cl = (ROOT/"content/interfaces/collection-log/src/main/kotlin/org/jesse/game/content/collectionlog/CollectionLogRewards.kt").read_text(encoding="utf-8")
DEAD = re.compile(r"\b(?:petBooster|larransBooster|slayerBooster|bloodMoneyBooster|revenantBooster|clueBooster|nexBooster|slayerTaskPicker|slayerTaskReset|coxSoloOrb|tobSoloOrb|toaSoloOrb) x \d+\b")
for m in re.finditer(r"arrayOf\(([^()]*(?:\([^()]*\)[^()]*)*)\)", cl):
    inner = m.group(1)
    if DEAD.search(inner):
        rest = DEAD.sub("", inner)
        if not re.search(r"\w+ x \d+", rest):
            err("A1-cl-empty", f"reward array would become empty: {inner[:70]}...")

# ---------------------------------------------------------------- deletions (git rm targets)
DELETIONS = [
    # engine code
    "engine/src/main/java/org/jesse/game/content/compcapes",                     # 7 files
    "engine/src/main/kotlin/org/jesse/game/content/storebundle",                 # 3 files
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/CosmeticMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/EasterMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/ThirdAgeMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/EnchancedUltimateMysteryBox.java",
    "engine/src/main/java/org/jesse/game/content/alternatetables/impl/MysteryBox3ATable.java",
    "engine/src/main/java/org/jesse/game/content/alternatetables/impl/MysteryBoxCosmeticTable.java",
    "engine/src/main/java/org/jesse/game/content/skills/slayer/dialogue/SumonaD.java",
    "engine/src/main/kotlin/org/jesse/game/content/slayer/dialogue/SumonaAssignmentD.kt",
    "engine/src/main/java/org/jesse/plugins/renewednpc/PrimulaNPC.java",
    "engine/src/main/java/org/jesse/game/content/chambersofxeric/Raids1BypassTask.java",  # zero-caller orb verb
    # assets
    "cache/assets/origins/pets",
    "cache/assets/rebirth/master_comp_cape",
]
for path in DELETIONS + list(WHOLE_FILE_KILLS):
    if not (ROOT/path).exists():
        err("DEL", f"MISSING: {path}")
if len(list((ROOT/"engine/src/main/java/org/jesse/game/content/compcapes").glob("*.java"))) != 7:
    err("DEL", "compcapes package != 7 files")
if len(list((ROOT/"engine/src/main/kotlin/org/jesse/game/content/storebundle").glob("*.kt"))) != 3:
    err("DEL", "storebundle package != 3 files")

# ---------------------------------------------------------------- ItemId / NpcId constants
KILL_IDS_FULL = KILL_ALL_DEF_IDS  # defs + whole files + store ankou + packer items
itemid_text = (ROOT/"core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt").read_text(encoding="utf-8")
tail_kill_consts = [(i, name) for i, name, src in constants if i in KILL_IDS_FULL]
for i, name in tail_kill_consts:
    if len(re.findall(rf"^const val {re.escape(name)} = {i}(?: //.*)?$", itemid_text, re.M)) != 1:
        err("A10-itemid", f"expected const val {name} = {i} exactly once")
print(f"  ItemId custom-tail constants to delete: {len(tail_kill_consts)}")
if not (150 <= len(tail_kill_consts) <= 200):
    err("A10-itemid", f"constant kill count {len(tail_kill_consts)} outside expected 150-200")
npcid_text = (ROOT/"core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt").read_text(encoding="utf-8")
NPC_CONST_KILL = ["SUMONA"] + [f"REMN_{s}" for s in
    ["POSTIE_PETE","IMP","TOUCAN","KING_PENGUIN","KKLIK","SHADOW_WARRIOR","SHADOW_ARCHER","SHADOW_WIZARD",
     "HEALER_DEATH_SPAWN","HOLY_DEATH_SPAWN","SEREN","CORRUPT_BEAST","ROC","KRATOS",
     "DARK_POSTIE_PETE","DARK_IMP","DARK_TOUCAN","DARK_KING_PENGUIN","DARK_KKLIK","DARK_SHADOW_WARRIOR",
     "DARK_SHADOW_ARCHER","DARK_SHADOW_WIZARD","DARK_HEALER_DEATH_SPAWN","DARK_HOLY_DEATH_SPAWN",
     "DARK_SEREN","DARK_CORRUPT_BEAST","DARK_ROC","DARK_KRATOS","FISSILE_KRATOS"]] + ["DRIFTER"]
for name in NPC_CONST_KILL:
    if len(re.findall(rf"^const val {name} = \d+$", npcid_text, re.M)) != 1:
        err("A10-npcid", f"NpcId const {name} not found exactly once")

# no code references to killed tail constants outside files this plan edits or deletes
EDITED_OR_DELETED = {str(ROOT/p) for p, _, _ in FINDS} | {str(ROOT/p) for p, _, _, _ in REGEX_FINDS} | {
    "core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt",
    "core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt",
    "engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java",
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityOriginsPacker.kt",
    "engine/src/main/kotlin/org/jesse/game/model/ui/cape_customizer/CapeCustomizerInterfacePlugin.kt",
    "engine/src/main/java/org/jesse/game/content/chambersofxeric/Raids1BypassTask.java",
    "cache/src/main/java/mgi/types/config/items/ItemDefinitions.java",
}
EDITED_OR_DELETED |= {p for p in DELETIONS}
kill_const_names = {name for _, name in tail_kill_consts} | set(NPC_CONST_KILL)
pat = re.compile(r"\b(" + "|".join(sorted((re.escape(n) for n in kill_const_names), key=len, reverse=True)) + r")\b")
SRC_ROOTS = ["engine/src", "content", "core-model/src", "scripts", "api/src", "cache/src"]
offenders = []
for sr in SRC_ROOTS:
    for f in (ROOT/sr).rglob("*"):
        if f.suffix not in (".java", ".kt") or "build" in f.parts: continue
        rel = str(f)
        if any(rel.startswith(str(ROOT/e).rstrip("/")) for e in EDITED_OR_DELETED): continue
        t = f.read_text(encoding="utf-8", errors="ignore")
        for m in pat.finditer(t):
            offenders.append((rel, m.group(1)))
for rel, name in offenders[:20]:
    err("A10-refs", f"killed constant {name} still referenced by un-planned file {rel}")

# literal-id sweep: hot killed ids must not appear in un-planned source files
LITERALS = re.compile(r"\b(32149|32151|32152|32153|32154|32155|32156|32157|32158|32163|32166|32167"
                      r"|32186|32187|32188|32189|32190|32191|32206|32209|32215|32357|32369|32370|32371"
                      r"|32425|32826|32854|32884|32955|32958|16064|16095|16123|16133)\b")
for sr in SRC_ROOTS:
    for f in (ROOT/sr).rglob("*"):
        if f.suffix not in (".java", ".kt") or "build" in f.parts: continue
        rel = str(f)
        if any(rel.startswith(str(ROOT/e).rstrip("/")) for e in EDITED_OR_DELETED): continue
        if rel.endswith(("ObjectId.kt", "GraphicsId.kt", "NpcId.kt", "ItemId.kt",
                         "InstantMovementObjects.java",   # object id 32206 (Myths Guild stairs)
                         "FightCaves.java")): continue    # arithmetic literal 16064
        for m in LITERALS.finditer(f.read_text(encoding="utf-8", errors="ignore")):
            # object/varbit namespaces legitimately reuse these numbers -- only flag item/npc-ish files
            err("A10-literal", f"killed id literal {m.group(1)} in un-planned file {rel}")

# ---------------------------------------------------------------- BossPet row regions
bosspet = (ROOT/"engine/src/main/java/org/jesse/game/content/follower/impl/BossPet.java").read_text(encoding="utf-8")
if len(re.findall(r"^    [A-Z_]+_THE_BOBBLED\(321(?:8|9)\d, 160\d\d\),$", bosspet, re.M)) != 6:
    err("A8-bosspet", "expected exactly 6 bobbled BossPet rows")
if len(re.findall(r"NpcId\.REMN_", bosspet)) != 29:
    err("A8-bosspet", "expected exactly 29 REMN_* BossPet rows")
if "PET_FRANK(30500, 10970" not in bosspet:
    err("KEEP", "BossPet.PET_FRANK row missing (live Vanstrom drop)")

# ---------------------------------------------------------------- custom.toml npc blocks
def npc_blocks(path):
    lines = path.read_text(encoding="utf-8").splitlines(keepends=True)
    starts = []
    for i, l in enumerate(lines):
        if l.strip().startswith("[[npc]]"):
            j = i
            while j > 0 and (lines[j-1].strip().startswith("#") or lines[j-1].strip() == ""):
                j -= 1
                if lines[j].strip() == "":
                    j += 1; break
            starts.append((j, i))
    out = []
    for k, (j, i) in enumerate(starts):
        end = starts[k+1][0] if k+1 < len(starts) else len(lines)
        text = "".join(lines[j:end])
        m = re.search(r"^id\s*=\s*(\d+)", text, re.M)
        out.append((int(m.group(1)) if m else None, text))
    return out
custom_npcs = npc_blocks(ROOT/"cache/assets/types/npc/custom.toml")
have_npc = {i for i, _ in custom_npcs}
for kill_npc in (16034, 16052, 16053, 16054, 16055, 16056, 16057, 16064):
    if kill_npc not in have_npc:
        err("C-npc", f"custom.toml missing npc block {kill_npc}")
rots = (ROOT/"cache/assets/types/npc/rots.toml").read_text(encoding="utf-8")
for brother in range(16045, 16051):
    if f"id={brother}" not in rots:
        err("KEEP", f"rots.toml missing ROTS brother {brother} (bobble pets shared these ids)")

# ---------------------------------------------------------------- keep-guards
KEEP_FILES = [
    "cache/assets/types/item/scroll_boxes.toml", "cache/assets/types/item/clue_progresser_item.toml",
    "cache/assets/types/item/pet_mystery_box.toml", "cache/assets/types/item/mystery_box.toml",
    "cache/assets/types/frank.toml", "cache/assets/types/pets.toml", "cache/assets/types/npc/rots.toml",
    "cache/assets/types/npc/xamphur.toml", "cache/assets/types/item/sled.toml",
    "cache/assets/types/item/max_capes.toml", "cache/assets/types/item/imbue_scroll.toml",
    "cache/assets/types/item/tp_scrolls.toml", "cache/assets/types/tournament.toml",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/MysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/SuperMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/UltimateMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/RegalMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvpMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/SkillingMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvmArenaMysteryBox.java",
    "engine/src/main/java/org/jesse/plugins/item/mysteryboxes/PvpTourneyMysteryBox.java",
    "content/minigames/pvm-arena/src/main/kotlin/org/jesse/game/content/pvm_arena/PvmArenaManager.kt",
]
for k in KEEP_FILES:
    if not (ROOT/k).exists():
        err("KEEP", f"missing keep file {k}")
    if k in DELETIONS:
        err("KEEP", f"keep file listed for deletion: {k}")

# primal keep-set: RareDrop rows, enum 10501, struct 10501, CL row, 18 distributor files
raredrop = (ROOT/"engine/src/main/java/org/jesse/game/model/item/enums/RareDrop.java").read_text(encoding="utf-8")
if len(re.findall(r"^    PRIMAL_[A-Z_]+\(ItemId\.PRIMAL_", raredrop, re.M)) != 15:
    err("KEEP", "RareDrop primal rows != 15")
enums = (ROOT/"cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomEnumsPacker.kt").read_text(encoding="utf-8")
for token in ["EnumDefinitions.create(10501", "PRIMAL_2H_SWORD", "this.values[26300] = 500",
              "this.values[26304] = 500", "this.values[32161] = 400", "this.values[32612] = 750",
              "this.values[26706] = 150"]:
    if token not in enums:
        err("KEEP", f"NearRealityCustomEnumsPacker keep token missing: {token!r}")
if "10501   // primals" not in enums:
    err("KEEP", "enum 2103 primal CL row missing")
structs = (ROOT/"cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomStructsPacker.kt").read_text(encoding="utf-8")
if "copy(10501)" not in structs:
    err("KEEP", "struct 10501 (primal CL category) missing")
if "private const val primal = 10501" not in cl:
    err("KEEP", "CollectionLogRewards primal row missing")
DISTRIBUTORS = ["engine/src/main/kotlin/org/jesse/game/content/araxxor/rewards/Reward.kt",
                "engine/src/main/java/org/jesse/game/content/alternatetables/impl/LarransLargeChestTable.java",
                "engine/src/main/java/org/jesse/game/content/alternatetables/impl/LarransSmallChestTable.java",
                "engine/src/main/java/org/jesse/game/content/stars/ShootingStar.java",
                "content/bosses/gauntlet/src/main/kotlin/org/jesse/game/content/gauntlet/rewards/CorruptedGauntletRewards.kt",
                "content/bosses/gauntlet/src/main/kotlin/org/jesse/game/content/gauntlet/rewards/CrystallineGauntletRewards.kt",
                "content/minigames/chaoskey/src/main/kotlin/org/jesse/game/content/chaoskey/ChaosChest.kt"] + [
                f"content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/revenant/npc/drop/Revenant{n}Processor.kt"
                for n in ["Demon","Pyrefiend","Goblin","Hobgoblin","Dragon","Imp","Knight","Hellhound","Ork","Cyclops","DarkBeast"]]
for d in DISTRIBUTORS:
    p = ROOT/d
    if not p.exists() or "PRIMAL_" not in p.read_text(encoding="utf-8"):
        err("KEEP", f"primal distributor missing/changed: {d}")

# live ticks stay
pv = (ROOT/"engine/src/main/java/org/jesse/game/world/entity/player/variables/PlayerVariables.java").read_text(encoding="utf-8")
for keep_field in ["pvmArenaBoosterTick", "enhancedStewTick", "bountyHunterSkipTick"]:
    if pv.count(f"private int {keep_field};") != 1:
        err("KEEP", f"PlayerVariables live field {keep_field} missing")
if "player.variables.pvmArenaBoosterTick = (1.hours.inWholeMilliseconds / 600).toInt()" not in \
        (ROOT/"content/minigames/pvm-arena/src/main/kotlin/org/jesse/game/content/pvm_arena/PvmArenaManager.kt").read_text(encoding="utf-8"):
    err("KEEP", "PvmArenaManager booster grant missing (pvmArenaBoosterTick is LIVE)")

# save-migration anchors present exactly once (insertion points for the A-7 guards)
assign = (ROOT/"engine/src/main/kotlin/org/jesse/game/content/slayer/Assignment.kt").read_text(encoding="utf-8")
for tok in ['private fun String.stripSumona(): String = removeSuffix("_SUMONA")',
            'taskName = old.taskName.stripSumona()', 'taskName = "RATS"']:
    if assign.count(tok) != 1:
        err("A7-assign", f"Assignment.kt migration anchor not unique: {tok[:40]!r}")

# sumona flag plumbing that STAYS (assignability removed, lookup retained)
bosstask = (ROOT/"engine/src/main/kotlin/org/jesse/game/content/slayer/BossTask.kt").read_text(encoding="utf-8")
if bosstask.count("sumonaOnly(true)") != 12:
    err("A7-bosstask", "expected 12 sumonaOnly(true) BossTask rows (rows stay; assignment path dies)")

# API Bond enum must NOT use bonds.toml ids (verified claim)
bond = (ROOT/"api/src/main/kotlin/org/jesse/api/model/UserModel.kt").read_text(encoding="utf-8")
for bad in ["30051", "30060", "30061", "60017", "60018"]:
    if re.search(rf"\b{bad}\b", bond):
        err("A-bond", f"api Bond enum references bonds.toml id {bad} — bonds.toml is NOT safe to delete")

# CACHE_KEEPSET: file present, nothing this stage touches its listed assets
if not (ROOT/"CACHE_KEEPSET.md").exists():
    err("KEEP", "CACHE_KEEPSET.md missing")
for keepdir in ["cache/assets/packed/misc/archive_3/1601", "cache/assets/sprites/spellbook_teleport",
                "cache/assets/types/spell_items.toml", "cache/assets/map/osnr_tournament"]:
    if not (ROOT/keepdir).exists():
        err("KEEP", f"CACHE_KEEPSET item missing: {keepdir}")

# comp-cape interface blobs to delete
for blob, n in (("cache/assets/packed/misc/archive_3/1617", 6), ("cache/assets/packed/misc/archive_3/1618", 6)):
    p = ROOT/blob
    if not p.exists():
        err("C-blob", f"missing {blob}")
    elif len(list(p.iterdir())) != n:
        err("C-blob", f"{blob}: {len(list(p.iterdir()))} files (expected {n})")

# recolour cosmetics survive (bunny-ears placeholder fix): 32712-32800 blocks stay
for keep_id in (32712, 32754, 32755, 32756, 32800, 32801, 32066, 32078, 32234, 32237, 32415):
    if def_ids[keep_id] == 0:
        err("KEEP", f"recolour cosmetic def {keep_id} missing from definitions.toml")
if keep_id_check := [i for i in (32712, 32755, 32756, 32801) if i in DEF_KILL]:
    err("KEEP", f"recolour cosmetics wrongly in kill set: {keep_id_check}")

# ---------------------------------------------------------------- summary
print(f"  definitions.toml blocks to delete: {len(kill_blocks)}")
print(f"  store_items.toml blocks to delete: {len(store_kill)}")
print(f"  model .dat files to delete: {len(kill_names_with_file)}  map entries: {len(map_entry_kills)}")
sys.exit(1 if fail else (print("CLEAN") or 0))
```
