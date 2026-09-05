# SESSION 2 — Tier 1: Struct Swap (exact edits)

Implements PLAN §6.1. Swaps all 16 server-module files from
`mgi.types.config.StructDefinitions` to OR2 `StructType` via `Or2Defs`.
One commit, verified with `./gradlew clean compileJava compileKotlin`.

**Non-goals:** No RSCM usage, no TypeParser/cache-module changes, no enum
edits (even in mixed files), no removal of mgi struct loading.

---

## Step 0 — Dependency wiring (3 edits)

### 0a. `settings.gradle.kts` — add OR2 + jitpack Maven repos

```
str_replace
path: settings.gradle.kts
old_str:
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven(url = "https://maven.rsps.cloud/")
        maven(url = "https://repo.runelite.net/") {
            content {
                includeGroup("net.runelite")
            }
        }
    }
}
new_str:
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven(url = "https://maven.rsps.cloud/")
        maven(url = "https://repo.runelite.net/") {
            content {
                includeGroup("net.runelite")
            }
        }
        maven(url = "https://raw.githubusercontent.com/OpenRune/hosting/master")
        maven(url = "https://jitpack.io")
    }
}
```

### 0b. `gradle/libs.versions.toml` — add OR2 library

Insert after the last entry in `[libraries]`. Find a line near the end of
the libraries section and insert after it. The exact anchor depends on
what's last — use this to insert after the hotswap-agent line (which is
currently last in the versions section but near the end of libraries):

```
str_replace
path: gradle/libs.versions.toml
old_str:
hotswap-agent = { module = "org.hotswapagent:hotswap-agent-core", version.ref = "hotswap-agent" }
new_str:
hotswap-agent = { module = "org.hotswapagent:hotswap-agent-core", version.ref = "hotswap-agent" }

or2 = { module = "dev.or2:all", version = "2.4.17" }
```

### 0c. `core/build.gradle.kts` — add `api(libs.or2)` after `api(projects.cache)`

```
str_replace
path: core/build.gradle.kts
old_str:
    api(projects.cache)
    api(projects.coreModel)
new_str:
    api(projects.cache)
    api(libs.or2)
    api(projects.coreModel)
```

**Verify:** `./gradlew clean compileJava compileKotlin` — should resolve
the OR2 artifact and compile cleanly with no source changes yet.

---

## Step 1 — Boot wiring (1 edit)

**File:** `core/src/main/java/com/zenyte/game/GameLoader.java`

Insert `Or2Defs.init(...)` after the existing mgi definitions load. Find
the line `CacheManager.loadDefinitions(pool, false);` and add after it:

```
str_replace
path: core/src/main/java/com/zenyte/game/GameLoader.java
old_str:
        CacheManager.loadDefinitions(pool, false);
new_str:
        CacheManager.loadDefinitions(pool, false);
        com.zenyte.game.cache.Or2Defs.init("./cache/data/cache/");
```

---

## Step 2 — New file: `Or2Defs.kt`

Create this file exactly as written:

```
create_file
path: core/src/main/kotlin/com/zenyte/game/cache/Or2Defs.kt
```

```kotlin
package com.zenyte.game.cache

import dev.openrune.OsrsCacheProvider
import dev.openrune.cache.CacheManager
import dev.openrune.definition.type.ParamType
import dev.openrune.definition.type.StructType
import dev.openrune.filesystem.Cache
import java.nio.file.Path

/** Boot-time loader for OR2 definitions + mgi-parity param-default helpers. */
object Or2Defs {

    lateinit var params: Map<Int, ParamType>
        private set

    @JvmStatic
    fun init(cachePath: String) {
        val cache = Cache.load(Path.of(cachePath))
        CacheManager.init(OsrsCacheProvider(cache, 228))
        val map = mutableMapOf<Int, ParamType>()
        OsrsCacheProvider.ParamDecoder(228).load(cache, map)
        params = map
    }

    /** mgi StructDefinitions.get parity: throws if id has no struct. */
    @JvmStatic
    fun struct(id: Int): StructType =
        requireNotNull(CacheManager.getStruct(id)) { "No struct $id" }

    @JvmStatic
    fun structOrNull(id: Int): StructType? = CacheManager.getStruct(id)
}

/* mgi getParamAs* parity: absent key falls back to the PARAM DEFINITION's default
 * (mgi StructDefinitions.java:90–131), not a flat -1/"" like StructType.getInt. */
fun StructType.paramInt(key: Int): Int =
    params?.get(key) as? Int ?: requireNotNull(Or2Defs.params[key]) { "No param $key" }.defaultInt

fun StructType.paramBool(key: Int): Boolean = paramInt(key) == 1

fun StructType.paramString(key: Int): String =
    params?.get(key) as? String
        ?: requireNotNull(Or2Defs.params[key]) { "No param $key" }.defaultString ?: ""
```

