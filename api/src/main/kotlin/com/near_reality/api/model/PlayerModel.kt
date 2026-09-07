package com.near_reality.api.model

import com.near_reality.api.model.ItemContainer.Policy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val PlayerData.totalVoteCredits: Int by attribute("vote_points", 0)
val PlayerData.registered: Boolean by attribute<PlayerData, Int>("registered", 0).asBoolean()

@Serializable
data class PlayerData(
    val playerInformation: MetaInformation,
    val appearance: Appearance = Appearance.DEFAULT_MALE,
    val skills: Skills = Skills(),
    val equipment: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val inventory: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val runePouch: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val seedBox: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val lootingBag: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val herbSack: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val bonePouch: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val dragonhidePouch: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val gemBag: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val retrievalService: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val privateStorage: ItemContainerWrapper = ItemContainerWrapper(Policy.NORMAL),
    val bank: ItemContainerWrapper = ItemContainerWrapper(Policy.ALWAYS_STACK),
    val attrPersistence: AttrPersistence = AttrPersistence(),
    override val attributes: Attributes = Attributes()
) : AttributeHolder {

    val username get() = playerInformation.username

    /**
     * Post-migration saves keep the retrieval-service and CoX
     * private-storage containers under attrPersistence["item_retrieval"] /
     * ["private_storage"] (same object shape as the legacy top-level keys,
     * which Gson omits once the live player no longer populates the
     * deprecated fields). Prefer the attr copy; fall back to the legacy
     * top-level key for pre-migration saves. A save never carries both.
     */
    val effectiveRetrievalService get() = attrPersistence.itemRetrieval ?: retrievalService
    val effectivePrivateStorage get() = attrPersistence.privateStorage ?: privateStorage

    val containerWrapperList get() = listOf(equipment, inventory, runePouch, seedBox, lootingBag, herbSack, bonePouch, dragonhidePouch, gemBag, effectiveRetrievalService, effectivePrivateStorage, bank)

    /**
     * The subset of attrPersistence this model consumes. Other persisted
     * attr keys (stash, god_books, ...) are ignored by the decoder's
     * unknown-key handling, exactly like every other save key this model
     * does not map. The stored values are the full service objects; only
     * their container halves are modelled here.
     */
    @Serializable
    data class AttrPersistence(
        @SerialName("item_retrieval")
        val itemRetrieval: ItemContainerWrapper? = null,
        @SerialName("private_storage")
        val privateStorage: ItemContainerWrapper? = null,
    )

    @Serializable
    data class MetaInformation(
        val username: String
    )

    @Serializable
    data class Skills(
        @SerialName("level")
        val levels: List<Int> = Skill.entries.map { it.defaultLevel },
        @SerialName("experience")
        val experiences: List<Double> = Skill.entries.map { it.defaultExperience.toDouble() },
    )


    @Serializable
    data class Appearance(
        @SerialName("appearance")
        val kitIds: List<Int>,
        @SerialName("colours")
        val colours: List<Int>,
        val male: Boolean,
        val headIcon: Int = -1,
    ) {

        companion object {
            val DEFAULT_MALE = Appearance(
                kitIds = listOf(0, 10, 18, 26, 33, 36, 42),
                colours = listOf(0, 0, 0, 0, 0),
                male = true,
            )
            val DEFAULT_FEMALE = Appearance(
                kitIds = listOf(45, 1000, 56, 61, 68, 70, 79),
                colours = listOf(0, 0, 0, 0, 0),
                male = false
            )
        }
    }
}
