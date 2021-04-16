package me.jikud.tdgame.core

import com.jogamp.newt.NewtFactory
import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.GL.*
import com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION
import me.jikud.tdgame.gui.GuiIOTransfer
import me.jikud.tdgame.helpers.FFont
import java.awt.Font
import java.awt.Toolkit


class JOGLEntry : GLEventListener {

    private val pixelTallness = 10

    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f) // set background (clear) color
        gl.glClearDepth(1.0) // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST) // enables depth testing
        gl.glDepthFunc(GL_LEQUAL) // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST) // best perspective correction
        gl.glShadeModel(GL_SMOOTH) // blends colors nicely, and smoothes out lighting

        gl.glMatrixMode(GL_PROJECTION)
        gl.glLoadIdentity()
        gl.glOrtho(0.0, INIT_WIDTH.toDouble(), INIT_HEIGHT.toDouble(), 0.0, 1.0, -1.0)
        gl.glMatrixMode(GL_MODELVIEW)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, w: Int, h: Int) {
        val gl = drawable.gl.gL2
        gl.glMatrixMode(GL_PROJECTION)
        gl.glLoadIdentity()

        val u = h / (w / pixelTallness).toDouble()
        gl.glOrtho(0.0, w.toDouble(), h.toDouble(), 0.0, u, -1.0)
        gl.glMatrixMode(GL_MODELVIEW)
    }

    /*
     * Called back by the animator to perform rendering.
     */
    override fun display(drawable: GLAutoDrawable) {
        Drawing.GL = drawable.gl.gL2
        Drawing.GL.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer
        Drawing.GL.glLoadIdentity()
        Drawing.GL.glEnable(GL_BLEND)
        Drawing.GL.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        Drawing.visualize()
        Drawing.GL.glDisable(GL_BLEND)
    }

    /*
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    override fun dispose(drawable: GLAutoDrawable?) {}

    companion object {
        val FONT: FFont = FFont("Consolas", Font.BOLD, 20)
        const val INIT_WIDTH = me.jikud.tdgame.TDMain.fieldWidth
        const val INIT_HEIGHT = me.jikud.tdgame.TDMain.fieldHeight + me.jikud.tdgame.TDMain.def_gui_height
        const val FPS = 120 // animator's target frames per second

        private lateinit var window: GLWindow

        fun init() {
            GLProfile.initSingleton()
            val glp = GLProfile.get(GLProfile.GL2)
            val caps = GLCapabilities(glp)
            caps.doubleBuffered = true
            caps.hardwareAccelerated = true
            val disp = NewtFactory.createDisplay("jogl init")
            val screen = NewtFactory.createScreen(disp, 0)
            window = GLWindow.create(screen, caps)
            window.addGLEventListener(JOGLEntry())
            window.addWindowListener(object : WindowAdapter() {

                override fun windowDestroyed(p0: WindowEvent?) {
                    Loop.stop()

                }
            })
            window.isSticky = true
            window.title = me.jikud.tdgame.TDMain.TITLE
            window.setSize(INIT_WIDTH, INIT_HEIGHT)
            val d = Toolkit.getDefaultToolkit().screenSize
            window.setPosition(( d.width - INIT_WIDTH) / 2, (d.height - INIT_HEIGHT) / 2)
            window.isVisible = true
            window.isFullscreen = false
            window.isResizable = false

            GuiIOTransfer(window)
//        animator.start()
        }

        fun render() {
            window.display()
        }
    }
}