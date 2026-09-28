package org.jesse.game.content.skills.sailing

import com.google.common.eventbus.Subscribe
import mgi.types.config.items.ItemDefinitions
import org.jesse.game.item.Item
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.model.ui.PaneType
import org.jesse.game.model.ui.UserInterface
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.AccessMask
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.dialogue.PlainChat
import org.jesse.plugins.events.LoginEvent

/*
 * Boat 1's cargo hold contents, in hold slot order, as (obj id, count) pairs. The hold belongs to the boat and
 * survives logout (porttasks t19: two repair kits already in the hold at the session's first open).
 * An IntArray survives the Gson list round-trip in AttributesExt (a List<Int> would come back as Doubles).
 */
private var Player.cargoHold1 by persistentAttribute<IntArray?>("sailing_boat_1_cargohold", null)

/**
 * The raft cargo hold (`sailing_boat_cargohold` 943 main modal + `sailing_boat_cargohold_side` 944 side modal,
 * inv 963 `sailing_boat_1_cargohold`). Everything here is from porttasks.txt (tick numbers) and
 * pandemonium_quest.txt ("quest" + tick):
 * - open (op1 "Open"): t18 step, t19 open, t20 `busy` = 1;
 * - close (client close / walk away / anything that closes the main modal): t21;
 * - "Deposit-held" (op1 on the `_cargo` variant): t101 step, t102 anim + worn slot 3 cleared, t103 the rest;
 * - withdraw a crate (943:10 op1, sub = hold slot): t272 / t273;
 * - hands full (crossbow in worn slot 3): quest t622 messagebox, pickup refused.
 *
 * The hold inventory is an "other" inventory: the client script reads it with `invother_getobj`, and the client
 * files an update_inv_full under inv | 0x8000 when its combined id is below -70000. Live sends com=65534:60108
 * (-70964) and stops it with id 33731 (963 | 0x8000). The engine's usual `updateInvFull(container)` (combined id -1)
 * would put the items in plain inv 963 and the hold would open empty.
 *
 * Not captured yet (see the handover's capture list): side-panel deposits, ordinary withdrawals (Withdraw-1/5/10/X/All
 * of non-crates), the deposit-all buttons and loc op2, a full hold, the tools, the side warning Dismiss.
 * Those clicks do nothing.
 */
object CargoHold {
    const val INTERFACE_MAIN = 943
    const val INTERFACE_SIDE = 944
    const val COMPONENT_ITEMS = 10

    private const val INV_ID = 963
    private const val INV_OTHER_FLAG = 0x8000
    private const val INV_COM_INTERFACE = 65534
    private const val INV_COM_COMPONENT = 60108

    private const val COMPONENT_FRAME = 1
    private const val COMPONENT_CAPACITY = 5
    private const val COMPONENT_TOOLS = 18
    private const val SIDE_COMPONENT_ITEMS = 1

    private const val VARP_CARGOHOLD_INV = 5204
    private const val VARP_SIDE_WHITELIST = 5205
    private const val VARBIT_CARRYING_CARGO = 19134
    private const val VARBIT_BUSY = 12393

    private const val SCRIPT_MAINMODAL_BACKGROUND = 917
    private const val SCRIPT_STEELBORDER = 227
    private const val SCRIPT_PVP_ICONS_COMLEVELRANGE = 5224

    private const val COMBAT_INTERFACE = 593
    private const val COMBAT_COMPONENT_CATEGORY = 5

    private const val SEQ_PICKUPTABLE = 832
    private const val SYNTH_OPEN = 10907
    private const val SYNTH_WITHDRAW = 10903
    private const val SYNTH_WITHDRAW_CLOSE = 10908
    private const val SYNTH_DEPOSIT = 10905

    private const val WORN_WEAPON = 3
    private const val WORN_SHIELD = 5
    private const val BACKPACK_SIZE = 28

