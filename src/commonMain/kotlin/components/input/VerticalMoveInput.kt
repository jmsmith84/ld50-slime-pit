package components.input

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.component.UpdateComponentWithViews
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.Views
import containers.SpriteEntity

class VerticalMoveInput(
    override val view: SpriteEntity,
    private val upAnimation: SpriteAnimation? = null,
    private val downAnimation: SpriteAnimation? = null
) : UpdateComponentWithViews {
    override fun update(views: Views, dt: TimeSpan) {
        if (views.input.keys[Key.UP]) {
            view.move.y -= 1.0
            if (upAnimation !== null) view.getSprite().playAnimationLooped(upAnimation, spriteDisplayTime = 130.milliseconds)
        } else if (views.input.keys[Key.DOWN]) {
            view.move.y += 1.0
            if (downAnimation !== null) view.getSprite().playAnimationLooped(downAnimation, spriteDisplayTime = 130.milliseconds)
        } else {
            view.move.y = 0.0
        }
    }
}
