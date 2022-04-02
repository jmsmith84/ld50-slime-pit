package program

import com.soywiz.klogger.Logger
import com.soywiz.klogger.setLevel

const val LOG_NAME = "GLOBAL"

object Log {
    private val logger = Logger(LOG_NAME).setLevel(Logger.Level.NONE)

    operator fun invoke(): Logger {
        return this.logger
    }

    fun setLevel(level: Logger.Level): Logger {
        logger.level = level
        return this.logger
    }
}
