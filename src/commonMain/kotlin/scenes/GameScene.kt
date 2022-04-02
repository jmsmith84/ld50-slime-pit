package scenes

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.position
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Point
import containers.GameEntity
import containers.player.Player
import containers.spawn.SlimeSpawner
import program.*

@Suppress("MemberVisibilityCanBePrivate")
open class GameScene : Scene() {
    protected lateinit var assets: AssetManager
    protected lateinit var soundManager: SoundManager
    protected lateinit var config: Config
    protected lateinit var mapView: TiledMapView
    protected lateinit var player: Player
    protected lateinit var levelManager: LevelManager

    override suspend fun Container.sceneInit() {
        config = injector.get()
        assets = injector.get()
        soundManager = injector.get()
        levelManager = injector.get()

        Log.setLevel(config.getLogLevel())
        views.gameWindow.fullscreen = config.getFullscreenOnStart()

        levelManager.setNewMap(1u, this)
        mapView = levelManager.getCurrentMapView()

        player = injector.get()
        resetGame()
        mapView.addChild(player)

        keys.down {
            when (it.key) {
                Key.ESCAPE -> exitToMenu()
                else -> {}
            }
        }
    }

    protected fun exitToMenu() {
        launchImmediately {
            sceneContainer.changeTo<MenuScene>()
        }
    }

    protected fun resetGame() {
        mapView.x = 0.0
        mapView.y = 0.0
        GameState.score = 0

        mapView.forEachChild {
            if (it is GameEntity && it !is Player) {
                it.removeFromParent()
            }
        }
        player.reset()

        getTiledMapObjects("player")?.forEach {
            player.position(it.x, it.y)
        }
        getTiledMapObjects("slime_spawn")?.forEach {
            mapView.addChild(SlimeSpawner(assets, soundManager, levelManager, Point(it.x, it.y)))
        }

        Log().debug { "Player spawn @ ${player.pos}" }
    }

    protected fun getTiledMapObjects(type: String): List<TiledMap.Object>? {
        if (levelManager.getCurrentMap().objectLayers.isEmpty()) return null

        val objectLayer = levelManager.getCurrentMap().objectLayers[0]
        return objectLayer.objectsByType[type]
    }

    override suspend fun Container.sceneMain() {
        addUpdater {
            //Log().info { player.move }
            GameState.hiScore = if (GameState.score > GameState.hiScore) GameState.score else GameState.hiScore
        }
    }
}
