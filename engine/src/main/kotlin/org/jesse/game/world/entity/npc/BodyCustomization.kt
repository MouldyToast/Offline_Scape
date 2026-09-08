package org.jesse.game.world.entity.npc

import mgi.types.config.npcs.NPCDefinitions

class BodyCustomization(
    val id: Int,
    var models: IntArray? = null,
    var recolors: ShortArray? = null,
    var retextures: ShortArray? = null,
    var localPlayer: Boolean? = null
) {
    val empty: Boolean
        get() = id == -1 || (models == null && recolors == null && retextures == null && localPlayer == null)

    init {
        val definitions = NPCDefinitions.get(id)
        val recolors = recolors
        if (recolors != null && recolors.isNotEmpty()) {
            check(recolors.size == definitions.replacementColours.size) {
                "Invalid recolors length for npc: $id"
            }
        }
        val retextures = retextures
        if (retextures != null && retextures.isNotEmpty()) {
            check(retextures.size == definitions.replacementTextures.size) { "Invalid retextures length for npc: $id" }
        }
    }

    fun remodel(models: IntArray) {
        check(id != -1)
        this.models = models
    }

    fun recolor(index: Int, color: Int) {
        check(id != -1)
        val definitions = NPCDefinitions.get(id)
        var recolors = recolors
        if (recolors == null || recolors.isEmpty()) {
            check(definitions.replacementColours != null) { "Cannot recolor this npc: $id" }
            recolors = definitions.replacementColours.copyOf()
            this.recolors = recolors
        }
        recolors[index] = color.toShort()
    }

    fun retexture(index: Int, texture: Int) {
        check(id != -1)
        val definitions = NPCDefinitions.get(id)
        var retextures = retextures
        if (retextures == null || retextures.isEmpty()) {
            check(definitions.replacementTextures != null) { "Cannot retexture this npc: $id" }
            retextures = definitions.replacementTextures.copyOf()
            this.retextures = retextures
        }
        retextures[index] = texture.toShort()
    }

    fun localPlayer(localPlayer: Boolean) {
        check(id != -1)
        this.localPlayer = localPlayer
    }

    companion object {
        @JvmStatic
        fun build(
            id: Int,
            models: IntArray? = null,
            recolors: ShortArray? = null,
            retextures: ShortArray? = null,
            localPlayer: Boolean? = null
        ): BodyCustomization {
            return BodyCustomization(id, models, recolors, retextures, localPlayer)
        }
    }
}
