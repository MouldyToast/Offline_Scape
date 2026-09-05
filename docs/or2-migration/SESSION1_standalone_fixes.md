# SESSION 1 — Standalone Fixes (exact edits)

Implements PLAN §5 "Immediate standalone commits". Three encoder fixes + one
diagnosis. Each fix is a separate commit. Verify after each with
`./gradlew clean compileJava compileKotlin`.

**Pre-requisite:** `docs/or2-migration/STATE.md` must exist in the repo
(commit it alongside this file if it doesn't).

---

## Commit 1: `maxHeapSize = "3g"` for generateCache

**File:** `cache/build.gradle.kts`

**Why:** generateCache OOMs at default heap. Later commits need cache regen
for verification.

```
str_replace
path: cache/build.gradle.kts
old_str:
tasks.register<JavaExec>("generateCache") {
    group = "_nr_data"
    mainClass.set("mgi.tools.parser.TypeParser")
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("--unzip", "false")
}
new_str:
tasks.register<JavaExec>("generateCache") {
    group = "_nr_data"
    mainClass.set("mgi.tools.parser.TypeParser")
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("--unzip", "false")
    maxHeapSize = "3g"
}
```

**Commit message:** `fix(cache): add 3g heap to generateCache task`

---

## Commit 2: ItemDefinitions.encode() — write op3 (examine) + op43 (subops)

**File:** `cache/src/main/java/mgi/types/config/items/ItemDefinitions.java`

**Why:** encode() never writes opcode 3 (examine) or opcode 43 (subOptions).
Any item TypeParser re-encodes loses its cache examine text and its
right-click submenu. Proven casualty: 8 Rings of dueling lose their
teleport submenu. Fixes dueling rings on next cache gen.

### Edit 2a — op3 (examine), insert before the zoom block

```
str_replace
path: cache/src/main/java/mgi/types/config/items/ItemDefinitions.java
old_str:
        if (zoom != 2000) {
            buffer.writeByte(4);
            buffer.writeShort(zoom);
        }
new_str:
        if (examine != null) {
            buffer.writeByte(3);
            buffer.writeString(examine);
        }
        if (zoom != 2000) {
            buffer.writeByte(4);
            buffer.writeShort(zoom);
        }
```

### Edit 2b — op43 (subOptions), insert before the grandExchange block

```
str_replace
path: cache/src/main/java/mgi/types/config/items/ItemDefinitions.java
old_str:
        if (grandExchange) {
            buffer.writeByte(65);
        }
new_str:
        if (subOptions != null) {
            for (int opId = 0; opId < subOptions.length; opId++) {
                if (subOptions[opId] != null) {
                    buffer.writeByte(43);
                    buffer.writeByte(opId);
                    for (int subOpId = 0; subOpId < subOptions[opId].length; subOpId++) {
                        if (subOptions[opId][subOpId] != null) {
                            buffer.writeByte(subOpId + 1);
                            buffer.writeString(subOptions[opId][subOpId]);
                        }
                    }
                    buffer.writeByte(0);
                }
            }
        }
        if (grandExchange) {
            buffer.writeByte(65);
        }
```

**Verification (after this commit):** run `./gradlew :cache:generateCache`,
decode the packed cache items with OR2, confirm item 2552 (Ring of dueling)
has non-null subOptions (count should be 106 in packed, matching the 106 in
base — was 98 before this fix).

**Commit message:** `fix(cache): encode item op3 (examine) and op43 (subops)`

---

## Commit 3: NPCDefinitions — decode op122/op123 + encode op122/op123

**File:** `cache/src/main/java/mgi/types/config/npcs/NPCDefinitions.java`

**Why:** mgi only handles legacy opcode 111 for follower status, but rev-228
uses opcodes 122 (lowPriorityFollowerOps) and 123 (isFollower). The decoder
silently ignores both. The encoder writes op111 which doesn't exist at
rev-228. Result: all 217 vanilla pet NPCs lose their follower flags on any
re-encode, and `NPCDefinitions.isFollower` is always false.

### Edit 3a — new field declaration

```
str_replace
path: cache/src/main/java/mgi/types/config/npcs/NPCDefinitions.java
old_str:
    private boolean isFollower;
new_str:
    private boolean isFollower;
    private boolean lowPriorityFollowerOps;
```

### Edit 3b — decode cases 122 + 123, insert after case 111

```
str_replace
path: cache/src/main/java/mgi/types/config/npcs/NPCDefinitions.java
old_str:
            case 111:
                isFollower = true;
                return;
            case 114:
new_str:
            case 111:
                isFollower = true;
                return;
            case 122:
                lowPriorityFollowerOps = true;
                return;
            case 123:
                isFollower = true;
                return;
            case 114:
```

### Edit 3c — encode: replace op111 with op122 + op123

```
str_replace
path: cache/src/main/java/mgi/types/config/npcs/NPCDefinitions.java
old_str:
        if (isFollower) {
            buffer.writeByte(111);
        }
new_str:
        if (lowPriorityFollowerOps) {
            buffer.writeByte(122);
        }
        if (isFollower) {
            buffer.writeByte(123);
        }
```

**Verification:** `./gradlew clean compileJava compileKotlin` must pass. Then
run `generateCache`, decode NPCs with OR2, confirm NPC count carrying
op122/op123 in packed cache matches base (217 op122, 240 op123 at base +
NR additions).

**Commit message:** `fix(cache): decode/encode NPC op122 (lowPriorityFollowerOps) + op123 (isFollower)`

---

## Commit 4 (investigation, not a fix): 403 missing structs diagnosis

After the three fixes above:

1. Reset the cache to pristine base: `./gradlew :cache:resetCache`
2. Decode base cache structs with OR2 — record all struct IDs present → `base_struct_ids.txt`
3. Run `./gradlew :cache:generateCache`
4. Decode packed cache structs with OR2 — record all struct IDs present → `packed_struct_ids.txt`
5. Diff: find IDs in base but not in packed. Expected: ~403 IDs missing.
6. For each missing ID, determine which packer step dropped it. Prime
   suspect: the struct group being rebuilt by one of:
   - `NearRealityCustomStructsPacker.pack()` (TypeParser:208) — packs 3 structs (10500-10502)
   - `pack(cacheLowPriorityDefinitions)` (TypeParser:212-216) — calls `StructDefinitions.pack()` on TOML-parsed instances
   - `packStructs()` (TypeParser:222) — reads 55 files from `assets/structs/`
7. Record findings in `docs/or2-migration/PLAN_or2_definition_swap.md` open
   items ledger under "Packed cache LOSES vanilla definitions".

**Commit message:** `docs(or2): 403 missing structs diagnosis — [findings]`

---

## After all commits

Update `docs/or2-migration/STATE.md`:
- Tick `Standalone fixes (PLAN §5)`
- Set NEXT ACTION to: `run Claude Code session 2 (Tier 1 — Struct swap).`
- Add log entry with date

**Final commit message:** `docs(or2): tick standalone fixes, advance STATE to Tier 1`
