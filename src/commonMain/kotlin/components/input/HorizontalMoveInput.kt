package components.input

import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.component.UpdateComponent
import com.soywiz.korge.component.UpdateComponentWithViews
import com.soywiz.korge.input.Input
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.Views
import com.soywiz.korma.math.clamp
import containers.SpriteEntity

class HorizontalMoveInput(
    override val view: SpriteEntity,
    private val leftAnimation: SpriteAnimation? = null,
    private val rightAnimation: SpriteAnimation? = null
) : UpdateComponentWithViews {
    override fun update(views: Views, dt: TimeSpan) {
        if (views.input.keys[Key.LEFT]) {
            view.move.x -= 1.0
            if (leftAnimation !== null) view.getSprite().playAnimationLooped(leftAnimation)
        } else if (views.input.keys[Key.RIGHT]) {
            view.move.x += 1.0
            if (rightAnimation !== null) view.getSprite().playAnimationLooped(rightAnimation)
        } else {
            view.move.x = 0.0
        }
    }
}
