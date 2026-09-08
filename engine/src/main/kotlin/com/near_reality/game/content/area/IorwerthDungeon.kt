package com.near_reality.game.content.area


import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.area.Tirannwn

class IorwerthDungeon : Tirannwn() {

    override fun getPolygons(): Array<RSPolygon> = arrayOf(
        RSPolygon(12994), RSPolygon(12738),
        RSPolygon(12993), RSPolygon(12737),
    )
    override fun name() = "Iorwerth Dungeon"


}
