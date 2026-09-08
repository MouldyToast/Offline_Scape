package org.jesse.cache

import mgi.tools.jagcached.cache.Archive
import mgi.tools.jagcached.cache.Cache
import mgi.tools.jagcached.cache.Group

sealed interface CacheOperation {

    fun Cache.apply()
    fun Cache.undo()


    sealed class LazyBinaryWrite<O>(
        private val archiveType: CacheArchiveType,
        val lazyData: () -> O
    ) : CacheOperation {

        protected val Cache.archive get() = getArchive(archiveType.id)?: error("No archive found for: $archiveType")

        class Group(
            archiveType: CacheArchiveType,
            lazyData: () -> mgi.tools.jagcached.cache.Group,
        ) : LazyBinaryWrite<mgi.tools.jagcached.cache.Group>(archiveType, lazyData) {

            private val group by lazy { lazyData() }
            private var overwrittenGroup: mgi.tools.jagcached.cache.Group? = null

            override fun Cache.apply() {
                overwrittenGroup = archive.groups[group.id]
                archive.addGroup(group)
            }

            override fun Cache.undo() {
                if (overwrittenGroup != null)
                    archive.addGroup(overwrittenGroup!!)
                else
                    archive.deleteGroup(group)
            }
        }
    }
}