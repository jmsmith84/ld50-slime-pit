package program

import com.soywiz.korge.view.Sprite
import com.soywiz.korinject.AsyncInjector
import containers.player.Player
import factories.PotionFactory
import factories.SlimeFactory
import scenes.GameScene
import scenes.LevelSelectScene
import scenes.MenuScene

object AppModule : DefaultAppModule("SLIME BATH") {
    override suspend fun AsyncInjector.configure() {
        val assets = AssetManager()

        mapSingleton { SoundManager() }
        mapSingleton { Config() }
        mapSingleton { LevelManager(get()) }
        mapSingleton { assets }

        mapPrototype { GameScene() }
        mapPrototype { MenuScene(title) }
        mapPrototype { LevelSelectScene() }
        mapPrototype { Player(Sprite(assets.playerBitmap), get(), get(), get()) }
        mapPrototype { SlimeFactory(get(), get(), get()) }
        mapPrototype { PotionFactory(get(), get(), get()) }
    }
}

