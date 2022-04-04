package factories

import com.soywiz.korge.view.position
import com.soywiz.korma.geom.IPoint
import containers.GameEntity
import containers.item.SpeedPotion
import program.AssetManager
import program.LevelManager
import program.Log
import program.SoundManager

class PotionFactory(
    private val assets: AssetManager,
    private val soundManager: SoundManager,
    private val levelManager: LevelManager
) : GameEntityFactory {
    override fun create(spawnPosition: IPoint): GameEntity {
        Log().info { "Potion created $slimeCount" }
        return SpeedPotion(assets, soundManager, levelManager)
            .position(spawnPosition)
    }
}
