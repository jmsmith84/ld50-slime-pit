package scenes

import com.soywiz.klock.seconds
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.view.*
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Point
import containers.GameEntity
import containers.player.Player
import containers.spawn.SlimeSpawner
import factories.GameEntityFactory
import factories.SlimeFactory
import program.*
import utility.getSecondsDisplay

@Suppress("MemberVisibilityCanBePrivate")
open class GameScene : Scene() {
    protected lateinit var assets: AssetManager
    protected lateinit var soundManager: SoundManager
    protected lateinit var config: Config
    protected lateinit var mapView: TiledMapView
    protected lateinit var player: Player
    protected lateinit var levelManager: LevelManager
    private lateinit var slimeFactory: GameEntityFactory

    override suspend fun Container.sceneInit() {
        config = injector.get()
        assets = injector.get()
        soundManager = injector.get()
        levelManager = injector.get()
        slimeFactory = injector.get<SlimeFactory>()

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

        text("LEVEL 01") {
            textSize = 18.0
            font = assets.defaultFont
            position(10, views.virtualHeight - 40)
            addUpdater {
                text = "LEVEL " + levelManager.getLevel().toString().padStart(2, '0')
            }
        }
        text("0 SECONDS") {
            textSize = 18.0
            font = assets.defaultFont
            position(10, views.virtualHeight - 20)
            addUpdater {
                text = "${GameState.timeAlive.getSecondsDisplay()} SECONDS"
            }
        }
        text("HI 0 SECONDS") {
            textSize = 18.0
            font = assets.defaultFont
            position(views.virtualWidth - 90, views.virtualHeight - 20)
            addUpdater {
                text = "HI ${GameState.hiTimeAlive.getSecondsDisplay()}"
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
        GameState.timeAlive = 0.0

        mapView.fastForEachChild {
            if (it is GameEntity && it !is Player) {
                it.removeFromParent()
            }
        }
        player.reset()

        getTiledMapObjects("player")?.forEach {
            player.position(it.x, it.y)
        }
        getTiledMapObjects("slime_spawn")?.forEach {
            mapView.addChild(SlimeSpawner(assets, soundManager, levelManager, slimeFactory, Point(it.x, it.y)))
        }

        Log().debug { "Player spawn @ ${player.pos}" }
    }

    protected fun getTiledMapObjects(type: String): List<TiledMap.Object>? {
        if (levelManager.getCurrentMap().objectLayers.isEmpty()) return null

        val objectLayer = levelManager.getCurrentMapObjects()
        return objectLayer.objectsByType[type]
    }

    override suspend fun Container.sceneMain() {
        addFixedUpdater(0.1.seconds) {
            GameState.hiTimeAlive =
                if (GameState.timeAlive > GameState.hiTimeAlive) GameState.timeAlive else GameState.hiTimeAlive
        }
        addFixedUpdater(1.0.seconds) {
            levelManager.getCurrentMapView().sortChildrenBy(
                Comparator { a, b ->
                    if (a is Player) {
                        if (b !is Player) return@Comparator 1
                        return@Comparator 0
                    } else if (b is Player) {
                        return@Comparator -1
                    }
                    return@Comparator 0
                }
            )
        }
    }
}
