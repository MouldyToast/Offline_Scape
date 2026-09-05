@file:JvmName("GrotesqueGuardiansKeys")

package com.zenyte.game.content.boss.grotesqueguardians

import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance
import org.rsmod.api.attr.AttributeKey

/**
 * Transient session key for the player's active Grotesque Guardians
 * instance. No persistenceKey: instances never survive a session.
 */
@JvmField
val GARG_INSTANCE_KEY: AttributeKey<GrotesqueGuardiansInstance> = AttributeKey()
