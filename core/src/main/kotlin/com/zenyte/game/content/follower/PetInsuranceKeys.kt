@file:JvmName("PetInsuranceKeys")

package com.zenyte.game.content.follower

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted pet-insurance state. Saved under attrPersistence["pet_insurance"]
 * as the [PetInsurance] instance itself; Gson skips the transient player
 * back-reference, so only the insured/claimable id sets round-trip.
 */
@JvmField
val PET_INSURANCE_KEY: AttributeKey<PetInsurance> = AttributeKey(persistenceKey = "pet_insurance")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new PetInsurance(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. Never returns null.
 */
fun petInsurance(player: Player): PetInsurance {
    val raw = rawPetInsuranceAttr(player)
    if (raw is PetInsurance) {
        return raw
    }
    val insurance = PetInsurance(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), PetInsurance::class.java)
        insurance.initialize(typed)
    }
    player.attr[PET_INSURANCE_KEY] = insurance
    return insurance
}

/**
 * Untyped view of the value stored under [PET_INSURANCE_KEY]; see
 * SeedVaultKeys.rawSeedVaultAttr for why this exists.
 */
fun rawPetInsuranceAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[PET_INSURANCE_KEY as AttributeKey<Any>]
}
