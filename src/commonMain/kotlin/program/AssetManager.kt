package program

import com.soywiz.korau.sound.Sound
import com.soywiz.korge.particle.ParticleEmitter
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.readTiledMap
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
    lateinit var levels: MutableMap<UShort, TiledMap>
    lateinit var defaultFont: Font

    lateinit var music01: Sound
    lateinit var pickupSfx: Sound

    lateinit var playerBitmap: Bitmap
    lateinit var playerDeathAnimBitmap: Bitmap
    lateinit var playerWalkBitmap: Bitmap

    lateinit var slimeBitmap: Bitmap

    lateinit var bulletRect: SolidRect
    lateinit var enemyBulletRect: SolidRect

    lateinit var starbeamParticle: ParticleEmitter

    lateinit var playerIdleAnimation: SpriteAnimation
    lateinit var playerDeathAnimation: SpriteAnimation
    lateinit var playerWalkRightAnimation: SpriteAnimation
    lateinit var playerWalkLeftAnimation: SpriteAnimation

    override suspend fun init(injector: AsyncInjector) {
        val config = injector.get<Config>()
        val dirs = getResourceSubdirs(config)

        bulletRect = SolidRect(4, 4, Colors.WHITE)
        enemyBulletRect = SolidRect(4, 4, Colors.YELLOW)

        defaultFont = resourcesVfs["${dirs["fonts"]}/DOTMATRI.TTF"].readFont()

        levels = mutableMapOf()
        levels[1u] = resourcesVfs["${dirs["maps"]}/acid001.tmx"].readTiledMap()

        playerBitmap = resourcesVfs["${dirs["graphics"]}/player_01.png"].readBitmap()
        playerDeathAnimBitmap = resourcesVfs["${dirs["graphics"]}/player_die.png"].readBitmap()
        playerWalkBitmap = playerBitmap

        slimeBitmap = resourcesVfs["${dirs["graphics"]}/acid_01.png"].readBitmap()

        buildSpriteAnimations()
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
            spriteMap = playerWalkBitmap,
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 3,
            rows = 1
        )
        playerWalkLeftAnimation = SpriteAnimation(
            spriteMap = playerWalkBitmap.clone().flipX(),
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 3,
            rows = 1
        )
        playerDeathAnimation = SpriteAnimation(
            spriteMap = playerDeathAnimBitmap.clone(),
            spriteWidth = 16,
            spriteHeight = 16,
            marginTop = 0,
            marginLeft = 0,
            columns = 5,
            rows = 1
        )
    }

    fun getResourceSubdirs(config: Config): Map<String, String> {
        return mapOf(
            Pair("maps", config.get("resourceDirMaps")),
            Pair("fonts", config.get("resourceDirFonts")),
            Pair("audio", config.get("resourceDirAudio")),
            Pair("graphics", config.get("resourceDirGraphics")),
            Pair("particles", config.get("resourceDirParticles")),
        )
    }
}
