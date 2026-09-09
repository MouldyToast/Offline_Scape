# PLAN: Stage 5b — legacy teleport system removal + keep-set formalization

**Verified against:** `claude/new-session-lo0m56` @ `8c0e2a88`. Dry-run (Part D) CLEAN on this tree.
**Removal order:** server code (A) → TypeParser (B1) → packer classes (B2-B4) → assets (C).

## 0. Scope & corrections
- **Roadmap correction: live teleport menu = interface 1601 (TELEPORTS), not 1700.** 1700 is the legacy Zenyte-era duplicate — removed wholesale (user decision). CustomTeleport anim packer removed (output verified orphaned; live teleports use vanilla 714/111). Free kills: osnr/universal_shop 878-file 5a leftover, dead NearRealityCustomCS2Packer (+ its commented call), empty CustomEnumId. CACHE_KEEPSET.md committed permanently (1601 pack-set, tournament, 1722, pipeline keeps, Stage-6 pointer table).
- Corrections: archive_12/10001-10009 legacy CS2 blobs NEVER EXISTED on this pipeline (nothing to delete; the 1700 UI was already half-broken); neither legacy entry point was spawned (NPC 10000 / object 35000 have zero spawns/defs); teleport scrolls are vanilla elite-clue ids 12100-12105 repurposed by teleport_scrolls.toml — the toml dies here AS the vanilla restore (held scrolls revert to elite clue scrolls); 31 unlock call sites (not 30), each exactly one line + one import; ::teleloc is the only legacy command; godwars has a name-twin PortalTeleport class — greps must use the teleportsystem package; scope additions with zero-ref evidence: SpellbookOpenTeleportInterface.kt, ZenytePortalStructure.java, ZenyteTabletStructure.java, 4 legacy tomls (teleport_scrolls, teleport_scroll_read_animation, zenyte_portal_teleport, zenyte_tablet_teleport — note crossed naming of the last two).

## Part A — server code
A-1/A-2 git rm the teleportsystem packages (5 java files + PortalTeleport.kt 803L, incl. the favorite_clear_for_donator_perk listener) · A-3 TeleportInterface.java · A-4 ZenyteTeleporter.java + ZenytePortal.java · A-5 SpellbookOpenTeleportInterface.kt + ZenytePortalStructure + ZenyteTabletStructure (ZENYTE_TABLET_TELEPORT TeleportType stays — uses vanilla TabletStructure) · A-6 Player.java import/@Expose field/getter · A-7 LoginManager legacy initialize line (live getTeleportsManager() line below STAYS) · A-8 GameInterface TELEPORT_MENU(1700) · A-9 InterfaceHandler walkable entry (TELEPORTS/ADVANCED_SETTINGS/DROP_VIEWER survive) · A-10 GameCommands import + ::teleloc block · A-11 ItemOnOrFromIronmanPlugin import + TeleportScroll line (item 30031 stays → 5c) · A-12 batch: the 31 unlock files, remove exactly the unlock line + PortalTeleport import each (regex-frozen in the dry-run; empty enter() bodies kept — RDIArea/classgraph lesson).

## Part B — cache side
B-1 TypeParser: CustomTeleport import + packAll() call (packHighRevision keeps FramePacker.write() no-op-safe + AnimationBase.pack()) + the commented CS2-packer line · B-2 AnimationBase.java: delete TELEPORT_BASE_5187-5190 entries (MANDATORY — pack() reads their files from disk; player 5000/5005 + holiday 5002-5004 + the pack(int,byte[]) overload stay) · B-3 git rm CustomTeleport.java, NearRealityCustomCS2Packer.kt, CustomEnumId.kt · B-4 git rm the 4 legacy tomls (KEEP tp_scrolls.toml, spell_items.toml, both component tomls).

## Part C — assets (1,034 files)
git rm -r assets/teleportation/ (118) · packed/misc/archive_3/1700/ (38) · osnr/universal_shop/ (878). NOT: archive_3/1601+panes, sprites/spellbook_teleport, params, structs, osnr/custom_cs2 (Stage 6 roster).

