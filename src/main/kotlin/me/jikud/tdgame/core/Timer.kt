package me.jikud.tdgame.core

class Timer {


    private var uid = 0

    init {
        GameTime.timerClassCounter++
        uid = GameTime.timerClassCounter
        GameTime.timers.add(this)
    }

    var actual = 0L
    var current = 0L

    //TODO: доделать возможность останавливать время на паузу
    private var started = false
    private var paused = false
    private var timeSinceStarted = 0L
    private var timeSincePaused = 0L
    private var internalLastUpdated = 0L
    private var timeSinceUnPaused = 0L
    private var timeSinceStartedActual = 0L
    private var timeSincePausedActual = 0L
    private var deltaPaused = 0L

    fun update() {
        actual =
            if (started) {
                if (paused)
                    timeSincePausedActual
                else
                    System.currentTimeMillis() - timeSinceStartedActual - deltaPaused
            } else 0L
        current =
            if (started) {
                if (paused)
                    timeSincePaused
                else
                    System.currentTimeMillis() - timeSinceStarted - deltaPaused
            } else 0L
        deltaPaused = if (paused) System.currentTimeMillis() - timeSincePaused else 0L

        if (Global.TileTimer_isUpdatable) unpause()
        else pause()
    }

    fun start() {
        if (started) return
        started = true
        timeSinceStarted = System.currentTimeMillis()
        timeSinceStartedActual = System.currentTimeMillis()
    }

    fun pause() {
        if (!started) return
        if (paused) return
        paused = true
        internalLastUpdated = actual
        timeSincePaused = System.currentTimeMillis() - timeSinceStarted
        timeSincePausedActual = System.currentTimeMillis() - timeSinceStartedActual
    }

    fun unpause() {
        if (!paused) return
        paused = false
        timeSinceUnPaused = System.currentTimeMillis()

    }

    fun trigger(time: Double, timerName: GameTime.TimerNames = GameTime.TimerNames.SEC): Boolean {
//            start()
        if (current == 0L) return false
        val t: Double = when (timerName) {
            GameTime.TimerNames.MLS -> GameTime.mls(current.toDouble()).toDouble()
            GameTime.TimerNames.SEC -> GameTime.sec(current.toDouble())
            GameTime.TimerNames.MIN -> GameTime.min(current.toDouble())
            GameTime.TimerNames.HRS -> GameTime.hrs(current.toDouble())
        }
        if (t >= time) {
            timeSinceStarted = System.currentTimeMillis()
            return true
        }
        return false
    }

    fun toString_(): String {
        return "{ac: $actual | " +
                "cu: $current | " +
                "lu: $internalLastUpdated | " +
                "pa: $paused | " +
                "tp: $timeSincePaused | " +
                "dp: $deltaPaused}"
    }
}