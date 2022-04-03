package program

import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.XY
import utility.recreateTileLayers

class LevelManager(private val assets: AssetManager) {
    private var mapView: TiledMapView? = null
    private var currentLevel: UShort = 0u
    private var currentMap: TiledMap? = null
    private val emptyTileId = 0

    var smoothing = false
    //private var mapWallTileLayer: Layer

    fun setNewMap(level: UShort, container: Container, callback: TiledMapView.() -> Unit = {}): TiledMapView {
        currentLevel = level
        currentMap = assets.levels[level] //?.clone()
        return createMapView(container, callback)
    }

    fun getLevel(): UShort {
        return currentLevel
    }

    fun getCurrentMap(): TiledMap {
        if (currentMap === null) throw RuntimeException("Trying to get current level map when there isn't one.")
        return currentMap!!
    }

    fun getCurrentMapView(): TiledMapView {
        if (mapView === null) throw RuntimeException("Trying to get current map view when there isn't one.")
        return mapView!!
    }

    private fun createMapView(
        container: Container,
        callback: TiledMapView.() -> Unit = {}
    ): TiledMapView {
        mapView?.removeFromParent()
        mapView = TiledMapView(getCurrentMap(), false, smoothing)
            .addTo(container, callback)
        return mapView!!
    }

    fun globalXYToTileXY(x: Double, y: Double): XY {
        return Point(
            (x / currentMap!!.tilewidth).toIntFloor(),
            (y / currentMap!!.tileheight).toIntFloor()
        )
    }

    fun getTileIdAt(x: Int, y: Int): Int? {
        if (x < 0 || y < 0 || x >= currentMap?.tileLayers?.get(0)?.width!! || y >= currentMap?.tileLayers?.get(0)?.height!!) {
            return null
        }
        return currentMap?.tileLayers?.get(0)?.get(x, y)
    }

    fun setTileIdAt(x: Int, y: Int, tileId: Int) {
        if (x < 0 || y < 0 || x >= currentMap?.tileLayers?.get(0)?.width!! || y >= currentMap?.tileLayers?.get(0)?.height!!) {
            throw RuntimeException("Trying to set map tile $x,$y is out of bounds")
        }
        currentMap?.tileLayers?.get(0)?.set(x, y, tileId)
        mapView?.recreateTileLayers(false)
    }

    fun isTileEmpty(x: Int, y: Int): Boolean {
        if (currentMap === null) return true

        val tile = getTileIdAt(x, y)
        Log().info { "tile find ${tile}" }
        if (tile === null) return false
        return (tile == emptyTileId)
    }

}