    /** `param_2504` = 1 on cargo crates (cargo_crate_platebodies_the_pandemonium, sailing_intro_cargo_crate). */
    private const val PARAM_CARGO_CRATE = 2504

    private const val MSG_DEPOSIT = "You deposit some cargo into the cargo hold."
    private const val MSG_HANDS_FULL = "You cannot pick up any cargo as your hands are full."

    /**
     * dbtable `sailing_boat_cargohold_whitelist_obj`, dbrow 8663 (rev-240 dump.dbrow): the 78 `entry` objs.
     * Ids resolved from dump.obj.
     */
    private val WHITELIST_OBJS: Set<Int> = hashSetOf(
        31986, // sailing_log
        31985, // sailing_log_initial
        31807, // sailing_charting_crowbar
        31805, // sailing_charting_current_duck
        31803, // sailing_charting_spyglass
        7535, // hundred_pirate_diving_backpack
        7534, // hundred_pirate_diving_helmet
        11334, // brut_fish_cuts
        32307, // sailing_fine_fish_offcuts
        32309, // raw_giant_krill
        31408, // poh_trophydrop_giant_krill
        32317, // raw_haddock
        31412, // poh_trophydrop_haddock
        32325, // raw_yellowfin
        31416, // poh_trophydrop_yellowfin
        32333, // raw_halibut
        31420, // poh_trophydrop_halibut
        32341, // raw_bluefin
        31424, // poh_trophydrop_bluefin
        32349, // raw_marlin
        31428, // poh_trophydrop_marlin
        32364, // camphor_crate
        6696, // slayer_icy_water
        32366, // fish_crate_empty
        32368, // fish_crate_giant_krill
        32371, // fish_crate_haddock
        32374, // fish_crate_yellowfin
        32377, // fish_crate_halibut
        32380, // fish_crate_bluefin
        32383, // fish_crate_marlin
        1919, // beer_glass
        1915, // grog
        5763, // cider
        31811, // whirlpool_surprise
        31814, // kraken_ink_stout
        31817, // perildance_bitter
        31820, // trawlers_trust
        31823, // horizons_lure
        31288, // skillcape_sailing
        31290, // skillcape_sailing_trimmed
        31292, // skillcape_sailing_hood
        303, // net
        305, // big_net
        307, // fishing_rod
        301, // lobster_pot
        311, // harpoon
        3157, // tbwt_karambwan_vessel
        313, // fishing_bait
        3150, // tbwt_raw_karambwanji
        13431, // piscarilius_sandworms
        317, // raw_shrimp
        315, // shrimp
        319, // anchovies
        321, // raw_anchovies
        325, // sardine
        327, // raw_sardine
        347, // herring
        345, // raw_herring
        355, // mackerel
        353, // raw_mackerel
        339, // cod
        341, // raw_cod
        365, // bass
        363, // raw_bass
        361, // tuna
        359, // raw_tuna
        373, // swordfish
        371, // raw_swordfish
        379, // lobster
        377, // raw_lobster
        3144, // tbwt_cooked_karambwan
        3142, // tbwt_raw_karambwan
        13441, // anglerfish
        13439, // raw_anglerfish
        7946, // monkfish
        7944, // raw_monkfish
        385, // shark
        383, // raw_shark
    )

    /** dbrow 8663 `entry_category`: category_2294, category_2296, category_2295, category_2280, category_1176. */
    private val WHITELIST_CATEGORIES: Set<Int> = hashSetOf(2294, 2296, 2295, 2280, 1176)

    private class Session(val boat: Boat, var whitelist: Int) {
        var busySet = false
    }

    /** Players with the hold open. World thread only. */
    private val sessions = HashMap<Player, Session>()

    /** `sailing_boat_cargohold_inv` is -1 at login (porttasks tick 0: varp 5204 0 -> -1); the first open sets it. */
    @JvmStatic
    @Subscribe
    fun onLogin(event: LoginEvent) {
        event.player.varManager.sendVar(VARP_CARGOHOLD_INV, -1)
    }

