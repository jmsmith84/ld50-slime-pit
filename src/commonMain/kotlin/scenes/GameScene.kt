package scenes

import com.soywiz.klock.seconds
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.view.*
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.Point
import containers.GameEntity
import containers.player.Player
import containers.spawn.SlimeSpawner
import factories.GameEntityFactory
import factories.PotionFactory
import factories.SlimeFactory
import program.*
import utility.getSecondsDisplay
import kotlin.random.Random

@Suppress("MemberVisibilityCanBePrivate")
open class GameScene : Scene() {
    protected lateinit var assets: AssetManager
    protected lateinit var soundManager: SoundManager
    protected lateinit var config: Config
    protected lateinit var player: Player
    protected lateinit var levelManager: LevelManager

    private lateinit var slimeFactory: GameEntityFactory
    private lateinit var potionFactory: GameEntityFactory

    override suspend fun Container.sceneInit() {
        config = injector.get()
        assets = injector.get()
        soundManager = injector.get()
        levelManager = injector.get()
        slimeFactory = injector.get<SlimeFactory>()
        potionFactory = injector.get<PotionFactory>()

        Log.setLevel(config.getLogLevel())
        views.gameWindow.fullscreen = config.getFullscreenOnStart()

        levelManager.setNewMap(levelManager.getLevel(), this)

        player = injector.get()
        resetGame()
        levelManager.getMapView().addChild(player)

        keys.down {
            when (it.key) {
                Key.ESCAPE -> exitToMenu()
                else -> {}
            }
        }

        text("LEVEL") {
            textSize = 18.0
            font = assets.defaultFont
            position(10, views.virtualHeight - 40)
            addUpdater {
                text = levelManager.getLevelName()
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
        levelManager.getMapView().x = 0.0
        levelManager.getMapView().y = 0.0
        GameState.timeAlive = 0.0

        levelManager.getMapView().fastForEachChild {
            if (it is GameEntity && it !is Player) {
                it.removeFromParent()
            }
        }
        player.reset()

        getTiledMapObjects("player")?.forEach {
            player.position(it.x, it.y)
        }
        getTiledMapObjects("slime_spawn")?.forEach {
            levelManager.getMapView()
                .addChild(SlimeSpawner(assets, soundManager, levelManager, slimeFactory, Point(it.x, it.y)))
        }

        Log().debug { "Player spawn @ ${player.pos}" }
    }

    protected fun getTiledMapObjects(type: String): List<TiledMap.Object>? {
        if (levelManager.getMap().objectLayers.isEmpty()) return null

        val objectLayer = levelManager.getMapObjects()
        return objectLayer.objectsByType[type]
    }

    override suspend fun Container.sceneMain() {
        addFixedUpdater(0.1.seconds) {
            GameState.hiTimeAlive =
                if (GameState.timeAlive > GameState.hiTimeAlive) GameState.timeAlive else GameState.hiTimeAlive
        }
        addFixedUpdater(1.0.seconds) {
            levelManager.getMapView().sortChildrenBy(
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
        addFixedUpdater(5.0.seconds) {
            if (Random.nextInt(1, 7) > 1) {
                return@addFixedUpdater
            }

            val emptyXY = levelManager.getRandomEmptyTile()
            if (emptyXY != null) {
                levelManager.getMapView().addChild(
                    potionFactory.create(
                        levelManager.tileXYToGlobalXY(emptyXY)
                    )
                )
            }
        }
    }
}
