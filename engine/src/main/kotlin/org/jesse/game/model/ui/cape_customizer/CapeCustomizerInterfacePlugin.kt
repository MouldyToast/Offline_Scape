package org.jesse.game.model.ui.cape_customizer

import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface
import org.jesse.game.util.JagexColor
import org.jesse.game.world.entity.masks.UpdateFlag
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.VarManager
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot
import org.jesse.game.item.ids.*

// Stage 5c: master comp cape item defs removed; the customizer itself is 5d scope.
// Local ids replace the deleted ItemId tail constants so this file keeps compiling.
private const val MASTER_COMP_CAPE = 32614
private const val MASTER_COMP_HOOD = 32617

/**
 * @author <a href="https://github.com/heavens">mack</a>
 */

class CapeCustomizerInterfacePlugin() : Interface() {

    init {
        for (preset in presetVars) {
            for (varp in preset) {
                VarManager.appendPersistentVarp(varp)
            }
        }
        for (varp in detailVars) {
            VarManager.appendPersistentVarp(varp)
        }
    }

    override fun attach() {
    }

    override fun open(player: Player) {
        super.open(player)
        for (preset in presetVars) {
            for (varp in preset) {
                player.syncVarp(varp)
            }
        }

        // Transmit cape colors immediately in case this is the first time recoloring
        // Also needed for force equipping the hood if it is ever added
        player.transmitCapeRecolors()
    }

    private fun Player.syncVarp(varp: Int) = varManager.sendVarInstant(varp, varManager.getValue(varp))

    override fun build() {

    }

    override fun getInterface(): GameInterface = GameInterface.CAPE_CUSTOMIZER

    companion object {

        private val preset1Vars = IntArray(4) { 29496 + it }

        private val preset2Vars = IntArray(4) { 29500 + it }

        private val preset3Vars = IntArray(4) { 29504 + it }

        private val preset4Vars = IntArray(4) { 29508 + it }

        private val preset5Vars = IntArray(4) { 29512 + it }

        private val preset6Vars = IntArray(4) { 29516 + it }

        val presetVars = arrayOf(
            preset1Vars,
            preset2Vars,
            preset3Vars,
            preset4Vars,
            preset5Vars,
            preset6Vars
        )

        val detailVars = IntArray(4) {
            29520 + it
        }

        @JvmStatic
        fun onResumeString(player: Player, value: String) {
            val parts = value.split(",")
            when (val key = parts[0]) {
                "save" -> {
                    val index = parts[1].toInt()
                    val preset = presetVars[index.coerceIn(0, presetVars.size - 1)]
                    for ((idx, varb) in preset.withIndex()) {
                        player.varManager.sendVar(varb, player.varManager.getValue(detailVars[idx]))

                    }
                }
                "load" -> {
                    val index = parts[1].toInt()
                    val preset = presetVars[index.coerceIn(0, presetVars.size - 1)]
                    for ((idx, varb) in detailVars.withIndex()) {
                        val color = player.varManager.getValue(preset[idx])
                        player.varManager.sendVar(varb, color)
                        recolorCape(player, JagexColor.rgbToHSL(color, 1.0).toInt(), idx)
                    }
                }
                else -> {// These need to update colors on the cape
                    val slot = key.toInt()
                    val hslColor = parts[1].toInt()
                    val jagColor = parts[2].toInt()
                    val varb = detailVars[slot]
                    player.varManager.sendVar(varb, hslColor)
                    recolorCape(player, jagColor, slot)
                }
            }
            player.updateFlags.flag(UpdateFlag.APPEARANCE)
        }

        private fun recolorCape(player: Player, recol: Int, slot: Int) {
            player.appearance.forcedAppearance.put(EquipmentSlot.CAPE.slot.toByte(), MASTER_COMP_CAPE.toShort())
//            player.appearance.forcedAppearance.put(EquipmentSlot.HELMET.slot.toByte(), MASTER_COMP_HOOD_T.toShort())
            if (slot < 2) {
                player.appearance.recol1(MASTER_COMP_CAPE, EquipmentSlot.CAPE.slot, slot, recol)
                player.appearance.recol1(MASTER_COMP_HOOD, EquipmentSlot.HELMET.slot, slot, recol)
            } else {
                player.appearance.recol2(MASTER_COMP_CAPE, EquipmentSlot.CAPE.slot, slot - 2, recol)
                player.appearance.recol2(MASTER_COMP_HOOD, EquipmentSlot.HELMET.slot, slot - 2, recol)
            }

        }

        @JvmStatic
        fun Player.transmitCapeRecolors() {
            detailVars.asSequence().withIndex()
                .map { (idx, varp) -> idx to JagexColor.rgbToHSL(varManager.getValue(varp), 1.0).toInt() }
                .filter { (_, color) -> color != 0 }
                .forEach { (index, color) ->
                    recolorCape(this, color, index)
                }
            updateFlags.flag(UpdateFlag.APPEARANCE)
        }
    }
}