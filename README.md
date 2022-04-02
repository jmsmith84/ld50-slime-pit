# KorGE Boot

## Quickstart project

### main.kt
```kotlin
object AppModule : DefaultAppModule()

suspend fun main() = Korge(Korge.Config(module = AppModule))
```
### app.properties
```properties
fullscreenOnStart=false
logLevel=DEBUG

virtualScreenWidth=640
virtualScreenHeight=480

resourceDirMaps=maps
resourceDirAudio=audio
resourceDirGraphics=sprites
resourceDirParticles=particles
resourceDirFonts=fonts
```
