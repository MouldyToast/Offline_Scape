# Rev-240 Migration: Remaining Work

> Status as of 2026-09-18. The protocol layer compiles against RSProt
> osrs-240-api and the IfSetEventsV2 bitmask bug is fixed. This document
> tracks everything that is NOT yet done.

---

## P0 -- Bugs That Will Cause Wrong Behavior

### ~~Camera V3 Coordinate Mismatch~~ FIXED

Build-area coords are now converted to absolute via `buildAreaOriginX/Z()`
in all 4 camera methods. Commit `0e5ee28a`.

### ~~Info Protocol: getPackets() Called 3x Per Tick~~ FIXED

`getPackets()` result is now cached per tick via `getOrComputeInfoPackets()`
and cleared after the last info send. Commit `0e5ee28a`.

---

## P1 -- Interface Component IDs

### ~~43 Testinterface Files With Potentially Stale IDs~~ VERIFIED SAFE

**Analysis (2026-09-18):** Cross-referencing all 43 files' component IDs
against both the rev-228 and rev-240 component.rscm symbol tables confirms:

- **Named component IDs are 100% stable** across revisions (only 1 of
  25,000+ named components changed: `seed_vault_deposit:inv` 1→2).
- **41 of 43** interfaces have identical max component counts — no layout
  change at all.
- **2 of 43** (`combat_interface`, `bankside`) gained new components at
  the END — existing IDs at positions used by the handlers are unchanged.
- The 24 files updated on the rev-240 branch were changed because
  **client scripts changed which component handles which action** (e.g.
  music volume moved from the zoom slider component to a dedicated
  bobble container). This is a CS2 behavior change, not a component ID
  shift.
- The remaining 43 files target interfaces where no such CS2 redesign
  occurred. They are very likely correct but only testing with a rev-240
  client can fully confirm.

**Status: Low risk.** These are expected to work. Fix individually if a
specific interface misbehaves during testing.

| File | Component IDs |
|---|---|
| AchievementDiaryTabInterface | 2 (slots 0-11) |
| AutocastInterface | 1 |
| AvasDevicesInterface | 2, 7, 12 |
| BankInventoryInterface | 3, 4, 6, 11 |
| BattlestaffEnchantmentInterface | 3-12 |
| CharacterSummaryInterface | 2, 3 |
| ChatChannelInterface | 20 |
| ChatInterface | 7, 15, 19, 23, 27 |
| ClanChatSetUpInterface | 10, 13, 16, 19 |
| CostumeRoomInterface | 4 |
| DecantingDialogueInterface | 3-6 |
| DiangoItemRetrievalService | 1, 3 |
| EmoteTabInterface | 2 |
| EquipmentInventoryInterface | 0 |
| EquipmentTabInterface | 1, 3, 5, 7, 28 |
| ExperienceTrackerInterface | 17, 21, 25, 30, 33, 38, 42, 46, 51-59 |
| ExplorerRingInterface | 1, 2, 4, 7, 8 |
| FriendsListInterface | 1 |
| GameModeSetupInterface | 21-27 |
| GnomeGliderInterface | 4, 7, 10, 13, 16, 21, 25 |
| GoldJewelleryInterface | 60-64 |
| GrandExchangeHistoryInterface | 2, 3 |
| IgnoreListInterface | 1 |
| InventoryTab | 0 |
| JossiksGodBooks | 3-8 |
| JournalHeaderTabInterface | 2, 10, 18 |
| LogoutTabInterface | 3, 8 |
| LootingBagInterface | 2, 5, 6 |
| MobilePaneInterface | 30 |
| MyceliumTeleportInterface | 4, 5, 8, 9, 12, 13, 16, 17 |
| NieveGravestoneInterface | 2-13 |
| PetInsuranceInterface | 11, 27, 37 |
| PrayerTab | 42 |
| PriceCheckerInterface | 2, 5, 8, 10, 12 |
| PriceCheckerInventoryInterface | 0 |
| QuickPrayerInterface | 4, 5 |
| SeedBoxInterface | 5, 6, 11 |
| SilverJewelleryInterface | 32-36 |
| TradeInventoryInterface | 0 |
| TradeStage1Interface | 10, 25 |
| TradeStage2Interface | 13, 14 |
| UnmorphInterface | 0 |
| WorldMapInterface | 4, 24, 38 |

### 8 Stub Interfaces (empty attach, may need revision)

