package me.jikud.tdgame.world.field

import me.jikud.engine.core.gui.GuiCore
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.engine.core.main.GLRenderHelper
import me.jikud.engine.core.main.GLRenderHelper.JGL
import me.jikud.engine.core.main.GLRenderHelper.Rect
import me.jikud.engine.core.main.IOComponent
import me.jikud.tdgame.TDMain
import me.jikud.tdgame.Global
import me.jikud.tdgame.gui.sets.editor.EditorGuiGroup
import me.jikud.tdgame.world.obj.TileObj
import java.awt.Color

object FieldDraw {

    private fun showObj() {
        Field.entityListOrder.forEach(TileObj::draw)
        Field.towerListOrder.forEach(TileObj::draw)
        Field.nodeListOrder.forEach(TileObj::draw)
    }

    private fun drawWorld() {
        drawGrid()
        if (GuiCore.currentGui is EditorGuiGroup || Global.debugMode) {
            drawCursorGridTileHovering()
            drawNodeConnections()
            Field.gridScan.forEach { it.forEach(FieldTile::rect) }
            Field.gridScan.forEach { it.forEach(FieldTile::highlight) }
        }
        showObj()
    }


    private fun drawCursorGridTileHovering() {
        if (IOComponent.y < TDMain.guiWithGapHeight) return
        val p = PPoint(IOComponent.x, IOComponent.y)
        JGL.glColor4f(.4f, .4f, .4f, .8f)
        val d = p.devide(TDMain.bs)
        Rect(d.xi * TDMain.bs * 1f, d.yi * TDMain.bs * 1f + TDMain.fieldHeightGap, TDMain.bs * 1f, TDMain.bs * 1f, true)
    }

    private fun drawNodeConnections() {
        if (Field.nodeListOrder.size < 2) return
        for (n in 1 until Field.nodeListOrder.size)
            drawArrowLine(
                CColor(Color.GRAY.brighter()),
                Field.nodeListOrder[n - 1].center,
                Field.nodeListOrder[n].center
            )
    }

    private fun drawArrowLine(c: CColor, pos1: PPoint, pos2: PPoint) {
        drawArrowLine(c, pos1.xi, pos1.yi, pos2.xi, pos2.yi)
    }

    private fun drawArrowLine(c: CColor, x1: Int, y1: Int, x2: Int, y2: Int) {
        JGL.glColor4f(c.r, c.g, c.b, c.a)
        GLRenderHelper.Line(x1 * 1f, y1 * 1f, x2 * 1f, y2 * 1f)
//        val magn = sqrt(Point2D.distanceSq(x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble())) * 0.1
//        val a = atan2(y2 - y1, x2 - x1)
//        val l1X = (x2 - magn * cos(a + Math.toRadians(45.0))).toInt()
//        val l1Y = (y2 - magn * sin(a + Math.toRadians(45.0))).toInt()
//        val l2X = (x2 - magn * cos(a - Math.toRadians(45.0))).toInt()
//        val l2Y = (y2 - magn * sin(a - Math.toRadians(45.0))).toInt()
//        g.drawLine(l1X, l1Y, x2.toInt(), y2.toInt())
//        g.drawLine(l2X, l2Y, x2.toInt(), y2.toInt())
    }

    private fun drawGrid() {
        for (y in 0 until TDMain.n) {
            for (x in 0 until TDMain.n) {
                JGL.glTranslatef(0f, TDMain.guiWithGapHeight * 1f, 0f)
                JGL.glColor4f(.2f, .2f, .2f, .4f)
                Rect(x * TDMain.bs * 1f, y * TDMain.bs * 1f, TDMain.bs * 1f, TDMain.bs * 1f, false)
            }
        }
    }

    fun repaintProcess() {
        JGL.glTranslatef(0f, TDMain.guiWithGapHeight * 1f, 0f)
        JGL.glColor3f(0.3f, 0.3f, 0.3f)
        Rect(0f, 0f, TDMain.fieldWidth * 1f, TDMain.fieldHeight * 1f, true)
        drawWorld()
    }
}