package jikud.tdgame.core

import com.jogamp.opengl.GL2
import com.jogamp.opengl.GL2.*
import com.jogamp.opengl.util.awt.TextRenderer
import jikud.tdgame.gui.GuiCore
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.FFont
import jikud.tdgame.world.FieldDraw
import java.awt.geom.Rectangle2D
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


object Drawing {
    lateinit var GL: GL2

    class FontRendering(val font: FFont = Renderer.FONT) {
        var renderer: TextRenderer = TextRenderer(font, true)

        fun bounds(text: String): Rectangle2D {
            return renderer.getBounds(text)
        }

        fun drawString(color: CColor, text: String, x: Float, y: Float) {
            renderer.setColor(color.color)
            renderer.beginRendering(Renderer.INIT_WIDTH, Renderer.INIT_HEIGHT)
            renderer.smoothing = true
            renderer.draw(
                text,
                (x - bounds(text).width / 2).toInt(),
                (Renderer.INIT_HEIGHT - bounds(text).height / 2 - y).toInt()
            )
            renderer.endRendering()
            renderer.flush()
        }
    }
    fun Rect(x: Float, y: Float, width: Float, height: Float, fill: Boolean, lineScale: Float = 1f): Unit = with(GL) {
        glPushMatrix()
        glTranslatef(x, y, 0f)
        if (fill) glBegin(GL_QUADS)
        else {
            glLineWidth(lineScale)
            glBegin(GL_LINE_LOOP)
        }
        glVertex2f(0f, 0f)
        glVertex2f(width, 0f)
        glVertex2f(width, height)
        glVertex2f(0f, height)
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun Circle(x: Float, y: Float, r: Float, fill: Boolean, lineScale: Float = 1f) = with(GL) {
        glPushMatrix()
        glTranslatef(x, y, 0f)
        if (fill) glBegin(GL_POLYGON)
        else {
            glLineWidth(lineScale)
            glBegin(GL_LINE_LOOP)
        }
        for (i in 0..360 step 10) {
            glVertex2d(r * cos(Math.toRadians(i.toDouble())), r * sin(Math.toRadians(i.toDouble())))
        }
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun Line(x1: Float, y1: Float, x2: Float, y2: Float, lineScale: Float = 1f) = with(GL) {
        glPushMatrix()
        glLineWidth(lineScale)
        glBegin(GL_LINES)
        glVertex3f(x1, y1, 0f)
        glVertex3f(x2, y2, 0f)
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun Polygon(fax: FloatArray, fay: FloatArray, n: Int, fill: Boolean, lineScale: Float = 1f) = with(GL) {
        var ax = 0f
        var ay = 0f
        for (i in 0 until n) {
            val ii = if (i + 1 > n - 1) 0 else i + 1
            ax += fax[i] * fay[ii]
            ay += fax[ii] * fay[i]
        }
        val a = abs(ax - ay) / 2f
        var cx = 0f
        var cy = 0f
        for (i in 0 until n) {
            val ii = if (i + 1 > n - 1) 0 else i + 1
            cx += (fax[i] + fax[ii]) * (fax[i] * fay[ii] - fax[ii] * fay[i])
            cy += (fay[i] + fay[ii]) * (fax[i] * fay[ii] - fax[ii] * fay[i])
        }
        val xx = cx / (6 * a)
        val yy = cy / (6 * a)
        glPushMatrix()
        glTranslatef(xx, yy, 0f)
        if (fill) glBegin(GL_POLYGON)
        else {
            glLineWidth(lineScale)
            glBegin(GL_LINE_LOOP)
        }
        for (i in 0 until n) {
            glVertex2f(fax[i], fay[i])
        }
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    var drawWorld = false
    fun visualize() {

        GuiCore.repaintProcess()
        if (!drawWorld) return
        FieldDraw.repaintProcess()
    }

//    private fun drawCommon() {
//        glColor3f(0f, 0f, 0f)
//        draw_x = 5
//        draw_y = 5
//        drawXnYs()
//        drawDebug()
//    }
//
//    private fun drawRectString(s: String, dx: Int, dy: Int) {
//        val sw = ttf.getWidth(s)
//        val sh = ttf.height
//        glColor3f(10f, 10f, 10f)
//        fillRect(dx, dy, sw, sh)
//        glColor3f(192f, 192f, 192f)
//        ttf.drawString(dx.toFloat(), dy.toFloat(), s)
//    }
//
//    private fun drawXnYs() {
//        ttf = TrueTypeFont(font.deriveFont((font.size * .9).toFloat()), true)
//        drawRectString("[field] x: ${Input.MouseMotionIO.mx} y: ${Input.MouseMotionIO.my}", draw_x, draw_y)
//        draw_y += ttf.height
//    }
//
//    private fun drawDebug() {
//        val sl = arrayListOf(
//            "GameTime: $GameTime",
//            "entities: ${Field.entityListOrder.size}",
//            "nodes: ${Field.nodeListOrder.size}",
//            "towers: ${Field.towerListOrder.size}"
//        )
//
//        ttf = TrueTypeFont(font.deriveFont((font.size * .9).toFloat()), true)
//        for (s in sl) {
//            drawRectString(s, draw_x, draw_y)
//            draw_y += ttf.height
//        }
//    }
}