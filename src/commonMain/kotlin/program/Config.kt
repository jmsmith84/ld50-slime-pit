package program

import com.soywiz.klogger.Logger
import com.soywiz.korinject.AsyncDependency
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.lang.Properties
import com.soywiz.korio.lang.readProperties

open class Config : AsyncDependency {
    private lateinit var appProperties: Properties

    override suspend fun init() {
        appProperties = resourcesVfs["app.properties"].readProperties()
    }

    fun get(key: String): String {
        val value = appProperties[key]
        if (value === null) return ""
        return value
    }

    fun getLogLevel(): Logger.Level {
        return Logger.Level.valueOf(get("logLevel"))
    }

    fun getFullscreenOnStart(): Boolean {
        return get("fullscreenOnStart").toBoolean()
    }
}