    @JvmStatic
    fun isCarryingCargo(player: Player): Boolean = player.varManager.getBitValue(VARBIT_CARRYING_CARGO) == 1

    /**
     * op1 "Open" (t19, the tick after the step), in live order: varp 5204 (only when not already 963 - it stays 963
     * after closing, so t270 / t477 don't re-send it), varp 5205, face (the walk-to runnable), the hold inventory
     * (every open), synth, main modal background, 943 in mainmodal, 944 in sidemodal, setevents, capacity, title.
     * `busy` = 1 the tick after (t20).
     *
     * The modals are opened with the pane-level send, not `sendInterface(CENTRAL, ...)`, which would send script
     * 2524 (`toplevel_mainmodal_open`) where live sends 917 (`toplevel_mainmodal_background`).
     */
    @JvmStatic
    fun open(player: Player, boat: Boat) {
        sessions.keys.removeIf { it.isFinished }
        if (sessions.containsKey(player)) {
            return
        }
        val vars = player.varManager
        val dispatcher = player.packetDispatcher
        val handler = player.interfaceHandler
        if (vars.getValue(VARP_CARGOHOLD_INV) != INV_ID) {
            vars.sendVarInstant(VARP_CARGOHOLD_INV, INV_ID)
        }
        val whitelist = whitelist(player)
        vars.sendVarInstant(VARP_SIDE_WHITELIST, whitelist)
        sendItems(player)
        dispatcher.sendSoundEffect(SoundEffect(SYNTH_OPEN))
        dispatcher.sendClientScript(SCRIPT_MAINMODAL_BACKGROUND, -1, -1)
        val pane = handler.pane ?: PaneType.FIXED
        handler.sendInterface(INTERFACE_MAIN, InterfacePosition.CENTRAL.getComponent(pane), pane, false)
        handler.sendInterface(INTERFACE_SIDE, InterfacePosition.SINGLE_TAB.getComponent(pane), pane, false)
        dispatcher.sendComponentSettings(
            INTERFACE_MAIN, COMPONENT_ITEMS, 0, 239,
            AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4,
            AccessMask.CLICK_OP5, AccessMask.CLICK_OP6, AccessMask.CLICK_OP10,
        )
        dispatcher.sendComponentSettings(
            INTERFACE_SIDE, SIDE_COMPONENT_ITEMS, 0, 27,
            AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4,
            AccessMask.CLICK_OP5, AccessMask.CLICK_OP6, AccessMask.CLICK_OP10,
            AccessMask.DRAG_DEPTH1, AccessMask.DRAG_TARGETABLE,
        )
        dispatcher.sendComponentSettings(INTERFACE_MAIN, COMPONENT_TOOLS, 0, 4, AccessMask.CLICK_OP1)
        dispatcher.sendComponentText(INTERFACE_MAIN, COMPONENT_CAPACITY, boat.type.cargoHoldSize.toString())
        dispatcher.sendClientScript(
            SCRIPT_STEELBORDER,
            (INTERFACE_MAIN shl 16) or COMPONENT_FRAME,
            "Cargo Hold: " + BoatName.of(player),
        )
        val session = Session(boat, whitelist)
        sessions[player] = session
        handler.setModalCloseHook { onModalClose(player) }
        WorldTasksManager.schedule(1) {
            if (sessions[player] === session) {
                vars.sendBitInstant(VARBIT_BUSY, 1)
                session.busySet = true
            }
        }
    }

