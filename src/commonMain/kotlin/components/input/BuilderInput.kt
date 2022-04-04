package components.input

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.korau.sound.Sound
import com.soywiz.korev.Key
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.position
import com.soywiz.korma.geom.centerX
import com.soywiz.korma.geom.centerY
import components.ai.WallTileStatus
import components.movement.MoveDirection
import containers.player.Player
import program.LevelManager
import program.Log
import program.SoundManager
import utility.timer.EventTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class BuilderInput(
    override val view: Player,
    val key: Key,
    private val levelManager: LevelManager,
    private val soundManager: SoundManager,
    private val wallBuildingAnimation: SpriteAnimation,
    private val playerBuildingAnimation: SpriteAnimation? = null,
    private val wallBuiltSound: Sound? = null
) : UpdateComponent {
    private val buildTime: Duration = 2.38.seconds
    private var wallSprite: Sprite
    var builderTimer: EventTimer = EventTimer(view, buildTime) {}
    var buildingMode = false
    var buildingDirection: MoveDirection = MoveDirection.UP

    init {
        view.addComponent(builderTimer)
        wallSprite = Sprite(wallBuildingAnimation, smoothing = false)

        view.keys.justDown(key) {
            Log().debug { "Z down" }
            buildingMode = true
            buildingDirection = view.getFacing()

            if (!builderTimer.isRunning()) {
                tryBuilding()
            }
        }
        view.keys.up {
            if (it.key == key) {
                Log().debug { "Z up" }
                stopBuilding()
                buildingMode = false
            }
        }
    }

    private fun stopBuilding() {
        builderTimer.stop()
        view.canMove = true
        wallSprite.stopAnimation()
        wallSprite.removeFromParent()
        view.resetSpriteImage()
    }

    private fun tryBuilding() {
        val playerPosition = view.pos.copy()
        val bounds = view.getBounds()

        when (buildingDirection) {
            MoveDirection.UP -> playerPosition.add(bounds.centerX, bounds.top - (bounds.height / 2))
            MoveDirection.DOWN -> playerPosition.add(bounds.centerX, bounds.bottom + (bounds.height / 2))
            MoveDirection.LEFT -> playerPosition.add(bounds.left - (bounds.width / 2), bounds.centerY)
            MoveDirection.RIGHT -> playerPosition.add(bounds.right + (bounds.width / 2), bounds.centerY)
            else -> {}
        }

        val tileXY = levelManager.globalXYToTileXY(playerPosition)
        if (!levelManager.isTileEmpty(tileXY)) return

        view.canMove = false
        wallSprite = Sprite(wallBuildingAnimation, smoothing = false) /* Have to create a new sprite
                    every time due to a Sprite bug where remaining frame time is never reset */
        wallSprite.position(levelManager.tileXYToGlobalXY(tileXY))
        wallSprite.addTo(levelManager.getMapView())
        wallSprite.playAnimation(spriteDisplayTime = 800.0.milliseconds / view.speedModifier, startFrame = 0)

        runPlayerBuildAnimation()

        builderTimer.newCallback {
            stopBuilding()
            if (!levelManager.isTileEmpty(tileXY)) return@newCallback
            levelManager.setTileIdAt(tileXY.x.toInt(), tileXY.y.toInt(), WallTileStatus.WOOD_NORMAL.id)

            if (wallBuiltSound !== null) soundManager.asyncPlaySound(wallBuiltSound)
        }
        builderTimer.setLength(buildTime / view.speedModifier)
        builderTimer.restart()
    }

    override fun update(dt: TimeSpan) {
        if (buildingMode && !builderTimer.isRunning()) {
            tryBuilding()
        } else if (builderTimer.isRunning()) {
            runPlayerBuildAnimation()
        }
    }

    private fun runPlayerBuildAnimation() {
        if (playerBuildingAnimation !== null) view.getSprite().playAnimationLooped(
            playerBuildingAnimation,
            150.milliseconds
        )
    }
}
