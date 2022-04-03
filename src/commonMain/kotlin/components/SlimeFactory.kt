package components

import com.soywiz.korma.geom.IPoint
import com.soywiz.korma.geom.Point
import containers.GameEntity
import containers.enemy.AcidSlime
import program.AssetManager
import program.LevelManager
import program.Log
import program.SoundManager

var slimeCount = 0

class SlimeFactory(
    private val assets: AssetManager,
    private val soundManager: SoundManager,
    private val levelManager: LevelManager
) : GameEntityFactory {
    override fun create(spawnPosition: IPoint): GameEntity {
        slimeCount++
        Log().info { "Slime created $slimeCount" }
        return AcidSlime(assets, soundManager, levelManager, spawnPosition)
    }
}
