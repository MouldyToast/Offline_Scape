package com.near_reality.game.world.entity.player.action.combat.effect

import com.zenyte.game.content.compcapes.CompletionistCape
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.region.area.plugins.HitProcessPlugin

object DeathCapeEffect {

    @JvmStatic
    fun apply(player: Player, target: Entity): Int? {
        if (target is NPC &&
            (player.equipment.isWearing(Item(ItemId.DEATH_CAPE)) ||
                    (player.getBooleanAttribute("death_cape_added_to_comp") &&
                    CompletionistCape.getCompletionistCapeTier(player.equipment.getId(EquipmentSlot.CAPE)) == 3)
            ) &&
            Utils.randomBoolean(100)
            ) {
            player.sendMessage(Colour.RS_RED.wrap("A surge of power erupts from your cape!"))
            World.sendProjectile(
                player, target,
                Projectile(
                    6012,
                    30,
                    27,
                    70,
                    0
                )
            )
            if (player.area != null && !(player.area.javaClass.isAssignableFrom(HitProcessPlugin::class.java)))
                return 150
        }
        return null
    }
}
