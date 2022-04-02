package components.collision

import com.soywiz.klock.milliseconds
import com.soywiz.korge.component.FixedUpdateComponent
import com.soywiz.korge.view.Views
import com.soywiz.korma.geom.Rectangle
import containers.GameEntity
import containers.player.Player
import program.Log

/**
 * Destroy game entities that get off-screen
 */
class DestroyOffscreen(override val view: GameEntity, val views: Views, val padding: Double = 12.0) :
    FixedUpdateComponent(view, 50.milliseconds) {
    override fun update() {
        val bounds = view.getCurrentBounds()
        val cameraBounds =
            Rectangle(
                -padding,
                -padding,
                views.virtualWidth.toDouble() + padding,
                views.virtualHeight.toDouble() + padding
            )

        if (!cameraBounds.contains(bounds)) {
            if (view is Player) {
                view.kill()
            } else {
                view.removeFromParent()
            }
            Log().info { "Entity(${view.name}) offscreen: removed" }
        }
    }
}
