package utility.timer

import com.soywiz.klock.TimeSpan

interface Timer {
    fun restart(): Timer
    fun start(): Timer
    fun stop(): Timer
    fun isFinished(): Boolean
    fun isRunning(): Boolean
    fun reset(): Timer
}
