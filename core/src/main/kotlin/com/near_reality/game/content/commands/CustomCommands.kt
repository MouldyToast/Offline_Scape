package com.near_reality.game.content.commands

import com.zenyte.game.item.ItemId
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
                        add(Item(ItemId.BRONZE_KEY_32044, 10))
                        add(Item(ItemId.SILVER_KEY_32046, 10))
                        add(Item(ItemId.GOLD_KEY, 10))
                        add(Item(ItemId.PLATINUM_KEY, 10))
                        add(Item(ItemId.DIAMOND_KEY_32052, 10))
                        add(Item(ItemId.NR_TABLET, 10))
                        add(Item(ItemId.PINK_PARTYHAT, 10))
                        add(Item(ItemId.ORANGE_PARTYHAT, 10))

                        add(Item(ItemId.DONATOR_PIN_10 , 10))
                        add(Item(ItemId.DONATOR_PIN_25 , 10))
                        add(Item(ItemId.DONATOR_PIN_50 , 10))
                        add(Item(ItemId.DONATOR_PIN_100, 10))

                        add(Item(ItemId.GANODERMIC_RUNT, 10))

                        add(Item(ItemId.BLUE_ANKOU_SOCKS, 10))
                        add(Item(ItemId.BLUE_ANKOU_GLOVES, 10))
                        add(Item(ItemId.BLUE_ANKOUS_LEGGINGS, 10))
                        add(Item(ItemId.BLUE_ANKOU_MASK, 10))
                        add(Item(ItemId.BLUE_ANKOU_TOP, 10))
                        add(Item(ItemId.GREEN_ANKOU_SOCKS, 10))
                        add(Item(ItemId.GREEN_ANKOU_GLOVES, 10))
                        add(Item(ItemId.GREEN_ANKOUS_LEGGINGS, 10))
                        add(Item(ItemId.GREEN_ANKOU_MASK, 10))
                        add(Item(ItemId.GREEN_ANKOU_TOP, 10))
                        add(Item(ItemId.GOLD_ANKOU_SOCKS, 10))
                        add(Item(ItemId.GOLD_ANKOU_GLOVES, 10))
                        add(Item(ItemId.GOLD_ANKOUS_LEGGINGS, 10))
                        add(Item(ItemId.GOLD_ANKOU_MASK, 10))
                        add(Item(ItemId.GOLD_ANKOU_TOP, 10))
                        add(Item(ItemId.WHITE_ANKOU_SOCKS, 10))
                        add(Item(ItemId.WHITE_ANKOU_GLOVES, 10))
                        add(Item(ItemId.WHITE_ANKOUS_LEGGINGS, 10))
                        add(Item(ItemId.WHITE_ANKOU_MASK, 10))
                        add(Item(ItemId.WHITE_ANKOU_TOP, 10))
                        add(Item(ItemId.BLACK_ANKOU_SOCKS, 10))
                        add(Item(ItemId.BLACK_ANKOU_GLOVES, 10))
                        add(Item(ItemId.BLACK_ANKOUS_LEGGINGS, 10))
                        add(Item(ItemId.BLACK_ANKOU_MASK, 10))
                        add(Item(ItemId.BLACK_ANKOU_TOP, 10))
                        add(Item(ItemId.WHITE_SANTA_HAT, 10))
                        add(Item(ItemId.WHITE_HWEEN_MASK, 10))
                        add(Item(ItemId.BLACK_SANTA_HAT_32714, 10))
                        add(Item(ItemId.BLACK_HWEEN_MASK_32715, 10))
                        add(Item(ItemId.BLACK_PARTY_HAT, 10))
                        add(Item(ItemId.LIME_PARTY_HAT, 10))
                        add(Item(ItemId.LIME_SANTA_HAT, 10))
                        add(Item(ItemId.LIME_HWEEN_MASK, 10))
                        add(Item(ItemId.PINK_SANTA_HAT, 10))
                        add(Item(ItemId.PINK_HWEEN_MASK, 10))
                        add(Item(ItemId.PINK_PARTY_HAT, 10))
                        add(Item(ItemId.CYAN_SANTA_HAT, 10))
                        add(Item(ItemId.CYAN_HWEEN_MASK, 10))
                        add(Item(ItemId.CYAN_PARTY_HAT, 10))
                        add(Item(ItemId.BLUE_SANTA_HAT, 10))
                        add(Item(ItemId.PURPLE_SANTA_HAT, 10))
                        add(Item(ItemId.PURPLE_HWEEN_MASK, 10))
                        add(Item(ItemId.ORANGE_SANTA_HAT, 10))
                        add(Item(ItemId.ORANGE_HWEEN_MASK, 10))
                        add(Item(ItemId.ORANGE_PARTY_HAT, 10))
                        add(Item(ItemId.LIGHT_PURPLE_SANTA_HAT, 10))
                        add(Item(ItemId.LIGHT_PURPLE_HWEEN_MASK, 10))
                        add(Item(ItemId.LIGHT_PURPLE_PARTY_HAT, 10))
                        add(Item(ItemId.LIGHT_BLUE_HWEEN_MASK, 10))
                        add(Item(ItemId.LIGHT_BLUE_PARTY_HAT, 10))
                        add(Item(ItemId.LIGHT_BLUE_SANTA_HAT, 10))
                        add(Item(ItemId.PEACH_SANTA_HAT, 10))
                        add(Item(ItemId.PEACH_HWEEN_MASK, 10))
                        add(Item(ItemId.PEACH_PARTY_HAT, 10))
                        add(Item(ItemId.LIGHT_GREEN_SANTA_HAT, 10))
                        add(Item(ItemId.LIGHT_GREEN_HWEEN_MASK, 10))
                        add(Item(ItemId.LIGHT_GREEN_PARTY_HAT, 10))
                        add(Item(ItemId.LIGHT_YELLOW_SANTA_HAT, 10))
                        add(Item(ItemId.LIGHT_YELLOW_HWEEN_MASK, 10))
                        add(Item(ItemId.LIGHT_YELLOW_PARTY_HAT, 10))
                        add(Item(ItemId.CREAM_SANTA_HAT, 10))
                        add(Item(ItemId.CREAM_HWEEN_MASK, 10))
                        add(Item(ItemId.CREAM_PARTY_HAT, 10))
                        add(Item(ItemId.MAGMA_HWEEN_MASK, 10))
                        add(Item(ItemId.MAGMA_SANTA_HAT, 10))
                        add(Item(ItemId.INVERTED_SANTA_HAT_32752, 10))
                        add(Item(ItemId.GREY_SANTA_HAT_T, 10))
                        add(Item(ItemId.SHADOW_SANTA_HAT, 10))
                        add(Item(ItemId.SHADOW_BUNNY_EARS, 10))
                        add(Item(ItemId.GREY_BUNNY_EARS, 10))
                    }
                }
            }
        }
    }
}
