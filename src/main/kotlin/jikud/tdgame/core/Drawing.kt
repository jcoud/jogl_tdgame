package jikud.tdgame.core

import com.jogamp.opengl.GL2.*
import com.jogamp.opengl.util.awt.TextRenderer
import jikud.tdgame.JOGLEntry
import jikud.tdgame.gui.GuiCore
import jikud.tdgame.helpers.CColor
import jikud.tdgame.helpers.FFont
import jikud.tdgame.world.FieldDraw
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


object Drawing {
    class FontRendering(font: FFont = JOGLEntry.FONT) {
        private var t: TextRenderer = TextRenderer(font, true)

        fun drawString(color: CColor, text: String, x: Float, y: Float) {
            t.setColor(color.color)
            t.beginRendering(JOGLEntry.WIDTH, JOGLEntry.HEIGHT)
            val r = t.getBounds(text)
            t.smoothing = true
            t.draw(text, (x - r.width / 2).toInt(), (JOGLEntry.HEIGHT - r.height / 2 - y).toInt())
            t.endRendering()
            t.flush()
        }
    }

    fun fillRect(x: Float, y: Float, width: Float, height: Float) = with(JOGLEntry.GRF) {
        glBegin(GL_QUADS)
        glVertex2f(x, y)
        glVertex2f(x + width, y)
        glVertex2f(x + width, y + height)
        glVertex2f(x, y + height)
        glEnd()
        glLoadIdentity()
    }

    fun drawLineRect(x: Float, y: Float, width: Float, height: Float, lineScale: Float = 1f) = with(JOGLEntry.GRF) {
        glPushMatrix()
        glLineWidth(lineScale)
        glBegin(GL_LINE_LOOP)
        glVertex2f(x, y)
        glVertex2f(x + width, y)
        glVertex2f(x + width, y + height)
        glVertex2f(x, y + height)
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun fillCircle(x: Float, y: Float, r: Float) = with(JOGLEntry.GRF) {
        glTranslatef(x, y, 0f)
        glBegin(GL_POLYGON)
        for (i in 0..360 step 10) {
            glVertex2d(r * cos(Math.toRadians(i.toDouble())), r * sin(Math.toRadians(i.toDouble())))
        }
        glEnd()
        glLoadIdentity()
    }

    fun drawLineCircle(x: Float, y: Float, r: Float, lineScale: Float = 1f) = with(JOGLEntry.GRF) {
        glPushMatrix()
        glLineWidth(lineScale)
        glTranslatef(x, y, 0f)
        glBegin(GL_LINE_LOOP)
        for (i in 0..360 step 10) {
            glVertex2d(r * cos(Math.toRadians(i.toDouble())), r * sin(Math.toRadians(i.toDouble())))
        }
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, lineScale: Float = 1f) = with(JOGLEntry.GRF) {
        glPushMatrix()
//        glTranslatef(x1, x2, 0f)
        glLineWidth(lineScale)
        glBegin(GL_LINES)
        glVertex3f(x1, y1, 0f)
        glVertex3f(x2, y2, 0f)
        glEnd()
        glPopMatrix()
        glLoadIdentity()
    }

    fun fillPolygon(fax: FloatArray, fay: FloatArray, n: Int) = with(JOGLEntry.GRF) {
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
        glBegin(GL_POLYGON)
        for (i in 0 until n) {
            glVertex2f(fax[i], fay[i])
        }
        glEnd()
        glTranslatef(-xx, -yy, 0f)
        glPopMatrix()
        glLoadIdentity()
    }

    //val l = sqrt(xx*xx + yy*yy)
//        drawLineCircle(0f, 0f, l, lineScale)
    fun drawLinePolygon(fax: FloatArray, fay: FloatArray, n: Int, lineScale: Float = 1f) = with(JOGLEntry.GRF) {
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
        glLineWidth(lineScale)
        glTranslatef(xx, yy, 0f)
        glBegin(GL_LINE_LOOP)
        for (i in 0 until n) {
            glVertex2f(fax[i], fay[i])
        }
        glEnd()
        glTranslatef(-xx, -yy, 0f)
        glPopMatrix()
        glLoadIdentity()
    }
//    }


    var drawWorld = false
    fun visualize() {

        GuiCore.repaintProcess()
        if (drawWorld) {
            FieldDraw.repaintProcess()
//            drawCommon()
        }
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