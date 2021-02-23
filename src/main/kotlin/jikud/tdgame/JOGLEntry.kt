package jikud.tdgame

import com.jogamp.opengl.GL.*
import com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION

import jikud.tdgame.core.Drawing
import jikud.tdgame.core.Renderer


class JOGLEntry : GLEventListener {
    // The window handle
    private val tallness = 10

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
        gl.glOrtho(0.0, Renderer.INIT_WIDTH.toDouble(), Renderer.INIT_HEIGHT.toDouble(), 0.0, 1.0, -1.0)
        gl.glMatrixMode(GL_MODELVIEW)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, w: Int, h: Int) {
        val gl = drawable.gl.gL2
        gl.glMatrixMode(GL_PROJECTION)
        gl.glLoadIdentity()

        val u = h / (w / tallness).toDouble()
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
}