## Part D — dry-run (validated CLEAN)
14 FINDs + 31-file A-12 regex assertions (one unlock line, one import, exactly 2 PortalTeleport occurrences each) + repo sweeps (teleportsystem/TELEPORT_MENU/sendClientScript 10001-9/CustomTeleport/etc. nowhere outside the kill set) + deletion counts (5+1 pkg files, 118, 38, 878, 4 tomls) + negative assert (archive_12 10001-10009 absent) + keep-guards (1601 stack, blob counts 28/98/97/95, 80 sprites, params 5000-5002, kept tomls, FramePacker/AnimationBase 5 kept entries, TeleportsPacker.pack() present, live LoginManager line, 230 DSL destinations). Embedded below; copied to repo root at execution, deleted in the exec commit.

## Part E — gates
dry-run → A → compile → B → compile → C → compile → post-greps (teleportsystem etc. zero; godwars twin excluded by package-qualified pattern) + keep-asserts → local: cache regen (no teleportation reads; seqs 15000-15003/gfx 12500-12501/sounds/bases absent; archive_3 no group 1700), plugin rescan (3 plugin classes deleted), boot, in-game (1601 menu: spellbook spells open right category, search, favourites, previous-teleport op3, Report-button; ::teleloc unknown; old save with legacy favourites + a 12100-12105 scroll loads clean — scroll shows as vanilla elite clue; pest control lander + Forinthry + one unlock-area work) → commit (plan+dry-run deleted; CACHE_KEEPSET.md permanent) → push.

## Part F — save impact
Legacy `teleportManager` subtree in saves (favorites/unlocks/lastCategory/lastTeleport) silently skipped by Gson, drops at next save — one-way loss accepted (system had no reachable entry point). Live teleportsManager favourites untouched. favorite_clear attribute lingers unread. Items 12100-12105 revert to vanilla Clue scroll (elite) — vanilla treasure-trail binds may pick them up (minor gain, accepted; 5c item audit can sweep strays).

## Part G — deliberately left
CACHE_KEEPSET.md (permanent) · osnr/custom_cs2 orphan dir → Stage 6 · christmas2019 cutscene Animation(15000-15002) refs (already wrong pre-5b) → holiday stage · generated ObjectId.NEARREALITY_PORTAL constant → Stage 6 regen · item 30031 → 5c · empty enter() bodies → Stage 6 tidy.

## Part H — roadmap bookkeeping
5b done: keep-set formalized (manifest committed), dual teleport system gone, minimal-packer EXTRACTION deferred to Stage 6 by decision. Corrections recorded (1700-vs-1601, absent legacy blobs, scroll toml = vanilla-restore resolved here). Next: 5c (CUSTOM_ITEM_IDS manifest first), then 5d.

## Embedded dry-run