**Note for Java callers:** `Or2Defs.struct(id)` and
`Or2DefsKt.paramInt(struct, key)` / `Or2DefsKt.paramBool(...)` /
`Or2DefsKt.paramString(...)`.

Do NOT map `getParamAsInt` to `StructType.getInt` — different default
semantics (see comment above).

---

## Step 3 — Callsite edits (16 files)

Mechanical patterns:
- `StructDefinitions.get(id)` → `Or2Defs.struct(id)` (Java+Kotlin)
- `Objects.requireNonNull(StructDefinitions.get(id))` → `Or2Defs.struct(id)` (requireNonNull is redundant — Or2Defs.struct throws)
- Type `StructDefinitions` in fields/params/locals → `StructType`
- `.getParamAsInt(k)` → `Or2DefsKt.paramInt(s, k)` (Java) / `.paramInt(k)` (Kotlin)
- `.getParamAsBoolean(k)` → `Or2DefsKt.paramBool(s, k)` (Java) / `.paramBool(k)` (Kotlin)
- `.getParamAsString(k)` → `Or2DefsKt.paramString(s, k)` (Java) / `.paramString(k)` (Kotlin)
- Remove `import mgi.types.config.StructDefinitions`; add needed OR2/Or2Defs imports
- Files that ALSO import EnumDefinitions/Enums/IntEnum/StringEnum: keep those mgi imports (Tier 2)

**Import note for Java files:** `dev.openrune.cache.CacheManager` must not
collide with `com.zenyte.CacheManager`. Check each file — if it imports
`com.zenyte.CacheManager`, use the fully qualified name
`dev.openrune.cache.CacheManager` in code. In practice only
StructExtractor.java needs this care.

---

### File 1: LoyaltyTitleShop.kt
`core/src/main/kotlin/com/near_reality/game/model/ui/loyaltytitles/LoyaltyTitleShop.kt`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions
new_str:
import com.zenyte.game.cache.Or2Defs
import com.zenyte.game.cache.paramInt
import com.zenyte.game.cache.paramString
import dev.openrune.definition.type.StructType
```

```
str_replace
old_str:
    private fun Player.getSelectedTitle(): StructDefinitions? =
        temporaryAttributes.getOrDefault("selectedTitle", null) as StructDefinitions?
new_str:
    private fun Player.getSelectedTitle(): StructType? =
        temporaryAttributes.getOrDefault("selectedTitle", null) as StructType?
