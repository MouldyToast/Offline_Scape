@file:Suppress("unused")

package org.jesse.api.resources

import org.jesse.api.model.SanctionLevel
import io.ktor.resources.*

@Resource("/sanction")
class Sanction {

    @Resource("submit")
    class Submit(val parent: Sanction = Sanction(), val sendCallback: Boolean = false)

    @Resource("revoke")
    class Revoke(val parent: Sanction = Sanction(), val id: Long, val level: SanctionLevel, val sendCallback: Boolean = false)

    @Resource("callback")
    class Callback(val parent: Sanction = Sanction()) {

        @Resource("submit")
        class Submit(val parent: Callback = Callback())

        @Resource("revoke")
        class Revoke(val parent: Callback = Callback())
    }
}