```python
#!/usr/bin/env python3
"""Stage 5b dry-run gate. Run from repo root: python3 dryrun_stage5b.py. Exit 0 = CLEAN.
Verifies: every FIND block byte-exact-unique; the 31 unlock files each carry exactly
one unlock line + one PortalTeleport import and nothing else; every deletion target
exists with expected file counts; the corrected claims (archive_12/10001-10009 ABSENT);
and the 1601 live-system keep-guards hold."""
import pathlib, re, sys

fail = 0
def err(tag, msg):
    global fail
    print(f"[{tag}] {msg}"); fail += 1

FINDS = [
    # --- Part A edits ---
    ("engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
     "import org.jesse.game.world.entity.player.teleportsystem.TeleportManager;\n", "A6-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
     "    @Expose\n    private TeleportManager teleportManager = new TeleportManager(this);\n", "A6-b"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
     "    public TeleportManager getTeleportManager() {\n        return teleportManager;\n    }\n\n", "A6-c"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/login/LoginManager.java",
     "        player.getTeleportManager().initialize(parser.getTeleportManager());\n", "A7-a"),
    ("engine/src/main/java/org/jesse/game/GameInterface.java",
     "    TELEPORT_MENU(1700),\n", "A8-a"),
    ("engine/src/main/java/org/jesse/game/model/ui/InterfaceHandler.java",
     "\t\t\tGameInterface.TELEPORT_MENU,\n", "A9-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     "import org.jesse.game.world.entity.player.teleportsystem.PortalTeleport;\n", "A10-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     '        new Command(PlayerPrivilege.ADMINISTRATOR, "teleloc",\n'
     '                "Teleports you to one of the available teleportations.", (p, args) -> {\n'
     "            final String query = StringUtilities.compile(args, 0, args.length, ' ').toLowerCase();\n"
     "            final PortalTeleport[] teleports = PortalTeleport.values();\n"
     "            for (final PortalTeleport teleport : teleports) {\n"
     "                final String name = teleport.getSmallDescription().toLowerCase();\n"
     "                if (name.startsWith(query)) {\n"
     "                    teleport.teleport(p);\n"
     "                    return;\n"
     "                }\n"
     "            }\n"
     "        });\n", "A10-b"),
    ("engine/src/main/java/org/jesse/plugins/itemonplayer/ItemOnOrFromIronmanPlugin.java",
     "import org.jesse.game.world.entity.player.teleportsystem.TeleportScroll;\n", "A11-a"),
    ("engine/src/main/java/org/jesse/plugins/itemonplayer/ItemOnOrFromIronmanPlugin.java",
     "        set.addAll(TeleportScroll.map.keySet());\n", "A11-b"),
    # --- Part B edits ---
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "import mgi.custom.CustomTeleport;\n", "B1-a"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "        new CustomTeleport().packAll();\n", "B1-b"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "        // NearRealityCustomCS2Packer.pack();\n", "B1-c"),
    ("cache/src/main/java/mgi/custom/AnimationBase.java",
     '    TELEPORT_BASE_5187(5187, "assets/teleportation/animations/bases/Base 5187.dat"),\n'
     '    TELEPORT_BASE_5188(5188, "assets/teleportation/animations/bases/Base 5188.dat"),\n'
     '    TELEPORT_BASE_5189(5189, "assets/teleportation/animations/bases/Base 5189.dat"),\n'
     '    TELEPORT_BASE_5190(5190, "assets/teleportation/animations/bases/Base 5190.dat"),\n', "B2-a"),
]

for path, needle, tag in FINDS:
    p = pathlib.Path(path)
    if not p.exists():
        err(tag, f"MISSING FILE: {path}"); continue
    n = p.read_text(encoding="utf-8").count(needle)
    if n != 1:
        err(tag, f"FIND matched {n} times (expected 1) in {path}")

# --- A-12 batch: unlock-line strip across 31 files ---
UNLOCK_FILES = [
    "engine/src/main/java/org/jesse/game/world/region/area/AsgarnianIceDungeon.java",
    "engine/src/main/java/org/jesse/game/world/region/area/Brimhaven.java",
    "engine/src/main/java/org/jesse/game/world/region/area/BrimhavenDungeon.java",
    "engine/src/main/java/org/jesse/game/world/region/area/CatacombsOfKourend.java",
    "engine/src/main/java/org/jesse/game/world/region/area/CorporealBeastCavern.java",
    "engine/src/main/java/org/jesse/game/world/region/area/CrabclawCaver.java",
    "engine/src/main/java/org/jesse/game/world/region/area/FossilIsland.java",
    "engine/src/main/java/org/jesse/game/world/region/area/HosidiusHouse.java",
    "engine/src/main/java/org/jesse/game/world/region/area/KourendCastle.java",
    "engine/src/main/java/org/jesse/game/world/region/area/Lletya.java",
    "engine/src/main/java/org/jesse/game/world/region/area/MageBank.java",
    "engine/src/main/java/org/jesse/game/world/region/area/MosLeHarmless.java",
    "engine/src/main/java/org/jesse/game/world/region/area/Nardah.java",
    "engine/src/main/java/org/jesse/game/world/region/area/Pollnivneach.java",
    "engine/src/main/java/org/jesse/game/world/region/area/Rimmington.java",
    "engine/src/main/java/org/jesse/game/world/region/area/ShiloVillage.java",
    "engine/src/main/java/org/jesse/game/world/region/area/SlayerTower.java",
    "engine/src/main/java/org/jesse/game/world/region/area/Slepe.java",
    "engine/src/main/java/org/jesse/game/world/region/area/SmokeDungeonArea.java",
    "engine/src/main/java/org/jesse/game/world/region/area/TaskOnlyWyvernCave.java",
    "engine/src/main/java/org/jesse/game/world/region/area/TaverleyDungeon.java",
    "engine/src/main/java/org/jesse/game/world/region/area/VoidKnightsOutpost.java",
    "engine/src/main/java/org/jesse/game/world/region/area/WyvernCave.java",
    "engine/src/main/java/org/jesse/game/world/region/area/Yanille.java",
    "engine/src/main/java/org/jesse/game/world/region/area/apeatoll/ApeAtollArea.java",
    "engine/src/main/java/org/jesse/game/world/region/area/apeatoll/ApeAtollDungeonArea.java",
    "engine/src/main/java/org/jesse/game/world/region/area/taskonlyareas/StrongholdSlayerDungeon.java",
    "engine/src/main/java/org/jesse/game/world/region/area/wilderness/EasternDragonsArea.java",
    "engine/src/main/java/org/jesse/game/world/region/area/wilderness/WesternDragonsArea.java",
    "engine/src/main/java/org/jesse/game/content/minigame/pestcontrol/area/AbstractLanderArea.java",
    "content/areas/wilderness/src/main/kotlin/org/jesse/game/content/wilderness/revenant/area/ForinthryDungeon.kt",
]
unlock_re = re.compile(r'^\s*player\.(getTeleportManager\(\)|teleportManager)\.unlock\(PortalTeleport\.[A-Z_]+\);?\s*$')
import_re = re.compile(r'^import org\.jesse\.game\.world\.entity\.player\.teleportsystem\.PortalTeleport;?$')
for path in UNLOCK_FILES:
    p = pathlib.Path(path)
    if not p.exists():
        err("A12", f"MISSING FILE: {path}"); continue
    lines = p.read_text(encoding="utf-8").splitlines()
    ul = [l for l in lines if unlock_re.match(l)]
    il = [l for l in lines if import_re.match(l)]
    total = sum(l.count("PortalTeleport") for l in lines)
    if len(ul) != 1: err("A12", f"{path}: {len(ul)} unlock lines (expected 1)")
    if len(il) != 1: err("A12", f"{path}: {len(il)} PortalTeleport imports (expected 1)")
    if total != 2:   err("A12", f"{path}: {total} PortalTeleport occurrences (expected 2)")

# All unlock/teleportsystem references must live inside the kill/edit set
KILL_OR_EDIT = set(UNLOCK_FILES) | {
    "engine/src/main/java/org/jesse/game/world/entity/player/Player.java",
    "engine/src/main/java/org/jesse/game/world/entity/player/login/LoginManager.java",
    "engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
    "engine/src/main/java/org/jesse/plugins/itemonplayer/ItemOnOrFromIronmanPlugin.java",
    "engine/src/main/java/org/jesse/game/model/ui/testinterfaces/TeleportInterface.java",
    "engine/src/main/java/org/jesse/plugins/renewednpc/ZenyteTeleporter.java",
    "content/generic/src/main/java/org/jesse/plugins/object/ZenytePortal.java",
    "engine/src/main/kotlin/org/jesse/game/content/skills/magic/spells/teleports/SpellbookOpenTeleportInterface.kt",
}
roots = ["engine/src", "content", "core-model/src", "scripts", "api"]
for root in roots:
    for p in pathlib.Path(root).rglob("*"):
        if p.suffix not in (".java", ".kt") or "build" in p.parts: continue
        sp = str(p)
        if "world/entity/player/teleportsystem" in sp: continue  # dying package
        try: text = p.read_text(encoding="utf-8")
        except Exception: continue
        if "teleportsystem" in text and sp not in KILL_OR_EDIT:
            err("SWEEP", f"unexpected teleportsystem reference in {sp}")

# TELEPORT_MENU only in known locations
TM_OK = {
    "engine/src/main/java/org/jesse/game/GameInterface.java",
    "engine/src/main/java/org/jesse/game/model/ui/InterfaceHandler.java",
    "engine/src/main/java/org/jesse/game/model/ui/testinterfaces/TeleportInterface.java",
    "engine/src/main/java/org/jesse/plugins/renewednpc/ZenyteTeleporter.java",
    "engine/src/main/java/org/jesse/game/world/entity/player/teleportsystem/TeleportManager.java",
    "content/generic/src/main/java/org/jesse/plugins/object/ZenytePortal.java",
}
for root in roots:
    for p in pathlib.Path(root).rglob("*"):
        if p.suffix not in (".java", ".kt") or "build" in p.parts: continue
        try: text = p.read_text(encoding="utf-8")
        except Exception: continue
        if "TELEPORT_MENU" in text and str(p) not in TM_OK:
            err("SWEEP", f"unexpected TELEPORT_MENU reference in {p}")

# Legacy cs2 invocations 10001-10009 only from dying files
cs2_re = re.compile(r'sendClientScript\(1000[1-9][,)]')
for root in roots:
    for p in pathlib.Path(root).rglob("*"):
        if p.suffix not in (".java", ".kt") or "build" in p.parts: continue
        try: text = p.read_text(encoding="utf-8")
        except Exception: continue
        if cs2_re.search(text) and "teleportsystem" not in str(p) and "TeleportInterface" not in str(p):
            err("SWEEP", f"unexpected legacy cs2 10001-10009 call in {p}")

# CustomTeleport referenced only by TypeParser; free-kill structures unreferenced
for pat, allowed, tag in [
    ("CustomTeleport", {"cache/src/main/java/mgi/tools/parser/TypeParser.java",
                        "cache/src/main/java/mgi/custom/CustomTeleport.java"}, "B-REF"),
    ("NearRealityCustomCS2Packer", {"cache/src/main/java/mgi/tools/parser/TypeParser.java",
        "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomCS2Packer.kt"}, "B-REF"),
    ("CustomEnumId", {"cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/CustomEnumId.kt"}, "B-REF"),
    ("ZenytePortalStructure", {"engine/src/main/java/org/jesse/game/content/skills/magic/spells/teleports/structures/ZenytePortalStructure.java"}, "A-REF"),
    ("ZenyteTabletStructure", {"engine/src/main/java/org/jesse/game/content/skills/magic/spells/teleports/structures/ZenyteTabletStructure.java"}, "A-REF"),
    ("SpellbookOpenTeleportInterface", {"engine/src/main/kotlin/org/jesse/game/content/skills/magic/spells/teleports/SpellbookOpenTeleportInterface.kt"}, "A-REF"),
]:
    for root in roots + ["cache/src"]:
        for p in pathlib.Path(root).rglob("*"):
            if p.suffix not in (".java", ".kt") or "build" in p.parts: continue
            try: text = p.read_text(encoding="utf-8")
            except Exception: continue
            if pat in text and str(p) not in allowed:
                err(tag, f"unexpected {pat} reference in {p}")

# --- deletion targets ---
def count_files(d): return sum(1 for f in pathlib.Path(d).rglob("*") if f.is_file())
DELETIONS = [
    ("engine/src/main/java/org/jesse/game/world/entity/player/teleportsystem", 5),
    ("engine/src/main/kotlin/org/jesse/game/world/entity/player/teleportsystem", 1),
    ("engine/src/main/java/org/jesse/game/model/ui/testinterfaces/TeleportInterface.java", None),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/ZenyteTeleporter.java", None),
    ("content/generic/src/main/java/org/jesse/plugins/object/ZenytePortal.java", None),
    ("engine/src/main/kotlin/org/jesse/game/content/skills/magic/spells/teleports/SpellbookOpenTeleportInterface.kt", None),
    ("engine/src/main/java/org/jesse/game/content/skills/magic/spells/teleports/structures/ZenytePortalStructure.java", None),
    ("engine/src/main/java/org/jesse/game/content/skills/magic/spells/teleports/structures/ZenyteTabletStructure.java", None),
    ("cache/src/main/java/mgi/custom/CustomTeleport.java", None),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomCS2Packer.kt", None),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/CustomEnumId.kt", None),
    ("cache/assets/types/item/teleport_scrolls.toml", None),
    ("cache/assets/types/animation/teleport_scroll_read_animation.toml", None),
    ("cache/assets/types/graphics/zenyte_portal_teleport.toml", None),
    ("cache/assets/types/graphics/zenyte_tablet_teleport.toml", None),
    ("cache/assets/teleportation", 118),
    ("cache/assets/packed/misc/archive_3/1700", 38),
    ("cache/assets/osnr/universal_shop", 878),
]
for path, expected in DELETIONS:
    p = pathlib.Path(path)
    if not p.exists():
        err("DEL", f"MISSING: {path}"); continue
    if expected is not None:
        c = count_files(path)
        if c != expected:
            err("DEL", f"{path}: {c} files (expected {expected})")

# corrected claim: legacy cs2 blobs 10001-10009 were NEVER packed on this branch
for i in range(10001, 10010):
    if pathlib.Path(f"cache/assets/packed/misc/archive_12/{i}").exists():
        err("C-CORR", f"archive_12/{i} exists — plan assumed absent; re-derive Part C")

# --- keep-guards (live 1601 system + shared pipeline pieces) ---
KEEPS = [
    "cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/packing/TeleportsPacker.kt",
    "cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/TeleportsList.kt",
    "engine/src/main/kotlin/org/jesse/game/world/entity/player/teleports/TeleportsManager.kt",
    "engine/src/main/kotlin/org/jesse/game/world/entity/player/teleports/DestinationTeleport.kt",
    "content/interfaces/teleports/src/main/kotlin/org/jesse/plugins/interfaces/teleports/teleports.kt",
    "content/interfaces/teleports/src/main/kotlin/org/jesse/plugins/interfaces/teleports/TeleportInterfaceDialog.kt",
    "cache/assets/types/spell_items.toml",
    "cache/assets/types/component/spellbook_secondary_home_teleports.toml",
    "cache/assets/types/component/interfaces/open_teleports.toml",
    "cache/assets/types/item/tp_scrolls.toml",           # vanilla price edits — NOT ours
    "cache/assets/params/5000", "cache/assets/params/5001", "cache/assets/params/5002",
    "cache/src/main/java/mgi/custom/FramePacker.java",
    "cache/src/main/java/mgi/custom/AnimationBase.java",
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/PackerExt.kt",
    "engine/src/main/java/org/jesse/game/content/godwars/PortalTeleport.java",  # name-twin, LIVE
    "engine/src/main/java/org/jesse/game/model/ui/testinterfaces/SidePanelsResizablePaneInterface.java",
]
for k in KEEPS:
    if not pathlib.Path(k).exists():
        err("KEEP", f"missing keep-file: {k}")
for d, expected in [("cache/assets/packed/misc/archive_3/1601", 28),
                    ("cache/assets/packed/misc/archive_3/161", 98),
                    ("cache/assets/packed/misc/archive_3/164", 97),
                    ("cache/assets/packed/misc/archive_3/548", 95)]:
    if not pathlib.Path(d).exists() or count_files(d) != expected:
        err("KEEP", f"{d}: expected {expected} blob files")
sprites = list(pathlib.Path("cache/assets/sprites/spellbook_teleport").glob("*"))
if len(sprites) != 80:
    err("KEEP", f"spellbook_teleport sprites: {len(sprites)} (expected 80)")
ab = pathlib.Path("cache/src/main/java/mgi/custom/AnimationBase.java").read_text(encoding="utf-8")
for keep_entry in ["PLAYER(5000", "TRICK_HALLOWEEN_EMOTE(5002", "THANKSGIVING_TURKEY(5003",
                   "THANKSGIVING_POOF(5004", "PLAYER_ALT(5005"]:
    if keep_entry not in ab:
        err("KEEP", f"AnimationBase entry missing: {keep_entry}")
tp = pathlib.Path("cache/src/main/java/mgi/tools/parser/TypeParser.java").read_text(encoding="utf-8")
for keep_line in ["TeleportsPacker.pack();", "FramePacker.write();", "AnimationBase.pack();",
                  'GenericDataPacker.INSTANCE.packAll(cache, "assets/packed/");']:
    if tp.count(keep_line) != 1:
        err("KEEP", f"TypeParser keep-line not unique/present: {keep_line}")
ih = pathlib.Path("engine/src/main/java/org/jesse/game/model/ui/InterfaceHandler.java").read_text(encoding="utf-8")
if "GameInterface.TELEPORTS,\n" not in ih:
    err("KEEP", "WALKABLE_INTERFACES lost GameInterface.TELEPORTS (live 1601)")
gi = pathlib.Path("engine/src/main/java/org/jesse/game/GameInterface.java").read_text(encoding="utf-8")
if "TELEPORTS(1601, CENTRAL)" not in gi:
    err("KEEP", "GameInterface lost TELEPORTS(1601, CENTRAL)")
# 230 destinations => structs 10000-10229 (positional!)
dest_re = re.compile(r'^\s*"[^"]+"\(-?[A-Z0-9_]+, *[0-9]+, *[0-9]+')
ndest = 0
for f in pathlib.Path("cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/categories").glob("*.kt"):
    for ln in f.read_text(encoding="utf-8").splitlines():
        s = ln.strip()
        if s.startswith("//") or 'Teleports"(' in s: continue
        if dest_re.match(ln): ndest += 1
if ndest != 230:
    err("KEEP", f"live destination count {ndest} (expected 230; struct ids are positional)")

print("CLEAN" if fail == 0 else f"{fail} FAILURES")
sys.exit(0 if fail == 0 else 1)
```
