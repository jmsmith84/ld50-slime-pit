package components.input

import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.component.UpdateComponentWithViews
import com.soywiz.korge.view.Views
import containers.player.Player

class ShootBulletsInput(
    override val view: Player,
    val canHoldFireDown: Boolean = true
) : UpdateComponentWithViews {
    override fun update(views: Views, dt: TimeSpan) {
        if ((canHoldFireDown && views.input.keys.pressing(Key.X))
            || (!canHoldFireDown && views.input.keys.justPressed(Key.X))
        ) {
            //view.fire()
        }
    }
}
