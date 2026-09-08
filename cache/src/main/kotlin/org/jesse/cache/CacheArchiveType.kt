package org.jesse.cache

enum class CacheArchiveType(@JvmField val id: Int) {
    SKELETONS(0),
    BASES(1),
    CONFIGS(2),
    INTERFACES(3),
    SYNTHS(4),
    MAPS(5),
    MUSIC(6),
    MODELS(7),
    SPRITES(8),
    TEXTURES(9),
    BINARY(10),
    JINGLES(11),
    CLIENTSCRIPTS(12),
    FONTMETRICS(13),
    VORBIS(14),
    INSTRUMENTS(15),
    WORLDMAPDATA_LEGACY(16),
    DEFAULTS(17),
    WORLDMAPGEOGRAPHY(18),
    WORLDMAPDATA(19),
    WORLDMAPGROUND(20),
    DBTABLEINDEX(21),
    REFERENCE(255);
    fun writeSymbolNamesHashes() = this == CLIENTSCRIPTS

    companion object {
        private val typeById = entries.associateBy { it.id }

        fun of(id: Int): CacheArchiveType =
            typeById[id] ?: throw IllegalArgumentException("No archive type with id $id")
    }
}