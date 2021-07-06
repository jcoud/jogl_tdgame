package me.jikud.tdgame.world.field

import me.jikud.engine.core.main.GLRenderHelper.JGL
import me.jikud.engine.core.main.GLRenderHelper.Rect
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.world.obj.Entity
import java.util.*

class FieldTile(private var gridPos_x: Int, private var gridPos_y: Int) {
    val entities = LinkedList<Entity>()

    fun isEmpty() = entities.isEmpty()
    fun add(e: Entity) = entities.add(e)
    fun clear() = entities.clear()

    fun rect() = with(JGL) {
        glLineWidth(1f)
        glColor4f(.9f, .55f, .9f, .4f)
        Rect(
            gridPos_x * Field.gridScanTileSize * 1f,
            gridPos_y * Field.gridScanTileSize * 1f + TDMain.guiWithGapHeight,
            Field.gridScanTileSize * 1f,
            Field.gridScanTileSize * 1f,
            false
        )
//        GL11.glLoadIdentity()
    }

    fun highlight() = with(JGL) {
        if (isEmpty()) return
        glColor4f(.9f, .55f, .9f, .1f)
        Rect(
            gridPos_x * Field.gridScanTileSize * 1f,
            gridPos_y * Field.gridScanTileSize * 1f,
            Field.gridScanTileSize * 1f,
            Field.gridScanTileSize * 1f,
            true
        )
//        GL11.glLoadIdentity()
    }
}