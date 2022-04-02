package program

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.SoundChannel
import com.soywiz.korge.sound.fadeOut
import com.soywiz.korio.async.launchAsap
import kotlinx.coroutines.Dispatchers

class SoundManager {
    private var musicChannel: SoundChannel? = null

    suspend fun playSound(sfx: Sound): SoundChannel {
        val channel = sfx.play()
        channel.volume = if (Settings.sfxEnabled) Settings.sfxVolume else 0.0
        return channel
    }

    suspend fun setBackgroundMusic(music: Sound) {
        if (!Settings.musicEnabled) return
        musicChannel = music.playForever()
        musicChannel?.volume = Settings.musicVolume
    }

    fun asyncPlaySound(sound: Sound) {
        launchAsap(Dispatchers.Default) {
            playSound(sound)
        }
    }

    fun fadeMusicOut(duration: TimeSpan = 1.5.seconds) {
        launchAsap(Dispatchers.Default) {
            musicChannel?.fadeOut(duration)
        }
    }
}
