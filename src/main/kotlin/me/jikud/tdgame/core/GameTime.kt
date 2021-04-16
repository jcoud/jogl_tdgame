package me.jikud.tdgame.core

import java.math.BigDecimal
import java.math.RoundingMode

object GameTime {

    internal var timerClassCounter = 0
    internal val timers = ArrayList<Timer>()
    private val timer = Timer().apply { start() }

    enum class TimerNames {
        MLS, SEC, MIN, HRS
    }

    fun mls(t: Double = timer.current.toDouble()): Int {
        return (t % 1000).toInt()
    }

    fun sec(t: Double = timer.current.toDouble()): Double {
        return BigDecimal(t / 1000 % 60).setScale(3, RoundingMode.DOWN).toDouble()
    }

    fun min(t: Double = timer.current.toDouble()): Double {
        return BigDecimal(t / 1000 / 60 % 60).setScale(3, RoundingMode.DOWN).toDouble()
    }

    fun hrs(t: Double = timer.current.toDouble()): Double {
        return BigDecimal(t / 1000 / 3600).setScale(3, RoundingMode.DOWN).toDouble()
    }

    override fun toString(): String {
        return "{now: ${timer.current} | " +
                "mls: ${mls()} | " +
                "sec: ${sec()} | " +
                "min: ${min()} | " +
                "hrs: ${hrs()}}"
    }



    fun update() {
        timers.forEach(Timer::update)
    }

}