AnotherClanInterface, BugReportInterface, ClanInterface,
ExperienceDropsWindow, KourendFavourTabInterface, QuestTabInterface,
SkillsTabInterface, SkotizoInterface.

---

## P2 -- Hardcoded Component IDs Outside testinterfaces/

### ~~Potentially stale IDs~~ VERIFIED SAFE

**Analysis (2026-09-18):** Checked every hardcoded (interfaceId,
componentId) pair from the audit against both rev-228 and rev-240
component.rscm. Zero named components shifted position. The only change
was 2 previously-unnamed components in interface 229 (messagebox) gaining
names — the IDs are the same.

**Status: Low risk.** Same conclusion as P1 — these are expected to work.
Fix individually if testing reveals a mismatch.

### Reference list (for testing)

### Engine Core

| File | Interface(s) | Components |
|---|---|---|
| InterfaceHandler.java | 378, 399, 216 | 4, 70; 7-9; 1 |
| Equipment.java | 85 | 0 |
| Bonuses.java | variable | 43, 117 |
| Skills.java | SKILL_GUIDE | 8 |
| CombatDefinitions.java | COMBAT_TAB | 2, 3 |
| NotificationSettings.java | 549 | 16 |
| GameCommands.java | 12, 84 | 13, variable |
| BankPin.java | BANK_PIN_VERIFICATION, BANK_PIN_SETTINGS | 2,7,14; 0,6,8,10,14,18,21,26,28,30,33,36 |
| BankPinSettingsInterface.java | getInterface() | 0,6,8,10,14,18,21,26,28 |
| Trade.java | INTERFACE, SECOND_INTERFACE | 9,24,25,27,28,30,31; 4,23,24,30 |
| Scoreboard.kt | SCOREBOARD | 9,11,13,15,17,19 |

### Dialogues

| File | Interface | Components |
|---|---|---|
| NPCMessage.java | NPC dialogue | name, head, continue, text constants |
| PlayerMessage.java | 217 | 2, 4, 5, 6 |
| PlainMessage.java | 229 | 3, 4 |
| ItemMessage.java | 193 | 0, 1, 2 |
| DoubleItemMessage.java | 11 | 1, 2, 3, 4 |
| OptionMessage.java | 219 | 1 |
| LevelUpMessage.java | 233 | 1, 2, 3, SKILL_IDS[]+1 |
| DestroyItemMessage.java | 584 | 0 |
| DualItemOptionMessage.java | OPTION_INTERFACE | 2-6 |
| SkillMessage.java | INTERFACE | 14+i |

### Content

| File | Interface | Components |
|---|---|---|
| ClanManager.java | 94 | 10 |
| Slayer.java | 68 | 4, 5 |
| Book.java | 392 | 6, 9, 10, 44-75, 77 |
| ChapteredBook.java | 680 | 6, 10, 11, 45-76, 108, 110 |
| Construction.java | 370 | 16 |
| HouseViewer.java | 422 | 5 |
| FremennikIslesSailing.java | 224 | 7 |
| PollManager.java | INTERFACE, VOTING | 3; 1, 4 |
| StrangeCasket.java | 231 | 1, 2 |
| StatSpy.java | 523 | 1, 94, computed |
| Teleother.java | INTERFACE | 89, 91 |
| Geomancy.java | 179 | variable |
| GrandExchange.java | INTERFACE | 6-14, 16, 17, 23-26 |
| GameShop.java | INTERFACE | 16; 0 |
| PrayerManager.java | QUICK_PRAYERS | 4 |
| WildernessDitchObject.java | 475 | 10, 13 |
| ThessaliaD.java | 591 | 3, 5, 7 |
| HairdresserD.java | 82 | 2, 8, 9 |
| MenuD.java | 187 | 3 |
| TanningInterface.java | 324 | 100+i, 108+i, 116+i |
| FairyRingCombination.java | 398 | 26 |
| FairyRingLog.java | 381 | 6, 140+i, 144+i |
| PetList.java | 210 | 3 |
| ZeahStatueScroll.java | INTERFACE | 4, index |
| CanoeHandler/Location.java | 416, 57 | variable |
| Costume Room (x6 files) | 675 | 4 |

### Raid/Boss Content