```

```
str_replace
old_str:
        lateinit var loyaltyTitles: List<StructDefinitions>

        @JvmStatic
        @Subscribe
        fun onServerLaunch(ev: ServerLaunchEvent) {
            var id = NONE_ID
            loyaltyTitles = mutableListOf<StructDefinitions>()
            repeat(InventoryDefinitions.get(2500)?.size ?: 0) {
                loyaltyTitles += StructDefinitions.get(id++)
            }
new_str:
        lateinit var loyaltyTitles: List<StructType>

        @JvmStatic
        @Subscribe
        fun onServerLaunch(ev: ServerLaunchEvent) {
            var id = NONE_ID
            loyaltyTitles = mutableListOf<StructType>()
            repeat(InventoryDefinitions.get(2500)?.size ?: 0) {
                loyaltyTitles += Or2Defs.struct(id++)
            }
```

```
str_replace
old_str:
        private fun getTitleStructDefinition(titleName: String): StructDefinitions? {
new_str:
        private fun getTitleStructDefinition(titleName: String): StructType? {
```

```
str_replace
old_str:
        private fun getTitleNameByDefinitions(def: StructDefinitions?): String {
new_str:
        private fun getTitleNameByDefinitions(def: StructType?): String {
```

---

### File 2: Challenge.kt
`core/src/main/kotlin/com/near_reality/game/content/challenges/Challenge.kt`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions
import java.util.function.Function

class Challenge<T>(
    val structId: Int,
    val name: String,
    @field:Transient val function: Function<T, Int>,
    @field:Transient val maxCount: Int = StructDefinitions.get(structId).getParamAsInt(5011)
)
new_str:
import com.zenyte.game.cache.Or2Defs
import com.zenyte.game.cache.paramInt
import java.util.function.Function

class Challenge<T>(
    val structId: Int,
    val name: String,
    @field:Transient val function: Function<T, Int>,
    @field:Transient val maxCount: Int = Or2Defs.struct(structId).paramInt(5011)
)
```

---

### File 3: SettingCategory.java
`core/src/main/java/com/zenyte/game/model/ui/testinterfaces/advancedsettings/SettingCategory.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
new_str:
import com.zenyte.game.cache.Or2Defs;
import com.zenyte.game.cache.Or2DefsKt;
import dev.openrune.definition.type.StructType;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
```

```
str_replace
old_str:
    SettingCategory(StructDefinitions struct) {
        this.id = struct.getParamAsInt(SETTINGS_CATEGORY_ID_PARAM);
        this.name = struct.getParamAsString(SETTINGS_CATEGORY_NAME_PARAM);
        final IntEnum settings = EnumDefinitions.getIntEnum(struct.getParamAsInt(SETTINGS_CATEGORY_SETTINGS_LIST_PARAM));
new_str:
    SettingCategory(StructType struct) {
        this.id = Or2DefsKt.paramInt(struct, SETTINGS_CATEGORY_ID_PARAM);
        this.name = Or2DefsKt.paramString(struct, SETTINGS_CATEGORY_NAME_PARAM);
        final IntEnum settings = EnumDefinitions.getIntEnum(Or2DefsKt.paramInt(struct, SETTINGS_CATEGORY_SETTINGS_LIST_PARAM));
```

```
str_replace
old_str:
            final StructDefinitions struct = StructDefinitions.get(element);
            list.add(new Setting(struct));
new_str:
            final StructType struct = Or2Defs.struct(element);
            list.add(new Setting(struct));
```

---

### File 4: Settings.java
`core/src/main/java/com/zenyte/game/model/ui/testinterfaces/advancedsettings/Settings.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
            final StructDefinitions struct = StructDefinitions.get(element);
            categories.add(new SettingCategory(struct));
new_str:
            final StructType struct = Or2Defs.struct(element);
            categories.add(new SettingCategory(struct));
```

---

### File 5: Setting.java
`core/src/main/java/com/zenyte/game/model/ui/testinterfaces/advancedsettings/Setting.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2DefsKt;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
    Setting(StructDefinitions struct) {
        this.structId = struct.getId();
        this.id = struct.getParamAsInt(ID_PARAM);
        this.typeId = struct.getParamAsInt(TYPE_PARAM);
        this.name = struct.getParamAsString(NAME_PARAM);
        this.searchKeywords = struct.getParamAsString(SEARCH_KEYWORDS_PARAM);
        this.sliderTransmitted = struct.getParamAsBoolean(IS_SLIDER_TRANSMITTED_PARAM);
        this.sliderNotchCount = struct.getParamAsInt(SLIDER_NOTCH_COUNT_PARAM);
        this.desktop = struct.getParamAsBoolean(IS_DESKTOP_PARAM);
        this.mobile = struct.getParamAsBoolean(IS_MOBILE_PARAM);
        this.ironman = struct.getParamAsBoolean(IS_IRONMAN_PARAM);
        this.nonIronman = struct.getParamAsBoolean(IS_NON_IRONMAN_PARAM);
        this.hasCustomRequirements = struct.getParamAsBoolean(HAS_CUSTOM_REQUIREMENTS_PARAM);
        this.preRequirementsEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_ENUM_ID_PARAM);
        this.preRequirementsValuesEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM);
        this.inversedPreRequirementsEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM);
        this.inversedPreRequirementsValuesEnumId = struct.getParamAsInt(PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM);
        this.toggleInversed = struct.getParamAsBoolean(HAS_TOGGLE_INVERSED_PARAM);
        this.chooseTransmit = struct.getParamAsBoolean(CHOOSE_TRANSMIT_PARAM);
        this.mobileName = struct.getParamAsString(MOBILE_NAME_PARAM);
        this.description = struct.getParamAsString(DESCRIPTION_PARAM);
        this.keyBindSprite = struct.getParamAsInt(KEYBIND_SPITE_PARAM);
        this.keyBindSpriteCoordGrid = struct.getParamAsInt(KEYBIND_SPRITE_SIZE_COORDGRID_PARAM);
        this.sliderSectors = struct.getParamAsInt(SLIDER_SECTORS_PARAM);
        this.sliderSectorsTextEnumId = struct.getParamAsInt(SLIDER_SECTOR_TEXT_ENUM_ID_PARAM);
        this.sliderCustomOnOpScript = struct.getParamAsBoolean(SLIDER_CUSTOM_ON_OP_PARAM);
        this.sliderCustomSetPos = struct.getParamAsBoolean(SLIDER_CUSTOM_SETPOS_PARAM);
        this.sliderDraggable = struct.getParamAsBoolean(IS_SLIDER_DRAGGABLE_PARAM);
        this.sliderDeadZone = struct.getParamAsInt(SLIDER_DEADZONE_PARAM);
        this.sliderDeadTime = struct.getParamAsInt(SLIDER_DEADTIME_PARAM);
        this.inputSingular = struct.getParamAsString(SLIDER_INPUT_SINGULAR_PARAM);
        this.inputPlural = struct.getParamAsString(SLIDER_INPUT_PLURAL_PARAM);
        this.inputZero = struct.getParamAsString(SLIDER_INPUT_ZERO_PARAM);
        this.opCheckerMessage = struct.getParamAsString(SLIDER_OP_CHECKER_MESSAGE_PARAM);
        this.mobileOpCheckerMessage = struct.getParamAsString(SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM);
        this.collapsibleInfobox = struct.getParamAsBoolean(HAS_COLLAPSIBLE_INFOBOX_PARAM);
        this.hideDescription = struct.getParamAsBoolean(HIDE_DESCRIPTION_PARAM);
        this.enhancedClientOnly = struct.getParamAsBoolean(IS_ENHANCED_CLIENT_PARAM);
        this.customNameExtraText = struct.getParamAsBoolean(CUSTOM_NAME_EXTRA_TEXT_PARAM);
        this.mobileAlwaysEnabled = struct.getParamAsBoolean(IS_MOBILE_ALWAYS_ENABLED_PARAM);
        this.hasCustomCheck = struct.getParamAsBoolean(HAS_CUSTOM_CHECK_PARAM);
        this.defaultColour = struct.getParamAsInt(DEFAULT_COLOUR_PARAM);
        this.nonDesktopOnly = struct.getParamAsBoolean(IS_NON_DESKTOP_ONLY_PARAM);
        this.leagueWorldOnly = struct.getParamAsBoolean(IS_LEAGUE_WORLD_ONLY_PARAM);
        this.leagueWorldEnhancedClientOnly = struct.getParamAsBoolean(IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM);
        this.dropdownEntriesEnumId = struct.getParamAsInt(DROPDOWN_ENTRIES_PARAM);
        this.mobileDropDownEntriesEnumId = struct.getParamAsInt(DROPDOWN_ENTRIES_MOBILE_PARAM);
new_str:
    Setting(StructType struct) {
        this.structId = struct.getId();
        this.id = Or2DefsKt.paramInt(struct, ID_PARAM);
        this.typeId = Or2DefsKt.paramInt(struct, TYPE_PARAM);
        this.name = Or2DefsKt.paramString(struct, NAME_PARAM);
        this.searchKeywords = Or2DefsKt.paramString(struct, SEARCH_KEYWORDS_PARAM);
        this.sliderTransmitted = Or2DefsKt.paramBool(struct, IS_SLIDER_TRANSMITTED_PARAM);
        this.sliderNotchCount = Or2DefsKt.paramInt(struct, SLIDER_NOTCH_COUNT_PARAM);
        this.desktop = Or2DefsKt.paramBool(struct, IS_DESKTOP_PARAM);
        this.mobile = Or2DefsKt.paramBool(struct, IS_MOBILE_PARAM);
        this.ironman = Or2DefsKt.paramBool(struct, IS_IRONMAN_PARAM);
        this.nonIronman = Or2DefsKt.paramBool(struct, IS_NON_IRONMAN_PARAM);
        this.hasCustomRequirements = Or2DefsKt.paramBool(struct, HAS_CUSTOM_REQUIREMENTS_PARAM);
        this.preRequirementsEnumId = Or2DefsKt.paramInt(struct, PRE_REQUIREMENTS_ENUM_ID_PARAM);
        this.preRequirementsValuesEnumId = Or2DefsKt.paramInt(struct, PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM);
        this.inversedPreRequirementsEnumId = Or2DefsKt.paramInt(struct, PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM);
        this.inversedPreRequirementsValuesEnumId = Or2DefsKt.paramInt(struct, PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM);
        this.toggleInversed = Or2DefsKt.paramBool(struct, HAS_TOGGLE_INVERSED_PARAM);
        this.chooseTransmit = Or2DefsKt.paramBool(struct, CHOOSE_TRANSMIT_PARAM);
        this.mobileName = Or2DefsKt.paramString(struct, MOBILE_NAME_PARAM);
        this.description = Or2DefsKt.paramString(struct, DESCRIPTION_PARAM);
        this.keyBindSprite = Or2DefsKt.paramInt(struct, KEYBIND_SPITE_PARAM);
        this.keyBindSpriteCoordGrid = Or2DefsKt.paramInt(struct, KEYBIND_SPRITE_SIZE_COORDGRID_PARAM);
        this.sliderSectors = Or2DefsKt.paramInt(struct, SLIDER_SECTORS_PARAM);
        this.sliderSectorsTextEnumId = Or2DefsKt.paramInt(struct, SLIDER_SECTOR_TEXT_ENUM_ID_PARAM);
        this.sliderCustomOnOpScript = Or2DefsKt.paramBool(struct, SLIDER_CUSTOM_ON_OP_PARAM);
        this.sliderCustomSetPos = Or2DefsKt.paramBool(struct, SLIDER_CUSTOM_SETPOS_PARAM);
        this.sliderDraggable = Or2DefsKt.paramBool(struct, IS_SLIDER_DRAGGABLE_PARAM);
        this.sliderDeadZone = Or2DefsKt.paramInt(struct, SLIDER_DEADZONE_PARAM);
        this.sliderDeadTime = Or2DefsKt.paramInt(struct, SLIDER_DEADTIME_PARAM);
        this.inputSingular = Or2DefsKt.paramString(struct, SLIDER_INPUT_SINGULAR_PARAM);
        this.inputPlural = Or2DefsKt.paramString(struct, SLIDER_INPUT_PLURAL_PARAM);
        this.inputZero = Or2DefsKt.paramString(struct, SLIDER_INPUT_ZERO_PARAM);
        this.opCheckerMessage = Or2DefsKt.paramString(struct, SLIDER_OP_CHECKER_MESSAGE_PARAM);
        this.mobileOpCheckerMessage = Or2DefsKt.paramString(struct, SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM);
        this.collapsibleInfobox = Or2DefsKt.paramBool(struct, HAS_COLLAPSIBLE_INFOBOX_PARAM);
        this.hideDescription = Or2DefsKt.paramBool(struct, HIDE_DESCRIPTION_PARAM);
        this.enhancedClientOnly = Or2DefsKt.paramBool(struct, IS_ENHANCED_CLIENT_PARAM);
        this.customNameExtraText = Or2DefsKt.paramBool(struct, CUSTOM_NAME_EXTRA_TEXT_PARAM);
        this.mobileAlwaysEnabled = Or2DefsKt.paramBool(struct, IS_MOBILE_ALWAYS_ENABLED_PARAM);
        this.hasCustomCheck = Or2DefsKt.paramBool(struct, HAS_CUSTOM_CHECK_PARAM);
        this.defaultColour = Or2DefsKt.paramInt(struct, DEFAULT_COLOUR_PARAM);
        this.nonDesktopOnly = Or2DefsKt.paramBool(struct, IS_NON_DESKTOP_ONLY_PARAM);
        this.leagueWorldOnly = Or2DefsKt.paramBool(struct, IS_LEAGUE_WORLD_ONLY_PARAM);
        this.leagueWorldEnhancedClientOnly = Or2DefsKt.paramBool(struct, IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM);
        this.dropdownEntriesEnumId = Or2DefsKt.paramInt(struct, DROPDOWN_ENTRIES_PARAM);
        this.mobileDropDownEntriesEnumId = Or2DefsKt.paramInt(struct, DROPDOWN_ENTRIES_MOBILE_PARAM);
```

---

### File 6: CollectionLogInterface.java
`core/src/main/java/com/zenyte/game/world/entity/player/collectionlog/CollectionLogInterface.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import com.zenyte.game.cache.Or2DefsKt;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
            final StructDefinitions struct = Objects.requireNonNull(StructDefinitions.get(type.struct()));
new_str:
            final StructType struct = Or2Defs.struct(type.struct());
```

(This pattern appears at lines 78, 153, 185. The old_str matches all three
only if the surrounding lines differ. Apply each occurrence — there are
three `Objects.requireNonNull(StructDefinitions.get(type.struct()))` and
one `Objects.requireNonNull(StructDefinitions.get(structId))`. Each must
be found by its unique surrounding context.)

```
str_replace
old_str:
        final var struct = Objects.requireNonNull(StructDefinitions.get(structId));
new_str:
        final var struct = Or2Defs.struct(structId);
```

```
str_replace
old_str:
    private IntArrayList getOptions(StructDefinitions struct, CLCategoryType type, int subCategory) {
new_str:
    private IntArrayList getOptions(StructType struct, CLCategoryType type, int subCategory) {
```

```
str_replace
old_str:
    private static int getStructParam(StructDefinitions struct, int param) {
new_str:
    private static int getStructParam(StructType struct, int param) {
```

Line 275 — the logging lambda. The `StructDefinitions.get(claiming)` inside
the lambda also needs swapping:

```
str_replace
old_str:
StructDefinitions.get(claiming).getParamAsString(STRUCT_POINTER_SUB_ENUM_CAT_NAME)
new_str:
Or2DefsKt.paramString(Or2Defs.struct(claiming), STRUCT_POINTER_SUB_ENUM_CAT_NAME)
```

---

### File 7: CollectionLog.java
`core/src/main/java/com/zenyte/game/world/entity/player/collectionlog/CollectionLog.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
            final StructDefinitions struct = Objects.requireNonNull(StructDefinitions.get(category.struct()));
new_str:
            final StructType struct = Or2Defs.struct(category.struct());
```

```
str_replace
old_str:
                final StructDefinitions bossStruct = Objects.requireNonNull(StructDefinitions.get(bossStructId));
new_str:
                final StructType bossStruct = Or2Defs.struct(bossStructId);
```

---

### File 8: CollectionLogRewardHandler.java
`core/src/main/java/com/zenyte/game/world/entity/player/collectionlog/CollectionLogRewardHandler.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
        final StructDefinitions sub = Objects.requireNonNull(StructDefinitions.get(struct));
new_str:
        final StructType sub = Or2Defs.struct(struct);
```

---

### File 9: CALogBossInterface.java
`core/src/main/java/com/zenyte/game/world/entity/player/calog/CALogBossInterface.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
			final StructDefinitions subCategoryStruct = Objects.requireNonNull(StructDefinitions.get(v));
new_str:
			final StructType subCategoryStruct = Or2Defs.struct(v);
```

---

### File 10: Player.java
`core/src/main/java/com/zenyte/game/world/entity/player/Player.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import com.zenyte.game.cache.Or2DefsKt;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
    public StructDefinitions getTitle() {
        final var titleId = getNumericAttributeOrDefault("title", LoyaltyTitleShop.NONE_ID).intValue();
        return StructDefinitions.get(titleId);
    }
new_str:
    public StructType getTitle() {
        final var titleId = getNumericAttributeOrDefault("title", LoyaltyTitleShop.NONE_ID).intValue();
        return Or2Defs.struct(titleId);
    }
```

The `getGenderTitleText` method (lines 4558-4561) calls
`title.getParamAsString(...)`. Since `title` is now `StructType`, these
need the extension function:

```
str_replace
old_str:
            return title.getParamAsString(isMale ? 11825 : 11827);
        } else {
            return title.getParamAsString(isMale ? 11826 : 11828);
new_str:
            return Or2DefsKt.paramString(title, isMale ? 11825 : 11827);
        } else {
            return Or2DefsKt.paramString(title, isMale ? 11826 : 11828);
```

---

### File 11: AdventDay.java
`core/src/main/java/com/zenyte/game/content/advent/AdventDay.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import com.zenyte.game.cache.Or2DefsKt;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
		StructDefinitions struct = StructDefinitions.get(structId);
		this.countToComplete = struct.getParamAsInt(5026);
		this.reward = new Item(struct.getParamAsInt(5027), struct.getParamAsInt(5028));
new_str:
		StructType struct = Or2Defs.struct(structId);
		this.countToComplete = Or2DefsKt.paramInt(struct, 5026);
		this.reward = new Item(Or2DefsKt.paramInt(struct, 5027), Or2DefsKt.paramInt(struct, 5028));
```

---

### File 12: StructExtractor.java
`core/src/main/java/com/zenyte/tools/StructExtractor.java`

This file iterates `StructDefinitions.definitions` (the mgi static array).
Replace with OR2's `CacheManager.getStructs()`. **Import note:**
this file is in `com.zenyte.tools` — no risk of `CacheManager` collision
since it doesn't import `com.zenyte.CacheManager`.

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import dev.openrune.cache.CacheManager;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
            for (final StructDefinitions t : StructDefinitions.definitions) {
                if (t == null) {
                    continue;
                }
                try {
                    writer.write("[" + t.getId() + "]\r\n");
                    writer.write("val=" + t.printParams() + "\r\n");
new_str:
            for (final StructType t : CacheManager.INSTANCE.getStructs().values()) {
                try {
                    writer.write("[" + t.getId() + "]\r\n");
                    writer.write("val=" + (t.getParams() != null ? t.getParams().toString() : "") + "\r\n");
```

---

### File 13: IronmanGroupTasks.kt
`content/other/group-ironman/src/main/kotlin/com/near_reality/content/group_ironman/player/IronmanGroupTasks.kt`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions
new_str:
import com.zenyte.game.cache.Or2Defs
import com.zenyte.game.cache.paramInt
```

```
str_replace
old_str:
		return StructDefinitions.get(Enums.GIM_STORAGE_REQS.getValue(enumIndex).orElseThrow(Enums.exception())).getParamAsInt(1547)
new_str:
		return Or2Defs.struct(Enums.GIM_STORAGE_REQS.getValue(enumIndex).orElseThrow(Enums.exception())).paramInt(1547)
```

---

### File 14: IronmanGroupChallengesInterface.kt
`content/other/group-ironman/src/main/kotlin/com/near_reality/content/group_ironman/challenges/IronmanGroupChallengesInterface.kt`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions
new_str:
import com.zenyte.game.cache.Or2Defs
import com.zenyte.game.cache.paramInt
```

```
str_replace
old_str:
                    tasksFinishedFully = tasksFinishedFully or (1 shl StructDefinitions.get(struct).getParamAsInt(5014))
                } else if (finishedCount > 0) {
                    tasksFinished = tasksFinished or (1 shl StructDefinitions.get(struct).getParamAsInt(5014))
new_str:
                    tasksFinishedFully = tasksFinishedFully or (1 shl Or2Defs.struct(struct).paramInt(5014))
                } else if (finishedCount > 0) {
                    tasksFinished = tasksFinished or (1 shl Or2Defs.struct(struct).paramInt(5014))
```

---

### File 15: IronmanGroupChallenges.kt
`content/other/group-ironman/src/main/kotlin/com/near_reality/content/group_ironman/challenges/IronmanGroupChallenges.kt`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions
new_str:
import com.zenyte.game.cache.Or2Defs
```

```
str_replace
old_str:
    private fun collectCollectionLogItems(structId: Int): List<Int> =
        StructDefinitions.get(structId)
new_str:
    private fun collectCollectionLogItems(structId: Int): List<Int> =
        Or2Defs.struct(structId)
```

---

### File 16: InvocationType.java
`content/raids/toa/src/main/java/com/zenyte/game/content/tombsofamascut/InvocationType.java`

```
str_replace
old_str:
import mgi.types.config.StructDefinitions;
new_str:
import com.zenyte.game.cache.Or2Defs;
import com.zenyte.game.cache.Or2DefsKt;
import dev.openrune.definition.type.StructType;
```

```
str_replace
old_str:
			final StructDefinitions definitions = StructDefinitions.get(VALUES[indx].structId);
			INDICES[indx] = definitions.getParamAsInt(STRUCT_ID_PARAM);
			CATAGORIES[indx] = InvocationCategoryType.VALUES[definitions.getParamAsInt(STRUCT_CATEGORY_PARAM) - 3];
			LEVEL_MODIFIERS[indx] = definitions.getParamAsInt(STRUCT_LEVEL_MODIFIER_PARAM);
new_str:
			final StructType definitions = Or2Defs.struct(VALUES[indx].structId);
			INDICES[indx] = Or2DefsKt.paramInt(definitions, STRUCT_ID_PARAM);
			CATAGORIES[indx] = InvocationCategoryType.VALUES[Or2DefsKt.paramInt(definitions, STRUCT_CATEGORY_PARAM) - 3];
			LEVEL_MODIFIERS[indx] = Or2DefsKt.paramInt(definitions, STRUCT_LEVEL_MODIFIER_PARAM);
```

---

## Step 4 — Verification

1. `./gradlew clean compileJava compileKotlin` — must pass with zero errors.
2. Grep confirms: `grep -rn "import mgi.types.config.StructDefinitions" core/ content/`
   should return **zero results**.
3. Do NOT remove `StructDefinitions` from
   `Definitions.serverLowPriorityDefinitions` — that's the tier's closing
   step once a follow-up grep confirms zero remaining server-module readers.

---

## After verification

Update `docs/or2-migration/STATE.md`:
- Tick `Tier 1 — Struct`
- Set NEXT ACTION to: `grep confirms zero server-module StructDefinitions readers → remove struct from serverLowPriorityDefinitions (Tier 1 closing step).`
- Add log entry

**Commit message:** `refactor(or2): Tier 1 — swap StructDefinitions to OR2 StructType via Or2Defs (16 files)`