    /**
     * Runs from the engine's main modal close hook BEFORE 943's closesub, so the order is live's (t21):
     * stop transmit, varp 5205 -> 0, `busy` -> 0, closesub 944, then the engine closes 943.
     * Fires for a client close, walking away, and anything else that closes or replaces the main modal.
     */
    private fun onModalClose(player: Player) {
        val session = sessions.remove(player) ?: return
        player.packetDispatcher.sender.updateInvStopTransmit(INV_ID or INV_OTHER_FLAG)
        player.varManager.sendVarInstant(VARP_SIDE_WHITELIST, 0)
        if (session.busySet) {
            player.varManager.sendBitInstant(VARBIT_BUSY, 0)
        }
        player.interfaceHandler.closeInterface(InterfacePosition.SINGLE_TAB)
    }

    /**
     * 943:10 op1 on a crate ("Withdraw", the crate's only op; sub = hold slot). Same tick (t272), in order: stop
     * transmit, varp 5205 -> 0, seq 832, worn slot 3 = the crate, synth 10903, closesub 944, closesub 943,
     * synth 10908. Next tick (t273): `sailing_carrying_cargo` = 1, `busy` = 0, appearance, weapon-slot refresh.
     *
     * Hands full (a weapon or shield worn): the quest capture's refused pickup at the loading-bay ledger (t622, crossbow
     * in worn slot 3) - messagebox, nothing taken. Applied here as the same rule; the hold-withdraw case itself and the
     * shield-only case are not captured. The engine closes the main modal when it opens a chat dialogue, so the hold
     * closes with it (whether live keeps the hold open is not captured).
     */
    @JvmStatic
    fun withdraw(player: Player, slot: Int, itemId: Int) {
        val session = sessions[player] ?: return
        val items = items(player)
        val item = items.getOrNull(slot) ?: return
        if (item.id != itemId || !isCargoCrate(item.id)) {
            // Ordinary withdrawals are not captured yet.
            return
        }
        if (handsFull(player)) {
            player.dialogueManager.start(PlainChat(player, MSG_HANDS_FULL))
            return
        }
        val vars = player.varManager
        val handler = player.interfaceHandler
        sessions.remove(player)
        handler.setModalCloseHook(null)
        player.packetDispatcher.sender.updateInvStopTransmit(INV_ID or INV_OTHER_FLAG)
        vars.sendVarInstant(VARP_SIDE_WHITELIST, 0)
        player.setAnimation(Animation(SEQ_PICKUPTABLE))
        items.removeAt(slot)
        store(player, items)
        setWornWeapon(player, item)
        synth(player, SYNTH_WITHDRAW)
        handler.closeInterface(InterfacePosition.SINGLE_TAB)
        handler.closeInterface(InterfacePosition.CENTRAL)
        synth(player, SYNTH_WITHDRAW_CLOSE)
        WorldTasksManager.schedule(1) {
            if (player.isFinished) {
                return@schedule
            }
            vars.sendBitInstant(VARBIT_CARRYING_CARGO, 1)
            if (session.busySet) {
                vars.sendBitInstant(VARBIT_BUSY, 0)
            }
            updateStance(player)
            refreshWeaponSlot(player)
        }
    }

    /**
     * op1 "Deposit-held" (the `_cargo` variant). Runs on the tick after the step (t102): face (the walk-to runnable),
     * seq 832, worn slot 3 cleared. Next tick (t103): `sailing_carrying_cargo` = 0, message, appearance,
     * synth 10905, weapon-slot refresh. The hold is not open, so no inv 963 update: the crate shows at the next open.
     * The crate goes to the end of the hold (porttasks t270: after the two repair kits).
     */
    @JvmStatic
    fun depositHeld(player: Player, boat: Boat) {
        val crate = player.equipment.getItem(WORN_WEAPON)
        if (crate == null || !isCargoCrate(crate.id)) {
            return
        }
        val items = items(player)
        if (items.size >= boat.type.cargoHoldSize) {
            // Full hold: message not captured yet.
            return
        }
        player.setAnimation(Animation(SEQ_PICKUPTABLE))
        setWornWeapon(player, null)
        items.add(crate)
        store(player, items)
        WorldTasksManager.schedule(1) {
            if (player.isFinished) {
                return@schedule
            }
            player.varManager.sendBitInstant(VARBIT_CARRYING_CARGO, 0)
            player.sendMessage(MSG_DEPOSIT)
            updateStance(player)
            synth(player, SYNTH_DEPOSIT)
            refreshWeaponSlot(player)
        }
    }

