package jikud.tdgame.world

import jikud.tdgame.JOGLEntry
import jikud.tdgame.core.Drawing
import jikud.tdgame.world.obj.Entity
import java.util.*

class FieldTile(private var gridpos_x: Int, private var gridpos_y: Int) {
    val entities = LinkedList<Entity>()

    fun isEmpty() = entities.isEmpty()
    fun add(e: Entity) = entities.add(e)
    fun clear() = entities.clear()

    fun rect() {
        JOGLEntry.GRF.glLineWidth(1f)
        JOGLEntry.GRF.glColor4f(.9f, .55f, .9f, .4f)
        Drawing.drawLineRect(
            gridpos_x * Field.gridSize * 1f,
            gridpos_y * Field.gridSize * 1f,
            Field.gridSize * 1f,
            Field.gridSize * 1f
        )
//        GL11.glLoadIdentity()
    }

    fun highlight() {
        if (isEmpty()) return
        JOGLEntry.GRF.glColor3f(0f, 0f, 0f)
        Drawing.drawLineRect(
            gridpos_x * Field.gridSize * 1f,
            gridpos_y * Field.gridSize * 1f,
            Field.gridSize * 1f,
            Field.gridSize * 1f,
            2f
        )
//        GL11.glLoadIdentity()
    }
}