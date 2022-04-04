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
    private var facingComponent: HasFacing = HasFacing(this)
    private val initialHp = hp
    var isDead = false

    init {
        name = PLAYER_NAME

        addComponent(HorizontalMoveInput(this))
        addComponent(VerticalMoveInput(this))
        addComponent(BuilderInput(this,
            Key.Z,
            levelManager,
            assets.wallBuildingAnimation,
            assets.playerBuildingAnimation))
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
                            EventTimer(this@Player, 5.0.seconds) {
                                this@Player.speedModifier = 1.0
                                it.destroy()
                            }.attach().start()
                        }
                    }
                }
            }
        }
        addUpdater {
            if (!isDead) GameState.timeAlive += it.seconds else return@addUpdater
        }
    }

    override fun kill() {
        if (isDead) return
        isDead = true
        GameState.hiTimeAlive =
            if (GameState.timeAlive > GameState.hiTimeAlive) GameState.timeAlive else GameState.hiTimeAlive

        removeAllComponents()

        getSprite().playAnimation(assets.playerDeathAnimation, 160.milliseconds)
        getSprite().onAnimationCompleted {
            Log().info { "player removed after death" }
            removeFromParent()
        }

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