    /**
     * Dev stand-in for a port task pickup: the writes captured at the ledger (porttasks t81 / quest t630) - the crate
     * in worn slot 3 and `sailing_carrying_cargo` = 1 - then the appearance next tick. Returns false when the hands are
     * full (the captured refusal message is sent).
     */
    @JvmStatic
    fun giveCrate(player: Player, crateId: Int): Boolean {
        if (handsFull(player)) {
            player.dialogueManager.start(PlainChat(player, MSG_HANDS_FULL))
            return false
        }
        player.varManager.sendBitInstant(VARBIT_CARRYING_CARGO, 1)
        player.setAnimation(Animation(SEQ_PICKUPTABLE))
        setWornWeapon(player, Item(crateId, 1))
        WorldTasksManager.schedule(1) {
            if (!player.isFinished) {
                updateStance(player)
            }
        }
        return true
    }

    /**
     * Keeps varp 5205 current while the hold is open (the side panel only offers "Deposit-..." on slots whose bit is
     * set, and redraws on varp transmit). Called every boat tick; a backpack change while the hold is open is not
     * captured, so the timing is ours.
     */
    internal fun tick(boat: Boat) {
        val iterator = sessions.entries.iterator()
        while (iterator.hasNext()) {
            val (player, session) = iterator.next()
            if (player.isFinished) {
                iterator.remove()
                continue
            }
            if (session.boat !== boat) {
                continue
            }
            val whitelist = whitelist(player)
            if (whitelist != session.whitelist) {
                session.whitelist = whitelist
                player.varManager.sendVar(VARP_SIDE_WHITELIST, whitelist)
            }
        }
    }

    /**
     * Varp 5205: bit n set when backpack slot n holds a whitelisted obj - a dbrow 8663 entry, or an obj whose category
     * is one of its entry categories (`sailing_boat_cargohold_side_drawitem`: testbit(varp, slot)). Verified against
     * the porttasks login backpack: only `sailing_log` in slot 5 qualifies -> 32, as in all three opens.
     */
    private fun whitelist(player: Player): Int {
        val inventory = player.inventory
        var bits = 0
        for (slot in 0 until BACKPACK_SIZE) {
            val item = inventory.getItem(slot) ?: continue
            if (isWhitelisted(item.id)) {
                bits = bits or (1 shl slot)
            }
        }
        return bits
    }

    private fun isWhitelisted(id: Int): Boolean {
        if (id in WHITELIST_OBJS) {
            return true
        }
        val category = ItemDefinitions.get(id)?.category ?: return false
        return category in WHITELIST_CATEGORIES
    }

    private fun isCargoCrate(id: Int): Boolean = ItemDefinitions.get(id)?.getIntParam(PARAM_CARGO_CRATE) == 1

    /** Crates are held in both hands (`wearpos=righthand`, `wearpos2=lefthand`); quest t622 refused with a weapon worn. */
    private fun handsFull(player: Player): Boolean =
        player.equipment.getItem(WORN_WEAPON) != null || player.equipment.getItem(WORN_SHIELD) != null

    /** update_inv_full for the hold, as live: com 65534:60108, only the occupied slots (t19: 2 objs, t270: 3). */
    private fun sendItems(player: Player) {
        player.packetDispatcher.sender.updateInvFull(
            INV_COM_INTERFACE, INV_COM_COMPONENT, INV_ID, *items(player).toTypedArray(),
        )
    }

