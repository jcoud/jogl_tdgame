package jikud.tdgame.world

import jikud.tdgame.core.Drawing
import jikud.tdgame.world.obj.Entity
import java.util.*

class FieldTile(private var gridPos_x: Int, private var gridPos_y: Int) {
    val entities = LinkedList<Entity>()

    fun isEmpty() = entities.isEmpty()
    fun add(e: Entity) = entities.add(e)
    fun clear() = entities.clear()

    fun rect() {
        Drawing.GL.glLineWidth(1f)
        Drawing.GL.glColor4f(.9f, .55f, .9f, .4f)
        Drawing.Rect(
            gridPos_x * Field.gridSize * 1f,
            gridPos_y * Field.gridSize * 1f,
            Field.gridSize * 1f,
            Field.gridSize * 1f,
            false
        )
//        GL11.glLoadIdentity()
    }

    fun highlight() {
        if (isEmpty()) return
        Drawing.GL.glColor3f(0f, 0f, 0f)
        Drawing.Rect(
            gridPos_x * Field.gridSize * 1f,
            gridPos_y * Field.gridSize * 1f,
            Field.gridSize * 1f,
            Field.gridSize * 1f,
            false,
            2f
        )
//        GL11.glLoadIdentity()
    }
}