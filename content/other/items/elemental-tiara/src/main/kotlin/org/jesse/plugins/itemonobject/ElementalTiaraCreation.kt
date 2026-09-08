package org.jesse.plugins.itemonobject

import org.jesse.game.content.achievementdiary.diaries.FaladorDiary
import org.jesse.game.content.skills.runecrafting.BasicRunecraftingAction
import org.jesse.game.content.skills.runecrafting.Runecrafting
import org.jesse.game.content.skills.runecrafting.Tiara
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnObjectAction
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.`object`.WorldObject

/**
 * Handles the creation of elemental tiara's.
 *
 * To create a talisman tiara, the player must have a tiara and the corresponding talisman in their inventory,
 * and use the talisman or tiara on the altar of that particular rune.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class ElementalTiaraCreation : ItemOnObjectAction {

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject) {

        val elementalTiara = ElementalTiara.findByElementalTalisMan(player)
        val tiaraItem = player.inventory.getAny(TIARA)

        if (elementalTiara != null && tiaraItem != null) {

            if (`object`.id != elementalTiara.runecrafting.altarObjectId) {
                player.dialogue { plain("You can only create elemental tiara's on the altar corresponding to the element.") }
                return
            }

            if (player.inventory.deleteItems(
                    Item(elementalTiara.talisManItemId),
                    tiaraItem
                ).result == RequestResult.SUCCESS
            ) {
                player.animation = BasicRunecraftingAction.RUNECRAFTING_ANIM
                player.graphics = BasicRunecraftingAction.RUNECRAFTING_GFX
                player.lock(2)
                WorldTasksManager.schedule({
                    if (elementalTiara == ElementalTiara.MIND_TIARA) {
                        player.achievementDiaries.update(FaladorDiary.MAKE_MIND_TIARA)
                    }
                    player.skills.addXp(SkillConstants.RUNECRAFTING, 45.0)
                    player.inventory.addItem(Item(elementalTiara.elementalTiaraItemId))
                    val elementType = elementalTiara.name.substringBefore("_").lowercase()
                    player.dialogue {
                        doubleItem(
                            elementalTiara.talisManItemId, elementalTiara.elementalTiaraItemId,
                            "You created an $elementType tiara by combining your tiara with an $elementType talisman."
                        )
                    }
                }, 2)
            }
        } else
            player.dialogue { plain("You need both an elemental talisman and a tiara in order to create an elemental tiara.") }
    }

    override fun getItems() = ElementalTiara.values()
        .map { it.talisManItemId }
        .toTypedArray() + TIARA

    override fun getObjects() = Runecrafting.VALUES
        .map { it.altarObjectId }
        .filter { it != -1 }
        .toTypedArray()

    private enum class ElementalTiara(
        val runecrafting: Runecrafting,
        val talisManItemId: Int,
        val elementalTiaraItemId: Int,
    ) {
        AIR_TIARA(Runecrafting.AIR_RUNE, AIR_TALISMAN, org.jesse.game.item.ids.AIR_TIARA),
        BLOOD_TIARA(Runecrafting.BLOOD_RUNE, BLOOD_TALISMAN, org.jesse.game.item.ids.BLOOD_TIARA),
        BODY_TIARA(Runecrafting.BODY_RUNE, BODY_TALISMAN, org.jesse.game.item.ids.BODY_TIARA),
        CHAOS_TIARA(Runecrafting.CHAOS_RUNE, CHAOS_TALISMAN, org.jesse.game.item.ids.CHAOS_TIARA),
        COSMIC_TIARA(Runecrafting.COSMIC_RUNE, COSMIC_TALISMAN, org.jesse.game.item.ids.COSMIC_TIARA),
        DEATH_TIARA(Runecrafting.DEATH_RUNE, DEATH_TALISMAN, org.jesse.game.item.ids.DEATH_TIARA),
        EARTH_TIARA(Runecrafting.EARTH_RUNE, EARTH_TALISMAN, org.jesse.game.item.ids.EARTH_TIARA),
        FIRE_TIARA(Runecrafting.FIRE_RUNE, FIRE_TALISMAN, org.jesse.game.item.ids.FIRE_TIARA),
        LAW_TIARA(Runecrafting.LAW_RUNE, LAW_TALISMAN, org.jesse.game.item.ids.LAW_TIARA),
        MIND_TIARA(Runecrafting.MIND_RUNE, MIND_TALISMAN, org.jesse.game.item.ids.MIND_TIARA),
        NATURE_TIARA(Runecrafting.NATURE_RUNE, NATURE_TALISMAN, org.jesse.game.item.ids.NATURE_TIARA),
        WATER_TIARA(Runecrafting.WATER_RUNE, WATER_TALISMAN, org.jesse.game.item.ids.WATER_TIARA),
        WRATH_TIARA(Runecrafting.WRATH_RUNE, WRATH_TALISMAN, org.jesse.game.item.ids.WRATH_TIARA);

        companion object {
            fun findByElementalTalisMan(player: Player) = values()
                .find { player.inventory.containsItem(it.talisManItemId) }
        }
    }
}