    /**
     * Puts [item] in (or clears) worn slot 3 without the equip / unequip rules, and sends the partial update now so it
     * goes out where live has it (before the synths, t272). The caller re-derives the stance on the next tick with
     * [updateStance] (t103 / t273: the appearance goes out the tick after), so Equipment.refresh and
     * Appearance.resetRenderAnimation (both flag the appearance immediately) are not called here.
     */
    private fun setWornWeapon(player: Player, item: Item?) {
        val container = player.equipment.container
        container.set(WORN_WEAPON, item)
        player.packetDispatcher.sender.updateInvPartial(container)
        container.modifiedSlots.remove(WORN_WEAPON)
    }

    /**
     * The weapon-slot-change refresh live sends while aboard (t103, t273): the sailing sidepanel's 7 setevents (it is
     * the side0 interface on a boat), then pvp_icons_comlevelrange, the combat category text, pvp_icons_comlevelrange.
     * The weapon slot is either empty or holds a crate, both "Category: Unarmed" in the captures. The engine's
     * CombatDefinitions.refresh is not used: it sends varps live does not send here.
     */
    private fun refreshWeaponSlot(player: Player) {
        val dispatcher = player.packetDispatcher
        val combatLevel = player.skills.combatLevel
        if (Boats.at(player.location) != null) {
            SailingSidepanel.sendEvents(player)
        }
        dispatcher.sendClientScript(SCRIPT_PVP_ICONS_COMLEVELRANGE, combatLevel)
        dispatcher.sendComponentText(COMBAT_INTERFACE, COMBAT_COMPONENT_CATEGORY, "Category: Unarmed")
        dispatcher.sendClientScript(SCRIPT_PVP_ICONS_COMLEVELRANGE, combatLevel)
    }

    /**
     * Re-derives the render anims from worn slot 3 and flags the appearance. A held crate carries the crate stance
     * from ItemDefinitions.json (ready 4193 `dttd_carrying_crate_ready`, walk/turn-walks 4194
     * `dttd_carrying_crate_walk`, run 10679 `dttd_carrying_crate_walk_fast`); an empty slot gives the default stance.
     */
    private fun updateStance(player: Player) {
        player.appearance.resetRenderAnimation()
    }

    private fun items(player: Player): MutableList<Item> {
        val packed = player.cargoHold1 ?: return ArrayList()
        val items = ArrayList<Item>(packed.size / 2)
        var i = 0
        while (i + 1 < packed.size) {
            items.add(Item(packed[i], packed[i + 1]))
            i += 2
        }
        return items
    }

    private fun store(player: Player, items: List<Item>) {
        if (items.isEmpty()) {
            player.cargoHold1 = null
            return
        }
        val packed = IntArray(items.size * 2)
        items.forEachIndexed { index, item ->
            packed[index * 2] = item.id
            packed[index * 2 + 1] = item.amount
        }
        player.cargoHold1 = packed
    }

    private fun synth(player: Player, id: Int) {
        player.packetDispatcher.sendSoundEffect(SoundEffect(id))
    }
}

/**
 * The boat name the client builds with `sailing_boat_name_core` from `sailing_boat_1_name_1..3`: each part n > 0 is
 * option n-1 of its dbrow, 0 is the row's default. Rows (rev-240 dump.dbrow, table `sailing_boat_name_options`):
 * 8545 prefix (default "", no options - so name_1 is never needed), 8546 descriptor (default ""), 8547 noun
 * (default "Boat"). The capture boat (parts 9, 22) gives "Bladed Craft" (porttasks t19 steelborder title).
 */
