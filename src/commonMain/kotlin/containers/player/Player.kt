package containers.player

import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
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
import containers.bullet.EnemyBullet
import containers.enemy.AcidSlime
import containers.enemy.Enemy
import program.*

const val PLAYER_NAME = "PLAYER"

open class Player(
    sprite: Sprite,
    assets: AssetManager,
    soundManager: SoundManager,
    levelManager: LevelManager
) : SpriteEntity(sprite, assets, soundManager, levelManager) {
    private var facingComponent: HasFacing = HasFacing(this)
    private val initialHp = hp
    var isDead = false

    init {
        name = PLAYER_NAME

        addComponent(HorizontalMoveInput(this))
        addComponent(VerticalMoveInput(this))
        addComponent(BuilderInput(this, Key.Z, levelManager, assets.wallBuildingAnimation))
        addComponent(ClampMovement(this, Point(2.0, 2.0)))
        addComponent(facingComponent)
        addComponent(MovesWithTilemapCollision(this, levelManager))

        onCollision {
            if (it is Enemy || it is EnemyBullet) {
                damage()
            }
        }
        addUpdater {
            if (!isDead) GameState.timeAlive += it.seconds else return@addUpdater
        }
    }

    override fun kill() {
        if (isDead) return
        isDead = true
        GameState.hiTimeAlive = if (GameState.timeAlive > GameState.hiTimeAlive) GameState.timeAlive else GameState.hiTimeAlive

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
}

