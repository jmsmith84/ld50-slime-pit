package program

import com.soywiz.korau.sound.Sound
import com.soywiz.korge.particle.ParticleEmitter
import com.soywiz.korge.particle.readParticleEmitter
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapData
import com.soywiz.korge.tiled.readTiledMapData
import com.soywiz.korge.tiled.readTiledSet
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.font.Font
import com.soywiz.korim.font.readFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korinject.InjectorAsyncDependency
import com.soywiz.korio.file.std.resourcesVfs

class AssetManager : InjectorAsyncDependency {
    lateinit var tileSets: MutableList<TiledMap.TiledTileset>
    lateinit var levels: MutableMap<UShort, TiledMapData>
    lateinit var music: MutableMap<UShort, Sound>

    lateinit var defaultFont: Font

    lateinit var playerBitmap: Bitmap
    private lateinit var playerDeathAnimBitmap: Bitmap
    private lateinit var playerWalkRightBitmap: Bitmap
    lateinit var playerWalkDownBitmap: Bitmap
    lateinit var playerWalkUpBitmap: Bitmap
    private lateinit var playerBuildBitmap: Bitmap

    lateinit var slimeBitmap: Bitmap
    private lateinit var wallBuildBitmap: Bitmap
    private lateinit var potionBitmap: Bitmap

    lateinit var slimyEmitter: ParticleEmitter

    lateinit var playerIdleAnimation: SpriteAnimation
    lateinit var playerDeathAnimation: SpriteAnimation
    lateinit var playerWalkRightAnimation: SpriteAnimation
    lateinit var playerWalkLeftAnimation: SpriteAnimation
    lateinit var playerBuildingAnimation: SpriteAnimation

    lateinit var wallBuildingAnimation: SpriteAnimation
    lateinit var potionAnimation: SpriteAnimation

    override suspend fun init(injector: AsyncInjector) {
        val config = injector.get<Config>()
        val dirs = getResourceSubdirs(config)

        defaultFont = resourcesVfs["${dirs["fonts"]}/DOTMATRI.TTF"].readFont()

        buildTiledMapData(dirs)

        playerBitmap = resourcesVfs["${dirs["graphics"]}/player_01.png"].readBitmap()
        playerDeathAnimBitmap = resourcesVfs["${dirs["graphics"]}/player_die.png"].readBitmap()
        playerBuildBitmap = resourcesVfs["${dirs["graphics"]}/player_build.png"].readBitmap()
        playerWalkRightBitmap = playerBitmap

        slimeBitmap = resourcesVfs["${dirs["graphics"]}/acid_01.png"].readBitmap()
        wallBuildBitmap = resourcesVfs["${dirs["graphics"]}/wall_build.png"].readBitmap()
        potionBitmap = resourcesVfs["${dirs["graphics"]}/potion_01.png"].readBitmap()

        slimyEmitter = resourcesVfs["${dirs["particles"]}/slimy/particle.pex"].readParticleEmitter()

        buildSpriteAnimations()
    }

    private suspend fun buildTiledMapData(dirs: Map<String, String>) {
        tileSets = mutableListOf(
            resourcesVfs["${dirs["maps"]}/acid_tiles.tsx"].readTiledSet()
        )

        levels = mutableMapOf()
        levels[1u] = resourcesVfs["${dirs["maps"]}/acid001.tmx"].readTiledMapData()
        levels[2u] = resourcesVfs["${dirs["maps"]}/acid002.tmx"].readTiledMapData()
        //levels[3u] = resourcesVfs["${dirs["maps"]}/acid003.tmx"].readTiledMapData()
        //levels[4u] = resourcesVfs["${dirs["maps"]}/acid004.tmx"].readTiledMapData()

        music = mutableMapOf()
    }

    private fun buildSpriteAnimations() {
        playerIdleAnimation = SpriteAnimation(
            spriteMap = playerBitmap,
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 1,
            rows = 1
        )
        playerWalkRightAnimation = SpriteAnimation(
            spriteMap = playerWalkRightBitmap,
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 3,
            rows = 1
        )
        playerWalkLeftAnimation = SpriteAnimation(
            spriteMap = playerWalkRightBitmap.clone().flipX(),
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 3,
            rows = 1
        )
        playerDeathAnimation = SpriteAnimation(
            spriteMap = playerDeathAnimBitmap,
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 5,
            rows = 1
        )
        playerBuildingAnimation = SpriteAnimation(
            spriteMap = playerBuildBitmap,
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 2,
            rows = 1
        )
        wallBuildingAnimation = SpriteAnimation(
            spriteMap = wallBuildBitmap,
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 3,
            rows = 1
        )
        potionAnimation = SpriteAnimation(
            spriteMap = potionBitmap,
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 2,
            rows = 1
        )
    }

    private fun getResourceSubdirs(config: Config): Map<String, String> {
        return mapOf(
            Pair("maps", config.get("resourceDirMaps")),
            Pair("fonts", config.get("resourceDirFonts")),
            Pair("audio", config.get("resourceDirAudio")),
            Pair("graphics", config.get("resourceDirGraphics")),
            Pair("particles", config.get("resourceDirParticles")),
        )
    }
}