object BoatName {
    private val DESCRIPTORS = arrayOf(
        "Adamant", "Almighty", "Ancient", "Angry", "Armadyl's", "Ascended", "Bandos'", "Beautiful",
        "Bladed", "Blasted", "Bloody", "Blue", "Brave", "Bronze", "Brutal", "Cheeky",
        "Cinnamon", "Classy", "Clever", "Cursed", "Dainty", "Dark", "Dawn", "Daylight",
        "Deadly", "Death", "Dragon", "Dream", "Earnest", "Enigmatic", "Excessive", "Extreme",
        "Fabulous", "Fatal", "Fearful", "Fervent", "Fierce", "Final", "Fine", "First",
        "Flying", "Foul", "Frivolous", "Gallant", "Giant", "Golden", "Gorgeous", "Green",
        "Grey", "Grim", "Grumpy", "Happy", "Holy", "Honest", "Illustrious", "Implacable",
        "Indomitable", "Insightful", "Iron", "Jaunty", "Jolly", "King", "Lady", "Last",
        "Laughing", "Light", "Little", "Lonely", "Lord", "Lunar", "Lusty", "Merry",
        "Mighty", "Misty", "Mithril", "Morbid", "Nervous", "Night", "Nightmare", "Noisome",
        "Omniscient", "Piebald", "Pink", "Prancing", "Prime", "Prince", "Princess", "Proud",
        "Pure", "Purple", "Queen", "Raging", "Rainbow", "Ralos'", "Rampaging", "Ranul's",
        "Red", "Reeking", "Ribald", "Rising", "Rune", "Sad", "Saradomin's", "Savage",
        "Scornful", "Screaming", "Sea", "Second", "Serene", "Smashing", "Smiling", "Steel",
        "Storm", "Sturdy", "Sun", "Supreme", "Swift", "Terrifying", "Third", "Thunder",
        "Tide", "Trusty", "Twilight", "Ultimate", "Unholy", "Unsinkable", "Vibrant", "Wandering",
        "Wave", "Weeping", "Whimsical", "Whistling", "Wily", "Wind", "Winged", "Yowling",
        "Zamorak's"
    )

    private val NOUNS = arrayOf(
        "Aegis", "Aeon", "Age", "Anchor", "Axe", "Bane", "Beard", "Beast",
        "Blood", "Blow", "Bounty", "Builder", "Calm", "Cannon", "Chaos", "Chaser",
        "Clam", "Claw", "Clipper", "Cloud", "Coral", "Craft", "Crow", "Dagger",
        "Dasher", "Death", "Demon", "Diver", "Diviner", "Doom", "Dragon", "Epoch",
        "Excess", "Flyer", "Fury", "Glow", "Grace", "Heart", "Hind", "Hunter",
        "Joker", "Killer", "Knife", "Knight", "Lament", "Law", "Lion", "Loner",
        "Manta", "Mind", "Miner", "Mirage", "Moon", "Ocean", "Oyster", "Pearl",
        "Pride", "Rage", "Raider", "Rain", "Ranger", "Raven", "Reaver", "Sea",
        "Shine", "Siren", "Slayer", "Storm", "Sun", "Sword", "Terror", "Tide",
        "Time", "Warrior", "Wave", "Whip", "Wind"
    )

    @JvmStatic
    fun of(player: Player): String {
        val (part2, part3) = BoatOwnership.nameParts(player)
        val descriptor = if (part2 > 0) DESCRIPTORS.getOrElse(part2 - 1) { "" } else ""
        val noun = if (part3 > 0) NOUNS.getOrElse(part3 - 1) { "Boat" } else "Boat"
        return listOf(descriptor, noun).filter { it.isNotEmpty() }.joinToString(" ")
    }
}

/** Cargo hold clicks. Only a crate's Withdraw (943:10 op1) is captured so far. */
@Suppress("unused")
class CargoHoldInterface : UserInterface {
    override fun handleComponentClick(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        slotId: Int,
        itemId: Int,
        optionId: Int,
        option: String?,
    ) {
        if (interfaceId == CargoHold.INTERFACE_MAIN && componentId == CargoHold.COMPONENT_ITEMS && optionId == 1) {
            CargoHold.withdraw(player, slotId, itemId)
        }
    }

    override fun getInterfaceIds(): IntArray = intArrayOf(CargoHold.INTERFACE_MAIN, CargoHold.INTERFACE_SIDE)
}