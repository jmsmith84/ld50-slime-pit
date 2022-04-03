package containers.player

import com.soywiz.klock.milliseconds
import com.soywiz.korge.component.attach
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.onCollision
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.dynamic.dyn
import com.soywiz.korma.geom.Point
import components.collision.MovesWithTilemapCollision
import components.input.HorizontalMoveInput
import components.input.VerticalMoveInput
import components.movement.ClampMovement
import containers.SpriteEntity
import containers.bullet.EnemyBullet
import containers.enemy.Enemy
import kotlinx.coroutines.Dispatchers
import program.*

const val PLAYER_NAME = "PLAYER"

open class Player(
    sprite: Sprite,
    assets: AssetManager,
    soundManager: SoundManager,
    levelManager: LevelManager
) : SpriteEntity(sprite, assets, soundManager, levelManager) {
    private val initialHp = hp
    var isDead = false

    init {
        name = PLAYER_NAME
        addComponent(HorizontalMoveInput(this))
        addComponent(VerticalMoveInput(this)).attach()
        addComponent(ClampMovement(this, Point(2.0, 2.0)))
        addComponent(MovesWithTilemapCollision(this, levelManager))

        onCollision {
            if (it is Enemy || it is EnemyBullet) {
                damage()
            }
        }
        addUpdater {
            if (!isDead) GameState.timeAlive += it.dyn.toDouble() else return@addUpdater
        }
    }

    override fun kill() {
        if (isDead) return
        isDead = true
        GameState.timeAlive = 0.0
        getSprite().playAnimation(assets.playerDeathAnimation, 160.milliseconds)
        getSprite().onAnimationCompleted {
            Log().info { "player removed after death" }
            removeFromParent()
        }
    }

    override fun reset() {
        super.reset()
        hp = initialHp
        isDead = false
    }
}
