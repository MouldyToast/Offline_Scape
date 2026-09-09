package org.jesse.game.content.commands

import org.jesse.game.item.ids.*
import org.jesse.game.item.Item
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

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
                        add(Item(BRONZE_KEY_32044, 10))
                        add(Item(SILVER_KEY_32046, 10))
                        add(Item(GOLD_KEY, 10))
                        add(Item(PLATINUM_KEY, 10))
                        add(Item(DIAMOND_KEY_32052, 10))
                        add(Item(NR_TABLET, 10))
                        add(Item(PINK_PARTYHAT, 10))
                        add(Item(ORANGE_PARTYHAT, 10))

                        add(Item(DONATOR_PIN_10 , 10))
                        add(Item(DONATOR_PIN_25 , 10))
                        add(Item(DONATOR_PIN_50 , 10))
                        add(Item(DONATOR_PIN_100, 10))


                        add(Item(BLUE_ANKOU_SOCKS, 10))
                        add(Item(BLUE_ANKOU_GLOVES, 10))
                        add(Item(BLUE_ANKOUS_LEGGINGS, 10))
                        add(Item(BLUE_ANKOU_MASK, 10))
                        add(Item(BLUE_ANKOU_TOP, 10))
                        add(Item(GREEN_ANKOU_SOCKS, 10))
                        add(Item(GREEN_ANKOU_GLOVES, 10))
                        add(Item(GREEN_ANKOUS_LEGGINGS, 10))
                        add(Item(GREEN_ANKOU_MASK, 10))
                        add(Item(GREEN_ANKOU_TOP, 10))
                        add(Item(GOLD_ANKOU_SOCKS, 10))
                        add(Item(GOLD_ANKOU_GLOVES, 10))
                        add(Item(GOLD_ANKOUS_LEGGINGS, 10))
                        add(Item(GOLD_ANKOU_MASK, 10))
                        add(Item(GOLD_ANKOU_TOP, 10))
                        add(Item(WHITE_ANKOU_SOCKS, 10))
                        add(Item(WHITE_ANKOU_GLOVES, 10))
                        add(Item(WHITE_ANKOUS_LEGGINGS, 10))
                        add(Item(WHITE_ANKOU_MASK, 10))
                        add(Item(WHITE_ANKOU_TOP, 10))
                        add(Item(BLACK_ANKOU_SOCKS, 10))
                        add(Item(BLACK_ANKOU_GLOVES, 10))
                        add(Item(BLACK_ANKOUS_LEGGINGS, 10))
                        add(Item(BLACK_ANKOU_MASK, 10))
                        add(Item(BLACK_ANKOU_TOP, 10))
                        add(Item(WHITE_SANTA_HAT, 10))
                        add(Item(WHITE_HWEEN_MASK, 10))
                        add(Item(BLACK_SANTA_HAT_32714, 10))
                        add(Item(BLACK_HWEEN_MASK_32715, 10))
                        add(Item(BLACK_PARTY_HAT, 10))
                        add(Item(LIME_PARTY_HAT, 10))
                        add(Item(LIME_SANTA_HAT, 10))
                        add(Item(LIME_HWEEN_MASK, 10))
                        add(Item(PINK_SANTA_HAT, 10))
                        add(Item(PINK_HWEEN_MASK, 10))
                        add(Item(PINK_PARTY_HAT, 10))
                        add(Item(CYAN_SANTA_HAT, 10))
                        add(Item(CYAN_HWEEN_MASK, 10))
                        add(Item(CYAN_PARTY_HAT, 10))
                        add(Item(BLUE_SANTA_HAT, 10))
                        add(Item(PURPLE_SANTA_HAT, 10))
                        add(Item(PURPLE_HWEEN_MASK, 10))
                        add(Item(ORANGE_SANTA_HAT, 10))
                        add(Item(ORANGE_HWEEN_MASK, 10))
                        add(Item(ORANGE_PARTY_HAT, 10))
                        add(Item(LIGHT_PURPLE_SANTA_HAT, 10))
                        add(Item(LIGHT_PURPLE_HWEEN_MASK, 10))
                        add(Item(LIGHT_PURPLE_PARTY_HAT, 10))
                        add(Item(LIGHT_BLUE_HWEEN_MASK, 10))
                        add(Item(LIGHT_BLUE_PARTY_HAT, 10))
                        add(Item(LIGHT_BLUE_SANTA_HAT, 10))
                        add(Item(PEACH_SANTA_HAT, 10))
                        add(Item(PEACH_HWEEN_MASK, 10))
                        add(Item(PEACH_PARTY_HAT, 10))
                        add(Item(LIGHT_GREEN_SANTA_HAT, 10))
                        add(Item(LIGHT_GREEN_HWEEN_MASK, 10))
                        add(Item(LIGHT_GREEN_PARTY_HAT, 10))
                        add(Item(LIGHT_YELLOW_SANTA_HAT, 10))
                        add(Item(LIGHT_YELLOW_HWEEN_MASK, 10))
                        add(Item(LIGHT_YELLOW_PARTY_HAT, 10))
                        add(Item(CREAM_SANTA_HAT, 10))
                        add(Item(CREAM_HWEEN_MASK, 10))
                        add(Item(CREAM_PARTY_HAT, 10))
                        add(Item(MAGMA_HWEEN_MASK, 10))
                        add(Item(MAGMA_SANTA_HAT, 10))
                        add(Item(INVERTED_SANTA_HAT_32752, 10))
                        add(Item(GREY_SANTA_HAT_T, 10))
                        add(Item(SHADOW_SANTA_HAT, 10))
                        add(Item(SHADOW_BUNNY_EARS, 10))
                        add(Item(GREY_BUNNY_EARS, 10))
                    }
                }
            }
        }
    }
}
