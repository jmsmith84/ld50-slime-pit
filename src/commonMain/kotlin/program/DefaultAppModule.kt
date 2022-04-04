package program

import com.soywiz.korge.scene.Module
import com.soywiz.korge.view.Sprite
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.*
import containers.player.Player
import factories.PotionFactory
import factories.SlimeFactory
import scenes.GameScene
import scenes.MenuScene

open class DefaultAppModule(override val title: String = "KorGE Boot Game", windowScale: Double = 2.0) : Module() {
    private val virtualScreenSize = SizeInt(Size(Point(320, 360)))

    override val mainScene = MenuScene::class
    override val windowSize = virtualScreenSize * windowScale
    override val size = virtualScreenSize
    override val fullscreen = false
    override val clipBorders = true
    override val scaleMode = ScaleMode.FIT

    override suspend fun AsyncInjector.configure() {
        mapSingleton { SoundManager() }
        mapSingleton { Config() }
        mapSingleton { LevelManager(get()) }
        mapSingleton { AssetManager() }
    }
}
