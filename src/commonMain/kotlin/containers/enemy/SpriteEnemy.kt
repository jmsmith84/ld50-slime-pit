package containers.enemy

import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.position
import com.soywiz.korma.geom.IPoint
import containers.SpriteEntity
import program.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class SpriteEnemy(
    sprite: Sprite,
    assets: AssetManager,
    soundManager: SoundManager,
    levelManager: LevelManager,
    position: IPoint
) : SpriteEntity(sprite, assets, soundManager, levelManager), Enemy {
    protected open val value = 100

    override fun kill() {
        this.removeFromParent()
        GameState.score += value
    }

    init {
        position(position)
        Log().debug { "Enemy created @ $position" }
    }
}
