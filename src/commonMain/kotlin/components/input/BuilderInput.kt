package components.input

import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.component.Component
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
import utility.timer.EventTimer
import kotlin.time.Duration.Companion.seconds

class BuilderInput(
    override val view: Player,
    val key: Key,
    private val levelManager: LevelManager,
    wallBuildingAnimation: SpriteAnimation,
    private val playerBuildingAnimation: SpriteAnimation? = null,
) : Component {
    private var builderTimer: EventTimer = EventTimer(view, 2.38.seconds) {}
    private var wallSprite: Sprite

    init {
        view.addComponent(builderTimer)
        wallSprite = Sprite(wallBuildingAnimation, smoothing = false)

        view.keys.justDown(key) {
            Log().debug { "Z down" }

            if (!builderTimer.isRunning()) {
                val playerPosition = view.pos.copy()
                val bounds = view.getBounds()

                when (view.getFacing()) {
                    MoveDirection.UP -> playerPosition.add(bounds.centerX, bounds.top - 1)
                    MoveDirection.DOWN -> playerPosition.add(bounds.centerX, bounds.bottom + 1)
                    MoveDirection.LEFT -> playerPosition.add(bounds.left - 1, bounds.centerY)
                    MoveDirection.RIGHT -> playerPosition.add(bounds.right + 1, bounds.centerY)
                    else -> {}
                }

                val tileXY = levelManager.globalXYToTileXY(playerPosition)
                if (!levelManager.isTileEmpty(tileXY)) return@justDown

                view.canMove = false
                wallSprite = Sprite(wallBuildingAnimation, smoothing = false) /* Have to create a new sprite
                    every time due to a Sprite bug where remaining frame time is never reset */
                wallSprite.position(levelManager.tileXYToGlobalXY(tileXY))
                wallSprite.addTo(levelManager.getCurrentMapView())
                wallSprite.playAnimation(spriteDisplayTime = 800.0.milliseconds, startFrame = 0)

                builderTimer.newCallback {
                    stopBuilding()
                    if (!levelManager.isTileEmpty(tileXY)) return@newCallback
                    levelManager.setTileIdAt(tileXY.x.toInt(), tileXY.y.toInt(), WallTileStatus.NORMAL.id)
                }
                builderTimer.restart()
            }
            if (playerBuildingAnimation !== null) view.getSprite().playAnimationLooped(
                playerBuildingAnimation,
                150.milliseconds
            )
        }
        view.keys.up {
            if (it.key == key) {
                Log().debug { "Z up" }
                stopBuilding()
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
}
