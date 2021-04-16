package me.jikud.tdgame.core

import com.jogamp.opengl.GL2
import com.jogamp.opengl.GL2.*
import com.jogamp.opengl.util.awt.TextRenderer
import me.jikud.tdgame.gui.GuiCore
import me.jikud.tdgame.gui.GuiIOTransfer
import me.jikud.tdgame.helpers.CColor
import me.jikud.tdgame.helpers.FFont
import me.jikud.tdgame.helpers.PPoint
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.field.FieldDraw
import java.awt.Color
import java.awt.geom.Rectangle2D
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


object Drawing {
    lateinit var GL: GL2

    class FontRendering(val font: FFont = JOGLEntry.FONT) {
        var fontRenderer: TextRenderer = TextRenderer(font, true, false, TextRenderer.DefaultRenderDelegate())

        fun bounds(text: String): Rectangle2D {
            return fontRenderer.getBounds(text)
        }

        fun drawString(color: CColor, text: String, x: Float, y: Float) {
            fontRenderer.setColor(color.color)
            fontRenderer.beginRendering(JOGLEntry.INIT_WIDTH, JOGLEntry.INIT_HEIGHT)
            fontRenderer.smoothing = true
            fontRenderer.draw(
                text,
                x.toInt(),
                (JOGLEntry.INIT_HEIGHT - bounds(text).height *.75 - y).toInt()
            )
            fontRenderer.endRendering()
            fontRenderer.flush()
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

        drawCommon()
    }

    private var draw_x = 0f
    private var draw_y = 0f
    private val F = FontRendering(JOGLEntry.FONT.deriveFont(.5f))

    private fun drawCommon() = with(GL) {
        draw_x = 5f
        draw_y = 5f
        glColor3f(0f, 0f, 0f)
        drawXnYs()
        drawDebug()
    }

    private fun drawRectString(s: String, dx: Float, dy: Float): PPoint = with(GL) {
        val b = F.bounds(s)
        val sw = b.width.toFloat()
        val sh = b.height.toFloat()
        glColor4f(.1f, .1f, .1f, .5f)
        Rect(dx, dy, sw, sh, true)
        F.drawString(CColor(Color.WHITE), s, dx, dy)

        return PPoint(draw_x, sh)
    }

    private fun drawXnYs() {
        draw_y += drawRectString("[field] x: ${GuiIOTransfer.x} y: ${GuiIOTransfer.x}", draw_x, draw_y).yi
    }

    private fun drawDebug() {
        val sl = arrayListOf(
            "GameTime: $GameTime",
            "Loop: $Loop",
            "entities: ${Field.entityListOrder.size}",
            "nodes: ${Field.nodeListOrder.size}",
            "towers: ${Field.towerListOrder.size}"
        )
        for (s in sl) {
            draw_y += drawRectString(s, draw_x, draw_y).yi
        }
    }
}