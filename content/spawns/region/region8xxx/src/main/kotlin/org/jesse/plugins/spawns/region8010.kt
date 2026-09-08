package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region8010 : NPCSpawnsScript() {

    init {
        STRANGE_WATCHER_334(2006, 4755, 0, SOUTH, 0)
        STRANGE_WATCHER_333(2012, 4754, 0, SOUTH, 0)
        STRANGE_WATCHER(2015, 4756, 0, SOUTH, 0)
        MIME(2011, 4762, 0, SOUTH, 0)
    }
}
