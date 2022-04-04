package program

object GameState {
    var score: ULong = 0u
    var timeAlive: MutableMap<UShort, Double> = mutableMapOf(
        Pair(1u, 0.0), Pair(2u, 0.0), Pair(3u, 0.0), Pair(4u, 0.0)
    )

    var hiScore: ULong = 0u
    var hiTimeAlive: MutableMap<UShort, Double> = mutableMapOf(
        Pair(1u, 0.0), Pair(2u, 0.0), Pair(3u, 0.0), Pair(4u, 0.0)
    )
}
