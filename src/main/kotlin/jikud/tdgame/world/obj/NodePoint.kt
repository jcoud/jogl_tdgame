package jikud.tdgame.world.obj

import jikud.tdgame.helpers.PPoint

open class NodePoint (
    override var pos: PPoint,
    override var name: String,
    override var color: Int
) : TileObj(pos, name, size = 0f, color) {
    companion object { var count = 0 }

    var visitorsList = ArrayList<String>()

}