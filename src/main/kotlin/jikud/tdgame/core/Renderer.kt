package jikud.tdgame.core

import com.jogamp.newt.NewtFactory
import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import jikud.tdgame.JOGLEntry
import jikud.tdgame.TDMain
import jikud.tdgame.gui.GuiCore
import jikud.tdgame.helpers.FFont
import java.awt.Font
import java.awt.Toolkit

object Renderer {

    val FONT: FFont = FFont("Arial", Font.BOLD, 20)
    const val INIT_WIDTH = TDMain.fieldWidth
    const val INIT_HEIGHT = TDMain.fieldHeight + TDMain.guiHeight
    const val FPS = 60 // animator's target frames per second

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
        window.title = TDMain.TITLE
        window.setSize(INIT_WIDTH, INIT_HEIGHT)
        val d = Toolkit.getDefaultToolkit().screenSize
        window.setPosition(( d.width - INIT_WIDTH) / 2, (d.height - INIT_HEIGHT) / 2)
        window.isVisible = true
        window.isFullscreen = false
        window.isResizable = false

        GuiCore.InputTransfer(window)
//        animator.start()
    }

    fun render() {
        window.display()
    }
}