| File | Interface | Components |
|---|---|---|
| TOAScoreBoardInterface | TOA | 58, 59, 66, 67, 76 |
| TOAPartyManagementInterface | TOA | 98, 1 |
| TOALobbyParty | TOA | 5 |
| TOA rewards.kt | TOA | 10 |
| ColosseumInstance | 865 | 8, 34, 37, 39 |
| ColosseumScoreboardInterface | COLOSSEUM | 9,10,12,14,16-18,20,22,24 |
| TheatreOfBloodStats | TOB | 41-66 |
| RaidParty.java (CoX) | 507 | 3 |
| EntangledPuzzle.kt (Vardorvis) | DT2 | 6-11 |
| CA Log interfaces | CA_TASKS, CA_OVERVIEW, etc. | 8,9,15-33 |
| Various scoreboard interfaces | Nightmare, Gauntlet, Nex, DT2 | 5-30 |

---

## P3 -- Missing Incoming Packet Handlers

| Packet | Priority | Notes |
|---|---|---|
| ~~**IfScriptTrigger**~~ | ~~Medium~~ DONE | Registered as no-op handler — receives and releases the buffer. Prevents unhandled-packet errors. Real handling can be added when needed. |
| **ResumePCountDialogLong** | Low | Only needed if a dialog requests a 64-bit numeric input. |
| **OpWorldEntity1-6** | Blocked | Requires world entity implementation (see Sailing section). |
| **OpWorldEntityT** | Blocked | Use-item-on-world-entity. Same blocker. |
| **OpWorldEntityU** | Blocked | Examine-world-entity. Same blocker. |

---

## P4 -- World Entity / Sailing Infrastructure

The RSProt world entity protocol is wired at the infrastructure level
(alloc/dealloc/send/reconnect works), but no world entities actually
exist:

- `Player.getWorldEntityId()` is hardcoded to return -1
- `setActiveWorld()` always passes -1
- No OpWorldEntity incoming packet handlers registered
- No world entity creation/management logic exists
- Existing "sailing" code is the old boat-travel system (fade + teleport),
  not the Sailing skill

To implement Sailing or any world-entity-based content:

1. Implement world entity creation and lifecycle management
2. Register OpWorldEntity1-6, OpWorldEntityT, OpWorldEntityU handlers
3. Implement `getWorldEntityId()` to return actual entity IDs when on one
4. Handle world entity camera targeting (infrastructure exists at
   `worldEntityCamTarget()`)
5. Consider Group packets (GroupFull, GroupVar*) for crew/party features

### New Packets Relevant to Sailing

| Packet | Purpose |
|---|---|
| AmbientSoundStart/Stop | Ocean ambience loops |
| CamSkybox | Skybox model for open water |
| CamUnlock | Full vertical camera panning |
| CamLookAtCycles | Precise cycle-based camera control |
| ScriptedProjAdd/Change | Cannon projectiles with scripted paths |
| GroupFull, GroupVar* | Crew/party system |

---

## P5 -- Minor / Cleanup

### Deprecated Typealias Usage

`LocAddChange` in `ZoneManager.kt` (lines 150, 154) resolves to
`LocAddChangeV2` via typealias. Works correctly but could be made explicit.

### CamRotateToCoordinateV1

Used at PacketSender.kt line ~352. Works fine but V2/V3 offer
`heightRelative` and `trackTarget` features.

### Varbit/Varp Verification

`SettingVariables.java` has 180+ varbit/varp constants (range 236-14196).
The settings-related ones were updated alongside the settings interface
files. However, hardcoded varbit IDs in content modules were not audited
against the rev-240 cache. Varbit IDs rarely change between revisions but
it's worth spot-checking any settings that don't work.

### IfResyncV2 Bitmask

The `ifResync()` method in PacketSender.kt accepts pre-built
`IfResyncV2.InterfaceEventsMessage` objects. It has no callers today, but
if one is added, the caller must apply the same events1/events2 split used
in `ifSetEvents()`.

---

## Recommended Attack Order

1. ~~**P0 Camera V3 coords**~~ DONE (commit `0e5ee28a`)
2. ~~**P0 getPackets() triple call**~~ DONE (commit `0e5ee28a`)
3. ~~**P1 Component ID audit**~~ VERIFIED SAFE -- no shifts detected.
   Test with rev-240 client to confirm individual interfaces.
4. ~~**P2 Hardcoded IDs**~~ VERIFIED SAFE -- no shifts detected.
5. **P3 IfScriptTrigger** -- Register a handler (even a no-op) to prevent
   client disconnect on unhandled packet. Implement real handling if any
   interface depends on it.
6. **P4 World entities** -- Large feature work, only when actively
   building Sailing content.
