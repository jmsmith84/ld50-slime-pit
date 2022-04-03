package containers

import com.soywiz.korge.view.BaseImage
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.HitTestDirection
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Rectangle
import program.AssetManager
import program.LevelManager
import program.SoundManager
import utility.rectHitTest

open class GameEntity(
    protected val assets: AssetManager,
    protected val soundManager: SoundManager,
    protected val levelManager: LevelManager,
    protected var hp: UInt = 1u
) : Container() {
    val move = Point(0, 0)
    lateinit var image: BaseImage

    fun getHP(): UInt {
        return hp
    }

    open fun damage(amount: UInt = 1u) {
        if (amount > hp) hp = 0u else hp -= amount
        if (hp == 0u) kill()
    }

    open fun kill() {}

    fun getCurrentBounds(): Rectangle {
        return getGlobalBounds()
    }

    fun isTouchingGround(): Boolean {
        val clone = getCurrentBounds().clone()
        clone.y++
        return levelManager.getCurrentMapView().rectHitTest(clone, HitTestDirection.DOWN) !== null
    }
}
