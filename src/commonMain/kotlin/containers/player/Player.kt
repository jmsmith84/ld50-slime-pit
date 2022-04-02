package containers.player

import com.soywiz.korge.component.attach
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.onCollision
import com.soywiz.korio.dynamic.dyn
import com.soywiz.korma.geom.Point
import components.collision.MovesWithTilemapCollision
import components.input.HorizontalMoveInput
import components.input.VerticalMoveInput
import components.movement.ClampMovement
import containers.SpriteEntity
import containers.bullet.EnemyBullet
import containers.enemy.Enemy
import program.AssetManager
import program.GameState
import program.LevelManager
import program.SoundManager

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
        HorizontalMoveInput(this).attach()
        VerticalMoveInput(this).attach()
        ClampMovement(this, Point(2.0, 2.0)).attach()
        MovesWithTilemapCollision(this, levelManager).attach()

        onCollision {
            if (it is Enemy || it is EnemyBullet) {
                damage()
            }
        }
        addUpdater {
            if (!isDead) GameState.timeAlive += it.dyn.toDouble() else return@addUpdater
        }
    }

    open fun damage(value: UInt = 1u) {
        hp -= value
        if (hp == 0u) kill()
    }

    open fun kill() {
        isDead = true
        GameState.timeAlive = 0.0
        removeFromParent()
    }

    override fun reset() {
        super.reset()
        hp = initialHp
        isDead = false
    }
}
