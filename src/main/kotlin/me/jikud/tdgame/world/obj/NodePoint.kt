package me.jikud.tdgame.world.obj

import me.jikud.tdgame.helpers.PPoint

open class NodePoint (
    pos: PPoint,
    name: String,
    color: Int
) : TileObj(pos, name, size = 0f, color) {
    companion object { var count = 0 }

    init {
        count++
    }
    var visitorsList = ArrayList<String>()

}