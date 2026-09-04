package com.near_reality.plugins.interfaces.characterdesign

import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.player.Player
import mgi.types.config.identitykit.IdentityKitDefinitions
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class CharacterDesignerInterface : InterfaceScript() {

    fun Player.changeDesign(design: CharacterCreatorDesign, next: Boolean) {
        if (design == CharacterCreatorDesign.Jaw && !appearance.isMale) return
        val kits = IdentityKitDefinitions.definitions
        var current = appearance.appearance[design.id].toInt()
        val genderOffset = if (appearance.isMale) 0 else 7
        var nextKit: IdentityKitDefinitions?
        do {
            if (next) {
                current++
                if (current >= kits.size) current = 0
            } else {
                current--
                if (current < 0) current = kits.size - 1
            }
            nextKit = IdentityKitDefinitions.get(current)
            val partId = nextKit?.bodyPartId
        } while (nextKit == null || !nextKit.isSelectable || partId == null || (design.id + genderOffset) != partId)
        appearance.appearance[design.id] = nextKit.id.toShort()
        updateFlags.flag(UpdateFlag.APPEARANCE)
    }

    fun Player.changeColour(colour: CharacterCreatorColour, next: Boolean) {
        var current = appearance.colours[colour.id].toInt()
        var found: Boolean
        do {
            if (next) {
                current++
                if (current >= colour.count) current = 0
            } else {
                current--
                if (current < 0) current = colour.count - 1
            }
            found = true
        } while (!found)
        appearance.colours[colour.id] = current.toByte()
        updateFlags.flag(UpdateFlag.APPEARANCE)
    }

    fun Player.changeGender(male: Boolean) {
        appearance.isMale = male
        varManager.sendBitInstant(14021, if (male) 0 else 1)
        varManager.sendBitInstant(10988, if (male) 0 else 1)
        for (design in enumValues<CharacterCreatorDesign>()) {
            if (design == CharacterCreatorDesign.Jaw) {
                appearance.appearance[design.id] = if (male) 10 else 1000
                continue
            }
            val parts = if (!male) design.maleParts else design.femaleParts
            val oppositeParts = if (!male) design.femaleParts else design.maleParts
            val current = IdentityKitDefinitions.get(appearance.appearance[design.id].toInt())
            val index = parts.indexOf(current)
            val next = oppositeParts.getOrNull(index) ?: oppositeParts.first()
            appearance.appearance[design.id] = next.id.toShort()
        }
        updateFlags.flag(UpdateFlag.APPEARANCE)
    }

    fun Player.confirm() = interfaceHandler.closeInterfaces()

    enum class CharacterCreatorDesign(val id: Int) {
        Head(0),
        Jaw(1),
        Torso(2),
        Arms(3),
        Hands(4),
        Legs(5),
        Feet(6);

        private val filteredConfigValues: List<IdentityKitDefinitions> get() = IdentityKitDefinitions.definitions.filterNot { !it.isSelectable }
        val maleParts = filteredConfigValues.filter { it.bodyPartId == id }
        val femaleParts = filteredConfigValues.filter { it.bodyPartId == id + 7 }
    }

    enum class CharacterCreatorColour(val id: Int, val count: Int) {
        Hair(0, 25),
        Torso(1, 29),
        Legs(2, 29),
        Feet(3, 6),
        Skin(4, 13),
    }

    init {
        /**
         * @author Kris | 14/06/2022
         */
        CHARACTER_DESIGN {
            "Previous head"(12 + 3) { player.changeDesign(CharacterCreatorDesign.Head, false) }
            "Next head"(13 + 3) { player.changeDesign(CharacterCreatorDesign.Head, true) }
            "Previous jaw"(16 + 3) { player.changeDesign(CharacterCreatorDesign.Jaw, false) }
            "Next jaw"(17 + 3) { player.changeDesign(CharacterCreatorDesign.Jaw, true) }
            "Previous torso"(20 + 3) { player.changeDesign(CharacterCreatorDesign.Torso, false) }
            "Next torso"(21 + 3) { player.changeDesign(CharacterCreatorDesign.Torso, true) }
            "Previous arms"(24 + 3) { player.changeDesign(CharacterCreatorDesign.Arms, false) }
            "Next arms"(25 + 3) { player.changeDesign(CharacterCreatorDesign.Arms, true) }
            "Previous hands"(28 + 3) { player.changeDesign(CharacterCreatorDesign.Hands, false) }
            "Next hands"(29 + 3) { player.changeDesign(CharacterCreatorDesign.Hands, true) }
            "Previous legs"(32 + 3) { player.changeDesign(CharacterCreatorDesign.Legs, false) }
            "Next legs"(33 + 3) { player.changeDesign(CharacterCreatorDesign.Legs, true) }
            "Previous feet"(36 + 3) { player.changeDesign(CharacterCreatorDesign.Feet, false) }
            "Next feet"(37 + 3) { player.changeDesign(CharacterCreatorDesign.Feet, true) }

            "Previous hair colour"(43 + 3) { player.changeColour(CharacterCreatorColour.Hair, false) }
            "Next hair colour"(44 + 3) { player.changeColour(CharacterCreatorColour.Hair, true) }
            "Previous torso colour"(47 + 3) { player.changeColour(CharacterCreatorColour.Torso, false) }
            "Next torso colour"(48 + 3) { player.changeColour(CharacterCreatorColour.Torso, true) }
            "Previous legs colour"(51 + 3) { player.changeColour(CharacterCreatorColour.Legs, false) }
            "Next legs colour"(52 + 3) { player.changeColour(CharacterCreatorColour.Legs, true) }
            "Previous feet colour"(55 + 3) { player.changeColour(CharacterCreatorColour.Feet, false) }
            "Next feet colour"(56 + 3) { player.changeColour(CharacterCreatorColour.Feet, true) }
            "Previous skin colour"(59 + 3) { player.changeColour(CharacterCreatorColour.Skin, false) }
            "Next skin colour"(60 + 3) { player.changeColour(CharacterCreatorColour.Skin, true) }

            "Male"(65 + 3) { player.changeGender(true) }
            "Female"(66 + 3) { player.changeGender(false) }
            "Confirm"(74) { player.confirm() }

            "Pronoun"(78) {
                when (slotID) {
                    1 -> { player.changeGender(true) }
                    2 -> { player.changeGender(false) }
                    3 -> { /* They/them */ }
                }
            }

            opened {
                sendInterface()

                packetDispatcher.sendComponentSettings(
                    `interface`.id,
                    78,
                    0,
                    200,
                    CLICK_OP1
                )
            }
        }
    }
}
