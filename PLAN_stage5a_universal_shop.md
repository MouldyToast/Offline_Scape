# PLAN: Stage 5a — universal shop removal

**Roadmap:** 5a — CONFIRMED KILL (checkpoint 4 closed). The original `PLAN_remove_universal_shop.md` predates this branch; this plan re-derives it against `claude/new-session-lo0m56` @ `1217ab5e` (Stages 1–4 executed). Dry-run (Part D) verified CLEAN on this tree.
**Removal order:** server code (A) → TypeParser (B) → packer → assets (C).

## 0. Scope & findings
- The universal shop's stock is a hardcoded catalog (`UniversalShopTable.kt`: 13 tables, **517** items — not 519); vanilla per-NPC shops are fully independent. Reachability was `::shops` (region-12342-gated), `::usm`, and three Emblem Trader binds. Chest object 60404 already died in Stage 4.
- **`openInterfaceToTab` ignored its tab argument** — every entry landed on the Overview; the "tabs" were cosmetic.
- **Blood Money replacement already exists**: vanilla `"Blood money Store"` (content/other/shops/Blood Money.kt, BLOOD_MONEY, STOCK_ONLY) — the Edgeville Emblem Trader's Talk-to already opens it. Machine-diff vs the univ table: identical except 4 NR-booster/box rows (32149/32154/32166/32203) present only in the univ table — **deliberately NOT transcribed** (those items die in 5c; adding rows now would need re-removal) — and 5 strictly-additive vanilla extras + one ironman-flag mismatch (MAGES_BOOK), all left as-is.
- **Bounty Hunter replacement is the one new artifact**: `content/other/shops/Bounty Hunter Rewards.kt` — 28 rows @ BH_POINTS (attribute-backed via ShopCurrencyHandler, zero new plumbing), STOCK_ONLY, house-convention stock amounts (100 gear / 1000 consumables; the literal `UnivShopItem.invoke` formula would give 10000 for all — convention preferred). All three Emblem Trader binds repointed: BH → new shop, Blood Money → "Blood money Store". Shop names are hard-unique (`Shop` ctor throws on duplicates); "Bounty Hunter Rewards" is collision-free.
- **`content/other/unused` module is live-but-dead** (auto-discovered by Gradle, zero references; 3 files incl. a stale UnivShopTable fork and an orphan PunishmentCategory) — deleted wholesale.
- Orphaned tables (Slayer/Capes/Loyalty/Vote) lose their storefront with the table file; their owning systems die in **5d** (the data survives in git history for any keeper-transcription).
- Currencies all stay (shared via ShopCurrencyHandler with Shop.java + tournament); BH_POINTS regains a storefront via the new shop.

