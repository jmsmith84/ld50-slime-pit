package containers.player

import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.component.attach
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.onCollision
import com.soywiz.korma.geom.Point
import components.collision.MovesWithTilemapCollision
import components.input.BuilderInput
import components.input.HorizontalMoveInput
import components.input.VerticalMoveInput
import components.movement.ClampMovement
import components.movement.HasFacing
import components.movement.MoveDirection
import containers.SpriteEntity
import containers.enemy.AcidSlime
import containers.enemy.Enemy
import containers.item.GamePickup
import containers.item.SpeedPotion
import program.*
import utility.timer.EventTimer
import kotlin.time.Duration.Companion.seconds

const val PLAYER_NAME = "PLAYER"

open class Player(
    sprite: Sprite,
    assets: AssetManager,
    soundManager: SoundManager,
    levelManager: LevelManager,
) : SpriteEntity(sprite, assets, soundManager, levelManager) {
    private val builderComponent: BuilderInput = BuilderInput(this,
        Key.Z,
        levelManager,
        soundManager,
        assets.wallBuildingAnimation,
        assets.playerBuildingAnimation,
        assets.wallDoneSfx)
    private var facingComponent: HasFacing = HasFacing(this)
    private val initialHp = hp
    private val speedUpTimer: EventTimer = EventTimer(this, 5.0.seconds) {
        this@Player.speedModifier = 1.0
        it.destroy()
    }.attach()
    var isDead = false

    init {
        name = PLAYER_NAME

        addComponent(HorizontalMoveInput(this, assets.playerWalkLeftAnimation, assets.playerWalkRightAnimation))
        addComponent(VerticalMoveInput(this, assets.playerWalkUpAnimation, assets.playerWalkDownAnimation))
        addComponent(builderComponent)
        addComponent(ClampMovement(this, Point(2.0, 2.0)))
        addComponent(facingComponent)
        addComponent(MovesWithTilemapCollision(this, levelManager))

        onCollision {
            when (it) {
                is Enemy -> damage()
                is GamePickup -> {
                    it.removeFromParent()
                    when (it) {
                        is SpeedPotion -> {
                            this@Player.speedModifier = 2.0
                            speedUpTimer.restart()
                            soundManager.asyncPlaySound(assets.speedUpSfx)
                        }
                    }
                }
            }
        }
        addUpdater {
            if (!isDead) {
                val timeAlive = if (GameState.timeAlive[levelManager.getLevel()] === null)
                    0.0 else GameState.timeAlive[levelManager.getLevel()]!!
                GameState.timeAlive.set(
                    levelManager.getLevel(),
                    timeAlive + it.seconds
                )
                if (!isMoving() && !builderComponent.builderTimer.isRunning()) {
                    getSprite().playAnimation(assets.playerIdleAnimation)
                }
            } else return@addUpdater
        }
    }

    override fun kill() {
        if (isDead) return
        isDead = true
        GameState.hiTimeAlive[levelManager.getLevel()] =
            if (GameState.timeAlive[levelManager.getLevel()]!! > GameState.hiTimeAlive[levelManager.getLevel()]!!)
                GameState.timeAlive[levelManager.getLevel()]!! else GameState.hiTimeAlive[levelManager.getLevel()]!!

        removeAllComponents()

        getSprite().playAnimation(assets.playerDeathAnimation, 160.milliseconds)
        getSprite().onAnimationCompleted {
            Log().info { "player removed after death" }
            removeFromParent()
        }
        soundManager.asyncPlaySound(assets.playerDieSfx)

        parent?.fastForEachChild {
            if (it is AcidSlime) {
                it.removeAllComponents()
            }
        }
    }

    override fun reset() {
        super.reset()
        hp = initialHp
        isDead = false
    }

    fun getFacing(): MoveDirection {
        return facingComponent.getFacing()
    }

    fun resetSpriteImage() {
        getSprite().playAnimation(assets.playerIdleAnimation)
    }
}

