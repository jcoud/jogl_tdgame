package jikud.tdgame

// GL constants
// GL2 constants
// GL constants
// GL2 constants

import com.jogamp.newt.NewtFactory
import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.*
import com.jogamp.opengl.GL.*
import com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION
import com.jogamp.opengl.util.FPSAnimator
import jikud.tdgame.core.Drawing
import jikud.tdgame.core.GameTime
import jikud.tdgame.gui.GuiCore
import jikud.tdgame.helpers.FFont
import jikud.tdgame.world.Field
import java.awt.Font
import kotlin.system.exitProcess


class JOGLEntry : GLEventListener {
    // The window handle

    private var animator: FPSAnimator
    private var window: GLWindow

    companion object {
        val FONT: FFont = FFont("Arial", Font.BOLD, 20)
        const val WIDTH = TDMain.fieldWidth
        const val HEIGHT = TDMain.fieldHeight + TDMain.guiHeight
        private const val TITLE = "JOGL TD Game" // window's title
        private const val FPS = 60 // animator's target frames per second
        lateinit var GRF: GL2

        @JvmStatic
        fun main(args: Array<String>) {
            JOGLEntry()
        }
    }

    init {
        GLProfile.initSingleton()
        val glp = GLProfile.get(GLProfile.GL2)
        val caps = GLCapabilities(glp)
        caps.doubleBuffered = true
        caps.hardwareAccelerated = true
        val disp = NewtFactory.createDisplay("jogl init")
        val screen = NewtFactory.createScreen(disp, 0)
        window = GLWindow.create(screen, caps)
        animator = FPSAnimator(window, FPS)
        window.addGLEventListener(this)
        window.addWindowListener(object : WindowAdapter() {
            override fun windowDestroyed(p0: WindowEvent?) {
                animator.stop()
                exitProcess(0)
            }
        })
        window.title = TITLE
        window.setSize(WIDTH, HEIGHT)
        window.isVisible = true
        window.isFullscreen = false
        window.isResizable = false

        GuiCore.InputTransfer(window)
        animator.start()
    }

    // ------ Implement methods declared in GLEventListener ------
    /*
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */

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
        gl.glOrtho(0.0, WIDTH.toDouble(), HEIGHT.toDouble(), 0.0, 1.0, -1.0)
        gl.glMatrixMode(GL_MODELVIEW)

        TDMain()
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
        var h = height
        val gl = drawable.gl.gL2 // get the OpenGL 2 graphics context

//        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, h)
//
//        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION) // choose projection matrix
        gl.glLoadIdentity() // reset projection matrix
//        glu!!.gluPerspective(45.0, aspect.toDouble(), 0.1, 100.0) // fovy, aspect, zNear, zFar
//
//        // Enable the model-view transform
        gl.glOrtho(0.0, width.toDouble(), height.toDouble(), 0.0, 1.0, -1.0)
        gl.glMatrixMode(GL_MODELVIEW)
    }

    /*
     * Called back by the animator to perform rendering.
     */
    override fun display(drawable: GLAutoDrawable) {
        Field.applyTasks()
        val gl = drawable.gl.gL2
        GRF = gl
        GRF.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer
        GRF.glLoadIdentity()
        GRF.glEnable(GL_BLEND)
        GRF.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        Drawing.visualize()
        GRF.glDisable(GL_BLEND)
        print("$GameTime\r")
    }

    /*
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    override fun dispose(drawable: GLAutoDrawable?) {
        animator.stop()
    }
}