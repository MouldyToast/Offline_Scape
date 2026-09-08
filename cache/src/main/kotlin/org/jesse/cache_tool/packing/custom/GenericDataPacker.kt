package org.jesse.cache_tool.packing.custom

import mgi.tools.jagcached.cache.Cache
import mgi.tools.jagcached.cache.Group
import mgi.utilities.ByteBuffer
import java.io.File

object GenericDataPacker {
    fun packAll(cache: Cache, path: String) {
        val dirs = File(path).listFiles()
        for(dir in dirs) {
            println("Packing ${dir.path}")
            pack(cache, dir.path)
        }
    }
    fun pack(cache: Cache, path: String) {
        val archives = File(path).listFiles()
        if (archives != null) {
            for (archiveFolder in archives) {
                val archiveId = archiveFolder.name.substringAfter("_").toIntOrNull() ?: continue
                val archive = cache.getArchive(archiveId)
                val groupsOrFiles = archiveFolder.listFiles()
                if (groupsOrFiles != null) {
                    for (dir in groupsOrFiles) {

                        if (dir.isFile) {// Pack group
                            val groupId = dir.name.substringAfter('_').toIntOrNull() ?: continue
                            var group = archive.findGroupByID(groupId)
                            if (group == null) {
                                group = Group(groupId, mgi.tools.jagcached.cache.File(ByteBuffer(dir.readBytes())))
                                archive.addGroup(group)
                            } else {
                                group.findFileByID(0).data = ByteBuffer(dir.readBytes())
                            }

                            println("Packed $path, archive $archiveId, group $groupId")
                            group.recalculate()
                        } else {// Pack files

                            val groupId = dir.name.substringAfter('_').toIntOrNull() ?: continue
                            var group = archive.findGroupByID(groupId)
                            if (group == null) {
                                group = Group(groupId)
                                archive.addGroup(group)
                            }
                            val files = dir.listFiles() ?: continue
                            for (fileFile in files) {
                                val fileId = fileFile.name.substringAfter("_").substringBefore('.').toIntOrNull() ?: continue
                                val file = mgi.tools.jagcached.cache.File(fileId, ByteBuffer(fileFile.readBytes()))
                                group.addFile(file)
                                println("Packed $path, archive $archiveId, group $groupId, file $fileId")
                            }
                            group.recalculate()
                        }
                    }
                }
                archive.finish()
            }
        }
    }
}