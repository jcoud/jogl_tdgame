package me.jikud.tdgame

import me.jikud.engine.core.EngineStates
import me.jikud.engine.core.helpers.CColor
import me.jikud.engine.core.helpers.PPoint
import me.jikud.engine.core.main.*
import me.jikud.tdgame.world.field.Field
import me.jikud.tdgame.world.field.FieldDraw
import java.awt.Color

object GameDraw {
    fun visualize() {
//        if (!Global.initCompleted) return

        if (EngineStates.draw) FieldDraw.repaintProcess()

        drawCommon()
    }

    private var draw_x = 0f
    private var draw_y = 0f
//    private val F = FontRendering().apply { fontRenderer = TextRenderer(JOGLEntry.FONT.deriveFont(.5f)) }

    private val timer = Timer()

    private var s = "[Loop]: $Loop"

    private fun drawCommon() {
        draw_x = 5f
        draw_y = JOGLEntry.INIT_HEIGHT * 1f -10f
        if (Global.debugMode) {
            timer.start()
            if (timer.trigger(1.0)) {
                s = "[Loop]: $Loop"
            }
            draw_y -= drawRectString(s, draw_x, draw_y).yi
            drawDebugInfo()
        }
    }

    private fun drawRectString(s: String, dx: Float, dy: Float): PPoint {
        val b = GLRenderHelper.FontRendering.bounds(s)
        val sw = b.width
        val sh = b.height+2
        with(GLRenderHelper) {
            JGL.glColor3f(0f, 0f, 0f)
            Rect(dx, dy - 3, sw, sh, true)
            GLRenderHelper.FontRendering.drawString(CColor(Color.WHITE), s, dx, dy)
        }

        return PPoint(draw_x, sh)
    }

    private fun drawDebugInfo() {
        val sl = arrayListOf(
            "[courser] x: ${IOComponent.x} y: ${IOComponent.y}",
            "[GameTime]: $GameTime",
            "[Timer]: $Timer",
            "[entities]: ${Field.entityListOrder.size}",
            "[nodes]: ${Field.nodeListOrder.size}",
            "[towers]: ${Field.towerListOrder.size}"
        )
        for (s in sl) {
            draw_y -= drawRectString(s, draw_x, draw_y).yi
        }
    }
}