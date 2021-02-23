package jikud.tdgame.core

import jikud.tdgame.world.Field
import kotlin.system.exitProcess

object Loop : Runnable {
    private var fps = 0
    private var ups = 0
    private var running = false
    private lateinit var thread: Thread
    private var unprocessed = 0.0
    private const val nsPerTick = 1_000_000_000L / Renderer.FPS

    override fun run() {
        var lastTime = System.nanoTime()
        var shouldRender: Boolean
        fps = 0
        ups = 0
        var lt = System.currentTimeMillis()
        while (running) {
            shouldRender = false
            val currentTime = System.nanoTime()
            unprocessed += (currentTime - lastTime) / nsPerTick
            lastTime = currentTime
            while (unprocessed >= 1) {
                ups++
                update()
                unprocessed -= 1.0
                shouldRender = true
            }
            if (shouldRender) {
                fps++
                render()
            }

            try {
                Thread.sleep((((1 - unprocessed) * 1000).toInt() / 60).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            if (System.currentTimeMillis() - lt > 1000) {
                lt = System.currentTimeMillis()
//                print("ups: $ups, fps: $fps\r")
                fps = 0
                ups = 0
            }
        }
    }

    @Synchronized
    fun start() {
        running = true
        thread = Thread(this, "Thread")
        thread.start()
    }

    @Synchronized
    fun stop() {
        running = false
        try {
            thread.join()
            exitProcess(0)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun update() {
        Field.update()
    }

    private fun render() {
        Renderer.render()
    }
}