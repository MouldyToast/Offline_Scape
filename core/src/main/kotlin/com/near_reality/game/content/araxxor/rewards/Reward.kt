package com.near_reality.game.content.araxxor.rewards

import com.zenyte.game.item.ids.*
import com.zenyte.game.content.follower.Follower
import com.zenyte.game.content.follower.PetWrapper
import com.zenyte.game.content.follower.impl.BossPet
import com.near_reality.game.content.slayer.SlayerMaster
import com.zenyte.game.item.Item
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.npc.NpcId.ARAXXOR
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.player.Player
import com.near_reality.game.world.entity.player.slayerBaseDryStreak
import com.zenyte.game.util.Utils
import com.zenyte.logger.NearRealityLogger
import org.slf4j.Logger
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-29
 */
class Reward: DropProcessor() {
    val logger: Logger = NearRealityLogger.getLogger(this::class.java)

    fun rollForItems(player: Player) : ArrayList<Item> {
        val items = arrayListOf<Item>()
        // killed in under 1:15 -> reward `Coagulated venom`
        val duration = System.currentTimeMillis() - player.bossTimer.currentTracker
        val seconds = duration / 1000L
        val hasEvolvedNid: Boolean =
            player.getBooleanAttribute("nid_rax_metamorph").or(false)
        if (seconds < 75 && !hasEvolvedNid && !player.containsItem(COAGULATED_VENOM))
            items.add(Item(COAGULATED_VENOM, 1))

        // Konar -> 1/50 to drop `Brimstone key`
        val master = player.slayer.master
        if (master != null) {
            if (master == SlayerMaster.KONAR_QUO_MATEN)
                if (ThreadLocalRandom.current().nextDouble() < 1/50.0)
                    items.add(Item(BRIMSTONE_KEY, 1))
        }

        // Roll for a Scroll
        if (ThreadLocalRandom.current().nextDouble() < 1/50.0)
            items.add(Item(SCROLL_BOX_ELITE, 1))

        // Roll for Nid
        rollForNid(player, 1/3000.0)

        // roll to see if we're in the Unique table
        // Halberd Pieces
        if (ThreadLocalRandom.current().nextDouble() < 1/120.0) {
            var piece = Item(NOXIOUS_POMMEL, 1)
            if (player.containsItem(piece))
                piece = Item(NOXIOUS_POINT, 1)
            if (player.containsItem(piece))
                piece = Item(NOXIOUS_BLADE, 1)
            items.add(piece)
            player.collectionLog.add(piece)
            WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, piece, "Araxxor")
        }
        // fang
        if (ThreadLocalRandom.current().nextDouble() < 1/350.0) {
            val fang = Item(ARAXYTE_FANG, 1)
            items.add(fang)
            player.collectionLog.add(fang)
            WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, fang, "Araxxor")
        }

        // roll for seed table
        if (ThreadLocalRandom.current().nextDouble() < 1/115.0) {
            val item = AraxxorTreeHerbSeedDropTable.rollForItem()
            if (item != null) items.add(item)
        }

        // roll for supplies
        if (ThreadLocalRandom.current().nextDouble() < 1/8.0) {
            val item = AraxxorSuppliesDropTable.rollForItem()
            if (item != null) items.add(item)
        }

        // do a normal drop
        val item = AraxxorNormalDropTable.rollForItem()
        if (item != null) {
            if(item.id != SLAYER_BASE) {
                player.slayerBaseDryStreak++
                if(player.slayerBaseDryStreak == 500) {
                    player.sendMessage("You have received a slayer base for hitting a 500 kill drystreak.")
                    items.add(Item(SLAYER_BASE))
                }
            }
            if(Utils.random(1000) == 0) {
                val pm = Item(PRIMAL_MAUL, 1)
                items.add(pm)
                player.collectionLog.add(pm)
                WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, pm, "Araxxor")
            }
            items.add(item)
            player.collectionLog.add(item)
            WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, item, "Araxxor")
        }

        return items
    }


    fun rollForNid(player: Player, chance: Double) {
        // Roll for Nid
        if (ThreadLocalRandom.current().nextDouble() < chance) {
            val nid = BossPet.NID
            val item = Item(nid.itemId)
            player.collectionLog.add(item)
            if ((PetWrapper.checkFollower(player) && player.follower.pet == nid) || player.containsItem(nid.itemId))
                player.sendMessage("<col=ff0000>You have a funny feeling like you would have been followed...</col>")

            else if (player.follower != null) {
                if (player.inventory.addItem(item).isFailure) {
                    if (player.bank.add(item).isFailure)
                        player.sendMessage("There was not enough space in your bank, and therefore the pet was lost.")
                    else player.sendMessage(
                        "<col=ff0000>You have a funny feeling like you're being followed - The pet has " +
                                "been added to your bank.</col>")
                }
                player.sendMessage("<col=ff0000>You feel something weird sneaking into your backpack.</col>")
                WorldBroadcasts.broadcast(player, BroadcastType.PET, nid)
            }
            else {
                player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.</col>")
                player.follower = Follower(nid.petId, player)
                WorldBroadcasts.broadcast(player, BroadcastType.PET, nid)
            }
        }
    }

    override fun attach() {
        appendDrop(DisplayedDrop(NOXIOUS_BLADE,     1,  1,  120.0))
        appendDrop(DisplayedDrop(NOXIOUS_POINT,     1,  1,  120.0))
        appendDrop(DisplayedDrop(NOXIOUS_POMMEL,    1,  1,  120.0))
        appendDrop(DisplayedDrop(ARAXYTE_FANG,      1,  1,  350.0))
        appendDrop(DisplayedDrop(NID,               1,  1,  3000.0))
        appendDrop(DisplayedDrop(PRIMAL_MAUL,               1,  1,  750.0))
        appendDrop(DisplayedDrop(BRIMSTONE_KEY,     1,  1,  50.0))
        put(ARAXXOR, BRIMSTONE_KEY, PredicatedDrop("This drop can only be obtained when killing the monster during a slayer assignment, assigned by Konar quo Maten."))

        AraxxorNormalDropTable.entries.forEach { normalDropTable ->
            appendDrop(DisplayedDrop(
                normalDropTable.id,
                normalDropTable.minAmount,
                normalDropTable.maxAmount,
                normalDropTable.weight
            ))
        }

        AraxxorTreeHerbSeedDropTable.entries.forEach { normalDropTable ->
            appendDrop(DisplayedDrop(
                normalDropTable.id,
                normalDropTable.minAmount,
                normalDropTable.maxAmount,
                normalDropTable.weight
            ))
        }

        AraxxorSuppliesDropTable.entries.forEach { normalDropTable ->
            appendDrop(DisplayedDrop(
                normalDropTable.id,
                normalDropTable.minAmount,
                normalDropTable.maxAmount,
                normalDropTable.weight
            ))
        }
    }

    override fun ids(): IntArray =
        intArrayOf(ARAXXOR)
}