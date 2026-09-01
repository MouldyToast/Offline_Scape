package com.near_reality.game.content.commands

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

/**
 * Developer commands for spawning all Near-Reality custom items in the game.
 *
 * @author Stan van der Bend.
 */
object CustomCommands {

    fun register() {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "customs") { p, _ ->
            p.options() {

                "Spawn all in bank" {
                    p.bank.apply {
                        add(Item(CustomItemId.BRONZE_KEY, 10))
                        add(Item(CustomItemId.SILVER_KEY, 10))
                        add(Item(CustomItemId.GOLD_KEY, 10))
                        add(Item(CustomItemId.PLATINUM_KEY, 10))
                        add(Item(CustomItemId.DIAMOND_KEY, 10))
                        add(Item(CustomItemId.NR_TABLET, 10))
                        add(Item(CustomItemId.PINK_PARTYHAT, 10))
                        add(Item(CustomItemId.ORANGE_PARTYHAT, 10))

                        add(Item(CustomItemId.DONATOR_PIN_10 , 10))
                        add(Item(CustomItemId.DONATOR_PIN_25 , 10))
                        add(Item(CustomItemId.DONATOR_PIN_50 , 10))
                        add(Item(CustomItemId.DONATOR_PIN_100, 10))

                        add(Item(CustomItemId.GANODERMIC_RUNT, 10))

                        add(Item(CustomItemId.BLUE_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.BLUE_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.BLUE_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.BLUE_ANKOU_MASK, 10))
                        add(Item(CustomItemId.BLUE_ANKOU_TOP, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.GREEN_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_MASK, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_TOP, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.GOLD_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_MASK, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_TOP, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.WHITE_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_MASK, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_TOP, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.BLACK_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_MASK, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_TOP, 10))
                        add(Item(CustomItemId.WHITE_SANTA_HAT, 10))
                        add(Item(CustomItemId.WHITE_HWEEN_MASK, 10))
                        add(Item(CustomItemId.BLACK_SANTA_HAT, 10))
                        add(Item(CustomItemId.BLACK_HWEEN_MASK, 10))
                        add(Item(CustomItemId.BLACK_PARTY_HAT, 10))
                        add(Item(CustomItemId.LIME_PARTY_HAT, 10))
                        add(Item(CustomItemId.LIME_SANTA_HAT, 10))
                        add(Item(CustomItemId.LIME_HWEEN_MASK, 10))
                        add(Item(CustomItemId.PINK_SANTA_HAT, 10))
                        add(Item(CustomItemId.PINK_HWEEN_MASK, 10))
                        add(Item(CustomItemId.PINK_PARTY_HAT, 10))
                        add(Item(CustomItemId.CYAN_SANTA_HAT, 10))
                        add(Item(CustomItemId.CYAN_HWEEN_MASK, 10))
                        add(Item(CustomItemId.CYAN_PARTY_HAT, 10))
                        add(Item(CustomItemId.BLUE_SANTA_HAT, 10))
                        add(Item(CustomItemId.PURPLE_SANTA_HAT, 10))
                        add(Item(CustomItemId.PURPLE_HWEEN_MASK, 10))
                        add(Item(CustomItemId.ORANGE_SANTA_HAT, 10))
                        add(Item(CustomItemId.ORANGE_HWEEN_MASK, 10))
                        add(Item(CustomItemId.ORANGE_PARTY_HAT, 10))
                        add(Item(CustomItemId.LIGHT_PURPLE_SANTA_HAT, 10))
                        add(Item(CustomItemId.LIGHT_PURPLE_HWEEN_MASK, 10))
                        add(Item(CustomItemId.LIGHT_PURPLE_PARTY_HAT, 10))
                        add(Item(CustomItemId.LIGHT_BLUE_HWEEN_MASK, 10))
                        add(Item(CustomItemId.LIGHT_BLUE_PARTY_HAT, 10))
                        add(Item(CustomItemId.LIGHT_BLUE_SANTA_HAT, 10))
                        add(Item(CustomItemId.PEACH_SANTA_HAT, 10))
                        add(Item(CustomItemId.PEACH_HWEEN_MASK, 10))
                        add(Item(CustomItemId.PEACH_PARTY_HAT, 10))
                        add(Item(CustomItemId.LIGHT_GREEN_SANTA_HAT, 10))
                        add(Item(CustomItemId.LIGHT_GREEN_HWEEN_MASK, 10))
                        add(Item(CustomItemId.LIGHT_GREEN_PARTY_HAT, 10))
                        add(Item(CustomItemId.LIGHT_YELLOW_SANTA_HAT, 10))
                        add(Item(CustomItemId.LIGHT_YELLOW_HWEEN_MASK, 10))
                        add(Item(CustomItemId.LIGHT_YELLOW_PARTY_HAT, 10))
                        add(Item(CustomItemId.CREAM_SANTA_HAT, 10))
                        add(Item(CustomItemId.CREAM_HWEEN_MASK, 10))
                        add(Item(CustomItemId.CREAM_PARTY_HAT, 10))
                        add(Item(CustomItemId.MAGMA_HWEEN_MASK, 10))
                        add(Item(CustomItemId.MAGMA_SANTA_HAT, 10))
                        add(Item(CustomItemId.INVERTED_SANTA_HAT, 10))
                        add(Item(CustomItemId.GREY_SANTA_HAT_T, 10))
                        add(Item(CustomItemId.SHADOW_SANTA_HAT, 10))
                        add(Item(CustomItemId.SHADOW_BUNNY_EARS, 10))
                        add(Item(CustomItemId.GREY_BUNNY_EARS, 10))
                    }
                }
            }
        }
    }
}