## Part A — server code
- A-1 NEW `content/other/shops/.../Bounty Hunter Rewards.kt` (28 transcribed rows — full text in the executed commit; sellPrice −1 preserved where the univ row had none).
- A-2 `EmblemTrader.kt`: import out; "Rewards" → `player.openShop("Bounty Hunter Rewards")`.
- A-3 `EdgevilleEmblemTrader.java`: import out; "Rewards" → `Shop.get("Blood money Store", …)`, "BH Shop" → `Shop.get("Bounty Hunter Rewards", …)` (matches the file's existing Talk-to style; Shop already imported).
- A-4 `GameCommands.java`: two imports, `UniversalShopCommands.INSTANCE.register();`, and the `::shops` block.
- A-5 `GameInterface.java`: `UNIVERSAL_SHOP(5003)` + `UNIVERSAL_SHOP_INVENTORY(5007, SINGLE_TAB)` (not in any walkable/handler list).
- A-6 `GameToggles.UNIVERSAL_SHOP_FLOODGATE`. A-7 the 3 `PlayerAttributes.kt` delegates (persisted keys linger harmlessly).
- A-8 `git rm`: engine `UniversalShop.kt`, `shop/UniversalShopCategory.kt`, `universalshop/` (Interface+Commands+ManagerDialogues); core-model `universalshop/` (Table+Item+Category); `content/other/unused/` wholesale.
- A-9 `ShopBuilder.kt`: UnivShopItem import + the zero-caller `invoke` overload. A-10 `ShopScriptCompilation.kt`: the 3 universalshop defaultImports (trailing comma legal).
- A-11 batch-strip the 3 `import org.jesse.game.content.universalshop…` lines from all 274 shop files (content-match, python — filenames contain spaces/apostrophes; only Blood Money.kt lacks them; zero non-import usages verified programmatically).

## Part B — cache
`TypeParser.java`: import, `POST_PACK_UNIV_SHOP` flag, and the postPack call (B1-a/b/c). `git rm UniversalShopPacker.kt` (pure code→cache DBROW/DBTABLEINDEX generator — dbrows 5000+, dbtables 1001–1014; nothing else emits them).

## Part C — assets (652 files)
`git rm -r`: `cache/assets/cs2/universal_shop/` (24 source-doc scripts, never packed), `cache/assets/packed/universal_shop/` (588: dbrow_38 ×542, dbtable_39 ×14, enum 20002, varc 2000-2002, archive_21 indexes 1001-1014), `packed/misc/archive_3/5003/` (14) + `5007/` (1), `packed/misc/archive_12/41001…41024` (24).
**Deliberately left:** `archive_12/41025` (inside the custom block but unidentified — Stage 6), `assets/params/5003` + `5007` (params namespace, contiguous 5000-5028 block — NOT the interfaces).

## Part D — dry-run gate (validated CLEAN)
`dryrun_stage5a.py` (repo root; deleted in the execution commit): 16 FINDs byte-exact-unique; deletion manifest with counts (packed/universal_shop=588, cs2=24 dirs, 5003=14, 5007=1, archive_12 41001-41024 present, unused module = exactly 3 .kt); 274 shop files carry the imports with ZERO non-import usages; keep-guards (Blood Money.kt defines "Blood money Store", params/5003+5007 + 41025 + ShopCurrencyHandler.kt present and not deleted; new shop file absent; all 28 ItemId constants for it exist).

## Part E — gates
1 dry-run CLEAN · 2 Part A (+ new shop + import strip) → compile · 3 Part B → compile · 4 Part C → compile · 5 post-greps: `UniversalShop|UnivShop|universalshop` zero outside PROVENANCE; `UNIVERSAL_SHOP_FLOODGATE|openInterfaceToTab|POST_PACK_UNIV_SHOP` zero; `::shops|::usm` gone; keep-asserts: "Bounty Hunter Rewards" ×3 (shop file + 2 traders), "Blood money Store" ≥2, ShopCurrencyHandler intact.
6-9 local: cache regen from pristine (dbrows 5000+ and dbtables 1001-1014 no longer packed; packed/ sweep no longer includes universal_shop subtree) · `clean` + plugin scanner (**required — the NEW shop registers via plugins.dat; without rescan it throws at the trader**) · boot · in-game: `::shops`/`::usm` unknown; Emblem Trader (Edgeville 308): Talk-to rewards + "Rewards" open Blood money Store, "BH Shop" opens Bounty Hunter Rewards, buy an imbue scroll with BH points; wilderness Emblem Trader 12113 "Rewards" likewise; vanilla NPC shops unaffected; a save with old univ-shop attributes loads clean.

## Part F — save impact
None: no item defs touched; the three univ-shop attribute keys (one lingering in old saves) are unread; bountyHunterPoints/blood money balances unchanged and now spendable in the replacement shops.

## Part G — deliberately left
archive_12/41025 (Stage 6 packed-blob pass) · params 5000-5028 block (not ours) · the 4 NR-booster rows NOT transcribed (die with their items in 5c) · Slayer/Capes/Loyalty/Vote table data (git history; owning systems → 5d) · BH_POINTS enum + handler branches (live again via new shop) · "RuneSpawn Credit Packages" string (Stage 6) · PROVENANCE_nr.txt historical paths.

## Part H — roadmap bookkeeping
5a executed; the roadmap's "PLAN READY" doc was re-derived (drift: ::shop→::shops, 517 not 519 items, chest already dead via Stage 4, tab param cosmetic, Blood Money shop pre-existing). Next: **5b** (teleport keep-set formalization) or **5c** (custom items — CUSTOM_ITEM_IDS manifest first) per sequencing; 5d gains the orphaned currency tables note.

## Embedded dry-run

```python
#!/usr/bin/env python3
"""Stage 5a dry-run gate. Run from repo root: python3 dryrun_stage5a.py. Exit 0 = CLEAN."""
import sys, pathlib

fail = 0
def err(tag, msg):
    global fail
    print(f"[{tag}] {msg}"); fail += 1

FINDS = [
    ("engine/src/main/kotlin/org/jesse/game/content/bountyhunter/EmblemTrader.kt",
     "import org.jesse.game.content.universalshop.UniversalShopInterface.Companion.openInterfaceToTab\n", "A2-a"),
    ("engine/src/main/kotlin/org/jesse/game/content/bountyhunter/EmblemTrader.kt",
     '        bind("Rewards") { player: Player, _: NPC -> openInterfaceToTab(player, 9) }\n', "A2-b"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/EdgevilleEmblemTrader.java",
     "import org.jesse.game.content.universalshop.UniversalShopInterface;\n", "A3-a"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/EdgevilleEmblemTrader.java",
     '        bind("Rewards", (player, npc) -> UniversalShopInterface.openInterfaceToTab(player, 11));\n        bind("BH Shop", (player, npc) -> UniversalShopInterface.openInterfaceToTab(player, 9));\n', "A3-b"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     "import org.jesse.game.content.universalshop.UniversalShopCommands;\nimport org.jesse.game.content.universalshop.UniversalShopInterface;\n", "A4-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     "        UniversalShopCommands.INSTANCE.register();\n", "A4-b"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java",
     '        new Command(PlayerPrivilege.PLAYER, "shops", "Opens the universal shop interface.", (p, args) -> {\n            if (p.isLocked() || p.getActionManager().wasInCombatThisTick()) {\n                return;\n            }\n            UniversalShopInterface.openInterfaceToTab(p, 0);\n        });\n', "A4-c"),
    ("engine/src/main/java/org/jesse/game/GameInterface.java",
     "    COMP_PROGRESS(1618),\n\n    UNIVERSAL_SHOP(5003),\n    UNIVERSAL_SHOP_INVENTORY(5007, SINGLE_TAB),\n", "A5-a"),
    ("engine/src/main/java/org/jesse/GameToggles.java",
     "\n    public static boolean UNIVERSAL_SHOP_FLOODGATE = true;\n", "A6-a"),
    ("engine/src/main/kotlin/org/jesse/game/world/entity/player/PlayerAttributes.kt",
     'var Player.selectedUniversalShopCategory: Int by attribute("selected_universal_shop_category", 0)\nvar Player.univShopSearchActive: Boolean by attribute("univ_shop_search_active", false)\nvar Player.univShopDoubleProcess: Boolean by attribute("univShopDoubleProcess", false)\n\n', "A7-a"),
    ("scripts/shops/src/main/kotlin/org/jesse/scripts/shops/ShopBuilder.kt",
     "import org.jesse.game.content.universalshop.UnivShopItem\n", "A9-a"),
    ("scripts/shops/src/main/kotlin/org/jesse/scripts/shops/ShopBuilder.kt",
     "\n    operator fun UnivShopItem.invoke(\n        restockTimer: Int = Shop.DEFAULT_RESTOCK_TIMER,\n    ) {\n        val quantity = if(this.quantity < 100) 10000 else quantity\n        val item = JsonShop.Item(this.id, quantity, this.sellPrice, this.buyPrice, restockTimer, this.ironmanRestricted)\n        items.add(item)\n    }\n", "A9-b"),
    ("scripts/shops/src/main/kotlin/org/jesse/scripts/shops/ShopScriptCompilation.kt",
     '            "org.jesse.game.item.ids.*",\n\n            "org.jesse.game.content.universalshop.*",\n            "org.jesse.game.content.universalshop.UnivShopItem",\n            "org.jesse.game.content.universalshop.UnivShopItem.*"\n', "A10-a"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "import org.jesse.cache_tool.packing.custom.UniversalShopPacker;\n", "B1-a"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "    public static final boolean POST_PACK_UNIV_SHOP = true;\n", "B1-b"),
    ("cache/src/main/java/mgi/tools/parser/TypeParser.java",
     "        postPackEdits();\n        if (POST_PACK_UNIV_SHOP)\n            UniversalShopPacker.INSTANCE.postPack();\n", "B1-c"),
]

for path, needle, tag in FINDS:
    p = pathlib.Path(path)
    if not p.exists():
        err(tag, f"MISSING FILE: {path}"); continue
    n = p.read_text(encoding="utf-8").count(needle)
    if n != 1:
        err(tag, f"FIND matched {n} times (expected 1) in {path}")

DELETIONS = [
    "engine/src/main/kotlin/org/jesse/game/content/UniversalShop.kt",
    "engine/src/main/kotlin/org/jesse/game/content/shop/UniversalShopCategory.kt",
    "engine/src/main/kotlin/org/jesse/game/content/universalshop",          # 3 files
    "core-model/src/main/kotlin/org/jesse/game/content/universalshop",     # 3 files
    "content/other/unused",                                                 # whole dead module
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/UniversalShopPacker.kt",
    "cache/assets/cs2/universal_shop",
    "cache/assets/packed/universal_shop",
    "cache/assets/packed/misc/archive_3/5003",
    "cache/assets/packed/misc/archive_3/5007",
]
for path in DELETIONS:
    if not pathlib.Path(path).exists():
        err("DEL", f"MISSING: {path}")
for i in range(41001, 41025):
    if not pathlib.Path(f"cache/assets/packed/misc/archive_12/{i}").exists():
        err("DEL", f"MISSING: archive_12/{i}")

# counts
def count_files(d): return sum(1 for f in pathlib.Path(d).rglob("*") if f.is_file())
for d, expected in [("cache/assets/packed/universal_shop", 588), ("cache/assets/cs2/universal_shop", 24),
                    ("cache/assets/packed/misc/archive_3/5003", 14), ("cache/assets/packed/misc/archive_3/5007", 1)]:
    c = count_files(d) if pathlib.Path(d).exists() else -1
    if c != expected:
        err("ASSET", f"{d}: {c} files (expected {expected})")
if len(list(pathlib.Path("content/other/unused").rglob("*.kt"))) != 3:
    err("ASSET", "content/other/unused: expected exactly 3 .kt files")

# shop-file import strip preconditions
SHOPS = pathlib.Path("content/other/shops/src/main/kotlin/org/jesse/plugins/shops")
IMP = "import org.jesse.game.content.universalshop"
with_imports = [f for f in SHOPS.glob("*.kt") if IMP in f.read_text(encoding="utf-8")]
if len(with_imports) != 274:
    err("IMPORTS", f"{len(with_imports)} shop files carry universalshop imports (expected 274)")
for f in with_imports[:0]:
    pass
# no non-import usages anywhere in shops dir
for f in SHOPS.glob("*.kt"):
    for ln in f.read_text(encoding="utf-8").splitlines():
        if ("UnivShop" in ln or "UniversalShop" in ln) and not ln.strip().startswith("import "):
            err("IMPORTS", f"non-import universalshop usage in {f.name}: {ln.strip()}")

# keep-guards
for keep in [
    "content/other/shops/src/main/kotlin/org/jesse/plugins/shops/Blood Money.kt",
    "cache/assets/params/5003", "cache/assets/params/5007",
    "cache/assets/packed/misc/archive_12/41025",
    "engine/src/main/kotlin/org/jesse/game/content/shop/ShopCurrencyHandler.kt",
]:
    if not pathlib.Path(keep).exists():
        err("KEEP", f"expected keep missing: {keep}")
    if keep in DELETIONS:
        err("KEEP", f"keep listed for deletion: {keep}")
bm = pathlib.Path("content/other/shops/src/main/kotlin/org/jesse/plugins/shops/Blood Money.kt").read_text(encoding="utf-8")
if '"Blood money Store"' not in bm:
    err("KEEP", 'Blood Money.kt does not define "Blood money Store"')
if pathlib.Path("content/other/shops/src/main/kotlin/org/jesse/plugins/shops/Bounty Hunter Rewards.kt").exists():
    err("NEW", "Bounty Hunter Rewards.kt already exists")
# new-shop item constants must exist in ItemId.kt
itemid = pathlib.Path("core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt").read_text(encoding="utf-8")
for const in ["ESOTERIC_EMBLEM_TIER_1", "TARGET_TELEPORT", "BOUNTY_TELEPORT_SCROLL", "VESTAS_LONGSWORD_BH",
              "STATIUSS_WARHAMMER_BH", "MORRIGANS_THROWING_AXE_BH", "MORRIGANS_JAVELIN_BH", "ZURIELS_STAFF_BH",
              "VESTAS_CHAINBODY_27831", "VESTAS_PLATESKIRT_27832", "STATIUSS_FULL_HELM_27833",
              "STATIUSS_PLATEBODY_27834", "STATIUSS_PLATELEGS_27835", "MORRIGANS_COIF_27836",
              "MORRIGANS_LEATHER_BODY_27837", "MORRIGANS_LEATHER_CHAPS_27838", "ZURIELS_HOOD_27839",
              "ZURIELS_ROBE_TOP_27840", "ZURIELS_ROBE_BOTTOM_27841", "DARK_BOW_IMBUE_SCROLL",
              "BARRELCHEST_ANCHOR_IMBUE_SCROLL", "DRAGON_MACE_IMBUE_SCROLL", "DRAGON_LONGSWORD_IMBUE_SCROLL",
              "ABYSSAL_DAGGER_IMBUE_SCROLL", "BOUNTY_HUNTER_ORNAMENT_KIT", "ELDER_MAUL_ORNAMENT_KIT",
              "HEAVY_BALLISTA_ORNAMENT_KIT", "ELDER_CHAOS_ROBES_ORNAMENT_KIT"]:
    if f"const val {const} = " not in itemid:
        err("NEW", f"ItemId constant missing for new shop: {const}")

sys.exit(1 if fail else print("CLEAN") or 0)
```
