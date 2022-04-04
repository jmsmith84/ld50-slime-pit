package containers.item

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.Sprite
import containers.SpriteEntity
import program.AssetManager
import program.LevelManager
import program.SoundManager

class SpeedPotion(assets: AssetManager, soundManager: SoundManager, levelManager: LevelManager) :
    SpriteEntity(Sprite(assets.potionAnimation), assets, soundManager, levelManager), GamePickup {

    init {
        getSprite().playAnimationLooped(spriteDisplayTime = 100.milliseconds)
    }
}
