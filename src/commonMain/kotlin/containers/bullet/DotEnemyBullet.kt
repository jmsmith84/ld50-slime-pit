package containers.bullet

import com.soywiz.korge.view.RectBase
import com.soywiz.korma.geom.XY
import program.AssetManager
import program.LevelManager
import program.SoundManager

class DotEnemyBullet(
    assets: AssetManager,
    soundManager: SoundManager,
    levelManager: LevelManager,
    bulletRect: RectBase,
    spawn: XY,
    target: XY,
    shotSpeed: Double = 100.0
) : DotBullet(assets, soundManager, levelManager, bulletRect, spawn, target, shotSpeed), EnemyBullet {
    init {
        addChild(assets.enemyBulletRect)
    }
}
