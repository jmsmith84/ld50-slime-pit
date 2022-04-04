package components.input

import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.component.Component
import com.soywiz.korge.component.attach
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.position
import components.WallTileStatus
import containers.player.Player
import program.LevelManager
import program.Log
import utility.timer.EventTimer
import kotlin.time.Duration.Companion.seconds

class BuilderInput(
    override val view: Player,
    private val levelManager: LevelManager,
    wallBuildingAnimation: SpriteAnimation,
    private val playerBuildingAnimation: SpriteAnimation? = null,
) : Component {
    private var builderTimer: EventTimer = EventTimer(view, 2.38.seconds) {}.attach()
    private var wallSprite: Sprite

    init {
        wallSprite = Sprite(wallBuildingAnimation, smoothing = false)

        view.keys.justDown(Key.Z) {
            Log().debug { "Z down" }

            view.canMove = false
            if (!builderTimer.isRunning()) {
                wallSprite.position(view.pos.copy().add(0.0,-16.0))
                wallSprite.addTo(levelManager.getCurrentMapView())
                wallSprite.playAnimation(spriteDisplayTime = 800.0.milliseconds, startFrame = 0)
                builderTimer.newCallback {
                    Log().info {"hello"}
                    stopBuilding()
                    val tileXY = levelManager.globalXYToTileXY(wallSprite.x, wallSprite.y)
                    levelManager.setTileIdAt(tileXY.x.toInt(), tileXY.y.toInt(), WallTileStatus.NORMAL.id)
                }
                builderTimer.restart()
            }
            if (playerBuildingAnimation !== null) view.getSprite().playAnimationLooped(playerBuildingAnimation)
        }
        view.keys.up {
            when (it.key) {
                Key.Z -> {
                    Log().debug { "Z up" }
                    stopBuilding()
                }
                else -> {}
            }
        }
    }

    private fun stopBuilding() {
        builderTimer.stop()
        view.canMove = true
        wallSprite.stopAnimation()
        wallSprite.removeFromParent()
    }
}
